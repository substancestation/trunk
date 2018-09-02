package org.orbit.substance.runtime.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.util.SubstanceClients;
import org.orbit.substance.runtime.SubstanceConstants;
import org.origin.common.rest.client.WSClientConstants;

public class OrbitClientHelper {

	public static OrbitClientHelper INSTANCE = new OrbitClientHelper();

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @param dfsId
	 * @return
	 * @throws IOException
	 */
	public DfsVolumeClient[] getDfsVolumeClient(String indexServiceUrl, String accessToken, String dfsId) throws IOException {
		if (indexServiceUrl == null) {
			throw new IllegalArgumentException("indexServiceUrl is null.");
		}
		if (dfsId == null) {
			throw new IllegalArgumentException("dfsId is null.");
		}

		List<DfsVolumeClient> dfsVolumeClients = new ArrayList<DfsVolumeClient>();

		List<IndexItem> dfsVolumesIndexItems = new ArrayList<IndexItem>();
		IndexService indexService = InfraClientsUtil.Indexes.getIndexServiceClient(indexServiceUrl, accessToken);
		List<IndexItem> indexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE);
		for (IndexItem currIndexItem : indexItems) {
			String currDfsId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID);
			if (currDfsId != null && currDfsId.equals(dfsId)) {
				dfsVolumesIndexItems.add(currIndexItem);
			}
		}

		for (IndexItem dfsVolumesIndexItem : dfsVolumesIndexItems) {
			boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsVolumesIndexItem);
			if (isOnline) {
				String hostUrl = (String) dfsVolumesIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__HOST_URL);
				String contextRoot = (String) dfsVolumesIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__CONTEXT_ROOT);

				if (hostUrl != null && contextRoot != null) {
					String dfsVolumeServiceUrl = hostUrl;
					if (!hostUrl.endsWith("/") && !contextRoot.startsWith("/")) {
						dfsVolumeServiceUrl += "/";
					}
					dfsVolumeServiceUrl += contextRoot;

					Map<String, Object> properties = new HashMap<String, Object>();
					properties.put(WSClientConstants.REALM, null);
					properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
					properties.put(WSClientConstants.URL, dfsVolumeServiceUrl);

					DfsVolumeClient dfsVolumeClient = SubstanceClients.getInstance().getDfsVolumeClient(properties);
					dfsVolumeClients.add(dfsVolumeClient);
				}
			}
		}

		return dfsVolumeClients.toArray(new DfsVolumeClient[dfsVolumeClients.size()]);
	}

}
