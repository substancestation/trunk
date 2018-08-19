package org.orbit.substance.connector.dfs.filesystem;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.substance.api.dfs.filesystem.File;
import org.orbit.substance.api.dfs.filesystem.FileSystemClient;
import org.orbit.substance.connector.RequestConstants;
import org.orbit.substance.connector.util.ModelConverter;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;

public class FileSystemClientImpl extends ServiceClientImpl<FileSystemClient, FileSystemWSClient> implements FileSystemClient {

	private static final File[] EMPTY_FILES = new File[0];

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public FileSystemClientImpl(ServiceConnector<FileSystemClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected FileSystemWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new FileSystemWSClient(config);
	}

	/**
	 * 
	 * @param e
	 * @return
	 * @throws IOException
	 */
	protected IOException handleException(ClientException e) throws IOException {
		throw new IOException(e);
	}

	@Override
	public File[] listRoots() throws IOException {
		File[] files = null;
		try {
			Request request = new Request(RequestConstants.LIST_ROOTS);
			Response response = sendRequest(request);
			if (response != null) {
				files = ModelConverter.DFS.getFiles(response);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		if (files == null) {
			files = EMPTY_FILES;
		}
		return files;
	}

}
