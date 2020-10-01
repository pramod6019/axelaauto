<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Executive_Update_Password"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
</head>
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
						<h1>Update Executive Password</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<%
							if (mybean.redirect.equals("1")) {
						%>
						<li><a href="executives.jsp">Executives</a> &gt;</li>
						<li><a href="executive-list.jsp?all=yes">List Executives</a>&gt;</li>
						<li><a
							href="executive-update-password.jsp?emp_id=<%=mybean.emp_id%>&redirect=<%=mybean.redirect%>">
								Update Executive Password</a>:</li>
						<%
							} else if (mybean.redirect.equals("2")) {
						%>
						<li><a href="exe.jsp">Executives</a> &gt;</li>
						<li><a href="exe-list.jsp?all=yes">List Executives</a>&gt;</li>
						<li><a
							href="executive-update-password.jsp?emp_id=<%=mybean.emp_id%>&redirect=<%=mybean.redirect%>">
								Update Executive Password</a><b>:</b></li>
						<%
							}
						%>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Update Executive
										Password</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font  size="1">Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required. </font>
										</center>
										<br />
										<form method="post" name="frm1" class="form-horizontal">
											<div class="form-element6 form-element-center form-element">
												<div class="form-element6">
												<label>Executive Name:</label>
														<%=mybean.emp_name%>
												</div>
											<div class="form-element6">
												<label>Email1:</label>
														<%=mybean.emp_email1%>
												</div>
											</div>
											<div class="form-element6 form-element-center">
												<label>Choose a new password <font color=red>*</font>: </label>
													<input class="form-control" type="text" name="txt_pass"
														id="txt_pass" value="<%=mybean.emp_upass%>" maxlength="20"> <span>(Combine
														8 to 20 letters and/or numbers to make your new password.)</span>
											</div>

											<div class="form-group">
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
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp" %>
</body>
</HTML>
