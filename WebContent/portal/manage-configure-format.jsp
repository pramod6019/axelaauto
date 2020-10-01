<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Manage_Configure_Format"
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

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link rel="shortcut icon" href="../test/favicon.ico" />
	<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
        <link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />	
        
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
	
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_format.focus()
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">

	<%@include file="header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>
							Format For
							<%=mybean.title%>
						</h1>
					</div>
					<!-- END PAGE TITLE -->

				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- BEGIN PAGE BREADCRUMBS -->
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="manager.jsp">Business Manager</a> &gt;</li>
						<li><a href="manage-configure.jsp">Configure Axela</a> &gt;</li>
						<li><a href="#">Format For <%=mybean.status%></a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Format For
										<%=mybean.title%>
									</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form method="post" name="form1" class="form-horizontal">
											<%
												if (mybean.email.equals("yes")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Subject: </label>
												<div class="col-md-6 col-xs-12">
													<input name="txt_subject" type="text" class="form-control"
														id="txt_subject" value="<%=mybean.subject%>" size="75"
														maxlength="255">
												</div>
											</div>
											<%
												}
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Description<font
													color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<textarea name="txt_format" cols="70" rows="10"
														class="form-control" id="txt_format"
														<%if (mybean.sms.equals("yes")) {%>
														onKeyUp="charcount('txt_format', 'span_txt_format','<font color=red>({CHAR} characters left)</font>', '160')"
														<%}%>><%=mybean.format_desc%></textarea>
													<br />
													<%
														if (mybean.email.equals("yes")) {
													%>
													<script type="text/javascript">
														var editor = CKEDITOR
																.replace(
																		'txt_format',
																		{
																			toolbar : 'MyToolbar',
																			//customConfig: '',
																			width : '95%'
																		});
													</script>
													<%
														} else {
													%><span id="span_txt_format"> 160 characters </span>
													<%
														}
													%>

												</div>
											</div>
											<div class="form-group">
												<center>
													<input name="update_button" type="submit"
														class="btn btn-success" id="update_button" value="Update" />
												</center>
											</div>
										</form>
									</div>
								</div>
							</div>

							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Substitution
										Variables</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<table class="table table-bordered">
											<tr>
												<td align="center">Student Name</td>
												<td align="center">[NAME]</td>
											</tr>
											<%
												if (mybean.status.equals("Enquiry Registration")) {
											%>
											<tr>
												<td align="center">Enquiry ID</td>
												<td align="center">[ENQUIRYID]</td>
											</tr>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Student Registration")) {
											%>
											<tr>
												<td align="center">Student No.</td>
												<td align="center">[STUDENTNO]</td>
											</tr>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Invoice Confirmation")) {
											%>
											<tr>
												<td align="center">Student No.</td>
												<td align="center">[STUDENTNO]</td>
											</tr>
											<tr>
												<td align="center">Invoice No.</td>
												<td align="center">[INVOICENO]</td>
											</tr>
											<tr>
												<td align="center">Invoice Date</td>
												<td align="center">[INVOICEDATE]</td>
											</tr>
											<tr>
												<td align="center">Invoice Amount</td>
												<td align="center">[INVOICEAMOUNT]</td>
											</tr>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Receipt Confirmation")) {
											%>
											<tr>
												<td align="center">Student No.</td>
												<td align="center">[STUDENT NO]</td>
											</tr>
											<tr>
												<td align="center">Receipt No.</td>
												<td align="center">[RECEIPTNO]</td>
											</tr>
											<tr>
												<td align="center">Receipt Date</td>
												<td align="center">[RECEIPTDATE]</td>
											</tr>
											<tr>
												<td align="center">Receipt Amount</td>
												<td align="center">[RECEIPTAMOUNT]</td>
											</tr>
											<tr>
												<td align="center">Invoice No.</td>
												<td align="center">[INVOICENO]</td>
											</tr>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Balance Due")
														|| mybean.status.equals("Balance Overdue")) {
											%>
											<tr>
												<td align="center">Student No.</td>
												<td align="center">[STUDENTNO]</td>
											</tr>
											<tr>
												<td align="center">Balance Date</td>
												<td align="center">[INSTALLDATE]</td>
											</tr>
											<tr>
												<td align="center">Balance No.</td>
												<td align="center">[INSTALLNO]</td>
											</tr>
											<tr>
												<td align="center">Balance Amount</td>
												<td align="center">[INSTALLAMOUNT]</td>
											</tr>
											<tr>
												<td align="center">Invoice No.</td>
												<td align="center">[INVOICENO]</td>
											</tr>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Batch Attendance")
														|| mybean.status.equals("Batch Attendance")) {
											%>
											<tr>
												<td align="center">Student No.</td>
												<td align="center">[STUDENTNO]</td>
											</tr>
											<tr>
												<td align="center">Student ID</td>
												<td align="center">[STUDENTID]</td>
											</tr>
											<tr>
												<td align="center">Batch ID</td>
												<td align="center">[BATCHID]</td>
											</tr>
											<tr>
												<td align="center">Subject</td>
												<td align="center">[SUBJECT]</td>
											</tr>
											<tr>
												<td align="center">Subject Code</td>
												<td align="center">[SUBJECTCODE]</td>
											</tr>
											<tr>
												<td align="center">Session No.</td>
												<td align="center">[SESSIONNO]</td>
											</tr>
											<tr>
												<td align="center">Session Time</td>
												<td align="center">[SESSIONTIME]</td>
											</tr>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Batch Test Marks")
														|| mybean.status.equals("Batch Test Marks")) {
											%>
											<tr>
												<td align="center">Student No.</td>
												<td align="center">[STUDENTNO]</td>
											</tr>
											<tr>
												<td align="center">Student ID</td>
												<td align="center">[STUDENTID]</td>
											</tr>
											<tr>
												<td align="center">Batch ID</td>
												<td align="center">[BATCHID]</td>
											</tr>
											<tr>
												<td align="center">Subject</td>
												<td align="center">[SUBJECT]</td>
											</tr>
											<tr>
												<td align="center">Subject Code</td>
												<td align="center">[SUBJECTCODE]</td>
											</tr>
											<tr>
												<td align="center">Test Name</td>
												<td align="center">[TESTNAME]</td>
											</tr>
											<tr>
												<td align="center">Test Code</td>
												<td align="center">[TESTCODE]</td>
											</tr>
											<tr>
												<td align="center">Test Marks</td>
												<td align="center">[TESTMARKS]</td>
											</tr>
											<tr>
												<td align="center">Test Total</td>
												<td align="center">[TESTTOTAL]</td>
											</tr>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Class Attendance")
														|| mybean.status.equals("Class Attendance")) {
											%>
											<tr>
												<td align="center">Student No.</td>
												<td align="center">[STUDENTNO]</td>
											</tr>
											<tr>
												<td align="center">Student ID</td>
												<td align="center">[STUDENTID]</td>
											</tr>
											<tr>
												<td align="center">Class Name</td>
												<td align="center">[CLASSNAME]</td>
											</tr>
											<tr>
												<td align="center">Attendance Date</td>
												<td align="center">[ATTDATE]</td>
											</tr>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Class Test Marks")
														|| mybean.status.equals("Class Test Marks")) {
											%>
											<tr>
												<td align="center">Student No.</td>
												<td align="center">[STUDENTNO]</td>
											</tr>
											<tr>
												<td align="center">Student ID</td>
												<td align="center">[STUDENTID]</td>
											</tr>
											<tr>
												<td align="center">Class Name</td>
												<td align="center">[CLASSNAME]</td>
											</tr>
											<tr>
												<td align="center">Test Name</td>
												<td align="center">[TESTNAME]</td>
											</tr>
											<tr>
												<td align="center">Test Marks</td>
												<td align="center">[TESTMARKS]</td>
											</tr>
											<tr>
												<td align="center">Test Total</td>
												<td align="center">[TESTTOTAL]</td>
											</tr>
											<%
					}
				%>
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

	<%@include file="../Library/admin-footer.jsp"%>
	
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
   <script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
</body>
</HTML>
