package org.orbit.substance.runtime.dfsvolume.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.runtime.dfsvolume.service.FileContentMetadata;
import org.origin.common.io.IOUtil;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

/*
 * Table name:
 * 		{dfs_volume_id}_{block_id}
 * 
 * Table name examples:
 * 		dfs1_volume1_block1
 * 		dfs1_volume1_block2
 * 		dfs1_volume1_block3
 * 
 */
public class VolumeFileContentTableHandler implements DatabaseTableAware {

	public static Map<String, VolumeFileContentTableHandler> tableHandlerMap = new HashMap<String, VolumeFileContentTableHandler>();

	/**
	 * 
	 * @param conn
	 * @param dfsId
	 * @param dfsVolumeId
	 * @param blockId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized VolumeFileContentTableHandler getInstance(Connection conn, String dfsId, String dfsVolumeId, String blockId) throws SQLException {
		String tableName = doGetTableName(dfsId, dfsVolumeId, blockId);
		VolumeFileContentTableHandler tableHandler = tableHandlerMap.get(tableName);
		if (tableHandler == null) {
			VolumeFileContentTableHandler newTableHandler = new VolumeFileContentTableHandler(dfsId, dfsVolumeId, blockId);
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
	 * @param blockId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized boolean dispose(Connection conn, String dfsId, String dfsVolumeId, String blockId) throws SQLException {
		String tableName = doGetTableName(dfsId, dfsVolumeId, blockId);

		VolumeFileContentTableHandler tableHandler = tableHandlerMap.get(tableName);
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

	protected String database;
	protected String dfsId;
	protected String dfsVolumeId;
	protected String blockId;

	/**
	 * 
	 * @param dfsId
	 * @param dfsVolumeId
	 * @param blockId
	 */
	public VolumeFileContentTableHandler(String dfsId, String dfsVolumeId, String blockId) {
		this.dfsId = dfsId;
		this.dfsVolumeId = dfsVolumeId;
		this.blockId = blockId;
	}

	public String getDatabase() {
		return this.database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	@Override
	public String getTableName() {
		String tableName = doGetTableName(this.dfsId, this.dfsVolumeId, this.blockId);
		return tableName;
	}

	/**
	 * 
	 * @param dfsId
	 * @param dfsVolumeId
	 * @param blockId
	 * @return
	 */
	public static String doGetTableName(String dfsId, String dfsVolumeId, String blockId) {
		return dfsId + "_" + dfsVolumeId + "_" + blockId;
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id int NOT NULL AUTO_INCREMENT,");
			sb.append("    fileId varchar(500) NOT NULL,");
			sb.append("    partId int DEFAULT 0,");
			sb.append("    size bigint DEFAULT 0,");
			sb.append("    checksum bigint DEFAULT 0,");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    fileContent mediumblob,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id serial NOT NULL,");
			sb.append("    fileId varchar(500) NOT NULL,");
			sb.append("    partId int DEFAULT 0,");
			sb.append("    size bigint DEFAULT 0,");
			sb.append("    checksum bigint DEFAULT 0,");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    fileContent bytea,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
		}
		return sb.toString();
	}

