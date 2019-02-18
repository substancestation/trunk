package org.orbit.substance.api;

import org.orbit.substance.api.util.SubstanceClients;
import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstanceAPIActivator extends AbstractBundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(SubstanceAPIActivator.class);

	protected static SubstanceAPIActivator instance;

	public static SubstanceAPIActivator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		LOG.debug("start()");
		super.start(bundleContext);

		SubstanceAPIActivator.instance = this;

		SubstanceClients.getInstance().start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		SubstanceClients.getInstance().stop(bundleContext);

		SubstanceAPIActivator.instance = null;
		super.stop(bundleContext);
	}

	@Override
	protected String[] getPropertyNames() {
		String[] propNames = new String[] { //
				SubstanceConstants.ORBIT_DFS_URL, //
		};
		return propNames;
	}

}
