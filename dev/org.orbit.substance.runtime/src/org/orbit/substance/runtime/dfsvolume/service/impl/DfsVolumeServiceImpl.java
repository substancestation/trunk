package org.orbit.substance.runtime.dfsvolume.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.orbit.substance.runtime.model.dfsvolume.DataBlockMetadata;
import org.orbit.substance.runtime.model.dfsvolume.FileContentMetadata;
import org.origin.common.io.IOUtil;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.DiskSpaceUnit;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DfsVolumeServiceImpl implements DfsVolumeService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected String database;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;

	/**
	 * 
	 * @param initProperties
	 */
	public DfsVolumeServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new ServiceEditPoliciesImpl(DfsVolumeService.class, this);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__DFS_ID);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__ID);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__VOLUME_CAPACITY_GB);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__BLOCK_CAPACITY_MB);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__NAME);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD);

		updateProperties(properties);

		String database = null;
		try {
			database = DatabaseUtil.getDatabase(this.databaseProperties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assert (database != null) : "database name cannot be retrieved.";

		// this.categoryTableHandler = AppCategoryTableHandler.INSTANCE;
		// this.appTableHandler = new AppMetadataTableHandler(database);
		// initialize();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(DfsVolumeService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	/**
	 * 
	 * @param configProps
	 */
	public synchronized void updateProperties(Map<Object, Object> configProps) {
		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String globalHostURL = (String) configProps.get(SubstanceConstants.ORBIT_HOST_URL);
		String dfsId = (String) configProps.get(SubstanceConstants.DFS_VOLUME__DFS_ID);
		String volumeId = (String) configProps.get(SubstanceConstants.DFS_VOLUME__ID);
		String volumeCapacity = (String) configProps.get(SubstanceConstants.DFS_VOLUME__VOLUME_CAPACITY_GB);
		String blockCapacity = (String) configProps.get(SubstanceConstants.DFS_VOLUME__BLOCK_CAPACITY_MB);
		String name = (String) configProps.get(SubstanceConstants.DFS_VOLUME__NAME);
		String hostURL = (String) configProps.get(SubstanceConstants.DFS_VOLUME__HOST_URL);
		String contextRoot = (String) configProps.get(SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(SubstanceConstants.DFS_VOLUME__JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(SubstanceConstants.DFS_VOLUME__JDBC_URL);
		String jdbcUsername = (String) configProps.get(SubstanceConstants.DFS_VOLUME__JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD);

		boolean printProps = false;
		if (printProps) {
			System.out.println();
			System.out.println("Config properties:");
			System.out.println("-----------------------------------------------------");
			System.out.println(SubstanceConstants.ORBIT_HOST_URL + " = " + globalHostURL);
			System.out.println(SubstanceConstants.DFS_VOLUME__DFS_ID + " = " + dfsId);
			System.out.println(SubstanceConstants.DFS_VOLUME__ID + " = " + volumeId);
			System.out.println(SubstanceConstants.DFS_VOLUME__VOLUME_CAPACITY_GB + " = " + volumeCapacity);
			System.out.println(SubstanceConstants.DFS_VOLUME__BLOCK_CAPACITY_MB + " = " + blockCapacity);
			System.out.println(SubstanceConstants.DFS_VOLUME__NAME + " = " + name);
			System.out.println(SubstanceConstants.DFS_VOLUME__HOST_URL + " = " + hostURL);
			System.out.println(SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT + " = " + contextRoot);
			System.out.println(SubstanceConstants.DFS_VOLUME__JDBC_DRIVER + " = " + jdbcDriver);
			System.out.println(SubstanceConstants.DFS_VOLUME__JDBC_URL + " = " + jdbcURL);
			System.out.println(SubstanceConstants.DFS_VOLUME__JDBC_USERNAME + " = " + jdbcUsername);
			System.out.println(SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD + " = " + jdbcPassword);
			System.out.println("-----------------------------------------------------");
			System.out.println();
		}

		this.properties = configProps;
		this.databaseProperties = getConnectionProperties(this.properties);
		try {
			this.database = DatabaseUtil.getDatabase(this.databaseProperties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
		String driver = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__JDBC_DRIVER);
		String url = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__JDBC_URL);
		String username = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__JDBC_USERNAME);
		String password = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	protected String getDatabase() {
		return this.database;
	}

	@Override
	public String getDfsId() {
		String dfsId = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__DFS_ID);
		return dfsId;
	}

	@Override
	public String getVolumeId() {
		String dfsVolumeId = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__ID);
		return dfsVolumeId;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(SubstanceConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleSQLException(SQLException e) throws ServerException {
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleIOException(IOException e) throws ServerException {
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	// ----------------------------------------------------------------------
	// Methods about service
	// ----------------------------------------------------------------------
	@Override
	public long getVolumeCapacity() {
		long volumeCapacityBytes = 0;
		try {
			String gbLiteral = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__VOLUME_CAPACITY_GB);
			if (gbLiteral != null && !gbLiteral.isEmpty()) {
				int gbValue = Integer.parseInt(gbLiteral);
				if (gbValue > 0) {
					// volumeCapacityBytes = volumeCapacityGB * 1024 * 1024 * 1024;
					volumeCapacityBytes = DiskSpaceUnit.GB.toBytes(gbValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// if (volumeCapacityBytes <= 0) {
		// throw new ServerException(StatusDTO.RESP_500, "Cannot get volume capacity.");
		// }
		return volumeCapacityBytes;
	}

	@Override
	public synchronized long getVolumeSize() {
		// Need caching/update mechanism to avoid reading every time, especially when there are more than 10000 blocks.
		long volumeSizeBytes = 0;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			List<DataBlockMetadata> dataBlocks = VolumeBlocksTableHandler.getInstance(conn, dfsVolumeId).getList(conn);
			for (DataBlockMetadata dataBlock : dataBlocks) {
				long size = dataBlock.getSize();
				volumeSizeBytes += size;
			}

		} catch (SQLException e) {
			// handleSQLException(e);
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return volumeSizeBytes;
	}

	@Override
	public long getDefaultBlockCapacity() {
		long blockCapacityBytes = 0;
		try {
			String blockCapacityMBStr = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__BLOCK_CAPACITY_MB);
			if (blockCapacityMBStr != null && !blockCapacityMBStr.isEmpty()) {
				int blockCapacityMB = Integer.parseInt(blockCapacityMBStr);
				if (blockCapacityMB > 0) {
					// 1MB = 1024KB
					// 1KB = 1024B
					blockCapacityBytes = blockCapacityMB * 1024 * 1024;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (blockCapacityBytes <= 0) {
			blockCapacityBytes = 100 * 1024 * 1024; // 100MB
		}
		return blockCapacityBytes;
	}

	// ----------------------------------------------------------------------
	// Methods for accessing data blocks
	// ----------------------------------------------------------------------
	public static DataBlockMetadata[] EMPTY_DATA_BLOCKS = new DataBlockMetadata[0];

	@Override
	public DataBlockMetadata[] getDataBlocks() throws ServerException {
		DataBlockMetadata[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			List<DataBlockMetadata> dataBlocks = VolumeBlocksTableHandler.getInstance(conn, dfsVolumeId).getList(conn);
			if (dataBlocks != null) {
				result = dataBlocks.toArray(new DataBlockMetadata[dataBlocks.size()]);
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_DATA_BLOCKS;
		}
		return result;
	}

	@Override
	public DataBlockMetadata[] getDataBlocks(String accountId) throws ServerException {
		DataBlockMetadata[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			List<DataBlockMetadata> dataBlocks = VolumeBlocksTableHandler.getInstance(conn, dfsVolumeId).getList(conn, accountId);
			if (dataBlocks != null) {
				result = dataBlocks.toArray(new DataBlockMetadata[dataBlocks.size()]);
			}
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_DATA_BLOCKS;
		}
		return result;
	}

	@Override
	public DataBlockMetadata getDataBlock(String accountId, String blockId) throws ServerException {
		DataBlockMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			result = VolumeBlocksTableHandler.getInstance(conn, dfsVolumeId).get(conn, blockId, accountId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public synchronized DataBlockMetadata createDataBlock(String accountId, long capacity) throws ServerException {
		DataBlockMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			String dfsVolumeId = getVolumeId();

			VolumeBlocksTableHandler tableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsVolumeId);

			if (capacity <= 0) {
				capacity = getDefaultBlockCapacity();
			}

			boolean isValid = false;
			String newBlockId = getNewBlockId(conn);
			if (newBlockId != null && !tableHandler.exists(conn, newBlockId)) {
				isValid = true;
			}
			if (!isValid) {
				int retryCount = 0;
				while (retryCount <= 3) {
					newBlockId = getNewBlockId(conn);
					if (newBlockId != null && !tableHandler.exists(conn, newBlockId)) {
						isValid = true;
						break;
					}
					retryCount++;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if (!isValid) {
				throw new ServerException(StatusDTO.RESP_500, "Cannot get new block id.");
			}

			DataBlockMetadata existingDataBlock = tableHandler.get(conn, newBlockId, accountId);
			if (existingDataBlock != null) {
				throw new ServerException(StatusDTO.RESP_500, "Data block metadata already exists.");
			}

			long dateCreated = System.currentTimeMillis();
			long dateModified = dateCreated;

			result = VolumeBlocksTableHandler.getInstance(conn, dfsVolumeId).insert(conn, newBlockId, accountId, capacity, 0, dateCreated, dateModified);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected synchronized String getNewBlockId(Connection conn) throws SQLException {
		String newBlockId = null;

		String dfsVolumeId = getVolumeId();

		int num = 1;
		while (true) {
			boolean exists = false;
			String newBlockIdCandidate = "block" + num;

			List<DataBlockMetadata> dataBlocks = VolumeBlocksTableHandler.getInstance(conn, dfsVolumeId).getList(conn);
			for (DataBlockMetadata dataBlock : dataBlocks) {
				if (newBlockIdCandidate.equals(dataBlock.getBlockId())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				newBlockId = newBlockIdCandidate;
				break;
			}
			num++;
		}

		return newBlockId;
	}

	@Override
	public boolean updateDataBlock(String accountId, DataBlockMetadata datablockMetadata) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			VolumeBlocksTableHandler tableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsVolumeId);

			String blockId = datablockMetadata.getBlockId();
			DataBlockMetadata existingDataBlock = tableHandler.get(conn, blockId, accountId);
			if (existingDataBlock == null) {
				throw new ServerException(StatusDTO.RESP_500, "Data block is not found.");
			}

			isUpdated = tableHandler.update(conn, datablockMetadata);

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteDataBlock(String accountId, String blockId) throws ServerException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			DataBlockMetadata dataBlock = VolumeBlocksTableHandler.getInstance(conn, dfsVolumeId).get(conn, blockId, accountId);
			if (dataBlock != null) {
				int id = dataBlock.getId();
				isDeleted = VolumeBlocksTableHandler.getInstance(conn, dfsVolumeId).delete(conn, id);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	// ----------------------------------------------------------------------
	// Methods for accessing file contents of a data block
	// ----------------------------------------------------------------------
	public static FileContentMetadata[] EMPTY_FILE_CONTENTS = new FileContentMetadata[0];

	@Override
	public FileContentMetadata[] getFileContentMetadatas(String accountId, String blockId) throws ServerException {
		FileContentMetadata[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			List<FileContentMetadata> fileContents = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId).getList(conn);
			if (fileContents != null) {
				result = fileContents.toArray(new FileContentMetadata[fileContents.size()]);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_FILE_CONTENTS;
		}
		return result;
	}

	@Override
	public FileContentMetadata getFileContentMetadata(String accountId, String blockId, String fileId, int partId) throws ServerException {
		FileContentMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			result = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId).get(conn, fileId, partId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public FileContentMetadata createFileContentMetadata(String accountId, String blockId, String fileId, int partId, long size, int startIndex, int endIndex, String checksum) throws ServerException {
		FileContentMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			FileContentMetadata existingFileContent = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId).get(conn, fileId, partId);
			if (existingFileContent != null) {
				throw new ServerException(StatusDTO.RESP_500, "File content metadata already exists.");
			}

			long dateCreated = System.currentTimeMillis();
			long dateModified = dateCreated;

			result = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId).insert(conn, fileId, partId, size, startIndex, endIndex, checksum, dateCreated, dateModified);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public boolean updateFileContentMetadata(String accountId, String blockId, FileContentMetadata fileContentMetadata) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			VolumeBlockTableHandler tableHandler = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId);

			String fileId = fileContentMetadata.getFileId();
			int partId = fileContentMetadata.getPartId();

			FileContentMetadata existingFileContent = tableHandler.get(conn, fileId, partId);
			if (existingFileContent == null) {
				throw new ServerException(StatusDTO.RESP_500, "File content metadata is not found.");
			}

			isUpdated = tableHandler.update(conn, fileContentMetadata);

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteFileContent(String accountId, String blockId, String fileId, int partId) throws ServerException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			FileContentMetadata fileContent = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId).get(conn, fileId, partId);
			if (fileContent != null) {
				int id = fileContent.getId();
				isDeleted = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId).delete(conn, id);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	// ----------------------------------------------------------------------
	// Methods for getting/setting file contents
	// ----------------------------------------------------------------------
	@Override
	public InputStream getContentInputStream(String accountId, String blockId, String fileId, int partId) throws ServerException {
		InputStream input = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			VolumeBlockTableHandler tableHandler = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId);
			tableHandler.setDatabase(this.database);

			input = tableHandler.getContentInputStream(conn, fileId, partId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return input;
	}

	@Override
	public void getContent(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ServerException {
		InputStream input = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			VolumeBlockTableHandler tableHandler = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId);
			tableHandler.setDatabase(this.database);

			input = tableHandler.getContentInputStream(conn, fileId, partId);
			if (input != null) {
				IOUtil.copy(input, output);
			}

		} catch (SQLException e) {
			handleSQLException(e);

		} catch (IOException e) {
			handleIOException(e);

		} finally {
			IOUtil.closeQuietly(input, true);
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	@Override
	public boolean setContent(String accountId, String blockId, String fileId, int partId, InputStream input) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			VolumeBlockTableHandler tableHandler = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId);
			tableHandler.setDatabase(this.database);

			FileContentMetadata existingFileContent = tableHandler.get(conn, fileId, partId);
			if (existingFileContent == null) {
				throw new ServerException(StatusDTO.RESP_500, "File content metadata is not found.");
			}

			isUpdated = tableHandler.setContent(conn, fileId, partId, input);

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean setContent(String accountId, String blockId, String fileId, int partId, InputStream input, FileContentMetadata fileContentMetadata) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsVolumeId = getVolumeId();
			VolumeBlockTableHandler tableHandler = VolumeBlockTableHandler.getInstance(conn, dfsVolumeId, blockId);
			tableHandler.setDatabase(this.database);

			FileContentMetadata existingFileContent = tableHandler.get(conn, fileId, partId);
			if (existingFileContent == null) {
				throw new ServerException(StatusDTO.RESP_500, "File content metadata is not found.");
			}

			boolean isContentUpdated = tableHandler.setContent(conn, fileId, partId, input);
			boolean isMetadataUpdated = false;
			if (isContentUpdated) {
				isMetadataUpdated = tableHandler.update(conn, fileContentMetadata);
			}

			if (isContentUpdated && isMetadataUpdated) {
				isUpdated = true;
			}

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

}

// /**
// * Initialize database tables.
// */
// public void initialize() {
// Connection conn = null;
// try {
// conn = DatabaseUtil.getConnection(this.databaseProperties);
//
// if (this.categoryTableHandler != null) {
// DatabaseUtil.initialize(conn, this.categoryTableHandler);
// }
// if (this.appTableHandler != null) {
// DatabaseUtil.initialize(conn, this.appTableHandler);
// }
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// DatabaseUtil.closeQuietly(conn, true);
// }
// }

// if (result == null) {
// List<FileContentMetadata> fileConatents = DFSVolumeBlockContentTableHandler.getInstance(conn, dfsVolumeId, blockId).getList(conn, fileId);
// if (fileConatents != null) {
// for (FileContentMetadata currFileConatent : fileConatents) {
// int currPartId = currFileConatent.getPartId();
// if (partId == currPartId) {
// result = currFileConatent;
// break;
// }
// }
// }
// }

// if (fileContent == null) {
// List<FileContentMetadata> fileConatents = DFSVolumeBlockContentTableHandler.getInstance(conn, dfsVolumeId, blockId).getList(conn, fileId);
// if (fileConatents != null) {
// for (FileContentMetadata currFileConatent : fileConatents) {
// int currPartId = currFileConatent.getPartId();
// if (partId == currPartId) {
// fileContent = currFileConatent;
// break;
// }
// }
// }
// }
