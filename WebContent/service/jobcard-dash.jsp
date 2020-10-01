<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.JobCard_Dash" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>

<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>
<link REL="stylesheet" type="text/css" href="../Library/slider.css" />
<style>
#billtype .bootstrap-select button
{
	margin-top: 0px;
}
</style>
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">

	<%@include file="../portal/header.jsp"%>
	
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
							Jobcard Dashboard &gt; Job Card ID<b>:</b>&nbsp;<%=mybean.jc_id%></h1>
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
						<%
							if (!mybean.modal.equals("yes")) {
						%>
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="../service/jobcard.jsp">Job Cards</a>&gt;</li>
						<li><a href="../service/jobcard-list.jsp?all=yes">List
								Job Cards</a> &gt; <a
							href="../service/jobcard-list.jsp?jc_id=<%=mybean.jc_id%>">Job
								Card ID<b>:</b>&nbsp;<%=mybean.jc_id%></a></li>
						<%
							}
						%>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div id="dialog-modal-contact"></div>
				
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<input type="hidden" name="txt_veh_id" id="txt_veh_id"
								value="<%=mybean.veh_id%>"> <input type="hidden"
								name="txt_customer_id" id="txt_customer_id"
								value="<%=mybean.jc_customer_id%>">

							<div class="tabbable tabbable-tabdrop">
								<ul class="nav nav-tabs">
									<li onclick="LoadJCDash('1');" class="active"><a href="#tabs-1" data-toggle="tab">Job Card</a></li>
									<li onclick="LoadJCDash('2');"><a href="#tabs-2" data-toggle="tab">Customer</a></li>
									<li onclick="LoadJCDash('3');"><a href="#tabs-3" data-toggle="tab">Parts</a></li>
									<li onclick="LoadJCDash('4');"><a href="#tabs-4" data-toggle="tab">Check List</a></li>
									<li onclick="LoadJCDash('5');"><a href="#tabs-5" data-toggle="tab">Car Inventory</a></li>
									<li onclick="LoadJCDash('6');"><a href="#tabs-6" data-toggle="tab">Images</a></li>
									<li onclick="LoadJCDash('7');"><a href="#tabs-7" data-toggle="tab">Documents</a></li>
									<li onclick="LoadJCDash('8');"><a href="#tabs-8" data-toggle="tab">Man Hours</a></li>
									<li onclick="LoadJCDash('12');"><a href="#tabs-12" data-toggle="tab">Bill</a></li>
									<li onclick="LoadJCDash('13');"><a href="#tabs-13" data-toggle="tab">Invoices</a></li>
									<li onclick="LoadJCDash('14');"><a href="#tabs-14" data-toggle="tab">Receipts</a></li>
 									<li onclick="LoadJCDash('9');"><a href="#tabs-9" data-toggle="tab">PSF</a></li>
									<li onclick="LoadJCDash('10');"><a href="#tabs-10" data-toggle="tab">History</a></li>
								</ul>
								<div class="tab-content">
									<div class="tab-pane active" id="tabs-1">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">&nbsp;Job
													Card Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form class="form-horizontal">
														<div class="row">
															<div class="form-element6 ">
																	<label>Job Card No: &nbsp;</label>
																		<a href="../service/jobcard-list.jsp?jc_id=<%=mybean.jc_id%>"><b><%=mybean.jc_no%></b></a>
																		<input name="jc_id" type="hidden" id="jc_id"
																			value="<%=mybean.jc_id%>">
															</div>

															<div class="form-element6 ">
																	<label>Branch: &nbsp;</label>
																		<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.jc_branch_id%>"><%=mybean.branch_name%></a>
															</div>
														</div>

														<div class="row">
															<div class="form-element6 ">
																	<label>Time In: &nbsp;</label>
																		<input name="txt_jc_time_in" type="hidden"
																			class="form-control" id="txt_jc_time_in"
																			value="<%=mybean.date%>">
																		<%=mybean.date%>
															</div>

															<div class="form-element6">
																	<label>Promised Time<font color="#ff0000">*</font>: &nbsp; </label>
																			<input type="text" size="16"
																				name="txt_jc_time_promised"
																				id="txt_jc_time_promised"
																				value="<%=mybean.jc_promisetime%>"
																				class="form-control datetimepicker"
																				onChange="SecurityCheck('txt_jc_time_promised',this,'hint_txt_jc_time_promised');">
																		<div class="hint" id="hint_txt_jc_time_promised"></div>
															</div>
														</div>
														<div class="row">
															<div class="form-element6 ">
																	<label>Model: &nbsp;</label>
																		<b><%=mybean.jc_model_name%></b>
															</div>

															<div class="form-element6 ">
																	<label>Variant: &nbsp;</label>
																	 <b><%=mybean.jc_item_name%></b>
															</div>
														</div>
														<div class="row">
															<div class="form-element6 ">
																	<label>Customer: &nbsp;</label>
																		<b><%=mybean.link_customer_name%></b>
															</div>

															<div class="form-element6 ">
																	<label>Contact: &nbsp;</label>
																		<b><%=mybean.link_contact_name%></b>
															</div>
														</div>
														<div class="row">
															<div class="form-element6">
																<label>Warranty: &nbsp;</label>
																<b><%=mybean.jcwarranty%></b>
															</div>
															<div class="form-element6">
																<label>Reference Customer: &nbsp; </label>
																<b><%=mybean.link_refcustomer_name%></b>
															</div>
														</div>
														<div class="row ">
															<div class="form-element6 ">
																	<label>Chassis Number: &nbsp;</label>
																		<b><%=mybean.veh_chassis_no%></b>
															</div>
															<div class="form-element6 ">
																	<label>Engine No.: &nbsp;</label>
																		<b><%=mybean.veh_engine_no%></b>
															</div>
														</div>
														<div class="row ">
															<div class="form-element6 form-element-margin">
																	<label>Reg. No.: &nbsp;</label>
																		<b><%=mybean.veh_reg_no%></b>
															</div>
															<div class="form-element6">
																	<label>Kms<font color="#ff0000">*</font>: &nbsp; </label>
																		<input name="txt_jc_kms" type="text"
																			class="form-control" id="txt_jc_kms"
																			value="<%=mybean.jc_kms%>" size="20" maxlength="25"
																			onkeyup="toInteger(this.id);"
																			onchange="SecurityCheck('txt_jc_kms',this,'hint_txt_jc_kms');">
																		<div class="hint" id="hint_txt_jc_kms"></div>
															</div>
														</div>
														<div class="row ">
															<div class="form-element6">
																	<label>Fuel Guage<font color="#ff0000">*</font>: &nbsp;
																	</label>
																		<input type="hidden" id="txt_jc_fuel_guage"
																			name="txt_jc_fuel_guage"
																			value="<%=mybean.jc_fuel_guage%>"
																			onChange="SecurityCheck('txt_jc_fuel_guage',this,'hint_txt_jc_fuel_guage');" />
																		<b><span name="div_jc_fuel_guage"
																			id="div_jc_fuel_guage"></span>%</b>
																		<div id="div_fuel_guage" class="col-md-6 form-control"></div>
																		<div class="col-md-12">
																			<div class="col-md-1 col-xs-1">0%</div>
																			<div class="col-md-offset-4 col-xs-offset-4 col-md-2 col-xs-2" style="text-align:center">    50%</div>
																			<div class="col-md-offset-4 col-xs-offset-4 col-md-1 col-xs-1" style="text-align:right">100%</div>
																		</div>
																		<div class="hint" id="hint_txt_jc_fuel_guage"></div>
															</div>
															<div class="form-element6">
																	<label>Type<font color="#ff0000">*</font>: &nbsp; </label>
																		<select name="dr_jc_type_id" id="dr_jc_type_id"
																			class="form-control"
																			onChange="SecurityCheck('dr_jc_type_id',this,'hint_dr_jc_type_id');">
																			<%=mybean.PopulateType()%>
																		</select>
																		<div class="hint" id="hint_dr_jc_type_id"></div>
															</div>
														</div>
														<div class="row ">
															<div class="form-element6">
																	<label>Category<font color="#ff0000">*</font>: &nbsp; </label>
																		<select name="dr_jc_cat_id" id="dr_jc_cat_id"
																			class="form-control"
																			onChange="SecurityCheck('dr_jc_cat_id',this,'hint_dr_jc_cat_id');">
																			<%=mybean.PopulateCategory()%>
																		</select>
																		<div class="hint" id="hint_dr_jc_cat_id"></div>
															</div>
															<div class="form-element6">
																	<label>Title<font color="#ff0000">*</font>: &nbsp; </label>
																		<input name="txt_jc_title" type="text"
																			class="form-control" id="txt_jc_title"
																			value="<%=mybean.jc_title%>" size="52"
																			maxlength="255"
																			onchange="SecurityCheck('txt_jc_title',this,'hint_txt_jc_title')" />
																		<div class="hint" id="hint_txt_jc_title"></div>
															</div>
														</div>

														<div class="row ">
															<div class="form-element6">
																	<label>Communication<font color="#ff0000">*</font>: &nbsp; </label>
																		<select name="dr_jc_comm_contact_id"
																			id="dr_jc_comm_contact_id" class="form-control"
																			onChange="SecurityCheck('dr_jc_comm_contact_id',this,'hint_dr_jc_comm_contact_id');">
																			<%=mybean.PopulateCustomerContacts(mybean.jc_customer_id, mybean.jc_comm_contact_id, mybean.comp_id)%>
																		</select>
																		<div class="hint" id="hint_dr_jc_comm_contact_id"></div>
																	</div>
																	<div class="form-element6">
																	<label>Customer Voice<font color="#ff0000">*</font>: &nbsp; </label>
																		<textarea name="txt_jc_cust_voice"
																			id="txt_jc_cust_voice" cols="50" rows="5"
																			class="form-control"
																			onChange="SecurityCheck('txt_jc_cust_voice',this,'hint_txt_jc_cust_voice');"><%=mybean.jc_cust_voice%></textarea>
																		<div class="hint" id="hint_txt_jc_cust_voice"></div>
															</div>
														</div>
														<div class="row ">
															<div class="form-element6">
																	<label>Service Advice: &nbsp; </label>
																		<textarea name="txt_jc_advice" id="txt_jc_advice"
																			cols="50" rows="5" class="form-control"
																			onChange="SecurityCheck('txt_jc_advice',this,'hint_txt_jc_advice');"><%=mybean.jc_advice%></textarea>
																		<div class="hint" id="hint_txt_jc_advice"></div>
															</div>
															<div class="form-element6">
																	<label>Service Instruction: &nbsp; </label>
																		<textarea name="txt_jc_instr" id="txt_jc_instr"
																			cols="50" rows="5" class="form-control"
																			onChange="SecurityCheck('txt_jc_instr',this,'hint_txt_jc_instr');"><%=mybean.jc_instr%></textarea>
																		<div class="hint" id="hint_txt_jc_instr"></div>
															</div>
														</div>
														<div class="row ">
															<div class="form-element6">
																	<label>Terms: &nbsp; </label>
																	 <textarea name="txt_jc_terms" cols="50" rows="4"
																			class="form-control" id="txt_jc_terms"
																			onChange="SecurityCheck('txt_jc_terms',this,'hint_txt_jc_terms')"><%=mybean.jc_terms%></textarea>
																		<div class="hint" id="hint_txt_jc_terms"></div>
															</div>
														</div>
													</form>
												</div>
											</div>
										</div>
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Status</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form class="form-horizontal">
														<div class="row ">
															<div class="form-element6 ">
																	<label>Critical: &nbsp; </label>
																		<input id="chk_jc_critical" type="checkbox"
																			name="chk_jc_critical"
																			<%=mybean.PopulateCheck(mybean.jc_critical)%>
																			onclick="SecurityCheck('chk_jc_critical',this,'hint_chk_jc_critical');" />
																		<div class="hint" id="hint_chk_jc_critical"></div>
															</div>

															<div class="form-element6 ">
																	<label>Ready Time: &nbsp; </label>
																		<span id="span_readytime"><%=mybean.jc_readytime%></span>
															</div>
														</div>
														<div class="row ">
															<div class="form-element6">
																	<label>Stage<font color="#ff0000">*</font>: &nbsp; </label>
																		<select name="dr_jc_stage_id" id="dr_jc_stage_id"
																			class="form-control"
																			onchange="SecurityCheckStage('dr_jc_stage_id',this,'hint_dr_jc_stage_id')">
																			<%=mybean.PopulateStage()%>
																		</select>
																		<div class="hint" id="hint_dr_jc_stage_id"></div>
															</div>

															<div class="form-element6 form-element-margin">
																	<label>Delivered Time: &nbsp; </label>
																		<span id="span_deliveredtime"><%=mybean.jc_timeout%></span>
																	</div>
														</div>
														<div class="row ">
															<div class="form-element6">
																	<label>Posted Invoice Date: &nbsp; </label>
																			<input type="text"  
																				name="txt_jc_time_posted" id="txt_jc_time_posted"
																				value="<%=mybean.jc_postedtime%>"
																				class="form-control datetimepicker"
																				onChange="SecurityCheck('txt_jc_time_posted',this,'hint_txt_jc_time_posted');" />
																			<div class="hint" id="hint_txt_jc_time_posted"></div>
															</div>
															<div class="form-element6">
																	<label>Service Advisor<font color="#ff0000">*</font>: &nbsp; </label>
																		<select name="dr_jc_emp_id" id="dr_jc_emp_id"
																			class="form-control"
																			onchange="SecurityCheck('dr_jc_emp_id',this,'hint_dr_jc_emp_id');">
																			<%=mybean.PopulateExecutive()%>
																		</select>
																		<div class="hint" id="hint_dr_jc_emp_id"></div>
															</div>
														</div>

														<div class="row ">
															<div class="form-element6">
																	<label>Technician: &nbsp; </label>
																		<select name="dr_jc_technician_emp_id"
																			id="dr_jc_technician_emp_id" class="form-control"
																			onchange="SecurityCheck('dr_jc_technician_emp_id',this,'hint_dr_jc_technician_emp_id');">
																			<%=mybean.PopulateTechnician()%>
																		</select>
																		<div class="hint" id="hint_dr_jc_technician_emp_id"></div>
															</div>
															<div class="form-element6">
																	<label>Bay: &nbsp; </label>
																		<select name="dr_bay" id="dr_bay" class="form-control"
																			onchange="SecurityCheck('dr_bay',this,'hint_dr_bay');">
																			<%=mybean.PopulateBay()%>
																		</select>
																		<div class="hint" id="hint_dr_bay"></div>
															</div>
														</div>
														<div class="row ">
															<div class="form-element6">
																	<label>Inventory Location<font color="#ff0000">*</font>: &nbsp; </label>
																		<select name="dr_location" id="dr_location"
																			class="form-control"
																			onchange="SecurityCheck('dr_location',this,'hint_dr_location');">
																			<%=mybean.PopulateInventoryLocation()%>
																		</select>
																		<div class="hint" id="hint_dr_location"></div>
															</div>
															<div class="form-element6">
																	<label>RO No.<font color="#ff0000">*</font>: &nbsp; </label>
																		<input name="txt_jc_ro_no2" type="text"
																			class="form-control" id="txt_jc_ro_no2"
																			value="<%=mybean.jc_ro_no%>" size="20"
																			maxlength="20"
																			onchange="SecurityCheckStage('txt_jc_ro_no',this,'hint_txt_jc_ro_no');" />
																		<div class="hint" id="hint_txt_jc_ro_no"></div>
															</div>

														</div>
														<div class="row ">
															<div class="form-element6">
																	<label>Priority<font color="#ff0000">*</font>: &nbsp; </label>
																		<select name="dr_jc_priorityjc_id"
																			class="form-control" id="dr_jc_priorityjc_id"
																			onchange="SecurityCheck('dr_jc_priorityjc_id',this,'hint_dr_jc_priorityjc_id');">
																			<%=mybean.PopulatePriority()%>
																		</select> <br />
																		<div class="hint" id="hint_dr_jc_priorityjc_id"></div>
															</div> 
															<div class="form-element6">
																	<label>Notes: &nbsp; </label>
																		<textarea name="txt_jc_notes" cols="70" rows="4"
																			class="form-control" id="txt_jc_notes"
																			onchange="SecurityCheck('txt_jc_notes',this,'hint_txt_jc_notes')"><%=mybean.jc_notes%></textarea>
																		<div class="hint" id="hint_txt_jc_notes"></div>
															</div>
														</div>
														<div class="row ">
															<div class="form-element6 ">
																	<label>Entry By: &nbsp; </label>
																		<%=mybean.entry_by%>
																		<input type="hidden" name="entry_by"
																			value="<%=mybean.entry_by%>">
															</div>
															<div class="form-element6 ">
																	<label>Entry Date: &nbsp; </label>
																		<%=mybean.entry_date%>
																		<input type="hidden" name="entry_date"
																			value="<%=mybean.entry_date%>">
															</div>
														</div>
														<%
															if (mybean.modified_by != null && !mybean.modified_by.equals("")) {
														%>
														<div class="row ">
															<div class="form-element6 ">
																	<label>Modified By: &nbsp; </label>
																		<%=mybean.modified_by%>
																		<input type="hidden" name="modified_by"
																			value="<%=mybean.modified_by%>">
															</div>
															<div class="form-element6 ">
																	<label>Modified Date: &nbsp; </label>
																		<%=mybean.modified_date%>
																		<input type="hidden" name="modified_date"
																			value="<%=mybean.modified_date%>">
															</div>
														</div>
														<%
															}
														%>

													</form>
												</div>
											</div>
										</div>


									</div>
									<div class="tab-pane" id="tabs-2">
										<form class="form-horizontal" name="Frmcontact" method="post"
											action="jobcard-dash.jsp?jc_id=<%=mybean.veh_id%>#tabs-2">


											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Customer
														Details</div>
												</div>
												<div class="portlet-body portlet-empty">

													<%=mybean.StrCustomerDetails%>
													<%=mybean.contactdetails%>

												</div>
											</div>


											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Add Contact
														Person</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<center>
														<font color="red"><%=mybean.contact_msg%></font>
													</center>

													<div class="form-element12">
													<div class="form-element2 form-element">
														<label> Contact<font color=red>*</font>: </label>
																<select name="dr_new_contact_title_id"
																	class="form-control" id="dr_new_contact_title_id">
																	<%=mybean.PopulateTitle(mybean.new_contact_title_id)%>
																</select> Title
															</div>
															<div  class="form-element5 form-element-margin ">
																<input name="txt_new_contact_fname" type="text"
																	class="form-control" id="txt_new_contact_fname"
																	value="<%=mybean.new_contact_fname%>" size="32"
																	maxlength="255" />First Name
															</div>
															<div class="form-element5 form-element-margin form-element">
																<input name="txt_new_contact_lname" type="text"
																	class="form-control" id="txt_new_contact_lname"
																	value="<%=mybean.new_contact_lname%>" size="32"
																	maxlength="255" />Last Name
															</div>
														</div>
													<div class="form-element6">
														<label>Mobile 1<font color="#ff0000">*</font>: &nbsp; </label>
															<input name="txt_new_contact_mobile1" type="text"
																class="form-control" id="txt_new_contact_mobile1"
																value="<%=mybean.new_contact_mobile1%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile1','Mobile 1')" />
															(91-9999999999)
													</div>


													<div class="form-element6">
														<label>Mobile 2: &nbsp; </label>
															<input name="txt_new_contact_mobile2" type="text"
																class="form-control" id="txt_new_contact_mobile2"
																value="<%=mybean.new_contact_mobile2%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile2','Mobile 2')" />
															(91-9999999999)
													</div>
													<div class="form-element6">
														<label>Mobile 3: &nbsp; </label>
															<input name="txt_new_contact_mobile3" type="text"
																class="form-control" id="txt_new_contact_mobile3"
																value="<%=mybean.new_contact_mobile3%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile3','Mobile 3')" />
															(91-9999999999)
													</div>
													<div class="form-element6">
														<label>Mobile 4: &nbsp; </label>
															<input name="txt_new_contact_mobile4" type="text"
																class="form-control" id="txt_new_contact_mobile4"
																value="<%=mybean.new_contact_mobile4%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile4','Mobile 4')" />
															(91-9999999999)
													</div>
													<div class="form-element6">
														<label>Mobile 5: &nbsp; </label>
															<input name="txt_new_contact_mobile5" type="text"
																class="form-control" id="txt_new_contact_mobile5"
																value="<%=mybean.new_contact_mobile5%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile5','Mobile 5')" />
															(91-9999999999)
													</div>
													<div class="form-element6">
														<label>Mobile 6: &nbsp; </label>
															<input name="txt_new_contact_mobile6" type="text"
																class="form-control" id="txt_new_contact_mobile6"
																value="<%=mybean.new_contact_mobile6%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile6','Mobile 6')" />
															(91-9999999999)
													</div>

													<div class="form-element6">
														<label>Email 1<font color="#ff0000">*</font>: &nbsp; </label>
															<input name="txt_new_contact_email1" type="text"
																class="form-control" id="txt_new_contact_email1"
																value="<%=mybean.new_contact_email1%>" size="32"
																maxlength="50" />
													</div>
													<div class="form-element6">
														<label>Notes: &nbsp; </label> 
															<textarea name="txt_new_contact_notes" cols="70" rows="4"
																class="form-control" id="txt_new_contact_notes"><%=mybean.new_contact_notes%></textarea>
													</div>

													<div class="form-element6">
														<label>Type: &nbsp; </label>
															<select id="dr_new_contact_contacttype_id"
																name="dr_new_contact_contacttype_id"
																class="form-control">
																<%=mybean.PopulateContactType()%>
															</select>
													</div>

													<center>
													<div class="form-element12 form-center">
														<input name="add_contact_button" type="submit"
															class="btn btn-success" id="add_contact_button"
															value="Add Contact" />
