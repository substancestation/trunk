<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.substance.api.*"%>
<%@ page import="org.orbit.substance.api.dfs.*"%>
<%@ page import="org.orbit.substance.model.dfs.*"%>
<%@ page import="org.orbit.substance.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);

	List<IndexItem> dfsIndexItems = (List<IndexItem>) request.getAttribute("dfsIndexItems");
	if (dfsIndexItems == null) {
		dfsIndexItems = new ArrayList<IndexItem>();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Files</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/admin_dfs_list.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="main_div01">
		<h2>DFS List</h2>
		<div class="top_tools_div01">
			<a class="button02" href="<%=contextRoot + "/admin/dfslist"%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="150">Id</th>
				<th class="th1" width="150">Name</th>
				<th class="th1" width="150">URL</th>
				<th class="th1" width="150">Context Root</th>
			</tr>
			<%
				if (dfsIndexItems.isEmpty()) {
			%>
			<tr>
				<td colspan="4">(n/a)</td>
			</tr>
			<%
				} else {
					for (IndexItem dfsIndexItem : dfsIndexItems) {
						String dfsId = (String)dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__ID);
						String name = (String)dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__NAME);
						String hostUrl = (String)dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__HOST_URL);
						String dfsContextRoot = (String)dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__CONTEXT_ROOT);
			%>
			<tr>
				<td class="td1"><%=dfsId%></td>
				<td class="td1"><%=name%></td>
				<td class="td1"><%=hostUrl%></td>
				<td class="td1"><%=dfsContextRoot%></td>
			</tr>
			<%
					}
				}
			%>
		</table>
	</div>

</body>
</html>
