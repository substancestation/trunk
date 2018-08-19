package org.orbit.substance.runtime.dfs.filesystem.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.substance.model.dfs.PathDTO;
import org.orbit.substance.runtime.RequestConstants;
import org.orbit.substance.runtime.common.util.ModelConverter;
import org.orbit.substance.runtime.dfs.filesystem.service.FileSystem;
import org.orbit.substance.runtime.dfs.filesystem.service.FileSystemService;
import org.orbit.substance.runtime.model.dfs.Path;
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
		String accessToken = getAccessToken();
		if (accessToken == null) {
			ErrorDTO error = new ErrorDTO("accessToken is not available in http header.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		FileSystemService service = getService();
		FileSystem fileSystem = service.getFileSystem(accessToken);
		if (fileSystem == null) {
			ErrorDTO error = new ErrorDTO("File system is not available for the accessToken.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Path[] rootFiles = fileSystem.listRoots();

		List<PathDTO> pathDTOs = new ArrayList<PathDTO>();
		for (Path path : rootFiles) {
			PathDTO pathDTO = ModelConverter.FILE_SYSTEM.toDTO(path);
			if (pathDTO != null) {
				pathDTOs.add(pathDTO);
			}
		}

		return Response.ok().entity(pathDTOs).build();
	}

}
