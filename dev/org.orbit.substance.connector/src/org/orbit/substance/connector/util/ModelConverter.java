package org.orbit.substance.connector.util;

import javax.ws.rs.core.Response;

import org.orbit.substance.api.dfs.filesystem.File;
import org.orbit.substance.connector.dfs.filesystem.FileImpl;
import org.orbit.substance.model.dfs.FileDTO;

public class ModelConverter {

	public static DFS DFS = new DFS();

	public static class DFS {

		public File toNode(FileDTO nodeDTO) {
			FileImpl file = new FileImpl();
			return file;
		}

		public File[] getFiles(Response response) {
			return null;
		}
	}

}
