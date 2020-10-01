<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Target_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>