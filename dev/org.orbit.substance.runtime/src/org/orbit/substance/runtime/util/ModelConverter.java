package org.orbit.substance.runtime.util;

import org.orbit.substance.model.dfs.DataBlockMetadataDTO;
import org.orbit.substance.model.dfs.FileContentMetadataDTO;
import org.orbit.substance.model.dfs.PathDTO;
import org.orbit.substance.runtime.model.dfs.DataBlockMetadata;
import org.orbit.substance.runtime.model.dfs.FileContentMetadata;
import org.orbit.substance.runtime.model.dfs.Path;

public class ModelConverter {

	public static File_System File_System = new File_System();
	public static File_Content File_Content = new File_Content();

	public static class File_System {
		/**
		 * 
		 * @param path
		 * @return
		 */
		public PathDTO toDTO(Path path) {
			String pathString = path.getPathString();

			PathDTO dto = new PathDTO();
			dto.setPathString(pathString);
			return dto;
		}
	}

	public static class File_Content {

		/**
		 * 
		 * @param dataBlock
		 * @return
		 */
		public DataBlockMetadataDTO toDTO(DataBlockMetadata dataBlock) {
			if (dataBlock == null) {
				return null;
			}

			String blockId = dataBlock.getBlockId();
			String accountId = dataBlock.getAccountId();
			long capacity = dataBlock.getCapacity();
			long size = dataBlock.getSize();
			// String[] fileIds = dataBlock.getFileIds();

			DataBlockMetadataDTO dto = new DataBlockMetadataDTO();
			dto.setBlockId(blockId);
			dto.setAccountId(accountId);
			dto.setCapacity(capacity);
			dto.setSize(size);
			// dto.setFileIds(fileIds);

			return dto;
		}

		public FileContentMetadataDTO toDTO(FileContentMetadata fileContent) {
			if (fileContent == null) {
				return null;
			}

			String fileId = fileContent.getFileId();
			long size = fileContent.getSize();
			int partId = fileContent.getPartId();
			int startIndex = fileContent.getStartIndex();
			int endIndex = fileContent.getEndIndex();
			String checksum = fileContent.getChecksum();

			FileContentMetadataDTO dto = new FileContentMetadataDTO();
			dto.setFileId(fileId);
			dto.setSize(size);
			dto.setPartId(partId);
			dto.setStartIndex(startIndex);
			dto.setEndIndex(endIndex);
			dto.setChecksum(checksum);

			return dto;
		}
	}

}
