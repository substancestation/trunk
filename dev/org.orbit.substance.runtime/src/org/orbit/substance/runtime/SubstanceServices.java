package org.orbit.substance.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.ws.DfsServiceAdapter;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.orbit.substance.runtime.dfsvolume.ws.DfsVolumeServiceAdapter;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstanceServices {

	protected static Logger LOG = LoggerFactory.getLogger(SubstanceServices.class);

	private static SubstanceServices INSTANCE = new SubstanceServices();

	public static SubstanceServices getInstance() {
		return INSTANCE;
	}

	protected Map<Object, Object> properties;
	protected ServiceConnectorAdapter<IndexProvider> indexProviderConnector;

	protected DfsServiceAdapter fileSystemServiceAdapter;
	protected DfsVolumeServiceAdapter fileContentServiceAdapter;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		this.properties = properties;

		this.indexProviderConnector = new ServiceConnectorAdapter<IndexProvider>(IndexProvider.class) {
			@Override
			public void connectorAdded(ServiceConnector<IndexProvider> connector) {
				doStart(bundleContext);
			}

			@Override
			public void connectorRemoved(ServiceConnector<IndexProvider> connector) {
			}
		};
		this.indexProviderConnector.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		doStop(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void doStart(BundleContext bundleContext) {
		// Start service adapters
		this.fileSystemServiceAdapter = new DfsServiceAdapter(this.properties);
		this.fileSystemServiceAdapter.start(bundleContext);

		this.fileContentServiceAdapter = new DfsVolumeServiceAdapter(this.properties);
		this.fileContentServiceAdapter.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void doStop(BundleContext bundleContext) {
		// Stop service adapters
		if (this.fileSystemServiceAdapter != null) {
			this.fileSystemServiceAdapter.stop(bundleContext);
			this.fileSystemServiceAdapter = null;
		}

		if (this.fileContentServiceAdapter != null) {
			this.fileContentServiceAdapter.stop(bundleContext);
			this.fileContentServiceAdapter = null;
		}
	}

	public DfsService getFileSystemService() {
		return (this.fileSystemServiceAdapter != null) ? this.fileSystemServiceAdapter.getService() : null;
	}

	public DfsVolumeService getFileContentService() {
		return (this.fileContentServiceAdapter != null) ? this.fileContentServiceAdapter.getService() : null;
	}

}
