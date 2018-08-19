package org.orbit.substance.runtime.dfs.filesystem.service;

import java.io.IOException;

import org.orbit.substance.runtime.model.dfs.Path;

public interface FileSystem {

	// String getUUID();

	Path[] listRoots();

	Path[] listFiles(Path parent);

	boolean exists(Path path);

	boolean isDirectory(Path path);

	boolean mkdirs(Path path) throws IOException;

}
