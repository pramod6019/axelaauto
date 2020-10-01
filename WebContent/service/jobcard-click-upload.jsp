<%@page import="org.apache.jasper.tagplugins.jstl.core.Import"%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Jobcard_Click_Upload" scope="request"/>
<%mybean.doPost(request, response);%>
