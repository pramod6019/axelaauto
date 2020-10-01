<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.JobCard_QuickAdd"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>&nbsp;Job Card
						</h1>
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
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="jobcard-list.jsp?all=yes">List Job Cards</a> &gt;</li>
						<li><a href="jobcard-quickadd.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Job
								Card</a><b> :</b>
						</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Job Card
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" id="form1" method="post"
											class="form-horizontal">
											<center>
												<input type="hidden" id="txt_veh_id" name="txt_veh_id"
													value="<%=mybean.jc_veh_id%>"> <input type="hidden"
													id="txt_variant_id" name="txt_variant_id"
													value="<%=mybean.veh_variant_id%>"> <input
														type="hidden" id="txt_model_id" name="txt_model_id"
														value="<%=mybean.jc_model_id%>"> <input
															name="txt_veh_chassis_no" type="hidden"
															id="txt_veh_chassis_no"
															value="<%=mybean.veh_chassis_no%>" /> <input
															name="txt_veh_engine_no" type="hidden"
															id="txt_veh_engine_no" value="<%=mybean.veh_engine_no%>" />
															<input name="txt_veh_reg_no" type="hidden"
															id="txt_veh_reg_no" value="<%=mybean.veh_reg_no%>" /> <input
															name="acct_id" type="hidden" id="acct_id"
															value="<%=mybean.jc_customer_id%>" /> <input
															name="cont_id" type="hidden" id="cont_id"
															value="<%=mybean.jc_contact_id%>" />
															<font size="1">Form
																	fields marked with a red asterisk <b><font
																		color="#ff0000">*</font></b> are required.
															</font>
											</center>
												<div class="form-element6">
														<label >Time In<font
															color=#ff0000><b>*</b></font>:
														</label>
																<input type="text" size="16" name="txt_jc_time_in"
																	id="txt_jc_time_in" value="<%=mybean.jc_time_in%>"
																	class="form-control datetimepicker">
												</div>

												<div class="form-element6">
														<label >Promised
															Time<font color=#ff0000><b>*</b></font>:
														</label>
																<input type="text" size="16" name="txt_jc_time_promised"
																	id="txt_jc_time_promised"
																	value="<%=mybean.jc_time_promised%>"
																	class="form-control datetimepicker"> 
												</div>
												<div class="form-element6">
														<label >Ready Time:</label>
																<input type="text" size="16" name="txt_jc_comptime"
																	id="txt_jc_comptime" value="<%=mybean.jc_time_ready%>"
																	class="form-control datetimepicker"> 
												</div>
												<div class="form-element6">
														<label >Delivered
															Time:</label>
																<input type="text" size="16" name="txt_jc_deltime"
																	id="txt_jc_deltime" value="<%=mybean.jc_time_out%>"
																	class="form-control datetimepicker"> 
												</div>
												<div class="form-element6">
														<label >Type<font
															color="red">*</font>:
														</label>
															<select name="dr_jc_type" id="dr_jc_type"
																class="form-control">
																<%=mybean.PopulateJobCardType()%>
															</select>
												</div>
												<div class="form-element6">
														<label >Category<font
															color="red">*</font>:
														</label>
															<select name="dr_jc_cat" id="dr_jc_cat"
																class="form-control">
																<%=mybean.PopulateJobCardCategory()%>
															</select>
												</div>
											<%
												if (!mybean.jc_contact_id.equals("") && !mybean.jc_contact_id.equals("0")) {
											%>
											<%
												}
											%>
											<div class="row">
												<div class="form-element6" id="div_jc_customer_id">
														<label >Customer: </label>
															<b><%=mybean.link_customer_name%></b>
												</div>
												<div class="form-element6" id="div_jc_contact_id">
														<label >Contact: </label>
															<b><%=mybean.link_contact_name%></b>
												</div>
											</div>
											
											<div class="row">
											<div class="form-element6" id="div_veh_chassis_no">
														<label >Chassis
															Number: </label>
															<b><%=mybean.veh_chassis_no%></b>
											</div>
												<div class="form-element6" id="div_veh_engine_no">
														<label >Engine No.:
														</label>
															<b><%=mybean.veh_engine_no%></b>
												</div>
												

											</div>
											
												<div class="form-element6" id="div_veh_reg_no">
														<label >Reg. No.: </label>
															<b><%=mybean.veh_reg_no%></b>
														</div>
													<div class="form-element6">
														<label >Vehicle: </label>
															<b><span id="span_jc_veh_id" name="span_jc_veh_id">
																	<%
																		if (mybean.jc_veh_id == null || mybean.jc_veh_id.equals("0")) {
																	%> <%
																		} else {
																	%> <%=mybean.link_veh_name%> <% } %>
															</span></b>
