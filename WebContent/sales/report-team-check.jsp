<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Team_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>