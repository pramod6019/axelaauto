<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Item_Details" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>