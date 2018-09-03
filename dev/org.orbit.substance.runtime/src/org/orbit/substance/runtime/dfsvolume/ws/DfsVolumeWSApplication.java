package org.orbit.substance.runtime.dfsvolume.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.substance.runtime.common.ws.OrbitWSApplication;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;
import org.origin.common.rest.server.FeatureConstants;

public class DfsVolumeWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param feature
	 */
	public DfsVolumeWSApplication(DfsVolumeService service, int feature) {
		super(service, feature);
		adapt(DfsVolumeService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(DfsVolumeService.class);
			}
		});
		register(DfsVolumeWSResource.class);
		register(DfsVolumeFileContentWSResource.class);
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

		DfsVolumeService service = getAdapter(DfsVolumeService.class);
		if (metadata instanceof ServiceMetadataImpl && service != null) {
			String dfsId = service.getDfsId();
			String dfsVolumeId = service.getVolumeId();
			long volumeCapacity = service.getVolumeCapacity();
			long volumeSize = service.getVolumeSize();
			// long blockCapacity = service.getDefaultBlockCapacity();

			((ServiceMetadataImpl) metadata).setProperty("dfs_id", dfsId);
			((ServiceMetadataImpl) metadata).setProperty("dfs_volume_id", dfsVolumeId);
			((ServiceMetadataImpl) metadata).setProperty("volume_capacity", volumeCapacity);
			((ServiceMetadataImpl) metadata).setProperty("volume_size", volumeSize);
			// ((ServiceMetadataImpl) metadata).setProperty("block_capacity", blockCapacity);
		}

		return metadata;
	}

}

// try {
// volumeCapacity = service.getVolumeCapacity();
// } catch (ServerException e) {
// e.printStackTrace();
// }
// try {
// volumeSize = service.getVolumeSize();
// } catch (ServerException e) {
// e.printStackTrace();
// }
