package org.orbit.substance.connector.dfsvolume;

import org.orbit.substance.api.dfsvolume.DfsVolumeServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;

public class DfsVolumeServiceMetadataImpl extends ServiceMetadataImpl implements DfsVolumeServiceMetadata {

	public static String PROP__DFS_ID = "dfs_id";
	public static String PROP__DFS_VOLUME_ID = "dfs_volume_id";
	public static String PROP__VOLUME_CAPACITY = "volume_capacity";
	public static String PROP__VOLUME_SIZE = "volume_size";
	public static String PROP__BLOCK_CAPACITY = "block_capacity";

	public DfsVolumeServiceMetadataImpl() {
	}

	@Override
	public String getDfsId() {
		String dfsId = null;
		if (hasProperty(PROP__DFS_ID)) {
			dfsId = getProperty(PROP__DFS_ID, String.class);
		}
		return dfsId;
	}

	@Override
	public String getDfsVolumeId() {
		String dfsVolumeId = null;
		if (hasProperty(PROP__DFS_VOLUME_ID)) {
			dfsVolumeId = getProperty(PROP__DFS_VOLUME_ID, String.class);
		}
		return dfsVolumeId;
	}

	@Override
	public long getVolumeCapacity() {
		long capacity = -1;
		if (hasProperty(PROP__VOLUME_CAPACITY)) {
			capacity = getProperty(PROP__VOLUME_CAPACITY, Long.class);
		}
		return capacity;
	}

	@Override
	public long getVolumeSize() {
		long size = -1;
		if (hasProperty(PROP__VOLUME_SIZE)) {
			size = getProperty(PROP__VOLUME_SIZE, Long.class);
		}
		return size;
	}

	// @Override
	// public long getDataBlockCapacity() {
	// long capacity = -1;
	// if (hasProperty(PROP__BLOCK_CAPACITY)) {
	// capacity = getProperty(PROP__BLOCK_CAPACITY, Long.class);
	// }
	// return capacity;
	// }

}

// File content service metadata property names
// public static String PROP__FILE_SYSTEM_ID = "dfs_metadata_id"; // file system service id
// public static String PROP__FILE_CONTENT_ID = "dfs_content_id"; // file content servece id
// public static String PROP__TOTAL_CAPACITY = "total_capacity";
// public static String PROP__TOTAL_SIZE = "total_size";
