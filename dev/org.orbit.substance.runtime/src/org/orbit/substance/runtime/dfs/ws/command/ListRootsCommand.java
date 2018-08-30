package org.orbit.substance.runtime.dfs.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.dfs.service.FileSystemService;
import org.orbit.substance.runtime.model.dfs.FileMetadata;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class ListRootsCommand extends AbstractFileSystemCommand<FileSystemService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs_metadata.ListRootsCommand";

	public ListRootsCommand() {
		super(FileSystemService.class);
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
			FileMetadataDTO fileMetadataDTO = ModelConverter.File_System.toDTO(rootFile);
			if (fileMetadataDTO != null) {
				fileMetadataDTOs.add(fileMetadataDTO);
			}
		}

		return Response.ok().entity(fileMetadataDTOs).build();
	}

}
