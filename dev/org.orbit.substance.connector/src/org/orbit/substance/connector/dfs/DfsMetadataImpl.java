package org.orbit.substance.connector.dfs;

import org.orbit.substance.api.dfs.DfsMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;

public class DfsMetadataImpl extends ServiceMetadataImpl implements DfsMetadata {

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
			Object value = getProperty(PROP__BLOCK_CAPACITY);
			if (value != null) {
				capacity = Long.valueOf(value.toString());
			}
		}
		return capacity;
	}

}
