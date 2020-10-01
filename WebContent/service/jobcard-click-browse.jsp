<%@page import="org.apache.jasper.tagplugins.jstl.core.Import"%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Click_Browse" scope="request"/>
<%mybean.doPost(request, response);%>
<%=mybean.StrHTML%>