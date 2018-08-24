package org.orbit.substance.runtime.dfs.filesystem.service;

import java.io.IOException;

import org.orbit.substance.runtime.model.dfs.FileMetadata;
import org.orbit.substance.runtime.model.dfs.Path;

public interface FileSystem {

	FileMetadata getMetaData(Path path);

	Path[] listRoots() throws IOException;

	Path[] listFiles(Path parent) throws IOException;

	boolean exists(Path path) throws IOException;

	boolean isDirectory(Path path) throws IOException;

	boolean createNewFile(Path path) throws IOException;

	boolean mkdirs(Path path) throws IOException;

	boolean delete(Path path) throws IOException;

}
