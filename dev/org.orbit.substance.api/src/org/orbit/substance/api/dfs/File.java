package org.orbit.substance.api.dfs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface File {

	FileSystemClient getFileSystemClient();

	String getFileId();

	String getParentFileId();

	Path getPath();

	String getName();

	boolean isDirectory();

	boolean isHidden();

	boolean inTrash();

	long getSize();

	List<FilePart> getFileParts();

	Map<String, Object> getProperties();

	long getDateCreated();

	long getDateModified();

	// ------------------------------------------------------
	// Actions
	// ------------------------------------------------------
	boolean createNewFile() throws IOException;

	boolean mkdirs() throws IOException;

	boolean moveToTrash() throws IOException;

	boolean putBackFromTrash() throws IOException;

	boolean delete() throws IOException;

}
