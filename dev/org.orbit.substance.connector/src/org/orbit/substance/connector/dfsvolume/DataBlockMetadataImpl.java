package org.orbit.substance.connector.dfsvolume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.api.dfsvolume.DataBlockMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.model.dfsvolume.PendingFile;

public class DataBlockMetadataImpl implements DataBlockMetadata {

	protected DfsVolumeClient dfsVolumeClient;
	protected String dfsVolumeId;
	protected String blockId;
	protected String accountId;
	protected long capacity;
	protected long size;
	protected List<PendingFile> pendingFiles;
	protected Map<String, Object> properties;
	protected long dateCreated;
	protected long dateModified;

	/**
	 * 
	 * @param dfsVolumeClient
	 */
	public DataBlockMetadataImpl(DfsVolumeClient dfsVolumeClient) {
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
