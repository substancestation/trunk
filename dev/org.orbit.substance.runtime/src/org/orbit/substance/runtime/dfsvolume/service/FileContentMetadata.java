package org.orbit.substance.runtime.dfsvolume.service;

public interface FileContentMetadata {

	int getId();

	String getFileId();

	int getPartId();

	long getSize();

	void setSize(long size);

	long getChecksum();

	void setChecksum(long checksum);

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
