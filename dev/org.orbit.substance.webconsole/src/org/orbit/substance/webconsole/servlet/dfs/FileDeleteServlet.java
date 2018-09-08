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
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.webconsole.WebConstants;
import org.orbit.substance.webconsole.util.DefaultDfsClientResolver;
import org.orbit.substance.webconsole.util.DefaultDfsVolumeClientResolver;
import org.orbit.substance.webconsole.util.MessageHelper;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class FileDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 8325542952806426875L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);

		String parentFileId = ServletUtil.getParameter(request, "parentFileId", "-1");
		String[] fileIds = ServletUtil.getParameterValues(request, "fileId", EMPTY_IDS);

		String message = "";
		if (fileIds.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "'fileId' parameter is not set.");
		}

		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			DfsClientResolver dfsClientResolver = new DefaultDfsClientResolver();
			DfsVolumeClientResolver dfsVolumeClientResolver = new DefaultDfsVolumeClientResolver(indexServiceUrl);

			for (int i = 0; i < fileIds.length; i++) {
				String fileId = fileIds[i];

				FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.getFile(dfsClientResolver, dfsServiceUrl, accessToken, fileId);
				if (fileMetadata == null) {
					message = MessageHelper.INSTANCE.add(message, "File doesn't exist. fileId='" + fileId + "'.");
					continue;
				}

				boolean isFileMetadataDeleted = SubstanceClientsUtil.Dfs.deleteFile(dfsClientResolver, dfsServiceUrl, accessToken, fileId);
				if (isFileMetadataDeleted) {
					if (!fileMetadata.getFileParts().isEmpty()) {
						boolean isFileContentDeleted = SubstanceClientsUtil.DfsVolume.deleteFileContent(dfsVolumeClientResolver, dfsServiceUrl, accessToken, fileMetadata);

						if (!isFileContentDeleted) {
							message = MessageHelper.INSTANCE.add(message, "File content is not deleted. fileId='" + fileId + "'.");
						}
					}
				}

				if (isFileMetadataDeleted) {
					hasSucceed = true;
				} else {
					hasFailed = true;
				}
			}
		} catch (ClientException e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, (fileIds.length > 1) ? "Files are deleted successfully." : "File is deleted successfully.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (fileIds.length > 1) ? "Files are not deleted." : "File is not deleted.");
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/files?parentFileId=" + parentFileId);
	}

}
