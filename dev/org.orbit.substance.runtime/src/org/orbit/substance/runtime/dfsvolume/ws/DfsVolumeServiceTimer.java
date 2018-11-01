package org.orbit.substance.runtime.dfsvolume.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.origin.common.service.WebServiceAwareHelper;

public class DfsVolumeServiceTimer extends ServiceIndexTimer<DfsVolumeService> {

	protected DfsVolumeService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public DfsVolumeServiceTimer(IndexServiceClient indexProvider, DfsVolumeService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public synchronized DfsVolumeService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, DfsVolumeService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, DfsVolumeService service) throws IOException {
		String dfsId = service.getDfsId();
		String dfsVolumeId = service.getDfsVolumeId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String url = WebServiceAwareHelper.INSTANCE.getURL(service);
		long volumeCapacityBytes = service.getVolumeCapacity();
		// long blockCapacityBytes = service.getDefaultBlockCapacity();

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID, dfsId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID, dfsVolumeId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__NAME, name);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__HOST_URL, hostURL);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__CONTEXT_ROOT, contextRoot);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__BASE_URL, url);
		props.put(SubstanceConstants.IDX_PROP__LAST_HEARTBEAT_TIME, now);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__VOLUME_CAPACITY, volumeCapacityBytes);
		// props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__BLOCK_CAPACITY, blockCapacityBytes);

		return indexProvider.addIndexItem(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, DfsVolumeService service, IndexItem indexItem) throws IOException {
		String dfsId = service.getDfsId();
		String dfsVolumeId = service.getDfsVolumeId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String url = WebServiceAwareHelper.INSTANCE.getURL(service);
		long volumeCapacityBytes = service.getVolumeCapacity();
		// long blockCapacityBytes = service.getDefaultBlockCapacity();

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID, dfsId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID, dfsVolumeId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__NAME, name);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__HOST_URL, hostURL);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__CONTEXT_ROOT, contextRoot);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__BASE_URL, url);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__VOLUME_CAPACITY, volumeCapacityBytes);
		// props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__BLOCK_CAPACITY, blockCapacityBytes);
		props.put(SubstanceConstants.IDX_PROP__LAST_HEARTBEAT_TIME, now);

		indexProvider.setProperties(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, indexItemId);
	}

}
