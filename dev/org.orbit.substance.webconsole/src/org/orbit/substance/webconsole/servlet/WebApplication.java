package org.orbit.substance.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.platform.sdk.http.PlatformWebApplication;
import org.orbit.service.servlet.impl.JspMetadataImpl;
import org.orbit.service.servlet.impl.ResourceMetadataImpl;
import org.orbit.service.servlet.impl.ServletMetadataImpl;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.webconsole.WebConstants;
import org.orbit.substance.webconsole.servlet.dfs.FileListServlet;
import org.orbit.substance.webconsole.servlet.dfs.FileUploadServlet;
import org.osgi.framework.BundleContext;

public class WebApplication extends PlatformWebApplication {

	/**
	 * 
	 * @param initProperties
	 */
	public WebApplication(Map<Object, Object> initProperties) {
		super(initProperties);
	}

	@Override
	protected String[] getPropertyNames() {
		String[] propNames = new String[] { //
				InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
				WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.DFS_VOLUME__WEB_CONSOLE_CONTEXT_ROOT, //
				SubstanceConstants.ORBIT_DFS_URL, //
		};
		return propNames;
	}

	@Override
	public String getContextRoot() {
		// e.g. "/orbit/webconsole/dfs"
		String contextRoot = (String) this.getProperties().get(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	protected void addResources(BundleContext bundleContext) {
		Hashtable<Object, Object> dicts = new Hashtable<Object, Object>();
		if (!this.properties.isEmpty()) {
			dicts.putAll(this.properties);
		}

		dicts.put("contextPath", getContextRoot());

		// put bundle name in the path, so that the bundle name can be retrieved from it and be used to get http context
		String bundlePrefix = "/org.orbit.substance.webconsole";

		// Web resources
		addResource(new ResourceMetadataImpl("/views/css", bundlePrefix + "/WEB-INF/views/css"));
		addResource(new ResourceMetadataImpl("/views/icons", bundlePrefix + "/WEB-INF/views/icons"));
		addResource(new ResourceMetadataImpl("/views/js", bundlePrefix + "/WEB-INF/views/js"));

		// Files
		addServlet(new ServletMetadataImpl("/files", new FileListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/fileupload", new FileUploadServlet(), dicts));

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}
