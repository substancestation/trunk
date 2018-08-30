package org.orbit.substance.runtime.dfsvolume.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.dfsvolume.service.FileContentService;

public class FileContentServiceTimer extends ServiceIndexTimer<FileContentService> {

	protected FileContentService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public FileContentServiceTimer(IndexProvider indexProvider, FileContentService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public synchronized FileContentService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, FileContentService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, FileContentService service) throws IOException {
		String dfsId = service.getDfsId();
		String dfsVolumeId = service.getVolumeId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		long volumeCapacityBytes = service.getVolumeCapacity();
		long blockCapacityBytes = service.getDefaultBlockCapacity();

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID, dfsId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID, dfsVolumeId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__NAME, name);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__HOST_URL, hostURL);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__CONTEXT_ROOT, contextRoot);
		props.put(SubstanceConstants.IDX_PROP__LAST_HEARTBEAT_TIME, now);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__VOLUME_CAPACITY, volumeCapacityBytes);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__BLOCK_CAPACITY, blockCapacityBytes);

		return indexProvider.addIndexItem(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, FileContentService service, IndexItem indexItem) throws IOException {
		String dfsId = service.getDfsId();
		String dfsVolumeId = service.getVolumeId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		long volumeCapacityBytes = service.getVolumeCapacity();
		long blockCapacityBytes = service.getDefaultBlockCapacity();

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID, dfsId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID, dfsVolumeId);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__NAME, name);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__HOST_URL, hostURL);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__CONTEXT_ROOT, contextRoot);
		props.put(SubstanceConstants.IDX_PROP__LAST_HEARTBEAT_TIME, now);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__VOLUME_CAPACITY, volumeCapacityBytes);
		props.put(SubstanceConstants.IDX_PROP__DFS_VOLUME__BLOCK_CAPACITY, blockCapacityBytes);

		indexProvider.setProperties(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, indexItemId);
	}

}
