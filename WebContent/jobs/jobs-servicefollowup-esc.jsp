<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.jobs.Jobs_ServiceFollowup_Esc" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>

