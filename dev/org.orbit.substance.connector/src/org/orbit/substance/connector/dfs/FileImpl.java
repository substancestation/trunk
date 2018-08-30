package org.orbit.substance.connector.dfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.api.dfs.File;
import org.orbit.substance.api.dfs.FilePart;
import org.orbit.substance.api.dfs.FileSystemClient;
import org.orbit.substance.api.dfs.Path;
import org.origin.common.rest.client.ClientException;

public class FileImpl implements File {

	protected FileSystemClient fsClient;

	protected String fileId;
	protected String parentFileId;
	protected Path path;
	protected long size;
	protected boolean isDirectory;
	protected boolean isHidden;
	protected boolean inTrash;
	protected List<FilePart> fileParts = new ArrayList<FilePart>();
	protected Map<String, Object> properties = new HashMap<String, Object>();
	protected long dateCreated;
	protected long dateModified;

	/**
	 * 
	 * @param fsClient
	 */
	public FileImpl(FileSystemClient fsClient) {
		this.fsClient = fsClient;
	}

	@Override
	public FileSystemClient getFileSystemClient() {
		return this.fsClient;
	}

	@Override
	public String getFileId() {
		return this.fileId;
	}

	@Override
	public String getParentFileId() {
		return this.parentFileId;
	}

	@Override
	public Path getPath() {
		return this.path;
	}

	@Override
	public String getName() {
		return this.path != null ? this.path.getLastSegment() : null;
	}

	@Override
	public long getSize() {
		return this.size;
	}

	@Override
	public boolean isDirectory() {
		return this.isDirectory;
	}

	@Override
	public boolean isHidden() {
		return this.isHidden;
	}

	@Override
	public boolean inTrash() {
		return this.inTrash;
	}

	@Override
	public List<FilePart> getFileParts() {
		return this.fileParts;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public long getDateCreated() {
		return this.dateCreated;
	}

	@Override
	public long getDateModified() {
		return this.dateModified;
	}

	// ------------------------------------------------------
	// Actions
	// ------------------------------------------------------
	/**
	 * 
	 * @param e
	 * @throws IOException
	 */
	protected void handleException(ClientException e) throws IOException {
		throw new IOException(e.getMessage(), e);
	}

	/**
	 * 
	 * @param sourceFile
	 */
	protected synchronized void update(File sourceFile) {
		if (sourceFile == null) {
			return;
		}

		this.fileId = sourceFile.getFileId();
		this.parentFileId = sourceFile.getParentFileId();
		this.path = sourceFile.getPath();
		this.size = sourceFile.getSize();
		this.isDirectory = sourceFile.isDirectory();
		this.isHidden = sourceFile.isHidden();
		this.inTrash = sourceFile.inTrash();
		this.fileParts = sourceFile.getFileParts();
		this.properties = sourceFile.getProperties();
		this.dateCreated = sourceFile.getDateCreated();
		this.dateModified = sourceFile.getDateModified();

		if (this.fileParts == null) {
			this.fileParts = new ArrayList<FilePart>();
		}
		if (this.properties == null) {
			this.properties = new HashMap<String, Object>();
		}
	}

	@Override
	public boolean createNewFile() throws IOException {
		boolean succeed = false;
		try {
			File newFile = this.fsClient.createNewFile(this.path);
			if (newFile != null) {
				succeed = true;
				update(newFile);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return succeed;
	}

	@Override
	public boolean mkdirs() throws IOException {
		boolean succeed = false;
		try {
			File newFile = this.fsClient.mkdirs(this.path);
			if (newFile != null) {
				succeed = true;
				update(newFile);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return succeed;
	}

	@Override
	public boolean moveToTrash() throws IOException {
		boolean succeed = false;
		try {
			if (this.fileId == null && this.path != null) {
				File file = this.fsClient.getFile(this.path);
				if (file != null) {
					update(file);
				}
			}
			if (this.fileId == null) {
				throw new IOException("File does not exist.");
			}

			if (inTrash()) {
				throw new IOException("File is already in trash.");
			}

			File newFile = this.fsClient.moveToTrash(this.fileId);
			if (newFile != null) {
				succeed = true;
				update(newFile);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return succeed;
	}

	@Override
	public boolean putBackFromTrash() throws IOException {
		boolean succeed = false;
		try {
			if (this.fileId == null && this.path != null) {
				File file = this.fsClient.getFile(this.path);
				if (file != null) {
					update(file);
				}
			}
			if (this.fileId == null) {
				throw new IOException("File does not exist.");
			}

			if (!inTrash()) {
				throw new IOException("File is not in trash.");
			}

			File newFile = this.fsClient.putBackFromTrash(this.fileId);
			if (newFile != null) {
				succeed = true;
				update(newFile);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return succeed;
	}

	@Override
	public boolean delete() throws IOException {
		boolean succeed = false;
		try {
			if (this.fileId == null && this.path != null) {
				File file = this.fsClient.getFile(this.path);
				if (file != null) {
					update(file);
				}
			}
			if (this.fileId == null) {
				throw new IOException("File does not exist.");
			}

			succeed = this.fsClient.delete(this.fileId);

		} catch (ClientException e) {
			handleException(e);
		}
		return succeed;
	}

}
