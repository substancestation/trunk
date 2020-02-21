package org.orbit.substance.webconsole.servlet.admin.dfs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.io.util.ConfigRegistryHelper;
import org.orbit.substance.webconsole.WebConstants;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DfsNodeAddServlet extends HttpServlet {

	private static final long serialVersionUID = 7108335969394140090L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

		String dfsId = ServletUtil.getParameter(request, "dfs_id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String enabledStr = ServletUtil.getParameter(request, "enabled", "");
		boolean enabled = ("true".equals(enabledStr)) ? true : false;

		String message = "";
		if (dfsId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'dfs_id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigElement configElement = null;
		if (!dfsId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = ConfigRegistryHelper.getDfsNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put(SubstanceConstants.IDX_PROP__DFS__ID, dfsId);
					attributes.put("enabled", enabled);
					configElement = cfgReg.createRootElement(name, attributes, true);

				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry for '" + ConfigRegistryHelper.REGISTRY__DFS_NODES + "' cannot be found or created.");
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}
		}

		if (configElement == null) {
			message = MessageHelper.INSTANCE.add(message, "Config element is not created.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Config element is created.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/dfslist");
	}

}