</div>
													</center>


												</div>
											</div>
										</form>
									</div>

									<div class="tab-pane" id="tabs-3">
										<!-- CASH PARTS -->
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Parts</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<input type="hidden" name="txt_gst_type" id="txt_gst_type" value="<%=mybean.gst_type %>" />
													<TABLE class="table">
														<TR>
															<td width="50%" height="1000" align="center" valign="top"
																scope="col"><table width="100%" border="0"
																	cellpadding="0" cellspacing="0" class="table">
																	<tr>
																		<td scope="col"><div id="jcitem_details"></div></td>
																	</tr>
																</table></td>
															<td width="50%" align="center" valign="top" scope="col">
																<table width="100%" align="center" cellpadding="0" cellspacing="0" class="table">
																	<tr>
																		<td align="left" valign="top">
																			Bill Type<font color=red>*</font>:
																			<select id="dr_jctrans_billtype" name="dr_jctrans_billtype" class="form-control">
																				<%=mybean.PopulateBillType(mybean.jctrans_billtype_id)%>
																			</select>
																		</td>
																		
																		<td align="left" valign="top">
																			Rate Class Type<font color=red>*</font>:
																			<select name="dr_rateclass_type" id="dr_rateclass_type"
																				class="form-control" style="margin-top: 9px;"
																				 onchange="PopulateBranchClass();" visible="true">
																				<%=mybean.itemPriceUpdate.PopulateBranchClassType()%>
																			</select>
																		</td>
																		<td align="left" valign="top">
																			Rate Class<font color=red>*</font>:
																			<span id="rateclass">
																				<%=mybean.itemPriceUpdate.PopulateBranchClass("1",mybean.jc_branch_id,mybean.jc_id)%>
																			</span>
																		</td>
																		
																	</tr>
																	<tr>
																		<td valign="top" align=left colspan='3'>Search Items:
																			<input type="hidden" name="txt_sales_rateclass_id"
																			id="txt_sales_rateclass_id"
																			value="<%=mybean.rateclass_id%>" /> <input
																			type="hidden" name="jcitem_id" id="jcitem_id" /> <!-- 																			 <input -->
																			<!-- 																			type="hidden" name="txt_jc_id" id="txt_jc_id" -->
																			<%-- 																			value="<%=mybean.jc_id%>" /> --%>
																			<input type="hidden" name="txt_branch_id"
																			id="txt_branch_id" value="<%=mybean.jc_branch_id%>" />
																			<input name="txt_search" type="text"
																			class="form-control" id="txt_search" value=""
																			size="30" maxlength="255" onKeyUp="ItemSearch(false);" />
																			<div class="admin-master">
																				<a href="../inventory/inventory-item-list.jsp?all=yes" title="Manage Item"></a>
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td valign="top" align=left colspan='3'><div class="hint"
																				id="hint_search_item">Enter your search parameter!</div></td>
																	</tr>
																	<tr>
																		<td valign="top" colspan='3'>
																			<div class="table-responsive table-bordered">
																				<table class="table table-bordered table-hover table-responsive" style="margin-bottom: 15em;" data-filter="#filter">
																					<thead>
																						<tr>
																							<th align="center" colspan="7"><div id='billtype_name'>Item Details</div></th>
																						</tr>
																					</thead>
																					<tbody>
																						<tr>
																							<td colspan="6" align="center" valign="top">
																								<div style="display: inline" id="item_name"></div>
																								<input name="txt_item_id" type="hidden" id="txt_item_id" />
																								<input type="hidden" name="txt_current_qty" id="txt_current_qty" />
																								<input type="text" name="txt_rateclass_id" id="txt_rateclass_id" hidden />
																							</td>
																						</tr>
																						<tr>
																							<td align="left" valign="top">Quantity:
																								<input name="txt_item_qty" type="text" class="form-control" id="txt_item_qty" size="10" maxlength="10" onKeyUp="CalItemTotal();" />
																								<input type="hidden" id="emp_jc_priceupdate" name="emp_jc_priceupdate" value="<%=mybean.emp_jc_priceupdate%>" />
																								<input type="hidden" id="jc_location_id" name="jc_location_id" value="<%=mybean.jc_location_id%>" />
																								<input type="hidden" id="emp_jc_discountupdate" name="emp_jc_discountupdate" value="<%=mybean.emp_jc_discountupdate%>" />
																								<input type="hidden" id="txt_item_baseprice" name="txt_item_baseprice" />
																								<input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>" />
																								<input type="hidden" id="txt_mode" name="txt_mode" />
																								<input type="hidden" id="txt_itemprice_updatemode" name="txt_itemprice_updatemode" />
																								<input type="hidden" id="txt_item_type_id" name="txt_item_type_id" />
																								<input type="hidden" id="txt_item_ticket_dept_id" name="txt_item_ticket_dept_id">
																								<input type="hidden" id="txt_item_pricevariable" name="txt_item_pricevariable" />
																								<input type="hidden" id="txt_serial" name="txt_serial" />
																								<input type="hidden" id="txt_type" name="txt_type" />
																								<input type="hidden" id="txt_jctrans_billtype" name="txt_jctrans_billtype" />
																							</td>
																							<td align="left" valign="top">Price:
																								<input name="txt_item_price" type="text" class="form-control" id="txt_item_price" size="10" maxlength="10" onKeyUp="CalItemTotal();" onChange="CheckBasePrice();" />
																							</td>
																							<td align="left" valign="top">Discount:
																								<input name="txt_item_disc" type="text" class="form-control" id="txt_item_disc" size="10" maxlength="10" onKeyUp="CalItemTotal();" />
																							</td>
																							
																							<td align="left" valign="top" nowrap>
																								<input name="txt_item_tax1" type="hidden" id="txt_item_tax1" />
																							 	<input name="txt_item_tax_id1" type="hidden" id="txt_item_tax_id1" />
																								<div style="display: inline" id="tax_name1"></div>
																								<div style="display: inline" id="item_tax1"></div><br />
																								<input name="txt_item_tax2" type="hidden" id="txt_item_tax2" />
																							 	<input name="txt_item_tax_id2" type="hidden" id="txt_item_tax_id2" />
																								<div style="display: inline" id="tax_name2"></div>
																								<div style="display: inline" id="item_tax2"></div><br />
																							</td>
																							<td align="center" valign="top">
																								<input name="txt_item_total" type="hidden" id="txt_item_total" />
																								<b>Total: &nbsp; <br /> <div style="display: inline" id="item_total">0</div> </b>
																							</td>
																							<td align="center" valign="top">
																								<div id="mode_button">
																									<input name="add_button" id="add_button" type="button" class="btn btn-success" value="Add" onClick="Addjcitem();" />
																								</div>
																							</td>
																						</tr>
																						<tr id="serial_details" style="display: none">
																							<td align="right" valign="top" nowrap>Serial No.<font color="#ff0000">*</font>: &nbsp; </td>
																							<td align="left" valign="top" colspan="5">
																								<input name="txt_item_serial" type="text" class="form-control" id="txt_item_serial" size="20" maxlength="30" />
																							</td>
																						</tr>
																						<tr>
																							<td colspan="6"><span id="configure-details"></span></td>
																						</tr>
																					</tbody>
																				</table>
																			</div>
																		</td>
																	</tr>
																</table>
															</td>
														</TR>
													</TABLE>

												</div>
											</div>
										</div>
										
									</div>
									<div class="tab-pane" id="tabs-4">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Check list</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<%=mybean.jc_check%>

												</div>
											</div>
										</div>
									</div>
									<div class="tab-pane" id="tabs-5">
										<center>
											<font color="#ff0000"><b><%=mybean.msg%></b></font>
										</center>
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Car Inventory</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form class="form-horizontal">
														<%=mybean.jc_inventory%>
														<input name="txt_jc_id" type="hidden" id="txt_jc_id"
															value="<%=mybean.jc_id%>" />
													</form>
												</div>
											</div>
										</div>
									</div>
									<div class="tab-pane" id="tabs-6">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Image</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<div style="float: right">
														<a
															href="jobcard-click-image.jsp?add=yes&amp;jc_id=<%=mybean.jc_id%>"
															target="_blank">Click Image...</a>&nbsp;&nbsp;<a
															href="../service/jobcard-image-update.jsp?add=yes&jc_id=<%=mybean.jc_id%>">Add
															New Image...</a>
													</div>
													<div class="container-fluid">
														<div class="col-md-12 col-xs-12">
															<%=mybean.jc_images%>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>

									<div class="tab-pane" id="tabs-7">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Document</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<div style="float: right">
														<a
															href="../portal/docs-update.jsp?add=yes&jc_id=<%=mybean.jc_id%>">Add
															New Document...</a><br />
													</div>
													<%=mybean.jc_docs%>
												</div>
											</div>
										</div>
									</div>
									<div class="tab-pane" id="tabs-8">
										<font color="#ff0000"><b><%=mybean.msg%></b></font>
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Man Hours</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													&nbsp;
													<div id="dialog-modal-man-hour"></div>
													<div id="div_manhours"><%=mybean.jc_manhours%></div>

												</div>
											</div>
										</div>
									</div>
									<div class="tab-pane" id="tabs-12">
