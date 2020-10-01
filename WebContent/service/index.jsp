<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Index" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>

<style>

@media screen and (min-width: 992px)
{ .dropdown toggle{ left:15px; } }


.btn.btn-outline.blue-oleo.active, .btn.btn-outline.blue-oleo:active,
	.btn.btn-outline.blue-oleo:active:focus, .btn.btn-outline.blue-oleo:active:hover,
	.btn.btn-outline.blue-oleo:focus, .btn.btn-outline.blue-oleo:hover {
	border-color: #94A0B2;
	color: #FFF;
	background-color: #94A0B2;
}

@media screen

.icon-btn {
	top: -13px;
	left: 17px;
}

.dashboard-stat2 {
	box-shadow: 2px 2px 2px #888888;
/* 	width: 200px; */
}

@media screen and (min-width:992px) {
	#portlet {
		margin-left: 7px;
	}
	.portlet.light {
		width: 99.5%;
		margin-left: 4px;
	}
}

 .dashboard-stat2 .display .number h3 {
        margin: 0 0 2px 0;
        padding: 0;
        font-size: 25px;
        font-weight: 400; }
        
        .dashboard-stat2{
        height:125px;
        }
</style>

</HEAD>
<!-- LoadAmChart(); -->
<body class="page-container-bg-solid page-header-menu-fixed" onload="allFilter();convertmultiselect();">
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
						<h1>Service</h1>
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
						<li><a href="../service/index.jsp">Service</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<div class="container-fluid">
								<!-- 							=====================start card================ -->
								
									<div class="col-md-12 col-sm-12">
									
										<div class="portlet light ">
											<div class="portlet-title">
												<div class="caption caption-md">
													<i class="icon-bar-chart font-dark hide"></i> <span
														class="caption-subject font-green-steel uppercase bold">Service Summary</span>
														 <span class="caption-helper hide">weekly stats...</span>
												</div>
												<div class="actions col-md-3 col-sm-3" style="margin-top: -23px;">
													<div class="btn-group btn-group-devided" data-toggle="buttons">
														<label
															class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm active" id='today'>
															<input type="radio" name="options"  onchange="count(this.value);getvalue(this.value);"
															id="option1" value="today">Today
														</label> <label
															class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm" id='month'>
															<input type="radio" name="options"  onchange="count(this.value);getvalue(this.value);" 
															id="option2" value="month">Month
														</label> <label
															class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm" id='quarter'>
															<input type="radio" name="options"  onchange="count(this.value);getvalue(this.value);"
															id="option3" value="quarter">Quarter
														</label>
													</div>
												</div>
											</div>
											
												<div class="actions col-md-12">
												
												<form name="form1" class="form-horizontal">
												<div class="form-body col-md-3 col-sm-3">
															<div class="form-group">
																<label class="control-label col-md-4 col-sm-4">Brand:</label>
																<div class="col-md-6" >
																	<span id="multiprincipal">
																		<select name="dr_principal" size="10"
																			multiple="multiple" class="form-control service_element hidden"
																			id="dr_principal"
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
														<div class="col-md-6" >
															<span id="regionHint">
														<%=mybean.mischeck.PopulateRegionMultySelect(mybean.filter_brand_id, mybean.region_ids, mybean.comp_id, request)%>
														</span>
														</div>
													</div>
												</div>
												<div class="form-body col-md-3 col-sm-3">
													<div class="form-group">
														<label class="control-label col-md-4 col-sm-4">Branch:</label>
														<div class="col-md-6" >
 															<span id="branchHint"> 
															<%=mybean.mischeck.PopulateBranchesMultySelect(mybean.filter_brand_id, mybean.filter_region_id, mybean.jc_branch_ids, mybean.comp_id, request)%>
															</span> 
														</div>
													</div>
												</div>
												
												</form>
												
												
												</div>
												<input type='text' value='today' id='cardvalue' name='cardvalue' hidden />
											
											<div class="portlet-body" id="portlet">
												<div id="dashbox" style="margin-left: -29px;" class="row">
											<center>
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<a>
														<div class="dashboard-stat2" id="vehicles" onclick="checkcall(this.id);">
															<div class="display" id='hint'>
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-green-sharp">
																		<span data-counter="counterup" id='veh_count' ></span>

																	</h3>
																	<small>Vehicles</small>
																</div>
																<div class="col-md-5 col-xs-5">
																<i class='btn-sm bg-green-sharp icon-btn col-md-5'> <i
																	class="fa fa-car" style="font-size: xx-large;margin-top: 5px;"></i>
