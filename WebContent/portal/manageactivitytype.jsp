<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.ManageActivityType"
	scope="request" />
<%mybean.doPost(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<%@include file="../Library/css.jsp"%>
</head>
<body
	<%if (mybean.advSearch.equals(null) || mybean.advSearch.equals("")) {%>
	onLoad="LoadRows();FormFocus();" <%}%>
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
	
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1> List Activity Type</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<div class="page-content">
				<div class="page-content-inner">
			
					<%@include file="../Library/list-body.jsp"%>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script src="../Library/smart.js" type="text/javascript"></script>
	</body>
</html>
