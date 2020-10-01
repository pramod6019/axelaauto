<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.Campaign_Export" scope="request"/>
<jsp:useBean id="export" class="axela.mktg.Campaign_Export" scope="request"/>
<%export.doPost(request,response); %>
<HTML>
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
<script type="text/javascript" src="../Library/jquery.js"></script>
<script type="text/javascript" src="../Library/jquery-ui.js"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>

</HEAD>
<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %><table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
  <td ><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
    <tr>
        <td valign="top"><a href="../portal/home.jsp">Home</a> &gt; <a href="index.jsp">Marketing</a> &gt;  <a href="campaign.jsp">Campaigns</a> &gt; <a href="campaign-export.jsp">Export</a>:</td>
      </tr>
    <tr>
      <td  align="center" valign="top" height="300"><%@include file="../Library/export.jsp" %></td>
      </tr>
    <tr align="center">
      <td align="center" valign="middle">&nbsp;</td>
      </tr>
    </table></td>
</tr>

	</table><%@include file="../Library/admin-footer.jsp" %></body>
</html>
