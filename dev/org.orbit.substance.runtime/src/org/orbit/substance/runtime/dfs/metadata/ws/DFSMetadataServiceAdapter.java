package org.orbit.substance.runtime.dfs.metadata.ws;

import java.util.Map;

import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.ExtensionAwareServiceEditPolicy;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.substance.runtime.dfs.metadata.service.DFSMetadataService;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * When service becomes available:
 * - install edit policy
 * - start web service
 * - start indexing
 * 
 * When service becomes unavailable:
 * - stop indexing
 * - stop web service
 * - uninstall edit policy
 * 
 */
public class DFSMetadataServiceAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(DFSMetadataServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<DFSMetadataService, DFSMetadataService> serviceTracker;
	protected DFSMetadataWSApplication webApp;
	protected ServiceIndexTimer<DFSMetadataService> indexTimer;
	protected ExtensionAwareServiceEditPolicy editPolicy;

	/**
	 * 
	 * @param properties
	 */
	public DFSMetadataServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProviderProxy(this.properties);
	}

	public DFSMetadataService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<DFSMetadataService, DFSMetadataService>(bundleContext, DFSMetadataService.class, new ServiceTrackerCustomizer<DFSMetadataService, DFSMetadataService>() {
			@Override
			public DFSMetadataService addingService(ServiceReference<DFSMetadataService> reference) {
				DFSMetadataService service = bundleContext.getService(reference);

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<DFSMetadataService> reference, DFSMetadataService service) {
			}

			@Override
			public void removedService(ServiceReference<DFSMetadataService> reference, DFSMetadataService service) {
				doStop(bundleContext, service);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStart(BundleContext bundleContext, DFSMetadataService service) {
		// Install edit policies
		this.editPolicy = new ExtensionAwareServiceEditPolicy(SubstanceConstants.DFS_METADATA_EDITPOLICY_ID, DFSMetadataService.class, SubstanceConstants.DFS_METADATA_SERVICE_NAME);
		ServiceEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstall(this.editPolicy.getId());
		editPolicies.install(this.editPolicy);

		// Start web app
		this.webApp = new DFSMetadataWSApplication(service, OrbitFeatureConstants.PING | OrbitFeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexProvider indexProvider = getIndexProvider();

		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, SubstanceConstants.DFS_METADATA_INDEXER_ID);
		if (extension != null) {
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<DFSMetadataService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(indexProvider, service);
				if (this.indexTimer != null) {
					this.indexTimer.start();
				}
			}
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	protected void doStop(BundleContext bundleContext, DFSMetadataService service) {
		// Stop indexing timer
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// Stop web app
		if (this.webApp != null) {
			this.webApp.stop(bundleContext);
			this.webApp = null;
		}

		// Uninstall edit policies
		if (this.editPolicy != null) {
			ServiceEditPolicies editPolicies = service.getEditPolicies();
			editPolicies.uninstall(this.editPolicy.getId());
			this.editPolicy = null;
		}
	}

}

// IExtension extension = ExtensionActivator.getDefault().getExtensionService().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID,
// SubstanceConstants.DFS_METADATA_INDEXER_ID);
