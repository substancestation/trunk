package org.orbit.substance.webconsole.util;

import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.util.SubstanceClientsUtil;

public class DefaultDfsClientResolver implements DfsClientResolver {

	@Override
	public DfsClient resolve(String dfsServiceUrl, String accessToken) {
		DfsClient dfsClient = SubstanceClientsUtil.Dfs.getDfsClient(dfsServiceUrl, accessToken);
		return dfsClient;
	}

}
