package org.orbit.substance.webconsole.servlet.admin.dfsvolume;

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
import org.orbit.substance.io.util.DfsNodeConfigHelper;
import org.orbit.substance.webconsole.WebConstants;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DfsVolumeNodeAddServlet extends HttpServlet {

	private static final long serialVersionUID = 6375167839530346003L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

		String dfsId = ServletUtil.getParameter(request, "dfsId", "");
		String dfsVolumeId = ServletUtil.getParameter(request, "dfs_volume_id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String enabledStr = ServletUtil.getParameter(request, "enabled", "");
		boolean enabled = ("true".equals(enabledStr)) ? true : false;

		String message = "";
		if (dfsVolumeId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'dfs_volume_id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigElement configElement = null;
		if (!dfsVolumeId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = DfsNodeConfigHelper.INSTANCE.getDfsNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					IConfigElement dfsConfigElement = DfsNodeConfigHelper.INSTANCE.getDfsConfigElement(cfgReg, dfsId);
					if (dfsConfigElement != null) {
						Map<String, Object> attributes = new HashMap<String, Object>();
						attributes.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID, dfsVolumeId);
						attributes.put("enabled", enabled);
						configElement = dfsConfigElement.createMemberConfigElement(name, attributes, true);

					} else {
						message = MessageHelper.INSTANCE.add(message, "Config element for DFS node (dfsId: '" + dfsId + "') cannot be found.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry for '" + DfsNodeConfigHelper.INSTANCE.getConfigRegistryName__DfsNodes() + "' cannot be found or created.");
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}
		}

		if (configElement != null) {
			message = MessageHelper.INSTANCE.add(message, "Config element is created successfully.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Config element is not created.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/dfsvolumelist?dfsId=" + dfsId);
	}

}
