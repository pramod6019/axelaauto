<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Index" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

<style>
@media screen and (min-width: 990px) {
	.plot {
		width: 370px;
	}
}
</style>
<style>

@media screen and (min-width: 992px) {
	.dropdown-toggle{ left:15px; } 
}


.btn.btn-outline.blue-oleo.active, .btn.btn-outline.blue-oleo:active,
	.btn.btn-outline.blue-oleo:active:focus, .btn.btn-outline.blue-oleo:active:hover,
	.btn.btn-outline.blue-oleo:focus, .btn.btn-outline.blue-oleo:hover {
	border-color: #94A0B2;
	color: #FFF;
	background-color: #94A0B2;
}

.dashboard-stat2 {
	box-shadow: 2px 2px 2px #888888;
}

 .dashboard-stat2 .display .number h3 {
        margin: 0 0 2px 0;
        padding: 0;
        font-size: 25px;
        font-weight: 400; }
        
</style>


</head>

<body class="page-container-bg-solid page-header-menu-fixed" onload="allFilter();">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Sales</h1>
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
						<li><a href="../sales/index.jsp">Sales</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="container-fluid">

								<!-- 		=====================start card================ -->

								<div class="col-md-12 col-sm-12">

									<div class="portlet light ">
										<div class="portlet-title">
											<div class="caption caption-md">
												<i class="icon-bar-chart font-dark hide"></i>
												<span class="caption-subject font-green-steel uppercase bold">Sales Summary</span>
<!-- 												<span class="caption-helper hide">weekly stats...</span> -->
											</div>
											<div class="actions col-md-3 col-sm-3" style="text-align: right;margin-top: -23px;">
												<div class="btn-group btn-group-devided" data-toggle="buttons">
													<label class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm active" id='today'>
													<input type="radio" name="dateopt" onchange="count(this.value);getvalue(this.value);" id="option1" value="today">Today </label>
													<label class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm" id='month'>
													<input type="radio" name="dateopt" onchange="count(this.value);getvalue(this.value);" id="option2" value="month">Month</label>
													<label class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm" id='quarter'>
													<input type="radio" name="dateopt" onchange="count(this.value);getvalue(this.value);" id="option3" value="quarter">Quarter </label>
												</div>
											</div>
										</div>

										<div class="actions col-md-12">
											<form name="form1" class="form-horizontal" id="form1">
												<div class="form-body col-md-3 col-sm-3">
													<div class="form-group">
														<label class="control-label col-md-4 col-sm-4">Brand:</label>
														<div class="col-md-6" >
															<span id="multiprincipal">
																<select name="dr_principal" size="10" multiple="multiple"
																	class="form-control service_element multiselect-dropdown" id="dr_principal"
																	onChange="PopulateBranches();PopulateRegion();"> 
																	<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
																</select>
															</span>
														</div>
													</div>
												</div>
												
												<div class="form-body col-md-3 col-sm-3">
													<div class="form-group">
														<label class="control-label col-md-4 col-sm-4">Region:</label>
														<div class="col-md-6">
															<span id="regionHint">
																<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
															</span>
														</div>
													</div>
												</div>
												<div class="form-body col-md-3 col-sm-3">
													<div class="form-group">
														<label class="control-label col-md-4 col-sm-4">Branch:</label>
														<div class="col-md-6" >
 															<span id="branchHint"> 
																<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
															</span> 
														</div>
													</div>
												</div>
										</div>
										</form>
										<input type='text' value='today' id='cardvalue' name='cardvalue' hidden />

										<div class="portlet-body" id="portlet">
											<div id="dashbox" style="margin-left: -29px;" class="row">
												<center>
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
													<a>
														<div class="dashboard-stat2" id="enquiry" onclick="checkcall(this.id);">
															<div class="display" id='hint'>
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-green-sharp">
																		<span data-counter="counterup" id='enquiry_count'></span>
																	</h3>
																	<small>Enquiry</small>
																</div>
																<div class="col-md-5 col-xs-5">
																	<i class='btn-sm bg-green-sharp icon-btn col-md-5'>
																		<i class="fa fa-list-alt" style="font-size: xx-large;margin-top: 5px;"></i> 
																	</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success green-sharp" id='enquiry_count_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='enquiry_count_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
													
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<a>
														<div class="dashboard-stat2" id="testdrive" onclick="checkcall(this.id);">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-red-haze">
																		<span data-counter="counterup" id='testdrive_count'></span>
																	</h3>
																	<small>Test Drive</small>
																</div>
																<div class="col-md-5">
																	<i class='btn-sm bg-red-haze icon-btn '>
																	<img src="../admin-ifx/test-drive.png" style="width: 51px;margin-top:-8px" alt="test-drive"></img>
																	</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success red-haze" id='testdrive_count_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='testdrive_count_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<a>
														<div class="dashboard-stat2" id="so" onclick="checkcall(this.id);">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-purple-soft">
																		<span data-counter="counterup" id='so_count'></span>
																	</h3>
																	<small>Sales Order</small>
																</div>
																<div class="col-md-5">
																	<i class='btn-sm icon-btn col-md-7' style="background-color: #a495c3">
																		<i class="fa fa-bar-chart" style="font-size: xx-large;margin-top: 5px;"></i>
																	</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success purple-soft" id='so_count_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='so_count_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
													
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<a>
														<div class="dashboard-stat2"  id="retail" onclick="checkcall(this.id);">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-blue-sharp">
																		<span data-counter="counterup" id='retail_count'></span>
																	</h3>
																	<small>Retail</small>
																</div>
																<div class="col-md-5">
																	<i class='btn-sm bg-blue-sharp icon-btn col-md-5'>
																		<i class="fa fa-cart-plus" style="font-size: xx-large;margin-top: 5px;"></i>
																	</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success blue-sharp" id='retail_count_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='retail_count_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
													
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<a>
														<div class="dashboard-stat2" id="delivered" onclick="checkcall(this.id);">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-blue-soft">
																		<span data-counter="counterup" id='delivery_count' ></span>
																	</h3>
																	<small>Delivered</small>
																</div>
																<div class="col-md-5">
																<i class='btn-sm icon-btn col-md-7' style="background-color:#f5b063 ">
