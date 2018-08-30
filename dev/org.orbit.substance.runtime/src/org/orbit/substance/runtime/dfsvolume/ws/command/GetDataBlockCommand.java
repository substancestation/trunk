package org.orbit.substance.runtime.dfsvolume.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfsvolume.DataBlockMetadataDTO;
import org.orbit.substance.runtime.Messages;
import org.orbit.substance.runtime.dfsvolume.service.FileContentService;
import org.orbit.substance.runtime.model.dfsvolume.DataBlockMetadata;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class GetDataBlockCommand extends AbstractFileContentCommand<FileContentService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_content.GetDataBlockCommand";

	public GetDataBlockCommand() {
		super(FileContentService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.GET_DATA_BLOCK.equalsIgnoreCase(requestName)) {
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

		String blockId = (String) request.getParameter("block_id");
		if (blockId == null || blockId.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'blockId' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		DataBlockMetadataDTO dataBlockDTO = null;
		boolean accountAndBlockMatch = false;

		FileContentService service = getService();
		DataBlockMetadata dataBlock = service.getDataBlock(accountId, blockId);
		if (dataBlock != null) {
			String theAccountId = dataBlock.getAccountId();
			if (accountId.equals(theAccountId)) {
				accountAndBlockMatch = true;
				dataBlockDTO = ModelConverter.File_Content.toDTO(dataBlock);
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
