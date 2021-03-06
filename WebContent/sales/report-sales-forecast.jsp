<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Sales_Forecast" scope="request"/>
<%mybean.doPost(request,response); %>
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
						<h1>Sales Forecast</h1>
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
						<li><a href="../sales/report-sales-forecast.jsp">Sales Forecast</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">

							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Sales Forecast
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="form-element4">
												<label> Brands:</label>
												<div >
													<select name="dr_principal" size="10" multiple="multiple" class="form-control multiselect-dropdown"
														id="dr_principal" onChange="PopulateBranches();PopulateRegion();" >
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
											
											<div class="form-element12" align="center">
												<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" />
												<input type="hidden" name="submit_button" value="Submit" />
											</div>
											
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
	<!-- END CONTAINER -->
	<%if(!mybean.enquiry_value.equals("")){%>
	<div class="container-fluid">
		<div class="portlet box">
			<div class="portlet-title" style="text-align: center">
				<div class="caption" style="float: none">&nbsp;</div>
			</div>
			<div class="portlet-body portlet-empty container-fluid">
				<div class="tab-pane" id="">
					<!-- START PORTLET BODY -->
					<div id="followupchartdiv" style="height: 700px;"></div>
				</div>
			</div>
		</div>
	</div>
	<%
		} else if (!mybean.NoSalesForecast.equals("")) {
	%>
	<div class="container-fluid">
		<div class="portlet box green">
			<div class="portlet-title" style="text-align: center">
				<div class="caption" style="float: none">&nbsp;</div>
			</div>
			<div class="portlet-body portlet-empty container-fluid">
				<div class="tab-pane" id="">
					<!-- START PORTLET BODY -->
					<%=mybean.NoSalesForecast%>
				</div>
			</div>
		</div>
	</div>
	<%
		}
	%>

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
	<script class="code" type="text/javascript">

		   var chart;
		   var export1 = {
		        menuTop:"-5px",
		        menuItems: [{
		            icon: '../Library/amcharts/images/export.png',
					format: 'jpg'
				}],
				menuItemOutput:{
					fileName: "CFS Dashboard"
				}
		   };
  
            AmCharts.ready(function () {
				<% if(mybean.NoSalesForecast.equals("")){%>
                var chartData = <%=mybean.enquiry_value%>;
    
                // SERIAL CHART
                chart = new AmCharts.AmSerialChart();
                chart.dataProvider = chartData;
                chart.categoryField = "month";
                chart.plotAreaBorderAlpha = 0.5;
		        chart.exportConfig = export1;
		     // this single line makes the chart a bar chart,
				// try to set it to false - your bars will turn to columns
				chart.rotate = true;
				// the following two lines makes chart 3D
                chart.depth3D = 30;
                chart.angle = 30;
				chart.type = "serial";

                // AXES
                // category
                var categoryAxis = chart.categoryAxis;
                categoryAxis.gridAlpha = 0.1;
                categoryAxis.axisAlpha = 0;
                categoryAxis.gridPosition = "start";

                // value
                var valueAxis = new AmCharts.ValueAxis();
               // valueAxis.dashLength = 5;
                valueAxis.gridAlpha = 0.1;
                valueAxis.axisAlpha = 0;
                chart.addValueAxis(valueAxis);

                // GRAPH
                var graph1 = new AmCharts.AmGraph();
                graph1.title = "Enquiry";
                graph1.labelText = "[[value]]";
                graph1.valueField = "column-1";
                graph1.type = "column";
                graph1.fillAlphas = 1;
                graph1.balloonText = "<span style='font-size:14px'>[[month]]</span><br><span style='font-size:14px'>[[title]]:<b>[[value]]</b></span>";
                chart.addGraph(graph1);
				
				
                var graph2 = new AmCharts.AmGraph();
                graph2.title = "Sales";
                graph2.labelText = "[[value]]";
                graph2.valueField = "column-2";
                graph2.type = "column";
                graph2.fillAlphas = 1;
                graph2.balloonText = "<span style='font-size:14px'>[[month]]</span><br><span style='font-size:14px'>[[title]]:<b>[[value]]</b></span>";
                chart.addGraph(graph2);
                
                var graph2 = new AmCharts.AmGraph();
                graph2.title = "Invoices";
                graph2.labelText = "[[value]]";
                graph2.valueField = "column-3";
                graph2.type = "column";
                graph2.fillAlphas = 1;
                graph2.balloonText = "<span style='font-size:14px'>[[month]]</span><br><span style='font-size:14px'>[[title]]:<b>[[value]]</b></span>";
                chart.addGraph(graph2);
				
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
                chart.write("followupchartdiv");
				<%}%>
            });
 	 </script>
	  
	  <script language="JavaScript" type="text/javascript">
	        function PopulateRegion() { //v1.0
				var brand_id =outputSelected(document.getElementById("dr_principal").options);
				 //alert("111111------"+brand_id);
				showHint('../sales/mis-check1.jsp?multiple=yes&brand_id='+brand_id+'&region=yes','regionHint');
		    }
	   
	        function PopulateBranches() { //v1.0
	    		var brand_id=outputSelected(document.getElementById("dr_principal").options);
	    		var region_id=outputSelected(document.getElementById("dr_region").options);
	    		showHint('../sales/mis-check1.jsp?multiple=yes&brand_id='+brand_id+ '&region_id=' + region_id +'&branch=yes','branchHint');
	    	}
	        
			function PopulateTeams(){		
				var branch_id=outputSelected(document.getElementById("dr_branch").options);	
			    showHint('../sales/mis-check1.jsp?branch_id='+branch_id+'&team=yes', 'teamHint');
			}
				
			function PopulateExecutives() { //v1.0a
				var team_id = outputSelected(document.getElementById("dr_team").options);
				var branch_id = outputSelected(document.getElementById("dr_branch").options);
				showHint('../sales/mis-check1.jsp?team_id=' + team_id + '&exe_branch_id='+ branch_id +'&executives=yes', 'exeHint');
		    }
			
			function PopulateCRMDays(){
			}
	  </script>
	</body>
</HTML>
