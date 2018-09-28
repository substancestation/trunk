package org.orbit.substance.api.dfsvolume;

import org.origin.common.rest.model.ServiceMetadata;

public interface DfsVolumeMetadata extends ServiceMetadata {

	String getDfsId();

	String getDfsVolumeId();

	long getVolumeCapacity();

	long getVolumeSize();

}

// long getDataBlockCapacity();
