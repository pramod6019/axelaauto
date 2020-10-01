<%--
    Document : executives-check
    Created on: June 20, 2013, 12:07:40 PM
    Author   : Ajit
--%>

<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Exestock_Check" scope="request"/>
<%mybean.doPost(request, response);%>
<%=mybean.StrHTML%> 