<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Configure_Accounting"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet"
	type="text/css" />
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
						<h1>Configure Accounting</h1>
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
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/manager.jsp#Inventory">Business
								Manager</a> &gt;</li>
						<li><a href="../portal/manager.jsp#accounting">Accounting</a>
							&gt;</li>
						<li><a href="../accounting/configure-accounting.jsp">Configure
								Accounting</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<!--	BODY START	 -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Configure
										Accounting</div>
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
											<div class="form-element6 form-element-center">
												<label >
													Invoice Terms & Conditions: </label>
													<textarea name="txt_invoice_terms" cols="70" rows="4"
														class="form-control summernote_1" id="txt_invoice_terms"><%=mybean.config_invoice_terms%></textarea>
													<script type="text/javascript">
														CKEDITOR
																.replace(
																		'txt_invoice_terms',
																		{
																			uiColor : hexc($(
																					"a:link")
																					.css(
																							"color")),

																		});
													</script>
											</div>
											<div class="form-element6 form-element-center">
												<label> Receipt Terms & Conditions: </label>
													<textarea name="txt_receipt_terms" cols="70" rows="4"
														class="form-control summernote_1" id="txt_receipt_terms"><%=mybean.config_receipt_terms%></textarea>
													<script type="text/javascript">
														CKEDITOR
																.replace(
																		'txt_invoice_terms',
																		{
																			uiColor : hexc($(
																					"a:link")
																					.css(
																							"color")),

																		});
													</script>
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>	
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js"></script>
	</body>
</HTML>
