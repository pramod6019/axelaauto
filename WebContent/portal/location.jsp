<%--
    Document : location
    Created on: Sep 17, 2012, 11:09:37 AM
    Author   : Ajit
--%>

<jsp:useBean id="mybean" class="axela.portal.Location" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
