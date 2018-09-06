package org.orbit.substance.model.util;

import static org.orbit.substance.model.util.FilePartsWriter.CONTENT_ACCESS__BLOCK_ID;
import static org.orbit.substance.model.util.FilePartsWriter.CONTENT_ACCESS__DFS_ID;
import static org.orbit.substance.model.util.FilePartsWriter.CONTENT_ACCESS__DFS_VOLUME_ID;
import static org.orbit.substance.model.util.FilePartsWriter.FILE_PART__CHECKSUM;
import static org.orbit.substance.model.util.FilePartsWriter.FILE_PART__CONTENT_ACCESS;
import static org.orbit.substance.model.util.FilePartsWriter.FILE_PART__END_INDEX;
import static org.orbit.substance.model.util.FilePartsWriter.FILE_PART__ID;
import static org.orbit.substance.model.util.FilePartsWriter.FILE_PART__START_INDEX;
import static org.orbit.substance.model.util.FilePartsWriter.ROOT__PARTS;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.substance.model.dfs.FileContentAccess;
import org.orbit.substance.model.dfs.FileContentAccessImpl;
import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.model.dfs.FilePartImpl;

public class FilePartsReader {

	/**
	 * 
	 * @param string
	 * @return
	 */
	public List<FilePart> read(String string) {
		List<FilePart> fileParts = null;
		if (string != null && !string.isEmpty()) {
			JSONObject rootJSONObject = new JSONObject(string);
			fileParts = jsonToContents(rootJSONObject);
		}
		if (fileParts == null) {
			fileParts = new ArrayList<FilePart>();
		}
		return fileParts;
	}

	/**
	 * 
	 * @param rootJSONObject
	 * @return
	 */
	protected List<FilePart> jsonToContents(JSONObject rootJSONObject) {
		// "parts" JSON array
		List<FilePart> fileParts = new ArrayList<FilePart>();
		if (rootJSONObject.has(ROOT__PARTS)) {
			JSONArray filePartsJSONArray = rootJSONObject.getJSONArray(ROOT__PARTS);
			if (filePartsJSONArray != null) {
				int length = filePartsJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject filePartJSONObject = filePartsJSONArray.getJSONObject(i);
					if (filePartJSONObject != null) {
						FilePart filePart = jsonToFilePart(filePartJSONObject);
						if (filePart != null) {
							fileParts.add(filePart);
						}
					}
				}
			}
		}
		return fileParts;
	}

	/**
	 * 
	 * @param filePartJSONObject
	 * @return
	 */
	protected FilePart jsonToFilePart(JSONObject filePartJSONObject) {
		if (filePartJSONObject == null) {
			return null;
		}
		FilePartImpl filePart = new FilePartImpl();

		// "id" attribute
		int partId = 0;
		if (filePartJSONObject.has(FILE_PART__ID)) {
			partId = filePartJSONObject.getInt(FILE_PART__ID);
		}
		filePart.setPartId(partId);

		// "startIndex" attribute
		long startIndex = 0;
		if (filePartJSONObject.has(FILE_PART__START_INDEX)) {
			startIndex = filePartJSONObject.getLong(FILE_PART__START_INDEX);
		}
		filePart.setStartIndex(startIndex);

		// "endIndex" attribute
		long endIndex = -1;
		if (filePartJSONObject.has(FILE_PART__END_INDEX)) {
			endIndex = filePartJSONObject.getLong(FILE_PART__END_INDEX);
		}
		filePart.setEndIndex(endIndex);

		// "checksum" attribute
		long checksum = -1;
		if (filePartJSONObject.has(FILE_PART__CHECKSUM)) {
			checksum = filePartJSONObject.getLong(FILE_PART__CHECKSUM);
		}
		filePart.setChecksum(checksum);

		// "contentAccess" JSON array
		List<FileContentAccess> contentAccessList = new ArrayList<FileContentAccess>();
		if (filePartJSONObject.has(FILE_PART__CONTENT_ACCESS)) {
			JSONArray contentAccessJSONArray = filePartJSONObject.getJSONArray(FILE_PART__CONTENT_ACCESS);
			if (contentAccessJSONArray != null) {
				int length = contentAccessJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject contentAccessJSONObject = contentAccessJSONArray.getJSONObject(i);
					if (contentAccessJSONObject != null) {
						FileContentAccess contentAccess = jsonToContentAccess(contentAccessJSONObject);
						if (contentAccess != null) {
							contentAccessList.add(contentAccess);
						}
					}
				}
			}
		}
		filePart.setContentAccess(contentAccessList);

		return filePart;
	}

	/**
	 * 
	 * @param contentAccessJSONObject
	 * @return
	 */
	protected FileContentAccess jsonToContentAccess(JSONObject contentAccessJSONObject) {
		if (contentAccessJSONObject == null) {
			return null;
		}

		FileContentAccessImpl contentAccess = new FileContentAccessImpl();

		// "dfsId" attribute
		String dfsId = null;
		if (contentAccessJSONObject.has(CONTENT_ACCESS__DFS_ID)) {
			dfsId = contentAccessJSONObject.getString(CONTENT_ACCESS__DFS_ID);
		}
		contentAccess.setDfsId(dfsId);

		// "dfsVolumeId" attribute
		String dfsVolumeId = null;
		if (contentAccessJSONObject.has(CONTENT_ACCESS__DFS_VOLUME_ID)) {
			dfsVolumeId = contentAccessJSONObject.getString(CONTENT_ACCESS__DFS_VOLUME_ID);
		}
		contentAccess.setDfsVolumeId(dfsVolumeId);

		// "blockId" attribute
		String blockId = null;
		if (contentAccessJSONObject.has(CONTENT_ACCESS__BLOCK_ID)) {
			blockId = contentAccessJSONObject.getString(CONTENT_ACCESS__BLOCK_ID);
		}
		contentAccess.setDataBlockId(blockId);

		return contentAccess;
	}

}
