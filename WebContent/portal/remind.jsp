<jsp:useBean id="mybean" class="axela.portal.Remind" scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html>
<html lang="en">
<head>

<title><%=mybean.AppName%> - <%=mybean.ClientName%></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport" />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript">
setTimeout ("changePage()", 1);
function changePage() {
if (self.parent.frames.length != 0)
self.parent.location="remind.jsp";
}

function FormFocus() { //v1.0
  document.frm_login.emailid.focus()
}

</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<!-- END HEAD -->

<body class="login">

	<!-- BEGIN LOGIN -->
	<div class="content">
		<div class="logo" style="position: relative; right: 10px;">
			<a href="../test/index.jsp"> 
				<img src="../admin-ifx/emax-logo.jpg" alt="" />
			</a>
		</div>
<!-- 		<p> -->
<!-- 			Please enter your email address below and your password will be sent -->
<!-- 			to you by email.<br> For security purposes, we encourage you to -->
<!-- 			consider resetting your password once you have logged back into your -->
<!-- 			account. -->
<!-- 		</p> -->
		<form class="login-form" method="post">
			<font color="#FF0000"><center>
					<b><%=mybean.msg%></b>
				</center></font>
			<h3 class="font">Forgot Password ?</h3>
			<p>Enter your e-mail address below to reset your password.</p>
			<div class="form-group">
				<input name="emailid" class="form-control placeholder-no-fix"
					type="text" placeholder="Email" id="emailid" value="Enter Email"
											onFocus="if(this.value == 'Enter Email') { this.value = ''; }"
											onBlur="if(this.value == '') { this.value = 'Enter Email'; }"/>
			</div>
			<div class="form-actions">
				<a href="index.jsp"><button type="button" id="back-btn"
						class="btn btn-success">Back</button></a> <a href="">
					<button name="submit_button" id="submit_button" type="submit"
						class="btn btn-success uppercase pull-right" value="Submit">Submit</button>
				</a>
			</div>

			<p>
				If you have trouble signing in or need help, <br> please call
				+91 9845 170 170 for assistance.
			</p>
		</form>
	</div>
	<div class="copyright">
		<h5>Emax</h5>
		&copy; 2007 - <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date())%> <a href="www.emax.in" target="_BLANK">Emax</a>. All
		Rights Reserved.
	</div>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
</body>

</html>