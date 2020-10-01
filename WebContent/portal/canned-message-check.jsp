<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.portal.Canned" scope="request"/>
<%mybean.doPost(request,response); %>
<font color=red><%=mybean.StrHTML%></font>
