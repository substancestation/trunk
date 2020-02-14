package org.orbit.substance.runtime.extension;

import java.util.Map;

import org.orbit.platform.sdk.ProcessContext;
import org.orbit.substance.runtime.SubstanceConstants;
import org.origin.common.extensions.condition.IPropertyTester;

public class DfsServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.substance.runtime.DfsServicePropertyTester";

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		if (context instanceof ProcessContext) {
			ProcessContext platformContext = (ProcessContext) context;
			Object value = platformContext.getProperty(SubstanceConstants.DFS__AUTOSTART);
			if (value != null && "true".equalsIgnoreCase(value.toString())) {
				return true;
			}
		}
		return false;
	}

}

// BundleContext bundleContext = null;
// if (context instanceof IPlatformContext) {
// IPlatformContext platformContext = (IPlatformContext) context;
// bundleContext = platformContext.getBundleContext();
// }
// if (bundleContext != null) {
// Map<Object, Object> properties = new Hashtable<Object, Object>();
// PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS__AUTOSTART);
//
// String autoStart = (String) properties.get(SubstanceConstants.DFS__AUTOSTART);
// if ("true".equalsIgnoreCase(autoStart)) {
// return true;
// }
// }
