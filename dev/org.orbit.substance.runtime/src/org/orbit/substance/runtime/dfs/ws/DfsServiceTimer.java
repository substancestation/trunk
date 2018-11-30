package org.orbit.substance.runtime.dfs.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.origin.common.service.WebServiceAwareHelper;

public class DfsServiceTimer extends ServiceIndexTimer<DfsService> {

	protected DfsService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public DfsServiceTimer(IndexServiceClient indexProvider, DfsService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		this.service = service;
		setDebug(true);
	}

	@Override
	public synchronized DfsService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexProvider, DfsService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(SubstanceConstants.IDX__DFS__INDEXER_ID, SubstanceConstants.IDX__DFS__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexProvider, DfsService service) throws IOException {
		String dfsId = service.getDfsId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String url = WebServiceAwareHelper.INSTANCE.getURL(service);

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS__ID, dfsId);
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, url);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		return indexProvider.addIndexItem(SubstanceConstants.IDX__DFS__INDEXER_ID, SubstanceConstants.IDX__DFS__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexProvider, DfsService service, IndexItem indexItem) throws IOException {
		String dfsId = service.getDfsId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String url = WebServiceAwareHelper.INSTANCE.getURL(service);

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS__ID, dfsId);
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, url);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		indexProvider.setProperties(SubstanceConstants.IDX__DFS__INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexServiceClient indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.deleteIndexItem(SubstanceConstants.IDX__DFS__INDEXER_ID, indexItemId);
	}

}
