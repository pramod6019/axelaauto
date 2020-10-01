<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Dispatch_List" scope="request"/>
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

function FormSubmit() { //v1.0
  document.getElementById("dr_item").value="0";
  //alert(document.getElementById("dr_subgroup").value);
  document.form1.submit();
}
        </script> 
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
    <body   leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
    
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td colspan="2">Home &gt; <a href="index.jsp">Inventory</a> &gt; Inventory Dispatches:</td>
  </tr>
  <td colspan="2" align="center"><font color="#ff0000"><b><%=mybean.msg%></b></font><br></td>
  </tr>
  <tr>
    <td width="80%" align="center"><table width="70%" border="0" align="center" cellpadding="1" cellspacing="0" bgcolor="#000000" >
        <form name="form1" method="get">
      <tr>
        <td>
        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
          <tr>
            <th colspan="2">Search</th>
          </tr>
          <tr align="center">
            <td width="30%" align="right" valign="top">Location:</td>
            <td align="left"><select name="dr_warehouse" class="selectbox" id="dr_warehouse" onChange="document.form1.submit()" visible="true">
              <%=mybean.PopulateLocation() %>
            </select></td>
          </tr>
          <tr align="center">
            <td align="right" valign="top">Category:</td>
            <td align="left"><select name="dr_category" class="selectbox" id="dr_category" onChange="document.form1.submit();" visible="true">
              <%=mybean.PopulateCategory() %>
            </select></td>
          </tr>
          <tr align="center">
            <td align="right" valign="top">Item:</td>
            <td align="left"><select name="dr_item" class="selectbox" id="dr_item" onChange="document.form1.submit()" visible="true">
              <%=mybean.PopulateItem() %>
            </select></td>
          </tr>
        </table>
       </td>
      </tr> </form>
    </table></td>
    <td align="center"><a href="inventory-dispatch-update.jsp?Add=yes&dispatch_id=<%=mybean.dispatch_id%>">Add New Dispatch...&nbsp;</a></td>
  </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" align="center"><strong><%=mybean.RecCountDisplay%></strong></td>
  </tr>
  <tr>
    <td colspan="2" align="center"><%=mybean.PageNaviStr%></td>
  </tr>
  <tr>
    <td valign="top" height="200" colspan="2" align="center"><%=mybean.StrHTML%></td>
  </tr>
  <tr>
    <td colspan="2" align="center"><%=mybean.PageNaviStr%></td>
  </tr>
  <tr>
    <td colspan="2" align="center">&nbsp;</td>
  </tr>
</table> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
