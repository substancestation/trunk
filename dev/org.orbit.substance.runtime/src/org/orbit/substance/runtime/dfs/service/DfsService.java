package org.orbit.substance.runtime.dfs.service;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.PropertiesAware;
import org.origin.common.service.WebServiceAware;

public interface DfsService extends WebServiceAware, PropertiesAware, ConnectionAware, EditPoliciesAwareService {

	/**
	 * 
	 * @return
	 */
	String getDfsId();

	/**
	 * 
	 * @param accountId
	 * @return
	 * @throws ServerException
	 */
	FileSystem getFileSystem(String accountId) throws ServerException;

}
