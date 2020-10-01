<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.SO_WaitingPeriodDays_Format"
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

  <link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
        <link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />
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
						<h1>SO Waiting Period Format</h1>
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
						<li><a href="so-waitingperioddays-list.jsp?all=yes">List SO Waiting Period</a> &gt;</li>
						<li><a href="so-waitingperioddays-format.jsp?<%=mybean.QueryString%>">
								Format For SO Waiting Period Days</a> <b>:</b></li>

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
										Format For SO Waiting Period Days
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
															CKEDITOR.replace(
 																			'txt_format',
																			{
																				uiColor : hexc($(
 																						"a:link")
																						.css(
 																								"color")),
																				height : '250px'
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
													if (mybean.status.equals("SO WaitingPeriodDays") || mybean.status.equals("CRM Dis-Satisfied SMS") 
															|| mybean.status.equals("CRM Satisfied SMS For Executive") || mybean.status.equals("CRM Dis-Satisfied SMS For Executive")
															|| mybean.status.equals("CRM Contactable SMS") || mybean.status.equals("CRM Contactable SMS For Executive")
															|| mybean.status.equals("CRM Non Contactable SMS") || mybean.status.equals("CRM Non Contactable SMS For Executive")) {
												%>
												<tr>
													<td align="right">So ID:</td>
													<td align="left">[SOID]</td>
												</tr>
												
												<tr>
													<td align="right">So Date:</td>
													<td align="left">[SODATE]</td>
												</tr>
												
												<tr>
													<td align="right">Promise Date:</td>
													<td align="left">[PROMISEDATE]</td>
												</tr>
												
												<tr>
													<td align="right">Enquiry ID:</td>
													<td align="left">[ENQUIRYID]</td>
												</tr>
												
												<tr>
													<td align="right">Sales Executive:</td>
													<td align="left">[SALESEXE]</td>
												</tr>
												
												<tr>
													<td align="right">Sales Executive Mobile1:</td>
													<td align="left">[SALESEXEMOBILE1]</td>
												</tr>
												
												<tr>
													<td align="right">Sales Executive E-Mail1:</td>
													<td align="left">[SALESEXEEMAIL1]</td>
												</tr>
												
												<tr>
													<td align="right">Sales Manager:</td>
													<td align="left">[SALESMANAGER]</td>
												</tr>
												
												<tr>
													<td align="right">Sales Manager Mobile1:</td>
													<td align="left">[SALESMANAGERMOBILE1]</td>
												</tr>
												
												<tr>
													<td align="right">Sales Manager E-Mail1:</td>
													<td align="left">[SALESMANAGEREMAIL1]</td>
												</tr>
												
												<tr>
													<td align="right">PBF Executive:</td>
													<td align="left">[PBFEXE]</td>
												</tr>
												
												<tr>
													<td align="right">PBF Executive Mobile1:</td>
													<td align="left">[PBFEXEMOBILE1]</td>
												</tr>
												
													<tr>
													<td align="right">PBF Executive E-Mail1:</td>
													<td align="left">[PBFEXEEMAIL1]</td>
												</tr>
												
												<tr>
													<td align="right">CRM Executive:</td>
													<td align="left">[CRMEXE]</td>
												</tr>
												
												<tr>
													<td align="right">CRM Executive Mobile1:</td>
													<td align="left">[CRMEXEMOBILE1]</td>
												</tr>
												
												<tr>
													<td align="right">CRM Executive E-Mail1:</td>
													<td align="left">[CRMEXEEMAIL1]</td>
												</tr>
												
												<tr>
													<td align="right">Customer Name:</td>
													<td align="left">[CUSTOMERNAME]</td>
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
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												
												<tr>
													<td align="right">Variant Name:</td>
													<td align="left">[VARIANTNAME]</td>
												</tr>
												
												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												
												 
												<%
													}
												%>
													<%
													if (mybean.status.equals("CRM Satisfied") || mybean.status.equals("CRM Dis-Satisfied")
															|| mybean.status.equals("CRM Satisfied For Executive") 
															|| mybean.status.equals("CRM Dis-Satisfied For Executive")
															|| mybean.status.equals("CRM Contactable")
															|| mybean.status.equals("CRM Non Contactable")
															|| mybean.status.equals("CRM Contactable For Executive")
															|| mybean.status.equals("CRM Non Contactable For Executive")){
												%>
												<tr>
													<td align="right">Enquiry ID:</td>
													<td align="left">[ENQUIRYID]</td>
												</tr>
												<tr>
													<td align="right">CRM Day:</td>
													<td align="left">[CRMDAY]</td>
												</tr>
												<tr>
													<td align="right">Entry Date:</td>
													<td align="left">[ENTRYDATE]</td>
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
													<td align="right">CRM Executive:</td>
													<td align="left">[CRMEXE]</td>
												</tr>
												<tr>
													<td align="right">CRM Executive Job Title:</td>
													<td align="left">[CRMEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">CRM Executive Mobile1:</td>
													<td align="left">[CRMEXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Sales Executive:</td>
													<td align="left">[SALESEXE]</td>
												</tr>
												<tr>
													<td align="right">Sales Executive Job Title:</td>
													<td align="left">[SALESEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Sales Executive Mobile1:</td>
													<td align="left">[SALESEXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Sales Executive E-Mail1:</td>
													<td align="left">[SALESEXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Variant Name:</td>
													<td align="left">[VARIANTNAME]</td>
												</tr>
												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
													<tr>
													<td align="right">Branch Email:</td>
													<td align="left">[BRANCEMAIL]</td>
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

    
    <script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
 <script src="../assets/js/summernote.min.js" type="text/javascript"></script>
   <script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<!-- <script type="text/javascript" src="../ckeditor/ckeditor.js"></script>    -->

</body>
</HTML>
