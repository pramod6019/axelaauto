<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Vehicle_Dash" scope="request" />
<% mybean.doPost(request, response); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">

<!-- tag CSS -->
<link href="../assets/css/bootstrap-tagsinput.css" rel="stylesheet" type="text/css" />
<!-- tag CSS -->
<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-timepicker.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="../assets/css/footable.core.css">
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />

<script language="JavaScript" type="text/javascript">
	function PopulateDriver() {
		var bookingid = document
				.getElementById("dr_vehfollowup_bookingtype_id").value;
		showHint('vehicle-check.jsp?listdriver=yes&booking_id=' + bookingid,
				'HintDriver');
	}

	function checknextfollowuptime() {
		var nextfollowuptime = document.getElementById("dr_insurlostcase1_id").value;
		if (nextfollowuptime == "0") {
			$("#nextfollowuptime").show();
		} else
			(nextfollowuptime != "0")
		{
			$("#nextfollowuptime").hide();
		}
	}
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
		showHintPost(
				'vehicle-dash-check.jsp?display_customer_details=yes&customer_id='
						+ customer_id, customer_id, customer_id,
				'div_customer_details');
	}

	function PopulateModelItem() {
		var item_model_id = document.getElementById("dr_item_model_id").value;
		document.getElementById("hint_dr_item_id").innerHTML = "";
		showHint('vehicle-dash-check.jsp?list_model_item=yes&item_model_id='
				+ item_model_id, 'model-item');
	}

	function LoadVehDash(tab) {
		// 			//////alert("coming....."+tab);
		var veh_id = document.getElementById("txt_veh_id").value;
		// 			//////alert("coming....."+veh_id);
		var customer_id = document.getElementById("txt_customer_id").value;
		// 			////alert("coming....."+customer_id);
		if (tab == '2') {
			if (document.getElementById("tabs-2").innerHTML == '') {
				showHintPost(
						'vehicle-dash-check.jsp?customer_details=yes&customer_id='
								+ customer_id, customer_id, customer_id,
						'tabs-2');
				$(function() {
					// Dialog
					$('#dialog-modal-contact').dialog({
						autoOpen : false,
						width : 900,
						height : 500,
						zIndex : 200,
						modal : true,
						title : "Add New Contact"
					});
					$('#new_contact_link')
							.click(
									function() {
										var customer_id = document
												.getElementById("txt_customer_id").value;
										$
												.ajax({
													success : function(data) {
														$(
																'#dialog-modal-contact')
																.html(
																		'<iframe src="../customer/customer-contact-update.jsp?Add=yes&customer_id='
																				+ customer_id
																				+ '&modal=yes" width="100%" height="100%" frameborder=0></iframe>');
													}
												});
										$('#dialog-modal-contact').dialog(
												'open');
										return true;
									});
				});
			}
		} else if (tab == '3') {
			if (document.getElementById("tabs-3").innerHTML == "") {
				showHintPost(
						'vehicle-dash-check.jsp?ownership_details=yes&veh_id='
								+ veh_id, veh_id, veh_id, 'tabs-3');
			}
		} else if (tab == '9') {
			if (document.getElementById("tabs-9").innerHTML == '') {
				showHintPost( 'vehicle-dash-check.jsp?insurance_details=yes&veh_id=' + veh_id, veh_id, veh_id, 'tabs-9');
			}
		}else if (tab == '10') {
			if (document.getElementById("tabs-10").innerHTML == '') {
				showHintPost('vehicle-dash-insurancefollowup.jsp?veh_id=' + veh_id, veh_id, veh_id, 'tabs-10');
			}
		}  else if (tab == '11') {
			if (document.getElementById("tabs-11").innerHTML == '') {
				showHintPost('vehicle-dash-check.jsp?history=yes&veh_id='
						+ veh_id, veh_id, veh_id, 'tabs-11');
			}
		} else if (tab == '14') {
			if (document.getElementById("tabs-14").innerHTML == '') {
				showHintPost('vehicle-dash-check.jsp?doc_details=yes&veh_id='
						+ veh_id, veh_id, veh_id, 'tabs-14');
			}
		} else if (tab == '13') {
			if (document.getElementById("tabs-13").innerHTML == '') {
				showHintPost('vehicle-dash-check.jsp?ticket_details=yes&veh_id='
						+ veh_id, veh_id, veh_id, 'tabs-13');
			}
		}
	}


	function SecurityCheck(name, obj, hint) {
// 				alert("Securitycheck-------");
		var value;
		var veh_id = GetReplacePost(document.getElementById("veh_id").value);
		var url = "../insurance/vehicle-dash-check.jsp?";
		var dat = document.getElementById("txt_veh_sale_date").value;
		var param;
		var str = "123";
		if (name != "chk_enquiry_avpresent"
				&& name != "chk_enquiry_manager_assist") {
			value = GetReplacePost(obj.value);
		} else {
			if (obj.checked == true) {
				value = "1";
			} else {
				value = "0";
			}
		}

		if (name == "dr_item_model_id") {
			value = GetReplacePost(obj.value);
			var stage_id;
			if (value == 0) {
				stage_id = 1;
			} else {
				stage_id = 2;
			}

			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&veh_id=" + veh_id + "&stage_id=" + stage_id;
		}

		var fromdate = "";
		if (fromdate != "") {
			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&from_date=" + fromdate + "&enquiry_id=" + enquiry_id;
		} else {
			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&veh_id=" + veh_id;
		}
		showHintPost(url + param, str, param, hint);
		setTimeout('RefreshHistory()', 1000);
	}
	//////////////////// eof security check /////////////////////

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
		$
				.ajax({
					success : function(data) {
						$('#dialog-modal-contact')
								.html(
										'<iframe src="../customer/customer-contact-update.jsp?Add=yes&customer_id='
												+ customer_id
												+ '&modal=yes" width="100%" height="100%" frameborder=0></iframe>');
					}
				});

		$('#dialog-modal-contact').dialog('open');
		//return true;
		//});
		//////alert("1");
	}

	function CloseModal(customer_id) {
		showHintPost('vehicle-dash-check.jsp?customer_details=yes&customer_id='
				+ customer_id, customer_id, 'tabs-2');
		$('#dialog-modal-contact').dialog('close');
	}

	function RefreshHistory() {
		var veh_id = document.getElementById("veh_id").value;
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1> Vehicle Dashboard &gt; Vehicle ID:&nbsp;<%=mybean.veh_id%></h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<% if (!mybean.modal.equals("yes")) { %>
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../insurance/index.jsp">Insurance</a> &gt;</li>
						<li><a href="../insurance/vehicle.jsp">Vehicles</a> &gt;</li>
						<li><a href="../insurance/vehicle-list.jsp?all=yes">List Vehicles</a> &gt;</li>
						<li><a href="../insurance/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>">Vehicle ID:&nbsp;<%=mybean.veh_id%></a>&nbsp;</li>
					</ul>
					<% } %>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">

<!-- 			TAGS START -->
						<CENTER>
							<div class="tag_class" id="customer_tagclass">
								<div class="bs-docs-example" id="bs-docs-example">
									<input type="text" class="form-control" id="enquiry_tags" name="enquiry_tags" />
									<a href="#" id="popover" class="btn btn-success btn-md"><span class="fa fa-angle-down"></a>
								</div>
							</div>
							<div class="hint" id="hint_enquiry_tags"> </div>
							
							<div id="popover-head" class="hide">
								<center>Tag List</center>
							</div>
							<div id="popover-content" class="hide">
								<%=mybean.tagcheck.PopulateTagsPopover( mybean.veh_customer_id, mybean.comp_id)%>
							</div>
						</CENTER>
						
						<!-- 			TAGS END -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<input type="hidden" name="txt_veh_id" id="txt_veh_id"
								value="<%=mybean.veh_id%>"> <input type="hidden"
								name="txt_customer_id" id="txt_customer_id"
								value="<%=mybean.veh_customer_id%>">
							<div class="tabbable tabbable-tabdrop">
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tabs-1" data-toggle="tab">Vehicle Info</a></li>
									<li><a href="#tabs-2" data-toggle="tab" onclick="LoadVehDash('2');">Customer</a></li>
									<li><a href="#tabs-3" data-toggle="tab" onclick="LoadVehDash('3');">Ownership</a></li>
									<li><a href="#tabs-9" data-toggle="tab" onclick="LoadVehDash('9');">Insurance</a></li>
<!-- 									<li><a href="#tabs-10" id="tab-10" data-toggle="tab" onclick="LoadVehDash('10');">Insurance Follow-up</a></li> -->
									<li><a href="#tabs-11" data-toggle="tab" onclick="LoadVehDash('11');">History</a></li>
									<li><a href="#tabs-14" data-toggle="tab" onclick="LoadVehDash('14');">Documents</a></li>
									<li><a href="#tabs-13" data-toggle="tab" onclick="LoadVehDash('13');">Tickets</a></li>
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
															<a href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>"><%=mybean.veh_id%></a>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-md-3">Branch:</label>
														<div class="txt-align">
															<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.veh_branch_id%>"><%=mybean.branch_name%></a>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-md-3">Model<font color="#ff0000">*</font>:
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
														<label class="control-label col-md-3">Item<font color="#ff0000">*</font>:
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
															Executive<font color="#ff0000">*</font>:
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
													
													<div class="form-group">
														<label class="control-label col-md-3">Insurance Type: </label>
															<div class="col-md-6">
																<select name="dr_veh_insurtype_id" class="form-control"
																	id="dr_veh_insurtype_id"
																	onchange="SecurityCheck('dr_veh_insurtype_id', this, 'hint_dr_veh_insurtype_id');">
																	<%=mybean.PopulateInsuranceType(mybean.comp_id)%>
																</select>
															<div class="hint" id="hint_dr_veh_insurtype_id"></div>
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
															<!-- 															<table width="100%"> -->
															<!-- 																<tr> -->
															<div class="col-md-4 col-xs-12">
																<select name="dr_title" class="form-control"
																	id="dr_title"
																	onchange="SecurityCheck('dr_title',this,'hint_dr_title')">
																	<%=mybean.PopulateTitle(mybean.comp_id,
					mybean.contact_title_id)%>
																</select> Title
																<div class="hint" id="hint_dr_title"></div>
															</div>
															<div class="col-md-4 col-xs-12">
																<input name="txt_contact_fname" type="text"
																	class="form-control" id="txt_contact_fname"
																	value="<%=mybean.contact_fname%>" size="32"
																	maxlength="255"
																	onchange="SecurityCheck('txt_contact_fname',this,'hint_txt_contact_fname')" />First
																Name
																<div class="hint" id="hint_txt_contact_fname"></div>
															</div>
															<div class="col-md-4 col-xs-12">
																<input name="txt_contact_lname" type="text"
																	class="form-control" id="txt_contact_lname"
																	value="<%=mybean.contact_lname%>" size="32"
																	maxlength="255"
																	onchange="SecurityCheck('txt_contact_lname',this,'hint_txt_contact_lname')" />Last
																Name
																<div class="hint" id="hint_txt_contact_lname"></div>
															</div>
															<!-- 																</tr> -->
															<!-- 															</table> -->
														</div>
													</div>

													<%
														if (!mybean.contact_mobile1.equals("")) {
													%>
													<div class="form-group">
														<label class="control-label col-md-3 ">Mobile 1: </label>
														<fieldset>
															<div class=" txt-align col-md-9">
																<%=mybean.contact_mobile1%><%=mybean.ClickToCall(mybean.contact_mobile1,
						mybean.comp_id)%>
															</div>

															<div class="col-md-2 col-xs-12">
																<select name="dr_new_phonetype_id1"
																	class="form-control " id="dr_new_phonetype_id1"
																	onchange="SecurityCheck('dr_new_phonetype_id1',this,'hint_dr_new_phonetype_id1')">
																	<%=mybean.PopulatePhoneType(mybean.comp_id,
						mybean.contact_mobile1_phonetype_id)%>
																</select>
																<div class="hint" id="hint_dr_new_phonetype_id1"></div>
															</div>
														</fieldset>
													</div>
													<%
														}
													%>
													<%
														if (mybean.contact_mobile1.equals("")) {
													%>
													<div class="form-group">
														<label class="control-label col-sm-2 col-md-3"><div
																id="tabview">Mobile 1 :</div></label>
														<div class="col-md-6 col-sm-11 col-xs-10">
															<input name="txt_contact_mobile1" type="text"
																class="form-control" id="txt_contact_mobile1"
																value="<%=mybean.contact_mobile1%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile1','Mobile 1')"
																onchange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')" />
															<span>(91-9999999999)</span>
														</div>

														<%if (!mybean.contact_mobile1.equals("")) { %>
														<div class="txt-align   col-xs-1" id="picon"><%=mybean
					.ClickToCall(mybean.contact_mobile1, mybean.comp_id)%></div>
					<%}else{	%>
					<div class="txt-align   col-xs-1" id="picon"></div>
					<%} %>
														<div class="col-md-1 col-xs-12" id="p-icon">
															<select name="dr_new_phonetype_id1"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id1"
																onchange="SecurityCheck('dr_new_phonetype_id1',this,'hint_dr_new_phonetype_id1')">
																<%=mybean.PopulatePhoneType(mybean.comp_id,
						mybean.contact_mobile1_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id1"></div>
														</div>
														<div class="hint p-update col-md-2"
															id="hint_txt_contact_mobile1"></div>

													</div>
													<%
														}
													%>
													<%
														if (!mybean.contact_mobile2.equals("")) {
													%>
													<div class="form-group">
														<label class="control-label col-md-3 ">Mobile 2: </label>
														<fieldset>
															<div class=" txt-align col-md-9">
																<%=mybean.contact_mobile2%><%=mybean.ClickToCall(mybean.contact_mobile2,
						mybean.comp_id)%>
															</div>

															<div class="col-md-2 col-xs-12">
																<select name="dr_new_phonetype_id2"
																	class="form-control " id="dr_new_phonetype_id2"
																	onchange="SecurityCheck('dr_new_phonetype_id2',this,'hint_dr_new_phonetype_id2')">
																	<%=mybean.PopulatePhoneType(mybean.comp_id,
						mybean.contact_mobile2_phonetype_id)%>
																</select>
																<div class="hint" id="hint_dr_new_phonetype_id2"></div>
															</div>
														</fieldset>
													</div>
													<%
														}
													%>

													<%
														if (mybean.contact_mobile2.equals("")) {
													%>
													<div class="form-group">
														<label class="control-label col-sm-2 col-md-3"><div
																id="tabview">Mobile 2 :</div></label>
														<div class="col-md-6 col-sm-11 col-xs-10">
															<input name="txt_contact_mobile2" type="text"
																class="form-control" id="txt_contact_mobile2"
																value="<%=mybean.contact_mobile2%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile2','Mobile 2')"
																onchange="SecurityCheck('txt_contact_mobile2',this,'hint_txt_contact_mobile2')" />
															<span>(91-9999999999)</span>
														</div>

														<%if (!mybean.contact_mobile2.equals("")) { %>
														<div class="txt-align   col-xs-1" id="picon"><%=mybean
					.ClickToCall(mybean.contact_mobile2, mybean.comp_id)%></div>
					<%}else{	%>
					<div class="txt-align   col-xs-1" id="picon"></div>
					<%} %>
														<div class="col-md-1 col-xs-12" id="p-icon">
															<select name="dr_new_phonetype_id2"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id2"
																onchange="SecurityCheck('dr_new_phonetype_id2',this,'hint_dr_new_phonetype_id2')">
																<%=mybean.PopulatePhoneType(mybean.comp_id,
						mybean.contact_mobile2_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id2"></div>
														</div>
														<div class="hint p-update col-md-2"
															id="hint_txt_contact_mobile2"></div>

													</div>
													<%
														}
													%>
													<div class="form-group">
														<label class="control-label col-sm-2 col-md-3"><div
																id="tabview">Mobile 3 :</div></label>
														<div class="col-md-6 col-sm-11 col-xs-10">
															<input name="txt_contact_mobile3" type="text"
																class="form-control" id="txt_contact_mobile3"
																value="<%=mybean.contact_mobile3%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile3','Mobile 3')"
																onchange="SecurityCheck('txt_contact_mobile3',this,'hint_txt_contact_mobile3')" />
															<span>(91-9999999999)</span>
														</div>
<%if (!mybean.contact_mobile3.equals("")) { %>
														<div class="txt-align   col-xs-1" id="picon"><%=mybean
					.ClickToCall(mybean.contact_mobile3, mybean.comp_id)%></div>
					<%}else{	%>
					<div class="txt-align   col-xs-1" id="picon"></div>
					<%} %>
					
				
														<div class="col-md-1 col-xs-12" id="p-icon">
															<select name="dr_new_phonetype_id3"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id3"
																onchange="SecurityCheck('dr_new_phonetype_id3',this,'hint_dr_new_phonetype_id3')">
																<%=mybean.PopulatePhoneType(mybean.comp_id,
					mybean.contact_mobile3_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id3"></div>
														</div>
														<div class="hint p-update col-md-2"
															id="hint_txt_contact_mobile3"></div>

													</div>

													<div class="form-group">
														<label class="control-label col-sm-2 col-md-3"><div
																id="tabview">Mobile 4 :</div> </label>
														<div class="col-md-6 col-sm-11 col-xs-10">
															<input name="txt_contact_mobile4" type="text"
																class="form-control" id="txt_contact_mobile4"
																value="<%=mybean.contact_mobile4%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile4','Mobile 4')"
																onchange="SecurityCheck('txt_contact_mobile4',this,'hint_txt_contact_mobile4')" />
															<span>(91-9999999999)</span>
														</div>

														<%if (!mybean.contact_mobile4.equals("")) { %>
														<div class="txt-align   col-xs-1" id="picon"><%=mybean
					.ClickToCall(mybean.contact_mobile4, mybean.comp_id)%></div>
					<%}else{	%>
					<div class="txt-align   col-xs-1" id="picon"></div>
					<%} %>
														<div class="col-md-1 col-xs-12" id="p-icon">
															<select name="dr_new_phonetype_id4"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id4"
																onchange="SecurityCheck('dr_new_phonetype_id4',this,'hint_dr_new_phonetype_id4')">
																<%=mybean.PopulatePhoneType(mybean.comp_id,
					mybean.contact_mobile4_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id4"></div>
														</div>
														<div class="hint p-update col-md-2"
															id="hint_txt_contact_mobile4"></div>
													</div>
													<div class="form-group">
														<label class="control-label col-sm-2 col-md-3"><div
																id="tabview">Mobile 5 :</div></label>
														<div class="col-md-6 col-sm-11 col-xs-10">
															<input name="txt_contact_mobile5" type="text"
																class="form-control" id="txt_contact_mobile5"
																value="<%=mybean.contact_mobile5%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile5','Mobile 5')"
																onchange="SecurityCheck('txt_contact_mobile5',this,'hint_txt_contact_mobile5')" />
															<span>(91-9999999999)</span>
														</div>

														<%if (!mybean.contact_mobile5.equals("")) { %>
														<div class="txt-align   col-xs-1" id="picon"><%=mybean
					.ClickToCall(mybean.contact_mobile5, mybean.comp_id)%></div>
					<%}else{	%>
					<div class="txt-align   col-xs-1" id="picon"></div>
					<%} %>
														<div class="col-md-1 col-xs-12" id="p-icon">
															<select name="dr_new_phonetype_id5"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id5"
																onchange="SecurityCheck('dr_new_phonetype_id5',this,'hint_dr_new_phonetype_id5')">
																<%=mybean.PopulatePhoneType(mybean.comp_id,
					mybean.contact_mobile5_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id5"></div>
														</div>
														<div class="hint p-update col-md-2"
															id="hint_txt_contact_mobile5"></div>
													</div>
													<div class="form-group">
														<label class="control-label col-sm-2 col-md-3"><div
																id="tabview">Mobile 6 :</div> </label>
														<div class="col-md-6 col-sm-11 col-xs-10">
															<input name="txt_contact_mobile6" type="text"
																class="form-control" id="txt_contact_mobile6"
																value="<%=mybean.contact_mobile6%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile6','Mobile 6')"
																onchange="SecurityCheck('txt_contact_mobile6',this,'hint_txt_contact_mobile6')" />
															<span>(91-9999999999)</span>
														</div>

														<%if (!mybean.contact_mobile6.equals("")) { %>
														<div class="txt-align   col-xs-1" id="picon"><%=mybean
					.ClickToCall(mybean.contact_mobile6, mybean.comp_id)%></div>
					<%}else{	%>
					<div class="txt-align   col-xs-1" id="picon"></div>
					<%} %>
														<div class="col-md-1 col-xs-12" id="p-icon">
															<select name="dr_new_phonetype_id6"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id6"
																onchange="SecurityCheck('dr_new_phonetype_id6',this,'hint_dr_new_phonetype_id6')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile6_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id6"></div>
														</div>
														<div class="hint p-update col-md-2"
															id="hint_txt_contact_mobile6"></div>

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
														<label class="control-label col-md-3 col-xs-4">Entry
															By: </label>
														<div class="txt-align col-md-4 col-xs-6" style="top: 0px">
															<%=mybean.veh_entry_by%>
															<input type="hidden" name="entry_by"
																value="<%=mybean.veh_entry_by%>">
														</div>
													</div>
													
													<div class="form-group">
														<label class="control-label col-md-3 col-xs-4">Entry
															Date: </label>
														<div class="txt-align col-md-4 col-xs-6" style="top: 0px">
															<%=mybean.veh_entry_date%>
															<input type="hidden" name="entry_by"
																value="<%=mybean.veh_entry_date%>">
														</div>
													</div>

													<%
														if (mybean.veh_modified_by != null
																&& !mybean.veh_modified_by.equals("")) {
													%>

													<div class="form-group">
														<label class="control-label col-md-3 col-xs-4">Modified
															By:</label>
														<div class="txt-align col-md-4 col-xs-6" style="top: 0px">
															<%=mybean.veh_modified_by%>
															<input type="hidden" name="modified_by"
																value="<%=mybean.veh_modified_by%>">
														</div>
													</div>

													<div class="form-group">
														<label class="control-label col-md-3 col-xs-4">Modified
															Date:</label>
														<div class="txt-align col-md-4 col-xs-6" style="top: 0px">
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
										<form class="form-horizontal" name="Frmcontact" method="post"
											action="vehicle-dash.jsp?veh_id=<%=mybean.veh_id%>#tabs-2">


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
														<div class="col-md-6 col-xs-12">
															<!-- 															<table width="100%"> -->
															<!-- 																<tr> -->
															<div class="col-md-4">
																<select name="dr_new_contact_title_id"
																	class="form-control" id="dr_new_contact_title_id">
																	<%=mybean.PopulateTitle(mybean.comp_id, mybean.new_contact_title_id)%>
																</select> Title
															</div>
															<div class="col-md-4">
																<input name="txt_new_contact_fname" type="text"
																	class="form-control" id="txt_new_contact_fname"
																	value="<%=mybean.new_contact_fname%>" size="32"
																	maxlength="255" />First Name
															</div>
															<div class="col-md-4 ">
																<input name="txt_new_contact_lname" type="text"
																	class="form-control" id="txt_new_contact_lname"
																	value="<%=mybean.new_contact_lname%>" size="32"
																	maxlength="255" />Last Name
															</div>
															<!-- 																</tr> -->
															<!-- 															</table> -->
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
														<div class="col-md-1 col-xs-12" style='margin:7px;'>
															<select name="dr_new_phonetype_id1"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id1"
																onchange="SecurityCheck('dr_new_phonetype_id1',this,'hint_dr_new_phonetype_id1')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile1_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id1"></div>
														</div>
														<div class="col-md-2"
															id="hint_txt_contact_mobile1"></div>
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
														<div class="col-md-1 col-xs-12" style='margin:7px;'>
															<select name="dr_new_phonetype_id2"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id2"
																onchange="SecurityCheck('dr_new_phonetype_id1',this,'hint_dr_new_phonetype_id2')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile2_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id2"></div>
														</div>
														<div class="col-md-2"
															id="hint_txt_contact_mobile1"></div>
													</div>
													<div class="form-group">
														<label class="control-label col-md-3">Mobile 3: </label>
														<div class="col-md-6 " style="top: 8px">
															<input name="txt_new_contact_mobile3" type="text"
																class="form-control" id="txt_new_contact_mobile3"
																value="<%=mybean.new_contact_mobile3%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile3','Mobile 3')" />
															(91-9999999999)
														</div>
														<div class="col-md-1 col-xs-12" style='margin:7px;'>
															<select name="dr_new_phonetype_id3"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id3"
																onchange="SecurityCheck('dr_new_phonetype_id3',this,'hint_dr_new_phonetype_id3')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile3_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id3"></div>
														</div>
														<div class="col-md-2"
															id="hint_txt_contact_mobile3"></div>
													</div>
													
													<div class="form-group">
														<label class="control-label col-md-3">Mobile 4: </label>
														<div class="col-md-6 " style="top: 8px">
															<input name="txt_new_contact_mobile4" type="text"
																class="form-control" id="txt_new_contact_mobile4"
																value="<%=mybean.new_contact_mobile4%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile4','Mobile 4')" />
															(91-9999999999)
														</div>
														<div class="col-md-1 col-xs-12" style='margin:7px;'>
															<select name="dr_new_phonetype_id4"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id4"
																onchange="SecurityCheck('dr_new_phonetype_id4',this,'hint_dr_new_phonetype_id4')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile4_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id4"></div>
														</div>
														<div class="col-md-2"
															id="hint_txt_contact_mobile4"></div>
													</div>
													
													<div class="form-group">
														<label class="control-label col-md-3">Mobile 5: </label>
														<div class="col-md-6 " style="top: 8px">
															<input name="txt_new_contact_mobile5" type="text"
																class="form-control" id="txt_new_contact_mobile5"
																value="<%=mybean.new_contact_mobile5%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile5','Mobile 5')" />
															(91-9999999999)
														</div>
														<div class="col-md-1 col-xs-12" style='margin:7px;'>
															<select name="dr_new_phonetype_id5"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id5"
																onchange="SecurityCheck('dr_new_phonetype_id5',this,'hint_dr_new_phonetype_id5')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile5_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id5"></div>
														</div>
														<div class="col-md-2"
															id="hint_txt_contact_mobile5"></div>
													</div>
													
													<div class="form-group">
														<label class="control-label col-md-3">Mobile 6: </label>
														<div class="col-md-6 " style="top: 8px">
															<input name="txt_new_contact_mobile6" type="text"
																class="form-control" id="txt_new_contact_mobile6"
																value="<%=mybean.new_contact_mobile6%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile6','Mobile 6')" />
															(91-9999999999)
														</div>
														<div class="col-md-1 col-xs-12" style='margin:7px;'>
															<select name="dr_new_phonetype_id6"
																class="form-control p-dropdown"
																id="dr_new_phonetype_id6"
																onchange="SecurityCheck('dr_new_phonetype_id6',this,'hint_dr_new_phonetype_id6')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile6_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id6"></div>
														</div>
														<div class="col-md-2"
															id="hint_txt_contact_mobile6"></div>
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

<!-- 									<div class="tab-pane" id="tabs-10"></div> -->
									<div class="tab-pane" id="tabs-3"></div>
									<div class="tab-pane" id="tabs-5"></div>
									<div class="tab-pane" id="tabs-9"></div>
									<div class="tab-pane" id="tabs-11"></div>
									<div class="tab-pane" id="tabs-14"></div>
									<div class="tab-pane" id="tabs-13"></div>
								</div>
							</div>

						</div>

					</div>
					<!-- 				page-content-inner END ----->
				</div>
			</div>
		</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script src="../assets/js/bootstrap-datepicker.js" type="text/javascript"></script>
	<script src="../assets/js/components-date-time-pickers.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/dynacheck-post.js"></script>
	<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script type="text/javascript" src="../Library/insurance-followup.js"></script>
	<script src="../assets/js/footable.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
	<!-- For Insurance followup tab -->
	<script type="text/javascript">
		function SecurityCheck2(name, obj, hint) {
			var value;
			var veh_id = GetReplacePost(document.getElementById("veh_id").value);
			var url = "../insurance/vehicle-dash-insurance-followup-check.jsp?";
			var param;
			var str = "123";
			value = GetReplacePost(obj.value);
			param = "name=" + name + "&value=" + value + "&veh_id=" + veh_id;
			showHintPost(url + param, str, param, hint);
		}
	</script>
	
	<script type="text/javascript">
		function expiryDate() {
			$('#txt_veh_insur_policyexpirydate').datepicker().on(
					'changeDate',
					function() {SecurityCheck2('txt_veh_insur_policyexpirydate', this,'hint_txt_veh_insur_policyexpirydate');
					});
			}
	</script>

	<script type="text/javascript">
		function expirytime() {
			$('#txt_veh_insur_policyexpirydate').datepicker('show');
		}
		
		function callme() {
			$('#txt_insurfollowup_time').datetimepicker('show');
		}
	</script>
	
	
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>

	<script>
		$(document).ready(
				function() {

					$('#txt_veh_sale_date').datepicker().on(
							'changeDate',
							function() {
								SecurityCheck('txt_veh_sale_date', this,
										'hint_txt_veh_sale_date');
							});

					$('#txt_veh_lastservice').datepicker().on(
							'changeDate',
							function() {
								SecurityCheck('txt_veh_lastservice', this,
										'hint_txt_veh_lastservice');
							});

					$('#txt_veh_service_duedate').datepicker().on(
							'changeDate',
							function() {
								SecurityCheck('txt_veh_service_duedate', this,
										'hint_txt_veh_service_duedate');
							});

					$('#txt_veh_warranty_expirydate').datepicker().on(
							'changeDate',
							function() {
								SecurityCheck('txt_veh_warranty_expirydate',
										this, 'hint_veh_warranty_expirydate')
							});

					$('#txt_veh_insur_date').datepicker().on(
							'changeDate',
							function() {
								SecurityCheck('txt_veh_insur_date', this, 'hint_txt_veh_insur_date');
							});

					$('#txt_veh_renewal_date').datepicker().on(
							'changeDate',
							function() {
								SecurityCheck('txt_veh_renewal_date', this, 'hint_txt_veh_renewal_date');
							});

				});
	</script>

	<!-- START OF TAGS CONFIGURATION -->
	<script src="../assets/js/bootstrap-tagsinput.js" type="text/javascript"></script>
	<script type="text/javascript">

// 	THIS IS TO POPULATE VALUE IN TAG-CONTAINER AT THE TIME OF PAGE LOAD
	<%=mybean.tagcheck.PopulateTags(mybean.veh_customer_id, mybean.comp_id)%>
	
//	THIS IS TO ADD TAGS IN TAG-CONTAINER ON CLICK FROM POPOVER
	<%=mybean.tagcheck.PopulateTagsJS( mybean.veh_customer_id, mybean.comp_id)%>
	
	function deleteTag(){
		$('#customer_tagclass > > input').tagsinput('remove', { 'value':  0, 'text': 'No Tag Selected' , 'continent': '#ff0000' });
	}
	
	function addNoTag(){
		$('#customer_tagclass > > input').tagsinput('add', { 'value':  0, 'text': 'No Tag Selected' , 'continent': '#ff0000' });
	}
	
	$(function() {
			$("#enquiry_tags").on('itemRemoved',function(){
					
					var url = "../customer/customer-tags-check.jsp?";
					
					var param = "update=yes&tags="+ $("#enquiry_tags").val()+"&id="+<%=mybean.veh_customer_id%>+"&vehicle=yes&enquiry_id="+<%=mybean.veh_enquiry_id%>+"&veh_id="+<%=mybean.veh_id%>;
					var hint = "hint_enquiry_tags";
					
					var param2 = "tags_content=yes&id=" + <%=mybean.veh_customer_id%>;
					var hint2 = "popover-content";
					
					setTimeout('showHint("'+ url + param2+'", "'+hint2+'")', 100);
					
					setTimeout('if($("#enquiry_tags").val()==""){ addNoTag(); }', 150);
					
					showHint(url + param, hint);
					
			});
		}); 
	    
		function tagcall(idname){
			
			var url = "../customer/customer-tags-check.jsp?";
			
			var param1 = "add=yes&name=enquiry&tags="+idname +"&id="+<%=mybean.veh_customer_id%>+"&vehicle=yes&enquiry_id="+<%=mybean.veh_enquiry_id%>+"&veh_id="+<%=mybean.veh_id%>;
			var hint1 = "hint_enquiry_tags";
			
			var param2 = "tags_content=yes&id=" + <%=mybean.veh_customer_id%>;
			var hint2 = "popover-content";
			
			setTimeout('showHint("'+url + param2+'", "'+hint2+'")', 100);
			
			setTimeout('addTag('+idname+')', 150);
			
			setTimeout('showHint("'+url + param1+'", "'+hint1+'")', 50);
			
			deleteTag();
			
		}
		
		//this is provide property to the TAG POPOVER 
		$('#popover').popover({ 
		    html : true,
		    title: function() {
		      return $("#popover-head").html();
		    },
		    trigger:'onclick',
		    content: function() {
		      return $("#popover-content").html();
		    }
		});
		
</script>


<!-- END OF TAGS CONFIGURATION -->
<!-- // -->
</body>
</html>
