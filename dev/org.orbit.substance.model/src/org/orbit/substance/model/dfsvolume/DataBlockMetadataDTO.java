package org.orbit.substance.model.dfsvolume;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataBlockMetadataDTO {

	@XmlElement
	protected String dfsVolumeId;
	@XmlElement
	protected String blockId;
	@XmlElement
	protected String accountId;
	@XmlElement
	protected long capacity;
	@XmlElement
	protected long size;
	@XmlElement
	protected String pendingFilesString;
	@XmlElement
	protected String propertiesString;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	@XmlElement
	public String getDfsVolumeId() {
		return this.dfsVolumeId;
	}

	public void setDfsVolumeId(String dfsVolumeId) {
		this.dfsVolumeId = dfsVolumeId;
	}

	@XmlElement
	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	@XmlElement
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@XmlElement
	public long getCapacity() {
		return this.capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	@XmlElement
	public long getSize() {
		return this.size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@XmlElement
	public String getPendingFilesString() {
		return this.pendingFilesString;
	}

	public void setPendingFilesString(String pendingFilesString) {
		this.pendingFilesString = pendingFilesString;
	}

	@XmlElement
	public String getPropertiesString() {
		return this.propertiesString;
	}

	public void setPropertiesString(String propertiesString) {
		this.propertiesString = propertiesString;
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
// protected String[] fileIds;

// @XmlElement
// public String[] getFileIds() {
// return this.fileIds;
// }
//
// public void setFileIds(String[] fileIds) {
// this.fileIds = fileIds;
// }
