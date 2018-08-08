package org.orbit.substance.runtime.dfs.metadata.ws.command;

import javax.ws.rs.core.Response;

import org.orbit.substance.runtime.dfs.metadata.service.DFSMetadataService;
import org.origin.common.rest.editpolicy.ServiceAwareWSCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class DownloadFileCommand extends ServiceAwareWSCommand<DFSMetadataService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_metadata.DownloadFileCommand";

	public DownloadFileCommand() {
		super(DFSMetadataService.class);
	}

	@Override
	public Response execute(Request request) throws Exception {
		return null;
	}

}
