<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.service.Report_JobCard_PSF_Followup_Esc_Dashboard"
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
				<!-- BEGIN PAGE TITLE -->
				<div class="page-title">
					<h1>PSF Follow-up Escalation Dashboard</h1>
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
				<li><a href="report-jobcard-psf-followup-esc-dashboard.jsp">PSF
						Follow-up Escalation Dashboard</a><b>:</b></li>

			</ul>
			<!-- END PAGE BREADCRUMBS -->

			
				<div class="tab-pane" id="">
					<!-- 					BODY START -->
					<!-- 	PORTLET -->

					<div class="portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">PSF Follow-up
								Escalation Dashboard</div>
						</div>
						<div class="portlet-body portlet-empty container-fluid">
							<div class="tab-pane" id="">
								<!-- START PORTLET BODY -->
								<form method="post" name="frm1" id="frm1" class="form-horizontal">
											<!-- start -->
										<div class="form-element3">
												 <label>Brands:</label>
												<div id="multiprincipal">
													<select name="dr_principal"  class="form-control multiselect-dropdown" multiple="multiple" id="dr_principal"
														onChange="PopulateZone();PopulateRegion();PopulateBranch();">
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
										
										<!-- end -->
									
									<div class="form-element3">
										<label> Branch:</label>
										<div id="branchHint"> 
												<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id,request)%>
												</div>
									</div>
									
											
									<div class = "row"></div>
										
									<div class="form-element4">
										<label>Service Advisor: </label> 
										<div id="exeHint">
										<%=mybean.PopulateServiceExecutives()%>
										</div>
									</div>


									<div class="form-element12">
										<center>
											<input name="submit_button" type="submit"
												class="btn btn-success" id="submit_button" value="Go" />
										</center>
										<input type="hidden" name="submit_button" value="Submit"/>

									</div>
								</form>	
							</div>
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
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

<script language="JavaScript" type="text/javascript">
	function PopulateRegion() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region=yes', 'regionHint');
	}
	
	function PopulateBranches() { 
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		var zone_id = outputSelected(document.getElementById("dr_zone").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone_id=' + zone_id + '&branch=yes', 'branchHint');
	}

	function PopulateModels() {
	}

	function PopulateAdviser() {
		var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
// 		alert(branch_id);
		showHint('../service/mis-check1.jsp?advisor=yes&exe_branch_id=' + branch_id, 'exeHint');
	}

	function PopulateTech() {
	}
	
	function PopulateZone() { 
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


</body>
</html>
