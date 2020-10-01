<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_SOE_SO_Dash" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
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
<!-- Start additional plugins -->
<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
<script type="text/javascript" src="../Library/amcharts/pie.js"></script>
<!-- Start Export Image additional plugins -->
<script src="../Library/amcharts/exporting/amexport.js" type="text/javascript"></script>
<script src="../Library/amcharts/exporting/rgbcolor.js" type="text/javascript"></script>
<script src="../Library/amcharts/exporting/canvg.js" type="text/javascript"></script>
<script src="../Library/amcharts/exporting/filesaver.js" type="text/javascript"></script>
<!-- End Export Image additional plugins -->
<!-- End additional plugins -->
 <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<script language="JavaScript" type="text/javascript">
   function PopulateTeam(){		
		var branch_id=document.getElementById("dr_branch").value;	
	    showHint('../sales/report-team-check.jsp?team=yes&exe_branch_id='+branch_id,  'multiteam');		
	    }
    function ExeCheck() { //v1.0
	var branch_id=document.getElementById("dr_branch").value;
	var team_id=outputSelected(document.getElementById("dr_team").options);
	showHint('../sales/mis-check1.jsp?multiple=yes&team_id='+team_id+'&exe_branch_id=' + GetReplace(branch_id) ,'exeHint');
	//showHint('../sales/campaign-dash-check.jsp?multiple=yes&team_id='+team_id+'&exe_branch_id=' + GetReplace(branch_id) ,'exeHint');
    }
    </SCRIPT>
<script>
$(function() {
    $( "#txt_starttime" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	
	 $( "#txt_endtime" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });

  });
    </script>
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
        	fileName: "SO SOE"
    	}
};
AmCharts.ready(function () {
	<% if(mybean.NoChart.equals("")){%> 
		var chartData = <%=mybean.chart_data%>;
		// PIE CHART
		chart = new AmCharts.AmPieChart();
		chart.dataProvider = chartData;
		chart.titleField = "type";
		chart.valueField = "total";
		chart.minRadius = 200;
	
		// LEGEND
		legend = new AmCharts.AmLegend();
		legend.align = "center";
		legend.markerType = "circle";
		chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
		chart.exportConfig = export1;
		chart.addLegend(legend);
	
		// WRITE
		chart.write("chart1");
	<%}%>
});
</script> 
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
  <TR>
    <TD align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="report-soe-so-dash.jsp">Sales Order SOE Dashboard</a>:</TD>
  </TR>
        <tr>
          <td align="center"><font color="red"><b><%=mybean.msg%></b></font><br></td>
        </tr>
        <tr>
          <td align="center"><form method="post" name="frm1"  id="frm1">
              <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0"  class="tableborder">
                <tr>
                  <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable" >
                      <tr>
                        <th colspan="7" align="center">Sales Order SOE Dashboard</th>
                      </tr>
                      <tr>
                        
                        <td align="right" valign="top">Branch:</td>
                        <td align="left" valign="top"><%if(mybean.branch_id.equals("0")){%>
                        <select name="dr_branch" id="dr_branch" class="selectbox" onChange="ExeCheck();PopulateTeam();">
                            <%=mybean.PopulateBranch(mybean.dr_branch_id,"all", "1,2", "", request)%>
                          </select>
                        <%}else{%>
                        <input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
                        <%=mybean.getBranchName(mybean.dr_branch_id,mybean.comp_id)%>
                        <%}%></td>
                        <td align="right" valign="top" nowrap>Start Date<font color=red>*</font>:</td>
                        <td valign="top"><input name ="txt_starttime" id="txt_starttime" type="text" class="textbox" value="<%=mybean.start_time %>" size="12" maxlength="10" /></td>
                        <td align="right" valign="top" nowrap>End Date<font color=red>*</font>:</td>
                        <td valign="top"><input name ="txt_endtime" id ="txt_endtime" type="text" class="textbox" value="<%=mybean.end_time %>" size="12" maxlength="10"/></td>
                        <td colspan="4" align="center" valign="top" nowrap>
                         
                          <input name="submit_button" type="submit" class="button" id="submit_button" value="Go" />
                        <input type="hidden" name="submit_button" value="Submit"></td>
                      </tr>
                      <tr>
                        <td align="right" valign="top">Team:</td>
                        <td align="left" valign="top"><div id="multiteam"><select name="dr_team" size="10" multiple="multiple" class="textbox" id="dr_team" onChange="ExeCheck();" style="width:250px">
                            <%=mybean.PopulateTeam()%>
                          </select></div></td>
                        <td align="right" valign="top">Sales Consultant:</td>
                        <td valign="top"><span id="exeHint"><%=mybean.PopulateSalesExecutives()%></span></td>
                        <td align="right" valign="top">Model:</td>
                        <td valign="top"><select name="dr_model" size="10" multiple="multiple" class="textbox" id="dr_model" style="width:250px">
                            <%=mybean.PopulateModel()%>
                          </select></td>
                        <td align="center">&nbsp;</td>
                      </tr>
                    </table></td>
                </tr>
              </table>
            </form></td>
        </tr>
        <tr align="center">
          <td  align="center">&nbsp;</td>
        </tr>
        <tr align="center">
          <td  align="center" valign="top">
		  <% if(mybean.NoChart.equals("")){%>           
              <div id="chart1" style="height:700px;"></div>
              <b>Total: <%=mybean.chart_data_total%></b>      
             <%}else{%>
            <font color="red"><b>
            <%=mybean.NoChart%>
            <%}%></b></font>		  
            </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
      
        <tr align="center">
          <td  align="center"><b>Closed Won SOE Summary</b></td>
        </tr>
        <tr align="center">
          <td height="100"  align="center" valign="top"><%=mybean.StrHTML%></td>
        </tr>
        <tr align="center">
          <td  align="center" valign="top">&nbsp;</td>
        </tr>
        <tr align="center">
          <td  align="center"><b>Closed Lost SOE Summary</b></td>
        </tr>
        <tr align="center">
          <td height="100"  align="center" valign="top"><%=mybean.StrClosedHTML%></td>
        </tr>
        <tr align="center">
          <td  align="center">&nbsp;</td>
        </tr>
</TABLE>
<%@include file="../Library/admin-footer.jsp" %>
</body>
</HTML>
