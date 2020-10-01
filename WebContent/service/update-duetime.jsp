<%--
    Document : contact-check
    Created on: March 11, 2013, 3:39:28 PM
    Author   : Ajit
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="mybean" class="axela.service.Update_DueTime" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
