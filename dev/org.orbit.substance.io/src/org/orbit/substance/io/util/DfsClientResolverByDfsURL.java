package org.orbit.substance.io.util;

import java.io.IOException;

import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.util.SubstanceClientsUtil;

public class DfsClientResolverByDfsURL implements DfsClientResolver {

	protected String dfsServiceUrl;

	/**
	 * 
	 * @param dfsServiceUrl
	 */
	public DfsClientResolverByDfsURL(String dfsServiceUrl) {
		this.dfsServiceUrl = dfsServiceUrl;

		if (this.dfsServiceUrl == null || this.dfsServiceUrl.isEmpty()) {
			throw new IllegalArgumentException("dfsServiceUrl is null.");
		}
	}

	public String getDfsServiceURL() {
		return this.dfsServiceUrl;
	}

	@Override
	public DfsClient resolve(String accessToken) throws IOException {
		DfsClient dfsClient = SubstanceClientsUtil.DFS.getDfsClient(this.dfsServiceUrl, accessToken);
		return dfsClient;
	}

}
