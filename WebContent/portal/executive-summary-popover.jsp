<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.portal.Executive_Summary_Popover" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
