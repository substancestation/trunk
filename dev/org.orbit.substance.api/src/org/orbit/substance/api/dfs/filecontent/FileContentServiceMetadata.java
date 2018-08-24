package org.orbit.substance.api.dfs.filecontent;

import java.util.Date;

import org.origin.common.rest.server.ServerException;

public interface FileContentServiceMetadata {

	String getFileSystemId();

	String getId();

	long getTotalCapacity() throws ServerException;

	long totalSize() throws ServerException;

	Date getTime() throws ServerException;

}
