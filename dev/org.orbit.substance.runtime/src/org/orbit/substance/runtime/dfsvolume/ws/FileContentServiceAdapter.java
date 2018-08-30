package org.orbit.substance.runtime.dfsvolume.ws;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.ExtensibleServiceEditPolicy;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.substance.runtime.dfsvolume.service.FileContentService;
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
public class FileContentServiceAdapter implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(FileContentServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<FileContentService, FileContentService> serviceTracker;
	protected FileContentWSApplication webApp;
	protected ServiceIndexTimer<FileContentService> indexTimer;
	protected ExtensibleServiceEditPolicy editPolicy;

	/**
	 * 
	 * @param properties
	 */
	public FileContentServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProvider(this.properties, true);
	}

	public FileContentService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<FileContentService, FileContentService>(bundleContext, FileContentService.class, new ServiceTrackerCustomizer<FileContentService, FileContentService>() {
			@Override
			public FileContentService addingService(ServiceReference<FileContentService> reference) {
				FileContentService service = bundleContext.getService(reference);

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<FileContentService> reference, FileContentService service) {
			}

			@Override
			public void removedService(ServiceReference<FileContentService> reference, FileContentService service) {
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
	protected void doStart(BundleContext bundleContext, FileContentService service) {
		// 1. Install edit policies
		this.editPolicy = new ExtensibleServiceEditPolicy(SubstanceConstants.DFS_VOLUME__EDITPOLICY_ID, FileContentService.class, SubstanceConstants.DFS_VOLUME__SERVICE_NAME);
		ServiceEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstall(this.editPolicy.getId());
		editPolicies.install(this.editPolicy);

		// 2. Start web app
		this.webApp = new FileContentWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// 3. Start indexing timer
		IndexProvider indexProvider = getIndexProvider();
		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID);
		if (extension != null) {
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<FileContentService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
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
	protected void doStop(BundleContext bundleContext, FileContentService service) {
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
