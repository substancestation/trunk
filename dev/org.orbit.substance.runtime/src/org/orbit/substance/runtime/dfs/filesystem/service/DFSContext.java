package org.orbit.substance.runtime.dfs.filesystem.service;

public interface DFSContext {

	void setProperty(String propName, Object propValue);

	Object getProperty(String propName);

	<T> T getProperty(String propName, Class<T> propValueClass);

}
