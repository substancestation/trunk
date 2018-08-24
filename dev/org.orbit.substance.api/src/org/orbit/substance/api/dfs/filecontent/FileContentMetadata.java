package org.orbit.substance.api.dfs.filecontent;

public interface FileContentMetadata {

	String getFileId();

	int getPartId();

	long getSize();

	int getStartIndex();

	int getEndIndex();

	String getChecksum();

}
