package org.orbit.substance.connector.dfs;

import java.util.ArrayList;
import java.util.List;

import org.orbit.substance.api.dfs.FileMetadata;
import org.orbit.substance.api.dfs.FilePart;
import org.orbit.substance.api.dfs.VolumeAccess;

public class FilePartImpl implements FilePart {

	protected FileMetadata file;
	protected int partId;
	protected long startIndex;
	protected long endIndex;
	protected String checksum;
	protected List<VolumeAccess> volumeAccessList = new ArrayList<VolumeAccess>();

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
	public FileMetadata getFile() {
		return this.file;
	}

	@Override
	public void setFile(FileMetadata file) {
		this.file = file;
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
	public synchronized List<VolumeAccess> getVolumeAccess() {
		if (this.volumeAccessList == null) {
			this.volumeAccessList = new ArrayList<VolumeAccess>();
		}
		return this.volumeAccessList;
	}

	public synchronized void setFileContentAccess(List<VolumeAccess> fileContentAccessList) {
		this.volumeAccessList = fileContentAccessList;
	}

	@Override
	public String toString() {
		return "FilePartMetadataImpl [partId=" + this.partId + ", startIndex=" + this.startIndex + ", endIndex=" + this.endIndex + ", checksum=" + this.checksum + "]";
	}

}
