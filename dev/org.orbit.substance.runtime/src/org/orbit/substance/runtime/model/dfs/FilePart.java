package org.orbit.substance.runtime.model.dfs;

import java.util.List;

public interface FilePart {

	FileMetadata getFileMetadata();

	void setFileMetadata(FileMetadata fileMetadata);

	int getPartId();

	void setPartId(int partId);

	long getStartIndex();

	void setStartIndex(long startIndex);

	long getEndIndex();

	void setEndIndex(long endIndex);

	String getChecksum();

	void setChecksum(String checksum);

	List<VolumeAccess> getVolumeAccess();

}
