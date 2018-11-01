<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.service.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.substance.api.*"%>
<%@ page import="org.orbit.substance.api.dfs.*"%>
<%@ page import="org.orbit.substance.model.dfs.*"%>
<%@ page import="org.orbit.substance.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.SUBSTANCE__WEB_CONSOLE_CONTEXT_ROOT);
	
	List<IndexItem> dfsIndexItems = (List<IndexItem>) request.getAttribute("dfsIndexItems");
	if (dfsIndexItems == null) {
		dfsIndexItems = new ArrayList<IndexItem>();
	}

	Map<String, DfsServiceMetadata> dfsIdToServiceMetadata = (Map<String, DfsServiceMetadata>) request.getAttribute("dfsIdToServiceMetadata");
	Map<String, PlatformServiceMetadata> dfsIdToPlatformMetadata = (Map<String, PlatformServiceMetadata>) request.getAttribute("dfsIdToPlatformMetadata");

	if (dfsIdToServiceMetadata == null) {
		dfsIdToServiceMetadata = new HashMap<String, DfsServiceMetadata>();
	}
	if (dfsIdToPlatformMetadata == null) {
		dfsIdToPlatformMetadata = new HashMap<String, PlatformServiceMetadata>();
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Substance</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/admin_dfs_list_v0.js"%>" defer></script>

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
				<th class="th1" width="100">JVM</th>
				<th class="th1" width="100">Id</th>
				<th class="th1" width="100">Name</th>
				<th class="th1" width="200">URL</th>
				<th class="th1" width="100">Status</th>
				<th class="th1" width="200">Metadata</th>
				<th class="th1" width="100">Action</th>
			</tr>
			<%
				if (dfsIndexItems.isEmpty()) {
			%>
			<tr>
				<td colspan="7">(n/a)</td>
			</tr>
			<%
				} else {
					for (IndexItem dfsIndexItem : dfsIndexItems) {
						String dfsId = (String)dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__ID);
						String name = (String)dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__NAME);
						String hostUrl = (String)dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__HOST_URL);
						String dfsContextRoot = (String)dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__CONTEXT_ROOT);
						String dfsServiceUrl = WebServiceAwareHelper.INSTANCE.getURL(hostUrl, dfsContextRoot);

						boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsIndexItem);
						String statusText = isOnline ? "Online" : "Offline";
						String statusColor = isOnline ? "#2eb82e" : "#cccccc";

						String metadataStr = "";
						String propStr = "";
						DfsServiceMetadata serviceMetadata = dfsIdToServiceMetadata.get(dfsId);
						if (serviceMetadata != null) {
							String currDfsId = serviceMetadata.getDfsId();
							long currDefaultBlockCapacity = serviceMetadata.getDataBlockCapacity();
							Map<String, Object> metadataProperties = serviceMetadata.getProperties();

							String currName = serviceMetadata.getName();
							String currHostUrl = serviceMetadata.getHostURL();
							String currContextRoot = serviceMetadata.getContextRoot();
							long currServerTime = serviceMetadata.getServerTime();
							String currServerTimeStr = DateUtil.toString(DateUtil.toDate(currServerTime), DateUtil.SIMPLE_DATE_FORMAT2);

							// metadataStr += "dfs_id = " + currDfsId + "<br/>";
							metadataStr += "block_capacity = " + DiskSpaceUtil.formatSize(currDefaultBlockCapacity) + "<br/>";
							// metadataStr += "name = " + currName + "<br/>";
							// metadataStr += "host_url = " + currHostUrl + "<br/>";
							// metadataStr += "context_root = " + currContextRoot + "<br/>";
							metadataStr += "server_time = " + currServerTimeStr + "<br/>";

							if (false && !metadataProperties.isEmpty()) {
								for (Iterator<String> propItor = metadataProperties.keySet().iterator(); propItor.hasNext();) {
									String propName = propItor.next();
									Object propValue = serviceMetadata.getProperty(propName);

									if (propValue != null) {
										if ("server_time".equals(propName)) {
											propValue = DateUtil.toString(DateUtil.toDate(Long.valueOf(propValue.toString())), DateUtil.SIMPLE_DATE_FORMAT2);
											
										} else if ("block_capacity".equals(propName)) {
											propValue = DiskSpaceUtil.formatSize(Long.valueOf(propValue.toString()));
										}

										propStr += propName + " = " + propValue + "<br/>";
									}
								}
							}
						}
						
						String jvmName = null;
						PlatformServiceMetadata platformMetadata = dfsIdToPlatformMetadata.get(dfsId);
						if (platformMetadata != null) {
							jvmName = platformMetadata.getJvmName();
						}
						if (jvmName == null) {
							jvmName = "";
						}
			%>
			<tr>
				<td class="td1"><%=jvmName%></td>
				<td class="td1"><%=dfsId%></td>
				<td class="td1"><%=name%></td>
				<td class="td1"><%=dfsServiceUrl%></td>
				<td class="td1" width="100"><font color="<%=statusColor%>"><%=statusText%></font></td>
				<td class="td2"><%=metadataStr%></td>
				<td class="td1">
					<a class="action01" href="<%=contextRoot%>/admin/dfsvolumelist?dfsId=<%=dfsId%>">DFS Volumes</a>
				</td>
			</tr>
			<%
					}
				}
			%>
		</table>
	</div>
	<br/>
</body>
</html>
