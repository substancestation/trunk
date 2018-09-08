package org.orbit.substance.model.dfs;

public class FileContentAccessImpl implements FileContentAccess {

	protected String dfsId;
	protected String dfsVolumeId;
	protected String blockId;

	public FileContentAccessImpl() {
	}

	/**
	 * 
	 * @param dfsId
	 * @param dfsVolumeId
	 * @param blockId
	 */
	public FileContentAccessImpl(String dfsId, String dfsVolumeId, String blockId) {
		checkDfsId(dfsId);
		checkDfsVolumeId(dfsVolumeId);
		checkBlockId(blockId);

		this.dfsId = dfsId;
		this.dfsVolumeId = dfsVolumeId;
		this.blockId = blockId;
	}

	protected void checkDfsId(String dfsId) {
		if (dfsId == null || dfsId.isEmpty()) {
			throw new IllegalArgumentException("dfsId is null.");
		}
	}

	protected void checkDfsVolumeId(String dfsVolumeId) {
		if (dfsVolumeId == null || dfsVolumeId.isEmpty()) {
			throw new IllegalArgumentException("dfsVolumeId is null.");
		}
	}

	protected void checkBlockId(String blockId) {
		if (blockId == null || blockId.isEmpty()) {
			throw new IllegalArgumentException("blockId is null.");
		}
	}

	@Override
	public String getDfsId() {
		return this.dfsId;
	}

	@Override
	public void setDfsId(String dfsId) {
		checkDfsId(dfsId);

		this.dfsId = dfsId;
	}

	@Override
	public String getDfsVolumeId() {
		return this.dfsVolumeId;
	}

	@Override
	public void setDfsVolumeId(String dfsVolumeId) {
		checkDfsVolumeId(dfsVolumeId);

		this.dfsVolumeId = dfsVolumeId;
	}

	@Override
	public String getBlockId() {
		return this.blockId;
	}

	@Override
	public void setBlockId(String blockId) {
		checkBlockId(blockId);

		this.blockId = blockId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.dfsId == null) ? 0 : this.dfsId.hashCode());
		result = prime * result + ((this.dfsVolumeId == null) ? 0 : this.dfsVolumeId.hashCode());
		result = prime * result + ((this.blockId == null) ? 0 : this.blockId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof FileContentAccess)) {
			return false;
		}
		FileContentAccess other = (FileContentAccess) obj;

		String otherDfsId = other.getDfsId();
		String otherDfsVolumeId = other.getDfsVolumeId();
		String otherBlockId = other.getBlockId();

		boolean matchDfsId = ((this.dfsId == null && otherDfsId == null) || (this.dfsId != null && this.dfsId.equals(otherDfsId))) ? true : false;
		boolean matchDfsVolumeId = ((this.dfsVolumeId == null && otherDfsVolumeId == null) || (this.dfsVolumeId != null && this.dfsVolumeId.equals(otherDfsVolumeId))) ? true : false;
		boolean matchBlockId = ((this.blockId == null && otherBlockId == null) || (this.blockId != null && this.blockId.equals(otherBlockId))) ? true : false;

		if (matchDfsId && matchDfsVolumeId && matchBlockId) {
			return true;
		}
		return false;
	}

}
