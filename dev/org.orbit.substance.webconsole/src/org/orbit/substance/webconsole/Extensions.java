package org.orbit.substance.webconsole;

import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.substance.webconsole.extension.WebApplicationActivator;
import org.orbit.substance.webconsole.extension.WebApplicationPropertyTester;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.substance.webconsole");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createServiceActivatorExtensions();
		createPropertyTesterExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String typeId = ServiceActivator.EXTENSION_TYPE_ID;

		// DFS Web Application Activator
		Extension extension2 = new Extension(typeId, WebApplicationActivator.ID, "DFS Web Application Activator");
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, WebApplicationActivator.class);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(WebApplicationPropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

	protected void createPropertyTesterExtensions() {
		String typeId = IPropertyTester.EXTENSION_TYPE_ID;

		// DFS Web Application Property Tester
		Extension extension2 = new Extension(typeId, WebApplicationPropertyTester.ID);
		extension2.addInterface(IPropertyTester.class, WebApplicationPropertyTester.class);
		addExtension(extension2);
	}

}
