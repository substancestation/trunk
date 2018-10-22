package org.orbit.substance.runtime.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.model.dfsvolume.DataBlockMetadataDTO;
import org.orbit.substance.model.dfsvolume.FileContentMetadataDTO;
import org.orbit.substance.model.dfsvolume.PendingFile;
import org.orbit.substance.model.util.FilePartsReader;
import org.orbit.substance.model.util.FilePartsWriter;
import org.orbit.substance.model.util.PendingFilesReader;
import org.orbit.substance.model.util.PendingFilesWriter;
import org.orbit.substance.runtime.dfs.service.FileMetadata;
import org.orbit.substance.runtime.dfsvolume.service.DataBlockMetadata;
import org.orbit.substance.runtime.dfsvolume.service.FileContentMetadata;
import org.origin.common.json.JSONUtil;
import org.origin.common.resource.Path;

public class ModelConverter {

	public static Dfs Dfs = new Dfs();
	public static DfsVolume DfsVolume = new DfsVolume();

	public static class Dfs {
		/**
		 * 
		 * @param fileMetadata
		 * @return
		 */
		public FileMetadataDTO toDTO(FileMetadata fileMetadata) {
			if (fileMetadata == null) {
				return null;
			}

			String dfsId = fileMetadata.getDfsId();
			String accountId = fileMetadata.getAccountId();
			String fileId = fileMetadata.getFileId();
			String parentFileId = fileMetadata.getParentFileId();
			Path path = fileMetadata.getPath();
			long size = fileMetadata.getSize();
			boolean isDirectory = fileMetadata.isDirectory();
			boolean isHidden = fileMetadata.isHidden();
			boolean inTrash = fileMetadata.isInTrash();
			List<FilePart> fileParts = fileMetadata.getFileParts();
			Map<String, Object> properties = fileMetadata.getProperties();
			long dateCreated = fileMetadata.getDateCreated();
			long dateModified = fileMetadata.getDateModified();

			String filePartsString = toFilePartsString(fileParts);
			String propertiesString = toPropertiesString(properties);

			FileMetadataDTO fileMetadataDTO = new FileMetadataDTO();
			fileMetadataDTO.setDfsId(dfsId);
			fileMetadataDTO.setAccountId(accountId);
			fileMetadataDTO.setFileId(fileId);
			fileMetadataDTO.setParentFileId(parentFileId);
			fileMetadataDTO.setPath(path.getPathString());
			fileMetadataDTO.setSize(size);
			fileMetadataDTO.setDirectory(isDirectory);
			fileMetadataDTO.setHidden(isHidden);
			fileMetadataDTO.setInTrash(inTrash);
			fileMetadataDTO.setFilePartsString(filePartsString);
			fileMetadataDTO.setPropertiesString(propertiesString);
			fileMetadataDTO.setDateCreated(dateCreated);
			fileMetadataDTO.setDateModified(dateModified);

			return fileMetadataDTO;
		}

		/**
		 * Convert FilePart objects to json string
		 * 
		 * @param fileParts
		 * @return
		 */
		public String toFilePartsString(List<FilePart> fileParts) {
			FilePartsWriter writer = new FilePartsWriter();
			String filePartsString = writer.write(fileParts);
			if (filePartsString == null) {
				filePartsString = "";
			}
			return filePartsString;
		}

		/**
		 * Convert json string to FilePart objects
		 * 
		 * @param filePartsString
		 * @return
		 */
		public List<FilePart> toFileParts(String filePartsString) {
			FilePartsReader reader = new FilePartsReader();
			List<FilePart> fileParts = reader.read(filePartsString);
			if (fileParts == null) {
				fileParts = new ArrayList<FilePart>();
			}
			return fileParts;
		}

		/**
		 * 
		 * @param properties
		 * @return
		 */
		public String toPropertiesString(Map<String, Object> properties) {
			String propertiesString = JSONUtil.toJsonString(properties);
			return propertiesString;
		}

