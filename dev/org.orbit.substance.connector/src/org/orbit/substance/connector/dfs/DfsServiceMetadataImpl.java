package org.orbit.substance.connector.dfs;

import org.orbit.substance.api.dfs.DfsServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;

public class DfsServiceMetadataImpl extends ServiceMetadataImpl implements DfsServiceMetadata {

	public static String PROP__DFS_ID = "dfs_id";
	public static String PROP__BLOCK_CAPACITY = "block_capacity";

	@Override
	public String getDfsId() {
		String dfsId = null;
		if (hasProperty(PROP__DFS_ID)) {
			dfsId = getProperty(PROP__DFS_ID, String.class);
		}
		return dfsId;
	}

	@Override
	public long getDataBlockCapacity() {
		long capacity = -1;
		if (hasProperty(PROP__BLOCK_CAPACITY)) {
			capacity = getProperty(PROP__BLOCK_CAPACITY, Long.class);
		}
		return capacity;
	}

}
