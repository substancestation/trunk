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
import org.origin.common.resource.Path;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class MkdirCommand extends AbstractDfsCommand<DfsService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs.MkdirCommand";

	public MkdirCommand() {
		super(DfsService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.MKDIRS.equalsIgnoreCase(requestName)) {
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

		String path_str = (String) request.getParameter("path");
		if (path_str == null || path_str.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'path' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileMetadataDTO fileMetadataDTO = null;

		FileSystem fileSystem = getService().getFileSystem(accountId);

		Path path = new Path(path_str);
		FileMetadata existingFileMetadata = fileSystem.getFile(path);
		if (existingFileMetadata != null) {
			// file exists
			if (existingFileMetadata.isDirectory()) {
				// directory already exists
				fileMetadataDTO = ModelConverter.Dfs.toDTO(existingFileMetadata);

			} else {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), String.format("Path '%s' already exists and is a file.", path_str));
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

		} else {
			// file doesn't exist
			FileMetadata newFileMetadata = fileSystem.mkdirs(path);
			if (newFileMetadata != null) {
				fileMetadataDTO = ModelConverter.Dfs.toDTO(newFileMetadata);
			}
		}

		if (fileMetadataDTO == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Directory cannot be created");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		return Response.ok().entity(fileMetadataDTO).build();
	}

}
