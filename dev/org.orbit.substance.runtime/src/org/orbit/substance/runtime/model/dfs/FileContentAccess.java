package org.orbit.substance.runtime.model.dfs;

public interface FileContentAccess {

	FilePart getFilePart();

	void setFilePart(FilePart filePart);

	int getId();

	void setId(int id);

	String getDfsId();

	void setDfsId(String dfsId);

	String getDfsVolumeId();

	void setDfsVolumeId(String dfsVolumeId);

	String getDataBlockId();

	void setDataBlockId(String dataBlockId);

}
