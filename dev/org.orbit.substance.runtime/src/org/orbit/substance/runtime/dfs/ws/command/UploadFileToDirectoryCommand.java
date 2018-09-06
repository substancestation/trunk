package org.orbit.substance.runtime.dfs.ws.command;

import javax.ws.rs.core.Response;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.runtime.common.ws.AbstractDfsCommand;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class UploadFileToDirectoryCommand extends AbstractDfsCommand<DfsService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs.UploadFileToDirectoryCommand";

	public UploadFileToDirectoryCommand() {
		super(DfsService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.UPLOAD_FILE_TO_DIRECTORY.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		return null;
	}

}
