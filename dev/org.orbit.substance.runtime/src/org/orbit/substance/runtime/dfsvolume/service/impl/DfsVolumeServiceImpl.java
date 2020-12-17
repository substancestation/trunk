package org.orbit.substance.runtime.dfsvolume.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.platform.sdk.http.AccessTokenProvider;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.orbit.substance.model.dfsvolume.PendingFile;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.SubstanceRuntimeActivator;
import org.orbit.substance.runtime.dfsvolume.service.DataBlockMetadata;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.orbit.substance.runtime.dfsvolume.service.FileContentMetadata;
import org.orbit.substance.runtime.util.DfsVolumeConfigPropertiesHandler;
import org.origin.common.event.PropertyChangeEvent;
import org.origin.common.event.PropertyChangeListener;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.DiskSpaceUnit;
import org.origin.common.util.IOUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DfsVolumeServiceImpl implements LifecycleAware, DfsVolumeService, PropertyChangeListener {

	protected Map<Object, Object> initProperties;
	protected Properties databaseProperties;
	protected String database;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies editPolicies;
	// protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected AccessTokenProvider accessTokenSupport;

	/**
	 * 
	 * @param initProperties
	 */
	public DfsVolumeServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = (initProperties != null) ? initProperties : new HashMap<Object, Object>();
		this.editPolicies = new ServiceEditPoliciesImpl(DfsVolumeService.class, this);
		this.accessTokenSupport = new AccessTokenProvider(SubstanceConstants.TOKEN_PROVIDER__ORBIT, OrbitRoles.DFS_VOLUME_ADMIN);
	}

	/** AccessTokenAware */
	@Override
	public String getAccessToken() {
		String tokenValue = this.accessTokenSupport.getAccessToken();
		return tokenValue;
	}

	@Override
	public Map<Object, Object> getInitProperties() {
		return this.initProperties;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		SubstanceRuntimeActivator.getInstance().getDfsVolumeConfigPropertiesHandler().addPropertyChangeListener(this);

		updateConnectionProperties();

		// Map<Object, Object> properties = new Hashtable<Object, Object>();
		// if (this.initProperties != null) {
		// properties.putAll(this.initProperties);
		// }
		//
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.ORBIT_HOST_URL);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__DFS_ID);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__ID);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__NAME);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__HOST_URL);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__JDBC_DRIVER);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__JDBC_URL);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__JDBC_USERNAME);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__VOLUME_CAPACITY_GB);
		// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__BLOCK_CAPACITY_MB);
		//
		// updateProperties(properties);
		//
		// String database = null;
		// try {
		// database = DatabaseUtil.getDatabase(this.databaseProperties);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// assert (database != null) : "database name cannot be retrieved.";

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

		SubstanceRuntimeActivator.getInstance().getDfsVolumeConfigPropertiesHandler().removePropertyChangeListener(this);
	}

	/** PropertyChangeListener */
	@Override
	public void notifyEvent(PropertyChangeEvent event) {
		String eventName = event.getName();
		if (SubstanceConstants.DFS_VOLUME__JDBC_DRIVER.equals(eventName) //
				|| SubstanceConstants.DFS_VOLUME__JDBC_URL.equals(eventName) //
				|| SubstanceConstants.DFS_VOLUME__JDBC_USERNAME.equals(eventName) //
				|| SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD.equals(eventName)) {
			updateConnectionProperties();
		}
	}

	// @Override
	// public Map<Object, Object> getProperties() {
	// return this.properties;
	// }
	//
	// /**
	// *
	// * @param configProps
	// */
	// public synchronized void updateProperties(Map<Object, Object> configProps) {
	// if (configProps == null) {
	// configProps = new HashMap<Object, Object>();
	// }
	//
	// String globalHostURL = (String) configProps.get(SubstanceConstants.ORBIT_HOST_URL);
	// String dfsId = (String) configProps.get(SubstanceConstants.DFS_VOLUME__DFS_ID);
	// String volumeId = (String) configProps.get(SubstanceConstants.DFS_VOLUME__ID);
	// String name = (String) configProps.get(SubstanceConstants.DFS_VOLUME__NAME);
	// String hostURL = (String) configProps.get(SubstanceConstants.DFS_VOLUME__HOST_URL);
	// String contextRoot = (String) configProps.get(SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT);
	// String jdbcDriver = (String) configProps.get(SubstanceConstants.DFS_VOLUME__JDBC_DRIVER);
	// String jdbcURL = (String) configProps.get(SubstanceConstants.DFS_VOLUME__JDBC_URL);
	// String jdbcUsername = (String) configProps.get(SubstanceConstants.DFS_VOLUME__JDBC_USERNAME);
	// String jdbcPassword = (String) configProps.get(SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD);
	// String volumeCapacity = (String) configProps.get(SubstanceConstants.DFS_VOLUME__VOLUME_CAPACITY_GB);
	// String blockCapacity = (String) configProps.get(SubstanceConstants.DFS_VOLUME__BLOCK_CAPACITY_MB);
	//
	// boolean printProps = false;
	// if (printProps) {
	// System.out.println();
	// System.out.println("Config properties:");
	// System.out.println("-----------------------------------------------------");
	// System.out.println(SubstanceConstants.ORBIT_HOST_URL + " = " + globalHostURL);
	// System.out.println(SubstanceConstants.DFS_VOLUME__DFS_ID + " = " + dfsId);
	// System.out.println(SubstanceConstants.DFS_VOLUME__ID + " = " + volumeId);
	// System.out.println(SubstanceConstants.DFS_VOLUME__NAME + " = " + name);
	// System.out.println(SubstanceConstants.DFS_VOLUME__HOST_URL + " = " + hostURL);
	// System.out.println(SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT + " = " + contextRoot);
	// System.out.println(SubstanceConstants.DFS_VOLUME__JDBC_DRIVER + " = " + jdbcDriver);
	// System.out.println(SubstanceConstants.DFS_VOLUME__JDBC_URL + " = " + jdbcURL);
	// System.out.println(SubstanceConstants.DFS_VOLUME__JDBC_USERNAME + " = " + jdbcUsername);
	// System.out.println(SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD + " = " + jdbcPassword);
	// System.out.println(SubstanceConstants.DFS_VOLUME__VOLUME_CAPACITY_GB + " = " + volumeCapacity);
	// System.out.println(SubstanceConstants.DFS_VOLUME__BLOCK_CAPACITY_MB + " = " + blockCapacity);
	// System.out.println("-----------------------------------------------------");
	// System.out.println();
	// }
	//
	// this.properties = configProps;
	// this.databaseProperties = getConnectionProperties(this.properties);
	// try {
	// this.database = DatabaseUtil.getDatabase(this.databaseProperties);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// /**
	// *
	// * @param props
	// * @return
	// */
	// protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
	// String driver = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__JDBC_DRIVER);
	// String url = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__JDBC_URL);
	// String username = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__JDBC_USERNAME);
	// String password = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD);
	// return DatabaseUtil.getProperties(driver, url, username, password);
	// }

	/** ConnectionAware */
	@Override
	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	protected synchronized void updateConnectionProperties() {
		DfsVolumeConfigPropertiesHandler configPropertiesHandler = SubstanceRuntimeActivator.getInstance().getDfsVolumeConfigPropertiesHandler();
		String driver = configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__JDBC_DRIVER, this.initProperties);
		String url = configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__JDBC_URL, this.initProperties);
		String username = configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__JDBC_USERNAME, this.initProperties);
		String password = configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD, this.initProperties);

		this.databaseProperties = DatabaseUtil.getProperties(driver, url, username, password);

		String database = null;
		try {
			database = DatabaseUtil.getDatabase(this.databaseProperties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assert (database != null) : "database name cannot be retrieved.";
		this.database = database;
	}

	protected String getDatabase() {
		return this.database;
	}

	/** WebServiceAware */
	@Override
	public String getName() {
		// return (String) this.properties.get(SubstanceConstants.DFS_VOLUME__NAME);
		DfsVolumeConfigPropertiesHandler configPropertiesHandler = SubstanceRuntimeActivator.getInstance().getDfsVolumeConfigPropertiesHandler();
		return configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__NAME, this.initProperties);
	}

	@Override
	public String getHostURL() {
		// String hostURL = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__HOST_URL);
		// if (hostURL != null) {
		// return hostURL;
		// }
		// String globalHostURL = (String) this.properties.get(SubstanceConstants.ORBIT_HOST_URL);
		// if (globalHostURL != null) {
		// return globalHostURL;
		// }
		DfsVolumeConfigPropertiesHandler configPropertiesHandler = SubstanceRuntimeActivator.getInstance().getDfsVolumeConfigPropertiesHandler();
		String hostURL = configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__HOST_URL, this.initProperties);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = configPropertiesHandler.getProperty(SubstanceConstants.ORBIT_HOST_URL, this.initProperties);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		// return (String) this.properties.get(SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT);
		DfsVolumeConfigPropertiesHandler configPropertiesHandler = SubstanceRuntimeActivator.getInstance().getDfsVolumeConfigPropertiesHandler();
		return configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT, this.initProperties);
	}

	/** EditPoliciesAware */
	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.editPolicies;
	}

	/** DfsVolumeService */
	@Override
	public String getDfsId() {
		// return (String) this.properties.get(SubstanceConstants.DFS_VOLUME__DFS_ID);
		DfsVolumeConfigPropertiesHandler configPropertiesHandler = SubstanceRuntimeActivator.getInstance().getDfsVolumeConfigPropertiesHandler();
		return configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__DFS_ID, this.initProperties);
	}

	@Override
	public String getDfsVolumeId() {
		// return (String) this.properties.get(SubstanceConstants.DFS_VOLUME__ID);
		DfsVolumeConfigPropertiesHandler configPropertiesHandler = SubstanceRuntimeActivator.getInstance().getDfsVolumeConfigPropertiesHandler();
		return configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__ID, this.initProperties);
	}

	// ----------------------------------------------------------------------
	// Methods about service
	// ----------------------------------------------------------------------
	@Override
	public long getVolumeCapacity() {
		long volumeCapacityBytes = 0;
		try {
			// String gbLiteral = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__VOLUME_CAPACITY_GB);
			DfsVolumeConfigPropertiesHandler configPropertiesHandler = SubstanceRuntimeActivator.getInstance().getDfsVolumeConfigPropertiesHandler();
			String gbLiteral = configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__VOLUME_CAPACITY_GB, this.initProperties);
			if (gbLiteral != null && !gbLiteral.isEmpty()) {
				int gbValue = Integer.parseInt(gbLiteral);
				if (gbValue > 0) {
					volumeCapacityBytes = DiskSpaceUnit.GB.toBytes(gbValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return volumeCapacityBytes;
	}

	@Override
	public synchronized long getVolumeSize() {
		// Need caching/update mechanism to avoid reading every time, especially when there are more than 10000 blocks.
		long volumeSizeBytes = 0;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			List<DataBlockMetadata> dataBlocks = blocksTableHandler.getList(conn);
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
		long capacityBytes = -1;
		try {
			// String capacityMBStr = (String) this.properties.get(SubstanceConstants.DFS_VOLUME__BLOCK_CAPACITY_MB);
			DfsVolumeConfigPropertiesHandler configPropertiesHandler = SubstanceRuntimeActivator.getInstance().getDfsVolumeConfigPropertiesHandler();
			String capacityMBStr = configPropertiesHandler.getProperty(SubstanceConstants.DFS_VOLUME__BLOCK_CAPACITY_MB, this.initProperties);
			if (capacityMBStr != null && !capacityMBStr.isEmpty()) {
				int capacityMB = Integer.parseInt(capacityMBStr);
				if (capacityMB > 0) {
					capacityBytes = DiskSpaceUnit.GB.toBytes(capacityMB);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return capacityBytes;
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

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			List<DataBlockMetadata> dataBlocks = blocksTableHandler.getList(conn);
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

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			List<DataBlockMetadata> dataBlocks = blocksTableHandler.getList(conn, accountId);
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

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			result = blocksTableHandler.get(conn, blockId, accountId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public boolean dataBlockExists(String accountId, String blockId) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			DataBlockMetadata dataBlock = blocksTableHandler.get(conn, blockId, accountId);
			if (dataBlock != null) {
				exists = true;
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean isDataBlockEmpty(String accountId, String blockId) throws ServerException {
		boolean isEmpty = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			DataBlockMetadata dataBlock = blocksTableHandler.get(conn, blockId, accountId);
			if (dataBlock == null) {
				// throw new IllegalStateException("Data block is not found. blockId='" + blockId + "'.");
				throw new ServerException(StatusDTO.RESP_404, "Data block is not found. blockId='" + blockId + "'.");
			}

			FileContentMetadata[] fileContents = getFileContents(accountId, blockId);
			if (fileContents == null || fileContents.length == 0) {
				isEmpty = true;
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isEmpty;
	}

	@Override
	public synchronized DataBlockMetadata createDataBlock(String accountId, long capacity, List<PendingFile> pendingFiles) throws ServerException {
		DataBlockMetadata newDataBlock = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			if (capacity <= 0) {
				capacity = getDefaultBlockCapacity();
			}
			if (capacity <= 0) {
				capacity = DiskSpaceUnit.MB.toBytes(100);
			}

			boolean isValid = false;
			String newBlockId = getNewBlockId(conn);
			if (newBlockId != null && !blocksTableHandler.exists(conn, newBlockId)) {
				isValid = true;
			}
			if (!isValid) {
				int retryCount = 0;
				while (retryCount <= 3) {
					newBlockId = getNewBlockId(conn);
					if (newBlockId != null && !blocksTableHandler.exists(conn, newBlockId)) {
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

			DataBlockMetadata dataBlock = blocksTableHandler.get(conn, newBlockId, accountId);
			if (dataBlock != null) {
				throw new ServerException(StatusDTO.RESP_500, "Data block already exists. blockId='" + newBlockId + "'.");
			}

			long dateCreated = System.currentTimeMillis();
			long dateModified = dateCreated;
			newDataBlock = blocksTableHandler.insert(conn, newBlockId, accountId, capacity, 0, pendingFiles, dateCreated, dateModified);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return newDataBlock;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected synchronized String getNewBlockId(Connection conn) throws SQLException {
		String newBlockId = null;

		String dfsId = getDfsId();
		String dfsVolumeId = getDfsVolumeId();
		VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

		int num = 1;
		while (true) {
			boolean exists = false;
			String newBlockIdCandidate = "block" + num;

			List<DataBlockMetadata> dataBlocks = blocksTableHandler.getList(conn);
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
	public boolean updateDataBlockSize(String accountId, String blockId, long newSize) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			DataBlockMetadata dataBlock = blocksTableHandler.get(conn, blockId, accountId);
			if (dataBlock == null) {
				throw new ServerException(StatusDTO.RESP_404, "Data block is not found. blockId='" + blockId + "'.");
			}

			int id = dataBlock.getId();
			isUpdated = blocksTableHandler.updateSize(conn, id, accountId, blockId, newSize);

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateDataBlockSizeByDelta(String accountId, String blockId, long sizeDelta) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			DataBlockMetadata dataBlock = blocksTableHandler.get(conn, blockId, accountId);
			if (dataBlock == null) {
				throw new ServerException(StatusDTO.RESP_404, "Data block is not found. blockId='" + blockId + "'.");
			}

			int id = dataBlock.getId();
			long newSize = dataBlock.getSize() + sizeDelta;

			isUpdated = blocksTableHandler.updateSize(conn, id, accountId, blockId, newSize);

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updatePendingFiles(String accountId, String blockId, String fileId, boolean cleanExpiredPendingFiles) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			DataBlockMetadata dataBlock = blocksTableHandler.get(conn, blockId, accountId);
			if (dataBlock == null) {
				throw new ServerException(StatusDTO.RESP_404, "Data block is not found. blockId='" + blockId + "'.");
			}

			List<PendingFile> pendingFilesToRemove = new ArrayList<PendingFile>();
			List<PendingFile> pendingFiles = dataBlock.getPendingFiles();

			for (PendingFile pendingFile : pendingFiles) {
				String currFileId = pendingFile.getFileId();
				if (fileId.equals(currFileId)) {
					pendingFilesToRemove.add(pendingFile);
				} else {
					if (cleanExpiredPendingFiles) {
						// Note:
						// - PendingFile item expires in 120 seconds.
						// - After certain space is reserved in a data block, it is expected that the file content can be uploaded within 120 seconds.long
						if (pendingFile.isExpired()) {
							pendingFilesToRemove.add(pendingFile);
						}
					}
				}
			}
			if (!pendingFilesToRemove.isEmpty()) {
				dataBlock.getPendingFiles().removeAll(pendingFilesToRemove);
			}

			int id = dataBlock.getId();
			isUpdated = blocksTableHandler.updatePendingFiles(conn, id, blockId, accountId, pendingFiles);

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

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeBlocksTableHandler blocksTableHandler = VolumeBlocksTableHandler.getInstance(conn, dfsId, dfsVolumeId);

			DataBlockMetadata dataBlock = blocksTableHandler.get(conn, blockId, accountId);
			if (dataBlock == null) {
				throw new ServerException(StatusDTO.RESP_404, "Data block is not found. blockId='" + blockId + "'.");
			}

			int id = dataBlock.getId();
			isDeleted = blocksTableHandler.delete(conn, id);

			if (isDeleted) {
				FileContentMetadata[] fileContents = getFileContents(accountId, blockId);
				if (fileContents != null && fileContents.length > 0) {
					// Delete all records from the <dfsId>_<dfsVolumeId>_<blockId> table, which are the file contents that belong to the data block.
					deleteFileContents(accountId, blockId);
					// Drop the <dfsId>_<dfsVolumeId>_<blockId> table as well, since the data block doesn't exist any more.
					VolumeFileContentTableHandler.dispose(conn, dfsId, dfsVolumeId, blockId);
				}
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
	public FileContentMetadata[] getFileContents(String accountId, String blockId) throws ServerException {
		FileContentMetadata[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeFileContentTableHandler fileContentTableHandler = VolumeFileContentTableHandler.getInstance(conn, dfsId, dfsVolumeId, blockId);

			List<FileContentMetadata> fileContents = fileContentTableHandler.getList(conn);
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
	public FileContentMetadata getFileContent(String accountId, String blockId, String fileId, int partId) throws ServerException {
		FileContentMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeFileContentTableHandler fileContentTableHandler = VolumeFileContentTableHandler.getInstance(conn, dfsId, dfsVolumeId, blockId);

			result = fileContentTableHandler.get(conn, fileId, partId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public FileContentMetadata createFileContent(String accountId, String blockId, String fileId, int partId, long size, long checksum) throws ServerException {
		FileContentMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeFileContentTableHandler fileContentTableHandler = VolumeFileContentTableHandler.getInstance(conn, dfsId, dfsVolumeId, blockId);

			FileContentMetadata existingFileContent = fileContentTableHandler.get(conn, fileId, partId);
			if (existingFileContent != null) {
				throw new ServerException(StatusDTO.RESP_500, "File content metadata already exists.");
			}

			long dateCreated = System.currentTimeMillis();
			long dateModified = dateCreated;
			result = fileContentTableHandler.insert(conn, fileId, partId, size, checksum, dateCreated, dateModified);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public boolean updateFileContent(String accountId, String blockId, FileContentMetadata fileContentMetadata) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeFileContentTableHandler fileContentTableHandler = VolumeFileContentTableHandler.getInstance(conn, dfsId, dfsVolumeId, blockId);

			String fileId = fileContentMetadata.getFileId();
			int partId = fileContentMetadata.getPartId();

			FileContentMetadata existingFileContent = fileContentTableHandler.get(conn, fileId, partId);
			if (existingFileContent == null) {
				throw new ServerException(StatusDTO.RESP_500, "File content metadata is not found.");
			}

			isUpdated = fileContentTableHandler.update(conn, fileContentMetadata);

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

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeFileContentTableHandler fileContentTableHandler = VolumeFileContentTableHandler.getInstance(conn, dfsId, dfsVolumeId, blockId);

			FileContentMetadata fileContent = fileContentTableHandler.get(conn, fileId, partId);
			if (fileContent != null) {
				int id = fileContent.getId();
				isDeleted = fileContentTableHandler.delete(conn, id);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteFileContents(String accountId, String blockId) throws ServerException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeFileContentTableHandler fileContentTableHandler = VolumeFileContentTableHandler.getInstance(conn, dfsId, dfsVolumeId, blockId);

			isDeleted = fileContentTableHandler.deleteAll(conn);

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
	public InputStream getContent(String accountId, String blockId, String fileId, int partId) throws ServerException {
		InputStream inputStream = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeFileContentTableHandler fileContentTableHandler = VolumeFileContentTableHandler.getInstance(conn, dfsId, dfsVolumeId, blockId);

			fileContentTableHandler.setDatabase(this.database);
			inputStream = fileContentTableHandler.getContent(conn, fileId, partId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return inputStream;
	}

	@Override
	public void getContent(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ServerException {
		InputStream input = null;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeFileContentTableHandler fileContentTableHandler = VolumeFileContentTableHandler.getInstance(conn, dfsId, dfsVolumeId, blockId);

			fileContentTableHandler.setDatabase(this.database);
			input = fileContentTableHandler.getContent(conn, fileId, partId);
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
	public boolean setContent(String accountId, String blockId, String fileId, int partId, InputStream inputStream) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();

			String dfsId = getDfsId();
			String dfsVolumeId = getDfsVolumeId();
			VolumeFileContentTableHandler fileContentTableHandler = VolumeFileContentTableHandler.getInstance(conn, dfsId, dfsVolumeId, blockId);

			fileContentTableHandler.setDatabase(this.database);
			FileContentMetadata fileContent = fileContentTableHandler.get(conn, fileId, partId);
			if (fileContent == null) {
				throw new ServerException(StatusDTO.RESP_500, "File content metadata is not found.");
			}

			isUpdated = fileContentTableHandler.setContent(conn, fileId, partId, inputStream);

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
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

// boolean setFileContent(String accountId, String blockId, String fileId, int partId, InputStream input, FileContentMetadata fileContentMetadata) throws
// ServerException;
// @Override
// public boolean setFileContent(String accountId, String blockId, String fileId, int partId, InputStream inputStream, FileContentMetadata
// fileContentToUpdate) throws ServerException {
// boolean isUpdated = false;
// Connection conn = null;
// try {
// conn = getConnection();
//
// String dfsVolumeId = getVolumeId();
// VolumeFileContentTableHandler tableHandler = VolumeFileContentTableHandler.getInstance(conn, dfsVolumeId, blockId);
// tableHandler.setDatabase(this.database);
//
// FileContentMetadata fileContent = tableHandler.get(conn, fileId, partId);
// if (fileContent == null) {
// throw new ServerException(StatusDTO.RESP_500, "File content metadata is not found.");
// }
//
// boolean isContentUpdated = tableHandler.setContent(conn, fileId, partId, inputStream);
// boolean isMetadataUpdated = false;
// if (isContentUpdated) {
// isMetadataUpdated = tableHandler.update(conn, fileContentToUpdate);
// }
//
// if (isContentUpdated && isMetadataUpdated) {
// isUpdated = true;
// }
//
// } catch (SQLException e) {
// handleSQLException(e);
//
// } finally {
// DatabaseUtil.closeQuietly(conn, true);
// }
// return isUpdated;
// }
