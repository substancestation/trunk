package org.orbit.substance.runtime;

import org.orbit.substance.runtime.util.DfsConfigPropertiesHandler;
import org.orbit.substance.runtime.util.DfsVolumeConfigPropertiesHandler;
import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstanceRuntimeActivator extends AbstractBundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(SubstanceRuntimeActivator.class);

	protected static SubstanceRuntimeActivator activator;

	public static SubstanceRuntimeActivator getInstance() {
		return activator;
	}

	protected DfsConfigPropertiesHandler dfsConfigPropertiesHandler;
	protected DfsVolumeConfigPropertiesHandler dfsVolumeConfigPropertiesHandler;
	protected Extensions extensions;
	protected SubstanceServices services;

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		LOG.debug("start()");
		super.start(bundleContext);

		activator = this;

		// Load config properties
		this.dfsConfigPropertiesHandler = new DfsConfigPropertiesHandler();
		this.dfsConfigPropertiesHandler.start(bundleContext);

		this.dfsVolumeConfigPropertiesHandler = new DfsVolumeConfigPropertiesHandler();
		this.dfsVolumeConfigPropertiesHandler.start(bundleContext);

		// Register extensions
		this.extensions = new Extensions();
		this.extensions.start(bundleContext);

		// Start service adapters
		this.services = new SubstanceServices();
		this.services.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		// Stop service adapters
		if (this.services != null) {
			this.services.stop(bundleContext);
			this.services = null;
		}

		// Unregister extensions
		if (this.extensions != null) {
			this.extensions.stop(bundleContext);
			this.extensions = null;
		}

		// Unload config properties
		if (this.dfsConfigPropertiesHandler != null) {
			this.dfsConfigPropertiesHandler.stop(bundleContext);
			this.dfsConfigPropertiesHandler = null;
		}
		if (this.dfsVolumeConfigPropertiesHandler != null) {
			this.dfsVolumeConfigPropertiesHandler.stop(bundleContext);
			this.dfsVolumeConfigPropertiesHandler = null;
		}

		activator = null;

		super.stop(bundleContext);
	}

	public DfsConfigPropertiesHandler getDfsConfigPropertiesHandler() {
		return this.dfsConfigPropertiesHandler;
	}

	public DfsVolumeConfigPropertiesHandler getDfsVolumeConfigPropertiesHandler() {
		return this.dfsVolumeConfigPropertiesHandler;
	}

	public Extensions getExtensions() {
		return this.extensions;
	}

	public SubstanceServices getServices() {
		return this.services;
	}

}
