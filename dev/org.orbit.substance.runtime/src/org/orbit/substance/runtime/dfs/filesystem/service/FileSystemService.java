package org.orbit.substance.runtime.dfs.filesystem.service;

import org.origin.common.rest.editpolicy.ServiceEditPoliciesAware;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;

public interface FileSystemService extends WebServiceAware, ServiceEditPoliciesAware {

	String getUUID();

	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	FileSystem getFileSystem(String accessToken) throws ServerException;

}
