<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.MIS_Check1" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>