<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.ManageLocation" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<%response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin")); %>
<%response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS"); %>
<%//response.setHeader("Access-Control-Allow-Headers", "GAuthorization, X-Requested-With, Content-Type, Origin, Accept"); %>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
	<%@include file="../Library/css.jsp"%>
</head>
<body   <%if(mybean.advSearch.equals(null)  || mybean.advSearch.equals("")){%>onLoad="LoadRows();FormFocus();" <%}%>leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>


<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>List Service Locations</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
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

 <%@include file="../Library/admin-footer.jsp" %>
 	<%@include file="../Library/js.jsp"%>
 	<script src="../Library/smart.js" type="text/javascript"></script>
</body>
</html>
