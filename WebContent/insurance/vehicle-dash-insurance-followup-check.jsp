<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Vehicle_Dash_Insurance_Followup_Check" scope="request"/>  
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>