package org.orbit.substance.runtime.dfs.metadata.service;

import org.origin.common.rest.editpolicy.ServiceEditPoliciesAware;
import org.origin.common.service.WebServiceAware;

public interface DFSMetadataService extends WebServiceAware, ServiceEditPoliciesAware {

	String getUUID();

}
