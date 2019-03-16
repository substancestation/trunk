package org.orbit.substance.runtime.dfs.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.orbit.platform.sdk.http.AccessTokenSupport;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.util.DfsConfigPropertiesHandler;
import org.origin.common.event.PropertyChangeEvent;
import org.origin.common.event.PropertyChangeListener;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.DiskSpaceUnit;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DfsServiceImpl implements LifecycleAware, DfsService, PropertyChangeListener {

	protected Map<Object, Object> initProperties;
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;
	protected Map<String, FileSystem> accountIdToFileSystemMap = new HashMap<String, FileSystem>();
	protected AccessTokenSupport accessTokenSupport;

	/**
	 * 
	 * @param initProperties
	 */
	public DfsServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = (initProperties != null) ? initProperties : new HashMap<Object, Object>();
		this.wsEditPolicies = new ServiceEditPoliciesImpl(DfsService.class, this);
		this.accessTokenSupport = new AccessTokenSupport(SubstanceConstants.TOKEN_PROVIDER__ORBIT, OrbitRoles.DFS_ADMIN);
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
		DfsConfigPropertiesHandler.getInstance().addPropertyChangeListener(this);

		updateConnectionProperties();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(DfsService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		this.accountIdToFileSystemMap.clear();

		DfsConfigPropertiesHandler.getInstance().removePropertyChangeListener(this);
	}

	/** PropertyChangeListener */
	@Override
	public void notifyEvent(PropertyChangeEvent event) {
		String eventName = event.getName();
		if (SubstanceConstants.DFS__JDBC_DRIVER.equals(eventName) //
				|| SubstanceConstants.DFS__JDBC_URL.equals(eventName) //
				|| SubstanceConstants.DFS__JDBC_USERNAME.equals(eventName) //
				|| SubstanceConstants.DFS__JDBC_PASSWORD.equals(eventName)) {
			updateConnectionProperties();
		}
	}

	/** ConnectionAware */
	@Override
	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	protected synchronized void updateConnectionProperties() {
		DfsConfigPropertiesHandler configPropertiesHandler = DfsConfigPropertiesHandler.getInstance();
		String driver = configPropertiesHandler.getProperty(SubstanceConstants.DFS__JDBC_DRIVER, this.initProperties);
		String url = configPropertiesHandler.getProperty(SubstanceConstants.DFS__JDBC_URL, this.initProperties);
		String username = configPropertiesHandler.getProperty(SubstanceConstants.DFS__JDBC_USERNAME, this.initProperties);
		String password = configPropertiesHandler.getProperty(SubstanceConstants.DFS__JDBC_PASSWORD, this.initProperties);

		this.databaseProperties = DatabaseUtil.getProperties(driver, url, username, password);
	}

	/** WebServiceAware */
	@Override
	public String getName() {
		// return (String) this.properties.get(SubstanceConstants.DFS__NAME);
		return DfsConfigPropertiesHandler.getInstance().getProperty(SubstanceConstants.DFS__NAME, this.initProperties);
	}

	@Override
	public String getHostURL() {
		// String hostURL = (String) this.properties.get(SubstanceConstants.DFS__HOST_URL);
		// if (hostURL != null) {
		// return hostURL;
		// }
		// String globalHostURL = (String) this.properties.get(SubstanceConstants.ORBIT_HOST_URL);
		// if (globalHostURL != null) {
		// return globalHostURL;
		// }
		String hostURL = DfsConfigPropertiesHandler.getInstance().getProperty(SubstanceConstants.DFS__HOST_URL, this.initProperties);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = DfsConfigPropertiesHandler.getInstance().getProperty(SubstanceConstants.ORBIT_HOST_URL, this.initProperties);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		// return (String) this.properties.get(SubstanceConstants.DFS__CONTEXT_ROOT);
		return DfsConfigPropertiesHandler.getInstance().getProperty(SubstanceConstants.DFS__CONTEXT_ROOT, this.initProperties);
	}

	/** EditPoliciesAware */
	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	/** DfsService */
	@Override
	public String getDfsId() {
		// return (String) this.properties.get(SubstanceConstants.DFS__ID);
		return DfsConfigPropertiesHandler.getInstance().getProperty(SubstanceConstants.DFS__ID, this.initProperties);
	}

	@Override
	public long getDefaultBlockCapacity() {
		long blockCapacityBytes = -1;
		try {
			// String blockCapacityMBStr = (String) this.properties.get(SubstanceConstants.DFS__BLOCK_CAPACITY_MB);
			String blockCapacityMBStr = DfsConfigPropertiesHandler.getInstance().getProperty(SubstanceConstants.DFS__BLOCK_CAPACITY_MB, this.initProperties);
			if (blockCapacityMBStr != null && !blockCapacityMBStr.isEmpty()) {
				int blockCapacityMB = Integer.parseInt(blockCapacityMBStr);
				if (blockCapacityMB > 0) {
					blockCapacityBytes = DiskSpaceUnit.MB.toBytes(blockCapacityMB);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

// 1MB = 1024KB
// 1KB = 1024B
// blockCapacityBytes = blockCapacityMB * 1024 * 1024;
// if (blockCapacityBytes <= 0) {
// blockCapacityBytes = 100 * 1024 * 1024; // 100MB
// }

// protected Map<Object, Object> properties = new HashMap<Object, Object>();

// Map<Object, Object> properties = new Hashtable<Object, Object>();
// if (this.initProperties != null) {
// properties.putAll(this.initProperties);
// }
//
// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.ORBIT_HOST_URL);
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__ID);
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__NAME);
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__HOST_URL);
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__CONTEXT_ROOT);
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__JDBC_DRIVER);
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__JDBC_URL);
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__JDBC_USERNAME);
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__JDBC_PASSWORD);
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__BLOCK_CAPACITY_MB);
//
// updateProperties(properties);
//
// initialize();

