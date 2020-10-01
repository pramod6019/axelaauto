<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Item_Price_Update" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>