package org.orbit.substance.runtime.dfs.datastorage.ws.command;

import org.orbit.substance.runtime.dfs.datastorage.service.DFSContentService;
import org.origin.common.rest.editpolicy.AbstractServiceEditPolicy;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class DFSContentEditPolicy extends AbstractServiceEditPolicy {

	public static final String ID = "substance.dfs_content.editpolicy";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public WSCommand getCommand(Request request) {
		DFSContentService service = getService(DFSContentService.class);
		if (service == null) {
			return null;
		}

		String requestName = request.getRequestName();

		return null;
	}

}
