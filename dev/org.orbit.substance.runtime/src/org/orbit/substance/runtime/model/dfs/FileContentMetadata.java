package org.orbit.substance.runtime.model.dfs;

public interface FileContentMetadata {

	int getId();

	String getFileId();

	int getPartId();

	long getSize();

	int getStartIndex();

	int getEndIndex();

	String getChecksum();

}
