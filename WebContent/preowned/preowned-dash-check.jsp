<%-- 
    Document : enquiry-dash-check
    Created on: Sep 17, 2012, 2:20:25 PM
    Author   : Ajit
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Dash_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
