
<%-- 
    Document : executives-check
    Created on: 4 april, 2013
    Author   : Smitha
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Vehicle_Dash_Insurance_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>