<!-- 																<i class="fa fa-key" style="font-size: xx-large;margin-top: 5px;"></i> -->
																<img src="../admin-ifx/delivered1.png" style="width: 51px;margin-top: -4px;height: 35px;" alt="Delivered"></img>
																</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success bg-blue-soft" id='delivery_count_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='delivery_count_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<a>
														<div class="dashboard-stat2" id="cancelled" onclick="checkcall(this.id);">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-yellow-soft">
																		<span data-counter="counterup" id='cancelled_count' ></span>
																	</h3>
																	<small>Cancelled</small>
																</div>
																<div class="col-md-5">
																<i class='btn-sm bg-yellow-soft icon-btn'>
																<i class="fa fa-ban" style="font-size: xx-large;margin-top: 5px;"></i>
<!-- 																	<div>Users</div> --> 
																</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success yellow-soft" id='cancelled_count_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='cancelled_count_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
												</center>
											</div>
										</div>
									</div>
								</div>

								<!--  =====================end card================ -->

								<div class="col-md-4 col-sm-6 col-xs-12">
									<div class="portlet box  " style="padding: 5px">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Escalation</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<div id="followupchartdiv" style="height: 300px;"></div>
												<br> <br> <br>
												<div id="crmfollowupchartdiv" style="height: 300px;"></div>
												<br> <br> <br>
												<div id="psfchartdiv" style="height: 300px;"></div>
											</div>
										</div>
									</div>
								</div>
								
								<div class="col-md-4">
								<div class="col-md-12  col-sm-6 col-xs-12">
									<div class="portlet box  " style="padding: 5px margin-bottom: -34px;">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Sales Funnel</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<center>
<!-- 												<div id="salesfunneldiv" class="plot" style="height: 450px;"></div> -->
													<% if (mybean.NoSalesPipeline.equals("")) { %>
													<div class="example-content">
														<style type="text/css"> .jqplot-target { margin: 5px; } </style>
														<div id="salesfunneldiv" class="plot" style="height: 450px;"></div>
													</div>
													<% } else { %>
													<%=mybean.NoSalesPipeline%>
													<% } %>
												</center>
											</div>
										</div>
									</div>
								</div>
