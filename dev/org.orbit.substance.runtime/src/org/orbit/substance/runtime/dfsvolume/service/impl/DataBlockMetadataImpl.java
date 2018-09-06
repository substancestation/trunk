package org.orbit.substance.runtime.dfsvolume.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfsvolume.PendingFile;
import org.orbit.substance.runtime.dfsvolume.service.DataBlockMetadata;

public class DataBlockMetadataImpl implements DataBlockMetadata {

	protected String dfsVolumeId;
	protected int id;
	protected String blockId;
	protected String accountId;
	protected long capacity;
	protected long size;
	protected List<PendingFile> pendingFiles;
	protected Map<String, Object> properties;
	protected long dateCreated;
	protected long dateModified;

	public DataBlockMetadataImpl() {
	}

	/**
	 * 
	 * @param dfsVolumeId
	 * @param id
	 * @param blockId
	 * @param accountId
	 * @param capacity
	 * @param size
	 * @param pendingFiles
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public DataBlockMetadataImpl(String dfsVolumeId, int id, String blockId, String accountId, long capacity, long size, List<PendingFile> pendingFiles, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.dfsVolumeId = dfsVolumeId;
		this.id = id;
		this.blockId = blockId;
		this.accountId = accountId;
		this.capacity = capacity;
		this.size = size;
		this.pendingFiles = pendingFiles;
		this.properties = properties;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	@Override
	public String getDfsVolumeId() {
		return this.dfsVolumeId;
	}

	public void setDfsVolumeId(String dfsVolumeId) {
		this.dfsVolumeId = dfsVolumeId;
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
	public synchronized List<PendingFile> getPendingFiles() {
		if (this.pendingFiles == null) {
			this.pendingFiles = new ArrayList<PendingFile>();
		}
		return this.pendingFiles;
	}

	public synchronized void setPendingFiles(List<PendingFile> pendingFiles) {
		this.pendingFiles = pendingFiles;
	}

	@Override
	public synchronized Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new HashMap<String, Object>();
		}
		return this.properties;
	}

	public synchronized void setProperties(Map<String, Object> properties) {
		this.properties = properties;
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
