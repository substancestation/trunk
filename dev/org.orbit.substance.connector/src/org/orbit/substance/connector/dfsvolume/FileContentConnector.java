package org.orbit.substance.connector.dfsvolume;

import java.util.Map;

import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.orbit.substance.api.dfsvolume.FileContentClient;
import org.origin.common.rest.client.ServiceConnector;

public class FileContentConnector extends ServiceConnector<FileContentClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.substance.connector.FileContentConnector";

	public FileContentConnector() {
		super(FileContentClient.class);
	}

	@Override
	protected FileContentClient create(Map<String, Object> properties) {
		return new FileContentClientImpl(this, properties);
	}

}
