package org.orbit.substance.model.dfsvolume;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileContentMetadataDTO {

	@XmlElement
	protected String fileId;
	@XmlElement
	protected int partId;
	// @XmlElement
	// protected long size;
	// @XmlElement
	// protected int startIndex;
	// @XmlElement
	// protected int endIndex;
	@XmlElement
	protected String checksum;

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

	@XmlElement
	public String getChecksum() {
		return this.checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

}
