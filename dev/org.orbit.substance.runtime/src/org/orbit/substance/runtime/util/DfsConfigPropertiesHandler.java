package org.orbit.substance.runtime.util;

import org.orbit.substance.runtime.SubstanceConstants;
import org.origin.common.model.AbstractConfigPropertiesHandler;

public class DfsConfigPropertiesHandler extends AbstractConfigPropertiesHandler {

	// VM arguments example:
	// -Dsubstance.dfs.autostart=true
	// -Dsubstance.dfs.id=dfs1
	// -Dsubstance.dfs.name=DFS1
	// -Dsubstance.dfs.context_root=/orbit/v1/dfs
	// -Dsubstance.dfs.block_capacity_mb=100
	// -Dsubstance.dfs.jdbc.driver=org.postgresql.Driver
	// -Dsubstance.dfs.jdbc.url=jdbc:postgresql://127.0.0.1:5432/dfs1
	// -Dsubstance.dfs.jdbc.username=postgres
	// -Dsubstance.dfs.jdbc.password=admin
	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				SubstanceConstants.ORBIT_HOST_URL, //

				SubstanceConstants.DFS__AUTOSTART, //
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

// InfraConstants.ORBIT_INDEX_SERVICE_URL, //
// InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
// InfraConstants.ORBIT_CONFIG_REGISTRY_URL, //
