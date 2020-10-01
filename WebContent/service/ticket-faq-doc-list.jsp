<%-- 
    Document : ticket-add
    Created on: Feb 11, 2013
    Author   : Gurumurthy TS
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Ticket_Faq_Doc_List" scope="request"/>
<%mybean.doGet(request, response);%>
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
        <body   leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %>
        <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
          <TR>
            <TD height="20" ><a href="../service/home.jsp">Home</a> &gt; <a href="ticket-faq-list.jsp?all=yes">List FAQs</a> &gt; <a href="ticket-faq-list.jsp?ticketfaq_id=<%=mybean.ticketfaq_id%>"><%=mybean.ticketfaq_question%></a> &gt; <a href="ticket-faq-doc-list.jsp?ticketfaq_id=<%=mybean.ticketfaq_id%>">List Documents</a>: </TD>
          </TR>
          <TR>
            <TD  align="center" vAlign="top" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <TBODY>
                <tr>
                    <td width="30%" align="right"><a href="../portal/docs-update.jsp?add=yes&ticketfaq_id=<%=mybean.ticketfaq_id%>">Add
                      New Document...</a><br></td> 
                  </tr>
                <tr>
                    <td align="center"><font color="#ff0000" ><b> <%=mybean.msg%> </b></font></td>
                  </tr>
                <tr>
                    <td align="center"><strong><%=mybean.RecCountDisplay%></strong></td>
                  </tr>
                <tr>
                    <td align="center"><%=mybean.PageNaviStr%></td>
                  </tr>
                <tr>
                    <td valign="top" height="200" align="center"><%=mybean.StrHTML%></td>
                  </tr>
                <tr>
                    <td align="center"><%=mybean.PageNaviStr%></td>
                  </tr>
                <tr>
                    <td align="center">&nbsp;</td>
                  </tr>
            </table></TD>
          </TR>
        </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
