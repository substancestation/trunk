package org.orbit.substance.webconsole.servlet.dfs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.io.util.DfsClientResolverImpl;
import org.orbit.substance.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class CreateDirectoryServlet extends HttpServlet {

	private static final long serialVersionUID = -7139617336247386165L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

		String parentFileId = ServletUtil.getParameter(request, "parentFileId", "-1");
		String fileName = ServletUtil.getParameter(request, "name", "");

		String message = "";
		if (fileName.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'name' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		if (!fileName.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				DfsClientResolver dfsClientResolver = new DfsClientResolverImpl(indexServiceUrl);

				FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.createDirectory(dfsClientResolver, dfsServiceUrl, accessToken, parentFileId, fileName);
				if (fileMetadata != null) {
					succeed = true;
				}

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, "Directory is created successfully.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Directory is not created.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/files?parentFileId=" + parentFileId);
	}

}
