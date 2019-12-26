package org.orbit.substance.io.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.orbit.substance.api.dfs.DfsServiceMetadata;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.io.DFS;
import org.orbit.substance.io.DFile;
import org.orbit.substance.io.DFileInputStream;
import org.orbit.substance.io.DFileListener;
import org.orbit.substance.io.DFileOutputStream;
import org.orbit.substance.io.DfsEvent;
import org.origin.common.io.IOUtil;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 */
public class DFSImpl extends DFS {

	/**
	 * 
	 * @param dfsServiceUrl
	 * @param accessToken
	 */
	public DFSImpl(String dfsServiceUrl, String accessToken) {
		super(dfsServiceUrl, accessToken);
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

	@Override
	public DfsServiceMetadata getServiceMetadata() throws IOException {
		DfsServiceMetadata metadata = null;
		try {
			metadata = SubstanceClientsUtil.DFS.getDfsMetadata(this.dfsClientResolver, this.accessToken);
		} catch (ClientException e) {
			handle(e);
		}
		if (metadata == null) {
			throw new IOException("Cannot get DFS metadata.");
		}
		return metadata;
	}

	@Override
	public DFile[] listRoot() throws IOException {
		List<DFile> files = new ArrayList<DFile>();
		try {
			FileMetadata[] fileMetadatas = SubstanceClientsUtil.DFS.listRoots(this.dfsClientResolver, this.accessToken);
			if (fileMetadatas != null) {
				for (FileMetadata fileMetadata : fileMetadatas) {
					// String fileId = fileMetadata.getFileId();
					// Path path = fileMetadata.getPath();
					DFile file = new DFileImpl(this, fileMetadata);
					files.add(file);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return files.toArray(new DFile[files.size()]);
	}

	@Override
	public DFile[] listFiles(String parentFileId) throws IOException {
		List<DFile> files = new ArrayList<DFile>();
		try {
			FileMetadata[] fileMetadatas = SubstanceClientsUtil.DFS.listFiles(this.dfsClientResolver, this.accessToken, parentFileId);
			if (fileMetadatas != null) {
				for (FileMetadata fileMetadata : fileMetadatas) {
					// String fileId = fileMetadata.getFileId();
					// Path path = fileMetadata.getPath();
					DFile file = new DFileImpl(this, fileMetadata);
					files.add(file);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return files.toArray(new DFile[files.size()]);
	}

	@Override
	public DFile[] listFiles(Path parentPath) throws IOException {
		if (parentPath == null || parentPath.getPathString() == null) {
			throw new IllegalArgumentException("path is null.");
		}
		if (parentPath.isRoot()) {
			return listRoot();
		}
		List<DFile> files = new ArrayList<DFile>();
		try {
			FileMetadata[] fileMetadatas = SubstanceClientsUtil.DFS.listFiles(this.dfsClientResolver, this.accessToken, parentPath);
			if (fileMetadatas != null) {
				for (FileMetadata fileMetadata : fileMetadatas) {
					// String fileId = fileMetadata.getFileId();
					// Path path = fileMetadata.getPath();
					DFile file = new DFileImpl(this, fileMetadata);
					files.add(file);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return files.toArray(new DFile[files.size()]);
	}

	@Override
	public FileMetadata getFileMetadata(String pathString) throws IOException {
		if (pathString == null) {
			throw new IllegalArgumentException("pathString is null.");
		}
		return getFileMetadata(new Path(pathString));
	}

	@Override
	public FileMetadata getFileMetadata(Path path) throws IOException {
		FileMetadata fileMetadata = null;
		try {
			fileMetadata = SubstanceClientsUtil.DFS.getFile(this.dfsClientResolver, this.accessToken, path);
		} catch (ClientException e) {
			handle(e);
		}
		return fileMetadata;
	}

	@Override
	public DFile getFileById(String fileId) throws IOException {
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}

		DFile file = null;
		try {
			FileMetadata fileMetadata = SubstanceClientsUtil.DFS.getFile(this.dfsClientResolver, this.accessToken, fileId);
			if (fileMetadata == null) {
				throw new IOException("File doesn't exist. fileId='" + fileId + "'.");
			}

			fileId = fileMetadata.getFileId();
			// Path path = fileMetadata.getPath();
			file = new DFileImpl(this, fileMetadata);

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
		return getFile(new Path(pathString));
	}

	@Override
	public DFile getFile(Path path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("path is null.");
		}

		DFile file = null;
		try {
			FileMetadata fileMetadata = SubstanceClientsUtil.DFS.getFile(this.dfsClientResolver, this.accessToken, path);
			if (fileMetadata != null) {
				// file exists
				// String fileId = fileMetadata.getFileId();
				file = new DFileImpl(this, fileMetadata);
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
	public boolean exists(Path path) throws IOException {
		boolean exists = false;
		try {
			if (path != null) {
				FileMetadata fileMetadata = SubstanceClientsUtil.DFS.getFile(this.dfsClientResolver, this.accessToken, path);
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
				FileMetadata fileMetadata = SubstanceClientsUtil.DFS.getFile(this.dfsClientResolver, this.accessToken, fileId);
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
	public FileMetadata mkdir(Path path, boolean createUniqueFolderIfExist) throws IOException {
		if (path == null || path.getPathString() == null) {
			throw new IllegalArgumentException("path is null.");
		}
		if (path.isRoot()) {
			throw new IllegalArgumentException("Cannot create root path.");
		}

		FileMetadata fileMetadata = null;
		try {
			// if (exists(path)) {
			// File already exists.
			// throw new IOException("File already exists. Path is '" + path.getPathString() + "'.");
			// }
			String lastSegment = path.getLastSegment();
			Path parentPath = path.getParent();

			DFile[] files = listFiles(parentPath);
			if (files != null) {
				int number = 1;
				boolean madeUnique = false;
				String baseLastSegment = lastSegment;
				for (DFile currFile : files) {
					if (lastSegment.equals(currFile.getName())) {
						if (!createUniqueFolderIfExist) {
							throw new IOException("File already exists.");
						} else {
							lastSegment = baseLastSegment + " " + number++;
							madeUnique = true;
						}
					}
				}
				if (madeUnique) {
					path = new Path(parentPath, lastSegment);
				}
			}

			fileMetadata = SubstanceClientsUtil.DFS.createDirectory(this.dfsClientResolver, this.accessToken, path);

			// Send notification
			if (fileMetadata != null) {
				Object source = getFile(path.getParent());
				if (path.getParent() == null || path.getParent().isRoot()) {
					source = this;
				} else {
					source = getFile(path.getParent());
				}
				DFile file = new DFileImpl(this, fileMetadata);
				notifyFileEvent(this, DfsEvent.CREATE, source, null, file);
			}

		} catch (ClientException e) {
			handle(e);
		}
		return fileMetadata;
	}

	@Override
	public FileMetadata createNewFile(Path path, long size, boolean notifyEvent) throws IOException {
		if (path == null || path.getPathString() == null) {
			throw new IllegalArgumentException("path is null.");
		}
		if (path.isRoot()) {
			throw new IllegalArgumentException("Cannot create root path.");
		}

		FileMetadata fileMetadata = null;
		try {
			fileMetadata = SubstanceClientsUtil.DFS.createNewFile(this.dfsClientResolver, this.accessToken, path, size);

			// Send notification
			if (fileMetadata != null && notifyEvent) {
				Object source = getFile(path.getParent());
				if (path.getParent() == null || path.getParent().isRoot()) {
					source = this;
				} else {
					source = getFile(path.getParent());
				}
				DFile file = new DFileImpl(this, fileMetadata);
				notifyFileEvent(this, DfsEvent.CREATE, source, null, file);
			}

		} catch (ClientException e) {
			handle(e);
		}
		return fileMetadata;
	}

	@Override
	public FileMetadata create(Path path, InputStream inputStream) throws IOException {
		if (path == null || path.getPathString() == null) {
			throw new IllegalArgumentException("path is null.");
		}
		if (path.isRoot()) {
			throw new IllegalArgumentException("Cannot create root path.");
		}
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream is null.");
		}

		FileMetadata fileMetadata = createNewFile(path, inputStream.available(), false);
		if (fileMetadata == null) {
			throw new IOException("New file cannot be created. Path is '" + path.getPathString() + "'.");
		}

		String fileId = fileMetadata.getFileId();
		setContents(fileId, inputStream);

		// Send notification
		Object source = getFile(path.getParent());
		if (path.getParent() == null || path.getParent().isRoot()) {
			source = this;
		} else {
			source = getFile(path.getParent());
		}
		DFile file = new DFileImpl(this, fileMetadata);
		notifyFileEvent(this, DfsEvent.CREATE, source, null, file);

		return fileMetadata;
	}

	@Override
	public FileMetadata create(Path path, byte[] bytes) throws IOException {
		if (path == null || path.getPathString() == null) {
			throw new IllegalArgumentException("path is null.");
		}
		if (path.isRoot()) {
			throw new IllegalArgumentException("Cannot create root path.");
		}
		if (bytes == null) {
			throw new IllegalArgumentException("bytes is null.");
		}

		FileMetadata fileMetadata = createNewFile(path, bytes.length, false);
		if (fileMetadata == null) {
			throw new IOException("New file cannot be created. Path is '" + path.getPathString() + "'.");
		}

		String fileId = fileMetadata.getFileId();
		setContents(fileId, bytes);

		// Send notification
		Object source = getFile(path.getParent());
		if (path.getParent() == null || path.getParent().isRoot()) {
			source = this;
		} else {
			source = getFile(path.getParent());
		}
		DFile file = new DFileImpl(this, fileMetadata);
		notifyFileEvent(this, DfsEvent.CREATE, source, null, file);

		return fileMetadata;
	}

	@Override
	public long getSize(String fileId) throws IOException {
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}
		DFile file = getFileById(fileId);
		if (file == null) {
			throw new IOException("File is not found. fileId is '" + fileId + "'.");
		}

		long length = 0;
		try {
			FileMetadata fileMetadata = SubstanceClientsUtil.DFS.getFile(this.dfsClientResolver, this.accessToken, fileId);
			if (fileMetadata != null) {
				length = fileMetadata.getSize();
			}
		} catch (ClientException e) {
			handle(e);
		}
		return length;
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
	public OutputStream getOutputStream(String fileId) throws IOException {
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}
		DFile file = getFileById(fileId);
		if (file == null) {
			throw new IOException("File is not found. fileId is '" + fileId + "'.");
		}
		return new DFileOutputStream(file);
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
			// long size = inputStream.available();
			outputStream = getOutputStream(fileId);
			IOUtil.copy(inputStream, outputStream);

			// Send notification
			DFile file = getFileById(fileId);
			notifyFileEvent(this, DfsEvent.CONTENT, file, file, file);

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

			// Send notification
			DFile file = getFileById(fileId);
			notifyFileEvent(this, DfsEvent.CONTENT, file, bytes, bytes);

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
			DFile file = getFileById(fileId);
			String oldName = (file != null) ? file.getName() : null;

			succeed = SubstanceClientsUtil.DFS.renameFile(this.dfsClientResolver, this.accessToken, fileId, newName);

			// Send notification
			if (succeed) {
				notifyFileEvent(this, DfsEvent.RENAME, file, oldName, newName);
			}
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
			DFile file = getFileById(fileId);
			DFile parentFile = (file != null) ? file.getParent() : null;

			succeed = SubstanceClientsUtil.DFS.deleteFile(this.dfsClientResolver, this.dfsVolumeClientResolver, this.accessToken, fileId);

			// Send notification
			if (succeed) {
				Object source = parentFile;
				if (parentFile == null) {
					source = this;
				} else if (parentFile != null && parentFile.getPath() != null && parentFile.getPath().isRoot()) {
					source = this;
				}
				notifyFileEvent(this, DfsEvent.DELETE, source, file, null);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return succeed;
	}

	/** DFileListener */
	protected List<DFileListener> fileListeners = new ArrayList<DFileListener>();

	@Override
	public void addFileListener(DFileListener listener) {
		if (listener != null && !this.fileListeners.contains(listener)) {
			this.fileListeners.add(listener);
		}
	}

	@Override
	public void removeFileListener(DFileListener listener) {
		if (listener != null && this.fileListeners.contains(listener)) {
			this.fileListeners.remove(listener);
		}
	}

	@Override
	public List<DFileListener> getFileListeners() {
		return this.fileListeners;
	}

	/**
	 * 
	 * @param dfs
	 * @param eventType
	 * @param source
	 * @param oldValue
	 * @param newValue
	 */
	@Override
	public void notifyFileEvent(DFS dfs, int eventType, Object source, Object oldValue, Object newValue) {
		DfsEvent event = new DfsEvent(dfs, eventType, source, oldValue, newValue);
		DFileListener[] array = this.fileListeners.toArray(new DFileListener[this.fileListeners.size()]);
		for (DFileListener listener : array) {
			try {
				if (DfsEvent.CREATE == eventType) {
					listener.onFileCreated(event);

				} else if (DfsEvent.CONTENT == eventType) {
					listener.onFileModified(event);

				} else if (DfsEvent.RENAME == eventType) {
					listener.onFileRenamed(event);

				} else if (DfsEvent.MOVE == eventType) {
					listener.onFileMoved(event);

				} else if (DfsEvent.REFRESH == eventType) {
					listener.onFileRefreshed(event);

				} else if (DfsEvent.DELETE == eventType) {
					listener.onFileDeleted(event);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

// FileMetadata fileMetadata = null;
// protected String dfsId;

// @Override
// public synchronized String getDfsId() throws IOException {
// if (this.dfsId == null) {
// DfsServiceMetadata metadata = null;
// try {
// metadata = SubstanceClientsUtil.Dfs.getDfsMetadata(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken);
// } catch (ClientException e) {
// handle(e);
// }
// if (metadata == null) {
// throw new IOException("Cannot get DFS metadata.");
// }
// this.dfsId = metadata.getDfsId();
// }
// return this.dfsId;
// }

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

// @Override
// public FileMetadata createNewFile(Path path, long size) throws IOException {
// if (path == null) {
// throw new IllegalArgumentException("path is null.");
// }
//
// FileMetadata fileMetadata = null;
// try {
// fileMetadata = SubstanceClientsUtil.Dfs.createNewFile(dfsClientResolver, dfsServiceUrl, accessToken, path, size);
//
// boolean exists = SubstanceClientsUtil.Dfs.fileExists(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, path);
// if (exists) {
// throw new IOException("File already exists. Path is '" + path.getPathString() + "'.");
// }
//
// String parentFileId = "-1";
// Path parentPath = path.getParent();
// if (parentPath != null && !parentPath.isRoot()) {
// DFile parentFile = getFile(parentPath);
// if (parentFile.exists()) {
// // File exists
// // - cannot be file. must be directory.
// if (!parentFile.isDirectory()) {
// throw new IOException("Parent file is not directory. Parent file path: '" + parentPath.getPathString() + "'.");
// }
// } else {
// // File doesn't exist
// // - create parent directory
// parentFile.mkdir();
// if (!parentFile.exists() || !parentFile.isDirectory()) {
// throw new IOException("Parent directory cannot be created. Parent file path: '" + parentPath.getPathString() + "'.");
// }
// }
// parentFileId = parentFile.getFileId();
// }
//
// String fileName = path.getLastSegment();
// fileMetadata = SubstanceClientsUtil.Dfs.createNewFile(this.dfsClientResolver, this.dfsServiceUrl, this.accessToken, parentFileId, fileName, size);
//
// } catch (ClientException e) {
// handle(e);
// }
// return fileMetadata;
// }

// public abstract boolean exists(String fileId) throws IOException;
// @Override
// public boolean exists(String fileId) throws IOException {
// boolean exists = false;
// try {
// if (fileId != null) {
// FileMetadata fileMetadata = SubstanceClientsUtil.DFS.getFile(this.dfsClientResolver, this.accessToken, fileId);
// if (fileMetadata != null) {
// exists = true;
// }
// }
// } catch (ClientException e) {
// handle(e);
// }
// return exists;
// }

// public abstract String getFileId(Path path) throws IOException;
// @Override
// public String getFileId(Path path) throws IOException {
// if (path == null) {
// throw new IllegalArgumentException("path is null.");
// }
//
// String fileId = null;
// try {
// FileMetadata fileMetadata = SubstanceClientsUtil.DFS.getFile(this.dfsClientResolver, this.accessToken, path);
// if (fileMetadata != null) {
// fileId = fileMetadata.getFileId();
// }
// } catch (ClientException e) {
// handle(e);
// }
// return fileId;
// }
