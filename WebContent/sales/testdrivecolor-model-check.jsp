<%-- 
    Document : executives-check
    Created on: Sep 17, 2012, 12:07:40 PM
    Author   : Ajit
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.TestDriveColor_Model_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>