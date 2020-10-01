<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.ManageDepartment_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</head>
<body class="page-container-bg-solid page-header-menu-fixed"
	onLoad="FormFocus()">
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
						<h1><%=mybean.status%>&nbsp;Department
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
						<!-- BEGIN PAGE BREADCRUMBS -->
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="home.jsp">Home</a> &gt;</li>
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managedepartment.jsp?all=yes">List
									Departments</a> &gt;</li>
							<li><a
								href="managedepartment-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Department</a>:</li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>
						<div class="potlet-box">
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Department
									</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form method="post" name="form1" class="form-horizontal">
											<div class="form-element6 form-element-center">
												<label>Name<font color="#ff0000">*</font>:
												</label> <input name="txt_department_name" id="txt_department_name"
													class="form-control" value="<%=mybean.department_name%>"
													size="50" maxlength="255" />
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
												<div class="form-element6">
													<label>Entry By:</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.entry_by%>" />
													<%
												}
											%>
												</div>
												<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
												<div class="form-element6">
													<label>Entry Date:</label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>" />
												</div>
												<%
												}
											%>
												<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
												<div class="form-element6">
													<label>Modified By:</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>" />
												</div>
												<%
												}
											%>
												<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
												<div class="form-element6">
													<label>Modified Date:</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>" />
												</div>
												<%
												}
											%>
												<div class="row"></div>
												<center>
													<%
														if (mybean.status.equals("Add")) {
													%>
													<input name="button" type="submit" class="btn btn-success"
														id="button" value="Add Department"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" name="add_button" value="yes">
													<%
															} else if (mybean.status.equals("Update")) {
														%>
													<input type="hidden" name="update_button" value="yes">
													<input name="button" type="submit" class="btn btn-success"
														id="button" value="Update Department"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input name="delete_button" type="submit"
														class="btn btn-success" id="delete_button"
														OnClick="return confirmdelete(this)"
														value="Delete Department" />
													<%	} %>

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
	<script type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_department_name.focus()
	}
	</script>
</body>
</HTML>
