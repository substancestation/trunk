package org.orbit.substance.runtime.dfs.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.substance.runtime.common.ws.OrbitWSApplication;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.FeatureConstants;

public class DfsWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param feature
	 */
	public DfsWSApplication(DfsService service, int feature) {
		super(service, feature);
		adapt(DfsService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(DfsService.class);
			}
		});
		register(DfsFilesWSResource.class);
		// register(DfsFileContentWSResource.class);
	}

	@Override
	protected int checkFeature(int feature) {
		feature = super.checkFeature(feature);

		if ((feature & FeatureConstants.JACKSON) == 0) {
			feature = feature | FeatureConstants.JACKSON;
		}
		if ((feature & FeatureConstants.MULTIPLEPART) == 0) {
			feature = feature | FeatureConstants.MULTIPLEPART;
		}
		return feature;
	}

	@Override
	public ServiceMetadata getMetadata() {
		ServiceMetadata metadata = super.getMetadata();

		DfsService service = getAdapter(DfsService.class);
		if (metadata instanceof ServiceMetadataImpl && service != null) {
			String dfsId = service.getDfsId();
			((ServiceMetadataImpl) metadata).setProperty("dfs_id", dfsId);
		}

		return metadata;
	}

}
