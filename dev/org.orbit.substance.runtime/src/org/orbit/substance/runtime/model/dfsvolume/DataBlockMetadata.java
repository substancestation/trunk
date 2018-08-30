package org.orbit.substance.runtime.model.dfsvolume;

public interface DataBlockMetadata {

	int getId();

	String getBlockId();

	String getAccountId();

	long getCapacity();

	long getSize();

	long getDateCreated();

	long getDateModified();

}
