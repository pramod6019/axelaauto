<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.User_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp" %>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.formuser.txt_user_fname.focus();
	}
</script>
 
</head>
<body onLoad="FormFocus();"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
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
						<h1><%=mybean.status%> Service User</h1>
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
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href=../app/home.jsp>App</a> &gt;</li>
						<li><a href=../app/servicebooking-list.jsp?all=yes>List Service Booking</a> &gt;</li>
						<li><a href=../portal/user-list.jsp?all=yes>List Users</a> &gt;</li>
						<li><a href="../portal/user-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								Service User</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>
										Service User
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="formuser" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                                                                </font><br>
											</center>

											<div class="form-element12 form-element-center">
												<div class="form-element2">
													<label> User Name<font color=red>*</font>: </label>
															<select name="dr_title" class="form-control"
																	id="dr_title">
																		<%=mybean.PopulateTitle()%>
																</select> Title
												</div>
												<div class="form-element5">
													<label>&nbsp;</label>
														<input name="txt_user_fname" type="text"
																class="form-control" id="txt_user_fname"
																value="<%=mybean.user_fname%>" size="35"
																maxlength="255" />First Name
												</div>
												<div class="form-element5">
													<label>&nbsp;</label>
														<input name="txt_user_lname" type="text"
																class="form-control" id="txt_user_lname"
																value="<%=mybean.user_lname%>" size="35"
																maxlength="255" />Last Name
												</div>
											
											<div class="row"></div>
												<div class="form-element6">
													<label> Mobile<font color=red>*</font>: </label>
														<input name="txt_user_mobile" type="text"
															class="form-control" id="txt_user_mobile"
															value="<%=mybean.user_mobile%>" size="32"
															maxlength="13" 
															onKeyUp="toPhone('txt_user_mobile','Mobile')"/> (91-9999999999)
												</div>
												<div class="form-element6">
													<label> Email: </label>
														<input name="txt_user_email" type="text"
															class="form-control" id="txt_user_email"
															value="<%=mybean.user_email%>" size="35"
															maxlength="255">
												</div>
												<div class="row"></div>
												<div class="form-element6">
												<label>Active:</label>
													<input id="chk_user_active" type="checkbox"
														name="chk_user_active"
														<%=mybean.PopulateCheck(mybean.user_active)%> />
											</div>
										</div>		
											<div class="row"></div>
											<center>
												<% if (mybean.status.equals("Update")) {
												%>
												<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													onClick="return SubmitFormOnce(document.formuser, this);"
													value="Update Contact Person" /> <input type="hidden"
													name="update_button" value="yes">
												<%
													}
												%>
											</center>
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp" %>
</body>
</html>
