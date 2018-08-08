package org.orbit.substance.runtime.extension.dfsmetadata;

import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.substance.runtime.dfs.metadata.service.DFSMetadataService;
import org.orbit.substance.runtime.dfs.metadata.service.DFSMetadataServiceImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class DFSMetadataServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.substance.runtime.DFSMetadataServiceActivator";

	public static DFSMetadataServiceActivator INSTANCE = new DFSMetadataServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start service
		DFSMetadataServiceImpl service = new DFSMetadataServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(DFSMetadataService.class, service);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop service
		DFSMetadataService service = process.getAdapter(DFSMetadataService.class);
		if (service instanceof LifecycleAware) {
			((LifecycleAware) service).stop(bundleContext);
		}
	}

}
