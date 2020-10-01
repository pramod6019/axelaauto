<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inbound.MIS_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>