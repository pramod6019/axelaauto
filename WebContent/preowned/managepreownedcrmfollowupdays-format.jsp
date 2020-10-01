<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.ManagePreOwnedCRMFollowupDays_Format"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<%@include file="../Library/css.jsp" %>

  <link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
  <link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />

</head>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Pre-Owned CRM Days Format</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="managepreownedcrmfollowupdays.jsp?all=yes">List Pre-Owned CRM Days</a> &gt;</li>
							<li><a href="managepreownedcrmfollowupdays-format.jsp?<%=mybean.QueryString%>"> Format For <%=mybean.status%></a> <b>:</b></li>
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
													<div class="col-md-6">
														
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
// 															CKEDITOR.replace(
//  																			'txt_format',
// 																			{
// 																				uiColor : hexc($(
//  																						"a:link")
// 																						.css(
//  																								"color")),
// 																				height : '250px'
// 																			});
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
													if (mybean.status.equals("Pre-Owned CRM Satisfied SMS") || mybean.status.equals("Pre-Owned CRM Dis-Satisfied SMS") 
															|| mybean.status.equals("Pre-Owned CRM Satisfied SMS For Executive") || mybean.status.equals("Pre-Owned CRM Dis-Satisfied SMS For Executive")
															|| mybean.status.equals("Pre-Owned CRM Contactable SMS") || mybean.status.equals("Pre-Owned RM Contactable SMS For Executive")
															|| mybean.status.equals("Pre-Owned CRM Non Contactable SMS") || mybean.status.equals("Pre-Owned CRM Non Contactable SMS For Executive")) {
												%>
												<tr>
													<td align="right">Pre-Owned ID:</td>
													<td align="left">[PREOWNEDID]</td>
												</tr>
												<tr>
													<td align="right">Pre-Owned CRM Day:</td>
													<td align="left">[PREOWNEDCRMDAY]</td>
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
													<td align="right">Sales Consultant:</td>
													<td align="left">[PREOWNEDSEXE]</td>
												</tr>
												<tr>
													<td align="right">Pre-Owned Consultant Job Title:</td>
													<td align="left">[PREOWNEDEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Pre-Owned Consultant Mobile1:</td>
													<td align="left">[PREOWNEDEXEMOBILE1]</td>
												</tr>
<!-- 												<tr> -->
<!-- 													<td align="right">Sales Consultant Mobile2:</td> -->
<!-- 													<td align="left">[EXEMOBILE2]</td> -->
<!-- 												</tr> -->
												<tr>
													<td align="right">Pre-Owned Consultant E-Mail1:</td>
													<td align="left">[PREOWNEDEXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">VOC:</td>
													<td align="left">[VOC]</td>
												</tr>
												<tr>
													<td align="right">CRM Concern:</td>
													<td align="left">[CONCERN]</td>
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
													<%
													if (mybean.status.equals("Pre-Owned CRM Satisfied") || mybean.status.equals("Pre-Owned CRM Dis-Satisfied")
															|| mybean.status.equals("Pre-Owned CRM Satisfied For Executive") 
															|| mybean.status.equals("Pre-Owned CRM Dis-Satisfied For Executive")
															|| mybean.status.equals("Pre-Owned CRM Contactable")
															|| mybean.status.equals("Pre-Owned CRM Non Contactable")
															|| mybean.status.equals("Pre-Owned CRM Contactable For Executive")
															|| mybean.status.equals("Pre-Owned CRM Non Contactable For Executive")){
												%>
												<tr>
													<td align="right">Pre-Owned ID:</td>
													<td align="left">[PREOWNEDID]</td>
												</tr>
												<tr>
													<td align="right">Pre-Owned CRM Day:</td>
													<td align="left">[PREOWNEDCRMDAY]</td>
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
													<td align="right">Pre-Owned Consultant:</td>
													<td align="left">[PREOWNEDEXE]</td>
												</tr>
												<tr>
													<td align="right">Pre-Owned Consultant Job Title:</td>
													<td align="left">[PREOWNEDEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Pre-Owned Consultant Mobile1:</td>
													<td align="left">[PREOWNEDEXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Pre-Owned Consultant E-Mail1:</td>
													<td align="left">[PREOWNEDEXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">VOC:</td>
													<td align="left">[VOC]</td>
												</tr>
												<tr>
													<td align="right">CRM Concern:</td>
													<td align="left">[CONCERN]</td>
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
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_format.focus();
	}
</script>
</body>
</html>
