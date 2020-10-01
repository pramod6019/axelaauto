<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Branch_Update" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />
</head>

<body onLoad="selectpreowned();FormFocus();" class="page-container-bg-solid page-header-menu-fixed">
<%@include file="header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY ----->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>&nbsp;Branch
						</h1>
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
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="branch-list.jsp?all=yes">List Branches</a> &gt;</li>
							<li><a href="branch-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Branch</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="container-fluid">
								<div class="portlet box ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>
											Branch
										</div>
									</div>
									<div class="portlet-body container-fluid">
										<div class="tab-pane" id="">
											<form name="form1" method="post" class="form-horizontal">
												<center>
													<font>Form fields marked with a red asterisk <b><font
															color="#ff0000">*</font></b> are required.
													</font>
												</center>
												<!-- START PORTLET BODY -->

												<div class="form-element6">
													<label> Name<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_name" type="text"
														class="form-control" value="<%=mybean.branch_name%>"
														maxlength="255" /> <span>(Enter Minimum of 3 characters)</span>
												</div>

                                               <div class="form-element6">
													<label>CIN : </label>
													<input name="txt_branch_cin" type="text"
														class="form-control" id="txt_branch_cin"
														value="<%=mybean.branch_cin%>" maxlength="21" />Example: A12345AA1234AAA123456
													 <span id="cin"></span>
												</div>
												<div class="form-element6">
													<label>GSTIN: </label>
													<input name="txt_branch_gst" type="text"
														class="form-control" id="txt_branch_gst"
														value="<%=mybean.branch_gst%>" maxlength="15" />Example: 22AAAAA0000A1Z5
													 <span id="gst"></span>
												</div>


												<div class="form-element6">
													<label>Name on Invoice<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_invoice_name" type="text"
														class="form-control"
														value="<%=mybean.branch_invoice_name%>" maxlength="255" />
												</div>

                                                 <div class="row"></div>
												<div class="form-element6">
													<label> Code<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_code" type="text"
														class="form-control" value="<%=mybean.branch_code%>"
														maxlength="25" /> (Only alphanumeric characters) are allowed!
												</div>
                                               <div class="form-element6">
													<label>TIN : </label>
													<input name="txt_branch_tin" id="txt_branch_tin" type="text"
														class="form-control" value="<%=mybean.branch_tin%>"
														maxlength="11" />
												</div>
