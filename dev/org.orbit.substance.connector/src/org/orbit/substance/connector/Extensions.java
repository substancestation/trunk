package org.orbit.substance.connector;

import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.orbit.substance.connector.dfs.DfsConnector;
import org.orbit.substance.connector.dfsvolume.FileContentConnector;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.substance.connector");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createConnectorExtensions();
	}

	protected void createConnectorExtensions() {
		String extensionTypeId = ConnectorActivator.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, DfsConnector.ID, "File System Service Connector", "Connect to file system service");
		InterfaceDescription desc1 = new InterfaceDescription(ConnectorActivator.class, DfsConnector.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, FileContentConnector.ID, "File Content Service Connector", "Connect to file content service");
		InterfaceDescription desc2 = new InterfaceDescription(ConnectorActivator.class, FileContentConnector.class);
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

}
