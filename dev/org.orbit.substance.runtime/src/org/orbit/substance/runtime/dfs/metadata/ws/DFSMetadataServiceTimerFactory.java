package org.orbit.substance.runtime.dfs.metadata.ws;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.substance.runtime.dfs.metadata.service.DFSMetadataService;

public class DFSMetadataServiceTimerFactory implements ServiceIndexTimerFactory<DFSMetadataService> {

	@Override
	public DFSMetadataServiceTimer create(IndexProvider indexProvider, DFSMetadataService service) {
		return new DFSMetadataServiceTimer(indexProvider, service);
	}

}
