package org.orbit.substance.model.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.substance.model.dfsvolume.PendingFile;

public class PendingFilesWriter {

	public static final String ROOT__PENDING_FILES = "pendingFiles";

	public static final String PENDING_FILE__FILE_ID = "fileId";
	public static final String PENDING_FILE__SIZE = "size";
	public static final String PENDING_FILE__DATE_CREATED = "dateCreated";

	/**
	 * 
	 * @param pendingFiles
	 * @return
	 */
	public String write(List<PendingFile> pendingFiles) {
		String string = null;
		if (pendingFiles != null && !pendingFiles.isEmpty()) {
			JSONObject rootJSONObject = contentsToJSON(pendingFiles);
			string = rootJSONObject.toString();
		}
		if (string == null) {
			string = "";
		}
		return string;
	}

	/**
	 * 
	 * @param pendingFiles
	 * @return
	 */
	protected JSONObject contentsToJSON(List<PendingFile> pendingFiles) {
		JSONObject rootJSONObject = new JSONObject();

		// "pendingFiles" JSON array
		JSONArray partsJSONArray = new JSONArray();
		int index = 0;
		for (PendingFile pendingFile : pendingFiles) {
			JSONObject pendingFileJSONObject = pendingFileToJSON(pendingFile);
			if (pendingFileJSONObject != null) {
				partsJSONArray.put(index++, pendingFileJSONObject);
			}
		}
		rootJSONObject.put(ROOT__PENDING_FILES, partsJSONArray);

		return rootJSONObject;
	}

	/**
	 * 
	 * @param pendingFile
	 * @return
	 */
	protected JSONObject pendingFileToJSON(PendingFile pendingFile) {
		if (pendingFile == null) {
			return null;
		}

		JSONObject pendingFileJSONObject = new JSONObject();

		// "fileId" attribute
		String fileId = pendingFile.getFileId();
		if (fileId != null) {
			pendingFileJSONObject.put(PENDING_FILE__FILE_ID, fileId);
		}

		// "size" attribute
		long size = pendingFile.getSize();
		pendingFileJSONObject.put(PENDING_FILE__SIZE, size);

		// "dateCreated" attribute
		long dateCreated = pendingFile.getDateCreated();
		pendingFileJSONObject.put(PENDING_FILE__DATE_CREATED, dateCreated);

		return pendingFileJSONObject;
	}

}
