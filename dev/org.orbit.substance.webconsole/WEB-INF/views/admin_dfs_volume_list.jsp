<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.service.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.substance.api.*"%>
<%@ page import="org.orbit.substance.api.dfs.*"%>
<%@ page import="org.orbit.substance.api.dfsvolume.*"%>
<%@ page import="org.orbit.substance.model.dfs.*"%>
<%@ page import="org.orbit.substance.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);

	String dfsId = (String) request.getAttribute("dfsId");

	String dfsName = null;
	IndexItem dfsIndexItem = (IndexItem) request.getAttribute("dfsIndexItem");
	if (dfsIndexItem != null) {
		dfsName = (String) dfsIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS__NAME);
	}

	List<IndexItem> dfsVolumeIndexItems = (List<IndexItem>) request.getAttribute("dfsVolumeIndexItems");
	if (dfsVolumeIndexItems == null) {
		dfsVolumeIndexItems = new ArrayList<IndexItem>();
	}

	Map<String, DfsVolumeMetadata> dfsVolumeIdToServiceMetadata = (Map<String, DfsVolumeMetadata>) request.getAttribute("dfsVolumeIdToServiceMetadata");
	Map<String, PlatformMetadata> dfsVolumeIdToPlatformMetadata = (Map<String, PlatformMetadata>) request.getAttribute("dfsVolumeIdToPlatformMetadata");

	if (dfsVolumeIdToServiceMetadata == null) {
		dfsVolumeIdToServiceMetadata = new HashMap<String, DfsVolumeMetadata>();
	}
	if (dfsVolumeIdToPlatformMetadata == null) {
		dfsVolumeIdToPlatformMetadata = new HashMap<String, PlatformMetadata>();
	}

	String dfsLabel = (dfsName != null) ? dfsName : dfsId;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>File System</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/admin_dfs_list.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/admin/dfslist">DFS List</a> >
		<%=dfsLabel%>
	</div>

	<div class="main_div01">
		<h2>DFS Volumes</h2>
		<div class="top_tools_div01">
			<a class="button02" href="<%=contextRoot + "/admin/dfsvolumelist?dfsId=" + dfsId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="100">JVM</th>
				<th class="th1" width="100">Volume Id</th>
				<th class="th1" width="100">Name</th>
				<th class="th1" width="200">URL</th>
				<th class="th1" width="100">Status</th>
				<th class="th1" width="250">Metadata</th>
			</tr>
			<%
				if (dfsVolumeIndexItems.isEmpty()) {
			%>
			<tr>
				<td colspan="6">(n/a)</td>
			</tr>
			<%
				} else {
					for (IndexItem dfsVolumeIndexItem : dfsVolumeIndexItems) {
						String theDfsId = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__DFS_ID);
						String dfsVolumeId = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__ID);
						String name = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__NAME);
						String hostUrl = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__HOST_URL);
						String dfsVolumeContextRoot = (String) dfsVolumeIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DFS_VOLUME__CONTEXT_ROOT);
						String dfsVolumeServiceUrl = WebServiceAwareHelper.INSTANCE.getURL(hostUrl, dfsVolumeContextRoot);

						boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dfsVolumeIndexItem);
						String statusText = isOnline ? "Online" : "Offline";
						String statusColor = isOnline ? "#2eb82e" : "#cccccc";

						String metadataStr = "";
						String propStr = "";

						DfsVolumeMetadata serviceMetadata = dfsVolumeIdToServiceMetadata.get(dfsVolumeId);
						if (serviceMetadata != null) {
							String currDfsId = serviceMetadata.getDfsId();
							String currDfsVolumeId = serviceMetadata.getDfsVolumeId();
							long currVolumeCapacity = serviceMetadata.getVolumeCapacity();
							long currVolumeSize = serviceMetadata.getVolumeSize();
							Map<String, Object> metadataProperties = serviceMetadata.getProperties();

							String currName = serviceMetadata.getName();
							String currHostUrl = serviceMetadata.getHostURL();
							String currContextRoot = serviceMetadata.getContextRoot();
							long currServerTime = serviceMetadata.getServerTime();
							String currServerTimeStr = DateUtil.toString(DateUtil.toDate(currServerTime), DateUtil.SIMPLE_DATE_FORMAT2);

							// metadataStr += "dfs_id = " + currDfsId + "<br/>";
							// metadataStr += "dfs_volume_id = " + currDfsVolumeId + "<br/>";
							metadataStr += "volume_capacity = " + DiskSpaceUtil.formatSize(currVolumeCapacity) + "<br/>";
							metadataStr += "volume_size = " + DiskSpaceUtil.formatSize(currVolumeSize) + "<br/>";
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

										} else if ("volume_capacity".equals(propName) || "volume_size".equals(propName)) {
											propValue = DiskSpaceUtil.formatSize(Long.valueOf(propValue.toString()));
										}

										propStr += propName + " = " + propValue + "<br/>";
									}
								}
							}
						}

						String jvmName = null;
						PlatformMetadata platformMetadata = dfsVolumeIdToPlatformMetadata.get(dfsVolumeId);
						if (platformMetadata != null) {
							jvmName = platformMetadata.getJvmName();
						}
						if (jvmName == null) {
							jvmName = "";
						}
			%>
			<tr>
				<td class="td1"><%=jvmName%></td>
				<td class="td1"><%=dfsVolumeId%></td>
				<td class="td1"><%=name%></td>
				<td class="td1"><%=dfsVolumeServiceUrl%></td>
				<td class="td1" width="100"><font color="<%=statusColor%>"><%=statusText%></font></td>
				<td class="td2"><%=metadataStr%></td>
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
