package org.orbit.substance.runtime.dfs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.model.dfs.Path;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.util.ModelConverter;

public class FileMetadataImpl implements FileMetadata {

	protected String dfsId;
	protected int id;
	protected String accountId;
	protected String fileId;
	protected String parentFileId;
	protected Path path;
	protected String name;
	protected long size;
	protected boolean isDirectory;
	protected boolean isHidden;
	protected boolean inTrash;
	protected List<FilePart> fileParts = new ArrayList<FilePart>();
	protected Map<String, Object> properties;
	protected long dateCreated;
	protected long dateModified;

	public FileMetadataImpl() {
	}

	/**
	 * 
	 * @param dfsId
	 * @param id
	 * @param accountId
	 * @param fileId
	 * @param parentFileId
	 * @param path
	 * @param name
	 * @param size
	 * @param isDirectory
	 * @param isHidden
	 * @param inTrash
	 * @param fileParts
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public FileMetadataImpl(String dfsId, int id, String accountId, String fileId, String parentFileId, Path path, String name, long size, boolean isDirectory, boolean isHidden, boolean inTrash, List<FilePart> fileParts, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.dfsId = dfsId;
		this.id = id;
		this.accountId = accountId;
		this.fileId = fileId;
		this.parentFileId = parentFileId;
		this.path = path;
		this.name = name;
		this.size = size;
		this.isDirectory = isDirectory;
		this.isHidden = isHidden;
		this.inTrash = inTrash;
		this.fileParts = fileParts;
		this.properties = properties;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	@Override
	public String getDfsId() {
		return this.dfsId;
	}

	public void setDfsId(String dfsId) {
		this.dfsId = dfsId;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getAccountId() {
		return this.accountId;
	}

	@Override
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public String getFileId() {
		return this.fileId;
	}

	@Override
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Override
	public String getParentFileId() {
		return this.parentFileId;
	}

	@Override
	public void setParentFileId(String parentFileId) {
		this.parentFileId = parentFileId;
	}

	@Override
	public Path getPath() {
		return this.path;
	}

	@Override
	public void setPath(Path path) {
		this.path = path;

		// Update local name at the same time.
		if (this.path != null) {
			String lastSegment = this.path.getLastSegment();

			if ((this.name != null && !this.name.equals(lastSegment)) || (this.name == null && lastSegment != null)) {
				this.name = lastSegment;
			}
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		if (this.path != null && this.path.isRoot()) {
			throw new UnsupportedOperationException("Cannot change root name.");
		}

		this.name = name;

		// Update path at the same time, if path has been set.
		if (this.path != null) {
			String lastSegment = this.path.getLastSegment();
			if ((this.name != null && !this.name.equals(lastSegment)) || (this.name == null && lastSegment != null)) {
				Path parentPath = this.path.getParent();
				if (parentPath == null) {
					this.path = new Path(name);
				} else {
					this.path = new Path(parentPath, name);
				}
			}
		}
	}

	@Override
	public long getSize() {
		return this.size;
	}

	@Override
	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public boolean isDirectory() {
		return this.isDirectory;
	}

	@Override
	public void setIsDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	@Override
	public boolean isHidden() {
		return this.isHidden;
	}

	@Override
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	@Override
	public boolean isInTrash() {
		return this.inTrash;
	}

	@Override
	public void setInTrash(boolean inTrash) {
		this.inTrash = inTrash;
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
	public long getDateCreated() {
		return this.dateCreated;
	}

	@Override
	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public long getDateModified() {
		return this.dateModified;
	}

	@Override
	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

	/**
	 * 
	 * @param attrName
	 * @return
	 */
	@Override
	public Object getAttribute(String attrName) {
		if (ATTR__FILEID.equalsIgnoreCase(attrName)) {
			return getFileId();

		} else if (ATTR__PARENT_FILEID.equalsIgnoreCase(attrName)) {
			return getParentFileId();

		} else if (ATTR__NAME.equalsIgnoreCase(attrName)) {
			return getName();

		} else if (ATTR__EXISTS.equalsIgnoreCase(attrName)) {
			// return exists();

		} else if (ATTR__IS_DIRECTORY.equalsIgnoreCase(attrName)) {
			return isDirectory();

		} else if (ATTR__SIZE.equalsIgnoreCase(attrName)) {
			return getSize();

		} else if (ATTR__DATE_CREATED.equalsIgnoreCase(attrName)) {
			return getDateCreated();

		} else if (ATTR__DATE_MODIFIED.equalsIgnoreCase(attrName)) {
			return getDateModified();
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		String filePartsString = ModelConverter.Dfs.toFilePartsString(this.fileParts);
		String propertiesString = ModelConverter.Dfs.toPropertiesString(this.properties);

		sb.append("FileMetadataImpl (");
		sb.append("dfsId='").append(this.dfsId).append("'");
		sb.append("id='").append(this.id).append("'");
		sb.append(", accountId='").append(this.accountId).append("'");
		sb.append(", fileId='").append(this.fileId).append("'");
		sb.append(", parentFileId='").append(this.parentFileId).append("'");
		if (this.path == null) {
			sb.append(", path=null");
		} else {
			sb.append(", path='").append(this.path.getPathString()).append("'");
		}
		sb.append(", name='").append(this.name).append("'");
		sb.append(", size=").append(this.size);
		sb.append(", isDirectory=").append(this.isDirectory);
		sb.append(", isHidden=").append(this.isHidden);
		sb.append(", inTrash=").append(this.inTrash);
		sb.append(", fileParts=").append(filePartsString);
		sb.append(", properties=").append(propertiesString);
		sb.append(", dateCreated=").append(this.dateCreated);
		sb.append(", dateModified=").append(this.dateModified);
		sb.append(")");

		return sb.toString();
	}

}
