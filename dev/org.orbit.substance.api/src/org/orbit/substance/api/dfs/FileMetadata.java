package org.orbit.substance.api.dfs;

import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfs.FilePart;
import org.orbit.substance.model.dfs.Path;

public interface FileMetadata {

	DfsClient getDfsClient();

	String getDfsId();

	String getAccountId();

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

}
