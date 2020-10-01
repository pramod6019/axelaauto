<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_JobCard_PSF"
	scope="request" />
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
						<h1>PSF Follow-up</h1>
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
						<li><a href="report-jobcard-psf.jsp">PSF Follow-up</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<center>
								<font color="red"><b> <%=mybean.msg%>
								</b></font>
							</center>
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">PSF Follow-up</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">

										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">
												<div class="form-element3">
													 <label>Brands:</label>
													<div id="multiprincipal">
														<select name="dr_principal"  class="form-control multiselect-dropdown" multiple="multiple" id="dr_principal"
															onChange="PopulateModels();PopulateBranches();PopulateZone();PopulateRegion();">
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
														<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id,request)%>
														</div>
											</div>

												<div  class="form-element3">
													<label> Start Date<font color=red>*</font>:&nbsp; </label>
														<input name="txt_starttime" id="txt_starttime"
															value="<%=mybean.start_time%>"
															class="form-control datepicker" type="text"/>
												</div>

												<div class="form-element3">
													<label> End Date<font color=red>*</font>:&nbsp; </label>
														<input name="txt_endtime" id="txt_endtime"
															value="<%=mybean.end_time%>"
															class="form-control datepicker" type="text"/>
												</div>

												<div class="form-element3">
													<label> Executive:&nbsp;</label>
													<div id="exeHint">
														<%=mybean.PopulatePSFExecutive(mybean.branch_id, mybean.comp_id)%>
													</div>
												</div>

												<div class="form-element3">
													<label>PSF Days:&nbsp;</label>
														<div id="psfdaysHint"> 
														<%=mybean.mischeck.PopulatePSFDays(mybean.psfdays_ids, mybean.branch_id, mybean.comp_id)%>
														</div>
												</div>
												
												<div class="row"></div>
												
												<div class="form-element3">
													<label>Feedback Type:&nbsp;</label>
														<select name="dr_jcpsffeedbacktype" class="form-control"
															id="dr_jcpsffeedbacktype">
															<%=mybean.PopulatePSFFeedbackType()%>
														</select>
												</div>

												<div class="form-element3">
													<label>Experience:&nbsp;</label>
														<select name="dr_jcpsf_satisfied" class="form-control"
															id="dr_jcpsf_satisfied">
															<%=mybean.PopulateCRMSatisfied(mybean.comp_id)%>
														</select>
												</div>
												
												<div class="form-element3 form-element-margin">
													<label>Pending Follow-up:&nbsp; </label> <input
														id="chk_pending_followup" name="chk_pending_followup"
														type="checkbox"
														<%=mybean.PopulateCheck(mybean.pending_followup)%> />

												</div>
												
												<div class="form-element3">
												<label>Model: </label>
												<div id="modelHint" > 
												<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.preownedmodel_ids, mybean.branch_id, mybean.comp_id, request)%>
												</div>
											</div>
											
											<div class="row"></div>
												<div class="form-element3">
													<label>Job Card Category:</label> 
												<div> 
													<%=mybean.PopulateJobCardCategory(mybean.jccat_ids, mybean.comp_id,request)%>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Job Card Type:</label> 
												<div> 
												<%=mybean.PopulateJobCardType(mybean.jctype_ids, mybean.comp_id,request)%>
												</div>
											</div>
														<div class="form-element12">
														<center>
															<input name="submit_button" type="submit" 
																class="btn btn-success" id="submit_button" value="Go" />
														<input type="hidden" name="submit_button" value="Submit"/>
														
														<input name="submit_button" type="submit" 
																class="btn btn-success" id="export_button" value="Export" />
														<input type="hidden" name="export_button" value="Export"/>
																</center>
																</div>
													
										</form>
									</div>
								</div>
							</div>
							<!-- 	PORTLET -->
								<%=mybean.StrHTML%>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

<script language="JavaScript" type="text/javascript">
	
	function PopulateRegion() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region=yes', 'regionHint');
	}
	
	function PopulateZone() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone=yes', 'zoneHint');
	}
	
	function PopulateBranches() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		var zone_id = outputSelected(document.getElementById("dr_zone").options);
	
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone_id=' + zone_id + '&branch=yes', 'branchHint');
	}
	
	function PopulateBranch() {
	}
	
	function PopulateModels() { 
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
		showHint('../service/mis-check1.jsp?brand_id=' + brand_id +"&branch_id= " + branch_id + '&model=yes', 'modelHint');
	}
	
	function PopulateAdviser(){
		ExeCheck();
	}
	
	function ExeCheck() {
		//alert("yudsd");//
		var branch_id = document.getElementById("dr_branch_id").value;
		showHint('../service/mis-check1.jsp?jcpsfexecutive=yes&branch_id='
				+ GetReplace(branch_id), 'exeHint');
	}

	function Populatepsfdays() {
		var branch_id = $("#dr_branch_id").val();
		showHint('../service/mis-check1.jsp?psfdays=yes'
				+ '&branch_id=' + branch_id + '&branch=yes', 'psfdaysHint');
	}
	
	function PopulateTech(){}

	function PopulateCRMDays(){}; 
	
	function days() { //v1.0
		var st = document.getElementById("txt_starttime");
		var et = document.getElementById("txt_endtime");
		alert("st=="+st);
		alert("et=="+et);
	}
	
</script>
</body>
</html>