
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_Vehicle_Service_Booking_Cancel_Model" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>
