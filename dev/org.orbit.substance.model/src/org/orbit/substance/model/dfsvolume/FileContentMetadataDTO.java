package org.orbit.substance.model.dfsvolume;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileContentMetadataDTO {

	@XmlElement
	protected String dfsId;
	@XmlElement
	protected String dfsVolumeId;
	@XmlElement
	protected String blockId;
	@XmlElement
	protected String fileId;
	@XmlElement
	protected int partId;
	@XmlElement
	protected long size;
	@XmlElement
	protected long checksum;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	@XmlElement
	public String getDfsId() {
		return this.dfsId;
	}

	public void setDfsId(String dfsId) {
		this.dfsId = dfsId;
	}

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
	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@XmlElement
	public int getPartId() {
		return this.partId;
	}

	public void setPartId(int partId) {
		this.partId = partId;
	}

	@XmlElement
	public long getSize() {
		return this.size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@XmlElement
	public long getChecksum() {
		return this.checksum;
	}

	public void setChecksum(long checksum) {
		this.checksum = checksum;
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
// protected long size;
// @XmlElement
// protected int startIndex;
// @XmlElement
// protected int endIndex;

// @XmlElement
// public long getSize() {
// return this.size;
// }
//
// public void setSize(long size) {
// this.size = size;
// }
//
// @XmlElement
// public int getStartIndex() {
// return this.startIndex;
// }
//
// public void setStartIndex(int startIndex) {
// this.startIndex = startIndex;
// }
//
// @XmlElement
// public int getEndIndex() {
// return this.endIndex;
// }
//
// public void setEndIndex(int endIndex) {
// this.endIndex = endIndex;
// }
