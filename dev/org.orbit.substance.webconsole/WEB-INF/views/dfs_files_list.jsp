<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.origin.common.resource.*"%>
<%@ page import="org.orbit.substance.api.dfs.*"%>
<%@ page import="org.orbit.substance.model.dfs.*"%>
<%@ page import="org.orbit.substance.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);

	String parentFileId = (String) request.getAttribute("parentFileId");
	List<FileMetadata> parentFiles = (List<FileMetadata>) request.getAttribute("parentFiles");
	FileMetadata[] files = (FileMetadata[]) request.getAttribute("files");
	String grandParentFileId = (String) request.getAttribute("grandParentFileId");

	if (parentFileId == null || parentFileId.isEmpty()) {
		parentFileId = "-1";
	}
	if (parentFiles == null) {
		parentFiles = new ArrayList<FileMetadata>();
	}
	if (files == null) {
		files = new FileMetadata[0];
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

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot + "/files"%>">Files</a> / 
		<%
			for (FileMetadata currParentFile : parentFiles) {
				String currParentFileId = currParentFile.getFileId();
				String currParentFileName = currParentFile.getName();
		%>
			<a href="<%=contextRoot + "/files/configelements?parentFileId=" + currParentFileId%>"><%=currParentFileName%></a> /
		<%
			}
		%>
	</div>

	<div class="main_div01">
		<h2>Files</h2>
		<div class="top_tools_div01">
			<a id="actionCreateDirectory" class="button02">New Directory</a>
			<a id="actionUploadFile" class="button02">Upload File</a>
			<a id="actionDeleteFiles" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/files?parentFileId=" + parentFileId%>">Refresh</a>
			<% if (grandParentFileId != null) { %>
				<a class="button02" href="<%=contextRoot + "/files?parentFileId=" + grandParentFileId%>">Up</a>
			<% } %>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="<%=contextRoot + "/filedelete"%>">
			<input type="hidden" name="parentFileId" value="<%=parentFileId%>" />
			<tr>
				<th class="th1" width="10">
					<input type="checkbox" onClick="toggleSelection(this, 'fileId')" />
				</th>
				<th class="th1" width="400">Path</th>
				<th class="th1" width="60">Directory</th>
				<th class="th1" width="60">Size</th>
				<th class="th1" width="60">Date Modified</th>
				<th class="th1" width="60">Actions</th>
			</tr>
			<%
				if (files.length == 0) {
			%>
			<tr>
				<td colspan="6">(n/a)</td>
			</tr>
			<%
				} else {
					for (FileMetadata file : files) {
						String fileId = file.getFileId();
						String currParentFileId = file.getParentFileId();
						String fileName = file.getName();
						Path path = file.getPath();
						boolean isDirectory = file.isDirectory();
						// boolean isHidden = file.isHidden();
						long size = file.getSize();
						long dateCreated = file.getDateCreated();
						long dateModified = file.getDateModified();

						String pathStr = (path != null) ? path.getPathString() : "";
						String sizeStr = DiskSpaceUtil.formatSize(size);
						String dateCreatedStr = DateUtil.toString(dateCreated, DateUtil.SIMPLE_DATE_FORMAT2);
						String dateModifiedStr = DateUtil.toString(dateCreated, DateUtil.SIMPLE_DATE_FORMAT2);
			%>
			<tr>
				<td class="td1">
					<input type="checkbox" name="fileId" value="<%=fileId%>">
				</td>
				<td class="td2">
					<% if (isDirectory) {%>
						<a class="action01" href="<%=contextRoot%>/files?parentFileId=<%=fileId%>" ><%=fileName%></a>
					<% } else { %>
						<% if (size > 0) { %>
							<a class="action01" href="<%=contextRoot%>/filedownload?fileId=<%=fileId%>" target="_blank"><%=fileName%></a>
						<% } else { %>
							<%=pathStr%>
						<% } %>
					<% } %>
				</td>
				<td class="td1"><%=isDirectory%></td>
				<td class="td1">
					<% if (!isDirectory) { %>
						<%=sizeStr%>
					<% } %>
				</td>
				<td class="td1"><%=dateModifiedStr%></td>
				<td class="td1">
					<% if (!isDirectory) { %>
					<a class="action01" target="_blank" href="<%=contextRoot + "/filecontent?fileId=" + fileId%>">View text content</a>
					<% } %>
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
		<input type="hidden" name="parentFileId" value="<%=parentFileId%>" />
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<input type="file" name="uploadFile" />
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okUploadFile" class="button02">OK</a>
			<a id="cancelUploadFile" class="button02b">Cancel</a>
		</div>
		</form>
	</dialog>

	<dialog id="createDirectoryDialog">
	<div class="dialog_title_div01">New Directory</div>
		<form id="new_form" method="post" action="<%=contextRoot + "/mkdir"%>">
		<input type="hidden" name="parentFileId" value="<%=parentFileId%>" />
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a class="button02" id="okCreateDirectory">OK</a>
			<a class="button02b" id="cancelCreateDirectory">Cancel</a>
		</div>
		</form>
	</dialog>

	<dialog id="deleteFilesDialog">
		<div class="dialog_title_div01">Delete Files</div>
		<div class="dialog_main_div01" id="deleteFilesDialogMessageDiv">Are you sure you want to delete selected files?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteFiles" class="button02">OK</a>
			<a id="cancelDeleteFiles" class="button02b">Cancel</a>
		</div>
	</dialog>

</body>
</html>
