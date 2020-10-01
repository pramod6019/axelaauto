<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.ddmotors_app.Success" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html>

<html lang="en" class="no-js">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">

<link href="../ddmotors-assets/css/bootstrap.min.css" rel="stylesheet"
	type="text/css">
<link href="../ddmotors-assets/css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">
<link href="../ddmotors-assets/css/default.css" rel="stylesheet" type="text/css"
	id="style_color">

<script src="../ddmotors-assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/bootstrap.min.js" type="text/javascript"></script>
<style>

center {
	color: #0f4c75;    
}
</style>
</head>
<body>
	<div class="container">
		<br><br>
		<center><i><b><%=mybean.msg %></b></i></center>

	</div>

	
</body>
<!-- END BODY -->
</html>