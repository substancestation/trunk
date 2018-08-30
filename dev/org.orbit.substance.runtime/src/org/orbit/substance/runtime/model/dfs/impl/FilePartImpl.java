package org.orbit.substance.runtime.model.dfs.impl;

import java.util.ArrayList;
import java.util.List;

import org.orbit.substance.runtime.model.dfs.FileContentAccess;
import org.orbit.substance.runtime.model.dfs.FileMetadata;
import org.orbit.substance.runtime.model.dfs.FilePart;

public class FilePartImpl implements FilePart {

	protected FileMetadata fileMetadata;
	protected int partId;
	protected int startIndex;
	protected int endIndex;
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
	public FilePartImpl(int partId, int startIndex, int endIndex, String checksum) {
		this.partId = partId;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.checksum = checksum;
	}

	@Override
	public FileMetadata getFileMetadata() {
		return this.fileMetadata;
	}

	@Override
	public void setFileMetadata(FileMetadata fileMetadata) {
		this.fileMetadata = fileMetadata;
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
	public int getStartIndex() {
		return this.startIndex;
	}

	@Override
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	@Override
	public int getEndIndex() {
		return this.endIndex;
	}

	@Override
	public void setEndIndex(int endIndex) {
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
	public synchronized List<FileContentAccess> getFileContentAccess() {
		if (this.fileContentAccessList == null) {
			this.fileContentAccessList = new ArrayList<FileContentAccess>();
		}
		return this.fileContentAccessList;
	}

	public synchronized void setFileContentAccess(List<FileContentAccess> fileContentAccessList) {
		this.fileContentAccessList = fileContentAccessList;
	}

	@Override
	public String toString() {
		return "FilePartMetadataImpl [partId=" + this.partId + ", startIndex=" + this.startIndex + ", endIndex=" + this.endIndex + ", checksum=" + this.checksum + "]";
	}

}
