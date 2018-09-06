package org.orbit.substance.model.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.substance.model.dfs.FileContentAccess;
import org.orbit.substance.model.dfs.FilePart;

public class FilePartsWriter {

	public static final String ROOT__PARTS = "parts";

	public static final String FILE_PART__ID = "id";
	public static final String FILE_PART__START_INDEX = "startIndex";
	public static final String FILE_PART__END_INDEX = "endIndex";
	public static final String FILE_PART__CHECKSUM = "checksum";
	public static final String FILE_PART__CONTENT_ACCESS = "contentAccess";

	public static final String CONTENT_ACCESS__DFS_ID = "dfsId";
	public static final String CONTENT_ACCESS__DFS_VOLUME_ID = "dfsVolumeId";
	public static final String CONTENT_ACCESS__BLOCK_ID = "blockId";

	/**
	 * 
	 * @param fileParts
	 * @return
	 */
	public String write(List<FilePart> fileParts) {
		String string = null;
		if (fileParts != null && !fileParts.isEmpty()) {
			JSONObject rootJSONObject = contentsToJSON(fileParts);
			string = rootJSONObject.toString();
		}
		if (string == null) {
			string = "";
		}
		return string;
	}

	/**
	 * 
	 * @param fileParts
	 * @return
	 */
	protected JSONObject contentsToJSON(List<FilePart> fileParts) {
		JSONObject rootJSONObject = new JSONObject();

		// "parts" JSON array
		JSONArray partsJSONArray = new JSONArray();
		int index = 0;
		for (FilePart filePart : fileParts) {
			JSONObject filePartJSONObject = filePartToJSON(filePart);
			if (filePartJSONObject != null) {
				partsJSONArray.put(index++, filePartJSONObject);
			}
		}
		rootJSONObject.put(ROOT__PARTS, partsJSONArray);

		return rootJSONObject;
	}

	/**
	 * 
	 * @param filePart
	 * @return
	 */
	protected JSONObject filePartToJSON(FilePart filePart) {
		if (filePart == null) {
			return null;
		}

		JSONObject filePartJSONObject = new JSONObject();

		// "id" attribute
		int partId = filePart.getPartId();
		filePartJSONObject.put(FILE_PART__ID, partId);

		// "startIndex" attribute
		long startIndex = filePart.getStartIndex();
		filePartJSONObject.put(FILE_PART__START_INDEX, startIndex);

		// "endIndex" attribute
		long endIndex = filePart.getEndIndex();
		filePartJSONObject.put(FILE_PART__END_INDEX, endIndex);

		// "checksum" attribute
		long checksum = filePart.getChecksum();
		filePartJSONObject.put(FILE_PART__CHECKSUM, checksum);

		// "contentAccess" JSON array
		List<FileContentAccess> contentAccessList = filePart.getContentAccess();
		JSONArray contentAccessJSONArray = new JSONArray();
		int index = 0;
		for (FileContentAccess contentAccess : contentAccessList) {
			JSONObject contentAccessJSONObject = contentAccessToJSON(contentAccess);
			if (contentAccessJSONObject != null) {
				contentAccessJSONArray.put(index++, contentAccessJSONObject);
			}
		}
		filePartJSONObject.put(FILE_PART__CONTENT_ACCESS, contentAccessJSONArray);

		return filePartJSONObject;
	}

	/**
	 * 
	 * @param contentAccess
	 * @return
	 */
	protected JSONObject contentAccessToJSON(FileContentAccess contentAccess) {
		if (contentAccess == null) {
			return null;
		}

		JSONObject contentAccessJSONObject = new JSONObject();

		// "dfsId" attribute
		String dfsId = contentAccess.getDfsId();
		if (dfsId != null && !dfsId.isEmpty()) {
			contentAccessJSONObject.put(CONTENT_ACCESS__DFS_ID, dfsId);
		}

		// "dfsVolumeId" attribute
		String dfsVolumeId = contentAccess.getDfsVolumeId();
		if (dfsVolumeId != null && !dfsVolumeId.isEmpty()) {
			contentAccessJSONObject.put(CONTENT_ACCESS__DFS_VOLUME_ID, dfsVolumeId);
		}

		// "blockId" attribute
		String blockId = contentAccess.getDataBlockId();
		if (blockId != null && !blockId.isEmpty()) {
			contentAccessJSONObject.put(CONTENT_ACCESS__BLOCK_ID, blockId);
		}

		return contentAccessJSONObject;
	}

}
