<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_Retention"
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
						<h1>Retention Report</h1>
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
							<li><a href="../service/report-retention.jsp">Retention
									Report</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Retention Report
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<!-- FORM START -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<div class="form-element3">
												<label> Sales Month:</label> <select name="dr_month"
													id="dr_month" class="form-control">
													<%=mybean.PopulateMonths()%>
												</select>

											</div>


											<div class="form-element3">
												<label> Service Year:&nbsp; </label> <select name="dr_year"
													id="dr_year" class="form-control">
													<%=mybean.PopulateYears()%>
												</select>

											</div>
											<div class="form-element2">
												<label>Brands:</label>
												<div id="multiprincipal">
													<select name="dr_principal"
														class="form-control multiselect-dropdown"
														multiple="multiple" id="dr_principal"
														onChange="PopulateBranches();PopulateRegion();">
														<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids,
											mybean.comp_id, request)%>
													</select>
												</div>
											</div>
											<div class="form-element2">
												<label>Regions:</label>
												<div id="regionHint">
													<%=mybean.mischeck.PopulateRegion(mybean.brand_id,
													mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element2">
												<label>Branches:</label>
												<div id="branchHint">
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id,
											mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<!-- FORM START -->
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
							<!-- 	PORTLET -->

							<center><%=mybean.StrHTML%>
							</center>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	<script language="JavaScript" type="text/javascript">
		function PopulateTeam() {
			var branch_id = document.getElementById("dr_branch").value;
			showHint('../sales/mis-check.jsp?team=yes&exe_branch_id='
					+ branch_id, 'multiteam');
		}

		function ExeCheck() { //v1.0
			var branch_id = document.getElementById("dr_branch").value;
			var team_id = document.getElementById("dr_team").value;
			//alert(team_id);	
			showHint('../sales/mis-check.jsp?executive=yes&team_id=' + team_id
					+ '&exe_branch_id=' + GetReplace(branch_id), 'exeHint');
		}
		function PopulateRegion() { 
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() { 
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}
		function PopulateAdviser() {

		}
		function PopulateTech() {

		}
		function PopulateCRMDays() {

		}
		function Populatepsfdays() {

		}
	</script>
</body>
</HTML>