<!-- 										CASH BILL PARTS DETAILS -->
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">&nbsp;Cash Bill Parts Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form class="form-horizontal">
													<div class="row">
														<div class="form-element6">
															<label>Cash Customer Name: &nbsp; </label>
															<input name="txt_jc_bill_cash_customername" type="text"
																class="form-control" id="txt_jc_bill_cash_customername"
																value="<%=mybean.jc_bill_cash_customername%>" size="32"
																maxlength="255"
																onChange="SecurityCheck('txt_jc_bill_cash_customername',this,'hint_txt_jc_bill_cash_customername');" />
															<div class="hint" id="hint_txt_jc_bill_cash_customername"></div>
														</div>
													</div>
													<div class="row">
														<div class="form-element3">
																<label>Cash Bill Date<font color="#ff0000">*</font>: </label>
																	<input name="txt_jc_bill_cash_date" type="text"
																		class="form-control datepicker"
																		 id="txt_jc_bill_cash_date"
																		value="<%=mybean.jc_bill_cash_date%>"
                                                                            onChange="SecurityCheck('txt_jc_bill_cash_date',this,'hint_txt_jc_bill_cash_date');" />
																	<div class="hint" id="hint_txt_jc_bill_cash_date"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill No: </label>
																	<input name="txt_jc_bill_cash_no" type="text"
																		class="form-control" id="txt_jc_bill_cash_no"
																		value="<%=mybean.jc_bill_cash_no%>" size="20"
																		maxlength="50" 
																		onChange="SecurityCheck('txt_jc_bill_cash_no',this,'hint_txt_jc_bill_cash_no'); " />
																	<div class="hint" id="hint_txt_jc_bill_cash_no"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Parts: &nbsp; </label>
																	<input id="txt_jc_bill_cash_parts" type="text"
																		class="form-control" name="txt_jc_bill_cash_parts"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts','Amount')"
																		value="<%=mybean.jc_bill_cash_parts%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts',this,'hint_txt_jc_bill_cash_parts');" />
																	<div class="hint" id="hint_txt_jc_bill_cash_parts"></div>
														</div>
														
														<div class="form-element3">
																<label>Cash Bill Parts Tyre Qty: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_tyre_qty" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_tyre_qty"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_tyre_qty','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_tyre_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_tyre_qty',this,'hint_txt_jc_bill_cash_parts_tyre_qty');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_tyre_qty"></div>
														</div>
													</div>
													<div class="row ">
														<div class="form-element3">
																<label>Cash Bill Parts Tyre: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_tyre" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_tyre"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_tyre','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_tyre%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_tyre',this,'hint_txt_jc_bill_cash_parts_tyre');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_tyre"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Parts Oil: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_oil" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_oil"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_oil','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_oil%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_oil',this,'hint_txt_jc_bill_cash_parts_oil');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_oil"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Parts Accessories: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_accessories" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_accessories"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_accessories','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_accessories%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_accessories',this,'hint_txt_jc_bill_cash_parts_accessories');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_accessories"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Parts Value Add: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_valueadd" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_valueadd"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_valueadd','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_valueadd%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_valueadd',this,'hint_txt_jc_bill_cash_parts_valueadd');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_valueadd"></div>
														</div>
													</div>
													<div class="row ">
														<div class="form-element3">
																<label>Cash Bill Parts Battery Qty: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_battery_qty" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_battery_qty"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_battery_qty','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_battery_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_battery_qty',this,'hint_txt_jc_bill_cash_parts_battery_qty');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_battery_qty"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Parts Battery: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_battery" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_battery"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_battery','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_battery%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_battery',this,'hint_txt_jc_bill_cash_parts_battery');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_battery"></div>
														</div>
														
														<div class="form-element3">
																<label>Cash Bill Parts Brake Qty: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_brake_qty" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_brake_qty"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_brake_qty','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_brake_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_brake_qty',this,'hint_txt_jc_bill_cash_parts_brake_qty');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_brake_qty"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Parts Brake: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_brake" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_brake"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_brake','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_brake%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_brake',this,'hint_txt_jc_bill_cash_parts_brake');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_brake"></div>
														</div>
													</div>
													<div class="row ">	
														<div class="form-element3">
																<label>Cash Bill Parts Extented Warranty Qty: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_extwarranty_qty" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_extwarranty_qty"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_extwarranty_qty','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_extwarranty_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_extwarranty_qty',this,'hint_txt_jc_bill_cash_parts_extwarranty_qty');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_extwarranty_qty"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Parts Extented Warranty: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_extwarranty" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_extwarranty"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_brake','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_extwarranty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_extwarranty',this,'hint_jc_bill_cash_parts_extwarranty');">
																	<div class="hint" id="hint_jc_bill_cash_parts_extwarranty"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Parts Wheel Alignment: &nbsp; </label>
																	<input name="txt_jc_bill_cash_parts_wheelalign" type="text"
																		class="form-control" id="txt_jc_bill_cash_parts_wheelalign"
																		onKeyUp="toFloat('txt_jc_bill_cash_parts_wheelalign','Amount')"
																		value="<%=mybean.jc_bill_cash_parts_wheelalign%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_parts_wheelalign',this,'hint_txt_jc_bill_cash_parts_wheelalign');">
																	<div class="hint" id="hint_txt_jc_bill_cash_parts_wheelalign"></div>
														</div>
														<div class="form-element3">
															<label>Cash Bill Parts CNG: &nbsp; </label>
																<input name="txt_jc_bill_cash_parts_cng" type="text"
																	class="form-control" id="txt_jc_bill_cash_parts_cng"
																	onKeyUp="toFloat('txt_jc_bill_cash_parts_cng','Amount')"
																	value="<%=mybean.jc_bill_cash_parts_cng%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_cash_parts_cng',this,'hint_txt_jc_bill_cash_parts_cng');">
																<div class="hint" id="hint_txt_jc_bill_cash_parts_cng"></div>
														</div>
													</div>
													<div class="row ">
															<div class="form-element3">
															<label>Cash Bill Parts Discount: &nbsp; </label>
																<input name="txt_jc_bill_cash_parts_discamt" type="text"
																	class="form-control" id="txt_jc_bill_cash_parts_discamt"
																	onKeyUp="toFloat('txt_jc_bill_cash_parts_discamt','Amount')"
																	value="<%=mybean.jc_bill_cash_parts_discamt%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_cash_parts_discamt',this,'hint_txt_jc_bill_cash_parts_discamt');">
																<div class="hint" id="hint_txt_jc_bill_cash_parts_discamt"></div>
														</div>
													</div>
													</form>

												</div>
											</div>
										</div>
										
										<!-- 										CASH BILL LABOUR DETAILS -->
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">&nbsp;Cash Bill Labour Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form class="form-horizontal">
													
													<div class="row ">
														<div class="form-element3">
																<label>Cash Bill Labour: &nbsp; </label>
																	<input id="txt_jc_bill_cash_labour" type="text"
																		class="form-control" name="txt_jc_bill_cash_labour"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour','Amount')"
																		value="<%=mybean.jc_bill_cash_labour%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour',this,'hint_txt_jc_bill_cash_labour');" />
																	<div class="hint" id="hint_txt_jc_bill_cash_labour"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Labour Tyre Qty: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_tyre_qty" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_tyre_qty"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_tyre_qty','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_tyre_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_tyre_qty',this,'hint_txt_jc_bill_cash_labour_tyre_qty');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_tyre_qty"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Labour Tyre: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_tyre" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_tyre"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_tyre','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_tyre%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_tyre',this,'hint_txt_jc_bill_cash_labour_tyre');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_tyre"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Labour Oil: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_oil" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_oil"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_oil','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_oil%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_oil',this,'hint_txt_jc_bill_cash_labour_oil');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_oil"></div>
														</div>
													</div>
														
													<div class="row ">
														<div class="form-element3">
																<label>Cash Bill Labour Accessories: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_accessories" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_accessories"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_accessories','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_accessories%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_accessories',this,'hint_txt_jc_bill_cash_labour_accessories');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_accessories"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Labour Value Add: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_valueadd" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_valueadd"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_valueadd','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_valueadd%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_valueadd',this,'hint_txt_jc_bill_cash_labour_valueadd');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_valueadd"></div>
														</div>

														<div class="form-element3">
																<label>Cash Bill Labour Battery Qty: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_battery_qty" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_battery_qty"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_battery_qty','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_battery_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_battery_qty',this,'hint_txt_jc_bill_cash_labour_battery_qty');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_battery_qty"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Labour Battery: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_battery" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_battery"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_battery','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_battery%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_battery',this,'hint_txt_jc_bill_cash_labour_battery');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_battery"></div>
														</div>
													</div>
													<div class="row ">
														<div class="form-element3">
																<label>Cash Bill Labour Brake Qty: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_brake_qty" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_brake_qty"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_brake_qty','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_brake_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_brake_qty',this,'hint_txt_jc_bill_cash_labour_brake_qty');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_brake_qty"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Labour Brake: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_brake" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_brake"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_brake','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_brake%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_brake',this,'hint_txt_jc_bill_cash_labour_brake');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_brake"></div>
														</div>
													
														<div class="form-element3">
																<label>Cash Bill Labour Extented Warranty Qty: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_extwarranty_qty" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_extwarranty_qty"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_extwarranty_qty','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_extwarranty_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_extwarranty_qty',this,'hint_txt_jc_bill_cash_labour_extwarranty_qty');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_extwarranty_qty"></div>
														</div>
														<div class="form-element3">
																<label>Cash Bill Labour Extented Warranty: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_extwarranty" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_extwarranty"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_brake','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_extwarranty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_extwarranty',this,'hint_jc_bill_cash_labour_extwarranty');">
																	<div class="hint" id="hint_jc_bill_cash_labour_extwarranty"></div>
														</div>
													</div>
													<div class="row ">	
														<div class="form-element3">
																<label>Cash Bill Labour Wheel Alignment: &nbsp; </label>
																	<input name="txt_jc_bill_cash_labour_wheelalign" type="text"
																		class="form-control" id="txt_jc_bill_cash_labour_wheelalign"
																		onKeyUp="toFloat('txt_jc_bill_cash_labour_wheelalign','Amount')"
																		value="<%=mybean.jc_bill_cash_labour_wheelalign%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_cash_labour_wheelalign',this,'hint_txt_jc_bill_cash_labour_wheelalign');">
																	<div class="hint" id="hint_txt_jc_bill_cash_labour_wheelalign"></div>
														</div>
														<div class="form-element3">
															<label>Cash Bill Labour CNG: &nbsp; </label>
																<input name="txt_jc_bill_cash_labour_cng" type="text"
																	class="form-control" id="txt_jc_bill_cash_labour_cng"
																	onKeyUp="toFloat('txt_jc_bill_cash_labour_cng','Amount')"
																	value="<%=mybean.jc_bill_cash_labour_cng%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_cash_labour_cng',this,'hint_txt_jc_bill_cash_labour_cng');">
																<div class="hint" id="hint_txt_jc_bill_cash_labour_cng"></div>
														</div>
														<div class="form-element3">
															<label>Cash Bill Labour Discount: &nbsp; </label>
																<input name="txt_jc_bill_cash_labour_discamt" type="text"
																	class="form-control" id="txt_jc_bill_cash_labour_discamt"
																	onKeyUp="toFloat('txt_jc_bill_cash_labour_discamt','Amount')"
																	value="<%=mybean.jc_bill_cash_labour_discamt%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_cash_labour_discamt',this,'hint_txt_jc_bill_cash_labour_discamt');">
																<div class="hint" id="hint_txt_jc_bill_cash_labour_discamt"></div>
														</div>
													</div>
													</form>
													</div>
												</div>
											</div>
										
										
