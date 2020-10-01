
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.SubGroup_Check" scope="request"/>
<%mybean.doPost(request, response);%>
<%=mybean.StrHTML%> 