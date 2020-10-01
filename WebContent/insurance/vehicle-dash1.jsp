<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Vehicle_Dash" scope="request"/>
<%mybean.doPost(request, response);%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
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
    
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<% if (!mybean.modal.equals("yes")) {%>    
<%@include file="../portal/header.jsp" %>
<%}%>         
<table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="top"><script type="text/javascript">
$(function() {    
$( "#tabs" ).tabs({
	alert("inside veh dash tabs");
event: "mouseover"
});
}); 
</script></td>
  </tr>
  <tr valign="top" bgcolor="#FFFFFF">
    <td colspan="4" align="center" height="300">
    <div id="tabs">
        <ul>
          <li><a href="#tabs-1">Vehicle Info</a></li>
          <li><a href="#tabs-2">Customer</a></li>
          <li><a href="#tabs-3">Ownership</a></li>
          <li><a href="#tabs-4">Calls</a></li>
          <li><a href="#tabs-5">Job Cards</a></li>
          <li><a href="#tabs-6">Invoices</a></li>
          <li><a href="#tabs-7">Receipts</a></li>
          <li><a href="#tabs-8">Insurance</a></li>
          <li><a href="#tabs-9">Insurance Follow-up</a></li>
        </ul>
        <div id="tabs-1"><%=mybean.StrHTML%></div>
        <div id="tabs-2"><%=mybean.customer_info%></div>
        <div id="tabs-3"><%=mybean.ownership_info%></div>
        <div id="tabs-4"><%=mybean.call_info%></div>
        <div id="tabs-5"><%=mybean.jobcard_info%></div>
        <div id="tabs-6"><%=mybean.invoice_info%></div>
        <div id="tabs-7"><%=mybean.receipt_info%></div>
        <div id="tabs-8"><%=mybean.insurance_info%></div>
        <div id="tabs-9"><%=mybean.followup_info%></div>
      </div></td>
  </tr>    
</table>       
<% if (!mybean.modal.equals("yes")) {%>  
<!-- #EndLibraryItem --> <%@include file="../Library/admin-footer.jsp" %>
<%}%>
</body>
</HTML>
