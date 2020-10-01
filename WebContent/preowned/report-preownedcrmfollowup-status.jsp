<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.preowned.Report_PreownedCRMFollowup_Status"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />

<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../assets/css/footable.core.css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


<!-- End Export Image additional plugins -->
<!-- End additional plugins -->

<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<script>
	$(function() {
		$("#txt_starttime").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
		$("#txt_endtime").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
	});
</script>
<style>
@media ( max-width : 1024px) {
	/*    TABLE RESPONSIVE START   */
	td {
		display: block;
	}
	/*    TABLE RESPONSIVE END   */
}
</style>


<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Pre-Owned CRM Follow-up Status</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-preownedcrmfollowup-status.jsp">Pre-Owned
								CRM Follow-up Status</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="red"><b> <%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Pre-Owned CRM
										Follow-up Status</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">

											<div class="container-fluid">
												<div class="">
													<div class="col-md-3 col-sm-4">
														<label class="control-label col-md-4">Type</label>
														<div class="col-md-6">
															<select name="dr_precrmfollowupdays_precrmtype_id"
																class="form-control"
																id="dr_precrmfollowupdays_precrmtype_id"
																onchange="PopulatePreownedCRMDays();PopulateCRMDissatisfied();">
																<%=mybean.PopulatePreowedCRMType()%>
															</select>
														</div>
													</div>

													<div class="col-md-3 col-sm-4">
														<label class="control-label col-md-4">Start Date:</label>
														<div class="col-md-6">
															<input name="txt_starttime" id="txt_starttime"
																value="<%=mybean.start_time%>"
																class="form-control date-picker"
																data-date-format="dd/mm/yyyy" type="text" value="" />
														</div>
													</div>
													<div class="col-md-3 col-sm-4">
														<label class="control-label col-md-4">End Date:</label>
														<div class="col-md-6">
															<input name="txt_endtime" id="txt_endtime"
																value="<%=mybean.end_time%>"
																class="form-control date-picker"
																data-date-format="dd/mm/yyyy" type="text" value="" />
														</div>
													</div>

													<div class="col-md-3 col-sm-12" align="center">
														<!-- <div> &nbsp;</div> -->
														<input type="submit" name="submit_button"
															id="submit_button" class="btn btn-success" value="Go" />
														<input type="hidden" name="submit_button" value="Submit" />
													</div>
												</div>
											</div>
											<br>

											<div class="container-fluid">
												<div class="">
													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Brands:
															<div id="multiprincipal">
																<select name="dr_principal" size="10"
																	multiple="multiple" class="form-control"
																	id="dr_principal"
																	onChange="PopulateBranches();PopulateRegion();">
																	<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
																</select>
															</div>
														</div>
													</div>

													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Regions: <span id="regionHint"> <%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
															</span>
														</div>
													</div>

													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Branches: <span id="branchHint"> <%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
															</span>
														</div>
													</div>

													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Model: <span id="modelHint"> <%=mybean.mischeck.PopulateModels(mybean.model_ids, mybean.comp_id, request)%>
															</span>
														</div>
													</div>

												</div>
											</div>
											<br>

											<div class="container-fluid">
												<div class="">
													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Teams:
															<div id="teamHint">
																<%=mybean.mischeck.PopulatePreownedTeams(mybean.branch_id,mybean.team_ids, mybean.comp_id,request)%>
															</div>
														</div>
													</div>

													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Pre-Owned Consultant: <span id="exeHint"> 
															<%=mybean.mischeck.PopulatePreownedExecutives( mybean.branch_id, mybean.team_id, mybean.comp_id)%></span>
														</div>
													</div>
													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Days: <span id="followupHint"> 
															<%=mybean.mischeck.PopulateCRMDays(mybean.comp_id, mybean.crmdays_ids, mybean.precrmfollowupdays_precrmtype_id, mybean.branch_id)%>
															</span>
														</div>

													</div>
													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Dis-Satisfied: <span id="dissatisfiedHint"> 
															<%=mybean.mischeck.PopulateConcern(mybean.comp_id, mybean.crmconcern_ids, mybean.precrmfollowupdays_precrmtype_id)%>
															</span>
														</div>

													</div>
												</div>
										</form>
									</div>
								</div>
							</div>
							<% if (!mybean.chart_data.equals("")) { %>
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">&nbsp;</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">

										<% if (mybean.NoChart.equals("")) { %>
										<div id="chart1" style="height: 700px;"></div>
										<center>
											<b>Total: <%=mybean.chart_data_total%></b>
										</center>
										<% } else { %>
										<%=mybean.NoChart%>
										<% } %>
										<% } %>
										<br></br>
										<% if (!mybean.StrHTML.equals("")) { %>

										<center><%=mybean.StrHTML%></center>
										<% } %>
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

	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
	<script src="../assets/js/components-date-time-pickers.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"></script>
	<script src="../assets/js/footable.js" type="text/javascript"></script>
	<!-- Start additional plugins -->
	<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
	<script type="text/javascript" src="../Library/amcharts/pie.js"></script>
	<!-- Start Export Image additional plugins -->
	<script src="../Library/amcharts/exporting/amexport.js" type="text/javascript"></script>
	<script src="../Library/amcharts/exporting/rgbcolor.js" type="text/javascript"></script>
	<script src="../Library/amcharts/exporting/canvg.js" type="text/javascript"></script>
	<script src="../Library/amcharts/exporting/filesaver.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>

	<script language="JavaScript" type="text/javascript">
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document
					.getElementById("dr_principal").options);
			//alert("111111------"+brand_id);
			showHint('../preowned/mis-check.jsp?multiple=yes&brand_id='
					+ brand_id + '&region=yes', 'regionHint');
		}
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document
					.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../preowned/mis-check.jsp?multiple=yes&brand_id='
					+ brand_id + '&region_id=' + region_id + '&branch=yes',
					'branchHint');
		}

		function PopulatePreownedTeams() {
			var branch_id = $('#dr_branch').val();
			showHint('../preowned/mis-check.jsp?branch_id=' + branch_id + '&preownedteam=yes', 'teamHint');
		}

		function PopulateExecutives() { //v1.0
			var team_id = $('#dr_preownedteam').val();
			showHint('../preowned/mis-check.jsp?team_id=' + team_id + '&executives=yes', 'exeHint');
		}

		function PopulatePreownedCRMDays() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			var crmtype_id = document
					.getElementById("dr_precrmfollowupdays_precrmtype_id").value;
			/*  alert("crmtype_id---------"+crmtype_id);
			alert("branch_id--------"+branch_id);  */
			showHint('../preowned/mis-check.jsp?crmdays=yes&exe_branch_id='
					+ branch_id + '&crmtype_id=' + crmtype_id, 'followupHint');
			//alert("done");
		}
		function PopulatePreownedCRMDissatisfied() {
			var crmtype_id = document
					.getElementById("dr_precrmfollowupdays_precrmtype_id").value;
			// alert("crmtype_id---------"+crmtype_id);
			showHint('../preowned/mis-check.jsp?crmconcern=yes&crmtype_id='
					+ crmtype_id, 'dissatisfiedHint');
			//alert("done");
		}
	</script>

	<script type="text/javascript">
		var chart;
		var legend;
		var export1 = {
			menuTop : "0px",
			menuItems : [ {
				icon : '../Library/amcharts/images/export.png',
				format : 'jpg'
			} ],
			menuItemOutput : {
				fileName : "CRM Follow-up Status"
			}
		};
		AmCharts.ready(function() {
	<%if (mybean.NoChart.equals("")) {%>
		var chartData = <%=mybean.chart_data%> ;
					// PIE CHART
					chart = new AmCharts.AmPieChart();
					chart.dataProvider = chartData;
					chart.titleField = "type";
					chart.valueField = "total";
					chart.minRadius = 200;

					// LEGEND
					legend = new AmCharts.AmLegend();
					legend.align = "center";
					legend.markerType = "circle";
					chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
					chart.exportConfig = export1;
					chart.addLegend(legend);

					// WRITE
					chart.write("chart1");
	<%}%>
		});
	</script>
</body>
</HTML>
