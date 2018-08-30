package org.orbit.substance.connector.dfsvolume;

import java.io.OutputStream;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.substance.api.dfsvolume.DataBlockMetadata;
import org.orbit.substance.api.dfsvolume.FileContentClient;
import org.orbit.substance.api.dfsvolume.FileContentMetadata;
import org.orbit.substance.connector.util.ModelConverter;
import org.orbit.substance.model.RequestConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;

public class FileContentClientImpl extends ServiceClientImpl<FileContentClient, FileContentWSClient> implements FileContentClient {

	private static final DataBlockMetadata[] EMPTY_DATA_BLOCKS = new DataBlockMetadata[0];
	private static final FileContentMetadata[] EMPTY_FILE_CONTENTS = new FileContentMetadata[0];

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public FileContentClientImpl(ServiceConnector<FileContentClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected FileContentWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new FileContentWSClient(config);
	}

	// ----------------------------------------------------------------------
	// Methods for accessing data blocks
	// ----------------------------------------------------------------------
	@Override
	public DataBlockMetadata[] getDataBlocks() throws ClientException {
		DataBlockMetadata[] dataBlocks = null;

		Request request = new Request(RequestConstants.LIST_ALL_DATA_BLOCKS);

		Response response = sendRequest(request);
		if (response != null) {
			dataBlocks = ModelConverter.FILE_CONTENT.getDataBlocks(response);
		}

		if (dataBlocks == null) {
			dataBlocks = EMPTY_DATA_BLOCKS;
		}
		return dataBlocks;
	}

	@Override
	public DataBlockMetadata[] getDataBlocks(String accountId) throws ClientException {
		DataBlockMetadata[] dataBlocks = null;

		Request request = new Request(RequestConstants.LIST_DATA_BLOCKS);
		request.setParameter("account_id", accountId);

		Response response = sendRequest(request);
		if (response != null) {
			dataBlocks = ModelConverter.FILE_CONTENT.getDataBlocks(response);
		}

		if (dataBlocks == null) {
			dataBlocks = EMPTY_DATA_BLOCKS;
		}
		return dataBlocks;
	}

	@Override
	public boolean dataBlockExists(String accountId, String blockId) throws ClientException {
		boolean succeed = false;

		Request request = new Request(RequestConstants.DATA_BLOCK_EXISTS);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);

		Response response = sendRequest(request);
		if (response != null) {
			succeed = ModelConverter.FILE_CONTENT.exists(response);
		}

		return succeed;
	}

	@Override
	public DataBlockMetadata getDataBlock(String accountId, String blockId) throws ClientException {
		DataBlockMetadata dataBlock = null;

		Request request = new Request(RequestConstants.GET_DATA_BLOCK);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);

		Response response = sendRequest(request);
		if (response != null) {
			dataBlock = ModelConverter.FILE_CONTENT.getDataBlock(response);
		}

		return dataBlock;
	}

	@Override
	public DataBlockMetadata createDataBlock(String accountId, long capacity) throws ClientException {
		DataBlockMetadata dataBlock = null;

		Request request = new Request(RequestConstants.CREATE_DATA_BLOCK);
		request.setParameter("account_id", accountId);
		request.setParameter("capacity", capacity);

		Response response = sendRequest(request);
		if (response != null) {
			dataBlock = ModelConverter.FILE_CONTENT.getDataBlock(response);
		}

		return dataBlock;
	}

	@Override
	public boolean deleteDataBlock(String accountId, String blockId) throws ClientException {
		boolean succeed = false;

		Request request = new Request(RequestConstants.DELETE_DATA_BLOCK);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);

		Response response = sendRequest(request);
		if (response != null) {
			succeed = ModelConverter.FILE_CONTENT.isDeleted(response);
		}

		return succeed;
	}

	// ----------------------------------------------------------------------
	// Methods for accessing file contents of a data block
	// ----------------------------------------------------------------------
	@Override
	public FileContentMetadata[] getFileContentMetadatas(String accountId, String blockId) throws ClientException {
		FileContentMetadata[] fileContents = null;

		Request request = new Request(RequestConstants.LIST_FILE_CONTENTS);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);

		Response response = sendRequest(request);
		if (response != null) {
			fileContents = ModelConverter.FILE_CONTENT.getFileContents(response);
		}

		if (fileContents == null) {
			fileContents = EMPTY_FILE_CONTENTS;
		}
		return fileContents;
	}

	@Override
	public boolean fileContentExists(String accountId, String blockId, String fileId, int partId) throws ClientException {
		boolean succeed = false;

		Request request = new Request(RequestConstants.FILE_CONTENT_EXISTS);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);
		request.setParameter("file_id", fileId);
		request.setParameter("part_id", partId);

		Response response = sendRequest(request);
		if (response != null) {
			succeed = ModelConverter.FILE_CONTENT.exists(response);
		}
		return succeed;
	}

	@Override
	public FileContentMetadata getFileContentMetadata(String accountId, String blockId, String fileId, int partId) throws ClientException {
		FileContentMetadata fileContent = null;

		Request request = new Request(RequestConstants.GET_FILE_CONTENT);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);
		request.setParameter("file_id", fileId);
		request.setParameter("part_id", partId);

		Response response = sendRequest(request);
		if (response != null) {
			fileContent = ModelConverter.FILE_CONTENT.getFileContent(response);
		}
		return fileContent;
	}

	@Override
	public boolean deleteFileContent(String accountId, String blockId, String fileId, int partId) throws ClientException {
		boolean succeed = false;

		Request request = new Request(RequestConstants.DELETE_FILE_CONTENT);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);
		request.setParameter("file_id", fileId);
		request.setParameter("part_id", partId);

		Response response = sendRequest(request);
		if (response != null) {
			succeed = ModelConverter.FILE_CONTENT.isDeleted(response);
		}
		return succeed;
	}

	// ----------------------------------------------------------------------
	// Methods for uploading/downloading file contents
	// ----------------------------------------------------------------------
	@Override
	public boolean uploadFile(String accountId, String blockId, String fileId, FileContentMetadata fileContentMetadata) throws ClientException {
		return false;
	}

	@Override
	public boolean downloadFile(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ClientException {
		return false;
	}

}

// @Override
// public FileContentServiceMetadata getMetadata() {
// // TODO Auto-generated method stub
// return null;
// }

/// **
// *
// * @param e
// * @return
// * @throws IOException
// */
// protected IOException handleException(ClientException e) throws IOException {
// throw new IOException(e);
// }
