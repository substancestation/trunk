package org.orbit.substance.runtime.dfs.service;

import java.util.Map;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAware;
import org.origin.common.service.AccessTokenAware;
import org.origin.common.service.WebServiceAware;

public interface DfsService extends ConnectionAware, WebServiceAware, EditPoliciesAware, AccessTokenAware {

	Map<Object, Object> getInitProperties();

	String getDfsId();

	long getDefaultBlockCapacity();

	FileSystem getFileSystem(String accountId);

}
