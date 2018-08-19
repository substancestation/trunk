package org.orbit.substance.runtime.dfs.filesystem.service;

import java.io.IOException;
import java.util.Map;

import org.orbit.substance.runtime.model.dfs.Path;

public class FileSystemImpl implements FileSystem {

	protected Map<Object, Object> properties;
	protected final String accessToken;
	protected final String username;

	/**
	 * 
	 * @param properties
	 * @param accessToken
	 * @param username
	 */
	public FileSystemImpl(Map<Object, Object> properties, String accessToken, String username) {
		this.properties = properties;
		this.accessToken = accessToken;
		this.username = username;
	}

	@Override
	public Path[] listRoots() {
		return null;
	}

	@Override
	public Path[] listFiles(Path parent) {
		return null;
	}

	@Override
	public boolean exists(Path path) {
		return false;
	}

	@Override
	public boolean isDirectory(Path path) {
		return false;
	}

	@Override
	public boolean mkdirs(Path path) throws IOException {
		return false;
	}

}
