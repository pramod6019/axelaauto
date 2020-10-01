<%@ page errorPage="../portal/error-page.jsp"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.customer.Customer_Contact_List_Modal" scope="request"/>
<%mybean.doPost(request,response); %>
<!-- <div class="modal-header"> -->
<!--     <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button> -->
<!--     <h4 class="modal-title" style=text-align:center>Add Pre-Owned</h4> -->
<!-- </div> -->
<div class="modal-body">

	<div class="modal-header">  
	<button class=" lg close" data-dismiss="modal"></button>
	</div>
	
    <div class="row">
        <div class="col-md-12"  style=text-align:center>
		<%=mybean.StrHTML%>
		 </div>
    </div>
</div>







