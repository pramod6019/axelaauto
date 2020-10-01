<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Sales_Ecover_Policy_Add" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>