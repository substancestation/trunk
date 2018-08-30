package org.orbit.substance.runtime.dfs.ws;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.substance.runtime.dfs.service.FileSystemService;

public class FileSystemServiceTimerFactory implements ServiceIndexTimerFactory<FileSystemService> {

	@Override
	public FileSystemServiceTimer create(IndexProvider indexProvider, FileSystemService service) {
		return new FileSystemServiceTimer(indexProvider, service);
	}

}
