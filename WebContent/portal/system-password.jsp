<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.System_Password"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>

	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Manage Password</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="system-config.jsp">System</a> &gt;</li>
						<li><a href="system-password.jsp">Manage Password</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Manage Password
									</div>
								</div>
								<div class="portlet-body container-fluid">
									<div class="tab-pane" id="">
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form method="post" name="frm1" class="form-horizontal">
											<div class="form-element6 form-element-center">
												<label>Your current password <font color=red>*</font>:
												</label>
													<input class="form-control" type="password"
														name="txt_oldpass" id="txt_oldpass" maxlength="20">
											</div>
											<div class="form-element6 form-element-center">
												<label >Choose a new password <font color=red>*</font>:
												</label>
													<input class="form-control" type="password" name="txt_pass"
														id="txt_pass" maxlength="20"> <span>(Combine
														8 to 20 letters and/or numbers to make your new password.)</span>
											</div>
											<div class="form-element6 form-element-center ">
												<label >Confirm your new password <font color=red>*</font>:
												</label>
													<input class="form-control" type="password"
														name="txt_pass1" id="txt_pass1" maxlength="20"> <span>(Please
														retype your new password.)</span>
											</div>
											<div class="form-element6 form-element-center">
												<center>
													<input name="update_button" type="submit"
														class="btn btn-success" id="update_button"
														value="Update Password" />
												</center>
											</div>
										</form>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
</body>
</HTML>