		/**
		 * 
		 * @param propertiesString
		 * @return
		 */
		public Map<String, Object> toProperties(String propertiesString) {
			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
			return properties;
		}
	}

	public static class DfsVolume {
		/**
		 * 
		 * @param dataBlock
		 * @return
		 */
		public DataBlockMetadataDTO toDTO(DataBlockMetadata dataBlock) {
			if (dataBlock == null) {
				return null;
			}

			String dfsId = dataBlock.getDfsId();
			String dfsVolumeId = dataBlock.getDfsVolumeId();
			String blockId = dataBlock.getBlockId();
			String accountId = dataBlock.getAccountId();
			long capacity = dataBlock.getCapacity();
			long size = dataBlock.getSize();
			List<PendingFile> pendingFiles = dataBlock.getPendingFiles();
			Map<String, Object> properties = dataBlock.getProperties();
			long dateCreated = dataBlock.getDateCreated();
			long dateModified = dataBlock.getDateModified();

			String pendingFilesString = toPendingFilesString(pendingFiles);
			String propertiesString = toPropertiesString(properties);

			DataBlockMetadataDTO dataBlockDTO = new DataBlockMetadataDTO();
			dataBlockDTO.setDfsId(dfsId);
			dataBlockDTO.setDfsVolumeId(dfsVolumeId);
			dataBlockDTO.setBlockId(blockId);
			dataBlockDTO.setAccountId(accountId);
			dataBlockDTO.setCapacity(capacity);
			dataBlockDTO.setSize(size);
			dataBlockDTO.setPendingFilesString(pendingFilesString);
			dataBlockDTO.setPropertiesString(propertiesString);
			dataBlockDTO.setDateCreated(dateCreated);
			dataBlockDTO.setDateModified(dateModified);

			return dataBlockDTO;
		}

		/**
		 * 
		 * @param fileContent
		 * @return
		 */
		public FileContentMetadataDTO toDTO(FileContentMetadata fileContent) {
			if (fileContent == null) {
				return null;
			}

			String dfsId = fileContent.getDfsId();
			String dfsVolumeId = fileContent.getDfsVolumeId();
			String blockId = fileContent.getBlockId();
			String fileId = fileContent.getFileId();
			int partId = fileContent.getPartId();
			long size = fileContent.getSize();
			long checksum = fileContent.getChecksum();
			long dateCreated = fileContent.getDateCreated();
			long dateModified = fileContent.getDateModified();

			FileContentMetadataDTO fileContentDTO = new FileContentMetadataDTO();
			fileContentDTO.setDfsId(dfsId);
			fileContentDTO.setDfsVolumeId(dfsVolumeId);
			fileContentDTO.setBlockId(blockId);
			fileContentDTO.setFileId(fileId);
			fileContentDTO.setPartId(partId);
			fileContentDTO.setSize(size);
			fileContentDTO.setChecksum(checksum);
			fileContentDTO.setDateCreated(dateCreated);
			fileContentDTO.setDateModified(dateModified);

			return fileContentDTO;
		}

		/**
		 * Convert PendingFile objects to json string
		 * 
		 * @param pendingFiles
		 * @return
		 */
		public String toPendingFilesString(List<PendingFile> pendingFiles) {
			PendingFilesWriter writer = new PendingFilesWriter();
			String pendingFilesString = writer.write(pendingFiles);
			if (pendingFilesString == null) {
				pendingFilesString = "";
			}
			return pendingFilesString;
		}

		/**
		 * Convert json string to PendingFile objects
		 * 
		 * @param pendingFilesString
		 * @return
		 */
		public List<PendingFile> toPendingFiles(String pendingFilesString) {
			PendingFilesReader reader = new PendingFilesReader();
			List<PendingFile> pendingFiles = reader.read(pendingFilesString);
			if (pendingFiles == null) {
				pendingFiles = new ArrayList<PendingFile>();
			}
			return pendingFiles;
		}

