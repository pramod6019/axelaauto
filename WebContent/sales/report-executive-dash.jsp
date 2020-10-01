<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Report_Executive_Dash"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</HEAD>

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
						<h1>Sales Consultant Dashboard</h1>
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
							<li><a href="../sales/report-executive-dash.jsp">Sales Consultant Dashboard</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="red"><b><%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Sales Consultant Dashboard</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="form-element6">
												<label>Year:</label>
												<div>
													<select name="dr_year" class="form-control" id="dr_year" >
														<%=mybean.PopulateYears()%>
													</select>
												</div>
											</div>

											<div class="form-element6">
												<label>Month:</label>
												<div id="month" >
													<select name="dr_month" id="dr_month" class="form-control">
														<%=mybean.PopulateMonths()%>
													</select> 
												</div>
											</div>
											
											<div class="form-element2">
												<label>Brands:</label>
												<div>
													<select name="dr_brand" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_brand"
														onChange="PopulateBranches();PopulateModels();PopulateRegion();">
														<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element2">
												<label>Regions: </label>
												<div id="regionHint">
													<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element2">
												<label>Branches:</label>
												<div id="branchHint">
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element2">
												<label>Model: </label>
												<div id="modelHint">
													<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
												</div>
											</div>
											
											<div class="form-element2">
												<label>Teams:</label>
												<div id="teamHint">
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element2">
												<label>Sales Consultant:</label>
												<div  id="exeHint">
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
						</div>
					</div>
				</div>
				<!-- 	main line chart PORTLET -->
				<%
					if (!mybean.chart_data.equals("")) {
				%>
				<div class="portlet box  ">
					<div class="portlet-title" style="text-align: center">
						<div class="caption" style="float: none"></div>
					</div>
					<div class="portlet-body portlet-empty">
						<div class="tab-pane" id="">
							<!-- START PORTLET BODY -->
							<div id="chartdiv" style="height: 800px;"></div>
							<div><%=mybean.Str1.toString()%></div>
							<div><%=mybean.Str3.toString()%></div>
							<div><%=mybean.Str2.toString()%></div>
						</div>
					</div>
				</div>
				<%
					}
				%>


				<!-- soe pie chart -->
				<%
					if (!mybean.chart_data_soe.equals("")) {
				%>

				<div class="portlet box  ">
					<div class="portlet-title" style="text-align: center">
						<div class="caption" style="float: none">
							<%=mybean.monthAndYear%> SOE
						</div>
					</div>
					<div class="portlet-body portlet-empty">
						<div class="tab-pane" id="">
							<%
								if (mybean.NoChart_soe.equals("")) {
							%>
							<div id="chart_soe" style="height: 800px;"></div>
							<center>
								<b>Total: <%=mybean.chart_data_total_soe%></b>
							</center>
							<%
								} else {
							%>
							<center>
								<div>
									<b><font color="red"><%=mybean.NoChart_soe%></font></b>
								</div>
							</center>
							<%
								}
							%>
						</div>
					</div>
				</div>
				<%
					}
				%>
				<!-- 				sob pie chart -->
				<%
					if (!mybean.chart_data_sob.equals("")) {
				%>
				<div class="portlet box  ">
					<div class="portlet-title" style="text-align: center">
						<div class="caption" style="float: none">
							<%=mybean.monthAndYear%> SOB
						</div>
					</div>
					<div class="portlet-body portlet-empty">
						<div class="tab-pane" id="">
							<%
								if (mybean.NoChart_sob.equals("")) {
							%>
							<div id="chart_sob" style="height: 800px;"></div>
							<center>
								<b>Total: <%=mybean.chart_data_total_sob%></b>
							</center>
							<%
								} else {
							%>
							<center>
								<div>
									<b><font color="red"><%=mybean.NoChart_sob%></font></b>
								</div>
							</center>
							<%
								}
							%>

						</div>
					</div>
				</div>
				<%
					}
				%>
				<!-- 				stage pie chart-->
				<%
					if (!mybean.chart_data_stage.equals("")) {
				%>
				<div class="portlet box  ">
					<div class="portlet-title" style="text-align: center">
						<div class="caption" style="float: none">
							<%=mybean.monthAndYear%> STAGE
						</div>
					</div>
					<div class="portlet-body portlet-empty">
						<div class="tab-pane" id="">
							<%
								if (mybean.NoChart_stage.equals("")) {
							%>
							<div id="chart_stage" style="height: 500px;"></div>
							<center>
								<b>Total: <%=mybean.chart_data_total_stage%></b>
							</center>
							<%
								} else {
							%>
							<center>
								<div>
									<b><font color="red"><%=mybean.NoChart_stage%></font></b>
								</div>
							</center>
							<%
								}
							%>

						</div>
					</div>
				</div>
				<%
					}
				%>
				<!-- 				status pie chart -->
				<%
					if (!mybean.chart_data_status.equals("")) {
				%>
				<div class="portlet box  ">
					<div class="portlet-title" style="text-align: center">
						<div class="caption" style="float: none">
							<%=mybean.monthAndYear%> STATUS
						</div>
					</div>
					<div class="portlet-body portlet-empty">
						<div class="tab-pane" id="">
							<%
								if (mybean.NoChart_status.equals("")) {
							%>
							<div id="chart_status" style="height: 500px;"></div>
							<center>
								<b>Total: <%=mybean.chart_data_total_status%></b>
							</center>
							<%
								} else {
							%>
							<center>
								<div>
									<b><font color="red"><%=mybean.NoChart_status%></font></b>
								</div>
							</center>
							<%
								}
							%>

						</div>
					</div>
				</div>
				<%
					}
				%>
				<!-- 				priority pie chart -->
				<%
					if (!mybean.chart_data_priority.equals("")) {
				%>
				<div class="portlet box ">
					<div class="portlet-title" style="text-align: center">
						<div class="caption" style="float: none">
							<%=mybean.monthAndYear%> PRIORITY
						</div>
					</div>
					<div class="portlet-body portlet-empty">
						<div class="tab-pane" id="">
							<%
								if (mybean.NoChart_priority.equals("")) {
							%>
							<div id="chart_priority" style="height: 500px;"></div>
							<center>
								<b>Total: <%=mybean.chart_data_total_priority%></b>
							</center>
							<%
								} else {
							%>
							<center>
								<div>
									<b><font color="red"><%=mybean.NoChart_priority%></font></b>
								</div>
							</center>
							<%
								}
							%>

						</div>
					</div>
				</div>
				<%
					}
				%>
				<!-- 				escalation -->
				<%
					if (!mybean.StrHTML.equals("")) {
				%>
				<center>
					<div><%=mybean.StrHTML%></div>
				</center>

				<%
					}
				%>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%@ include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

	<!-- Additional plugins go here -->
	<script type="text/javascript" src="../axelaauto-assets/js/amcharts.js"></script>
	<script type="text/javascript" src="../axelaauto-assets/js/serial.js"></script>
	<script type="text/javascript" src="../Library/amcharts/pie.js"></script>
	<!-- Start Export Image additional plugins /patterns-->
	<script src="../Library/amcharts/exporting/amexport.js" type="text/javascript"></script>
	<script src="../Library/amcharts/exporting/rgbcolor.js" type="text/javascript"></script>
	<script src="../Library/amcharts/exporting/canvg.js" type="text/javascript"></script>
	<script src="../Library/amcharts/exporting/filesaver.js" type="text/javascript"></script>
	<!-- End Export Image additional plugins -->
	<!-- End additional plugins -->

	<!-- main line chart starting -->
	<script type="text/javascript">
		var chart;
		var export1 = {
			menuTop : "-5px",
			menuItems : [ {
				icon : '../Library/amcharts/images/export.png',
				format : 'jpg'
			} ],
			menuItemOutput : {
				fileName : "Executive Dashboard"
			}
		};
		var chart;
		var chartData = [];

		AmCharts.ready(function() {
			// generate some random data first
			//                generateChartData();

			// SERIAL CHART
			chart = new AmCharts.AmSerialChart();
			var chartdata = <%=mybean.chart_data%>;
               chart.dataProvider = chartdata;
               chart.categoryField = "month";
               // listen for "dataUpdated" event (fired when chart is inited) and call zoomChart method when it happens
               chart.addListener("dataUpdated", zoomChart);

               chart.synchronizeGrid = true; // this makes all axes grid to be at the same intervals

               // AXES
               // category
               var categoryAxis = chart.categoryAxis;
               categoryAxis.parseDates = false; // as our data is date-based, we set parseDates to true
               categoryAxis.minPeriod = "MM"; // our data is daily, so we set minPeriod to DD
               categoryAxis.minorGridEnabled = true;
               categoryAxis.axisColor = "#DADADA";
               categoryAxis.twoLineMode = true;

               // third value axis (on the left, detached)
              var valueAxis = new AmCharts.ValueAxis();
               valueAxis.axisColor = "#FF6600";
               valueAxis.axisThickness = 2;
               chart.addValueAxis(valueAxis);

               // GRAPHS
               // first graph
               var graph1 = new AmCharts.AmGraph();
               graph1.valueAxis = valueAxis; // we have to indicate which value axis should be used
               graph1.labelText = "[[value]]";
               graph1.title = "Enquiries";
               graph1.valueField = "column-1";
               graph1.bullet = "round";
               graph1.hideBulletsCount = 30;
               graph1.lineThickness = 4;
               graph1.bulletBorderThickness = 1;
               chart.addGraph(graph1);
               // second graph
               var graph2 = new AmCharts.AmGraph();
               graph2.valueAxis = valueAxis; // we have to indicate which value axis should be used
               graph2.labelText = "[[value]]";
               graph2.title = "Test Drives";
               graph2.valueField = "column-2";
               graph2.bullet = "square";
               graph2.hideBulletsCount = 30;
               graph2.lineThickness = 4;
               graph2.bulletBorderThickness = 1;
               chart.addGraph(graph2);

               // third graph
               var graph3 = new AmCharts.AmGraph();
               graph3.valueAxis = valueAxis; // we have to indicate which value axis should be used
               graph3.labelText = "[[value]]";
               graph3.valueField = "column-3";
               graph3.title = "Bookings";
               graph3.bullet = "triangleUp";
               graph3.hideBulletsCount = 30;
               graph3.lineThickness = 4;
               graph3.bulletBorderThickness = 1;
               chart.addGraph(graph3);
			   
			   var graph4 = new AmCharts.AmGraph();
               graph4.valueAxis = valueAxis; // we have to indicate which value axis should be used
               graph4.labelText = "[[value]]";
               graph4.valueField = "column-4";
               graph4.title = "Deliveries";
               graph4.bullet = "rectangle";
               graph4.hideBulletsCount = 20;
               graph4.lineThickness = 4;
               graph4.bulletBorderThickness = 1;
               chart.addGraph(graph4);

               // CURSOR
               var chartCursor = new AmCharts.ChartCursor();
               chartCursor.cursorAlpha = 0.1;
               chartCursor.fullWidth = true;
               chartCursor.valueLineBalloonEnabled = true;
               chart.addChartCursor(chartCursor);

               // SCROLLBAR
//                var chartScrollbar = new AmCharts.ChartScrollbar();
//                chart.addChartScrollbar(chartScrollbar);

               // LEGEND
               var legend = new AmCharts.AmLegend();
               legend.marginLeft = 110;
               legend.useGraphSettings = true;
               chart.addLegend(legend);

               // WRITE
               chart.write("chartdiv");
           });

           
           // this method is called when chart is first inited as we listen for "dataUpdated" event
           function zoomChart() {
               // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
//                chart.zoomToIndexes(10, 20);
           }
        </script>
	<!-- main line chart ending -->

	<!-- SOE piechart starting -->

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
		<%if (mybean.NoChart.equals("")) {%>
			var chartData = <%=mybean.chart_data_soe%> ;
					// PIE CHART
					chart = new AmCharts.AmPieChart();
					chart.dataProvider = chartData;
					chart.titleField = "type";
					chart.valueField = "total";
					chart.minRadius = 150;

					// LEGEND
					legend = new AmCharts.AmLegend();
					legend.align = "center";
					legend.markerType = "circle";
					chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
					chart.exportConfig = export1;
					chart.addLegend(legend);

					// WRITE
					chart.write("chart_soe");
		<%}%>
		});
	</script>
	<!-- SOE piechart ending -->

	<!-- SOB piechart starting -->
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
				fileName : "SOB"
			}
		};
		AmCharts.ready(function() {
		<%if (mybean.NoChart_sob.equals("")) {%>
			var chartData = <%=mybean.chart_data_sob%> ;
					// PIE CHART
					chart = new AmCharts.AmPieChart();
					chart.dataProvider = chartData;
					chart.titleField = "type";
					chart.valueField = "total";
					chart.minRadius = 150;

					// LEGEND
					legend = new AmCharts.AmLegend();
					legend.align = "center";
					legend.markerType = "circle";
					chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
					chart.exportConfig = export1;
					chart.addLegend(legend);

					// WRITE
					chart.write("chart_sob");
		<%}%>
		});
	</script>
	<!-- SOB piechart ending -->

	<!-- Stage piechart Starting -->

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
				fileName : "STAGE"
			}
		};
		AmCharts.ready(function() {
		<%if (mybean.NoChart_stage.equals("")) {%>
			var chartData = <%=mybean.chart_data_stage%> ;
					// PIE CHART
					chart = new AmCharts.AmPieChart();
					chart.dataProvider = chartData;
					chart.titleField = "type";
					chart.valueField = "total";
					chart.minRadius = 150;

					// LEGEND
					legend = new AmCharts.AmLegend();
					legend.align = "center";
					legend.markerType = "circle";
					chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
					chart.exportConfig = export1;
					chart.addLegend(legend);

					// WRITE
					chart.write("chart_stage");
		<%}%>
		});
	</script>
	<!-- Stage piechart ending -->


	<!-- Status piechart Strating -->

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
				fileName : "STATUS"
			}
		};
		AmCharts.ready(function() {
		<%if (mybean.NoChart_status.equals("")) {%>
			var chartData = <%=mybean.chart_data_status%> ;
					// PIE CHART
					chart = new AmCharts.AmPieChart();
					chart.dataProvider = chartData;
					chart.titleField = "type";
					chart.valueField = "total";
					chart.minRadius = 150;

					// LEGEND
					legend = new AmCharts.AmLegend();
					legend.align = "center";
					legend.markerType = "circle";
					chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
					chart.exportConfig = export1;
					chart.addLegend(legend);

					// WRITE
					chart.write("chart_status");
		<%}%>
		});
	</script>
	<!-- Status piechart Ending -->

	<!-- Priority piechart Starting -->
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
				fileName : "PRIORITY"
			}
		};
		AmCharts.ready(function() {
		<%if (mybean.NoChart_priority.equals("")) {%>
			var chartData = <%=mybean.chart_data_priority%> ;
					// PIE CHART
					chart = new AmCharts.AmPieChart();
					chart.dataProvider = chartData;
					chart.titleField = "type";
					chart.valueField = "total";
					chart.minRadius = 150;

					// LEGEND
					legend = new AmCharts.AmLegend();
					legend.align = "center";
					legend.markerType = "circle";
					chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
					chart.exportConfig = export1;
					chart.addLegend(legend);

					// WRITE
					chart.write("chart_priority");
		<%}%>
		});
	</script>
	<!-- Priority piechart Ending -->



	<!-- End example scripts -->
	<script language="JavaScript" type="text/javascript">

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
	</script>
</body>
</HTML>
