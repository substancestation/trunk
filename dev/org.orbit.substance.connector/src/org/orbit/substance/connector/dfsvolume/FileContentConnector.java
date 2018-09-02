package org.orbit.substance.connector.dfsvolume;

import java.util.Map;

import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.origin.common.rest.client.ServiceConnector;

public class FileContentConnector extends ServiceConnector<DfsVolumeClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.substance.connector.FileContentConnector";

	public FileContentConnector() {
		super(DfsVolumeClient.class);
	}

	@Override
	protected DfsVolumeClient create(Map<String, Object> properties) {
		return new DfsVolumeClientImpl(this, properties);
	}

}