<!-- 								<div class="col-md-12"> -->
								<div class="col-md-12 col-sm-6 col-xs-12">
									<div class="portlet box  " style="padding: 5px; margin-top: 17px;"" >
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Priority Status</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<center>
<!-- 												<div id="salesfunneldiv" class="plot" style="height: 450px;"></div> -->
													<% if (mybean.no_enquirypriority.equals("")) { %>
													<div class="example-content">
													<div id="prioritychartdiv" class="plot" style="height: 450px;"></div>
<!-- 														<div id="prioritychartdiv"></div> -->
													</div>
													<% } else { %>
													<%=mybean.no_enquirypriority%>
													<% } %>
												</center>
											</div>
										</div>
<!-- 									</div> -->
								</div>
								</div>
								</div>
								<div class=" col-md-4 col-sm-6 col-xs-12">
									<div class="portlet box" style="padding: 5px">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Sales Reports
											</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<%=mybean.ListReports()%>
											</div>
										</div>
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
<!-- 	<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script> -->
<!-- 	<script type="text/javascript" src="../Library/amcharts/funnel.js"></script> -->
<!-- 	<script type="text/javascript" src="../Library/amcharts/serial.js" ></script> -->
<!-- 	<script type="text/javascript" src="../Library/amcharts/pie.js"></script> -->

	<script type="text/javascript" src="../Library/amcharts-v3/amcharts-v3.js"></script>
	<script type="text/javascript" src="../Library/amcharts-v3/funnel-v3.js"></script>
	<script type="text/javascript" src="../Library/amcharts-v3/serial-v3.js"></script>
	<script type="text/javascript" src="../Library/amcharts-v3/pie-v3.js"></script>
	<script type="text/javascript" src="../Library/amcharts-v3/light-v3.js"></script>



	<script language="javascript" type="text/javascript">
		
		function LoadAmChart(jsonstring,hint){
			var chart;
			switch (hint){
					
			case 'followupchartdiv':		
				
				var followupchartData =  JSON.parse(jsonstring);
				
			// SERIAL CHART FOR FOLLOWUP
				var followupchart = new AmCharts.AmSerialChart();
				followupchart.dataProvider = followupchartData;
				followupchart.categoryField = "level";
				// this single line makes the chart a bar chart,
				// try to set it to false - your bars will turn to columns
				followupchart.rotate = true;
				// the following two lines makes chart 3D
				followupchart.depth3D = 20;
				followupchart.angle = 30;
				followupchart.startDuration = 2;
				followupchart.startEffect = "easeOutSine";
				followupchart.marginsUpdated = false;
				followupchart.autoMarginOffset = 0;
				followupchart.marginLeft = 0;
				followupchart.marginRight = 0;
				
				// AXES
				// Category
				var categoryAxis1 = followupchart.categoryAxis;
				categoryAxis1.gridPosition = "start";
				categoryAxis1.axisColor = "#DADADA";
				categoryAxis1.fillAlpha = 1;
				categoryAxis1.gridAlpha = 1;
				categoryAxis1.gridColor = "#DADADA";
				categoryAxis1.fillColor = "#FAFAFA";
				// value
				var valueAxis1 = new AmCharts.ValueAxis();
				valueAxis1.axisColor = "#DADADA";
				valueAxis1.title = "Enquiry Follow-up Escalation";
				//valueAxis1.position = "top";
				valueAxis1.gridAlpha = 1;
				valueAxis1.gridColor = "#DADADA";
				followupchart.addValueAxis(valueAxis1);
		
				// GRAPH
				var followupgraph = new AmCharts.AmGraph();
				followupgraph.title = "Followup";
				followupgraph.valueField = "value";
				followupgraph.urlField = "url";
				followupgraph.urlTarget = "_target";
				followupgraph.type = "column";
				followupgraph.balloonText = "[[category]]:[[value]]";
				followupgraph.lineAlpha = 0;				
				followupgraph.colorField = "color";
				followupgraph.fillAlphas = 1;
				followupgraph.labelText = "[[value]]";
				followupgraph.labelPosition = "top";
				followupchart.addGraph(followupgraph);
		
				followupchart.creditsPosition = "top-right";
		
				// WRITE
				followupchart.write("followupchartdiv");		
				break;
				
			case 'crmfollowupchartdiv':
				
				var crmfollowupchartData = JSON.parse(jsonstring);
				
				// SERIAL CHART FOR CRM FOLLOW-UP
				var crmfollowupchart = new AmCharts.AmSerialChart();
				crmfollowupchart.dataProvider = crmfollowupchartData;
				crmfollowupchart.categoryField = "level";
				// this single line makes the chart a bar chart,
				// try to set it to false - your bars will turn to columns
				crmfollowupchart.rotate = true;
				// the following two lines makes chart 3D
				crmfollowupchart.depth3D = 20;
				crmfollowupchart.angle = 30;
				crmfollowupchart.startDuration = 1;
				crmfollowupchart.startEffect = "easeOutSine";
				crmfollowupchart.marginsUpdated = false;
				crmfollowupchart.autoMarginOffset = 0;
				crmfollowupchart.marginLeft = 0;
				crmfollowupchart.marginRight = 0;
			//	crmfollowupchart.panEventsEnabled : false;
				// AXES
				// Category
				var categoryAxis2 = crmfollowupchart.categoryAxis;
				categoryAxis2.gridPosition = "start";
				categoryAxis2.axisColor = "#DADADA";
				categoryAxis2.fillAlpha = 1;
				categoryAxis2.gridAlpha = 1;
				categoryAxis2.gridColor = "#DADADA";
				categoryAxis2.fillColor = "#FAFAFA";
				// value
				var valueAxis2 = new AmCharts.ValueAxis();
				valueAxis2.axisColor = "#DADADA";
				valueAxis2.title = "CRM Follow-up Escalation";
				valueAxis2.gridAlpha = 1;
				valueAxis2.gridColor = "#DADADA";
		// 		valueAxis2.panEventsEnabled : false;
				crmfollowupchart.addValueAxis(valueAxis2);
		
				// GRAPH
				var crmfollowupgraph = new AmCharts.AmGraph();
				crmfollowupgraph.title = "Call";
				crmfollowupgraph.valueField = "value";
				crmfollowupgraph.urlField = "url";
				crmfollowupgraph.urlTarget = "_target";
				crmfollowupgraph.type = "column";
				crmfollowupgraph.balloonText = "[[category]]:[[value]]";
				crmfollowupgraph.lineAlpha = 0;				
				crmfollowupgraph.colorField = "color";
				crmfollowupgraph.fillAlphas = 1;
				crmfollowupgraph.labelText = "[[value]]";
				crmfollowupgraph.labelPosition = "top";
				crmfollowupchart.addGraph(crmfollowupgraph);
				crmfollowupchart.creditsPosition = "top-right";
				// WRITE
				crmfollowupchart.write("crmfollowupchartdiv");
				
				break;
				
			case 'salesfunneldiv':
				
				var data = JSON.parse(jsonstring);
				chart = new AmCharts.AmFunnelChart();
				chart.titleField = "title";
				chart.marginRight = 150;
				chart.marginLeft = 15;
				chart.labelPosition = "right";
				chart.valueField = "total";
				chart.dataProvider = data;
				chart.startX = -800;
				chart.depth3D = 100;
				chart.angle = 40;
				chart.outlineAlpha = 1;
				chart.outlineColor = "#FFFFFF";
				chart.outlineThickness = 2;
				chart.balloon.drop = true;
				chart.balloon.adjustBorderColor = false;
				chart.balloon.color = "white";
				chart.balloon.fontSize = 10;
				chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[total]]</b> ([[percents]]%)</span>";
				chart.balloonText = "[[title]]:<b>[[total]]</b>";
				chart.write("salesfunneldiv");

				break;


			case 'prioritychartdiv':
				var chart = new AmCharts.AmPieChart();
				var data = JSON.parse(jsonstring);
				chart.theme = "light";
				chart.dataProvider = data;
				chart.titleField = "title";
				chart.valueField = "value";
				chart.minRadius = 150;
				chart.innerRadius = "50%";
// 				chart.depth3D =  30;
				chart.gradientRatio = [-0.4, -0.4, -0.4, -0.4, -0.4, -0.4, 0, 0.1, 0.2, 0.1, 0, -0.2, -0.5];
				chart.colorField = "color";
				chart.urlField = "dataurl";
				chart.urlTarget = "_target";
				chart.balloon.drop = true;
				chart.balloon.adjustBorderColor = false;
				chart.balloon.color = "white";
				chart.balloon.fontSize = 10;
				chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";

				// WRITE
				chart.write("prioritychartdiv");

			}
		}
	</script>

	<script type="text/javascript">
		
		
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() { //v1.0
			
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
			
		}
		
		
		var carddata;
		var bardata;
		var periodType = "today";

		function count(type) {
			periodType = type;
			$('#cardvalue').val(type);

			switch(type){
			case 'today':
				count_type_loop(carddata[0], "enquiry_count");
				count_type_loop(carddata[3], "testdrive_count");
				count_type_loop(carddata[6], "so_count");
				count_type_loop(carddata[9], "retail_count");
				count_type_loop(carddata[12], "delivery_count");
				count_type_loop(carddata[15], "cancelled_count");

				bar_type_loop(bardata[0],carddata[0], "enquiry_count");
				bar_type_loop(bardata[3],carddata[3], "testdrive_count");
				bar_type_loop(bardata[6],carddata[6], "so_count");
				bar_type_loop(bardata[9],carddata[9], "retail_count");
				bar_type_loop(bardata[12],carddata[12], "delivery_count");
				bar_type_loop(bardata[15],carddata[15], "cancelled_count");
				
				break;
			
			case 'month':
				count_type_loop(carddata[1], "enquiry_count");
				count_type_loop(carddata[4], "testdrive_count");
				count_type_loop(carddata[7], "so_count");
				count_type_loop(carddata[10], "retail_count");
				count_type_loop(carddata[13], "delivery_count");
				count_type_loop(carddata[16], "cancelled_count");
				
				bar_type_loop(bardata[1],carddata[1], "enquiry_count");
				bar_type_loop(bardata[4],carddata[4], "testdrive_count");
				bar_type_loop(bardata[7],carddata[7], "so_count");
				bar_type_loop(bardata[10],carddata[10], "retail_count");
				bar_type_loop(bardata[13],carddata[13], "delivery_count");
				bar_type_loop(bardata[16],carddata[16], "cancelled_count");
				
				break;
			
			case 'quarter':
				count_type_loop(carddata[2],"enquiry_count");
				count_type_loop(carddata[5], "testdrive_count");
				count_type_loop(carddata[8], "so_count");
				count_type_loop(carddata[11], "retail_count");
				count_type_loop(carddata[14], "delivery_count");
				count_type_loop(carddata[17], "cancelled_count");
				
				bar_type_loop(bardata[2],carddata[2], "enquiry_count");
				bar_type_loop(bardata[5],carddata[5], "testdrive_count");
				bar_type_loop(bardata[8],carddata[8], "so_count");
				bar_type_loop(bardata[11],carddata[11], "retail_count");
				bar_type_loop(bardata[14],carddata[14], "delivery_count");
				bar_type_loop(bardata[17],carddata[17], "cancelled_count");

				break;
			}
		}

		function count_type_loop(mybean_value, Hint){
			var t= 100;
			if(mybean_value>999999){
				mybean_value = eval(mybean_value/100000).toFixed(3);
				for(i = 0, j = eval(mybean_value/10), t=100 ; Math.round(i) <= mybean_value ; i=i+j, t=t+100){
					setTimeout('document.getElementById("' + Hint + '").innerHTML=' + Math.round(i) +'+"L"', t) ;
					if(mybean_value==0 || mybean_value=="" || mybean_value==null){break;}
				}
			}else{
				for(i = 0, j = eval(mybean_value/10), t=100 ; Math.round(i) <= mybean_value ; i=i+j, t=t+100){
					setTimeout('document.getElementById("' + Hint + '").innerHTML=' + Math.round(i), t) ;
					if(mybean_value==0 || mybean_value=="" || mybean_value==null){break;}
				}
			}
		}

		function bar_type_loop(mybean_old_value, mybean_new_value, Hint){
			var t = 100;
			if(mybean_old_value > 0){
				var per = Math.round(eval((mybean_new_value * 100)/mybean_old_value));
					for(i = 0, j = eval(per/10), t=100 ; Math.round(i) <= per ; i=i+j, t=t+100){
						setTimeout('document.getElementById("' + Hint + '_label").innerHTML=' + Math.round(i)+'+"%";', t) ;
						setTimeout('document.getElementById("' + Hint + '_bar").style.width="' + Math.round(i)+'%";', t) ;
						if(per==0 || per=="" || per==null){break;}
					}
			}else {
				if(mybean_old_value == 0 && mybean_new_value > 0){
					var per = Math.round(100);
					for(i = 0, j = eval(per/10), t=100 ; Math.round(i) <= per ; i=i+j, t=t+100){
						setTimeout('document.getElementById("' + Hint + '_label").innerHTML=' + Math.round(i)+'+"%";', t) ;
						setTimeout('document.getElementById("' + Hint + '_bar").style.width="' + Math.round(i)+'%";', t) ;
					}
				}else{
					setTimeout('document.getElementById("' + Hint + '_label").innerHTML="0%";', t) ;
					setTimeout('document.getElementById("' + Hint + '_bar").style.width="0%";', t) ;
				}
			}
		}

