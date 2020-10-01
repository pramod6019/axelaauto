<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.portal.City_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
