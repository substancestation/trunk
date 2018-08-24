package org.orbit.substance.model.dfs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataBlockMetadataDTO {

	@XmlElement
	protected String blockId;
	@XmlElement
	protected String accountId;
	@XmlElement
	protected long capacity;
	@XmlElement
	protected long size;

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
