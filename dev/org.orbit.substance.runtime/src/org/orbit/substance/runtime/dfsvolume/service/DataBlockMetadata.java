package org.orbit.substance.runtime.dfsvolume.service;

import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfsvolume.PendingFile;

public interface DataBlockMetadata {

	String getDfsVolumeId();

	int getId();

	String getBlockId();

	String getAccountId();

	long getCapacity();

	long getSize();

	List<PendingFile> getPendingFiles();

	Map<String, Object> getProperties();

	long getDateCreated();

	long getDateModified();

}
