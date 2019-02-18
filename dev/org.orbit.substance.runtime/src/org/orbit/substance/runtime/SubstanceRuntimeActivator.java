package org.orbit.substance.runtime;

import org.orbit.substance.runtime.util.DfsConfigPropertiesHandler;
import org.orbit.substance.runtime.util.DfsVolumeConfigPropertiesHandler;
import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstanceRuntimeActivator extends AbstractBundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(SubstanceRuntimeActivator.class);

	protected static SubstanceRuntimeActivator instance;

	public static SubstanceRuntimeActivator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		LOG.debug("start()");
		super.start(bundleContext);

		SubstanceRuntimeActivator.instance = this;

		// Load config properties
		DfsConfigPropertiesHandler.getInstance().start(bundleContext);
		DfsVolumeConfigPropertiesHandler.getInstance().start(bundleContext);

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);

		// Start service adapters
		SubstanceServices.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		// Stop service adapters
		SubstanceServices.getInstance().stop(bundleContext);

		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		// Unload config properties
		DfsConfigPropertiesHandler.getInstance().stop(bundleContext);
		DfsVolumeConfigPropertiesHandler.getInstance().stop(bundleContext);

		SubstanceRuntimeActivator.instance = null;
		super.stop(bundleContext);
	}

}