<!-- 																	<div>Users</div> -->

																</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
<!-- 																style="width: 200%;" -->
																	<span class="progress-bar progress-bar-success green-sharp" id='veh_count_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='veh_count_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
													<a>
														<div class="dashboard-stat2 " id="jobcard" onclick="checkcall(this.id);">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-red-haze">
																		<span data-counter="counterup" id='jc_count' ></span>
																	</h3>
																	<small>Job Card</small>
																</div>
																<div class="col-md-5">
																	<i class='btn-sm bg-red-haze icon-btn '> <i
																		class="fa fa-vcard"  style="font-size: xx-large;margin-top: 5px;"></i>
<!-- 																		<div>Users</div> -->
																	</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success red-haze" id='jc_count_bar' > </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='jc_count_label' ></div>
																</div>
															</div>
														</div>
														</a>
													</div>
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
													<a>
														<div class="dashboard-stat2 " id="tickets" onclick="checkcall(this.id);">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-blue-sharp">
																		<span data-counter="counterup" id='ticket_count' ></span>
																	</h3>
																	<small>Tickets</small>
																</div>
																<div class="col-md-5">
																<i class='btn-sm bg-blue-sharp icon-btn col-md-5'>
																<i class="fa fa-ticket" style="font-size: xx-large;margin-top: 5px;"></i>

																</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success blue-sharp" id='ticket_count_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='ticket_count_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
													<a>
														<div class="dashboard-stat2 " id="booking" onclick="checkcall(this.id);">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-purple-soft">
																		<span data-counter="counterup" id='service_booking_count'></span>
																	</h3>
																	<small>Service Booking</small>
																</div>
																<div class="col-md-5">
																<i class='btn-sm icon-btn col-md-7' style="background-color: #a495c3">
																<i class="fa fa-cog" style="font-size: xx-large;margin-top: 5px;"></i>
																</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span  class="progress-bar progress-bar-success purple-soft" id='service_booking_count_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='service_booking_count_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
													<a>
														<div class="dashboard-stat2 " id="partsamount" onclick="checkcall(this.id);">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-blue-soft">
																		<span data-counter="counterup" id='parts_amt' ></span>
																	</h3>
																	<small>Parts Amount</small>
																</div>
																<div class="col-md-5">
																<i class='btn-sm  icon-btn col-md-7' style="background-color:#4adbc7 "> 
<!-- 																<img src="../assets/img/money.png" style="width: 46px;margin-top: -12px;" alt="Parts Amount"></img> -->
																	<i class="fa fa-money" style="font-size: xx-large;margin-top: 5px;"></i>
																</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success bg-blue-soft" id='parts_amt_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='parts_amt_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
													<a>
														<div class="dashboard-stat2 " id="labouramount" onclick="checkcall(this.id);">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-yellow-soft">
																		<span data-counter="counterup" id='labour_amt' ></span>
																	</h3>
																	<small>Labour Amount</small>
																</div>
																<div class="col-md-5">
																<i class='btn-sm bg-yellow-soft icon-btn'> 
<!-- 																<img src="../assets/img/money.png" style="width: 46px;margin-top: -12px;" alt="Labour Amount"></img> -->
																	<i class="fa fa-money" style="font-size: xx-large;margin-top: 5px;"></i>
																</i>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success yellow-soft" id='labour_amt_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='labour_amt_label'></div>
																</div>
															</div>
														</div>
														</a>
													</div>
												</center>
												</div>
											</div></div>
										</div>
							
		<!--  =====================end card================ -->
									<div class="col-md-4 col-sm-6 col-xs-12" style="padding: 5px">
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">&nbsp;</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<div id="ticketchartdiv" style="height: 300px;"></div>
													<br> <br> <br>
													<div id="servicefollowupescchartdiv" style="height: 300px;"></div>
													<br> <br> <br>
													<div id="jcprioritychartdiv" style="height: 300px;"></div>
													<br> <br> <br>
													<div id="psfchartdiv" style="height: 300px;"></div>
													<br> <br> <br>
												</div>
											</div>
										</div>
									</div>
									<div class=" col-md-4 col-sm-6 col-xs-12" style="padding: 5px">
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Open Tickets</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="openticketdiv">
													<center>
<%-- 														<%=mybean.TicketsOpen(mybean.comp_id)%> --%>
													</center>

												</div>
											</div>
										</div>
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Open Job
													Cards</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="openjcdiv">
													<center>
