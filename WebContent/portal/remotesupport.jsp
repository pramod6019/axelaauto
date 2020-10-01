<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Remote_Support"
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

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>

<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>

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
						<h1>Remote Support</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="#">Remote Support</a>:</li>
					</ul>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<center>
								<a href="../rc.exe" target="_blank"><strong>Click
										here</strong></a> for remote support.<br> <br> Please call Anil
										Kumar on +91 99008 77557 for assistance.<br>
							</center>
							<%
								if (!mybean.StrHTML.equals("")) {
							%>
							<%
								}
							%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>




	<%@include file="../Library/admin-footer.jsp"%></body>
</html>
