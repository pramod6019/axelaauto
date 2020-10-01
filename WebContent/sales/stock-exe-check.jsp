<%--
    Document : executives-check
    Created on: June 20, 2013, 12:07:40 PM
    Author   : Ajit
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Stock_Exe_Check" scope="request"/>
<%mybean.doPost(request, response);%>
<%=mybean.StrHTML%> 