package org.orbit.substance.webconsole.servlet.dfs;

import java.io.File;
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
import org.orbit.infra.api.InfraConstants;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.connector.util.ModelConverter;
import org.orbit.substance.io.util.DefaultDfsClientResolver;
import org.orbit.substance.io.util.DefaultDfsVolumeClientResolver;
import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.webconsole.WebConstants;
import org.orbit.substance.webconsole.util.MessageHelper;
import org.origin.common.util.DiskSpaceUnit;
import org.origin.common.util.ServletUtil;

/**
 * Upon receiving file upload submission, parses the request to read upload data and saves the file on disk.
 * 
 * @see org.apache.felix.webconsole.internal.core.BundlesServlet
 * @see AppUploadServlet
 * @see https://github.com/codelibs/commons-fileupload-1.2/blob/master/src/java/org/apache/commons/fileupload/FileUploadBase.java
 * @see https://www.programcreek.com/java-api-examples/?api=com.sun.jersey.multipart.FormDataMultiPart
 */
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -8884733342899016175L;

	protected static final long MEMORY_THRESHOLD = DiskSpaceUnit.MB.toBytes(50);
	protected static final long MAX_FILE_SIZE = DiskSpaceUnit.MB.toBytes(250);
	protected static final long MAX_REQUEST_SIZE = DiskSpaceUnit.MB.toBytes(260);

	// location to store file uploaded
	protected static final String UPLOAD_DIRECTORY = "upload";

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);

		String message = "";
		String parentFileId = ServletUtil.getParameter(request, "parentFileId", "-1");

		// 1. Check http request with multipart form
		boolean isMultipartForm = ServletFileUpload.isMultipartContent(request);
		if (!isMultipartForm) {
			message = MessageHelper.INSTANCE.add(message, "Error: Form must has enctype=multipart/form-data.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		if (isMultipartForm) {
			File dfsUploadDir = null;
			List<File> localFiles = new ArrayList<File>();
			try {
				// 2. Prepare common upload directory
				String platformHome = PlatformSDKActivator.getInstance().getPlatform().getHome();
				String uploadPath = platformHome + File.separator + UPLOAD_DIRECTORY;

				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				// 3. Prepare dfs file upload directory
				dfsUploadDir = new File(uploadDir, "dfs");
				if (!dfsUploadDir.exists()) {
					dfsUploadDir.mkdirs();
				}

				// 4. Configure upload settings
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold((int) MEMORY_THRESHOLD); // sets memory threshold - beyond which files are stored in disk
				factory.setRepository(new File(System.getProperty("java.io.tmpdir"))); // sets temporary location to store files

				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setFileSizeMax(MAX_FILE_SIZE);
				upload.setSizeMax(MAX_REQUEST_SIZE);

				// 5. Parse request
				List<FileItem> fileItems = upload.parseRequest(request);
				if (fileItems == null) {
					fileItems = new ArrayList<FileItem>();
				}

				// 6. Upload file to local folder of the web server
				for (FileItem fileItem : fileItems) {
					if (!fileItem.isFormField()) {
						String fileName = fileItem.getName();
						if (fileName == null || fileName.isEmpty()) {
							continue;
						}
						try {
							File localFile = new File(dfsUploadDir, fileName);
							fileItem.write(localFile);
							localFiles.add(localFile);

							message = MessageHelper.INSTANCE.add(message, "File '" + fileName + "' is uploaded to web server.");

						} catch (Exception e) {
							message = MessageHelper.INSTANCE.add(message, "File '" + fileName + "' is not uploaded to web server. " + e.getMessage());
						}
					} else {
						String itemFieldName = fileItem.getFieldName();
						if ("parentFileId".equals(itemFieldName)) {
							parentFileId = fileItem.getString();
						}
					}
				}

				if (localFiles.isEmpty()) {
					message = MessageHelper.INSTANCE.add(message, "File is not selected.");
				}

				// 7. Create file metadata in dfs and upload file content to dfs volume.
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				DfsClientResolver dfsClientResolver = new DefaultDfsClientResolver();
				DfsVolumeClientResolver dfsVolumeClientResolver = new DefaultDfsVolumeClientResolver(indexServiceUrl);

				for (File localFile : localFiles) {
					// (1) Create file metadata in DFS
					String fileName = localFile.getName();
					long fileSize = localFile.length();
					FileMetadata fileMetadata = SubstanceClientsUtil.Dfs.createNewFile(dfsClientResolver, dfsServiceUrl, accessToken, parentFileId, fileName, fileSize);
					if (fileMetadata == null) {
						message = MessageHelper.INSTANCE.add(message, "File metadata cannot be created in DFS.");
						break;
					} else {
						message = MessageHelper.INSTANCE.add(message, "File metadata is created in DFS.");
					}

					// (2) Upload file to DFS volumes
					boolean isUploaded = SubstanceClientsUtil.DfsVolume.upload(dfsVolumeClientResolver, accessToken, fileMetadata, localFile);
					if (isUploaded) {
						message = MessageHelper.INSTANCE.add(message, "File '" + localFile.getName() + "' is uploaded to DFS volume.");

						// (3) Update file metadata's file parts checksum in DFS
						String fileId = fileMetadata.getFileId();
						List<FilePart> fileParts = fileMetadata.getFileParts();
						String filePartsString = ModelConverter.Dfs.toFilePartsString(fileParts);

						boolean isFilePartsUpdated = SubstanceClientsUtil.Dfs.updateFileParts(dfsClientResolver, dfsServiceUrl, accessToken, fileId, filePartsString);
						if (isFilePartsUpdated) {
							message = MessageHelper.INSTANCE.add(message, "File parts are updated.");
						} else {
							message = MessageHelper.INSTANCE.add(message, "File parts are not updated.");
						}

					} else {
						message = MessageHelper.INSTANCE.add(message, "File '" + localFile.getName() + "' is not uploaded to DFS volume.");
						break;
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, e.getMessage());
				e.printStackTrace();

			} finally {
				// 8. Clean up
				for (File localFile : localFiles) {
					try {
						if (localFile.exists()) {
							localFile.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/files?parentFileId=" + parentFileId);
	}

}
