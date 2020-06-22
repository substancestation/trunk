package org.orbit.substance.runtime;

import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.substance.runtime.dfs.ws.DfsServiceTimerFactory;
import org.orbit.substance.runtime.dfs.ws.command.AllocateVolumesCommand;
import org.orbit.substance.runtime.dfs.ws.command.CreateDirectoryCommand;
import org.orbit.substance.runtime.dfs.ws.command.CreateNewFileCommand;
import org.orbit.substance.runtime.dfs.ws.command.DeleteFileByIdCommand;
import org.orbit.substance.runtime.dfs.ws.command.DeleteFileByPathCommand;
import org.orbit.substance.runtime.dfs.ws.command.DownloadFileCommand;
import org.orbit.substance.runtime.dfs.ws.command.FileExistsByIdCommand;
import org.orbit.substance.runtime.dfs.ws.command.FileExistsByPathCommand;
import org.orbit.substance.runtime.dfs.ws.command.FileIsDirectoryCommand;
import org.orbit.substance.runtime.dfs.ws.command.GetFileByIdCommand;
import org.orbit.substance.runtime.dfs.ws.command.GetFileByPathCommand;
import org.orbit.substance.runtime.dfs.ws.command.GetFileIdByPathCommand;
import org.orbit.substance.runtime.dfs.ws.command.ListFilesByParentFileIdCommand;
import org.orbit.substance.runtime.dfs.ws.command.ListFilesByParentPathCommand;
import org.orbit.substance.runtime.dfs.ws.command.ListRootsCommand;
import org.orbit.substance.runtime.dfs.ws.command.MkdirCommand;
import org.orbit.substance.runtime.dfs.ws.command.MoveToTrashByIdCommand;
import org.orbit.substance.runtime.dfs.ws.command.MoveToTrashByPathCommand;
import org.orbit.substance.runtime.dfs.ws.command.PutBackFromTrashByIdCommand;
import org.orbit.substance.runtime.dfs.ws.command.PutBackFromTrashByPathCommand;
import org.orbit.substance.runtime.dfs.ws.command.RenameFileCommand;
import org.orbit.substance.runtime.dfs.ws.command.UpdateFilePartsCommand;
import org.orbit.substance.runtime.dfs.ws.command.UploadFileToDirectoryCommand;
import org.orbit.substance.runtime.dfs.ws.command.UploadFileToFileCommand;
import org.orbit.substance.runtime.dfsvolume.ws.DfsVolumeServiceTimerFactory;
import org.orbit.substance.runtime.dfsvolume.ws.command.CreateDataBlockCommand;
import org.orbit.substance.runtime.dfsvolume.ws.command.DataBlockExistsCommand;
import org.orbit.substance.runtime.dfsvolume.ws.command.DeleteDataBlockCommand;
import org.orbit.substance.runtime.dfsvolume.ws.command.DeleteFileContentCommand;
import org.orbit.substance.runtime.dfsvolume.ws.command.FileContentExistsCommand;
import org.orbit.substance.runtime.dfsvolume.ws.command.GetDataBlockCommand;
import org.orbit.substance.runtime.dfsvolume.ws.command.GetFileContentCommand;
import org.orbit.substance.runtime.dfsvolume.ws.command.ListAllDataBlocksCommand;
import org.orbit.substance.runtime.dfsvolume.ws.command.ListDataBlocksCommand;
import org.orbit.substance.runtime.dfsvolume.ws.command.ListFileContentsCommand;
import org.orbit.substance.runtime.dfsvolume.ws.command.UpdateDataBlockSizeByDeltaCommand;
import org.orbit.substance.runtime.extension.DfsServiceActivator;
import org.orbit.substance.runtime.extension.DfsServicePropertyTester;
import org.orbit.substance.runtime.extension.DfsVolumeServiceActivator;
import org.orbit.substance.runtime.extension.DfsVolumeServicePropertyTester;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ParameterDefinition;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.rest.editpolicy.WSCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	// public Extensions() {
	// setBundleId("org.orbit.substance.runtime");
	// }

	@Override
	protected String[] getPropertyNames() {
		return new String[] { //
				SubstanceConstants.DFS__CONTEXT_ROOT, //
				SubstanceConstants.DFS_VOLUME__CONTEXT_ROOT, //
		};
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

		Extension extension1 = new Extension(extensionTypeId, DfsServiceActivator.ID, "DFS Service", "Distributed file system service.");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, DfsServiceActivator.class);

		// Conditions conditions = ConditionFactory.getInstance().newConditions(Conditions.OPERATOR.AND);
		// ICondition propertyTester1 = ConditionFactory.getInstance().newPropertyTesterCondition(DfsServicePropertyTester.ID);
		// ICondition propertyTester2 =
		// ConditionFactory.getInstance().newPropertyTesterCondition(PlatformConstants.SERVICE_ACTIVATOR_PARAMETERS_PROPERTY_TESTER_ID);
		// conditions.add(propertyTester1);
		// conditions.add(propertyTester2);
		// interfaceDesc1.setTriggerCondition(conditions);
		// ICondition propertyTester1 = ConditionFactory.getInstance().newPropertyTesterCondition(DfsServicePropertyTester.ID);
		// desc1.setTriggerCondition(propertyTester1);

		// VM arguments example:
		// -Dsubstance.dfs.autostart=true
		// -Dsubstance.dfs.id=dfs1
		// -Dsubstance.dfs.name=DFS1
		// -Dsubstance.dfs.context_root=/orbit/v1/dfs
		// -Dsubstance.dfs.block_capacity_mb=100
		// -Dsubstance.dfs.jdbc.driver=org.postgresql.Driver
		// -Dsubstance.dfs.jdbc.url=jdbc:postgresql://127.0.0.1:5432/dfs1
		// -Dsubstance.dfs.jdbc.username=postgres
		// -Dsubstance.dfs.jdbc.password=admin

		// Set parameter definitions:
		// (1) The parameters values can to be retrieved from BundleContext. If not found, use the default value from the parameter definition.

		// (2) If ConfigRegistry > platforms > platformId > services > serviceName (InterfaceDescription name) > paramName exists (not null), the value from
		// ConfigRegistry overrides the parameter value.

		// For starting of ServiceActivator:
		// (1) ServiceActivatorHelper.isAutoStart(IPlatformContext, InterfaceDescription) returns a boolean value. If ConfigRegistry > platforms > platformId >
		// services > serviceName (InterfaceDescription name) > autostart exists (not null), the value from ConfigRegistry overrides the boolean value. When the
		// value is true, start the ServiceActivator.

		// (2) When all NotEmpty parameters have values, the ServiceActivator will be started. If there are NotEmpty parameters without values, the
		// ServiceActivator will not be started. An exception should be thrown.
		desc1.setParameterDefinitions( //
				// new ParameterDefinition("autostart", "DFS service auto start", false, "false"), //
				new ParameterDefinition("substance.dfs.id", "DFS Id", true, null), //
				new ParameterDefinition("substance.dfs.name", "DFS Name", true, null), //
				new ParameterDefinition("substance.dfs.host.url", "DFS web service host URL", false, null), //
				new ParameterDefinition("substance.dfs.context_root", "DFS web service context root", true, "/orbit/v1/dfs"), //
				new ParameterDefinition("substance.dfs.block_capacity_mb", "DFS block capacity in MB", true, "100"), //
				new ParameterDefinition("substance.dfs.jdbc.driver", "JDBC driver", true, null), //
				new ParameterDefinition("substance.dfs.jdbc.url", "JDBC URL", true, null), //
				new ParameterDefinition("substance.dfs.jdbc.username", "JDBC username", true, null), //
				new ParameterDefinition("substance.dfs.jdbc.password", "JDBC password", true, null, true) //
		);

		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, DfsVolumeServiceActivator.ID, "DFS Volume Service", "Distributed file system volume service.");
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, DfsVolumeServiceActivator.class);
		// desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DfsVolumeServicePropertyTester.ID));

		desc2.setParameterDefinitions( //
				// new ParameterDefinition("autostart", "DFS volume service auto start", false, "false"), //
				new ParameterDefinition("substance.dfs_volume.dfs_id", "DFS Id", true, null), //
				new ParameterDefinition("substance.dfs_volume.id", "DFS volume Id", true, null), //
				new ParameterDefinition("substance.dfs_volume.name", "DFS volume name", true, null), //
				new ParameterDefinition("substance.dfs_volume.host.url", "DFS volume web service host URL", false, null), //
				new ParameterDefinition("substance.dfs_volume.context_root", "DFS volume web service context root", true, "/orbit/v1/dfs_volume"), //
				new ParameterDefinition("substance.dfs_volume.volume_capacity_gb", "DFS volume volume capacity in GB", true, "10"), //
				new ParameterDefinition("substance.dfs_volume.block_capacity_mb", "DFS volume block capacity in MB", true, "100"), //
				new ParameterDefinition("substance.dfs_volume.jdbc.driver", "JDBC driver", true, null), //
				new ParameterDefinition("substance.dfs_volume.jdbc.url", "JDBC URL", true, null), //
				new ParameterDefinition("substance.dfs_volume.jdbc.username", "JDBC username", true, null), //
				new ParameterDefinition("substance.dfs_volume.jdbc.password", "JDBC password", true, null, true) //
		);

		extension2.addInterface(desc2);
		addExtension(extension2);
	}

	protected void createPropertyTesterExtensions() {
		String extensionTypeId = IPropertyTester.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, DfsServicePropertyTester.ID);
		InterfaceDescription desc1 = new InterfaceDescription(IPropertyTester.class, DfsServicePropertyTester.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, DfsVolumeServicePropertyTester.ID);
		InterfaceDescription desc2 = new InterfaceDescription(IPropertyTester.class, DfsVolumeServicePropertyTester.class);
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

	protected void createIndexProvideExtensions() {
		String extensionTypeId = ServiceIndexTimerFactory.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, SubstanceConstants.IDX__DFS__INDEXER_ID, "DFS service index provider");
		extension1.addInterface(ServiceIndexTimerFactory.class, DfsServiceTimerFactory.class);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, SubstanceConstants.IDX__DFS_VOLUME__INDEXER_ID, "DFS Volume service index provider");
		extension2.addInterface(ServiceIndexTimerFactory.class, DfsVolumeServiceTimerFactory.class);
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

		Extension extension2 = new Extension(extensionTypeId, ListFilesByParentFileIdCommand.ID);
		extension2.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc2 = new InterfaceDescription(WSCommand.class, ListFilesByParentFileIdCommand.class);
		desc2.setSingleton(false);
		extension2.addInterface(desc2);
		addExtension(extension2);

		Extension extension3 = new Extension(extensionTypeId, ListFilesByParentPathCommand.ID);
		extension3.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc3 = new InterfaceDescription(WSCommand.class, ListFilesByParentPathCommand.class);
		desc3.setSingleton(false);
		extension3.addInterface(desc3);
		addExtension(extension3);

		Extension extension4 = new Extension(extensionTypeId, GetFileByIdCommand.ID);
		extension4.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc4 = new InterfaceDescription(WSCommand.class, GetFileByIdCommand.class);
		desc4.setSingleton(false);
		extension4.addInterface(desc4);
		addExtension(extension4);

		Extension extension5 = new Extension(extensionTypeId, GetFileByPathCommand.ID);
		extension5.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc5 = new InterfaceDescription(WSCommand.class, GetFileByPathCommand.class);
		desc5.setSingleton(false);
		extension5.addInterface(desc5);
		addExtension(extension5);

		Extension extension6 = new Extension(extensionTypeId, GetFileIdByPathCommand.ID);
		extension6.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc6 = new InterfaceDescription(WSCommand.class, GetFileIdByPathCommand.class);
		desc6.setSingleton(false);
		extension6.addInterface(desc6);
		addExtension(extension6);

		Extension extension7 = new Extension(extensionTypeId, FileExistsByPathCommand.ID);
		extension7.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc7 = new InterfaceDescription(WSCommand.class, FileExistsByPathCommand.class);
		desc7.setSingleton(false);
		extension7.addInterface(desc7);
		addExtension(extension7);

		Extension extension8 = new Extension(extensionTypeId, FileExistsByIdCommand.ID);
		extension8.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc8 = new InterfaceDescription(WSCommand.class, FileExistsByIdCommand.class);
		desc8.setSingleton(false);
		extension8.addInterface(desc8);
		addExtension(extension8);

		Extension extension9 = new Extension(extensionTypeId, FileIsDirectoryCommand.ID);
		extension9.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc9 = new InterfaceDescription(WSCommand.class, FileIsDirectoryCommand.class);
		desc9.setSingleton(false);
		extension9.addInterface(desc9);
		addExtension(extension9);

		Extension extension10 = new Extension(extensionTypeId, CreateDirectoryCommand.ID);
		extension10.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc10 = new InterfaceDescription(WSCommand.class, CreateDirectoryCommand.class);
		desc10.setSingleton(false);
		extension10.addInterface(desc10);
		addExtension(extension10);

		Extension extension11 = new Extension(extensionTypeId, CreateNewFileCommand.ID);
		extension11.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc11 = new InterfaceDescription(WSCommand.class, CreateNewFileCommand.class);
		desc11.setSingleton(false);
		extension11.addInterface(desc11);
		addExtension(extension11);

		Extension extension12 = new Extension(extensionTypeId, AllocateVolumesCommand.ID);
		extension12.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc12 = new InterfaceDescription(WSCommand.class, AllocateVolumesCommand.class);
		desc12.setSingleton(false);
		extension12.addInterface(desc12);
		addExtension(extension12);

		Extension extension13 = new Extension(extensionTypeId, UpdateFilePartsCommand.ID);
		extension13.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc13 = new InterfaceDescription(WSCommand.class, UpdateFilePartsCommand.class);
		desc13.setSingleton(false);
		extension13.addInterface(desc13);
		addExtension(extension13);

		Extension extension14 = new Extension(extensionTypeId, MkdirCommand.ID);
		extension14.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc14 = new InterfaceDescription(WSCommand.class, MkdirCommand.class);
		desc14.setSingleton(false);
		extension14.addInterface(desc14);
		addExtension(extension14);

		Extension extension15 = new Extension(extensionTypeId, MoveToTrashByIdCommand.ID);
		extension15.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc15 = new InterfaceDescription(WSCommand.class, MoveToTrashByIdCommand.class);
		desc15.setSingleton(false);
		extension15.addInterface(desc15);
		addExtension(extension15);

		Extension extension16 = new Extension(extensionTypeId, MoveToTrashByPathCommand.ID);
		extension16.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc16 = new InterfaceDescription(WSCommand.class, MoveToTrashByPathCommand.class);
		desc16.setSingleton(false);
		extension16.addInterface(desc16);
		addExtension(extension16);

		Extension extension17 = new Extension(extensionTypeId, PutBackFromTrashByIdCommand.ID);
		extension17.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc17 = new InterfaceDescription(WSCommand.class, PutBackFromTrashByIdCommand.class);
		desc17.setSingleton(false);
		extension17.addInterface(desc17);
		addExtension(extension17);

		Extension extension18 = new Extension(extensionTypeId, PutBackFromTrashByPathCommand.ID);
		extension18.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc18 = new InterfaceDescription(WSCommand.class, PutBackFromTrashByPathCommand.class);
		desc18.setSingleton(false);
		extension18.addInterface(desc18);
		addExtension(extension18);

		Extension extension19 = new Extension(extensionTypeId, RenameFileCommand.ID);
		extension19.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc19 = new InterfaceDescription(WSCommand.class, RenameFileCommand.class);
		desc19.setSingleton(false);
		extension19.addInterface(desc19);
		addExtension(extension19);

		Extension extension20 = new Extension(extensionTypeId, DeleteFileByIdCommand.ID);
		extension20.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc20 = new InterfaceDescription(WSCommand.class, DeleteFileByIdCommand.class);
		desc20.setSingleton(false);
		extension20.addInterface(desc20);
		addExtension(extension20);

		Extension extension21 = new Extension(extensionTypeId, DeleteFileByPathCommand.ID);
		extension21.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc21 = new InterfaceDescription(WSCommand.class, DeleteFileByPathCommand.class);
		desc21.setSingleton(false);
		extension21.addInterface(desc21);
		addExtension(extension21);

		Extension extension22 = new Extension(extensionTypeId, UploadFileToFileCommand.ID);
		extension22.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc22 = new InterfaceDescription(WSCommand.class, UploadFileToFileCommand.class);
		desc22.setSingleton(false);
		extension22.addInterface(desc22);
		addExtension(extension22);

		Extension extension23 = new Extension(extensionTypeId, UploadFileToDirectoryCommand.ID);
		extension23.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc23 = new InterfaceDescription(WSCommand.class, UploadFileToDirectoryCommand.class);
		desc23.setSingleton(false);
		extension23.addInterface(desc23);
		addExtension(extension23);

		Extension extension24 = new Extension(extensionTypeId, DownloadFileCommand.ID);
		extension24.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc24 = new InterfaceDescription(WSCommand.class, DownloadFileCommand.class);
		desc24.setSingleton(false);
		extension24.addInterface(desc24);
		addExtension(extension24);
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

		Extension extension16 = new Extension(extensionTypeId, UpdateDataBlockSizeByDeltaCommand.ID);
		extension16.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc16 = new InterfaceDescription(WSCommand.class, UpdateDataBlockSizeByDeltaCommand.class);
		desc16.setSingleton(false);
		extension16.addInterface(desc16);
		addExtension(extension16);

		Extension extension17 = new Extension(extensionTypeId, DeleteDataBlockCommand.ID);
		extension17.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc17 = new InterfaceDescription(WSCommand.class, DeleteDataBlockCommand.class);
		desc17.setSingleton(false);
		extension17.addInterface(desc17);
		addExtension(extension17);

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
