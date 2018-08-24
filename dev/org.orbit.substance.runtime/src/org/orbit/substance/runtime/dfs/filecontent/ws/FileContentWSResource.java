package org.orbit.substance.runtime.dfs.filecontent.ws;

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
import org.orbit.substance.runtime.dfs.filecontent.service.FileContentService;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * File content web service resource.
 * 
 * {contextRoot} example: /orbit/v1/dfs_content
 *
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
 * 
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.USER })
@javax.ws.rs.Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class FileContentWSResource extends AbstractWSApplicationResource {

	@Inject
	public FileContentService service;

	@Override
	public FileContentService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("FileContentService is not available.");
		}
		return this.service;
	}

	@POST
	@Path("request")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response request(@Context HttpHeaders httpHeaders, Request request) {
		EditPoliciesAwareService editPoliciesAwareService = null;
		Object service = getService();
		if (service instanceof EditPoliciesAwareService) {
			editPoliciesAwareService = (EditPoliciesAwareService) service;
		}

		if (editPoliciesAwareService == null) {
			ErrorDTO error = new ErrorDTO("The 'request' POST method is not supported by service.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		WSCommand command = editPoliciesAwareService.getEditPolicies().getCommand(httpHeaders, request);
		if (command != null) {
			try {
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

// @POST
// @Path("request")
// @Consumes(MediaType.APPLICATION_JSON)
// public Response request(@Context HttpHeaders httpHeaders, Request request) {
// EditPoliciesAwareService service = getService();
//
// WSCommand command = service.getEditPolicies().getCommand(httpHeaders, request);
// if (command != null) {
// try {
// return command.execute(request);
//
// } catch (Exception e) {
// String statusCode = String.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode());
// ErrorDTO error = handleError(e, statusCode, true);
// return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
// }
// } else {
// ErrorDTO error = new ErrorDTO("Request '" + request.getRequestName() + "' is not supported.");
// return Response.status(Status.BAD_REQUEST).entity(error).build();
// }
// }
