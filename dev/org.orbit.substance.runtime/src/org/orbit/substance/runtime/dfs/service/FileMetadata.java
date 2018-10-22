package org.orbit.substance.runtime.dfs.service;

import java.util.List;
import java.util.Map;

import org.orbit.substance.model.dfs.FilePart;
import org.origin.common.resource.Path;

public interface FileMetadata {

	public static final String ATTR__FILEID = "fileId";
	public static final String ATTR__PARENT_FILEID = "parentFileId";
	public static final String ATTR__NAME = "name";
	public static final String ATTR__EXISTS = "exists";
	public static final String ATTR__IS_DIRECTORY = "isDirectory";
	public static final String ATTR__SIZE = "size";
	public static final String ATTR__DATE_CREATED = "dateCreated";
	public static final String ATTR__DATE_MODIFIED = "dateModified";

	String getDfsId();

	void setDfsId(String dfsId);

	int getId();

	void setId(int id);

	String getAccountId();

	void setAccountId(String accountId);

	String getFileId();

	void setFileId(String fileId);

	String getParentFileId();

	void setParentFileId(String parentFileId);

	Path getPath();

	void setPath(Path path);

	String getName();

	void setName(String name);

	long getSize();

	void setSize(long size);

	boolean isDirectory();

	void setIsDirectory(boolean isDirectory);

	boolean isHidden();

	void setHidden(boolean isHidden);

	boolean isInTrash();

	void setInTrash(boolean inTrash);

	Map<String, Object> getProperties();

	List<FilePart> getFileParts();

	Object getAttribute(String attrName);

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
