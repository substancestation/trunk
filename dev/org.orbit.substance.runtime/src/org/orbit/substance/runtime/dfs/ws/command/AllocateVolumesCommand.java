package org.orbit.substance.runtime.dfs.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.runtime.common.ws.AbstractDfsCommand;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.util.RequestUtil;

public class AllocateVolumesCommand extends AbstractDfsCommand<DfsService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs.AllocateVolumesCommand";

	public AllocateVolumesCommand() {
		super(DfsService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.ALLOCATE_VOLUMES.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String accountId = getAccountId();
		if (accountId == null || accountId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("accountId is not available.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		String file_id = (String) request.getParameter("file_id");
		if (file_id == null || file_id.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'file_id' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		// long size = RequestUtil.getParameter(request, "size", Long.class, Long.valueOf(-1));
		long size = 0;
		Object size_obj = request.getParameter("size");
		if (size_obj != null) {
			size = Long.valueOf(size_obj.toString());
		}
		if (size < 0) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'size' parameter is not set or invalid.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		// Get user's FileSystem.
		FileSystem fileSystem = getService().getFileSystem(accountId);

		// Get FileMetadata
		FileMetadata fileMetadata = fileSystem.getFile(file_id);
		if (fileMetadata == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "File doesn't exist.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		// Allocate DFS volumes for the file
		fileMetadata = fileSystem.allocateVolumes(file_id, size);

		// Return FileMetadata with updated volumes information
		FileMetadataDTO fileMetadataDTO = null;
		if (fileMetadata != null) {
			fileMetadataDTO = ModelConverter.Dfs.toDTO(fileMetadata);
		}
		if (fileMetadataDTO != null) {
			return Response.ok().entity(fileMetadataDTO).build();
		} else {
			return Response.ok().build();
		}
	}

}
