<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Executive_Access"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<style>
#display-line {
	display: inline-block;
	min-width: 100px;
}
/* // */
</style>
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
						<h1>Access Rights</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY --------->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="home.jsp">Home</a> &gt;</li>
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="../portal/executive-list.jsp?all=yes">List
									Executives</a> &gt;</li>
							<li><a
								href="../portal/executive-list.jsp?emp_id=<%=mybean.emp_id%>"><%=mybean.emp_name%></a>
								&gt;</li>
							<li><a>Access Rights</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<strong><%=mybean.RecCountDisplay%></strong>
						</center>
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Access Rights</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<form name="formemp" method="post" class="form-horizontal">

										<div class="container-fluid ">
												<div class="form-element3">
													<label>MIS: </label> <input type="checkbox"
														name="ch_emp_mis_access"
														<%=mybean.PopulateCheck(mybean.emp_mis_access)%> />
												</div>
												<div class="form-element3">
													<label>Export: </label> <input type="checkbox"
														name="ch_emp_export_access"
														<%=mybean.PopulateCheck(mybean.emp_export_access)%> />
												</div>
												<div class="form-element3">
													<label>Report: </label> <input type="checkbox"
														name="ch_emp_report_access"
														<%=mybean.PopulateCheck(mybean.emp_report_access)%> />
												</div>
												<div class="form-element3">
													<label>Copy: </label> <input type="checkbox"
														name="ch_emp_copy_access"
														<%=mybean.PopulateCheck(mybean.emp_copy_access)%> />
												</div>
										</div>
										<%-- <div class="form-group">
												<label class="control-label col-xs-2 col-md-1"> MIS:</label>
												<div class="col-md-1 col-xs-1" id="emprows">
													<input type="checkbox" name="ch_emp_mis_access"
														<%=mybean.PopulateCheck(mybean.emp_mis_access)%>>
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-xs-2 col-md-1"> Export:</label>
												<div class="col-md-1 col-xs-1" id="emprows">
													<input type="checkbox" name="ch_emp_export_access"
														<%=mybean.PopulateCheck(mybean.emp_export_access)%>>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-xs-2 col-md-1"> Report: </label>
												<div class="col-md-1 col-xs-1" id="emprows">
													<input type="checkbox" name="ch_emp_report_access"
														<%=mybean.PopulateCheck(mybean.emp_report_access)%>>
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-xs-2 col-md-1"> Copy: </label>
												<div class="col-md-1 col-xs-1" id="emprows">
													<input type="checkbox" name="ch_emp_copy_access"
														<%=mybean.PopulateCheck(mybean.emp_copy_access)%>>
												</div> --%>
										<div><%=mybean.StrHTML%></div>
										<!-- 											<center> -->
										<%
												if (!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
										<div class="form-element6 form-element-center">
											<div class="form-element6">
												<label> Entry By: &nbsp; </label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.entry_by%>" />
											</div>
											<%
												}
											%>
											<%
												if (!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label> Entry Date: &nbsp;</label>
												<%=mybean.entry_date%>
												<input type="hidden" name="entry_date"
													value="<%=mybean.entry_date%>" />
											</div>
										</div>
										<% } %>
										<% if (!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
										<div class="form-element6 form-element-center">
											<div class="form-element6">
												<label> Modified By: &nbsp; </label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by"
													value="<%=mybean.modified_by%>" />
											</div>
											<% } %>
											<% if (!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
											<div class="form-element6">
												<label> Modified Date: &nbsp;</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>" />
											</div>
										</div>
										<%
												}
											%>
										<!-- 											</center> -->
										<div class="row"></div>
										<center>
											<input name="update_button" type="submit"
												class="btn btn-success" id="update_button" value="Update" />
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

</body>
</html>