<!-- 										INSURANCE BILL PARTS DETAILS -->
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">&nbsp;Insurance Bill Parts Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form class="form-horizontal">
													<div class="row">
														<div class="form-element6">
															<label>Insurance Customer Name: &nbsp; </label>
															<input name="txt_jc_bill_insur_customername" type="text"
																class="form-control" id="txt_jc_bill_insur_customername"
																value="<%=mybean.jc_bill_insur_customername%>" size="32"
																maxlength="255"
																onChange="SecurityCheck('txt_jc_bill_insur_customername',this,'hint_txt_jc_bill_insur_customername');" />
															<div class="hint" id="hint_txt_jc_bill_insur_customername"></div>
														</div>
													</div>
													<div class="row">
														<div class="form-element3">
																<label>Insurance Bill Date<font color="#ff0000">*</font>: </label>
																	<input name="txt_jc_bill_insur_date" type="text"
																		class="form-control datepicker"
																		 id="txt_jc_bill_insur_date"
																		value="<%=mybean.jc_bill_insur_date%>"
                                                                            onChange="SecurityCheck('txt_jc_bill_insur_date',this,'hint_txt_jc_bill_insur_date');" />
																	<div class="hint" id="hint_txt_jc_bill_insur_date"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill No: </label>
																	<input name="txt_jc_bill_insur_no" type="text"
																		class="form-control" id="txt_jc_bill_insur_no"
																		value="<%=mybean.jc_bill_insur_no%>" size="20"
																		maxlength="50"
																		onChange="SecurityCheck('txt_jc_bill_insur_no',this,'hint_txt_jc_bill_insur_no');" />
																	<div class="hint" id="hint_txt_jc_bill_insur_no"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Parts: &nbsp; </label>
																	<input id="txt_jc_bill_insur_parts" type="text"
																		class="form-control" name="txt_jc_bill_insur_parts"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts','Amount')"
																		value="<%=mybean.jc_bill_insur_parts%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts',this,'hint_txt_jc_bill_insur_parts');" />
																	<div class="hint" id="hint_txt_jc_bill_insur_parts"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Parts Tyre Qty: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_tyre_qty" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_tyre_qty"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_tyre_qty','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_tyre_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_tyre_qty',this,'hint_txt_jc_bill_insur_parts_tyre_qty');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_tyre_qty"></div>
														</div>
													</div>
													<div class="row ">
														<div class="form-element3">
																<label>Insurance Bill Parts Tyre: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_tyre" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_tyre"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_tyre','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_tyre%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_tyre',this,'hint_txt_jc_bill_insur_parts_tyre');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_tyre"></div>
														</div>
													
														<div class="form-element3">
																<label>Insurance Bill Parts Oil: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_oil" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_oil"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_oil','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_oil%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_oil',this,'hint_txt_jc_bill_insur_parts_oil');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_oil"></div>
														</div>
														
														<div class="form-element3">
																<label>Insurance Bill Parts Accessories: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_accessories" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_accessories"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_accessories','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_accessories%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_accessories',this,'hint_txt_jc_bill_insur_parts_accessories');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_accessories"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Parts Value Add: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_valueadd" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_valueadd"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_valueadd','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_valueadd%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_valueadd',this,'hint_txt_jc_bill_insur_parts_valueadd');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_valueadd"></div>
														</div>
													</div>
													<div class="row ">
														<div class="form-element3">
																<label>Insurance Bill Parts Battery Qty: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_battery_qty" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_battery_qty"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_battery_qty','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_battery_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_battery_qty',this,'hint_txt_jc_bill_insur_parts_battery_qty');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_battery_qty"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Parts Battery: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_battery" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_battery"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_battery','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_battery%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_battery',this,'hint_txt_jc_bill_insur_parts_battery');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_battery"></div>
														</div>
														
														<div class="form-element3">
																<label>Insurance Bill Parts Brake Qty: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_brake_qty" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_brake_qty"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_brake_qty','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_brake_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_brake_qty',this,'hint_txt_jc_bill_insur_parts_brake_qty');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_brake_qty"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Parts Brake: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_brake" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_brake"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_brake','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_brake%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_brake',this,'hint_txt_jc_bill_insur_parts_brake');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_brake"></div>
														</div>
													</div>
													<div class="row ">	
														<div class="form-element3">
																<label>Insurance Bill Parts Extented Warranty Qty: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_extwarranty_qty" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_extwarranty_qty"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_extwarranty_qty','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_extwarranty_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_extwarranty_qty',this,'hint_txt_jc_bill_insur_parts_extwarranty_qty');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_extwarranty_qty"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Parts Extented Warranty: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_extwarranty" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_extwarranty"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_brake','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_extwarranty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_extwarranty',this,'hint_jc_bill_insur_parts_extwarranty');">
																	<div class="hint" id="hint_jc_bill_insur_parts_extwarranty"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Parts Wheel Alignment: &nbsp; </label>
																	<input name="txt_jc_bill_insur_parts_wheelalign" type="text"
																		class="form-control" id="txt_jc_bill_insur_parts_wheelalign"
																		onKeyUp="toFloat('txt_jc_bill_insur_parts_wheelalign','Amount')"
																		value="<%=mybean.jc_bill_insur_parts_wheelalign%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_parts_wheelalign',this,'hint_txt_jc_bill_insur_parts_wheelalign');">
																	<div class="hint" id="hint_txt_jc_bill_insur_parts_wheelalign"></div>
														</div>
														
														<div class="form-element3">
															<label>Insurance Bill Labour CNG: &nbsp; </label>
																<input name="txt_jc_bill_insur_parts_cng" type="text"
																	class="form-control" id="txt_jc_bill_insur_parts_cng"
																	onKeyUp="toFloat('txt_jc_bill_insur_parts_cng','Amount')"
																	value="<%=mybean.jc_bill_insur_parts_cng%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_insur_parts_cng',this,'hint_txt_jc_bill_insur_parts_cng');">
																<div class="hint" id="hint_txt_jc_bill_insur_parts_cng"></div>
														</div>
													</div>
													<div class="row ">
														<div class="form-element3">
														<label>Insurance Bill Parts Discount: &nbsp; </label>
															<input name="txt_jc_bill_insur_parts_discamt" type="text"
																class="form-control" id="txt_jc_bill_insur_parts_discamt"
																onKeyUp="toFloat('txt_jc_bill_insur_parts_discamt','Amount')"
																value="<%=mybean.jc_bill_insur_parts_discamt%>" size="20"
																maxlength="15" readonly
																onChange="SecurityCheck('txt_jc_bill_insur_parts_discamt',this,'hint_txt_jc_bill_insur_parts_discamt');">
															<div class="hint" id="hint_txt_jc_bill_insur_parts_discamt"></div>
													</div>
													</div>
												</form>

												</div>
											</div>
										</div>
										
										<!-- 										INSURANCE BILL LABOUR DETAILS -->
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">&nbsp;Insurance Bill Labour Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form class="form-horizontal">
													<div class="row">
														<div class="form-element3">
																<label>Insurance Bill Labour: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour','Amount')"
																		value="<%=mybean.jc_bill_insur_labour%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour',this,'hint_txt_jc_bill_insur_labour');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Labour Tyre Qty: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_tyre_qty" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_tyre_qty"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_tyre_qty','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_tyre_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_tyre_qty',this,'hint_txt_jc_bill_insur_labour_tyre_qty');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_tyre_qty"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Labour Tyre: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_tyre" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_tyre"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_tyre','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_tyre%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_tyre',this,'hint_txt_jc_bill_insur_labour_tyre');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_tyre"></div>
														</div>
													
														<div class="form-element3">
																<label>Insurance Bill Labour Oil: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_oil" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_oil"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_oil','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_oil%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_oil',this,'hint_txt_jc_bill_insur_labour_oil');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_oil"></div>
														</div>
													</div>
													<div class="row ">
														<div class="form-element3">
																<label>Insurance Bill Labour Accessories: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_accessories" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_accessories"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_accessories','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_accessories%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_accessories',this,'hint_txt_jc_bill_insur_labour_accessories');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_accessories"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Labour Value Add: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_valueadd" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_valueadd"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_valueadd','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_valueadd%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_valueadd',this,'hint_txt_jc_bill_insur_labour_valueadd');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_valueadd"></div>
														</div>

														<div class="form-element3">
																<label>Insurance Bill Labour Battery Qty: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_battery_qty" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_battery_qty"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_battery_qty','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_battery_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_battery_qty',this,'hint_txt_jc_bill_insur_labour_battery_qty');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_battery_qty"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Labour Battery: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_battery" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_battery"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_battery','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_battery%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_battery',this,'hint_txt_jc_bill_insur_labour_battery');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_battery"></div>
														</div>

														
													</div>
													<div class="row ">
														<div class="form-element3">
																<label>Insurance Bill Labour Brake Qty: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_brake_qty" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_brake_qty"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_brake_qty','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_brake_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_brake_qty',this,'hint_txt_jc_bill_insur_labour_brake_qty');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_brake_qty"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Labour Brake: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_brake" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_brake"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_brake','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_brake%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_brake',this,'hint_txt_jc_bill_insur_labour_brake');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_brake"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Labour Extented Warranty Qty: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_extwarranty_qty" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_extwarranty_qty"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_extwarranty_qty','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_extwarranty_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_extwarranty_qty',this,'hint_txt_jc_bill_insur_labour_extwarranty_qty');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_extwarranty_qty"></div>
														</div>
														<div class="form-element3">
																<label>Insurance Bill Labour Extented Warranty: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_extwarranty" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_extwarranty"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_brake','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_extwarranty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_extwarranty',this,'hint_jc_bill_insur_labour_extwarranty');">
																	<div class="hint" id="hint_jc_bill_insur_labour_extwarranty"></div>
														</div>
													</div>
													<div class="row ">	
														<div class="form-element3">
																<label>Insurance Bill Labour Wheel Alignment: &nbsp; </label>
																	<input name="txt_jc_bill_insur_labour_wheelalign" type="text"
																		class="form-control" id="txt_jc_bill_insur_labour_wheelalign"
																		onKeyUp="toFloat('txt_jc_bill_insur_labour_wheelalign','Amount')"
																		value="<%=mybean.jc_bill_insur_labour_wheelalign%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_insur_labour_wheelalign',this,'hint_txt_jc_bill_insur_labour_wheelalign');">
																	<div class="hint" id="hint_txt_jc_bill_insur_labour_wheelalign"></div>
														</div>
														<div class="form-element3">
															<label>Insurance Bill Labour CNG: &nbsp; </label>
																<input name="txt_jc_bill_insur_labour_cng" type="text"
																	class="form-control" id="txt_jc_bill_insur_labour_cng"
																	onKeyUp="toFloat('txt_jc_bill_insur_labour_cng','Amount')"
																	value="<%=mybean.jc_bill_insur_labour_cng%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_insur_labour_cng',this,'hint_txt_jc_bill_insur_labour_cng');">
																<div class="hint" id="hint_txt_jc_bill_insur_labour_cng"></div>
														</div>
														<div class="form-element3">
															<label>Insurance Bill Labour Discount: &nbsp; </label>
																<input name="txt_jc_bill_insur_labour_discamt" type="text"
																	class="form-control" id="txt_jc_bill_insur_labour_discamt"
																	onKeyUp="toFloat('txt_jc_bill_insur_labour_discamt','Amount')"
																	value="<%=mybean.jc_bill_insur_labour_discamt%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_insur_labour_discamt',this,'hint_txt_jc_bill_insur_labour_discamt');">
																<div class="hint" id="hint_txt_jc_bill_insur_labour_discamt"></div>
														</div>
													</div>
												</form>

												</div>
											</div>
										</div>
										
