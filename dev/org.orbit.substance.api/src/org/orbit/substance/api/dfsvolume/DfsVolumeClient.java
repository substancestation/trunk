package org.orbit.substance.api.dfsvolume;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface DfsVolumeClient extends ServiceClient {

	@Override
	DfsVolumeServiceMetadata getMetadata() throws ClientException;

	// ----------------------------------------------------------------------
	// Data blocks
	// ----------------------------------------------------------------------
	DataBlockMetadata[] getDataBlocks() throws ClientException;

	DataBlockMetadata[] getDataBlocks(String accountId) throws ClientException;

	DataBlockMetadata[] getDataBlocks(String accountId, long minFreeSpace) throws ClientException;

	boolean dataBlockExists(String accountId, String blockId) throws ClientException;

	DataBlockMetadata getDataBlock(String accountId, String blockId) throws ClientException;

	DataBlockMetadata createDataBlock(String accountId, long capacity) throws ClientException;

	boolean updateDataBlockSizeByDelta(String accountId, String blockId, long sizeDelta) throws ClientException;

	boolean deleteDataBlock(String accountId, String blockId) throws ClientException;

	// ----------------------------------------------------------------------
	// File contents
	// ----------------------------------------------------------------------
	FileContentMetadata[] getFileContentMetadatas(String accountId, String blockId) throws ClientException;

	boolean fileContentExists(String accountId, String blockId, String fileId, int partId) throws ClientException;

	FileContentMetadata getFileContentMetadata(String accountId, String blockId, String fileId, int partId) throws ClientException;

	boolean deleteFileContent(String accountId, String blockId, String fileId, int partId) throws ClientException;

	// ----------------------------------------------------------------------
	// Upload and download
	// ----------------------------------------------------------------------
	FileContentMetadata uploadFile(String accountId, String blockId, String fileId, long checksum, File file) throws ClientException;

	FileContentMetadata uploadFile(String accountId, String blockId, String fileId, int partId, long size, long checksum, InputStream inputStream) throws ClientException;

	boolean downloadFile(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ClientException;

}
