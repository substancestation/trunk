package org.orbit.substance.api.util;

import java.util.Map;

import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
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

	protected ServiceConnectorAdapter<DfsClient> fileSystemServiceConnector;
	protected ServiceConnectorAdapter<DfsVolumeClient> fileContentServiceConnector;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.fileSystemServiceConnector = new ServiceConnectorAdapter<DfsClient>(DfsClient.class);
		this.fileSystemServiceConnector.start(bundleContext);

		this.fileContentServiceConnector = new ServiceConnectorAdapter<DfsVolumeClient>(DfsVolumeClient.class);
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
	public DfsClient getDfsClient(Map<String, Object> properties) {
		DfsClient client = this.fileSystemServiceConnector.getService(properties);
		if (client == null) {
			throw new RuntimeException("DfsClient is not available.");
		}
		return client;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public DfsVolumeClient getDfsVolumeClient(Map<String, Object> properties) {
		DfsVolumeClient client = this.fileContentServiceConnector.getService(properties);
		if (client == null) {
			throw new RuntimeException("DfsVolumeClient is not available.");
		}
		return client;
	}

}
