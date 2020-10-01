<jsp:useBean id="mybean" class="axela.inventory.Report_Stock_Gross_Margin" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>

