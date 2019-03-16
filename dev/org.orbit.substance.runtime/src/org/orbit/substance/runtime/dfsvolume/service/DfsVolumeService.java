package org.orbit.substance.runtime.dfsvolume.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfsvolume.PendingFile;
import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAware;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenAware;
import org.origin.common.service.WebServiceAware;

public interface DfsVolumeService extends ConnectionAware, WebServiceAware, EditPoliciesAware, AccessTokenAware {

	Map<Object, Object> getInitProperties();

	// ----------------------------------------------------------------------
	// Methods about service
	// ----------------------------------------------------------------------
	String getDfsId();

	String getDfsVolumeId();

	long getVolumeCapacity();

	long getVolumeSize();

	long getDefaultBlockCapacity();

	// ----------------------------------------------------------------------
	// Data blocks
	// ----------------------------------------------------------------------
	DataBlockMetadata[] getDataBlocks() throws ServerException;

	DataBlockMetadata[] getDataBlocks(String accountId) throws ServerException;

	DataBlockMetadata getDataBlock(String accountId, String blockId) throws ServerException;

	boolean dataBlockExists(String accountId, String blockId) throws ServerException;

	boolean isDataBlockEmpty(String accountId, String blockId) throws ServerException;

	DataBlockMetadata createDataBlock(String accountId, long capacity, List<PendingFile> pendingFiles) throws ServerException;

	boolean updateDataBlockSize(String accountId, String blockId, long newSize) throws ServerException;

	boolean updateDataBlockSizeByDelta(String accountId, String blockId, long sizeDelta) throws ServerException;

	boolean updatePendingFiles(String accountId, String blockId, String fileId, boolean cleanExpiredPendingFiles) throws ServerException;

	boolean deleteDataBlock(String accountId, String blockId) throws ServerException;

	// ----------------------------------------------------------------------
	// File contents
	// ----------------------------------------------------------------------
	FileContentMetadata[] getFileContents(String accountId, String blockId) throws ServerException;

	FileContentMetadata getFileContent(String accountId, String blockId, String fileId, int partId) throws ServerException;

	FileContentMetadata createFileContent(String accountId, String blockId, String fileId, int partId, long size, long checksum) throws ServerException;

	boolean updateFileContent(String accountId, String blockId, FileContentMetadata fileContentMetadata) throws ServerException;

	boolean deleteFileContent(String accountId, String blockId, String fileId, int partId) throws ServerException;

	boolean deleteFileContents(String accountId, String blockId) throws ServerException;

	// ----------------------------------------------------------------------
	// Download/upload
	// ----------------------------------------------------------------------
	InputStream getContent(String accountId, String blockId, String fileId, int partId) throws ServerException;

	void getContent(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ServerException;

	boolean setContent(String accountId, String blockId, String fileId, int partId, InputStream input) throws ServerException;

}
