/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.substance.webconsole.extension;

import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.substance.webconsole.servlet.WebApplication;
import org.osgi.framework.BundleContext;

public class WebApplicationActivator implements ServiceActivator {

	public static final String ID = "org.orbit.substance.webconsole.WebApplicationActivator";

	public static WebApplicationActivator INSTANCE = new WebApplicationActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		WebApplication webApp = new WebApplication(properties);
		webApp.start(bundleContext);

		process.adapt(WebApplication.class, webApp);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		WebApplication webApp = process.getAdapter(WebApplication.class);
		if (webApp != null) {
			webApp.stop(bundleContext);
		}
	}

}
