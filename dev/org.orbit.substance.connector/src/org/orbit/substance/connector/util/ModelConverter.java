package org.orbit.substance.connector.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfs.FilePart;
import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.Path;
import org.orbit.substance.api.dfsvolume.DataBlockMetadata;
import org.orbit.substance.api.dfsvolume.FileContentMetadata;
import org.orbit.substance.connector.dfs.FileImpl;
import org.orbit.substance.connector.dfsvolume.DataBlockMetadataImpl;
import org.orbit.substance.connector.dfsvolume.FileContentMetadataImpl;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.model.dfsvolume.DataBlockMetadataDTO;
import org.orbit.substance.model.dfsvolume.FileContentMetadataDTO;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class ModelConverter {

	public static Dfs Dfs = new Dfs();
	public static DfsVolume DfsVolume = new DfsVolume();

	public static class Dfs {
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
		 * @param filePartsString
		 * @return
		 */
		public List<FilePart> toFileParts(String filePartsString) {
			List<FilePart> fileParts = new ArrayList<FilePart>();
			if (filePartsString != null) {

			}
			return fileParts;
		}

		/**
		 * 
		 * @param fsClient
		 * @param fileMetadataDTO
		 * @return
		 */
		public FileMetadata toFile(DfsClient fsClient, FileMetadataDTO fileMetadataDTO) {
			if (fileMetadataDTO == null) {
				return null;
			}

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
			Map<String, Object> properties = toProperties(propertiesString);
			List<FilePart> fileParts = toFileParts(filePartsString);

			FileImpl file = new FileImpl(fsClient);
			file.setAccountId(accountId);
			file.setFileId(fileId);
			file.setParentFileId(parentFileId);
			file.setPath(path);
			file.setSize(size);
			file.setIsDirectory(isDirectory);
			file.setHidden(isHidden);
			file.setInTrash(inTrash);
			file.setProperties(properties);
			file.setFileParts(fileParts);
			file.setDateCreated(dateCreated);
			file.setDateModified(dateModified);

			return file;
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
				FileMetadata file = toFile(fsClient, fileDTO);
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
				file = toFile(fsClient, fileDTO);
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
	}

	public static class DfsVolume {
		/**
		 * 
		 * @param datablockDTO
		 * @return
		 */
		public DataBlockMetadata toDataBlock(DataBlockMetadataDTO datablockDTO) {
			if (datablockDTO == null) {
				return null;
			}

			DataBlockMetadataImpl dataBlock = new DataBlockMetadataImpl();
			if (datablockDTO != null) {
				String blockId = datablockDTO.getBlockId();
				String accountId = datablockDTO.getAccountId();
				long capacity = datablockDTO.getCapacity();
				long size = datablockDTO.getSize();
				// String[] fileIds = datablockDTO.getFileIds();

				dataBlock.setBlockId(blockId);
				dataBlock.setAccountId(accountId);
				dataBlock.setCapacity(capacity);
				dataBlock.setSize(size);
				// dataBlock.setFileIds(fileIds);
			}
			return dataBlock;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata[] getDataBlocks(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			List<DataBlockMetadata> datablocks = new ArrayList<DataBlockMetadata>();
			List<DataBlockMetadataDTO> datablockDTOs = response.readEntity(new GenericType<List<DataBlockMetadataDTO>>() {
			});
			for (DataBlockMetadataDTO datablockDTO : datablockDTOs) {
				DataBlockMetadata datablock = toDataBlock(datablockDTO);
				if (datablock != null) {
					datablocks.add(datablock);
				}
			}
			return datablocks.toArray(new DataBlockMetadata[datablocks.size()]);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public DataBlockMetadata getDataBlock(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			DataBlockMetadata datablock = null;
			DataBlockMetadataDTO datablockDTO = response.readEntity(DataBlockMetadataDTO.class);
			if (datablockDTO != null) {
				datablock = toDataBlock(datablockDTO);
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
		public boolean isDeleted(Response response) throws ClientException {
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
		 * 
		 * @param dto
		 * @return
		 */
		public FileContentMetadata toFileContentMetadata(FileContentMetadataDTO dto) {
			if (dto == null) {
				return null;
			}

			String fileId = dto.getFileId();
			int partId = dto.getPartId();
			// long size = dto.getSize();
			// int startIndex = dto.getStartIndex();
			// int endIndex = dto.getEndIndex();
			String checksum = dto.getChecksum();

			FileContentMetadataImpl metadata = new FileContentMetadataImpl();
			metadata.setFileId(fileId);
			metadata.setPartId(partId);
			// metadata.setSize(size);
			// metadata.setStartIndex(startIndex);
			// metadata.setEndIndex(endIndex);
			metadata.setChecksum(checksum);

			return metadata;
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public FileContentMetadata[] getFileContents(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			List<FileContentMetadata> fileContents = new ArrayList<FileContentMetadata>();
			List<FileContentMetadataDTO> fileContentDTOs = response.readEntity(new GenericType<List<FileContentMetadataDTO>>() {
			});
			for (FileContentMetadataDTO fileContentDTO : fileContentDTOs) {
				FileContentMetadata fileContent = toFileContentMetadata(fileContentDTO);
				if (fileContent != null) {
					fileContents.add(fileContent);
				}
			}
			return fileContents.toArray(new FileContentMetadata[fileContents.size()]);
		}

		/**
		 * 
		 * @param response
		 * @return
		 * @throws ClientException
		 */
		public FileContentMetadata getFileContent(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			FileContentMetadata fileContent = null;
			FileContentMetadataDTO fileContentDTO = response.readEntity(FileContentMetadataDTO.class);
			if (fileContentDTO != null) {
				fileContent = toFileContentMetadata(fileContentDTO);
			}
			return fileContent;
		}
	}

}
