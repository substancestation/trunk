package org.orbit.substance.model;

import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstanceModelActivator extends AbstractBundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(SubstanceModelActivator.class);

	protected static SubstanceModelActivator instance;

	public static SubstanceModelActivator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		LOG.debug("start()");
		super.start(bundleContext);

		SubstanceModelActivator.instance = this;

	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		SubstanceModelActivator.instance = null;
		super.stop(bundleContext);
	}

}