// @Override
// public Map<Object, Object> getProperties() {
// return this.properties;
// }

// /**
// * Initialize database tables.
// */
// public void initialize() {
// String database = null;
// try {
// database = DatabaseUtil.getDatabase(this.databaseProperties);
// } catch (SQLException e) {
// e.printStackTrace();
// }
// assert (database != null) : "database name cannot be retrieved.";
//
// Connection conn = null;
// try {
// conn = DatabaseUtil.getConnection(this.databaseProperties);
//
// // if (this.categoryTableHandler != null) {
// // DatabaseUtil.initialize(conn, this.categoryTableHandler);
// // }
// // if (this.appTableHandler != null) {
// // DatabaseUtil.initialize(conn, this.appTableHandler);
// // }
// } catch (SQLException e) {
// e.printStackTrace();
// } finally {
// DatabaseUtil.closeQuietly(conn, true);
// }
// }

// /**
// *
// * @param configProps
// */
// public synchronized void updateProperties(Map<Object, Object> configProps) {
// if (configProps == null) {
// configProps = new HashMap<Object, Object>();
// }
//
// String indexServiceUrl = (String) configProps.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
// String globalHostURL = (String) configProps.get(SubstanceConstants.ORBIT_HOST_URL);
// String id = (String) configProps.get(SubstanceConstants.DFS__ID);
// String name = (String) configProps.get(SubstanceConstants.DFS__NAME);
// String hostURL = (String) configProps.get(SubstanceConstants.DFS__HOST_URL);
// String contextRoot = (String) configProps.get(SubstanceConstants.DFS__CONTEXT_ROOT);
// String jdbcDriver = (String) configProps.get(SubstanceConstants.DFS__JDBC_DRIVER);
// String jdbcURL = (String) configProps.get(SubstanceConstants.DFS__JDBC_URL);
// String jdbcUsername = (String) configProps.get(SubstanceConstants.DFS__JDBC_USERNAME);
// String jdbcPassword = (String) configProps.get(SubstanceConstants.DFS__JDBC_PASSWORD);
// String blockCapacityMB = (String) configProps.get(SubstanceConstants.DFS__BLOCK_CAPACITY_MB);
//
// boolean printProps = false;
// if (printProps) {
// System.out.println();
// System.out.println("Config properties:");
// System.out.println("-----------------------------------------------------");
// System.out.println(InfraConstants.ORBIT_INDEX_SERVICE_URL + " = " + indexServiceUrl);
// System.out.println(SubstanceConstants.ORBIT_HOST_URL + " = " + globalHostURL);
// System.out.println(SubstanceConstants.DFS__ID + " = " + id);
// System.out.println(SubstanceConstants.DFS__NAME + " = " + name);
// System.out.println(SubstanceConstants.DFS__HOST_URL + " = " + hostURL);
// System.out.println(SubstanceConstants.DFS__CONTEXT_ROOT + " = " + contextRoot);
// System.out.println(SubstanceConstants.DFS__JDBC_DRIVER + " = " + jdbcDriver);
// System.out.println(SubstanceConstants.DFS__JDBC_URL + " = " + jdbcURL);
// System.out.println(SubstanceConstants.DFS__JDBC_USERNAME + " = " + jdbcUsername);
// System.out.println(SubstanceConstants.DFS__JDBC_PASSWORD + " = " + jdbcPassword);
// System.out.println(SubstanceConstants.DFS__BLOCK_CAPACITY_MB + " = " + blockCapacityMB);
// System.out.println("-----------------------------------------------------");
// System.out.println();
// }
//
// this.properties = configProps;
// this.databaseProperties = getConnectionProperties(this.properties);
// }
//
// /**
// *
// * @param props
// * @return
// */
// protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
// String driver = (String) this.properties.get(SubstanceConstants.DFS__JDBC_DRIVER);
// String url = (String) this.properties.get(SubstanceConstants.DFS__JDBC_URL);
// String username = (String) this.properties.get(SubstanceConstants.DFS__JDBC_USERNAME);
// String password = (String) this.properties.get(SubstanceConstants.DFS__JDBC_PASSWORD);
// return DatabaseUtil.getProperties(driver, url, username, password);
// }
