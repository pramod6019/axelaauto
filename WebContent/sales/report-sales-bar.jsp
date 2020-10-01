<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Sales_Dash" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
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
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>



 <link rel="stylesheet" type="text/css" href="../Library/jqplot/jquery.jqplot.min.css">
  <link rel="stylesheet" type="text/css" href="../Library/jqplot/examples.min.css">
  <link rel="stylesheet" type="text/css" href="../Library/jqplot/syntaxhighlighter/styles/shCoreDefault.min.css">
  <link rel="stylesheet" type="text/css" href="../Library/jqplot/syntaxhighlighter/styles/shThemejqPlot.min.css">
  <!--[if lt IE 9]><script language="javascript" type="text/javascript" src="../excanvas.js"></script><![endif]-->

  
  <!-- Don't touch this! -->
    
  <script class="include" type="text/javascript" src="../Library/jqplot/jquery.jqplot.min.js"></script>
  <script type="text/javascript" src="../Library/jqplot/syntaxhighlighter/scripts/shCore.min.js"></script>
  <script type="text/javascript" src="../Library/jqplot/syntaxhighlighter/scripts/shBrushJScript.min.js"></script>
  <script type="text/javascript" src="../Library/jqplot/syntaxhighlighter/scripts/shBrushXml.min.js"></script>
  <!-- End Don't touch this! -->
  <!-- Additional plugins go here -->
<script type="text/javascript" src="../Library/jqplot/plugins/jqplot.barRenderer.min.js"></script>
  <script class="include" language="javascript" type="text/javascript" src="../Library/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
  <script class="include" language="javascript" type="text/javascript" src="../Library/jqplot/plugins/jqplot.pointLabels.min.js"></script>
  

  <!-- End additional plugins -->

  
   <script class="code" type="text/javascript">
$(document).ready(function(){
    var s1 = [200, 600, 700, 1000];
    var s2 = [460, -210, 690, 820];
    var s3 = [-260, -440, 320, 200];
    // Can specify a custom tick Array.
    // Ticks should match up one for each y value (category) in the series.
    var ticks = ['May', 'June', 'July', 'August'];
    
    var plot1 = $.jqplot('chart1', [s1, s2, s3], {
        // The "seriesDefaults" option is an options object that will
        // be applied to all series in the chart.
		// Turns on animatino for all series in this plot.
            animate: true,
            // Will animate plot on calls to plot1.replot({resetAxes:true})
            animateReplot: true,
        seriesDefaults:{
			 pointLabels: { show: true, location: 'n', edgeTolerance: -50 },
            renderer:$.jqplot.BarRenderer,
            rendererOptions: {fillToZero: true}
        },
        // Custom labels for the series are specified with the "label"
        // option on the series option.  Here a series option object
        // is specified for each series.
        series:[
            {label:'Hotel'},
            {label:'Event Regristration'},
            {label:'Airfare'}
        ],
        // Show the legend and put it outside the grid, but inside the
        // plot container, shrinking the grid to accomodate the legend.
        // A value of "outside" would not shrink the grid and allow
        // the legend to overflow the container.
        legend: {
            show: true,
            placement: 'outsideGrid'
        },
        axes: {
            // Use a category axis on the x axis and use our custom ticks.
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: ticks
            },
            // Pad the y axis just a little so bars can get close to, but
            // not touch, the grid boundaries.  1.2 is the default padding.
            yaxis: {
                pad: 1.05,
                tickOptions: {formatString: '$%d'}
            },
			
        }
    });
});
  </script>
  

  <!-- End example scripts -->
  
   <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

  <body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
  <%@include file="../portal/header.jsp" %>
  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="index.jsp">Sales</a>:</td>
    </tr>
    <tr>
      <td height="300" align="center" valign="top"><table width="100%" border="0" align="center" cellpadding="5" cellspacing="0">
          <tr>
          <td align="center" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="5">
              <tr>
                <td align="center" valign="top">&nbsp;</td>
                <td rowspan="3" align="center" valign="top"><table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                    <tr>
                      <th valign="top">Sales Bar Graph </th>
                    </tr>
                    <tr>
                      <td height="350" width="400" align="center" nowrap><div class="example-content">
                        <!-- <style type="text/css">
    
    .note {
        font-size: 0.8em;
    }
    .jqplot-yaxis-tick {
      white-space: nowrap;
    }
  </style>-->
                          <div id="chart1" style="width:700px; height:250px;"></div>
                        </div></td>
                    </tr>
                  </table></td>
                <td width="50%" rowspan="5" align="center" valign="top">&nbsp;</td>
              </tr>
            </table></td>
        </tr>
        </table></td>
    </tr>
  </table> <%@include file="../Library/admin-footer.jsp" %></body>
</html>
