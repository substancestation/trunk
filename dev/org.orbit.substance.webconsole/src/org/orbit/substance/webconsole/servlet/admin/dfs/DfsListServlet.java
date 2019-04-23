package org.orbit.substance.webconsole.servlet.admin.dfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.api.PlatformServiceMetadata;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfs.DfsClient;
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.DfsServiceMetadata;
import org.orbit.substance.io.util.DfsClientResolverByDfsURL;
import org.orbit.substance.io.util.DfsIndexItemHelper;
import org.orbit.substance.io.util.OrbitClientHelper;
import org.orbit.substance.webconsole.WebConstants;
import org.origin.common.service.WebServiceAwareHelper;
import org.origin.common.servlet.MessageHelper;

public class DfsListServlet extends HttpServlet {

	private static final long serialVersionUID = 8967936416518581588L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		List<IndexItem> dfsIndexItems = null;
		Map<String, DfsServiceMetadata> dfsIdToServiceMetadata = new HashMap<String, DfsServiceMetadata>();
		Map<String, PlatformServiceMetadata> dfsIdToPlatformMetadata = new HashMap<String, PlatformServiceMetadata>();

		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			dfsIndexItems = DfsIndexItemHelper.getDfsIndexItems(accessToken);

			for (IndexItem dfsIndexItem : dfsIndexItems) {
				String dfsId = (String) dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__ID);
				String currHostUrl = (String) dfsIndexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
				String currContextRoot = (String) dfsIndexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);
				String dfsServiceUrl = WebServiceAwareHelper.INSTANCE.getURL(currHostUrl, currContextRoot);

				boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsIndexItem);

				DfsServiceMetadata dfsServiceMetadata = null;
				PlatformServiceMetadata platformMetadata = null;
				if (isOnline) {
					try {
						DfsClientResolver dfsClientResolver = new DfsClientResolverByDfsURL(dfsServiceUrl);
						DfsClient dfsClient = dfsClientResolver.resolve(accessToken);
						if (dfsClient != null) {
							dfsServiceMetadata = dfsClient.getMetadata();
						}
					} catch (Exception e) {
						message = MessageHelper.INSTANCE.add(message, e.getMessage());
					}

					try {
						String platformId = (String) dfsIndexItem.getProperties().get(PlatformConstants.IDX_PROP__PLATFORM_ID);
						if (platformId != null) {
							IndexItem platformIndexItem = OrbitClientHelper.INSTANCE.getPlatformIndexItem(accessToken, platformId);
							if (platformIndexItem != null) {
								PlatformClient dfsPlatformClient = OrbitClientHelper.INSTANCE.getPlatformClient(accessToken, platformIndexItem);
								if (dfsPlatformClient != null) {
									platformMetadata = dfsPlatformClient.getMetadata();
								}
							}
						}
					} catch (Exception e) {
						// message = MessageHelper.INSTANCE.add(message, e.getMessage());
					}
				}

				if (dfsServiceMetadata != null) {
					dfsIdToServiceMetadata.put(dfsId, dfsServiceMetadata);
				}
				if (platformMetadata != null) {
					dfsIdToPlatformMetadata.put(dfsId, platformMetadata);
				}
			}

		} catch (Exception e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}
		if (dfsIndexItems == null) {
			dfsIndexItems = new ArrayList<IndexItem>();
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("dfsIndexItems", dfsIndexItems);
		request.setAttribute("dfsIdToServiceMetadata", dfsIdToServiceMetadata);
		request.setAttribute("dfsIdToPlatformMetadata", dfsIdToPlatformMetadata);

		request.getRequestDispatcher(contextRoot + "/views/admin_dfs_list_v0.jsp").forward(request, response);
	}

}
