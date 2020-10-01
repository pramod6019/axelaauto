<%--
    Document : inventory-location-check
    Created on: Oct 16, 2012, 11:30:29 AM
    Author   : Ajit
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_AltUOM_Check" scope="request"/>
<%mybean.doPost(request,response); %>          
<%=mybean.StrHTML%>  
  