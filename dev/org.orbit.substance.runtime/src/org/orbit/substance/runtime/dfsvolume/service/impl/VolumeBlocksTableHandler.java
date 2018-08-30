package org.orbit.substance.runtime.dfsvolume.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.runtime.model.dfsvolume.DataBlockMetadata;
import org.orbit.substance.runtime.model.dfsvolume.impl.DataBlockMetadataImpl;
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
	 * @param dfsVolumeId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized VolumeBlocksTableHandler getInstance(Connection conn, String dfsVolumeId) throws SQLException {
		String tableName = doGetTableName(dfsVolumeId);
		VolumeBlocksTableHandler tableHandler = tableHandlerMap.get(tableName);
		if (tableHandler == null) {
			VolumeBlocksTableHandler newTableHandler = new VolumeBlocksTableHandler(dfsVolumeId);
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

	protected String dfsVolumeId;

	/**
	 * 
	 * @param dfsVolumeId
	 */
	public VolumeBlocksTableHandler(String dfsVolumeId) {
		this.dfsVolumeId = dfsVolumeId;
	}

	@Override
	public String getTableName() {
		String tableName = doGetTableName(this.dfsVolumeId);
		return tableName;
	}

	/**
	 * 
	 * @param dfsVolumeId
	 * @return
	 */
	public static String doGetTableName(String dfsVolumeId) {
		return dfsVolumeId + "_blocks";
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
	protected static DataBlockMetadata toDataBlock(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String blockId = rs.getString("blockId");
		String accountId = rs.getString("accountId");
		long capacity = rs.getLong("capacity");
		long size = rs.getLong("size");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		return new DataBlockMetadataImpl(id, blockId, accountId, capacity, size, dateCreated, dateModified);
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
	 * @param dateCreated
	 * @param dateModified
	 * @return
	 * @throws SQLException
	 */
	public DataBlockMetadata insert(Connection conn, String blockId, String accountId, long capacity, long size, long dateCreated, long dateModified) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (blockId, accountId, capacity, size, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { blockId, accountId, capacity, size, dateCreated, dateModified }, 1);
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
		long dateModified = new Date().getTime();

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

}
