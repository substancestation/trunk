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
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.io.util.DfsClientResolverByDfsId;
import org.orbit.substance.io.util.DfsVolumeClientResolverImpl;
import org.orbit.substance.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class FileDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 8325542952806426875L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		// String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);
		String dfsId = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_ID);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

		String parentFileId = ServletUtil.getParameter(request, "parentFileId", "-1");
		String[] fileIds = ServletUtil.getParameterValues(request, "fileId", EMPTY_IDS);

		String message = "";
		if (fileIds.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "'fileId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			DfsClientResolver dfsClientResolver = new DfsClientResolverByDfsId(dfsId);
			DfsVolumeClientResolver dfsVolumeClientResolver = new DfsVolumeClientResolverImpl();

			for (int i = 0; i < fileIds.length; i++) {
				String fileId = fileIds[i];
				// FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.getFile(dfsClientResolver, dfsServiceUrl, accessToken, fileId);
				// if (fileMetadata == null) {
				// message = MessageHelper.INSTANCE.add(message, "File doesn't exist. fileId='" + fileId + "'.");
				// continue;
				// }
				if (!SubstanceClientsUtil.DFS.fileExists(dfsClientResolver, accessToken, fileId)) {
					message = MessageHelper.INSTANCE.add(message, "File doesn't exist. fileId='" + fileId + "'.");
					continue;
				}

				boolean isDeleted = SubstanceClientsUtil.DFS.deleteFile(dfsClientResolver, dfsVolumeClientResolver, accessToken, fileId);
				if (isDeleted) {
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
			message = MessageHelper.INSTANCE.add(message, (fileIds.length > 1) ? "Files are deleted." : "File is deleted.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (fileIds.length > 1) ? "Files are not deleted." : "File is not deleted.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/files?parentFileId=" + parentFileId);
	}

}

// List<FileMetadata> encounteredFiles = new ArrayList<FileMetadata>();
// /**
// *
// * @param dfsClientResolver
// * @param dfsVolumeClientResolver
// * @param dfsServiceUrl
// * @param accessToken
// * @param fileMetadata
// * @param encounteredFiles
// * @return
// * @throws ClientException
// * @throws IOException
// */
// protected boolean delete(DfsClientResolver dfsClientResolver, DfsVolumeClientResolver dfsVolumeClientResolver, String dfsServiceUrl, String accessToken,
// FileMetadata fileMetadata, List<FileMetadata> encounteredFiles) throws ClientException, IOException {
// if (fileMetadata == null) {
// return false;
// }
// if (encounteredFiles.contains(fileMetadata)) {
// return true;
// }
// encounteredFiles.add(fileMetadata);
//
// boolean isDeleted = false;
// String fileId = fileMetadata.getFileId();
//
// if (fileMetadata.isDirectory()) {
// FileMetadata[] memberFiles = SubstanceClientsUtil.Dfs.listFiles(dfsClientResolver, dfsServiceUrl, accessToken, fileId);
// for (FileMetadata memberFile : memberFiles) {
// boolean currDeleted = delete(dfsClientResolver, dfsVolumeClientResolver, dfsServiceUrl, accessToken, memberFile, encounteredFiles);
// if (!currDeleted) {
// return false;
// }
// }
// isDeleted = SubstanceClientsUtil.Dfs.deleteFile(dfsClientResolver, dfsServiceUrl, accessToken, fileId);
//
// } else {
// SubstanceClientsUtil.DfsVolume.deleteFileContent(dfsVolumeClientResolver, dfsServiceUrl, accessToken, fileMetadata);
// isDeleted = SubstanceClientsUtil.Dfs.deleteFile(dfsClientResolver, dfsServiceUrl, accessToken, fileId);
// }
//
// return isDeleted;
// }
