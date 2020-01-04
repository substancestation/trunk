package org.orbit.substance.io.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.io.DFS;
import org.orbit.substance.io.DFile;
import org.orbit.substance.io.DFileInputStream;
import org.orbit.substance.io.DFileOutputStream;
import org.orbit.substance.io.DfsEvent;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.resource.Path;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 */
public class DFileImpl implements DFile {

	protected DFS dfs;
	protected FileMetadata metadata;
	protected Path path;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param dfs
	 * @param path
	 * @throws IOException
	 */
	public DFileImpl(DFS dfs, Path path) throws IOException {
		this.dfs = dfs;
		this.path = path;
	}

	/**
	 * 
	 * @param dfs
	 * @param fileMetadata
	 * @throws IOException
	 */
	public DFileImpl(DFS dfs, FileMetadata fileMetadata) throws IOException {
		this.dfs = dfs;
		this.metadata = fileMetadata;
		this.path = fileMetadata.getPath();
	}

	@Override
	public void resolveMetadata(boolean reset) throws IOException {
		if (reset) {
			this.metadata = null;
		}
		if (this.metadata == null && this.path != null) {
			this.metadata = this.dfs.getFileMetadata(this.path);
			if (this.metadata != null) {
				this.path = this.metadata.getPath();
			}
		}
	}

	/*
	 * Examples:
	 * 
	 * dfs://dfs1/path/to/file.txt
	 * 
	 * dfs://dfs1/path/to/dir/
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	@Override
	public URI toURI() throws IOException {
		try {
			String uriScheme = "dfs";

			String accessToken = this.dfs.getAccessToken();
			String uriHost = this.dfs.getServiceMetadata().getDfsId();
			String uriPath = this.path.getPathString();
			if (isDirectory()) {
				if (!uriPath.endsWith("/")) {
					uriPath = uriPath + "/";
				}
			}
			if (!uriPath.startsWith("/")) {
				uriPath = "/" + uriPath;
			}
			String uriFragment = null;

			return new URI(uriScheme, accessToken, uriHost, -1, uriPath, null, uriFragment);

		} catch (URISyntaxException x) {
			throw new Error(x); // Can't happen
		}
	}

	/**
	 * 
	 * @param path
	 * @param isDirectory
	 * @return
	 */
	protected static String slashify(String path, boolean isDirectory) {
		String p = path;
		if (File.separatorChar != '/') {
			p = p.replace(File.separatorChar, '/');
		}
		if (!p.startsWith("/")) {
			p = "/" + p;
		}
		if (!p.endsWith("/") && isDirectory) {
			p = p + "/";
		}
		return p;
	}

	@Override
	public DFS getDFS() {
		return this.dfs;
	}

	@Override
	public Path getPath() {
		return this.path;
	}

	@Override
	public DFile getParent() throws IOException {
		Path parentPath = this.path.getParent();
		if (parentPath == null || parentPath.isEmpty() || parentPath.isRoot()) {
			return null;
		}
		return this.dfs.getFile(parentPath);
	}

	@Override
	public String getFileId() throws IOException {
		resolveMetadata(false);

		if (this.metadata != null) {
			return this.metadata.getFileId();
		}
		return null;
	}

	@Override
	public String getName() throws IOException {
		// resolveMetadata(false);

		return this.path.getLastSegment();
	}

	@Override
	public String getFileExtension() throws IOException {
		if (isDirectory()) {
			return null;
		}
		String fileExtension = null;
		String lastSegment = this.path.getLastSegment();
		if (lastSegment != null) {
			int index = lastSegment.lastIndexOf(".");
			if (index >= 0 && index < lastSegment.length() - 1) {
				fileExtension = lastSegment.substring(index + 1);
			}
		}
		return fileExtension;
	}

