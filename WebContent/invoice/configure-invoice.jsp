<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.invoice.Configure_Invoice" scope="request"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
        <HEAD>
        <title><%=mybean.AppName%></title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
       <link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link rel="shortcut icon" href="../test/favicon.ico" />
        <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
        <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
        <body  onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Configure Invoice</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/manager.jsp#Invoice">Business Manager</a> &gt;</li>
						<li><a href="../portal/manager.jsp#Invoice">Invoice</a> &gt; </li>
						<li><a href="../invoice/configure-invoice.jsp">Configure Invoice</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
						<center><font color="#ff0000" ><b><%=mybean.msg%></b></font></center>
							<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
					Configure Invoice
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
						<form name="form1"  method="post" class="form-horizontal">
		<center>
<label class="control-label"> Reduce Current Stock for Invoice: </label>
	<input type="checkbox" name="chk_config_invoice_reduce_current_stock" id="chk_config_invoice_reduce_current_stock" <%=mybean.PopulateCheck(mybean.config_invoice_reduce_current_stock)%> />
</center><br>
<center><input name="update_button" type="submit" class="btn btn-success" id="update_button" value="Update" /></center>

			</form>
					</div>
				</div>
			</div>
					</div>
				</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
      
        <%@include file="../Library/admin-footer.jsp" %>
        <script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
        </body>
</HTML>
