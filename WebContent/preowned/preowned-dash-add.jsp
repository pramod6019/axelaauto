<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Dash_Add" scope="request"/>
<%mybean.doPost(request,response); %>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
    <h4 class="modal-title" style=text-align:center>Add Evaluation</h4>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-md-12"  style=text-align:center>
		<%=mybean.StrHTML%>
		 </div>
    </div>
</div>
<div class="modal-footer">
    <button type="button" class="btn default" data-dismiss="modal">Close</button>
</div>
