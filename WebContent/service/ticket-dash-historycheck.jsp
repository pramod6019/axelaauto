<jsp:useBean id="mybean" class="axela.service.Ticket_Dash_HistoryCheck" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>