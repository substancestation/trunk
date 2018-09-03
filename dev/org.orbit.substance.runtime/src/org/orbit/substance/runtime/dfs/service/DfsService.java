package org.orbit.substance.runtime.dfs.service;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
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
	 * @return
	 */
	long getDefaultBlockCapacity();

	/**
	 * 
	 * @param accountId
	 * @return
	 */
	FileSystem getFileSystem(String accountId);

}
