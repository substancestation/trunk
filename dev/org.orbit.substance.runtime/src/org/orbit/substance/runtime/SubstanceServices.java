package org.orbit.substance.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.substance.runtime.dfs.service.FileSystemService;
import org.orbit.substance.runtime.dfs.ws.FileSystemServiceAdapter;
import org.orbit.substance.runtime.dfsvolume.service.FileContentService;
import org.orbit.substance.runtime.dfsvolume.ws.FileContentServiceAdapter;
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

	protected FileSystemServiceAdapter fileSystemServiceAdapter;
	protected FileContentServiceAdapter fileContentServiceAdapter;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.ORBIT_INDEX_SERVICE_URL);
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
		this.fileSystemServiceAdapter = new FileSystemServiceAdapter(this.properties);
		this.fileSystemServiceAdapter.start(bundleContext);

		this.fileContentServiceAdapter = new FileContentServiceAdapter(this.properties);
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

	public FileSystemService getFileSystemService() {
		return (this.fileSystemServiceAdapter != null) ? this.fileSystemServiceAdapter.getService() : null;
	}

	public FileContentService getFileContentService() {
		return (this.fileContentServiceAdapter != null) ? this.fileContentServiceAdapter.getService() : null;
	}

}
