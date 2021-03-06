package org.orbit.substance.io.util;

import java.io.IOException;
import java.util.List;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.IndexServiceUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.origin.common.service.WebServiceHelper;

public class DfsClientResolverByDfsId implements DfsClientResolver {

	protected String dfsId;

	public DfsClientResolverByDfsId(String dfsId) {
		this.dfsId = dfsId;

		if (this.dfsId == null || this.dfsId.isEmpty()) {
			throw new IllegalArgumentException("dfsId is null.");
		}
	}

	public String getDfsId() {
		return this.dfsId;
	}

	@Override
	public DfsClient resolve(String accessToken) throws IOException {
		String dfsServiceUrl = getURL(this.dfsId, accessToken);
		if (dfsServiceUrl == null || dfsServiceUrl.isEmpty()) {
			throw new IllegalArgumentException("dfsServiceUrl cannot be retrieved.");
		}

		DfsClient dfsClient = SubstanceClientsUtil.DFS.getDfsClient(dfsServiceUrl, accessToken);
		return dfsClient;
	}

	protected String getURL(String dfsId, String accessToken) throws IOException {
		if (dfsId == null || dfsId.isEmpty()) {
			throw new IllegalArgumentException("dfsId is empty.");
		}

		IndexItem dfsIndexItem = null;
		IndexServiceClient indexService = IndexServiceUtil.getClient(accessToken);
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
			String hostURL = (String) dfsIndexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
			String contextRoot = (String) dfsIndexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);
			serviceURL = WebServiceHelper.INSTANCE.getURL(hostURL, contextRoot);
		}
		return serviceURL;
	}

}

// @Override
// public DfsClient resolve(String dfsServiceUrl, String accessToken) {
// if (dfsServiceUrl == null || dfsServiceUrl.isEmpty()) {
// throw new IllegalArgumentException("dfsServiceUrl is empty.");
// }
//
// DfsClient dfsClient = SubstanceClientsHelper.Dfs.getDfsClient(dfsServiceUrl, accessToken);
// return dfsClient;
// }
