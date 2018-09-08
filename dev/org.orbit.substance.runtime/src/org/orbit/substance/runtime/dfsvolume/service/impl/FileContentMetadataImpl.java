package org.orbit.substance.runtime.dfsvolume.service.impl;

import org.orbit.substance.runtime.dfsvolume.service.FileContentMetadata;

public class FileContentMetadataImpl implements FileContentMetadata {

	protected String dfsId;
	protected String dfsVolumeId;
	protected String blockId;
	protected int id;
	protected String fileId;
	protected int partId;
	protected long size;
	protected long checksum;
	protected long dateCreated;
	protected long dateModified;

	public FileContentMetadataImpl() {
	}

	/**
	 * 
	 * @param dfsId
	 * @param dfsVolumeId
	 * @param blockId
	 * @param id
	 * @param fileId
	 * @param partId
	 * @param size
	 * @param checksum
	 * @param dateCreated
	 * @param dateModified
	 */
	public FileContentMetadataImpl(String dfsId, String dfsVolumeId, String blockId, int id, String fileId, int partId, long size, long checksum, long dateCreated, long dateModified) {
		this.dfsId = dfsId;
		this.dfsVolumeId = dfsVolumeId;
		this.blockId = blockId;
		this.id = id;
		this.fileId = fileId;
		this.partId = partId;
		this.size = size;
		this.checksum = checksum;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	@Override
	public String getDfsId() {
		return this.dfsId;
	}

	@Override
	public void setDfsId(String dfsId) {
		this.dfsId = dfsId;
	}

	@Override
	public String getDfsVolumeId() {
		return this.dfsVolumeId;
	}

	@Override
	public void setDfsVolumeId(String dfsVolumeId) {
		this.dfsVolumeId = dfsVolumeId;
	}

	@Override
	public String getBlockId() {
		return this.blockId;
	}

	@Override
	public void setBlockId(String blockId) {
		this.blockId = blockId;
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
	public String getFileId() {
		return this.fileId;
	}

	@Override
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Override
	public int getPartId() {
		return this.partId;
	}

	@Override
	public void setPartId(int partId) {
		this.partId = partId;
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
	public long getChecksum() {
		return this.checksum;
	}

	@Override
	public void setChecksum(long checksum) {
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
