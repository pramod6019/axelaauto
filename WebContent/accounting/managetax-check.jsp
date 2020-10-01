<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.accounting.ManageTax_Update" scope="request"/>
<% mybean.doPost(request,response); %>  
<%=mybean.StrHTML%>