<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Quote_Email"
	scope="request" />
<%
	mybean.doPost(request, response);
%>

<!DOCTYPE html>
<html lang="en">
<title>AxelaAuto</title>
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">
</head>
<body>
	<center><%=mybean.msg%></center>
</body>
</html>