	/**
	 * Convert a ResultSet record to a FileContentMetadata object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected FileContentMetadata toFileContent(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String fileId = rs.getString("fileId");
		int partId = rs.getInt("partId");
		long size = rs.getLong("size");
		long checksum = rs.getLong("checksum");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		return new FileContentMetadataImpl(this.dfsId, this.dfsVolumeId, this.blockId, id, fileId, partId, size, checksum, dateCreated, dateModified);
	}

	/**
	 * 
	 * @param conn
	 * @param dfsId
	 * @param dfsVolumeId
	 * @param blockId
	 * @return
	 * @throws SQLException
	 */
	public List<FileContentMetadata> getList(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY fileId ASC";
		ResultSetListHandler<FileContentMetadata> rsHandler = new ResultSetListHandler<FileContentMetadata>() {
			@Override
			protected FileContentMetadata handleRow(ResultSet rs) throws SQLException {
				return toFileContent(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] {}, rsHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public List<FileContentMetadata> getList(Connection conn, String fileId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=? ORDER BY fileId ASC";
		ResultSetListHandler<FileContentMetadata> rsHandler = new ResultSetListHandler<FileContentMetadata>() {
			@Override
			protected FileContentMetadata handleRow(ResultSet rs) throws SQLException {
				return toFileContent(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileId }, rsHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param partId
	 * @return
	 * @throws SQLException
	 */
	public FileContentMetadata get(Connection conn, String fileId, int partId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=? AND partId=?";
		ResultSetSingleHandler<FileContentMetadata> rsHandler = new ResultSetSingleHandler<FileContentMetadata>() {
			@Override
			protected FileContentMetadata handleRow(ResultSet rs) throws SQLException {
				return toFileContent(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileId, partId }, rsHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param partId
	 * @return
	 * @throws SQLException
	 */
	public boolean exists(Connection conn, String fileId, int partId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=? AND partId=?";
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
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileId, partId }, rsHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param partId
	 * @param size
	 * @param checksum
	 * @param dateCreated
	 * @param dateModified
	 * @return
	 * @throws SQLException
	 */
	public FileContentMetadata insert(Connection conn, String fileId, int partId, long size, long checksum, long dateCreated, long dateModified) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (fileId, partId, size, checksum, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { fileId, partId, size, checksum, dateCreated, dateModified }, 1);
		if (succeed) {
			return get(conn, fileId, partId);
		}
		return null;
	}

	/**
	 * 
	 * @param fileContent
	 * @return
	 * @throws SQLException
	 */
	public boolean update(Connection conn, FileContentMetadata fileContent) throws SQLException {
		String fileId = fileContent.getFileId();
		int partId = fileContent.getPartId();
		long checksum = fileContent.getChecksum();
		long dateModified = new Date().getTime();

		String updateSQL = "UPDATE " + getTableName() + " SET checksum=?, dateModified=? WHERE fileId=? AND partId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { checksum, dateModified, fileId, partId }, 1);
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
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteAll(Connection conn) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName();
		return DatabaseUtil.update(conn, deleteSQL, null, -1);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param partId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String fileId, int partId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE fileId=? AND partId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { fileId, partId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param partId
	 * @return
	 * @throws SQLException
	 */
	public InputStream getContent(Connection conn, String fileId, int partId) throws SQLException {
		InputStream inputStream = null;

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(this.database)) {
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			try {
				String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=? AND partId=?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, fileId);
				pstmt.setInt(2, partId);

				rs = pstmt.executeQuery();
				if (rs.next()) {
					inputStream = rs.getBinaryStream("fileContent");
				}
			} finally {
				DatabaseUtil.closeQuietly(rs, true);
				DatabaseUtil.closeQuietly(pstmt, true);
			}

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(this.database)) {
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			try {
				String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=? AND appVersion=?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, fileId);
				pstmt.setInt(2, partId);

				rs = pstmt.executeQuery();
				if (rs.next()) {
					byte[] bytes = rs.getBytes("fileContent");
					if (bytes != null) {
						inputStream = new ByteArrayInputStream(bytes);
					}
				}
			} finally {
				DatabaseUtil.closeQuietly(rs, true);
				DatabaseUtil.closeQuietly(pstmt, true);
			}

		} else {
			System.out.println(getClass().getName() + ".getContentInputStream() ### ### Unsupported database: '" + this.database + "'.");
		}

		return inputStream;
	}

	/**
	 * 
	 * @param conn
	 * @param fileId
	 * @param partId
	 * @param inputStream
	 * @return
	 * @throws SQLException
	 */
	public boolean setContent(Connection conn, String fileId, int partId, InputStream inputStream) throws SQLException {
		byte[] bytes = null;
		ByteArrayInputStream bais = null;
		try {
			bytes = IOUtil.toByteArray(inputStream);
			bais = new ByteArrayInputStream(bytes);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (bytes == null) {
			bytes = new byte[0];
		}
		int contentLength = bytes.length;

		try {
			if (DatabaseTableAware.MYSQL.equalsIgnoreCase(this.database)) {
				PreparedStatement pstmt = null;
				try {
					String updateSQL = "UPDATE " + getTableName() + " SET fileContent=? WHERE fileId=? AND partId=?";
					pstmt = conn.prepareStatement(updateSQL);

					pstmt.setBinaryStream(1, bais, contentLength);
					pstmt.setString(2, fileId);
					pstmt.setInt(3, partId);

					int updatedRowCount = pstmt.executeUpdate();
					if (updatedRowCount == 1) {
						return true;
					}
				} finally {
					DatabaseUtil.closeQuietly(pstmt, true);
				}

			} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(this.database)) {
				PreparedStatement pstmt = null;
				try {
					String updateSQL = "UPDATE " + getTableName() + " SET fileContent=? WHERE fileId=? AND partId=?";
					pstmt = conn.prepareStatement(updateSQL);

					pstmt.setBinaryStream(1, bais, contentLength); // need to specify length of the input stream.
					pstmt.setString(2, fileId);
					pstmt.setInt(3, partId);

					int updatedRowCount = pstmt.executeUpdate();
					if (updatedRowCount == 1) {
						return true;
					}
				} finally {
					DatabaseUtil.closeQuietly(pstmt, true);
				}
			}

		} finally {
			IOUtil.closeQuietly(bais, true);
		}
		return false;
	}

}

// sb.append(" startIndex int DEFAULT 0,");
// sb.append(" endIndex int DEFAULT -1,");

// sb.append(" startIndex int DEFAULT 0,");
// sb.append(" endIndex int DEFAULT -1,");

// long size = rs.getLong("size");
// int startIndex = rs.getInt("startIndex");
// int endIndex = rs.getInt("endIndex");
