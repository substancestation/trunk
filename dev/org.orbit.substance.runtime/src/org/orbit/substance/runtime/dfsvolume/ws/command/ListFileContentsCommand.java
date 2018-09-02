package org.orbit.substance.runtime.dfsvolume.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfsvolume.FileContentMetadataDTO;
import org.orbit.substance.runtime.Messages;
import org.orbit.substance.runtime.common.ws.AbstractDfsVolumeWSCommand;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.orbit.substance.runtime.model.dfsvolume.DataBlockMetadata;
import org.orbit.substance.runtime.model.dfsvolume.FileContentMetadata;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class ListFileContentsCommand extends AbstractDfsVolumeWSCommand<DfsVolumeService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_content.ListFileContentsCommand";

	public ListFileContentsCommand() {
		super(DfsVolumeService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.LIST_FILE_CONTENTS.equalsIgnoreCase(requestName)) {
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

		List<FileContentMetadataDTO> fileContentDTOs = new ArrayList<FileContentMetadataDTO>();
		boolean accountAndBlockMatch = false;

		DfsVolumeService service = getService();
		DataBlockMetadata dataBlock = service.getDataBlock(accountId, blockId);
		if (dataBlock != null) {
			String theAccountId = dataBlock.getAccountId();
			if (accountId.equals(theAccountId)) {
				accountAndBlockMatch = true;

				FileContentMetadata[] fileContents = service.getFileContentMetadatas(accountId, blockId);
				if (fileContents != null) {
					for (FileContentMetadata fileContent : fileContents) {
						FileContentMetadataDTO fileContentDTO = ModelConverter.DfsVolume.toDTO(fileContent);
						if (fileContentDTO != null) {
							fileContentDTOs.add(fileContentDTO);
						}
					}
				}
			}
		}

		if (!accountAndBlockMatch) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), Messages.ACCOUNT_AND_BLOCK_NOT_MATCH, null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (dataBlock == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Block is not found.", null);
			return Response.status(Status.NOT_FOUND).entity(error).build();

		} else {
			return Response.status(Status.OK).entity(fileContentDTOs).build();
		}
	}

}
