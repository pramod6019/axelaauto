<%-- 
    Document : model-check
    Created on: Nov 25, 2015, 4:00:48 PM
    Author   : Annappa
--%>
<%-- <%@ page errorPage="../portal/error-page.jsp" %> --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="mybean" class="axela.ddmotors_app.Model_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
