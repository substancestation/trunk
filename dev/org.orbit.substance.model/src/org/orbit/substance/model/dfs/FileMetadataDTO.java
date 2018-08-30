package org.orbit.substance.model.dfs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileMetadataDTO {

	@XmlElement
	protected String accountId;
	@XmlElement
	protected String fileId;
	@XmlElement
	protected String parentFileId;
	@XmlElement
	protected String name;
	@XmlElement
	protected boolean isDirectory;
	@XmlElement
	protected boolean isHidden;
	@XmlElement
	protected long size;
	@XmlElement
	protected String filePartsString;
	@XmlElement
	protected String propertiesString;
	@XmlElement
	protected boolean inTrash;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	public FileMetadataDTO() {
	}

	@XmlElement
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@XmlElement
	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@XmlElement
	public String getParentFileId() {
		return this.parentFileId;
	}

	public void setParentFileId(String parentFileId) {
		this.parentFileId = parentFileId;
	}

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public boolean isDirectory() {
		return this.isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	@XmlElement
	public boolean isHidden() {
		return this.isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	@XmlElement
	public long getSize() {
		return this.size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@XmlElement
	public String getFilePartsString() {
		return this.filePartsString;
	}

	public void setFilePartsString(String filePartsString) {
		this.filePartsString = filePartsString;
	}

	@XmlElement
	public String getPropertiesString() {
		return this.propertiesString;
	}

	public void setPropertiesString(String propertiesString) {
		this.propertiesString = propertiesString;
	}

	@XmlElement
	public boolean isInTrash() {
		return this.inTrash;
	}

	public void setInTrash(boolean inTrash) {
		this.inTrash = inTrash;
	}

	@XmlElement
	public long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@XmlElement
	public long getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}

// @XmlElement
// protected boolean exists;

// @XmlElement
// public boolean exists() {
// return this.exists;
// }
//
// public void setExists(boolean exists) {
// this.exists = exists;
// }
