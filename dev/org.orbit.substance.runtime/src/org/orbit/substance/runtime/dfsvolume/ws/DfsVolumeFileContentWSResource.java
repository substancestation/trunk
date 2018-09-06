package org.orbit.substance.runtime.dfsvolume.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.orbit.substance.model.dfsvolume.FileContentMetadataDTO;
import org.orbit.substance.model.dfsvolume.PendingFile;
import org.orbit.substance.model.dfsvolume.PendingFileImpl;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.dfsvolume.service.DataBlockMetadata;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.orbit.substance.runtime.dfsvolume.service.FileContentMetadata;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.util.DiskSpaceUnit;

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
	public DfsVolumeService service;

	public DfsVolumeService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DfsVolumeService is not available.");
		}
		return this.service;
	}

	/**
	 * Upload a file.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/file/content (FormData: InputStream and FormDataContentDisposition)
	 * 
	 * @param httpHeaders
	 * @param fileId
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
			@QueryParam(value = "accountId") String accountId, //
			@QueryParam(value = "blockId") String blockId, //
			@QueryParam(value = "capacity") long capacity, //
			@QueryParam(value = "fileId") String fileId, //
			@QueryParam(value = "partId") int partId, //
			@QueryParam(value = "size") long size, //
			@QueryParam(value = "checksum") long checksum, //
			@FormDataParam("file") InputStream contentInputStream, //
			@FormDataParam("file") FormDataContentDisposition fileDetail //
	) {
		// Note:
		// - If accountId query parameter is set, the access token must belong to the dfs service.
		// - If accountId is not set, get the accountId from the access token. The access token must belong to a user.
		if (accountId != null) {
			// validate the access token to be the dfs service.
		} else {
			// validate the access token to be a user.
			accountId = OrbitTokenUtil.INSTANCE.getAccountId(httpHeaders, PlatformConstants.TOKEN_PROVIDER__ORBIT);
		}
		if (accountId == null || accountId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("accountId is not available.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (fileId == null || fileId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("fileId is null.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileContentMetadata fileContent = null;
		boolean isDataBlockCreated = false;
		boolean isFileContentCreated = false;
		boolean isContentSet = false;
		try {
			DfsVolumeService service = getService();

			// 1. Create or get data block.
			DataBlockMetadata dataBlock = null;
			if (blockId == null || blockId.isEmpty() || "-1".equals(blockId)) {
				// Create new data block
				if (capacity <= 0) {
					capacity = service.getDefaultBlockCapacity();
				}
				if (capacity <= 0) {
					capacity = DiskSpaceUnit.MB.toBytes(100);
				}

				List<PendingFile> pendingFiles = new ArrayList<PendingFile>();
				PendingFile pendingFile = new PendingFileImpl(fileId, size);
				pendingFiles.add(pendingFile);

				dataBlock = service.createDataBlock(accountId, capacity, pendingFiles);
				if (dataBlock == null) {
					ErrorDTO error = new ErrorDTO("New data block cannot be created.");
					return Response.status(Status.BAD_REQUEST).entity(error).build();
				}
				isDataBlockCreated = true;
				blockId = dataBlock.getBlockId();

			} else {
				// Get existing data block
				dataBlock = service.getDataBlock(accountId, blockId);
				if (dataBlock == null) {
					ErrorDTO error = new ErrorDTO("Data block is not found.");
					return Response.status(Status.BAD_REQUEST).entity(error).build();
				}
			}

			// 2. Get or create file content record (for fileId + partId)
			fileContent = service.getFileContent(accountId, blockId, fileId, partId);
			if (fileContent == null) {
				fileContent = service.createFileContent(accountId, blockId, fileId, partId, size, checksum);
				if (fileContent != null) {
					isFileContentCreated = true;
				}
			}
			if (fileContent == null) {
				ErrorDTO error = new ErrorDTO("File content record cannot be created.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			// 3. Set file content to the record
			isContentSet = service.setContent(accountId, blockId, fileId, partId, contentInputStream);

			// 4. Update data block
			if (isContentSet) {
				// (1) Update the size of the data block.
				service.updateDataBlockSizeByDelta(accountId, blockId, size);

				// (2) Remove the pending file record for reserving space from the data block.
				service.updatePendingFiles(accountId, blockId, fileId, true);
			}

		} catch (Exception e) {
			ErrorDTO error = handleError(e, "500", true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();

		} finally {
			IOUtil.closeQuietly(contentInputStream, true);

			// 5. Date cleanup, when file content cannot be set.
			try {
				DfsVolumeService service = getService();
				if (!isContentSet) {
					if (isFileContentCreated) {
						// delete the newly created file content record
						service.deleteFileContent(accountId, blockId, fileId, partId);
					}
					if (isDataBlockCreated) {
						// delete the newly created data block record
						service.deleteDataBlock(accountId, blockId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 6. Return the file content DTO (if succeed) or return error status (if failed to set file content).
		if (!isContentSet) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "File content is not set.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}

		FileContentMetadataDTO fileContentDTO = ModelConverter.DfsVolume.toDTO(fileContent);
		return Response.ok().entity(fileContentDTO).build();
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
			// FileSystem fileSystem = getService().getFileSystem(accountId);
			FileSystem fileSystem = null;

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

			// input = fileSystem.getFileContentInputStream(fileId);
			// if (input != null) {
			// bytes = IOUtil.toByteArray(input);
			// }

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
