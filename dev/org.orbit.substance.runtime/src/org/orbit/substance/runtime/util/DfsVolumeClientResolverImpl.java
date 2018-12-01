package org.orbit.substance.runtime.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.api.util.SubstanceClientsHelper;
import org.origin.common.service.WebServiceAwareHelper;

public class DfsVolumeClientResolverImpl implements DfsVolumeClientResolver {

	protected String indexServiceUrl;

	/**
	 * 
	 * @param indexServiceUrl
	 */
	public DfsVolumeClientResolverImpl(String indexServiceUrl) {
		if (indexServiceUrl == null || indexServiceUrl.isEmpty()) {
			throw new IllegalArgumentException("indexServiceUrl is empty.");
		}

		this.indexServiceUrl = indexServiceUrl;
	}

	@Override
	public DfsVolumeClient resolve(String dfsVolumeServiceUrl, String accessToken) {
		if (dfsVolumeServiceUrl == null || dfsVolumeServiceUrl.isEmpty()) {
			throw new IllegalArgumentException("dfsVolumeServiceUrl is empty.");
		}

		DfsVolumeClient dfsVolumeClient = SubstanceClientsHelper.DfsVolume.getDfsVolumeClient(dfsVolumeServiceUrl, accessToken);
		return dfsVolumeClient;
	}

	@Override
	public String getURL(String dfsId, String dfsVolumeId, String accessToken) throws IOException {
		if (dfsId == null || dfsId.isEmpty()) {
			throw new IllegalArgumentException("dfsId is empty.");
		}
		if (dfsVolumeId == null || dfsVolumeId.isEmpty()) {
			throw new IllegalArgumentException("dfsVolumeId is empty.");
		}

		IndexItem dfsVolumeIndexItem = null;
		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(this.indexServiceUrl, accessToken);
		List<IndexItem> indexItems = indexService.getIndexItems(SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, SubstanceConstants.IDX__DFS_VOLUME__TYPE);
		for (IndexItem currIndexItem : indexItems) {
			String currDfsId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID);
			String currDfsVolumeId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID);
			if (dfsId.equals(currDfsId) && dfsVolumeId.equals(currDfsVolumeId)) {
				dfsVolumeIndexItem = currIndexItem;
				break;
			}
		}

		String serviceURL = null;
		if (dfsVolumeIndexItem != null) {
			String hostURL = (String) dfsVolumeIndexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
			String contextRoot = (String) dfsVolumeIndexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);
			serviceURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
		}
		return serviceURL;
	}

	@Override
	public DfsVolumeClient[] resolve(String dfsId, String accessToken, Comparator<?> indexItemsComparator) throws IOException {
		if (dfsId == null || dfsId.isEmpty()) {
			throw new IllegalArgumentException("dfsId is empty.");
		}

		List<DfsVolumeClient> dfsVolumeClients = new ArrayList<DfsVolumeClient>();

		List<IndexItem> dfsVolumesIndexItems = new ArrayList<IndexItem>();
		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(indexServiceUrl, accessToken);
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
			// boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsVolumeIndexItem);
			String hostURL = (String) dfsVolumeIndexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
			String contextRoot = (String) dfsVolumeIndexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);

			String serviceURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
			if (serviceURL != null) {
				DfsVolumeClient dfsVolumeClient = resolve(serviceURL, accessToken);
				if (dfsVolumeClient != null) {
					dfsVolumeClients.add(dfsVolumeClient);
				}
			}
		}

		return dfsVolumeClients.toArray(new DfsVolumeClient[dfsVolumeClients.size()]);
	}

	@Override
	public DfsVolumeClient resolve(String dfsId, String dfsVolumeId, String accessToken) throws IOException {
		if (dfsId == null || dfsId.isEmpty()) {
			throw new IllegalArgumentException("dfsId is empty.");
		}
		if (dfsVolumeId == null || dfsVolumeId.isEmpty()) {
			throw new IllegalArgumentException("dfsVolumeId is empty.");
		}

		DfsVolumeClient dfsVolumeClient = null;
		String serviceURL = getURL(dfsId, dfsVolumeId, accessToken);
		if (serviceURL != null) {
			dfsVolumeClient = resolve(serviceURL, accessToken);
		}
		return dfsVolumeClient;
	}

}
