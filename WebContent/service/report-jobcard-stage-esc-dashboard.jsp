<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_JobCard_Stage_Esc_Dashboard" scope="request"/>
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
<script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  //document.formcontact.txt_customer_name.focus(); 
}
function frmSubmit() 
{
	document.formemp.submit();
}
    </script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
 <%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0" >
<TR>
    <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="report-jobcard-stage-esc-dashboard.jsp">Job Card Stage Escalation Dashboard</a>:</TD>
  </TR>
<TR>
  <TD align="left" >&nbsp;</TD>
</TR>
  <tr><td>
  <form name="formemp"  method="post">
  <table width="100%" border="1" cellpadding="0" cellspacing="0" class="tableborder">
    <tr>
      <td><table width="100%"  border="0" cellpadding="0" cellspacing="0" class="listtable">
        <tr>
          <th colspan="5" align="right" valign="top">Job Card Stage Escalation Dashboard</th>
          </tr>
        <tr>
          <td align="right" valign="top">Stage: </td>
          <td align="left" valign="top"><select name="dr_stage" size="10" multiple="multiple" class="selectbox" id="dr_stage" style="width:250px">
            <%=mybean.PopulateStage()%>
            </select></td>
          <td align="right" valign="top">Executive: </td>
          <td align="left" valign="top"><select name="dr_emp" size="10" multiple="multiple" class="selectbox" id="dr_emp" style="width:250px">
            <%=mybean.PopulateAllExecutive()%>
            </select></td>
          <td align="center" valign="top"><input name="submit_button" type="submit" class="button" id="submit_button" value="Go" />
            <input type="hidden" name="submit_button" value="Submit"></td>
          </tr>
        </table></td>
      </tr>
  </table> </form></td></tr>
      
      <tr>
        <td>&nbsp;</td></tr>    
    
        <tr align="center">
          <td>&nbsp;</td>
        </tr>
        <tr align="center">
          <td height="200"align="center" valign="top"><%=mybean.StrHTML%></td>
        </tr>
        <tr align="center">
          <td align="center" valign="middle">&nbsp;</td>
        </tr>
        <tr align="center">
          <td align="center" valign="middle"></td>
        </tr>
     
    </table> <%@include file="../Library/admin-footer.jsp" %></body>
</html>
