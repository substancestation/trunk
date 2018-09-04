package org.orbit.substance.model.dfs;

import java.util.ArrayList;
import java.util.List;

public class FilePartImpl implements FilePart {

	protected int partId;
	protected long startIndex;
	protected long endIndex;
	protected String checksum;
	protected List<FileContentAccess> fileContentAccessList = new ArrayList<FileContentAccess>();

	public FilePartImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param fileId
	 * @param partId
	 * @param startIndex
	 * @param endIndex
	 * @param checksum
	 */
	public FilePartImpl(int partId, long startIndex, long endIndex, String checksum) {
		this.partId = partId;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.checksum = checksum;
	}

	@Override
	public int getPartId() {
		return this.partId;
	}

	@Override
	public void setPartId(int partId) {
		this.partId = partId;
	}

	@Override
	public long getStartIndex() {
		return this.startIndex;
	}

	@Override
	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}

	@Override
	public long getEndIndex() {
		return this.endIndex;
	}

	@Override
	public void setEndIndex(long endIndex) {
		this.endIndex = endIndex;
	}

	@Override
	public String getChecksum() {
		return this.checksum;
	}

	@Override
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Override
	public synchronized List<FileContentAccess> getContentAccess() {
		if (this.fileContentAccessList == null) {
			this.fileContentAccessList = new ArrayList<FileContentAccess>();
		}
		return this.fileContentAccessList;
	}

	@Override
	public String toString() {
		return "FilePartImpl [partId=" + this.partId + ", startIndex=" + this.startIndex + ", endIndex=" + this.endIndex + ", checksum=" + this.checksum + "]";
	}

}

// FileMetadata getFileMetadata();
// void setFileMetadata(FileMetadata fileMetadata);
// protected FileMetadata fileMetadata;
// @Override
// public FileMetadata getFileMetadata() {
// return this.fileMetadata;
// }
// @Override
// public void setFileMetadata(FileMetadata fileMetadata) {
// this.fileMetadata = fileMetadata;
// }
// public synchronized void setContentAccess(List<FileContentAccess> fileContentAccessList) {
// this.fileContentAccessList = fileContentAccessList;
// }
