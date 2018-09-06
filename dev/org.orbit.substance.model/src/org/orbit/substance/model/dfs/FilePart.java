package org.orbit.substance.model.dfs;

import java.util.List;

public interface FilePart {

	int getPartId();

	void setPartId(int partId);

	long getStartIndex();

	void setStartIndex(long startIndex);

	long getEndIndex();

	void setEndIndex(long endIndex);

	long getChecksum();

	void setChecksum(long checksum);

	List<FileContentAccess> getContentAccess();

}
