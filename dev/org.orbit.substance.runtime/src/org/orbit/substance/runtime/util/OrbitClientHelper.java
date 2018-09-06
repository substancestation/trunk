package org.orbit.substance.runtime.util;

import java.io.IOException;

import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;

public class OrbitClientHelper {

	public static OrbitClientHelper INSTANCE = new OrbitClientHelper();

	/**
	 * 
	 * @param dfsVolumeClientResolver
	 * @param accessToken
	 * @param dfsId
	 * @return
	 * @throws IOException
	 */
	public DfsVolumeClient[] getDfsVolumeClient(DfsVolumeClientResolver dfsVolumeClientResolver, String accessToken, String dfsId) throws IOException {
		DfsVolumeClient[] dfsVolumeClients = dfsVolumeClientResolver.resolve(dfsId, accessToken, Comparators.DfsVolumeIndexItemComparatorByVolumeId_ASC);
		return dfsVolumeClients;
	}

}

// if (dfsId == null) {
// throw new IllegalArgumentException("dfsId is null.");
// }
//
// List<DfsVolumeClient> dfsVolumeClients = new ArrayList<DfsVolumeClient>();
//
// List<IndexItem> dfsVolumesIndexItems = new ArrayList<IndexItem>();
// IndexService indexService = InfraClientsUtil.Indexes.getIndexServiceClient(indexServiceUrl, accessToken);
// List<IndexItem> indexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE);
// for (IndexItem currIndexItem : indexItems) {
// String currDfsId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID);
// if (currDfsId != null && currDfsId.equals(dfsId)) {
// dfsVolumesIndexItems.add(currIndexItem);
// }
// }
//
// Comparators.sort(dfsVolumesIndexItems, Comparators.DfsVolumeIndexItemComparatorByVolumeId_ASC);
//
// for (IndexItem dfsVolumeIndexItem : dfsVolumesIndexItems) {
// boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsVolumeIndexItem);
// if (isOnline) {
// String hostUrl = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__HOST_URL);
// String contextRoot = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__CONTEXT_ROOT);
//
// if (hostUrl != null && contextRoot != null) {
// String dfsVolumeServiceUrl = hostUrl;
// if (!hostUrl.endsWith("/") && !contextRoot.startsWith("/")) {
// dfsVolumeServiceUrl += "/";
// }
// dfsVolumeServiceUrl += contextRoot;
//
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(WSClientConstants.REALM, null);
// properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
// properties.put(WSClientConstants.URL, dfsVolumeServiceUrl);
//
// DfsVolumeClient dfsVolumeClient = SubstanceClients.getInstance().getDfsVolumeClient(properties);
// dfsVolumeClients.add(dfsVolumeClient);
// }
// }
// }
//
// return dfsVolumeClients.toArray(new DfsVolumeClient[dfsVolumeClients.size()]);
