<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Veh_Salesorder_Details" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>