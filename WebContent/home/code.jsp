<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.home.Code" scope="request"/>
<%mybean.doPost(request,response); %>
<%response.setHeader("Access-Control-Allow-Origin","*"); %>
<%response.setHeader("Access-Control-Allow-Credentials","true"); %>
<%=mybean.StrHTML%>