<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Configure_Inventory"
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

<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body onLoad="FormFocus()" 
class="page-container-bg-solid page-header-menu-fixed">
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
					<div class="page-title">
						<h1>Configure Inventory</h1>
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
						<li><a href="../portal/manager.jsp#Inventory">Business
								Manager</a> &gt;</li>
						<li><a href="../portal/manager.jsp#inventory">Inventory</a>
							&gt;</li>
						<li><a href="../inventory/configure-inventory.jsp">Configure
								Inventory</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Configure
										Inventory</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.<br>
											</font><br>
										</center>
										<form name="form1" method="post" class="form-horizontal">
											<div class="form-group">
												<label class="control-label col-md-6"> Enable Leads:
												</label>
												<div class="txt-align">
													<input type="checkbox" name="chk_config_sales_leads"
														<%=mybean.PopulateCheck(mybean.config_sales_leads)%> />
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-6"> Enable
													Enquiry: </label>
												<div class="txt-align">
													<input type="checkbox" name="chk_config_sales_enquiry"
														<%=mybean.PopulateCheck(mybean.config_sales_enquiry)%> />
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-6"> Enable
													Quotes: </label>
												<div class="txt-align">
													<input type="checkbox" name="chk_config_sales_quote"
														<%=mybean.PopulateCheck(mybean.config_sales_quote)%> />
												</div>
											</div>
											<center>
												<input name="update_button" type="submit"
													class="btn btn-success" id="update_button" value="Update" />
											</center>
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
	<%@include file="../Library/admin-footer.jsp"%>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_config_email1.focus()
	}
	function check() {
		// alert("count6565");
		var count = 0;
		count = count + 1;
		alert("count" + count);

	}
</script>
	</body>
</HTML>
