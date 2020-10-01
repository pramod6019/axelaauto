<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Success"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en" class="no-js">
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<style>
center {
	color: #8E44AD;
	margin-top: 250px;
}
</style>
</head>
<body>
	<div class="container">
		<center>
			<b>Access denied. Please contact system administrator!</b>
		</center>
	</div>
</body>
<!-- END BODY -->
</html>