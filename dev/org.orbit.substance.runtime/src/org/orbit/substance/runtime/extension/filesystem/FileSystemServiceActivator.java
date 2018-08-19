package org.orbit.substance.runtime.extension.filesystem;

import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.substance.runtime.dfs.filesystem.service.FileSystem;
import org.orbit.substance.runtime.dfs.filesystem.service.FileSystemService;
import org.orbit.substance.runtime.dfs.filesystem.service.FileSystemServiceImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class FileSystemServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.substance.runtime.FileSystemServiceActivator";

	public static FileSystemServiceActivator INSTANCE = new FileSystemServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start service
		FileSystemServiceImpl service = new FileSystemServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(FileSystemService.class, service);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop service
		FileSystem service = process.getAdapter(FileSystem.class);
		if (service instanceof LifecycleAware) {
			((LifecycleAware) service).stop(bundleContext);
		}
	}

}
