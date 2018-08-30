package org.orbit.substance.connector.dfs;

import java.util.Map;

import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.orbit.substance.api.dfs.FileSystemClient;
import org.origin.common.rest.client.ServiceConnector;

public class FileSystemConnector extends ServiceConnector<FileSystemClient> implements ConnectorActivator {

	public static final String ID = "org.orbit.substance.connector.FileSystemConnector";

	public FileSystemConnector() {
		super(FileSystemClient.class);
	}

	@Override
	protected FileSystemClient create(Map<String, Object> properties) {
		return new FileSystemClientImpl(this, properties);
	}

}
