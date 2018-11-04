package org.orbit.substance.runtime.util;

import org.orbit.infra.api.InfraConstants;
import org.orbit.substance.runtime.SubstanceConstants;
import org.origin.common.model.AbstractConfigPropertiesHandler;

public class DfsConfigPropertiesHandler extends AbstractConfigPropertiesHandler {

	protected static DfsConfigPropertiesHandler INSTANCE = new DfsConfigPropertiesHandler();

	public static DfsConfigPropertiesHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
				InfraConstants.ORBIT_CONFIG_REGISTRY_URL, //

				SubstanceConstants.ORBIT_HOST_URL, //

				SubstanceConstants.DFS__ID, //
				SubstanceConstants.DFS__NAME, //
				SubstanceConstants.DFS__HOST_URL, //
				SubstanceConstants.DFS__CONTEXT_ROOT, //
				SubstanceConstants.DFS__JDBC_DRIVER, //
				SubstanceConstants.DFS__JDBC_URL, //
				SubstanceConstants.DFS__JDBC_USERNAME, //
				SubstanceConstants.DFS__JDBC_PASSWORD, //
				SubstanceConstants.DFS__BLOCK_CAPACITY_MB //
		};
		return propNames;
	}

}
