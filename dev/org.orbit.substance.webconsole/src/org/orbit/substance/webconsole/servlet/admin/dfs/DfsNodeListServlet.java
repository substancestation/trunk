package org.orbit.substance.webconsole.servlet.admin.dfs;

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
import org.orbit.substance.api.dfs.DfsClientResolver;
import org.orbit.substance.api.dfs.DfsServiceMetadata;
import org.orbit.substance.api.util.SubstanceClientsUtil;
import org.orbit.substance.io.util.DfsClientResolverByDfsURL;
import org.orbit.substance.io.util.DfsIndexItemHelper;
import org.orbit.substance.io.util.NodeConfigHelper;
import org.orbit.substance.webconsole.WebConstants;
import org.origin.common.servlet.MessageHelper;

public class DfsNodeListServlet extends HttpServlet {

	private static final long serialVersionUID = -1262894023641678270L;

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
		IConfigElement[] configElements = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			IConfigRegistry cfgReg = NodeConfigHelper.getDfsNodesConfigRegistry(accessToken, true);
			if (cfgReg != null) {
				configElements = cfgReg.listRootConfigElements();

				if (configElements != null) {
					Map<String, IndexItem> dfsIndexItemMap = DfsIndexItemHelper.getDfsIndexItemsMap(accessToken);

					for (IConfigElement configElement : configElements) {
						String dfsId = configElement.getAttribute(SubstanceConstants.IDX_PROP__DFS__ID, String.class);

						IndexItem dfsndexItem = dfsIndexItemMap.get(dfsId);
						if (dfsndexItem != null) {
							configElement.adapt(IndexItem.class, dfsndexItem);

							boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsndexItem);
							if (isOnline) {
								try {
									String dfsServiceUrl = (String) dfsndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
									DfsClientResolver dfsClientResolver = new DfsClientResolverByDfsURL(dfsServiceUrl);

									DfsServiceMetadata metadata = SubstanceClientsUtil.DFS.getServiceMetadata(dfsClientResolver, accessToken);
									if (metadata != null) {
										configElement.adapt(DfsServiceMetadata.class, metadata);
									}
								} catch (Exception e) {
									message = MessageHelper.INSTANCE.add(message, e.getMessage() + " DFS Id: '" + dfsId + "'");
									e.printStackTrace();
								}
							}
						}
					}
				}

			} else {
				message = MessageHelper.INSTANCE.add(message, "Config registry for '" + NodeConfigHelper.REGISTRY__DFS_NODES + "' cannot be found or created.");
			}

		} catch (Exception e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("configElements", configElements);

		request.getRequestDispatcher(contextRoot + "/views/admin_dfs_list.jsp").forward(request, response);
	}

}
