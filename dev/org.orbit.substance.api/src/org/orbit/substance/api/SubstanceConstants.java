package org.orbit.substance.api;

public interface SubstanceConstants {

	// ----------------------------------------------------------------------------------------
	// orbit service config properties
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_DFS_URL = "orbit.dfs.url";

	public static String ORBIT_DFS_VOLUME_URL = "orbit.dfs_volume.url";

	// ----------------------------------------------------------------------------------------
	// Dfs volume service
	// ----------------------------------------------------------------------------------------
	// Index item properties
	public static String IDX_PROP__DFS_VOLUME__DFS_ID = "dfs_volume.dfs_id";
	public static String IDX_PROP__DFS_VOLUME__ID = "dfs_volume.id";
	public static String IDX_PROP__DFS_VOLUME__NAME = "dfs_volume.name";
	public static String IDX_PROP__DFS_VOLUME__HOST_URL = "dfs_volume.host.url";
	public static String IDX_PROP__DFS_VOLUME__CONTEXT_ROOT = "dfs_volume.context_root";
	public static String IDX_PROP__DFS_VOLUME__VOLUME_CAPACITY = "dfs_volume.volume_capacity";
	// public static String IDX_PROP__DFS_VOLUME__BLOCK_CAPACITY = "dfs_volume.block_capacity";

	// Index item values
	public static String IDX__DFS_VOLUME__INDEXER_ID = "substance.dfs_volume.indexer";
	public static String IDX__DFS_VOLUME__TYPE = "Volume";

}
