package org.orbit.substance.connector.dfs;

import org.orbit.substance.api.dfs.FilePart;
import org.orbit.substance.api.dfs.VolumeAccess;

public class VolumeAccessImpl implements VolumeAccess {

	protected FilePart filePart;
	protected int id;
	protected String dfsId;
	protected String dfsVolumeId;
	protected String dataBlockId;

	public VolumeAccessImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param dfsId
	 * @param dfsVolumeId
	 * @param dataBlockId
	 */
	public VolumeAccessImpl(int id, String dfsId, String dfsVolumeId, String dataBlockId) {
		this.id = id;
		this.dfsId = dfsId;
		this.dfsVolumeId = dfsVolumeId;
		this.dataBlockId = dataBlockId;
	}

	@Override
	public FilePart getFilePart() {
		return this.filePart;
	}

	public void setFilePart(FilePart filePart) {
		this.filePart = filePart;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getDfsId() {
		return this.dfsId;
	}

	@Override
	public void setDfsId(String dfsId) {
		this.dfsId = dfsId;
	}

	@Override
	public String getDfsVolumeId() {
		return this.dfsVolumeId;
	}

	@Override
	public void setDfsVolumeId(String dfsVolumeId) {
		this.dfsVolumeId = dfsVolumeId;
	}

	@Override
	public String getDataBlockId() {
		return this.dataBlockId;
	}

	@Override
	public void setDataBlockId(String dataBlockId) {
		this.dataBlockId = dataBlockId;
	}

}
