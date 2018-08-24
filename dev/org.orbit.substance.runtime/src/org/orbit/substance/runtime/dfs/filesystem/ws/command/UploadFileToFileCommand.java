package org.orbit.substance.runtime.dfs.filesystem.ws.command;

import javax.ws.rs.core.Response;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.runtime.dfs.filesystem.service.FileSystemService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class UploadFileToFileCommand extends AbstractFileSystemCommand<FileSystemService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_metadata.UploadFileToFileCommand";

	public UploadFileToFileCommand() {
		super(FileSystemService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.UPLOAD_FILE_TO_FILE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		return null;
	}

}
