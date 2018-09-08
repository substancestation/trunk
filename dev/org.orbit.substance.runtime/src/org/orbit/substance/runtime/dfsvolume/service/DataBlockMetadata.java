package org.orbit.substance.runtime.dfsvolume.service;

import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfsvolume.PendingFile;

public interface DataBlockMetadata {

	String getDfsId();

	void setDfsId(String dfsId);

	String getDfsVolumeId();

	void setDfsVolumeId(String dfsVolumeId);

	int getId();

	void setId(int id);

	String getBlockId();

	void setBlockId(String blockId);

	String getAccountId();

	void setAccountId(String accountId);

	long getCapacity();

	void setCapacity(long capacity);

	long getSize();

	void setSize(long size);

	List<PendingFile> getPendingFiles();

	void setPendingFiles(List<PendingFile> pendingFiles);

	Map<String, Object> getProperties();

	void setProperties(Map<String, Object> properties);

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
