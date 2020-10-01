<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.ManageBrandConfig_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<%@include file="../Library/css.jsp"%>
</head>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>&nbsp;Brand Config
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
							<li><a href="managebrandconfig-list.jsp?all=yes">List
									Brand Config</a> &gt;</li>
							<li><a
								href="managebrandconfig-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Brand
									Config</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">
									<%=mybean.status%>
									Brand Config
								</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="container-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal ">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.
												</font>
											</center>
											<!-- START PORTLET BODY -->
											<div class="form-element6">
												<label> Brand<font color="#ff0000">*</font>:
												</label> <select name="dr_brand_id" class="form-control"
													id="dr_brand_id">
													<%=mybean.PopulateBrand(mybean.comp_id)%>
												</select>
											</div>
											<div class="form-element6 form-element-margin">
												<label>Enable SMS:</label> <input type="checkbox"
													name="chk_brandconfig_sms_enable"
													<%=mybean.PopulateCheck(mybean.brandconfig_sms_enable)%>>
											</div>
											<div class="form-element12 form-element">
											<div class="form-element6 form-element">
												<div class="form-element12" id="sms_url">
													<label>SMS URL:</label>
													<textarea name="txt_brandconfig_sms_url" cols="70" rows="6"
														class="form-control" id="txt_brandconfig_sms_url"><%=mybean.brandconfig_sms_url%></textarea>
												</div>
											</div>
											<div class="form-element6 form-element">
												<div class="form-element12 form-element-margin">
													<label> Stock Preference: </label> <select
														name="dr_stockfifo_id" class="form-control"
														id="dr_stockfifo_id">
														<%=mybean.PopulateStockPreference(mybean.brandconfig_allocatestock_fifo, mybean.comp_id)%>
													</select>
												</div>
												<div class="form-element12">
												<label> Executive Stock View: </label> <select
													name="dr_exestockview_id" class="form-control"
													id="dr_exestockview_id">
													<%=mybean.PopulateExeStockView(mybean.brandconfig_exestockview, mybean.comp_id)%>
												</select>
											</div>
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label>Principal: </label>
													<select class="form-control select2"
													id="accountingcustomer" name="accountingcustomer">
												<%=mybean.ledgercheck.PopulateLedgers("31",mybean.brandconfig_principal_id, mybean.comp_id)%>
												</select>
												</div>
											<div class="form-element5">
												<label> Close Enquiry After: </label> <input
													name="txt_close_enquiry_after_days" type="text"
													class="form-control" id="txt_close_enquiry_after_days"
													value="<%=mybean.brandconfig_close_enquiry_after_days%>"
													onKeyUp="toInteger('txt_close_enquiry_after_days','Close Enquiry After')"
													size="20" maxlength="3"> </input> 
											</div>
											<div class="form-element1" style="top: 28px;">Days</div>
											<div class="row"></div>

											<div class="form-element6">
												<label> De-Allocate Stock: </label> <input type="checkbox"
													name="chk_brandconfig_deallocatestock_enable"
													id="chk_brandconfig_deallocatestock_enable"
													<%=mybean.PopulateCheck(mybean.brandconfig_deallocatestock_enable)%> />
											</div>
											<div class="form-element6">
												<label> De-Allocate Stock Days: </label> <input
													name="txt_brandconfig_deallocatestock_days" type="text"
													class="form-control"
													id="txt_brandconfig_deallocatestock_days"
													value="<%=mybean.brandconfig_deallocatestock_days%>"
													onKeyUp="toInteger('txt_brandconfig_deallocatestock_days','Deallocate Stock Days')"
													size="20" maxlength="3" />

											</div>
											
												<div class="form-element5">
													<label> Amount Percentage: </label> <input
														name="txt_brandconfig_deallocatestock_amountperc"
														type="text" class="form-control"
														id="txt_brandconfig_deallocatestock_amountperc"
														value="<%=mybean.brandconfig_deallocatestock_amountperc%>"
														onKeyUp="toInteger('txt_brandconfig_deallocatestock_amountperc','Amount')"
														size="20" maxlength="3"></input> 
												</div>
												<div class="form-element1" style="top: 28px;">%</div>
												<div class="form-element6 form-element-margin">
													<label> No Show: </label> <input type="checkbox"
														name="chk_brandconfig_noshow_enable"
														id="chk_brandconfig_noshow_enable"
														<%=mybean.PopulateCheck(mybean.brandconfig_noshow_enable)%> />
												</div>
												<div class="row"></div>
												<div class="form-element6">
												<label> No Show Days: </label> <input
													name="txt_brandconfig_noshow_days" type="text"
													class="form-control" id="txt_brandconfig_noshow_days"
													value="<%=mybean.brandconfig_noshow_days%>"
													onKeyUp="toInteger('txt_brandconfig_noshow_days','No Show Days')"
													size="20" maxlength="3" />
												</div>
												<div class="form-element6">
												<label> No Show Future Days: </label> <input
													name="txt_brandconfig_noshow_future_days" type="text"
													class="form-control"
													id="txt_brandconfig_noshow_future_days"
													value="<%=mybean.brandconfig_noshow_future_days%>"
													onKeyUp="toInteger('txt_brandconfig_noshow_future_days','No Show Future Days')"
													size="20" maxlength="3" />
												</div>
												
												<div class="form-element6">
													<label> Vehicle Follow-up Days: </label> <input
														name="txt_brandconfig_vehfollowup_days"
														type="text" class="form-control"
														id="txt_brandconfig_vehfollowup_days"
														value="<%=mybean.brandconfig_vehfollowup_days%>"
														onKeyUp="toInteger('txt_brandconfig_vehfollowup_days','Follow-up Days')"
														size="20" maxlength="3"></input> 
												</div>
											
											<div class="form-element6">
												<label> Notes: </label>
												<textarea name="txt_brandconfig_notes" cols="70" rows="4"
													class="form-control" id="txt_brandconfig_notes"> <%=mybean.brandconfig_notes%></textarea>
											</div>
											<div class="form-element6 form-element-margin">
													<label>Incentive: </label> <input type="checkbox"
														name="chk_brandconfig_incentive"
														id="chk_brandconfig_incentive"
														<%=mybean.PopulateCheck(mybean.brandconfig_incentive)%> />
												</div>
												
											<div class="form-element6 form-element-margin">
													<label>Discount Authorize: </label> <input type="checkbox"
														name="chk_brandconfig_discountauthorize"
														id="chk_brandconfig_discountauthorize"
														<%=mybean.PopulateCheck(mybean.brandconfig_discountauthorize)%> />
												</div>
											
											<% if (mybean.status.equals("Update")) { %>
											<div class="form-element12">
												<div class="form-element2"></div>
												<div class="form-element8">
												<label> Automated Tasks: </label>
												<table
													class="table table-responsive table-hover table-bordered">
													<thead>
														<tr>
															<th>Task Type</th>
															<th>Send Email</th>
															<th>Email Format</th>
															<th>Send SMS</th>
															<th>SMS Format</th>
														</tr>
													</thead>
													<tbody>
													
													<!-- New Fields Start -->
													
													<tr>
														<td align="left">Enquiry:</td>
														<td align="center">
															<input name="chk_brandconfig_enquiry_email_enable" type="checkbox" id="chk_brandconfig_enquiry_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_enquiry_email_enable)%>>
														</td>
														<td align="center">
															<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Enquiry&opt=brandconfig_enquiry_email_format">Format</a>
														</td>
														<td align="center">
															<input type="checkbox" name="chk_brandconfig_enquiry_sms_enable" <%=mybean.PopulateCheck(mybean.brandconfig_enquiry_sms_enable)%>>
														</td>
														<td align="center">
														<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Enquiry&opt=brandconfig_enquiry_sms_format">Format</a>
														</td>
													</tr>
													
													<tr>
														<td align="left">Enquiry for Executive:</td>
														<td align="center">
															<input type="checkbox" name="chk_brandconfig_enquiry_email_exe_enable" <%=mybean.PopulateCheck(mybean.brandconfig_enquiry_email_exe_enable)%>>
														</td>
														<td align="center">
															<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Enquiry for Executive&opt=brandconfig_enquiry_email_exe_format">Format</a>
														</td>
														<td align="center">
															<input type="checkbox" name="chk_brandconfig_enquiry_sms_exe_enable" <%=mybean.PopulateCheck(mybean.brandconfig_enquiry_sms_exe_enable)%>>
														</td>
														<td align="center">
														<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Enquiry for Executive&opt=brandconfig_enquiry_sms_exe_format">Format</a>
														</td>
													</tr>
													
													<tr>
														<td align="left">Brochure:</td>
														<td align="center"><input type="checkbox"
															name="chk_brandconfig_enquiry_brochure_email_enable" id="chk_brandconfig_enquiry_brochure_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_enquiry_brochure_email_enable)%>></td>
														<td align="center">
														<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Brochure&opt=brandconfig_enquiry_brochure_email_format">Format</a>
														</td>
														<td align="center">&nbsp;</td>
														<td align="center">&nbsp;</td>
													</tr>
													
													<tr>
														<td align="left">Test Drive:</td>
														<td align="center"><input type="checkbox"
															name="chk_brandconfig_testdrive_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_testdrive_email_enable)%>></td>
														<td align="center">
															<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Test Drive&opt=brandconfig_testdrive_email_format">Format</a></td>
														<td align="center"><input type="checkbox"
															name="chk_brandconfig_testdrive_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_testdrive_sms_enable)%>></td>
														<td align="center">
															<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Test Drive&opt=brandconfig_testdrive_sms_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Test Drive for Executive:</td>
														<td align="center">
														<input type="checkbox" name="chk_brandconfig_testdrive_email_exe_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_testdrive_email_exe_enable)%>>
															</td>
														<td align="center">
															<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Test Drive for Executive&opt=brandconfig_testdrive_email_exe_format">Format</a></td>
														<td align="center">
														<input type="checkbox" name="chk_brandconfig_testdrive_sms_exe_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_testdrive_sms_exe_enable)%>>
															</td>
														<td align="center">
														<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Test Drive for Executive&opt=brandconfig_testdrive_sms_exe_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Test Drive Feedback:</td>
														<td align="center"><input type="checkbox"
															name="chk_brandconfig_testdrive_feedback_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_testdrive_feedback_email_enable)%>></td>
														<td align="center">
															<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Test Drive Feedback&opt=brandconfig_testdrive_feedback_email_format">Format</a></td>
														<td align="center"><input type="checkbox"
															name="chk_brandconfig_testdrive_feedback_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_testdrive_feedback_sms_enable)%>></td>
														<td align="center">
															<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Test Drive Feedback&opt=brandconfig_testdrive_feedback_sms_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Test Drive Feedback for Executive:</td>
														<td align="center">
														<input type="checkbox" name="chk_brandconfig_testdrive_feedback_email_exe_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_testdrive_feedback_email_exe_enable)%>>
															</td>
														<td align="center">
															<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Test Drive Feedback for Executive&opt=brandconfig_testdrive_feedback_email_exe_format">Format</a></td>
														<td align="center">
														<input type="checkbox" name="chk_brandconfig_testdrive_feedback_sms_exe_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_testdrive_feedback_sms_exe_enable)%>>
															</td>
														<td align="center">
														<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Test Drive Feedback for Executive&opt=brandconfig_testdrive_feedback_sms_exe_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Quote:</td>
														<td align="center"><input type="checkbox"
															name="chk_brandconfig_quote_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_quote_email_enable)%>></td>
														<td align="center">
															<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Quote&opt=brandconfig_quote_email_format">Format</a></td>
														<td align="center"><input type="checkbox"
															name="chk_brandconfig_quote_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_quote_sms_enable)%>></td>
														<td align="center">
														<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Quote&opt=brandconfig_quote_sms_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Quote for Executive:</td>
														<td align="center">
														<input type="checkbox" name="chk_brandconfig_quote_email_exe_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_quote_email_exe_enable)%>>
															</td>
														<td align="center">
														<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Quote for Executive&opt=brandconfig_quote_email_exe_format">Format</a></td>
														<td align="center">
														<input type="checkbox" name="chk_brandconfig_quote_sms_exe_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_quote_sms_exe_enable)%>>
															</td>
														<td align="center">
														<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Quote for Executive&opt=brandconfig_quote_sms_exe_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Quote for Discount Authorize:</td>
														<td align="center">
														<input type="checkbox" name="chk_brandconfig_quote_discount_authorize_email_enable" 
															id="chk_brandconfig_quote_discount_authorize_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_quote_discount_authorize_email_enable)%>></td>
														<td align="center">
														<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Quote for Discount Authorize&opt=brandconfig_quote_discount_authorize_email_format">Format</a>
														<td align="center">
														<input type="checkbox" name="chk_brandconfig_quote_discount_authorize_sms_enable"
															id="chk_brandconfig_quote_discount_authorize_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_quote_discount_authorize_sms_enable)%>>
															</td>
														<td align="center">
														<a href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Quote for Discount Authorize&opt=brandconfig_quote_discount_authorize_sms_format">Format</a></td>
														</td>
													</tr>
													
													<tr>
														<td align="left">Sales Order:</td>
														<td align="center"><input
															name="chk_brandconfig_so_email_enable" type="checkbox"
															id="chk_brandconfig_so_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_so_email_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Sales Order&opt=brandconfig_so_email_format">Format</a></td>
														<td align="center"><input
															name="chk_brandconfig_so_sms_enable" type="checkbox"
															id="chk_brandconfig_so_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_so_sms_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Sales Order&opt=brandconfig_so_sms_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Sales Order for Executive:</td>
														<td align="center">
														<input name="chk_brandconfig_so_email_exe_enable" type="checkbox"
															id="chk_brandconfig_so_email_exe_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_so_email_exe_enable)%>>
															</td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Sales Order for Executive&opt=brandconfig_so_email_exe_format">Format</a></td>
														<td align="center">
														<input name="chk_brandconfig_so_sms_exe_enable" type="checkbox"
															id="chk_brandconfig_so_sms_exe_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_so_sms_exe_enable)%>>
															</td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Sales Order for Executive&opt=brandconfig_so_sms_exe_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Sales Order Delivered:</td>
														<td align="center"><input
															name="chk_brandconfig_so_delivered_email_enable"
															type="checkbox"
															id="chk_brandconfig_so_delivered_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_so_delivered_email_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Sales Order Delivered&opt=brandconfig_so_delivered_email_format">Format</a></td>
														<td align="center"><input
															name="chk_brandconfig_so_delivered_sms_enable"
															type="checkbox"
															id="chk_brandconfig_so_delivered_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_so_delivered_sms_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Sales Order Delivered&opt=brandconfig_so_delivered_sms_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Pre Owned:</td>
														<td align="center">
															<input name="chk_brandconfig_preowned_email_enable" type="checkbox" id="chk_brandconfig_preowned_email_enable" <%=mybean.PopulateCheck(mybean.brandconfig_preowned_email_enable)%>></td>
														<td align="center">
															<a  href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Pre Owned&opt=brandconfig_preowned_email_format">Format</a></td> 
															<td align="center">
															<input name="chk_brandconfig_preowned_sms_enable" type="checkbox" id="chk_brandconfig_preowned_sms_enable" <%=mybean.PopulateCheck(mybean.brandconfig_preowned_sms_enable)%>>
														</td>
														<td align="center">
														<a  href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Pre Owned&opt=brandconfig_preowned_sms_format">Format</a>
														</td>
													</tr>
													
													<tr>
														<td align="left">Pre Owned For Executive:</td>
														<td align="center">
															<input name="chk_brandconfig_preowned_email_exe_enable" type="checkbox" id="chk_brandconfig_preowned_email_exe_enable" <%=mybean.PopulateCheck(mybean.brandconfig_preowned_email_exe_enable)%>></td>
														<td align="center">
															<a  href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Pre Owned For Executive&opt=brandconfig_preowned_email_exe_format">Format</a>
														</td>
														<td align="center">
															<input name="chk_brandconfig_preowned_sms_exe_enable" type="checkbox" id="chk_brandconfig_preowned_sms_exe_enable" <%=mybean.PopulateCheck(mybean.brandconfig_preowned_sms_exe_enable)%>>
														</td>
														<td align="center">
															<a  href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Pre Owned For Executive&opt=brandconfig_preowned_sms_exe_format">Format</a>
														</td>
													</tr>
													
													<tr>
														<td align="left">New Job Card:</td>
														<td align="center">
														<input name="chk_brandconfig_jc_new_email_enable" type="checkbox"
															id="chk_brandconfig_jc_new_email_enable"
															<%=mybean .PopulateCheck(mybean.brandconfig_jc_new_email_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=New Job Card&opt=brandconfig_jc_new_email_format">Format</a></td>
														<td align="center"><input
															name="chk_brandconfig_jc_new_sms_enable" type="checkbox"
															id="chk_brandconfig_jc_new_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_jc_new_sms_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=New Job Card&opt=brandconfig_jc_new_sms_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Ready Job Card:</td>
														<td align="center"><input
															name="chk_brandconfig_jc_ready_email_enable"
															type="checkbox" id="chk_brandconfig_jc_ready_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_jc_ready_email_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Ready Job Card&opt=brandconfig_jc_ready_email_format">Format</a></td>
														<td align="center"><input
															name="chk_brandconfig_jc_ready_sms_enable" type="checkbox"
															id="chk_brandconfig_jc_ready_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_jc_ready_sms_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Ready Job Card&opt=brandconfig_jc_ready_sms_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Delivered Job Card:</td>
														<td align="center"><input
															name="chk_brandconfig_jc_delivered_email_enable"
															type="checkbox"
															id="chk_brandconfig_jc_delivered_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_jc_delivered_email_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Delivered Job Card&opt=brandconfig_jc_delivered_email_format">Format</a></td>
														<td align="center"><input
															name="chk_brandconfig_jc_delivered_sms_enable"
															type="checkbox"
															id="chk_brandconfig_jc_delivered_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_jc_delivered_sms_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Delivered Job Card&opt=brandconfig_jc_delivered_sms_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Job Card Estimate:</td>
														<td align="center"><input
															name="chk_brandconfig_jc_estimate_email_enable"
															type="checkbox"
															id="chk_brandconfig_jc_estimate_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_jc_estimate_email_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Job Card Estimate&opt=brandconfig_jc_estimate_email_format">Format</a></td>
														<td align="center"><input
															name="chk_brandconfig_jc_estimate_sms_enable"
															type="checkbox" id="chk_brandconfig_jc_estimate_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_jc_estimate_sms_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Job Card Estimate&opt=brandconfig_jc_estimate_sms_format">Format</a></td>
													</tr>
													
													<tr>
														<td align="left">Job Card Feedback:</td>
														<td align="center"><input
															name="chk_brandconfig_jc_feedback_email_enable"
															type="checkbox"
															id="chk_brandconfig_jc_feedback_email_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_jc_feedback_email_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Job Card Feedback&opt=brandconfig_jc_feedback_email_format">Format</a></td>
														<td align="center"><input
															name="chk_brandconfig_jc_feedback_sms_enable"
															type="checkbox" id="chk_brandconfig_jc_feedback_sms_enable"
															<%=mybean.PopulateCheck(mybean.brandconfig_jc_feedback_sms_enable)%>></td>
														<td align="center"><a
															 href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Job Card Feedback&opt=brandconfig_jc_feedback_sms_format">Format</a></td>
													</tr>
													
													
													
													<!-- New Fields End -->
													
													
													
													
														<tr>
															<td align="left">Vehicle Follow-up Not Contactable:</td>
															<td align="center"><input
																name="chk_brandconfig_vehfollowup_notcontactable_email_enable"
																type="checkbox"
																id="chk_brandconfig_vehfollowup_notcontactable_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_vehfollowup_notcontactable_email_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Not Contactable&opt=brandconfig_vehfollowup_notcontactable_email_format">Format</a></td>
															<td align="center"><input
																name="chk_brandconfig_vehfollowup_notcontactable_sms_enable"
																type="checkbox"
																id="chk_brandconfig_vehfollowup_notcontactable_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_vehfollowup_notcontactable_sms_enable)%> />
															</td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Not Contactable&opt=brandconfig_vehfollowup_notcontactable_sms_format">Format</a></td>
														</tr>
													</tbody>

													<tbody>
														<tr>
															<td align="left">Vehicle Follow-up Daily Due:</td>
															<td align="center"><input
																name="chk_brandconfig_vehfollowup_dailydue_email_enable"
																type="checkbox"
																id="chk_brandconfig_vehfollowup_dailydue_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_vehfollowup_dailydue_email_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Daily Due&opt=brandconfig_vehfollowup_dailydue_email_format">Format</a></td>
															<td align="center"><input
																name="chk_brandconfig_vehfollowup_dailydue_sms_enable"
																type="checkbox"
																id="chk_brandconfig_vehfollowup_dailydue_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_vehfollowup_dailydue_sms_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Daily Due&opt=brandconfig_vehfollowup_dailydue_sms_format">Format</a></td>
														</tr>
													</tbody>
													<tbody>
														<tr>
															<td align="left">Service Booking:</td>
															<td align="center"><input
																name="chk_brandconfig_vehfollowup_booking_email_enable"
																type="checkbox"
																id="chk_brandconfig_vehfollowup_booking_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_vehfollowup_booking_email_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Booking&opt=brandconfig_vehfollowup_booking_email_format">Format</a></td>
															<td align="center"><input
																name="chk_brandconfig_vehfollowup_booking_sms_enable"
																type="checkbox"
																id="chk_brandconfig_vehfollowup_booking_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_vehfollowup_booking_sms_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Booking&opt=brandconfig_vehfollowup_booking_sms_format">Format</a></td>
														</tr>
													</tbody>
													<tbody>
														<tr>
															<td align="left">Service Booking For
																Executive:</td>
															<td align="center">&nbsp;</td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Booking For Executive&opt=brandconfig_vehfollowup_booking_exe_email_format">Format</a></td>
															<td align="center">&nbsp;</td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Booking For Executive&opt=brandconfig_vehfollowup_booking_exe_sms_format">Format</a></td>
														</tr>
													</tbody>
													<tbody>
														<tr>
															<td align="left">Vehicle Follow-up Serviced:</td>
															<td align="center"><input
																name="chk_brandconfig_vehfollowup_serviced_email_enable"
																type="checkbox"
																id="chk_brandconfig_vehfollowup_serviced_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_vehfollowup_serviced_email_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Serviced&opt=brandconfig_vehfollowup_serviced_email_format">Format</a></td>
															<td align="center"><input
																name="chk_brandconfig_vehfollowup_serviced_sms_enable"
																type="checkbox"
																id="chk_brandconfig_vehfollowup_serviced_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_vehfollowup_serviced_sms_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Serviced&opt=brandconfig_vehfollowup_serviced_sms_format">Format</a></td>
														</tr>
													</tbody>
													<tbody>
														<tr>
															<td align="left">Coupon Issue:</td>
															<td align="center"><input
																name="chk_brandconfig_coupon_email_enable"
																type="checkbox"
																id="chk_brandconfig_coupon_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_coupon_email_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=IssueCoupon&opt=brandconfig_coupon_email_format">Format</a></td>
															  <td align="center"><input
																name="chk_brandconfig_coupon_sms_enable"
																type="checkbox"
																id="chk_brandconfig_coupon_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_coupon_sms_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=IssueCoupon&opt=brandconfig_coupon_sms_format">Format</a></td>
														</tr>
													</tbody>
													<!-- Coupon Issue For Executive 
													<tbody>
														<tr>
															<td align="left">Coupon Issue For Executive:</td>
															<td align="center"><input
																name="chk_brandconfig_coupon_email_enable"
																type="checkbox"
																id="chk_brandconfig_coupon_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_coupon_email_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=IssueForExecutive&opt=brandconfig_coupon_exe_email_format">Format</a></td>
															<td align="center"><input
																name="chk_brandconfig_coupon_sms_enable"
																type="checkbox"
																id="chk_brandconfig_coupon_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_coupon_sms_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=IssueForExecutive&opt=brandconfig_coupon_exe_sms_format">Format</a></td>
														</tr>
													</tbody>-->
													<tbody>
														<tr>
															<td align="left">Vehicle Insurance Enquiry:</td>
															<td align="center"><input
																name="chk_brandconfig_insur_enquiry_email_enable"
																type="checkbox"
																id="chk_brandconfig_insur_enquiry_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_insur_enquiry_email_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Insurance&opt=brandconfig_insur_enquiry_email_format">Format</a></td>
															<td align="center"><input
																name="chk_brandconfig_insur_enquiry_sms_enable"
																type="checkbox"
																id="chk_brandconfig_insur_enquiry_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_insur_enquiry_sms_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Insurance&opt=brandconfig_insur_enquiry_sms_format">Format</a></td> 
														</tr>
													</tbody>
													<tbody>
														<tr>
															<td align="left">Vehicle Insurance Enquiry For
																Executive:</td>
															<td align="center">&nbsp;</td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Insurance For Executive&opt=brandconfig_insur_enquiry_exe_email_format">Format</a></td>
															<td align="center">&nbsp;</td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Insurance For Executive&opt=brandconfig_insur_enquiry_exe_sms_format">Format</a></td>
														</tr>
													</tbody>
													<tbody>
														<tr>
															<td align="left">SO CIN:</td>
															<td align="center"><input
																name="chk_brandconfig_socin_email_enable"
																type="checkbox" id="chk_brandconfig_socin_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_socin_email_enable)%> />
															</td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=
																<%=mybean.brandconfig_id%>&email=yes&status=SoCin&opt=brandconfig_socin_email_format">Format</a>
															</td>
															<td align="center"><input
																name="chk_brandconfig_socin_sms_enable" type="checkbox"
																id="chk_brandconfig_socin_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_socin_sms_enable)%> />
															</td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=
																	<%=mybean.brandconfig_id%>&sms=yes&status=SoCin&opt=brandconfig_socin_sms_format">Format</a>
															</td>
														</tr>
													</tbody>
													<tbody>
														<tr>
															<td align="left">SO CIN Executive:</td>
															<td align="center"><input
																name="chk_brandconfig_socin_exe_email_enable"
																type="checkbox"
																id="chk_brandconfig_socin_exe_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_socin_exe_email_enable)%> />
															</td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=
																<%=mybean.brandconfig_id%>&email=yes&status=SoCinExe&opt=brandconfig_socin_exe_email_format">Format</a>
															</td>
															<td align="center"><input
																name="chk_brandconfig_socin_exe_sms_enable"
																type="checkbox"
																id="chk_brandconfig_socin_exe_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_socin_exe_sms_enable)%> />
															</td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=
																	<%=mybean.brandconfig_id%>&sms=yes&status=SoCinExe&opt=brandconfig_socin_exe_sms_format">Format</a>
															</td>
														</tr>
													</tbody>
													<tbody>
														<tr>
															<td align="left">DIN:</td>
															<td align="center"><input
																name="chk_brandconfig_din_email_enable" type="checkbox"
																id="chk_brandconfig_din_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_din_email_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Din&opt=brandconfig_din_email_format">Format</a></td>
															<td align="center"><input
																name="chk_brandconfig_din_sms_enable" type="checkbox"
																id="chk_brandconfig_din_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_din_sms_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Din&opt=brandconfig_din_sms_format">Format</a></td>
														</tr>
													</tbody>
													<tbody>
														<tr>
															<td align="left">Receipt:</td>
															<td align="center"><input
																name="chk_brandconfig_receipt_email_enable"
																type="checkbox"
																id="chk_brandconfig_receipt_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_receipt_email_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Receipt&opt=brandconfig_receipt_email_format">Format</a></td>
															<td align="center"><input
																name="chk_brandconfig_receipt_sms_enable"
																type="checkbox" id="chk_brandconfig_receipt_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_receipt_sms_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Receipt&opt=brandconfig_receipt_sms_format">Format</a></td>
														</tr>
													</tbody>
													<tbody>
														<tr>
															<td align="left">Receipt Authorize:</td>
															<td align="center"><input type="checkbox"
																name="chk_brandconfig_receipt_authorize_email_enable"
																id="chk_brandconfig_receipt_authorize_email_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_receipt_authorize_email_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&email=yes&status=Receipt Authorize&opt=brandconfig_receipt_authorize_email_format">Format</a></td>
															<td align="center"><input type="checkbox"
																name="chk_brandconfig_receipt_authorize_sms_enable"
																id="chk_brandconfig_receipt_authorize_sms_enable"
																<%=mybean.PopulateCheck(mybean.brandconfig_receipt_authorize_sms_enable)%> /></td>
															<td align="center"><a
																href="brandconfig-format.jsp?brandconfig_id=<%=mybean.brandconfig_id%>&sms=yes&status=Receipt Authorize&opt=brandconfig_receipt_authorize_sms_format">Format</a></td>
														</tr>
													</tbody>
												</table>
												</div>
												<div class="form-element2"></div>
											</div>
											<% } %>
											
											<div class="row"></div>
											<% if (mybean.status.equals("Update") && !(mybean.brandconfig_entry_by == null) && !(mybean.brandconfig_entry_by.equals(""))) { %>
											<div class="form-element6 form-element-center ">
											<div class="form-element6">
												<label>Entry By: </label>
												<%=mybean.unescapehtml(mybean.brandconfig_entry_by)%><input
													type="hidden" name="brandconfig_entry_by"
													value="<%=mybean.brandconfig_entry_by%>">
											</div>
											<% } %>
											<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>

											<div class="form-element6">
												<label>Entry Date: </label>
												<%=mybean.entry_date%><input type="hidden" name="entry_date"
													value="<%=mybean.entry_date%>">
											</div>
											</div>
											<% } %>

											<% if (mybean.status.equals("Update") && !(mybean.brandconfig_modified_by == null) && !(mybean.brandconfig_modified_by.equals(""))) { %>
											<div class="form-element6 form-element-center ">
											<div class="form-element6">
												<label>Modified By: </label>
												<%=mybean.unescapehtml(mybean.brandconfig_modified_by)%>
												<input type="hidden" name="brandconfig_modified_by"
													value="<%=mybean.brandconfig_modified_by%>">
											</div>

											<% } %>

											<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
											<div class="form-element6">
												<label>Modified Date: </label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>">
											</div>
											</div>
											<% } %>
											<center>
												<div class="form-element12 form-element">
													<label> <% if (mybean.status.equals("Add")) { %> <input
														name="button" type="submit" class="btn btn-success"
														id="button" value="Add Brand Config"
														onClick="return SubmitFormOnce(document.form1, this);" />
														<input type="hidden" name="add_button" value="yes">
														<% } else if (mybean.status.equals("Update")) { %> <input
														type="hidden" name="update_button" value="yes"> <input
														name="button" type="submit" class="btn btn-success"
														id="button" value="Update Brand Config"
														onClick="return SubmitFormOnce(document.form1, this);" />
														<input name="delete_button" type="submit"
														class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)"
														value="Delete Brand Config" /> <% } %>
													</label>
												</div>
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
	<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	<!-- 	function FormFocus() { //v1.0 -->
	<!-- 		document.form1.txt_branch_name.focus(); -->
	<!-- 	} -->
	<!-- 	/*  -->
	<!-- 	 function selectpreowned() { -->
	<!-- 	 var temp = document.getElementById('dr_branch_branchtype_id').value; -->
	<!-- 	 if (temp == 1) { -->
	<!-- 	 $("#preowned_div").show(); -->
	<!-- 	 } else { -->
	<!-- 	 $("#preowned_div").hide(); -->
	<!-- 	 } -->
	<!-- 	 } -->
<!-- 	*/ -->
	</script>
</body>
</HTML>