<div class="row"></div>

												<div class="form-element6">
													<label> VAT: </label>
													<input name="txt_branch_vat" type="text"
														id="txt_branch_vat" class="form-control"
														value="<%=mybean.branch_vat%>" maxlength="25" />
												</div>

												<div class="form-element6">
													<label> CST<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_cst" type="text"
														id="txt_branch_cst" class="form-control"
														value="<%=mybean.branch_cst%>" maxlength="25" />
												</div>

												<div class="form-element6">
													<label>PAN<font color="#ff0000">*</font>: </label>
													 <input name="txt_branch_pan" type="text"
														id="txt_branch_pan" class="form-control"
														value="<%=mybean.branch_pan%>" maxlength="25" />
												</div>

												<div class="form-element6">
													<label> Quote Prefix<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_quote_prefix" type="text" class="form-control"
														value="<%=mybean.branch_quote_prefix%>" maxlength="25" />
												</div>

												<div class="form-element6">
													<label> SO Prefix<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_so_prefix" type="text"
														class="form-control" value="<%=mybean.branch_so_prefix%>"
														maxlength="25" />
												</div>

												<div class="form-element6">
													<label> Invoice Prefix<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_invoice_prefix" type="text" class="form-control"
														value="<%=mybean.branch_invoice_prefix%>" maxlength="25" />
												</div>

												<div class="form-element6">
													<label> Receipt Prefix<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_receipt_prefix" type="text" class="form-control"
														value="<%=mybean.branch_receipt_prefix%>" maxlength="25" />
												</div>
												
												<div class="form-element6">
													<label> Bill Prefix<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_bill_prefix" type="text" class="form-control"
														value="<%=mybean.branch_bill_prefix%>" maxlength="25" />
												</div>
												
												<div class="form-element6">
													<label> Job Card Prefix<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_jc_prefix" type="text" class="form-control"
														value="<%=mybean.branch_jc_prefix%>" maxlength="25" />
												</div>

												<div class="form-element6">
													<label> Region<font color="#ff0000">*</font>: </label>
													<select name="dr_branch_region_id" class="form-control" id="dr_branch_region_id">
														<%=mybean.PopulateRegion()%>
													</select>

												</div>
												
												<div class="form-element6">
													<label> Zone<font color="#ff0000">*</font>: </label>
													<select name="dr_branch_zone_id" class="form-control" id="dr_branch_zone_id">
														<%=mybean.PopulateZone()%>
													</select>

												</div>

												<div class="form-element6">
													<label> Type<font color="#ff0000">*</font>: </label>
													<select name="dr_branch_branchtype_id"
														class="form-control" id="dr_branch_branchtype_id">
														<%=mybean.PopulateBranchType()%>
													</select>

												</div>

												<div class="form-element6">
													<label>Brand<font color="#ff0000">*</font>: </label>
													<select name="dr_brand_id" class="form-control"
														id="dr_brand_id">
														<%=mybean.PopulatePrincipal()%>
													</select>

												</div>

												<div class="form-element6">
													<label>Rate Class<font color="#ff0000">*</font>: </label>
													<select name="dr_branch_rateclass_id" class="form-control"
														id="dr_branch_rateclass_id">
														<%=mybean.PopulateRateClass()%>
													</select>
												</div>
												
												<div class="form-element6">
													<label>Vehicle Follow-up Executive: </label>
													<select name="dr_vehfollowup_crmexe_id" class="form-control"
														id="dr_vehfollowup_crmexe_id">
														<%=mybean.PopulateCRMExecutive(mybean.comp_id)%>
													</select>
												</div>
												
												<div class="row"></div>

												<div class="form-element6">
													<label> Franchisee<font color="#ff0000">*</font>: </label>
													<select name="dr_franchisee_id" class="form-control">
														<%=mybean.PopulateFranchisee()%>
													</select>
												</div>

												<div class="form-element3">
													<label> Mobile 1<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_mobile1" type="text"
														id="txt_branch_mobile1"
														onKeyUp="toPhone('txt_branch_mobile1','Mobile 1')"
														class="form-control" value="<%=mybean.branch_mobile1%>"
														maxlength="13" /> <span>(91-9999999999)</span>

												</div>

												<div class="form-element3">
													<label>Mobile 2:</label> 
													<input name="txt_branch_mobile2"
														type="text" class="form-control" id="txt_branch_mobile2"
														onKeyUp="toPhone('txt_branch_mobile2','Mobile 2')"
														value="<%=mybean.branch_mobile2%>" maxlength="13" /> <span>
														(91-9999999999) </span>
												</div>

												<div class="form-element3">
													<label> Phone 1<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_phone1" type="text"
														class="form-control" id="txt_branch_phone1"
														onKeyUp="toPhone('txt_branch_phone1','Phone 1')"
														value="<%=mybean.branch_phone1%>" maxlength="14" /> <span>(91-80-33333333)</span>
												</div>

												
												<div class="form-element3">
													<label> Phone 2: </label> 
													<input name="txt_branch_phone2"
														type="text" class="form-control" id="txt_branch_phone2"
														onKeyUp="toPhone('txt_branch_phone2','Phone 2')"
														value="<%=mybean.branch_phone2%>" maxlength="14" />
													<span> (91-80-33333333)</span>
												</div>

												<div class="form-element3">
													<label> Email 1<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_email1" type="text"
														class="form-control" value="<%=mybean.branch_email1%>"
														maxlength="100" />
												</div>
												
												
											
												<div class="form-element3">
													<label>Email 2: </label> 
													<input name="txt_branch_email2"
														type="text" class="form-control"
														value="<%=mybean.branch_email2%>" maxlength="100" />
												</div>
												
                                             <div class="row"></div>
												<div class="form-element6">
													<label> Sales Mobile: </label>
													<textarea name="txt_branch_sales_mobile" cols="50" rows="3" class="form-control" id="txt_branch_sales_mobile" maxlength="500"> <%=mybean.branch_sales_mobile%></textarea>
													<span>(91-9999999999)</span>
												</div>

												<div class="form-element6">
													<label> Sales Email: </label>
													<textarea name="txt_branch_sales_email" cols="50" rows="3" class="form-control" id="txt_branch_sales_email" onKeyUp="charcount('txt_branch_sales_email', 'span_txt_branch_sales_email',
														'<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.branch_sales_email%> </textarea>
													<span id=span_txt_branch_sales_email> 1000 characters </span>

												</div>

												<div class="form-element6">
													<label> Service Booking Email: </label>
													<textarea name="txt_branch_servicebooking_email" cols="50" rows="5"
														class="form-control" branch_servicebooking_emailur_email"
														onKeyUp="charcount('txt_branch_servicebooking_email', 'span_txt_branch_servicebooking_email',
														'<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.branch_servicebooking_email%></textarea>
													<span id=span_txt_branch_servicebooking_email> 1000 characters </span>
												</div>

												<div class="form-element6">
													<label> Ticket Email: </label>
													<textarea name="txt_branch_ticket_email" cols="50" rows="5"
														class="form-control" id="txt_branch_ticket_email"
														onKeyUp="charcount('txt_branch_ticket_email', 'span_txt_branch_ticket_email',
														'<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.branch_ticket_email%></textarea>
													<span id=span_txt_branch_ticket_email> 1000 characters </span>
												</div>

												<div class="form-element6">
													<label> CRM Email: </label>
													<textarea name="txt_branch_crm_email" cols="50" rows="5"
														class="form-control" id="txt_branch_crm_email"
														onKeyUp="charcount('txt_branch_crm_email', 'span_txt_branch_crm_email',
														'<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.branch_crm_email%></textarea>
													<span id=span_txt_branch_crm_email> 1000 characters </span>
												</div>

												<div class="form-element6">
													<label> CRM Mobile: </label> 
													<input name="txt_branch_crm_mobile" type="text" id="txt_branch_crm_mobile"
														onKeyUp="toPhone('txt_branch_crm_mobile','CRM SMS')"
														class="form-control" value="<%=mybean.branch_crm_mobile%>" maxlength="13" />
													<span>(91-9999999999)</span>

												</div>


												<div class="form-element6">
													<label> JCPSF Mobile: </label> 
													<input name="txt_branch_jcpsf_mobile" type="text"
														id="txt_branch_jcpsf_mobile"
														onKeyUp="toPhone('txt_branch_jcpsf_mobile','JCPSF SMS')"
														class="form-control"
														value="<%=mybean.branch_jcpsf_mobile%>" size="20"
														maxlength="13" /> <span>(91-9999999999)</span>

												</div>

												<div class="form-element6">
													<label> JCPSF Email: </label>
													<textarea name="txt_branch_jcpsf_email" cols="50" rows="5"
														class="form-control" id="txt_branch_jcpsf_email"
														onKeyUp="charcount('txt_branch_jcpsf_email', 'span_txt_branch_jcpsf_email','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.branch_jcpsf_email%></textarea>
													<span id=span_txt_branch_jcpsf_email> 1000
														characters </span>
												</div>

												<!-- CIN starts -->
												<div class="form-element6">
													<label>SO CIN Email: </label>
													<textarea name="txt_branch_socin_email" cols="50" rows="5"
														class="form-control" id="txt_branch_socin_email"
														onKeyUp="charcount('txt_branch_socin_email', 'span_txt_branch_socin_email','
														<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.branch_socin_email%>
													</textarea>
													<span id=span_txt_branch_socin_email> 1000 characters
													</span>
												</div>

												<div class="form-element6">
													<label>SO CIN Mobile: </label>
													<textarea name="txt_branch_socin_mobile" cols="50" rows="3"
														class="form-control" id="txt_branch_socin_mobile">
														<%=mybean.branch_socin_mobile%></textarea>
													<span>(91-9999999999)</span>
												</div>

												<div class="form-element6">
													<label> DIN Mobile: </label>
													<textarea name="txt_branch_din_mobile" cols="50" rows="3" class="form-control" id="txt_branch_din_mobile"> <%=mybean.branch_din_mobile%></textarea> 
													<span>(91-9999999999)</span>
												</div>


												<div class="form-element6">
													<label> DIN Email: </label>
													<textarea name="txt_branch_din_email" cols="50" rows="5" class="form-control" id="txt_branch_din_email" onKeyUp="charcount('txt_branch_din_email', 'span_txt_branch_din_email',' <font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.branch_din_email%></textarea>
													<span id=span_txt_branch_din_email> 1000 characters </span>
												</div>


												<div class="form-element6">
													<label> Address<font color="#ff0000">*</font>: </label>
													<textarea name="txt_branch_add" cols="50" rows="5" class="form-control" id="txt_branch_add" onKeyUp="charcount('txt_branch_add', 'span_txt_branch_add', '<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.branch_add%></textarea>
													<span id=span_txt_branch_add> 255 characters </span>
												</div>

												<div class="form-element6">
													<label> Pin/Zip<font color="#ff0000">*</font>: </label>
													<input name="txt_branch_pin" type="text"
														class="form-control" id="txt_branch_pin"
														onKeyUp="toInteger('txt_branch_pin','Pin')"
														value="<%=mybean.branch_pin%>" maxlength="6" />
												</div>

												<div class="form-element6">
													<label> City<font color="#ff0000">*</font>: </label>
													<select class="form-control select2" id="maincity"
														name="maincity">
														<%=mybean.citycheck.PopulateCities(mybean.branch_city_id, mybean.comp_id)%>
													</select>
												</div>

												<div class="form-element6">
													<label>Quote Description: </label>
													<textarea name="txt_quote_desc" cols="70" rows="4" class="form-control summernote_1" id="txt_quote_desc"><%=mybean.quote_desc%></textarea>

												</div>

												<div class="form-element6">
													<label> Quote Terms & Conditions: </label>
													<textarea name="txt_quote_terms" cols="70" rows="4"
														class="form-control summernote_1" id="txt_quote_terms"><%=mybean.quote_terms%></textarea>

												</div>
												<div class="form-element6">
													<label> Sales Order Description: </label>
													<textarea name="txt_so_desc" cols="70" rows="4"
														class="form-control summernote_1" id="txt_so_desc"><%=mybean.so_desc%></textarea>

												</div>
												<div class="form-element6">
													<label> Sales Order Terms & Conditions: </label>
													<textarea name="txt_so_terms" cols="70" rows="4"
														class="form-control summernote_1" id="txt_so_terms"><%=mybean.so_terms%></textarea>

												</div>
												
												<div class="form-element6">
													<label> Invoice Terms & Conditions: </label>
													<textarea name="branch_invoice_terms" cols="70" rows="4"
														class="form-control summernote_1" id="branch_invoice_terms"><%=mybean.branch_invoice_terms%></textarea>

												</div>
												<div class="form-element6">
													<label> Bill Terms & Conditions: </label>
													<textarea name="branch_bill_terms" cols="70" rows="4"
														class="form-control summernote_1" id="branch_bill_terms"><%=mybean.branch_bill_terms%></textarea>

												</div>

												<div class="form-element6 form-element-margin">
													<label> Active: </label> 
													<input type="checkbox" name="ch_branch_active"
														<%=mybean.PopulateCheck(mybean.branch_active)%> />
												</div>

												<div class="form-element6">
													<label> Notes: </label>
													<textarea name="txt_branch_notes" cols="70" rows="4"
														class="form-control" id="txt_branch_notes"><%=mybean.branch_notes%></textarea>
												</div>

												


												<%
													if (mybean.status.equals("Update")) {
												%>

												<div class="form-element12" >
												<div class="form-element2 "></div>
													<div class="form-element8 ">
														<label> Automated Tasks: </label>
														<table class="table table-responsive table-hover table-bordered">
															<thead>
																<tr>
																	<th>Task Type</th>
																	<th>Send Email</th>
																	<th>Format Email</th>
																	<th>Send SMS</th>
																	<th>Format SMS</th>
																</tr>
															</thead>
															<tbody>

																<%-- <tr>
																<td align="left">Lead:</td>
																<td align="center"><input type="checkbox"
																	name="chk_branch_lead_email_enable"
																	<%=mybean.PopulateCheck(mybean.branch_lead_email_enable)%>></td>
																<td align="center"><a
																	href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Lead&opt=branch_lead_email_format">Format</a></td>
																<td align="center"><input type="checkbox"
																	name="chk_branch_lead_sms_enable"
																	<%=mybean.PopulateCheck(mybean.branch_lead_sms_enable)%>></td>
																<td align="center"><a
																	href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Lead&opt=branch_lead_sms_format">Format</a></td>
															</tr> --%>
																<%-- <tr>
																<td align="left">Lead for Executive:</td>
																<td align="center">&nbsp;</td>
																<td align="center"><a
																	href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Lead for Executive&opt=branch_lead_email_exe_format">Format</a></td>
																<td align="center">&nbsp;</td>
																<td align="center"><a
																	href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Lead for Executive&opt=branch_lead_sms_exe_format">Format</a></td>
															</tr> --%>
																<tr>
																	<td align="left">Enquiry:</td>
																	<td align="center">
																		<input name="chk_branch_enquiry_email_enable" type="checkbox" id="chk_branch_enquiry_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_enquiry_email_enable)%>>
																	</td>
																	<td align="center">
																		<a href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Enquiry&opt=branch_enquiry_email_format">Format</a>
																	</td>
																	<td align="center">
																		<input type="checkbox" name="chk_branch_enquiry_sms_enable" <%=mybean.PopulateCheck(mybean.branch_enquiry_sms_enable)%>>
																	</td>
																	<td align="center">
																		<a href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Enquiry&opt=branch_enquiry_sms_format">Format</a>
																	</td>
																</tr>
																<tr>
																	<td align="left">Enquiry for Executive:</td>
																	<td align="center">&nbsp;</td>
																	<td align="center">
																		<a href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Enquiry for Executive&opt=branch_enquiry_email_exe_format">Format</a>
																	</td>
																	<td align="center">&nbsp;</td>
																	<td align="center">
																	<a href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Enquiry for Executive&opt=branch_enquiry_sms_exe_format">Format</a>
																	</td>
																</tr>
																<tr>
																	<td align="left">Brochure:</td>
																	<td align="center"><input type="checkbox"
																		name="chk_branch_enquiry_brochure_email_enable"
																		id="chk_branch_enquiry_brochure_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_enquiry_brochure_email_enable)%>>
																	</td>
																	<td align="center">
																	<a href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Brochure&opt=branch_enquiry_brochure_email_format">Format</a>
																	</td>
																	<td align="center">&nbsp;</td>
																	<td align="center">&nbsp;</td>
																</tr>

																<tr>
																	<td align="left">Test Drive:</td>
																	<td align="center"><input type="checkbox"
																		name="chk_branch_testdrive_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_testdrive_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Test Drive&opt=branch_testdrive_email_format">Format</a></td>
																	<td align="center"><input type="checkbox"
																		name="chk_branch_testdrive_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_testdrive_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Test Drive&opt=branch_testdrive_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Test Drive for Executive:</td>
																	<td align="center">&nbsp;</td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Test Drive for Executive&opt=branch_testdrive_email_exe_format">Format</a></td>
																	<td align="center">&nbsp;</td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Test Drive for Executive&opt=branch_testdrive_sms_exe_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Quote:</td>
																	<td align="center"><input type="checkbox"
																		name="chk_branch_quote_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_quote_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Quote&opt=branch_quote_email_format">Format</a></td>
																	<td align="center"><input type="checkbox"
																		name="chk_branch_quote_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_quote_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Quote&opt=branch_quote_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Quote for Executive:</td>
																	<td align="center">&nbsp;</td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Quote for Executive&opt=branch_quote_email_exe_format">Format</a></td>
																	<td align="center">&nbsp;</td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Quote for Executive&opt=branch_quote_sms_exe_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Sales Order:</td>
																	<td align="center"><input
																		name="chk_branch_so_email_enable" type="checkbox"
																		id="chk_branch_so_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_so_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Sales Order&opt=branch_so_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_so_sms_enable" type="checkbox"
																		id="chk_branch_so_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_so_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Sales Order&opt=branch_so_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Sales Order Delivered:</td>
																	<td align="center"><input
																		name="chk_branch_so_delivered_email_enable"
																		type="checkbox"
																		id="chk_branch_so_delivered_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_so_delivered_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Sales Order Delivered&opt=branch_so_delivered_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_so_delivered_sms_enable"
																		type="checkbox"
																		id="chk_branch_so_delivered_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_so_delivered_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Sales Order Delivered&opt=branch_so_delivered_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Sales Order for Executive:</td>
																	<td align="center">&nbsp;</td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Sales Order for Executive&opt=branch_so_email_exe_format">Format</a></td>
																	<td align="center">&nbsp;</td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Sales Order for Executive&opt=branch_so_sms_exe_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Service Due:</td>
																	<td align="center"><input
																		name="chk_branch_service_due_email_enable"
																		type="checkbox"
																		id="chk_branch_service_due_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_service_due_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Service Due&opt=branch_service_due_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_service_due_sms_enable"
																		type="checkbox" id="chk_branch_service_due_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_service_due_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Service Due&opt=branch_service_due_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Service Booking:</td>
																	<td align="center"><input
																		name="chk_branch_service_appointment_email_enable"
																		type="checkbox"
																		id="chk_branch_service_appointment_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_service_appointment_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Service Booking&opt=branch_service_appointment_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_service_appointment_sms_enable"
																		type="checkbox"
																		id="chk_branch_service_appointment_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_service_appointment_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Service Booking&opt=branch_service_appointment_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Pre Owned:</td>
																	<td align="center">
																		<input name="chk_branch_preowned_email_enable" type="checkbox" id="chk_branch_preowned_email_enable" <%=mybean.PopulateCheck(mybean.branch_preowned_email_enable)%>></td>
																	<td align="center">
																		<a href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Pre Owned&opt=branch_preowned_email_format">Format</a></td> <td align="center">
																		<input name="chk_branch_preowned_sms_enable" type="checkbox" id="chk_branch_preowned_sms_enable" <%=mybean.PopulateCheck(mybean.branch_preowned_sms_enable)%>>
																	</td>
																	<td align="center">
																	<a href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Pre Owned&opt=branch_preowned_sms_format">Format</a>
																	</td>
																</tr>
																<tr>
																	<td align="left">Pre Owned For Executive:</td>
																	<td align="center">&nbsp;</td>
																	<td align="center">
																		<a href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Pre Owned For Executive&opt=branch_preowned_email_exe_format">Format</a>
																	</td>
																	<td align="center">&nbsp;</td>
																	<td align="center">
																		<a href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Pre Owned For Executive&opt=branch_preowned_sms_exe_format">Format</a>
																	</td>
																</tr>
																<tr>
																	<td align="left">New Job Card:</td>
																	<td align="center">
																	<input name="chk_branch_jc_new_email_enable" type="checkbox"
																		id="chk_branch_jc_new_email_enable"
																		<%=mybean .PopulateCheck(mybean.branch_jc_new_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=New Job Card&opt=branch_jc_new_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_jc_new_sms_enable" type="checkbox"
																		id="chk_branch_jc_new_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_jc_new_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=New Job Card&opt=branch_jc_new_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Ready Job Card:</td>
																	<td align="center"><input
																		name="chk_branch_jc_ready_email_enable"
																		type="checkbox" id="chk_branch_jc_ready_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_jc_ready_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Ready Job Card&opt=branch_jc_ready_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_jc_ready_sms_enable" type="checkbox"
																		id="chk_branch_jc_ready_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_jc_ready_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Ready Job Card&opt=branch_jc_ready_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Delivered Job Card:</td>
																	<td align="center"><input
																		name="chk_branch_jc_delivered_email_enable"
																		type="checkbox"
																		id="chk_branch_jc_delivered_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_jc_delivered_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Delivered Job Card&opt=branch_jc_delivered_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_jc_delivered_sms_enable"
																		type="checkbox"
																		id="chk_branch_jc_delivered_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_jc_delivered_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Delivered Job Card&opt=branch_jc_delivered_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Job Card Estimate:</td>
																	<td align="center"><input
																		name="chk_branch_jc_estimate_email_enable"
																		type="checkbox"
																		id="chk_branch_jc_estimate_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_jc_estimate_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Job Card Estimate&opt=branch_jc_estimate_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_jc_estimate_sms_enable"
																		type="checkbox" id="chk_branch_jc_estimate_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_jc_estimate_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Job Card Estimate&opt=branch_jc_estimate_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">Job Card Feedback:</td>
																	<td align="center"><input
																		name="chk_branch_jc_feedback_email_enable"
																		type="checkbox"
																		id="chk_branch_jc_feedback_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_jc_feedback_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Job Card Feedback&opt=branch_jc_feedback_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_jc_feedback_sms_enable"
																		type="checkbox" id="chk_branch_jc_feedback_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_jc_feedback_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Job Card Feedback&opt=branch_jc_feedback_sms_format">Format</a></td>
																</tr>

																<tr>
																	<td align="left">Insurance New:</td>
																	<td align="center"><input
																		name="chk_branch_insur_new_email_enable"
																		type="checkbox" id="chk_branch_insur_new_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_insur_new_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Insurance New&opt=branch_insur_new_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_insur_new_sms_enable" type="checkbox"
																		id="chk_branch_insur_new_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_insur_new_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Insurance New&opt=branch_insur_new_sms_format">Format</a></td>
																</tr>

																<tr>
																	<td align="left">Insurance Lost:</td>
																	<td align="center"><input
																		name="chk_branch_insur_lost_email_enable"
																		type="checkbox"
																		id="chk_branch_insur_lost_email_enable"
																		<%=mybean.PopulateCheck(mybean.branch_insur_lost_email_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Insurance Lost&opt=branch_insur_lost_email_format">Format</a></td>
																	<td align="center"><input
																		name="chk_branch_insur_lost_sms_enable"
																		type="checkbox" id="chk_branch_insur_lost_sms_enable"
																		<%=mybean.PopulateCheck(mybean.branch_insur_lost_sms_enable)%>></td>
																	<td align="center"><a
																		href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Insurance Lost&opt=branch_insur_lost_sms_format">Format</a></td>
																</tr>

																<!-- 															<tr> -->
																<!-- 																<td align="left">Invoice:</td> -->
																<!-- 																<td align="center"><input type="checkbox" -->
																<!-- 																	name="chk_branch_invoice_email_enable" -->
																<%-- 																	<%=mybean.PopulateCheck(mybean.branch_invoice_email_enable)%>></td> --%>
																<!-- 																<td align="center"><a -->
																<%-- 																	href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Invoice Confirmation&opt=branch_invoice_email_format">Format</a></td> --%>
																<!-- 																<td align="center"><input type="checkbox" -->
																<!-- 																	name="chk_branch_invoice_sms_enable" -->
																<%-- 																	<%=mybean.PopulateCheck(mybean.branch_invoice_sms_enable)%>></td> --%>
																<!-- 																<td align="center"><a -->
																<%-- 																	href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Invoice Confirmation&opt=branch_invoice_sms_format">Format</a></td> --%>
																<!-- 															</tr> -->
																<!-- 															<tr> -->
																<!-- 																<td align="left">Receipt:</td> -->
																<!-- 																<td align="center"><input type="checkbox" -->
																<!-- 																	name="chk_branch_receipt_email_enable" -->
																<%-- 																	<%=mybean.PopulateCheck(mybean.branch_receipt_email_enable)%>></td> --%>
																<!-- 																<td align="center"><a -->
																<%-- 																	href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Receipt Confirmation&opt=branch_receipt_email_format">Format</a></td> --%>
																<!-- 																<td align="center"><input type="checkbox" -->
																<!-- 																	name="chk_branch_receipt_sms_enable" -->
																<%-- 																	<%=mybean.PopulateCheck(mybean.branch_receipt_sms_enable)%>></td> --%>
																<!-- 																<td align="center"><a -->
																<%-- 																	href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Receipt Confirmation&opt=branch_receipt_sms_format">Format</a></td> --%>
																<!-- 															</tr> -->
															</tbody>
														</table>
													</div>
												</div>
												<%
													}
												%>
												<!-- 											<div class="form-group"> -->
												<!-- 												<label > Enable SMS: </label> -->
												<!-- 												<div class="col-md-6 col-xs-12" -->
												<!-- 													 -->
												<!-- 													<input type="checkbox" name="chk_branch_sms_enable" -->
												<%-- 														<%=mybean.PopulateCheck(mybean.branch_sms_enable)%>> --%>
												<!-- 											</div> -->

												<!-- 											<div class="form-group"> -->
												<!-- 												<label > SMS URL: </label> -->
												<!-- 												 -->
												<!-- 													<textarea name="txt_branch_sms_url" cols="70" rows="4" -->
												<%-- 														class="form-control" id="txt_branch_sms_url"><%=mybean.branch_sms_url%></textarea> --%>
												<!-- 											</div> -->
												<div class="form-element12">
													<div class="form-element8 form-element-center">
														<div class="form-element4 form-element">
															<label>Enable Enquiry Escalation: </label> 
															<input type="checkbox" name="ch_branch_esc_enquiry"
																<%=mybean.PopulateCheck(mybean.branch_esc_enquiry)%> />
														</div>

														<div class="form-element4">
															<label>Enable Enquiry Follow-up Escalation: </label> 
															<input type="checkbox" name="ch_branch_esc_enquiry_followup"
																<%=mybean.PopulateCheck(mybean.branch_esc_enquiry_followup)%> />
														</div>

														<div class="form-element4">
															<label>Pre-Owned Follow-up Escalation: </label> 
															<input type="checkbox" name="ch_branch_esc_preowned_followup"
																<%=mybean.PopulateCheck(mybean.branch_esc_preowned_followup)%> />
														</div>
													</div>
												</div>

												<div class="form-element12">
													<div class="form-element8 form-element-center">
														<div class="form-element4 form-element">
															<label>Pre-Owned Evaluation Escalation: </label> 
															<input type="checkbox" name="ch_branch_esc_preowned_eval_followup"
																<%=mybean.PopulateCheck(mybean.branch_esc_preowned_eval_followup)%> />
														</div>

														<div class="form-element4">
															<label>Enable CRM Follow-up Escalation:</label> 
															<input type="checkbox" name="ch_branch_esc_crm_followup" <%=mybean.PopulateCheck(mybean.branch_esc_crm_followup)%> />
														</div>

														<div class="form-element4">
															<label>Enable PSF Follow-up Escalation:</label> 
															<input type="checkbox" name="ch_branch_esc_servicepsf_followup" <%=mybean.PopulateCheck(mybean.branch_esc_servicepsf_followup)%> />
														</div>
													</div>
												</div>
												
												<div class="form-element12">
													<div class="form-element8 form-element-center">
														<div class="form-element4 form-element">
															<label>Enable Service Follow-up Escalation: </label> 
															<input type="checkbox" name="ch_branch_esc_serviceveh_followup"
																<%=mybean.PopulateCheck(mybean.branch_esc_serviceveh_followup)%> />
														</div>
													</div>
												</div>		
												
												<% if (mybean.status.equals("Update") && !(mybean.branch_entry_by == null)
															&& !(mybean.branch_entry_by.equals(""))) {
												%>
												<div class="form-element12">
													<div class="form-element8 form-element form-element-center">
														<div class="form-element6">
															<label>Entry By: </label>
															<%=mybean.unescapehtml(mybean.branch_entry_by)%>
															<input type="hidden" name="branch_entry_by"
																value="<%=mybean.branch_entry_by%>" />
														</div>
														<% } %>

														<% if (mybean.status.equals("Update") && !(mybean.entry_date == null)
																	&& !(mybean.entry_date.equals(""))) {
														%>
														<div class="form-element6">
															<label>Entry Date: </label>
															<%=mybean.entry_date%>
															<input type="hidden" name="entry_date"
																value="<%=mybean.entry_date%>" />
														</div>
													</div>
												</div>
												
												<% } %>

												<% if (mybean.status.equals("Update") && !(mybean.branch_modified_by == null)
															&& !(mybean.branch_modified_by.equals(""))) {
												%>

												<div class="form-element12">
													<div class="form-element8 form-element form-element-center">
														<div class="form-element6">
															<label>Modified By: </label>
															<%=mybean.unescapehtml(mybean.branch_modified_by)%>
															<input type="hidden" name="branch_modified_by"
																value="<%=mybean.branch_modified_by%>" />
														</div>

														<% } %>


														<% if (mybean.status.equals("Update") && !(mybean.modified_date == null)
																	&& !(mybean.modified_date.equals(""))) {
														%>

														<div class="form-element6">
															<label>Modified Date: </label>
															<%=mybean.modified_date%>
															<input type="hidden" name="modified_date"
																value="<%=mybean.modified_date%>" />
														</div>
													</div>
												</div>
												
												<% }
												%>

												<div class="row"></div>
												<% if (mybean.status.equals("Add")) { %>
												
												<div class="form-element12">
													<div class="form-element2 form-element-center">
														<input name="button" type="submit" class="btn btn-success"
														id="button" value="Add Branch"
														onClick="return SubmitFormOnce(document.form1, this);" />
														<input type="hidden" name="add_button" value="yes" />
													</div>
												</div>

												<% } else if (mybean.status.equals("Update")) { %>
												<center>
													<input type="hidden" name="update_button" value="yes" />
													<input name="button" type="submit" class="btn btn-success"
														id="button" value="Update Branch"
														onClick="return SubmitFormOnce(document.form1, this);" />

													<input name="delete_button" type="submit"
														class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)" value="Delete Branch" />
												</center>

												<%
													}
												%>

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
	</div>


	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>


	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js" type="text/javascript"></script>


	<script>
	$(function() {
		$("#txt_branch_gst").focusout(function(){
			var gst_no=$("#txt_branch_gst").val();
			var regex=/^([0-9]{2})([a-zA-Z]{5})([0-9]{4})([a-zA-Z]{1})([a-zA-Z0-9]{3})?$/;
			if(gst_no.length!=0 && gst_no.length < 15){
				$("#gst").html("<br><b><font color='red'>GSTIN is invalid</font></b>");
			}else if(gst_no.length!=0 && regex.test(gst_no) == false){
				$("#gst").html("<br><b><font color='red'>GSTIN is invalid</font></b>");
			}else{
				$("#gst").html("");
			}			
		});
		$("#txt_branch_cin").focusout(function(){
			var branch_cin=$("#txt_branch_cin").val();
			var regex=/^([A-Z]{1})([0-9]{5})([A-Z]{2})([0-9]{4})([A-Z]{3})([0-9]{6})?$/;
			if(branch_cin.length!=0 && branch_cin.length < 21){
				$("#cin").html("<br><b><font color='red'>CIN is invalid</font></b>");
			}else if(branch_cin.length!=0 && regex.test(branch_cin) == false){
				$("#cin").html("<br><b><font color='red'>CIN is invalid</font></b>");
			}else{
				$("#cin").html("");
			}	
		});
		
 	});
	</script>

	<script>
	  $("#txt_branch_cin, #txt_branch_gst").bind('keyup',function(e){
		  $("#txt_branch_cin").val(($("#txt_branch_cin").val()).toUpperCase());
		  $("#txt_branch_gst").val(($("#txt_branch_gst").val()).toUpperCase());
	  });
	  
	</script>

	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_branch_name.focus();
	}

	function selectpreowned() {
		var temp = document.getElementById('dr_branch_branchtype_id').value;
		if (temp == 1) {
			$("#preowned_div").show();
		} else {
			$("#preowned_div").hide();
		}
	}
</script>
</body>
</html>