<!-- 															&nbsp;<a href="#" id="jc_veh_link">(Select Vehicle)</a> -->
															 <input name="span_veh_id" type="hidden" id="span_veh_id"
																value="<%=mybean.veh_id%>" /> <input name="veh_id"
																type="hidden" id="veh_id" value="<%=mybean.jc_veh_id%>" />
													</div>
												<div class="form-element6">
														<label >Kms<font
															color="#ff0000">*</font>:
														</label>
															<input name="txt_jc_kms" type="text" class="form-control"
																id="txt_jc_kms" value="<%=mybean.jc_kms%>" size="20"
																maxlength="25" onkeyup="toInteger(this.id);">
												</div>

											<div class="row "></div>
											<div class="row ">
												<div class="form-element6">
													<div class="portlet box  ">
														<div class="portlet-title" style="text-align: center">
															<div class="caption" style="float: none">Billing
																Address</div>
														</div>
														<div class="portlet-body portlet-empty">
															<div class="tab-pane" id="">
																<!-- START PORTLET BODY -->
																<div class="row">
																<div class="form-element6">
																	<label >Address<font
																		color="#ff0000">*</font>:
																	</label>
																		<textarea name="txt_bill_address" cols="48" rows="5"
																			class="form-control" id="txt_bill_address"
																			onKeyUp="charcount('txt_bill_address', 'span_txt_bill_address','<font color=red>({CHAR} characters left)
																		</font>', '255')"><%=mybean.jc_bill_address%></textarea>
																		 <span id="span_txt_bill_address">
																				(255 Characters)</span>
																</div>
																<div class="form-element6">
																	<label>City<font
																		color="#ff0000">*</font>:
																	</label>
																		<input name="txt_bill_city" type="text"
																			class="form-control" id="txt_bill_city"
																			value="<%=mybean.jc_bill_city%>" size="50"
																			MaxLength="255">
																</div>
																</div>
																<div class="row">
																<div class="form-element6">
																	<label >Pin/Zip<font
																		color="#ff0000">*</font>:
																	</label>
																		<input name="txt_bill_pin" type="text"
																			class="form-control" id="txt_bill_pin"
																			onKeyUp="toInteger('txt_bill_pin','Pin')"
																			value="<%=mybean.jc_bill_pin%>" size="50"
																			maxlength="10" />
																</div>
																<div class="form-element6">
																	<label >State<font
																		color="#ff0000">*</font>:
																	</label>
																		<input name="txt_bill_state" type="text"
																			class="form-control" id="txt_bill_state"
																			value="<%=mybean.jc_bill_state%>" size="50"
																			MaxLength="255">
																	</div>
																</div>

															</div>
														</div>
													</div>
												</div>
												<div class="form-element6">
													<div class="portlet box  ">
														<div class="portlet-title" style="text-align: center">
															<div class="caption" style="float: none">Delivery Address (<a
																	href="javascript:CopyBillingAddress();"
																	style="color: white">Same as Billing</a>)</div>
														</div>
														<div class="portlet-body portlet-empty">
															<div class="tab-pane" id="">
																<!-- START PORTLET BODY -->
																	<div class="row">
																<div class="form-element6">
																	<label >Address<font
																		color="#ff0000">*</font>:
																	</label>
																		<textarea name="txt_del_address" cols="48" rows="5"
																			class="form-control" id="txt_del_address"
																			onKeyUp="charcount('txt_del_address', 'span_txt_del_address',' <font color=red>({CHAR} characters left)</font>', '255')"> <%=mybean.jc_del_address%></textarea>
																		<span id="span_txt_del_address"> (255
																				Characters)</span>
																</div>
																<div class="form-element6">
																	<label >City<font
																		color="#ff0000">*</font>:
																	</label>
																		<input name="txt_del_city" type="text"
																			class="form-control" id="txt_del_city"
																			value="<%=mybean.jc_del_city%>" size="50"
																			MaxLength="255">
																</div>	</div><div class="row">
																<div class="form-element6">
																	<label >Pin/Zip<font
																		color="#ff0000">*</font>:
																	</label>
																		<input name="txt_del_pin" type="text"
																			class="form-control" id="txt_del_pin"
																			onKeyUp="toInteger('txt_del_pin','Pin')"
																			value="<%=mybean.jc_del_pin%>" size="50"
																			maxlength="10" />
																</div>
																<div class="form-element6">
																	<label >State<font
																		color="#ff0000">*</font>:
																	</label>
																		<input name="txt_del_state" type="text"
																			class="form-control" id="txt_del_state"
																			value="<%=mybean.jc_del_state%>" size="50"
																			MaxLength="255">
																	</div>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
											<div class="row ">
												<div class="form-element6">
														<label >Customer Voice<font color="#ff0000">*</font>:
														</label>
															<textarea name="jc_cust_voice" id="jc_cust_voice"
																cols="50" rows="5" class="form-control"><%=mybean.jc_cust_voice%></textarea>
												</div>
												<div class="form-element6">
														<label>Service Advice:</label>
															<textarea name="txt_jc_advice" id="txt_jc_advice"
																cols="50" rows="5" class="form-control"><%=mybean.jc_advice%></textarea>
												</div>
											</div>
											<div class="row ">
												<div class="form-element6">
														<label >Service Instruction: </label>
															<textarea name="txt_jc_instructions" 
															id="txt_jc_instructions" cols="50" rows="5" class="form-control"><%=mybean.jc_instructions%></textarea>
													</div>
												<div class="form-element6">
														<label >Parts Cost: </label>
															<input name="txt_jc_part_cost" type="text" class="form-control"  id ="txt_jc_part_cost" 
															value = "<%=mybean.jc_part_cost%>" onkeyup="toFloat(this.id);" size="10" maxlength="10">
												</div>
												</div>
												<div class="row">
												<div class="form-element6">
														<label >Labour Cost: </label>
															<input name="txt_jc_labour_cost" type="text" class="form-control"  id ="txt_jc_labour_cost" 
															value ="<%=mybean.jc_labour_cost%>" onkeyup="toFloat(this.id);"  maxlength="10">
												</div>
												
												<div class="form-element6">
														<label >Service Advisor<font color="red">*</font>: </label>
															<select name="dr_jc_emp_id" id="dr_jc_emp_id"
																class="form-control">
																<%=mybean.PopulateExecutive()%>
															</select>
												</div>
												</div>
												<div class="row">
												<div class="form-element6">
														<label >Technician: </label>
															<select name="dr_jc_technician_emp_id"  id="dr_jc_technician_emp_id" class="form-control">
											                        <%=mybean.PopulateTechnician()%>
											                      </select>
												</div>
												<div class="form-element6">
														<label >RO.No.: </label>
															<input name="txt_jc_ro_no" type="text" class="form-control"  id ="txt_jc_ro_no" 
															value = "<%=mybean.jc_ro_no%>" size="20" maxlength="20"/>
												</div>
												</div>
												<div class="row">
												<div class="form-element6">
														<label>Notes:
														</label>
															<textarea name="txt_jc_notes" cols="70" rows="4" 
															class="form-control" id="txt_jc_notes"><%=mybean.jc_notes%></textarea>
												</div>
												</div>
												<center>
												 <input name="addbutton" id="addbutton" type="button" onClick="return SubmitFormOnce(document.form1, this);" class="btn btn-success" value="Add Job Card" />
                    							  <input type="hidden" id="add_button" name="add_button" value="yes"/>
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
	</div>
