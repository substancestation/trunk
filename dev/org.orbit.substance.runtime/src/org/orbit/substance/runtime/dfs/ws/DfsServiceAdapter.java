package org.orbit.substance.runtime.dfs.ws;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.ExtensibleServiceEditPolicy;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.substance.runtime.dfs.service.DfsService;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.rest.util.LifecycleAware;
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
public class DfsServiceAdapter implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(DfsServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<DfsService, DfsService> serviceTracker;
	protected DfsWSApplication webApp;
	protected ServiceIndexTimer<DfsService> indexTimer;
	protected ExtensibleServiceEditPolicy editPolicy;

	/**
	 * 
	 * @param properties
	 */
	public DfsServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexServiceClient getIndexProvider() {
		return InfraClients.getInstance().getIndexService(this.properties, true);
	}

	public DfsService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<DfsService, DfsService>(bundleContext, DfsService.class, new ServiceTrackerCustomizer<DfsService, DfsService>() {
			@Override
			public DfsService addingService(ServiceReference<DfsService> reference) {
				DfsService service = bundleContext.getService(reference);

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<DfsService> reference, DfsService service) {
			}

			@Override
			public void removedService(ServiceReference<DfsService> reference, DfsService service) {
				doStop(bundleContext, service);
			}
		});
		this.serviceTracker.open();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
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
	protected void doStart(BundleContext bundleContext, DfsService service) {
		// Install edit policies
		this.editPolicy = new ExtensibleServiceEditPolicy(SubstanceConstants.DFS__EDITPOLICY_ID, DfsService.class, SubstanceConstants.DFS__SERVICE_NAME);
		ServiceEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstall(this.editPolicy.getId());
		editPolicies.install(this.editPolicy);

		// Start web app
		this.webApp = new DfsWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexServiceClient indexProvider = getIndexProvider();

		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, SubstanceConstants.IDX__DFS__INDEXER_ID);
		if (extension != null) {
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<DfsService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
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
	protected void doStop(BundleContext bundleContext, DfsService service) {
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
