package org.orbit.substance.runtime.dfs.metadata.service;

import java.util.Map;

import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class DFSMetadataServiceImpl implements DFSMetadataService, LifecycleAware {

	public DFSMetadataServiceImpl(Map<Object, Object> properties) {
	}

	@Override
	public void start(BundleContext bundleContext) {

	}

	@Override
	public void stop(BundleContext bundleContext) {

	}

	@Override
	public String getUUID() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getHostURL() {
		return null;
	}

	@Override
	public String getContextRoot() {
		return null;
	}

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return null;
	}

}
