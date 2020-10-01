<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.jobs.Jobs_Ticket_Escalation" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>