package org.orbit.substance.runtime.dfsvolume.service;

import java.io.InputStream;
import java.io.OutputStream;

import org.orbit.substance.runtime.model.dfsvolume.DataBlockMetadata;
import org.orbit.substance.runtime.model.dfsvolume.FileContentMetadata;
import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.WebServiceAware;

/**
 * File system id: dfs_metadata_1
 * 
 * File content id: dfs_content_1_1, dfs_content_1_2, dfs_content_1_3, ...
 * 
 * Block id: 1, 2, 3, 4, ...
 * 
 */
public interface FileContentService extends WebServiceAware, ConnectionAware, EditPoliciesAwareService {

	// ----------------------------------------------------------------------
	// Methods about service
	// ----------------------------------------------------------------------
	String getDfsId();

	String getVolumeId();

	long getVolumeCapacity();

	long getVolumeSize();

	long getDefaultBlockCapacity();

	// ----------------------------------------------------------------------
	// Methods for accessing data blocks
	// ----------------------------------------------------------------------
	DataBlockMetadata[] getDataBlocks() throws ServerException;

	DataBlockMetadata[] getDataBlocks(String accountId) throws ServerException;

	DataBlockMetadata getDataBlock(String accountId, String blockId) throws ServerException;

	DataBlockMetadata createDataBlock(String accountId, long capacity) throws ServerException;

	boolean updateDataBlock(String accountId, DataBlockMetadata datablockMetadata) throws ServerException;

	boolean deleteDataBlock(String accountId, String blockId) throws ServerException;

	// ----------------------------------------------------------------------
	// Methods for accessing file contents of a data block
	// ----------------------------------------------------------------------
	FileContentMetadata[] getFileContentMetadatas(String accountId, String blockId) throws ServerException;

	FileContentMetadata getFileContentMetadata(String accountId, String blockId, String fileId, int partId) throws ServerException;

	FileContentMetadata createFileContentMetadata(String accountId, String blockId, String fileId, int partId, long size, int startIndex, int endIndex, String checksum) throws ServerException;

	boolean updateFileContentMetadata(String accountId, String blockId, FileContentMetadata fileContentMetadata) throws ServerException;

	boolean deleteFileContent(String accountId, String blockId, String fileId, int partId) throws ServerException;

	// ----------------------------------------------------------------------
	// Methods for getting/setting file contents
	// ----------------------------------------------------------------------
	InputStream getContentInputStream(String accountId, String blockId, String fileId, int partId) throws ServerException;

	void getContent(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ServerException;

	boolean setContent(String accountId, String blockId, String fileId, int partId, InputStream input) throws ServerException;

	boolean setContent(String accountId, String blockId, String fileId, int partId, InputStream input, FileContentMetadata fileContentMetadata) throws ServerException;

}
