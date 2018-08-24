package org.orbit.substance.runtime.extension.dfs;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.substance.runtime.SubstanceConstants;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class FileContentServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.substance.runtime.FileContentServicePropertyTester";

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.DFS_VOLUME__AUTOSTART);
			String autoStart = (String) properties.get(SubstanceConstants.DFS_VOLUME__AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
