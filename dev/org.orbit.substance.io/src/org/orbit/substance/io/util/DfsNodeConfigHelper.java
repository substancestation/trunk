package org.orbit.substance.io.util;

import java.io.IOException;

import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.substance.api.SubstanceConstants;

public class DfsNodeConfigHelper {

	protected static final String CONFIG_REGISTRY_TYPE__NODE_CONFIG_LIST = "NodeConfigList";

	protected static final String CONFIG_REGISTRY_NAME__DFS_NODES = "DFSNodes";

	public static DfsNodeConfigHelper INSTANCE = new DfsNodeConfigHelper();

	public String getNodeConfigListType() {
		return CONFIG_REGISTRY_TYPE__NODE_CONFIG_LIST;
	}

	public String getConfigRegistryName__DfsNodes() {
		return CONFIG_REGISTRY_NAME__DFS_NODES;
	}

	/**
	 * 
	 * @param accessToken
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public IConfigRegistry getDfsNodesConfigRegistry(String accessToken, boolean createIfNotExist) throws IOException {
		IConfigRegistry cfgReg = null;
		CFG cfg = CFG.getDefault(accessToken);
		if (cfg != null) {
			cfgReg = cfg.getConfigRegistryByName(getConfigRegistryName__DfsNodes());
			if (cfgReg == null) {
				if (createIfNotExist) {
					cfgReg = cfg.createConfigRegistry(getNodeConfigListType(), getConfigRegistryName__DfsNodes(), null, false);
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
	public IConfigElement getDfsConfigElement(IConfigRegistry cfgReg, String dfsId) throws IOException {
		IConfigElement result = null;
		if (cfgReg != null && dfsId != null) {
			IConfigElement[] rootElements = cfgReg.listRootConfigElements();
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
