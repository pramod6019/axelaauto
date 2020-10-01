<jsp:useBean id="mybean" class="axela.portal.Access" scope="request" />
<%mybean.doPost(request,response); %>
<HTML>
<HEAD>
<title><%=mybean.AppName%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
	<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onLoad="FormFocus();">

	<div class="user-login-5">
		<div class="row bs-reset">
			<div class="col-md-6 bs-reset">
				<div class="login-bg">
					<img class="login-logo" src="../admin-ifx/signin-banner1.jpg"
						width="100%" />
				</div>
			</div>
			<!--                 <img src="../assets//img/logo.jpg" width="140" height="43"> -->
			<div class="col-md-6 login-container bs-reset">
				<center style="padding: 10%">
					<img src="../admin-ifx/logo.jpg" width="140" height="43">
				</center>

				<center>
					Please check your company URL. <br> please contact
					+91-9845170170 for account renewal and assistance.
				</center>
				<br> <br> <br>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript" src="../Library/jquery.js"></script>
	<script type="text/javascript" src="../Library/jquery-ui.js"></script>

	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>

</body>
</HTML>
