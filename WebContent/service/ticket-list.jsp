<%-- 
    Document : ticket-add
    Created on: Feb 11, 2013
    Author   : Gurumurthy TS
--%>
<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Ticket_List"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<style >
@media screen and (min-width: 300px) and (max-width: 780px) {
#brand{
	margin-left:-52%;
	color: red;
}
#region{
	margin-left:-50%;
}
#branch{
	margin-left:-48%;
}
}
</style>
	

</HEAD>
<body
	<%if (mybean.advSearch.equals(null) || mybean.advSearch.equals("")) {%>
	onLoad="LoadRows();FormFocus();" <%}%>
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT --->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>List Tickets</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<%@include file="../Library/list-body-adv.jsp"%>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script src="../Library/smart.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript">
	function PopulateRegion() { //v1.0
// 		alert("aaaaaa");
		var brand_id = outputSelected(document.getElementById("dr_brand").options);
		showHint('../accessories/mis-check.jsp?multiplecheckregion=yes&brand_id=' + brand_id, 'regionHint');
	}

	function PopulateBranches() { //v1.0
// 		alert("bbbbb");
		var brand_id = outputSelected(document.getElementById("dr_brand").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		//alert("222------------"+region_id);
		showHint('../accessories/mis-check.jsp?multiplecheckbranch=yes&brand_id='+ brand_id + '&region_id=' + region_id,
				'branchHint');
	}
	function PopulateExecutives(){
		
	}
</script>
		<script type="text/javascript">
		function PopulateDays() {
			var brand_id=$("#dr_brand").val();
			var tickettype_id=$("#dr_tickettype_id").val();
			showHint('../service/mis-check1.jsp?ticketdays=yes&tickettype_id='+tickettype_id+'&ticket_brand_id='+brand_id, 'crmdaysHint');
		    }

		</script>	
</body>
</HTML>
