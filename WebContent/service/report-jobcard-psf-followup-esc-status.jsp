<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.service.Report_JobCard_PSF_Followup_Esc_Status"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>

<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		//document.formcontact.txt_customer_name.focus(); 
	}
	function frmSubmit() {
		document.formemp.submit();
	}
	function PopulateTeam() {

		var branch_id = document.getElementById("dr_branch").value;
		showHint('../sales/report-team-check.jsp?team=yes&exe_branch_id='
				+ branch_id, 'multiteam');
	}

	function ExeCheck() {
		var branch_id = document.getElementById("dr_branch").value;
		showHint('../service/report-check.jsp?multiple=yes&executive=yes&exe_branch_id='
						+ GetReplace(branch_id), 'exeHint');
	}
</script>
</HEAD>

<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
	<%@include file="../portal/header.jsp"%>
	<!-- 	BODY -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>PSF Follow-up Escalation Status</h1>
					</div>
					<!-- END PAGE TITLE -->
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-jobcard-psf-followup-esc-status.jsp">PSF
								Follow-up Escalation Status</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">PSF Follow-up
										Escalation Status</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

												<form method="post" name="frm1" id="frm1"
													class="form-horizontal">
													<div class="form-element4">
														<label style="margin-bottom: 8px;"> Branch<font color=red>*</font>: </label>
															<%
																if (mybean.branch_id.equals("0")) {
															%>
															<select name="dr_branch" class="form-control"
																id="dr_branch" onChange="ExeCheck();">
																<%=mybean.PopulateBranch(mybean.dr_branch_id, "all", "1,3", "", request)%>
															</select>
															<%
																} else {
															%>
															<input type="hidden" name="dr_branch" id="dr_branch"
																value="<%=mybean.branch_id%>" />
															<%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
															<%
																}
															%>

													</div>

													<div class="form-element4">
														<label>Service Advisor: </label>
															<div id="exeHint"><%=mybean.PopulateServiceExecutives()%></div>
													</div>


													<div class="form-element12">
															<center>
																<input name="submit_button" type="submit"
																	class="btn btn-success" id="submit_button" value="Go" />
															</center>
															<input type="hidden" name="submit_button" value="Submit">
													</div>
												</form>

									</div>
								</div>
							</div>

							<!-- END	PORTLET -->
							<%
								if (!mybean.StrHTML.equals("")) {
							%>
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">&nbsp;</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<%=mybean.StrHTML%>


									</div>
								</div>
							</div>
							<%
								}
							%>
						</div>
					</div>
			</div>
		</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

</body>
</html>
