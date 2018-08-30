package org.orbit.substance.runtime.dfs.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.dfs.service.FileSystemService;
import org.orbit.substance.runtime.model.dfs.FileMetadata;
import org.orbit.substance.runtime.model.dfs.Path;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.server.ServerException;

public class FileSystemImpl implements FileSystem {

	public static FileMetadata[] EMPTY_FILES = new FileMetadata[0];

	protected FileSystemService fileSystemService;
	protected String accountId;

	/**
	 * 
	 * @param fileSystemService
	 * @param accountId
	 */
	public FileSystemImpl(FileSystemService fileSystemService, String accountId) {
		this.fileSystemService = fileSystemService;
		this.accountId = accountId;
	}

	@Override
	public FileSystemService getFileSystemService() {
		return this.fileSystemService;
	}

	@Override
	public String getDfsId() {
		return this.fileSystemService.getDfsId();
	}

	@Override
	public String getAccountId() {
		return this.accountId;
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		Connection conn = this.fileSystemService.getConnection();
		if (conn == null) {
			throw new SQLException("Cannot get JDBC Connection.");
		}
		return conn;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected FilesMetadataTableHandler getFilesMetadataTableHandler(Connection conn) throws SQLException {
		FilesMetadataTableHandler tableHandler = FilesMetadataTableHandler.getInstance(conn, getDfsId(), getAccountId());
		return tableHandler;
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleSQLException(SQLException e) throws IOException {
		throw new IOException(e.getMessage(), e);
	}

	/**
	 * 
	 * @param fileMetadatas
	 * @throws IOException
	 */
	protected void setPath(FileMetadata[] fileMetadatas) throws IOException {
		if (fileMetadatas != null) {
			for (FileMetadata fileMetadata : fileMetadatas) {
				setPath(fileMetadata);
			}
		}
	}

	/**
	 * 
	 * @param fileMetadata
	 * @throws IOException
	 */
	protected void setPath(FileMetadata fileMetadata) throws IOException {
		if (fileMetadata != null) {
			String fileId = fileMetadata.getFileId();
			Path path = getPath(fileId);
			if (path != null) {
				fileMetadata.setPath(path);
			}
		}
	}

	@Override
	public FileMetadata[] listRoots() throws IOException {
		FileMetadata[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();

			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);
			List<FileMetadata> fileMetadatas = tableHandler.getList(conn, "-1", false);
			if (fileMetadatas != null) {
				result = fileMetadatas.toArray(new FileMetadata[fileMetadatas.size()]);
				setPath(result);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_FILES;
		}
		return result;
	}

	@Override
	public FileMetadata[] listFiles(String parentFileId) throws IOException {
		FileMetadata[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);

			List<FileMetadata> fileMetadatas = tableHandler.getList(conn, parentFileId, false);
			if (fileMetadatas != null) {
				result = fileMetadatas.toArray(new FileMetadata[fileMetadatas.size()]);
				setPath(result);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_FILES;
		}
		return result;
	}

	@Override
	public FileMetadata[] listFiles(Path parentPath) throws IOException {
		FileMetadata[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);

			FileMetadata parentFile = getFile(parentPath);
			if (parentFile != null) {
				String parentFileId = parentFile.getFileId();

				List<FileMetadata> fileMetadatas = tableHandler.getList(conn, parentFileId, false);
				if (fileMetadatas != null) {
					result = fileMetadatas.toArray(new FileMetadata[fileMetadatas.size()]);
					setPath(result);
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_FILES;
		}
		return result;
	}

	@Override
	public FileMetadata[] listTrashFiles() throws IOException {
		FileMetadata[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);

			List<FileMetadata> fileMetadatas = tableHandler.getList(conn, true);
			if (fileMetadatas != null) {
				result = fileMetadatas.toArray(new FileMetadata[fileMetadatas.size()]);
				setPath(result);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_FILES;
		}
		return result;
	}

	@Override
	public FileMetadata getFile(String fileId) throws IOException {
		FileMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);

			result = tableHandler.getByFileId(conn, fileId);
			setPath(result);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public FileMetadata getFile(Path path) throws IOException {
		FileMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);

			String[] segments = path.getSegments();
			if (segments == null || segments.length == 0) {
				// path is empty, which cannot be used to resolve a file. File cannot be resolved.
				return null;
			}

			String currParentFileId = "-1";

			for (int i = 0; i < segments.length; i++) {
				boolean isLastSegment = (i == (segments.length - 1)) ? true : false;

				String currName = segments[i];
				FileMetadata currFile = tableHandler.getByName(conn, currParentFileId, currName);

				if (!isLastSegment) {
					// Current file is an intermediate segment, which should be a directory, if the directory exists.
					if (currFile == null) {
						// Current intermediate file cannot be found. File cannot be resolved. File doesn't exist.
						return null;
					}
					if (!currFile.isDirectory()) {
						// Current intermediate file is not a directory. File cannot be resolved. File doesn't exist.
						return null;
					}

					// Look for the next segment until reaching the last segment, which should be the file name, if the file exists.
					currParentFileId = currFile.getFileId();

				} else {
					// Current file is the last segment, which should be the file (either file or directory), if exists.
					if (currFile != null) {
						result = currFile;
					}
				}
			}

			if (result != null) {
				result.setPath(path);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public Path getPath(String fileId) throws IOException {
		Path result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);

			FileMetadata fileMetadata = tableHandler.getByFileId(conn, fileId);
			if (fileMetadata == null) {
				// File metadata doesn't exists. Path cannot be constructed.
				return null;
			}

			Path currPath = new Path(fileMetadata.getName());

			String currParentFileId = fileMetadata.getParentFileId();
			while (currParentFileId != null && !currParentFileId.isEmpty() && !currParentFileId.equals("-1")) {
				FileMetadata parentFileMetadata = tableHandler.getByFileId(conn, fileId);
				if (parentFileMetadata == null) {
					// Parent file doesn't exists. Path cannot be constructed.
					return null;
				}
				if (!parentFileMetadata.isDirectory()) {
					// Parent file is not a directory. Path cannot be constructed, strictly speaking.
					// - Let's just say this should never happen and ignore it for now.
					// - Validation framework need to be provided to identify such "dangling" files (can only be caused by defects, if any).
					// return null;
				}

				String parentFileName = parentFileMetadata.getName();
				currPath = new Path(currPath, parentFileName);

				currParentFileId = parentFileMetadata.getParentFileId();
			}

			result = new Path(Path.ROOT, currPath);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public boolean exists(String fileId) throws IOException {
		FileMetadata fileMetadata = this.getFile(fileId);
		if (fileMetadata != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean exists(Path path) throws IOException {
		FileMetadata fileMetadata = getFile(path);
		if (fileMetadata != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDirectory(Path path) throws IOException {
		boolean isDirectory = false;
		FileMetadata fileMetadata = getFile(path);
		if (fileMetadata != null) {
			isDirectory = fileMetadata.isDirectory();
		}
		return isDirectory;
	}

	@Override
	public FileMetadata createNewFile(Path path) throws IOException {
		FileMetadata result = null;
		FileMetadata fileMetadata = getFile(path);
		if (fileMetadata != null) {
			if (fileMetadata.isDirectory()) {
				throw new IOException("Path already exists and is a directory.");
			} else {
				throw new IOException("Path already exists and is a file.");
			}
		}
		if (result != null) {
			result.setPath(path);
		}
		return null;
	}

	@Override
	public FileMetadata mkdirs(Path path) throws IOException {
		FileMetadata result = null;
		FileMetadata fileMetadata = getFile(path);
		if (fileMetadata != null) {
			if (fileMetadata.isDirectory()) {
				result = fileMetadata;
			} else {
				throw new IOException("Path already exists and is a file.");
			}
		}
		if (result != null) {
			result.setPath(path);
		}
		return result;
	}

	@Override
	public FileMetadata moveToTrash(String fileId) throws IOException {
		FileMetadata result = null;
		if (result != null) {
			setPath(result);
		}
		return result;
	}

	@Override
	public FileMetadata moveToTrash(Path path) throws IOException {
		FileMetadata result = null;
		if (result != null) {
			result.setPath(path);
		}
		return result;
	}

	@Override
	public FileMetadata putBackFromTrash(String fileId) throws IOException {
		FileMetadata result = null;
		if (result != null) {
			setPath(result);
		}
		return result;
	}

	@Override
	public FileMetadata putBackFromTrash(Path path) throws IOException {
		FileMetadata result = null;
		if (result != null) {
			result.setPath(path);
		}
		return result;
	}

	@Override
	public boolean delete(String fileId) throws IOException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();
			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);

			FileMetadata fileMetadata = tableHandler.getByFileId(conn, fileId);
			if (fileMetadata == null) {
				throw new IOException("File doesn't exist.");
			}

			doDelete(conn, fileMetadata);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	@Override
	public boolean delete(Path path) throws IOException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();

			FileMetadata fileMetadata = getFile(path);
			if (fileMetadata == null) {
				throw new IOException("File doesn't exist.");
			}

			doDelete(conn, fileMetadata);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	/**
	 * 
	 * @param conn
	 * @param fileMetadata
	 * @return
	 * @throws SQLException
	 */
	protected boolean doDelete(Connection conn, FileMetadata fileMetadata) throws SQLException {
		if (fileMetadata == null) {
			return false;
		}
		String fileId = fileMetadata.getFileId();

		// 1. delete file contents
		boolean isFileContentDeleted = false;

		// 2. delete file metadata
		FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);
		boolean isFileMetadataDeleted = tableHandler.deleteByFileId(conn, fileId);

		if (isFileContentDeleted && isFileMetadataDeleted) {
			return true;
		}
		return false;
	}

}
