<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.portal.Ecover_SignIn" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
