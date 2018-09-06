package org.orbit.substance.runtime.dfsvolume.service;

public interface FileContentMetadata {

	int getId();

	String getFileId();

	int getPartId();

	long getSize();

	long getChecksum();

	long getDateCreated();

	long getDateModified();

}

// void setSize(long size);
// void setChecksum(long checksum);
// void setDateCreated(long dateCreated);
// void setDateModified(long dateModified);