		/**
		 * 
		 * @param properties
		 * @return
		 */
		public String toPropertiesString(Map<String, Object> properties) {
			String propertiesString = JSONUtil.toJsonString(properties);
			return propertiesString;
		}

		/**
		 * 
		 * @param propertiesString
		 * @return
		 */
		public Map<String, Object> toProperties(String propertiesString) {
			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
			return properties;
		}

		/**
		 * 
		 * @param dfsVolumeClientResolver
		 * @param accessToken
		 * @param dfsId
		 * @return
		 * @throws IOException
		 */
		public DfsVolumeClient[] getDfsVolumeClient(DfsVolumeClientResolver dfsVolumeClientResolver, String accessToken, String dfsId) throws IOException {
			DfsVolumeClient[] dfsVolumeClients = dfsVolumeClientResolver.resolve(dfsId, accessToken, SubstanceComparators.DfsVolumeIndexItemComparatorByVolumeId_ASC);
			return dfsVolumeClients;
		}
	}

}

// /**
// *
// * @param path
// * @return
// */
// public PathDTO toDTO(Path path) {
// String pathString = path.getPathString();
//
// PathDTO dto = new PathDTO();
// dto.setPathString(pathString);
// return dto;
// }

// String name = fileMetadata.getName();
// dto.setName(name);

// long size = fileContent.getSize();
// int startIndex = fileContent.getStartIndex();
// int endIndex = fileContent.getEndIndex();
// dto.setSize(size);
// dto.setStartIndex(startIndex);
// dto.setEndIndex(endIndex);

// String[] fileIds = dataBlock.getFileIds();
// dto.setFileIds(fileIds);

// /**
// *
// * @param dfsVolumeClientResolver
// * @param accessToken
// * @param dfsId
// * @return
// * @throws IOException
// */
// public DfsVolumeClient[] getDfsVolumeClient(DfsVolumeClientResolver dfsVolumeClientResolver, String accessToken, String dfsId) throws IOException {
// DfsVolumeClient[] dfsVolumeClients = dfsVolumeClientResolver.resolve(dfsId, accessToken, Comparators.DfsVolumeIndexItemComparatorByVolumeId_ASC);
// return dfsVolumeClients;
// }

// if (dfsId == null) {
// throw new IllegalArgumentException("dfsId is null.");
// }
//
// List<DfsVolumeClient> dfsVolumeClients = new ArrayList<DfsVolumeClient>();
//
// List<IndexItem> dfsVolumesIndexItems = new ArrayList<IndexItem>();
// IndexService indexService = InfraClientsUtil.Indexes.getIndexServiceClient(indexServiceUrl, accessToken);
// List<IndexItem> indexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE);
// for (IndexItem currIndexItem : indexItems) {
// String currDfsId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID);
// if (currDfsId != null && currDfsId.equals(dfsId)) {
// dfsVolumesIndexItems.add(currIndexItem);
// }
// }
//
// Comparators.sort(dfsVolumesIndexItems, Comparators.DfsVolumeIndexItemComparatorByVolumeId_ASC);
//
// for (IndexItem dfsVolumeIndexItem : dfsVolumesIndexItems) {
// boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsVolumeIndexItem);
// if (isOnline) {
// String hostUrl = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__HOST_URL);
// String contextRoot = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__CONTEXT_ROOT);
//
// if (hostUrl != null && contextRoot != null) {
// String dfsVolumeServiceUrl = hostUrl;
// if (!hostUrl.endsWith("/") && !contextRoot.startsWith("/")) {
// dfsVolumeServiceUrl += "/";
// }
// dfsVolumeServiceUrl += contextRoot;
//
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(WSClientConstants.REALM, null);
// properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
// properties.put(WSClientConstants.URL, dfsVolumeServiceUrl);
//
// DfsVolumeClient dfsVolumeClient = SubstanceClients.getInstance().getDfsVolumeClient(properties);
// dfsVolumeClients.add(dfsVolumeClient);
// }
// }
// }
//
// return dfsVolumeClients.toArray(new DfsVolumeClient[dfsVolumeClients.size()]);
