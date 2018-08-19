package org.orbit.substance.runtime.dfs.filesystem.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.runtime.SubstanceConstants;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class FileSystemServiceImpl implements FileSystemService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;
	protected Map<String, FileSystem> usernameToFileSystemMap = new HashMap<String, FileSystem>();

	/**
	 * 
	 * @param initProperties
	 */
	public FileSystemServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new ServiceEditPoliciesImpl(FileSystemService.class, this);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.FILE_SYSTEM_UUID);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.FILE_SYSTEM_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.FILE_SYSTEM_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.FILE_SYSTEM_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.FILE_SYSTEM_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.FILE_SYSTEM_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.FILE_SYSTEM_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.FILE_SYSTEM_JDBC_PASSWORD);

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
		this.serviceRegistry = bundleContext.registerService(FileSystemService.class, this, props);
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
		String uuid = (String) configProps.get(SubstanceConstants.FILE_SYSTEM_UUID);
		String name = (String) configProps.get(SubstanceConstants.FILE_SYSTEM_NAME);
		String hostURL = (String) configProps.get(SubstanceConstants.FILE_SYSTEM_HOST_URL);
		String contextRoot = (String) configProps.get(SubstanceConstants.FILE_SYSTEM_CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(SubstanceConstants.FILE_SYSTEM_JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(SubstanceConstants.FILE_SYSTEM_JDBC_URL);
		String jdbcUsername = (String) configProps.get(SubstanceConstants.FILE_SYSTEM_JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(SubstanceConstants.FILE_SYSTEM_JDBC_PASSWORD);

		boolean printProps = false;
		if (printProps) {
			System.out.println();
			System.out.println("Config properties:");
			System.out.println("-----------------------------------------------------");
			System.out.println(SubstanceConstants.ORBIT_HOST_URL + " = " + globalHostURL);
			System.out.println(SubstanceConstants.FILE_SYSTEM_UUID + " = " + uuid);
			System.out.println(SubstanceConstants.FILE_SYSTEM_NAME + " = " + name);
			System.out.println(SubstanceConstants.FILE_SYSTEM_HOST_URL + " = " + hostURL);
			System.out.println(SubstanceConstants.FILE_SYSTEM_CONTEXT_ROOT + " = " + contextRoot);
			System.out.println(SubstanceConstants.FILE_SYSTEM_JDBC_DRIVER + " = " + jdbcDriver);
			System.out.println(SubstanceConstants.FILE_SYSTEM_JDBC_URL + " = " + jdbcURL);
			System.out.println(SubstanceConstants.FILE_SYSTEM_JDBC_USERNAME + " = " + jdbcUsername);
			System.out.println(SubstanceConstants.FILE_SYSTEM_JDBC_PASSWORD + " = " + jdbcPassword);
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
		String driver = (String) this.properties.get(SubstanceConstants.FILE_SYSTEM_JDBC_DRIVER);
		String url = (String) this.properties.get(SubstanceConstants.FILE_SYSTEM_JDBC_URL);
		String username = (String) this.properties.get(SubstanceConstants.FILE_SYSTEM_JDBC_USERNAME);
		String password = (String) this.properties.get(SubstanceConstants.FILE_SYSTEM_JDBC_PASSWORD);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() throws SQLException {
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
	public String getUUID() {
		String uuid = (String) this.properties.get(SubstanceConstants.FILE_SYSTEM_UUID);
		return uuid;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(SubstanceConstants.FILE_SYSTEM_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(SubstanceConstants.FILE_SYSTEM_HOST_URL);
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
		String contextRoot = (String) this.properties.get(SubstanceConstants.FILE_SYSTEM_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	@Override
	public synchronized FileSystem getFileSystem(String accessToken) throws ServerException {
		String username = OrbitTokenUtil.INSTANCE.getUsername(accessToken, PlatformConstants.TOKEN_PROVIDER__ORBIT);
		if (username == null) {
			throw new ServerException("400", "Username is not found from access token.");
		}

		FileSystem fileSystem = this.usernameToFileSystemMap.get(username);
		if (fileSystem == null) {
			fileSystem = new FileSystemImpl(this.properties, accessToken, username);
			this.usernameToFileSystemMap.put(username, fileSystem);
		}
		return fileSystem;
	}

}
