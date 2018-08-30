package org.orbit.substance.api.dfs;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface FileSystemClient extends ServiceClient {

	File[] listRoots() throws ClientException;

	File[] listFiles(String parentFileId) throws ClientException;

	File[] listFiles(Path parentPath) throws ClientException;

	File getFile(String fileId) throws ClientException;

	File getFile(Path path) throws ClientException;

	boolean exists(String fileId) throws ClientException;

	boolean exists(Path path) throws ClientException;

	File createNewFile(Path path) throws ClientException;

	File mkdirs(Path path) throws ClientException;

	boolean delete(String fileId) throws ClientException;

	boolean delete(Path path) throws ClientException;

	File moveToTrash(String fileId) throws ClientException;

	File moveToTrash(Path path) throws ClientException;

	File putBackFromTrash(String fileId) throws ClientException;

	File putBackFromTrash(Path path) throws ClientException;

	boolean emptyTrash() throws ClientException;

}
