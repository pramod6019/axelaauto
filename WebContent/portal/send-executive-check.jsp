<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.portal.Send_Executive_SMS" scope="request"/>
<%mybean.doPost(request,response); %>
<font color=red><%=mybean.StrHTML%></font>
