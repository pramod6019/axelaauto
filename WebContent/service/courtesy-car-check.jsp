<%-- 
    Document : Courtesy-car-check
    Created on: April 4, 2013, 12:07:40 PM
    Author   : Saiman
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Courtesy_Car_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>