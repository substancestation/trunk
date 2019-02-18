package org.orbit.substance.sdk;

import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstanceSDKActivator extends AbstractBundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(SubstanceSDKActivator.class);

	protected static SubstanceSDKActivator instance;

	public static SubstanceSDKActivator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		LOG.debug("start()");
		super.start(bundleContext);

		SubstanceSDKActivator.instance = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		SubstanceSDKActivator.instance = null;
		super.stop(bundleContext);
	}

}
