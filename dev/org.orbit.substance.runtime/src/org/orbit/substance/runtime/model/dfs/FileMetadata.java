package org.orbit.substance.runtime.model.dfs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for File meta data.
 *
 */
@XmlRootElement
public class FileMetadata {

	public static final String FILEID = "fileId";
	public static final String PARENT_FILEID = "parentFileId";
	public static final String NAME = "name";
	public static final String IS_DIRECTORY = "isDirectory";
	public static final String IS_HIDDEN = "isHidden";
	public static final String PATH = "path";
	public static final String PARENT_PATH = "parentPath";
	public static final String EXISTS = "exists";
	public static final String CAN_EXECUTE = "canExecute";
	public static final String CAN_READ = "canRead";
	public static final String CAN_WRITE = "canWrite";
	public static final String LENGTH = "length";
	public static final String LAST_MODIFIED = "lastModified";

	@XmlElement
	protected int fileId;
	@XmlElement
	protected int parentFileId;
	@XmlElement
	protected String name;
	@XmlElement
	protected boolean isDirectory;
	@XmlElement
	protected boolean isHidden;
	@XmlElement
	protected String path;
	@XmlElement
	protected String parentPath;
	@XmlElement
	protected boolean exists;
	@XmlElement
	protected boolean canExecute;
	@XmlElement
	protected boolean canRead;
	@XmlElement
	protected boolean canWrite;
	@XmlElement
	protected long length;
	@XmlElement
	protected long lastModified;

	public FileMetadata() {
	}

	@XmlElement
	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	@XmlElement
	public int getParentFileId() {
		return parentFileId;
	}

	public void setParentFileId(int parentFileId) {
		this.parentFileId = parentFileId;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public boolean isDirectory() {
		return isDirectory;
	}

	public void setIsDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	@XmlElement
	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	@XmlElement
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlElement
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	@XmlElement
	public boolean exists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	@XmlElement
	public boolean canExecute() {
		return canExecute;
	}

	public void setCanExecute(boolean canExecute) {
		this.canExecute = canExecute;
	}

	@XmlElement
	public boolean canRead() {
		return canRead;
	}

	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}

	@XmlElement
	public boolean canWrite() {
		return canWrite;
	}

	public void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}

	@XmlElement
	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	@XmlElement
	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * 
	 * @param attrName
	 * @return
	 */
	public Object getAttribute(String attrName) {
		if (FILEID.equalsIgnoreCase(attrName)) {
			return getFileId();
		} else if (PARENT_FILEID.equalsIgnoreCase(attrName)) {
			return getParentFileId();
		} else if (NAME.equalsIgnoreCase(attrName)) {
			return getName();
		} else if (IS_DIRECTORY.equalsIgnoreCase(attrName)) {
			return isDirectory();
		} else if (IS_HIDDEN.equalsIgnoreCase(attrName)) {
			return isHidden();
		} else if (PATH.equalsIgnoreCase(attrName)) {
			return getPath();
		} else if (PARENT_PATH.equalsIgnoreCase(attrName)) {
			return getParentPath();
		} else if (EXISTS.equalsIgnoreCase(attrName)) {
			return exists();
		} else if (CAN_EXECUTE.equalsIgnoreCase(attrName)) {
			return canExecute();
		} else if (CAN_READ.equalsIgnoreCase(attrName)) {
			return canRead();
		} else if (CAN_WRITE.equalsIgnoreCase(attrName)) {
			return canWrite();
		} else if (LENGTH.equalsIgnoreCase(attrName)) {
			return getLength();
		} else if (LAST_MODIFIED.equalsIgnoreCase(attrName)) {
			return getLastModified();
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("FileMetadata (");
		sb.append("name='").append(this.name).append("'");
		sb.append(", isDirectory=").append(this.isDirectory);
		sb.append(", isHidden=").append(this.isHidden);
		sb.append(", path=").append(this.path).append("'");
		sb.append(", parentPath=").append(this.parentPath).append("'");
		sb.append(", exists=").append(this.exists);
		sb.append(", canExecute=").append(this.canExecute);
		sb.append(", canRead=").append(this.canRead);
		sb.append(", canWrite=").append(this.canWrite);
		sb.append(", length=").append(this.length);
		sb.append(", lastModified=").append(this.lastModified);
		sb.append(")");

		return sb.toString();
	}

}
