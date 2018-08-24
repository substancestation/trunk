package org.orbit.substance.runtime;

import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.substance.runtime.dfs.filecontent.ws.FileContentServiceTimerFactory;
import org.orbit.substance.runtime.dfs.filecontent.ws.command.CreateDataBlockCommand;
import org.orbit.substance.runtime.dfs.filecontent.ws.command.DataBlockExistsCommand;
import org.orbit.substance.runtime.dfs.filecontent.ws.command.DeleteDataBlockCommand;
import org.orbit.substance.runtime.dfs.filecontent.ws.command.DeleteFileContentCommand;
import org.orbit.substance.runtime.dfs.filecontent.ws.command.FileContentExistsCommand;
import org.orbit.substance.runtime.dfs.filecontent.ws.command.GetDataBlockCommand;
import org.orbit.substance.runtime.dfs.filecontent.ws.command.GetFileContentCommand;
import org.orbit.substance.runtime.dfs.filecontent.ws.command.ListAllDataBlocksCommand;
import org.orbit.substance.runtime.dfs.filecontent.ws.command.ListDataBlocksCommand;
import org.orbit.substance.runtime.dfs.filecontent.ws.command.ListFileContentsCommand;
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
import org.orbit.substance.runtime.extension.dfs.FileContentServiceActivator;
import org.orbit.substance.runtime.extension.dfs.FileContentServicePropertyTester;
import org.orbit.substance.runtime.extension.dfs.FileSystemServiceActivator;
import org.orbit.substance.runtime.extension.dfs.FileSystemServicePropertyTester;
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

		createServiceActivatorExtensions();

		createPropertyTesterExtensions();

		createIndexProvideExtensions();

		createEditPolicyCommandExtensions1();
		createEditPolicyCommandExtensions2();
	}

	protected void createServiceActivatorExtensions() {
		String extensionTypeId = ServiceActivator.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, FileSystemServiceActivator.ID, "File System Service Activator");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, FileSystemServiceActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(FileSystemServicePropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, FileContentServiceActivator.ID, "File Content Service Activator");
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, FileContentServiceActivator.class);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(FileContentServicePropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

	protected void createPropertyTesterExtensions() {
		String extensionTypeId = IPropertyTester.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, FileSystemServicePropertyTester.ID);
		InterfaceDescription desc1 = new InterfaceDescription(IPropertyTester.class, FileSystemServicePropertyTester.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, FileContentServicePropertyTester.ID);
		InterfaceDescription desc2 = new InterfaceDescription(IPropertyTester.class, FileContentServicePropertyTester.class);
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

	protected void createIndexProvideExtensions() {
		String extensionTypeId = ServiceIndexTimerFactory.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, SubstanceConstants.IDX__DFS__INDEXER_ID, "File system service index provider");
		extension1.addInterface(ServiceIndexTimerFactory.class, FileSystemServiceTimerFactory.class);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, "File content service index provider");
		extension2.addInterface(ServiceIndexTimerFactory.class, FileContentServiceTimerFactory.class);
		addExtension(extension2);
	}

	protected void createEditPolicyCommandExtensions1() {
		String extensionTypeId = WSCommand.EXTENSION_TYPE_ID;
		String serviceName = SubstanceConstants.DFS__SERVICE_NAME;

		Extension extension1 = new Extension(extensionTypeId, ListRootsCommand.ID);
		extension1.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc1 = new InterfaceDescription(WSCommand.class, ListRootsCommand.class);
		desc1.setSingleton(false);
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, ListFilesCommand.ID);
		extension2.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc2 = new InterfaceDescription(WSCommand.class, ListFilesCommand.class);
		desc2.setSingleton(false);
		extension2.addInterface(desc2);
		addExtension(extension2);

		Extension extension3 = new Extension(extensionTypeId, GetFileCommand.ID);
		extension3.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc3 = new InterfaceDescription(WSCommand.class, GetFileCommand.class);
		desc3.setSingleton(false);
		extension3.addInterface(desc3);
		addExtension(extension3);

		Extension extension4 = new Extension(extensionTypeId, FileExistsCommand.ID);
		extension4.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc4 = new InterfaceDescription(WSCommand.class, FileExistsCommand.class);
		desc4.setSingleton(false);
		extension4.addInterface(desc4);
		addExtension(extension4);

		Extension extension5 = new Extension(extensionTypeId, FileIsDirectoryCommand.ID);
		extension5.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc5 = new InterfaceDescription(WSCommand.class, FileIsDirectoryCommand.class);
		desc5.setSingleton(false);
		extension5.addInterface(desc5);
		addExtension(extension5);

		Extension extension6 = new Extension(extensionTypeId, MkdirCommand.ID);
		extension6.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc6 = new InterfaceDescription(WSCommand.class, MkdirCommand.class);
		desc6.setSingleton(false);
		extension6.addInterface(desc6);
		addExtension(extension6);

		Extension extension7 = new Extension(extensionTypeId, CreateNewFileCommand.ID);
		extension7.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc7 = new InterfaceDescription(WSCommand.class, CreateNewFileCommand.class);
		desc7.setSingleton(false);
		extension7.addInterface(desc7);
		addExtension(extension7);

		Extension extension8 = new Extension(extensionTypeId, DeleteFileCommand.ID);
		extension8.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc8 = new InterfaceDescription(WSCommand.class, DeleteFileCommand.class);
		desc8.setSingleton(false);
		extension8.addInterface(desc8);
		addExtension(extension8);

		Extension extension9 = new Extension(extensionTypeId, UploadFileToFileCommand.ID);
		extension9.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc9 = new InterfaceDescription(WSCommand.class, UploadFileToFileCommand.class);
		desc9.setSingleton(false);
		extension9.addInterface(desc9);
		addExtension(extension9);

		Extension extension10 = new Extension(extensionTypeId, UploadFileToDirectoryCommand.ID);
		extension10.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc10 = new InterfaceDescription(WSCommand.class, UploadFileToDirectoryCommand.class);
		desc10.setSingleton(false);
		extension10.addInterface(desc10);
		addExtension(extension10);

		Extension extension11 = new Extension(extensionTypeId, DownloadFileCommand.ID);
		extension11.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc11 = new InterfaceDescription(WSCommand.class, DownloadFileCommand.class);
		desc11.setSingleton(false);
		extension11.addInterface(desc11);
		addExtension(extension11);
	}

	protected void createEditPolicyCommandExtensions2() {
		String extensionTypeId = WSCommand.EXTENSION_TYPE_ID;
		String serviceName = SubstanceConstants.DFS_VOLUME__SERVICE_NAME;

		// ----------------------------------------------------------------------
		// Commands for accessing data blocks
		// ----------------------------------------------------------------------
		Extension extension11 = new Extension(extensionTypeId, ListAllDataBlocksCommand.ID);
		extension11.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc11 = new InterfaceDescription(WSCommand.class, ListAllDataBlocksCommand.class);
		desc11.setSingleton(false);
		extension11.addInterface(desc11);
		addExtension(extension11);

		Extension extension12 = new Extension(extensionTypeId, ListDataBlocksCommand.ID);
		extension12.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc12 = new InterfaceDescription(WSCommand.class, ListDataBlocksCommand.class);
		desc12.setSingleton(false);
		extension12.addInterface(desc12);
		addExtension(extension12);

		Extension extension13 = new Extension(extensionTypeId, DataBlockExistsCommand.ID);
		extension13.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc13 = new InterfaceDescription(WSCommand.class, DataBlockExistsCommand.class);
		desc13.setSingleton(false);
		extension13.addInterface(desc13);
		addExtension(extension13);

		Extension extension14 = new Extension(extensionTypeId, GetDataBlockCommand.ID);
		extension14.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc14 = new InterfaceDescription(WSCommand.class, GetDataBlockCommand.class);
		desc14.setSingleton(false);
		extension14.addInterface(desc14);
		addExtension(extension14);

		Extension extension15 = new Extension(extensionTypeId, CreateDataBlockCommand.ID);
		extension15.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc15 = new InterfaceDescription(WSCommand.class, CreateDataBlockCommand.class);
		desc15.setSingleton(false);
		extension15.addInterface(desc15);
		addExtension(extension15);

		Extension extension16 = new Extension(extensionTypeId, DeleteDataBlockCommand.ID);
		extension16.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc16 = new InterfaceDescription(WSCommand.class, DeleteDataBlockCommand.class);
		desc16.setSingleton(false);
		extension16.addInterface(desc16);
		addExtension(extension16);

		// ----------------------------------------------------------------------
		// Commands for accessing file contents of a data block
		// ----------------------------------------------------------------------
		Extension extension21 = new Extension(extensionTypeId, ListFileContentsCommand.ID);
		extension21.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc21 = new InterfaceDescription(WSCommand.class, ListFileContentsCommand.class);
		desc21.setSingleton(false);
		extension21.addInterface(desc21);
		addExtension(extension21);

		Extension extension22 = new Extension(extensionTypeId, FileContentExistsCommand.ID);
		extension22.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc22 = new InterfaceDescription(WSCommand.class, FileContentExistsCommand.class);
		desc22.setSingleton(false);
		extension22.addInterface(desc22);
		addExtension(extension22);

		Extension extension23 = new Extension(extensionTypeId, GetFileContentCommand.ID);
		extension23.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc23 = new InterfaceDescription(WSCommand.class, GetFileContentCommand.class);
		desc23.setSingleton(false);
		extension23.addInterface(desc23);
		addExtension(extension23);

		Extension extension24 = new Extension(extensionTypeId, DeleteFileContentCommand.ID);
		extension24.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc24 = new InterfaceDescription(WSCommand.class, DeleteFileContentCommand.class);
		desc24.setSingleton(false);
		extension24.addInterface(desc24);
		addExtension(extension24);
	}

}
