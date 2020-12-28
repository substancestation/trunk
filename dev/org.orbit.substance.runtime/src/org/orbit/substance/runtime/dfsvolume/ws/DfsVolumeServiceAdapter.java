package org.orbit.substance.runtime.dfsvolume.ws;

import java.util.Map;

import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.ExtensibleServiceEditPolicy;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.substance.runtime.dfsvolume.service.DfsVolumeService;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.server.FeatureConstants;
import org.origin.common.service.ILifecycle;
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
public class DfsVolumeServiceAdapter implements ILifecycle {

	protected static Logger LOG = LoggerFactory.getLogger(DfsVolumeServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<DfsVolumeService, DfsVolumeService> serviceTracker;
	protected DfsVolumeWSApplication webApp;
	protected ServiceIndexTimer<DfsVolumeService> indexTimer;
	protected ExtensibleServiceEditPolicy editPolicy;

	/**
	 * 
	 * @param properties
	 */
	public DfsVolumeServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public DfsVolumeService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/** ILifecycle */
	@Override
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<DfsVolumeService, DfsVolumeService>(bundleContext, DfsVolumeService.class, new ServiceTrackerCustomizer<DfsVolumeService, DfsVolumeService>() {
			@Override
			public DfsVolumeService addingService(ServiceReference<DfsVolumeService> reference) {
				DfsVolumeService service = bundleContext.getService(reference);

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<DfsVolumeService> reference, DfsVolumeService service) {
			}

			@Override
			public void removedService(ServiceReference<DfsVolumeService> reference, DfsVolumeService service) {
				doStop(bundleContext, service);
			}
		});
		this.serviceTracker.open();
	}

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
	protected void doStart(BundleContext bundleContext, DfsVolumeService service) {
		// 1. Install edit policies
		this.editPolicy = new ExtensibleServiceEditPolicy(SubstanceConstants.DFS_VOLUME__EDITPOLICY_ID, DfsVolumeService.class, SubstanceConstants.DFS_VOLUME__SERVICE_NAME);
		ServiceEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstall(this.editPolicy.getId());
		editPolicies.install(this.editPolicy);

		// 2. Start web app
		this.webApp = new DfsVolumeWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// 3. Start indexing timer
		// IndexServiceClient indexProvider = getIndexProvider();
		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID);
		if (extension != null) {
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<DfsVolumeService> indexTimerFactory = extension.createInstance(ServiceIndexTimerFactory.class);
			if (indexTimerFactory != null) {
				this.indexTimer = indexTimerFactory.create(service);
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
	protected void doStop(BundleContext bundleContext, DfsVolumeService service) {
		// 1. Stop indexing timer
		if (this.indexTimer != null) {
			this.indexTimer.stop();
			this.indexTimer = null;
		}

		// 2. Stop web app
		if (this.webApp != null) {
			this.webApp.stop(bundleContext);
			this.webApp = null;
		}

		// 3. Uninstall edit policies
		if (this.editPolicy != null) {
			ServiceEditPolicies editPolicies = service.getEditPolicies();
			editPolicies.uninstall(this.editPolicy.getId());
			this.editPolicy = null;
		}
	}

}

// public IndexServiceClient getIndexProvider() {
// return InfraClients.getInstance().getIndexService(this.properties, true);
// }
