<%-- <%@ page errorPage="../axelaauto-app/app-error-page.jsp"%> --%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Error"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html lang="en">
<head>

<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">

<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js"></script>
<script type="text/javascript" src="../Library/jquery.js"></script>
<script type="text/javascript" src="../Library/jquery-ui.js"></script>
<script type="text/javascript"
	src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>

</head>
<!-- ----- -->
<body>
<br><br><br><br><br>
<div style="color:red" align=center><%=mybean.msg %></div>
</body>
</html>
