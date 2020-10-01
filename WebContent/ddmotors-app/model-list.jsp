<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.ddmotors_app.Model_List"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en" class="no-js">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">

<link href="../ddmotors-assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="../ddmotors-assets/css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">

<script src="../ddmotors-assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/bootstrap.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/mobilecall.js" type="text/javascript"></script>
<style>
.row{
	border: 1px solid #888888;
	padding: 5px 0px;
}

b {
	color: #000000;
/* 	position: relative; */
/*  	top: 10px;  */
}
</style>
</head>
<body <%if(!mybean.msg.equals("")) {%>onload="showToast('<%=mybean.msg%>')"<%} %>>

	<div class="container">
		&nbsp;
		<%=mybean.StrHTML%>

	</div>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>