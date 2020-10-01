<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Vehicle_Service_Booking_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
