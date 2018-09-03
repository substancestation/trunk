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

	protected ServiceConnectorAdapter<DfsClient> dfsServiceConnector;
	protected ServiceConnectorAdapter<DfsVolumeClient> dfsVolumeServiceConnector;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.dfsServiceConnector = new ServiceConnectorAdapter<DfsClient>(DfsClient.class);
		this.dfsServiceConnector.start(bundleContext);

		this.dfsVolumeServiceConnector = new ServiceConnectorAdapter<DfsVolumeClient>(DfsVolumeClient.class);
		this.dfsVolumeServiceConnector.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.dfsServiceConnector != null) {
			this.dfsServiceConnector.stop(bundleContext);
			this.dfsServiceConnector = null;
		}

		if (this.dfsVolumeServiceConnector != null) {
			this.dfsVolumeServiceConnector.stop(bundleContext);
			this.dfsVolumeServiceConnector = null;
		}
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public DfsClient getDfsClient(Map<String, Object> properties) {
		DfsClient client = this.dfsServiceConnector.getService(properties);
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
		DfsVolumeClient client = this.dfsVolumeServiceConnector.getService(properties);
		if (client == null) {
			throw new RuntimeException("DfsVolumeClient is not available.");
		}
		return client;
	}

}
