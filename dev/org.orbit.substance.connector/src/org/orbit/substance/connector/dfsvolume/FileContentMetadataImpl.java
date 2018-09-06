package org.orbit.substance.connector.dfsvolume;

import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.FileContentMetadata;

public class FileContentMetadataImpl implements FileContentMetadata {

	protected DfsVolumeClient dfsVolumeClient;
	protected String fileId;
	protected int partId;
	protected long size;
	protected long checksum;
	protected long dateCreated;
	protected long dateModified;

	/**
	 * 
	 * @param dfsVolumeClient
	 */
	public FileContentMetadataImpl(DfsVolumeClient dfsVolumeClient) {
		this.dfsVolumeClient = dfsVolumeClient;
	}

	@Override
	public DfsVolumeClient getDfsVolumeClient() {
		return this.dfsVolumeClient;
	}

	public void setDfsVolumeClient(DfsVolumeClient dfsVolumeClient) {
		this.dfsVolumeClient = dfsVolumeClient;
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
	public long getSize() {
		return this.size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public long getChecksum() {
		return this.checksum;
	}

	public void setChecksum(long checksum) {
		this.checksum = checksum;
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

}

// protected long size;
// protected int startIndex;
// protected int endIndex;

// long getSize();
// int getStartIndex();
// int getEndIndex();

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
