package org.orbit.substance.runtime.dfsvolume.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfsvolume.PendingFile;
import org.orbit.substance.runtime.dfsvolume.service.DataBlockMetadata;
import org.orbit.substance.runtime.util.RuntimeModelConverter;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

/*
 * Table name:
 * 		{dfs_volume_id}_blocks
 * 
 * Table name examples:
 * 		dfs1_volume1_blocks
 *      dfs1_volume2_blocks
 *      dfs1_volume3_blocks
 *      dfs1_volume4_blocks
 * 
 */
public class VolumeBlocksTableHandler implements DatabaseTableAware {

	public static Map<String, VolumeBlocksTableHandler> tableHandlerMap = new HashMap<String, VolumeBlocksTableHandler>();

	/**
	 * 
	 * @param conn
	 * @param dfsId
	 * @param dfsVolumeId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized VolumeBlocksTableHandler getInstance(Connection conn, String dfsId, String dfsVolumeId) throws SQLException {
		String tableName = doGetTableName(dfsId, dfsVolumeId);
		VolumeBlocksTableHandler tableHandler = tableHandlerMap.get(tableName);
		if (tableHandler == null) {
			VolumeBlocksTableHandler newTableHandler = new VolumeBlocksTableHandler(dfsId, dfsVolumeId);
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
	 * @param dfsId
	 * @param dfsVolumeId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized boolean dispose(Connection conn, String dfsId, String dfsVolumeId) throws SQLException {
		String tableName = doGetTableName(dfsId, dfsVolumeId);

		VolumeBlocksTableHandler tableHandler = tableHandlerMap.get(tableName);
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

	protected String dfsId;
	protected String dfsVolumeId;

	/**
	 * 
	 * @param dfsId
	 * @param dfsVolumeId
	 */
	public VolumeBlocksTableHandler(String dfsId, String dfsVolumeId) {
		this.dfsId = dfsId;
		this.dfsVolumeId = dfsVolumeId;
	}

	@Override
	public String getTableName() {
		String tableName = doGetTableName(this.dfsId, this.dfsVolumeId);
		return tableName;
	}

