package org.orbit.substance.api.util;

import java.util.Map;

import org.orbit.substance.api.dfs.FileSystemClient;
import org.orbit.substance.api.dfsvolume.FileContentClient;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstanceClients implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(SubstanceClients.class);

	private static SubstanceClients INSTANCE = new SubstanceClients();

	public static SubstanceClients getInstance() {
		return INSTANCE;
	}

	protected ServiceConnectorAdapter<FileSystemClient> fileSystemServiceConnector;
	protected ServiceConnectorAdapter<FileContentClient> fileContentServiceConnector;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.fileSystemServiceConnector = new ServiceConnectorAdapter<FileSystemClient>(FileSystemClient.class);
		this.fileSystemServiceConnector.start(bundleContext);

		this.fileContentServiceConnector = new ServiceConnectorAdapter<FileContentClient>(FileContentClient.class);
		this.fileContentServiceConnector.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.fileSystemServiceConnector != null) {
			this.fileSystemServiceConnector.stop(bundleContext);
			this.fileSystemServiceConnector = null;
		}

		if (this.fileContentServiceConnector != null) {
			this.fileContentServiceConnector.stop(bundleContext);
			this.fileContentServiceConnector = null;
		}
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public FileSystemClient getFileSystemClient(Map<String, Object> properties) {
		FileSystemClient client = this.fileSystemServiceConnector.getService(properties);
		if (client == null) {
			throw new RuntimeException("FileSystemClient is not available.");
		}
		return client;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public FileContentClient getFileContentClient(Map<String, Object> properties) {
		FileContentClient client = this.fileContentServiceConnector.getService(properties);
		if (client == null) {
			throw new RuntimeException("FileContentClient is not available.");
		}
		return client;
	}

}
