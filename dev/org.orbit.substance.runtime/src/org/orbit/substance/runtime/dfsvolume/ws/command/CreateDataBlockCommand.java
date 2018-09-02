package org.orbit.substance.runtime.dfsvolume.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfsvolume.DataBlockMetadataDTO;
import org.orbit.substance.runtime.Messages;
import org.orbit.substance.runtime.common.ws.AbstractDfsVolumeWSCommand;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.orbit.substance.runtime.model.dfsvolume.DataBlockMetadata;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class CreateDataBlockCommand extends AbstractDfsVolumeWSCommand<DfsVolumeService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_content.CreateDataBlockCommand";

	public CreateDataBlockCommand() {
		super(DfsVolumeService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CREATE_DATA_BLOCK.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String accountId = (String) request.getParameter("account_id");
		if (accountId == null || accountId.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'accountId' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		long capacity = (long) request.getParameter("capacity");
		if (capacity <= 0) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'capacity' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		DataBlockMetadataDTO dataBlockDTO = null;
		boolean accountAndBlockMatch = false;

		DfsVolumeService service = getService();
		DataBlockMetadata dataBlock = service.createDataBlock(accountId, capacity);
		if (dataBlock != null) {
			String theAccountId = dataBlock.getAccountId();
			if (accountId.equals(theAccountId)) {
				accountAndBlockMatch = true;
				dataBlockDTO = ModelConverter.DfsVolume.toDTO(dataBlock);
			}
		}

		if (!accountAndBlockMatch) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), Messages.ACCOUNT_AND_BLOCK_NOT_MATCH, null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (dataBlockDTO == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Block is not found.");
			return Response.status(Status.NOT_FOUND).entity(error).build();

		} else {
			return Response.status(Status.OK).entity(dataBlockDTO).build();
		}
	}

}
