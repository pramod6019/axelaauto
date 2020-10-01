<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.sales.Form_21_Print" scope="request"/>
<%mybean.doPost(request,response); %>

