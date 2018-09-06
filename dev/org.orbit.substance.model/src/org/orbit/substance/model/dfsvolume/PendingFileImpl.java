package org.orbit.substance.model.dfsvolume;

import java.util.Date;

public class PendingFileImpl implements PendingFile {

	protected String fileId;
	protected long size;
	protected long dateCreated;

	public PendingFileImpl() {
	}

	public PendingFileImpl(String fileId, long size) {
		this.fileId = fileId;
		this.size = size;
		this.dateCreated = new Date().getTime();
	}

	@Override
	public String getFileId() {
		return this.fileId;
	}

	@Override
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Override
	public long getSize() {
		return this.size;
	}

	@Override
	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

}
