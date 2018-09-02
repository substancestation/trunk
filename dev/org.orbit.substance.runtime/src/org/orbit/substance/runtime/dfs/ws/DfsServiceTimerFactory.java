package org.orbit.substance.runtime.dfs.ws;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.substance.runtime.dfs.service.DfsService;

public class DfsServiceTimerFactory implements ServiceIndexTimerFactory<DfsService> {

	@Override
	public DfsServiceTimer create(IndexProvider indexProvider, DfsService service) {
		return new DfsServiceTimer(indexProvider, service);
	}

}
