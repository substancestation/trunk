package org.orbit.substance.connector.dfs;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfs.Path;
import org.orbit.substance.connector.util.ModelConverter;
import org.orbit.substance.model.RequestConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;

public class FileSystemClientImpl extends ServiceClientImpl<DfsClient, FileSystemWSClient> implements DfsClient {

	private static final FileMetadata[] EMPTY_FILES = new FileMetadata[0];

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public FileSystemClientImpl(ServiceConnector<DfsClient> connector, Map<String, Object> properties) {
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

	protected void checkParentFileId(String parentFileId) {
		if (parentFileId == null || parentFileId.isEmpty()) {
			throw new IllegalArgumentException("Parent file id is null.");
		}
	}

	protected void checkPath(Path path) {
		if (path == null) {
			throw new IllegalArgumentException("Path is null.");
		}
	}

	protected void checkFileName(String fileName) {
		if (fileName == null) {
			throw new IllegalArgumentException("File name is null.");
		}
	}

	@Override
	public FileMetadata[] listRoots() throws ClientException {
		Request request = new Request(RequestConstants.LIST_ROOTS);

		FileMetadata[] files = null;
		Response response = sendRequest(request);
		if (response != null) {
			files = ModelConverter.Dfs.getFiles(this, response);
		}
		if (files == null) {
			files = EMPTY_FILES;
		}
		return files;
	}

	@Override
	public FileMetadata[] listFiles(String parentFileId) throws ClientException {
		checkFileId(parentFileId);

		Request request = new Request(RequestConstants.LIST_FILES_BY_PARENT_ID);
		request.setParameter("parent_file_id", parentFileId);

		FileMetadata[] files = null;
		Response response = sendRequest(request);
		if (response != null) {
			files = ModelConverter.Dfs.getFiles(this, response);
		}
		if (files == null) {
			files = EMPTY_FILES;
		}
		return files;
	}

	@Override
	public FileMetadata[] listFiles(Path parentPath) throws ClientException {
		checkPath(parentPath);

		Request request = new Request(RequestConstants.LIST_FILES_BY_PARENT_PATH);
		request.setParameter("parent_path", parentPath.getPathString());

		FileMetadata[] files = null;
		Response response = sendRequest(request);
		if (response != null) {
			files = ModelConverter.Dfs.getFiles(this, response);
		}
		if (files == null) {
			files = EMPTY_FILES;
		}
		return files;
	}

	@Override
	public FileMetadata getFile(String fileId) throws ClientException {
		checkFileId(fileId);

		Request request = new Request(RequestConstants.GET_FILE_BY_ID);
		request.setParameter("file_id", fileId);

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
		}
		return file;
	}

	@Override
	public FileMetadata getFile(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.GET_FILE_BY_PATH);
		request.setParameter("path", path.getPathString());

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
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
			exists = ModelConverter.Dfs.exists(response);
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
			exists = ModelConverter.Dfs.exists(response);
		}
		return exists;
	}

	@Override
	public FileMetadata createNewFile(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.CREATE_NEW_FILE);
		request.setParameter("path", path.getPathString());

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
		}
		return file;
	}

	@Override
	public FileMetadata createNewFile(Path path, long size) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.CREATE_NEW_FILE);
		request.setParameter("path", path.getPathString());

		if (size > 0) {
			// file size is optional
			// - if specified, the volumes will be allocated using it, by the DFS.
			request.setParameter("size", size);
		}

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
		}
		return file;
	}

	@Override
	public FileMetadata createNewFile(String parentFileId, String fileName, long size) throws ClientException {
		checkFileName(fileName);

		Request request = new Request(RequestConstants.CREATE_NEW_FILE);
		request.setParameter("parent_file_id", parentFileId);
		request.setParameter("file_name", fileName);

		if (size > 0) {
			// file size is optional
			// - if specified, the volumes will be allocated using it, by the DFS.
			request.setParameter("size", size);
		}

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
		}
		return file;
	}

	@Override
	public FileMetadata allocateVolumes(String fileId, long size) throws ClientException {
		Request request = new Request(RequestConstants.ALLOCATE_VOLUMES);

		// file id is required
		// - file metadata must exists
		// - if file metadata is not found, file not found exception will be thrown.
		request.setParameter("file_id", fileId);

		// file size is optional
		// - if specified, the volumes will be allocated using it, by the DFS.
		// - if not specified, the volumes will be allocated using the size from file metadata. If size from file metadata is 0, IllegalStateException will be
		// thrown.
		if (size > 0) {
			request.setParameter("size", String.valueOf(size));
		}

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
		}
		return file;
	}

	@Override
	public FileMetadata mkdirs(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.MKDIRS);
		request.setParameter("path", path.getPathString());

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
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
			isDeleted = ModelConverter.Dfs.isDeleted(response);
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
			isDeleted = ModelConverter.Dfs.isDeleted(response);
		}
		return isDeleted;
	}

	@Override
	public FileMetadata moveToTrash(String fileId) throws ClientException {
		checkFileId(fileId);

		Request request = new Request(RequestConstants.MOVE_TO_TRASH_BY_ID);
		request.setParameter("file_id", fileId);

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
		}
		return file;
	}

	@Override
	public FileMetadata moveToTrash(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.MOVE_TO_TRASH_BY_PATH);
		request.setParameter("path", path.getPathString());

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
		}
		return file;
	}

	@Override
	public FileMetadata putBackFromTrash(String fileId) throws ClientException {
		checkFileId(fileId);

		Request request = new Request(RequestConstants.PUT_BACK_FROM_TRASH_BY_ID);
		request.setParameter("file_id", fileId);

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
		}
		return file;
	}

	@Override
	public FileMetadata putBackFromTrash(Path path) throws ClientException {
		checkPath(path);

		Request request = new Request(RequestConstants.PUT_BACK_FROM_TRASH_BY_PATH);
		request.setParameter("path", path.getPathString());

		FileMetadata file = null;
		Response response = sendRequest(request);
		if (response != null) {
			file = ModelConverter.Dfs.getFile(this, response);
		}
		return file;
	}

	@Override
	public boolean emptyTrash() throws ClientException {
		Request request = new Request(RequestConstants.EMPTY_TRASH);

		boolean isEmptied = false;
		Response response = sendRequest(request);
		if (response != null) {
			isEmptied = ModelConverter.Dfs.isEmptied(response);
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

// @Override
// public File uploadFile(String parentFileId, java.io.File localFile) throws ClientException {
// File newFile = null;
// FileMetadataDTO newFileMetadataDTO = this.client.upload(parentFileId, localFile);
// if (newFileMetadataDTO != null) {
// newFile = ModelConverter.Dfs.toFile(this, newFileMetadataDTO);
// }
// return newFile;
// }
