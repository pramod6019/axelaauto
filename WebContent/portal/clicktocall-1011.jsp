<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.portal.ClickToCall_1011" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>