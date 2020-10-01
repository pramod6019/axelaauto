<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.PSFCustomField_List" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%></title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>
</head>

<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>PSF Custom Field List</h1>
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
							<%=mybean.LinkHeader%>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font><br></br>
							</center>

							<!-- START PORTLET BODY -->
							<div style="float: right">
								<%if (!mybean.psfdays_id.equals("0")) {%>

								<a href="../service/psfcustomfield-update.jsp?add=yes&psfdays_id=<%=mybean.psfdays_id %>">Add Custom Field...</a>

								<%}%>
							</div>
							<br />
							<div>
								<%=mybean.StrCustomField%>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

<%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp" %>
</body> 
</html>
