package org.orbit.substance.connector.dfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.model.dfs.Path;
import org.origin.common.rest.client.ClientException;

public class FileMetadataImpl implements FileMetadata {

	protected DfsClient fsClient;

	protected String dfsId;
	protected String accountId;
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
	public FileMetadataImpl(DfsClient fsClient) {
		this.fsClient = fsClient;
	}

	@Override
	public DfsClient getDfsClient() {
		return this.fsClient;
	}

	public void setDfsClient(DfsClient fsClient) {
		this.fsClient = fsClient;
	}

	@Override
	public String getDfsId() {
		return this.dfsId;
	}

	public void setDfsId(String dfsId) {
		this.dfsId = dfsId;
	}

	@Override
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Override
	public String getParentFileId() {
		return this.parentFileId;
	}

	public void setParentFileId(String parentFileId) {
		this.parentFileId = parentFileId;
	}

	@Override
	public Path getPath() {
		return this.path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	@Override
	public String getName() {
		return this.path != null ? this.path.getLastSegment() : null;
	}

	@Override
	public long getSize() {
		return this.size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public boolean isDirectory() {
		return this.isDirectory;
	}

	public void setIsDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	@Override
	public boolean isHidden() {
		return this.isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	@Override
	public boolean inTrash() {
		return this.inTrash;
	}

	public void setInTrash(boolean inTrash) {
		this.inTrash = inTrash;
	}

	@Override
	public synchronized List<FilePart> getFileParts() {
		if (this.fileParts == null) {
			this.fileParts = new ArrayList<FilePart>();
		}
		return this.fileParts;
	}

	public synchronized void setFileParts(List<FilePart> fileParts) {
		this.fileParts = fileParts;
	}

	@Override
	public synchronized Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new HashMap<String, Object>();
		}
		return this.properties;
	}

	public synchronized void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public long getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
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
	protected synchronized void update(FileMetadata sourceFile) {
		if (sourceFile == null) {
			return;
		}

		this.fileId = sourceFile.getFileId();
		this.parentFileId = sourceFile.getParentFileId();
		this.path = sourceFile.getPath();
		// this.name = sourceFile.getName();
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

}

// protected String name;

// ------------------------------------------------------
// Actions
// ------------------------------------------------------
// boolean createNewFile() throws IOException;
// boolean mkdirs() throws IOException;
// boolean moveToTrash() throws IOException;
// boolean putBackFromTrash() throws IOException;
// boolean delete() throws IOException;

// @Override
// public boolean createNewFile() throws IOException {
// boolean succeed = false;
// try {
// FileMetadata newFile = this.fsClient.createNewFile(this.path);
// if (newFile != null) {
// succeed = true;
// update(newFile);
// }
// } catch (ClientException e) {
// handleException(e);
// }
// return succeed;
// }
//
// @Override
// public boolean mkdirs() throws IOException {
// boolean succeed = false;
// try {
// FileMetadata newFile = this.fsClient.mkdirs(this.path);
// if (newFile != null) {
// succeed = true;
// update(newFile);
// }
// } catch (ClientException e) {
// handleException(e);
// }
// return succeed;
// }
//
// @Override
// public boolean moveToTrash() throws IOException {
// boolean succeed = false;
// try {
// if (this.fileId == null && this.path != null) {
// FileMetadata file = this.fsClient.getFile(this.path);
// if (file != null) {
// update(file);
// }
// }
// if (this.fileId == null) {
// throw new IOException("File does not exist.");
// }
//
// if (inTrash()) {
// throw new IOException("File is already in trash.");
// }
//
// FileMetadata newFile = this.fsClient.moveToTrash(this.fileId);
// if (newFile != null) {
// succeed = true;
// update(newFile);
// }
// } catch (ClientException e) {
// handleException(e);
// }
// return succeed;
// }
//
// @Override
// public boolean putBackFromTrash() throws IOException {
// boolean succeed = false;
// try {
// if (this.fileId == null && this.path != null) {
// FileMetadata file = this.fsClient.getFile(this.path);
// if (file != null) {
// update(file);
// }
// }
// if (this.fileId == null) {
// throw new IOException("File does not exist.");
// }
//
// if (!inTrash()) {
// throw new IOException("File is not in trash.");
// }
//
// FileMetadata newFile = this.fsClient.moveOutOfTrash(this.fileId);
// if (newFile != null) {
// succeed = true;
// update(newFile);
// }
// } catch (ClientException e) {
// handleException(e);
// }
// return succeed;
// }
//
// @Override
// public boolean delete() throws IOException {
// boolean succeed = false;
// try {
// if (this.fileId == null && this.path != null) {
// FileMetadata file = this.fsClient.getFile(this.path);
// if (file != null) {
// update(file);
// }
// }
// if (this.fileId == null) {
// throw new IOException("File does not exist.");
// }
//
// succeed = this.fsClient.delete(this.fileId);
//
// } catch (ClientException e) {
// handleException(e);
// }
// return succeed;
// }
