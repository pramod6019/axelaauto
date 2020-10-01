<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Index" scope="request" />
<% mybean.doPost(request,response); %>  
<%=mybean.StrHTML%>