package org.orbit.substance.io.util;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.io.Activator;
import org.orbit.substance.io.DFS;
import org.orbit.substance.io.DFile;

public class DfsIndexUtil {

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static DFile getFile(URL url) throws IOException {
		if (url == null) {
			return null;
		}
		DFile file = null;

		String accessToken = url.getUserInfo();
		String dfsId = url.getHost();
		String path = url.getPath();

		String dfsServiceUrl = null;
		String indexServiceUrl = Activator.getInstance().getProperty(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		if (indexServiceUrl != null) {
			IndexItem dfsIndexItem = DfsIndexUtil.getDfsIndexItem(indexServiceUrl, accessToken, dfsId);
			if (dfsIndexItem != null) {
				dfsServiceUrl = DfsIndexUtil.getDfsServiceUrl(dfsIndexItem);
			}
		}
		if (dfsServiceUrl != null && indexServiceUrl != null) {
			DFS dfs = DFS.get(dfsServiceUrl, indexServiceUrl, accessToken);
			file = dfs.getFile(path);
		}
		return file;
	}

	/**
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public static DFile getFile(URI uri) throws IOException {
		if (uri == null) {
			return null;
		}
		DFile file = null;

		String accessToken = uri.getUserInfo();
		String dfsId = uri.getHost();
		String path = uri.getPath();

		String dfsServiceUrl = null;
		String indexServiceUrl = Activator.getInstance().getProperty(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		if (indexServiceUrl != null) {
			IndexItem dfsIndexItem = DfsIndexUtil.getDfsIndexItem(indexServiceUrl, accessToken, dfsId);
			if (dfsIndexItem != null) {
				dfsServiceUrl = DfsIndexUtil.getDfsServiceUrl(dfsIndexItem);
			}
		}
		if (dfsServiceUrl != null && indexServiceUrl != null) {
			DFS dfs = DFS.get(dfsServiceUrl, indexServiceUrl, accessToken);
			file = dfs.getFile(path);
		}
		return file;
	}

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
	 * @param dfsIndexItem
	 * @see OrbitClientHelper.getNodeControlClient(String indexServiceUrl, String accessToken, String platformId) throws IOException
	 * @return
	 */
	public static String getDfsServiceUrl(IndexItem dfsIndexItem) {
		String dfsServiceUrl = null;
		if (dfsIndexItem != null) {
			String hostURL = (String) dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__HOST_URL);
			String contextRoot = (String) dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__CONTEXT_ROOT);
			if (hostURL != null && contextRoot != null) {
				dfsServiceUrl = hostURL;
				if (!hostURL.endsWith("/") && !contextRoot.startsWith("/")) {
					dfsServiceUrl += "/";
				}
				dfsServiceUrl += contextRoot;
			}
		}
		return dfsServiceUrl;
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
