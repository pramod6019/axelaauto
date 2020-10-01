<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.Campaign_Statistics" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.ClientName%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu-mobile.css" rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/jquery.js"></script>
<script type="text/javascript" src="../Library/jquery-ui.js"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<!-- Start additional plugins -->
<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
<script type="text/javascript" src="../Library/amcharts/pie.js"></script>
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
var legend;
var export1 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "Campaign Statistics"
    	}
};
var export2 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "Campaign Traffic Statistics"
    	}
};
AmCharts.ready(function () {
	<% if(mybean.UniqueVisitorChart.equals("")){%> 
		var chartData = <%=mybean.chart_data%>;
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
		chart.write("chart");
	<%}%>
	
	<% if(mybean.TrafficChart.equals("")){%>
                var trafficchartData = <%=mybean.trafficchart_data%>;
    
                // SERIAL CHART
                trafficchart = new AmCharts.AmSerialChart();
                trafficchart.dataProvider = trafficchartData;
                trafficchart.categoryField = "month";
                trafficchart.plotAreaBorderAlpha = 0.5;
		        trafficchart.exportConfig = export2;
                // the following two lines makes chart 3D
                trafficchart.depth3D = 30;
                trafficchart.angle = 30;
				trafficchart.type = "serial";

                // AXES
                // category
                var categoryAxis = trafficchart.categoryAxis;
                categoryAxis.gridAlpha = 0.1;
                categoryAxis.axisAlpha = 0;
                categoryAxis.gridPosition = "start";

                // value
                var valueAxis = new AmCharts.ValueAxis();
                valueAxis.gridAlpha = 0.1;
                valueAxis.axisAlpha = 0;
                trafficchart.addValueAxis(valueAxis);

                // GRAPH
                var graph1 = new AmCharts.AmGraph();
                graph1.title = "Views";
                graph1.labelText = "[[value]]";
                graph1.valueField = "column-1";
                graph1.type = "column";
                graph1.fillAlphas = 1;
                graph1.balloonText = "<span style='font-size:14px'>[[month]]</span><br><span style='font-size:14px'>[[title]]:<b>[[value]]</b></span>";
                trafficchart.addGraph(graph1);
				
				 // LEGEND
                var legend = new AmCharts.AmLegend();
                legend.borderAlpha = 0.2;
                legend.horizontalGap = 10;
				legend.markerLabelGap = 25;
				legend.valueAlign = "left";
                trafficchart.addLegend(legend);

                // WRITE
                trafficchart.write("chartdiv");
				<%}%>
});
</script>


  </HEAD>

  <body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
  <%@include file="../portal/header.jsp" %>
  <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
    <TR>
    <TD align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href=campaign.jsp>Campaign</a> &gt; <a href="../mktg/campaign-list.jsp?all=recent">List Campaign</a> &gt; <a href=campaign-list.jsp?campaign_id=<%=mybean.campaign_id%>><%=mybean.campaign_subject%> (<%=mybean.campaign_id%>)</a> &gt; <a href="../portal/news-letter-statistics.jsp">Statistics</a>:</TD>
    </TR>
          <tr>
          <td align="center"><font color="red"><b><%=mybean.msg%></b></font><br/></td>
        </tr>
          <tr>
          <td  align="center"><form method="post" name="frm1"  id="frm1">
              <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
                <tr>
                  <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable" >
                      <tr>
                        <th align="center">Unique Visitor Statistics</th>
                      </tr>
                      
                      <tr height="200" align="center">
        <td align="center" valign="top">
		<% if(mybean.UniqueVisitorChart.equals("")){%>
            <div id="chart" style="height:500px;"></div>
            <%}else{%>
            <font color="red"><b>
            <%=mybean.UniqueVisitorChart%>
            <%}%></b></font>
		</td>
        </tr>
        
        <tr align="center">
          <th align="center">Trafic Statistics</th>
        </tr>
        
        <tr height="200" align="center">
          <td align="center" valign="top">
		  <% if(mybean.TrafficChart.equals("")){%>
          <div id="chartdiv" style="width: 1000px; height: 800px;"></div>
            <%}else{%>
            <font color="red"><b>
            <%=mybean.TrafficChart%></b></font>
            <%}%>
		</td>
        </tr>     
                    </table></td>
                </tr>
              </table>
            </form></td>
       </tr>
          
          <tr align="center">
          <td  align="center">&nbsp;</td>
        </tr>
  </TABLE><%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
