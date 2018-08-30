package org.orbit.substance.runtime.dfs.service.impl.other;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;

/*
 * Table name:
 * 		{dfs_volume_id}_metadata
 * 
 * Table columns (6):
 * 		id (pk)
 * 		volumeId
 * 		volumeCapacity
 * 		volumeSize
 * 		dateCreated
 *		dateModified
 *
 * Table name examples:
 * 		dfs1_volume1_metadata
 * 
 */
public class DFSVolumesMetadataTableHandler implements DatabaseTableAware {

	public static Map<String, DFSVolumesMetadataTableHandler> tableHandlerMap = new HashMap<String, DFSVolumesMetadataTableHandler>();

	/**
	 * 
	 * @param dfsVolumeId
	 * @return
	 */
	public static String doGetTableName(String dfsVolumeId) {
		return dfsVolumeId + "_blocks";
	}

	/**
	 * 
	 * @param conn
	 * @param dfsVolumeId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized DFSVolumesMetadataTableHandler getInstance(Connection conn, String dfsVolumeId) throws SQLException {
		String tableName = doGetTableName(dfsVolumeId);
		DFSVolumesMetadataTableHandler tableHandler = tableHandlerMap.get(tableName);
		if (tableHandler == null) {
			DFSVolumesMetadataTableHandler newTableHandler = new DFSVolumesMetadataTableHandler(dfsVolumeId);
			tableHandlerMap.put(tableName, newTableHandler);
			tableHandler = newTableHandler;
		}

		if (!DatabaseUtil.tableExist(conn, tableName)) {
			boolean initialized = DatabaseUtil.initialize(conn, tableHandler);
			if (!initialized) {
				System.err.println("Table '" + tableName + "' cannot be initialized.");
				throw new SQLException("Table '" + tableName + "' cannot be initialized.");
			}
		}
		return tableHandler;
	}

	/**
	 * 
	 * @param conn
	 * @param dfsVolumeId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized boolean dispose(Connection conn, String dfsVolumeId) throws SQLException {
		String tableName = doGetTableName(dfsVolumeId);

		DFSVolumesMetadataTableHandler tableHandler = tableHandlerMap.get(tableName);
		if (tableHandler != null) {
			if (DatabaseUtil.tableExist(conn, tableHandler)) {
				boolean disposed = DatabaseUtil.dispose(conn, tableHandler);
				if (disposed) {
					tableHandlerMap.remove(tableName);
				} else {
					System.err.println("Table '" + tableName + "' cannot be disposed.");
					throw new SQLException("Table '" + tableName + "' cannot be disposed.");
				}
				return disposed;
			}
		}
		return false;
	}

	protected String dfsVolumeId;

	/**
	 * 
	 * @param dfsVolumeId
	 */
	public DFSVolumesMetadataTableHandler(String dfsVolumeId) {
		this.dfsVolumeId = dfsVolumeId;
	}

	@Override
	public String getTableName() {
		String tableName = doGetTableName(dfsVolumeId);
		return tableName;
	}

	@Override
	public String getCreateTableSQL(String database) {
		return null;
	}

}
