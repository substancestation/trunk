package org.orbit.substance.runtime.util;

import org.orbit.infra.api.InfraConstants;
import org.orbit.substance.runtime.SubstanceConstants;
import org.origin.common.model.AbstractConfigPropertiesHandler;

public class DfsVolumeConfigPropertiesHandler extends AbstractConfigPropertiesHandler {

	protected static DfsVolumeConfigPropertiesHandler INSTANCE = new DfsVolumeConfigPropertiesHandler();

	public static DfsVolumeConfigPropertiesHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
				InfraConstants.ORBIT_CONFIG_REGISTRY_URL, //

				SubstanceConstants.ORBIT_HOST_URL, //

				SubstanceConstants.DFS_VOLUME__DFS_ID, //
				SubstanceConstants.DFS_VOLUME__ID, //
				SubstanceConstants.DFS_VOLUME__NAME, //
				SubstanceConstants.DFS_VOLUME__HOST_URL, //
				SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT, //
				SubstanceConstants.DFS_VOLUME__JDBC_DRIVER, //
				SubstanceConstants.DFS_VOLUME__JDBC_URL, //
				SubstanceConstants.DFS_VOLUME__JDBC_USERNAME, //
				SubstanceConstants.DFS_VOLUME__JDBC_PASSWORD, //
				SubstanceConstants.DFS_VOLUME__VOLUME_CAPACITY_GB, //
				SubstanceConstants.DFS_VOLUME__BLOCK_CAPACITY_MB //
		};
		return propNames;
	}

}
