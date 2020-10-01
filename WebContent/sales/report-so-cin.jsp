<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Report_SO_CIN" scope="request" />
<jsp:useBean id="mischeck" class="axela.sales.Report_SO_CIN" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>

</head>

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
						<h1>Sales Order CIN Status</h1>
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
							<li><a href="report-so-cin.jsp">Sales Order CIN Status</a><b>:</b></li> 
						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- START PORTLET  -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Sales Order CIN Status</div>
								</div>

								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form method="post" name="frm1" id="frm1" class="form-horizontal">
												<!--<div class="col-md-3 col-sm-4"> -->
												<!--	<label class="control-label col-md-4 col-sm-4 col-xs-12">Delivery Status:</label> -->
												<!--	<div class="col-md-6"> -->
												<!--		<select name="dr_status_id" class="form-control" id="dr_status_id"> -->
												<%--           <%=mybean.PopulateStatus()%> --%>
												<!--       	</select> -->
												<!--	</div> -->
												<!--</div> -->
												<!--<div class="col-md-1 col-sm-12" align="center" style="top: 10px"> -->
												<!-- <center> -->
												<!-- <a href="veh-salesorder-list.jsp?smart=yes">(Export)</a> -->
												<!-- </center> -->
												<!-- </div> -->

											<div class="form-element4">
												<label >Brands:</label>
												<div  id="multiprincipal">
													<select name="dr_brand" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_brand"
														onChange="PopulateBranches();PopulateModels();PopulateRegion();">
															<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element4">
												<label >Regions: </label>
												<div id="regionHint">
													<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label > Branches:</label>
												<div id="branchHint">
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label >Model: </label>
												<div id="modelHint">
													<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label > Teams: </label>
												<div id="teamHint">
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label >Sales Consultant: </label>
												<div id="exeHint">
													<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
												</div>
											</div>
											
											<div class="form-element12" align="center" >
												<center>
													<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />
													<input type="hidden" name="submit_button" value="Submit">
												</center>
											</div>
												<!--<div class="col-md-3 col-sm-6"> -->
												<!--		<div style="margin: 10px;"> Delivery Status: -->
												<!--			<select name="dr_deliverystatus_id" class="form-control" size=10 style="padding:10px" id="dr_deliverystatus_id" multiple="multiple"> -->
												<%--			<%=mybean.PopulateDeliveryStatus(mybean.deliverystatus_ids, mybean.comp_id)%> --%>
												<!--		</select> -->
												<!--		</div> -->
												<!--	</div> -->

												<!--	<div class="col-md-3 col-sm-6"> -->
												<!--		<div style="margin: 10px;"> Fuel Type: -->
												<!--			<select id="dr_fuel_id" name="dr_fuel_id" size=10 style="padding:10px" class="form-control" multiple="multiple" id="dr_fuel_id"> -->
												<%--			<%=mybean.PopulateFuelType(mybean.fueltype_ids, mybean.comp_id)%> --%>
												<!--			</select> -->
												<!--		</div> -->
												<!--	</div> -->

										</form>

									</div>
								</div>
								<!--  </div> -->

								<%
									if (!mybean.StrHTML.equals("")) {
								%>
								<!--<div class="portlet box "> -->
								<!--	<div class="portlet-title" style="text-align: center"> -->
								<!--		<div class="caption" style="float: none">&nbsp;Sales Order CIN Status</div> -->
								<!--	</div> -->
								<!--	<div class="portlet-body portlet-empty"> -->
								<div class="tab-pane" id="">
									<div class="">

										<%
											if (mybean.chart_data_total != 0) {
										%>

										<center>
											<div id="chart1" style="height: 700px; width: 1000px;"></div>
										</center>

										<center>
											<b>Total: <%=mybean.chart_data_total%></b>
										</center>

										<%
											}
										%>

										<br><%=mybean.StrHTML%><br>
									</div>
									<!--		</div> -->
								</div>
							</div>
							<%} %>
						</div>
					</div>
				</div>
				<!-- end PORTLET  -->

			</div>
		</div>
	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>
	
	<!-- Start additional plugins -->
	<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
	<script type="text/javascript" src="../Library/amcharts/pie.js"></script>
	<!-- Start Export Image additional plugins -->
<!-- 	<script src="../Library/amcharts/exporting/amexport.js" type="text/javascript"></script> -->
<!-- 	<script src="../Library/amcharts/exporting/rgbcolor.js" type="text/javascript"></script> -->
<!-- 	<script src="../Library/amcharts/exporting/canvg.js" type="text/javascript"></script> -->
<!-- 	<script src="../Library/amcharts/exporting/filesaver.js" type="text/javascript"></script> -->

	<script type="text/javascript">

		//This sholuldn't be removed because it is need branch and model
		function PopulateCRMDays() { 
		}
		function PopulateVariants() { 
		}
		function PopulateColor() { 
		}
		//This sholuldn't be removed because it is need branch and model

		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_brand").options);
			showHint( '../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_brand").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint( '../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}

		function PopulateModels() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_brand").options);
			showHint('../sales/mis-check1.jsp?brand_id=' + brand_id
					+ '&model=yes', 'modelHint');
		}

		function PopulateTeams() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?branch_id=' + branch_id + '&team=yes', 'teamHint');
		}

		function PopulateExecutives() { //v1.0
			var team_id = outputSelected(document.getElementById("dr_team").options);
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?team_id=' + team_id
					+ '&exe_branch_id=' + branch_id + '&executives=yes',
					'exeHint');
		}

		function ExeCheck() {
			var branch_id = document.getElementById("dr_branch").value;
			var team_id = outputSelected(document.getElementById("dr_team").options);
			//showHint('../sales/mis-check.jsp?multiple=yes&team_id='+team_id ,team_id,'exeHint');
			showHint('../sales/mis-check1.jsp?multiple=yes&team_id=' + team_id + '&exe_branch_id=' + GetReplace(branch_id), 'exeHint');
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
				fileName : "SOE"
			}
		};
		AmCharts.ready(function() {
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
		});
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
				fileName : "SOE"
			}
		};
		AmCharts.ready(function() {
			var chartData1 = <%=mybean.cinstatuschart_data%> ;
			// PIE CHART
			chart1 = new AmCharts.AmPieChart();
			chart1.dataProvider = chartData1;
			chart1.titleField = "type";
			chart1.valueField = "total";
			chart1.minRadius = 200;

			// LEGEND
			legend = new AmCharts.AmLegend();
			legend.align = "center";
			legend.markerType = "circle";
			chart1.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
			chart1.exportConfig = export1;
			chart1.addLegend(legend);

			// WRITE
			chart1.write("chart2");
		});
	</script>

</body>
</HTML>
