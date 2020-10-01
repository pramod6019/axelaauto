<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.ManagePSFDays_Format"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />

<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


<!-- <script type="text/javascript" -->
<%-- 	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script> --%>
<!-- <script type="text/javascript" src="../Library/jquery.js"></script> -->
<!-- <script type="text/javascript" src="../Library/jquery-ui.js"></script> -->

<!-- <script type="text/javascript" -->
<%-- 	src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script> --%>



<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_format.focus();
	}
</script>
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
						<h1>PSF Days Format</h1>
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
						<li><a href="psfdays.jsp?all=yes">List PSF Days</a> &gt;</li>
						<li><a
							href="managepsfdays-format.jsp?<%=mybean.QueryString%>">
								Format For <%=mybean.status%></a> <b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Format For
										<%=mybean.title%>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<%
												if (mybean.email.equals("yes")) {
											%>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-3 control-label">Subject:</label>
													<div class="col-md-6">
														<input name="txt_subject" type="text" class="form-control"
															id="txt_subject" value="<%=mybean.subject%>" size="75"
															maxlength="1000">
													</div>
												</div>
											</div>
											<%
												}
											%>

											<div class="form-body">

												<div class="form-group">
													<label class="col-md-3 control-label"></label>
													<div class="col-md-9">

														<br>
														<%
															if (mybean.email.equals("yes")) {
														%>
														<textarea name="txt_format" cols="70" rows="5"
															class="form-control summernote_1" id="txt_format"
															<%if (mybean.sms.equals("yes")) {%>
															onKeyUp="charcount('txt_format', 'span_txt_format','<font color=red>({CHAR} characters left)</font>', '1000')"
															<%}%>><%=mybean.format_desc%></textarea>
														<script type="text/javascript">
															CKEDITOR
																	.replace(
																			'txt_format',
																			{
																				uiColor : hexc($(
																						"a:link")
																						.css(
																								"color")),
																				height : '200px'
																			});
														</script>
														<%
															} else {
														%>
														<textarea name="txt_format" cols="70" rows="5"
															class="form-control" id="txt_format"
															<%if (mybean.sms.equals("yes")) {%>
															onKeyUp="charcount('txt_format', 'span_txt_format','<font color=red>({CHAR} characters left)</font>', '1000')"
															<%}%>><%=mybean.format_desc%></textarea>
														<span id="span_txt_format"> 1000 characters </span>
														<%
															}
														%>
													</div>
												</div>
											</div>

											<center>
												<input name="update_button" type="submit"
													class="btn btn-success" id="update_button" value="Update" />
											</center>
											<br></br>

											<table class="table table-hover table-bordered">
												<thead>
													<tr>
														<th colspan="2" align="center">Substitution Variables</th>
													</tr>
												</thead>
												<%
													if (mybean.status.equals("PSF Satisfied SMS") || mybean.status.equals("PSF Dis-Satisfied SMS")
															|| mybean.status.equals("PSF Satisfied SMS For Executive") || mybean.status.equals("PSF Dis-Satisfied SMS For Executive")
															|| mybean.status.equals("PSF Contactable SMS") || mybean.status.equals("PSF Contactable SMS For Executive")
															|| mybean.status.equals("PSF Non-Contactable SMS") || mybean.status.equals("PSF Non-Contactable SMS For Executive")) {
												%>
												<tr>
													<td align="right">JC ID:</td>
													<td align="left">[JCID]</td>
												</tr>
												<tr>
													<td align="right">JC Date:</td>
													<td align="left">[JCDATE]</td>
												</tr>
												<tr>
													<td align="right">PSF Day:</td>
													<td align="left">[PSFDAY]</td>
												</tr>
												<tr>
													<td align="right">Entry Date:</td>
													<td align="left">[ENTRYDATE]</td>
												</tr>
												<tr>
													<td align="right">VOC:</td>
													<td align="left">[VOC]</td>
												</tr>
												<tr>
													<td align="right">PSF Concern:</td>
													<td align="left">[PSFCONCERN]</td>
												</tr>
												<tr>
													<td align="right">JCPSF Executive:</td>
													<td align="left">[JCPSFEXE]</td>
												</tr>
												<tr>
													<td align="right">JCPSF Executive Job Title:</td>
													<td align="left">[JCPSFEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">JCPSF Executive Mobile1:</td>
													<td align="left">[JCPSFEXEMOBILE1]</td>
												</tr>
												</tr>
												<tr>
													<td align="right">JCPSF Executive Email1:</td>
													<td align="left">[JCPSFEXEEMAIL1]</td>
												</tr>
												
												<tr>
													<td align="right">SA Executive:</td>
													<td align="left">[SAEXE]</td>
												</tr>
												<tr>
													<td align="right">SA Executive Job Title:</td>
													<td align="left">[SAEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">SA Executive Mobile1:</td>
													<td align="left">[SAEXEMOBILE1]</td>
												</tr>
												</tr>
												<tr>
													<td align="right">SA Executive Email1:</td>
													<td align="left">[SAEXEEMAIL1]</td>
												</tr>
												
												<tr>
													<td align="right">Customer ID:</td>
													<td align="left">[CUSTOMERID]</td>
												</tr>
												<tr>
													<td align="right">Customer Name:</td>
													<td align="left">[CUSTOMERNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact ID:</td>
													<td align="left">[CONTACTID]</td>
												</tr>
												<tr>
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact E-Mail1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile2:</td>
													<td align="left">[CONTACTMOBILE2]</td>
												</tr>
												<tr>
													<td align="right">Contact E-Mail2:</td>
													<td align="left">[CONTACTEMAIL2]</td>
												</tr>

												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												<tr>
													<td align="right">Branch Email:</td>
													<td align="left">[BRANCHEMAIL]</td>
												</tr>
												<tr>
													<td align="right">Branch Mobile1:</td>
													<td align="left">[BRANCHMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Branch Mobile2:</td>
													<td align="left">[BRANCHMOBILE2]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Variant:</td>
													<td align="left">[VARIANT]</td>
												</tr>
												<tr>
													<td align="right">Registration Number:</td>
													<td align="left">[REGNO]</td>
												</tr>

												<%
													}
												%>
												<%
													if (mybean.status.equals("PSF Satisfied For Customer") || mybean.status.equals("PSF Dis-Satisfied For Customer")
															|| mybean.status.equals("PSF Satisfied For Executive")
															|| mybean.status.equals("PSF Dis-Satisfied For Executive")
															|| mybean.status.equals("PSF Contactable For Customer")
															|| mybean.status.equals("PSF Non-Contactable For Customer")
															|| mybean.status.equals("PSF Contactable For Executive")
															|| mybean.status.equals("PSF Non-Contactable For Executive")) {
												%>
												<tr>
													<td align="right">JC ID:</td>
													<td align="left">[JCID]</td>
												</tr>
												<tr>
													<td align="right">JC Date:</td>
													<td align="left">[JCDATE]</td>
												</tr>
												<tr>
													<td align="right">PSF Day:</td>
													<td align="left">[PSFDAY]</td>
												</tr>
												<tr>
													<td align="right">Entry Date:</td>
													<td align="left">[ENTRYDATE]</td>
												</tr>
												<tr>
													<td align="right">VOC:</td>
													<td align="left">[VOC]</td>
												</tr>
												<tr>
													<td align="right">PSF Concern:</td>
													<td align="left">[PSFCONCERN]</td>
												</tr>
												<tr>
													<td align="right">JCPSF Executive:</td>
													<td align="left">[JCPSFEXE]</td>
												</tr>
												<tr>
													<td align="right">JCPSF Executive Job Title:</td>
													<td align="left">[JCPSFEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">JCPSF Executive Mobile1:</td>
													<td align="left">[JCPSFEXEMOBILE1]</td>
												</tr>
												</tr>
												<tr>
													<td align="right">JCPSF Executive Email1:</td>
													<td align="left">[JCPSFEXEEMAIL1]</td>
												</tr>
												
												<tr>
													<td align="right">SA Executive:</td>
													<td align="left">[SAEXE]</td>
												</tr>
												<tr>
													<td align="right">SA Executive Job Title:</td>
													<td align="left">[SAEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">SA Executive Mobile1:</td>
													<td align="left">[SAEXEMOBILE1]</td>
												</tr>
												</tr>
												<tr>
													<td align="right">SA Executive Email1:</td>
													<td align="left">[SAEXEEMAIL1]</td>
												</tr>
												
												<tr>
													<td align="right">Customer ID:</td>
													<td align="left">[CUSTOMERID]</td>
												</tr>
												<tr>
													<td align="right">Customer Name:</td>
													<td align="left">[CUSTOMERNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact ID:</td>
													<td align="left">[CONTACTID]</td>
												</tr>
												<tr>
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact E-Mail1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile2:</td>
													<td align="left">[CONTACTMOBILE2]</td>
												</tr>
												<tr>
													<td align="right">Contact E-Mail2:</td>
													<td align="left">[CONTACTEMAIL2]</td>
												</tr>

												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												<tr>
													<td align="right">Branch Email:</td>
													<td align="left">[BRANCHEMAIL]</td>
												</tr>
												<tr>
													<td align="right">Branch Mobile1:</td>
													<td align="left">[BRANCHMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Branch Mobile2:</td>
													<td align="left">[BRANCHMOBILE2]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Variant:</td>
													<td align="left">[VARIANT]</td>
												</tr>
												<tr>
													<td align="right">Registration Number:</td>
													<td align="left">[REGNO]</td>
												</tr>

												<%
													}
												%>
												<tr>
											</table>
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

	<!-- 	<script src="../assets/js/ckeditor.js" type="text/javascript"></script> -->

	<!-- 	<script type="text/javascript" src='../Library/Validate.js'></script> -->
	<!-- 	<script type="text/javascript" src='../Library/dynacheck.js'></script> -->


	<!-- <script src="../assets/js/bootstrap-datepicker.js" -->
	<!-- 	type="text/javascript"></script> -->

	<!--   <script src="../assets/global/plugins/bootstrap-wysihtml5/wysihtml5-0.3.0.js" type="text/javascript"></script> -->
	<!--         <script src="../assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.js" type="text/javascript"></script> -->
	<!--         <script src="../assets/global/plugins/bootstrap-markdown/lib/markdown.js" type="text/javascript"></script> -->
	<!--         <script src="./../assets/global/plugins/bootstrap-markdown/js/bootstrap-markdown.js" type="text/javascript"></script> -->

	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js"
		type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<!-- <script type="text/javascript" src="../ckeditor/ckeditor.js"></script>    -->

</body>
</HTML>
