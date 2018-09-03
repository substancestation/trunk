package org.orbit.substance.runtime.dfsvolume.service;

public interface DataBlockMetadata {

	String getDfsVolumeId();

	int getId();

	String getBlockId();

	String getAccountId();

	long getCapacity();

	long getSize();

	long getDateCreated();

	long getDateModified();

}
