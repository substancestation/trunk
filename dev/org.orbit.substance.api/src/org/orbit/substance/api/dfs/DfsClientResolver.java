package org.orbit.substance.api.dfs;

import java.io.IOException;

public interface DfsClientResolver {

	/**
	 * 
	 * @param dfsServiceUrl
	 * @param accessToken
	 * @return
	 */
	DfsClient resolve(String dfsServiceUrl, String accessToken);

	/**
	 * 
	 * @param dfsId
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	String getURL(String dfsId, String accessToken) throws IOException;

}
