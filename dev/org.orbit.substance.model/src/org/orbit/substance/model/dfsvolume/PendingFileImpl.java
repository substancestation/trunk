package org.orbit.substance.model.dfsvolume;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.origin.common.util.DateUtil;

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

	@Override
	public boolean isExpired() {
		long dateCreated = getDateCreated();
		boolean isExpired = false;
		Date expireTime = DateUtil.addTimeToDate(new Date(dateCreated), 120, TimeUnit.SECONDS);
		Date timeNow = new Date();
		if (timeNow.after(expireTime)) {
			isExpired = true;
		}
		return isExpired;
	}

}
