package org.orbit.substance.api.dfsvolume;

public interface DataBlockMetadata {

	String getDfsVolumeId();

	String getBlockId();

	String getAccountId();

	long getCapacity();

	long getSize();

}
