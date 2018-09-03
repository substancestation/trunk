package org.orbit.substance.runtime.dfs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.api.dfsvolume.DataBlockMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.DfsVolumeServiceMetadata;
import org.orbit.substance.model.dfs.FileContentAccessImpl;
import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.model.dfs.FilePartImpl;
import org.orbit.substance.model.dfs.Path;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.util.Comparators;
import org.orbit.substance.runtime.util.Comparators.DfsVolumeClientComparatorByFreeSpace;
import org.orbit.substance.runtime.util.OrbitClientHelper;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.server.ServerException;
import org.origin.common.util.DiskSpaceUnit;
import org.origin.common.util.UUIDUtil;

public class FileSystemImpl implements FileSystem {

	public static FileMetadata[] EMPTY_FILES = new FileMetadata[0];

	protected DfsService dfsService;
	protected String accountId;

	/**
	 * 
	 * @param dfsService
	 * @param accountId
	 */
	public FileSystemImpl(DfsService dfsService, String accountId) {
		this.dfsService = dfsService;
		this.accountId = accountId;
	}

	@Override
	public DfsService getFileSystemService() {
		return this.dfsService;
	}

	@Override
	public String getDfsId() {
		return this.dfsService.getDfsId();
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
		Connection conn = this.dfsService.getConnection();
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
	 * @param e
	 * @throws ServerException
	 */
	protected void handleClientException(ClientException e) throws IOException {
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
		FileMetadata[] memberFileMetadatas = null;
		Connection conn = null;
		try {
			conn = getConnection();
			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);

			List<FileMetadata> fileMetadatas = tableHandler.getList(conn, parentFileId, false);
			if (fileMetadatas != null) {
				memberFileMetadatas = fileMetadatas.toArray(new FileMetadata[fileMetadatas.size()]);
				setPath(memberFileMetadatas);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (memberFileMetadatas == null) {
			memberFileMetadatas = EMPTY_FILES;
		}
		return memberFileMetadatas;
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
	public boolean exists(String parentFileId, String fileName) throws IOException {
		FileMetadata[] memberFileMetadatas = listFiles(parentFileId);
		for (FileMetadata memberFileMetadata : memberFileMetadatas) {
			String currFileName = memberFileMetadata.getName();
			if (currFileName != null && currFileName.equals(fileName)) {
				return true;
			}
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
	public FileMetadata createNewFile(Path path, long size) throws IOException {
		FileMetadata newFileMetadata = null;

		if (path == null || path.isEmpty()) {
			throw new IOException("Path is empty.");
		}
		FileMetadata existingFileMetadata = getFile(path);
		if (existingFileMetadata != null) {
			if (existingFileMetadata.isDirectory()) {
				throw new IOException("Path already exists and is a directory.");
			} else {
				throw new IOException("Path already exists and is a file.");
			}
		}

		Connection conn = null;
		try {
			conn = getConnection();
			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);

			String parentFileId = null;
			Path parentPath = path.getParent();
			if (parentPath == null || parentPath.isRoot()) {
				parentFileId = "-1";

			} else {
				FileMetadata parentFileMetadata = getFile(parentPath);
				if (parentFileMetadata == null) {
					throw new IOException("Parent directory doesn't exist.");
				} else {
					if (!parentFileMetadata.isDirectory()) {
						throw new IOException("Parent file is not a directory.");
					}
				}
				parentFileId = parentFileMetadata.getFileId();
			}

			String fileId = generateFileId();
			String fileName = path.getLastSegment();
			boolean isDirectory = false;
			boolean isHidden = false;
			boolean inTrash = false;
			List<FilePart> fileParts = new ArrayList<FilePart>();
			Map<String, Object> properties = new HashMap<String, Object>();

			newFileMetadata = tableHandler.create(conn, fileId, parentFileId, fileName, size, isDirectory, isHidden, inTrash, fileParts, properties);
			if (newFileMetadata == null) {
				throw new RuntimeException("New file metadata cannot be created.");
			}

			updatePath(newFileMetadata);

			if (size > 0) {
				allocateVolumes(newFileMetadata, size);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return newFileMetadata;
	}

	@Override
	public FileMetadata createNewFile(String parentFileId, String fileName, long size) throws IOException {
		FileMetadata newFileMetadata = null;

		Connection conn = null;
		try {
			conn = getConnection();
			FilesMetadataTableHandler tableHandler = getFilesMetadataTableHandler(conn);

			String fileId = generateFileId();
			boolean isDirectory = false;
			boolean isHidden = false;
			boolean inTrash = false;
			List<FilePart> fileParts = new ArrayList<FilePart>();
			Map<String, Object> properties = new HashMap<String, Object>();

			newFileMetadata = tableHandler.create(conn, fileId, parentFileId, fileName, size, isDirectory, isHidden, inTrash, fileParts, properties);
			if (newFileMetadata == null) {
				throw new RuntimeException("New file metadata cannot be created.");
			}

			updatePath(newFileMetadata);

			if (size > 0) {
				allocateVolumes(newFileMetadata, size);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return newFileMetadata;
	}

	public String generateFileId() {
		String fileId = UUIDUtil.generateBase64EncodedUUID();
		return fileId;
	}

	/**
	 * 
	 * @param fileMetadata
	 * @throws IOException
	 */
	public void updatePath(FileMetadata fileMetadata) throws IOException {
		if (fileMetadata != null) {
			String fileId = fileMetadata.getFileId();
			Path path = getPath(fileId);
			if (path != null) {
				fileMetadata.setPath(path);
			}
		}
	}

	@Override
	public FileMetadata allocateVolumes(String fileId, long size) throws IOException {
		FileMetadata fileMetadata = getFile(fileId);
		if (fileMetadata == null) {
			throw new IOException("File is not found.");
		}
		if (size <= 0 && fileMetadata.getSize() <= 0) {
			throw new IOException("File size is 0.");
		}

		long fileSize = size > 0 ? size : fileMetadata.getSize();
		allocateVolumes(fileMetadata, fileSize);

		return fileMetadata;
	}

	/**
	 * 
	 * @param fileMetadata
	 * @param size
	 * @throws IOException
	 */
	protected void allocateVolumes(FileMetadata fileMetadata, long size) throws IOException {
		if (fileMetadata == null) {
			throw new RuntimeException("File is null.");
		}
		if (size <= 0) {
			throw new RuntimeException("File size is 0.");
		}

		String dfsId = getDfsId();
		String accountId = fileMetadata.getAccountId();
		String indexServiceUrl = (String) this.dfsService.getProperties().get(SubstanceConstants.ORBIT_INDEX_SERVICE_URL);
		// TODO:
		// Need to get access token for the dfs to access the dfs volume.
		String dfsAccessToken = "";

		List<FilePart> fileParts = new ArrayList<FilePart>();

		// dfs volumes are sorted by volume id
		DfsVolumeClient[] dfsVolumeClients = OrbitClientHelper.INSTANCE.getDfsVolumeClient(indexServiceUrl, dfsAccessToken, dfsId);

		// 1. Find an existing datablock with enough space to hold the file content alone.
		for (DfsVolumeClient dfsVolumeClient : dfsVolumeClients) {
			try {
				DataBlockMetadata[] dataBlocksWithEnoughFreeSpace = dfsVolumeClient.getDataBlocks(accountId, size);
				for (DataBlockMetadata dataBlock : dataBlocksWithEnoughFreeSpace) {
					String currDfsVolumeId = dataBlock.getDfsVolumeId();
					String currDataBlockId = dataBlock.getBlockId();

					// a datablock with enough space is found, which can be used to store the file content alone.
					int partId = 0;
					long startIndex = 0;
					long endIndex = -1; // means to the end of the file
					String checksum = null; // checksum cannot be calculated now

					FilePartImpl filePart = new FilePartImpl(partId, startIndex, endIndex, checksum);
					FileContentAccessImpl contentAccess = new FileContentAccessImpl(dfsId, currDfsVolumeId, currDataBlockId);
					filePart.getFileContentAccess().add(contentAccess);

					fileParts.add(filePart);
				}
			} catch (ClientException e) {
				e.printStackTrace();
			}
		}

		if (!fileParts.isEmpty()) {
			fileMetadata.getFileParts().clear();
			fileMetadata.getFileParts().addAll(fileParts);
			return;
		}

		// 2. There is no one existing datablock, which is big enough to hold the file content alone.
		// - Create new datablocks in dfs volumes.
		Map<DfsVolumeClient, DfsVolumeServiceMetadata> serviceMetadataMap = new HashMap<DfsVolumeClient, DfsVolumeServiceMetadata>();
		for (DfsVolumeClient dfsVolumeClient : dfsVolumeClients) {
			try {
				DfsVolumeServiceMetadata serviceMetadata = dfsVolumeClient.getMetadata();
				serviceMetadataMap.put(dfsVolumeClient, serviceMetadata);
			} catch (ClientException e) {
				e.printStackTrace();
			}
		}
		// volumes with larger free space first.
		DfsVolumeClientComparatorByFreeSpace freeSpaceComparator = new DfsVolumeClientComparatorByFreeSpace(serviceMetadataMap);
		Comparators.sort(dfsVolumeClients, freeSpaceComparator);

		long defaultDataBlockCapacity = this.dfsService.getDefaultBlockCapacity();
		if (defaultDataBlockCapacity <= 0) {
			defaultDataBlockCapacity = DiskSpaceUnit.MB.toBytes(100);
		}

		int partId = 0;
		long sizeLeft = size;
		long startIndex = 0;
		long endIndex = -1;
		String checksum = null;

		for (DfsVolumeClient dfsVolumeClient : dfsVolumeClients) {
			DfsVolumeServiceMetadata serviceMetadata = serviceMetadataMap.get(dfsVolumeClient);
			String dfsVolumeId = serviceMetadata.getDfsVolumeId();

			// get block capacity
			long dataBlockCapacity = defaultDataBlockCapacity;
			if (dataBlockCapacity <= 0) {
				dataBlockCapacity = DiskSpaceUnit.MB.toBytes(100);
			}

			DataBlockMetadata dataBlock = null;
			try {
				dataBlock = dfsVolumeClient.createDataBlock(accountId, dataBlockCapacity);
			} catch (ClientException e) {
				handleClientException(e);
			}
			if (dataBlock == null) {
				throw new RuntimeException("Data block cannot be created.");
			}
			String dataBlockId = dataBlock.getBlockId();
			FileContentAccessImpl contentAccess = new FileContentAccessImpl(dfsId, dfsVolumeId, dataBlockId);

			if (sizeLeft <= dataBlockCapacity) {
				// The data block is the last one (or the only one) for holding the content of the file.
				FilePartImpl filePart = new FilePartImpl(partId, startIndex, -1, checksum);
				filePart.getFileContentAccess().add(contentAccess);
				fileParts.add(filePart);
				break;

			} else {
				// The data block is not the last one. More data block is needed.
				endIndex = startIndex + dataBlockCapacity;

				FilePartImpl filePart = new FilePartImpl(partId, startIndex, endIndex, checksum);
				filePart.getFileContentAccess().add(contentAccess);
				fileParts.add(filePart);

				startIndex = endIndex;
			}

			sizeLeft -= dataBlockCapacity;
			partId++;
		}
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

	@Override
	public boolean setFileContent(String fileId, InputStream contentInputStream) throws IOException {
		int available = contentInputStream.available();
		System.out.println("fileId = " + fileId);
		System.out.println("available = " + available);
		return false;
	}

	@Override
	public InputStream getFileContentInputStream(String fileId) throws IOException {
		System.out.println("fileId = " + fileId);
		return null;
	}

	@Override
	public void dispose() {

	}

}

// get dfs volumes of the dfs
// SubstanceClientsUtil.DfsVolume.listDataBlocks(fileContentServiceUrl, accessToken, accountId);

// for (DataBlockMetadata dataBlock : dataBlocks) {
// String currAccountId = dataBlock.getAccountId();
// if (!accountId.equals(currAccountId)) {
// throw new RuntimeException("Data block's account id doesn't match.");
// }
// String currBlockId = dataBlock.getBlockId();
// long currCapacity = dataBlock.getCapacity();
// long currSize = dataBlock.getSize();
// }
