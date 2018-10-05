package org.orbit.substance.webconsole.servlet.dfsadmin;

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
import org.orbit.platform.api.PlatformMetadata;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfsvolume.DfsVolumeClient;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.api.dfsvolume.DfsVolumeServiceMetadata;
import org.orbit.substance.io.util.DefaultDfsVolumeClientResolver;
import org.orbit.substance.io.util.DfsUtil;
import org.orbit.substance.webconsole.WebConstants;
import org.orbit.substance.webconsole.util.OrbitClientHelper;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DfsVolumeListServlet extends HttpServlet {

	private static final long serialVersionUID = 8967936416518581588L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		String dfsId = ServletUtil.getParameter(request, "dfsId", "");

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IndexItem dfsIndexItem = null;
		List<IndexItem> dfsVolumeIndexItems = null;
		Map<String, DfsVolumeServiceMetadata> dfsVolumeIdToServiceMetadata = new HashMap<String, DfsVolumeServiceMetadata>();
		Map<String, PlatformMetadata> dfsVolumeIdToPlatformMetadata = new HashMap<String, PlatformMetadata>();

		if (!dfsId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				dfsIndexItem = DfsUtil.getDfsIndexItem(indexServiceUrl, accessToken, dfsId);

				dfsVolumeIndexItems = DfsUtil.getDfsVolumeIndexItems(indexServiceUrl, accessToken, dfsId);

				DfsVolumeClientResolver dfsVolumeClientResolver = new DefaultDfsVolumeClientResolver(indexServiceUrl);

				for (IndexItem dfsVolumeIndexItem : dfsVolumeIndexItems) {
					String dfsVolumeId = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID);

					boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsVolumeIndexItem);

					DfsVolumeServiceMetadata dfsVolumeServiceMetadata = null;
					PlatformMetadata platformMetadata = null;
					if (isOnline) {
						try {
							DfsVolumeClient dfsVolumeClient = dfsVolumeClientResolver.resolve(dfsId, dfsVolumeId, accessToken);
							if (dfsVolumeClient != null) {
								dfsVolumeServiceMetadata = dfsVolumeClient.getMetadata();
							}
						} catch (Exception e) {
							message = MessageHelper.INSTANCE.add(message, e.getMessage());
						}

						try {
							String platformId = (String) dfsVolumeIndexItem.getProperties().get(PlatformConstants.PLATFORM_ID);
							if (platformId != null) {
								IndexItem platformIndexItem = OrbitClientHelper.INSTANCE.getPlatformIndexItem(indexServiceUrl, accessToken, platformId);
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

					if (dfsVolumeServiceMetadata != null) {
						dfsVolumeIdToServiceMetadata.put(dfsVolumeId, dfsVolumeServiceMetadata);
					}
					if (platformMetadata != null) {
						dfsVolumeIdToPlatformMetadata.put(dfsVolumeId, platformMetadata);
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (dfsVolumeIndexItems == null) {
			dfsVolumeIndexItems = new ArrayList<IndexItem>();
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("dfsId", dfsId);
		if (dfsIndexItem != null) {
			request.setAttribute("dfsIndexItem", dfsIndexItem);
		}
		request.setAttribute("dfsVolumeIndexItems", dfsVolumeIndexItems);
		request.setAttribute("dfsVolumeIdToServiceMetadata", dfsVolumeIdToServiceMetadata);
		request.setAttribute("dfsVolumeIdToPlatformMetadata", dfsVolumeIdToPlatformMetadata);

		request.getRequestDispatcher(contextRoot + "/views/admin_dfs_volume_list.jsp").forward(request, response);
	}

}
