package org.orbit.substance.runtime.extension;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.platform.sdk.ProcessContext;
import org.orbit.substance.runtime.SubstanceConstants;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class DfsVolumeServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.substance.runtime.DfsVolumeServicePropertyTester";

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof ProcessContext) {
			ProcessContext platformContext = (ProcessContext) context;
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
