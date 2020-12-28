package org.orbit.substance.runtime.dfs.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.util.RuntimeModelConverter;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableProvider;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

/*
 * Table name:
 * 		{dfs_id}_accountId
 * 
 * Table name examples:
 * 		dfs1_account1
 *      dfs1_account2
 *      dfs1_account3
 *      dfs1_account4
 * 
 */
public class FilesMetadataTableHandler implements DatabaseTableProvider {

	public static Map<String, FilesMetadataTableHandler> tableHandlerMap = new HashMap<String, FilesMetadataTableHandler>();

	/**
	 * 
	 * @param conn
	 * @param dfsId
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized FilesMetadataTableHandler getInstance(Connection conn, String dfsId, String accountId) throws SQLException {
		String tableName = doGetTableName(dfsId, accountId);
		FilesMetadataTableHandler tableHandler = tableHandlerMap.get(tableName);
		if (tableHandler == null) {
			FilesMetadataTableHandler newTableHandler = new FilesMetadataTableHandler(dfsId, accountId);
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
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized boolean dispose(Connection conn, String dfsId, String accountId) throws SQLException {
		String tableName = doGetTableName(dfsId, accountId);

		FilesMetadataTableHandler tableHandler = tableHandlerMap.get(tableName);
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
	protected String accountId;

	/**
	 * 
	 * @param dfsId
	 * @param accountId
	 */
	public FilesMetadataTableHandler(String dfsId, String accountId) {
		this.dfsId = dfsId;
		this.accountId = accountId;
	}

	@Override
	public String getTableName() {
		String tableName = doGetTableName(this.dfsId, this.accountId);
		return tableName;
	}

	/**
	 * 
	 * @param dfsId
	 * @param accountId
	 * @return
	 */
	public static String doGetTableName(String dfsId, String accountId) {
		if (accountId.contains(".")) {
			accountId = accountId.replaceAll("\\.", "_");
		}
		if (accountId.contains(".")) {
			accountId = accountId.replaceAll("\\.", "_");
		}
		if (accountId.contains("-")) {
			accountId = accountId.replaceAll("\\-", "_");
		}
		return dfsId + "_" + accountId;
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();
		if (DatabaseTableProvider.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id int NOT NULL AUTO_INCREMENT,");
			sb.append("    fileId varchar(250) NOT NULL,");
			sb.append("    parentFileId varchar(250) NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    size bigint DEFAULT 0,");
			sb.append("    isDirectory boolean NOT NULL DEFAULT false,");
			sb.append("    isHidden boolean NOT NULL DEFAULT false,");
			sb.append("    inTrash boolean DEFAULT false,");
			sb.append("    fileParts varchar(20000) DEFAULT NULL,");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableProvider.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id serial NOT NULL,");
			sb.append("    fileId varchar(250) NOT NULL,");
			sb.append("    parentFileId varchar(250) NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    size bigint DEFAULT 0,");
			sb.append("    isDirectory boolean NOT NULL DEFAULT false,");
			sb.append("    isHidden boolean NOT NULL DEFAULT false,");
			sb.append("    inTrash boolean DEFAULT false,");
			sb.append("    fileParts varchar(20000) DEFAULT NULL,");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
		}
		return sb.toString();
	}

