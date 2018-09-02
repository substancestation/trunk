package org.orbit.substance.connector.dfsvolume;

import org.orbit.substance.api.dfsvolume.DataBlockMetadata;

public class DataBlockMetadataImpl implements DataBlockMetadata {

	protected String blockId;
	protected String accountId;
	protected long capacity;
	protected long size;

	public DataBlockMetadataImpl() {
	}

	@Override
	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getAccountId() {
		return accountId;
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

// String getAccountId();
// String[] getFileIds();

// protected String accountId;
// protected String[] fileIds;

// @Override
// public String getAccountId() {
// return this.accountId;
// }
//
// public void setAccountId(String accountId) {
// this.accountId = accountId;
// }

// @Override
// public String[] getFileIds() {
// return this.fileIds;
// }
//
// public void setFileIds(String[] fileIds) {
// this.fileIds = fileIds;
// }

// protected String[] fileIds;
//
// public synchronized String[] getFileIds() {
// if (this.fileIds == null) {
// this.fileIds = new String[0];
// }
// return this.fileIds;
// }
//
// public synchronized void setFileIds(String[] fileIds) {
// this.fileIds = fileIds;
// }
