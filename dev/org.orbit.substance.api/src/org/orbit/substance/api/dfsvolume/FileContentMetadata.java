package org.orbit.substance.api.dfsvolume;

public interface FileContentMetadata {

	DfsVolumeClient getDfsVolumeClient();

	String getDfsId();

	String getDfsVolumeId();

	String getBlockId();

	String getFileId();

	int getPartId();

	long getSize();

	long getChecksum();

	long getDateCreated();

	long getDateModified();

}
