package org.orbit.substance.runtime.dfs.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.dfs.service.FileSystemService;
import org.orbit.substance.runtime.model.dfs.Path;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class FileIsDirectoryCommand extends AbstractFileSystemCommand<FileSystemService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_metadata.FileIsDirectoryCommand";

	public FileIsDirectoryCommand() {
		super(FileSystemService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.IS_DIRECTORY.equalsIgnoreCase(requestName)) {
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
		boolean isDirectory = fileSystem.isDirectory(path);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("isDirectory", isDirectory);
		return Response.status(Status.OK).entity(result).build();
	}

}
