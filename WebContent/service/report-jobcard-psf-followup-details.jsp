<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.service.Report_JobCard_PSF_Followup_Details"
	scope="request" />
<jsp:useBean id="export"
	class="axela.service.Report_JobCard_PSF_Followup_Details"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<%
	export.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
						
<script>
	function PopulatePSFDays() {
// 		alert("rghhfh");
		var branch_id = document.getElementById("dr_branch").value;
		showHint('../service/report-check.jsp?psfdays=yes&branch_id='
				+ GetReplace(branch_id), 'psfdaysHint');
	}
</script>
<%@include file="../Library/css.jsp"%> 							
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

	<!-- 	BODY -->
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
						<h1>
							<%
								if (mybean.satisfied.equals("yes")) {
							%>
							PSF Follow-up Details
							<%
								} else {
							%>
							PSF Follow-up Dissatisfied Details
							<%
								}
							%>
						</h1>
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
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<%
							if (mybean.satisfied.equals("yes")) {
						%>
						<li><a
							href="report-jobcard-psf-followup-details.jsp?satisfied=yes">PSF
								Follow-up Details</a>:&nbsp;</li>
						<%
							} else {
						%>
						</li>
						<a href="report-jobcard-psf-followup-details.jsp?satisfied=no">PSF
							Follow-up Dissatisfied Details</a>:&nbsp;
						</li>
						<%
							}
						%>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b><%=export.msg%></b></font>
							</center>
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%
											if (mybean.satisfied.equals("yes")) {
										%>
										PSF Follow-up Details
										<%
											} else {
										%>
										PSF Follow-up Dissatisfied Details
										<%
											}
										%>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">

										<form name="form" id="form" method="post"
											class="form-horizontal">
											<div class="container-fluid ">

													<div class=form-element3>
														<label> Branch<font color=red>*</font>: </label>
															<%
																if (export.branch_id.equals("0")) {
															%>
															<select name="dr_branch" id="dr_branch"
																class="form-control" onChange="PopulatePSFDays();">
																<%=mybean.PopulateBranch(export.dr_branch_id, "", "1,3", "", request)%>
															</select>
															<%
																} else {
															%>
															<input type="hidden" name="dr_branch" id="dr_branch"
																value="<%=export.branch_id%>" />
															<%=mybean.getBranchName(export.dr_branch_id, export.comp_id)%>
															<%
																}
															%>

													</div>
												<%
														if (mybean.satisfied.equals("yes")) {
													%>
												<div class="form-element3">

													<label> Follow-up Days<font color=red>*</font>: </label>
														<span id="psfdaysHint"><%=export.PopulatePSFDays()%></span>
												</div>
												<%
														}
													%>
													
													<%
														if (mybean.satisfied.equals("yes")) {
													%>
													
												<div class="form-element3">
													<label> From Date<font color="#ff0000">*</font>: </label>
														<input name="txt_from_date" type="text"
															class="form-control datepicker" id="txt_from_date"
															value="<%=export.start_time%>" size="12" maxlength="10" />
												</div>
												<div class="form-element3">

													<label> To Date<font color=red>*</font>: </label>
														<input name="txt_to_date" type="text" class="form-control datepicker"
															id="txt_to_date" value="<%=export.end_time%>" size="12"
															maxlength="10" />
												</div>
												<div class="form-element12">
														<center><input name="btn_export" id="btn_export" type="submit"
															class="btn btn-success" value="Export" /></center>
												</div>
												<%
														}else{
													%>
													
														<div class="form-element3">
													<label> From Date<font color="#ff0000">*</font>: </label>
														<input name="txt_from_date" type="text"
															class="form-control datepicker" id="txt_from_date"
															value="<%=export.start_time%>" size="12" maxlength="10" />
												</div>
												
												<div class="form-element3">
													<label> To Date<font color=red>*</font>: </label>
														<input name="txt_to_date" type="text" class="form-control datepicker" 
															id="txt_to_date" value="<%=export.end_time%>" size="12"
															 maxlength="10" />
												</div>
												
												<div class="form-element12">
														<center><input name="btn_export" id="btn_export" type="submit"
															class="btn btn-success" value="Export" /></center>
												</div>
												
												<%} %>	
													
													
											</div>
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
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%> 
</body>
</html>
