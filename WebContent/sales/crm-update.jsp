<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.CRM_Update" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

<script> 	
	function FormFocus() { //v1.0
		//document.formcontact.txt_customer_name.focus();
	}

	function DisplayExecutive() {
		var str = document.getElementById('dr_crmfollowup_2ndday_q7').value;
		if (str == "2") {
			displayRow('executive');
		} else {
			hideRow('executive');
		}
	}
	function DisplayTicketExecutive() {
		var str1 = document.getElementById('dr_crm_satisfied').value;
		if (str1 == "2") {
			displayRow('concern');
			displayRow('executive');
			
		} else {
			hideRow('concern');
			hideRow('executive');
			
		}
	}
	function DisplaylostcaseExecutive() {
		var str = document.getElementById('dr_crmfollowup_lostcase_q3').value;
		if (str == "2") {
			displayRow('executive');
		} else {
			hideRow('executive');
		}
	}
	function Displayregno() {
		var str = document.getElementById('dr_crmfollowup_2ndday_q4').value;
		//alert(str);

		if (str == "1" || str == "0") {
			hideRow('regno');
		} else {
			displayRow('regno');
		}
	}
	function populateLostCase2() {
		var crm_lostcase1_id = document.getElementById("dr_crm_lostcase1_id").value;
		showHint("../sales/enquiry-check.jsp?lostcase2=yes&crm_lostcase1_id="
				+ crm_lostcase1_id, "lostcase2");
	}
	function populateLostCase3() {
		var crm_lostcase2_id = document.getElementById("dr_crm_lostcase2_id").value;
		//alert("crm_lostcase2_id-------" +crm_lostcase2_id);
		showHint('../sales/enquiry-check.jsp?lostcase3=yes&crm_lostcase2_id='
				+ crm_lostcase2_id, 'lostcase3');
	}

	
	
	// if feedback type contactable sataisfied or 
	// dissatisfied is mandatory
	function populateAsterisk() {
		var contactable = document.getElementById("dr_feedbacktype").value;
		var satisfied = document.getElementById("dr_crm_satisfied").value;
		if (contactable == 1) {
			$('#star').show();
		} else {
			$('#star').hide();
		}
		
		if(satisfied == 2){
			$('#star1').show();
		}
		else {
			$('#star1').hide();
		}
		
	}
	function populateExperience(){ // experience
		var feedback = document.getElementById("dr_feedbacktype").value;
	//alert("feedback---------"+feedback);
	if(feedback == 2){
		$("#experience, #concern, #executive").hide();
	}else{
		$("#experience, #concern, #executive").show();
	}
	} 
</script>


