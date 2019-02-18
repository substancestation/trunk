package org.orbit.substance.webconsole;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstanceWebConsoleActivator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(SubstanceWebConsoleActivator.class);

	protected static BundleContext bundleContext;
	protected static SubstanceWebConsoleActivator instance;

	static BundleContext getContext() {
		return bundleContext;
	}

	public static SubstanceWebConsoleActivator getInstance() {
		return instance;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");
		SubstanceWebConsoleActivator.bundleContext = bundleContext;
		SubstanceWebConsoleActivator.instance = this;

		// Register extensions
		Extensions.INSTANCE.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		// Unregister extensions
		Extensions.INSTANCE.stop(bundleContext);

		SubstanceWebConsoleActivator.instance = null;
		SubstanceWebConsoleActivator.bundleContext = null;
	}

}
