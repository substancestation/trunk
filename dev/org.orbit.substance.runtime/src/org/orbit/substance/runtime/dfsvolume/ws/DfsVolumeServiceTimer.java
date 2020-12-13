package org.orbit.substance.runtime.dfsvolume.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.origin.common.service.WebServiceAwareHelper;
import org.origin.common.util.MapHelper;

public class DfsVolumeServiceTimer extends ServiceIndexTimer<DfsVolumeService> {

	/**
	 * 
	 * @param service
	 */
	public DfsVolumeServiceTimer(DfsVolumeService service) {
		super(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, "Index Timer [" + service.getName() + "]", service);
		setDebug(true);
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexService) throws IOException {
		DfsVolumeService dfsVolume = getService();

		String name = dfsVolume.getName();
		return indexService.getIndexItem(getIndexProviderId(), SubstanceConstants.IDX__DFS_VOLUME__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexService) throws IOException {
		DfsVolumeService dfsVolume = getService();

		String dfsId = dfsVolume.getDfsId();
		String dfsVolumeId = dfsVolume.getDfsVolumeId();
		long volumeCapacityBytes = dfsVolume.getVolumeCapacity();
		String name = dfsVolume.getName();
		String hostURL = dfsVolume.getHostURL();
		String contextRoot = dfsVolume.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(dfsVolume);
		// long blockCapacityBytes = service.getDefaultBlockCapacity();

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID, dfsId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID, dfsVolumeId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__VOLUME_CAPACITY, volumeCapacityBytes);
		// props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__BLOCK_CAPACITY, blockCapacityBytes);
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		return indexService.addIndexItem(getIndexProviderId(), SubstanceConstants.IDX__DFS_VOLUME__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		DfsVolumeService dfsVolume = getService();

		String dfsId = dfsVolume.getDfsId();
		String dfsVolumeId = dfsVolume.getDfsVolumeId();
		long volumeCapacityBytes = dfsVolume.getVolumeCapacity();
		String name = dfsVolume.getName();
		String hostURL = dfsVolume.getHostURL();
		String contextRoot = dfsVolume.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(dfsVolume);

		// long blockCapacityBytes = service.getDefaultBlockCapacity();

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID, dfsId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID, dfsVolumeId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__VOLUME_CAPACITY, volumeCapacityBytes);
		// props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__BLOCK_CAPACITY, blockCapacityBytes);
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		indexService.setProperties(getIndexProviderId(), indexItemId, props);
	}

	@Override
	public void cleanupIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = indexItem.getProperties();
		List<String> propertyNames = MapHelper.INSTANCE.getKeyList(props);
		indexService.removeProperties(getIndexProviderId(), indexItemId, propertyNames);
	}

	@Override
	public void removeIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		indexService.removeIndexItem(getIndexProviderId(), indexItemId);
	}

}
