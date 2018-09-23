package org.orbit.substance.io.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.io.DFS;
import org.orbit.substance.io.DFile;
import org.orbit.substance.io.DFileInputStream;
import org.orbit.substance.io.DFileOutputStream;
import org.orbit.substance.model.dfs.Path;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.ClientException;

public class DFSImpl extends DFS {

	/**
	 * 
	 * @param dfsServiceUrl
	 * @param indexServiceUrl
	 * @param accessToken
	 */
	public DFSImpl(String dfsServiceUrl, String indexServiceUrl, String accessToken) {
		super(dfsServiceUrl, indexServiceUrl, accessToken);
	}

	@Override
	public DFile getFileById(String fileId) throws IOException {
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}

		DFile file = null;
		try {
			FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.getFile(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, fileId);
			if (fileMetadata == null) {
				throw new IOException("File doesn't exist. fileId='" + fileId + "'.");
			}

			fileId = fileMetadata.getFileId();
			Path path = fileMetadata.getPath();
			file = new DFileImpl(this, fileId, path);

		} catch (ClientException e) {
			handle(e);
		}
		return file;
	}

	@Override
	public DFile getFile(String pathString) throws IOException {
		if (pathString == null) {
			throw new IllegalArgumentException("pathString is null.");
		}
		Path path = new Path(pathString);
		return getFile(path);
	}

	@Override
	public DFile getFile(Path path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("path is null.");
		}

		DFile file = null;
		try {
			FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.getFile(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, path);
			if (fileMetadata != null) {
				// file exists
				String fileId = fileMetadata.getFileId();
				file = new DFileImpl(this, fileId, path);
			} else {
				// file doesn't exists
				file = new DFileImpl(this, path);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return file;
	}

	@Override
	public String getFileId(Path path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("path is null.");
		}

		String fileId = null;
		try {
			FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.getFile(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, path);
			if (fileMetadata != null) {
				fileId = fileMetadata.getFileId();
			}
		} catch (ClientException e) {
			handle(e);
		}
		return fileId;
	}

	@Override
	public boolean exists(String fileId) throws IOException {
		boolean exists = false;
		try {
			if (fileId != null) {
				FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.getFile(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, fileId);
				if (fileMetadata != null) {
					exists = true;
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public boolean exists(Path path) throws IOException {
		boolean exists = false;
		try {
			if (path != null) {
				FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.getFile(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, path);
				if (fileMetadata != null) {
					exists = true;
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public boolean isDirectory(String fileId) throws IOException {
		boolean isDirectory = false;
		try {
			if (fileId != null) {
				FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.getFile(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, fileId);
				if (fileMetadata != null) {
					isDirectory = fileMetadata.isDirectory();
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return isDirectory;
	}

	@Override
	public FileMetadata mkdir(Path path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("path is null.");
		}

		FileMetadata fileMetadata = null;
		try {
			if (exists(path)) {
				// File already exists.
				throw new IOException("File already exists. Path is '" + path.getPathString() + "'.");
			}

			fileMetadata = SubstanceClientsUtil.Dfs.createDirectory(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, path);

		} catch (ClientException e) {
			handle(e);
		}
		return fileMetadata;
	}

	@Override
	public FileMetadata createNewFile(Path path, long size) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("path is null.");
		}

		FileMetadata fileMetadata = null;
		try {
			boolean exists = SubstanceClientsUtil.Dfs.fileExists(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, path);
			if (exists) {
				throw new IOException("File already exists. Path is '" + path.getPathString() + "'.");
			}

			String parentFileId = "-1";
			Path parentPath = path.getParent();
			if (parentPath != null && !parentPath.isRoot()) {
				DFile parentFile = getFile(parentPath);
				if (parentFile.exists()) {
					// File exists
					// - cannot be file. must be directory.
					if (!parentFile.isDirectory()) {
						throw new IOException("Parent file is not directory. Parent file path: '" + parentPath.getPathString() + "'.");
					}
				} else {
					// File doesn't exist
					// - create parent directory
					parentFile.mkdir();
					if (!parentFile.exists() || !parentFile.isDirectory()) {
						throw new IOException("Parent directory cannot be created. Parent file path: '" + parentPath.getPathString() + "'.");
					}
				}
				parentFileId = parentFile.getFileId();
			}

			String fileName = path.getLastSegment();
			fileMetadata = SubstanceClientsUtil.Dfs.createNewFile(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, parentFileId, fileName, size);

		} catch (ClientException e) {
			handle(e);
		}
		return fileMetadata;
	}

	@Override
	public FileMetadata create(Path path, InputStream inputStream) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("path is null.");
		}
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream is null.");
		}

		FileMetadata fileMetadata = createNewFile(path, inputStream.available());
		if (fileMetadata == null) {
			throw new IOException("New file cannot be created. Path is '" + path.getPathString() + "'.");
		}

		String fileId = fileMetadata.getFileId();
		setContents(fileId, inputStream);

		return fileMetadata;
	}

	@Override
	public FileMetadata create(Path path, byte[] bytes) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("path is null.");
		}
		if (bytes == null) {
			throw new IllegalArgumentException("bytes is null.");
		}

		FileMetadata fileMetadata = createNewFile(path, bytes.length);
		if (fileMetadata == null) {
			throw new IOException("New file cannot be created. Path is '" + path.getPathString() + "'.");
		}

		String fileId = fileMetadata.getFileId();
		setContents(fileId, bytes);

		return fileMetadata;
	}

	@Override
	public InputStream getInputStream(String fileId) throws IOException {
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}
		DFile file = getFileById(fileId);
		if (file == null) {
			throw new IOException("File is not found. fileId is '" + fileId + "'.");
		}
		return new DFileInputStream(file);
	}

	@Override
	public OutputStream getOutputStream(String fileId, long size) throws IOException {
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}
		DFile file = getFileById(fileId);
		if (file == null) {
			throw new IOException("File is not found. fileId is '" + fileId + "'.");
		}
		return new DFileOutputStream(file, size);
	}

	@Override
	public void setContents(String fileId, InputStream inputStream) throws IOException {
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream is null.");
		}

		OutputStream outputStream = null;
		try {
			long size = inputStream.available();
			outputStream = getOutputStream(fileId, size);

			IOUtil.copy(inputStream, outputStream);

		} finally {
			IOUtil.closeQuietly(outputStream, true);
		}
	}

	@Override
	public void setContents(String fileId, byte[] bytes) throws IOException {
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}
		if (bytes == null) {
			throw new IllegalArgumentException("bytes is null.");
		}

		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new ByteArrayInputStream(bytes);
			long size = bytes.length;
			outputStream = getOutputStream(fileId, size);

			IOUtil.copy(inputStream, outputStream);

		} finally {
			IOUtil.closeQuietly(outputStream, true);
			IOUtil.closeQuietly(inputStream, true);
		}
	}

	@Override
	public boolean rename(String fileId, String newName) throws IOException {
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}
		if (newName == null || newName.isEmpty()) {
			throw new IllegalArgumentException("newName is empty.");
		}

		boolean succeed = false;
		try {
			succeed = SubstanceClientsUtil.Dfs.renameFile(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, fileId, newName);

		} catch (ClientException e) {
			handle(e);
		}
		return succeed;
	}

	@Override
	public boolean delete(String fileId) throws IOException {
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}
		boolean succeed = false;
		try {
			succeed = SubstanceClientsUtil.Dfs.deleteFile(this.dfsClientResolver, this.dfsVolumeClientResolver, this.dfsServiceUrl, this.accessToken, fileId);

		} catch (ClientException e) {
			handle(e);
		}
		return succeed;
	}

	/**
	 * 
	 * @param e
	 * @throws IOException
	 */
	protected void handle(Exception e) throws IOException {
		if (e instanceof IOException) {
			throw (IOException) e;
		}
		throw new IOException(e);
	}

}

// FileMetadata fileMetadata = null;
// try {
// fileMetadata = SubstanceClientsUtil.Dfs.getFile(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, fileId);
// } catch (ClientException e) {
// handle(e);
// }
// if (fileMetadata == null) {
// throw new IOException("File doesn't exists. fileId is '" + fileId + "'.");
// }
//
// final FileMetadata theFileMetadata = fileMetadata;
// final PipedOutputStream pipeOutput = new PipedOutputStream();
// final PipedInputStream pipeInput = new PipedInputStream(pipeOutput);
// new Thread() {
// @Override
// public void run() {
// try {
// SubstanceClientsUtil.DfsVolume.downloadFile(dfsVolumeClientResolver, fileId, theFileMetadata, pipeOutput);
//
// } catch (Exception e) {
// try {
// pipeOutput.close();
// } catch (IOException e2) {
// e2.printStackTrace();
// }
// }
// }
// }.start();
// return pipeInput;
