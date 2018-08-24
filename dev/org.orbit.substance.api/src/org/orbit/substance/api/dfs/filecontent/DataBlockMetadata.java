package org.orbit.substance.api.dfs.filecontent;

public interface DataBlockMetadata {

	String getBlockId();

	String getAccountId(); // may need to remove in the future for security concern

	long getCapacity();

	long size();

	// String[] getFileIds(); // may need to remove in the future for security concern

}
