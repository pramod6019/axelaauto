<%-- 
    Document : inventory-check
    Created on: Sep 18, 2012, 11:30:29 AM
    Author   : Ajit
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="mybean" class="axela.inventory.Inventory_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
