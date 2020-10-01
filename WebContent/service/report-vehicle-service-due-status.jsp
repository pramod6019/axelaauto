<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.service.Report_Vehicle_Service_Due_Status" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>

<script>
	function ExeCheck() {
		var branch_id = document.getElementById("dr_branch_id").value;
		showHint('../service/report-check.jsp?jcpsfexecutive=yes&branch_id='
				+ GetReplace(branch_id), 'exeHint');
	}
</script>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- 	MULTIPLE SELECT END-->
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
						<h1>Service Due Status</h1>
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
						<li><a href="report-vehicle-service-due-status.jsp">Service
								Due Status</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<center>
								<font color="red"><b> <%=mybean.msg%>
								</b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Service Due
										Status</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<!-- START PORTLET BODY -->
											<div class="container-fluid">
												<!-- FORM START -->
														
												<div class="form-element3">
													 <label>Brands:</label>
													<div id="multiprincipal">
														<select name="dr_principal"  class="form-control multiselect-dropdown" multiple="multiple" id="dr_principal"
															onChange="PopulateBranches();PopulateZone();PopulateRegion();">
															<%=mybean.mischeck.PopulatePrincipal( mybean.brand_ids, mybean.comp_id, request)%>
														</select>
													</div>
												</div>
												
												<div class="form-element3">
													<label>Regions:</label> 
													<div id="regionHint" >
														<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
													</div>
												</div>
													
												<div class="form-element3">
												<label>Zone:</label> 
												<div id="zoneHint" >
													<%=mybean.mischeck.PopulateZone(mybean.brand_id, mybean.region_ids, mybean.zone_ids, mybean.comp_id, request)%>
												</div>
											</div>
												
												<div class="form-element3">
												<label> Branch:</label>
												<div id="branchHint"> 
														<%=mybean.mischeck.PopulateServiceSalesBranch(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id,request)%>
														</div>
											</div>
												
												<div class = "row"></div>	
											<div class = "form-element12">
												<div class="form-element3">
													<label> CRM Executive: </label>
														<span id="exeHint"><%=mybean.PopulateCRMExecutives(mybean.empcrm_id, mybean.comp_id, mybean.ExeAccess)%></span>
												</div>

												<div class="form-element3">
													<label> Call Type: </label>
														<select name="dr_vehcalltype_id" class="form-control"
															id="dr_vehcalltype_id">
															<%=mybean.PopulateCallTypeDays()%>
														</select>
												</div>
											
												<div class="form-element3">
													<label> Start Date<font color=red>*</font>: </label>
														<input name="txt_start_time" id="txt_start_time"
															type="text" value="<%=mybean.start_time%>" size="12"
															maxlength="10" class="form-control datepicker"/>
												</div>

												<div class="form-element3">
													<label> End Date<font color=red>*</font>: </label>
														<input name="txt_end_time" id="txt_end_time" type="text"
															value="<%=mybean.end_time%>" size="12" maxlength="10"
															class="form-control datepicker"/>
												</div>
											</div>
												<!-- <div class="col-md-3" > -->
												<!-- <label class="control-label col-md-7" > Pending Follow-up: </label> -->
												<!-- <div class="col-md-2 " id="emprows" style="top:8px"> -->
												<%-- <input id="chk_pending_followup" name="chk_pending_followup" type="checkbox" <%=mybean.PopulateCheck(mybean.pending_followup)%> />	 --%>
												<!-- </div> -->
												<!-- </div> -->

												<div class="form-element12">
														<center>
															<input name="submit_button" type="submit"
																class="btn btn-success" id="submit_button" value="Go" />
														</center>
														<input type="hidden" name="submit_button" value="Submit" />
												</div>

											</div>
										</form>
									</div>
								</div>
							</div>
							<!-- 	PORTLET -->

							<center><%=mybean.StrHTML%></center>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
		<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	<script language="JavaScript" type="text/javascript">
	function ExeCheck() {
		var branch_id = document.getElementById("dr_branch_id").value;
		showHint('../service/report-check.jsp?jcpsfexecutive=yes&branch_id='
				+ GetReplace(branch_id), 'exeHint');
	}
	
	function PopulateRegion() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region=yes', 'regionHint');
	}
	
	function PopulateBranches() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		var zone_id = outputSelected(document.getElementById("dr_zone").options);

		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone_id=' + zone_id + '&salesandservicebranch=yes', 'branchHint');
	}

	function PopulateModels() { //v1.0
	}

	function PopulateAdviser() {
	}

	function PopulateTech() {
	}
	
	function PopulateZone() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone=yes', 'zoneHint');
	}
	
	function PopulateBranch() {
	}
	
	function PopulateCRMDays(){
	}
	
	function Populatepsfdays(){
	}
	</script>
	<!-- END CONTAINER -->
</body>
</html>