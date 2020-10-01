<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Ticket_Summary_Print_PDF" scope="request"/>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<%mybean.doPost(request,response); %>

