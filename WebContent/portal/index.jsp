<jsp:useBean id="mybean" class="axela.portal.Index" scope="request" />
<%mybean.doPost(request,response); %>
<HTML>
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp" %>
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="../assets/Login/login-4.min.css" rel="stylesheet"	type="text/css" />

<script language="JavaScript" type="text/javascript">
                    setTimeout ("changePage()", 1);
					function changePage() {
						if (self.parent.frames.length != 0)
						self.parent.location="index.jsp?msg=<%=mybean.msg%>";  
					}
					
					function FormFocus() { //v1.0
					  document.form1.userid.focus()
					}
</script>

<style>
a {
	color: black;
}

</style>

</HEAD>
<body class="login" style="color: black">
	<div class="content" style="margin-top: 5%;">
		<form name="form1" class="login-form" method="post" >
			<div class="logo">
				<a href="index.jsp"> <img src="../admin-ifx/axelaauto-logo.png" alt="Axela Auto"
					style="width: 250px; position: relative; right: 10px; opacity: 0.9" />
				</a>
			</div>
			<font color="#FF0000"><center>
					<b><%=mybean.msg%></b>
				</center></font>

			<h4 style="color: #3e67bf;" class="form-title">
				<b>Sign In</b>
			</h4>

			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">Username</label>
				<div class="input-icon">
					<i class="fa fa-envelope" style="font-size: 16px; top:-1px"></i> 
					<input
						class="form-control placeholder-no-fix" type="text"
						autocomplete="off" placeholder="Email ID" name="userid" id="userid"/>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">Password</label>
				<div class="input-icon">
					<i class="fa fa-lock" style="font-size: 20px; top:-1px"></i> <input  name="password" type="password"
						autocomplete="off" placeholder="Password"
						class="form-control form-control-solid placeholder-no-fix"
						maxlength="50" size="39" value="">
				</div>
			</div>



			<div class="form-actions">
			<input name="action" id="action" type="text" value="Sign In" hidden>
				<center>
					<button name="action1" id="action1" type="submit"
						class="btn btn-info" value="Sign In">Sign In</button>
				</center>
				<div class="col-md-12 col-xs-12">
					<div class="col-md-12 col-xs-12" style="float: left">
						<label class="rememberme check"> <input type="checkbox"
							name="remember" value="1" /> <a>Remember Me</a></label>
					</div>
				</div>

			</div>
		</form>
		<hr>
		<div class="container-fluid">
			<center>
				<div style="color: #241d1d" class="copyright">
					<h5>Emax</h5>
					&copy; 2007 - <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date())%> 
					<a style="color: #3e67bf;" href="http://www.emax.in" target="_BLANK">Emax</a>.
					All Rights Reserved.
				</div>
			</center>
		</div>

	</div>

	<br></br>

	<%@include file="../Library/js.jsp" %>
	<!-- Login4 Page	 -->
	<script src="../assets/Login/login-4.min.js" type="text/javascript"></script>
	<script src="../assets/Login/jquery.validate.min.js" type="text/javascript"></script>
	<script src="../assets/Login/additional-methods.min.js" type="text/javascript"></script>
	<script src="../assets/Login/jquery.backstretch.min.js" type="text/javascript"></script>

</body>
</HTML>

