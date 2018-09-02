package org.orbit.substance.runtime.dfsvolume.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfsvolume.DataBlockMetadataDTO;
import org.orbit.substance.runtime.common.ws.AbstractDfsVolumeWSCommand;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.orbit.substance.runtime.model.dfsvolume.DataBlockMetadata;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class ListDataBlocksCommand extends AbstractDfsVolumeWSCommand<DfsVolumeService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_content.ListDataBlocksCommand";

	public ListDataBlocksCommand() {
		super(DfsVolumeService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.LIST_DATA_BLOCKS.equalsIgnoreCase(requestName)) {
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

		List<DataBlockMetadataDTO> dataBlockDTOs = new ArrayList<DataBlockMetadataDTO>();
		DfsVolumeService service = getService();
		DataBlockMetadata[] dataBlocks = service.getDataBlocks(accountId);
		if (dataBlocks != null) {
			for (DataBlockMetadata dataBlock : dataBlocks) {
				DataBlockMetadataDTO dataBlockDTO = ModelConverter.DfsVolume.toDTO(dataBlock);
				if (dataBlockDTO != null) {
					dataBlockDTOs.add(dataBlockDTO);
				}
			}
		}
		return Response.status(Status.OK).entity(dataBlockDTOs).build();
	}

}
