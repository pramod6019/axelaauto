<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Enquiry_Traffic" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
  
<%@include file="../Library/css.jsp"%>

   
</head>
  <body onload="TrafficCheck();" class="page-container-bg-solid page-header-menu-fixed">
  <%@include file="../portal/header.jsp" %>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Enquiry Traffic</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY ----->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
							<li><a href="../sales/report-enquiry-traffic.jsp">Enquiry Traffic</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="red"><b> <%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Enquiry Traffic
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="row">
												<div class="form-element4">
													<label >Traffic: </label>
													<div >
														<select name="dr_traffic" class="form-control" id="dr_traffic" onchange="TrafficCheck()">
															<%=mybean.PopulateTraffic()%>
														</select>
													</div>
												</div>
												
												<div class="form-element4">
													<label >Date<font color="red">*</font></>: </label>
													<div >
														<span id="hourly">
															<input name="txt_endtime" id="txt_endtime" value="<%=mybean.end_time%>"
																class="form-control datepicker" type="text" maxlength="10" />
														</span>
														
														<span class="form-element6 form-element" id="daily">
															<select name="dr_month" id="dr_month" class="form-control">
																<%=mybean.PopulateMonths()%>
															</select>
														</span>
														
														<span class="form-element6 form-element" id="monthly">
															<select name="dr_year" id="dr_year" class="form-control">
																<%=mybean.PopulateYears()%>
															</select>
														</span>
													</div>
												</div>
											
											<!-- 	                                    ====================  -->

												<div class="form-element4">
													<label>Brands:</label>
													<div>
														<select name="dr_brand" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_brand"
															onChange="PopulateBranches();PopulateModels();PopulateRegion();">
																<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
														</select>
													</div>
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
												<label>Model: </label>
												<div id="modelHint">
													<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Teams:</label>
												<div id="teamHint">
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Sales Consultant: </label>
												<div id="exeHint">
													<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
												</div>
											</div>
											
											<div class="form-element3">
												<label>SOE: </label>
												<div id="multisoe">
													<select name="dr_soe" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_soe">
														<%=mybean.PopulateSOE()%>
													</select>
												</div>
											</div>
											
											<div class="form-element12" align="center" >
												<center>
													<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>
											
											<div class="form-element12" id="chartdiv" style="height: 700px;"></div>
											
										</form>
									</div>

								</div>
							</div>
						</div>
					</div>

				</div>
			</div>

		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
		
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
// 	  $("#chartdiv").hide();
	   var chart;
	   var export1 = {
	        menuTop:"-5px",
	        menuItems: [{
	            icon: '../Library/amcharts/images/export.png',
				format: 'jpg'
			}],
			menuItemOutput:{
				fileName: "Enquiry Traffic"
			}
	   };
	   var chartData = <%=mybean.chart_data%>;
	  
           AmCharts.ready(function () {
			
               // SERIAL CHART
               chart = new AmCharts.AmSerialChart();
               chart.dataProvider = chartData;
               chart.categoryField = "hour";
               chart.plotAreaBorderAlpha = 0.5;
	        chart.exportConfig = export1;
               // the following two lines makes chart 3D
               chart.depth3D = 60;
               chart.angle = 30;
			chart.type = "serial";

                // AXES
               // category
               var categoryAxis = chart.categoryAxis;
               categoryAxis.gridAlpha = 0.1;
               categoryAxis.axisAlpha = 0;
               categoryAxis.gridPosition = "start";
			
			var valueAxis = new AmCharts.ValueAxis();
               valueAxis.stackType = "3d"; // This line makes chart 3D stacked (columns are placed one behind another)
			valueAxis.gridAlpha = 0.2;
               valueAxis.gridColor = "#FFFFFF";
               valueAxis.axisColor = "#FFFFFF";
               valueAxis.axisAlpha = 0.5;
               valueAxis.dashLength = 5;
               //valueAxis.unit = "%";
			chart.addValueAxis(valueAxis);
			
			// GRAPHS         
               // first graph
			var graph3 = new AmCharts.AmGraph();
               graph3.title = "Deliveries=<%=mybean.deliverycount%> ";
               graph3.labelText = "[[value]]";
               graph3.valueField = "column-3";
               graph3.type = "column";
               graph3.lineAlpha = 0;
               graph3.lineColor = "#FF0F00";
               graph3.fillAlphas = 1;
               graph3.balloonText = "<span style='font-size:14px'>[[month]]</span><br><span style='font-size:14px'>Deliveries:<b>[[value]]</b></span>";
               chart.addGraph(graph3);
			
			var graph2 = new AmCharts.AmGraph();
               graph2.title = "Sales Orders=<%=mybean.salesordercount%> ";
               graph2.labelText = "[[value]]";
               graph2.valueField = "column-2";
               graph2.type = "column";
               graph2.lineAlpha = 0;
               graph2.lineColor = "#04D215";
               graph2.fillAlphas = 1;
               graph2.balloonText = "<span style='font-size:14px'>[[month]]</span><br><span style='font-size:14px'>Sales Orders:<b>[[value]]</b></span>";
               chart.addGraph(graph2);
			
               var graph1 = new AmCharts.AmGraph();
               graph1.title = "Enquiry=<%=mybean.enquirycount%> ";
               graph1.labelText = "[[value]]";
               graph1.valueField = "column-1";
               graph1.type = "column";
               graph1.lineAlpha = 0;
               graph1.lineColor = "#7ba5de";
               graph1.fillAlphas = 1;
               graph1.balloonText = "<span style='font-size:14px'>[[month]]</span><br><span style='font-size:14px'>Enquiry:<b>[[value]]</b></span>";
               chart.addGraph(graph1);
              
              						
			 // LEGEND
               var legend = new AmCharts.AmLegend();
               legend.borderAlpha = 0.2;
               legend.horizontalGap = 10;
			legend.markerLabelGap = 25;
			legend.valueAlign = "left";
               chart.addLegend(legend);
			
			 // CURSOR
               var chartCursor = new AmCharts.ChartCursor();
               chartCursor.cursorAlpha = 0;
               chartCursor.zoomable = false;
               chartCursor.categoryBalloonEnabled = false;
               chart.addChartCursor(chartCursor);

               chart.creditsPosition = "top-right";

               // WRITE
               chart.write("chartdiv");
           });
	  
	  
  	</script>

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

		function TrafficCheck() {
			var traffic_id = document.getElementById("dr_traffic").value;
			if (traffic_id == 1) {
				document.getElementById("hourly").style.display = "block";
				document.getElementById("daily").style.display = "none";
				document.getElementById("monthly").style.display = "none";
			}
			if (traffic_id == 2) {
				document.getElementById("daily").style.display = 'block';
				document.getElementById("monthly").style.display = 'block';
				document.getElementById("hourly").style.display = 'none';
			}
			if (traffic_id == 3) {
				document.getElementById("monthly").style.display = 'block';
				document.getElementById("hourly").style.display = 'none';
				document.getElementById("daily").style.display = 'none';
			}
		}
	</script>

</body>
</HTML>					
				
