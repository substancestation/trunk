package org.orbit.substance.webconsole.servlet.dfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.orbit.substance.io.util.DfsClientResolverByDfsId;
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
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		// String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);
		String dfsId = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_ID);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

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
		List<FileMetadata> parentFiles = new ArrayList<FileMetadata>();
		FileMetadata[] files = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			DfsClientResolver dfsClientResolver = new DfsClientResolverByDfsId(dfsId);

			if (parentFileId == null || parentFileId.isEmpty() || "-1".equals(parentFileId)) {
				files = SubstanceClientsUtil.DFS.listRoots(dfsClientResolver, accessToken);

			} else {
				files = SubstanceClientsUtil.DFS.listFiles(dfsClientResolver, accessToken, parentFileId);

				FileMetadata parentFile = SubstanceClientsUtil.DFS.getFile(dfsClientResolver, accessToken, parentFileId);
				if (parentFile != null) {
					grandParentFileId = parentFile.getParentFileId();
				}
				while (parentFile != null) {
					parentFiles.add(0, parentFile);
					
					String _theParentFileId = parentFile.getParentFileId();
					if (_theParentFileId == null || "-1".equals(_theParentFileId)) {
						break;
					}
					parentFile = SubstanceClientsUtil.DFS.getFile(dfsClientResolver, accessToken, _theParentFileId);
				}
			}

		} catch (Exception e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}
		if (files == null) {
			files = SubstanceClientsUtil.DFS.EMPTY_FILES;
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("parentFileId", parentFileId);
		request.setAttribute("parentFiles", parentFiles);
		request.setAttribute("files", files);
		if (grandParentFileId != null) {
			request.setAttribute("grandParentFileId", grandParentFileId);
		}

		request.getRequestDispatcher(contextRoot + "/views/dfs_files_list.jsp").forward(request, response);
	}

}
