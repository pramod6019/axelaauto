<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Booking_Item_Details" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>