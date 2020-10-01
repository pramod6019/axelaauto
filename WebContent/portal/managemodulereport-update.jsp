<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.ManageModuleReport_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<HTML>
<HEAD>
<title><%=mybean.AppName%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
</HEAD>

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
						<h1><%=mybean.status%> Report </h1>
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
						<li><a href="managemodulereport.jsp"> Manage Report</a> &gt;</li>
						<li><a
							href="managemodulereport-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								Report</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>
										Report
									</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.s
											</font>
										</center>
										<br />
										<form method="post" name="form1" class="form-horizontal">
											<div class="form-element6 form-element-center">
												<label>Module<font color="#ff0000">*</font>: </label>
													<select name="dr_report_module_id" id="dr_report_module_id" class="form-control">
													<option value=0>Select Module</option>
													<%=mybean.PopulateModule()%></select>
											</div>

											<div class="form-element6 form-element-center">
												<label>Report<font color="#ff0000">*</font>: </label>
													<input name="txt_report_name" id="txt_report_name"
														type="text" class="form-control"
														value="<%=mybean.report_name%>" size="90" maxlength="255" />
											</div>
											<div class="form-element6 form-element-center">
												<label>URL<font color="#ff0000">*</font>: </label>
													<input name="txt_report_url" id="txt_report_url"
													type="text" class="form-control" value="<%=mybean.report_url%>"
													size="90" maxlength="255" />
											</div>
											<div class="form-element6 form-element-center">
												<div class="form-element4">
														<label>Module Display:</label>
														<input name="ch_report_moduledisplay" type="checkbox"
														id="ch_report_moduledisplay" <%=mybean.PopulateCheck(mybean.report_moduledisplay)%> />
												</div>
											<div class="form-element4">
												<label>MIS Display: </label>
													<input name="ch_report_misdisplay" type="checkbox"
														id="ch_report_misdisplay"
														<%=mybean.PopulateCheck(mybean.report_misdisplay)%> />
											</div>
											
											<div class="form-element4">
												<label>Active:</label>
													<input name="ch_report_active" type="checkbox"
														id="ch_report_active" <%=mybean.PopulateCheck(mybean.report_active)%> />
											</div>
										</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
											<div class="form-element6">
												<label >Entry By:</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.entry_by%>" />
											</div>
											<div class="form-element6">
												<label >Entry Date:</label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>" />
											</div>
											</div>
											<% } %>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
											<div class="form-element6">
												<label >Modified By:</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>" />
											</div>
											<div class="form-element6">
												<label >Modified Date:</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>" />
											</div>
											</div>
											<%
												}
											%>
											<div class="row"></div>
												<center>
													<% if (mybean.status == ("Add")) { %>
													<input name="button" type="submit" class="btn btn-success"
														id="button" value="Add Report" /> <input type="hidden"
														name="add_button" value="Add Report">
													<% } else if (mybean.status == ("Update")) { %>
													<input type="hidden" name="update_button"
														value="Update Report"> <input name="update_button"
														type="submit" class="btn btn-success" id="button"
														value="Update Report" /> <input name="delete_button"
														type="submit" class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)" value="Delete Report" />
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
	<%@include file="../Library/admin-footer.jsp"%></body>
	<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<%@include file="../Library/js.jsp" %>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_desig_desc.focus()
	}
</script>
</HTML>
