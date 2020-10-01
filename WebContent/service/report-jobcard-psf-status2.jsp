<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_JobCard_PSF_Status" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
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
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<script language="JavaScript" type="text/javascript">
  
    function ExeCheck() { //v1.0
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/report-check.jsp?multiple=yes&jcpsfexecutive=yes&branch_id=' + GetReplace(branch_id),'exeHint');
    }  
    function ExeTechnicianCheck() { //v1.0
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/report-check.jsp?multiple=yes&technician=yes&branch_id=' + GetReplace(branch_id),'technicianHint');
    }
  
    function ExeAdvisorCheck() { //v1.0
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/report-check.jsp?multiple=yes&advisor=yes&branch_id=' + GetReplace(branch_id),'advisorHint');
    }

	function PopulatePsfdays(){
		var branch_id=document.getElementById("dr_branch").value;
		//alert(branch_id);
		showHint('../service/report-check.jsp?multiple=yes&psfdays=yes&branch_id=' + GetReplace(branch_id),'followupHint');
	}
	
    </script>
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
var chart1, chartq1, chartconcern;
var legend1, legendq1,legendq2, legendq3, legendq4, legendq5, legendconcern;
var export1 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF Feedback"
    	}
};
var exportq1 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF 3rd Day Q1"
    	}
};
var exportq2 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF 3rd Day Q2"
    	}
};
var exportq3 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF 3rd Day Q3"
    	}
};
var exportq11 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF 7th Day Q1"
    	}
};
var exportq12 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF 7th Day Q2"
    	}
};
var exportq13 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF 7th Day Q3"
    	}
};
var exportq14 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF 7th Day Q4"
    	}
};
var exportq15 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF 7th Day Q5"
    	}
};
var exportq16 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF 7th Day Q6"
    	}
};
var exportq17 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF 7th Day Q7"
    	}
};
var exportconcern = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "JobCard PSF Concern"
    	}
};

AmCharts.ready(function () {
	 <% if(mybean.NoChart.equals("")){%> 
		 var chartData = <%=mybean.chart_data%>;
		// PIE CHART
		chart1 = new AmCharts.AmPieChart();
		chart1.dataProvider = chartData;
		chart1.titleField = "type";
		chart1.valueField = "total";
		chart1.minRadius = 200;
		
		// LEGEND
		legend1 = new AmCharts.AmLegend();
		legend1.align = "center";
		legend1.markerType = "circle";
		chart1.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
		chart1.exportConfig = export1;
		chart1.addLegend(legend1);
		
		// WRITE
		chart1.write("chart1");
	<%}%>
					  
	<% if(mybean.NoChartQ1.equals("")){%>
		var dataq1 = <%=mybean.chart_data_q1%>;
		// PIE CHART
		chartq1 = new AmCharts.AmPieChart();
		chartq1.dataProvider = dataq1;
		chartq1.titleField = "type";
		chartq1.valueField = "total";
		chartq1.minRadius = 200;
		
		// LEGEND
		legendq1 = new AmCharts.AmLegend();
		legendq1.align = "center";
		legendq1.markerType = "circle";
		chartq1.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
		chartq1.exportConfig = exportq1;
		chartq1.addLegend(legendq1);
		
		// WRITE
		chartq1.write("chartq1");
	<%}%>
	   
	<% if(mybean.NoChartConcern.equals("")){%>
		var dataconcern = <%=mybean.chart_data_concern%>;
		// PIE CHART
		chartconcern = new AmCharts.AmPieChart();
		chartconcern.dataProvider = dataconcern;
		chartconcern.titleField = "type";
		chartconcern.valueField = "total";
		chartconcern.minRadius = 200;
		
		// LEGEND
		legendconcern = new AmCharts.AmLegend();
		legendconcern.align = "center";
		legendconcern.markerType = "circle";
		chartconcern.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
		chartconcern.exportConfig = exportconcern;
		chartconcern.addLegend(legendconcern);
		
		// WRITE
		chartconcern.write("chartconcern");
	<%}%>
});

</script>

