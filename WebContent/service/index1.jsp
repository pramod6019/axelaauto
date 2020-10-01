<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Index" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     

<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script src="../Library/amcharts/amcharts.js" type="text/javascript"></script>
<script src="../Library/amcharts/serial.js" type="text/javascript"></script>
<script type="text/javascript">
            var ticketchart, callchart, jcprioritychart, jcstagechart, psfchart, insurancechart;
            var ticketchartData = <%=mybean.ticketchart_data%>;
			var callchartData = <%=mybean.callchart_data%>;
			var jcprioritychartData = <%=mybean.jcprioritychart_data%>;
			var jcstagechartData = <%=mybean.jcstagechart_data%>;
			var psfchartData = <%=mybean.psfchart_data%>;
			var insurancechartData = <%=mybean.insurancechart_data%>;
            AmCharts.ready(function () {
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
                categoryAxis1.gridAlpha = 0;
                categoryAxis1.fillColor = "#FAFAFA";
                // value
                var valueAxis1 = new AmCharts.ValueAxis();
                valueAxis1.axisColor = "#DADADA";
                valueAxis1.title = "";
                valueAxis1.gridAlpha = 0.1;
                ticketchart.addValueAxis(valueAxis1);

                // GRAPH
                var ticketgraph = new AmCharts.AmGraph();
                ticketgraph.title = "Ticket";
                ticketgraph.valueField = "value";
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
				
				
				
				// SERIAL CHART FOR CALL
                callchart = new AmCharts.AmSerialChart();
                callchart.dataProvider = callchartData;
                callchart.categoryField = "level";
                // this single line makes the chart a bar chart,
                // try to set it to false - your bars will turn to columns
                callchart.rotate = true;
                // the following two lines makes chart 3D
                callchart.depth3D = 20;
                callchart.angle = 30;
				callchart.marginsUpdated = false;
				callchart.autoMarginOffset = 0;
				callchart.marginLeft = 0;
				callchart.marginRight = 0;

                // AXES
                // Category
                var categoryAxis2 = callchart.categoryAxis;
                categoryAxis2.gridPosition = "start";
                categoryAxis2.axisColor = "#DADADA";
                categoryAxis2.fillAlpha = 1;
                categoryAxis2.gridAlpha = 0;
                categoryAxis2.fillColor = "#FAFAFA";
                // value
                var valueAxis2 = new AmCharts.ValueAxis();
                valueAxis2.axisColor = "#DADADA";
                valueAxis2.title = "Call Follow-up Escalation";
                valueAxis2.gridAlpha = 0.1;
                callchart.addValueAxis(valueAxis2);

                // GRAPH
                var callgraph = new AmCharts.AmGraph();
                callgraph.title = "Call";
                callgraph.valueField = "value";
                callgraph.type = "column";
                callgraph.balloonText = "[[category]]:[[value]]";
                callgraph.lineAlpha = 0;				
                callgraph.colorField = "color";
                callgraph.fillAlphas = 1;
				callgraph.labelText = "[[value]]";
				callgraph.labelPosition = "top";
                callchart.addGraph(callgraph);

                callchart.creditsPosition = "top-right";

                // WRITE
                callchart.write("callchartdiv");
				
				
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
                categoryAxis3.gridAlpha = 0;
                categoryAxis3.fillColor = "#FAFAFA";
                // value
                var valueAxis3 = new AmCharts.ValueAxis();
                valueAxis3.axisColor = "#DADADA";
                valueAxis3.title = "Job Card Priority Escalation";
                valueAxis3.gridAlpha = 0.1;
                jcprioritychart.addValueAxis(valueAxis3);

                // GRAPH
                var jcprioritygraph = new AmCharts.AmGraph();
                jcprioritygraph.title = "JC Priority";
                jcprioritygraph.valueField = "value";
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
				
				
				// SERIAL CHART FOR JOB CARD STAGE
                jcstagechart = new AmCharts.AmSerialChart();
                jcstagechart.dataProvider = jcstagechartData;
                jcstagechart.categoryField = "level";
                // this single line makes the chart a bar chart,
                // try to set it to false - your bars will turn to columns
                jcstagechart.rotate = true;
                // the following two lines makes chart 3D
                jcstagechart.depth3D = 20;
                jcstagechart.angle = 30;
				jcstagechart.marginsUpdated = false;
				jcstagechart.autoMarginOffset = 0;
				jcstagechart.marginLeft = 0;
				jcstagechart.marginRight = 0;

                // AXES
                // Category
                var categoryAxis4 = jcstagechart.categoryAxis;
                categoryAxis4.gridPosition = "start";
                categoryAxis4.axisColor = "#DADADA";
                categoryAxis4.fillAlpha = 1;
                categoryAxis4.gridAlpha = 0;
                categoryAxis4.fillColor = "#FAFAFA";
                // value
                var valueAxis4 = new AmCharts.ValueAxis();
                valueAxis4.axisColor = "#DADADA";
                valueAxis4.title = "Job Card Stage Escalation";
                valueAxis4.gridAlpha = 0.1;
                jcstagechart.addValueAxis(valueAxis4);

                // GRAPH
                var jcstagegraph = new AmCharts.AmGraph();
                jcstagegraph.title = "JC Stage";
                jcstagegraph.valueField = "value";
                jcstagegraph.type = "column";
                jcstagegraph.balloonText = "[[category]]:[[value]]";
                jcstagegraph.lineAlpha = 0;				
                jcstagegraph.colorField = "color";
                jcstagegraph.fillAlphas = 1;
				jcstagegraph.labelText = "[[value]]";
				jcstagegraph.labelPosition = "top";
                jcstagechart.addGraph(jcstagegraph);

                jcstagechart.creditsPosition = "top-right";

                // WRITE
                jcstagechart.write("jcstagechartdiv");
				
				
				
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
				psfchart.marginsUpdated = false;
				psfchart.autoMarginOffset = 0;
				psfchart.marginLeft = 0;
				psfchart.marginRight = 0;

                // AXES
                // Category
                var categoryAxis5 = callchart.categoryAxis;
                categoryAxis5.gridPosition = "start";
                categoryAxis5.axisColor = "#DADADA";
                categoryAxis5.fillAlpha = 1;
                categoryAxis5.gridAlpha = 0;
                categoryAxis5.fillColor = "#FAFAFA";
                // value
                var valueAxis5 = new AmCharts.ValueAxis();
                valueAxis5.axisColor = "#DADADA";
                valueAxis5.title = "PSF Escalation";
                valueAxis5.gridAlpha = 0.1;
                psfchart.addValueAxis(valueAxis5);

                // GRAPH
                var psfgraph = new AmCharts.AmGraph();
                psfgraph.title = "Psf";
                psfgraph.valueField = "value";
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
				
				
				// SERIAL CHART FOR INSURANCE FOLLOW-UP
                insurancechart = new AmCharts.AmSerialChart();
                insurancechart.dataProvider = insurancechartData;
                insurancechart.categoryField = "level";
                // this single line makes the chart a bar chart,
                // try to set it to false - your bars will turn to columns
                insurancechart.rotate = true;
                // the following two lines makes chart 3D
                insurancechart.depth3D = 20;
                insurancechart.angle = 30;
				insurancechart.marginsUpdated = false;
				insurancechart.autoMarginOffset = 0;
				insurancechart.marginLeft = 0;
				insurancechart.marginRight = 0;

                // AXES
                // Category
                var categoryAxis6 = insurancechart.categoryAxis;
                categoryAxis6.gridPosition = "start";
                categoryAxis6.axisColor = "#DADADA";
                categoryAxis6.fillAlpha = 1;
                categoryAxis6.gridAlpha = 0;
                categoryAxis6.fillColor = "#FAFAFA";
                // value
                var valueAxis6 = new AmCharts.ValueAxis();
                valueAxis6.axisColor = "#DADADA";
                valueAxis6.title = "Insurance Follow-up Escalation";
                valueAxis6.gridAlpha = 0.1;
                insurancechart.addValueAxis(valueAxis6);

                // GRAPH
                var insurancegraph = new AmCharts.AmGraph();
                insurancegraph.title = "Insurance";
                insurancegraph.valueField = "value";
                insurancegraph.type = "column";
                insurancegraph.balloonText = "[[category]]:[[value]]";
                insurancegraph.lineAlpha = 0;				
                insurancegraph.colorField = "color";
                insurancegraph.fillAlphas = 1;
				insurancegraph.labelText = "[[value]]";
				insurancegraph.labelPosition = "top";
                insurancechart.addGraph(insurancegraph);

                insurancechart.creditsPosition = "top-right";

                // WRITE
                insurancechart.write("insurancechartdiv");
            });
        </script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
	<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	  <tr>
	    <td align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="index.jsp">Service</a>:</td>
      </tr>
	  <tr>
	    <td height="300" align="center" valign="top">
        <table width="100%" border="0" align="center" cellpadding="5" cellspacing="0">
	     
          <tr>
	         <td width="33%" align="left" valign="top">
             Ticket Escalation
                 <div id="ticketchartdiv" style="width: 300px; height: 300px;"></div>
                  <br> 
                  <div id="callchartdiv" style="width: 300px; height: 300px;"></div>      
                  <br>       
                  <div id="jcprioritychartdiv" style="width: 300px; height: 300px;"></div> 
                       <br>      
                  <div id="jcstagechartdiv" style="width: 300px; height: 300px;"></div> 
                           <br> 
                           <div id="psfchartdiv" style="width: 300px; height: 300px;"></div>
                           <br>
                           <div id="insurancechartdiv" style="width: 300px; height: 300px;"></div>       </td>
	         <td width="33%" align="center" valign="top">
                <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
	          <tr>
	            <th colspan="2" valign="top">Open Tickets</th>
              </tr>
	          <%=mybean.TicketsOpen()%>
             </table>      <br>  
             <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
	           <tr>
	             <th colspan="2" valign="top">Call Follow-up Overdue</th>
               </tr>
	           <%=mybean.CallsOverdue()%>
             </table>	   <br>        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
	          <tr>
	            <th colspan="2" valign="top">Open Job Cards</th>
              </tr>
	          <%=mybean.JCsOpen()%>
             </table>      <br>         <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
	          <tr>
	            <th colspan="2" valign="top">Insurance Follow-up</th>
              </tr>
	          <%=mybean.InsuranceFollowup()%>
             </table></td>
	        <td width="33%" align="center" valign="top">
            <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
	          <tr>
	            <th valign="top">Service Reports</th>
              </tr>
	          <tr>
	            <td height="200" valign="top"><%=mybean.ListReports()%></td>
              </tr>
            </table></td>
          </tr>
        </table></td>
      </tr>
</table> <%@include file="../Library/admin-footer.jsp" %></body>
</html>
