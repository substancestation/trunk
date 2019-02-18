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
import org.orbit.substance.webconsole.servlet.admin.dfs.DfsNodeActionServlet;
import org.orbit.substance.webconsole.servlet.admin.dfs.DfsNodeAddServlet;
import org.orbit.substance.webconsole.servlet.admin.dfs.DfsNodeDeleteServlet;
import org.orbit.substance.webconsole.servlet.admin.dfs.DfsNodeListServlet;
import org.orbit.substance.webconsole.servlet.admin.dfs.DfsNodeUpdateServlet;
import org.orbit.substance.webconsole.servlet.admin.dfsvolume.DfsVolumeNodeActionServlet;
import org.orbit.substance.webconsole.servlet.admin.dfsvolume.DfsVolumeNodeAddServlet;
import org.orbit.substance.webconsole.servlet.admin.dfsvolume.DfsVolumeNodeDeleteServlet;
import org.orbit.substance.webconsole.servlet.admin.dfsvolume.DfsVolumeNodeListServlet;
import org.orbit.substance.webconsole.servlet.admin.dfsvolume.DfsVolumeNodeUpdateServlet;
import org.orbit.substance.webconsole.servlet.dfs.CreateDirectoryServlet;
import org.orbit.substance.webconsole.servlet.dfs.FileContentServlet;
import org.orbit.substance.webconsole.servlet.dfs.FileDeleteServlet;
import org.orbit.substance.webconsole.servlet.dfs.FileDownloadServlet;
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
				WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT, //
				SubstanceConstants.ORBIT_DFS_URL, //
		};
		return propNames;
	}

	@Override
	public String getContextRoot() {
		// e.g. "/orbit/webconsole/substance"
		String contextRoot = (String) this.getProperties().get(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);
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
		addServlet(new ServletMetadataImpl("/mkdir", new CreateDirectoryServlet(), dicts));
		addServlet(new ServletMetadataImpl("/fileupload", new FileUploadServlet(), dicts));
		addServlet(new ServletMetadataImpl("/filecontent", new FileContentServlet(), dicts));
		addServlet(new ServletMetadataImpl("/filedownload", new FileDownloadServlet(), dicts));
		addServlet(new ServletMetadataImpl("/filedelete", new FileDeleteServlet(), dicts));

		// Admin
		addServlet(new ServletMetadataImpl("/admin/dfslist", new DfsNodeListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/dfsadd", new DfsNodeAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/dfsupdate", new DfsNodeUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/dfsdelete", new DfsNodeDeleteServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/dfsaction", new DfsNodeActionServlet(), dicts));

		addServlet(new ServletMetadataImpl("/admin/dfsvolumelist", new DfsVolumeNodeListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/dfsvolumeadd", new DfsVolumeNodeAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/dfsvolumeupdate", new DfsVolumeNodeUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/dfsvolumedelete", new DfsVolumeNodeDeleteServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/dfsvolumeaction", new DfsVolumeNodeActionServlet(), dicts));

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}

// addServlet(new ServletMetadataImpl("/admin/dfslist", new DfsListServlet(), dicts));
// addServlet(new ServletMetadataImpl("/admin/dfsvolumelist", new DfsVolumeListServlet(), dicts));