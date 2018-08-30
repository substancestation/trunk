package org.orbit.substance.api.dfsvolume;

import java.io.OutputStream;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface FileContentClient extends ServiceClient {

	// File content service metadata property names
	public static String PROP__FILE_SYSTEM_ID = "dfs_metadata_id"; // file system service id
	public static String PROP__FILE_CONTENT_ID = "dfs_content_id"; // file content servece id
	public static String PROP__TOTAL_CAPACITY = "total_capacity";
	public static String PROP__TOTAL_SIZE = "total_size";

	// ----------------------------------------------------------------------
	// Methods for accessing data blocks
	// ----------------------------------------------------------------------
	DataBlockMetadata[] getDataBlocks() throws ClientException;

	DataBlockMetadata[] getDataBlocks(String accountId) throws ClientException;

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
	boolean uploadFile(String accountId, String blockId, String fileId, FileContentMetadata fileContentMetadata) throws ClientException;

	boolean downloadFile(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ClientException;

}
