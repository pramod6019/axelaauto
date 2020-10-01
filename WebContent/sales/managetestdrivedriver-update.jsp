<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.sales.ManageTestDriveDriver_Update" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</head>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">

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
						<h1><%=mybean.status%>&nbsp;Test Drive Driver
						</h1>
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
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managetestdrivedriver.jsp?all=yes">List Test Drive Drivers</a> &gt;</li>
							<li><a href="managetestdrivedriver-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Test Drive Driver</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%><br> </b></font>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Test Drive Driver
									</div>
								</div>
								
								<div class="container-fluid portlet-body">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.<br>
											</font>
										</center>

										<form name="form1" method="post" class="form-horizontal">

											<div class="form-element6 form-element-center">
												<label> Name<font color=red>*</font>: </label>
												<input name="txt_driver_name" type="text"
													class="form-control" id="txt_driver_name"
													value="<%=mybean.driver_name%>" size="50" maxlength="255" />
											</div>

											<div class="form-element6 form-element-center">
												<label> Active: </label> 
												<input name="ch_driver_active"
													type="checkbox" id="ch_driver_active"
													<%=mybean.PopulateCheck(mybean.driver_active)%> />
											</div>


											<% if (mybean.status.equals("Update") && (mybean.entry_by != null) && !(mybean.entry_by.equals(""))) { %>
											<div class="form-element6 form-element-center">
												<div class="form-element6 form-element">
													<label> Entry By: </label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>">
												</div>

												<div class="form-element6 ">
													<label> Entry Date: </label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>">
												</div>
											</div>
											
											<% } %>

											<% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
											
											<div class="form-element6 form-element-center">
												<div class="form-element6 form-element">
													<label> Modified By: </label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>">
												</div>

												<div class="form-element6">
													<label> Modified Date: </label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
												</div>
											</div>

											<% } %>
											
											<div class="form-element12"></</div>

											<center>

												<% if (mybean.status.equals("Add")) { %>

												<input name="button" type="submit" class="btn btn-success"
													id="button"
													onClick="return SubmitFormOnce(document.form1, this);"
													value="Add Driver" /> <input type="hidden"
													name="add_button" value="yes">

												<% } else if (mybean.status.equals("Update")) { %>

												<input type="hidden" name="update_button" value="yes">
												<input name="button" type="submit" class="btn btn-success"
													id="button"
													onClick="return SubmitFormOnce(document.form1, this);"
													value="Update Driver" /> <input name="delete_button"
													type="submit" class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Driver" />

												<% } %>

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
	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
            function FormFocus() {
                document.form1.txt_driver_name.focus();
            }
        </script>
</body>
</html>
