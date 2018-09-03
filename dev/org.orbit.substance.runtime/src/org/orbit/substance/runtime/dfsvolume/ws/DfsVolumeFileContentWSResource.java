package org.orbit.substance.runtime.dfsvolume.ws;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * DFS file web service resource.
 * 
 * {contextRoot} example: /orbit/v1/dfs
 *
 * // Upload a file.
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/file/content (FormData: InputStream and FormDataContentDisposition)
 * 
 * // Download a file.
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/file/content
 * 
 * @see AppStoreWSAppResource
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.USER })
@javax.ws.rs.Path("/file")
@Produces(MediaType.APPLICATION_JSON)
public class DfsVolumeFileContentWSResource extends AbstractWSApplicationResource {

	@Inject
	public DfsService service;

	public DfsService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DfsService is not available.");
		}
		return this.service;
	}

	/**
	 * Upload a file.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/file/content (FormData: InputStream and FormDataContentDisposition)
	 * 
	 * @param httpHeaders
	 * @param parentType
	 * @param parentValue
	 * @param contentInputStream
	 * @param fileDetail
	 * @return
	 */
	@POST
	@Path("/content")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	public Response setContent( //
			@Context HttpHeaders httpHeaders, //
			@QueryParam(value = "parentType") String parentType, // "fileId" or "path"
			@QueryParam(value = "parentValue") String parentValue, // fileId string or path string
			@FormDataParam("file") InputStream contentInputStream, //
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		String accountId = OrbitTokenUtil.INSTANCE.getAccountId(httpHeaders, PlatformConstants.TOKEN_PROVIDER__ORBIT);
		if (accountId == null || accountId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("accountId is not available.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (parentType == null || parentType.isEmpty()) {
			ErrorDTO error = new ErrorDTO("parentType is null.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (parentValue == null || parentValue.isEmpty()) {
			ErrorDTO error = new ErrorDTO("parentValue is null.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (!"fileId".equals(parentType) && !"path".equals(parentType)) {
			ErrorDTO error = new ErrorDTO("parentType is not supported. Supported parentType is: 'fileId' and 'path'.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileMetadata newFile = null;
		boolean succeed = false;
		try {
			FileSystem fileSystem = getService().getFileSystem(accountId);

			String parentFileId = null;
			org.orbit.substance.model.dfs.Path parentPath = null;
			if ("fileId".equals(parentType)) {
				parentFileId = parentValue;
			} else if ("path".equals(parentType)) {
				parentPath = new org.orbit.substance.model.dfs.Path(parentValue);
			}

			boolean isRootDir = ("-1".equals(parentFileId)) ? true : false;

			FileMetadata parentFile = null;
			if (parentFileId != null) {
				parentFile = fileSystem.getFile(parentFileId);

			} else if (parentPath != null) {
				parentFile = fileSystem.getFile(parentPath);
				if (parentFile == null) {
					parentFile = fileSystem.mkdirs(parentPath);
				}
			}

			if (parentFile == null && !isRootDir) {
				ErrorDTO error = new ErrorDTO("Parent file is not available.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
			if (parentFile != null && !parentFile.isDirectory()) {
				ErrorDTO error = new ErrorDTO("Parent file is not directory.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			String fileName = fileDetail.getFileName();
			String newFileName = null;
			int index = 0;
			FileMetadata[] memberFiles = isRootDir ? fileSystem.listRoots() : fileSystem.listFiles(parentFile.getParentFileId());
			while (true) {
				boolean exists = false;
				String candidateFileName = (index == 0) ? fileName : fileName + "(" + index + ")";

				for (FileMetadata memberFile : memberFiles) {
					String currFileName = memberFile.getName();
					if (candidateFileName.equals(currFileName)) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					newFileName = candidateFileName;
					break;
				}
				index++;
			}

			org.orbit.substance.model.dfs.Path theParentPath = isRootDir ? org.orbit.substance.model.dfs.Path.ROOT : parentFile.getPath();
			org.orbit.substance.model.dfs.Path newFilePath = new org.orbit.substance.model.dfs.Path(theParentPath, newFileName);

			newFile = fileSystem.createNewFile(newFilePath, 0);
			if (newFile == null) {
				ErrorDTO error = new ErrorDTO("New file cannot be created.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			String fileId = newFile.getFileId();
			succeed = fileSystem.setFileContent(fileId, contentInputStream);
			if (!succeed) {
				StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "File content is not set.");
				return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
			}

		} catch (IOException e) {
			ErrorDTO error = handleError(e, "500", true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();

		} finally {
			IOUtil.closeQuietly(contentInputStream, true);
		}

		FileMetadataDTO newFileMetadataDTO = ModelConverter.Dfs.toDTO(newFile);
		return Response.ok().entity(newFileMetadataDTO).build();
	}

	/**
	 * Download a file.
	 *
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/file/content
	 * 
	 * @param type:
	 *            "fileId" or "path"
	 * @param value:
	 *            fileId string or path string
	 * @return
	 */
	@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.USER })
	@GET
	@Path("/content")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM })
	public Response getContent( //
			@Context HttpHeaders httpHeaders, //
			@QueryParam("type") String type, //
			@QueryParam("value") String value) {

		String accountId = OrbitTokenUtil.INSTANCE.getAccountId(httpHeaders, PlatformConstants.TOKEN_PROVIDER__ORBIT);
		if (accountId == null || accountId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("accountId is not available.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (type == null || type.isEmpty()) {
			ErrorDTO error = new ErrorDTO("type is null.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (value == null || value.isEmpty()) {
			ErrorDTO error = new ErrorDTO("value is null.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		String fileName = null;
		byte[] bytes = null;

		InputStream input = null;
		try {
			FileSystem fileSystem = getService().getFileSystem(accountId);

			FileMetadata file = null;
			org.orbit.substance.model.dfs.Path path = null;
			if ("fileId".equals(type)) {
				String fileId = value;
				file = fileSystem.getFile(fileId);
			} else if ("path".equals(type)) {
				path = new org.orbit.substance.model.dfs.Path(value);
				file = fileSystem.getFile(path);
			}

			if (file == null) {
				ErrorDTO error = new ErrorDTO("File is not found.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			String fileId = file.getFileId();
			fileName = file.getName();

			input = fileSystem.getFileContentInputStream(fileId);
			if (input != null) {
				bytes = IOUtil.toByteArray(input);
			}

		} catch (IOException e) {
			ErrorDTO error = handleError(e, StatusDTO.RESP_500, true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();

		} finally {
			IOUtil.closeQuietly(input, true);
		}

		if (bytes == null) {
			bytes = new byte[0];
		}

		return Response.ok(bytes, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + fileName).build();
	}

}
