<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_Enquiry_Dash_Check" scope="request"/>  
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>