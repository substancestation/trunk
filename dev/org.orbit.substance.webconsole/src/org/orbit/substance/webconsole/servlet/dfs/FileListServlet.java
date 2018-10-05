package org.orbit.substance.webconsole.servlet.dfs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.io.util.DefaultDfsClientResolver;
import org.orbit.substance.webconsole.WebConstants;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class FileListServlet extends HttpServlet {

	private static final long serialVersionUID = -6860996521046593549L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);
		String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		String parentFileId = ServletUtil.getParameter(request, "parentFileId", "-1");
		String grandParentFileId = null;

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		FileMetadata[] files = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			DfsClientResolver dfsClientResolver = new DefaultDfsClientResolver();

			if (parentFileId == null || parentFileId.isEmpty() || "-1".equals(parentFileId)) {
				files = SubstanceClientsUtil.Dfs.listRoots(dfsClientResolver, dfsServiceUrl, accessToken);

			} else {
				files = SubstanceClientsUtil.Dfs.listFiles(dfsClientResolver, dfsServiceUrl, accessToken, parentFileId);

				FileMetadata parentFile = SubstanceClientsUtil.Dfs.getFile(dfsClientResolver, dfsServiceUrl, accessToken, parentFileId);
				if (parentFile != null) {
					grandParentFileId = parentFile.getParentFileId();
				}
			}

		} catch (Exception e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}
		if (files == null) {
			files = SubstanceClientsUtil.Dfs.EMPTY_FILES;
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("parentFileId", parentFileId);
		request.setAttribute("files", files);
		if (grandParentFileId != null) {
			request.setAttribute("grandParentFileId", grandParentFileId);
		}

		request.getRequestDispatcher(contextRoot + "/views/dfs_files_list.jsp").forward(request, response);
	}

}
