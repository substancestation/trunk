package org.orbit.substance.runtime.dfs.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.orbit.infra.api.InfraConstants;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.DiskSpaceUnit;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DfsServiceImpl implements DfsService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;
	protected Map<String, FileSystem> accountIdToFileSystemMap = new HashMap<String, FileSystem>();

	/**
	 * 
	 * @param initProperties
	 */
	public DfsServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new ServiceEditPoliciesImpl(DfsService.class, this);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__ID);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__NAME);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__JDBC_PASSWORD);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__BLOCK_CAPACITY_MB);

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

		initialize();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(DfsService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		for (Iterator<String> accountIdItor = this.accountIdToFileSystemMap.keySet().iterator(); accountIdItor.hasNext();) {
			String accountId = accountIdItor.next();
			FileSystem fileSystem = this.accountIdToFileSystemMap.get(accountId);
			fileSystem.dispose();
		}
		this.accountIdToFileSystemMap.clear();
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	/**
	 * 
	 * @param configProps
	 */
	public synchronized void updateProperties(Map<Object, Object> configProps) {
		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String indexServiceUrl = (String) configProps.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String globalHostURL = (String) configProps.get(SubstanceConstants.ORBIT_HOST_URL);
		String id = (String) configProps.get(SubstanceConstants.DFS__ID);
		String name = (String) configProps.get(SubstanceConstants.DFS__NAME);
		String hostURL = (String) configProps.get(SubstanceConstants.DFS__HOST_URL);
		String contextRoot = (String) configProps.get(SubstanceConstants.DFS__CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(SubstanceConstants.DFS__JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(SubstanceConstants.DFS__JDBC_URL);
		String jdbcUsername = (String) configProps.get(SubstanceConstants.DFS__JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(SubstanceConstants.DFS__JDBC_PASSWORD);
		String blockCapacityMB = (String) configProps.get(SubstanceConstants.DFS__BLOCK_CAPACITY_MB);

		boolean printProps = false;
		if (printProps) {
			System.out.println();
			System.out.println("Config properties:");
			System.out.println("-----------------------------------------------------");
			System.out.println(InfraConstants.ORBIT_INDEX_SERVICE_URL + " = " + indexServiceUrl);
			System.out.println(SubstanceConstants.ORBIT_HOST_URL + " = " + globalHostURL);
			System.out.println(SubstanceConstants.DFS__ID + " = " + id);
			System.out.println(SubstanceConstants.DFS__NAME + " = " + name);
			System.out.println(SubstanceConstants.DFS__HOST_URL + " = " + hostURL);
			System.out.println(SubstanceConstants.DFS__CONTEXT_ROOT + " = " + contextRoot);
			System.out.println(SubstanceConstants.DFS__JDBC_DRIVER + " = " + jdbcDriver);
			System.out.println(SubstanceConstants.DFS__JDBC_URL + " = " + jdbcURL);
			System.out.println(SubstanceConstants.DFS__JDBC_USERNAME + " = " + jdbcUsername);
			System.out.println(SubstanceConstants.DFS__JDBC_PASSWORD + " = " + jdbcPassword);
			System.out.println(SubstanceConstants.DFS__BLOCK_CAPACITY_MB + " = " + blockCapacityMB);
			System.out.println("-----------------------------------------------------");
			System.out.println();
		}

		this.properties = configProps;
		this.databaseProperties = getConnectionProperties(this.properties);
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
		String driver = (String) this.properties.get(SubstanceConstants.DFS__JDBC_DRIVER);
		String url = (String) this.properties.get(SubstanceConstants.DFS__JDBC_URL);
		String username = (String) this.properties.get(SubstanceConstants.DFS__JDBC_USERNAME);
		String password = (String) this.properties.get(SubstanceConstants.DFS__JDBC_PASSWORD);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/**
	 * Initialize database tables.
	 */
	public void initialize() {
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection(this.databaseProperties);

			// if (this.categoryTableHandler != null) {
			// DatabaseUtil.initialize(conn, this.categoryTableHandler);
			// }
			// if (this.appTableHandler != null) {
			// DatabaseUtil.initialize(conn, this.appTableHandler);
			// }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	@Override
	public String getDfsId() {
		String dfsId = (String) this.properties.get(SubstanceConstants.DFS__ID);
		return dfsId;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(SubstanceConstants.DFS__NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(SubstanceConstants.DFS__HOST_URL);
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
		String contextRoot = (String) this.properties.get(SubstanceConstants.DFS__CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	@Override
	public long getDefaultBlockCapacity() {
		long blockCapacityBytes = -1;
		try {
			String blockCapacityMBStr = (String) this.properties.get(SubstanceConstants.DFS__BLOCK_CAPACITY_MB);
			if (blockCapacityMBStr != null && !blockCapacityMBStr.isEmpty()) {
				int blockCapacityMB = Integer.parseInt(blockCapacityMBStr);
				if (blockCapacityMB > 0) {
					// 1MB = 1024KB
					// 1KB = 1024B
					// blockCapacityBytes = blockCapacityMB * 1024 * 1024;
					blockCapacityBytes = DiskSpaceUnit.GB.toBytes(blockCapacityMB);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// if (blockCapacityBytes <= 0) {
		// blockCapacityBytes = 100 * 1024 * 1024; // 100MB
		// }
		return blockCapacityBytes;
	}

	@Override
	public synchronized FileSystem getFileSystem(String accountId) {
		if (accountId == null || accountId.isEmpty()) {
			throw new IllegalArgumentException("accountId is null.");
		}

		FileSystem fileSystem = this.accountIdToFileSystemMap.get(accountId);
		if (fileSystem == null) {
			fileSystem = new FileSystemImpl(this, accountId);
			this.accountIdToFileSystemMap.put(accountId, fileSystem);
		}
		return fileSystem;
	}

}
