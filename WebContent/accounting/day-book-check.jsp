<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.accounting.Report_Day_Book" scope="request" />
<% mybean.doPost(request,response); %>  
<%=mybean.StrHTML%>