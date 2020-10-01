<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.MIS_Check1" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>