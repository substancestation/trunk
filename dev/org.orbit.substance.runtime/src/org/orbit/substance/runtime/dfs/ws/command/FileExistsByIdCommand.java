package org.orbit.substance.runtime.dfs.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.runtime.common.ws.AbstractDfsCommand;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class FileExistsByIdCommand extends AbstractDfsCommand<DfsService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs.FileExistsByIdCommand";

	public FileExistsByIdCommand() {
		super(DfsService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.FILE_ID_EXISTS.equalsIgnoreCase(requestName)) {
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
		boolean exists = fileSystem.exists(file_id);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("exists", exists);
		return Response.status(Status.OK).entity(result).build();
	}

}