<%-- 														<%=mybean.JCsOpen(mybean.comp_id)%> --%>
													</center>

												</div>
											</div>
										</div>
									</div>


									<div class=" col-md-4 col-xs-12" style="padding: 5px">
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Service
													Reports</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<center>
														<%=mybean.ListReports()%>
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
			</div>
		</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
	<script type="text/javascript" src="../Library/amcharts/funnel.js"></script>
	<script type="text/javascript" src="../Library/amcharts/serial.js"></script>


	<script type="text/javascript">

$(document).ready(function() {
    $('#dr_principal').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
    });
    
    $('#dr_region').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
    });
    
    $('#dr_branch_id').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
    });
});

function LoadAmChart(jsonstring,hint){
	
		switch (hint){
		
		case 'ticketchartdiv':
			
			var ticketchartData = JSON.parse(jsonstring);
			// SERIAL CHART FOR TICKET
			ticketchart = new AmCharts.AmSerialChart();
			ticketchart.dataProvider = ticketchartData;
			ticketchart.categoryField = "level";
			// this single line makes the chart a bar chart,
			// try to set it to false - your bars will turn to columns
			ticketchart.rotate = true;
			// the following two lines makes chart 3D
			ticketchart.depth3D = 20;
			ticketchart.angle = 30;
			ticketchart.startDuration = 2;
			ticketchart.startEffect = "easeOutSine";
			ticketchart.marginsUpdated = false;
			ticketchart.autoMarginOffset = 0;
			ticketchart.marginLeft = 0;
			ticketchart.marginRight = 0;

			// AXES
			// Category
			var categoryAxis1 = ticketchart.categoryAxis;
			categoryAxis1.gridPosition = "start";
			categoryAxis1.axisColor = "#DADADA";
			categoryAxis1.fillAlpha = 1;
			categoryAxis1.gridAlpha = 1;
			categoryAxis1.gridColor = "#DADADA";
			categoryAxis1.fillColor = "#FAFAFA";
			// value
			var valueAxis1 = new AmCharts.ValueAxis();
			valueAxis1.axisColor = "#DADADA";
			valueAxis1.title = "Ticket Escalation";
			valueAxis1.gridAlpha = 1;
			valueAxis1.gridColor = "#DADADA";
			ticketchart.addValueAxis(valueAxis1);

			// GRAPH
			var ticketgraph = new AmCharts.AmGraph();
			ticketgraph.title = "Ticket";
			ticketgraph.valueField = "value";
			ticketgraph.urlField = "url";
			ticketgraph.urlTarget = "_target";
			ticketgraph.type = "column";
			ticketgraph.balloonText = "[[category]]:[[value]]";
			ticketgraph.lineAlpha = 0;
			ticketgraph.colorField = "color";
			ticketgraph.fillAlphas = 1;
			ticketgraph.labelText = "[[value]]";
			ticketgraph.labelPosition = "top";
			ticketchart.addGraph(ticketgraph);
			ticketchart.creditsPosition = "top-right";

			// WRITE
			ticketchart.write("ticketchartdiv");
			break;
			
		case 'jcprioritychartdiv':
			
			var jcprioritychartData = JSON.parse(jsonstring);
			
			// SERIAL CHART FOR JOB CARD PRIORITY
			jcprioritychart = new AmCharts.AmSerialChart();
			jcprioritychart.dataProvider = jcprioritychartData;
			jcprioritychart.categoryField = "level";
			// this single line makes the chart a bar chart,
			// try to set it to false - your bars will turn to columns
			jcprioritychart.rotate = true;
			// the following two lines makes chart 3D
			jcprioritychart.depth3D = 20;
			jcprioritychart.angle = 30;
			jcprioritychart.startDuration = 1;
			jcprioritychart.startEffect = "easeOutSine";
			jcprioritychart.marginsUpdated = false;
			jcprioritychart.autoMarginOffset = 0;
			jcprioritychart.marginLeft = 0;
			jcprioritychart.marginRight = 0;

			// AXES
			// Category
			var categoryAxis3 = jcprioritychart.categoryAxis;
			categoryAxis3.gridPosition = "start";
			categoryAxis3.axisColor = "#DADADA";
			categoryAxis3.fillAlpha = 1;
			categoryAxis3.gridAlpha = 1;
			categoryAxis3.gridColor = "#DADADA";
			categoryAxis3.fillColor = "#FAFAFA";
			// value
			var valueAxis3 = new AmCharts.ValueAxis();
			valueAxis3.axisColor = "#DADADA";
			valueAxis3.title = "Job Card Priority Escalation";
			valueAxis3.gridAlpha = 1;
			valueAxis3.gridColor = "#DADADA";
			jcprioritychart.addValueAxis(valueAxis3);

			// GRAPH
			var jcprioritygraph = new AmCharts.AmGraph();
			jcprioritygraph.title = "JC Priority";
			jcprioritygraph.valueField = "value";
			jcprioritygraph.urlField = "url";
			jcprioritygraph.urlTarget = "_target";
			jcprioritygraph.type = "column";
			jcprioritygraph.balloonText = "[[category]]:[[value]]";
			jcprioritygraph.lineAlpha = 0;
			jcprioritygraph.colorField = "color";
			jcprioritygraph.fillAlphas = 1;
			jcprioritygraph.labelText = "[[value]]";
			jcprioritygraph.labelPosition = "top";
			jcprioritychart.addGraph(jcprioritygraph);
			jcprioritychart.creditsPosition = "top-right";
			
			// WRITE
			jcprioritychart.write("jcprioritychartdiv");
			
			break;
		case 'psfchartdiv':
			
			var psfchartData = JSON.parse(jsonstring);

			// SERIAL CHART FOR PSF
			psfchart = new AmCharts.AmSerialChart();
			psfchart.dataProvider = psfchartData;
			psfchart.categoryField = "level";
			// this single line makes the chart a bar chart,
			// try to set it to false - your bars will turn to columns
			psfchart.rotate = true;
			// the following two lines makes chart 3D
			psfchart.depth3D = 20;
			psfchart.angle = 30;
			psfchart.startDuration = 1;
			psfchart.startEffect = "easeOutSine";
			psfchart.marginsUpdated = false;
			psfchart.autoMarginOffset = 0;
			psfchart.marginLeft = 0;
			psfchart.marginRight = 0;

			// AXES
			// Category
			var categoryAxis5 = psfchart.categoryAxis;
			categoryAxis5.gridPosition = "start";
			categoryAxis5.axisColor = "#DADADA";
			categoryAxis5.fillAlpha = 1;
			categoryAxis5.gridAlpha = 1;
			categoryAxis5.gridColor = "#DADADA";
			categoryAxis5.fillColor = "#FAFAFA";
			// value
			var valueAxis5 = new AmCharts.ValueAxis();
			valueAxis5.axisColor = "#DADADA";
			valueAxis5.title = "Job Card PSF Escalation";
			valueAxis5.gridAlpha = 1;
			valueAxis5.gridColor = "#DADADA";
			psfchart.addValueAxis(valueAxis5);

			// GRAPH
			var psfgraph = new AmCharts.AmGraph();
			psfgraph.title = "Psf";
			psfgraph.valueField = "value";
			psfgraph.urlField = "url";
			psfgraph.urlTarget = "_target";
			psfgraph.type = "column";
			psfgraph.balloonText = "[[category]]:[[value]]";
			psfgraph.lineAlpha = 0;
			psfgraph.colorField = "color";
			psfgraph.fillAlphas = 1;
			psfgraph.labelText = "[[value]]";
			psfgraph.labelPosition = "top";
			psfchart.addGraph(psfgraph);
			psfchart.creditsPosition = "top-right";

			// WRITE
			psfchart.write("psfchartdiv");
			
			break;
		case "servicefollowupescchartdiv":
			
			var servicefollowupescchartData = JSON.parse(jsonstring);
			// SERIAL CHART FOR Service FollowUp Escalation 
			servicefollowupescchart = new AmCharts.AmSerialChart();
			servicefollowupescchart.dataProvider = servicefollowupescchartData;
			servicefollowupescchart.categoryField = "level";
			// this single line makes the chart a bar chart,
			// try to set it to false - your bars will turn to columns
			servicefollowupescchart.rotate = true;
			// the following two lines makes chart 3D
			servicefollowupescchart.depth3D = 20;
			servicefollowupescchart.angle = 30;
			servicefollowupescchart.startDuration = 1;
			servicefollowupescchart.startEffect = "easeOutSine";
			servicefollowupescchart.marginsUpdated = false;
			servicefollowupescchart.autoMarginOffset = 0;
			servicefollowupescchart.marginLeft = 0;
			servicefollowupescchart.marginRight = 0;

			// AXES
			// Category
			var categoryAxis4 = servicefollowupescchart.categoryAxis;
			categoryAxis4.gridPosition = "start";
			categoryAxis4.axisColor = "#DADADA";
			categoryAxis4.fillAlpha = 1;
			categoryAxis4.gridAlpha = 1;
			categoryAxis4.gridColor = "#DADADA";
			categoryAxis4.fillColor = "#FAFAFA";
			// value
			var valueAxis4 = new AmCharts.ValueAxis();
			valueAxis4.axisColor = "#DADADA";
			valueAxis4.title = "Service Follow-up Escalation";
			valueAxis4.gridAlpha = 1;
			valueAxis4.gridColor = "#DADADA";
			servicefollowupescchart.addValueAxis(valueAxis4);

			// GRAPH
			var servicefollowupescgraph = new AmCharts.AmGraph();
			servicefollowupescgraph.title = "Service FollowUp Escalation Stage";
			servicefollowupescgraph.valueField = "value";
			servicefollowupescgraph.urlField = "url";
			servicefollowupescgraph.urlTarget = "_target";
			servicefollowupescgraph.type = "column";
			servicefollowupescgraph.balloonText = "[[category]]:[[value]]";
			servicefollowupescgraph.lineAlpha = 0;
			servicefollowupescgraph.colorField = "color";
			servicefollowupescgraph.fillAlphas = 1;
			servicefollowupescgraph.labelText = "[[value]]";
			servicefollowupescgraph.labelPosition = "top";
			servicefollowupescchart.addGraph(servicefollowupescgraph);
			servicefollowupescchart.creditsPosition = "top-right";

			// WRITE
			servicefollowupescchart.write("servicefollowupescchartdiv");
			break;
		}
}

