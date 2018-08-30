package org.orbit.substance.runtime.dfs.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.dfs.service.FileSystemService;
import org.orbit.substance.runtime.model.dfs.FileMetadata;
import org.orbit.substance.runtime.model.dfs.Path;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class CreateNewFileCommand extends AbstractFileSystemCommand<FileSystemService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_metadata.CreateNewFileCommand";

	public CreateNewFileCommand() {
		super(FileSystemService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CREATE_NEW_FILE.equalsIgnoreCase(requestName)) {
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

		FileSystem fileSystem = getService().getFileSystem(accountId);

		Path path = new Path(path_str);
		if (fileSystem.exists(path)) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), String.format("Path '%s' already exists.", path_str));
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileMetadataDTO fileMetadataDTO = null;
		FileMetadata newFileMetadata = fileSystem.createNewFile(path);
		if (newFileMetadata != null) {
			fileMetadataDTO = ModelConverter.File_System.toDTO(newFileMetadata);
		}
		if (fileMetadataDTO == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "File cannot be created");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		return Response.ok().entity(fileMetadataDTO).build();
	}

}
