package org.orbit.substance.runtime;

import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.substance.runtime.dfs.filesystem.ws.FileSystemServiceTimerFactory;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.CreateNewFileCommand;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.DeleteFileCommand;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.DownloadFileCommand;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.FileExistsCommand;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.FileIsDirectoryCommand;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.GetFileCommand;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.ListFilesCommand;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.ListRootsCommand;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.MkdirCommand;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.UploadFileToDirectoryCommand;
import org.orbit.substance.runtime.dfs.filesystem.ws.command.UploadFileToFileCommand;
import org.orbit.substance.runtime.extension.filesystem.FileSystemServiceActivator;
import org.orbit.substance.runtime.extension.filesystem.FileSystemServicePropertyTester;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.rest.editpolicy.WSCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.substance.runtime");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createServiceActivatorExtensions1();

		createPropertyTesterExtensions1();

		createIndexProvideExtensions1();

		createEditPolicyCommandExtensions1();
	}

	protected void createServiceActivatorExtensions1() {
		String extensionTypeId = ServiceActivator.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, FileSystemServiceActivator.ID, "DFS Metadata service activator");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, FileSystemServiceActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(FileSystemServicePropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

	protected void createPropertyTesterExtensions1() {
		String extensionTypeId = IPropertyTester.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, FileSystemServicePropertyTester.ID);
		InterfaceDescription desc1 = new InterfaceDescription(IPropertyTester.class, FileSystemServicePropertyTester.class);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

	protected void createIndexProvideExtensions1() {
		String extensionTypeId = ServiceIndexTimerFactory.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, SubstanceConstants.DFS_METADATA_INDEXER_ID, "DFS Metadata Index Provider");
		extension1.addInterface(ServiceIndexTimerFactory.class, FileSystemServiceTimerFactory.class);
		addExtension(extension1);
	}

	protected void createEditPolicyCommandExtensions1() {
		String extensionTypeId = WSCommand.EXTENSION_TYPE_ID;
		String fileSystemServiceName = SubstanceConstants.DFS_FILE_SYSTEM_SERVICE_NAME;

		Extension extension1 = new Extension(extensionTypeId, ListRootsCommand.ID);
		extension1.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc1 = new InterfaceDescription(WSCommand.class, ListRootsCommand.class);
		desc1.setSingleton(false);
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, ListFilesCommand.ID);
		extension2.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc2 = new InterfaceDescription(WSCommand.class, ListFilesCommand.class);
		desc2.setSingleton(false);
		extension2.addInterface(desc2);
		addExtension(extension2);

		Extension extension3 = new Extension(extensionTypeId, GetFileCommand.ID);
		extension3.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc3 = new InterfaceDescription(WSCommand.class, GetFileCommand.class);
		desc3.setSingleton(false);
		extension3.addInterface(desc3);
		addExtension(extension3);

		Extension extension4 = new Extension(extensionTypeId, FileExistsCommand.ID);
		extension4.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc4 = new InterfaceDescription(WSCommand.class, FileExistsCommand.class);
		desc4.setSingleton(false);
		extension4.addInterface(desc4);
		addExtension(extension4);

		Extension extension5 = new Extension(extensionTypeId, FileIsDirectoryCommand.ID);
		extension5.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc5 = new InterfaceDescription(WSCommand.class, FileIsDirectoryCommand.class);
		desc5.setSingleton(false);
		extension5.addInterface(desc5);
		addExtension(extension5);

		Extension extension6 = new Extension(extensionTypeId, MkdirCommand.ID);
		extension6.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc6 = new InterfaceDescription(WSCommand.class, MkdirCommand.class);
		desc6.setSingleton(false);
		extension6.addInterface(desc6);
		addExtension(extension6);

		Extension extension7 = new Extension(extensionTypeId, CreateNewFileCommand.ID);
		extension7.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc7 = new InterfaceDescription(WSCommand.class, CreateNewFileCommand.class);
		desc7.setSingleton(false);
		extension7.addInterface(desc7);
		addExtension(extension7);

		Extension extension8 = new Extension(extensionTypeId, DeleteFileCommand.ID);
		extension8.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc8 = new InterfaceDescription(WSCommand.class, DeleteFileCommand.class);
		desc8.setSingleton(false);
		extension8.addInterface(desc8);
		addExtension(extension8);

		Extension extension9 = new Extension(extensionTypeId, UploadFileToFileCommand.ID);
		extension9.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc9 = new InterfaceDescription(WSCommand.class, UploadFileToFileCommand.class);
		desc9.setSingleton(false);
		extension9.addInterface(desc9);
		addExtension(extension9);

		Extension extension10 = new Extension(extensionTypeId, UploadFileToDirectoryCommand.ID);
		extension10.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc10 = new InterfaceDescription(WSCommand.class, UploadFileToDirectoryCommand.class);
		desc10.setSingleton(false);
		extension10.addInterface(desc10);
		addExtension(extension10);

		Extension extension11 = new Extension(extensionTypeId, DownloadFileCommand.ID);
		extension11.setProperty(WSCommand.PROP__SERVICE_NAME, fileSystemServiceName);
		InterfaceDescription desc11 = new InterfaceDescription(WSCommand.class, DownloadFileCommand.class);
		desc11.setSingleton(false);
		extension11.addInterface(desc11);
		addExtension(extension11);
	}

}
