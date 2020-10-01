<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Campaign_Dash_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>