package org.orbit.substance.webconsole.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
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
		IndexServiceClient indexService = InfraClientsUtil.Indexes.getIndexServiceClient(indexServiceUrl, accessToken);
		if (indexService != null) {
			dfsIndexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS__INDEXER_ID, SubstanceConstants.IDX__DFS__TYPE);
		}
		if (dfsIndexItems == null) {
			dfsIndexItems = new ArrayList<IndexItem>();
		}
		return dfsIndexItems;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static IndexItem getDfsIndexItem(String indexServiceUrl, String accessToken, String dfsId) throws IOException {
		IndexItem dfsIndexItem = null;
		IndexServiceClient indexService = InfraClientsUtil.Indexes.getIndexServiceClient(indexServiceUrl, accessToken);
		if (indexService != null) {
			List<IndexItem> dfsIndexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS__INDEXER_ID, SubstanceConstants.IDX__DFS__TYPE);
			if (dfsIndexItems != null) {
				for (IndexItem currIndexItem : dfsIndexItems) {
					String currDfsId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__ID);
					if (dfsId != null && dfsId.equals(currDfsId)) {
						dfsIndexItem = currIndexItem;
						break;
					}
				}
			}
		}
		return dfsIndexItem;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @param dfsId
	 * @return
	 * @throws IOException
	 */
	public static List<IndexItem> getDfsVolumeIndexItems(String indexServiceUrl, String accessToken, String dfsId) throws IOException {
		List<IndexItem> dfsVolumeIndexItems = new ArrayList<IndexItem>();
		IndexServiceClient indexService = InfraClientsUtil.Indexes.getIndexServiceClient(indexServiceUrl, accessToken);
		if (indexService != null) {
			List<IndexItem> allDfsVolumeIndexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE);

			for (IndexItem currIndexItem : allDfsVolumeIndexItems) {
				String currDfsId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID);
				// String currDfsVolumeId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID);
				if (dfsId != null && dfsId.equals(currDfsId)) {
					dfsVolumeIndexItems.add(currIndexItem);
				}
			}
		}
		return dfsVolumeIndexItems;
	}

}
