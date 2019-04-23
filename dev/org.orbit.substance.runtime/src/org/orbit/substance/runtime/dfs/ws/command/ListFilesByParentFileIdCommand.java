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
import org.orbit.substance.runtime.util.RuntimeModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class ListFilesByParentFileIdCommand extends AbstractDfsCommand<DfsService> implements WSCommand {

	public static String ID = "org.orbit.substance.runtime.dfs.ListFilesByParentFileIdCommand";

	public ListFilesByParentFileIdCommand() {
		super(DfsService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.LIST_FILES_BY_PARENT_ID.equalsIgnoreCase(requestName)) {
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

		String parent_file_id = (String) request.getParameter("parent_file_id");
		if (parent_file_id == null || parent_file_id.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'parent_file_id' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileSystem fileSystem = getService().getFileSystem(accountId);

		FileMetadata[] memberFiles = fileSystem.listFiles(parent_file_id);

		List<FileMetadataDTO> fileMetadataDTOs = new ArrayList<FileMetadataDTO>();
		for (FileMetadata memberFile : memberFiles) {
			FileMetadataDTO fileMetadataDTO = RuntimeModelConverter.Dfs.toDTO(memberFile);
			if (fileMetadataDTO != null) {
				fileMetadataDTOs.add(fileMetadataDTO);
			}
		}

		return Response.ok().entity(fileMetadataDTOs).build();
	}

}
