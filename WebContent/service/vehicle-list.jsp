<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Vehicle_List"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>

</HEAD>
<body
	<%if (mybean.advSearch.equals(null) || mybean.advSearch.equals("")) {%>
	onLoad="LoadRows();" <%}%>
	class="page-container-bg-solid page-header-menu-fixed">
	<%
		if (mybean.group.equals("")) {
	%>
	<%@include file="../portal/header.jsp"%>
	<%
		} else {
	%>
	<div class="modal-header">
		<button class=" lg close" data-dismiss="modal"></button>
	</div>
	<%
		}
	%>

	<!-- 	BODY -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>List Vehicles</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<%@include file="../Library/list-body-adv.jsp"%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- END CONTAINER -->
	<%
		if (mybean.group.equals("")) {
	%>
	<%@include file="../Library/admin-footer.jsp"%>
	<%
		}
	%>
	<%@include file="../Library/js.jsp"%>
	<script src="../Library/smart.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript">
		function PopulateModel() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_brand").options);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id='
					+ brand_id + '&model=yes', 'modelHint');
		}
	</script>
</body>
</HTML>
