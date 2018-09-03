package org.orbit.substance.api.dfsvolume;

import java.io.InputStream;
import java.io.OutputStream;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface DfsVolumeClient extends ServiceClient {

	@Override
	DfsVolumeServiceMetadata getMetadata() throws ClientException;

	// ----------------------------------------------------------------------
	// Methods for accessing data blocks
	// ----------------------------------------------------------------------
	DataBlockMetadata[] getDataBlocks() throws ClientException;

	DataBlockMetadata[] getDataBlocks(String accountId) throws ClientException;

	DataBlockMetadata[] getDataBlocks(String accountId, long minFreeSpace) throws ClientException;

	boolean dataBlockExists(String accountId, String blockId) throws ClientException;

	DataBlockMetadata getDataBlock(String accountId, String blockId) throws ClientException;

	DataBlockMetadata createDataBlock(String accountId, long capacity) throws ClientException;

	boolean deleteDataBlock(String accountId, String blockId) throws ClientException;

	// ----------------------------------------------------------------------
	// Methods for accessing file contents of a data block
	// ----------------------------------------------------------------------
	FileContentMetadata[] getFileContentMetadatas(String accountId, String blockId) throws ClientException;

	boolean fileContentExists(String accountId, String blockId, String fileId, int partId) throws ClientException;

	FileContentMetadata getFileContentMetadata(String accountId, String blockId, String fileId, int partId) throws ClientException;

	boolean deleteFileContent(String accountId, String blockId, String fileId, int partId) throws ClientException;

	// ----------------------------------------------------------------------
	// Methods for uploading/downloading file contents
	// ----------------------------------------------------------------------
	boolean uploadFile(String accountId, String blockId, String fileId, int partId, String checksum, InputStream inputStream) throws ClientException;

	boolean downloadFile(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ClientException;

}
