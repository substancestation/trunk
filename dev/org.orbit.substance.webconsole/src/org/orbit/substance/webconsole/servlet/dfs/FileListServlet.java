package org.orbit.substance.webconsole.servlet.dfs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.webconsole.WebConstants;
import org.orbit.substance.webconsole.util.MessageHelper;

public class FileListServlet extends HttpServlet {

	private static final long serialVersionUID = -6860996521046593549L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);
		String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);

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
		FileMetadata[] files = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			files = SubstanceClientsUtil.Dfs.listRoots(dfsServiceUrl, accessToken);

		} catch (Exception e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		if (files != null) {
			request.setAttribute("files", files);
		}

		// e.g. contextRoot is "/orbit/webconsole/dfs"
		// /orbit/webconsole/dfs/views/dfs_fils_list.jsp
		// /orbit/webconsole/component/views/user_accounts_list_v1.jsp
		// request.getRequestDispatcher(contextRoot + "/views/dfs_fils_list.jsp").forward(request, response);
		request.getRequestDispatcher(contextRoot + "/views/dfs_files_list.jsp").forward(request, response);
	}

}
