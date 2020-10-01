<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Call_Export" scope="request"/>
<jsp:useBean id="export" class="axela.service.Call_Export" scope="request"/>
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
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<table width="98%" height="300" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
  <td ><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
    <tr>
      <td valign="top"><a href="../portal/home.jsp">Home</a> &gt; <a href="../service/index.jsp">Service</a> &gt; <a href="call.jsp">Call</a> &gt; <a href="call-export.jsp">Export</a>:</td>
      </tr>
    <tr>
      <td align="center" valign="top" height="300">
<table width="100%" border="1" cellspacing="0" cellpadding="0" class="listtable">
  <tr>
    <th align="center">Export</th>
  </tr>
  <tr align="center">
    <td height="70" align="center" valign="middle"><form name="frmexport" method="get" target="_blank" action="<%=export.exportpage %>">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td align="right">Report Type:&nbsp;</td>
            <td><select name="report" class="selectbox" id="report">
                <%=export.PopulatePrintOption() %>
              </select></td>
            <td align="right">Export Format:&nbsp;</td>
            <td><select name="exporttype" class="selectbox" id="exporttype">
                <%=mybean.PopulateExportFormat(mybean.PadQuotes(request.getParameter("exporttype"))) %>
              </select></td>
            <td align="center"><input name="btn_export" id="btn_export" type="submit" class="button" value="Export"></td>
          </tr>
        </table>
      </form></td>
  </tr>
</table><!-- #EndLibraryItem --></td>
      </tr>
    <tr align="center">
      <td align="center" valign="middle">&nbsp;</td>
      </tr>
    </table></td>
</tr>

	</table> <%@include file="../Library/admin-footer.jsp" %></body>
</html>
