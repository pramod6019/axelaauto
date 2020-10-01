<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_SO_Promised" scope="request"/>
<%mybean.doPost(request,response); %>
<jsp:setProperty name="mybean" property="*" />
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

     <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
    <script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  //document.formcontact.txt_customer_name.focus();
}
function frmSubmit()
{
	document.formcontact.submit();
}
    </script>

    <body bgColor="#ffffff" onLoad="FormFocus();" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
    <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
    <TR>
      <TD height="20" align="center"><font color="red"><b><%=mybean.msg%></b></font></TD>
    </TR>
    <TR>
    <TD height="20" align="center" bgColor="white">
    <form name="form1" method="get">
    <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
    <tr>
      <th colspan="7">Sales Order Pending Delivery</th>
    </tr>
      <tr>
        <td align="right" valign="left"> Branch: </td>
        <td valign="left"><select name="dr_branch" id="dr_branch" onChange="document.form1.submit()" class="selectbox">
          <%=mybean.PopulateBranch(mybean.dr_branch_id,"", "1,2", "", request)%>
        </select></td>
        <td align="right">Model:</td>
          <td><select name="dr_model_id" class="selectbox" id="dr_model_id" onChange="document.form1.submit()" >
            <%=mybean.PopulateModel()%>
          </select></td>
          <td align="right">Order By:</td>
          <td><select name="dr_order_by" class="selectbox" id="dr_order_by" onChange="document.form1.submit()" >
            <%=mybean.PopulateOrderBy()%>
          </select></td>
<td align="center"><a href="salesorder-list.jsp?smart=yes" target="_blank">(Export)</a></td>
</tr>
<tr>
  <td align="right">Sales Consultant:</td>
  <td><select name="dr_executive_id" class="selectbox" id="dr_executive_id" onChange="document.form1.submit()">
    <%=mybean.PopulateExecutive() %>
  </select></td>
  <td align="right">Delivery Status:</td>
  <td><select name="dr_status_id" class="selectbox" id="dr_status_id" onChange="document.form1.submit()" >
    <%=mybean.PopulateStatus()%>
  </select></td>
  <td align="right">Critical:</td>
  <td><select name="dr_critical" class="selectbox" id="dr_critical" onChange="document.form1.submit()" >
    <%=mybean.PopulateCritical()%>
  </select></td>
<td>&nbsp;</td>
</tr>
    </table></form></TD>
    </TR>
    <tr>
      <td height="200" align="center" valign="top"><%=mybean.StrHTML%></td>
      </tr>
    </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
    </HTML>
