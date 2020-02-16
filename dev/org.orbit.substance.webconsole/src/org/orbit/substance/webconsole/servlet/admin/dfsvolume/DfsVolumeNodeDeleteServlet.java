package org.orbit.substance.webconsole.servlet.admin.dfsvolume;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.io.IConfigRegistry;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.io.util.ConfigRegistryHelper;
import org.orbit.substance.webconsole.WebConstants;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DfsVolumeNodeDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 189227949653735760L;

	private static String[] EMPTY_ELEMENT_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

		String dfsId = ServletUtil.getParameter(request, "dfsId", "");
		String[] elementIds = ServletUtil.getParameterValues(request, "elementId", EMPTY_ELEMENT_IDS);

		String message = "";
		if (elementIds.length == 0) {
			// message = MessageHelper.INSTANCE.add(message, "'elementIds' parameter is not set.");
			message = MessageHelper.INSTANCE.add(message, "DFS volume nodes are not selected.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		if (elementIds.length > 0) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = ConfigRegistryHelper.getDfsNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					for (String elementId : elementIds) {
						boolean currIsDeleted = cfgReg.deleteConfigElement(elementId);
						if (currIsDeleted) {
							hasSucceed = true;
						} else {
							hasFailed = true;
						}
					}

				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry for '" + ConfigRegistryHelper.REGISTRY__DFS_NODES + "' cannot be found or created.");
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, (elementIds.length > 1) ? "DFS volume nodes are deleted." : "DFS volume node is deleted.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (elementIds.length > 1) ? "DFS volume nodes are not deleted." : "DFS volume node is not deleted.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/dfsvolumelist?dfsId=" + dfsId);
	}

}
