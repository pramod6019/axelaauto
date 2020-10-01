<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Group_Update" scope="request" />
<% mybean.doGet(request, response); %>
<%=mybean.msg%>