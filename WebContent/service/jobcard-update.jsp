
<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.JobCard_Update"
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
<link REL="stylesheet" type="text/css" href="../Library/slider.css" />
</HEAD>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
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
			<!--- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="jobcard-list.jsp?all=yes">List Job Cards</a>&gt;</li>
						<li><a href="jobcard-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Job
								Card</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%> Job Card </center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<input type="hidden" id="txt_veh_id" name="txt_veh_id" value="<%=mybean.jc_veh_id%>">
												<input type="hidden" id="txt_variant_id" name="txt_variant_id" value="<%=mybean.jc_variant_id%>">
												<input type="hidden" id="txt_model_id" name="txt_model_id" value="<%=mybean.modelid%>">
												<input name="txt_veh_chassis_no" type="hidden" id="txt_veh_chassis_no" value="<%=mybean.veh_chassis_no%>" />
												<input name="txt_veh_engine_no" type="hidden" id="txt_veh_engine_no" value="<%=mybean.veh_engine_no%>" />
												<input name="txt_veh_reg_no" type="hidden" id="txt_veh_reg_no" value="<%=mybean.veh_reg_no%>" />
												<input name="acct_id" type="hidden" id="acct_id" value="<%=mybean.jc_customer_id%>" /> 
												<input name="cont_id" type="hidden" id="cont_id" value="<%=mybean.jc_contact_id%>" /> <font size="">Form
													fields marked with a red asterisk <font color=#ff0000>*</font> are required.
												</font>
											</center>
											<div class="form-element6">
												<label >Title<font color="red">*</font>: </label>
												<input name="txt_jc_title" type="text"
													class="form-control" id="txt_jc_title"
													value="<%=mybean.jc_title%>" size="40" maxlength="255">
												<input type="hidden" name="txt_status" id="txt_status"
													value="<%=mybean.status%>" /> <input type="hidden"
													name="jobcard_contact" id="jobcard_contact" value="" />
												<div id="dialog-modal"></div>
											</div>

											<div class="form-element6">
												<label >Branch<font color="red">*</font>: </label>
													<% if (mybean.jc_branch_id.equals("0") || mybean.update.equals("yes")) { %>
														<select name="dr_branch" class="form-control"
															id="dr_branch"
															onChange="PopulateInventoryLocation();PopulateBranchExecutive();">
															<%=mybean.PopulateBranch(mybean.jc_branch_id, "", "3", "", request)%>
														</select>
													<% } else { %>
														<input type="hidden" id="dr_branch" name="dr_branch" value="<%=mybean.jc_branch_id%>" />
														<%=mybean.getBranchName(mybean.jc_branch_id, mybean.comp_id)%>
													<% } %>

											</div>
											<div class="form-element6">
												<label >Time In<font color="red">*</font>: </label>
												<input type="text" name="txt_jc_time_in"
														id="txt_jc_time_in" value="<%=mybean.jc_time_in%>"
														class="form-control datetimepicker">
											</div>
											<div class="form-element6">
												<label >Promised Time<font color="red">*</font>: </label>
												<input type="text" name="txt_jc_time_promised"
														id="txt_jc_time_promised"
														value="<%=mybean.jc_time_promised%>"
														class="form-control datetimepicker">
											</div>
											<% if (mybean.status.equals("Update")) { %>
												<div class="form-element6 ">
													<label >Ready Time:</label>
													<input type="text" name="txt_jc_comptime"
														id="txt_jc_comptime" value="<%=mybean.jc_time_ready%>"
														class="form-control datetimepicker">
												</div>
												<div class="form-element6">
													<label >Delivered Time:</label>
													<input type="text" name="txt_jc_deltime"
														id="txt_jc_deltime" value="<%=mybean.jc_time_out%>"
														class="form-control datetimepicker"> 
													</span>
												</div>
											<% } %>
											<div class="form-element6 ">
												<label >Posted Date: </label>
												<input type="text" size="50" name="txt_jc_time_posted"
															id="txt_jc_time_posted" value="<%=mybean.jc_time_posted%>"
															class="form-control datetimepicker"> 
											</div>
											<div class="form-element6">
												<label >Stage<font color="red">*</font>: </label>
												<select name="dr_jc_stage" id="dr_jc_stage" class="form-control">
													<%=mybean.PopulateStage()%>
												</select>
											</div>
											<div class="form-element6 ">
												<label >Type<font color="#ff0000">*</font>: </label>
												<select name="dr_jc_type" id="dr_jc_type"
													class="form-control">
													<%=mybean.PopulateJobCardType()%>
												</select>
											</div>

											<div class="form-element6">
												<label >Category<font color="red">*</font>: </label>
												<select name="dr_jc_cat" id="dr_jc_cat"
													class="form-control">
													<%=mybean.PopulateJobCardCategory()%>
												</select>
											</div>
											<% if (!mybean.jc_contact_id.equals("") && !mybean.jc_contact_id.equals("0")) { %>
											<% } %>
											<div class="form-element6"id="div_jc_customer_id">
												<label>Customer:</label> <b> <%=mybean.link_customer_name%> </b>
											</div>
											<div class="form-element6" id="div_jc_contact_id">
												<label>Contact:</label> <b><%=mybean.link_contact_name%></b>
											</div>
											
											<div class="form-element6" id="div_veh_chassis_no ">
												<label>Chassis Number:</label> <b><%=mybean.veh_chassis_no%></b>
											</div>

											<div class="form-element6" id="div_veh_engine_no ">
												<label>Engine No.: </label> <b><%=mybean.veh_engine_no%></b>
											</div>
											<div class="form-element6" id="div_veh_reg_no ">
												<label>Vehicle Reg. No.:</label> <b><%=mybean.veh_reg_no%></b>
											</div>
											<div class="form-element6 ">
												<label >Vehicle:</label>
												<b><span id="span_jc_veh_id" name="span_jc_veh_id">
													<% if(mybean.jc_veh_id==null || mybean.jc_veh_id.equals("0")){ %>
													<% } else { %>
														<%=mybean.link_veh_name%>
													<% } %>
												</span></b>
												<input name="span_veh_id" type="hidden" id="span_veh_id" value="<%=mybean.veh_id%>" />
												<input name="veh_id" type="hidden" id="veh_id" value="<%=mybean.jc_veh_id%>" />
											</div>
											<div class="form-element6">
												<label >Kms<font color="#ff0000">*</font>: </label>
												<input name="txt_jc_kms" type="text" class="form-control"
													id="txt_jc_kms" value="<%=mybean.jc_kms%>" size="20"
													maxlength="25" onkeyup="toInteger(this.id);">
											</div>
											<div class="form-element6">
												<label >Fuel Guage<font color="#ff0000">*</font>: </label>
												<input type="hidden" id="txt_jc_fuel_guage"
													name="txt_jc_fuel_guage"
													value="<%=mybean.jc_fuel_guage%>"
													onChange="SecurityCheck('txt_jc_fuel_guage',this,'hint_txt_jc_fuel_guage');" />
												<b><span name="div_jc_fuel_guage" id="div_jc_fuel_guage"></span>%</b>
												<div id="div_fuel_guage" class="col-md-6 form-control"></div>
												<div class="col-md-12">
													<div class="col-md-1 col-xs-1">0%</div>
													<div class="col-md-offset-4 col-xs-offset-4 col-md-2 col-xs-2" style="text-align:center">    50%</div>
													<div class="col-md-offset-4 col-xs-offset-4 col-md-1 col-xs-1" style="text-align:right">100%</div>
												</div>
												<div class="hint" id="hint_txt_jc_fuel_guage"></div>
											</div>
											<div class="row "></div>
											<div class="form-element6">
												<label >JC Reg. No.<font color="#ff0000">*</font>: </label>
												<input name="txt_jc_reg_no" type="text"
													class="form-control" id="txt_jc_reg_no"
													value="<%=mybean.jc_reg_no%>" size="20" maxlength="20">
											</div>
											<div class="form-element6">
												<label>Warranty:</label>
												<%if(mybean.status.equals("Add")){ %>
													<input id="chk_jc_warranty" type="checkbox" name="chk_jc_warranty"
															<%=mybean.PopulateCheck(mybean.jc_warranty)%> />
												<%}else if(mybean.status.equals("Update")){ %>
													<%=mybean.jcwarranty %>
												<%} %>
											</div>
											<div class="form-element6"id="div_jc_refcustomer_id">
												<label>Reference Customer:</label> <b> <%=mybean.link_refcustomer_name%> </b>
											</div>	
											<div class="row "></div>
											<div id="dialog-modal-veh"></div>
											<div class="row">
												<div class="form-element6">
													<div class="portlet box">
														<div class="portlet-title" style="text-align: center">
															<div class="caption" style="float: none">Billing Address</div>
														</div>
														<div class="portlet-body portlet-empty">
															<div class="tab-pane" id="">
																<!-- START PORTLET BODY -->
																<div class="row">
																<div class="form-element6">
																	<label >Address<font color="#ff0000">*</font>:
																	</label>
																		<textarea name="txt_bill_address" cols="48" rows="5"
																			class="form-control" id="txt_bill_address"
																			onKeyUp="charcount('txt_bill_address', 'span_txt_bill_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.jc_bill_address%></textarea>
																		<br> <span id="span_txt_bill_address">
																				(255 Characters)</span>
																</div>
																<div class="form-element6">
																	<label >City<font color="#ff0000">*</font>: </label>
																		<input name="txt_bill_city" type="text"
																			class="form-control" id="txt_bill_city"
																			value="<%=mybean.jc_bill_city%>" size="50"
																			MaxLength="255">
																</div>
																</div>
																<div class="row">
																<div class="form-element6">
																	<label >Pin<font color="#ff0000">*</font>: </label>
																		<input name="txt_bill_pin" type="text"
																			class="form-control" id="txt_bill_pin"
																			onKeyUp="toInteger('txt_bill_pin','Pin')"
																			value="<%=mybean.jc_bill_pin%>" size="50"
																			maxlength="10" />
																</div>
																<div class="form-element6">
																	<label >State<font color="#ff0000">*</font>: </label>
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
													<div class="portlet box">
														<div class="portlet-title" style="text-align: center">
															<div class="caption" style="float: none">
																Delivery Address (<a
																	href="javascript:CopyBillingAddress();"
																	style="color: white">Same as Billing</a>)
															</div>
														</div>
														<div class="portlet-body portlet-empty">
															<div class="tab-pane" id="">
																<!-- START PORTLET BODY -->
																<div class="row">
																<div class="form-element6">
																	<label >Address<font color="#ff0000">*</font>: </label>
																		<textarea name="txt_del_address" cols="48" rows="5"
																			class="form-control" id="txt_del_address"
																			onKeyUp="charcount('txt_del_address', 'span_txt_del_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.jc_del_address%></textarea>
																		<br> <span id="span_txt_del_address"> (255
																				Characters)</span>
																</div>
																<div class="form-element6">
																	<label >City<font color="#ff0000">*</font>: </label>
																		<input name="txt_del_city" type="text"
																			class="form-control" id="txt_del_city"
																			value="<%=mybean.jc_del_city%>" size="50"
																			MaxLength="255">
																</div>
																</div>
																<div class="row">
																<div class="form-element6">
																	<label >Pin<font color="#ff0000">*</font>: </label>
																		<input name="txt_del_pin" type="text"
																			class="form-control" id="txt_del_pin"
																			onKeyUp="toInteger('txt_del_pin','Pin')"
																			value="<%=mybean.jc_del_pin%>" size="50"
																			maxlength="10" />
																</div>
																<div class="form-element6">
																	<label >State<font color="#ff0000">*</font>: </label>
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
												<div class="form-element6">
														<label >Customer Voice<font color="#ff0000">*</font>: </label>
															<textarea name="jc_cust_voice" id="jc_cust_voice"
																cols="50" rows="5" class="form-control"><%=mybean.jc_cust_voice%></textarea>
												</div>

												<div class="form-element6">
														<label >Service Advice:</label>
															<textarea name="txt_jc_advice" id="txt_jc_advice"
																cols="50" rows="5" class="form-control"><%=mybean.jc_advice%></textarea>
												</div>
												<div class="form-element6">
														<label >Service Instruction:</label>
															<textarea name="txt_jc_instructions"
																id="txt_jc_instructions" cols="50" rows="5"
																class="form-control"><%=mybean.jc_instructions%></textarea>
												</div>
												<div class="form-element6">
														<label >Terms:</label>
															<textarea name="txt_jc_terms" id="txt_jc_terms" cols="50"
																rows="5" class="form-control"><%=mybean.jc_terms%></textarea>
												</div>
												<div class="form-element6">
														<%=mybean.PopulateVehicleInventory(request)%>
												</div>
												<div class="form-element6">
											<label >RO No.<font color="#ff0000">*</font>: </label>
															<input name="txt_jc_ro_no" type="text"
																class="form-control" id="txt_jc_ro_no"
																value="<%=mybean.jc_ro_no%>" size="20" maxlength="20">
												</div>
												<div class="row"></div>
												<div class="form-element6">
														<label >Priority<font color="#ff0000">*</font>: </label>
															<select name="dr_jc_priority" class="form-control"
																id="dr_jc_priority">
																<%=mybean.PopulatePriority()%>
															</select>
												</div>
												<div class="form-element6">
														<label >Service Advisor<font color="#ff0000">*</font>: </label>
															<span id="dr_executive"> 
															<%=mybean.PopulateExecutive(mybean.jc_branch_id, mybean.comp_id)%>
															</span>
												</div>
												<div class="form-element6">
														<label >Technician<font color="#ff0000">*</font>: </label>
															<span id="dr_jc_technician_emp_id">
																<%=mybean.PopulateTechnicianExecutive(mybean.jc_branch_id, mybean.comp_id)%>
														</span>
												</div>
												<div class="form-element6">
														<label>Inventory Location<font color="#ff0000">*</font>: </label>
														<span id="dr_location">
														<%=mybean.PopulateInventoryLocation(mybean.jc_branch_id, mybean.comp_id)%>
														</span>
												</div>
											<div class="form-element6 ">
												<div class="form-element6 ">
														<label >Active:</label>
															<input id="chk_jc_active" type="checkbox"
																name="chk_jc_active"
																<%=mybean.PopulateCheck(mybean.jc_active)%> />
												</div>
													<div class="form-element6 ">
														<label >Critical:</label>
															<input id="chk_jc_critical" type="checkbox"
																name="chk_jc_critical"
																<%=mybean.PopulateCheck(mybean.jc_critical)%> />
													</div>
											</div>
											<div class="form-element6 ">
														<label>Notes:</label>
															<textarea name="txt_jc_notes" cols="70" rows="4"
																class="form-control" id="txt_jc_notes"><%=mybean.jc_notes%></textarea>
												</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.jc_entry_date == null) && !(mybean.jc_entry_date.equals(""))) {
											%>
											<div class="row ">
												<div class="form-element6 ">
														<label >Entry By:</label>
															<%=mybean.unescapehtml(mybean.entry_by)%>
															<input name="entry_by" type="hidden" id="entry_by"
																value="<%=mybean.unescapehtml(mybean.entry_by)%>">
												</div>
												<div class="form-element6 ">
														<label >Entry Date:</label>
															<%=mybean.jc_entry_date%>
															<input type="hidden" id="entry_date" name="entry_date"
																value="<%=mybean.jc_entry_date%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.jc_modified_date == null) && !(mybean.jc_modified_date.equals(""))) {
											%>
											<div class="row">
												<div class="form-element6 ">
														<label >Modified By:</label>
															<%=mybean.unescapehtml(mybean.modified_by)%>
															<input type="hidden" id="modified_by" name="modified_by"
																value="<%=mybean.unescapehtml(mybean.modified_by)%>">
												</div>
												<div class="form-element6 ">
														<label >Modified Date:</label>
															<%=mybean.jc_modified_date%>
															<input type="hidden" id="modified_date"
																name="modified_date"
																value="<%=mybean.jc_modified_date%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Add")) {
											%>
											<center>
											<div class="form-element12 form-center">
												<input name="addbutton" id="addbutton" type="button"
													class="btn btn-success"
													onClick="return SubmitFormOnce(document.form1, this);"
													class="button" value="Add Job Card" /> <input
													type="hidden" id="add_button" name="add_button" value="yes" />
													</div>
											</center>

											<%}%><%else if (mybean.status.equals("Update")) {%>
											<center>
											<div class="form-element12">
												<input type="hidden" id="update_button" name="update_button"
													value="yes" /> <input name="updatebutton" id="updatebutton"
													type="submit" class="btn btn-success"
													value="Update Job Card"
													onclick="return SubmitFormOnce(document.form1,this);" />
													 <input name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" onClick="return confirmdelete(this)"
													value="Delete Job Card" />
													</div>
													</div>
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
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../Library/slider.js"></script>
	<script type="text/javascript" src="../Library/sc.js"></script>
	
	<script type="text/javascript">
	
	$(function() {
		// Dialog
		$('#dialog-modal-veh').dialog({
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
											//url: "home.jsp",
											success : function(data) {
												$('#dialog-modal-veh')
														.html(
																'<iframe src="../service/vehicle-list.jsp?group=select_jc_veh" width="100%" height="100%" frameborder=0></iframe>');
											}
										});
								$('#dialog-modal-veh').dialog('open');
								return true;
							});
		});

		function SelectVeh(veh_id, item_id, item_name, veh_chassis_no,
				veh_engine_no, veh_reg_no, customer_id, contact_id,
				customer_name, contact_name) {
			document.getElementById("div_veh_chassis_no").innerHTML = veh_chassis_no;
			document.getElementById("div_veh_engine_no").innerHTML = veh_engine_no;
			document.getElementById("div_veh_reg_no").innerHTML = veh_reg_no;
			document.getElementById("txt_jc_reg_no").value = veh_reg_no;
			//txt_jc_reg_no
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
			$('#dialog-modal-veh').dialog('close');
		}
		

		$(function() {
			var guage = document.getElementById("txt_jc_fuel_guage").value;
			$("#div_fuel_guage").slider({
				range : "min",
				value : guage,
				min : 0,
				max : 100,
				slide : function(event, ui) {
					$("#div_jc_fuel_guage").html(ui.value);
					$("#txt_jc_fuel_guage").val(ui.value);
					// 			 	 SecurityCheck('txt_jc_fuel_guage', document.getElementById("txt_jc_fuel_guage"), 'hint_txt_jc_fuel_guage');
				}
			});
			$("#div_jc_fuel_guage").html($("#div_fuel_guage").slider("value"));
			$("#txt_jc_fuel_guage").val($("#div_fuel_guage").slider("value"));
		});
	</script>
</body>
</HTML>