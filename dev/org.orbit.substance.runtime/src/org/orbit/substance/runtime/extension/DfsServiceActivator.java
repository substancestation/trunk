package org.orbit.substance.runtime.extension;

import java.util.Map;

import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.impl.DfsServiceImpl;
import org.origin.common.service.ILifecycle;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class DfsServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.substance.runtime.DfsServiceActivator";

	/** ILifecycle */
	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start DFS service
		DfsServiceImpl service = new DfsServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(DfsService.class, service);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop DFS service
		DfsService service = process.getAdapter(DfsService.class);
		if (service instanceof ILifecycle) {
			((ILifecycle) service).stop(bundleContext);
		}
	}

}
