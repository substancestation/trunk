package org.orbit.substance.api.dfs;

public interface DfsClientResolver {

	/**
	 * 
	 * @param dfsServiceUrl
	 * @param accessToken
	 * @return
	 */
	DfsClient resolve(String dfsServiceUrl, String accessToken);

}
