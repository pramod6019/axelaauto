<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Branch_Format" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta content="width=device-width, initial-scale=1" name="viewport" />

<%@include file="../Library/css.jsp"%>

<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />

</head>
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
						<h1>Branch Format</h1>
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
							<li><a href="branch.jsp">Branch</a> &gt;</li>
							<li><a href="branch-list.jsp?all=yes">List Branches</a> &gt;</li>
							<li><a href="branch-format.jsp?<%=mybean.QueryString%>"> Format For <%=mybean.status%></a> <b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Format For <%=mybean.title%>
									</div>
								</div>
								
								<div class="container-fluid portlet-body">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
										
											<%
												if (mybean.email.equals("yes")) {
											%>
											
											<div class="form-element6 form-element-center">
												<label>Subject:</label> <input name="txt_subject"
													type="text" class="form-control" id="txt_subject"
													value="<%=mybean.subject%>" size="75" maxlength="1000">
											</div>
											
											<%
												}
											%>

											<div class="form-element6 form-element-center">
												<label></label> <br>
												<%
													if (mybean.email.equals("yes")) {
												%>
												<textarea name="txt_format" cols="70" rows="5"
													class="form-control summernote_1" id="txt_format"
													<%if (mybean.sms.equals("yes")) {%>
													onKeyUp="charcount('txt_format', 'span_txt_format', '<font color=red>({CHAR} characters left)</font>', '1000')"
													<%}%>><%=mybean.format_desc%> </textarea>

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

												<% } else { %>

												<textarea name="txt_format" cols="70" rows="5"
													class="form-control" id="txt_format"
													<%if (mybean.sms.equals("yes")) {%>
													onKeyUp="charcount('txt_format', 'span_txt_format', '<font color=red>({CHAR} characters left)</font>', '1000')"
													<%}%>><%=mybean.format_desc%></textarea>
												<span id="span_txt_format"> 1000 characters </span>

												<% } %>
											</div>

											<center>
												<input name="update_button" type="submit"
													class="btn btn-success" id="update_button" value="Update" />
											</center>
											
											<br>
											<div class="form-element12">
												<div class="form-element8 form-element-center">
													<table class="table table-hover table-bordered">
														<thead>
															<tr>
																<th colspan="2" align="center">Substitution
																	Variables</th>
															</tr>
														</thead>
														<%-- <%
													if (mybean.status.equals("Lead") || mybean.status.equals("Lead for Executive")) {
												%>
												<tr>
													<td align="right">Lead ID:</td>
													<td align="left">[LEADID]</td>
												</tr>
												<tr>
													<td align="right">Lead Company:</td>
													<td align="left">[LEADCOMPANY]</td>
												</tr>
												<tr>
													<td align="right">Lead Contact Name:</td>
													<td align="left">[LEADCONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Lead Job Title:</td>
													<td align="left">[LEADJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Lead Mobile:</td>
													<td align="left">[LEADMOBILE]</td>
												</tr>
												<tr>
													<td align="right">Lead Phone:</td>
													<td align="left">[LEADPHONE]</td>
												</tr>
												<tr>
													<td align="right">Lead Email:</td>
													<td align="left">[LEADEMAIL]</td>
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
												<%
													}
												%> --%>
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
														<%
															}
														%>
														<%
															if (mybean.status.equals("Enquiry") || mybean.status.equals("Brochure")) {
														%>
														<tr>
															<td align="right">Branch Email1:</td>
															<td align="left">[BRANCHEMAIL1]</td>
														</tr>
														<%
															}
														%>

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
														<%
															}
														%>
														<%
															if (mybean.status.equals("Sales Order Delivered")) {
														%>
														<tr>
															<td align="right">Sales Order Delivered ID:</td>
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
														<%
															}
														%>

														<%
															if (mybean.status.equals("Service Due")) {
														%>
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
														<%
															}
														%>
														<%
															if (mybean.status.equals("Service Booking")) {
														%>
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
														<%
															}
														%>
														<%
															if (mybean.status.equals("Invoice Confirmation")) {
														%>
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
														<%
															}
														%>

														<!--    Insurance New -->
														<%
															if (mybean.status.equals("Insurance New")) {
														%>
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
														<%
															}
														%>
														<!-- ----Insurance Lost -->

														<%
															if (mybean.status.equals("Insurance Lost")) {
														%>
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
														<%
															}
														%>


														<%
															if (mybean.status.equals("Receipt Confirmation")) {
														%>
														<tr>
															<td align="right">Receipt ID:</td>
															<td align="left">[RECEIPTID]</td>
														</tr>
														<tr>
															<td align="right">Invoice ID:</td>
															<td align="left">[INVOICEID]</td>
														</tr>
														<tr>
															<td align="right">Invoice Date:</td>
															<td align="left">[INVOICEDATE]</td>
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
														<%
															}
														%>
														<%
															if (mybean.status.equals("Bill")) {
														%>
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
														<%
															}
														%>
														<%
															if (mybean.status.equals("Balance Due")) {
														%>
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
														<%
															}
														%>
														<%
															if (mybean.status.equals("Balance Overdue")) {
														%>
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
														<%
															}
														%>

														<%
															if (mybean.status.equals("Test Drive") || mybean.status.equals("Test Drive for Executive")) {
														%>
														<tr>
															<td align="right">Contact Name:</td>
															<td align="left">[CONTACTNAME]</td>
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
														<%
															}
														%>
														<%
															if (mybean.status.equals("Enquiry") || mybean.status.equals("Enquiry for Executive") || mybean.status.equals("Brochure") || mybean.status.equals("Quote")
																	|| mybean.status.equals("Quote for Executive") || mybean.status.equals("Sales Order") || mybean.status.equals("Sales Order for Executive")) {
														%>
														<tr>
															<td align="right">Model Name:</td>
															<td align="left">[MODELNAME]</td>
														</tr>
														<tr>
															<td align="right">Item Name:</td>
															<td align="left">[ITEMNAME]</td>
														</tr>
														<%
													if (mybean.status.equals("Enquiry for Executive") && mybean.sms.equals("yes")) {
												%>
														<tr>
															<td align="right">SOE:</td>
															<td align="left">[SOE]</td>
														</tr>
														<%
													}
												%>
														<%
													}
												%>
														<tr>
															<td width="50%" align="right">&nbsp;</td>
															<td width="50%" align="left">&nbsp;</td>
														</tr>
													</table>
												</div>
											</div>
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
	<script src="../assets/js/components-editors.min.js"
		type="text/javascript"></script>

	<!-- <script type="text/javascript" src="../ckeditor/ckeditor.js"></script>    -->

	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_format.focus();
	}
</script>
</body>
</html>
