<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Update_Kms_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<font color=red><%=mybean.StrHTML%></font>
