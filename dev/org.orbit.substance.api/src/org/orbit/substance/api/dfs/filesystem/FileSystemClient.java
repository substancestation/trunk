package org.orbit.substance.api.dfs.filesystem;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface FileSystemClient extends ServiceClient {

	File[] listRoots() throws ClientException;

}
