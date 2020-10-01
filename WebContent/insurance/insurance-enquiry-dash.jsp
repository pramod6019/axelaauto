<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_Enquiry_Dash" scope="request" />

<% mybean.doPost(request, response); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transi tional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>
<style type="text/css">

.bootstrap-select .dropdown-toggle
{
	width: 10.5pc;
}
.pop {
	top: 280px;
	left: 740px;
}
</style>
</head>

<body  class="page-container-bg-solid page-header-menu-fixed">
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
						<h1> Insurance Enquiry Dashboard &gt; Insurance Enquiry ID<b>: </b> <%=mybean.insurenquiry_id%></h1>
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
						<li><a href="../insurance/index.jsp">Insurance</a> &gt;</li>
						<li><a href="../insurance/insurance-enquiry-list.jsp?all=yes"> Insurance Enquiry List</a> &gt;</li>
						<li><a href="../insurance/insurance-enquiry-list.jsp?insurenquiry_id=<%=mybean.insurenquiry_id%>">Insurance Enquiry ID<b>: &nbsp;<%=mybean.insurenquiry_id%></b> </a></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							
							
							<div class="tabbable tabbable-tabdrop">
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tabs-1" data-toggle="tab">Insurance Enquiry Details</a></li>
									<li><a href="#tabs-2" id="tab-2" data-toggle="tab">Follow-up</a></li>
									<li><a href="#tabs-3" data-toggle="tab" onclick="LoadInsuranceDash('3');">Policy</a></li>
									<li><a href="#tabs-8" data-toggle="tab">History</a></li>
								</ul>
								<div class="tab-content">



									<div id="dialog-modal"></div>
									<div class="tab-pane active" id="tabs-1">
										<!--                                                Insurance Enquiry Details -->

										<form name="form1" id="form1" class="form-horizontal"
											method="post">
											<!-- Start Insurance Enquiry details -->

											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Insurance
														Enquiry Details</div>
												</div>

												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->

														<div class="form-element3 form-element-margin">
															<label> Insurance Enquiry ID: </label>
																<a href="insurance-enquiry-list.jsp?insurenquiry_id=<%=mybean.insurenquiry_id%>">
																	<b><%=mybean.insurenquiry_id%></b>
																</a> <input class="form-control" name="insurenquiry_id"
																	type="hidden" id="insurenquiry_id"
																	value="<%=mybean.insurenquiry_id%>" />
														</div>

														<div class="form-element3 form-element-margin">
															<label> Branch:&nbsp; </label>
																<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.insurenquiry_branch_id%>">
																	<%=mybean.branch_name%></a>
														</div>

														<div class="form-element3 form-element-margin">
															<label> Date: &nbsp;</label>
																<input class="form-control" name="txt_enquiry_date"
																	type="hidden" id="txt_enquiry_date"
																	value="<%=mybean.date%>">
																<%=mybean.date%>
														</div>

														<div class="form-element3">
															<label> Insurance Executive<font color="#ff0000">*</font>: &nbsp; </label>
																<select name="dr_insurenquiry_emp_id"
																	id="dr_insurenquiry_emp_id" class="form-control"
																	onChange="SecurityCheck('dr_insurenquiry_emp_id',this,'hint_dr_insurenquiry_emp_id');">
																	<%=mybean.PopulateExecutive(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_insurenquiry_emp_id"></div>

															</div>
														</div>

														<div class="row">
														<div class="form-element3 form-element-margin">
															<label> Ref. Executive: &nbsp;</label>
																<%-- 																<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.ref_emp_name%>"> --%>
																<%=mybean.ref_emp_name%>
																<!-- 																</a> -->
														</div>

														<div class="form-element3">
															<label> Variant<font color="#ff0000">*</font>:&nbsp; </label>
																<select class="form-control select2"
																	id="preownedvariant" name="preownedvariant"
																	onChange="SecurityCheck('preownedvariant',this,'hint_preownedvariant');">
																	<%=mybean.modelcheck.PopulateVariant(mybean.insurenquiry_variant_id)%>
																</select>
																<div class="hint" id="hint_preownedvariant"></div>
														</div>

														<div class="form-element3">
															<label> Reg. Number:&nbsp; </label>
																<input name="txt_insurenquiry_reg_no"
																	id="txt_insurenquiry_reg_no" type="text"
																	class="form-control"
																	onChange="SecurityCheck('txt_insurenquiry_reg_no',this,'hint_txt_insurenquiry_reg_no');"
																	value="<%=mybean.insurenquiry_reg_no%>" size="20"
																	maxlength="20" />
																<div class="hint" id="hint_txt_insurenquiry_reg_no"></div>
														</div>

																<div class="form-element3">
																	<label> Chassis Number:&nbsp; </label>
																		<input name="txt_insurenquiry_chassis_no"
																			id="txt_insurenquiry_chassis_no"
																			onChange="SecurityCheck('txt_insurenquiry_chassis_no',this,'hint_txt_insurenquiry_chassis_no');"
																			type="text" class="form-control"
																			value="<%=mybean.insurenquiry_chassis_no%>" size="20"
																			maxlength="25" />
																		<div class="hint"
																			id="hint_txt_insurenquiry_chassis_no"></div>
																	</div>
																	</div>

																<div class="row">
																<div class="form-element3">
																	<label> Engine Number:&nbsp; </label>
																		<input name="txt_insurenquiry_engine_no"
																			id="txt_insurenquiry_engine_no" type="text"
																			class="form-control"
																			onChange="SecurityCheck('txt_insurenquiry_engine_no',this,'hint_txt_insurenquiry_engine_no');"
																			value="<%=mybean.insurenquiry_engine_no%>" size="20"
																			maxlength="25" />
																		<div class="hint" id="hint_txt_insurenquiry_engine_no"></div>
																	</div>

																<div class="form-element3">
																	<label> Sale Date<b>:</b>&nbsp; </label>
																		<input name="txt_insurenquiry_sale_date" type="text"
																			class="form-control datepicker"
																			id="txt_insurenquiry_sale_date"
																			onChange="SecurityCheck('txt_insurenquiry_sale_date',this,'hint_txt_insurenquiry_sale_date');"
																			value="<%=mybean.insurenquirysaledate%>" size="12"
																			maxlength="10" />
																		<div class="hint" id="hint_txt_insurenquiry_sale_date"></div>
																	</div>
																	
																<div class="form-element3">
																	<label>Insurance Renewal Date:&nbsp;</label>
																		<input name="txt_insurenquiry_renewal_date"
																			id="txt_insurenquiry_renewal_date" type="text"
																			class="form-control datepicker"
																			onChange="SecurityCheck('txt_insurenquiry_renewal_date',this,'hint_txt_insurenquiry_renewal_date');"
																			value="<%=mybean.renewal_date%>" size="12"
																			maxlength="10" />
																		<div class="hint" id="hint_txt_insurenquiry_renewal_date"></div>
																	</div>

																<div class="form-element3">
																	<label>Insurance Type<font color="#ff0000">*</font>:&nbsp; </label>
																		<select id="dr_insurenquiry_insurtype_id"
																			name="dr_insurenquiry_insurtype_id"
																			class="form-control"
																			onChange="SecurityCheck('dr_insurenquiry_insurtype_id',this,'hint_dr_insurenquiry_insurtype_id');">
																			<%=mybean.PopulateInsurType(mybean.comp_id)%>
																		</select>
																		<div class="hint" id="hint_dr_insurenquiry_insurtype_id"></div>
																	</div></div>

																</div>

													</div>
<!-- 												</div> -->
											



											<!-- Start Customer Details-->
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Customer</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->


														<div class="row">
														<div class="form-element3 ">
																	<label>Customer:&nbsp;</label>
																	<input name="txt_enquiry_customer_name" type="text"
																		class="form-control" id="txt_enquiry_customer_name"
																		value="<%=mybean.insurenquiry_customer_name%>"
																		size="32" maxlength="255" 
																		onChange="SecurityCheck('txt_enquiry_customer_name',this,'hint_txt_enquiry_customer_name')" />
															<div class="hint" id="hint_txt_enquiry_customer_name"></div>
															</div>
														</div>

															

														<div class="row">
															<div class="form-element2 ">
																<label>Contact<font color=red>*</font>:&nbsp; </label>
																<select name="dr_title" class="form-control" id="dr_title"
																onChange="SecurityCheck('dr_title',this,'hint_dr_title')">
																	<%=mybean.PopulateContactTitle(mybean.comp_id)%>
																</select>
																<span>Title</span>
																<div class="hint" id="hint_dr_title"></div>
															</div> 
															
															<div class="form-element5 form-element-margin">
																<input name="txt_contact_fname" type="text" class="form-control"
																	id="txt_contact_fname" value="<%=mybean.contact_fname%>"
																	size="30" maxlength="255" onkeyup="ShowNameHint();" 
																	onChange="SecurityCheck('txt_contact_fname',this,'hint_txt_contact_fname')"/>
																<span>First Name</span>
																<div class="hint" id="hint_txt_contact_fname"></div>
															</div> 
																	
															<div class="form-element5 form-element-margin">
																<input name="txt_contact_lname" type="text" class="form-control"
																	id="txt_contact_lname" value="<%=mybean.contact_lname%>"
																	size="30" maxlength="255" onkeyup="ShowNameHint();" 
																	onChange="SecurityCheck('txt_contact_lname',this,'hint_txt_contact_lname')"/> 
																<span>Last Name</span>
																<div class="hint" id="hint_txt_contact_lname"></div>
															</div>
														</div>



														<div class="row">
														<div class="form-element3 ">
															<label> Mobile 1<font color="#ff0000">*</font>:&nbsp; </label>
																	<input name="txt_contact_mobile1" type="text"
																		class="form-control" id="txt_contact_mobile1"
																		onKeyUp="toPhone('txt_contact_mobile1','Contact mobile2')"
																		value="<%=mybean.contact_mobile1%>" size="32"
																		maxlength="13"
																		onChange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')" />
																	(91-9999999999)
																	<div class="hint" id="hint_txt_contact_mobile1"></div>
																</div>
																

															<div class="form-element3 ">
																	<label>Mobile 2:&nbsp;</label>
																	<input name="txt_contact_mobile2" type="text"
																		class="form-control" id="txt_contact_mobile2"
																		onKeyUp="toPhone('txt_contact_mobile2','Contact Mobile2')"
																		value="<%=mybean.contact_mobile2%>" size="32"
																		maxlength="13"
																		onChange="SecurityCheck('txt_contact_mobile2',this,'hint_txt_contact_mobile2')" />
																	(91-9999999999)
																	<div class="hint" id="hint_txt_contact_mobile2"></div>
																	<span><br></span>
																</div>
																
														<div class="form-element3 ">
																	<label>Phone 1<font color="#ff0000">*</font>:&nbsp; </label>
																	<input name="txt_contact_phone1" type="text"
																		class="form-control" id="txt_contact_phone1"
																		onKeyUp="toPhone('txt_contact_phone1','Contact Phone1')"
																		value="<%=mybean.contact_phone1%>" size="32"
																		maxlength="14"
																		onchange="SecurityCheck('txt_contact_phone1',this,'hint_txt_contact_phone1')" />
																	(91-80-33333333)
																	<div class="hint" id="hint_txt_contact_phone1"></div>
																</div>

															<div class="form-element3">
																	<label>Phone 2:&nbsp;</label>
																	<input name="txt_contact_phone2" type="text"
																		class="form-control" id="txt_contact_phone2"
																		onKeyUp="toPhone('txt_contact_phone2','Contact Phone2')"
																		value="<%=mybean.contact_phone2%>" size="32"
																		maxlength="14"
																		onchange="SecurityCheck('txt_contact_phone2',this,'hint_txt_contact_phone2')" />
																	(91-80-33333333)
																	<div class="hint" id="hint_txt_contact_phone2"></div>
																</div></div>
																
														<div class="row">
														<div class="form-element3 " style="margin-bottom: 15px">
																	<label> Email 1:&nbsp;</label>
																	<input name="txt_contact_email1" type="text"
																		class="form-control" id="txt_contact_email1"
																		value="<%=mybean.contact_email1%>" size="35"
																		maxlength="100"
																		onChange="SecurityCheck('txt_contact_email1',this,'hint_txt_contact_email1')">
																	<div class="hint" id="hint_txt_contact_email1"></div>
																</div>

															<div class="form-element3">
																	<label> Email 2:&nbsp;</label>
																	<input name="txt_contact_email2" type="text"
																		class="form-control" id="txt_contact_email2"
																		value="<%=mybean.contact_email2%>" size="35"
																		maxlength="100"
																		onChange="SecurityCheck('txt_contact_email2',this,'hint_txt_contact_email2')">
																	<div class="hint" id="hint_txt_contact_email2"></div>
																</div>
																
														         <div class="form-element3 ">
																	<label>Address<font color="#ff0000">*</font>:&nbsp; </label>
																	<textarea name="txt_contact_address" cols="20" rows="4"
																		class="form-control" id="txt_contact_address"
																		onChange="SecurityCheck('txt_contact_address',this,'hint_txt_contact_address')"
																		onKeyUp="charcount('txt_contact_address', 'span_txt_contact_address','<font color=red>({CHAR} characters left)</font>', '255')"
																		><%=mybean.contact_address%></textarea>
																	<span id="span_txt_contact_address"> (255
																		Characters)</span>
																	<div class="hint" id="hint_txt_contact_address"></div>
																</div>

															<div class="form-element3">
																	<label>City<font color="#ff0000">*</font>:&nbsp; </label>
																	<select class="form-control select2" id="maincity"
																		name="maincity"
																		onchange="SecurityCheck('maincity',this,'hint_maincity')">
																		<%=mybean.PopulateCity()%>
																	</select>

																	<div class="hint" id="hint_maincity"></div>
																</div>
																
																
														<div class="form-element3 ">
																	<label>Pin/Zip<font color="#ff0000">*</font>:&nbsp; </label>
																	<input name="txt_contact_pin" type="text"
																		class="form-control" id="txt_contact_pin"
																		onKeyUp="toInteger('txt_contact_pin','Pin')"
																		onChange="SecurityCheck('txt_contact_pin',this,'hint_txt_contact_pin')"
																		value="<%=mybean.contact_pin%>" size="10"
																		maxlength="6"></input>
																	<div class="hint" id="hint_txt_contact_pin"></div>
																</div>
																</div>



												</div>
											</div></div>
											<!--                       End Customer Details-->

											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Status</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->
														<div class="row">
														<div class="form-element3 ">
																	<label>Status<font color="#ff0000">*</font>:&nbsp; </label>
																	<select name="dr_insurenquiry_status_id"
																		class="form-control" id="dr_insurenquiry_status_id"
																		onChange="SecurityCheck('dr_insurenquiry_status_id',this,'hint_dr_insurenquiry_status_id')">
																		<%=mybean.PopulateStatus(mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_insurenquiry_status_id"></div>
																</div>

																<div class="form-element3">
																	<label> Lost
																		Case 1<font color="#ff0000">*</font>:&nbsp;
																	</label>
																		<span id="span_lostcase1"> <select
																			name="dr_insurenquiry_lostcase1_id"
																			class="form-control"
																			id="dr_insurenquiry_lostcase1_id"
																			onChange="SecurityCheck('dr_insurenquiry_lostcase1_id',this,'hint_dr_insurenquiry_status_id')">
																				<%=mybean.PopulateLostCase1(mybean.comp_id)%>
																		</select> <!-- 																			<div class="hint" id="hint_dr_insurenquiry_lostcase1_id"></div> -->
																	</div>
																	
														<div class="form-element2 form-element-margin">
																	<label>SOE:&nbsp;</label>
																	<%=mybean.soe_name%>
																</div>

																<div class="form-element2 form-element-margin">
																	<label>SOB:&nbsp;</label>
																	<%=mybean.sob_name%>
																</div>

																<div class="form-element2 form-element-margin">
																	<label>Campaign:&nbsp;</label>
																	<%=mybean.campaign_name%>
																</div>
																</div>
														
														<div class="form-element6 ">
																	<label>Status Comments<font color="#ff0000">*</font>:&nbsp; </label>
																	<textarea name="txt_insurenquiry_status_desc" cols="50"
																		rows="4" class="form-control"
																		id="txt_insurenquiry_status_desc"
																		onKeyUp="charcount('txt_insurenquiry_status_desc', 'span_txt_insurenquiry_status_desc','<font color=red>({CHAR} characters left)</font>', '1000')"
																		onChange="SecurityCheck('txt_insurenquiry_status_desc',this,'hint_txt_insurenquiry_status_desc')"><%=mybean.insurenquiry_insurstatus_desc%> </textarea>
																	<span id="span_txt_insurenquiry_status_desc">(1000 Characters)</span><br>
																	<span id="hint_txt_insurenquiry_status_desc"></span>
																	<!-- 																	<div class="hint" id="hint_dr_insurenquiry_status_id"></div> -->

																</div>
																
																

																<div class="row">
																<div class="form-element6 ">
																	<label>Notes:&nbsp;</label>
																	<textarea name="txt_insurenquiry_notes" cols="70"
																		rows="4" class="form-control"
																		id="txt_insurenquiry_notes"
																		onKeyUp="charcount('txt_insurenquiry_notes', 'span_txt_insurenquiry_notes','<font color=red>({CHAR} characters left)</font>', '1000')"
																		onChange="SecurityCheck('txt_insurenquiry_notes',this,'hint_txt_insurenquiry_notes')"> <%=mybean.insurenquiry_notes%> </textarea>
																	<span id="span_txt_insurenquiry_notes">(1000 Characters)</span><br>
																	<span id="hint_txt_insurenquiry_notes"></span>
																</div></div>

														<div class="form-element6 ">
																<label>Entry By:&nbsp;</label>
																	<%=mybean.entry_by%>
																	<input type="hidden" name="entry_by"
																		value="<%=mybean.entry_by%>"/>
																</div>
															</div>

															<div class="form-element6">
																<label>Entry Date:&nbsp;</label>
																	<%=mybean.entry_date%>
																	<input type="hidden" name="entry_date"
																		value="<%=mybean.entry_date%>"/>
																</div>

															<%
																	if (mybean.modified_by != null && !mybean.modified_by.equals("")) {
																%>

															<div class="form-element6">
																<label>Modified By:&nbsp;</label>
																	<%=mybean.modified_by%>
																	<input type="hidden" name="modified_by"
																		value="<%=mybean.modified_by%>"/>
																</div>

															<div class="form-element6">
																<label>Modified Date:&nbsp;</label>
																	<%=mybean.modified_date%>
																	<input type="hidden" name="modified_date"
																		value="<%=mybean.modified_date%>"/>
																</div>
															<%
																	}
																%>

														</div>
													</div>
												</div>

										</form>

									<div class="tab-pane" id="tabs-2">
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Insurance Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">

												<form class="form-horizontal">
													<div class="form-element3">
														<label>Customer<b>:&nbsp;</b></label>
															<a href="../customer/customer-list.jsp?customer_id=<%=mybean.insurenquiryfollowup_customer_id%>" target="_blank"><%=mybean.insurenquiryfollowup_customer_name%></a>
													  </div>
													  
													  <div class="form-element3">
														<label>Contact Name<b>:&nbsp;</b> </label>
															<a href="../customer/customer-contact-list.jsp?contact_id=<%=mybean.insurenquiryfollowup_contact_id%>"><%=mybean.insurenquiryfollowup_contact_name%></a>
																<%-- <label class="control-label text-right"><%=mybean.insurenquiryfollowup_contact_name%></label> --%>
														</div>
														

													<div class="form-element3">
														<label>Mobile<b>:&nbsp;</b></label>
															<% if (!mybean.insurenquiryfollowup_contact_mobile.equals("")) { %>
																<%=mybean.insurenquiryfollowup_contact_mobile%><%=mybean.ClickToCall(mybean.contact_mobile1,mybean.comp_id)%><span class="txt-align" id="picon"></span>
															<% } %>
													</div>
													  
													  <div class="form-element3">	
														<label>Variant<b>:&nbsp;</b> </label>
															<%=mybean.insurenquiryfollowup_variant%>
														</div>

													<div class="row">
													<div class="form-element3">
														<label>Previous Insurance Company Name<b>:&nbsp;</b> </label>
															<input name="txt_insurfollowup_previouscompname"
																id="txt_insurfollowup_previouscompname" type="text" class="form-control"
																value="<%=mybean.veh_insur_previouscompname%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_previouscompname',this,'hint_txt_insurfollowup_previouscompname')" />
															<span class="hint" id="hint_txt_insurfollowup_previouscompname"></span>
														</div>
														<!-- 												</div> -->

															
														<div class="form-element3">
														<label>Previous Year IDV<b>:&nbsp;</b> </label>
															<input name="txt_insurfollowup_previousyearidv"
																id="txt_insurfollowup_previousyearidv" type="text" class="form-control"
																value="<%=mybean.veh_insur_previousyearidv%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_previousyearidv',this,'hint_txt_insurfollowup_previousyearidv')" />
															<span class="hint" id="hint_txt_insurfollowup_previousyearidv"></span>
														</div>
														
													<div class="form-element3">
														<label>Previous Gross Premium<b>:&nbsp;</b> </label>
															<input name="txt_insurfollowup_previousgrosspremium"
																id="txt_insurfollowup_previousgrosspremium" type="text" class="form-control"
																value="<%=mybean.veh_insur_previousgrosspremium%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_previousgrosspremium',this,'hint_txt_insurfollowup_previousgrosspremium')" />
															<span class="hint" id="hint_txt_insurfollowup_previousgrosspremium"></span>
														</div>
														<!-- 												</div> -->

														<div class="form-element3">
														<label>Previous Plan Name<b>:&nbsp;</b> </label>
															<input name="txt_insurfollowup_previousplanname"
																id="txt_insurfollowup_previousplanname" type="text" class="form-control"
																value="<%=mybean.veh_insur_previousplanname%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_previousplanname',this,'hint_txt_insurfollowup_previousplanname')" />
															<span class="hint" id="hint_txt_insurfollowup_previousplanname"></span>
														</div></div>

													<div class="row">
													<div class="form-element3">
														<label>Premium With Zero Dept<b>:&nbsp;</b> </label>
															<input name="txt_insurfollowup_premiumwithzerodept"
																id="txt_insurfollowup_premiumwithzerodept" type="text" class="form-control"
																value="<%=mybean.veh_insur_premiumwithzerodept%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_premiumwithzerodept',this,'hint_txt_insurfollowup_premiumwithzerodept')" />
															<span class="hint" id="hint_txt_insurfollowup_premiumwithzerodept"></span>
														</div>
														<!-- 												</div> -->


														<div class="form-element3">
														<label>Policy Expiry Date<b>:&nbsp;</b> </label>
															<input name="txt_insurfollowup_policyexpirydate"
																id="txt_insurfollowup_policyexpirydate"
																value="<%=mybean.veh_insur_policyexpirydate%>" size="12"
																onchange="SecurityCheck2('txt_insurfollowup_policyexpirydate',this,'hint_txt_insurfollowup_policyexpirydate')"
																 maxlength="14" class="form-control datepicker" type="text" />
															<span class="hint" id="hint_txt_insurfollowup_policyexpirydate"></span>
														</div>

													<div class="form-element3">
														<label>Current IDV<b>:&nbsp;</b> </label>
															<input name="txt_insurfollowup_currentidv"
																id="txt_insurfollowup_currentidv" type="text" class="form-control"
																value="<%=mybean.veh_insur_currentidv%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_currentidv',this,'hint_txt_insurfollowup_currentidv')" />
															<span class="hint" id="hint_txt_insurfollowup_currentidv"></span>
														</div>
														<!-- 												</div> -->

														<div class="form-element3">
														<label>Premium<b>:&nbsp;</b></label>
															<input name="txt_insurfollowup_premium"
																id="txt_insurfollowup_premium" type="text" class="form-control"
																value="<%=mybean.veh_insur_premium%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_premium',this,'hint_txt_insurfollowup_premium')" />
															<span class="hint" id="hint_txt_insurfollowup_premium"></span>
														</div>
														</div>

													<div class="row">
													<div class="form-element3">
														<label>NCB<b>:&nbsp;</b></label>
															<input name="txt_insurfollowup_ncb" id="txt_insurfollowup_ncb" type="text"
																class="form-control" value="<%=mybean.veh_insur_ncb%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_ncb',this,'hint_txt_insurfollowup_ncb')" />
															<span class="hint" id="hint_txt_insurfollowup_ncb"></span>
														</div>
														<!-- 												</div> -->

														<div class="form-element3">
														<label>Insurance Company Offered<b>:&nbsp;</b> </label>
															<input name="txt_insurfollowup_companyoffered"
																id="txt_insurfollowup_companyoffered" type="text" class="form-control"
																value="<%=mybean.veh_insur_compoffered%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_companyoffered',this,'hint_txt_insurfollowup_companyoffered')" />
															<span class="hint" id="hint_txt_insurfollowup_companyoffered"></span>
														</div>

													<div class="form-element3">
														<label>Plan Suggested<b>:&nbsp;</b> </label>
															<input name="txt_insurfollowup_plansuggested"
																id="txt_insurfollowup_plansuggested" type="text" class="form-control"
																value="<%=mybean.veh_insur_plansuggested%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_plansuggested',this,'hint_txt_insurfollowup_plansuggested')" />
															<span class="hint" id="hint_txt_insurfollowup_plansuggested"></span>
														</div>
														<!-- 												</div> -->

														<div class="form-element3">
														<label>Variant<b>:&nbsp;</b></label>
															<input name="txt_insurfollowup_variant"
																id="txt_insurfollowup_variant" type="text" class="form-control"
																value="<%=mybean.veh_insur_variant%>" size="25" maxlength="20"
																onchange="SecurityCheck2('txt_insurfollowup_variant',this,'hint_txt_insurfollowup_variant')" />
															<span class="hint" id="hint_txt_insurfollowup_variant"></span>
														</div></div>


													<div class="row">
													<div class="form-element3">
														<label> Reg. Number<b>:&nbsp; </b> </label>
															<input name="txt_insurfollowup_reg_no"
																id="txt_insurfollowup_reg_no" type="text" class="form-control"
																onChange="SecurityCheck2('txt_insurfollowup_reg_no',this,'hint_txt_insurfollowup_reg_no');"
																value="<%=mybean.veh_insur_reg_no%>" size="20" maxlength="20" />
															<span class="hint" id="hint_txt_insurfollowup_reg_no"></span>
														</div>
														<!-- 												</div> -->


														<div class="form-element3">
														<label> Chassis Number<b>:&nbsp;</b> </label>
															<input name="txt_insurfollowup_chassis_no" id="txt_insurfollowup_chassis_no"
																onChange="SecurityCheck2('txt_insurfollowup_chassis_no',this,'hint_txt_insurfollowup_chassis_no');"
																type="text" class="form-control"
																value="<%=mybean.veh_insur_chassis_no%>" size="20" maxlength="25" />
															<span class="hint" id="hint_txt_insurfollowup_chassis_no"></span>
														</div>

														<div class="form-element3">
															<label> Gift<b>:&nbsp;</b> </label>
															<select name="dr_insurenquiry_insurgift_id" class="form-control"
															id="dr_insurenquiry_insurgift_id" onChange="SecurityCheck2('dr_insurenquiry_insurgift_id',this,'hint_insurenquiry_insurgift_id');" >
															<%=mybean.PopulateGift()%>
															</select>
															<span class="hint" id="hint_insurenquiry_insurgift_id"></span>
														</div>
													
													</div>
														<div class="form-element6">
															<label>Customer Address<b>:&nbsp;</b></label>
															<textarea name="txt_insurfollowup_address" cols="50"
																rows="3" class="form-control"
																id="txt_insurfollowup_address"
																onchange="SecurityCheck2('txt_insurfollowup_address',this,'hint_txt_insurfollowup_address')"
																onKeyUp="charcount('txt_insurfollowup_address', 'span_txt_insurfollowup_address','<font color=red>({CHAR} characters left)</font>', '500')"><%=mybean.veh_insur_address%></textarea>
															<span id="span_txt_insurfollowup_address">(500 characters)</span> <br /> 
															<span class="hint" id="hint_txt_insurfollowup_address"></span>
														</div>
														<div class="form-element6">
															<label>Pick Up Address<b>:&nbsp;</b></label>
															<textarea name="txt_insurfollowup_pickupaddress" cols="50"
																rows="3" class="form-control"
																id="txt_insurfollowup_address"
																onchange="SecurityCheck2('txt_insurfollowup_pickupaddress',this,'hint_txt_insurfollowup_pickupaddress')"
																onKeyUp="charcount('txt_insurfollowup_pickupaddress', 'span_txt_insurfollowup_pickupaddress','<font color=red>({CHAR} characters left)</font>', '500')"><%=mybean.insur_pickupaddress%></textarea>
															<span id="hint_txt_insurfollowup_pickupaddress">(500 characters)</span> <br /> 
															<span class="hint" id="hint_txt_insurfollowup_pickupaddress"></span>
														</div> <div class="row"></div>
														<div class="form-element3">
															<label>City<font color="#ff0000">*</font>:&nbsp;
															</label> <select class="form-control select2" id="maincity"
																name="contactmaincity"
																onchange="SecurityCheck2('contactmaincity',this,'hint_insurfollowup_maincity')">
																<%=mybean.PopulateCity()%>
															</select>
															<div class="hint" id="hint_insurfollowup_maincity"></div>
														</div>

														<div class="form-element3 ">
															<label>Pin/Zip<font color="#ff0000">*</font>:&nbsp;
															</label> <input name="txt_insurfollowup_contact_pin" type="text"
																class="form-control" id="txt_insurfollowup_contact_pin"
																onKeyUp="toInteger('txt_insurfollowup_contact_pin','Pin')"
																onChange="SecurityCheck2('txt_insurfollowup_contact_pin',this,'hint_txt_insurfollowup_contact_pin')"
																value="<%=mybean.contact_pin%>" size="10" maxlength="6"></input>
															<div class="hint" id="hint_txt_insurfollowup_contact_pin"></div>
														</div>
												</form>
											</div>

										</div>
										 <div>
											<center>
											<div id="listfollowup" name="listfollowup"><%=mybean.followup_info%></div>
											</center>
										</div> 
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Add Follow-up</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<center>
													<font color='red'><b><div id="insurmsg" name="insurmsg"></div></b></font>
												</center><br></br>

												<!-- START PORTLET BODY -->
												<form class="form-horizontal" name="Frmcontact" method="post">
												<div class="container-fluid">
													<div class="form-element12" id="">
													<div id="contactable" class="form-element2 form-element-padding">
													<%=mybean.PopulateInsurFollowupFields(mybean.comp_id, "0", 0) %></div>
                                                    <div id="disprow1" class="form-element2 form-element-padding" hidden></div>
													<div id="disprow2" class="form-element2 form-element-padding" hidden></div>
													<div id="disprow3" class="form-element2 form-element-padding" hidden></div>
													<div id="disprow4" class="form-element2 form-element-padding" hidden></div>
													<div id="disprow5" class="form-element2 form-element-padding" hidden></div>
												</div>	
													<div id="followupinstructions" class="form-element12" hidden></div>
													<div id="nextfollowup" class="form-element12" hidden></div>
													<div id="followupcomments" class="form-element12" hidden></div>
													<input type="hidden" id='rowcount' name='rowcount' value='1' />
													
													<center>
														<input name="add_insurfollowup_button" type="button" onclick="holdfolllowupfields();"
															class="btn btn-success" id="add_insurfollowup_button" value="Add Follow-up" />
														<input type="hidden" name=insurenquiry_id id="insurenquiry_id" value="<%=mybean.insurenquiry_id%>" />
													</center>
											</div>		
												</form>
											</div>
										</div>
										<input id="currenttime" type="hidden" value='<%=mybean.currenttimevalidate%>' />
										
										
										
									</div>
									<div class="tab-pane" id="tabs-3"></div>
									<div class="tab-pane" id="tabs-8">
										<%=mybean.ListHistory(mybean.comp_id)%>
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
	
	
	<script type="text/javascript">
	/* $("#disprow1 .bootstrap-select").ready(function(){
		$("#disprow1 .bootstrap-select").addClass('form-element4');
		alert(22)
	}); */
	
			function insurvalidation(){
				var feedback_type = $("#dr_feedbacktype_id").val();
				var feedback_desc = $("#txt_feedback_desc").val();
				var nextfollowup_time = $("#txt_insurfollowup_time").val();
				var nextfollowup_type = $("#dr_nextfollowup_type_id").val();
				
				if(feedback_type == "0"){
					$("#feedbacktypeerrormsg").html("<font color='red'><b>Select Feedback Type!<b></font>");
					return false;
				}
				if(feedback_desc == ""){
					$("#feedbackdescerrormsg").html("<font color='red'><b>Enter Feedback!<b></font>");
					return false;
				}
				if(nextfollowup_time == ""){
					$("#insurnextfollowerrormsg").html("<font color='red'><b>Enter Next Followup Time!<b></font>");
					return false;
				}
				if(nextfollowup_type == "0"){
					$("#insurnextfollowtypeerrormsg").html("<font color='red'><b>Select Next Followup Type!<b></font>");
					return false;
				}}

		</script>
	
	<script language="JavaScript" type="text/javascript">
		function SecurityCheck(name, obj, hint) {
			var value;
			var insurenquiry_id = GetReplace(document.getElementById("insurenquiry_id").value);
			var insurenquiry_branch_id = <%=mybean.insurenquiry_branch_id%>;
			var url = "../insurance/insurance-enquiry-dash-check.jsp?";
			var param;
			var str = "123";
			value = GetReplace(obj.value);
			param = "name=" + name + "&value=" + value + "&insurenquiry_id=" + insurenquiry_id +"&insurenquiry_branch_id="+insurenquiry_branch_id;
			showHint(url + param, hint);
		}
	
		function RefreshHistory() {
			var insurenquiry_id = document.form1.insurenquiry_id.value;
		}
	
		function LoadInsuranceDash(tab) {
			var insurenquiry_id = document.getElementById("insurenquiry_id").value;
			if (tab == '3') {
				if (document.getElementById("tabs-3").innerHTML == '') {
					showHint('insurance-enquiry-dash-check.jsp?listpolicy=yes&insurenquiry_id=' + insurenquiry_id, 'tabs-3');
				}
			}
	}
		
		function SecurityCheck2(name, obj, hint) {
			var value;
			var insurenquiry_id = GetReplace(document.getElementById("insurenquiry_id").value);
			var url = "../insurance/vehicle-dash-insurance-followup-check.jsp?";
			var param;
			var str = "123";
			
			if(name!= "check_giftcombo" && name!= "check_rs1000" && name!= "check_servicecoupon" ){
				value = GetReplace(obj.value);
			}else{
				if (obj.checked == true) {
					value = "1";
				} else {
					value = "0";
				}
			}
			
			param = "name=" + name + "&value=" + value + "&insurenquiry_id=" + insurenquiry_id;
			showHint(url + param, hint);
		}
		
		function populateSob() {
			var insur_soe_id = document.getElementById('dr_insur_soe_id').value;
			showHint( "../insurance/mis-check.jsp?insurfollowup_sob_id=yes&insurfollowup_soe_id=" + insur_soe_id, "dr_insur_sob_id");
		}
		function PopulateCampaign(branch_id) {
			showHint("../insurance/mis-check.jsp?insurenquiry_branch_id=" + branch_id + "&insurcampaign=yes", "insurcampaign");
		}
		
		
	function test(){
	}
	
	
	function populatefolllowupfields(parentid_id, divid){
		var parentid = document.getElementById(parentid_id).value;
		$("#rowcount").val(divid);
		showHint('insurance-enquiry-dash-check.jsp?followupdetails=yes&parentid=' + parentid+'&divid='+divid.replace("disprow",""), divid );
		showHint('insurance-enquiry-dash-check.jsp?followupinstructions=yes&parentid=' + parentid+'&divid='+divid.replace("disprow",""), 'followupinstructions' );
		showHint('insurance-enquiry-dash-check.jsp?nextfollowup=yes&parentid=' + parentid+'&divid='+divid.replace("disprow",""), 'nextfollowup' );
		showHint('insurance-enquiry-dash-check.jsp?followupcomments=yes&parentid=' + parentid+'&divid='+divid.replace("disprow",""), 'followupcomments' );
	}
	
	function hidefields(divid){
		divid=divid.replace("dr_followup_disp","");
		for(i=divid;i<=5;i++){
			$('#disprow'+i).html('');
			$('#disprow'+i).hide();
		}
		$('#nextfollowup').hide();
		$('#followupcomments').hide();
		$('#followupinstructions').hide();
	}
	
	function holdfolllowupfields(){
		var insurenquiry_id = $("#insurenquiry_id").val();
		var dr_followup_disp1 = $("#dr_followup_disp1").val();
		var dr_followup_disp2 = $("#dr_followup_disp2").val();
		var dr_followup_disp3 = $("#dr_followup_disp3").val();
		var dr_followup_disp4 = $("#dr_followup_disp4").val();
		var dr_followup_disp5 = $("#dr_followup_disp5").val();
		var txt_followup_comment = $("#txt_followup_comment").val();
		var txt_next_followup_time = $("#txt_next_followup_time").val();
		
		if(txt_followup_comment == undefined){
			txt_followup_comment = "";
		}
		if(txt_next_followup_time == undefined){
			txt_next_followup_time = "";
		}
		
		
		showHint('insurance-enquiry-dash-check.jsp?validatefollowupdetails=yes&insurenquiry_id='+insurenquiry_id
				+'&dr_followup_disp1='+dr_followup_disp1
				+'&dr_followup_disp2='+dr_followup_disp2
				+'&dr_followup_disp3='+dr_followup_disp3
				+'&dr_followup_disp4='+dr_followup_disp4
				+'&dr_followup_disp5='+dr_followup_disp5
				+'&txt_followup_comment='+txt_followup_comment
				+'&txt_next_followup_time='+ txt_next_followup_time, 'insurmsg');
		
		setTimeout('disablebuttuon()', 400);
		
	}
	
	
	function disablebuttuon() {
		var message = $("#insurmsg").text();
		var insurenquiry_id = $("#insurenquiry_id").val();
		if (message == 'Follow-up added successfully!') {
	$('#disprow1, #disprow2, #disprow3, #disprow4, #disprow5, #txt_followup_comment,#followupinstructions, #nextfollowup, #followupcomments').hide();
	showHint('insurance-enquiry-dash-check.jsp?listfollowupdetails=yes&insurenquiry_id=' + insurenquiry_id, 'listfollowup' );
	showHint('insurance-enquiry-dash-check.jsp?followupdetails=yes&parentid=0&divid=0', 'contactable' );
		}
	}
	
	
	
	</script>

</body>
</html>
