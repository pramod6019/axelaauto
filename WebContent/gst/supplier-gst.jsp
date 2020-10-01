<jsp:useBean id="mybean" class="axela.gst.Gst_Supplier_Update" scope="request" />
<%mybean.doPost(request,response); %>
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title></title>
<style>
   .mydisplay{
    text-align:center;
    vertical-align:middle;
   }
</style>
</HEAD>
 <body>
     <%if(!mybean.msg.equals("")) {%>
      <b><center style="margin-top: 14pc;"><span><%=mybean.msg %></span></center></b>
     <%}else{ %>
     <b><center style="margin-top: 14pc;"><span>Thanks For Updateing Company Information!</span></center></b>
<%} %>
 </body>
</html>