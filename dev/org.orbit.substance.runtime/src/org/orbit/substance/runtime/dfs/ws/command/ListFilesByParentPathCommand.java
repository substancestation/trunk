package org.orbit.substance.runtime.dfs.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.RequestConstants;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.runtime.common.ws.AbstractDfsCommand;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.dfs.service.FileSystem;
import org.orbit.substance.runtime.util.ModelConverter;
import org.origin.common.resource.Path;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class ListFilesByParentPathCommand extends AbstractDfsCommand<DfsService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs.ListFilesByParentPathCommand";

	public ListFilesByParentPathCommand() {
		super(DfsService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.LIST_FILES_BY_PARENT_PATH.equalsIgnoreCase(requestName)) {
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

		String parent_path = (String) request.getParameter("parent_path");
		if (parent_path == null || parent_path.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'parent_path' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileSystem fileSystem = getService().getFileSystem(accountId);

		Path parentPath = new Path(parent_path);
		FileMetadata[] memberFiles = fileSystem.listFiles(parentPath);

		List<FileMetadataDTO> fileMetadataDTOs = new ArrayList<FileMetadataDTO>();
		for (FileMetadata memberFile : memberFiles) {
			FileMetadataDTO fileMetadataDTO = ModelConverter.Dfs.toDTO(memberFile);
			if (fileMetadataDTO != null) {
				fileMetadataDTOs.add(fileMetadataDTO);
			}
		}

		return Response.ok().entity(fileMetadataDTOs).build();
	}

}
