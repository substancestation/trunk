package org.orbit.substance.runtime.model.dfs;

public class DataBlockMetadataImpl implements DataBlockMetadata {

	protected int id;
	protected String blockId;
	protected String accountId;
	protected long capacity;
	protected long size;

	public DataBlockMetadataImpl() {
	}

	public DataBlockMetadataImpl(int id, String blockId, String accountId, long capacity, long size) {
		this.id = id;
		this.blockId = blockId;
		this.accountId = accountId;
		this.capacity = capacity;
		this.size = size;
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
