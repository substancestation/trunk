package org.orbit.substance.runtime.dfsvolume.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.runtime.Messages;
import org.orbit.substance.runtime.common.ws.AbstractDfsVolumeWSCommand;
import org.orbit.substance.runtime.dfsvolume.service.DataBlockMetadata;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class UpdateDataBlockSizeByDeltaCommand extends AbstractDfsVolumeWSCommand<DfsVolumeService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_volume.UpdateDataBlockSizeByDeltaCommand";

	public UpdateDataBlockSizeByDeltaCommand() {
		super(DfsVolumeService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.UPDATE_DATA_BLOCK_SIZE_BY_DELTA.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String accountId = (String) request.getParameter("account_id");
		if (accountId == null || accountId.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'account_id' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		String blockId = (String) request.getParameter("block_id");
		if (blockId == null || blockId.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'block_id' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Object sizeObj = request.getParameter("size_delta");
		if (sizeObj == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'size_delta' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		long sizeDelta = (Long) request.getParameter("size_delta");

		boolean exists = false;
		boolean accountAndBlockMatch = false;

		DfsVolumeService service = getService();
		DataBlockMetadata dataBlock = service.getDataBlock(accountId, blockId);
		if (dataBlock != null) {
			String theAccountId = dataBlock.getAccountId();
			if (accountId.equals(theAccountId)) {
				accountAndBlockMatch = true;
				exists = true;
			}
		}

		if (!accountAndBlockMatch) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), Messages.ACCOUNT_AND_BLOCK_NOT_MATCH, null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (!exists) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Data block is not found.");
			return Response.status(Status.NOT_FOUND).entity(error).build();
		}

		boolean succeed = service.updateDataBlockSizeByDelta(accountId, blockId, sizeDelta);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
