package org.orbit.substance.webconsole.servlet.dfsadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.webconsole.WebConstants;
import org.orbit.substance.webconsole.util.DfsAdminUtil;
import org.orbit.substance.webconsole.util.MessageHelper;

public class DfsListServlet extends HttpServlet {

	private static final long serialVersionUID = 8967936416518581588L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		List<IndexItem> dfsIndexItems = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			dfsIndexItems = DfsAdminUtil.getDfsIndexItems(indexServiceUrl, accessToken);

		} catch (Exception e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}
		if (dfsIndexItems == null) {
			dfsIndexItems = new ArrayList<IndexItem>();
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("dfsIndexItems", dfsIndexItems);

		request.getRequestDispatcher(contextRoot + "/views/admin_dfs_list.jsp").forward(request, response);
	}

}
