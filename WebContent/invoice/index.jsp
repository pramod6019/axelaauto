<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.invoice.Index" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
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
					<div class="page-title">
						<h1>Invoice</h1>
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
						<li><a href="index.jsp">Invoice</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<div class="containe-fluid">
								<div class="col-md-4 col-sm-6">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">&nbsp;</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<table class="table table-bordered">
													<tr>
														<th valign="top">Top Quotes</th>
														<th valign="top">Amount</th>
													</tr>
													<%=mybean.TopQuotes()%>
												</table>

											</div>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">&nbsp;</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<table class="table table-bordered">
													<tr>
														<th valign="top">Top Invoices</th>
														<th valign="top">Amount</th>
													</tr>
													<%=mybean.TopInvoices()%>
												</table>

											</div>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">&nbsp;</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<table class="table table-bordered">
													<tr>
														<th valign="top">Customer Reports</th>
													</tr>
													<tr>
														<td height="200" valign="top"><%=mybean.ListReports()%></td>
													</tr>
												</table>

											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="container-fluid">
								<div class="col-md-4 col-sm-6">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">&nbsp;</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<table class="table table-bordered">
													<tr>
														<th valign="top">Top Receipts</th>
														<th valign="top">Amount</th>
													</tr>
													<%=mybean.TopReceipts()%>
												</table>

											</div>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">&nbsp;</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<table class="table table-bordered">
													<tr>
														<th valign="top">Top Payments</th>
														<th valign="top">Amount</th>
													</tr>
													<%=mybean.TopPayments()%>
												</table>

											</div>
										</div>
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
</body>
</html>
