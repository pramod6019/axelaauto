<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Report_Insurance_Details"
	scope="request" />
<jsp:useBean id="export" class="axela.insurance.Report_Insurance_Details"
	scope="request" />
<%
	export.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>

<script>

	function PopulateCRMDays() {
		var branch_id = document.getElementById("dr_branch").value;
		var crmtype_id = document.getElementById("dr_crmdays_crmtype_id").value;
		showHint('../sales/mis-check.jsp?crmdays=yes&crmtype_id=' + crmtype_id
				+ '&branch_id=' + GetReplace(branch_id), 'dr_crmdays_id');
	}
</script>
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
						<h1>Insurance Follow-up Details</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-insurance-details.jsp">Insurance
								Follow-up Details</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<!-- 	PORTLET -->
							<center>
								<font color="red"><b><%=export.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Insurance
										Follow-up Details</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<!-- FORM START -->
										<form name="form" id="form" method="post"
											class="form-horizontal">
												<div class="form-element3">
													<label> Branch: </label>
														<%
															if (export.branch_id.equals("0")) {
														%>
														<select name="dr_branch" id="dr_branch"
															class="dropdown form-control">
															<%=mybean.PopulateBranch(export.dr_branch_id, "", "3", "", request)%>
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

												<!-- FORM START -->
												<div class="form-element3">
													<label> Type: </label>
														<select name="dr_crmdays_crmtype_id"
															class="dropdown form-control" id="dr_crmdays_crmtype_id">
															<%=mybean.PopulateFollowuptype(export.comp_id)%>
														</select>

												</div>
											
												<!-- FORM START -->
												<div class=form-element3>
													<label> From Date<font color="#ff0000">*</font>: </label>
														<input name="txt_from_date" type="text" id="txt_from_date"
															value="<%=export.from_date%>" size="12" maxlength="10"
															class="form-control datepicker"/>
												</div>

												<div class="form-element3">
													<label> To Date<font
														color="#ff0000">*</font>:
													</label>
														<input name="txt_to_date" type="text" id="txt_to_date"
															value="<%=export.to_date%>" size="12" maxlength="10"
															class="form-control datepicker" />

												</div>
												<!-- FORM START -->
												<div class="form-element12" style="float:center">
														<center>
															<input name="btn_export" id="btn_export" type="submit"
																class="btn btn-success" value="Export"/>
														</center>

												</div><br></br>

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

	<%@include file="../Library/js.jsp"%>

	<%@include file="../Library/admin-footer.jsp"%>
</body>
</HTML>
</body>
</html>
