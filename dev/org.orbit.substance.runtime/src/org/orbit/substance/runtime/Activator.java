package org.orbit.substance.runtime;

import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends AbstractBundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static Activator instance;

	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		LOG.debug("start()");
		super.start(bundleContext);

		Activator.instance = this;

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

		Activator.instance = null;
		super.stop(bundleContext);
	}

}