<!-- 										FOC -->

										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">&nbsp;FOC Bill Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form class="form-horizontal">
													<div class="row">
														<div class="form-element6">
															<label>FOC Customer Name: &nbsp; </label>
															<input name="txt_jc_bill_foc_customername" type="text"
																class="form-control" id="txt_jc_bill_foc_customername"
																value="<%=mybean.jc_bill_foc_customername%>" size="32"
																maxlength="255"
																onChange="SecurityCheck('txt_jc_bill_foc_customername',this,'hint_txt_jc_bill_foc_customername');" />
															<div class="hint" id="hint_txt_jc_bill_foc_customername"></div>
														</div>
														<div class="form-element3">
																<label>FOC Bill Date<font color="#ff0000">*</font>: </label>
																	<input name="txt_jc_bill_foc_date" type="text"
																		class="form-control datepicker"
																		 id="txt_jc_bill_foc_date"
																		value="<%=mybean.jc_bill_foc_date%>"
                                                                            onChange="SecurityCheck('txt_jc_bill_foc_date',this,'hint_txt_jc_bill_foc_date');" />
																	<div class="hint" id="hint_txt_jc_bill_foc_date"></div>
														</div>
														<div class="form-element3">
															<label>FOC Bill No: </label>
																<input name="txt_jc_bill_foc_no" type="text"
																	class="form-control" id="txt_jc_bill_foc_no"
																	value="<%=mybean.jc_bill_foc_no%>" size="20"
																	maxlength="50"
																	onChange="SecurityCheck('txt_jc_bill_foc_no',this,'hint_txt_jc_bill_foc_no');" />
																<div class="hint" id="hint_txt_jc_bill_foc_no"></div>
														</div>
													</div>
												</form>
												</div>
											</div>
										</div>
										
										
