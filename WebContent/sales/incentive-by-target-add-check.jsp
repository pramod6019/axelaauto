<%@ page errorPage="../portal/error-page.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.sales.Incentive_By_Target_Add" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>