package org.orbit.substance.api;

public interface SubstanceConstants {

	// ----------------------------------------------------------------------------------------
	// orbit service config properties
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_DFS_URL = "orbit.dfs.url";

	public static String ORBIT_DFS_VOLUME_URL = "orbit.dfs_volume.url";

	// ----------------------------------------------------------------------------------------
	// Dfs service
	// ----------------------------------------------------------------------------------------
	// Index item properties
	public static String IDX_PROP__DFS__ID = "dfs.id";
	public static String IDX_PROP__DFS__NAME = "dfs.name";
	public static String IDX_PROP__DFS__HOST_URL = "dfs.host.url";
	public static String IDX_PROP__DFS__CONTEXT_ROOT = "dfs.context_root";
	public static String IDX_PROP__DFS__BASE_URL = "dfs.base_url";

	// Index item values
	public static String IDX__DFS__INDEXER_ID = "substance.dfs.indexer";
	public static String IDX__DFS__TYPE = "DFS";

	// ----------------------------------------------------------------------------------------
	// Dfs volume service
	// ----------------------------------------------------------------------------------------
	// Index item properties
	public static String IDX_PROP__DFS_VOLUME__DFS_ID = "dfs_volume.dfs_id";
	public static String IDX_PROP__DFS_VOLUME__ID = "dfs_volume.id";
	public static String IDX_PROP__DFS_VOLUME__NAME = "dfs_volume.name";
	public static String IDX_PROP__DFS_VOLUME__HOST_URL = "dfs_volume.host.url";
	public static String IDX_PROP__DFS_VOLUME__CONTEXT_ROOT = "dfs_volume.context_root";
	public static String IDX_PROP__DFS_VOLUME__BASE_URL = "dfs_volume.base_url";
	public static String IDX_PROP__DFS_VOLUME__VOLUME_CAPACITY = "dfs_volume.volume_capacity";

	// Index item values
	public static String IDX__DFS_VOLUME__INDEXER_ID = "substance.dfs_volume.indexer";
	public static String IDX__DFS_VOLUME__TYPE = "Volume";

}

// public static String IDX_PROP__DFS_VOLUME__BLOCK_CAPACITY = "dfs_volume.block_capacity";
