package org.orbit.substance.runtime.dfs.ws;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimer;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClients;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.ExtensibleServiceEditPolicy;
import org.orbit.substance.runtime.SubstanceConstants;
import org.orbit.substance.runtime.common.ws.OrbitFeatureConstants;
import org.orbit.substance.runtime.dfs.service.FileSystemService;
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
public class FileSystemServiceAdapter implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(FileSystemServiceAdapter.class);

	protected Map<Object, Object> properties;
	protected ServiceTracker<FileSystemService, FileSystemService> serviceTracker;
	protected FileSystemWSApplication webApp;
	protected ServiceIndexTimer<FileSystemService> indexTimer;
	protected ExtensibleServiceEditPolicy editPolicy;

	/**
	 * 
	 * @param properties
	 */
	public FileSystemServiceAdapter(Map<Object, Object> properties) {
		this.properties = properties;
	}

	public IndexProvider getIndexProvider() {
		return InfraClients.getInstance().getIndexProvider(this.properties, true);
	}

	public FileSystemService getService() {
		return (this.serviceTracker != null) ? serviceTracker.getService() : null;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		this.serviceTracker = new ServiceTracker<FileSystemService, FileSystemService>(bundleContext, FileSystemService.class, new ServiceTrackerCustomizer<FileSystemService, FileSystemService>() {
			@Override
			public FileSystemService addingService(ServiceReference<FileSystemService> reference) {
				FileSystemService service = bundleContext.getService(reference);

				doStart(bundleContext, service);
				return service;
			}

			@Override
			public void modifiedService(ServiceReference<FileSystemService> reference, FileSystemService service) {
			}

			@Override
			public void removedService(ServiceReference<FileSystemService> reference, FileSystemService service) {
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
	protected void doStart(BundleContext bundleContext, FileSystemService service) {
		// Install edit policies
		this.editPolicy = new ExtensibleServiceEditPolicy(SubstanceConstants.DFS__EDITPOLICY_ID, FileSystemService.class, SubstanceConstants.DFS__SERVICE_NAME);
		ServiceEditPolicies editPolicies = service.getEditPolicies();
		editPolicies.uninstall(this.editPolicy.getId());
		editPolicies.install(this.editPolicy);

		// Start web app
		this.webApp = new FileSystemWSApplication(service, FeatureConstants.METADATA | FeatureConstants.NAME | FeatureConstants.PING | FeatureConstants.ECHO | OrbitFeatureConstants.AUTH_TOKEN_REQUEST_FILTER);
		this.webApp.start(bundleContext);

		// Start indexing timer
		IndexProvider indexProvider = getIndexProvider();

		IExtension extension = PlatformSDKActivator.getInstance().getExtensionRegistry().getExtension(ServiceIndexTimerFactory.EXTENSION_TYPE_ID, SubstanceConstants.IDX__DFS__INDEXER_ID);
		if (extension != null) {
			@SuppressWarnings("unchecked")
			ServiceIndexTimerFactory<FileSystemService> indexTimerFactory = extension.createExecutableInstance(ServiceIndexTimerFactory.class);
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
	protected void doStop(BundleContext bundleContext, FileSystemService service) {
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
