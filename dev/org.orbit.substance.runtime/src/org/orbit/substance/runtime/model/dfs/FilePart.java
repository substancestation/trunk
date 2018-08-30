package org.orbit.substance.runtime.model.dfs;

import java.util.List;

public interface FilePart {

	FileMetadata getFileMetadata();

	void setFileMetadata(FileMetadata fileMetadata);

	int getPartId();

	void setPartId(int partId);

	int getStartIndex();

	void setStartIndex(int startIndex);

	int getEndIndex();

	void setEndIndex(int endIndex);

	String getChecksum();

	void setChecksum(String checksum);

	List<FileContentAccess> getFileContentAccess();

}
