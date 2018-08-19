package org.orbit.substance.runtime.dfs.filesystem.ws.command;

import javax.ws.rs.core.Response;

import org.orbit.substance.runtime.RequestConstants;
import org.orbit.substance.runtime.dfs.filesystem.service.FileSystem;
import org.origin.common.rest.editpolicy.ServiceAwareWSCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class DownloadFileCommand extends ServiceAwareWSCommand<FileSystem> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_metadata.DownloadFileCommand";

	public DownloadFileCommand() {
		super(FileSystem.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DOWNLOAD_FILE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		return null;
	}

}