var carddata;
var bardata;
var periodType = "today";

function count(type) {
	
	periodType = type;
	$('#cardvalue').val(type);

	switch(type){
	case 'today':
		count_type_loop(carddata[0],"veh_count");
		count_type_loop(carddata[3], "jc_count");
		count_type_loop(carddata[6], "ticket_count");
		count_type_loop(carddata[9], "service_booking_count");
		count_type_loop(carddata[12], "parts_amt");
		count_type_loop(carddata[15], "labour_amt");
		
		bar_type_loop(bardata[0],carddata[0],"veh_count");
		bar_type_loop(bardata[3],carddata[3],"jc_count");
		bar_type_loop(bardata[6],carddata[6],"ticket_count");
		bar_type_loop(bardata[9],carddata[9],"service_booking_count");
		bar_type_loop(bardata[12],carddata[12],"parts_amt");
		bar_type_loop(bardata[15],carddata[15],"labour_amt");
		
		break;
	
	case 'month':
		count_type_loop(carddata[1],"veh_count");
		count_type_loop(carddata[4], "jc_count");
		count_type_loop(carddata[7], "ticket_count");
		count_type_loop(carddata[10], "service_booking_count");
		count_type_loop(carddata[13], "parts_amt");
		count_type_loop(carddata[16], "labour_amt");
		
		bar_type_loop(bardata[1],carddata[1],"veh_count");
		bar_type_loop(bardata[4],carddata[4],"jc_count");
		bar_type_loop(bardata[7],carddata[7],"ticket_count");
		bar_type_loop(bardata[10],carddata[10],"service_booking_count");
		bar_type_loop(bardata[13],carddata[13],"parts_amt");
		bar_type_loop(bardata[16],carddata[16],"labour_amt");
		
		break;
	
	case 'quarter':
		count_type_loop(carddata[2],"veh_count");
		count_type_loop(carddata[5], "jc_count");
		count_type_loop(carddata[8], "ticket_count");
		count_type_loop(carddata[11], "service_booking_count");
		count_type_loop(carddata[14], "parts_amt");
		count_type_loop(carddata[17], "labour_amt");
		
		bar_type_loop(bardata[2],carddata[2],"veh_count");
		bar_type_loop(bardata[5],carddata[5],"jc_count");
		bar_type_loop(bardata[8],carddata[8],"ticket_count");
		bar_type_loop(bardata[11],carddata[11],"service_booking_count");
		bar_type_loop(bardata[14],carddata[14],"parts_amt");
		bar_type_loop(bardata[17],carddata[17],"labour_amt");
		
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
		setTimeout('document.getElementById("' + Hint + '").innerHTML=' +Math.round(mybean_value)+'+"L"', t) ;
	}else{
		for(i = 0, j = eval(mybean_value/10), t=100 ; Math.round(i) <= mybean_value ; i=i+j, t=t+100){
			setTimeout('document.getElementById("' + Hint + '").innerHTML=' + Math.round(i), t) ;
			if(mybean_value==0 || mybean_value=="" || mybean_value==null){break;}
		}
		setTimeout('document.getElementById("' + Hint + '").innerHTML=' + Math.round(mybean_value) , t) ;
	}
}

