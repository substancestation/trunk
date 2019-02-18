package org.orbit.substance.webconsole.servlet.dfs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.api.util.SubstanceClientsHelper;
import org.orbit.substance.io.util.DfsClientResolverImpl;
import org.orbit.substance.io.util.DfsVolumeClientResolverImpl;
import org.orbit.substance.webconsole.WebConstants;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class FileContentServlet extends HttpServlet {

	private static final long serialVersionUID = 603939213946101366L;

	// location to store file to be downloaded
	protected static final String DOWNLOAD_DIRECTORY = "download";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

		String message = "";
		String parentFileId = ServletUtil.getParameter(request, "parentFileId", "-1");
		String fileId = ServletUtil.getParameter(request, "fileId", "");
		if (fileId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'fileId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		// OutputStream output = null;
		ByteArrayOutputStream output = null;
		String fileName = null;
		String fileContent = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			DfsClientResolver dfsClientResolver = new DfsClientResolverImpl(indexServiceUrl);
			DfsVolumeClientResolver dfsVolumeClientResolver = new DfsVolumeClientResolverImpl(indexServiceUrl);

			FileMetadata fileMetadata = SubstanceClientsHelper.Dfs.getFile(dfsClientResolver, dfsServiceUrl, accessToken, fileId);
			if (fileMetadata == null) {
				message = MessageHelper.INSTANCE.add(message, "File is not found.");

				// ---------------------------------------------------------------
				// Render data
				// ---------------------------------------------------------------
				HttpSession session = request.getSession(true);
				session.setAttribute("message", message);

				response.sendRedirect(contextRoot + "/files?parentFileId=" + parentFileId);
				return;
			}

			// Prepare download directory
			String platformHome = PlatformSDKActivator.getInstance().getPlatform().getHome();
			String downloadPath = platformHome + File.separator + DOWNLOAD_DIRECTORY;

			File downloadDir = new File(downloadPath);
			if (!downloadDir.exists()) {
				downloadDir.mkdirs();
			}
			File dfsDownloadDir = new File(downloadDir, "dfs");
			if (!dfsDownloadDir.exists()) {
				dfsDownloadDir.mkdirs();
			}
			File fileDownloadDir = new File(downloadDir, fileId);
			if (!fileDownloadDir.exists()) {
				fileDownloadDir.mkdirs();
			}

			fileName = fileMetadata.getName();

			// File localFile = new File(fileDownloadDir, fileName);
			// output = new FileOutputStream(localFile);
			output = new ByteArrayOutputStream();

			SubstanceClientsHelper.DfsVolume.download(dfsVolumeClientResolver, accessToken, fileMetadata, output);

			fileContent = output.toString("UTF-8"); //$NON-NLS-1$

			// if (localFile.exists()) {
			// FileInputStream input = new FileInputStream(localFile);
			//
			// // String fileContent = IOUtil.toString(input, "utf-8");
			// // ByteArrayOutputStream output = null;
			// try {
			// output = new ByteArrayOutputStream();
			// IOUtil.copy(input, output);
			// fileContent = output.toString("UTF-8"); //$NON-NLS-1$
			//
			// } finally {
			// if (output != null) {
			// output.close();
			// }
			// }
			//
			// input.close();
			// }
		} catch (ClientException e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();

		} finally {
			IOUtil.closeQuietly(output, true);
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("fileId", fileId);
		if (fileName != null) {
			request.setAttribute("fileName", fileName);
		}
		if (fileContent != null) {
			request.setAttribute("fileContent", fileContent);
		}

		request.getRequestDispatcher(contextRoot + "/views/dfs_file_content.jsp").forward(request, response);
	}

}
