package org.orbit.substance.api.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.substance.api.dfs.filecontent.DataBlockMetadata;
import org.orbit.substance.api.dfs.filecontent.FileContentClient;
import org.orbit.substance.api.dfs.filesystem.FileSystemClient;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;

public class SubstanceClientsUtil {

	public static File_System File_System = new File_System();
	public static File_Content File_Content = new File_Content();

	public static class File_System {
		/**
		 * 
		 * @param fileSystemServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public ServiceMetadata getFileSystemMetadata(String fileSystemServiceUrl, String accessToken) throws ClientException {
			ServiceMetadata metadata = null;
			FileSystemClient client = getFileSystemClient(fileSystemServiceUrl, accessToken);
			if (client != null) {
				metadata = client.getMetadata();
			}
			if (metadata == null) {
				metadata = new ServiceMetadataImpl();
			}
			return metadata;
		}

		/**
		 * 
		 * @param fileSystemServiceUrl
		 * @param accessToken
		 * @return
		 */
		public FileSystemClient getFileSystemClient(String fileSystemServiceUrl, String accessToken) {
			FileSystemClient client = null;
			if (fileSystemServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, fileSystemServiceUrl);
				client = SubstanceClients.getInstance().getFileSystemClient(properties);
			}
			return client;
		}
	}

	public static class File_Content {
		public static final DataBlockMetadata[] EMPTY_DATA_BLOCKS = new DataBlockMetadata[0];

		/**
		 * 
		 * @param fileContentServiceUrl
		 * @param accessToken
		 * @return
		 */
		public FileContentClient getFileContentClient(String fileContentServiceUrl, String accessToken) {
			FileContentClient client = null;
			if (fileContentServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, fileContentServiceUrl);
				client = SubstanceClients.getInstance().getFileContentClient(properties);
			}
			return client;
		}

		/**
		 * 
		 * @param fileContentServiceUrl
		 * @param accessToken
		 * @param accountId
		 * @param blockId
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteDataBlock(String fileContentServiceUrl, String accessToken, String accountId, String blockId) throws ClientException {
			boolean succeed = false;
			FileContentClient client = getFileContentClient(fileContentServiceUrl, accessToken);
			if (client != null) {
				succeed = client.deleteDataBlock(accountId, blockId);
			}
			return succeed;
		}

		// ----------------------------------------------------------------------
		// Methods for accessing data blocks (of users)
		// ----------------------------------------------------------------------
		/**
		 * 
		 * @param fileContentServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public ServiceMetadata getFileContentMetadata(String fileContentServiceUrl, String accessToken) throws ClientException {
			ServiceMetadata metadata = null;
			FileContentClient client = getFileContentClient(fileContentServiceUrl, accessToken);
			if (client != null) {
				metadata = client.getMetadata();
			}
			if (metadata == null) {
				metadata = new ServiceMetadataImpl();
			}
			return metadata;
		}

		/**
		 * 
		 * @param fileContentServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata[] listDataBlocks(String fileContentServiceUrl, String accessToken) throws ClientException {
			DataBlockMetadata[] datablocks = null;
			FileContentClient client = getFileContentClient(fileContentServiceUrl, accessToken);
			if (client != null) {
				datablocks = client.getDataBlocks();
			}
			if (datablocks == null) {
				datablocks = EMPTY_DATA_BLOCKS;
			}
			return datablocks;
		}

		/**
		 * 
		 * @param fileContentServiceUrl
		 * @param accessToken
		 * @param accountId
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata[] listDataBlocks(String fileContentServiceUrl, String accessToken, String accountId) throws ClientException {
			DataBlockMetadata[] datablocks = null;
			FileContentClient client = getFileContentClient(fileContentServiceUrl, accessToken);
			if (client != null) {
				datablocks = client.getDataBlocks(accountId);
			}
			if (datablocks == null) {
				datablocks = EMPTY_DATA_BLOCKS;
			}
			return datablocks;
		}

		/**
		 * 
		 * @param fileContentServiceUrl
		 * @param accessToken
		 * @param accountId
		 * @param blockId
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata getDataBlock(String fileContentServiceUrl, String accessToken, String accountId, String blockId) throws ClientException {
			DataBlockMetadata datablock = null;
			FileContentClient client = getFileContentClient(fileContentServiceUrl, accessToken);
			if (client != null) {
				datablock = client.getDataBlock(accountId, blockId);
			}
			return datablock;
		}

		/**
		 * 
		 * @param fileContentServiceUrl
		 * @param accessToken
		 * @param accountId
		 * @param capacity
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata createDataBlock(String fileContentServiceUrl, String accessToken, String accountId, long capacity) throws ClientException {
			DataBlockMetadata datablock = null;
			FileContentClient client = getFileContentClient(fileContentServiceUrl, accessToken);
			if (client != null) {
				datablock = client.createDataBlock(accountId, capacity);
			}
			return datablock;
		}
	}

}
