<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.Invoice_Details" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%> 