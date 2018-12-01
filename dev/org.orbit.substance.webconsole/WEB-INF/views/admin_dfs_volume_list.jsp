<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.service.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.infra.api.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.infra.io.*"%>
<%@ page import="org.orbit.substance.api.*"%>
<%@ page import="org.orbit.substance.api.dfsvolume.*"%>
<%@ page import="org.orbit.substance.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

	String dfsId = (String) request.getAttribute("dfsId");

	String dfsName = null;
	IConfigElement dfsConfigElement = (IConfigElement) request.getAttribute("dfsConfigElement");
	if (dfsConfigElement != null) {
		String name = (String) dfsConfigElement.getName();
		if (name != null) {
			dfsName = name;
		} else {
			dfsName = dfsId;
		}
	}

	IConfigElement[] configElements = (IConfigElement[]) request.getAttribute("configElements");
	if (configElements == null) {
		configElements = new IConfigElement[] {};
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Substance</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/admin_dfs_volume_list.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/admin/dfslist">DFS Nodes</a> >
		<%=dfsName%>
	</div>

	<div class="main_div01">
		<h2>DFS Volume Nodes</h2>
		<div class="top_tools_div01">
			<a id="actionAddNode" class="button02">Create</a>
			<a id="actionEnableNodes" class="button02" onClick="onNodeAction('enable', '<%=contextRoot + "/admin/dfsvolumeaction?dfsId=" + dfsId%>')">Enable</a> 
			<a id="actionDisableNodes" class="button02" onClick="onNodeAction('disable', '<%=contextRoot + "/admin/dfsvolumeaction?dfsId=" + dfsId%>')">Disable</a>
			<a id="actionDeleteNodes" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/admin/dfsvolumelist?dfsId=" + dfsId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post">
			<input type="hidden" name="dfsId" value="<%=dfsId%>">
			<input id ="main_list__action" type="hidden" name="action" value="">
			<tr>
				<th class="th1" width="20">
					<input type="checkbox" onClick="toggleSelection(this, 'elementId')" />
				</th>
				<th class="th1" width="120">JVM</th>
				<th class="th1" width="100">Name</th>
				<th class="th1" width="100">DFS Volume Id</th>
				<th class="th1" width="50">Enabled</th>
				<th class="th1" width="180">URL</th>
				<th class="th1" width="50">Status</th>
				<th class="th1" width="180">Metadata</th>
				<th class="th1" width="100">Action</th>
			</tr>
			<%
				if (configElements.length == 0) {
			%>
			<tr>
				<td colspan="9">(n/a)</td>
			</tr>
			<%
				} else {
					for (IConfigElement configElement : configElements) {
						String elementId = configElement.getElementId();
						boolean enabled = configElement.getAttribute("enabled", Boolean.class);
						String dfsVolumeId = configElement.getAttribute(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID, String.class);

						String name = "";
						String serviceUrl = "";
						boolean isOnline = false;
						String metadataStr = "";
						String propStr = "";
						String jvmName = "";

						IndexItem indexItem = configElement.getAdapter(IndexItem.class);
						if (indexItem != null) {
							name = (String) indexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID);
							serviceUrl = (String) indexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
							isOnline = IndexItemHelper.INSTANCE.isOnline(indexItem);

							// config name overrides index setting
							if (configElement.getName() != null) {
								name = configElement.getName();
							}
						} else {
							name = configElement.getName();
						}

						DfsVolumeServiceMetadata metadata = configElement.getAdapter(DfsVolumeServiceMetadata.class);
						if (metadata != null) {
							long currServerTime = metadata.getServerTime();
							String currServerTimeStr = DateUtil.toString(DateUtil.toDate(currServerTime), DateUtil.SIMPLE_DATE_FORMAT2);
							jvmName = metadata.getJvmName();

							// String currDfsVolumeId = serviceMetadata.getDfsVolumeId();
							// String currName = metadata.getName();
							// String currHostUrl = metadata.getHostURL();
							// String currContextRoot = metadata.getContextRoot();
							// metadataStr += "dfs_volume_id = " + currDfsVolumeId + "<br/>";
							// metadataStr += "name = " + currName + "<br/>";
							// metadataStr += "host_url = " + currHostUrl + "<br/>";
							// metadataStr += "context_root = " + currContextRoot + "<br/>";
							metadataStr += "server_time = " + currServerTimeStr + "<br/>";

							Map<String, Object> metadataProperties = metadata.getProperties();
							if (!metadataProperties.isEmpty()) {
								for (Iterator<String> propItor = metadataProperties.keySet().iterator(); propItor.hasNext();) {
									String propName = propItor.next();
									Object propValue = metadata.getProperty(propName);

									if (propValue != null) {
										if ("server_time".equals(propName)) {
											propValue = DateUtil.toString(DateUtil.toDate(Long.valueOf(propValue.toString())), DateUtil.SIMPLE_DATE_FORMAT2);
										}
										propStr += propName + " = " + propValue + "<br/>";
									}
								}
							}
						}

						String statusText = isOnline ? "Online" : "Offline";
						String statusColor = isOnline ? "#2eb82e" : "#cccccc";
						String enabledStr = enabled ? "true" : "false";

						if (serviceUrl == null) {
							serviceUrl = "";
						}
			%>
			<tr>
				<td class="td1">
					<input type="checkbox" name="elementId" value="<%=elementId%>">
				</td>
				<td class="td1"><%=jvmName%></td>
				<td class="td1"><%=name%></td>
				<td class="td1"><%=dfsVolumeId%></td>
				<td class="td1"><%=enabledStr%></td>
				<td class="td2"><%=serviceUrl%></td>
				<td class="td1">
					<font color="<%=statusColor%>"><%=statusText%></font>
				</td>
				<td class="td2"><%=metadataStr%></td>
				<td class="td1">
					<a class="action01" href="javascript:changeNode('<%=elementId%>', '<%=dfsVolumeId%>', '<%=name%>', <%=enabled%>)">Edit</a>
				</td>
			</tr>
			<%
					} // loop
				}
			%>
			</form>
		</table>
	</div>
	<br/>

	<dialog id="newNodeDialog">
	<div class="dialog_title_div01">Create Node</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/admin/dfsvolumeadd"%>">
		<input type="hidden" name="dfsId" value="<%=dfsId%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<td width="25%">DFS Volume Id:</td>
					<td width="75%"><input type="text" name="dfs_volume_id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Enabled:</td>
					<td>
						<input name="enabled" type="radio" value="true" checked> <label>true</label> 
						<input name="enabled" type="radio" value="false" > <label>false</label> 
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okAddNode" class="button02">OK</a> 
			<a id="cancelAddNode" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changeNodeDialog">
	<div class="dialog_title_div01">Change Node</div>
	<form id="update_form" name="update_form_name" method="post" action="<%=contextRoot + "/admin/dfsvolumeupdate"%>">
		<input type="hidden" name="dfsId" value="<%=dfsId%>">
		<input type="hidden" id="node__elementId" name="elementId" >
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input id="node__name" type="text" name="name"></td>
				</tr>
				<tr>
					<td width="25%">DFS Volume Id:</td>
					<td width="75%"><input type="text" id="node__dfs_volume_id" name="dfs_volume_id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Enabled:</td>
					<td>
						<input name="enabled" type="radio" value="true"> <label>true</label> 
						<input name="enabled" type="radio" value="false"> <label>false</label> 
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeNode" class="button02">OK</a> 
			<a id="cancelChangeNode" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteNodesDialog">
	<form id="delete_form" method="post" action="<%=contextRoot + "/admin/dfsvolumedelete"%>">
		<input type="hidden" name="dfsId" value="<%=dfsId%>">
		<div class="dialog_title_div01">Delete Nodes</div>
		<div class="dialog_main_div01" id="deleteNodesDialogMessageDiv">Are you sure you want to delete selected nodes?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteNodes" class="button02">OK</a>
			<a id="cancelDeleteNodes" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="nodeActionDialog">
		<div class="dialog_title_div01" id="nodeActionDialogTitleDiv" >{Action} Nodes</div>
		<div class="dialog_main_div01" id="nodeActionDialogMessageDiv">Are you sure you want to {action} the nodes?</div>
		<div class="dialog_button_div01">
			<a id="okNodeAction" class="button02">OK</a> 
			<a id="cancelNodeAction" class="button02b">Cancel</a>
		</div>
	</dialog>

</body>
</html>