	/**
	 * 
	 * @param dfsVolumeId
	 * @return
	 */
	public static String doGetTableName(String dfsId, String dfsVolumeId) {
		return dfsId + "_" + dfsVolumeId + "_blocks";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id int NOT NULL AUTO_INCREMENT,");
			sb.append("    blockId varchar(250) NOT NULL,");
			sb.append("    accountId varchar(250) NOT NULL,");
			sb.append("    capacity bigint DEFAULT 0,");
			sb.append("    size bigint DEFAULT 0,");
			sb.append("    pendingFiles varchar(5000) DEFAULT NULL,");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id serial NOT NULL,");
			sb.append("    blockId varchar(250) NOT NULL,");
			sb.append("    accountId varchar(500) NOT NULL,");
			sb.append("    capacity bigint DEFAULT 0,");
			sb.append("    size bigint DEFAULT 0,");
			sb.append("    pendingFiles varchar(5000) DEFAULT NULL,");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
		}
		return sb.toString();
	}

	/**
	 * Convert a ResultSet record to a DataBlockMetadata object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected DataBlockMetadata toDataBlock(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String blockId = rs.getString("blockId");
		String accountId = rs.getString("accountId");
		long capacity = rs.getLong("capacity");
		long size = rs.getLong("size");
		String pendingFilesString = rs.getString("pendingFiles");
		String propertiesString = rs.getString("properties");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		List<PendingFile> pendingFiles = RuntimeModelConverter.DfsVolume.toPendingFiles(pendingFilesString);
		Map<String, Object> properties = RuntimeModelConverter.Dfs.toProperties(propertiesString);

		return new DataBlockMetadataImpl(this.dfsId, this.dfsVolumeId, id, blockId, accountId, capacity, size, pendingFiles, properties, dateCreated, dateModified);
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<DataBlockMetadata> getList(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY blockId ASC";
		ResultSetListHandler<DataBlockMetadata> rsHandler = new ResultSetListHandler<DataBlockMetadata>() {
			@Override
			protected DataBlockMetadata handleRow(ResultSet rs) throws SQLException {
				return toDataBlock(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] {}, rsHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public List<DataBlockMetadata> getList(Connection conn, String accountId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE accountId=? ORDER BY blockId ASC";
		ResultSetListHandler<DataBlockMetadata> rsHandler = new ResultSetListHandler<DataBlockMetadata>() {
			@Override
			protected DataBlockMetadata handleRow(ResultSet rs) throws SQLException {
				return toDataBlock(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { accountId }, rsHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param blockId
	 * @return
	 * @throws SQLException
	 */
	public boolean exists(Connection conn, String blockId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE blockId=?";
		ResultSetSingleHandler<Boolean> rsHandler = new ResultSetSingleHandler<Boolean>() {
			@Override
			protected Boolean handleRow(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}

			@Override
			protected Boolean getEmptyValue() {
				return false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { blockId }, rsHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param blockId
	 * @return
	 * @throws SQLException
	 */
	public DataBlockMetadata get(Connection conn, String blockId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE blockId=?";
		ResultSetSingleHandler<DataBlockMetadata> rsHandler = new ResultSetSingleHandler<DataBlockMetadata>() {
			@Override
			protected DataBlockMetadata handleRow(ResultSet rs) throws SQLException {
				return toDataBlock(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { blockId }, rsHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param blockId
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public DataBlockMetadata get(Connection conn, String blockId, String accountId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE blockId=? AND accountId=?";
		ResultSetSingleHandler<DataBlockMetadata> rsHandler = new ResultSetSingleHandler<DataBlockMetadata>() {
			@Override
			protected DataBlockMetadata handleRow(ResultSet rs) throws SQLException {
				return toDataBlock(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { blockId, accountId }, rsHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param blockId
	 * @param accountId
	 * @param capacity
	 * @param size
	 * @param pendingFiles
	 * @param dateCreated
	 * @param dateModified
	 * @return
	 * @throws SQLException
	 */
	public DataBlockMetadata insert(Connection conn, String blockId, String accountId, long capacity, long size, List<PendingFile> pendingFiles, long dateCreated, long dateModified) throws SQLException {
		String pendingFilesString = RuntimeModelConverter.DfsVolume.toPendingFilesString(pendingFiles);

		String insertSQL = "INSERT INTO " + getTableName() + " (blockId, accountId, capacity, size, pendingFiles, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { blockId, accountId, capacity, size, pendingFilesString, dateCreated, dateModified }, 1);
		if (succeed) {
			return get(conn, blockId, accountId);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param dataBlock
	 * @return
	 * @throws SQLException
	 */
	public boolean update(Connection conn, DataBlockMetadata dataBlock) throws SQLException {
		int id = dataBlock.getId();
		String blockId = dataBlock.getBlockId();
		String accountId = dataBlock.getAccountId();
		long capacity = dataBlock.getCapacity();
		long size = dataBlock.getSize();
		long dateModified = getCurrentTime();

		if (id > 0) {
			String updateSQL = "UPDATE " + getTableName() + " SET capacity=?, size=?, dateModified=? WHERE id=?";
			return DatabaseUtil.update(conn, updateSQL, new Object[] { capacity, size, dateModified, id }, 1);

		} else {
			String updateSQL = "UPDATE " + getTableName() + " SET capacity=?, size=?, dateModified=? WHERE blockId=? AND accountId=?";
			return DatabaseUtil.update(conn, updateSQL, new Object[] { capacity, size, dateModified, blockId, accountId }, 1);
		}
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @param accountId
	 * @param blockId
	 * @param newSize
	 * @return
	 * @throws SQLException
	 */
	public boolean updateSize(Connection conn, int id, String accountId, String blockId, long newSize) throws SQLException {
		long dateModified = getCurrentTime();

		if (id > 0) {
			String updateSQL = "UPDATE " + getTableName() + " SET size=?, dateModified=? WHERE id=?";
			return DatabaseUtil.update(conn, updateSQL, new Object[] { newSize, dateModified, id }, 1);

		} else {
			String updateSQL = "UPDATE " + getTableName() + " SET size=?, dateModified=? WHERE blockId=? AND accountId=?";
			return DatabaseUtil.update(conn, updateSQL, new Object[] { newSize, dateModified, blockId, accountId }, 1);
		}
	}

	// /**
	// *
	// * @param conn
	// * @param id
	// * @param blockId
	// * @param accountId
	// * @param sizeDelta
	// * @return
	// * @throws SQLException
	// */
	// public boolean updateSizeByDelta(Connection conn, int id, String blockId, String accountId, long sizeDelta) throws SQLException {
	// DataBlockMetadata dataBlock = get(conn, blockId, accountId);
	// if (dataBlock == null) {
	// return false;
	// }
	//
	// long dateModified = getCurrentTime();
	//
	// long newSize = dataBlock.getSize() + sizeDelta;
	// if (id > 0) {
	// String updateSQL = "UPDATE " + getTableName() + " SET size=?, dateModified=? WHERE id=?";
	// return DatabaseUtil.update(conn, updateSQL, new Object[] { newSize, dateModified, id }, 1);
	//
	// } else {
	// String updateSQL = "UPDATE " + getTableName() + " SET size=?, dateModified=? WHERE blockId=? AND accountId=?";
	// return DatabaseUtil.update(conn, updateSQL, new Object[] { newSize, dateModified, blockId, accountId }, 1);
	// }
	// }

	/**
	 * 
	 * @param conn
	 * @param id
	 * @param blockId
	 * @param accountId
	 * @param pendingFiles
	 * @return
	 * @throws SQLException
	 */
	public boolean updatePendingFiles(Connection conn, int id, String blockId, String accountId, List<PendingFile> pendingFiles) throws SQLException {
		String pendingFilesString = RuntimeModelConverter.DfsVolume.toPendingFilesString(pendingFiles);

		if (id > 0) {
			String updateSQL = "UPDATE " + getTableName() + " SET pendingFiles=?, dateModified=? WHERE id=?";
			return DatabaseUtil.update(conn, updateSQL, new Object[] { pendingFilesString, getCurrentTime(), id }, 1);

		} else {
			String updateSQL = "UPDATE " + getTableName() + " SET pendingFiles=?, dateModified=? WHERE blockId=? AND accountId=?";
			return DatabaseUtil.update(conn, updateSQL, new Object[] { pendingFilesString, getCurrentTime(), blockId, accountId }, 1);
		}
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param properties
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, String fileId, Map<String, Object> properties) throws SQLException {
		String propertiesString = RuntimeModelConverter.DfsVolume.toPropertiesString(properties);

		String updateSQL = "UPDATE " + getTableName() + " SET properties=?, dateModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { propertiesString, getCurrentTime(), fileId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, int id) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE id=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param blockId
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String blockId, String accountId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE blockId=? AND accountId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { blockId, accountId }, 1);
	}

	/**
	 * 
	 * @return
	 */
	protected long getCurrentTime() {
		return new Date().getTime();
	}
}
