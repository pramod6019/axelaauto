<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Canned_Email_List" scope="request" />
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
<style>
@media ( max-width : 1024px) {
	#tblsmartsearch td {
		display: block;
	}
}
</style>
	
</HEAD>
<body <%if (mybean.advSearch.equals(null) || mybean.advSearch.equals("")) {%>
	onLoad="LoadRows();FormFocus();" <%}%>
	class="page-container-bg-solid page-header-menu-fixed">

	<%@include file="header.jsp"%>
		<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>List Canned Email</h1>
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
					<div class="container-fluid">
<!-- 					BODY START -->
					
						  <%@include file="../Library/list-body.jsp" %>

					</div>
				</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	<script src="../Library/smart.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		//document.form1.dr_branch_id.focus()
	}
</script>
</body>
</HTML>
