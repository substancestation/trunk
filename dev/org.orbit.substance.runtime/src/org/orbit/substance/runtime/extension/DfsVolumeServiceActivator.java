package org.orbit.substance.runtime.extension;

import java.util.Map;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.orbit.substance.runtime.dfsvolume.service.impl.DfsVolumeServiceImpl;
import org.origin.common.service.ILifecycle;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class DfsVolumeServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.substance.runtime.DfsVolumeServiceActivator";

	/** ILifecycle */
	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start DFS Volume service
		DfsVolumeServiceImpl service = new DfsVolumeServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(DfsVolumeService.class, service);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop DFS Volume service
		DfsVolumeService service = process.getAdapter(DfsVolumeService.class);
		if (service instanceof ILifecycle) {
			((ILifecycle) service).stop(bundleContext);
		}
	}

}
