<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.MIS_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>