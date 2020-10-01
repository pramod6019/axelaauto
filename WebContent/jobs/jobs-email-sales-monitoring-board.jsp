<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.jobs.Jobs_Email_Sales_Monitoring_Board" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>

