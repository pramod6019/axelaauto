<%--
    Document : veh-quote-email
    Created on: Jan 12, 2013, 4:49:28 PM
    Author   : Ajit
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="mybean" class="axela.sales.Veh_Quote_Email" scope="request"/>
<%mybean.doPost(request,response); %>
