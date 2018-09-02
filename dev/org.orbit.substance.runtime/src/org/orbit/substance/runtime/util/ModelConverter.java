package org.orbit.substance.runtime.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.orbit.substance.model.dfs.PathDTO;
import org.orbit.substance.model.dfsvolume.DataBlockMetadataDTO;
import org.orbit.substance.model.dfsvolume.FileContentMetadataDTO;
import org.orbit.substance.runtime.model.dfs.FileMetadata;
import org.orbit.substance.runtime.model.dfs.FilePart;
import org.orbit.substance.runtime.model.dfs.Path;
import org.orbit.substance.runtime.model.dfsvolume.DataBlockMetadata;
import org.orbit.substance.runtime.model.dfsvolume.FileContentMetadata;
import org.origin.common.json.JSONUtil;

public class ModelConverter {

	public static Dfs Dfs = new Dfs();
	public static DfsVolume DfsVolume = new DfsVolume();

	public static class Dfs {
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
		 * TODO: Convert list of FilePart objects to json string
		 * 
		 * @param fileParts
		 * @return
		 */
		public String toFilePartsString(List<FilePart> fileParts) {
			String filePartsString = "";
			if (fileParts != null) {

			}
			return filePartsString;
		}

		/**
		 * TODO: Convert json string to list of FilePart objects
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
		 * @param fileMetadata
		 * @return
		 */
		public FileMetadataDTO toDTO(FileMetadata fileMetadata) {
			if (fileMetadata == null) {
				return null;
			}

			// String name = fileMetadata.getName();
			// dto.setName(name);

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

			FileMetadataDTO dto = new FileMetadataDTO();
			dto.setAccountId(accountId);
			dto.setFileId(fileId);
			dto.setParentFileId(parentFileId);
			dto.setPath(path.getPathString());
			dto.setSize(size);
			dto.setDirectory(isDirectory);
			dto.setHidden(isHidden);
			dto.setInTrash(inTrash);
			dto.setFilePartsString(filePartsString);
			dto.setPropertiesString(propertiesString);
			dto.setDateCreated(dateCreated);
			dto.setDateModified(dateModified);

			return dto;
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
			int partId = fileContent.getPartId();
			// long size = fileContent.getSize();
			// int startIndex = fileContent.getStartIndex();
			// int endIndex = fileContent.getEndIndex();
			String checksum = fileContent.getChecksum();

			FileContentMetadataDTO dto = new FileContentMetadataDTO();
			dto.setFileId(fileId);
			dto.setPartId(partId);
			// dto.setSize(size);
			// dto.setStartIndex(startIndex);
			// dto.setEndIndex(endIndex);
			dto.setChecksum(checksum);

			return dto;
		}
	}

}
