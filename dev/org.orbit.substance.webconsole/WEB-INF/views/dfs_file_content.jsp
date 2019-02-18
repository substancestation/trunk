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

	String fileId = (String) request.getAttribute("fileId");
	String fileName = (String) request.getAttribute("fileName");
	String fileContent = (String) request.getAttribute("fileContent");

	if (fileId == null) {
		fileId = "";
	}
	if (fileName == null) {
		fileName = "";
	}
	if (fileContent == null) {
		fileContent = "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>File Content</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="main_div01">
		<h2>File Content</h2>
		<div class="top_tools_div01">
			<a class="button02" href="<%=contextRoot + "/filecontent?fileId=" + fileId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<td class="td2">
					<%=fileName%>
				</td>
			</tr>
			<tr>
				<td class="td2">
					<textarea rows="20" cols="200" readonly><%=fileContent%></textarea>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