<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
	

		<script type="text/javascript" src="../Library/sc.js?Math.random()"></script>
		


<script type="text/javascript">

	$(function() {
		// Dialog
		$('#dialog-modal').dialog({
			autoOpen : false,
			width : 900,
			height : 500,
			zIndex : 200,
			modal : true,
			title : "Select Vehicle"
		});
		$('#jc_veh_link')
				.click(
						function() {
							$
									.ajax({
										success : function(data) {
											$('#dialog-modal')
													.html(
															'<iframe src="../service/vehicle-list.jsp?group=select_jc_veh" width="100%" height="100%" frameborder=0></iframe>');
										}
									});
							$('#dialog-modal').dialog('open');
							return true;
						});
	});

	function CopyBillingAddress() {
		var billing_address = document.getElementById("txt_bill_address").value;
		var billing_city = document.getElementById("txt_bill_city").value;
		var billing_pin = document.getElementById("txt_bill_pin").value;
		var billing_state = document.getElementById("txt_bill_state").value;
		document.getElementById("txt_del_address").value = billing_address;
		document.getElementById("txt_del_city").value = billing_city;
		document.getElementById("txt_del_pin").value = billing_pin;
		document.getElementById("txt_del_state").value = billing_state;
	}

	function SelectVeh(veh_id, item_id, item_name, veh_chassis_no,
			veh_engine_no, veh_reg_no, customer_id, contact_id, customer_name,
			contact_name) {

		document.getElementById("div_veh_chassis_no").innerHTML = veh_chassis_no;
		document.getElementById("div_veh_engine_no").innerHTML = veh_engine_no;
		document.getElementById("div_veh_reg_no").innerHTML = veh_reg_no;
		document.getElementById("div_jc_customer_id").innerHTML = "<a href=../customer/customer-list.jsp?customer_id="
				+ customer_id + ">" + customer_name + "</a>";
		document.getElementById("div_jc_contact_id").innerHTML = "<a href=../customer/customer-contact-list.jsp?contact_id="
				+ contact_id + ">" + contact_name + "</a>";

		document.getElementById("acct_id").value = customer_id;
		document.getElementById("cont_id").value = contact_id;
		document.getElementById("txt_veh_chassis_no").value = veh_chassis_no;
		document.getElementById("txt_veh_engine_no").value = veh_engine_no;
		document.getElementById("txt_veh_reg_no").value = veh_reg_no;
		document.getElementById("txt_variant_id").value = item_id;
		document.getElementById("span_veh_id").value = veh_id;
		document.getElementById("span_jc_veh_id").innerHTML = "<a href=../service/vehicle-list.jsp?veh_id="
				+ veh_id + ">" + item_name + " </a>";
		$('#dialog-modal').dialog('close');
	}
</script>
	
</body>
</HTML>
