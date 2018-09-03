package org.orbit.substance.runtime.dfs.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.common.ws.AbstractDfsCommand;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class ListRootsCommand extends AbstractDfsCommand<DfsService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_metadata.ListRootsCommand";

	public ListRootsCommand() {
		super(DfsService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.LIST_ROOTS.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String accountId = getAccountId();
		if (accountId == null || accountId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("accountId is not available.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileSystem fileSystem = getService().getFileSystem(accountId);

		FileMetadata[] rootFiles = fileSystem.listRoots();

		List<FileMetadataDTO> fileMetadataDTOs = new ArrayList<FileMetadataDTO>();
		for (FileMetadata rootFile : rootFiles) {
			FileMetadataDTO fileMetadataDTO = ModelConverter.Dfs.toDTO(rootFile);
			if (fileMetadataDTO != null) {
				fileMetadataDTOs.add(fileMetadataDTO);
			}
		}

		return Response.ok().entity(fileMetadataDTOs).build();
	}

}
