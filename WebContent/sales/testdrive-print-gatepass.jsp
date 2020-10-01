<%-- 
    Document : quote-check
    Created on: Sep 17, 2012, 4:49:28 PM
    Author   : Ajit
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="mybean" class="axela.sales.TestDrive_Print_GatePass" scope="request"/>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/portal-style.css">
<%mybean.doPost(request,response); %>
<table width="700" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><%=mybean.StrHTML%></td>
  </tr>
</table>
