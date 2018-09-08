package org.orbit.substance.runtime.dfsvolume.service;

public interface FileContentMetadata {

	String getDfsId();

	void setDfsId(String dfsId);

	String getDfsVolumeId();

	void setDfsVolumeId(String dfsVolumeId);

	String getBlockId();

	void setBlockId(String blockId);

	int getId();

	void setId(int id);

	String getFileId();

	void setFileId(String fileId);

	int getPartId();

	void setPartId(int partId);

	long getSize();

	void setSize(long size);

	long getChecksum();

	void setChecksum(long checksum);

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dataModified);

}