<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
  <TR>
     <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="report-jobcard-psf-status.jsp">PSF Follow-up</a>:</TD>
  </TR>
        <tr>
          <td align="center"><font color="red"><b><%=mybean.msg%></b></font><br/></td>
        </tr>
        <tr>
          <td align="center"><form method="post" name="frm1"  id="frm1">
              <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0"  class="tableborder">
                <tr>
                  <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable" >
                      <tr>
                        <th colspan="8" align="center">PSF Follow-up Status</th>
                      </tr>
                      <tr>
                        
                        <td align="right" valign="top">Branch<font color=red>*</font>:</td>
                        <td align="left" valign="top"><%if(mybean.branch_id.equals("0")){%>
                        <select name="dr_branch" id="dr_branch" class="selectbox" onChange="ExeCheck(); ExeTechnicianCheck(); ExeAdvisorCheck(); PopulatePsfdays();">
                            <%=mybean.PopulateBranch(mybean.dr_branch_id,"",request)%>
                          </select>
                        <%}else{%>
                        <input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
                        <%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
                        <%}%></td>
                        <td align="right" valign="top" nowrap>Start Date<font color=red>*</font>:</td>
                        <td valign="top"><input name ="txt_starttime" id="txt_starttime" type="text" class="textbox" value="<%=mybean.start_time %>" size="12" maxlength="10" /></td>
                        <td align="right" valign="top" nowrap>End Date<font color=red>*</font>:</td>
                        <td valign="top"><input name ="txt_endtime" id ="txt_endtime" type="text" class="textbox" value="<%=mybean.end_time %>" size="12" maxlength="10"/></td>
                        <td colspan="2" align="center" valign="top" nowrap>
                         
                          <input name="submit_button" type="submit" class="button" id="submit_button" value="Go" />
                        <input type="hidden" name="submit_button" value="Submit"/></td>
                      </tr>
                      <tr>
                        <td align="right" valign="top">Executive:</td>
                        <td valign="top"><span id="exeHint"><%=mybean.PopulatePSFExecutives()%></span></td>
                        <td align="right" valign="top">Model:</td>
                        <td valign="top"><select name="dr_model" size="10" multiple="multiple" class="selectbox" id="dr_model" style="width:250px">
                            <%=mybean.PopulateModel()%>
                          </select></td> 
                        <td align="right" valign="top">Days:</td>
                        <td valign="top"><span id="followupHint"><%=mybean.reportexe.PopulateListPSFDays(mybean.dr_branch_id, mybean.jcpsfdays_ids)%></span></td>
                        <td align="right" valign="top">&nbsp;</td> 
                        <td valign="top">&nbsp;</td>
                      </tr>
                      
                      
                      <tr>
                      
                        <td align="right" valign="top">Advisor:</td>
                        <td valign="top"><span id="advisorHint"><%=mybean.reportexe.PopulateServiceAdvisors(mybean.dr_branch_id, mybean.advisorexe_ids, mybean.ExeAccess)%></span></td>
                        <td align="right" valign="top">Technician:</td>
                        <td valign="top"><span id="technicianHint"><%=mybean.reportexe.PopulateTechnicians(mybean.dr_branch_id, mybean.technicianexe_ids, mybean.ExeAccess)%></span></td>
                        <td align="right" valign="top">&nbsp;</td> 
                        <td valign="top">&nbsp;</td>
                        <td align="right" valign="top">&nbsp;</td> 
                        <td valign="top">&nbsp;</td>
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
          <td  align="center" valign="top" height="100">
        <%if(mybean.go.equals("Go")) {%>
           
            <% if(mybean.NoChart.equals("")){%>
            <h3><u>Feedback Type</u></h3>
              <div id="chart1" style="height:700px; width:1000px;"></div>
              <b>Total: <%=mybean.chart_data_total%></b>
               <%}else{%>
            <%=mybean.NoChart%>
            <%}%>
            
            <br><br><br>
            <% if(mybean.NoChartQ1.equals("")){%>
           <h3><u> Q1. How is the vehicle performing?</u></h3>
             <div id="chartq1" style="height:700px; width:1000px;"></div>
             <b>Total: <%=mybean.chart_data_q1_total%></b>             
            <%}else{%>
            <%=mybean.NoChartQ1%>
            <%}%>
             <br><br><br>
        <%}%>
            </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr><td>&nbsp;</td></tr>

</TABLE>
 <%@include file="../Library/admin-footer.jsp" %>
</body>
</HTML>
