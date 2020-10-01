<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.BrandConfig_Format" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<meta content="width=device-width, initial-scale=1" name="viewport" />
<%@include file="../Library/css.jsp"%>
<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />

</HEAD>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
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
						<h1>Brand Config Format</h1>
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
						<li><a href="managebrandconfig-list.jsp?all=yes">List Brand Config</a> &gt;</li>
						<li><a href="brandconfig-format.jsp?<%=mybean.QueryString%>">
								Format For <%=mybean.status%></a> <b>:</b></li>

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
										Format For <%=mybean.title%>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<% if (mybean.email.equals("yes")) { %>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-3 control-label">Subject:&nbsp;</label>
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
														<% if (mybean.email.equals("yes")) { %>
														<textarea name="txt_format" cols="70" rows="5"
															class="form-control summernote_1" id="txt_format"
															<%if (mybean.sms.equals("yes")) {%>
															onKeyUp="charcount('txt_format', 'span_txt_format','<font color=red>({CHAR} characters left)</font>', '1000')"
															<%}%>><%=mybean.format_desc%></textarea>
															
														<% } else { %>
														<textarea name="txt_format" cols="70" rows="5"
															class="form-control" id="txt_format"
															<%if (mybean.sms.equals("yes")) {%>
															onKeyUp="charcount('txt_format', 'span_txt_format','<font color=red>({CHAR} characters left)</font>', '1000')"
															<%}%>><%=mybean.format_desc%></textarea>
														<span id="span_txt_format"> 1000 characters </span>
														<% } %>
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
													if (mybean.status.equals("IssueCoupon")) {
												%>
												
												<tr>
													<td align="right">Contact ID:</td>
													<td align="left">[CONTACTID]</td>
												</tr>
												<tr>
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Coupon ID:</td>
													<td align="left">[COUPONID]</td>
												</tr>
												<tr>
													<td align="right">Coupon Offer:</td>
													<td align="left">[COUPONOFFER]</td>
												</tr>
												<tr>
													<td align="right">Coupon Value:</td>
													<td align="left">[COUPONVALUE]</td>
												</tr>
												<tr>
													<td align="right">Coupon Validity:</td>
													<td align="left">[COUPONVALIDITY]</td>
												</tr>
												<%} %>
												<%
													if (mybean.status.equals("Enquiry") || mybean.status.equals("Enquiry for Executive") || mybean.status.equals("Brochure") || mybean.status.equals("Pre Owned")
															|| mybean.status.equals("Pre Owned For Executive")) {
												%>
												<tr>
													<td align="right">Enquiry ID:</td>
													<td align="left">[ENQUIRYID]</td>
												</tr>
												<tr>
													<td align="right">Enquiry Name:</td>
													<td align="left">[ENQUIRYNAME]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												<tr>
													<td align="right">Branch Mobile1:</td>
													<td align="left">[BRANCHMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Branch Address:</td>
													<td align="left">[BRANCHADDRESS]</td>
												</tr>
												<%
													}
												%>
												<%
													if (mybean.status.equals("Enquiry") || mybean.status.equals("Brochure")){%>
													<tr>
													<td align="right">Branch Email1:</td>
													<td align="left">[BRANCHEMAIL1]</td>
												</tr>
													<%} %>
												
												<%
													if (mybean.status.equals("Quote") || mybean.status.equals("Quote for Executive")) {
												%>
												<tr>
													<td align="right">Quote ID:</td>
													<td align="left">[QUOTEID]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Branch Address:</td>
													<td align="left">[BRANCHADDRESS]</td>
												</tr>
												<tr>
													<td align="right">Branch Mobile1:</td>
													<td align="left">[BRANCHMOBILE1]</td>
												</tr>
												<%
													}
												%>
												<%
													if (mybean.status.equals("Sales Order") || mybean.status.equals("Sales Order for Executive")) {
												%>
												<tr>
													<td align="right">Sales Order ID:</td>
													<td align="left">[SOID]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												<tr>
													<td align="right">Branch Address:</td>
													<td align="left">[BRANCHADDRESS]</td>
												</tr>
												<%
													}
												%>
												<%
													if (mybean.status.equals("Sales Order Delivered")) {
												%>
												<tr>
													<td align="right">Sales Order ID:</td>
													<td align="left">[SODID]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Item Name:</td>
													<td align="left">[ITEMNAME]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Delivered Date:</td>
													<td align="left">[DELIVEREDDATE]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												<tr>
													<td align="right">Branch Address:</td>
													<td align="left">[BRANCHADDRESS]</td>
												</tr>
												<%
													}
												%>
												
												<%
													if (mybean.status.equals("New Job Card") || mybean.status.equals("Ready Job Card") || mybean.status.equals("Delivered Job Card") || mybean.status.equals("Job Card Estimate")
															|| mybean.status.equals("Job Card Feedback")) {
												%>
												<tr>
													<td align="right">Job Card ID:</td>
													<td align="left">[JOBCARDID]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Item Name:</td>
													<td align="left">[ITEMNAME]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Reg. No.:</td>
													<td align="left">[REGNO]</td>
												</tr>
												<tr>
													<td align="right">Branch Address:</td>
													<td align="left">[BRANCHADDRESS]</td>
												</tr>
												<%
													}
												%>
												
												<% if (mybean.status.equals("Not Contactable") || mybean.status.equals("Daily Due")
													|| mybean.status.equals("Booking") || mybean.status.equals("Booking For Executive")
													|| mybean.status.equals("Serviced") ) { %>
												<tr>
													<td align="right">Vehicle ID:</td>
													<td align="left">[VEHID]</td>
												</tr>
												<tr>
													<td align="right">Reg. No.:</td>
													<td align="left">[REGNO]</td>
												</tr>
												<tr>
													<td align="right">Model:</td>
													<td align="left">[MODEL]</td>
												</tr>
												<tr>
													<td align="right">Variant:</td>
													<td align="left">[VARIANT]</td>
												</tr>
												<%
													if (mybean.status.equals("Not Contactable") || mybean.status.equals("Daily Due")){ %>
												<tr>
													<td align="right">Service Due Date:</td>
													<td align="left">[SERVICEDUEDATE]</td>
												</tr>
												<%} %>
												
												<%
													if (mybean.status.equals("Booking")){ %>
												<tr>
													<td align="right">Service Type(Call Type):</td>
													<td align="left">[SERVICETYPE]</td>
												</tr>
												<%} %>
												<% if (mybean.status.equals("Booking") || mybean.status.equals("Serviced")){ %>
												<tr>
													<td align="right">Booking Time:</td>
													<td align="left">[BOOKINGTIME]</td>
												</tr>
												<%} %>
												<tr>
													<td align="right">CRM Executive:</td>
													<td align="left">[CRMEXE]</td>
												</tr>
												<tr>
													<td align="right">CRM Executive Jobtitle:</td>
													<td align="left">[CRMEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">CRM Executive Mobile1</td>
													<td align="left">[CRMMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">CRM Executive Email1:</td>
													<td align="left">[CRMEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												
												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												<tr>
													<td align="right">Branch Phone:</td>
													<td align="left">[BRANCHPHONE]</td>
												</tr>
												<tr>
													<td align="right">Branch Mobile1:</td>
													<td align="left">[BRANCHMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Branch Email1:</td>
													<td align="left">[BRANCHEMAIL1]</td>
												</tr>
												
												<% } %>
												
												<% if (mybean.status.equals("Insurance") || mybean.status.equals("Insurance For Executive")){%>
												<tr>
													<td align="right">Enquiry ID:</td>
													<td align="left">[ENQUIRYID]</td>
												</tr>
												<tr>
													<td align="right">Reg. No.:</td>
													<td align="left">[REGNO]</td>
												</tr>
												<tr>
													<td align="right">Model:</td>
													<td align="left">[MODEL]</td>
												</tr>
												<tr>
													<td align="right">Variant:</td>
													<td align="left">[VARIANT]</td>
												</tr>
												<tr>
													<td align="right">Ref. Executive Name:</td>
													<td align="left">[REFEXENAME]</td>
												</tr>
												<tr>
													<td align="right">Ref. Executive Job Title:</td>
													<td align="left">[REFEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Ref. Executive Mobile1:</td>
													<td align="left">[REFEXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Ref. Executive Email1:</td>
													<td align="left">[REFEXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Insurance Executive Name:</td>
													<td align="left">[INSUREXENAME]</td>
												</tr>
												<tr>
													<td align="right">Insurance Executive Job Title:</td>
													<td align="left">[INSUREXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Insurance Executive Mobile1:</td>
													<td align="left">[INSUREXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Insurance Executive Email1:</td>
													<td align="left">[INSUREXEMAIL1]</td>
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
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												<tr>
													<td align="right">Branch Phone:</td>
													<td align="left">[BRANCHPHONE]</td>
												</tr>
												<tr>
													<td align="right">Branch Mobile1:</td>
													<td align="left">[BRANCHMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Branch Email1:</td>
													<td align="left">[BRANCHEMAIL1]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("JCPSF Satisfied SMS") || mybean.status.equals("JCPSF Dis-Satisfied SMS")) { %>
												<tr>
													<td align="right">Ticket ID:</td>
													<td align="left">[TICKETID]</td>
												</tr>
												<tr>
													<td align="right">Ticket Subject:</td>
													<td align="left">[TICKETSUBJECT]</td>
												</tr>
												<tr>
													<td align="right">Ticket Description:</td>
													<td align="left">[TICKETDESCRIPTION]</td>
												</tr>
												<tr>
													<td align="right">Ticket Status:</td>
													<td align="left">[TICKETSTATUS]</td>
												</tr>
												<tr>
													<td align="right">Ticket Time:</td>
													<td align="left">[TICKETTIME]</td>
												</tr>
												<tr>
													<td align="right">Ticket Category:</td>
													<td align="left">[TICKETCAT]</td>
												</tr>
												<tr>
													<td align="right">Ticket Type:</td>
													<td align="left">[TICKETTYPE]</td>
												</tr>
												<tr>
													<td align="right">Due Time:</td>
													<td align="left">[DUETIME]</td>
												</tr>
												<tr>
													<td align="right">Enquiry ID:</td>
													<td align="left">[ENQUIRYID]</td>
												</tr>
												<tr>
													<td align="right">Enquiry Date:</td>
													<td align="left">[ENQUIRYDATE]</td>
												</tr>
												<tr>
													<td align="right">SO Date:</td>
													<td align="left">[SODATE]</td>
												</tr>
												<tr>
													<td align="right">Delivery Date:</td>
													<td align="left">[DELIVERYDATE]</td>
												</tr>
												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												<tr>
													<td align="right">Team Lead:</td>
													<td align="left">[TEAMLEAD]</td>
												</tr>
												<tr>
													<td align="right">Sales Executive:</td>
													<td align="left">[SALESEXE]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile2:</td>
													<td align="left">[EXEMOBILE2]</td>
												</tr>
												<tr>
													<td align="right">Executive E-Mail1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive E-Mail2:</td>
													<td align="left">[EXEEMAIL2]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Registration Number:</td>
													<td align="left">[REGNO]</td>
												</tr>
												<tr>
													<td align="right">Department:</td>
													<td align="left">[DEPARTMENT]</td>
												</tr>
												<tr>
													<td align="right">Priority:</td>
													<td align="left">[PRIORITY]</td>
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
													<td align="right">Customer ID:</td>
													<td align="left">[CUSTOMERID]</td>
												</tr>
												<tr>
													<td align="right">Customer Name:</td>
													<td align="left">[CUSTOMERNAME]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("JCPSF Dis-Satisfied") || mybean.status.equals("JCPSF Satisfied")) { %>
												<tr>
													<td align="right">Ticket ID:</td>
													<td align="left">[TICKETID]</td>
												</tr>
												<tr>
													<td align="right">Ticket Subject:</td>
													<td align="left">[TICKETSUBJECT]</td>
												</tr>
												<tr>
													<td align="right">Ticket Description:</td>
													<td align="left">[TICKETDESCRIPTION]</td>
												</tr>
												<tr>
													<td align="right">Ticket Status:</td>
													<td align="left">[TICKETSTATUS]</td>
												</tr>
												<tr>
													<td align="right">Ticket Time:</td>
													<td align="left">[TICKETTIME]</td>
												</tr>
												<tr>
													<td align="right">Ticket Category:</td>
													<td align="left">[TICKETCAT]</td>
												</tr>
												<tr>
													<td align="right">Ticket Type:</td>
													<td align="left">[TICKETTYPE]</td>
												</tr>
												<tr>
													<td align="right">Due Time:</td>
													<td align="left">[DUETIME]</td>
												</tr>
												<tr>
													<td align="right">Department:</td>
													<td align="left">[DEPARTMENT]</td>
												</tr>
												<tr>
													<td align="right">Priority:</td>
													<td align="left">[PRIORITY]</td>
												</tr>
												<tr>
													<td align="right">Enquiry ID:</td>
													<td align="left">[ENQUIRYID]</td>
												</tr>
												<tr>
													<td align="right">Enquiry Date:</td>
													<td align="left">[ENQUIRYDATE]</td>
												</tr>
												<tr>
													<td align="right">SO Date:</td>
													<td align="left">[SODATE]</td>
												</tr>
												<tr>
													<td align="right">Delivery Date:</td>
													<td align="left">[DELIVERYDATE]</td>
												</tr>
												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												<tr>
													<td align="right">Team Lead:</td>
													<td align="left">[TEAMLEAD]</td>
												</tr>
												<tr>
													<td align="right">Sales Executive:</td>
													<td align="left">[SALESEXE]</td>
												</tr>
												
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile2:</td>
													<td align="left">[EXEMOBILE2]</td>
												</tr>
												<tr>
													<td align="right">Executive E-Mail1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive E-Mail2:</td>
													<td align="left">[EXEEMAIL2]</td>
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
													<td align="right">Customer ID:</td>
													<td align="left">[CUSTOMERID]</td>
												</tr>
												<tr>
													<td align="right">Customer Name:</td>
													<td align="left">[CUSTOMERNAME]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Registration Number:</td>
													<td align="left">[REGNO]</td>
												</tr>
												<% } %>
												
<!-- 												SO CIN Starts & CIN Exe Starts -->
											<% if (mybean.status.equals("SoCin") || mybean.status.equals("SoCinExe")) { %>
												<tr>
													<td align="right">Sales Order ID:</td>
													<td align="left">[SOID]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("Din")) { %>
												<tr>
													<td align="right">So ID:</td>
													<td align="left">[SOID]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Customer DOB:</td>
													<td align="left">[CUSTOMERDOB]</td>
												</tr>
												<tr>
													<td align="right">Customer Anniversary:</td>
													<td align="left">[CUSTOMERANNIVERSARY]</td>
												</tr>
												<tr>
													<td align="right">Branch Name:</td>
													<td align="left">[BRANCHNAME]</td>
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
													<td align="right">Color:</td>
													<td align="left">[COLOR]</td>
												</tr>
												<tr>
													<td align="right">Delivery Location:</td>
													<td align="left">[DELIVERYLOCATION]</td>
												</tr>
												<tr>
													<td align="right">Chassis Number:</td>
													<td align="left">[CHASSISNUMBER]</td>
												</tr>
												<tr>
													<td align="right">Engine Number:</td>
													<td align="left">[ENGINENUMBER]</td>
												</tr>
												<tr>
													<td align="right">Free:</td>
													<td align="left">[FREE]</td>
												</tr>
												<tr>
													<td align="right">Paid:</td>
													<td align="left">[PAID]</td>
												</tr>
												<tr>
													<td align="right">Star Packages:</td>
													<td align="left">[STARPACKAGES]</td>
												</tr>
												<tr>
													<td align="right">Promised Time:</td>
													<td align="left">[PROMISEDTIME]</td>
												</tr>
												<tr>
													<td align="right">Sales Person:</td>
													<td align="left">[SALESPERSON]</td>
												</tr>
												<tr>
													<td align="right">Special Occasion:</td>
													<td align="left">[SPECIALOCCASION]</td>
												</tr>
												<tr>
													<td align="right">Registration Mode:</td>
													<td align="left">[REGISTRATIONMODE]</td>
												</tr>
												<tr>
													<td align="right">Registration Number:</td>
													<td align="left">[REGISTRATIONNUMBER]</td>
												</tr>
												<tr>
													<td align="right">Promised Delivery Date:</td>
													<td align="left">[PROMISEDDELIVERYDATE]</td>
												</tr>
												<% } %>
												
												<% if (mybean.status.equals("Quote") || mybean.status.equals("Quote for Executive") 
													|| mybean.status.equals("Quote for Discount Authorize")) { %>
												<tr>
													<td align="right">Quote ID:</td>
													<td align="left">[QUOTEID]</td>
												</tr>
												<tr>
													<td align="right">Requested Discount Amount:</td>
													<td align="left">[DISCOUNTAMOUNT]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Authorize Executive Name:</td>
													<td align="left">[AUTHORIZEEXENAME]</td>
												</tr>
												<tr>
													<td align="right">Authorize Executive Job Title:</td>
													<td align="left">[AUTHORIZEEXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Authorize Executive Mobile1:</td>
													<td align="left">[AUTHORIZEEXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Authorize Executive Phone1:</td>
													<td align="left">[AUTHORIZEEXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Authorize Executive Email1:</td>
													<td align="left">[AUTHORIZEEXEEMAIL1]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("Sales Order") || mybean.status.equals("Sales Order for Executive")) { %>
												<tr>
													<td align="right">Sales Order ID:</td>
													<td align="left">[SOID]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("Sales Order Delivered")) { %>
												<tr>
													<td align="right">Sales Order ID:</td>
													<td align="left">[SODID]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Delivered Date:</td>
													<td align="left">[DELIVEREDDATE]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<% } %>

												<% if (mybean.status.equals("Service Due")) { %>
												<tr>
													<td align="right">Customer ID:</td>
													<td align="left">[CUSTOMERID]</td>
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
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Item Name:</td>
													<td align="left">[ITEMNAME]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Reg. No.:</td>
													<td align="left">[REGNO]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("Service Booking") || mybean.status.equals("Service Due Day")
														|| mybean.status.equals("Booking Day") || mybean.status.equals("Booking Day Advance")) { %>
												<tr>
													<td align="right">Call ID:</td>
													<td align="left">[CALLID]</td>
												</tr>
												<tr>
													<td align="right">Booking Time:</td>
													<td align="left">[APPOINMENTTIME]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Item Name:</td>
													<td align="left">[ITEMNAME]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Reg. No.:</td>
													<td align="left">[REGNO]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("New Job Card") || mybean.status.equals("Ready Job Card")
														|| mybean.status.equals("Delivered Job Card") || mybean.status.equals("Job Card Estimate")
															|| mybean.status.equals("Job Card Feedback")) { %>
												<tr>
													<td align="right">Job Card ID:</td>
													<td align="left">[JOBCARDID]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Item Name:</td>
													<td align="left">[ITEMNAME]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Reg. No.:</td>
													<td align="left">[REGNO]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("Invoice Confirmation")) { %>
												<tr>
													<td align="right">Invoice ID:</td>
													<td align="left">[INVOICEID]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<% } %>

												<!--    Insurance New -->
												<% if (mybean.status.equals("Insurance New")) { %>
												<tr>
													<td align="right">Insurance ID:</td>
													<td align="left">[INSURID]</td>
												</tr>
												<tr>
													<td align="right">Insurance Start Date:</td>
													<td align="left">[INSURSTRDATE]</td>
												</tr>
												<tr>
													<td align="right">Insurance End Date:</td>
													<td align="left">[INSURENDDATE]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>

												<tr>
													<td align="right">Insurance Executive Name:</td>
													<td align="left">[INSUREXENAME]</td>
												</tr>

												<tr>
													<td align="right">Insurance Executive Job Title:</td>
													<td align="left">[INSUREXEJOBTITLE]</td>
												</tr>

												<tr>
													<td align="right">Insurance Executive Mobile1:</td>
													<td align="left">[INSUREXEMOBILE1]</td>
												</tr>

												<tr>
													<td align="right">Insurance Executive Phone1:</td>
													<td align="left">[INSUREXEPHONE1]</td>
												</tr>

												<tr>
													<td align="right">Insurance Executive Email1:</td>
													<td align="left">[INSUREXEEMAIL1]</td>
												</tr>

												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Item Name:</td>
													<td align="left">[ITEMNAME]</td>
												</tr>
												
												<% } %>
												<!-- ----Insurance Lost -->

												<% if (mybean.status.equals("Insurance Lost")) { %>
												<!--  <tr>
	                                                    <td align="right">Insurance ID:</td>
	                                                    <td align="left">[INSURID]</td>
                                                 	</tr> -->
												<tr>
													<td align="right">Customer ID:</td>
													<td align="left">[CUSTOMERID]</td>
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
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>

												<tr>
													<td align="right">Insurance Executive Name:</td>
													<td align="left">[INSUREXENAME]</td>
												</tr>

												<tr>
													<td align="right">Insurance Executive Job Title:</td>
													<td align="left">[INSUREXEJOBTITLE]</td>
												</tr>

												<tr>
													<td align="right">Insurance Executive Mobile1:</td>
													<td align="left">[INSUREXEMOBILE1]</td>
												</tr>

												<tr>
													<td align="right">Insurance Executive Phone1:</td>
													<td align="left">[INSUREXEPHONE1]</td>
												</tr>

												<tr>
													<td align="right">Insurance Executive Email1:</td>
													<td align="left">[INSUREXEEMAIL1]</td>
												</tr>


												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Item Name:</td>
													<td align="left">[ITEMNAME]</td>
												</tr>
												<% } %>


												<%
												%>
												<% if (mybean.status.equals("Bill")) { %>
												<tr>
													<td align="right">Bill ID:</td>
													<td align="left">[BILLID]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("Balance Due")) { %>
												<tr>
													<td align="right">Balance Due Date:</td>
													<td align="left">[BALANCEDUEDATE]</td>
												</tr>
												<tr>
													<td align="right">Sales Order ID:</td>
													<td align="left">[SOID]</td>
												</tr>
												<tr>
													<td align="right">Sales Order Date:</td>
													<td align="left">[SODATE]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("Balance Overdue")) { %>
												<tr>
													<td align="right">Balance Overdue Date:</td>
													<td align="left">[BALANCEOVERDUEDATE]</td>
												</tr>
												<tr>
													<td align="right">Sales Order ID:</td>
													<td align="left">[SOID]</td>
												</tr>
												<tr>
													<td align="right">Sales Order Date:</td>
													<td align="left">[SODATE]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Phone1:</td>
													<td align="left">[CONTACTPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Job Title:</td>
													<td align="left">[EXEJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<% } %>
												
												<% if (mybean.status.equals("Receipt")) { %>
												<tr>
													<td align="right">Receipt ID:</td>
													<td align="left">[RECEIPTID]</td>
												</tr>
												<tr>
													<td align="right">Voucher NO:</td>
													<td align="left">[VOUCHERNO]</td>
												</tr>
												<tr>
													<td align="right">Invoice ID:</td>
													<td align="left">[INVOICEID]</td>
												</tr>
												<tr>
													<td align="right">SO ID:</td>
													<td align="left">[SOID]</td>
												</tr>
												<tr>
													<td align="right">SO Date:</td>
													<td align="left">[SODATE]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Voucher Amount:</td>
													<td align="left">[AMOUNT]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Branch Phone1:</td>
													<td align="left">[BRANCHPHONE1]</td>
												</tr>
												
												<tr>
													<td align="right">Branch Mobile1:</td>
													<td align="left">[BRANCHMOBILE1]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("Receipt Authorize")) { %>
												<tr>
													<td align="right">Receipt ID:</td>
													<td align="left">[RECEIPTID]</td>
												</tr>
												<tr>
													<td align="right">Voucher NO:</td>
													<td align="left">[VOUCHERNO]</td>
												</tr>
												<tr>
													<td align="right">Invoice ID:</td>
													<td align="left">[INVOICEID]</td>
												</tr>
												<tr>
													<td align="right">SO ID:</td>
													<td align="left">[SOID]</td>
												</tr>
												<tr>
													<td align="right">SO Date:</td>
													<td align="left">[SODATE]</td>
												</tr>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
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
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Payment Type:</td>
													<td align="left">[PAYMENTTYPE]</td>
												</tr>
												<tr>
													<td align="right">Voucher Amount:</td>
													<td align="left">[AMOUNT]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Phone1:</td>
													<td align="left">[EXEPHONE1]</td>
												</tr>
												<tr>
													<td align="right">Executive Email1:</td>
													<td align="left">[EXEEMAIL1]</td>
												</tr>
												
												<tr>
													<td align="right">Branch Phone1:</td>
													<td align="left">[BRANCHPHONE1]</td>
												</tr>
												
												<tr>
													<td align="right">Branch Mobile1:</td>
													<td align="left">[BRANCHMOBILE1]</td>
												</tr>
												
												<% } %>
												<% if (mybean.status.equals("Test Drive") || mybean.status.equals("Test Drive for Executive")
														|| mybean.status.equals("Test Drive Feedback") || mybean.status.equals("Test Drive Feedback for Executive")) { %>
												<tr>
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Item Name:</td>
													<td align="left">[ITEMNAME]</td>
												</tr>
												<tr>
													<td align="right">Date:</td>
													<td align="left">[DATE]</td>
												</tr>
												<tr>
													<td align="right">Time:</td>
													<td align="left">[TIME]</td>
												</tr>
												<tr>
													<td align="right">Executive Name:</td>
													<td align="left">[EXENAME]</td>
												</tr>
												<tr>
													<td align="right">Executive Mobile1:</td>
													<td align="left">[EXEMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Location:</td>
													<td align="left">[LOCATION]</td>
												</tr>
												<% } %>
												<% if (mybean.status.equals("Enquiry") || mybean.status.equals("Enquiry for Executive")
														|| mybean.status.equals("Brochure") || mybean.status.equals("Quote")
														|| mybean.status.equals("Quote for Executive") || mybean.status.equals("Sales Order")
														|| mybean.status.equals("Sales Order for Executive")) { %>
												<tr>
													<td align="right">Model Name:</td>
													<td align="left">[MODELNAME]</td>
												</tr>
												<tr>
													<td align="right">Item Name:</td>
													<td align="left">[ITEMNAME]</td>
												</tr>
												<% if (mybean.status.equals("Enquiry for Executive") && mybean.sms.equals("yes")) { %>
												<tr>
													<td align="right">SOE:</td>
													<td align="left">[SOE]</td>
												</tr>
												<% } %>
												
												<% } %>
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
	<%@include file="../Library/js.jsp"%>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_format.focus();
	}
</script>
</body>
</HTML>
