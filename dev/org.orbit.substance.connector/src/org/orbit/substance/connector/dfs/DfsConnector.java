package org.orbit.substance.connector.dfs;

import java.util.Map;

import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.orbit.substance.api.dfs.DfsClient;
import org.origin.common.rest.client.ServiceConnector;

public class DfsConnector extends ServiceConnector<DfsClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.substance.connector.DfsConnector";

	public DfsConnector() {
		super(DfsClient.class);
	}

	@Override
	protected DfsClient create(Map<String, Object> properties) {
		return new DfsClientImpl(this, properties);
	}

}
