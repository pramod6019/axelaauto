<%@ page isErrorPage="true" import="java.io.*" %>
<jsp:useBean id="mybean" class="cloudify.connect.Connect" scope="request"/>
<html>
<head>
	<title>Exceptional Even Occurred!</title>
	<style>
	body, p { font-family:Tahoma; font-size:10pt; padding-left:30; }
	pre { font-size:8pt; }
	</style>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body>

<font color="red">
<%if(mybean.AppRun().equals("0")) {%>
<%= exception.toString() %><%}%><br>
</font>
<%if(mybean.AppRun().equals("1")) {%>
<%
out.println("An error has occured.<br>The solution provider has been notified.");
%><%}%>
<%= exception.toString() %>

</body>
</html>