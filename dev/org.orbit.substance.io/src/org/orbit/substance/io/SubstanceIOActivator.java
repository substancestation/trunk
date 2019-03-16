package org.orbit.substance.io;

import org.orbit.substance.io.util.DfsURLStreamHandlerFactory;
import org.orbit.substance.io.util.URIHandlerDFileImpl;
import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstanceIOActivator extends AbstractBundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(SubstanceIOActivator.class);

	protected static SubstanceIOActivator instance;

	public static SubstanceIOActivator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		LOG.debug("start()");
		super.start(bundleContext);

		SubstanceIOActivator.instance = this;

		URIHandlerDFileImpl.INSTANCE.register();
		DfsURLStreamHandlerFactory.INSTANCE.register(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		DfsURLStreamHandlerFactory.INSTANCE.unregister(bundleContext);
		URIHandlerDFileImpl.INSTANCE.unregister();

		SubstanceIOActivator.instance = null;
		super.stop(bundleContext);
	}

	@Override
	protected String[] getPropertyNames() {
		String[] propNames = new String[] { //
				// InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				// InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
		};
		return propNames;
	}

}
