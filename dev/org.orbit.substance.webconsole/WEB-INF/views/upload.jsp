<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="org.orbit.substance.webconsole.*"%>
<%
	String contextRoot = getServletConfig().getInitParameter(WebConstants.DFS__WEB_CONSOLE_CONTEXT_ROOT);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>File Upload Demo</title>
</head>
<body>
    <center>
        <form method="post" action="<%=contextRoot%>/upload" enctype="multipart/form-data">
            Select file to upload:
            <input type="file" name="uploadFile" /><br/><br/>
            <input type="submit" value="Upload" />
        </form>
    </center>
</body>
</html>
