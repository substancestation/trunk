package org.orbit.substance.api.dfsvolume;

import java.util.Date;

import org.origin.common.rest.server.ServerException;

public interface DfsVolumeServiceMetadata {

	String getFileSystemId();

	String getId();

	long getTotalCapacity() throws ServerException;

	long totalSize() throws ServerException;

	Date getTime() throws ServerException;

}
