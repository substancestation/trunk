<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.substance.api.dfs.*"%>
<%@ page import="org.orbit.substance.api.dfs.File"%>
<%@ page import="org.orbit.substance.api.dfs.Path"%>
<%@ page import="org.orbit.substance.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);

	File[] files = (File[]) request.getAttribute("files");
	if (files == null) {
		files = new File[0];
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Files</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
<script type="text/javascript" src="<%=contextRoot + "/views/js/dfs_files.js"%>" defer></script>
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="main_div01">
		<h2>Machines</h2>
		<div class="top_tools_div01">
			<a id="action.addMachine" class="button02">Add</a>
			<a id="action.deleteMachines" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/domain/machines"%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="<%=contextRoot + "/domain/machinedelete"%>">
			<tr>
				<th class="th1" width="10"></th>
				<th class="th1" width="180">File Id</th>
				<th class="th1" width="180">Parent File Id</th>
				<th class="th1" width="180">Path</th>
				<th class="th1" width="180">Is Directory</th>
				<th class="th1" width="180">Is Hidden</th>
				<th class="th1" width="180">Size</th>
				<th class="th1" width="180">Date Created</th>
				<th class="th1" width="180">Date Modified</th>
				<th class="th1" width="180">Actions</th>
			</tr>
			<%
				if (files.length == 0) {
			%>
			<tr>
				<td colspan="10">(n/a)</td>
			</tr>
			<%
				} else {
					for (File file : files) {
						String fileId = file.getFileId();
						String parentFileId = file.getParentFileId();
						Path path = file.getPath();
						boolean isDirectory = file.isDirectory();
						boolean isHidden = file.isHidden();
						long size = file.getSize();
						long dateCreated = file.getDateCreated();
						long dateModified = file.getDateModified();

						String pathStr = (path != null) ? path.getPathString() : "";
						String dateCreatedStr = DateUtil.toString(dateCreated, DateUtil.SIMPLE_DATE_FORMAT2);
						String dateModifiedStr = DateUtil.toString(dateCreated, DateUtil.SIMPLE_DATE_FORMAT2);
			%>
			<tr>
				<td class="td1">
					<input type="checkbox" name="file_id" value="<%=fileId%>">
				</td>
				<td class="td1"><%=fileId%></td>
				<td class="td1"><%=parentFileId%></td>
				<td class="td1"><%=pathStr%></td>
				<td class="td2"><%=isDirectory%></td>
				<td class="td2"><%=isHidden%></td>
				<td class="td2"><%=size%></td>
				<td class="td2"><%=dateCreatedStr%></td>
				<td class="td2"><%=dateModifiedStr%></td>
				<td class="td1">

				</td>
			</tr>
			<%
					}
				}
			%>
			</form>
		</table>
	</div>

	<dialog id="newMachineDialog">
	<div class="dialog_title_div01">Add Machine</div>
		<form id="new_form" method="post" action="<%=contextRoot + "/domain/machineadd"%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" name="id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<td>IP Address:</td>
					<td><input type="text" name="ip"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<!-- <button class="button02" type="submit">OK</button> -->
			<!-- <button class="button02" id="cancelAddMachine" type="reset">Cancel</button> -->
			<a class="button02" href="javascript:document.getElementById('new_form').submit();">OK</a>
			<a class="button02b" id="cancelAddMachine" href="javascript:document.getElementById('new_form').reset();">Cancel</a>
		</div>
		</form>
	</dialog>

	<dialog id="changeMachineDialog">
	<div class="dialog_title_div01">Change Machine</div>
		<form id="update_form" method="post" action="<%=contextRoot + "/domain/machineupdate"%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" id="machine_id" name="id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input id="machine_name" type="text" name="name"></td>
				</tr>
				<tr>
					<td>IP Address:</td>
					<td><input id="machine_ip" type="text" name="ip"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<!-- <button type="submit">OK</button> -->
			<!-- <button id="cancelChangeMachine" type="reset">Cancel</button> -->
			<a class="button02" href="javascript:document.getElementById('update_form').submit();">OK</a>
			<a id="cancelChangeMachine" class="button02b" href="javascript:document.getElementById('update_form').reset();">Cancel</a>
		</div>
		</form>
	</dialog>

	<dialog id="deleteMachineDialog">
		<div class="dialog_title_div01">Delete Machine</div>
		<div class="dialog_main_div01" id="deleteMachineDialogMessageDiv">Are you sure you want to delete the machine?</div>
		<div class="dialog_button_div01">
			<!-- <button id="doDeleteMachine">OK</button> -->
			<!-- <button id="cancelDeleteMachine">Cancel</button> -->
			<a id="okDeleteMachine" class="button02">OK</a>
			<a id="cancelDeleteMachine" class="button02b">Cancel</a>
		</div>
	</dialog>

	<dialog id="deleteMachinesDialog">
		<div class="dialog_title_div01">Delete Machine</div>
		<div class="dialog_main_div01" id="deleteMachinesDialogMessageDiv">Are you sure you want to delete selected machines?</div>
		<div class="dialog_button_div01">
			<!-- <button id="doDeleteMachines">OK</button> -->
			<!-- <button id="cancelDeleteMachines">Cancel</button> -->
			<a id="okDeleteMachines" class="button02">OK</a>
			<a id="cancelDeleteMachines" class="button02b">Cancel</a>
		</div>
	</dialog>

</body>
</html>
