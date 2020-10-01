<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Report_Pricebook_Export" scope="request"/>
<jsp:useBean id="export" class="axela.inventory.Inventory_Report_Pricebook_Export" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

	<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
	<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
 	<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
	<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
	<%@include file="../portal/header.jsp"%>

	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../inventory/index.jsp">Inventory</a> &gt;</li>
						<li><a href="inventory-report-pricebook.jsp">Price Book</a> &gt;</li>
						<li><a href="inventory-report-pricebook-export.jsp">Export</a><b>:</b></li>
					</ul>
					<div class="portlet box ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Price Book Export</div>
						</div>
						<div class="portlet-body portlet-empty">
							<div class="tab-pane" id="">
								<div align="center" valign="top" height="300"><%@ include
										file="../Library/export.jsp"%></div>
							</div>
						</div>
					</div>

					<div align="center">
						<td align="center" valign="middle">&nbsp;</td>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
</body>
</html>
