package org.orbit.substance.runtime.model.dfs;

public class FileContentMetadataImpl implements FileContentMetadata {

	protected int id;
	protected String fileId;
	protected long size;
	protected int partId;
	protected int startIndex;
	protected int endIndex;
	protected String checksum;

	public FileContentMetadataImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param fileId
	 * @param size
	 * @param partId
	 * @param startIndex
	 * @param endIndex
	 * @param checksum
	 */
	public FileContentMetadataImpl(int id, String fileId, long size, int partId, int startIndex, int endIndex, String checksum) {
		this.id = id;
		this.fileId = fileId;
		this.size = size;
		this.partId = partId;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.checksum = checksum;
	}

	@Override
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Override
	public long getSize() {
		return this.size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public int getPartId() {
		return this.partId;
	}

	public void setPartId(int partId) {
		this.partId = partId;
	}

	@Override
	public int getStartIndex() {
		return this.startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	@Override
	public int getEndIndex() {
		return this.endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	@Override
	public String getChecksum() {
		return this.checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

}
