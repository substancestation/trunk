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
import org.orbit.substance.connector.util.ClientModelConverter;
import org.orbit.substance.model.RequestConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadataDTO;
import org.origin.common.rest.util.ResponseUtil;

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

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				dataBlocks = ClientModelConverter.DfsVolume.getDataBlocks(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
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

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				dataBlocks = ClientModelConverter.DfsVolume.getDataBlocks(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
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

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				dataBlocks = ClientModelConverter.DfsVolume.getDataBlocks(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
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

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ResponseUtil.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public DataBlockMetadata getDataBlock(String accountId, String blockId) throws ClientException {
		DataBlockMetadata dataBlock = null;

		Request request = new Request(RequestConstants.GET_DATA_BLOCK);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				dataBlock = ClientModelConverter.DfsVolume.getDataBlock(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return dataBlock;
	}

	@Override
	public DataBlockMetadata createDataBlock(String accountId, long capacity) throws ClientException {
		DataBlockMetadata dataBlock = null;

		Request request = new Request(RequestConstants.CREATE_DATA_BLOCK);
		request.setParameter("account_id", accountId);
		request.setParameter("capacity", capacity);

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				dataBlock = ClientModelConverter.DfsVolume.getDataBlock(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
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

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ClientModelConverter.DfsVolume.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteDataBlock(String accountId, String blockId) throws ClientException {
		boolean succeed = false;

		Request request = new Request(RequestConstants.DELETE_DATA_BLOCK);
		request.setParameter("account_id", accountId);
		request.setParameter("block_id", blockId);

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ClientModelConverter.DfsVolume.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
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

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				fileContents = ClientModelConverter.DfsVolume.getFileContents(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
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

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ResponseUtil.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
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

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				fileContent = ClientModelConverter.DfsVolume.getFileContent(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return fileContent;
	}

	@Override
	public boolean deleteFileContent(String accountId, String blockId, String fileId, int partId) throws ClientException {
		boolean succeed = false;

		Request request = new Request(RequestConstants.DELETE_FILE_CONTENT);
		if (accountId != null && !accountId.isEmpty()) {
			request.setParameter("account_id", accountId);
		}
		request.setParameter("block_id", blockId);
		request.setParameter("file_id", fileId);
		request.setParameter("part_id", partId);

		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				succeed = ClientModelConverter.DfsVolume.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return succeed;
	}

	// ----------------------------------------------------------------------
	// Upload and download
	// ----------------------------------------------------------------------
	@Override
	public FileContentMetadata uploadFile(String accountId, String blockId, String fileId, int partId, File file, long checksum) throws ClientException {
		FileContentMetadata fileContent = null;
		Response response = getWSClient().upload(accountId, blockId, fileId, partId, file, checksum);
		if (response != null) {
			fileContent = ClientModelConverter.DfsVolume.getUpdatedFileContent(this, response);
		}
		return fileContent;
	}

	@Override
	public FileContentMetadata uploadFile(String accountId, String blockId, String fileId, int partId, InputStream inputStream, long size, long checksum) throws ClientException {
		FileContentMetadata fileContent = null;
		Response response = getWSClient().upload(accountId, blockId, fileId, partId, inputStream, size, checksum);
		if (response != null) {
			fileContent = ClientModelConverter.DfsVolume.getUpdatedFileContent(this, response);
		}
		return fileContent;
	}

	@Override
	public boolean downloadFile(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ClientException {
		boolean succeed = false;
		try {
			succeed = getWSClient().download(accountId, blockId, fileId, partId, output);
		} catch (ClientException e) {
			throw e;
		}
		return succeed;
	}

}
