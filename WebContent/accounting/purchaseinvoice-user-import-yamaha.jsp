<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.PurchaseInvoice_User_Import_Yamaha"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.frmdoc.dr_branch.focus();
	}
	function PopulateLocation() {
		var branch_id = document.getElementById("dr_branch").value;
		showHint('../accounting/accounting-branch-check.jsp?sales_branch_id='
				+ branch_id + '&branch_location=yes', 'span_location');
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed"
	onload="FormFocus()">
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
						<h1>Import Yamaha Purchase Invoices</h1>
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
						<li><a href="../accounting/index.jsp">Accessories</a> &gt;</li>
						<li><a href="purchaseinvoice-user-import-yamaha.jsp?branchtype=<%=mybean.branchtype%>">Import Yamaha Purchase Invoices</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div>
						<center>
							<font color="#FF0000"><b><%=mybean.msg%></b></font>
						</center>
					</div>
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form class="form-horizontal" name="frmdoc" id="frmdoc"
								enctype="MULTIPART/FORM-DATA" method="post"
								onsubmit="aletdisp();">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Download the Yamaha Purchase Invoices template file</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												Start importing Purchase Invoices by downloading template file. <br />
												Download the template and enter Variant Price data as per
												the headings. <br /> Headings marked in red are manadatory.
												Don't change the header columns.
											</center>
											<center>
												<a href="../Library/template/purchaseinvoice-import-yamaha.xlsx"
													target="_blank"><b>Click here to download Yamaha Purchase Invoices
														template</b></a>
											</center>
											<br>
										</div>
									</div>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Import Yamaha Purchase Invoices</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-group">
												<label class="control-label col-md-4"
													style="margin-top: 10px;">Purchase Rate Class<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<select name="dr_rateclass" id="dr_rateclass"
														class="form-control" style="margin-top: 9px;">
														<%=mybean.PopulatePurchaseRateclass(mybean.comp_id)%>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"
													style="margin-top: 10px;">Sales Rate Class<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<select name="dr_rateclass" id="dr_rateclass"
														class="form-control" style="margin-top: 9px;">
														<%=mybean.PopulateSalesRateclass(mybean.comp_id)%>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2">
													Branch<font color="#ff0000">*</font>:&nbsp;
												</label>
												<div class="col-md-6 col-xs-12 col-sm-10">
													<select class="form-control" name="dr_branch"
														id="dr_branch" onchange="PopulateLocation()">
														<%=mybean.PopulateBranch(mybean.branch_id, "", mybean.branchtype, "102", request)%>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"
													style="margin-top: 10px;"> Location<font color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" style="top: 8px" id="emprows">
													<span id="span_location" name="span_location"> 
														<%=mybean.PopulateLocation(mybean.comp_id)%>
													</span>
												</div>
											</div>


											<div class="form-group">
												<label class="control-label col-md-4" style="margin-top: 10px;">
													Select Document<font color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" style="top: 0px"  id="emprows">
													<input name="filename" type="file" class="button btn btn-success" id="filename"
														style="margin-left: 1px;" value="<%=mybean.doc_value%>" size="30">
												</div>
											</div>

											<div align="center">
												<font size="">Click the Browse button to select the
													document from your computer!</font>
											</div>
											<div colspan="2" align="center">
												Allowed Formats: <b><%=mybean.importdocformat%></b>
											</div>
											<div colspan="2" align="center">
												Maximum Size: <b><%=mybean.docsize%> Mb</b>
											</div>
											<div align="center">
												<input name="addbutton" type="submit" class="button btn btn-success" id="addbutton" value="Upload" />
												<input name="add_button1" type="hidden" class="button" id="add_button" value="Upload" />
												<input name="add_button2" type="hidden" class="button" id="add_button3" value="Upload" /> 
												<input name="add_button4" type="hidden" class="button" id="add_button4" value="Upload" />
												<input name="add_button5" type="hidden" class="button" id="add_button5" value="Upload" />
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
</body>
</html>
