<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Preownedstock_Details"
	scope="request" />
<%
	mybean.doPost(request, response);
%>

<!DOCTYPE html>

<html lang="en">
<title>AxelaAuto</title>
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">
<link href="css/default.css" rel="stylesheet" type="text/css" id="style_color">
<style>
b {
	color: #8E44AD;
}

.container {
	padding-right: 15px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: auto;
}

.con {
	margin-top: 40px;
}

.panel-heading {
	margin-bottom: 20px;
	background-color: #8E44AD;
	border: 1px solid transparent;
	border-radius: 0px;
	box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}

strong {
	color: #fff;
}

.header-wrap {
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
}

.loader {
	display: none;
	position: fixed;
	left: 0px;
	top: 0px;
	width: 100%;
	height: 100%;
	bottom: 3px;
	background: url('../admin-ifx/loading.gif') 50% 100% no-repeat;
}

.btn {
	background-color: #fff;
}
</style>

</head>

<body>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title">
				<center>
					<strong>Stocks Details</strong>
				</center>
			</span>
		</div>
	</div>
	<h4>&nbsp;</h4>

	<div id="container">
		<div class="col-md-12 col-xs-12" style="margin-top: 30px">

			<form class="form-horizontal" role="form">
				<div class="form-body">
				
					<%=mybean.StrHTML%>

				</div>
			</form>

		</div>
	</div>
<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>
</body>
<!-- END BODY -->
</html>