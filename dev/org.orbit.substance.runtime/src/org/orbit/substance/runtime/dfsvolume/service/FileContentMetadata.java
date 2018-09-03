package org.orbit.substance.runtime.dfsvolume.service;

public interface FileContentMetadata {

	int getId();

	String getFileId();

	int getPartId();

	String getChecksum();

	void setChecksum(String checksum);

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