	/**
	 * Convert a ResultSet record to a FileMetadata object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected FileMetadata toFileMetadata(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String fileId = rs.getString("fileId");
		String parentFileId = rs.getString("parentFileId");
		String name = rs.getString("name");
		long size = rs.getLong("size");
		boolean isDirectory = rs.getBoolean("isDirectory");
		boolean isHidden = rs.getBoolean("isHidden");
		boolean inTrash = rs.getBoolean("inTrash");
		String filePartsString = rs.getString("fileParts");
		String propertiesString = rs.getString("properties");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		if (parentFileId == null || parentFileId.isEmpty()) {
			// this should never happen. just in case.
			parentFileId = "-1";
		}

		List<FilePart> fileParts = RuntimeModelConverter.Dfs.toFileParts(filePartsString);
		Map<String, Object> properties = RuntimeModelConverter.Dfs.toProperties(propertiesString);

		return new FileMetadataImpl(this.dfsId, id, this.accountId, fileId, parentFileId, null, name, size, isDirectory, isHidden, inTrash, fileParts, properties, dateCreated, dateModified);
	}

	/**
	 * 
	 * @param conn
	 * @param inTrash
	 * @return
	 * @throws SQLException
	 */
	public List<FileMetadata> getList(Connection conn, boolean inTrash) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE inTrash=? ORDER BY id ASC";
		ResultSetListHandler<FileMetadata> handler = new ResultSetListHandler<FileMetadata>() {
			@Override
			protected FileMetadata handleRow(ResultSet rs) throws SQLException {
				return toFileMetadata(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { inTrash }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param inTrash
	 * @return
	 * @throws SQLException
	 */
	public List<FileMetadata> getList(Connection conn, String parentFileId, boolean inTrash) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentFileId=? AND inTrash=? ORDER BY id ASC";
		ResultSetListHandler<FileMetadata> handler = new ResultSetListHandler<FileMetadata>() {
			@Override
			protected FileMetadata handleRow(ResultSet rs) throws SQLException {
				return toFileMetadata(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentFileId, inTrash }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean recordExistsByFileId(Connection conn, String fileId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean recordExistsByName(Connection conn, String parentFileId, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentFileId=? AND name=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentFileId, name }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public FileMetadata getByFileId(Connection conn, String fileId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=?";
		ResultSetSingleHandler<FileMetadata> handler = new ResultSetSingleHandler<FileMetadata>() {
			@Override
			protected FileMetadata handleRow(ResultSet rs) throws SQLException {
				return toFileMetadata(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public FileMetadata getByName(Connection conn, String parentFileId, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentFileId=? AND name=?";
		ResultSetSingleHandler<FileMetadata> handler = new ResultSetSingleHandler<FileMetadata>() {
			@Override
			protected FileMetadata handleRow(ResultSet rs) throws SQLException {
				return toFileMetadata(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentFileId, name }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	protected boolean getBoolean(Connection conn, String fileId, final String columnName) throws SQLException {
		String querySQL = "SELECT " + columnName + " FROM " + getTableName() + " WHERE fileId=?";
		ResultSetSingleHandler<Boolean> handler = new ResultSetSingleHandler<Boolean>() {
			@Override
			protected Boolean handleRow(ResultSet rs) throws SQLException {
				boolean value = rs.getBoolean(columnName);
				return value ? true : false;
			}

			@Override
			protected Boolean getEmptyValue() {
				return false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean isDirectory(Connection conn, String fileId) throws SQLException {
		return getBoolean(conn, fileId, "isDirectory");
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean isHidden(Connection conn, String fileId) throws SQLException {
		return getBoolean(conn, fileId, "isHidden");
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean isInTrash(Connection conn, String fileId) throws SQLException {
		return getBoolean(conn, fileId, "inTrash");
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param parentFileId
	 * @param name
	 * @param isDirectory
	 * @param isHidden
	 * @param size
	 * @param fileParts
	 * @param properties
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public FileMetadata create(Connection conn, String fileId, String parentFileId, String name, long size, boolean isDirectory, boolean isHidden, boolean inTrash, List<FilePart> fileParts, Map<String, Object> properties) throws SQLException, IOException {
		if (recordExistsByFileId(conn, fileId)) {
			throw new IOException("File metadata with same file Id already exists.");
		}
		if (recordExistsByName(conn, parentFileId, name)) {
			throw new IOException("File metadata with same name already exists.");
		}

		String filePartsString = RuntimeModelConverter.Dfs.toFilePartsString(fileParts);
		String propertiesString = RuntimeModelConverter.Dfs.toPropertiesString(properties);

		long dateCreated = getCurrentTime();
		long dateModified = dateCreated;

		String insertSQL = "INSERT INTO " + getTableName() + " (fileId, parentFileId, name, size, isDirectory, isHidden, inTrash, fileParts, properties, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { fileId, parentFileId, name, size, isDirectory, isHidden, inTrash, filePartsString, propertiesString, dateCreated, dateModified }, 1);
		if (succeed) {
			return getByFileId(conn, fileId);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param parentFileId
	 * @return
	 * @throws SQLException
	 */
	public boolean updateParentId(Connection conn, String fileId, int parentFileId) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET parentFileId=?, dateModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { parentFileId, getCurrentTime(), fileId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String fileId, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, dateModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, getCurrentTime(), fileId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param size
	 * @return
	 * @throws SQLException
	 */
	public boolean updateSize(Connection conn, String fileId, long size) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET size=?, dateModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { size, getCurrentTime(), fileId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param isDirectory
	 * @return
	 * @throws SQLException
	 */
	public boolean updateIsDirectory(Connection conn, String fileId, boolean isDirectory) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET isDirectory=?, dateModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { isDirectory, getCurrentTime(), fileId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param isHidden
	 * @return
	 * @throws SQLException
	 */
	public boolean updateIsHidden(Connection conn, String fileId, boolean isHidden) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET isHidden=?, dateModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { isHidden, getCurrentTime(), fileId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param inTrash
	 * @return
	 * @throws SQLException
	 */
	public boolean updateInTrash(Connection conn, String fileId, boolean inTrash) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET inTrash=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { inTrash, fileId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param fileParts
	 * @return
	 * @throws SQLException
	 */
	public boolean updateFileParts(Connection conn, String fileId, List<FilePart> fileParts) throws SQLException {
		String filePartsString = RuntimeModelConverter.Dfs.toFilePartsString(fileParts);

		String updateSQL = "UPDATE " + getTableName() + " SET fileParts=?, dateModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { filePartsString, getCurrentTime(), fileId }, 1);
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
		String propertiesString = RuntimeModelConverter.Dfs.toPropertiesString(properties);

		String updateSQL = "UPDATE " + getTableName() + " SET properties=?, dateModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { propertiesString, getCurrentTime(), fileId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param dateModified
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDateModified(Connection conn, String fileId, long dateModified) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET dateModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { dateModified, fileId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteByFileId(Connection conn, String fileId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE fileId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { fileId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteByName(Connection conn, String parentFileId, String name) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE parentFileId=? AND name=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { parentFileId, name }, 1);
	}

	/**
	 * 
	 * @return
	 */
	protected long getCurrentTime() {
		return new Date().getTime();
	}

}

// boolean exists = false;
// if (fileParts != null && !fileParts.isEmpty()) {
// exists = true;
// }

// /**
// *
// * @param conn
// * @return
// * @throws SQLException
// */
// public List<FileMetadata> getList(Connection conn) throws SQLException {
// String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY id ASC";
// ResultSetListHandler<FileMetadata> handler = new ResultSetListHandler<FileMetadata>() {
// @Override
// protected FileMetadata handleRow(ResultSet rs) throws SQLException {
// return toFileMetadata(rs);
// }
// };
// return DatabaseUtil.query(conn, querySQL, null, handler);
// }

// /**
// *
// * @param conn
// * @param parentFileId
// * @return
// * @throws SQLException
// */
// public List<FileMetadata> getList(Connection conn, String parentFileId) throws SQLException {
// String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentFileId=? ORDER BY id ASC";
// ResultSetListHandler<FileMetadata> handler = new ResultSetListHandler<FileMetadata>() {
// @Override
// protected FileMetadata handleRow(ResultSet rs) throws SQLException {
// return toFileMetadata(rs);
// }
// };
// return DatabaseUtil.query(conn, querySQL, new Object[] { parentFileId }, handler);
// }
