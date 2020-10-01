<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Executive_Docs_List"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
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
						<h1>List Documents</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="home.jsp">Home</a> &gt;</li>
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="executive-list.jsp?all=yes">List Executives</a>
								&gt;</li>
							<li><a href="executive-list.jsp?emp_id=<%=mybean.emp_id%>"><%=mybean.emp_name%></a>
								&gt;</li>
							<li><a
								href="executive-docs-list.jsp?emp_id=<%=mybean.emp_id%>">List Documents</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->

						<div style="float: right">
							<a href="docs-update.jsp?add=yes&exe_id=<%=mybean.emp_id%>">Add
								New Document...</a>
						</div>
						<br></br>
						<center>
							<font color="#FF0000"><b><%=mybean.msg%></b></font>
						</center>
						<center>
							<strong><%=mybean.RecCountDisplay%></strong>
						</center>
						<center><%=mybean.PageNaviStr%></center>
							</div>
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<%=mybean.StrHTML%>
								</div>
						<center><%=mybean.PageNaviStr%></center>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>

</body>
</HTML>
