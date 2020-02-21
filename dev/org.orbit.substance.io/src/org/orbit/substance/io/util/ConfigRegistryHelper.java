package org.orbit.substance.io.util;

import java.io.IOException;

import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.substance.api.SubstanceConstants;

public class ConfigRegistryHelper {

	public static final String REGISTRY__DFS_NODES = "DFSNodes";
	public static final String TYPE__NODE_CONFIG_LIST = "NodeConfigList";

	/**
	 * 
	 * @param accessToken
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public static IConfigRegistry getDfsNodesConfigRegistry(String accessToken, boolean createIfNotExist) throws IOException {
		IConfigRegistry cfgReg = null;
		CFG cfg = CFG.getDefault(accessToken);
		if (cfg != null) {
			cfgReg = cfg.getConfigRegistryByName(REGISTRY__DFS_NODES);
			if (cfgReg == null) {
				if (createIfNotExist) {
					cfgReg = cfg.createConfigRegistry(TYPE__NODE_CONFIG_LIST, REGISTRY__DFS_NODES, null, false);
				}
			}
		}
		return cfgReg;
	}

	/**
	 * 
	 * @param cfgReg
	 * @param dfsId
	 * @return
	 * @throws IOException
	 */
	public static IConfigElement getDfsConfigElement(IConfigRegistry cfgReg, String dfsId) throws IOException {
		IConfigElement result = null;
		if (cfgReg != null && dfsId != null) {
			IConfigElement[] rootElements = cfgReg.listRootElements();
			if (rootElements != null) {
				for (IConfigElement rootElement : rootElements) {
					String currDfsId = rootElement.getAttribute(SubstanceConstants.IDX_PROP__DFS__ID, String.class);
					if (dfsId.equals(currDfsId)) {
						result = rootElement;
						break;
					}
				}
			}
		}
		return result;
	}

}