function bar_type_loop(mybean_old_value, mybean_new_value, Hint){
	var t= 100;
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

//	end populate cards

function convertmultiselect(){
	$("#dr_principal").multiselect({ nonSelectedText :'Brand'});
	$("#dr_region").multiselect({ nonSelectedText :'Region'});
	$("#dr_branch_id").multiselect({ nonSelectedText :'Branch'});
}

function PopulateRegion() {
	var brand_id = outputSelected(document.getElementById("dr_principal").options);
	showHintMultySelect('../service/mis-check1.jsp?multiplecheck=yes&brand_id=' + brand_id + '&multiplecheckregion=yes', 'regionHint');
}

function PopulateBranches() {
	var brand_id = outputSelected(document.getElementById("dr_principal").options);
	var region_id = outputSelected(document.getElementById("dr_region").options);
	showHintMultySelect('../service/mis-check1.jsp?multiplecheck=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&multiplecheckbranch=yes', 'branchHint');
}


function allFilter(){
	var brand_id = outputSelected(document.getElementById("dr_principal").options);
	var region_id = outputSelected(document.getElementById("dr_region").options);
	var dr_branch_id = outputSelected(document.getElementById("dr_branch_id").options);

	var url="../service/index-check.jsp?refreshAll=yes&brand_id=" + brand_id + "&region_id="+region_id+"&dr_branch_id="+dr_branch_id+"&timefilter="+periodType;

	// for Am-Chart
	setTimeout('showHintChart("'+url+'&TicketEscStatus=yes", "ticketchartdiv");',100);
	setTimeout('showHintChart("'+url+'&JCPriorityEscStatus=yes", "jcprioritychartdiv");',80);
	setTimeout('showHintChart("'+url+'&ServiceFollowupEscStatus=yes", "servicefollowupescchartdiv");',60);
	setTimeout('showHintChart("'+url+'&JcPSFEscStatus=yes", "psfchartdiv");',40);
	// for Cards
	setTimeout('showHintCards("'+url+'&cards=yes", "veh_count,jc_count,ticket_count,service_booking_count,parts_amt,labour_amt");',20);
	
	//for TICKET AND JC
	setTimeout('showHint("'+url+'&opentickets=yes", "openticketdiv");',20);
	setTimeout('showHint("'+url+'&openjobcards=yes", "openjcdiv");',10);
	
}

function showHintChart(url, Hint) {
			$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
				$.ajax({
					url: url,
					type: 'GET',
					success: function (data){
						if(data.trim() != 'SignIn'){
						LoadAmChart(data.trim(),Hint);
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

function showHintMultySelect(url, Hint) {
			$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
				$.ajax({
					url: url,
					type: 'GET',
					success: function (data){
						if(data.trim() != 'SignIn'){
							$('#'+Hint).fadeIn(500).html('' + data.trim() + '');
							convertmultiselect();
							}
						else{
							window.location.href = "../portal/";
							}
					}
				});
		
}

</script>

<script type="text/javascript">
$(function() {
	$('table').footable({
						toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
					});
});
</script>
<!-- for redirect -->
	 <script type="text/javascript">
	var dts = 'today';
	//to get the Date selected [Today , Month, Quarter]
	function getvalue(getdt){
		dts = getdt;
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		var dr_branch_id = outputSelected(document.getElementById("dr_branch_id").options);
		var url="../service/index-check.jsp?refreshAll=yes&brand_id=" + brand_id + "&region_id="+region_id+"&dr_branch_id="+dr_branch_id;
		//for TICKET AND JC
		showHint(url+"&timefilter="+dts+"&opentickets=yes", "openticketdiv");
		showHint(url+"&timefilter="+dts+"&openjobcards=yes", "openjcdiv");
	}
	
	function checkcall(opt){
		var url;
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		var dr_branch_id = outputSelected(document.getElementById("dr_branch_id").options);
		url = "../service/index-check.jsp?filter=yes&opt="+opt;
		url = url+"&brand_id=" + brand_id + "&region_id="+region_id+"&dr_branch_id="+dr_branch_id+"&period="+dts;
		window.open(url,'_blank');
	}
	
	</script> 	
	<!-- for redirect -->
</body>
</html>