<!-- 										Warranty BILL PARTS DETAILS -->
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">&nbsp;Warranty Bill Parts Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form class="form-horizontal">
													<div class="row">
														<div class="form-element6">
														<label>Warranty Customer Name: &nbsp; </label>
															<input name="txt_jc_bill_warranty_customername" type="text"
																class="form-control" id="txt_jc_bill_warranty_customername"
																value="<%=mybean.jc_bill_warranty_customername%>" size="32"
																maxlength="255"
																onChange="SecurityCheck('txt_jc_bill_warranty_customername',this,'hint_txt_jc_bill_warranty_customername');" />
															<div class="hint" id="hint_txt_jc_bill_warranty_customername"></div>
														</div>
													</div>
													<div class="row">
														<div class="form-element3">
																<label>Warranty Bill Date<font color="#ff0000">*</font>: </label>
																	<input name="txt_jc_bill_warranty_date" type="text"
																		class="form-control datepicker"
																		 id="txt_jc_bill_warranty_date"
																		value="<%=mybean.jc_bill_warranty_date%>"
                                                                            onChange="SecurityCheck('txt_jc_bill_warranty_date',this,'hint_txt_jc_bill_warranty_date');" />
																	<div class="hint" id="hint_txt_jc_bill_warranty_date"></div>
														</div>
														<div class="form-element3">
															<label>Warranty Bill No: </label>
																<input name="txt_jc_bill_warranty_no" type="text"
																	class="form-control" id="txt_jc_bill_warranty_no"
																	value="<%=mybean.jc_bill_warranty_no%>" size="20"
																	maxlength="50"
																	onChange="SecurityCheck('txt_jc_bill_warranty_no',this,'hint_txt_jc_bill_warranty_no');" />
																<div class="hint" id="hint_txt_jc_bill_warranty_no"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Parts: &nbsp; </label>
																	<input id="txt_jc_bill_warranty_parts" type="text"
																		class="form-control" name="txt_jc_bill_warranty_parts"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts',this,'hint_txt_jc_bill_warranty_parts');" />
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts"></div>
														</div>
														
														<div class="form-element3">
																<label>Warranty Bill Parts Tyre Qty: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_tyre_qty" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_tyre_qty"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_tyre_qty','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_tyre_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_tyre_qty',this,'hint_txt_jc_bill_warranty_parts_tyre_qty');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_tyre_qty"></div>
														</div>
													</div>
													<div class="row ">
														<div class="form-element3">
																<label>Warranty Bill Parts Tyre: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_tyre" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_tyre"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_tyre','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_tyre%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_tyre',this,'hint_txt_jc_bill_warranty_parts_tyre');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_tyre"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Parts Oil: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_oil" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_oil"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_oil','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_oil%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_oil',this,'hint_txt_jc_bill_warranty_parts_oil');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_oil"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Parts Accessories: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_accessories" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_accessories"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_accessories','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_accessories%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_accessories',this,'hint_txt_jc_bill_warranty_parts_accessories');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_accessories"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Parts Value Add: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_valueadd" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_valueadd"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_valueadd','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_valueadd%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_valueadd',this,'hint_txt_jc_bill_warranty_parts_valueadd');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_valueadd"></div>
														</div>
													</div>
													<div class="row ">
														<div class="form-element3">
																<label>Warranty Bill Parts Battery Qty: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_battery_qty" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_battery_qty"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_battery_qty','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_battery_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_battery_qty',this,'hint_txt_jc_bill_warranty_parts_battery_qty');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_battery_qty"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Parts Battery: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_battery" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_battery"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_battery','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_battery%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_battery',this,'hint_txt_jc_bill_warranty_parts_battery');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_battery"></div>
														</div>
														
														<div class="form-element3">
																<label>Warranty Bill Parts Brake Qty: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_brake_qty" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_brake_qty"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_brake_qty','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_brake_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_brake_qty',this,'hint_txt_jc_bill_warranty_parts_brake_qty');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_brake_qty"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Parts Brake: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_brake" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_brake"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_brake','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_brake%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_brake',this,'hint_txt_jc_bill_warranty_parts_brake');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_brake"></div>
														</div>
													</div>
													<div class="row ">	
														<div class="form-element3">
																<label>Warranty Bill Parts Extented Warranty Qty: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_extwarranty_qty" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_extwarranty_qty"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_extwarranty_qty','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_extwarranty_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_extwarranty_qty',this,'hint_txt_jc_bill_warranty_parts_extwarranty_qty');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_extwarranty_qty"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Parts Extented Warranty: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_extwarranty" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_extwarranty"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_brake','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_extwarranty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_extwarranty',this,'hint_jc_bill_warranty_parts_extwarranty');">
																	<div class="hint" id="hint_jc_bill_warranty_parts_extwarranty"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Parts Wheel Alignment: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_parts_wheelalign" type="text"
																		class="form-control" id="txt_jc_bill_warranty_parts_wheelalign"
																		onKeyUp="toFloat('txt_jc_bill_warranty_parts_wheelalign','Amount')"
																		value="<%=mybean.jc_bill_warranty_parts_wheelalign%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_parts_wheelalign',this,'hint_txt_jc_bill_warranty_parts_wheelalign');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_parts_wheelalign"></div>
														</div>
														<div class="form-element3">
															<label>Warranty Bill Parts CNG: &nbsp; </label>
																<input name="txt_jc_bill_warranty_parts_cng" type="text"
																	class="form-control" id="txt_jc_bill_warranty_parts_cng"
																	onKeyUp="toFloat('txt_jc_bill_warranty_parts_cng','Amount')"
																	value="<%=mybean.jc_bill_warranty_parts_cng%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_warranty_parts_cng',this,'hint_txt_jc_bill_warranty_parts_cng');">
																<div class="hint" id="hint_txt_jc_bill_warranty_parts_cng"></div>
														</div>
													</div>
													<div class="row ">
															<div class="form-element3">
															<label>Warranty Bill Parts Discount: &nbsp; </label>
																<input name="txt_jc_bill_warranty_parts_discamt" type="text"
																	class="form-control" id="txt_jc_bill_warranty_parts_discamt"
																	onKeyUp="toFloat('txt_jc_bill_warranty_parts_discamt','Amount')"
																	value="<%=mybean.jc_bill_warranty_parts_discamt%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_warranty_parts_discamt',this,'hint_txt_jc_bill_warranty_parts_discamt');">
																<div class="hint" id="hint_txt_jc_bill_warranty_parts_discamt"></div>
														</div>
													</div>
													</form>

												</div>
											</div>
										</div>
										
										<!-- 										Warranty BILL LABOUR DETAILS -->
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">&nbsp;Warranty Bill Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form class="form-horizontal">
													
													<div class="row ">
														<div class="form-element3">
																<label>Warranty Bill Labour: &nbsp; </label>
																	<input id="txt_jc_bill_warranty_labour" type="text"
																		class="form-control" name="txt_jc_bill_warranty_labour"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour',this,'hint_txt_jc_bill_warranty_labour');" />
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Labour Tyre Qty: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_tyre_qty" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_tyre_qty"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_tyre_qty','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_tyre_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_tyre_qty',this,'hint_txt_jc_bill_warranty_labour_tyre_qty');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_tyre_qty"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Labour Tyre: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_tyre" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_tyre"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_tyre','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_tyre%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_tyre',this,'hint_txt_jc_bill_warranty_labour_tyre');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_tyre"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Labour Oil: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_oil" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_oil"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_oil','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_oil%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_oil',this,'hint_txt_jc_bill_warranty_labour_oil');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_oil"></div>
														</div>
													</div>
														
													<div class="row ">
														<div class="form-element3">
																<label>Warranty Bill Labour Accessories: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_accessories" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_accessories"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_accessories','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_accessories%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_accessories',this,'hint_txt_jc_bill_warranty_labour_accessories');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_accessories"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Labour Value Add: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_valueadd" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_valueadd"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_valueadd','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_valueadd%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_valueadd',this,'hint_txt_jc_bill_warranty_labour_valueadd');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_valueadd"></div>
														</div>

														<div class="form-element3">
																<label>Warranty Bill Labour Battery Qty: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_battery_qty" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_battery_qty"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_battery_qty','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_battery_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_battery_qty',this,'hint_txt_jc_bill_warranty_labour_battery_qty');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_battery_qty"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Labour Battery: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_battery" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_battery"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_battery','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_battery%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_battery',this,'hint_txt_jc_bill_warranty_labour_battery');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_battery"></div>
														</div>
													</div>
													<div class="row ">
														<div class="form-element3">
																<label>Warranty Bill Labour Brake Qty: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_brake_qty" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_brake_qty"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_brake_qty','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_brake_qty%>" size="20"
																		maxlength="15"
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_brake_qty',this,'hint_txt_jc_bill_warranty_labour_brake_qty');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_brake_qty"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Labour Brake: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_brake" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_brake"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_brake','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_brake%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_brake',this,'hint_txt_jc_bill_warranty_labour_brake');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_brake"></div>
														</div>
													
														<div class="form-element3">
																<label>Warranty Bill Labour Extented Warranty Qty: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_extwarranty_qty" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_extwarranty_qty"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_extwarranty_qty','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_extwarranty_qty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_extwarranty_qty',this,'hint_txt_jc_bill_warranty_labour_extwarranty_qty');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_extwarranty_qty"></div>
														</div>
														<div class="form-element3">
																<label>Warranty Bill Labour Extented Warranty: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_extwarranty" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_extwarranty"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_brake','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_extwarranty%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_extwarranty',this,'hint_jc_bill_warranty_labour_extwarranty');">
																	<div class="hint" id="hint_jc_bill_warranty_labour_extwarranty"></div>
														</div>
													</div>
													<div class="row ">	
														<div class="form-element3">
																<label>Warranty Bill Labour Wheel Alignment: &nbsp; </label>
																	<input name="txt_jc_bill_warranty_labour_wheelalign" type="text"
																		class="form-control" id="txt_jc_bill_warranty_labour_wheelalign"
																		onKeyUp="toFloat('txt_jc_bill_warranty_labour_wheelalign','Amount')"
																		value="<%=mybean.jc_bill_warranty_labour_wheelalign%>" size="20"
																		maxlength="15" readonly
																		onChange="SecurityCheck('txt_jc_bill_warranty_labour_wheelalign',this,'hint_txt_jc_bill_warranty_labour_wheelalign');">
																	<div class="hint" id="hint_txt_jc_bill_warranty_labour_wheelalign"></div>
														</div>
														<div class="form-element3">
															<label>Warranty Bill Labour CNG: &nbsp; </label>
																<input name="txt_jc_bill_warranty_labour_cng" type="text"
																	class="form-control" id="txt_jc_bill_warranty_labour_cng"
																	onKeyUp="toFloat('txt_jc_bill_warranty_labour_cng','Amount')"
																	value="<%=mybean.jc_bill_warranty_labour_cng%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_warranty_labour_cng',this,'hint_txt_jc_bill_warranty_labour_cng');">
																<div class="hint" id="hint_txt_jc_bill_warranty_labour_cng"></div>
														</div>
														<div class="form-element3">
															<label>Warranty Bill Labour Discount: &nbsp; </label>
																<input name="txt_jc_bill_warranty_labour_discamt" type="text"
																	class="form-control" id="txt_jc_bill_warranty_labour_discamt"
																	onKeyUp="toFloat('txt_jc_bill_warranty_labour_discamt','Amount')"
																	value="<%=mybean.jc_bill_warranty_labour_discamt%>" size="20"
																	maxlength="15" readonly
																	onChange="SecurityCheck('txt_jc_bill_warranty_labour_discamt',this,'hint_txt_jc_bill_warranty_labour_discamt');">
																<div class="hint" id="hint_txt_jc_bill_warranty_labour_discamt"></div>
														</div>
													</div>
													</form>
													</div>
												</div>
											</div>
										
									</div>
									<div class="tab-pane" id="tabs-13"></div>
									<div class="tab-pane" id="tabs-14"></div>
									<!-- 									<div class="tab-pane" id="tabs-11"></div> -->
									<div class="tab-pane" id="tabs-9">
									<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">PSF Follow-up</div>
											</div>
											<center>
											<font color="#ff0000"><b><%=mybean.jcpsfmsg%></b></font>
											<br>
											<font color="#ff0000"><b><%=mybean.ticketmsg%></b></font>
										</center>
											<div class="portlet-body portlet-empty">
												<%=mybean.ListPSFFollowup(mybean.jc_id, mybean.comp_id)%>
											</div>
										</div>
									</div>
									
									
									
									
									
									
									
									<div class="tab-pane" id="tabs-10">
									<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">History</div>
											</div>
											<div class="portlet-body portlet-empty">
												<%=mybean.jc_history%>
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
	</div>



	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script>
	var demoVar="";
	</script>
	<script type="text/javascript" src="../Library/slider.js"></script>
	<script type="text/javascript" src="../Library/bill.js"></script>
	<script type="text/javascript" src="../Library/sc.js"></script>

	<script type="text/javascript">
		$(function() {
			var guage = document.getElementById("txt_jc_fuel_guage").value;
			$("#div_fuel_guage").slider(
					{
						range : "min",
						value : guage,
						min : 0,
						max : 100,
						slide : function(event, ui) {
							$("#div_jc_fuel_guage").html(ui.value);
							$("#txt_jc_fuel_guage").val(ui.value);
							SecurityCheck('txt_jc_fuel_guage', document.getElementById("txt_jc_fuel_guage"), 'hint_txt_jc_fuel_guage');
						}
					});
			$("#div_jc_fuel_guage").html($("#div_fuel_guage").slider("value"));
			$("#txt_jc_fuel_guage").val($("#div_fuel_guage").slider("value"));
		});
	</script>
	<script language="JavaScript" type="text/javascript">
		function ClearMsg() {
			document.getElementById("hint_dr_item_model_id").innerHTML = "";
		}

		function UpdateCustomer() {
			document.getElementById("customer_details").style.display = "";
			document.getElementById("customer_display").style.display = "none";
		}

		function HideCustomerDetails() {
			var customer_id = document.getElementById("txt_customer_id").value;
			document.getElementById("customer_details").style.display = "none";
			document.getElementById("customer_display").style.display = "";
			showHint( 'jobcard-dash-check.jsp?display_customer_details=yes&customer_id=' + customer_id, 'div_customer_details');
		}

		function PopulateModelItem() {
			var item_model_id = document.getElementById("dr_item_model_id").value;
			document.getElementById("hint_dr_item_id").innerHTML = "";
			showHint( 'vehicle-dash-check.jsp?list_model_item=yes&item_model_id=' + item_model_id, 'model-item');
		}

		function SecurityCheck(name, obj, hint) {
			var value;
			if (name != "chk_jc_critical") {
				value = GetReplace(obj.value);
			} else {
				if (obj.checked == true) {
					value = "1";
				} else {
					value = "0";
				}
			}
			var jc_id = GetReplace(document.getElementById("jc_id").value);
			var url = "../service/jobcard-dash-check.jsp?name=" + name + "&value=" + value + "&jc_id=" + jc_id;
			showHint(url, hint);

			if (name == 'dr_jc_stage_id') {
				if (value == '6') {
					setTimeout('populatedeliverytime()', 1000);
				} else {
					document.getElementById("span_deliveredtime").innerHTML = "";
				}

				if (value == '5') {
					setTimeout('populatereadytime()', 1000);
				} else {
					document.getElementById("span_readytime").innerHTML = "";
				}
			}
		}

		function SecurityCheckStage(name, obj, hint) {
			var jc_stage_id = document.getElementById("dr_jc_stage_id").value;
			var jc_ro_no = document.getElementById("txt_jc_ro_no2").value;
			var jc_id = GetReplace(document.getElementById("jc_id").value);
			var url = "";
			if (jc_stage_id == 6) {
				if (jc_ro_no == '') {
					document.getElementById("hint_txt_jc_ro_no").innerHTML = "<font color=red > Enter RO No.! </font>";
				} else {
					url = "../service/jobcard-dash-check.jsp?name=" + name
							+ "&jc_ro_no=" + jc_ro_no + "&jc_id=" + jc_id
							+ "&jc_stage_id=" + jc_stage_id;
					showHint(url, hint);
				}
			} else {
				document.getElementById("hint_txt_jc_ro_no").innerHTML = "";
				url = "../service/jobcard-dash-check.jsp?name=" + name
						+ "&jc_ro_no=" + jc_ro_no + "&jc_id=" + jc_id
						+ "&jc_stage_id=" + jc_stage_id;
				showHint(url, hint);
			}
		}

		function UpdateInventory(chk_id, value, chk_name) {
			var check = document.getElementById(chk_id).checked;
			var hint = 'hint_jc_invent_id';
			var jc_id = document.getElementById("txt_jc_id").value;
			var url = "../service/jobcard-dash-check.jsp?jc_id=" + jc_id
					+ "&checked=" + check + "&value=" + value
					+ "&name=chk_jc_invent_id&chk_invent_name=" + chk_name + "";
			// 			showHintPost(url, "123", jc_id, hint);
			showHint(url, hint);
		}

		function UpdateJCInventory(value) {
			var hint = 'hint_jc_invent_id';
			var jc_id = document.getElementById("txt_jc_id").value;
			var url = "../service/jobcard-dash-check.jsp?jc_id=" + jc_id
					+ "&value=" + value + "&name=txt_jc_inventory";
			showHint(url, hint);
		}

		function populatedeliverytime() {
			var deliveredtime = document.getElementById("deltime").value;
			document.getElementById("span_deliveredtime").innerHTML = deliveredtime;
			var readytime = document.getElementById("comptime").value;
			document.getElementById("span_readytime").innerHTML = readytime;
		}

		function LoadJCDash(tab) {
			var jc_id = document.getElementById("txt_jc_id").value;
			var veh_id = document.getElementById("txt_veh_id").value;
			var customer_id = document.getElementById("txt_customer_id").value;
			if (tab == '1') {
				var jc_comm_contact_id = document .getElementById("dr_jc_comm_contact_id").value;
				showHint( 'jobcard-dash-check.jsp?populate_contacts=yes&customer_id=' + customer_id
						+ '&jc_comm_contact_id=' + jc_comm_contact_id, 'dr_jc_comm_contact_id');
			} else if (tab == '2') {
				if (document.getElementById("tabs-2").innerHTML == '') {
					showHint( 'jobcard-dash-check.jsp?customer_details=yes&customer_id=' + customer_id, 'tabs-2');
				}
			} else if (tab == '3') {
				list_jcitems();
			} else if (tab == '6') {
				if (document.getElementById("tabs-6").innerHTML == '') {
					showHint('jobcard-dash-check.jsp?jc_images=yes&jc_id=' + jc_id, 'tabs-6');

				}
			} else if (tab == '7') {
				if (document.getElementById("tabs-7").innerHTML == '') {
					showHint('jobcard-dash-check.jsp?jc_documents=yes&jc_id=' + jc_id, 'tabs-7');
				}
			} else if (tab == '9') {
				if (document.getElementById("tabs-9").innerHTML == '') {
					showHint( 'jobcard-dash-check.jsp?jc_psf=yes&jc_id=' + jc_id, 'tabs-9');
				}
			} else if (tab == '10') {
				if (document.getElementById("tabs-10").innerHTML == '') {
					showHint('jobcard-dash-check.jsp?history=yes&jc_id=' + jc_id, 'tabs-10');
				}
			} else if (tab == '13') {
				if (document.getElementById("tabs-13").innerHTML == '') {
					showHint( 'jobcard-dash-check.jsp?invoice_details=yes&jc_id=' + jc_id, 'tabs-13');
				}
			} else if (tab == '14') {
				if (document.getElementById("tabs-14").innerHTML == '') {
					showHint( 'jobcard-dash-check.jsp?receipt_details=yes&jc_id=' + jc_id, 'tabs-14');
				}
			}
		}

		function ChecklistItem(count, name, value, hint) {
			var check = document.getElementById("chk_check_id" + count).checked;
			var jc_id = document.getElementById("txt_jc_id").value;
			var url = "../service/jobcard-dash-check.jsp?name=" + name
					+ "&value=" + value + "&jc_id=" + jc_id + "&checked="
					+ check;
			// 			showHintPost(url, "123", jc_id, hint);
			showHint(url, hint);
		}

		function CheckAllChecklist(param) {
			var ins_count = parseInt(document .getElementById("txt_ins_check_count").value);
			var wash_count = parseInt(document .getElementById("txt_wash_check_count").value);
			if (param == '1') {
				for (var i = 1; i <= ins_count; i++) {
					document.getElementById("chk_check_id" + i).checked = "checked";
				}
			} else if (param == '2') {
				for (var i = ins_count; i <= parseInt(wash_count + ins_count); i++) {
					document.getElementById("chk_check_id" + i).checked = "checked";
				}
			}
			var jc_id = document.getElementById("txt_jc_id").value;
			var url = "../service/jobcard-dash-check.jsp?name=all_checklist&value=" + param + "&jc_id=" + jc_id;
			showHint(url, 'hint_chk_check_id');
		}

		function SearchStockStatus(itemName) {
			var location_id = document.getElementById("dr_location_id").value;
			showHint('vehicle-dash-check.jsp?stock_status=yes&location_id=' + location_id
					+ '&item_name=' + itemName, 'div_stock_status');
		}

		function GetManHours(baytrans_emp_id) {
			var jc_id = document.getElementById("txt_jc_id").value;
			showHint('jobcard-dash-check.jsp?man_hours=yes&jc_id=' + jc_id
					+ '&baytrans_emp_id=' + baytrans_emp_id, 'div_manhours');
		}

		function PopulateItem(branch_id) {
			showHint('vehicle-dash-check.jsp?location=yes&branch_id=' + branch_id, 'span_location');
		}

		function AddNewContact(customer_id) {
			$('#dialog-modal-contact').dialog({
				autoOpen : false,
				width : 900,
				height : 500,
				zIndex : 200,
				modal : true,
				title : "Add New Contact"
			});

			//$('#new_contact_link').click(function(){
			var customer_id = document.getElementById("txt_customer_id").value;
			$ .ajax({
				success : function(data) {
					$('#dialog-modal-contact') .html(
						'<iframe src="../customer/customer-contact-update.jsp?Add=yes&customer_id=' + customer_id
								+ '&modal=yes" width="100%" height="100%" frameborder=0></iframe>');
				}
			});

			$('#dialog-modal-contact').dialog('open');
		}

		function AddNewManHour(jc_id) {
			$('#dialog-modal-man-hour').dialog({
				autoOpen : true,
				width : 900,
				height : 500,
				zIndex : 200,
				modal : true,
				title : "Add New Man Hours",
				close : function(e) {
					window.parent.CloseModalManHour(jc_id);
					//alert("close");
					$(this).empty();
					$(this).dialog('close');
				}
			});

			//$('#new_contact_link').click(function(){
			var jc_id = document.getElementById("txt_jc_id").value;
			var dr_branch = document.getElementById("txt_branch_id").value;
			$ .ajax({
				success : function(data) {
					$('#dialog-modal-man-hour') .html(
						'<iframe src="jobcard-bay-dash.jsp?add=yes&jc_id=' + jc_id
								+ '&dr_branch=' + dr_branch
								+ '&modal=yes" width="100%" height="100%" frameborder=0></iframe>');
				}
			});

			$('#dialog-modal-man-hour').dialog('open');
		}

		function AddNewManHourUpdate(baytrans_id) {
			$('#dialog-modal-man-hour').dialog({
				autoOpen : false,
				width : 900,
				height : 500,
				zIndex : 200,
				modal : true,
				title : "Update Man Hours",
				close : function(e) {
					$(this).empty();
					$(this).dialog('close');
				}

			});

			//$('#new_contact_link').click(function(){
			var jc_id = document.getElementById("txt_jc_id").value;
			var dr_branch = document.getElementById("txt_branch_id").value;
			$ .ajax({
				success : function(data) {
					$('#dialog-modal-man-hour') .html(
						'<iframe src="jobcard-bay-dash.jsp?update=yes&baytrans_id=' + baytrans_id
								+ '&jc_id=' + jc_id
								+ '&dr_branch=' + dr_branch
								+ '&modal=yes" width="100%" height="100%" frameborder=0></iframe>');
				}
			});

			$('#dialog-modal-man-hour').dialog('open');
		}

		function CloseModal(customer_id) {
			showHint('jobcard-dash-check.jsp?customer_details=yes&customer_id=' + customer_id, 'tabs-2');
			$('#dialog-modal-contact').dialog('close');
		}

		function CloseModalManHour(jc_id) {
			showHint('jobcard-dash-check.jsp?man_hours=yes&jc_id=' + jc_id, 'tabs-8');
			$('#dialog-modal-man-hour').dialog('close');
			//AddNewManHour(jc_id);    
			//$('#dialog-modal-man-hour').dialog('close');
		}

		function RefreshHistory() {
			var veh_id = document.getElementById("veh_id").value;
		}
		
		function PopulateBranchClass() {
			  var rateclass_type = document.getElementById('dr_rateclass_type').value;
			  showHint("../inventory/item-price-update-check.jsp?branch_id="+<%=mybean.jc_branch_id%>+"&jc_id="+<%=mybean.jc_id%>+"&rateclass_type="+rateclass_type+"&rateclasstype=yes", "rateclass");
		  }
	</script>
</body>
</HTML>
