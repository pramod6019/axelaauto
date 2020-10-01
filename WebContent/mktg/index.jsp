<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.Index" scope="request"/>
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
    <script type="text/javascript" src="../Library/dynacheck.js"></script>
    <script type="text/javascript" src="../Library/jquery.js"></script>
    <script type="text/javascript" src="../Library/jquery-ui.js"></script>
    <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
   <!-- Start additional plugins -->
<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
<script type="text/javascript" src="../Library/amcharts/funnel.js"></script>
<!-- End additional plugins -->
<% if(mybean.NoSalesPipeline.equals("")){%>
	<script language="javascript" type="text/javascript">
	AmCharts.ready(function () {
		var chart;
		var data = <%=mybean.SalesPipeline()%>;
		chart = new AmCharts.AmFunnelChart();
		chart.titleField = "title";
		chart.balloon.cornerRadius = 0;
		chart.marginRight = 150;
		chart.marginLeft = 15;
		chart.labelPosition = "right";
		chart.funnelAlpha = 0.9;
		chart.valueField = "total";
		chart.dataProvider = data;
		chart.startX = 0;
		chart.balloon.animationTime = 0.2;
		chart.neckWidth = "40%";
		chart.startAlpha = 0;
		chart.neckHeight = "30%";
		chart.balloonText = "[[title]]:<b>[[total]]</b>";
		chart.creditsPosition = "top-right";
		chart.write("salesfunnel");   
	});
	</script>
<%}%>  
   
    <script language="JavaScript" type="text/javascript">
    function EmpCheck() { //v1.0
	
	var emp_id=document.getElementById("dr_executive").value;
	var month_id=document.getElementById("dr_month").value;
	showHint('../sales/target-check.jsp?emp_id='+emp_id+'&month_id=' + month_id ,'exeHint');
	//alert("sdhsdg");
    }
    </script>
    </HEAD>

    <body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
    <%@include file="../portal/header.jsp" %>
    <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="../mktg/index.jsp">Marketing</a>:</td>
      </tr>
      <tr>
        <td height="300" align="center" valign="top"><table width="100%" border="0" align="center" cellpadding="5" cellspacing="0">
            <tr>
            <td align="center" valign="top"><form name="formindex"  method="post">
                <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr>
                    <td width="200" align="center" valign="top"><table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                        <tr>
                        <th colspan="2" valign="top">Follow-up Escalation</th>
                      </tr>
                        <%=mybean.FollowupEscStatus()%>
                      </table></td>
                    <td rowspan="3" align="center" valign="top"><table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                        <tr>
                        <th valign="top">Sales Open Pipeline</th>
                      </tr>
                        <tr>
                        <td height="450" width="350" align="center" nowrap>
						<% if(mybean.NoSalesPipeline.equals("")){%>                           
                            <div id="salesfunnel" style="height: 400px;"></div>
                            <%}else{%>
                            <%=mybean.NoSalesPipeline%>
                          <%}%>
						</td>
                      </tr>
                      </table></td>
                    <td width="25%" rowspan="5" align="center" valign="top">
                    <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                        <tr>
                        <th valign="top">Sales Reports</th>
                      </tr>
                        <tr>
                        <td height="200" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable">
                            <tr>
                             <td>
                             <a href="javascript:remote=window.open('../sales/report-branch-dash.jsp','reportsales','');remote.focus();">
                           Branch Dashboard</a></td>
                            </tr>
                             <tr>
                              <td>
                              <a href="javascript:remote=window.open('../sales/report-followup-esc-dashboard.jsp','reportsales','');remote.focus();">Follow-up Escalation Dashboard</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-followup-esc-status.jsp','reportsales','');remote.focus();"> Follow-up Escalation Status</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-oppr-esc-dashboard.jsp','reportsales','');remote.focus();"> Opportunity Escalation Dashboard</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-oppr-esc-status.jsp','reportsales','');remote.focus();">Opportunity Escalation Status</a></td>
                            </tr>
                            <tr>
                              <td>
                              <a href="javascript:remote=window.open('../sales/report-sales-dash.jsp','reportsales','');remote.focus();">
                              Sales Dashboard</a></td>
                            </tr>
                             <tr>
                              <td>
                              <a href="javascript:remote=window.open('../sales/report-sales-pipeline.jsp','reportsales','');remote.focus();">
                             Sales Pipeline</a></td>
                            </tr>
                            <tr>
                              <td>
                              <a href="javascript:remote=window.open('../sales/report-sales-forecast.jsp','reportsales','');remote.focus();">
                             Sales Forecasts</a></td>
                            </tr>
                            <tr>
                              <td>
                              <a href="javascript:remote=window.open('../sales/report-top-accounts.jsp','reportsales','');remote.focus();">
                             Top Accounts</a></td>
                            </tr>
                             <tr>
                              <td>
                              <a href="javascript:remote=window.open('../sales/report-top-products.jsp','reportsales','');remote.focus();">
                              Top Products</a></td>
                            </tr>
                             <tr>
                              <td>
                              <a href="javascript:remote=window.open('../sales/report-product-dash.jsp','reportsales','');remote.focus();">
                              Products Dashboard</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-campaign-dash.jsp','reportsales','');remote.focus();">
                             Campaign Dashboard</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-soe-dash.jsp','reportsales','');remote.focus();">
                              SOE Dashboard</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-sob-dash.jsp','reportsales','');remote.focus();">
                              SOB Dashboard</a></td>
                            </tr>
                             <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-target-dash.jsp','reportsales','');remote.focus();">
                             Target Dashboard</a></td>
                            </tr>
                             <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-crmfollowup-esc-dashboard.jsp','reportsales','');remote.focus();">CRM Follow-up Escalation Dashboard</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-crmfollowup-esc-status.jsp','reportsales','');remote.focus();">CRM Follow-up Escalation Status</a></td>
                            </tr>
                             <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-demo-cust-fb-dash.jsp','reportsales','');remote.focus();">
                              Customer Demo Feedback</a></td>
                            </tr>
                             <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-crmfollowup.jsp','reportsales','');remote.focus();">
                             CRM Follow-up</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-crmfollowup-status.jsp','reportsales','');remote.focus();">CRM Follow-up Status</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-cfs-dash.jsp','reportsales','');remote.focus();">
                             CFS Dashboard</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-totalbalance.jsp','reportsales','');remote.focus();">
                             Total Balance Dues</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-balance.jsp','reportsales','');remote.focus();">
                              Balance Dues</a></td>
                            </tr>                     
                            
                            <%if(!mybean.AppRun().equals("1")){%>
                            
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-target-dash.jsp','reportsales','');remote.focus();">
                             Executive Dashboard</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/mis-dash-enquiry.jsp','reportsales','');remote.focus();">
                              Opportunity Dashboard</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/mis-dash-target.jsp','reportsales','');remote.focus();">
                              Competition Dashboard</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/balance-trigger-status.jsp','reportsales','');remote.focus();">
                             Balance Escalation Status</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/balance-report-pendingauth.jsp','reportsales','');remote.focus();">Authorization Pending</a></td>
                            </tr>
                            <%}%>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-so-wf-pending.jsp','reportsales','');remote.focus();">
                             Pending Work Flow</a></td>
                              <!--<td><a href="installment-report-pendingauth.jsp" target="_blank">Pending Work Flow</a></td>--> 
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-so-delivered.jsp?dr_date=1','reportsales','');remote.focus();">Sales Orders</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-so-delivered.jsp?dr_date=2','reportsales','');remote.focus();">
                             Sales Order Promised</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-so-delivered.jsp?dr_date=3','reportsales','');remote.focus();">Sales Order Delivered</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-so-pendingdelivery.jsp','reportsales','');remote.focus();">Sales Orders Pending Delivery</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-demo-fb-pending.jsp','reportsales','');remote.focus();">
                            Demo Feedback Pending</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-demo-cust-fb-pending','reportsales','');remote.focus();">
                             Demo Customer Feedback Pending</a></td>
                            </tr>
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-stock-exe.jsp','reportsales','');remote.focus();">
                              Executive Stock Status</a></td>
                            </tr>                           
                            <tr>
                              <td>
                               <a href="javascript:remote=window.open('../sales/report-oppr-history.jsp','reportsales','');remote.focus();">
                            Opportunity History</a></td>
                            </tr>
                          </table></td>
                      </tr>
                      </table></td>
                  </tr>
                <tr>
                    <td align="center" valign="top"><table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                        <tr>
                        <th colspan="2" valign="top">Opportunity Escalation</th>
                      </tr>
                        <%=mybean.OpprEscStatus()%>
                      </table></td>
                  </tr>
                <tr>
                  <td align="center" valign="top"><table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                    <tr>
                      <th colspan="2" valign="top">CRM Follow-up Escalation</th>
                    </tr>
                    <%=mybean.CRMFollowupEscStatus()%>
                  </table></td>
                  </tr>
                <tr>
                    <td colspan="2" align="center" valign="top"><table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                        <tr>
                        <th valign="top">My Dashboard</th>
                      </tr>
                        <tr>
                        <td align="center" valign="top">Month: <%=mybean.PopulateMonth()%> Executives: <%=mybean.PopulateSalesExecutives()%></td>
                      </tr>
                        <tr>
                        <td><span id="exeHint"><%=mybean.ListTarget()%></span></td>
                      </tr>
                      </table></td>
                  </tr>
                <tr>
                    <td align="center" valign="top">&nbsp;</td>
                    <td align="center" valign="top">&nbsp;</td>
                    <td align="center" valign="top">&nbsp;</td>
                  </tr>
              </table>
              </form></td>
          </tr>
          </table></td>
      </tr>
    </table><%@include file="../Library/admin-footer.jsp" %></body>
</html>
