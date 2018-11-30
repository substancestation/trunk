package org.orbit.substance.io.util;

import java.io.IOException;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.api.util.PlatformClientsUtil;

public class OrbitClientHelper {

	public static OrbitClientHelper INSTANCE = new OrbitClientHelper();

	/**
	 * 
	 * @param accessToken
	 * @param indexItem
	 * @return
	 */
	public PlatformClient getPlatformClient(String accessToken, IndexItem indexItem) {
		PlatformClient platformClient = null;
		if (indexItem != null) {
			String platformUrl = null;
			String platformHostUrl = (String) indexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
			String platformContextRoot = (String) indexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);

			if (platformHostUrl != null && platformContextRoot != null) {
				platformUrl = platformHostUrl;
				if (!platformUrl.endsWith("/") && !platformContextRoot.startsWith("/")) {
					platformUrl += "/";
				}
				platformUrl += platformContextRoot;
			}

			if (platformUrl != null) {
				platformClient = PlatformClientsUtil.Platform.getPlatformClient(accessToken, platformUrl);
			}
		}
		return platformClient;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	public IndexItem getPlatformIndexItem(String indexServiceUrl, String accessToken, String platformId) throws IOException {
		IndexItem platformIndexItem = null;
		if (indexServiceUrl != null && platformId != null) {
			IndexServiceClient indexService = getIndexService(indexServiceUrl, accessToken);
			if (indexService != null) {
				platformIndexItem = indexService.getIndexItem(PlatformConstants.PLATFORM_INDEXER_ID, PlatformConstants.PLATFORM_INDEXER_TYPE, platformId);
			}
		}
		return platformIndexItem;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @return
	 */
	protected IndexServiceClient getIndexService(String indexServiceUrl, String accessToken) {
		IndexServiceClient indexService = null;
		if (indexServiceUrl != null) {
			indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(indexServiceUrl, accessToken);
		}
		return indexService;
	}

}
