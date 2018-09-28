package org.orbit.substance.api.dfs;

import org.orbit.substance.model.dfs.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface DfsClient extends ServiceClient {

	@Override
	DfsMetadata getMetadata() throws ClientException;

	FileMetadata[] listRoots() throws ClientException;

	FileMetadata[] listFiles(String parentFileId) throws ClientException;

	FileMetadata[] listFiles(Path parentPath) throws ClientException;

	FileMetadata getFile(String fileId) throws ClientException;

	FileMetadata getFile(Path path) throws ClientException;

	boolean exists(String fileId) throws ClientException;

	boolean exists(Path path) throws ClientException;

	FileMetadata createDirectory(Path path) throws ClientException;

	FileMetadata createDirectory(String parentFileId, String fileName) throws ClientException;

	FileMetadata mkdirs(Path path) throws ClientException;

	FileMetadata createNewFile(String parentFileId, String fileName, long size) throws ClientException;

	FileMetadata createNewFile(Path path, long size) throws ClientException;

	FileMetadata allocateVolumes(String fileId, long size) throws ClientException;

	boolean updateFileParts(String fileId, String filePartsString) throws ClientException;

	boolean rename(String fileId, String newName) throws ClientException;

	boolean delete(String fileId) throws ClientException;

	boolean delete(Path path) throws ClientException;

	FileMetadata moveToTrash(String fileId) throws ClientException;

	FileMetadata moveToTrash(Path path) throws ClientException;

	FileMetadata moveOutOfTrash(String fileId) throws ClientException;

	FileMetadata moveOutOfTrash(Path path) throws ClientException;

	boolean emptyTrash() throws ClientException;

}

// FileMetadata createNewFile(Path path, long size) throws ClientException;
// File uploadFile(String parentFileId, java.io.File localFile) throws ClientException;
