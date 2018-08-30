package org.orbit.substance.connector.util;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.orbit.substance.api.dfs.File;
import org.orbit.substance.api.dfsvolume.DataBlockMetadata;
import org.orbit.substance.api.dfsvolume.FileContentMetadata;
import org.orbit.substance.connector.dfsvolume.DataBlockMetadataImpl;
import org.orbit.substance.connector.dfsvolume.FileContentMetadataImpl;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.model.dfsvolume.DataBlockMetadataDTO;
import org.orbit.substance.model.dfsvolume.FileContentMetadataDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class ModelConverter {

	public static FILE_SYSTEM FILE_SYSTEM = new FILE_SYSTEM();
	public static FILE_CONTENT FILE_CONTENT = new FILE_CONTENT();

	public static class FILE_SYSTEM {

		public File toFile(FileMetadataDTO fileMetadataDTO) {
			// FileImpl file = new FileImpl();
			return null;
		}

		public File[] getFiles(Response response) {
			return null;
		}

		public File getFile(Response response) {
			return null;
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

	public static class FILE_CONTENT {
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
