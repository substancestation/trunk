package org.orbit.substance.runtime.dfs.service;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAware;
import org.origin.common.service.PropertiesAware;
import org.origin.common.service.WebServiceAware;

public interface DfsService extends WebServiceAware, PropertiesAware, ConnectionAware, EditPoliciesAware {

	String getDfsId();

	long getDefaultBlockCapacity();

	FileSystem getFileSystem(String accountId);

}
