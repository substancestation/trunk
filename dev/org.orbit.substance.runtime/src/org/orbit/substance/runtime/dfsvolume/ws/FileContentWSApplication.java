package org.orbit.substance.runtime.dfsvolume.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.substance.runtime.common.ws.OrbitWSApplication;
import org.orbit.substance.runtime.dfsvolume.service.FileContentService;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;

public class FileContentWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param contextRoot
	 * @param feature
	 */
	public FileContentWSApplication(FileContentService service, int feature) {
		super(service, feature);
		adapt(FileContentService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(FileContentService.class);
			}
		});
		register(FileContentWSResource.class);
	}

	@Override
	public ServiceMetadata getMetadata() {
		ServiceMetadata metadata = super.getMetadata();

		FileContentService service = getAdapter(FileContentService.class);
		if (metadata instanceof ServiceMetadataImpl && service != null) {
			String dfsId = service.getDfsId();
			String dfsVolumeId = service.getVolumeId();
			long volumeCapacity = service.getVolumeCapacity();
			long volumeSize = volumeSize = service.getVolumeSize();
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

			((ServiceMetadataImpl) metadata).setProperty("dfs_id", dfsId);
			((ServiceMetadataImpl) metadata).setProperty("dfs_volume_id", dfsVolumeId);
			((ServiceMetadataImpl) metadata).setProperty("volume_capacity", volumeCapacity);
			((ServiceMetadataImpl) metadata).setProperty("volume_size", volumeSize);
		}

		return metadata;
	}

}
