package org.orbit.substance.api.dfs;

import java.io.IOException;

public interface DfsClientResolver {

	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	DfsClient resolve(String accessToken) throws IOException;

}
