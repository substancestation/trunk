package org.orbit.substance.connector.dfsvolume;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.substance.api.dfsvolume.DataBlockMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.DfsVolumeServiceMetadata;
import org.orbit.substance.api.dfsvolume.FileContentMetadata;
import org.orbit.substance.connector.util.ModelConverter;
import org.orbit.substance.model.RequestConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadataDTO;

public class DfsVolumeClientImpl extends ServiceClientImpl<DfsVolumeClient, DfsVolumeWSClient> implements DfsVolumeClient {

	private static final DataBlockMetadata[] EMPTY_DATA_BLOCKS = new DataBlockMetadata[0];
	private static final FileContentMetadata[] EMPTY_FILE_CONTENTS = new FileContentMetadata[0];

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public DfsVolumeClientImpl(ServiceConnector<DfsVolumeClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected DfsVolumeWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new DfsVolumeWSClient(config);
	}

	@Override
	public DfsVolumeServiceMetadata getMetadata() throws ClientException {
		DfsVolumeServiceMetadataImpl metadata = new DfsVolumeServiceMetadataImpl();
		ServiceMetadataDTO metadataDTO = getWSClient().getMetadata();
		if (metadataDTO != null) {
			Map<String, Object> properties = metadataDTO.getProperties();
			if (properties != null && !properties.isEmpty()) {
				metadata.getProperties().putAll(properties);
			}
		}
		return metadata;
	}

	// ----------------------------------------------------------------------
	// Data blocks
	// ----------------------------------------------------------------------
	@Override
	public DataBlockMetadata[] getDataBlocks() throws ClientException {
		DataBlockMetadata[] dataBlocks = null;

		Request request = new Request(RequestConstants.LIST_ALL_DATA_BLOCKS);

		Response response = sendRequest(request);
		if (response != null) {
			dataBlocks = ModelConverter.DfsVolume.getDataBlocks(this, response);
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
			dataBlocks = ModelConverter.DfsVolume.getDataBlocks(this, response);
		}

		if (dataBlocks == null) {
			dataBlocks = EMPTY_DATA_BLOCKS;
		}
		return dataBlocks;
	}

	@Override
	public DataBlockMetadata[] getDataBlocks(String accountId, long minFreeSpace) throws ClientException {
		DataBlockMetadata[] dataBlocks = null;

		Request request = new Request(RequestConstants.LIST_DATA_BLOCKS);
		request.setParameter("account_id", accountId);
		if (minFreeSpace > 0) {
			request.setParameter("min_free_space", minFreeSpace);
		}

		Response response = sendRequest(request);
		if (response != null) {
			dataBlocks = ModelConverter.DfsVolume.getDataBlocks(this, response);
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
			succeed = ModelConverter.DfsVolume.exists(response);
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
			dataBlock = ModelConverter.DfsVolume.getDataBlock(this, response);
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
			dataBlock = ModelConverter.DfsVolume.getDataBlock(this, response);
		}

		return dataBlock;
	}

	@Override
	public boolean updateDataBlockSizeByDelta(String accountId, String blockId, long sizeDelta) throws ClientException {
		boolean succeed = false;

		Request request = new Request(RequestConstants.UPDATE_DATA_BLOCK_SIZE_BY_DELTA);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);
		request.setParameter("size_delta", sizeDelta);

		Response response = sendRequest(request);
		if (response != null) {
			succeed = ModelConverter.DfsVolume.isUpdated(response);
		}

		return succeed;
	}

	@Override
	public boolean deleteDataBlock(String accountId, String blockId) throws ClientException {
		boolean succeed = false;

		Request request = new Request(RequestConstants.DELETE_DATA_BLOCK);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);

		Response response = sendRequest(request);
		if (response != null) {
			succeed = ModelConverter.DfsVolume.isDeleted(response);
		}

		return succeed;
	}

	// ----------------------------------------------------------------------
	// File contents
	// ----------------------------------------------------------------------
	@Override
	public FileContentMetadata[] getFileContentMetadatas(String accountId, String blockId) throws ClientException {
		FileContentMetadata[] fileContents = null;

		Request request = new Request(RequestConstants.LIST_FILE_CONTENTS);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);

		Response response = sendRequest(request);
		if (response != null) {
			fileContents = ModelConverter.DfsVolume.getFileContents(this, response);
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
			succeed = ModelConverter.DfsVolume.exists(response);
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
			fileContent = ModelConverter.DfsVolume.getFileContent(this, response);
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
			succeed = ModelConverter.DfsVolume.isDeleted(response);
		}
		return succeed;
	}

	// ----------------------------------------------------------------------
	// Upload and download
	// ----------------------------------------------------------------------
	@Override
	public boolean uploadFile(String accountId, String blockId, String fileId, long checksum, File file) throws ClientException {
		boolean succeed = false;
		Response response = getWSClient().upload(accountId, blockId, fileId, checksum, file);
		if (response != null) {
			FileContentMetadata fileContent = ModelConverter.DfsVolume.getUpdatedFileContent(this, response);
			if (fileContent != null) {
				succeed = true;
			}
		}
		return succeed;
	}

	@Override
	public boolean uploadFile(String accountId, String blockId, String fileId, int partId, long size, long checksum, InputStream inputStream) throws ClientException {
		boolean succeed = false;
		Response response = getWSClient().upload(accountId, blockId, fileId, partId, size, checksum, inputStream);
		if (response != null) {
			FileContentMetadata fileContent = ModelConverter.DfsVolume.getUpdatedFileContent(this, response);
			if (fileContent != null) {
				succeed = true;
			}
		}
		return succeed;
	}

	@Override
	public boolean downloadFile(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ClientException {
		return false;
	}

}

// succeed = ModelConverter.DfsVolume.isUploaded(response);
// succeed = ModelConverter.DfsVolume.isUploaded(response);
