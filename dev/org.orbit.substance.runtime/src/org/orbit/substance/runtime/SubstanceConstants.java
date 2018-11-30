package org.orbit.substance.runtime;

public class SubstanceConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	// Configuration property names
	public static String ORBIT_HOST_URL = "orbit.host.url";

	public static String TOKEN_PROVIDER__ORBIT = "orbit";

	// ----------------------------------------------------------------------------------------
	// Dfs service
	// ----------------------------------------------------------------------------------------
	// Configuration property names
	public static String DFS__AUTOSTART = "substance.dfs.autostart";
	public static String DFS__ID = "substance.dfs.id";
	public static String DFS__NAME = "substance.dfs.name";
	public static String DFS__HOST_URL = "substance.dfs.host.url";
	public static String DFS__CONTEXT_ROOT = "substance.dfs.context_root";
	public static String DFS__JDBC_DRIVER = "substance.dfs.jdbc.driver";
	public static String DFS__JDBC_URL = "substance.dfs.jdbc.url";
	public static String DFS__JDBC_USERNAME = "substance.dfs.jdbc.username";
	public static String DFS__JDBC_PASSWORD = "substance.dfs.jdbc.password";
	public static String DFS__BLOCK_CAPACITY_MB = "substance.dfs.block_capacity_mb";

	// Index item values
	public static String IDX__DFS__INDEXER_ID = "substance.dfs.indexer";
	public static String IDX__DFS__TYPE = "DFS";

	// Index item properties
	public static String IDX_PROP__DFS__ID = "dfs.id";

	// EditPolicy values
	public static String DFS__EDITPOLICY_ID = "substance.dfs.editpolicy";
	public static String DFS__SERVICE_NAME = "substance.dfs.service";

	// ----------------------------------------------------------------------------------------
	// Dfs volume service
	// ----------------------------------------------------------------------------------------
	// Configuration property names
	public static String DFS_VOLUME__AUTOSTART = "substance.dfs_volume.autostart";
	public static String DFS_VOLUME__DFS_ID = "substance.dfs_volume.dfs_id";
	public static String DFS_VOLUME__ID = "substance.dfs_volume.id";
	public static String DFS_VOLUME__NAME = "substance.dfs_volume.name";
	public static String DFS_VOLUME__HOST_URL = "substance.dfs_volume.host.url";
	public static String DFS_VOLUME__CONTEXT_ROOT = "substance.dfs_volume.context_root";
	public static String DFS_VOLUME__JDBC_DRIVER = "substance.dfs_volume.jdbc.driver";
	public static String DFS_VOLUME__JDBC_URL = "substance.dfs_volume.jdbc.url";
	public static String DFS_VOLUME__JDBC_USERNAME = "substance.dfs_volume.jdbc.username";
	public static String DFS_VOLUME__JDBC_PASSWORD = "substance.dfs_volume.jdbc.password";
	public static String DFS_VOLUME__VOLUME_CAPACITY_GB = "substance.dfs_volume.volume_capacity_gb";
	public static String DFS_VOLUME__BLOCK_CAPACITY_MB = "substance.dfs_volume.block_capacity_mb";

	// Index item values
	public static String IDX__DFS_VOLUME__INDEXER_ID = "substance.dfs_volume.indexer";
	public static String IDX__DFS_VOLUME__TYPE = "Volume";

	// Index item properties
	public static String IDX_PROP__DFS_VOLUME__DFS_ID = "dfs_volume.dfs_id";
	public static String IDX_PROP__DFS_VOLUME__ID = "dfs_volume.id";
	public static String IDX_PROP__DFS_VOLUME__VOLUME_CAPACITY = "dfs_volume.volume_capacity";
	public static String IDX_PROP__DFS_VOLUME__BLOCK_CAPACITY = "dfs_volume.block_capacity";

	// EditPolicy values
	public static String DFS_VOLUME__EDITPOLICY_ID = "substance.dfs_volume.editpolicy";
	public static String DFS_VOLUME__SERVICE_NAME = "substance.dfs_volume.service";

}
