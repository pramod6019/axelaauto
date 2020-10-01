<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.jobs.Jobs_CrmFollowup" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>

