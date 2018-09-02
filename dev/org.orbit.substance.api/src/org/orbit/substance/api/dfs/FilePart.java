package org.orbit.substance.api.dfs;

import java.util.List;

public interface FilePart {

	FileMetadata getFile();

	void setFile(FileMetadata file);

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
