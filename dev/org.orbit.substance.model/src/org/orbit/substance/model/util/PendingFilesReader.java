package org.orbit.substance.model.util;

import static org.orbit.substance.model.util.PendingFilesWriter.PENDING_FILE__DATE_CREATED;
import static org.orbit.substance.model.util.PendingFilesWriter.PENDING_FILE__FILE_ID;
import static org.orbit.substance.model.util.PendingFilesWriter.PENDING_FILE__SIZE;
import static org.orbit.substance.model.util.PendingFilesWriter.ROOT__PENDING_FILES;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.substance.model.dfsvolume.PendingFile;
import org.orbit.substance.model.dfsvolume.PendingFileImpl;

public class PendingFilesReader {

	/**
	 * 
	 * @param string
	 * @return
	 */
	public List<PendingFile> read(String string) {
		List<PendingFile> pendingFiles = null;
		if (string != null && !string.isEmpty()) {
			JSONObject rootJSONObject = new JSONObject(string);
			pendingFiles = jsonToContents(rootJSONObject);
		}
		if (pendingFiles == null) {
			pendingFiles = new ArrayList<PendingFile>();
		}
		return pendingFiles;
	}

	/**
	 * 
	 * @param rootJSONObject
	 * @return
	 */
	protected List<PendingFile> jsonToContents(JSONObject rootJSONObject) {
		// "pendingFiles" JSON array
		List<PendingFile> pendingFiles = new ArrayList<PendingFile>();
		if (rootJSONObject.has(ROOT__PENDING_FILES)) {
			JSONArray pendingFilesJSONArray = rootJSONObject.getJSONArray(ROOT__PENDING_FILES);
			if (pendingFilesJSONArray != null) {
				int length = pendingFilesJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject pendingFileJSONObject = pendingFilesJSONArray.getJSONObject(i);
					if (pendingFileJSONObject != null) {
						PendingFile pendingFile = jsonToPendingFile(pendingFileJSONObject);
						if (pendingFile != null) {
							pendingFiles.add(pendingFile);
						}
					}
				}
			}
		}
		return pendingFiles;
	}

	/**
	 * 
	 * @param pendingFileJSONObject
	 * @return
	 */
	protected PendingFile jsonToPendingFile(JSONObject pendingFileJSONObject) {
		if (pendingFileJSONObject == null) {
			return null;
		}
		PendingFileImpl pendingFile = new PendingFileImpl();

		// "fileId" attribute
		String fileId = null;
		if (pendingFileJSONObject.has(PENDING_FILE__FILE_ID)) {
			fileId = pendingFileJSONObject.getString(PENDING_FILE__FILE_ID);
		}
		pendingFile.setFileId(fileId);

		// "size" attribute
		long size = 0;
		if (pendingFileJSONObject.has(PENDING_FILE__SIZE)) {
			size = pendingFileJSONObject.getLong(PENDING_FILE__SIZE);
		}
		pendingFile.setSize(size);

		// "dateCreated" attribute
		long dateCreated = -1;
		if (pendingFileJSONObject.has(PENDING_FILE__DATE_CREATED)) {
			dateCreated = pendingFileJSONObject.getLong(PENDING_FILE__DATE_CREATED);
		}
		pendingFile.setDateCreated(dateCreated);

		return pendingFile;
	}

}
