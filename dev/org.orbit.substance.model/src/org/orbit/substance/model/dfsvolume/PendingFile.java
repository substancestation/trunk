package org.orbit.substance.model.dfsvolume;

public interface PendingFile {

	String getFileId();

	void setFileId(String fileId);

	long getSize();

	void setSize(long size);

	long getDateCreated();

}
