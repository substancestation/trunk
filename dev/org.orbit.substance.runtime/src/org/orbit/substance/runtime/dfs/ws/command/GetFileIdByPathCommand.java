package org.orbit.substance.runtime.dfs.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfs.Path;
import org.orbit.substance.runtime.common.ws.AbstractDfsCommand;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class GetFileIdByPathCommand extends AbstractDfsCommand<DfsService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs.GetFileIdByPathCommand";

	public GetFileIdByPathCommand() {
		super(DfsService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.GET_FILE_ID_BY_PATH.equalsIgnoreCase(requestName)) {
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

		String fileId = null;
		Path path = new Path(path_str);
		FileMetadata fileMetadata = fileSystem.getFile(path);
		if (fileMetadata != null) {
			fileId = fileMetadata.getFileId();
		}
		if (fileId != null) {
			Map<String, String> result = new HashMap<String, String>();
			result.put("file_id", fileId);
			return Response.status(Status.OK).entity(result).build();
		} else {
			return Response.ok().build();
		}
	}

}
