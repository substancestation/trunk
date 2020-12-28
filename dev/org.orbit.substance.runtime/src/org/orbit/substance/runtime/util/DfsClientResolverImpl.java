package org.orbit.substance.runtime.util;

import java.io.IOException;

import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.DfsClientResolver;

public class DfsClientResolverImpl implements DfsClientResolver {

	@Override
	public DfsClient resolve(String accessToken) throws IOException {
		return null;
	}

	// @Override
	// public DfsClient resolve(String dfsServiceUrl, String accessToken) {
	// if (dfsServiceUrl == null || dfsServiceUrl.isEmpty()) {
	// throw new IllegalArgumentException("dfsVolumeServiceUrl is empty.");
	// }
	//
	// DfsClient dfsClient = SubstanceClientsHelper.Dfs.getDfsClient(dfsServiceUrl, accessToken);
	// return dfsClient;
	// }
	//
	// @Override
	// public String getURL(String dfsId, String accessToken) throws IOException {
	// if (dfsId == null || dfsId.isEmpty()) {
	// throw new IllegalArgumentException("dfsId is empty.");
	// }
	//
	// IndexItem dfsIndexItem = null;
	// IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(accessToken);
	// List<IndexItem> indexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS__INDEXER_ID, SubstanceConstants.IDX__DFS__TYPE);
	// for (IndexItem currIndexItem : indexItems) {
	// String currDfsId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__ID);
	// if (dfsId.equals(currDfsId)) {
	// dfsIndexItem = currIndexItem;
	// break;
	// }
	// }
	//
	// String serviceURL = null;
	// if (dfsIndexItem != null) {
	// String hostURL = (String) dfsIndexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
	// String contextRoot = (String) dfsIndexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);
	// serviceURL = WebServiceHelper.INSTANCE.getURL(hostURL, contextRoot);
	// }
	// return serviceURL;
	// }

}
