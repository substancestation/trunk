package org.orbit.substance.webconsole.servlet.dfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.webconsole.WebConstants;
import org.orbit.substance.webconsole.util.MessageHelper;

/**
 * Upon receiving file upload submission, parses the request to read upload data and saves the file on disk.
 * 
 * @see org.apache.felix.webconsole.internal.core.BundlesServlet
 * @see AppUploadServlet
 */
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -8884733342899016175L;

	// location to store file uploaded
	protected static final String UPLOAD_DIRECTORY = "upload";

	// upload settings
	protected static final int MEMORY_THRESHOLD = 10 * 1024 * 1024; // 10MB
	protected static final int MAX_FILE_SIZE = 250 * 1024 * 1024; // 250MB
	protected static final int MAX_REQUEST_SIZE = 260 * 1024 * 1024; // 260MB

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);
		String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);

		String message = "";

		String parentFileId = (String) request.getParameter("parentFileId");
		if (parentFileId == null || parentFileId.isEmpty()) {
			parentFileId = "-1";
		}

		// 1. Check multipart form
		boolean isMultipartForm = ServletFileUpload.isMultipartContent(request);
		if (!isMultipartForm) {
			message = MessageHelper.INSTANCE.add(message, "Error: Form must has enctype=multipart/form-data.");
		}

		if (isMultipartForm) {
			java.io.File dfsUploadDir = null;
			try {
				// 2. Prepare upload directory
				String platformHome = PlatformSDKActivator.getInstance().getPlatform().getHome();
				String uploadPath = platformHome + java.io.File.separator + UPLOAD_DIRECTORY;

				java.io.File uploadDir = new java.io.File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				// 3. Configure upload settings
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(MEMORY_THRESHOLD); // sets memory threshold - beyond which files are stored in disk
				factory.setRepository(new java.io.File(System.getProperty("java.io.tmpdir"))); // sets temporary location to store files

				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setFileSizeMax(MAX_FILE_SIZE);
				upload.setSizeMax(MAX_REQUEST_SIZE);

				// 4. Parse request
				List<FileItem> fileItems = upload.parseRequest(request);
				if (fileItems == null) {
					fileItems = new ArrayList<FileItem>();
				}

				// 6. Prepare file upload directory
				dfsUploadDir = new java.io.File(uploadDir, "dfs");
				if (!dfsUploadDir.exists()) {
					dfsUploadDir.mkdirs();
				}

				// 7. Upload file to local folder (of the web server)
				List<java.io.File> localFiles = new ArrayList<java.io.File>();
				for (FileItem fileItem : fileItems) {
					if (!fileItem.isFormField()) {
						String fileName = fileItem.getName();
						try {
							java.io.File localFile = new java.io.File(dfsUploadDir, fileName);
							fileItem.write(localFile);
							localFiles.add(localFile);

							message = MessageHelper.INSTANCE.add(message, "File '" + fileName + "' is uploaded to web server.");

						} catch (Exception e) {
							message = MessageHelper.INSTANCE.add(message, "File '" + fileName + "' is not uploaded to web server. " + e.getMessage());
						}
					}
				}

				// 8. Upload file to dfs server
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				List<FileMetadata> files = new ArrayList<FileMetadata>();
				for (java.io.File localFile : localFiles) {
					FileMetadata file = SubstanceClientsUtil.Dfs.createNewFile(dfsServiceUrl, accessToken, parentFileId, localFile);
					if (file != null) {
						message = MessageHelper.INSTANCE.add(message, "File metadata is created in DFS successfully!");
						files.add(file);
					} else {
						message = MessageHelper.INSTANCE.add(message, "File metadata is not created to DFS.");
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();

			} finally {
				// 9. Clean up
				if (dfsUploadDir != null && dfsUploadDir.exists()) {
					try {
						// dfsUploadDir.delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/files");
	}

}

// org.orbit.substance.api.dfs.File file = SubstanceClientsUtil.Dfs.uploadFile(dfsServiceUrl, accessToken, parentFileId, localFiles);
// if (file != null) {
// message = MessageHelper.INSTANCE.add(message, "File is uploaded to DFS successfully!");
// } else {
// message = MessageHelper.INSTANCE.add(message, "File is not uploaded to DFS.");
// }
