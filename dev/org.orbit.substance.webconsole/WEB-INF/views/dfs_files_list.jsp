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

	org.orbit.substance.api.dfs.File[] files = (org.orbit.substance.api.dfs.File[]) request.getAttribute("files");
	if (files == null) {
		files = new org.orbit.substance.api.dfs.File[0];
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Files</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/dfs_files.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="main_div01">
		<h2>Files</h2>
		<div class="top_tools_div01">
			<a id="action.addMachine" class="button02">Add</a>
			<a id="action.deleteMachines" class="button02">Delete</a>
			<a id="actionUploadFile" class="button02">Upload</a>
			<a class="button02" href="<%=contextRoot + "/files"%>">Refresh</a>
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

	<dialog id="uploadFileDialog">
	<div class="dialog_title_div01">Upload File</div>
		<form id="upload_form" method="post" action="<%=contextRoot + "/fileupload"%>" enctype="multipart/form-data">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<input type="file" name="uploadFile" />
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a class="button02" href="javascript:document.getElementById('upload_form').submit();">OK</a>
			<a id="cancelUploadFile" class="button02b">Cancel</a>
		</div>
		</form>
	</dialog>

</body>
</html>
