package org.orbit.substance.webconsole.servlet.dfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.io.util.DefaultDfsClientResolver;
import org.orbit.substance.io.util.DefaultDfsVolumeClientResolver;
import org.orbit.substance.webconsole.WebConstants;
import org.orbit.substance.webconsole.util.MessageHelper;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.MimeTypes;
import org.origin.common.util.ServletUtil;

public class FileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = -5770524896660449763L;

	// location to store file to be downloaded
	protected static final String DOWNLOAD_DIRECTORY = "download";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);

		String message = "";
		String parentFileId = ServletUtil.getParameter(request, "parentFileId", "-1");
		String fileId = ServletUtil.getParameter(request, "fileId", "");
		if (fileId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'fileId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		OutputStream output = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			DfsClientResolver dfsClientResolver = new DefaultDfsClientResolver();
			DfsVolumeClientResolver dfsVolumeClientResolver = new DefaultDfsVolumeClientResolver(indexServiceUrl);

			FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.getFile(dfsClientResolver, dfsServiceUrl, accessToken, fileId);
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

			String fileName = fileMetadata.getName();
			File localFile = new File(fileDownloadDir, fileName);

			output = new FileOutputStream(localFile);

			SubstanceClientsUtil.DfsVolume.download(dfsVolumeClientResolver, accessToken, fileMetadata, output);

			if (localFile.exists()) {
				String fileType = MimeTypes.get().getByFileName(localFile.getName());
				response.setContentType(fileType);
				response.setHeader("Content-disposition", "attachment; filename=" + fileName);

				// ---------------------------------------------------------------
				// Send the file to browser
				// ---------------------------------------------------------------
				FileInputStream input = new FileInputStream(localFile);

				OutputStream out = response.getOutputStream();
				IOUtil.copy(input, out);
				out.flush();
				out.close();

				input.close();
			}

		} catch (ClientException e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();

		} finally {
			IOUtil.closeQuietly(output, true);
		}
	}

}
