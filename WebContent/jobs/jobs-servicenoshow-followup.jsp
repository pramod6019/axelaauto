
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.jobs.Jobs_ServiceNoShow_Followup" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>

