package org.orbit.substance.runtime.dfs.filesystem.service;

import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;

public interface FileSystemService extends WebServiceAware, EditPoliciesAwareService {

	String getDfsId();

	/**
	 * 
	 * @param accountId
	 * @return
	 * @throws ServerException
	 */
	FileSystem getFileSystem(String accountId) throws ServerException;

}