//			end populate cards

		function allFilter(){
			
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			var dr_branch_id = outputSelected(document.getElementById("dr_branch").options);
		
			var url="../sales/index-check.jsp?refreshAll=yes&brand_id=" + brand_id + "&region_id="+region_id+"&dr_branch_id="+dr_branch_id;
			// for Am-Chart
			setTimeout('showHintChart("'+url+'&followupstatus=yes", "followupchartdiv");',100);
			setTimeout('showHintChart("'+url+'&crmfollowupstatus=yes", "crmfollowupchartdiv");',90);
			setTimeout('showHintChart("'+url+'&salesfunnel=yes", "salesfunneldiv");',80);
			setTimeout('showHintChart("'+url+'&enquirypriority=yes", "prioritychartdiv");',60);
			
			// for Cards
			setTimeout('showHintCards("'+url+'&cards=yes", "enquiry_count,testdrive_count,so_count,retail_count,delivery_count,cancelled_count");',20);
			
		}
		
		function showHintChart(url, Hint) {
			$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
				$.ajax({
					url: url,
					type: 'GET',
					success: function (data){
						if(data.trim() != 'SignIn'){
						LoadAmChart(data.trim(), Hint);
						} else{
							window.location.href = "../portal/";
						}
					}
				});
		}
		
		function showHintCards(url, Hint) {
			var card_ids = Hint.split(',');
			
			$('#'+card_ids[0]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[1]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[2]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[3]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[4]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[5]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			
				$.ajax({
					url: url,
					type: 'GET',
					success: function (data){
						if(data.trim() != 'SignIn'){
							carddata=data.trim().substring(0,data.trim().indexOf(":")).split(",");
							bardata=data.trim().substring(data.trim().indexOf(":")+1,data.trim().length).split(",");
							count(periodType);
						} else {
							window.location.href = "../portal/";
						}
					}
				});
		}
	</script>
	
<!-- 	//should not be deleted written for multiple ajax calls -->
	<script>
	//
	function PopulateExecutives(){
	}
	function PopulateTeams(){
	}
	function PopulateCRMDays(){
	}
	</script>
	
	<script type="text/javascript">
	var dts = 'today';
	
	//to get the Date selected [Today , Month, Quarter]
	function getvalue(getdt){
		dts = getdt;
	}
	
	function checkcall(opt){
		var url;
		
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		var dr_branch_id = outputSelected(document.getElementById("dr_branch").options);
		url = "../sales/index-check.jsp?filter=yes&opt="+opt;
		url = url+"&brand_id=" + brand_id + "&region_id="+region_id+"&dr_branch_id="+dr_branch_id+"&period="+dts;
		window.open(url,'_blank');
	}
	
	</script>
	
</body>
</html>
