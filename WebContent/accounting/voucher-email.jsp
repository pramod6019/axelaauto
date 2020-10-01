<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Voucher_Email"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%></title>
<meta http-equiv="pragma" content="no-cache">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="../Library/css.jsp" %>
</HEAD>
<body onload=FormFocus() leftmargin="0" rightmargin="0" topmargin="0"
	bottommargin="0">
	<%@include file="../portal/header.jsp"%>
	<!-- 	BODY -->
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
						<h1>Email Receipt</h1>
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
						<li><a href="index.jsp">Accounting</a> &gt;</li>
						<li><a href="voucher-list.jsp?all=yes">List Voucher</a> &gt;
						</li>
						<li><a
							href="voucher-list.jsp?voucher_id=<%=mybean.voucher_id%>"><%=mybean.vouchertype_name%>:
								<%=mybean.voucher_id%></a> &gt;</li>
						<li><a
							href="voucher-email.jsp?voucher_id=<%=mybean.voucher_id%>">Email
								<%=mybean.vouchertype_name%></a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Email
										<%=mybean.vouchertype_name%>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
											<div class="form-element6 form-element-center">
													<label>Voucher:</label>
														<a href="voucher-list.jsp?voucher_id=<%=mybean.voucher_id%>"><%=mybean.vouchertype_name%></a>
											</div>
											<div class="form-element6 form-element-center">
													<label>Voucher ID:</label>
														<a href="voucher-list.jsp?voucher_id=<%=mybean.voucher_id%>"><%=mybean.voucher_id%> </a>
													</div>
											<div class="form-element6 form-element-center">
													<label>Voucher No:</label>
														<%=mybean.voucher_no%>
											</div>
											<%
												if (!mybean.customer_name.equals("")) {
											%>
											<div class="form-element6 form-element-center">
													<label>Ledger:</label>
														<%=mybean.customer_name%>
											</div>
											<%
												}
											%>
											<div class="form-element6 form-element-center">
													<label>Contact:</label>
														<%=mybean.contact_name%>
											</div>
											<div class="form-element6 form-element-center">
													<label>To:</label>
														<%=mybean.contact_email1%>
											</div>
											<div class="form-element6 form-element-center">
													<label>CC:</label>
														<input type="text" id="txt_cc" value="<%=mybean.cc%>"
															class="form-control" name="txt_cc" size="70" />
											</div>
											<div class="form-element6 form-element-center">
													<label>BCC:</label>
														<input name="txt_bcc" type="text" class="form-control"
															id="txt_bcc" value="<%=mybean.bcc%>" size="70" />
											</div>
											<div class="row"></div>
											<center>
												<input type="submit" class="btn btn-success"
													name="sendemail" id="sendemail" value="Email Voucher" /> <input
													type="hidden" id="sendB" name="sendB" value="yes"></input>
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
	<%@include file="../Library/js.jsp" %>
</body>
</HTML>