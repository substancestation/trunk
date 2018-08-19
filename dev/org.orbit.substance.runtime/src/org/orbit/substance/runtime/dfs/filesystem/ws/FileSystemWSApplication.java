package org.orbit.substance.runtime.dfs.filesystem.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.substance.runtime.common.ws.OrbitWSApplication;
import org.orbit.substance.runtime.dfs.filesystem.service.FileSystemService;
import org.origin.common.service.WebServiceAware;

public class FileSystemWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param feature
	 */
	public FileSystemWSApplication(FileSystemService service, int feature) {
		super(service, feature);
		adapt(FileSystemService.class, service);
		adapt(WebServiceAware.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(FileSystemService.class);
			}
		});
		register(FileSystemWSResource.class);
	}

}
