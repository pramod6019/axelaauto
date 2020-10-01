<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Email_Quote"
	scope="request" />
<%
	mybean.doPost(request, response);
%>

