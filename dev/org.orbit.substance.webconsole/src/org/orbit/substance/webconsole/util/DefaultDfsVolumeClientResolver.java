package org.orbit.substance.webconsole.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.api.util.SubstanceClientsUtil;

public class DefaultDfsVolumeClientResolver implements DfsVolumeClientResolver {

	protected String indexServiceUrl;

	/**
	 * 
	 * @param indexServiceUrl
	 */
	public DefaultDfsVolumeClientResolver(String indexServiceUrl) {
		this.indexServiceUrl = indexServiceUrl;
	}

	@Override
	public String getDfsVolumeServiceUrl(String dfsId, String dfsVolumeId, String accessToken) throws IOException {
		if (dfsId == null || dfsId.isEmpty()) {
			throw new IllegalArgumentException("dfsId is null.");
		}
		if (dfsVolumeId == null || dfsVolumeId.isEmpty()) {
			throw new IllegalArgumentException("dfsVolumeId is null.");
		}

		String dfsVolumeServiceUrl = null;

		IndexItem dfsVolumeIndexItem = null;
		IndexServiceClient indexService = InfraClientsUtil.Indexes.getIndexServiceClient(this.indexServiceUrl, accessToken);
		List<IndexItem> indexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE);
		for (IndexItem currIndexItem : indexItems) {
			String currDfsId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID);
			String currDfsVolumeId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID);
			if (dfsId.equals(currDfsId) && dfsVolumeId.equals(currDfsVolumeId)) {
				dfsVolumeIndexItem = currIndexItem;
				break;
			}
		}

		if (dfsVolumeIndexItem != null) {
			String hostUrl = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__HOST_URL);
			String contextRoot = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__CONTEXT_ROOT);

			if (hostUrl != null && contextRoot != null) {
				dfsVolumeServiceUrl = hostUrl;
				if (!hostUrl.endsWith("/") && !contextRoot.startsWith("/")) {
					dfsVolumeServiceUrl += "/";
				}
				dfsVolumeServiceUrl += contextRoot;
			}

			boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsVolumeIndexItem);
			if (!isOnline) {
				throw new IllegalStateException("DFS volume service '" + dfsVolumeServiceUrl + "' is not online.");
			}
		}

		return dfsVolumeServiceUrl;
	}

	@Override
	public DfsVolumeClient[] resolve(String dfsId, String accessToken, Comparator<?> indexItemsComparator) throws IOException {
		if (dfsId == null) {
			throw new IllegalArgumentException("dfsId is null.");
		}

		List<DfsVolumeClient> dfsVolumeClients = new ArrayList<DfsVolumeClient>();

		List<IndexItem> dfsVolumesIndexItems = new ArrayList<IndexItem>();
		IndexServiceClient indexService = InfraClientsUtil.Indexes.getIndexServiceClient(indexServiceUrl, accessToken);
		List<IndexItem> indexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE);
		for (IndexItem currIndexItem : indexItems) {
			String currDfsId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID);
			if (currDfsId != null && currDfsId.equals(dfsId)) {
				dfsVolumesIndexItems.add(currIndexItem);
			}
		}

		if (indexItemsComparator != null) {
			Collections.sort(dfsVolumesIndexItems, (Comparator<IndexItem>) indexItemsComparator);
		}

		for (IndexItem dfsVolumeIndexItem : dfsVolumesIndexItems) {
			boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsVolumeIndexItem);
			if (isOnline) {
				String hostUrl = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__HOST_URL);
				String contextRoot = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__CONTEXT_ROOT);

				if (hostUrl != null && contextRoot != null) {
					String dfsVolumeServiceUrl = hostUrl;
					if (!hostUrl.endsWith("/") && !contextRoot.startsWith("/")) {
						dfsVolumeServiceUrl += "/";
					}
					dfsVolumeServiceUrl += contextRoot;

					DfsVolumeClient dfsVolumeClient = resolve(dfsVolumeServiceUrl, accessToken);
					if (dfsVolumeClient != null) {
						dfsVolumeClients.add(dfsVolumeClient);
					}
				}
			}
		}

		return dfsVolumeClients.toArray(new DfsVolumeClient[dfsVolumeClients.size()]);
	}

	@Override
	public DfsVolumeClient resolve(String dfsId, String dfsVolumeId, String accessToken) throws IOException {
		DfsVolumeClient dfsVolumeClient = null;
		String dfsVolumeServiceUrl = getDfsVolumeServiceUrl(dfsId, dfsVolumeId, accessToken);
		if (dfsVolumeServiceUrl != null) {
			dfsVolumeClient = resolve(dfsVolumeServiceUrl, accessToken);
		}
		return dfsVolumeClient;
	}

	@Override
	public DfsVolumeClient resolve(String dfsVolumeServiceUrl, String accessToken) {
		DfsVolumeClient dfsVolumeClient = SubstanceClientsUtil.DfsVolume.getDfsVolumeClient(dfsVolumeServiceUrl, accessToken);
		return dfsVolumeClient;
	}

}

// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(WSClientConstants.REALM, null);
// properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
// properties.put(WSClientConstants.URL, dfsVolumeServiceUrl);
// DfsVolumeClient dfsVolumeClient = SubstanceClients.getInstance().getDfsVolumeClient(properties);
// dfsVolumeClients.add(dfsVolumeClient);
