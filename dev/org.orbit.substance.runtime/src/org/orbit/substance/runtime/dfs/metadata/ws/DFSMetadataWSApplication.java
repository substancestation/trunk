package org.orbit.substance.runtime.dfs.metadata.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.substance.runtime.common.ws.OrbitWSApplication;
import org.orbit.substance.runtime.dfs.metadata.service.DFSMetadataService;
import org.origin.common.service.WebServiceAware;

public class DFSMetadataWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param feature
	 */
	public DFSMetadataWSApplication(DFSMetadataService service, int feature) {
		super(service, feature);
		adapt(DFSMetadataService.class, service);
		adapt(WebServiceAware.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(DFSMetadataService.class);
			}
		});
		register(DFSMetadataWSResource.class);
	}

}
