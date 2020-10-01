<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.portal.Franchisee" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="header.jsp" %>
	<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	  <tr>
	    <td><a href="home.jsp">Home</a> &gt; <a href="franchisee.jsp">Franchisee</a>:</td>
      </tr>
</table> <%@include file="../Library/landing-branch.jsp" %> <%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp"%>
</body>
</html>
