<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Product_Dash" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</head>

<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Product Dashboard</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY ----->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../sales/report-product-dash.jsp">Product
								Dashboard</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="red"><b> <%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Product
										Dashboard</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="form-element6" >
												<label >End Date:</label>
												<input name="txt_endtime" id="txt_endtime" value="<%=mybean.end_time%>"
													class="form-control datepicker" type="text"  />
											</div>

											<div class="form-element6">
												<label> Brands:</label>
												<div>
													<select name="dr_principal" size="10" multiple="multiple" class="form-control multiselect-dropdown" id="dr_principal"
														onChange="PopulateRegion();PopulateBranches();PopulateModels();">
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

											<div class="form-element4">
												<label> Sales Category:</label>
												<div>
													<%=mybean.PopulateSalesCat()%>
												</div>
											</div>

											<div class="form-element4">
												<label> Items:</label>
												<div id="itemHint">
													<%=mybean.PopulateItems()%>
												</div>
											</div>

											<div class="form-element12">
												<center>
													<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>
										</form>
										
										<center>
											<div class="form-element12" id="amountchartdiv" style="height: 800px;"></div>
											<br>
											<div class="form-element12" id="quantitychartdiv" style="height: 800px;"></div>
										</center>
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
	  
	   var chart;
	   var chart1;
	   var export1 = {
	        menuTop:"-5px",
	        menuItems: [{
	            icon: '../Library/amcharts/images/export.png',
				format: 'jpg'
			}],
			menuItemOutput:{
				fileName: "Product Dashboard"
			}
	   };
	   var export2 = {
	        menuTop:"-5px",
	        menuItems: [{
	            icon: '../Library/amcharts/images/export.png',
				format: 'jpg'
			}],
			menuItemOutput:{
				fileName: "Product Dashboard"
			}
	   };
	   var amountchartData = <%=mybean.amountchart_data%>;
	   var quantitychartData = <%=mybean.quantitychart_data%>;
	            AmCharts.ready(function () {				
	                // SERIAL CHART
	                chart = new AmCharts.AmSerialChart();
	                chart.dataProvider = amountchartData;
	                chart.categoryField = "month";
	                chart.plotAreaBorderAlpha = 0.5;
			        chart.exportConfig = export1;
	                // the following two lines makes chart 3D
	                chart.depth3D = 30;
	                chart.angle = 30;
					chart.type = "serial";
					
	                // category
	                var categoryAxis = chart.categoryAxis;
					categoryAxis.autoRotateAngle = 50;
					categoryAxis.autoRotateCount = 0;
	                categoryAxis.gridAlpha = 0.1;
	                categoryAxis.axisAlpha = 0;
	                categoryAxis.gridPosition = "start";
	
	                // value
	                var valueAxis = new AmCharts.ValueAxis();
	                valueAxis.gridAlpha = 0.1;
	                valueAxis.axisAlpha = 0;
	                chart.addValueAxis(valueAxis);
	
	                // GRAPH
	                var graph = new AmCharts.AmGraph();
	                graph.title = "Amount";
	                graph.labelText = "[[value]]";
	                graph.valueField = "amount";
	                graph.type = "column";
	                graph.lineAlpha = 0;				
	                graph.fillColors = "#13ade8";
	                graph.fillAlphas = 1;
	                graph.balloonText = "<span style='font-size:14px'>[[month]]</span><br><span style='font-size:14px'>[[title]]:<b>[[value]]</b></span>";
	                chart.addGraph(graph);
					
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
	                chart.write("amountchartdiv");
					
	                 //////FOR QUANTITY
	                // SERIAL CHART
	                chart1 = new AmCharts.AmSerialChart();
	                chart1.dataProvider = quantitychartData;
	                chart1.categoryField = "month";
	                chart1.plotAreaBorderAlpha = 0.5;
			        chart1.exportConfig = export2;
	                // the following two lines makes chart 3D
	                chart1.depth3D = 30;
	                chart1.angle = 30;
					chart1.type = "serial";
					
	                // category
	                var categoryAxis1 = chart1.categoryAxis;
					categoryAxis1.autoRotateAngle = 50;
					categoryAxis1.autoRotateCount = 0;
	                categoryAxis1.gridAlpha = 0.1;
	                categoryAxis1.axisAlpha = 0;
	                categoryAxis1.gridPosition = "start";
	
	                // value
	                var valueAxis1 = new AmCharts.ValueAxis();
	                valueAxis1.gridAlpha = 0.1;
	                valueAxis1.axisAlpha = 0;
	                chart1.addValueAxis(valueAxis1);
	                // GRAPH
	                var graph1 = new AmCharts.AmGraph();
	                graph1.title = "Quantity";
	                graph1.labelText = "[[value]]";
	                graph1.valueField = "quantity";
	                graph1.type = "column";
	                graph1.lineAlpha = 0;				
	                graph1.fillColors = "#FFFF00";
	                graph1.fillAlphas = 1;
	                graph1.balloonText = "<span style='font-size:14px'>[[month]]</span><br><span style='font-size:14px'>[[title]]:<b>[[value]]</b></span>";
	                chart1.addGraph(graph1);
					
					 // LEGEND
	                var legend1 = new AmCharts.AmLegend();
	                legend1.borderAlpha = 0.2;
	                legend1.horizontalGap = 10;
					legend1.markerLabelGap = 25;
					legend1.valueAlign = "left";
	                chart1.addLegend(legend1);
					
					 // CURSOR
	                var chartCursor1 = new AmCharts.ChartCursor();
	                chartCursor1.cursorAlpha = 0;
	                chartCursor1.zoomable = false;
	                chartCursor1.categoryBalloonEnabled = false;
	                chart1.addChartCursor(chartCursor1);
	                chart1.creditsPosition = "top-right";
	
	                // WRITE
	                chart1.write("quantitychartdiv");
	            });
	  
	  </script>
	  <!-- End example scripts -->
  
	<script type="text/javascript">
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			//alert("111111------"+brand_id);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&branch=yes', 'branchHint');
		}

		function PopulateTeams() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?branch_id=' + branch_id + '&team=yes', 'teamHint');
		}

		function PopulateExecutives() { //v1.0
			var team_id = outputSelected(document.getElementById("dr_team").options);
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?team_id=' + team_id + '&exe_branch_id='+ branch_id +'&executives=yes', 'exeHint');
		}
		// 	function ItemCheck() { //v1.0
		// 	var branch_id=document.getElementById("dr_branch").value;
		// 	var team_id=outputSelected(document.getElementById("dr_team").options);
		// 	var cat_id=outputSelected(document.getElementById("dr_salescat").options);
		// 	showHint('../sales/mis-check.jsp?multiple=yes&cat_id='+cat_id,branch_id,'itemHint');
		//     }
	</script>
</body>
</html>
