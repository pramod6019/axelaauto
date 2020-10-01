<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.jobs.Test_Url" scope="request"/>
<%mybean.doPost(request,response); %>