package org.orbit.substance.api;

public interface SubstanceConstants {

	// ----------------------------------------------------------------------------------------
	// orbit service config properties
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_DFS_ID = "orbit.dfs.id";
	public static String ORBIT_DFS_URL = "orbit.dfs.url";

	public static String ORBIT_DFS_VOLUME_URL = "orbit.dfs_volume.url";

	// ----------------------------------------------------------------------------------------
	// Dfs service
	// ----------------------------------------------------------------------------------------
	// Index item properties
	public static String IDX_PROP__DFS__ID = "dfs.id";

	// Index item values
	public static String IDX__DFS__INDEXER_ID = "substance.dfs.indexer";
	public static String IDX__DFS__TYPE = "DFS";

	// ----------------------------------------------------------------------------------------
	// Dfs volume service
	// ----------------------------------------------------------------------------------------
	// Index item properties
	public static String IDX_PROP__DFS_VOLUME__DFS_ID = "dfs_volume.dfs_id";
	public static String IDX_PROP__DFS_VOLUME__ID = "dfs_volume.id";
	public static String IDX_PROP__DFS_VOLUME__VOLUME_CAPACITY = "dfs_volume.volume_capacity";

	// Index item values
	public static String IDX__DFS_VOLUME__INDEXER_ID = "substance.dfs_volume.indexer";
	public static String IDX__DFS_VOLUME__TYPE = "Volume";

}

// public static String IDX_PROP__DFS_VOLUME__BLOCK_CAPACITY = "dfs_volume.block_capacity";
