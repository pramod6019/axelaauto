<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Report_CRMFollowup" scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>

<%@include file="../Library/css.jsp"%> 
<style type="text/css">
.multiselect, .multiselect-container {
	width: 32.5pc;
	text-align: initial;
}

.multiselect .caret {
	float: right;
	margin-top: -12px;
}
</style>
</head>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>CRM Follow-up</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-crmfollowup.jsp">CRM Follow-up</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b> <%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">CRM Follow-up</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" class="form-horizontal" name="frm1" id="frm1">

											<div class="row">
												<div class="form-element4">
													<label>Brand:</label>
													<div>
														<select name="dr_brand" class="form-control " id="dr_brand"
															onchange="PopulateCRMDays();PopulateBranches();PopulateRegion();">
															<%=mybean.PopulatePrincipal(mybean.brand_id, mybean.comp_id, request)%>
														</select>
	
													</div>
												</div>
	
												<div class="form-element4">
													<label>Region:</label>
													<div id="regionHint">
														<%=mybean.PopulateRegion("", mybean.region_id, mybean.comp_id, request)%>
													</div>
												</div>
												
												<div class="form-element4">
													<label>Branch<font color="#ff0000">*</font>:</label>
													<div id="branchHint">
														<%=mybean.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_id, mybean.comp_id, request)%>
													</div>
												</div>
												
											</div>
											
											<div class="row">
	
												<div class="form-element4">
													<label>Start Date:</label>
													<input name="txt_starttime" id="txt_starttime" value="<%=mybean.start_time%>"
														class="form-control datepicker" type="text"  />
												</div>
											
												<div class="form-element4">
													<label>End Date:</label>
													<input name="txt_endtime" id="txt_endtime" value="<%=mybean.end_time%>"
														 class="form-control datepicker" type="text"  />
												</div>
	
												<div class="form-element4">
													<label>Type<font color="#ff0000">*</font>:</label>
													<div>
														<select name="dr_crmdays_crmtype_id" class="form-control "
															id="dr_crmdays_crmtype_id" onchange="PopulateCRMDays();">
															<%=mybean.PopulateCRMType()%>
														</select>
													</div>
												</div>
												
											</div>
												
											<div class="row">

												<div class="form-element4">
													<label>Team:</label>
													<div id="teamHint">
														<%=mybean.PopulateTeam(mybean.branch_id, mybean.comp_id)%>
													</div>
												</div>
												
												<div class="form-element4">
													<label>Sales Consultant:</label>
													<div id="exeHint">
														<%=mybean.PopulateSalesExecutives(mybean.branch_id, mybean.team_id, mybean.comp_id)%>
													</div>
												</div>


												<div class="form-element4">
													<label>Contactable:</label>
													<div>
														<select name="dr_feedbacktype" class="form-control " id="dr_feedbacktype" visible="true">
															<%=mybean.PopulateCRMFeedbackType()%>
														</select>
													</div>
												</div>
												
											</div>

											<div class="row">
	
												<div class="form-element4">
													<label>Follow-up Days:</label>
													<div id="crmdaysHint1">
														<%=mybean.PopulateCRMDaysMulti(mybean.comp_id, mybean.crmdays_crmtype_id, mybean.crmdays_ids, mybean.branch_id, mybean.brand_id)%>
													</div>
													<input type="text" id="txt_dr_crmdays_id" name="txt_dr_crmdays_id" hidden>
												</div>

												<div class="form-element4">
													<label>Experience:</label>
													<div >
														<select name="dr_crm_satisfied" class="form-control " id="dr_crm_satisfied">
															<%=mybean.PopulateCRMSatisfied()%>
														</select>
													</div>
												</div>
	
												<div class="form-element4 form-element-margin">
													<label>Pending Follow-up:</label>
													<input id="chk_pending_followup" name="chk_pending_followup" type="checkbox"
														<%=mybean.PopulateCheck(mybean.pending_followup)%> />
												</div>

											</div>
											
											<div class="form-element4">
												<label>SOE:</label>
												<div >
													<select name="dr_soe" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_soe" onchange = "PopulateSob()">
														<%=mybean.PopulateSoe()%>
													</select>
												</div>
											</div>
											
											<div class="form-element4">
												<label>SOB:</label>
												<div id="sobHint">
														<%=mybean.PopulateSob(mybean.soe_id, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element12">
												<center>
													<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>
										</div>
									</form>
								</div>

							</div>
						</div>

						<%
							if (!mybean.StrHTML.equals("")) {
						%>
						<!-- 	<div class="portlet box  "> -->
						<!-- 		<div class="portlet-title" style="text-align: left"></div> -->
						<!-- 		<div class="portlet-body portlet-empty"> -->
						<!-- 			<div class="tab-pane" id=""> -->
						<!-- START PORTLET BODY -->
						<center><%=mybean.StrHTML%></center>
						<!-- 			</div> -->
						<!-- 		</div> -->
						<!-- 	</div> -->
						<%} %>

					</div>

				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script type="text/javascript">
		function PopulateTeams() {
			var branch_id = document.getElementById("dr_branch").value;
			showHint('../sales/mis-check1.jsp?single=yes&team=yes&branch_id=' + branch_id, 'teamHint');
		}
		
		function PopulateExecutives() {
			var team_id = document.getElementById("dr_team").value;
			var branch_id = document.getElementById("dr_branch").value;
			showHint( '../sales/mis-check1.jsp?executives=crmfollowupexe&exe_branch_id=' + branch_id + '&team_id=' + team_id, 'exeHint');
		}
		
		function GetCrmDaysId() {
			var temp = $("#dr_crmdays_id").val();
			// alert("temp-----------"+temp);
			document.getElementById('txt_dr_crmdays_id').value = temp;
		}
		
		function PopulateCRMDays() {
			var brand_id = document.getElementById("dr_brand").value;
			var crmtype_id = document.getElementById("dr_crmdays_crmtype_id").value;
			var branch_id = document.getElementById("dr_branch").value;
			showHint( '../sales/mis-check1.jsp?crmdaysfollowupsingle=multi&esc=yes&crmtype_id=' + crmtype_id + '&brand_id=' + brand_id + '&branch_id=' + branch_id, 'crmdaysHint1');
		}

		function PopulateRegion() {
			var brand_id = document.getElementById("dr_brand").value;
			showHint( '../sales/mis-check1.jsp?region=crmfollowupregion&brand_id=' + brand_id, 'regionHint');
		}
		
		function PopulateBranches() { //v1.0
			var brand_id = document.getElementById("dr_brand").value;
			var region_id = document.getElementById("dr_region").value;
			showHint('../sales/mis-check1.jsp?brand_id=' + brand_id + '&region_id=' + region_id + '&branch=crmfollowupbranch', 'branchHint');
		}
		
		function PopulateSob() {
			var soe_id = outputSelected(document.getElementById("dr_soe").options);
			showHint( '../sales/mis-check1.jsp?soe_id=' + soe_id + '&sob=yes', 'sobHint');
		}
	</script>

</body>
</html>
