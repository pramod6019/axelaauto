<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Exe_Esc" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
  
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
						<h1>Sales Consultant Escalation</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt; </li>
						<li><a href="../sales/report-exe-esc.jsp">Sales Consultant Escalation </a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="red"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Sales Consultant Escalation</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="form-element6">
												<label >Year:</label>
												<div>
													<select name="dr_year" class="form-control" id="dr_year">
														<%=mybean.PopulateYears()%>
													</select>
												</div>
											</div>

											<div class="form-element6">
												<label >Month:</label>
												<div id="month" >
													<select name="dr_month" id="dr_month" class="form-control">
														<%=mybean.PopulateMonths()%>
													</select>
												</div>
											</div>
											
											<div class="form-element4">
												<label> Brands<font color="red">*</font>:</label>
												<div>
													<select name="dr_brand" size="10" multiple="multiple"
														class="form-control multiselect-dropdown" id="dr_brand"
														onChange="PopulateBranches();PopulateModels();PopulateRegion();">
														<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element4">
												<label> Regions:</label>
												<div id="regionHint">
													<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label> Branches:</label>
												<div id="branchHint">
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label> Model:</label>
												<div id="modelHint">
													<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
												</div>
											</div>
											
											<div class="form-element4">
												<label> Teams:</label>
												<div id="teamHint">
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label> Sales Consultant:</label>
												<div id="exeHint">
													<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
												</div>
											</div>
											
											<div class="form-element12" align="center">
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
								<div class="form-element12">
									<center><%=mybean.StrHTML%></center>
								</div>
							<%
								}
							%>
							<!-- <center><div id="chartdiv" style="height: 800px;"></div></center>	  -->

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<!-- END CONTAINER -->
	<%@ include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>

	<!-- Additional plugins go here -->
	<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
	<script type="text/javascript" src="../Library/amcharts/serial.js"></script>
	<!-- Start Export Image additional plugins -->
	<script src="../Library/amcharts/exporting/amexport.js" type="text/javascript"></script>
	<script src="../Library/amcharts/exporting/rgbcolor.js" type="text/javascript"></script>
	<script src="../Library/amcharts/exporting/canvg.js" type="text/javascript"></script>
	<script src="../Library/amcharts/exporting/filesaver.js" type="text/javascript"></script>
	<!-- End Export Image additional plugins -->
	<!-- End additional plugins -->
	
	<script type="text/javascript">
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_brand").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_brand").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}

		function PopulateModels() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_brand").options);
			showHint('../sales/mis-check1.jsp?brand_id=' + brand_id + '&model=yes', 'modelHint');
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
		function PopulateCRMDays(){
		}
		function PopulateVariants(){
		}
	</script>
</body>
</HTML>
