<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_Call_Dashboard1" scope="request"/>
<%mybean.doPost(request,response);%>
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
    <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
    <body   leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
        <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0" >
        <TR>
    <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="../service/report-call-dashboard1.jsp">Call Dashboard</a>:</TD>
  </TR>
            <TR>
                <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font><br>
                </TD>
            </TR>
            
            <TR>
                <TD  align="middle" vAlign="top"><table width="100%"  border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td colspan="2" align="center" valign="top"><table width="100%"  border="0" cellpadding="0" cellspacing="0" class="listtable">
                                    <tr align="center" valign="top">
                                      <td width="25%" ><table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                                        <tr>
                                          <th colspan="2">Calls for Today</th>
                                        </tr>
                                        <%=mybean.StrCallsToday%>
                                      </table></td>
                                      <td width="25%"><table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0" class="listtable" >
                                        <tr align="center">
                                          <th colspan="2"> Follow-up for Today</th>
                                        </tr>
                                        <%=mybean.StrFollowupToday%>
                                      </table></td>
                                      <td width="25%"><table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0" class="listtable" >
                                        <tr align="center">
                                          <th colspan="2">Closed Follow-up for Today</th>
                                        </tr>
                                        <%=mybean.StrClosedFollowups%>
                                      </table></td>
                                      <td width="25%"><table width="100%"  border="1" align="center" cellpadding="0" cellspacing="0" class="listtable" >
                                        <tr align="center">
                                          <th colspan="2">Priority</th>
                                        </tr>
                                        <%=mybean.StrPriority%>
                                      </table></td>
                                    </tr>
                          </table></td>
                        </tr>
                        <tr valign="center">
                            <td colspan="3">&nbsp;</td>
                        </tr>
                        <tr>
                            <td align="center"><table width="100%"  border="1" cellspacing="0" cellpadding="0" class="listtable">
                              <tr align="center">
                                <th colspan="8">Calls Traffic</th>
                              </tr>
                              <tr align="center">
                                <td><%=mybean.day8name%><br><%=mybean.day8%></td>
                                <td><%=mybean.day7name%><br><%=mybean.day7%></td>
                                <td><%=mybean.day6name%><br><%=mybean.day6%></td>
                                <td><%=mybean.day5name%><br><%=mybean.day5%></td>
                                <td><%=mybean.day4name%><br><%=mybean.day4%></td>
                                <td><%=mybean.day3name%><br><%=mybean.day3%></td>
                                <td><%=mybean.day2name%><br><%=mybean.day2%></td>
                                <td><%=mybean.day1name%><br><%=mybean.day1%></td>
                              </tr>
                              <tr align="center">
                                <td colspan="8">&nbsp;</td>
                              </tr>
                              <tr align="center">
                                <td colspan="2">Week <%=mybean.week4%><br><%=mybean.logWeek4%></td>
                                <td colspan="2">Week <%=mybean.week3%><br><%=mybean.logWeek3%></td>
                                <td colspan="2">Week <%=mybean.week2%><br><%=mybean.logWeek2%></td>
                                <td colspan="2">Week <%=mybean.week1%><br><%=mybean.logWeek1%></td>
                              </tr>
                              <tr align="center">
                                <td colspan="8">&nbsp;</td>
                              </tr>
                              <tr align="center">
                                <td colspan="2"><%=mybean.month4%><br><%=mybean.logMonth4%></td>
                                <td colspan="2"><%=mybean.month3%><br><%=mybean.logMonth3%></td>
                                <td colspan="2"><%=mybean.month2%><br><%=mybean.logMonth2%></td>
                                <td colspan="2"><%=mybean.month1%><br><%=mybean.logMonth1%></td>
                              </tr>
                              <tr align="center">
                                <td colspan="8">&nbsp;</td>
                              </tr>
                              <tr align="center">
                                <td colspan="2"><%=mybean.qur4%><br><%=mybean.logQur4%></td>
                                <td colspan="2"><%=mybean.qur3%><br><%=mybean.logQur3%></td>
                                <td colspan="2"><%=mybean.qur2%><br><%=mybean.logQur2%></td>
                                <td colspan="2"><%=mybean.qur1%><br><%=mybean.logQur1%></td>
                              </tr>
                            </table></td>
                        </tr>
                        <tr>
                            <td align="center">&nbsp;</td>
                            
                        </tr>
                </table></TD>
            </TR>
        </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
