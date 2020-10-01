<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.jobs.Jobs_Pre_Owned_Evaluation" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>