package org.orbit.substance.connector.dfsvolume;

import org.orbit.substance.api.dfsvolume.FileContentMetadata;

public class FileContentMetadataImpl implements FileContentMetadata {

	protected String fileId;
	protected int partId;
	// protected long size;
	// protected int startIndex;
	// protected int endIndex;
	protected String checksum;

	@Override
	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Override
	public int getPartId() {
		return this.partId;
	}

	public void setPartId(int partId) {
		this.partId = partId;
	}

	// @Override
	// public long getSize() {
	// return this.size;
	// }
	//
	// public void setSize(long size) {
	// this.size = size;
	// }
	//
	// @Override
	// public int getStartIndex() {
	// return this.startIndex;
	// }
	//
	// public void setStartIndex(int startIndex) {
	// this.startIndex = startIndex;
	// }
	//
	// @Override
	// public int getEndIndex() {
	// return this.endIndex;
	// }
	//
	// public void setEndIndex(int endIndex) {
	// this.endIndex = endIndex;
	// }

	@Override
	public String getChecksum() {
		return this.checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

}
