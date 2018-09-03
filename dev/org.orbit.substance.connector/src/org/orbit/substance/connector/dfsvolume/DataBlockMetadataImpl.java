package org.orbit.substance.connector.dfsvolume;

import org.orbit.substance.api.dfsvolume.DataBlockMetadata;

public class DataBlockMetadataImpl implements DataBlockMetadata {

	protected String dfsVolumeId;
	protected String blockId;
	protected String accountId;
	protected long capacity;
	protected long size;

	public DataBlockMetadataImpl() {
	}

	@Override
	public String getDfsVolumeId() {
		return this.dfsVolumeId;
	}

	public void setDfsVolumeId(String dfsVolumeId) {
		this.dfsVolumeId = dfsVolumeId;
	}

	@Override
	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	@Override
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public long getCapacity() {
		return this.capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	@Override
	public long getSize() {
		return this.size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
