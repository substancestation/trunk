package org.orbit.substance.runtime.dfs.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.runtime.common.ws.AbstractDfsCommand;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.util.RuntimeModelConverter;
import org.origin.common.resource.Path;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class CreateDirectoryCommand extends AbstractDfsCommand<DfsService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs.CreateDirectoryCommand";

	public CreateDirectoryCommand() {
		super(DfsService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CREATE_DIRECTORY.equalsIgnoreCase(requestName)) {
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
		String parent_file_id = (String) request.getParameter("parent_file_id");
		String file_name = (String) request.getParameter("file_name");

		boolean createByPath = false;
		boolean createByParentIdAndName = false;
		if (path_str != null && !path_str.isEmpty()) {
			createByPath = true;
		} else if (file_name != null && !file_name.isEmpty()) {
			if (parent_file_id == null || parent_file_id.isEmpty()) {
				parent_file_id = "-1";
			}
			createByParentIdAndName = true;
		}
		if (!createByPath && !createByParentIdAndName) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'path' OR 'parent_file_id' and 'file_name' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileMetadata newDirectoryMetadata = null;

		FileSystem fileSystem = getService().getFileSystem(accountId);
		if (createByPath) {
			Path path = new Path(path_str);
			if (fileSystem.exists(path)) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), String.format("Path '%s' already exists.", path_str));
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
			newDirectoryMetadata = fileSystem.createDirectory(path);

		} else if (createByParentIdAndName) {
			if (!"-1".equals(parent_file_id)) {
				FileMetadata parentFileMetadata = fileSystem.getFile(parent_file_id);
				if (parentFileMetadata == null) {
					ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), String.format("Parent file with file id '%s' is not found.", parent_file_id));
					return Response.status(Status.BAD_REQUEST).entity(error).build();
				}
			}
			if (fileSystem.exists(parent_file_id, file_name)) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "File already exists.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
			newDirectoryMetadata = fileSystem.createDirectory(parent_file_id, file_name);
		}

		if (newDirectoryMetadata == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "File cannot be created");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileMetadataDTO fileMetadataDTO = RuntimeModelConverter.Dfs.toDTO(newDirectoryMetadata);
		return Response.ok().entity(fileMetadataDTO).build();
	}

}
