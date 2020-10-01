<%@ page errorPage="error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Report_VoucherNo"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</head>
<body onLoad="FormFocus();"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Missing Voucher No</h1>
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
							<li><a href="../accounting/index.jsp">Accounting</a> &gt;</li>
							<li><a href="report-missing-voucherno.jsp">Missing
									VoucherNo</a><b>:</b></li>
						</ul>
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>
						<div class="tab-pane" id="">
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Missing
										Voucher No</div>
								</div>
								<div class="container-fluid portlet-body portlet-empty">
									<div class="tab-pane" id="">

										<form name="form1" method="post" class="form-horizontal ">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.
												</font>
											</center>
											<!-- START PORTLET BODY -->

											<div class="form-element2">
												<label>From No.<font color="#ff0000">*</font>:
												</label> <input name="txt_from_no" id="txt_from_no" type="text"
													class="form-control" value="<%=mybean.from_no%>" size="12"
													maxlength="10" onkeyup="toInteger('txt_from_no');" />
											</div>

											<div class="form-element2">
												<label>To No.<font color="#ff0000">*</font>:
												</label> <input name="txt_to_no" id="txt_to_no" type="text"
													class="form-control" value="<%=mybean.to_no%>" size="12"
													maxlength="10" onkeyup="toInteger('txt_to_no');" />
											</div>
											<div class="form-element2">
												<label>Branch.<font color="#ff0000">*</font>:
												</label> <select name="voucher_branch_id" class="form-control"
													id="voucher_branch_id">
													<%=mybean.PopulateBranch()%>
												</select>
											</div>
											<div class="form-element2">
												<label>Voucher Type.<font color="#ff0000">*</font>:
												</label> <select name="dr_voucher_type" class="form-control"
													id="dr_voucher_type">
													<%=mybean.PopulateVoucherType()%>
												</select>
											</div>
											<div class="form-element2" style="margin-top: 11px">
												<center>
													<input name="submit_button" type="submit"
														class="btn btn-success pull-left" id="submit_button"
														value="Go" /> <input type="hidden" name="submit_button"
														value="Submit">
												</center>
											</div>
											<div class="form-element2" style="margin-top: 11px">
												<% if (!mybean.StrHTML.equals("") && mybean.ExportPerm.equals("1")) { %>
												<input name="export_button" type="submit"
													class="btn btn-success pull-right" id="export_button"
													value="Export" />
												<%
													}
												%>
											</div>
										</form>
									</div>
								</div>
							</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<%=mybean.StrHTML%>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
</body>
</HTML>
