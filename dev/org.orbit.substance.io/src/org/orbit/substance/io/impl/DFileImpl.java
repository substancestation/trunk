package org.orbit.substance.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.io.DFS;
import org.orbit.substance.io.DFile;
import org.orbit.substance.io.DFileInputStream;
import org.orbit.substance.io.DFileOutputStream;
import org.orbit.substance.model.dfs.Path;

public class DFileImpl implements DFile {

	protected DFS dfs;
	protected String fileId;
	protected Path path;

	/**
	 * 
	 * @param dfs
	 * @param path
	 * @throws IOException
	 */
	public DFileImpl(DFS dfs, Path path) throws IOException {
		if (path == null) {
			throw new IOException("path is null.");
		}
		this.dfs = dfs;
		this.path = path;
	}

	/**
	 * 
	 * @param dfs
	 * @param fileId
	 * @param path
	 * @throws IOException
	 */
	public DFileImpl(DFS dfs, String fileId, Path path) throws IOException {
		if (path == null) {
			throw new IOException("path is null.");
		}
		this.dfs = dfs;
		this.fileId = fileId;
		this.path = path;
	}

	@Override
	public DFS getDFS() {
		return this.dfs;
	}

	@Override
	public DFile getParent() throws IOException {
		Path parentPath = this.path.getParent();
		if (parentPath == null) {
			return null;
		}
		return this.dfs.getFile(parentPath);
	}

	@Override
	public String getFileId() {
		return this.fileId;
	}

	@Override
	public Path getPath() {
		return this.path;
	}

	@Override
	public String getName() {
		return this.path.getLastSegment();
	}

	@Override
	public synchronized boolean exists() throws IOException {
		boolean exists = false;
		if (this.fileId != null) {
			exists = this.dfs.exists(this.fileId);
		}
		return exists;
	}

	@Override
	public boolean isDirectory() throws IOException {
		boolean isDirectory = false;
		if (this.fileId != null) {
			isDirectory = this.dfs.isDirectory(this.fileId);
		}
		return isDirectory;
	}

	@Override
	public synchronized boolean mkdir() throws IOException {
		if (exists()) {
			return false;
		}

		String newFileId = null;
		FileMetadata fileMetadata = this.dfs.mkdir(this.path);
		if (fileMetadata != null) {
			newFileId = fileMetadata.getFileId();
			if (!fileMetadata.isDirectory()) {
				throw new IOException("New file is not a directory.");
			}
		}

		boolean exists = false;
		if (newFileId != null) {
			exists = this.dfs.exists(newFileId);
		}
		if (!exists) {
			throw new IOException("New directory is not created.");
		}

		this.fileId = newFileId;
		return true;
	}

	@Override
	public synchronized boolean createNewFile() throws IOException {
		if (exists()) {
			throw new IOException("File already exists.");
		}

		String newFileId = null;
		FileMetadata fileMetadata = this.dfs.createNewFile(this.path, 0);
		if (fileMetadata != null) {
			newFileId = fileMetadata.getFileId();
			if (fileMetadata.isDirectory()) {
				throw new IOException("New file is a directory.");
			}
		}

		boolean exists = false;
		if (newFileId != null) {
			exists = this.dfs.exists(newFileId);
		}
		if (!exists) {
			throw new IOException("New file is not created.");
		}

		this.fileId = newFileId;
		return true;
	}

	@Override
	public boolean create(InputStream inputStream) throws IOException {
		if (exists()) {
			throw new IOException("File already exists.");
		}

		String newFileId = null;
		FileMetadata fileMetadata = this.dfs.create(this.path, inputStream);
		if (fileMetadata != null) {
			newFileId = fileMetadata.getFileId();
			if (fileMetadata.isDirectory()) {
				throw new IOException("New file is a directory.");
			}
		}

		boolean exists = false;
		if (newFileId != null) {
			exists = this.dfs.exists(newFileId);
		}
		if (!exists) {
			throw new IOException("New file is not created.");
		}

		this.fileId = newFileId;
		return true;
	}

	@Override
	public boolean create(byte[] bytes) throws IOException {
		if (exists()) {
			throw new IOException("File already exists.");
		}

		String newFileId = null;
		FileMetadata fileMetadata = this.dfs.create(this.path, bytes);
		if (fileMetadata != null) {
			newFileId = fileMetadata.getFileId();
			if (fileMetadata.isDirectory()) {
				throw new IOException("New file is a directory.");
			}
		}

		boolean exists = false;
		if (newFileId != null) {
			exists = this.dfs.exists(newFileId);
		}
		if (!exists) {
			throw new IOException("New file is not created.");
		}

		this.fileId = newFileId;
		return true;
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
		this.dfs.setContents(this.fileId, inputStream);
	}

	@Override
	public synchronized void setContents(byte[] bytes) throws IOException {
		if (!exists()) {
			throw new IOException("File doesn't exists.");
		}
		this.dfs.setContents(this.fileId, bytes);
	}

	@Override
	public synchronized boolean rename(String newName) throws IOException {
		if (!exists()) {
			throw new IOException("File doesn't exists.");
		}
		boolean succeed = this.dfs.rename(this.fileId, newName);
		if (succeed) {
			Path parentPath = this.path.getParent();
			this.path = new Path(parentPath, newName);
		}
		return succeed;
	}

	@Override
	public synchronized boolean delete() throws IOException {
		if (exists()) {
			return false;
		}
		boolean succeed = this.dfs.delete(this.fileId);
		if (succeed) {
			this.fileId = null;
		}
		return succeed;
	}

}
