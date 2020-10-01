<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.portal.HelpDesk" scope="request"/>
<%mybean.doPost(request,response); %>

