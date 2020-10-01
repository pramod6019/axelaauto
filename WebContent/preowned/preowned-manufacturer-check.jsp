<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Manufacturer_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