	@Override
	public synchronized boolean exists() throws IOException {
		resolveMetadata(false);
		if (this.metadata != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDirectory() throws IOException {
		resolveMetadata(false);

		if (this.metadata != null) {
			return this.metadata.isDirectory();
		}
		return false;
	}

	@Override
	public synchronized boolean mkdir(boolean createUniqueFolderIfExist) throws IOException {
		resolveMetadata(false);
		if (exists()) {
			return false;
		}

		this.metadata = this.dfs.mkdir(this.path, createUniqueFolderIfExist);
		if (this.metadata != null) {
			if (!metadata.isDirectory()) {
				throw new IOException("New file is not a directory.");
			}
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean createNewFile() throws IOException {
		if (exists()) {
			throw new IOException("File already exists.");
		}

		this.metadata = this.dfs.createNewFile(this.path, 0, true);
		if (this.metadata != null) {
			if (this.metadata.isDirectory()) {
				throw new IOException("New file is a directory.");
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean create(InputStream inputStream) throws IOException {
		if (exists()) {
			throw new IOException("File already exists.");
		}

		this.metadata = this.dfs.create(this.path, inputStream);
		if (metadata != null) {
			if (this.metadata.isDirectory()) {
				throw new IOException("New file is a directory.");
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean create(byte[] bytes) throws IOException {
		if (exists()) {
			throw new IOException("File already exists.");
		}

		this.metadata = this.dfs.create(this.path, bytes);
		if (this.metadata != null) {
			if (this.metadata.isDirectory()) {
				throw new IOException("New file is a directory.");
			}
			return true;
		}
		return false;
	}

	@Override
	public synchronized long getSize() throws IOException {
		if (!exists()) {
			throw new IOException("File doesn't exists.");
		}
		return this.metadata.getSize();
	}

	@Override
	public synchronized InputStream getInputStream() throws IOException {
		if (!exists()) {
			throw new IOException("File doesn't exists.");
		}
		if (isDirectory()) {
			throw new IOException("File is directory.");
		}
		return new DFileInputStream(this);
	}

	@Override
	public synchronized OutputStream getOutputStream() throws IOException {
		if (!exists()) {
			throw new IOException("File doesn't exists.");
		}
		if (isDirectory()) {
			throw new IOException("File is directory.");
		}
		return new DFileOutputStream(this);
	}

	@Override
	public synchronized OutputStream getOutputStream(long size) throws IOException {
		if (!exists()) {
			throw new IOException("File doesn't exists.");
		}
		if (isDirectory()) {
			throw new IOException("File is directory.");
		}
		return new DFileOutputStream(this, size);
	}

	@Override
	public synchronized void setContents(InputStream inputStream) throws IOException {
		if (!exists()) {
			throw new IOException("File doesn't exists.");
		}
		this.dfs.setContents(this.metadata.getFileId(), inputStream);
	}

	@Override
	public synchronized void setContents(byte[] bytes) throws IOException {
		if (!exists()) {
			throw new IOException("File doesn't exists.");
		}
		this.dfs.setContents(this.metadata.getFileId(), bytes);
	}

	@Override
	public synchronized boolean rename(String newName) throws IOException {
		if (!exists()) {
			throw new IOException("File doesn't exists.");
		}
		if (newName == null) {
			throw new IOException("File name cannot be null.");
		}

		String oldName = getName();
		if (newName.equals(oldName)) {
			// no change is made. file is already with new name.
			return true;
		}

		boolean succeed = this.dfs.rename(this.metadata.getFileId(), newName, false);
		if (succeed) {
			Path parentPath = this.path.getParent();
			this.path = new Path(parentPath, newName);
			this.metadata.setPath(this.path);

			this.dfs.notifyFileEvent(this.dfs, DfsEvent.RENAME, this, oldName, newName);
		}
		return succeed;
	}

	@Override
	public synchronized boolean delete() throws IOException {
		if (exists()) {
			return false;
		}
		boolean succeed = this.dfs.delete(this.metadata.getFileId());
		return succeed;
	}

	@Override
	public DFile[] listFiles() throws IOException {
		resolveMetadata(false);

		DFile[] memberFiles = null;
		if (this.metadata != null) {
			memberFiles = this.dfs.listFiles(this.metadata.getFileId());
		}
		if (memberFiles == null) {
			memberFiles = new DFile[0];
		}
		return memberFiles;
	}

	@Override
	public long getDateCreated() throws IOException {
		resolveMetadata(false);

		if (this.metadata != null) {
			return this.metadata.getDateCreated();
		}
		return 0;
	}

	@Override
	public long getDateModified() throws IOException {
		resolveMetadata(false);

		if (this.metadata != null) {
			return this.metadata.getDateModified();
		}
		return 0;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	/** implement IAdaptable */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T t = this.adaptorSupport.getAdapter(adapter);
		if (t == null) {
			if (Path.class.equals(adapter)) {
				t = (T) this.path;
			}
		}
		return t;
	}

	@Override
	public String toString() {
		return "DFileImpl [metadata=" + this.metadata + ", path=" + this.path + "]";
	}

}

// public static void main(String[] args) {
// try {
// URI uri1 = new URI("dfs", "dfs1", "/path/to/file1.txt", null);
// URI uri2 = new URI("dfs", "dfs1", "/path/to/file3.txt", null);
// URI uri3 = new URI("dfs", "dfs1", "/path/to/dir/", null);
// URI uri4 = new URI("dfs", "dfs1", "/path/to/dir", null);
// System.out.println(uri1);
// System.out.println(uri2);
// System.out.println(uri3);
// System.out.println(uri4);
//
// } catch (URISyntaxException e) {
// e.printStackTrace();
// }
// }
