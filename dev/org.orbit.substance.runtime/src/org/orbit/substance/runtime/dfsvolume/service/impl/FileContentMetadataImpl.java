package org.orbit.substance.runtime.dfsvolume.service.impl;

import org.orbit.substance.runtime.dfsvolume.service.FileContentMetadata;

public class FileContentMetadataImpl implements FileContentMetadata {

	protected int id;
	protected String fileId;
	protected int partId;
	protected String checksum;
	protected long dateCreated;
	protected long dateModified;

	public FileContentMetadataImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param fileId
	 * @param partId
	 * @param checksum
	 * @param dateCreated
	 * @param dateModified
	 */
	public FileContentMetadataImpl(int id, String fileId, int partId, String checksum, long dateCreated, long dateModified) {
		this.id = id;
		this.fileId = fileId;
		this.partId = partId;
		this.checksum = checksum;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	@Override
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	@Override
	public String getChecksum() {
		return this.checksum;
	}

	@Override
	public void setChecksum(String checksum) {
		this.checksum = checksum;
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

}

// protected int startIndex;
// protected int endIndex;

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
