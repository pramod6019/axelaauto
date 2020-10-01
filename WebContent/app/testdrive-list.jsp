<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.app.TestDrive_List" scope="request"/>
<%mybean.doPost(request, response);%>
<!DOCTYPE html>
<html>
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.ClientName%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ include file="../Library/css.jsp" %>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed"
 <%if(mybean.advSearch.equals(null)  || mybean.advSearch.equals("")){%>onLoad="LoadRows();FormFocus();" <%}%>>
<%@include file="../portal/header.jsp" %>

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
						<h1> List Test Drives</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>

				<!-- END PAGE HEAD-->
				<!-- BEGIN PAGE CONTENT BODY -->
				<div class="page-content">
					<%@include file="../Library/list-body.jsp"%>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->

<%@ include file="../Library/admin-footer.jsp" %>
<%@ include file="../Library/js.jsp" %>
<script src="../Library/smart.js" type="text/javascript"></script>
</body>
</HTML>
