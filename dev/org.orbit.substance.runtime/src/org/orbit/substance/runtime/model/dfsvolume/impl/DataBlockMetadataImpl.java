package org.orbit.substance.runtime.model.dfsvolume.impl;

import org.orbit.substance.runtime.model.dfsvolume.DataBlockMetadata;

public class DataBlockMetadataImpl implements DataBlockMetadata {

	protected int id;
	protected String blockId;
	protected String accountId;
	protected long capacity;
	protected long size;
	protected long dateCreated;
	protected long dateModified;

	public DataBlockMetadataImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param blockId
	 * @param accountId
	 * @param capacity
	 * @param size
	 * @param dateCreated
	 * @param dateModified
	 */
	public DataBlockMetadataImpl(int id, String blockId, String accountId, long capacity, long size, long dateCreated, long dateModified) {
		this.id = id;
		this.blockId = blockId;
		this.accountId = accountId;
		this.capacity = capacity;
		this.size = size;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
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
