<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Vehicle_Dash_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%mybean.SOP("coming.."); %>
<%=mybean.StrHTML%>