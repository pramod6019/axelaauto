<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_Vehicle_Service_Booking_Followup" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
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
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu-mobile.css" rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script><script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script><script type="text/javascript" src="../Library/jquery.js"></script><script type="text/javascript" src="../Library/jquery-ui.js"></script><script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>

<script language="JavaScript" type="text/javascript">
	
 $(function() {
  $( "#txt_start_time" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	 $( "#txt_end_time" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });       
  });
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" >
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
  <TR>
    <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="report-vehicle-service-booking-followup.jsp">Service Booking Follow-up</a>:</TD>
  </TR>
  <tr>
    <td align="center"><font color="red"><b> <%=mybean.msg%> </b></font><br/></td>
  </tr>
  <tr align="center">
    <td colspan="2" align="center" valign="middle"><form method="post" name="frm1"  id="frm1">
        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
          <tr>
            <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                <tbody>
                  <tr align="center">
                    <th colspan="9"><b>Service Booking Follow-up</b></th>
                  </tr>
                  <tr valign="left">
                    <td align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable">
                        <tr>
                         <td align="right">Branch:</td>
                       <td>   
                      <%=mybean.PopulateBranch() %>    
                    </td>
                          <td align="right">CRM Executive:</td>
                     <td>
                      <span id="exeHint"><%=mybean.PopulateCRMExecutives(mybean.empcrm_id, mybean.comp_id, mybean.ExeAccess)%></span>
                    </td>
                        <td align="right" nowrap>Start Date:</td>
                        <td><input name ="txt_start_time" id="txt_start_time" type="text" class="textbox" value="<%=mybean.start_time %>" size="12" maxlength="10" /></td>
                        
                      <td align="right" nowrap>End Date:</td>    
                      <td><input name ="txt_end_time" id ="txt_end_time" type="text" class="textbox" value="<%=mybean.end_time %>" size="12" maxlength="10"/></td>
                           <td align="right">Call Type:</td>
                       <td>   
                      <%=mybean.PopulateCallType(mybean.calltype_id)%>    
                    </td>
                      <td align="right">Pending Follow-up:</td>
                          <td align="left"><input id="chk_pending_followup" name="chk_pending_followup" type="checkbox" <%=mybean.PopulateCheck(mybean.pending_followup)%> /></td>
                           <td align="center"><input name="submit_button" type="submit" class="button" id="submit_button" value="Go" />
                            <input type="hidden" name="submit_button" value="Submit"/></td>
                        </tr>
                        
                      </table></td>
                  </tr>
                </tbody>
              </table></td>
          </tr>
        </table>
      </form></td>
  </tr>
  <tr align="center">
    <td colspan="2" align="center" valign="middle">&nbsp;</td>
  </tr>
  <tr align="center">
    <td height="300" colspan="2" align="center" valign="top"><%=mybean.StrHTML%></td>
  </tr>
  <tr align="center">
    <td colspan="2" align="center" valign="middle">&nbsp;</td>
  </tr>
  <tr align="center">
    <td colspan="2" align="center" valign="middle"></td>
  </tr>
</TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</html>