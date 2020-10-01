<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.View" scope="request"/>
<%mybean.doGet(request,response); %>
<HTML>
    <HEAD>
    <title><%=mybean.AppName%> - <%=mybean.ClientName%></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
    </HEAD>
    <body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0"></body>
</HTML>
