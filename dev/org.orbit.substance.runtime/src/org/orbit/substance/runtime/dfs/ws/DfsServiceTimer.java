package org.orbit.substance.runtime.dfs.ws;

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
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.origin.common.lang.MapHelper;
import org.origin.common.service.WebServiceAwareHelper;

public class DfsServiceTimer extends ServiceIndexTimer<DfsService> {

	/**
	 * 
	 * @param indexService
	 * @param service
	 */
	public DfsServiceTimer(IndexServiceClient indexService, DfsService service) {
		super(SubstanceConstants.IDX__DFS__INDEXER_ID, "Index Timer [" + service.getName() + "]", indexService, service);
		setDebug(true);
	}

	@Override
	public IndexItem getIndex(IndexServiceClient indexService, DfsService service) throws IOException {
		String name = service.getName();
		return indexService.getIndexItem(getIndexProviderId(), SubstanceConstants.IDX__DFS__TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexServiceClient indexService, DfsService service) throws IOException {
		String dfsId = service.getDfsId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(service);

		Date now = new Date();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS__ID, dfsId);
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		return indexService.addIndexItem(getIndexProviderId(), SubstanceConstants.IDX__DFS__TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexServiceClient indexService, DfsService service, IndexItem indexItem) throws IOException {
		String dfsId = service.getDfsId();
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();
		String baseURL = WebServiceAwareHelper.INSTANCE.getURL(service);

		Date now = new Date();

		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(SubstanceConstants.IDX_PROP__DFS__ID, dfsId);
		props.put(InfraConstants.SERVICE__NAME, name);
		props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
		props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
		props.put(InfraConstants.SERVICE__BASE_URL, baseURL);
		props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, now);

		indexService.setProperties(getIndexProviderId(), indexItemId, props);
	}

	@Override
	public void cleanupIndex(IndexServiceClient indexService, DfsService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = indexItem.getProperties();
		List<String> propertyNames = MapHelper.INSTANCE.getKeyList(props);
		indexService.removeProperties(getIndexProviderId(), indexItemId, propertyNames);
	}

	@Override
	public void removeIndex(IndexServiceClient indexService, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		indexService.deleteIndexItem(getIndexProviderId(), indexItemId);
	}

}
