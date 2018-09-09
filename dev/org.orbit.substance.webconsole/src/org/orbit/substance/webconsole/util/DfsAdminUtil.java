package org.orbit.substance.webconsole.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.substance.api.SubstanceConstants;

public class DfsAdminUtil {

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static List<IndexItem> getDfsIndexItems(String indexServiceUrl, String accessToken) throws IOException {
		List<IndexItem> dfsIndexItems = null;
		IndexService indexService = InfraClientsUtil.Indexes.getIndexServiceClient(indexServiceUrl, accessToken);
		if (indexService != null) {
			dfsIndexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS__INDEXER_ID, SubstanceConstants.IDX__DFS__TYPE);
		}
		if (dfsIndexItems == null) {
			dfsIndexItems = new ArrayList<IndexItem>();
		}
		return dfsIndexItems;
	}

}
