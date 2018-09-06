package org.orbit.substance.api.dfsvolume;

import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfsvolume.PendingFile;

public interface DataBlockMetadata {

	DfsVolumeClient getDfsVolumeClient();

	String getDfsVolumeId();

	String getBlockId();

	String getAccountId();

	long getCapacity();

	long getSize();

	List<PendingFile> getPendingFiles();

	Map<String, Object> getProperties();

	long getDateCreated();

	long getDateModified();

}
