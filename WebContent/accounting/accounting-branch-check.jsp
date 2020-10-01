<%--
    Document : invoiceorder-check
    Created on: Sep 17, 2012, 4:59:30 PM
    Author   : Ajit
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.accounting.Accounting_Branch_Check" scope="request"/> 
<%mybean.doPost(request,response); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html> 
<body>
<%=mybean.StrHTML%>
</body>
</html>
