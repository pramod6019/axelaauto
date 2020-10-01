<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Export" scope="request" />
<jsp:useBean id="export" class="axela.preowned.Preowned_Export" scope="request" />
<%export.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<!-- 					<div class="page-title"> -->
					<!-- 						<h1>E</h1> -->
					<!-- 					</div>  -->
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../preowned/index.jsp">Pre-Owned</a> &gt;</li>
							<li><a href="preowned.jsp">Pre-Owned</a> &gt;</li>
							<li><a href="../preowned/preowned-export.jsp">Export</a><b>:</b></li>
						</ul>
					<div class="portlet box ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Pre-Owned Export</div>
						</div>
						<div class="container-fluid portlet-body portlet-empty">
							<div class="tab-pane" id="">
								<div valign="top" height="300">
								<%@ include file="../Library/export.jsp"%>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
</body>
</html>
