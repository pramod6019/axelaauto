<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Vehicle_Dash"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<style>
.modal-dialog {
	width: 85%;
	height: 90%;
}

.modal-body {
	max-height: calc(100vh - 100px);
	overflow-y: auto;
}
/* input{ */
/* position: relative; */
/* } */

</style>

<script>
function createurl(){
// 	alert("coming..")
	var url = $("#contact-add").serialize();
	url = url+'&add_contact_button=Add Contact';
	alert("url=="+url);
	showHint('../service/vehicle-dash-modal.jsp?'
			+ url, '');
}


</script>
</HEAD>
<body onLoad="Contactableband();"
	class="page-container-bg-solid page-header-menu-fixed">
	<%
		if (!mybean.modal.equals("yes")) {
	%>
	<%@include file="../portal/header.jsp"%>
	<%
		}
	%>

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
							Vehicle Dashboard &gt; Vehicle ID:&nbsp;<%=mybean.veh_id%></h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<%
						if (!mybean.modal.equals("yes")) {
					%>
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="../service/vehicle.jsp">Vehicles</a> &gt;</li>
						<li><a href="../service/vehicle-list.jsp?all=yes">List
								Vehicles</a> &gt;</li>
						<li><a
							href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>">Vehicle
								ID:&nbsp;<%=mybean.veh_id%></a>&nbsp;:</li>


					</ul>
					<%
						}
					%>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<input type="hidden" name="txt_veh_id" id="txt_veh_id"
								value="<%=mybean.veh_id%>"> <input type="hidden"
								name="txt_customer_id" id="txt_customer_id"
								value="<%=mybean.veh_customer_id%>">
							<div class="tabbable tabbable-tabdrop">
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tabs-1" data-toggle="tab">Vehicle
											Info</a></li>
									<li><a href="#tabs-2" data-toggle="tab"
										onclick="LoadVehDash('2');">Customer</a></li>
									<li><a href="#tabs-3" data-toggle="tab"
										onclick="LoadVehDash('3');">Ownership</a></li>
									<li><a href="#tabs-4" data-toggle="tab"
										onclick="LoadVehDash('4');">Follow-up</a></li>
									<li><a href="#tabs-5" data-toggle="tab"
										onclick="LoadVehDash('5');">Bookings</a></li>
									<li><a href="#tabs-6" data-toggle="tab"
										onclick="LoadVehDash('6');">Job Cards</a></li>
									<li><a href="#tabs-7" data-toggle="tab"
										onclick="LoadVehDash('7');">Invoices</a></li>
									<li><a href="#tabs-8" data-toggle="tab"
										onclick="LoadVehDash('8');">Receipts</a></li>
									<li><a href="#tabs-9" data-toggle="tab"
										onclick="LoadVehDash('9');">Insurance</a></li>
									<li><a href="#tabs-10" data-toggle="tab">Insurance
											Follow-up</a></li>
									<li><a href="#tabs-11" data-toggle="tab"
										onclick="LoadVehDash('11');">History</a></li>
									<li><a href="#tabs-12" data-toggle="tab">Stock Status</a></li>
									<li><a href="#tabs-13" data-toggle="tab"
										onclick="LoadVehDash('13');">Tickets</a></li>


									<!-- 								<li><a href="#tabs-1">Vehicle Info</a></li> -->
									<!--                         <li onmouseover="LoadVehDash('2');"><a href="#tabs-2">Customer</a></li> -->
									<!--                         <li onmouseover="LoadVehDash('3');"><a href="#tabs-3">Ownership</a></li> -->
									<!--                         <li onmouseover="LoadVehDash('4');"><a href="#tabs-4">Follow-up</a></li> -->
									<!--                         <li onmouseover="LoadVehDash('5');"><a href="#tabs-5">Bookings</a></li> -->
									<!--                         <li onmouseover="LoadVehDash('6');"><a href="#tabs-6">Job Cards</a></li> -->
									<!--                         <li onmouseover="LoadVehDash('7');"><a href="#tabs-7">Invoices</a></li> -->
									<!--                         <li onmouseover="LoadVehDash('8');"><a href="#tabs-8">Receipts</a></li> -->
									<!--                         <li onmouseover="LoadVehDash('9');"><a href="#tabs-9">Insurance</a></li> -->
									<!--                         <li><a href="#tabs-10">Insurance Follow-up</a></li> -->
									<!--                         <li onmouseover="LoadVehDash('13');"><a href="#tabs-13">Tickets</a></li> -->
									<!--                         <li onmouseover="LoadVehDash('11');"><a href="#tabs-11">History</a></li> -->
									<!--                         <li><a href="#tabs-12">Stock Status</a></li> -->
								</ul>
								<div class="tab-content">
									<div class="tab-pane active" id="tabs-1">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Vehicle
													Details</div>
											</div>
											<div class="portlet-body portlet-empty">
												<!-- START PORTLET BODY -->
												<form class="form-horizontal" action="">
													<div class="form-group">
														<label class="control-label col-md-3">Vehicle ID:</label>
														<div class="txt-align">
															<a
																href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>"><%=mybean.veh_id%></a>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-md-3">Branch:</label>
														<div class="txt-align">
															<a
																href="../portal/branch-summary.jsp?branch_id=<%=mybean.veh_branch_id%>"><%=mybean.branch_name%></a>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-md-3">Model<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_item_model_id" class="form-control"
																id="dr_item_model_id"
																onChange="PopulateModelItem();SecurityCheck('dr_item_model_id',this,'hint_dr_item_model_id');">
																<%=mybean.PopulateModel(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_item_model_id"></div>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-md-3">Item<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="veh_id" type="hidden" id="veh_id"
																value="<%=mybean.veh_id%>" /> <span id="model-item">
																<%=mybean.PopulateItem(mybean.comp_id, mybean.item_model_id)%></span>
															<div class="hint" id="hint_dr_item_id"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Exterior<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_exterior_id" class="form-control"
																id="dr_exterior_id"
																onchange="SecurityCheck('dr_exterior_id', this, 'hint_dr_exterior_id');">
																<%=mybean.PopulateExterior(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_exterior_id"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Interior<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_interior_id" class="form-control"
																id="dr_interior_id"
																onchange="SecurityCheck('dr_interior_id', this, 'hint_dr_interior_id');">
																<%=mybean.PopulateInterior(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_interior_id"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Reg. No.<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_veh_reg_no" id="txt_veh_reg_no"
																type="text" class="form-control"
																value="<%=mybean.veh_reg_no%>" size="25" maxlength="20"
																onchange="SecurityCheck('txt_veh_reg_no',this,'hint_txt_veh_reg_no')" />
															<div class="hint" id="hint_txt_veh_reg_no"></div>
														</div>
													</div>


													<div class="form-group">
														<label class="control-label col-md-3">Model Year<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_veh_modelyear" id="txt_veh_modelyear"
																type="text" class="form-control"
																value="<%=mybean.veh_modelyear%>" size="10"
																maxlength="4"
																onchange="SecurityCheck('txt_veh_modelyear',this,'hint_txt_veh_modelyear')" />
															<div class="hint" id="hint_txt_veh_modelyear"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Sale Date<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_veh_sale_date" id="txt_veh_sale_date"
																value="<%=mybean.veh_sale_date%>" size="12"
																maxlength="10" class="form-control date-picker"
																data-date-format="dd/mm/yyyy" type="text" />
															<!-- onChange="SecurityCheck('txt_veh_sale_date',this,'hint_txt_veh_sale_date')"  -->

															<div class="hint" id="hint_txt_veh_sale_date"></div>
														</div>
													</div>



													<div class="form-group">
														<label class="control-label col-md-3">Sale Amount:
														</label>
														<div class="col-md-6">
															<input name="txt_veh_sale_amount"
																id="txt_veh_sale_amount" type="text"
																class="form-control" value="<%=mybean.veh_sale_amount%>"
																onkeyup="toInteger('txt_veh_sale_amount','Sale Amount')"
																size="20" maxlength="11"
																onchange="SecurityCheck('txt_veh_sale_amount',this,'hint_txt_veh_sale_amount')" />
															<div class="hint" id="hint_txt_veh_sale_amount"></div>
														</div>
													</div>


													<div class="form-group">
														<label class="control-label col-md-3">Veh. Source:
														</label>
														<div class="col-md-6">
															<select class="form-control" name="dr_veh_vehsource_id"
																id="dr_veh_vehsource_id"
																onChange="SecurityCheck('dr_veh_vehsource_id',this,'hint_dr_veh_vehsource_id');">
																<%=mybean.PopulateVehSource(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_veh_vehsource_id"></div>
														</div>
													</div>


													<div class="form-group">
														<label class="control-label col-md-3">Chassis
															Number<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_veh_chassis_no" id="txt_veh_chassis_no"
																type="text" class="form-control"
																value="<%=mybean.veh_chassis_no%>" size="25"
																maxlength="17"
																onChange="SecurityCheck('txt_veh_chassis_no',this,'hint_txt_veh_chassis_no')" />
															<div class="hint" id="hint_txt_veh_chassis_no"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Engine
															Number<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_veh_engine_no" id="txt_veh_engine_no"
																type="text" class="form-control"
																value="<%=mybean.veh_engine_no%>" size="20"
																maxlength="14"
																onchange="SecurityCheck('txt_veh_engine_no',this,'hint_txt_veh_engine_no')" />
															<div class="hint" id="hint_txt_veh_engine_no"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Last Sevice
															Date: </label>
														<div class="col-md-6">
															<input name="txt_veh_lastservice"
																id="txt_veh_lastservice" type="text"
																class="form-control date-picker"
																data-date-format="dd/mm/yyyy" type="text"
																value="<%=mybean.veh_lastservice%>" size="12"
																maxlength="10" />
															<!-- onchange="SecurityCheck('txt_veh_lastservice',this,'hint_txt_veh_lastservice')" -->
															<div class="hint" id="hint_txt_veh_lastservice"></div>
														</div>
													</div>






													<div class="form-group">
														<label class="control-label col-md-3">Last Sevice
															Kms: </label>
														<div class="col-md-6">
															<input name="txt_veh_lastservice_kms"
																id="txt_veh_lastservice_kms" type="text"
																class="form-control"
																value="<%=mybean.veh_lastservice_kms%>" size="12"
																maxlength="10"
																onchange="SecurityCheck('txt_veh_lastservice_kms',this,'hint_txt_veh_lastservice_kms')" />
															<div class="hint" id="hint_txt_veh_lastservice_kms"></div>
														</div>
													</div>


													<div class="form-group">
														<label class="control-label col-md-3">Service Due
															Kms: </label>
														<div class="col-md-6">
															<input name="txt_veh_service_duekms"
																id="txt_veh_service_duekms" type="text"
																class="form-control"
																value="<%=mybean.veh_service_duekms%>" size="25"
																maxlength="17" onkeyup="toInteger(this.id);" />
															<!-- onChange="SecurityCheck('txt_veh_service_duekms',this,'hint_txt_veh_service_duekms')" -->

															<div class="hint" id="hint_txt_veh_service_duekms"></div>
														</div>
													</div>




													<div class="form-group">
														<label class="control-label col-md-3">Service Due
															Date:</label>
														<div class="col-md-6">
															<input name="txt_veh_service_duedate"
																id="txt_veh_service_duedate" type="text"
																class="form-control date-picker"
																data-date-format="dd/mm/yyyy" type="text"
																value="<%=mybean.veh_service_duedate%>" size="12"
																maxlength="10" />
															<div class="hint" id="hint_txt_veh_service_duedate"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Warranty
															Expiry Date:</label>
														<div class="col-md-6">
															<input name="txt_veh_warranty_expirydate" type="text"
																class="form-control date-picker"
																data-date-format="dd/mm/yyyy" type="text"
																id="txt_veh_warranty_expirydate"
																value="<%=mybean.vehwarrantyexpirydate%>" size="12"
																maxlength="10" onChange="" />
															<div class="hint" id="hint_veh_warranty_expirydate"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">IACS:</label>
														<div class="txt-align">
															<input id="chk_veh_iacs" type="checkbox"
																name="chk_veh_iacs"
																<%=mybean.PopulateCheck(mybean.veh_iacs)%>
																onChange="SecurityCheck('chk_veh_iacs',this,'hint_veh_iacs')" />
															<div class="hint" id="hint_veh_iacs"></div>
														</div>
													</div>



													<div class="form-group">
														<label class="control-label col-md-3">CRM
															Executive<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select id="dr_veh_crmemp_id" name="dr_veh_crmemp_id"
																class="form-control"
																onChange="SecurityCheck('dr_veh_crmemp_id',this,'hint_dr_veh_crmemp_id')">
																<%=mybean.PopulateCRMExecutive(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_veh_crmemp_id"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Service
															Advisor<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select id="dr_veh_emp_id" name="dr_veh_emp_id"
																class="form-control"
																onChange="SecurityCheck('dr_veh_emp_id',this,'hint_dr_veh_emp_id')">
																<%=mybean.PopulateServiceExecutive(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_veh_emp_id"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Insurance
															Executive<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select id="dr_veh_insuremp_id" name="dr_veh_insuremp_id"
																class="form-control"
																onChange="SecurityCheck('dr_veh_insuremp_id',this,'hint_dr_veh_insuremp_id')">
																<%=mybean.PopulateInsurExecutive(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_veh_insuremp_id"></div>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-md-3">Insurance
															Date:</label>
														<div class="col-md-6">
															<input name="txt_veh_insur_date" id="txt_veh_insur_date"
																value="<%=mybean.veh_insur_date%>"
																class="form-control date-picker"
																data-date-format="dd/mm/yyyy" type="text" />
															<div class="hint" id="hint_txt_veh_insur_date"></div>
														</div>
													</div>

													<!-- 													<div class="form-group"> -->
													<!-- 														<label class="control-label col-md-3">Insurance -->
													<!-- 															Date: </label> -->
													<!-- 														<div class="col-md-6"> -->
													<!-- 															<input name="txt_veh_insur_date" type="text" -->
													<!-- 																class="form-control" id="txt_veh_insur_date" -->
													<%-- 																value="<%=mybean.veh_insur_date%>" size="12" --%>
													<!-- 																maxlength="10" -->
													<!-- 																onChange="SecurityCheck('txt_veh_insur_date',this,'hint_txt_veh_insur_date')" /> -->
													<!-- 															<div class="hint" id="hint_txt_veh_insur_date"></div> -->
													<!-- 														</div> -->
													<!-- 													</div> -->

													<div class="form-group">
														<label class="control-label col-md-3">Insurance
															Source: </label>
														<div class="col-md-6">
															<select name="dr_veh_insursource_id" class="form-control"
																id="dr_veh_insursource_id"
																onchange="SecurityCheck('dr_veh_insursource_id', this, 'hint_dr_veh_insursource_id');">
																<%=mybean.PopulateInsurSource(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_veh_insursource_id"></div>
														</div>
													</div>


													<div class="form-group">
														<label class="control-label col-md-3">Renewal
															Date: </label>
														<div class="col-md-6">
															<input name="txt_veh_renewal_date"
																class="form-control date-picker"
																id="txt_veh_renewal_date"
																value="<%=mybean.veh_renewal_date%>" size="12"
																maxlength="10" data-date-format="dd/mm/yyyy" type="text" />
															<div class="hint" id="hint_txt_veh_renewal_date"></div>
														</div>
													</div>

												</form>
											</div>
										</div>


										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Customer
													Details</div>
											</div>
											<div class="portlet-body portlet-empty">
												<form class="form-horizontal">


													<div class="form-group">
														<label class="control-label col-md-3">Customer<font
															color="#ff0000">*</font>:
															</td>
														</label>
														<div class="col-md-6">
															<input name="txt_veh_customer_name" type="text"
																class="form-control" id="txt_veh_customer_name"
																value="<%=mybean.veh_customer_name%>" size="70"
																maxlength="255"
																onChange="SecurityCheck('txt_veh_customer_name',this,'hint_txt_veh_customer_name')" />
															<div class="hint" id="hint_txt_veh_customer_name"></div>
														</div>
													</div>




													<div class="form-group">
														<label class="control-label col-md-3"> Contact<font
															color=red>*</font>:
														</label>
														<div class="col-md-6 col-xs-12" id="emprows">
															<table width="100%">
																<tr>
																	<td><select name="dr_title" class="form-control"
																		id="dr_title"
																		onchange="SecurityCheck('dr_title',this,'hint_dr_title')">
																			<%=mybean.PopulateTitle(mybean.comp_id,
					mybean.contact_title_id)%>
																	</select> Title
																		<div class="hint" id="hint_dr_title"></div></td>
																	<td><input name="txt_contact_fname" type="text"
																		class="form-control" id="txt_contact_fname"
																		value="<%=mybean.contact_fname%>" size="32"
																		maxlength="255"
																		onchange="SecurityCheck('txt_contact_fname',this,'hint_txt_contact_fname')" />First
																		Name
																		<div class="hint" id="hint_txt_contact_fname"></div></td>
																	<td><input name="txt_contact_lname" type="text"
																		class="form-control" id="txt_contact_lname"
																		value="<%=mybean.contact_lname%>" size="32"
																		maxlength="255"
																		onchange="SecurityCheck('txt_contact_lname',this,'hint_txt_contact_lname')" />Last
																		Name
																		<div class="hint" id="hint_txt_contact_lname"></div></td>
																</tr>
															</table>
														</div>
													</div>



													<div class="form-group">
														<label class="control-label col-md-3">Mobile 1<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_contact_mobile1" type="text"
																class="form-control" id="txt_contact_mobile1"
																value="<%=mybean.contact_mobile1%>" size="32"
																maxlength="13"
																onchange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')" />
															(91-9999999999)
															<div class="hint" id="hint_txt_contact_mobile1"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Mobile 2: </label>
														<div class="col-md-6">
															<input name="txt_contact_mobile2" type="text"
																class="form-control" id="txt_contact_mobile2"
																value="<%=mybean.contact_mobile2%>" size="32"
																maxlength="13"
																onchange="SecurityCheck('txt_contact_mobile2',this,'hint_txt_contact_mobile2')" />
															(91-9999999999)
															<div class="hint" id="hint_txt_contact_mobile2"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Phone 1<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_contact_phone1" type="text"
																class="form-control" id="txt_contact_phone1"
																value="<%=mybean.contact_phone1%>" size="35"
																maxlength="14"
																onchange="SecurityCheck('txt_contact_phone1',this,'hint_txt_contact_phone1')" />
															(91-80-33333333)
															<div class="hint" id="hint_txt_contact_phone1"></div>
														</div>
													</div>


													<div class="form-group">
														<label class="control-label col-md-3">Email 1<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_contact_email1" type="text"
																class="form-control" id="txt_contact_email1"
																value="<%=mybean.contact_email1%>" size="35"
																maxlength="100"
																onchange="SecurityCheck('txt_contact_email1',this,'hint_txt_contact_email1')" />
															<div class="hint" id="hint_txt_contact_email1"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Address: </label>
														<div class="col-md-6">
															<textarea name="txt_contact_address" cols="40" rows="4"
																class="form-control" id="txt_contact_address"
																onchange="SecurityCheck('txt_contact_address',this,'hint_txt_contact_address')"
																onkeyup="charcount('txt_contact_address', 'span_txt_contact_address','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.contact_address%></textarea>
															<span id="span_txt_contact_address"> (255
																Characters)</span>
															<div class="hint" id="hint_txt_contact_address"></div>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">City<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_city_id" id="dr_city_id"
																class="form-control"
																onchange="SecurityCheck('dr_city_id',this,'hint_dr_city_id')">
																<%=mybean.PopulateCity(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_city_id"></div>
														</div>
													</div>



													<div class="form-group">
														<label class="control-label col-md-3">Pin/Zip<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_contact_pin" type="text"
																class="form-control" id="txt_contact_pin"
																onkeyup="toInteger('txt_contact_pin','Pin')"
																onchange="SecurityCheck('txt_contact_pin',this,'hint_txt_contact_pin')"
																value="<%=mybean.contact_pin%>" size="10" maxlength="6" />
															<div class="hint" id="hint_txt_contact_pin"></div>
															<input name="veh_id" type="hidden" id="veh_id"
																value="<%=mybean.veh_id%>" />
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Notes: </label>
														<div class="col-md-6">
															<textarea name="txt_veh_notes" cols="50" rows="5"
																class="form-control" id="txt_veh_notes"
																onChange="SecurityCheck('txt_veh_notes',this,'hint_txt_veh_notes')"><%=mybean.veh_notes%></textarea>
															<div class="hint" id="hint_txt_veh_notes"></div>
															<%
																if (!mybean.option.equals("")) {
															%>
															Options:
															<%
																}
															%></td>
															<%=mybean.option%>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Entry By: </label>
														<div class="col-md-6 " style="top: 8px">
															<%=mybean.veh_entry_by%>
															<input type="hidden" name="entry_by"
																value="<%=mybean.veh_entry_by%>">
														</div>
													</div>

													<%
														if (mybean.veh_modified_by != null
																&& !mybean.veh_modified_by.equals("")) {
													%>

													<div class="form-group">
														<label class="control-label col-md-3">Modified By:</label>
														<div class="col-md-6 " style="top: 8px">
															<%=mybean.veh_modified_by%>
															<input type="hidden" name="modified_by"
																value="<%=mybean.veh_modified_by%>">
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Modified
															Date:</label>
														<div class="col-md-6 " style="top: 8px">
															<%=mybean.veh_entry_date%>
															<input type="hidden" name="veh_entry_date"
																value="<%=mybean.veh_entry_date%>">
														</div>
													</div>

													<%
														}
													%>
												</form>
											</div>

										</div>
									</div>

									<div class="tab-pane" id="tabs-2">
										<form class="form-horizontal" name="Frmcontact"  method="post" action="vehicle-dash.jsp?veh_id=<%=mybean.veh_id%>#tabs-2">


											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Customer
														Details</div>
												</div>
												<div class="portlet-body portlet-empty">

													<%=mybean.StrCustomerDetails%>
													<%=mybean.customerdetail%>

												</div>
											</div>


											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Add Contact
														Person</div>
												</div>
												<div class="portlet-body portlet-empty">
													<center>
														<font color="red"><%=mybean.contact_msg%></font>
													</center>

													<div class="form-group">
														<label class="control-label col-md-3"> Contact<font
															color=red>*</font>:
														</label>
														<div class="col-md-6 col-xs-12" id="emprows">
															<table width="100%">
																<tr>
																	<td><select name="dr_new_contact_title_id" class="form-control"
																		id="dr_new_contact_title_id">
																			<%=mybean.PopulateTitle(mybean.comp_id, mybean.new_contact_title_id)%>
																	</select> Title</td>
																	<td><input name="txt_new_contact_fname" type="text"
																		class="form-control" id="txt_new_contact_fname"
																		value="<%=mybean.new_contact_fname%>" size="32"
																		maxlength="255" />First Name</td>
																	<td><input name="txt_new_contact_lname" type="text"
																		class="form-control" id="txt_new_contact_lname"
																		value="<%=mybean.new_contact_lname%>" size="32"
																		maxlength="255" />Last Name</td>
																</tr>
															</table>
														</div>
													</div>


													<div class="form-group">
														<label class="control-label col-md-3">Mobile 1<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6 " style="top: 8px">
															<input name="txt_new_contact_mobile1" type="text"
																class="form-control" id="txt_new_contact_mobile1"
																value="<%=mybean.new_contact_mobile1%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile1','Mobile 1')" />
															(91-9999999999)
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Mobile 2: </label>
														<div class="col-md-6 " style="top: 8px">
															<input name="txt_new_contact_mobile2" type="text"
																class="form-control" id="txt_new_contact_mobile2"
																value="<%=mybean.new_contact_mobile2%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile2','Mobile 2')" />
															(91-9999999999)
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Email 1: </label>
														<div class="col-md-6 " style="top: 8px">
															<input name="txt_new_contact_email1" type="text"
																class="form-control" id="txt_new_contact_email1"
																value="<%=mybean.new_contact_email1%>" size="32"
																maxlength="50" />
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Notes: </label>
														<div class="col-md-6 " style="top: 8px">
															<textarea name="txt_new_contact_notes" cols="70" rows="4"
																class="form-control" id="txt_new_contact_notes"><%=mybean.new_contact_notes%></textarea>
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3">Type: </label>
														<div class="col-md-6 " style="top: 8px">
															<select id="dr_new_contact_contacttype_id"
																name="dr_new_contact_contacttype_id"
																class="form-control">
																<%=mybean.PopulateContactType(mybean.comp_id)%>
															</select>
														</div>
													</div>

													<center>
														<input name="add_contact_button" type="submit"
															class="btn btn-success" id="add_contact_button"
															value="Add Contact" />

													</center>


												</div>
											</div>
										</form>
									</div>

									<div class="tab-pane" id="tabs-4">
										<form class="form-horizontal" name="Frmtasks" id="Frmtasks" method="post"
											action="vehicle-dash.jsp?veh_id=<%=mybean.veh_id%>#tabs-4" onsubmit="return Followupvalidation();">
											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none"><%=mybean.status%>&nbsp;
														Follow-up
													</div>
												</div>
												<div class="portlet-body portlet-empty">
													<center>
														<font color="red"><%=mybean.listfollowup_msg%></font>
													</center>
													<center><%=mybean.customerdetail%></center>
													<%--    <%if(mybean.status.equals("Update")){%> --%>

													<div class="form-group">
														<label class="control-label col-md-3">Contactable<font color="red">*</font>:</label>
														<div class="col-md-6 " style="top: 8px">
															<select name="dr_vehfollowup_contactable_id"
																class="form-control" id="dr_vehfollowup_contactable_id" onChange="Contactableband();"
																visible="true">
																<option value=0>Select</option>
																<%=mybean.PopulateServiceContactable(mybean.comp_id, mybean.vehfollowup_contactable_id)%>
															</select>
														</div>
													</div><span id="errormsg" style="margin-left:26%"></span>
													
													<!-- Start Contactable Showing Yes  -->
												<div id="contactableyesband">
													<div class="form-group">
														<label class="control-label col-md-3">Action<font color="red">*</font>:</label>
														<div class="col-md-6 " style="top: 8px">
															<select name="dr_vehfollowup_vehaction_id"
																class="form-control" id="dr_vehfollowup_vehaction_id" onchange="Serviceactionband()"
																visible="true">
																<%=mybean.PopulateServiceAction(mybean.comp_id, mybean.vehfollowup_vehaction_id)%>
															</select>
														</div>
													</div><span id="actionerrormsg" style="margin-left:26%"></span>
													
													<div class="form-group">
														<label class="control-label col-md-3">Current Mileage :</label>
														<div class="col-md-6 " style="top: 8px">
															<input type="text" name="txt_vehfollowup_kms" size="20"
																maxlength="9" class="form-control"
																id="txt_vehfollowup_kms"
																value="<%=mybean.vehfollowup_kms%>"
																onkeyup="toPhone('txt_vehfollowup_kms','Vehicle Kms')" />
														</div>
													</div>
													<!-- Start Action Band -->
													<div id="actionband">
													<!-- Start Book A Service Band -->
												<div id="bookaserviceband">
													<div class="form-group">
														<label class="control-label col-md-3">Workshop<font color="red">*</font>:</label>
														<div class="col-md-6 " style="top: 8px">
															<select name="dr_vehfollowup_workshop_branch_id"
																class="form-control" id="dr_vehfollowup_workshop_branch_id"
																visible="true">
																<%=mybean.PopulateServiceWorkshopBranch(mybean.comp_id, mybean.vehfollowup_workshop_branch_id)%>
															</select>
														</div>
													</div><span id="workshoperrormsg" style="margin-left:26%"></span>

													<div class="form-group">
														<label class="control-label col-md-3">Booking Date Time<font color="red">*</font>:</label>
														<div class="col-md-6 " style="top: 8px">
															<input name="txt_vehfollowup_appt_time" type="text"
																class="form-control date form_datetime"
																id="txt_vehfollowup_appt_time"
																value="<%=mybean.vehfollowup_appt_time1%>" size="20"
																maxlength="16">
														</div>
													</div><span id="datetimeerrormsg" style="margin-left:26%"></span>													

													<div class="form-group">
														<label class="control-label col-md-3">Booking Type<font color="red">*</font>:</label>
														<div class="col-md-6 " style="top: 8px">
															<select name="dr_vehfollowup_bookingtype_id"
																class="form-control" id="dr_vehfollowup_bookingtype_id" Onchange="Pickupband();PopulateDriver()"
																visible="true">
																<%=mybean.PopulateServiceBookingType(mybean.comp_id, mybean.vehfollowup_bookingtype_id)%>
															</select>
														</div>
													</div><span id="bookingerrormsg" style="margin-left:26%"></span>
													<!-- Start PickupBand  -->
													<div id="pickupband">
													<div class="form-group">
														<label class="control-label col-md-3">Driver<font color="red">*</font>:</label>
														<div class="col-md-6 " style="top: 8px">
															<select name="dr_vehfollowup_pickupdriver_emp_id"
																class="form-control" id="dr_vehfollowup_pickupdriver_emp_id"
																visible="true"> 
																<span id="HintDriver">
																<%=mybean.vehicle.PopulateServicePickUp(mybean.comp_id)%>
																</span>
															</select>
														</div>
													</div><span id="drivererrormsg" style="margin-left:26%"></span>
												
												<div class="form-group">
														<label class="control-label col-md-3">Address1:</label>
														 <div class="txt-align" style="color:blue">
														<%=mybean.vehfollowup_pickuplocation%>
														</div>
													</div>

												<div class="form-group">
														<label class="control-label col-md-3">Other Address: </label>
														<div class="col-md-6">
															<textarea name="txt_vehfollowup_pickuplocation" cols="40" rows="4"
																class="form-control" id="txt_vehfollowup_pickuplocation"
																onchange="SecurityCheck('txt_vehfollowup_pickuplocation',this,'hint_txt_vehfollowup_pickuplocation')"
																onkeyup="charcount('txt_vehfollowup_pickuplocation', 'span_txt_vehfollowup_pickuplocation','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.vehfollowup_pickuplocation%></textarea>
															<span id="span_txt_vehfollowup_pickuplocation"> (255
																Characters)</span>
															<div class="hint" id="hint_txt_vehfollowup_pickuplocation"></div>
														</div>
													</div>
												</div>
													<!-- End PickupBand  -->
											</div>
													<!-- End Book A Service Band -->
													
													<div class="form-group" id="callatterband">
														<label class="control-label col-md-3">Next Follow-up Time<font color="red">*</font>:</label>
														<div class="col-md-6 " style="top: 8px">
															<input name="txt_vehfollowup_time" type="text"
																class="form-control date form_datetime"
																id="txt_vehfollowup_time"
																value="<%=mybean.vehfollowup_time%>" size="20"
																maxlength="16">
														</div>
													</div><span id="nextfollowerrormsg" style="margin-left:26%"></span>
													
													<div class="form-group" id="lostcaseband">
														<label class="control-label col-md-3">Lost Case<font color="red">*</font>:</label>
														<div class="col-md-6 " style="top: 8px">
															<select name="dr_vehlostcase1_id" class="form-control"
																id="dr_vehlostcase1_id" visible="true">
																<%=mybean.PopulateVehLostcase1(mybean.comp_id, mybean.vehlostcase1_id)%>
															</select>
														</div>
													</div><span id="lostcaseerrormsg" style="margin-left:26%"></span>
													</div><!-- End Action Band -->
											 </div><!-- End Contactable Showing Yes  -->
															
											<div id="contactablenoband">
													<div class="form-group">
														<label class="control-label col-md-3">Reason<font color="red">*</font>:</label>
														<div class="col-md-6 " style="top: 8px">
															<select name="dr_vehfollowup_notcontactable_id"
																class="form-control" id="dr_vehfollowup_notcontactable_id"
																visible="true">
																<%=mybean.PopulateServiceNotContactable(mybean.comp_id, mybean.vehfollowup_notcontactable_id)%>
															</select>
														 </div>
													</div><span id="reasonerrormsg" style="margin-left:26%"></span>
										  </div>
													
													<div class="form-group" id="feedbackband">
														<label class="control-label col-md-3">Feedback</label>
														<div class="col-md-6 " style="top: 8px">
															<textarea name="txt_vehfollowup_desc" cols="50" rows="4"
																class="form-control" id="txt_vehfollowup_desc"
																onKeyUp="charcount('txt_vehfollowup_desc', 'span_txt_vehfollowup_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.vehfollowup_desc%></textarea>
															<span id="span_txt_vehfollowup_desc">1000
																characters</span>
														</div>
													</div>
									
													<center>
														<input name="add_followup_button" type="submit"
															class="btn btn-success" id="add_followup_button"
															value="Add" /> <input type="hidden" name="veh_id"
															id="veh_id" value="<%=mybean.veh_id%>" ></input>
													</center>
													<center><%=mybean.listfollowup_info%></center>

												</div>
											</div>
										</form>
									</div>

									<div class="tab-pane" id="tabs-10">

										<div class="portlet box  ">
											<%-- 										<a href="../insurance/insurance-update.jsp?add=yes&veh_id=<%mybean.veh_id%>" target="_blank\">Add New Insurance... </a> --%>
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Add Follow-up</div>
											</div>
											<div class="portlet-body portlet-empty">
												<center style="text-align: right">
													<a
														href="../insurance/insurance-update.jsp?add=yes&insurpolicy_veh_id=<%=mybean.veh_id%>"
														target="_blank\">Add New Insurance... </a>
												</center>
												<center>
													<font color="red"><%=mybean.list_insurfollowup_msg%></font>
												</center>
												<center><%=mybean.insurcustomerdetail%></center>

												<!-- START PORTLET BODY -->
												<form class="form-horizontal">

													<div class="form-group">
														<label class="col-md-3 control-label">Follow-up
															Action<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_insurfollowup_insuraction_id"
																class="form-control"
																id="dr_insurfollowup_insuraction_id">
																<%=mybean.PopulateFollowupAction(mybean.comp_id)%>
															</select>
														</div>
													</div>


													<div class="form-group">
														<label class="col-md-3 control-label">Feedback<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<textarea name="txt_insurfollowup_desc" cols="50"
																rows="4" class="form-control"
																id="txt_insurfollowup_desc"
																onKeyUp="charcount('txt_insurfollowup_desc', 'span_txt_insurfollowup_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.insurfollowup_desc%></textarea>
															<span id="span_txt_insurfollowup_desc">1000
																characters</span>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-3 control-label">Lost Case: </label>
														<div class="col-md-6">
															<select name="dr_insurlostcase1_id" class="form-control"
																id="dr_insurlostcase1_id" visible="true"
																onchange="checknextfollowuptime();">
																<%=mybean.PopulateInsurLostCase(mybean.comp_id,
					mybean.insurlostcase1_id)%>
															</select>
														</div>
													</div>


													<div class="form-group">
														<label class="col-md-3 control-label">Contactable<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_insurfollowup_psffeedbacktype_id"
																class="form-control"
																id="dr_insurfollowup_psffeedbacktype_id" visible="true">
																<%=mybean.PopulateInsurancePsfFeedback(mybean.comp_id,
					mybean.insurfollowup_psffeedbacktype_id)%>
															</select>
														</div>
													</div>


													<div class="form-group">
														<label class="col-md-3 control-label">Next
															Follow-up Time<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_insurfollowup_time" type="text"
																class="form-control date form_datetime"
																id="txt_insurfollowup_time"
																value="<%=mybean.insurfollowup_time%>" size="20"
																maxlength="16">
														</div>

													</div>



													<div class="form-group">
														<label class="col-md-3 control-label">Next
															Follow-up Type<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_insurfollowup_followuptype_id"
																class="form-control"
																id="dr_insurfollowup_followuptype_id" visible="true">
																<%=mybean.PopulateFollowuptype(mybean.comp_id)%>
															</select>
														</div>
													</div>




													<div class="form-group">
														<label class="col-md-3 control-label">Next
															Follow-up Priority<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_insurfollowup_priorityinsurfollowup_id"
																class="form-control"
																id="dr_insurfollowup_priorityinsurfollowup_id"
																visible="true">
																<%=mybean.PopulateFollowupPriority(mybean.comp_id)%>
															</select>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-3 control-label">Field
															Executive: </label>
														<div class="col-md-6">
															<select name="dr_insur_field_emp_id"
																id="dr_insur_field_emp_id" class="form-control">
																<%=mybean.PopulateFieldExecutive()%>
															</select>
														</div>
													</div>

													<center>
														<input name="submit_button" type="submit"
															class="btn btn-success" id="submit_button" value="Submit" />
														<input type="hidden" name="veh_id" id="veh_id"
															value="<%=mybean.veh_id%>">
													</center>
													<center><%=mybean.followup_info%></center>

												</form>
											</div>
										</div>
									</div>



									<div class="tab-pane" id="tabs-12">

										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Stock Status</div>
											</div>
											<div class="portlet-body portlet-empty">
												<form class="form-horizontal" id="" name="">

													<div class="form-group">
														<label class="col-md-3 control-label">Branch: </label>
														<div class="col-md-6">
															<select name="dr_branch" id="dr_branch"
																class="form-control"
																onChange="PopulateItem(this.value);">
																<%=mybean.PopulateBranch(mybean.branch_id, "", "1,3",
					request)%>
															</select>
														</div>
													</div>

													<div class="form-group">
														<label class="col-md-3 control-label">Location<font
															color=red>*</font>:
														</label>
														<div class="col-md-6">
															<%=mybean.PopulateLocation(mybean.branch_id)%>
														</div>
													</div>

													<div class="form-group">
														<label class="col-md-3 control-label">Enter Search
															Text: </label>
														<div class="col-md-6">
															<input type="text" id="txt_item" name="txt_item"
																class="form-control" size="42"
																onkeyup="SearchStockStatus(this.value);" />
														</div>
													</div>
												</form>
											</div>
										</div>


									</div>
									<div class="tab-pane" id="tabs-3"></div>
									<div class="tab-pane" id="tabs-5"></div>
									<div class="tab-pane" id="tabs-6"></div>
									<div class="tab-pane" id="tabs-7"></div>
									<div class="tab-pane" id="tabs-8"></div>
									<div class="tab-pane" id="tabs-9"></div>
									<div class="tab-pane" id="tabs-11"></div>
									<div class="tab-pane" id="tabs-13"></div>
								</div>
							</div>

						</div>

					</div>
					<!-- 				page-content-inner END -->
				</div>
			</div>
		</div>

	</div>
	

</body>
</HTML>
