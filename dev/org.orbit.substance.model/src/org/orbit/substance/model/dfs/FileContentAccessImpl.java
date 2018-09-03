package org.orbit.substance.model.dfs;

public class FileContentAccessImpl implements FileContentAccess {

	protected String dfsId;
	protected String dfsVolumeId;
	protected String dataBlockId;

	public FileContentAccessImpl() {
	}

	/**
	 * 
	 * @param dfsId
	 * @param dfsVolumeId
	 * @param dataBlockId
	 */
	public FileContentAccessImpl(String dfsId, String dfsVolumeId, String dataBlockId) {
		checkDfsId(dfsId);
		checkDfsVolumeId(dfsVolumeId);
		checkDataBlockId(dataBlockId);

		this.dfsId = dfsId;
		this.dfsVolumeId = dfsVolumeId;
		this.dataBlockId = dataBlockId;
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

	protected void checkDataBlockId(String dataBlockId) {
		if (dataBlockId == null || dataBlockId.isEmpty()) {
			throw new IllegalArgumentException("dataBlockId is null.");
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
	public String getDataBlockId() {
		return this.dataBlockId;
	}

	@Override
	public void setDataBlockId(String dataBlockId) {
		checkDataBlockId(dataBlockId);

		this.dataBlockId = dataBlockId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.dfsId == null) ? 0 : this.dfsId.hashCode());
		result = prime * result + ((this.dfsVolumeId == null) ? 0 : this.dfsVolumeId.hashCode());
		result = prime * result + ((this.dataBlockId == null) ? 0 : this.dataBlockId.hashCode());
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
		String otherDataBlockId = other.getDataBlockId();

		boolean matchDfsId = ((this.dfsId == null && otherDfsId == null) || (this.dfsId != null && this.dfsId.equals(otherDfsId))) ? true : false;
		boolean matchDfsVolumeId = ((this.dfsVolumeId == null && otherDfsVolumeId == null) || (this.dfsVolumeId != null && this.dfsVolumeId.equals(otherDfsVolumeId))) ? true : false;
		boolean matchDataBlockId = ((this.dataBlockId == null && otherDataBlockId == null) || (this.dataBlockId != null && this.dataBlockId.equals(otherDataBlockId))) ? true : false;

		if (matchDfsId && matchDfsVolumeId && matchDataBlockId) {
			return true;
		}
		return false;
	}

}

// FilePart getFilePart();
// void setFilePart(FilePart filePart);
// protected FilePart filePart;
// @Override
// public FilePart getFilePart() {
// return this.filePart;
// }
// public void setFilePart(FilePart filePart) {
// this.filePart = filePart;
// }
// int getId();
// void setId(int id);
// protected int id;
// /**
// *
// * @param id
// * @param dfsId
// * @param dfsVolumeId
// * @param dataBlockId
// */
// public FileContentAccessImpl(int id, String dfsId, String dfsVolumeId, String dataBlockId) {
// this.id = id;
// this.dfsId = dfsId;
// this.dfsVolumeId = dfsVolumeId;
// this.dataBlockId = dataBlockId;
// }
// @Override
// public int getId() {
// return this.id;
// }
//
// @Override
// public void setId(int id) {
// this.id = id;
// }
