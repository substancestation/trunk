package org.orbit.substance.api.dfs;

import org.origin.common.rest.model.ServiceMetadata;

public interface DfsServiceMetadata extends ServiceMetadata {

	String getDfsId();

	long getDataBlockCapacity();

}
