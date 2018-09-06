package org.orbit.substance.runtime.dfsvolume.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfsvolume.DataBlockMetadataDTO;
import org.orbit.substance.runtime.common.ws.AbstractDfsVolumeWSCommand;
import org.orbit.substance.runtime.dfsvolume.service.DataBlockMetadata;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class ListAllDataBlocksCommand extends AbstractDfsVolumeWSCommand<DfsVolumeService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_volume.ListAllDataBlocksCommand";

	public ListAllDataBlocksCommand() {
		super(DfsVolumeService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.LIST_ALL_DATA_BLOCKS.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		List<DataBlockMetadataDTO> dataBlockDTOs = new ArrayList<DataBlockMetadataDTO>();
		DfsVolumeService service = getService();
		DataBlockMetadata[] dataBlocks = service.getDataBlocks();
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
