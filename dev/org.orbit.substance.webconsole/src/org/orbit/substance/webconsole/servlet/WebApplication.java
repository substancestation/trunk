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
		String contextRoot = (String) this.getProperties().get(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	protected void addResources(BundleContext bundleContext) {
		Hashtable<Object, Object> dicts = new Hashtable<Object, Object>();
		if (!this.properties.isEmpty()) {
			dicts.putAll(this.properties);
		}

		String contextRoot = (String) this.properties.get(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);
		dicts.put("contextPath", contextRoot);

		String bundlePrefix = "/org.orbit.substance.webconsole";

		// Web resources
		addResource(new ResourceMetadataImpl("/views/css", bundlePrefix + "/WEB-INF/views/css"));
		addResource(new ResourceMetadataImpl("/views/icons", bundlePrefix + "/WEB-INF/views/icons"));
		addResource(new ResourceMetadataImpl("/views/js", bundlePrefix + "/WEB-INF/views/js"));

		// Files
		addServlet(new ServletMetadataImpl("/files", new FileListServlet(), dicts)); // public

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}

// Identity
// addServlet(new ServletMetadataImpl("/signup", new SignUpPage(), dicts)); // public
// addServlet(new ServletMetadataImpl("/signin", new SignInPage(), dicts)); // public
// addServlet(new ServletMetadataImpl("/signup_req", new SignUpServlet(), dicts)); // public
// addServlet(new ServletMetadataImpl("/signin_req", new SignInServlet(), dicts)); // public

// addServlet(new ServletMetadataImpl("/signout", new SignOutServlet(), dicts));
// addServlet(new ServletMetadataImpl("/user_main", new UserMainPage(), dicts));

// User accounts
// addServlet(new ServletMetadataImpl("/useraccounts", new UserAccountListServlet(), dicts));
// addServlet(new ServletMetadataImpl("/useraccountadd", new UserAccountAddServlet(), dicts));
// addServlet(new ServletMetadataImpl("/useraccountupdate", new UserAccountUpdateServlet(), dicts));
// addServlet(new ServletMetadataImpl("/useraccountdelete", new UserAccountDeleteServlet(), dicts));

// Machines
// addServlet(new ServletMetadataImpl("/domain/machines", new MachineListServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/machineadd", new MachineAddServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/machineupdate", new MachineUpdateServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/machinedelete", new MachineDeleteServlet(), dicts));

// Platforms
// addServlet(new ServletMetadataImpl("/domain/platforms", new PlatformListServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/platformadd", new PlatformAddServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/platformupdate", new PlatformUpdateServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/platformdelete", new PlatformDeleteServlet(), dicts));

// Platform properties
// addServlet(new ServletMetadataImpl("/domain/platformproperties", new PlatformPropertyListServlet(), dicts));

// Nodes
// addServlet(new ServletMetadataImpl("/domain/nodes", new NodeListServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodecreate", new NodeCreateServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodeupdate", new NodeUpdateServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodedelete", new NodeDeleteServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodestart", new NodeStartServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodestop", new NodeStopServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodestatus", new NodeListServlet(), dicts));

// Node properties
// addServlet(new ServletMetadataImpl("/domain/nodeproperties", new NodePropertyListServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodeattributeadd", new NodePropertyAddServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodeattributeupdate", new NodePropertyUpdateServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodeattributedelete", new NodePropertyDeleteServlet(), dicts));

// Node programs
// addServlet(new ServletMetadataImpl("/domain/nodeprograms", new NodeProgramListServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodeprograminstall", new NodeProgramInstallServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodeprogramuninstall", new NodeProgramUninstallServlet(), dicts));
// addServlet(new ServletMetadataImpl("/domain/nodeprogramaction", new NodeProgramActionServlet(), dicts));

// AppStore
// addServlet(new ServletMetadataImpl("/appstore/apps", new AppListServlet(), dicts));
// addServlet(new ServletMetadataImpl("/appstore/appadd", new AppAddServlet(), dicts));
// addServlet(new ServletMetadataImpl("/appstore/appupdate", new AppUpdateServlet(), dicts));
// addServlet(new ServletMetadataImpl("/appstore/appupload", new AppUploadServlet(), dicts));
// addServlet(new ServletMetadataImpl("/appstore/appdownload", new AppDownloadServlet(), dicts));
// addServlet(new ServletMetadataImpl("/appstore/appdelete", new AppDeleteServlet(), dicts));
// addServlet(new ServletMetadataImpl("/appstore/programsprovider", new ProgramsProviderServlet(), dicts));

// Other
// addServlet(new ServletMetadataImpl("/upload", new FileUploadServlet(), dicts));
