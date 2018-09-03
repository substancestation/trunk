package org.orbit.substance.api.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfsvolume.DataBlockMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.model.dfs.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;

public class SubstanceClientsUtil {

	public static Dfs Dfs = new Dfs();
	public static DfsVolume DfsVolume = new DfsVolume();

	public static class Dfs {
		public static FileMetadata[] EMPTY_FILES = new FileMetadata[0];

		/**
		 * 
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public ServiceMetadata getDfsMetadata(String dfsServiceUrl, String accessToken) throws ClientException {
			ServiceMetadata metadata = null;
			DfsClient client = getDfsClient(dfsServiceUrl, accessToken);
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
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata[] listRoots(String dfsServiceUrl, String accessToken) throws ClientException {
			FileMetadata[] rootFiles = null;
			DfsClient dfsClient = getDfsClient(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				rootFiles = dfsClient.listRoots();
			}
			if (rootFiles == null) {
				rootFiles = EMPTY_FILES;
			}
			return rootFiles;
		}

		/**
		 * 
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @param path
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata createNewFile(String dfsServiceUrl, String accessToken, Path path) throws ClientException {
			FileMetadata file = null;
			DfsClient dfsClient = getDfsClient(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				file = dfsClient.createNewFile(path);
			}
			return file;
		}

		/**
		 * 
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @param path
		 * @param localFile
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata createNewFile(String dfsServiceUrl, String accessToken, Path path, java.io.File localFile) throws ClientException {
			FileMetadata file = null;
			DfsClient dfsClient = getDfsClient(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				long size = localFile.length();
				file = dfsClient.createNewFile(path, size);
			}
			return file;
		}

		/**
		 * 
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @param parentFileId
		 * @param localFile
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata createNewFile(String dfsServiceUrl, String accessToken, String parentFileId, java.io.File localFile) throws ClientException {
			FileMetadata file = null;
			DfsClient dfsClient = getDfsClient(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				String fileName = localFile.getName();
				long size = localFile.length();

				file = dfsClient.createNewFile(parentFileId, fileName, size);

				if (file != null) {
					List<FilePart> fileParts = file.getFileParts();
					for (FilePart filePart : fileParts) {
						long startIndex = filePart.getStartIndex();
						long endIndex = filePart.getEndIndex();

					}
				}
			}
			return file;
		}

		/**
		 * 
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @param fileId
		 * @param size
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata allocateVolumes(String dfsServiceUrl, String accessToken, String fileId, long size) throws ClientException {
			FileMetadata file = null;
			DfsClient dfsClient = getDfsClient(dfsServiceUrl, accessToken);
			if (dfsClient != null) {
				file = dfsClient.allocateVolumes(fileId, size);
			}
			return file;
		}

		/**
		 * 
		 * @param dfsServiceUrl
		 * @param accessToken
		 * @return
		 */
		public DfsClient getDfsClient(String dfsServiceUrl, String accessToken) {
			DfsClient dfsClient = null;
			if (dfsServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, dfsServiceUrl);
				dfsClient = SubstanceClients.getInstance().getDfsClient(properties);
			}
			return dfsClient;
		}

		// /**
		// *
		// * @param dfsServiceUrl
		// * @param accessToken
		// * @param parentFileId
		// * @param localFiles
		// * @return
		// * @throws ClientException
		// */
		// public File uploadFile(String dfsServiceUrl, String accessToken, String parentFileId, List<java.io.File> localFiles) throws ClientException {
		// File file = null;
		// FileSystemClient dfsClient = getFileSystemClient(dfsServiceUrl, accessToken);
		// if (dfsClient != null) {
		// java.io.File localFile = (localFiles != null && !localFiles.isEmpty()) ? localFiles.get(0) : null;
		// if (localFile != null && localFile.exists()) {
		// file = dfsClient.uploadFile(parentFileId, localFile);
		// }
		// }
		// return file;
		// }
	}

	public static class DfsVolume {
		public static final DataBlockMetadata[] EMPTY_DATA_BLOCKS = new DataBlockMetadata[0];

		/**
		 * 
		 * @param fileContentServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public ServiceMetadata getDfsVolumeMetadata(String fileContentServiceUrl, String accessToken) throws ClientException {
			ServiceMetadata metadata = null;
			DfsVolumeClient client = getDfsVolumeClient(fileContentServiceUrl, accessToken);
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
			DfsVolumeClient client = getDfsVolumeClient(fileContentServiceUrl, accessToken);
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
			DfsVolumeClient client = getDfsVolumeClient(fileContentServiceUrl, accessToken);
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
			DfsVolumeClient client = getDfsVolumeClient(fileContentServiceUrl, accessToken);
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
			DfsVolumeClient client = getDfsVolumeClient(fileContentServiceUrl, accessToken);
			if (client != null) {
				datablock = client.createDataBlock(accountId, capacity);
			}
			return datablock;
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
			DfsVolumeClient client = getDfsVolumeClient(fileContentServiceUrl, accessToken);
			if (client != null) {
				succeed = client.deleteDataBlock(accountId, blockId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param fileContentServiceUrl
		 * @param accessToken
		 * @return
		 */
		public DfsVolumeClient getDfsVolumeClient(String fileContentServiceUrl, String accessToken) {
			DfsVolumeClient client = null;
			if (fileContentServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, fileContentServiceUrl);
				client = SubstanceClients.getInstance().getDfsVolumeClient(properties);
			}
			return client;
		}
	}

}
