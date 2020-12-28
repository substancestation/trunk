package org.orbit.substance.runtime.dfs.service;

import java.util.Map;

import org.origin.common.jdbc.ConnectionProvider;
import org.origin.common.service.AccessTokenProvider;
import org.origin.common.service.IWebService;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface DfsService extends IWebService, ConnectionProvider, AccessTokenProvider {

	Map<Object, Object> getInitProperties();

	String getDfsId();

	long getDefaultBlockCapacity();

	FileSystem getFileSystem(String accountId);

}
