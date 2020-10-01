<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.portal.FAQ" scope="request"/>
<jsp:useBean id="export" class="axela.portal.Email_Export" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
<%@include file="header.jsp" %>
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
						<h1>FAQ</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="faq.jsp">Faq</a><b>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
					<div class="tab-pane" id="">
						 <%@include file="../Library/landing.jsp" %> 
					</div>
				</div>
				</div>
			</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
<%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp" %>
<!-- 	<script type="text/javascript" src="../Library/smart.js"></script> -->
</body>
</html>
