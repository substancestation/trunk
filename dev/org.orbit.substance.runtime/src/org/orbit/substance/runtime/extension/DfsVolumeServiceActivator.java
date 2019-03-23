package org.orbit.substance.runtime.extension;

import java.util.Map;

import org.orbit.platform.sdk.IProcessContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.orbit.substance.runtime.dfsvolume.service.impl.DfsVolumeServiceImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class DfsVolumeServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.substance.runtime.DfsVolumeServiceActivator";

	@Override
	public void start(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start service
		DfsVolumeServiceImpl service = new DfsVolumeServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(DfsVolumeService.class, service);
	}

	@Override
	public void stop(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop service
		DfsVolumeService service = process.getAdapter(DfsVolumeService.class);
		if (service instanceof LifecycleAware) {
			((LifecycleAware) service).stop(bundleContext);
		}
	}

}
