package org.orbit.substance.io.util;

import java.io.IOException;
import java.util.List;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.origin.common.service.WebServiceAwareHelper;

public class DfsClientResolverImpl implements DfsClientResolver {

	protected String indexServiceUrl;

	/**
	 * 
	 * @param indexServiceUrl
	 */
	public DfsClientResolverImpl(String indexServiceUrl) {
		if (indexServiceUrl == null || indexServiceUrl.isEmpty()) {
			throw new IllegalArgumentException("indexServiceUrl is empty.");
		}

		this.indexServiceUrl = indexServiceUrl;
	}

	@Override
	public DfsClient resolve(String dfsServiceUrl, String accessToken) {
		if (dfsServiceUrl == null || dfsServiceUrl.isEmpty()) {
			throw new IllegalArgumentException("dfsServiceUrl is empty.");
		}

		DfsClient dfsClient = SubstanceClientsUtil.Dfs.getDfsClient(dfsServiceUrl, accessToken);
		return dfsClient;
	}

	@Override
	public String getURL(String dfsId, String accessToken) throws IOException {
		if (dfsId == null || dfsId.isEmpty()) {
			throw new IllegalArgumentException("dfsId is empty.");
		}

		IndexItem dfsIndexItem = null;
		IndexServiceClient indexService = InfraClientsUtil.INDEX_SERVICE.getIndexServiceClient(this.indexServiceUrl, accessToken);
		List<IndexItem> indexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS__INDEXER_ID, SubstanceConstants.IDX__DFS__TYPE);
		for (IndexItem currIndexItem : indexItems) {
			String currDfsId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__ID);
			if (dfsId.equals(currDfsId)) {
				dfsIndexItem = currIndexItem;
				break;
			}
		}

		String serviceURL = null;
		if (dfsIndexItem != null) {
			String hostURL = (String) dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__HOST_URL);
			String contextRoot = (String) dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__CONTEXT_ROOT);
			serviceURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
		}
		return serviceURL;
	}

}
