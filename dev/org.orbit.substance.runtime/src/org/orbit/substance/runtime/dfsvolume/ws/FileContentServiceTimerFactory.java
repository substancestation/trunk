package org.orbit.substance.runtime.dfsvolume.ws;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.substance.runtime.dfsvolume.service.FileContentService;

public class FileContentServiceTimerFactory implements ServiceIndexTimerFactory<FileContentService> {

	@Override
	public FileContentServiceTimer create(IndexProvider indexProvider, FileContentService service) {
		return new FileContentServiceTimer(indexProvider, service);
	}

}
