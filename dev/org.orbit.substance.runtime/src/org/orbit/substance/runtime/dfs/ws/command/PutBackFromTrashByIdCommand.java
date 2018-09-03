package org.orbit.substance.runtime.dfs.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.common.ws.AbstractDfsCommand;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class PutBackFromTrashByIdCommand extends AbstractDfsCommand<DfsService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_metadata.PutBackFromTrashByIdCommand";

	public PutBackFromTrashByIdCommand() {
		super(DfsService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.PUT_BACK_FROM_TRASH_BY_ID.equalsIgnoreCase(requestName)) {
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

		FileSystem fileSystem = getService().getFileSystem(accountId);

		FileMetadata existingFileMetadata = fileSystem.getFile(file_id);
		if (existingFileMetadata == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "File doesn't exist.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileMetadata fileMetadata = fileSystem.putBackFromTrash(file_id);
		FileMetadataDTO fileMetadataDTO = ModelConverter.Dfs.toDTO(fileMetadata);
		return Response.ok().entity(fileMetadataDTO).build();
	}

}
