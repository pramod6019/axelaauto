<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.accounting.Purchase_Check2" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
