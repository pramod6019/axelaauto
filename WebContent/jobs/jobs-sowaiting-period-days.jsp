<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.jobs.Jobs_Sowaiting_Period_Days" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>

