<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.Bill_Sundry" scope="request" />  
<% mybean.doPost(request,response); %>
<%=mybean.StrHTML%>