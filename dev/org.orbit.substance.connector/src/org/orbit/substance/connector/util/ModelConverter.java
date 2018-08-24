package org.orbit.substance.connector.util;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.orbit.substance.api.dfs.filecontent.DataBlockMetadata;
import org.orbit.substance.api.dfs.filecontent.FileContentMetadata;
import org.orbit.substance.api.dfs.filesystem.File;
import org.orbit.substance.connector.dfs.filecontent.DataBlockMetadataImpl;
import org.orbit.substance.connector.dfs.filecontent.FileContentMetadataImpl;
import org.orbit.substance.connector.dfs.filesystem.FileImpl;
import org.orbit.substance.model.dfs.DataBlockMetadataDTO;
import org.orbit.substance.model.dfs.FileContentMetadataDTO;
import org.orbit.substance.model.dfs.FileDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class ModelConverter {

	public static FILE_SYSTEM FILE_SYSTEM = new FILE_SYSTEM();
	public static FILE_CONTENT FILE_CONTENT = new FILE_CONTENT();

	public static class FILE_SYSTEM {

		public File toFile(FileDTO nodeDTO) {
			FileImpl file = new FileImpl();
			return file;
		}

		public File[] getFiles(Response response) {
			return null;
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
			long size = dto.getSize();
			int partId = dto.getPartId();
			int startIndex = dto.getStartIndex();
			int endIndex = dto.getEndIndex();
			String checksum = dto.getChecksum();

			FileContentMetadataImpl metadata = new FileContentMetadataImpl();
			metadata.setFileId(fileId);
			metadata.setSize(size);
			metadata.setPartId(partId);
			metadata.setStartIndex(startIndex);
			metadata.setEndIndex(endIndex);
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
