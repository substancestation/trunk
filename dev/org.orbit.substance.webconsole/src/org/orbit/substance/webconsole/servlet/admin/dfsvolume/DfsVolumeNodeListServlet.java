package org.orbit.substance.webconsole.servlet.admin.dfsvolume;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.substance.api.SubstanceConstants;
import org.orbit.substance.api.dfsvolume.DfsVolumeClientResolver;
import org.orbit.substance.api.dfsvolume.DfsVolumeServiceMetadata;
import org.orbit.substance.api.util.SubstanceClientsHelper;
import org.orbit.substance.io.util.DfsIndexItemHelper;
import org.orbit.substance.io.util.DfsVolumeClientResolverImpl;
import org.orbit.substance.io.util.NodeConfigHelper;
import org.orbit.substance.webconsole.WebConstants;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DfsVolumeNodeListServlet extends HttpServlet {

	private static final long serialVersionUID = 3280290443571561510L;

	protected static IConfigElement[] EMPTY_CONFIG_ELEMENTS = new IConfigElement[] {};

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

		String dfsId = ServletUtil.getParameter(request, "dfsId", "");

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigElement dfsConfigElement = null;
		IConfigElement[] configElements = null;

		if (!dfsId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = NodeConfigHelper.INSTANCE.getDfsNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					dfsConfigElement = NodeConfigHelper.INSTANCE.getDfsConfigElement(cfgReg, dfsId);
					if (dfsConfigElement != null) {
						configElements = dfsConfigElement.memberConfigElements();
					} else {
						message = MessageHelper.INSTANCE.add(message, "Config element for DFS node (dfsId: '" + dfsId + "') cannot be found.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry for '" + NodeConfigHelper.INSTANCE.getConfigRegistryName__DfsNodes() + "' cannot be found or created.");
				}

				if (configElements != null) {
					Map<String, IndexItem> dfsVolumeIndexItemMap = DfsIndexItemHelper.getDfsVolumeIndexItemsMap(accessToken, dfsId);

					DfsVolumeClientResolver clientResolver = new DfsVolumeClientResolverImpl();

					for (IConfigElement configElement : configElements) {
						String dfsVolumeId = configElement.getAttribute(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID, String.class);

						IndexItem dfsVolumeIndexItem = dfsVolumeIndexItemMap.get(dfsVolumeId);
						if (dfsVolumeIndexItem != null) {
							configElement.adapt(IndexItem.class, dfsVolumeIndexItem);

							boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsVolumeIndexItem);
							if (isOnline) {
								try {
									String dfsVolumeServiceUrl = (String) dfsVolumeIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
									DfsVolumeServiceMetadata metadata = SubstanceClientsHelper.DfsVolume.getServiceMetadata(clientResolver, dfsVolumeServiceUrl, accessToken);
									if (metadata != null) {
										configElement.adapt(DfsVolumeServiceMetadata.class, metadata);
									}
								} catch (Exception e) {
									message = MessageHelper.INSTANCE.add(message, e.getMessage() + " dfsVolumeId: '" + dfsVolumeId + "'");
									e.printStackTrace();
								}
							}
						}
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (configElements == null) {
			configElements = EMPTY_CONFIG_ELEMENTS;
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("dfsId", dfsId);
		if (dfsConfigElement != null) {
			request.setAttribute("dfsConfigElement", dfsConfigElement);
		}
		request.setAttribute("configElements", configElements);

		request.getRequestDispatcher(contextRoot + "/views/admin_dfs_volume_list.jsp").forward(request, response);
	}

}
