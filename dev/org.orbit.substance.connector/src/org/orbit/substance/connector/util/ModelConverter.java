package org.orbit.substance.connector.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfsvolume.DataBlockMetadata;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.FileContentMetadata;
import org.orbit.substance.connector.dfs.FileMetadataImpl;
import org.orbit.substance.connector.dfsvolume.DataBlockMetadataImpl;
import org.orbit.substance.connector.dfsvolume.FileContentMetadataImpl;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.model.dfsvolume.DataBlockMetadataDTO;
import org.orbit.substance.model.dfsvolume.FileContentMetadataDTO;
import org.orbit.substance.model.dfsvolume.PendingFile;
import org.orbit.substance.model.util.FilePartsReader;
import org.orbit.substance.model.util.FilePartsWriter;
import org.orbit.substance.model.util.PendingFilesReader;
import org.orbit.substance.model.util.PendingFilesWriter;
import org.origin.common.json.JSONUtil;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class ModelConverter {

	public static Dfs Dfs = new Dfs();
	public static DfsVolume DfsVolume = new DfsVolume();

	public static class Dfs {
		/**
		 * 
		 * @param fsClient
		 * @param fileMetadataDTO
		 * @return
		 */
		public FileMetadata toFileMetadata(DfsClient fsClient, FileMetadataDTO fileMetadataDTO) {
			if (fileMetadataDTO == null) {
				return null;
			}

			String dfsId = fileMetadataDTO.getDfsId();
			String accountId = fileMetadataDTO.getAccountId();
			String fileId = fileMetadataDTO.getFileId();
			String parentFileId = fileMetadataDTO.getParentFileId();
			String pathString = fileMetadataDTO.getPath();
			long size = fileMetadataDTO.getSize();
			boolean isDirectory = fileMetadataDTO.isDirectory();
			boolean isHidden = fileMetadataDTO.isHidden();
			boolean inTrash = fileMetadataDTO.isInTrash();
			String filePartsString = fileMetadataDTO.getFilePartsString();
			String propertiesString = fileMetadataDTO.getPropertiesString();
			long dateCreated = fileMetadataDTO.getDateCreated();
			long dateModified = fileMetadataDTO.getDateModified();

			Path path = new Path(pathString);
			List<FilePart> fileParts = toFileParts(filePartsString);
			Map<String, Object> properties = toProperties(propertiesString);

			FileMetadataImpl fileMetadata = new FileMetadataImpl(fsClient);
			fileMetadata.setDfsId(dfsId);
			fileMetadata.setAccountId(accountId);
			fileMetadata.setFileId(fileId);
			fileMetadata.setParentFileId(parentFileId);
			fileMetadata.setPath(path);
			fileMetadata.setSize(size);
			fileMetadata.setIsDirectory(isDirectory);
			fileMetadata.setHidden(isHidden);
			fileMetadata.setInTrash(inTrash);
			fileMetadata.setFileParts(fileParts);
			fileMetadata.setProperties(properties);
			fileMetadata.setDateCreated(dateCreated);
			fileMetadata.setDateModified(dateModified);

			return fileMetadata;
		}

		/**
		 * 
		 * @param fsClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata[] getFiles(DfsClient fsClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<FileMetadata> files = new ArrayList<FileMetadata>();
			List<FileMetadataDTO> fileDTOs = response.readEntity(new GenericType<List<FileMetadataDTO>>() {
			});
			for (FileMetadataDTO fileDTO : fileDTOs) {
				FileMetadata file = toFileMetadata(fsClient, fileDTO);
				if (file != null) {
					files.add(file);
				}
			}
			return files.toArray(new FileMetadata[files.size()]);
		}

		/**
		 * 
		 * @param fsClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public FileMetadata getFile(DfsClient fsClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			FileMetadata file = null;
			FileMetadataDTO fileDTO = response.readEntity(FileMetadataDTO.class);
			if (fileDTO != null) {
				file = toFileMetadata(fsClient, fileDTO);
			}
			return file;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public String getFileId(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			String fileId = null;
			try {
				fileId = ResponseUtil.getSimpleValue(response, "file_id", String.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return fileId;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean exists(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean exists = false;
			try {
				exists = ResponseUtil.getSimpleValue(response, "exists", Boolean.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return exists;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isDirectory(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean exists = false;
			try {
				exists = ResponseUtil.getSimpleValue(response, "isDirectory", Boolean.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return exists;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isUpdated(Response response) throws ClientException {
			return isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isRenamed(Response response) throws ClientException {
			return isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isDeleted(Response response) throws ClientException {
			return isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isEmptied(Response response) throws ClientException {
			return isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isSucceed(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean succeed = false;
			try {
				succeed = ResponseUtil.getSimpleValue(response, "succeed", Boolean.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return succeed;
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
		 * @param dfsVolumeClient
		 * @param dataBlockDTO
		 * @return
		 */
		public DataBlockMetadata toDataBlock(DfsVolumeClient dfsVolumeClient, DataBlockMetadataDTO dataBlockDTO) {
			if (dataBlockDTO == null) {
				return null;
			}

			DataBlockMetadataImpl dataBlock = new DataBlockMetadataImpl(dfsVolumeClient);
			if (dataBlockDTO != null) {
				String dfsId = dataBlockDTO.getDfsId();
				String dfsVolumeId = dataBlockDTO.getDfsVolumeId();
				String blockId = dataBlockDTO.getBlockId();
				String accountId = dataBlockDTO.getAccountId();
				long capacity = dataBlockDTO.getCapacity();
				long size = dataBlockDTO.getSize();
				String pendingFilesString = dataBlockDTO.getPendingFilesString();
				String propertiesString = dataBlockDTO.getPropertiesString();
				long dateCreated = dataBlockDTO.getDateCreated();
				long dateModified = dataBlockDTO.getDateModified();

				List<PendingFile> pendingFiles = toPendingFiles(pendingFilesString);
				Map<String, Object> properties = toProperties(propertiesString);

				dataBlock.setDfsId(dfsId);
				dataBlock.setDfsVolumeId(dfsVolumeId);
				dataBlock.setBlockId(blockId);
				dataBlock.setAccountId(accountId);
				dataBlock.setCapacity(capacity);
				dataBlock.setSize(size);
				dataBlock.setPendingFiles(pendingFiles);
				dataBlock.setProperties(properties);
				dataBlock.setDateCreated(dateCreated);
				dataBlock.setDateModified(dateModified);
			}
			return dataBlock;
		}

		/**
		 * 
		 * @param dfsVolumeClient
		 * @param fileContentDTO
		 * @return
		 */
		public FileContentMetadata toFileContent(DfsVolumeClient dfsVolumeClient, FileContentMetadataDTO fileContentDTO) {
			if (fileContentDTO == null) {
				return null;
			}

			String dfsId = fileContentDTO.getDfsId();
			String dfsVolumeId = fileContentDTO.getDfsVolumeId();
			String blockId = fileContentDTO.getBlockId();
			String fileId = fileContentDTO.getFileId();
			int partId = fileContentDTO.getPartId();
			long size = fileContentDTO.getSize();
			long checksum = fileContentDTO.getChecksum();
			long dateCreated = fileContentDTO.getDateCreated();
			long dateModified = fileContentDTO.getDateModified();

			FileContentMetadataImpl fileContent = new FileContentMetadataImpl(dfsVolumeClient);
			fileContent.setDfsId(dfsId);
			fileContent.setDfsVolumeId(dfsVolumeId);
			fileContent.setBlockId(blockId);
			fileContent.setFileId(fileId);
			fileContent.setPartId(partId);
			fileContent.setSize(size);
			fileContent.setChecksum(checksum);
			fileContent.setDateCreated(dateCreated);
			fileContent.setDateModified(dateModified);

			return fileContent;
		}

		/**
		 * 
		 * @param dfsVolumeClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata[] getDataBlocks(DfsVolumeClient dfsVolumeClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			List<DataBlockMetadata> datablocks = new ArrayList<DataBlockMetadata>();
			List<DataBlockMetadataDTO> datablockDTOs = response.readEntity(new GenericType<List<DataBlockMetadataDTO>>() {
			});
			for (DataBlockMetadataDTO datablockDTO : datablockDTOs) {
				DataBlockMetadata datablock = toDataBlock(dfsVolumeClient, datablockDTO);
				if (datablock != null) {
					datablocks.add(datablock);
				}
			}
			return datablocks.toArray(new DataBlockMetadata[datablocks.size()]);
		}

		/**
		 * 
		 * @param dfsVolumeClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata getDataBlock(DfsVolumeClient dfsVolumeClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			DataBlockMetadata datablock = null;
			DataBlockMetadataDTO datablockDTO = response.readEntity(DataBlockMetadataDTO.class);
			if (datablockDTO != null) {
				datablock = toDataBlock(dfsVolumeClient, datablockDTO);
			}
			return datablock;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean exists(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean exists = false;
			try {
				exists = ResponseUtil.getSimpleValue(response, "exists", Boolean.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return exists;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isUpdated(Response response) throws ClientException {
			return isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isDeleted(Response response) throws ClientException {
			return isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isUploaded(Response response) throws ClientException {
			return isSucceed(response);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public FileContentMetadata getUpdatedFileContent(DfsVolumeClient dfsVolumeClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			FileContentMetadata fileContent = null;
			FileContentMetadataDTO fileContentDTO = response.readEntity(FileContentMetadataDTO.class);
			if (fileContentDTO != null) {
				fileContent = toFileContent(dfsVolumeClient, fileContentDTO);
			}
			return fileContent;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public boolean isSucceed(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean succeed = false;
			try {
				succeed = ResponseUtil.getSimpleValue(response, "succeed", Boolean.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return succeed;
		}

		/**
		 * 
		 * @param dfsVolumeClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public FileContentMetadata[] getFileContents(DfsVolumeClient dfsVolumeClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			List<FileContentMetadata> fileContents = new ArrayList<FileContentMetadata>();
			List<FileContentMetadataDTO> fileContentDTOs = response.readEntity(new GenericType<List<FileContentMetadataDTO>>() {
			});
			for (FileContentMetadataDTO fileContentDTO : fileContentDTOs) {
				FileContentMetadata fileContent = toFileContent(dfsVolumeClient, fileContentDTO);
				if (fileContent != null) {
					fileContents.add(fileContent);
				}
			}
			return fileContents.toArray(new FileContentMetadata[fileContents.size()]);
		}

		/**
		 * 
		 * @param dfsVolumeClient
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public FileContentMetadata getFileContent(DfsVolumeClient dfsVolumeClient, Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			FileContentMetadata fileContent = null;
			FileContentMetadataDTO fileContentDTO = response.readEntity(FileContentMetadataDTO.class);
			if (fileContentDTO != null) {
				fileContent = toFileContent(dfsVolumeClient, fileContentDTO);
			}
			return fileContent;
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
	}

}

// long size = dto.getSize();
// int startIndex = dto.getStartIndex();
// int endIndex = dto.getEndIndex();
// metadata.setSize(size);
// metadata.setStartIndex(startIndex);
// metadata.setEndIndex(endIndex);
