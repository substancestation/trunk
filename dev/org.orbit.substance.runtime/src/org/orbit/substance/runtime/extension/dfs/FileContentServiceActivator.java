package org.orbit.substance.runtime.extension.dfs;

import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.substance.runtime.dfs.filecontent.service.FileContentService;
import org.orbit.substance.runtime.dfs.filecontent.service.impl.FileContentServiceImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class FileContentServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.substance.runtime.FileContentServiceActivator";

	@Override
	public void start(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start service
		FileContentServiceImpl service = new FileContentServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(FileContentService.class, service);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop service
		FileContentService service = process.getAdapter(FileContentService.class);
		if (service instanceof LifecycleAware) {
			((LifecycleAware) service).stop(bundleContext);
		}
	}

}
