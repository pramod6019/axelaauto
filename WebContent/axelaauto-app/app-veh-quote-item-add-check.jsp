<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Veh_Quote_Item_Add_Check" scope="request"/>
<%mybean.doPost(request,response); %>
<%=mybean.StrHTML%>