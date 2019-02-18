package org.orbit.substance.connector;

import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstanceConnectorActivator extends AbstractBundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(SubstanceConnectorActivator.class);

	protected static SubstanceConnectorActivator instance;

	public static SubstanceConnectorActivator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		LOG.debug("start()");
		super.start(bundleContext);

		SubstanceConnectorActivator.instance = this;

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		SubstanceConnectorActivator.instance = null;
		super.stop(bundleContext);
	}

}
