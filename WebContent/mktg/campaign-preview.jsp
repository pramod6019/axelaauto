<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.Campaign_Preview" scope="request"/>
<%mybean.doGet(request,response); %>
   <%=mybean.campaign_msg%>
