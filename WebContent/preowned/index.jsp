<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.Index" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
	
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
<%@include file="../portal/header.jsp" %>

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
						<h1><%=mybean.ReturnPreOwnedName(request)%></h1>
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
						<li><a href="../preowned/index.jsp"><%=mybean.ReturnPreOwnedName(request)%></a> &gt;</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
				
					<div class="container-fluid">
						<div class="col-md-4 col-xs-12">
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Follow-up Escalation</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
<%-- 										<%=mybean.PreownedFollowupEscStatus()%> --%>
	                                 <div id="followupchartdiv" style="height: 300px;" style="width: 200px;"></div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-4 col-xs-12">
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Evaluation Escalation</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
<%-- 										<%=mybean.PreownedFollowupEscStatus()%> --%>
	                                 <div id="escalationchartdiv" style="height: 300px;" style="width: 200px;"></div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-4 col-xs-12">
						</div>
						
						<div class="col-md-4 col-xs-12">
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.ReturnPreOwnedName(request)%> Reports</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="" style="height: 600px;" style="width: 200px;">
										<!-- START PORTLET BODY -->
										<%=mybean.ListReports(request)%>
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

 <%@include file="../Library/admin-footer.jsp" %>
 <%@include file="../Library/js.jsp"%>
 
 	<!-- Start additional plugins -->
<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
<script type="text/javascript" src="../Library/amcharts/funnel.js"></script>
<script src="../Library/amcharts/serial.js" type="text/javascript"></script>
<!-- End additional plugins -->

<script language="javascript" type="text/javascript">
	var followupchart, evaluationchart;
	var followupchartData = <%=mybean.followupchart_data%>;
	var evaluationchartData = <%=mybean.evaluationchart_data%>;
	
	AmCharts.ready(function () {
	
		// SERIAL CHART FOR FOLLOWUP Escalation
		followupchart = new AmCharts.AmSerialChart();
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
		valueAxis1.title = "Follow-up Escalation";
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
		
		//// SERIAL CHART FOR Pre-Owned Evaluation Escalation
		evaluationchart = new AmCharts.AmSerialChart();
		evaluationchart.dataProvider = evaluationchartData;
		evaluationchart.categoryField = "level";
		// this single line makes the chart a bar chart,
		// try to set it to false - your bars will turn to columns
		evaluationchart.rotate = true;
		// the following two lines makes chart 3D
		evaluationchart.depth3D = 20;
		evaluationchart.angle = 30;
		evaluationchart.startDuration = 2;
		evaluationchart.startEffect = "easeOutSine";
		evaluationchart.marginsUpdated = false;
		evaluationchart.autoMarginOffset = 0;
		evaluationchart.marginLeft = 0;
		evaluationchart.marginRight = 0;
		
		// AXES
		// Category
		var categoryAxis2 = evaluationchart.categoryAxis;
		categoryAxis2.gridPosition = "start";
		categoryAxis2.axisColor = "#DADADA";
		categoryAxis2.fillAlpha = 1;
		categoryAxis2.gridAlpha = 1;
		categoryAxis2.gridColor = "#DADADA";
		categoryAxis2.fillColor = "#FAFAFA";
		// value
		var valueAxis2 = new AmCharts.ValueAxis();
		valueAxis2.axisColor = "#DADADA";
		valueAxis2.title = "Evaluation Escalation";
		//valueAxis1.position = "top";
		valueAxis2.gridAlpha = 1;
		valueAxis2.gridColor = "#DADADA";
		evaluationchart.addValueAxis(valueAxis2);

		// GRAPH
		var evaluationgraph = new AmCharts.AmGraph();
		evaluationgraph.title = "Evaluation";
		evaluationgraph.valueField = "value";
		evaluationgraph.urlField = "url";
		evaluationgraph.urlTarget = "_target";
		evaluationgraph.type = "column";
		evaluationgraph.balloonText = "[[category]]:[[value]]";
		evaluationgraph.lineAlpha = 0;				
		evaluationgraph.colorField = "color";
		evaluationgraph.fillAlphas = 1;
		evaluationgraph.labelText = "[[value]]";
		evaluationgraph.labelPosition = "top";
		evaluationchart.addGraph(evaluationgraph);

		evaluationchart.creditsPosition = "top-right";

		// WRITE
		evaluationchart.write("escalationchartdiv");	
	});
	
</script>
 
</body>
</html>
