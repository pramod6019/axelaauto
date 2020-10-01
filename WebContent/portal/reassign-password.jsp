<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Reassign_Password"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>

<script type="text/javascript">
function validatePassword(){
	var newpasswd = document.getElementById("txt_new_password").value;
	var confirmpasswd = document.getElementById("txt_confirm_password").value;
	if(newpasswd!=confirmpasswd){
		document.getElementById("passwd").innerHTML = "Passwords do not match";
	}else{
		document.getElementById("passwd").innerHTML = "";
	}
}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body class="page-container-bg-solid page-boxed page-header-menu-fixed">
	<%@include file="header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Re-assign Password</h1>
					</div>
					<!-- END PAGE TITLE -->

				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- BEGIN PAGE BREADCRUMBS -->
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="system-config.jsp">System</a> &gt;</li>
						<li><a href="reassign-password.jsp">Re-assign Password</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

				</div>
				<!-- START CHANGE PASSWORD -->
				<div class="container-fluid">
					<div class="col-md-12 col-xs-12">
						<center>
							<font color="#FF0000"><b><%=mybean.msg%></b></font>
						</center>
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Reset your
									password</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<center>
										<p>
											Enter a new password for <b><%=mybean.emp_email%></b>. We
											highly recommend you create a unique password - one that you
											do not use for any other websites.
										</p>
									</center>
									<center>
										Form fields marked with a red asterisk <font color=#ff0000><b>*</b></font>
										are required.
									</center>
									<br />
										<form class="form-horizontal" role="form" id="frm1">

											<div class="form-group">
												<label class="col-md-4 control-label">Choose a new
													password :</label>
												<div class="col-md-6">
													<input name="txt_new_password" id="txt_new_password"
														type="password" class="form-control">
													<div>(Combine 8 to 20 letters and/or numbers to make
														your new password.)</div>
												</div>
											</div>
											<div class="form-group">
												<label class="col-md-4 control-label">Confirm your
													new password :</label>
												<div class="col-md-6">
													<input name="txt_confirm_password" type="password"
														id="txt_confirm_password" class="form-control"
														onKeyUp="validatePassword();"><span id="passwd"></span>
													<div>(Please retype your new password.)</div>
												</div>
											</div>
											<div class="form-group">
												<center>
													<button name="submit_button" type="submit"
														class="btn btn-success" id="submit_button"
														value="Continue">Continue</button>
													<button name="cancel_button" type="submit"
														class="btn btn-success" id="cancel_button" value="Cancel">Cancel</button>
												</center>
											</div>
											</form>
											
										
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- END CHANGE PASSWORD -->
			</div>
			<!-- END PAGE CONTENT BODY -->
			<!-- END CONTENT BODY -->
		</div>
		<!-- END CONTENT -->
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
</body>
</HTML>
