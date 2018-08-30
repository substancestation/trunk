package org.orbit.substance.connector.dfs;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.substance.api.dfs.File;
import org.orbit.substance.api.dfs.FileSystemClient;
import org.orbit.substance.api.dfs.Path;
import org.orbit.substance.connector.util.ModelConverter;
import org.orbit.substance.model.RequestConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;

public class FileSystemClientImpl extends ServiceClientImpl<FileSystemClient, FileSystemWSClient> implements FileSystemClient {

	private static final File[] EMPTY_FILES = new File[0];

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public FileSystemClientImpl(ServiceConnector<FileSystemClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected FileSystemWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new FileSystemWSClient(config);
	}

	protected void checkFileId(String fileId) {
		if (fileId == null || fileId.isEmpty()) {
			throw new IllegalArgumentException("File id is null.");
		}
	}

	protected void checkPath(Path path) {
		if (path == null) {
			throw new IllegalArgumentException("Path is null.");
		}
	}

	@Override
	public File[] listRoots() throws ClientException {
		Request request = new Request(RequestConstants.LIST_ROOTS);

		File[] files = null;
		Response response = sendRequest(request);
		if (response != null) {
			files = ModelConverter.FILE_SYSTEM.getFiles(response);
		}
		if (files == null) {
			files = EMPTY_FILES;
		}
		return files;
	}

	@Override
	public File[] listFiles(String parentFileId) throws ClientException {
		checkFileId(parentFileId);

		Request request = new Request(RequestConstants.LIST_FILES_BY_PARENT_ID);
		request.setParameter("parent_file_id", parentFileId);

		File[] files = null;
		Response response = sendRequest(request);
		if (response != null) {
			files = ModelConverter.FILE_SYSTEM.getFiles(response);
		}
		if (files == null) {
			files = EMPTY_FILES;
		}
		return files;
	}

	@Override
	public File[] listFiles(Path parentPath) throws ClientException {
		checkPath(parentPath);

		Request request = new Request(RequestConstants.LIST_FILES_BY_PARENT_PATH);
		request.setParameter("parent_path", parentPath.getPathString());

		File[] files = null;
		Response response = sendRequest(request);
		if (response != null) {
			files = ModelConverter.FILE_SYSTEM.getFiles(response);
		}
		if (files == null) {
			files = EMPTY_FILES;
		}
		return files;
	}

	@Override
	public File getFile(String fileId) throws ClientException {
		checkFileId(fileId);

		Request request = new Request(RequestConstants.GET_FILE_BY_ID);
		request.setParameter("file_id", fileId);

		File file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.FILE_SYSTEM.getFile(response);
		}
		return file;
	}

	@Override
	public File getFile(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.GET_FILE_BY_PATH);
		request.setParameter("path", path.getPathString());

		File file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.FILE_SYSTEM.getFile(response);
		}
		return file;
	}

	@Override
	public boolean exists(String fileId) throws ClientException {
		checkFileId(fileId);

		Request request = new Request(RequestConstants.FILE_ID_EXISTS);
		request.setParameter("file_id", fileId);

		boolean exists = false;
		Response response = sendRequest(request);
		if (response != null) {
			exists = ModelConverter.FILE_SYSTEM.exists(response);
		}
		return exists;
	}

	@Override
	public boolean exists(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.PATH_EXISTS);
		request.setParameter("path", path.getPathString());

		boolean exists = false;
		Response response = sendRequest(request);
		if (response != null) {
			exists = ModelConverter.FILE_SYSTEM.exists(response);
		}
		return exists;
	}

	@Override
	public File createNewFile(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.CREATE_NEW_FILE);
		request.setParameter("path", path.getPathString());

		File file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.FILE_SYSTEM.getFile(response);
		}
		return file;
	}

	@Override
	public File mkdirs(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.MKDIRS);
		request.setParameter("path", path.getPathString());

		File file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.FILE_SYSTEM.getFile(response);
		}
		return file;
	}

	@Override
	public boolean delete(String fileId) throws ClientException {
		checkFileId(fileId);

		Request request = new Request(RequestConstants.DELETE_BY_ID);
		request.setParameter("file_id", fileId);

		boolean isDeleted = false;
		Response response = sendRequest(request);
		if (response != null) {
			isDeleted = ModelConverter.FILE_SYSTEM.isDeleted(response);
		}
		return isDeleted;
	}

	@Override
	public boolean delete(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.DELETE_BY_PATH);
		request.setParameter("path", path.getPathString());

		boolean isDeleted = false;
		Response response = sendRequest(request);
		if (response != null) {
			isDeleted = ModelConverter.FILE_SYSTEM.isDeleted(response);
		}
		return isDeleted;
	}

	@Override
	public File moveToTrash(String fileId) throws ClientException {
		checkFileId(fileId);

		Request request = new Request(RequestConstants.MOVE_TO_TRASH_BY_ID);
		request.setParameter("file_id", fileId);

		File file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.FILE_SYSTEM.getFile(response);
		}
		return file;
	}

	@Override
	public File moveToTrash(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.MOVE_TO_TRASH_BY_PATH);
		request.setParameter("path", path.getPathString());

		File file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.FILE_SYSTEM.getFile(response);
		}
		return file;
	}

	@Override
	public File putBackFromTrash(String fileId) throws ClientException {
		checkFileId(fileId);

		Request request = new Request(RequestConstants.PUT_BACK_FROM_TRASH_BY_ID);
		request.setParameter("file_id", fileId);

		File file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.FILE_SYSTEM.getFile(response);
		}
		return file;
	}

	@Override
	public File putBackFromTrash(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.PUT_BACK_FROM_TRASH_BY_PATH);
		request.setParameter("path", path.getPathString());

		File file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.FILE_SYSTEM.getFile(response);
		}
		return file;
	}

	@Override
	public boolean emptyTrash() throws ClientException {
		Request request = new Request(RequestConstants.EMPTY_TRASH);

		boolean isEmptied = false;
		Response response = sendRequest(request);
		if (response != null) {
			isEmptied = ModelConverter.FILE_SYSTEM.isEmptied(response);
		}
		return isEmptied;
	}

}

// boolean isDirectory(Path path) throws ClientException;
// String getFileId(Path path) throws ClientException;

// @Override
// public boolean isDirectory(Path path) throws ClientException {
// checkPath(path);
//
// Request request = new Request(RequestConstants.IS_DIRECTORY);
// request.setParameter("path", path.getPathString());
//
// boolean exists = false;
// Response response = sendRequest(request);
// if (response != null) {
// exists = ModelConverter.FILE_SYSTEM.isDirectory(response);
// }
// return exists;
// }

// @Override
// public String getFileId(Path path) throws ClientException {
// checkPath(path);
//
// Request request = new Request(RequestConstants.GET_FILE_ID_BY_PATH);
// request.setParameter("path", path.getPathString());
//
// String fileId = null;
// Response response = sendRequest(request);
// if (response != null) {
// fileId = ModelConverter.FILE_SYSTEM.getFileId(response);
// }
// return fileId;
// }
