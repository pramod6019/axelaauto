<jsp:useBean id="mybean" class="axela.inventory.Report_Stock_Summary" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>