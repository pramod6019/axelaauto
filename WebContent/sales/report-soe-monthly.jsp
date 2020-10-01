<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Report_SOE_Monthly" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</head>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

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
						<h1>SOE Dashboard</h1>
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
						<li><a href="../sales/report-soe-monthly.jsp">SOE Dashboard</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="red"><b> <%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">

							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">SOE Dashboard</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form method="post" name="frm1" id="frm1" >
										
											<div class="form-element6">
												<label>Month: </label>
												<select name="dr_month" id="dr_month" class="form-control">
													<%=mybean.PopulateMonth()%>
												</select>
											</div>
											
											<div class="form-element6">
												<label>Year: </label>
												<select name="dr_year" id="dr_year" class="form-control">
													<%=mybean.PopulateYear()%>
												</select>
											</div>
											
											<br>
											
											<div class="form-element3">
												<label>Brands:</label>
												<div id="multiprincipal">
													<select name="dr_principal" size="10" multiple="multiple" class="form-control multiselect-dropdown"
														id="dr_principal" onChange="PopulateBranches();PopulateRegion();">
															<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element3">
												<label>Regions:</label>
												<div id="regionHint">
													<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Branches:</label>
												<div id="branchHint">
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Teams:</label>
												<div id="teamHint">
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>


											<div class="form-element3">
												<label> Sales Consultant: </label>
												<div id="exeHint">
													<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
												</div>
											</div>
											
											<div class="form-element3">
												<label>SOE:</label>
												<div>
													<select name="dr_soe" size="10" class="form-control multiselect-dropdown" id="dr_soe" multiple="multiple" >
														<%=mybean.PopulateSoe()%>
													</select>
												</div>
											</div>
											
											<div class="form-element3 form-element-margin">
												<label>Only Team Lead: </label>
												<input id="chk_team_lead" type="checkbox" name="chk_team_lead" <%=mybean.PopulateCheck(mybean.chk_team_lead)%> />
											</div>
											
											<div class="form-element12">
												<center>
													<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>

										</form>
									</div>
								</div>
							</div>
							
							<%
								if (!mybean.StrHTML.equals("")) {
							%>
								<center><%=mybean.StrHTML%></center>
							<%
								}
							%>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>

	<script type="text/javascript">

		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			//alert("111111------"+brand_id);
			showHint( '../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
		function PopulateSoe() {
			var branch_id = document.getElementById("dr_branch").value;
			showHint('../sales/mis-check.jsp?soe=yes&exe_branch_id=' + branch_id, 'soe');
		}

		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			//	alert("222222222=--33--"+brand_id);
			showHint( '../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}

		function PopulateTeams() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?branch_id=' + branch_id + '&team=yes', 'teamHint');
		}

		function PopulateExecutives() { //v1.0
			var team_id = outputSelected(document.getElementById("dr_team").options);
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?team_id=' + team_id + '&exe_branch_id=' + branch_id + '&executives=yes', 'exeHint');
		}
		
		//Dnt Remove this
		function PopulateCRMDays (){ 
		}
	</script>
</body>
</HTML>
