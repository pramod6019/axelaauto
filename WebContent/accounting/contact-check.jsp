<%-- 
    Document : quote-check
    Created on: Sep 17, 2012, 4:49:28 PM
    Author   : Ajit
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.accounting.Contact_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>