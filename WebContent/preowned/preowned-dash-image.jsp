<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Dash_Image"
	scope="request" />
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../assets/css/footable.core.css">

<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-timepicker.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />

<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/slider.css">

<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<%@ include file="../Library/preowned-dash.jsp"%>

<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
		<div class="page-content">
				<div class="container-fluid">
	<div class="portlet box  ">
		<div class="portlet-title" style="text-align: center">
			<div class="caption" style="float: none">Image</div>
		</div>
		<div class="portlet-body portlet-empty">
			<div class="tab-pane" id="">
				<!-- START PORTLET BODY -->
				<div style="float: right">
					<a
						href="preowned-click-image.jsp?add=yes&amp;preowned_id=<%=mybean.preowned_id%>"
						target="_blank">Click Image...</a>&nbsp;&nbsp;<a
						href="preowned-dash-image-update.jsp?add=yes&amp;preowned_id=<%=mybean.preowned_id%>">Add
						New Image...</a>
				</div>
				<div class="container-fluid">
					<div class="col-md-12 col-xs-12">
						<%=mybean.StrHTML%>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	</div>
	</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>

	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap-timepicker.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datetimepicker.js"
		type="text/javascript"></script>

	<script type="text/javascript" src="../Library/dynacheck.js"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-select2.min.js"
		type="text/javascript"></script>
	<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>


	<script type="text/javascript" src="../Library/dynacheck-post.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck-post.js?target=<%=mybean.jsver%>"></script>






	<script>
$(document).ready(function(){ 
// 	alert("library");
	$(".page-content").css({'min-height' : '20px'})
});

</script>
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>
</body>
</HTML>
