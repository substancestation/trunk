package org.orbit.substance.runtime.dfs.filesystem.ws;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.platform.sdk.http.OrbitRoles;
import org.orbit.substance.runtime.dfs.filesystem.service.FileSystemService;
import org.origin.common.annotation.AnnotationUtil;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * DFS metadata web service resource.
 * 
 * {contextRoot} example: /orbit/v1/dfs_metadata
 *
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
 * 
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.USER })
@javax.ws.rs.Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class FileSystemWSResource extends AbstractWSApplicationResource {

	@Inject
	public FileSystemService service;

	public FileSystemService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("FileSystemService is not available.");
		}
		return this.service;
	}

	@POST
	@Path("request")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response request(@Context HttpHeaders httpHeaders, Request request) {
		FileSystemService service = getService();

		WSCommand command = service.getEditPolicies().getCommand(request);
		if (command != null) {
			try {
				// Inject HttpHeaders to the command, so the command can get the access token (and username from it) for accessing the user's file system.
				try {
					AnnotationUtil.fieldInject(command, Inject.class, HttpHeaders.class, httpHeaders);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return command.execute(request);

			} catch (Exception e) {
				String statusCode = String.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode());
				ErrorDTO error = handleError(e, statusCode, true);
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
			}
		} else {
			ErrorDTO error = new ErrorDTO("Request '" + request.getRequestName() + "' is not supported.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
	}

}
