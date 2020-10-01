<jsp:useBean id="mybean" class="axela.service.Ticket_Faq_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>