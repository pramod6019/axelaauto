<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_JobCard_Priority_Esc_Status" scope="request"/>
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
    <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="report-jobcard-priority-esc-status.jsp">Job Card Priority Escalation Status</a>:</TD>
  </TR>  
  <TR>
    <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font></TD>
  </TR>
  <tr><td align="center">&nbsp;</td></tr>
      <form name="formemp"  method="post">
     
        <tr align="center">
          <td colspan="2" align="center" valign="middle"><table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
            <tbody>
              <tr align="center">
                <th><b>Job Card Priority Escalation Status</b></th>
              </tr>
              <tr valign="left">
                <td align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0" >
                  <tr>
                    <td align="center">Branch:&nbsp;                      
                     <%if(mybean.branch_id.equals("0")){%>
                     <select name="dr_branch_id" class="selectbox" id="dr_branch_id" onChange="document.formemp.submit()">
                        <%=mybean.PopulateBranch(mybean.dr_branch_id,"", "1,3", "", request)%>
                    </select>                    
                    <%}else{%>
                        <input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
                        <%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
                        <%}%>
                    </td>
                  </tr>
                </table></td>
              </tr>
            </tbody>
          </table></td>
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
      </form>
    </table> <%@include file="../Library/admin-footer.jsp" %></body>
</html>
