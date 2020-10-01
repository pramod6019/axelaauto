<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.service.JobCard_Dash_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>