</HEAD>
<body onLoad="FormFocus();DisplayTicketExecutive();populateAsterisk();populateExperience();"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- 	BODY -->
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
						<h1>
							Update <%=mybean.crmtype_name%> Follow-up
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="../sales/enquiry.jsp">Enquiry</a>&gt;</li>
						<li><a href="enquiry-list.jsp?all=yes">List Enquiry</a>&gt;</li>
						<li><a href="enquiry-dash.jsp?pop=yes&amp;enquiry_id=<%=mybean.enquiry_id%>">Enquiry ID: <%=mybean.enquiry_id%></a>&gt;</li>
						<li><a href="crm-update.jsp?update=yes&crm_id=<%=mybean.crm_id%>">Update <%=mybean.crmtype_name%> Follow-up </a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<!-- 	PORTLET -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Update <%=mybean.crmtype_name%> Follow-up
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<!-- START PORTLET BODY -->
											<input type="hidden" name="txt_add" id="txt_add" value="add">
											<center>
												<font size="1">Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.
												</font>
											</center>
											
											<input type="hidden" name="txt_crmfollowup_lostfollowup" id="txt_crmfollowup_lostfollowup"
												class="textbox" value="<%=mybean.crmdays_lostfollowup%>" />
											<input type="hidden" name="txt_crmfollowupdays_id" id="txt_crmfollowupdays_id"
												class="textbox" value="<%=mybean.crm_crmdays_id%>" />
											<input type="hidden" name="txt_StrScript" id="txt_StrScript" class="textbox" 
												value="<%=mybean.StrScript%>" />

												<table class="table table-responsive table-bordered table-hover" data-filter="#filter">
													<thead>
														<tr>
															<td colspan="8" align="center"><b><%=mybean.crmdays_desc%></b>
															</td>
														</tr>
													</thead>
													<tr>
														<td align="right" valign="top">Enquiry ID:</td>
														<td valign="top"><a
															href="enquiry-list.jsp?enquiry_id=<%=mybean.enquiry_id%>"><%=mybean.enquiry_id%></a>
														</td>
														<td align="right" valign="top">Enquiry No.:</td>
														<td valign="top"><%=mybean.enquiry_no%></td>
														<td align="right" valign="top">Enquiry Date:</td>
														<td valign="top"><%=mybean.enquiry_date%></td>
														<td align="right" valign="top">Enquiry DMS No.:</td>
														<td valign="top"><%=mybean.enquiry_dmsno%></td>
													</tr>
													<tr>
														<td align="right" valign="top">Customer:</td>
														<td valign="top"><a href="../customer/customer-list.jsp?customer_id=<%=mybean.customer_id%>" target=_blank><%=mybean.customer_name%></a></td>
														<td align="right" valign="top">Contact:</td>
														<td valign="top"><a href="../customer/customer-contact-list.jsp?contact_id=<%=mybean.contact_id%>" target=_blank><%=mybean.contact_name%></a></td>
														<td align="right" valign="top">Mobile1:</td>
														<%
															if (!mybean.crm_mobile1.equals("")) {
														%>
														<td valign="top"><%=mybean.crm_mobile1%><%=mybean.ClickToCall(mybean.crm_mobile1, mybean.comp_id)%>
														</td>
														<%
															} else {
														%>
														<td valign="top"><%=mybean.crm_mobile1%></td>
														<%
															}
														%>
														<td align="right" valign="top">Mobile2:</td>
														<%
															if (!mybean.crm_mobile2.equals("")) {
														%>
														<td valign="top"><%=mybean.crm_mobile2%><%=mybean.ClickToCall(mybean.crm_mobile2, mybean.comp_id)%>
														</td>
														<%
															} else {
														%>
														<td valign="top"><%=mybean.crm_mobile2%></td>
														<%
															}
														%>
													</tr>

													<tr>
														<td align="right" valign="top">Phone1:</td>
														<%
															if (!mybean.contact_phone1.equals("")) {
														%>
														<td valign="top"><%=mybean.contact_phone1%><%=mybean.ClickToCall(mybean.contact_phone1, mybean.comp_id)%>
														</td>
														<%
															} else {
														%>
														<td valign="top"><%=mybean.contact_phone1%></td>
														<%
															}
														%>
														<td align="right" valign="top">Phone2:</td>
														<%
															if (!mybean.contact_phone2.equals("")) {
														%>
														<td valign="top"><%=mybean.contact_phone2%><%=mybean.ClickToCall(mybean.contact_phone2, mybean.comp_id)%>
														</td>
														<%
															} else {
														%>
														<td valign="top"><%=mybean.contact_phone2%></td>
														<%
															}
														%>
														<td align="right" valign="top">Email1:</td>
														<td valign="top"><%=mybean.crm_email1%></td>
														<td align="right" valign="top">Emai2:</td>
														<td valign="top"><%=mybean.crm_email2%></td>
													</tr>
													<tr>
														<td align="right" valign="top">Customer Type:</td>
														<td valign="top"><%=mybean.custtype_name%></td>
														<td align="right" valign="top">Extended Warranty:</td>
														<td valign="top"><%=mybean.so_ew_amount%></td>
														<td align="right" valign="top">Variant:</td>
														<td valign="top"><%=mybean.item_name%></td>
														<td align="right" valign="top">Colour:</td>
														<td valign="top"><%=mybean.option_name%></td>
													</tr>

													<tr>
														<td align="right" valign="top">Branch Name:</td>
														<td valign="top"><%=mybean.branch_name%></td>
														<td align="right" valign="top">Sales Consultant:</td>
														<td valign="top"><%=mybean.enquiryexe_name%></td>
														<td align="right" valign="top">Team Leader:</td>
														<td valign="top"><%=mybean.manager_name%></td>
														<td align="right" valign="top">CRM Executive:</td>
														<td valign="top"><%=mybean.crm_name%></td>
													</tr>

													<tr>
														<td align="right" valign="top">Status:</td>
														<td valign="top"><%=mybean.status_name%></td>
														<td align="right" valign="top">Stage:</td>
														<td valign="top"><%=mybean.stage_name%></td>
														<td align="right" valign="top">SOE:</td>
														<td valign="top"><%=mybean.soe_name%></td>
														<td align="right" valign="top">SOB:</td>
														<td valign="top"><%=mybean.sob_name%></td>
													</tr>

													<%
														if (!mybean.so_id.equals("0")) {
													%>
													<tr>

														<td align="right" valign="top">SO ID:</td>
														<td valign="top"><a
															href="veh-salesorder-list.jsp?so_id=<%=mybean.so_id%>"><%=mybean.so_id%></a></td>
														<td align="right" valign="top">SO No:</td>
														<td valign="top"><%=mybean.so_no%></td>
														<td align="right" valign="top">SO Date:</td>
														<td valign="top"><%=mybean.so_date%></td>
														<td align="right" valign="top">SO Deliverydate:</td>
														<td valign="top"><%=mybean.so_delivered_date%></td>
													</tr>
													<tr>
														<td align="right" valign="top">SO Payment Date:</td>
														<td valign="top"><%=mybean.so_payment_date%></td>
														<td align="right" valign="top">Bank Name:</td>
														<td valign="top"><%=mybean.fincomp_name%></td>
														<td align="right" valign="top">SO Variant:</td>
														<td valign="top"><%=mybean.so_item_name%></td>
														<td align="right" valign="top">SO Colour</td>
														<td valign="top"><%=mybean.sooptionname%></td>
													</tr>
													<tr>

														<td align="right" valign="top">Tentative Delievery
															Date:</td>
														<td valign="top"><%=mybean.so_promise_date%></td>
														<td align="right" valign="top">&nbsp;</td>
														<td valign="top">&nbsp;&nbsp;</td>
													</tr>

													<%
														if (mybean.crmdays_crmtype_id.equals("3")) {
													%>
													<tr>
														<td align="right" valign="top">Engine No.:</td>
														<td valign="top"><%=mybean.engine_no%></td>
														<td align="right" valign="top">&nbsp;</td>
														<td valign="top">&nbsp;</td>
														<td align="right" valign="top">Vehicle Chassis No.:</td>
														<td valign="top"><%=mybean.chassis_no%></td>
														<td align="right" valign="top">Vehicle Register No.:</td>
														<td valign="top"><%=mybean.reg_no%></td>
													</tr>
													<tr>
														<td align="right" valign="top">Contact Address:</td>
														<td valign="top"><%=mybean.contact_address%></td>
														<td align="right" valign="top">Occupation:</td>
														<td valign="top"><%=mybean.occ_name%></td>
													
													<%
														}
													%>
													
														<td align="right" valign="top">Booking Amount:</td>
														<td valign="top"><%=mybean.so_booking_amount%></td>
														<td align="right" valign="top">SO Amount:</td>
														<td valign="top"><%=mybean.so_grandtotal%></td>
													</tr>
													<%
														}
													%>

												</table>

												<table class="table table-responsive table-bordered table-hover"
													data-filter="#filter">
													<tbody>
														<tr>

															<%
																if (!mybean.StrScript.equals("")) {
															%>
															<td colspan="4" align="left"><br> <b><%=mybean.StrScript%></b>
																<br> <br></td>
															<%
																}
															%>
														</tr>


														<%=mybean.CRMCustomFieldView(mybean.comp_id, mybean.crm_id, "2", request)%>


														<tr valign="center">
															<td align="right" valign="top">Mobile 1:</td>
															<td>
																<input name="txt_crm_mobile1" onkeyup="toPhone('txt_crm_mobile1', 'Mobile1')"
																type="text" class="form-control" id="txt_crm_mobile1"
																value="<%=mybean.crm_mobile1%>" size="32" maxlength="13" style="width: 250px" />
															</td>
														</tr>

														<tr>
															<td align="right" valign="top">Mobile 2:</td>
															<td><input name="txt_crm_mobile2"
																onkeyup="toPhone('txt_crm_mobile2', 'Mobile2')"
																type="text" class="form-control" id="txt_crm_mobile2"
																value="<%=mybean.crm_mobile2%>" size="32" maxlength="13"
																style="width: 250px" /></td>
														</tr>

														<tr valign="center">
															<td align="right" valign="top">Email 1:</td>
															<td><input name="txt_crm_email1" type="text"
																class="form-control" id="txt_crm_email1"
																value="<%=mybean.crm_email1%>" size="32" maxlength="255"
																style="width: 250px"></td>
														</tr>

														<tr valign="center">
															<td align="right" valign="top">Email 2:</td>
															<td><input name="txt_crm_email2" type="text"
																class="form-control" id="txt_crm_email2"
																value="<%=mybean.crm_email2%>" size="32" maxlength="255"
																style="width: 250px"></td>
														</tr>

														<%
															if (mybean.crmdays_lostfollowup.equals("1")) {
														%>
														<tr>
															<td align="right" valign="top">Lost Case 1<font
																color="#ff0000">*</font>:
															</td>
															<td align="left" valign="top"><select
																name="dr_crm_lostcase1_id" class="form-control"
																id="dr_crm_lostcase1_id" onchange="populateLostCase2()">
																	<%=mybean.PopulateLostCase1(mybean.comp_id, mybean.crm_lostcase1_id)%>
															</select>&nbsp;<%=mybean.lostcase1_name%>
																<div class="hint" id="hint_dr_crm_lostcase1_id"></div></td>
														</tr>
														<tr>
															<td align="right" valign="top">Lost Case 2<font
																color="#ff0000">*</font>:
															</td>
															<td align="left" valign="top"><span id="lostcase2">
																	<%=mybean.PopulateLostCase2(mybean.comp_id, mybean.crm_lostcase2_id, mybean.crm_lostcase1_id)%>
															</span>&nbsp;<%=mybean.lostcase2_name%>
																<div class="hint" id="hint_dr_crm_lostcase2_id"></div></td>
														</tr>
														<tr>
															<td align="right" valign="top">Lost Case 3<font
																color="#ff0000">*</font>:
															</td>
															<td align="left" valign="top"><span id="lostcase3">
																	<%=mybean.PopulateLostCase3(mybean.comp_id, mybean.crm_lostcase3_id, mybean.crm_lostcase2_id)%>
															</span>&nbsp;<%=mybean.lostcase3_name%>
																<div class="hint" id="hint_dr_crm_lostcase3_id"></div></td>
														</tr>
														<%
															}
														%>

														<%
															if (mybean.crmdays_so_inactive.equals("1")) {
														%>
														<tr>
															<td align="right">Cancel Reason :</td>
															<td valign="top"><select
																name="dr_crm_cancelreason_id" class="form-control"
																id="dr_crm_cancelreason_id">
																	<%=mybean.PopulateCancelReason(mybean.comp_id)%>
															</select></td>
														</tr>
														<%
															}
														%>

														<tr id="feedback">
															<td align="right">Feedback<font color="#ff0000">*</font>:<br></td>
															<td valign="top">
																<textarea name="txt_crmfollowup_desc" cols="50" rows="4"
																	class="form-control" id="txt_crmfollowup_desc"
																	onKeyUp="charcount('txt_crmfollowup_desc', 'span_txt_crmfollowup_desc','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.crm_desc%></textarea>
																<span id="span_txt_crmfollowup_desc"> (8000 characters)</span></td>
														</tr>

														<tr>
															<td align="right" width="50%">Feedback Type<font
																color="#ff0000">*</font>:
															</td>
															<td valign="top"><select name="dr_feedbacktype"
																class="form-control" id="dr_feedbacktype" visible="true"
																onchange="populateAsterisk();populateExperience();">
																	<%=mybean.PopulateCRMFeedbackType(mybean.comp_id)%>
															</select></td>
														</tr>

														<tr id="experience">
															<td align="right">How was your overall experience?<span
																id="star"><font color="#ff0000">*</font></span>:
															</td>
															<td valign="top"><select name="dr_crm_satisfied"
																class="form-control" id="dr_crm_satisfied"
																onchange="DisplayTicketExecutive(); populateAsterisk();">
																	<%=mybean.PopulateCRMSatisfied(mybean.comp_id)%>
															</select></td>
														</tr>
														<tr id="concern">
															<td align="right">CRM Concern:<span
																id="star1"><font color="#ff0000">*</font></span>:</td>
															<td valign="top"><select name="dr_crm_concern_id"
																id="dr_crm_concern_id" class="form-control">
																	<%=mybean.PopulateConcern(mybean.comp_id, mybean.crm_crmconcern_id)%>
															</select></td>
														</tr>

														<tr id="executive">
															<td align="right">Ticket Owner<span
																id="star1"><font color="#ff0000">*</font></span>:</td></td>
															<td valign="top"><select name="dr_ticketowner_id"
																id="dr_ticketowner_id" class="form-control">
																	<%=mybean.PopulateExecutive(mybean.comp_id)%>
															</select></td>
														</tr>

														<%
															if (mybean.update.equals("yes") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
														%>
														<tr>
															<td align="right">Entry By:</td>
															<td align="left"><%=mybean.unescapehtml(mybean.entry_by)%>
																<input name="entry_by" type="hidden" id="entry_by"
																value="<%=mybean.entry_by%>"></td>
														</tr>
														<tr>
															<td align="right">Entry Date:</td>
															<td align="left"><%=mybean.entry_date%> <input
																type="hidden" name="entry_date"
																value="<%=mybean.entry_date%>"></td>
														</tr>
														<%
															}
														%>
														<%
															if (mybean.update.equals("yes") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
														%>
														<tr>
															<td align="right">Modified By:</td>
															<td align="left"><%=mybean.unescapehtml(mybean.modified_by)%>
																<input name="modified_by" type="hidden" id="modified_by"
																value="<%=mybean.modified_by%>"></td>
														</tr>
														<tr>
															<td align="right">Modified Date:</td>
															<td align="left"><%=mybean.modified_date%> <input
																type="hidden" name="modified_date"
																value="<%=mybean.modified_date%>"></td>
														</tr>
														<%
															}
														%>
														<tr>
															<td colspan="4" align="center" valign="middle">
																<center>
																	<input name="update_button" type="submit"
																		class="btn btn-success" id="update_button"
																		value="Update Follow-up" />
																</center> <input type="hidden" name="enquiry_id" id="enquiry_id"
																value="<%=mybean.enquiry_id%>"> <input
																type="hidden" name="crmfollowup_id" id="crmfollowup_id"
																value="<%=mybean.crm_id%>"> <input type="hidden"
																name="crmfollowup_entry_id" id="crmfollowup_entry_id"
																value="<%=mybean.crm_entry_id%>"> <input
																type="hidden" name="crmfollowup_modified_id"
																id="crmfollowup_modified_id"
																value="<%=mybean.crm_modified_id%>">
															</td>
														</tr>


													</tbody>
												</table>
									</div>


									<!-- END PORTLET BODY -->
								</div>
							</div>
						</div>
						<!-- 	PORTLET -->


					</div>
				</div>
			</div>
		</div>
	</div>

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>
	
</body>
</HTML>
