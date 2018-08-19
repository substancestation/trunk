package org.orbit.substance.api.dfs.filesystem;

import java.io.IOException;

import org.origin.common.rest.client.ServiceClient;

public interface FileSystemClient extends ServiceClient {

	File[] listRoots() throws IOException;

}
