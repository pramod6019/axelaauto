<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Leave_List"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<%response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin")); %>
<%response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS"); %>
<%//response.setHeader("Access-Control-Allow-Headers", "GAuthorization, X-Requested-With, Content-Type, Origin, Accept"); %>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>


<style>
#listcheck {
	display: inline-block;
}
</style>
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body <%if(mybean.advSearch.equals(null) || mybean.advSearch.equals("")){%>
	onLoad="LoadRows();FormFocus();" <%} else {%>
	onLoad="" <%}%>
	class="page-container-bg-solid page-header-menu-fixed">
	<%//if(!mybean.pop.equals("yes")){%>
	<%@include file="../portal/header.jsp"%>
	<%//}%>
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
						<h1>List Leave</h1>
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
							<!-- BODY START -->
							<%@include file="../Library/list-body-adv.jsp"%>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<script src="../Library/smart.js" type="text/javascript"></script>
	<%@include file="../Library/js.jsp"%>


</body>
</HTML>
