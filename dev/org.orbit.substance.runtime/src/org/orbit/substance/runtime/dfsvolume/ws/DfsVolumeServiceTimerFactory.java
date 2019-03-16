package org.orbit.substance.runtime.dfsvolume.ws;

import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;

public class DfsVolumeServiceTimerFactory implements ServiceIndexTimerFactory<DfsVolumeService> {

	@Override
	public DfsVolumeServiceTimer create(DfsVolumeService service) {
		return new DfsVolumeServiceTimer(service);
	}

}
