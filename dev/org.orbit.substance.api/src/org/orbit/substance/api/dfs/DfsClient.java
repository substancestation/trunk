package org.orbit.substance.api.dfs;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface DfsClient extends ServiceClient {

	FileMetadata[] listRoots() throws ClientException;

	FileMetadata[] listFiles(String parentFileId) throws ClientException;

	FileMetadata[] listFiles(Path parentPath) throws ClientException;

	FileMetadata getFile(String fileId) throws ClientException;

	FileMetadata getFile(Path path) throws ClientException;

	boolean exists(String fileId) throws ClientException;

	boolean exists(Path path) throws ClientException;

	FileMetadata createNewFile(Path path) throws ClientException;

	FileMetadata createNewFile(Path path, long size) throws ClientException;

	FileMetadata createNewFile(String parentFileId, String fileName, long size) throws ClientException;

	FileMetadata allocateVolumes(String fileId, long size) throws ClientException;

	FileMetadata mkdirs(Path path) throws ClientException;

	boolean delete(String fileId) throws ClientException;

	boolean delete(Path path) throws ClientException;

	FileMetadata moveToTrash(String fileId) throws ClientException;

	FileMetadata moveToTrash(Path path) throws ClientException;

	FileMetadata putBackFromTrash(String fileId) throws ClientException;

	FileMetadata putBackFromTrash(Path path) throws ClientException;

	boolean emptyTrash() throws ClientException;

}

// File uploadFile(String parentFileId, java.io.File localFile) throws ClientException;
