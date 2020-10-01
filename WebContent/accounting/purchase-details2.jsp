<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.Purchase_Details2" scope="request"/>   
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>