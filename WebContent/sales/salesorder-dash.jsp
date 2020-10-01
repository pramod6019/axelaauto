<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.SalesOrder_Dash"
	scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp" %>
<!-- tag CSS -->
<link href="../assets/css/bootstrap-tagsinput.css" rel="stylesheet" type="text/css" />
<!-- tag CSS -->
<!-- .. -->
<script>
		function LoadSODash(tab) {
			var so_id = document.getElementById("txt_so_id").value;
			var customer_id = document.getElementById("txt_customer_id").value;
			if (tab == '7') {
				showHint('salesorder-dash-check.jsp?doc_details=yes&so_id=' + so_id, 'tabs-3');
			} else if (tab == '2') {
				if (document.getElementById("tabs-2").innerHTML == "") {
					showHint( 'salesorder-dash-check.jsp?customer_details=yes&customer_id=' + customer_id + '&so_id=' + so_id, 'tabs-2');
				}
			} else if (tab == '3') {
				showHint('salesorder-dash-check.jsp?doc_details=yes&so_id=' + so_id, 'tabs-3');
			} else if (tab == '4') {
				if (document.getElementById("tabs-4").innerHTML == '') {
					showHint( 'salesorder-dash-check.jsp?invoice_details=yes&so_id=' + so_id,'tabs-4');
				}
			} else if (tab == '5') {
				if (document.getElementById("tabs-5").innerHTML == '') {
					showHint( 'salesorder-dash-check.jsp?receipt_details=yes&so_id=' + so_id, 'tabs-5');
				}
			} else if (tab == '6') {
				if (document.getElementById("tabs-6").innerHTML == '') { 
					showHint('salesorder-dash-check.jsp?history=yes&so_id=' + so_id, 'tabs-6');
				}
			} else if (tab == '10') {
				if (document.getElementById("tabs-10").innerHTML == '') {
					showHint('salesorder-dash-check.jsp?insurance=yes&so_id=' + so_id, 'tabs-10');
				}
			} else if (tab == '11') {
				if (document.getElementById("tabs-11").innerHTML == '') {
					showHint('salesorder-dash-check.jsp?profitability=yes&so_id=' + so_id, 'tabs-11');
				}
			}
		}
// 		$(function() {
// 			// Dialog
// 			$('#dialog-modal').dialog({
// 				autoOpen : false,
// 				width : 900,
// 				height : 500,
// 				zIndex : 200,
// 				modal : true,
// 				title : "Select Contact Company for Corporate Discount"
// 			});
// 			$('#contact_company_link') .click( function() {
// 				$ .ajax({
// 					//url: "home.jsp",
// 					success : function(data) {
// 						$('#dialog-modal')
// 								.html(
// 										'<iframe src="../customer/managecontactcompany.jsp?group=select_contact_company" width="100%" height="100%" align="right" frameborder=0 onload="window.parent.scrollTo(0,0)"></iframe>');
// 					}
// 				});
// 				$('#dialog-modal').dialog('open');
// 				return true;
// 			});
// 		});
	</script>

<script language="JavaScript" type="text/javascript">
		function FinStatusCheck(name, obj, hint) {
			var error = 0;
			var so_finstatus_id = document.getElementById("dr_so_finstatus_id").value;
			var so_finstatus_desc = document.getElementById("txt_so_finstatus_desc").value;
			var so_finstatus_loan_amt = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_loan_amt").value));
			var so_finstatus_emi_value = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_emi_value").value));
			var so_finstatus_advance = document.getElementById("dr_so_finstatus_advance").value;
			var so_finstatus_disbursed_date = document.getElementById("txt_so_finstatus_disbursed_date").value;
			var so_finstatus_disbursed_amt = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_disbursed_amt").value));
			var so_finstatus_tenure = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_tenure").value));
			var so_finstatus_bank_id = document.getElementById("dr_so_finstatus_bank_id").value;
			var so_finstatus_scheme = document.getElementById("txt_so_finstatus_scheme").value;
			var so_finstatus_process_fee = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_process_fee").value));
			var so_finstatus_subvention = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_subvention").value));
			var so_finstatus_gross_payout = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_gross_payout").value));
			var so_finstatus_income_on_payment = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_income_on_payment").value));
			var so_finstatus_bank_rack_rate = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_bank_rack_rate").value));
			var so_finstatus_customer_roi = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_customer_roi").value));
			var so_finstatus_occupation = document.getElementById("txt_so_finstatus_occupation").value;
			var so_finstatus_industry = document.getElementById("txt_so_finstatus_industry").value;
			var so_finstatus_income_asperdoc = parseInt(CheckNumeric(document.getElementById("txt_so_finstatus_income_asperdoc").value));
			var so_finstatus_dob = document.getElementById("txt_so_finstatus_dob").value;
			var so_finstatus_sex = document.getElementById("dr_so_finstatus_sex").value;
			if (so_finstatus_id == '5' && name == 'dr_so_finstatus_id') {
				if (so_finstatus_desc == '') {
					document.getElementById("hint_txt_so_finstatus_desc").innerHTML = "<font color='red'>Enter Description!</font>";
					error = 1;
				} else {
					document.getElementById("hint_txt_so_finstatus_desc").innerHTML = "";
				}
				if (so_finstatus_loan_amt == '0') {
					document.getElementById("hint_txt_so_finstatus_loan_amt").innerHTML = "<font color='red'>Enter Loan Amount!</font>";
					error = 1;
				} else {
					document.getElementById("hint_txt_so_finstatus_loan_amt").innerHTML = "";
				}
				if (so_finstatus_emi_value == '0') {
					document.getElementById("hint_txt_so_finstatus_emi_value").innerHTML = "<font color='red'>Enter EMI/Value!</font>";
					error = 1;
				} else {
					document.getElementById("hint_txt_so_finstatus_emi_value").innerHTML = "";
				}
				if (so_finstatus_advance == '0') {
					document.getElementById("hint_dr_so_finstatus_advance").innerHTML = "<font color='red'>Enter Advance/Arrears!</font>";
					error = 1;
				} else {
					document.getElementById("hint_dr_so_finstatus_advance").innerHTML = "";
				}
				if (so_finstatus_disbursed_date == '') {
					document
							.getElementById("hint_txt_so_finstatus_disbursed_date").innerHTML = "<font color='red'>Enter Disbursed Date!</font>";
					error = 1;
				} else {
					document .getElementById("hint_txt_so_finstatus_disbursed_date").innerHTML = "";
				}
				if (so_finstatus_disbursed_amt == '0') {
					document .getElementById("hint_txt_so_finstatus_disbursed_amt").innerHTML = "<font color='red'>Enter Disbursed Amount!</font>";
					error = 1;
				} else {
					document .getElementById("hint_txt_so_finstatus_disbursed_amt").innerHTML = "";
				}
				if (so_finstatus_tenure == '0') {
					document.getElementById("hint_txt_so_finstatus_tenure").innerHTML = "<font color='red'>Enter Tenure!</font>";
					error = 1;
				} else {
					document.getElementById("hint_txt_so_finstatus_tenure").innerHTML = "";
				}
				if (so_finstatus_bank_id == '0') {
					document.getElementById("hint_dr_so_finstatus_bank_id").innerHTML = "<font color='red'>Select Bank!</font>";
					error = 1;
				} else {
					document.getElementById("hint_dr_so_finstatus_bank_id").innerHTML = "";
				}
				if (so_finstatus_scheme == '') {
					document.getElementById("hint_txt_so_finstatus_scheme").innerHTML = "<font color='red'>Enter Scheme!</font>";
					error = 1;
				} else {
					document.getElementById("hint_txt_so_finstatus_scheme").innerHTML = "";
				}
				if (so_finstatus_process_fee == '0') {
					document .getElementById("hint_txt_so_finstatus_process_fee").innerHTML = "<font color='red'>Enter Processing Fees!</font>";
					error = 1;
				} else {
					document .getElementById("hint_txt_so_finstatus_process_fee").innerHTML = "";
				}
				if (so_finstatus_subvention == '0') {
					document.getElementById("hint_txt_so_finstatus_subvention").innerHTML = "<font color='red'>Enter Subvention Amount!</font>";
					error = 1;
				} else {
					document.getElementById("hint_txt_so_finstatus_subvention").innerHTML = "";
				}
				if (so_finstatus_gross_payout == '0') {
					document .getElementById("hint_txt_so_finstatus_gross_payout").innerHTML = "<font color='red'>Enter Gross Payout!</font>";
					error = 1;
				} else {
					document .getElementById("hint_txt_so_finstatus_gross_payout").innerHTML = "";
				}
				if (so_finstatus_income_on_payment == '0') {
					document .getElementById("hint_txt_so_finstatus_income_on_payment").innerHTML = "<font color='red'>Enter Income On Payment!</font>";
					error = 1;
				} else {
					document .getElementById("hint_txt_so_finstatus_income_on_payment").innerHTML = "";
				}
				if (so_finstatus_bank_rack_rate == '0') {
					document .getElementById("hint_txt_so_finstatus_bank_rack_rate").innerHTML = "<font color='red'>Enter Bank Rack Rate!</font>";
					error = 1;
				} else {
					document .getElementById("hint_txt_so_finstatus_bank_rack_rate").innerHTML = "";
				}
				if (so_finstatus_customer_roi == '0') {
					document .getElementById("hint_txt_so_finstatus_customer_roi").innerHTML = "<font color='red'>Enter Customer ROI!</font>";
					error = 1;
				} else {
					document .getElementById("hint_txt_so_finstatus_customer_roi").innerHTML = "";
				}
				if (so_finstatus_occupation == '') {
					document.getElementById("hint_txt_so_finstatus_occupation").innerHTML = "<font color='red'>Enter Occupation!</font>";
					error = 1;
				} else {
					document.getElementById("hint_txt_so_finstatus_occupation").innerHTML = "";
				}
				if (so_finstatus_industry == '') {
					document.getElementById("hint_txt_so_finstatus_industry").innerHTML = "<font color='red'>Enter Industry!</font>";
					error = 1;
				} else {
					document.getElementById("hint_txt_so_finstatus_industry").innerHTML = "";
				}
				if (so_finstatus_income_asperdoc == '0') {
					document .getElementById("hint_txt_so_finstatus_income_asperdoc").innerHTML = "<font color='red'>Enter Income as per Doc!</font>";
					error = 1;
				} else {
					document .getElementById("hint_txt_so_finstatus_income_asperdoc").innerHTML = "";
				}
				if (so_finstatus_dob == '') {
					document.getElementById("hint_txt_so_finstatus_dob").innerHTML = "<font color='red'>Enter Date of Birth!</font>";
					error = 1;
				} else {
					document.getElementById("hint_txt_so_finstatus_dob").innerHTML = "";
				}
				if (so_finstatus_sex == '0') {
					document.getElementById("hint_dr_so_finstatus_sex").innerHTML = "<font color='red'>Select Gender!</font>";
					error = 1;
				} else {
					document.getElementById("hint_dr_so_finstatus_sex").innerHTML = "";
				}
				
				if(error == '0'){
					var hint = "hint_dr_so_finstatus_id";
					SecurityCheck(name, obj, hint);
				}else{
					var a = <%=mybean.so_finstatus_id%>;
					document.getElementById("dr_so_finstatus_id").value = '<%=mybean.so_finstatus_id%>';
					document.getElementById("hint_dr_so_finstatus_id").value = '';
				}

			// 				if (error == '0') {
			// 					var so_id = document.getElementById("txt_so_id").value
			// 					var url = "../sales/salesorder-dash-check.jsp?";
			// 					var param = "so_id=" + so_id
			// 							+ "&so_finstatus=yes&so_finstatus_desc="
			// 							+ so_finstatus_desc + "&so_finstatus_loan_amt="
			// 							+ so_finstatus_loan_amt
			// 							+ "&so_finstatus_emi_value="
			// 							+ so_finstatus_emi_value + "&so_finstatus_advance="
			// 							+ so_finstatus_advance
			// 							+ "&so_finstatus_disbursed_date="
			// 							+ so_finstatus_disbursed_date
			// 							+ "&so_finstatus_disbursed_amt="
			// 							+ so_finstatus_disbursed_amt
			// 							+ "&so_finstatus_tenure=" + so_finstatus_tenure
			// 							+ "&so_finstatus_bank_id=" + so_finstatus_bank_id
			// 							+ "&so_finstatus_scheme=" + so_finstatus_scheme
			// 							+ "&so_finstatus_process_fee="
			// 							+ so_finstatus_process_fee
			// 							+ "&so_finstatus_subvention="
			// 							+ so_finstatus_subvention
			// 							+ "&so_finstatus_gross_payout="
			// 							+ so_finstatus_gross_payout
			// 							+ "&so_finstatus_income_on_payment="
			// 							+ so_finstatus_income_on_payment
			// 							+ "&so_finstatus_bank_rack_rate="
			// 							+ so_finstatus_bank_rack_rate
			// 							+ "&so_finstatus_customer_roi="
			// 							+ so_finstatus_customer_roi
			// 							+ "&so_finstatus_occupation="
			// 							+ so_finstatus_occupation
			// 							+ "&so_finstatus_industry=" + so_finstatus_industry
			// 							+ "&so_finstatus_income_asperdoc="
			// 							+ so_finstatus_income_asperdoc
			// 							+ "&so_finstatus_dob=" + so_finstatus_dob
			// 							+ "&so_finstatus_sex=" + so_finstatus_sex;
			// 					var str = "123";
			// 					var hint = "hint_dr_so_finstatus_id";
			// 					showHint(url + param, str, param, hint);
			// 				}
		} else {
			document.getElementById("hint_txt_so_finstatus_desc").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_loan_amt").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_emi_value").innerHTML = "";
			document.getElementById("hint_dr_so_finstatus_advance").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_disbursed_date").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_disbursed_amt").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_tenure").innerHTML = "";
			document.getElementById("hint_dr_so_finstatus_bank_id").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_scheme").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_process_fee").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_subvention").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_gross_payout").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_income_on_payment").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_bank_rack_rate").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_customer_roi").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_occupation").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_industry").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_income_asperdoc").innerHTML = "";
			document.getElementById("hint_txt_so_finstatus_dob").innerHTML = "";
			document.getElementById("hint_dr_so_finstatus_sex").innerHTML = "";
			SecurityCheck(name, obj, hint);
		}
	}

	function CheckActive(param, obj) {
		var active_value = document.getElementById("chk_so_active");
		var cancel_date = document.getElementById("txt_so_cancel_date").value;
		var cancel_reason = document.getElementById("dr_cancel_reason").value;
		var emp_close_enquiry = document.getElementById("txt_emp_close_enquiry").value;
		if (active_value.checked == false && emp_close_enquiry == 0 && param == 'active') {
			alert("Error!\nInactivating Sales Order Permission Denied!");
			document.getElementById("chk_so_active").checked = "checked";
		} else if (active_value.checked == false && cancel_date == '' && param == 'active') {
			alert("Error!\nEnter Cancel Date for Inactivating Sales Order!");
			document.getElementById("chk_so_active").checked = "checked";
		} else if (active_value.checked == false && cancel_reason == '0' && param == 'active') {
			alert("Error!\nSelect Cancel Reason for Inactivating Sales Order!");
			document.getElementById("chk_so_active").checked = "checked";
		} else {
			if (param == 'active') {
				SecurityCheck('chk_so_active', obj, 'hint_chk_so_active');
			} else if (param == 'cancel_date') {
				SecurityCheck('txt_so_cancel_date', obj, 'hint_txt_so_cancel_date');
			} else if (param == 'cancel_reason') {
				SecurityCheck('dr_cancel_reason', obj, 'hint_dr_cancel_reason');
			}
		}
	}

	function CheckNumeric(num) {
		if (isNaN(num) || num == '' || num == null) {
			num = 0;
		}
		return num;
	}

	function PopulateCheck(name, obj, hint) {
		var value;
		var so_id = document.getElementById("txt_so_id").value;
		var url = "../sales/salesorder-dash-check.jsp?";
		var param = "";
		var str = "123";
		value = GetReplace(obj.value);
		var stage_id;
		if (value == 0) {
			stage_id = 1;
		} else {
			stage_id = 2;
		}
		param = "name=" + name + "&value=" + value + "&so_id=" + so_id
				+ "&stage_id=" + stage_id;
		showHint(url + param,hint);
		setTimeout('RefreshHistory()', 1000);
	}

	function SecurityCheck(name, obj, hint) {
		var value;
		var so_id = document.getElementById("txt_so_id").value;
		var item_id = document.getElementById("txt_item_id").value;
		var url = "../sales/salesorder-dash-check.jsp?";
		var param = "";
		var str = "123";
		if (name != "chk_so_open" && name != "chk_so_critical"
				&& name != "chk_so_active" && name != "chk_so_reg_hsrp"
				&& name != "chk_so_exchange" && name != "chk_so_authorize_pdi"
				&& name != "chk_so_authorize_accessories" && name != "chk_so_authorize_accounts"
				&& name != "chk_so_authorize_insurance" && name != "chk_so_authorize_registration"
				&& name != "chk_so_authorize_deliverycoordinator") {
			value = GetReplace(obj.value);
			/*if(value == "" && name == "txt_so_booking_amount")
			{
				document.getElementById("txt_so_booking_amount").value = "0";
			}*/
		} else {
			var date = new Date();
			var day = date.getDate();
			var year = date.getFullYear();
			var month = date.getMonth() + 1;
			if (obj.checked == true) {
				value = "1";
			} else {
				value = "0";
			}
			if (name == "chk_so_active") {
				if (value == '0') {
					var cancel_date = document
							.getElementById("txt_so_cancel_date").value;
					var cancel_reason = document
							.getElementById("dr_cancel_reason").value;
					param = "name=" + name + "&value=" + value
							+ "&so_cancel_date=" + cancel_date
							+ "&so_cancel_reason=" + cancel_reason + "&so_id="
							+ so_id + "&item_id=" + item_id;
				} else if (value == '1') {
					document.getElementById("txt_so_cancel_date").value = '';
					document.getElementById("dr_cancel_reason").value = '0';
					param = "name=" + name + "&value=" + value + "&so_id="
							+ so_id + "&item_id=" + item_id;
				}
			}
		}
		if (param == '') {
			param = "name=" + name + "&value=" + value + "&so_id=" + so_id
					+ "&item_id=" + item_id;
		}
		showHint(url + param, hint);
		setTimeout('RefreshHistory()', 1000);
	}

	//////////////////// eof security check /////////////////////
	function RefreshHistory() {
		var so_id = document.getElementById("txt_so_id").value;
	}
</script>
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
							Sales Order Dashboard &gt; SO ID<b>:</b>
							<%=mybean.so_id%></h1>

					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<%
						//if (!mybean.modal.equals("yes")) {
					%>
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="../sales/veh-salesorder.jsp">Sales Order</a>
							&gt;</li>
						<li><a
							href="../sales/salesorder-dash.jsp?so_id=<%=mybean.so_id%>">SO
								ID<b>:</b> <%=mybean.so_id%></a></li>

					</ul>
					<%
						//}
					%>
					<!-- 			TAGS START -->
						<CENTER>
							<div class="tag_class" id="customer_tagclass">
								<div class="bs-docs-example" id="bs-docs-example">
									<input type="text" class="form-control" id="enquiry_tags" name="enquiry_tags" />
									<a href="#" id="popover" class="btn btn-success"><span class="fa fa-angle-down"></span></a>
								</div>
							</div>
							<div class="hint" id="hint_enquiry_tags"> </div>
							
							
							
							<div id="popover-head" class="hide">
								<center>Tag List</center>
							</div>
							<div id="popover-content" class="hide">
								<%=mybean.tagcheck.PopulateTagsPopover( mybean.so_customer_id, mybean.comp_id)%>
							</div>
						</CENTER>
						
					<!-- 			TAGS END -->
					
					
						<div class="tab-pane" id="">

							<input type="hidden" name="txt_customer_id" id="txt_customer_id"
								value="<%=mybean.so_customer_id%>" /> <input type="hidden"
								name="txt_so_id" id="txt_so_id" value="<%=mybean.so_id%>" />


							<div class="tabbable tabbable-tabdrop">
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tabs-1" data-toggle="tab">Sales Order Details</a></li>
									<li><a href="#tabs-9" data-toggle="tab">Accounts</a></li>
									<li><a href="#tabs-8" data-toggle="tab">Finance Status</a></li>
									<li><a href="#tabs-7" data-toggle="tab">Registration</a></li>
									<li onclick="LoadSODash('10');"><a href="#tabs-10" data-toggle="tab">Insurance</a></li>
									<li onclick="LoadSODash('2');"><a href="#tabs-2" data-toggle="tab">Customer</a></li>
									<li onclick="LoadSODash('3');"><a href="#tabs-3" data-toggle="tab">Documents</a></li>
									<li onclick="LoadSODash('4');"><a href="#tabs-4" data-toggle="tab">Invoices</a></li>
									<li onclick="LoadSODash('5');"><a href="#tabs-5" data-toggle="tab">Receipts</a></li>
									<li onclick="LoadSODash('11');"><a href="#tabs-11" data-toggle="tab">Profitability</a></li>
									<li onclick="LoadSODash('6');"><a href="#tabs-6" data-toggle="tab">History</a></li>

								</ul>
								<div class="tab-content">
									<div class="tab-pane active" id="tabs-1">
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Sales Order Details</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="container-fluid">
													<!-- START PORTLET BODY -->
													<form name="form1" id="form1" method="post" class="form-horizontal">
														<input name="txt_emp_close_enquiry" type="hidden"
															id="txt_emp_close_enquiry"
															value="<%=mybean.emp_close_enquiry%>" /> <input
															name="txt_emp_role_id" type="hidden" id="txt_emp_role_id"
															value="<%=mybean.emp_role_id%>" />

														<div class="form-element6">
															<label>Customer:&nbsp;&nbsp;</label>
																<%=mybean.link_customer_name%>
														</div>
														<div class="form-element6">
															<label>Contact:&nbsp;&nbsp;</label>
																<%=mybean.link_contact_name%>
														</div>
														<div class="form-element6">
															<label>SO ID:&nbsp;&nbsp;</label>
																<%=mybean.so_id%>
																<input name="txt_item_id" type="hidden" id="txt_item_id"
																	value="<%=mybean.item_id%>" />
														</div>
														<div class="form-element6">
															<label>SO Date:&nbsp;&nbsp;</label>
																<%=mybean.sodate%>
														</div>
														<div class="form-element6">
															<label>Enquiry ID:&nbsp;&nbsp;</label>
																<%=mybean.enquiry_link%>
																<input type="hidden" id="so_id" name="so_id"
																	value="<%=mybean.so_id%>" /> <input
																	name="txt_enquiry_enquirytype_id" type="hidden"
																	id="txt_enquiry_enquirytype_id"
																	value="<%=mybean.enquiry_enquirytype_id%>" />
														</div>
														<div class="form-element6">
															<label>Quote ID:&nbsp;&nbsp;</label>
																<%=mybean.quote_link%>
														</div>
                                                        <div class="row"></div>

														<div class="row">
															<div class="form-element6">
																<%
																	if (mybean.enquiry_enquirytype_id.equals("1")) {
																%>
																<label>Stock ID : </label> <input name="txt_vehstock_id"
																	type="text" class="form-control" id="txt_vehstock_id"
																	value="<%=mybean.so_vehstock_id%>"
																	onkeyup="toInteger(this.id);"
																	onchange="SecurityCheck('txt_vehstock_id',this,'hint_txt_vehstock_id');" />
																<div class="hint" id="hint_txt_vehstock_id"></div>
																<%
																	} else if (mybean.enquiry_enquirytype_id.equals("2")) {
																%>
																<label>Pre-Owned Stock ID : </label> <input
																	name="txt_so_preownedstock_id" type="text"
																	class="form-control" id="txt_so_preownedstock_id"
																	value="<%=mybean.so_preownedstock_id%>"
																	onkeyup="toInteger(this.id);"
																	onchange="SecurityCheck('txt_so_preownedstock_id',this,'hint_txt_so_preownedstock_id');" />
																<div class="hint" id="hint_txt_so_preownedstock_id"></div>
																<%
																	}
																%>
															</div>

															<div class="form-element6">
																<label>Colour : </label> <select id="dr_option_id"
																	name="dr_option_id" class="form-control"
																	onchange="PopulateCheck('dr_option_id',this,'hint_dr_option_id');">
																	<%=mybean.PopulateColour(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_option_id"></div>
															</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Allotment No.:</label>
																<input name="txt_so_allot_no" type="text"
																	id="txt_so_allot_no" value="<%=mybean.so_allot_no%>"
																	size="12" maxlength="10" onkeyup="toInteger(this.id);"
																	class="form-control"
																	onchange="SecurityCheck('txt_so_allot_no',this,'hint_so_allot_no');" />
																<div class="hint" id="hint_so_allot_no"></div>
														</div>
														<div class="form-element6">
															<label>Booking Amount:&nbsp;&nbsp;</label>
																<%=mybean.so_booking_amount%>
														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Payment Date<font color="#ff0000">*</font></b>: </label>
																<input name="txt_so_payment_date" type="text"
																	class="form-control datepicker"
																	id="txt_so_payment_date"
																	value="<%=mybean.so_paymentdate%>" size="12"
																	maxlength="10" 
									                                onchange="SecurityCheck('txt_so_payment_date',this,'hint_txt_so_payment_date');" />
																<div class="hint" id="hint_txt_so_payment_date"></div>
														</div>
														<div class="form-element6">
															<label>Tentative Delivery Date :</label>
																<input name="txt_so_promise_date" type="text"
																	class="form-control datepicker"
																	id="txt_so_promise_date"
																	value="<%=mybean.so_promisedate%>" size="12"
																	maxlength="10" 
                                                                    onchange="SecurityCheck('txt_so_promise_date',this,'hint_txt_so_promise_date');" /> 
																<div class="hint" id="hint_txt_so_promise_date"></div>
														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Retail Date :</label>
															<!--	<div class="col-md-6 col-xs-12"> -->
															<!--		<input name="txt_so_retail_date" type="text" -->
															<!--			class="form-control datepicker" -->
															<!--			 id="txt_so_retail_date" -->
															<%--			value="<%=mybean.so_retaildate%>" size="12" --%>
															<!--			maxlength="10" -->
															<!--			onchange="SecurityCheck('txt_so_retail_date', this, 'hint_txt_so_retail_date');" /> -->
															<!--		<div class="hint" id="hint_txt_so_retail_date"></div> -->
															<!--	</div> -->
																<%
																	if (mybean.so_retaildate.equals("")
																			|| (mybean.emp_id.equals("1"))
																			|| (mybean.comp_id.equals("1011") && mybean.emp_id
																					.equals("88"))) {
																%>
																	<input type="text" size="16" name="txt_so_retail_date"
																		id="txt_so_retail_date"
																		value="<%=mybean.so_retaildate%>" class="form-control datetimepicker"
																		onchange="SecurityCheck('txt_so_retail_date',this,'hint_txt_so_retail_date');">
																	<div class="hint" id="hint_txt_so_retail_date"></div>
																	 
																<%
																	} else {
																%>
																<div class="txt-align" id="">
																	<%=mybean.so_retaildate%>
																</div>
																<input name="txt_so_retail_date123" type="hidden"
																	id="txt_so_retail_date123"
																	value="<%=mybean.so_retaildate%>" />
																<%
																	}
																%>
														</div>
														<div class="form-element6">
															<label>Delivered Date:</label>
																<%
																	if (mybean.so_delivereddate.equals("")) {
																%>
																	<input type="text" size="16"
																		name="txt_so_delivered_date"
																		id="txt_so_delivered_date"
																		value="<%=mybean.so_delivereddate%>"
																		class="form-control datetimepicker"
																		onchange="SecurityCheck('txt_so_delivered_date',this,'hint_txt_so_delivered_date');">
																	 
																<!--	enabled -->
																<!--		<input name="txt_so_delivered_date" type="text" -->
																<!--			class="form-control datepicker" -->
																<!--			 -->
																<!--			id="txt_so_delivered_date" -->
																<%--			value="<%=mybean.so_delivereddate%>" size="20" --%>
																<!--			maxlength="16"/> -->
																<!-- <!--			onchange="SecurityCheck('txt_so_delivered_date',this,'hint_txt_so_delivered_date');" /> -->
																<%
																	} else if (!(mybean.emp_id.equals("1") || mybean.emp_id
																			.equals("88"))) {
																%>
																	<%=mybean.so_delivereddate%>
																<input name="txt_so_delivered_date123" type="hidden"
																	id="txt_so_delivered_date123"
																	value="<%=mybean.so_delivereddate%>" />
																<%
																	} else {
																%>
																	<input type="text"
																		name="txt_so_delivered_date"
																		id="txt_so_delivered_date"
																		value="<%=mybean.so_delivereddate%>"
																		class="form-control datetimepicker"
																		onchange="SecurityCheck('txt_so_delivered_date',this,'hint_txt_so_delivered_date');">
																<%
																	}
																%>
																<div class="hint" id="hint_txt_so_delivered_date">
																</div>
														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Registration No.:</label>
																<input name="txt_so_reg_no" type="text"
																	class="form-control" id="txt_so_reg_no"
																	value="<%=mybean.so_reg_no%>" size="22" maxlength="20"
																	onchange="SecurityCheck('txt_so_reg_no',this,'hint_txt_so_reg_no');" />
																<div class="hint" id="hint_txt_so_reg_no"></div>
														</div>

														<div class="form-element6">
															<label>Registration Date:</label>
																<input name="txt_so_reg_date" type="text"
																	class="form-control datepicker"
																	id="txt_so_reg_date"
																	value="<%=mybean.so_regdate%>" size="12" maxlength="10" 
																onchange="SecurityCheck('txt_so_reg_date',this,'hint_txt_so_reg_date');" />
																<div class="hint" id="hint_txt_so_reg_date"></div>
														</div>
														</div>
														<div class="row">

														<div class="form-element6">
															<label>DOB:</label>
																<input name="txt_so_dob" type="text"
																	class="form-control datepicker"
																	id="txt_so_dob"
																	value="<%=mybean.so_dob%>" size="12" maxlength="10" 
														            onchange="SecurityCheck('txt_so_dob',this,'hint_txt_so_dob');" /> 
																<div class="hint" id="hint_txt_so_dob"></div>
														</div>

														<div class="form-element6">
															<label>PAN:</label>
																<input class="form-control" name="txt_so_pan"
																	type="text" id="txt_so_pan" value="<%=mybean.so_pan%>"
																	size="10" maxlength="10"
																	onchange="SecurityCheck('txt_so_pan',this,'hint_txt_so_pan');" />
																<div class="hint" id="hint_txt_so_pan"></div>
														</div>
														</div>
														<div class="row">
													<div class="form-element6 form-element-margin">
															<label>Sales Order Open:</label>
																<input id="chk_so_open" type="checkbox"
																	name="chk_so_open"
																	<%=mybean.PopulateCheck(mybean.so_open)%>
																	onchange="SecurityCheck('chk_so_open',this,'hint_chk_so_open');" />
															<div class="hint" id="hint_chk_so_open"></div>
														</div>

													<div class="form-element6">
															<label>Sales Order Reference No.: </label>
																<input name="txt_so_refno" type="text"
																	class="form-control" id="txt_so_refno"
																	value="<%=mybean.so_refno%>" size="32" maxlength="50"
																	onchange="SecurityCheck('txt_so_refno',this,'hint_txt_so_refno');"
																	onkeyup="toInteger('txt_so_refno','Ref No.');" />
																<div class="hint" id="hint_txt_so_refno"></div>
														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Cancel Date: </label>
															<div id="cancel_date" class="txt-align">
																<%=mybean.so_canceldate%>
															</div>
														</div>
														<div class="form-element6">
															<label>Cancel Reason: </label>
															<div id="cancel_reason" class="txt-align">
																<%=mybean.cancelreason_name%>
															</div>
														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>CIN Status: </label>
															<div id ="so_cinstatus_id">
															<%if(mybean.so_active.equals("0")){ %>
																<select name="dr_so_cinstatus_id" class="form-control" id="dr_so_cinstatus_id" 
																onchange="SecurityCheck('dr_so_cinstatus_id',this,'hint_dr_so_cinstatus_id');">
																	<%=mybean.PopulateCINStatus(mybean.comp_id) %>
																</select>
															<%}else{ %>
																<%=mybean.cinstatus_name%>
															<%} %>
															<div class="hint" id="hint_dr_so_cinstatus_id"></div>
															</div>
														</div>

														<div class="form-element6">
																	<label>Active: </label>
																		<%if(mybean.so_active.equals("1")){ %>
																			<a href="../sales/salesorder-cancel.jsp?so_id=<%=mybean.so_id%>" 
																				data-target="#Hintclicktocall" data-toggle="modal">
																				<input id="chk_so_active" type="checkbox" name="chk_so_active"
																				<%=mybean.PopulateCheck(mybean.so_active)%> />
																			</a>
																		<%} else {%>
																			<input id="chk_so_active" type="checkbox" name="chk_so_active"
																				<%=mybean.PopulateCheck(mybean.so_active)%>
																				onchange="ActivateSO();" />
																		<%} %>
																		<div class="hint col-md-offset-4" id="hint_chk_so_active"></div>
														</div>
														</div>
													<div class="row"></div>	
													<div class="form-element6">
														<div class="form-element4 form-element-margin">
																<label>Authorize PDI:</label>
																	<input id="chk_so_authorize_pdi" type="checkbox"
																		name="chk_so_authorize_pdi"
																		<%=mybean.PopulateCheck(mybean.so_authorize_pdi)%>
																		onchange="SecurityCheck('chk_so_authorize_pdi',this,'hint_chk_so_authorize_pdi');" />
																<div class="hint" id="hint_chk_so_authorize_pdi"></div>
															</div>
															
															<div class="form-element4 form-element-margin">
																<label>Authorize Accessories:</label>
																	<input id="chk_so_authorize_accessories" type="checkbox"
																		name="chk_so_authorize_accessories"
																		<%=mybean.PopulateCheck(mybean.so_authorize_accessories)%>
																		onchange="SecurityCheck('chk_so_authorize_accessories',this,'hint_chk_so_authorize_accessories');" />
																<div class="hint" id="hint_chk_so_authorize_accessories"></div>
															</div>
															
															<div class="form-element4 form-element-margin">
																<label>Authorize Accounts:</label>
																	<input id="chk_so_authorize_accounts" type="checkbox"
																		name="chk_so_authorize_accounts"
																		<%=mybean.PopulateCheck(mybean.so_authorize_accounts)%>
																		onchange="SecurityCheck('chk_so_authorize_accounts',this,'hint_chk_so_authorize_accounts');" />
																<div class="hint" id="hint_chk_so_authorize_accounts"></div>
															</div>
														</div>
														<div class="form-element6">	
															<div class="form-element3 form-element-margin">
																<label>Authorize Insurance:</label>
																	<input id="chk_so_authorize_insurance" type="checkbox"
																		name="chk_so_authorize_insurance"
																		<%=mybean.PopulateCheck(mybean.so_authorize_insurance)%>
																		onchange="SecurityCheck('chk_so_authorize_insurance',this,'hint_chk_so_authorize_insurance');" />
																<div class="hint" id="hint_chk_so_authorize_insurance"></div>
															</div>
															
															<div class="form-element4 form-element-margin">
																<label>Authorize Registration:</label>
																	<input id="chk_so_authorize_registration" type="checkbox"
																		name="chk_so_authorize_registration"
																		<%=mybean.PopulateCheck(mybean.so_authorize_registration)%>
																		onchange="SecurityCheck('chk_so_authorize_registration',this,'hint_chk_so_authorize_registration');" />
																<div class="hint" id="hint_chk_so_authorize_registration"></div>
															</div>
															
															<div class="form-element5 form-element-margin">
																<label>Authorize Delivery Coordinator:</label>
																	<input id="chk_so_authorize_deliverycoordinator" type="checkbox"
																		name="chk_so_authorize_deliverycoordinator"
																		<%=mybean.PopulateCheck(mybean.so_authorize_deliverycoordinator)%>
																		onchange="SecurityCheck('chk_so_authorize_deliverycoordinator',this,'hint_chk_so_authorize_deliverycoordinator');" />
																<div class="hint" id="hint_chk_so_authorize_deliverycoordinator"></div>
															</div>
														</div>	
														<div class="row"></div>
														<div class="form-element6">
															<label>Notes:</label>
																<textarea name="txt_so_notes" cols="70" rows="4" class="form-control" id="txt_so_notes"
																	onchange="SecurityCheck('txt_so_notes',this,'hint_txt_so_notes')"><%=mybean.so_notes%></textarea>
																<div class="hint" id="hint_txt_so_notes"></div>
														</div>
														
                                                        <div class="row"></div>
														<div class="form-element6">
															<label>Entry By:&nbsp;&nbsp;</label>
																<%=mybean.so_entry_by%>
																<input type="hidden" name="entry_by"
																	value="<%=mybean.so_entry_by%>">
														</div>
														
														<div class="form-element6">
															<label>Entry Date:&nbsp;&nbsp; </label>
																<%=mybean.so_entry_date%>
																<input type="hidden" name="so_entry_date"
																	value="<%=mybean.so_entry_date%>">
														</div>
														<%
															if (mybean.so_modified_by != null
																	&& !mybean.so_modified_by.equals("")) {
														%>

														<div class="form-element6">
															<label>Modified By:&nbsp;&nbsp;</label>
																<%=mybean.so_modified_by%>
																<input type="hidden" name="modified_by"
																	value="<%=mybean.so_modified_by%>">
														</div>

														<div class="form-element6">
															<label>Modified Date:&nbsp;&nbsp;</label>
																<%=mybean.so_modified_date%>
																<input type="hidden" name="so_modified_date"
																	value="<%=mybean.so_modified_date%>">
														</div>
														<%
															}
														%>

														<%
															//}
														%>

													</form>

												</div>
<!-- 												end of first tab -->
											</div>
										</div>






									</div>

									<div class="tab-pane" id="tabs-7">
										<div class="portlet box ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Registration
													Status</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<font color="#ff0000"><b><%=mybean.msg%></b></font>
													<form name="form1" id="form1" method="post" class="form-horizontal">
														
														<div class="form-element6">
																<label>Customer:&nbsp;</label>
																	<%=mybean.link_customer_name%>
														</div>
														<div class="form-element6">
																<label>Contact:&nbsp;</label>
																	<%=mybean.link_contact_name%>
														</div>
														<div class="form-element6">
																<label>SO ID:&nbsp;</label>
																	<%=mybean.so_id%>
																	<input name="txt_item_id" type="hidden"
																		id="txt_item_id" value="<%=mybean.item_id%>" />
														</div>
														<div class="form-element6">
																<label>SO Date:&nbsp;</label>
																		<%=mybean.sodate%>
														</div>
														
														<div class="form-element6">
																<label>Promised Delivery Date:&nbsp;</label>
																		<%=mybean.so_promisedate%>
														</div>
														<div class="form-element6">
																<label>Delivery Date:&nbsp;</label>
																		<%=mybean.so_delivereddate%>
														</div>

                                                        <div class="form-element6">
																<label>Invoice ID:&nbsp;</label>
																	<%=mybean.invoice_id%>
														</div>
														<div class="form-element6">
																<label>Invoice Date:&nbsp;</label>
																		<%=mybean.invoice_date%>
														</div>
														
                                                     <div class="form-element6">
																<label>Enquiry ID:&nbsp;</label>
																	<%=mybean.enquiry_link%>
																	<input name="txt_enquiry_custtype_id" type="hidden"
																		id="txt_enquiry_custtype_id"
																		value="<%=mybean.enquiry_custtype_id%>" />
														</div>
														<div class="form-element6">
																<label>Quote ID:&nbsp;</label>
																		<%=mybean.quote_link%>
														</div>
														
														<div class="form-element6">
																<label>FASTag ID:&nbsp;</label>
																		<%=mybean.so_vehstock_fastag%>
														</div>
														<div class="row"></div>
														
														<div class="form-element6">
															<label>RC Delivery:</label>
																<select name="dr_so_reg_rc_delivery"
																	class="form-control" id="dr_so_reg_rc_delivery"
																	onchange="PopulateCheck('dr_so_reg_rc_delivery',this,'hint_dr_so_reg_rc_delivery')">
																	<%=mybean.PopulateRcDelivary()%>
																</select>
																<div class="hint" id="hint_dr_so_reg_rc_delivery"></div>
														</div>

														<!--<div class="form-group"> -->
														<!--	<label class="control-label col-md-4 col-xs-12">Sales -->
														<!--		File Received:</label> -->
														<!--	<div class="col-md-6 col-xs-12"> -->
														<!--		<input name="txt_so_reg_salesfile_received_date" -->
														<!--			type="text" class="form-control" -->
														<!--			id="txt_so_reg_salesfile_received_date" -->
														<%--			value="<%=mybean.salesfile_received_date%>" size="12" --%>
														<!--			maxlength="14" -->
														<!--			onchange="SecurityCheck('txt_so_reg_salesfile_received_date',this,'hint_txt_so_reg_salesfile_received_date');" /> -->
														<%--		TAT:<span id="txt_so_reg_salesfile_received_tat">&nbsp;<%=mybean.so_reg_salesfile_received_tat%> --%>
														<%--			<% --%>
														<!-- //				if (!mybean.so_reg_salesfile_received_tat.equals("")) { -->
														<%--			%> Days <% --%>
														<!-- //				} -->
														<%--			%></span> <br /> --%>
														<!--		<div class="hint" -->
														<!--			id="hint_txt_so_reg_salesfile_received_date"></div> -->
														<!--	</div> -->
														<!--</div> -->
														
														<div class="form-element6 form-element-margin">
															<label>HSRP:</label>
															<input id="chk_so_reg_hsrp" type="checkbox" name="chk_so_reg_hsrp"
																<%=mybean.PopulateCheck(mybean.so_reg_hsrp)%>
																onchange="SecurityCheck('chk_so_reg_hsrp',this,'hint_chk_so_reg_hsrp');" />
															<div class="hint" id="hint_chk_so_reg_hsrp"></div>
														</div>


														 <div class="row"></div>
				                                         <div class="form-element3">
															<label>HSRP Received Date:</label>
															<input name="txt_so_reg_hsrp_received_date" type="text"
																class="form-control datepicker" id="txt_so_reg_hsrp_received_date"
																value="<%=mybean.hsrp_received_date%>" size="12" maxlength="14"
																onchange="SecurityCheck('txt_so_reg_hsrp_received_date',this,'hint_txt_so_reg_hsrp_received_date');" /> 
															<div class="hint" id="hint_txt_so_reg_hsrp_received_date"></div>
														</div>
														
														<div class="form-element3 form-element-margin">
															<label>TAT:</label>
															<span id="txt_so_reg_hsrp_received_tat">&nbsp;<%=mybean.so_reg_hsrp_received_tat%>
																<%
																	if (!mybean.so_reg_hsrp_received_tat.equals("")) {
																%> Days
																<%
																	}
																%>
															</span>
														</div>

													 	<div class="form-element3">
															<label>HSRP Installation Date:</label>
															<input name="txt_so_reg_hsrp_install_date" type="text"
																class="form-control datepicker" id="txt_so_reg_hsrp_install_date"
																value="<%=mybean.hsrp_install_date%>" size="12" maxlength="14"
																onchange="SecurityCheck('txt_so_reg_hsrp_install_date',this,'hint_txt_so_reg_hsrp_install_date');" /> 
															<div class="hint" id="hint_txt_so_reg_hsrp_install_date"></div>
														</div>
														
														<div class="form-element3 form-element-margin">
															<label>TAT:</label>
															<span id="txt_so_reg_hsrp_install_tat">&nbsp;<%=mybean.so_reg_hsrp_install_tat%>
																<%
																	if (!mybean.so_reg_hsrp_install_tat.equals("")) {
																%> Days
																<%
																	}
																%>
															</span>
														</div>
														
														 <div class="row"></div>

														<div class="form-element6">
															<label>Permanent Registration Number:</label>
																<input name="txt_so_reg_perm_reg_no" type="text"
																	class="form-control" id="txt_so_reg_perm_reg_no"
																	value="<%=mybean.so_reg_perm_reg_no%>" size="32"
																	maxlength="20"
																	onchange="SecurityCheck('txt_so_reg_perm_reg_no',this,'hint_txt_so_reg_perm_reg_no');" />
																<div class="hint" id="hint_txt_so_reg_perm_reg_no"></div>
														</div>

														<div class="form-element3">
															<label>RC Received Date:</label> <input
																name="txt_so_reg_rc_received_date" type="text"
																class="form-control datepicker"
																id="txt_so_reg_rc_received_date"
																value="<%=mybean.rc_received_date%>" size="12"
																maxlength="14" 
															onchange="SecurityCheck('txt_so_reg_rc_received_date',this,'hint_txt_so_reg_rc_received_date');" />
															<div class="hint" id="hint_txt_so_reg_rc_received_date"></div>
														</div>
														<div class="form-element3 form-element-margin">
														<label>TAT:</label>
														<span id="txt_so_reg_rc_received_tat">&nbsp;<%=mybean.so_reg_rc_received_tat%>
																			<%
																				if (!mybean.so_reg_rc_received_tat.equals("")) {
																			%> Days <%
																				}
																			%></span>
														</div>
																
														<div class="row"></div>		
														<div class="form-element3">
															<label>RC Handover Date to Customer:</label>
															<input name="txt_so_reg_rc_handover_date" type="text"
																class="form-control datepicker" id="txt_so_reg_rc_handover_date"
																value="<%=mybean.rc_handover_date%>" size="12" maxlength="14"
																onchange="SecurityCheck('txt_so_reg_rc_handover_date',this,'hint_txt_so_reg_rc_handover_date');" />
															<div class="hint" id="hint_txt_so_reg_rc_handover_date"></div>
														</div>
														<div class="form-element3 form-element-margin">
															<label>TAT:</label>
															<span id="txt_so_reg_rc_handover_tat">&nbsp;<%=mybean.so_reg_rc_handover_tat%>
																<%
																	if (!mybean.so_reg_rc_handover_tat.equals("")) {
																%> Days
																<%
																	}
																%>
															</span>
														</div>		
																
														<div class="form-element6">
															<label >RC Delivery Received By:</label>
															<input name="txt_so_rcdel_person_received" type="text"
																class="form-control" id="txt_so_rcdel_person_received"
																value="<%=mybean.so_rcdel_person_received%>" size="32" maxlength="20"
																onchange="SecurityCheck('txt_so_rcdel_person_received',this,'hint_txt_so_rcdel_person_received');" />
															<div class="hint" id="hint_txt_so_rcdel_person_received"></div>
														</div>
														
														 <div class="row"></div>
														
														<div class="form-element6">
															<label >RC Delivery Person Contact No.:</label>
															<input name="txt_so_rcdel_person_contact_no" type="text"
																class="form-control" id="txt_so_rcdel_person_contact_no"
																value="<%=mybean.so_rcdel_person_contact_no%>" size="12" maxlength="10"
																onkeyup="toPhone('txt_so_rcdel_person_contact_no', 'Contact Number');"
																onchange="SecurityCheck('txt_so_rcdel_person_contact_no',this,'hint_txt_so_rcdel_person_contact_no');" />
															<div class="hint" id="hint_txt_so_rcdel_person_contact_no"></div>
														</div>

														<div class="form-element6">
															<label >RC Delivery Person Relation:</label>
															<input name="txt_so_rcdel_person_relation" type="text"
																class="form-control" id="txt_so_rcdel_person_relation"
																value="<%=mybean.so_rcdel_person_relation%>" size="32" maxlength="20"
																onchange="SecurityCheck('txt_so_rcdel_person_relation',this,'hint_txt_so_rcdel_person_relation');" />
															<div class="hint" id="hint_txt_so_rcdel_person_relation"></div>
														</div>
														
														 <div class="row"></div>
														
														<div class="form-element3">
															<label>RC Delivery Time:</label>
															<input type="text" size="16" name="txt_so_rcdel_delivery_time"
																id="txt_so_rcdel_delivery_time" value="<%=mybean.rcdel_delivery_time%>"
																class="form-control datetimepicker"
																onchange="SecurityCheck('txt_so_rcdel_delivery_time',this,'hint_txt_so_rcdel_delivery_time');">
															<div class="hint" id="hint_txt_so_rcdel_delivery_time"></div>
														</div>
														<div class="form-element3 form-element-margin">
															<label>TAT:</label>
															<span id="txt_so_rcdel_delivery_time_tat">&nbsp;<%=mybean.so_rcdel_delivery_tat%>
																<%
																	if (!mybean.so_rcdel_delivery_tat.equals("")) {
																%>
																Days
																<%
																	}
																%>
															</span>
														</div>	
														
														<div class="form-element6">
															<label>Notes:</label>
																<textarea name="txt_so_reg_notes" cols="70" rows="4"
																	class="form-control" id="txt_so_reg_notes"
																	onchange="SecurityCheck('txt_so_reg_notes',this,'hint_txt_so_reg_notes')"><%=mybean.so_reg_notes%></textarea>
																<div class="hint" id="hint_txt_so_reg_notes"></div>
														</div>
													</form>
												</div>
											</div>
										</div>
									</div>
									<div class="tab-pane" id="tabs-8">
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Finance
													Status</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<form name="form1" id="form1" method="post" class="form-horizontal">
														<div class="form-element6">
															<label>Customer:</label>
																<%=mybean.link_customer_name%>
														</div>
														<div class="form-element6">
															<label>Contact:</label>
																<%=mybean.link_contact_name%>
														</div>

														<div class="form-element6">
															<label>SO ID:</label>
																<%=mybean.so_id%>
																<input name="txt_item_id" type="hidden" id="txt_item_id"
																	value="<%=mybean.item_id%>" />
														</div>
														<div class="form-element6">
															<label>SO Date:</label>
																<%=mybean.sodate%>
														</div>
														<div class="form-element6">
															<label>Enquiry ID:</label>
																<%=mybean.enquiry_link%>
																<input type="hidden" id="so_id" name="so_id"
																	value="<%=mybean.so_id%>" />
														</div>
														<div class="form-element3">
															<label>Quote ID:</label>
																<%=mybean.quote_link%>
														</div>

														<div class="form-element3">
															<label>Promised
																Delivery Date:</label>
																<%=mybean.so_promisedate%>
														</div>

														<div class="form-element6">
															<label>Finance
																Status:</label>
																<select name="dr_so_finstatus_id"
																	id="dr_so_finstatus_id" class="form-control"
																	onchange="FinStatusCheck('dr_so_finstatus_id',this,'hint_dr_so_finstatus_id');">
																	<%=mybean.PopulateFinanceStatus(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_so_finstatus_id"></div>
														</div>

														<div class="form-element6">
															<label>Loan
																Amount:</label>
																<input name="txt_so_finstatus_loan_amt" type="text"
																	class="form-control" id="txt_so_finstatus_loan_amt"
																	value="<%=mybean.so_finstatus_loan_amt%>" size="12"
																	maxlength="9"
																	onchange="FinStatusCheck('txt_so_finstatus_loan_amt',this,'hint_txt_so_finstatus_loan_amt');"
																	onkeyup="toInteger(this.id);" />
																<div class="hint" id="hint_txt_so_finstatus_loan_amt"></div>
														</div>
														<div class="row"></div>
														<div class="form-element6">
															<label>Description:</label>
																<textarea id="txt_so_finstatus_desc"
																	name="txt_so_finstatus_desc" cols="70" rows="4"
																	class="form-control"
																	onchange="FinStatusCheck('txt_so_finstatus_desc',this,'hint_txt_so_finstatus_desc');"><%=mybean.so_finstatus_desc%></textarea>
																<div class="hint" id="hint_txt_so_finstatus_desc"></div>
														</div>
														<div class="form-element6">
															<label>EMI/Value:</label>
																<input name="txt_so_finstatus_emi_value" type="text"
																	class="form-control" id="txt_so_finstatus_emi_value"
																	value="<%=mybean.so_finstatus_emi_value%>" size="12"
																	maxlength="9"
																	onchange="FinStatusCheck('txt_so_finstatus_emi_value',this,'hint_txt_so_finstatus_emi_value');"
																	onkeyup="toInteger(this.id);" />
																<div class="hint" id="hint_txt_so_finstatus_emi_value"></div>
														</div>
														<div class="form-element6">
															<label>Advance/Arrears:</label>
																<select name="dr_so_finstatus_advance"
																	class="form-control" id="dr_so_finstatus_advance"
																	onchange="FinStatusCheck('dr_so_finstatus_advance',this,'hint_dr_so_finstatus_advance');">
																	<%=mybean.PopulateAdvance()%>
																</select>
																<div class="hint" id="hint_dr_so_finstatus_advance"></div>
														</div>
														<div class="row"></div>
														<div class="row">
														<div class="form-element6">
															<label>Disbursed Date<font color=#ff0000> *</font><b>:</b></label>
															 <input name="txt_so_finstatus_disbursed_date"
																id="txt_so_finstatus_disbursed_date"
																class="form-control datepicker" type="text"
																value="<%=mybean.so_finstatus_disburseddate%>" 
																onchange="FinStatusCheck('txt_so_finstatus_disbursed_date',this,'hint_txt_so_finstatus_disbursed_date');" />
														<div class="hint" style="top: 10px;" id="hint_txt_so_finstatus_disbursed_date"></div>
														</div>

														<div class="form-element6 form-element-margin">
															<label>TAT:</label>
																<span id="txt_so_finstatus_disbursed_tat">&nbsp;<%=mybean.so_finstatus_disbursed_tat%>
																			<%
																				if (!mybean.so_finstatus_disbursed_tat.equals("")) {
																			%> Days <%
																				}
																			%></span>
														</div>
														</div>

                                                        <div class="row"></div>
														<div class="form-element6">
															<label >Disbursed
																Amount:</label>
																<input name="txt_so_finstatus_disbursed_amt" type="text"
																	class="form-control"
																	id="txt_so_finstatus_disbursed_amt"
																	value="<%=mybean.so_finstatus_disbursed_amt%>"
																	size="12" maxlength="9"
																	onchange="FinStatusCheck('txt_so_finstatus_disbursed_amt',this,'hint_txt_so_finstatus_disbursed_amt');"
																	onkeyup="toInteger(this.id);" />
																<div class="hint"
																	id="hint_txt_so_finstatus_disbursed_amt"></div>
															
														</div>
														<div class="form-element6">
															<label>Tenure:</label>
																<input name="txt_so_finstatus_tenure" type="text"
																	class="form-control" id="txt_so_finstatus_tenure"
																	value="<%=mybean.so_finstatus_tenure%>" size="12"
																	maxlength="9"
																	onchange="FinStatusCheck('txt_so_finstatus_tenure',this,'hint_txt_so_finstatus_tenure');"
																	onkeyup="toInteger(this.id);" /> Months
																<div class="hint" id="hint_txt_so_finstatus_tenure"></div>

														</div>
														<div class="form-element6">
															<label>Bank:</label>
																<select name="dr_so_finstatus_bank_id"
																	id="dr_so_finstatus_bank_id" class="form-control"
																	onchange="FinStatusCheck('dr_so_finstatus_bank_id',this,'hint_dr_so_finstatus_bank_id');">
																	<%=mybean.PopulateSOBank(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_so_finstatus_bank_id"></div>

														</div>
														<!--	<div class="form-group"> -->
														<!--		<label class="control-label col-md-4 col-xs-12">Bank:</label> -->
														<!--		<div class="col-md-6 col-xs-12"> -->
														<!--			<select name="dr_so_finstatus_bank_id" -->
														<!--				id="dr_so_finstatus_bank_id" class="form-control" -->
														<!--				onchange="FinStatusCheck('dr_so_finstatus_bank_id',this,'hint_dr_so_finstatus_bank_id');"> -->
														<%--				<%=mybean.PopulateSOBank(mybean.comp_id)%> --%>
														<!--			</select> -->
														<!--			<div class="hint" id="hint_dr_so_finstatus_bank_id"></div> -->

														<!--		</div> -->
														<!--	</div> -->
														<div class="form-element6">
															<label>Scheme:</label>
																<input name="txt_so_finstatus_scheme" type="text"
																	class="form-control" id="txt_so_finstatus_scheme"
																	value="<%=mybean.so_finstatus_scheme%>" size="70"
																	maxlength="255"
																	onchange="FinStatusCheck('txt_so_finstatus_scheme',this,'hint_txt_so_finstatus_scheme');" />
																<div class="hint" id="hint_txt_so_finstatus_scheme"></div>

														</div>
														<div class="row"></div>
														<div class="form-element6">
															<label>Processing
																Fees:</label>
																<input name="txt_so_finstatus_process_fee" type="text"
																	class="form-control" id="txt_so_finstatus_process_fee"
																	value="<%=mybean.so_finstatus_process_fee%>" size="12"
																	maxlength="10"
																	onchange="FinStatusCheck('txt_so_finstatus_process_fee',this,'hint_txt_so_finstatus_process_fee');"
																	onkeyup="toFloat(this.id);" />
																<div class="hint" id="hint_txt_so_finstatus_process_fee"></div>

														</div>
														<div class="form-element6">
															<label >Subvention
																Amount:</label>
																<input name="txt_so_finstatus_subvention" type="text"
																	class="form-control" id="txt_so_finstatus_subvention"
																	value="<%=mybean.so_finstatus_subvention%>" size="12"
																	maxlength="9"
																	onchange="FinStatusCheck('txt_so_finstatus_subvention',this,'hint_txt_so_finstatus_subvention');"
																	onkeyup="toInteger(this.id);" />
																<div class="hint" id="hint_txt_so_finstatus_subvention"></div>
														</div>
														<div class="row"></div>
														<div class="form-element6">
															<label>Gross
																Payout:</label>
																<input name="txt_so_finstatus_gross_payout" type="text"
																	class="form-control" id="txt_so_finstatus_gross_payout"
																	value="<%=mybean.so_finstatus_gross_payout%>" size="12"
																	maxlength="9"
																	onchange="FinStatusCheck('txt_so_finstatus_gross_payout',this,'hint_txt_so_finstatus_gross_payout');"
																	onkeyup="toInteger(this.id);" />
																<div class="hint"
																	id="hint_txt_so_finstatus_gross_payout"></div>

														</div>
														<div class="form-element6">
															<label>Income
																On Payment:</label>
																<input name="txt_so_finstatus_income_on_payment"
																	type="text" class="form-control"
																	id="txt_so_finstatus_income_on_payment"
																	value="<%=mybean.so_finstatus_income_on_payment%>"
																	size="12" maxlength="9"
																	onchange="FinStatusCheck('txt_so_finstatus_income_on_payment',this,'hint_txt_so_finstatus_income_on_payment');"
																	onkeyup="toInteger(this.id);" />
																<div class="hint"
																	id="hint_txt_so_finstatus_income_on_payment"></div>

														</div>
														<div class="row"></div>
														<div class="form-element6">
															<label >Bank
																Rack Rate:</label>
																<input name="txt_so_finstatus_bank_rack_rate"
																	type="text" class="form-control"
																	id="txt_so_finstatus_bank_rack_rate"
																	value="<%=mybean.so_finstatus_bank_rack_rate%>"
																	size="12" maxlength="9"
																	onchange="FinStatusCheck('txt_so_finstatus_bank_rack_rate',this,'hint_txt_so_finstatus_bank_rack_rate');"
																	onkeyup="toInteger(this.id);" />
																<div class="hint"
																	id="hint_txt_so_finstatus_bank_rack_rate"></div>

														</div>
														<div class="form-element6">
															<!--		<div class="col-md-12 col-xs-12"> -->
															<label>Customer
																ROI:</label>
																<input name="txt_so_finstatus_customer_roi" type="text"
																	class="form-control" id="txt_so_finstatus_customer_roi"
																	value="<%=mybean.so_finstatus_customer_roi%>" size="12"
																	maxlength="9"
																	onchange="FinStatusCheck('txt_so_finstatus_customer_roi',this,'hint_txt_so_finstatus_customer_roi');"
																	onkeyup="toFloat(this.id);" />%
																<div class="hint"
																	id="hint_txt_so_finstatus_customer_roi"></div>

															<!--		</div> -->
														</div>
														<div class="form-element6">
															<label >Occupation:</label>
																<input name="txt_so_finstatus_occupation" type="text"
																	class="form-control" id="txt_so_finstatus_occupation"
																	value="<%=mybean.so_finstatus_occupation%>" size="70"
																	maxlength="255"
																	onchange="FinStatusCheck('txt_so_finstatus_occupation',this,'hint_txt_so_finstatus_occupation');" />
																<div class="hint" id="hint_txt_so_finstatus_occupation"></div>

														</div>
														<div class="form-element6">
															<label >Industry:</label>
																<input name="txt_so_finstatus_industry" type="text"
																	class="form-control" id="txt_so_finstatus_industry"
																	value="<%=mybean.so_finstatus_industry%>" size="70"
																	maxlength="255"
																	onchange="FinStatusCheck('txt_so_finstatus_industry',this,'hint_txt_so_finstatus_industry');" />
																<div class="hint" id="hint_txt_so_finstatus_industry"></div>

														</div>
														<div class="row"></div>
														<div class="form-element6">
															<label >Income
																as per Doc:</label>
																<input name="txt_so_finstatus_income_asperdoc"
																	type="text" class="form-control"
																	id="txt_so_finstatus_income_asperdoc"
																	value="<%=mybean.so_finstatus_income_asperdoc%>"
																	size="12" maxlength="9"
																	onchange="FinStatusCheck('txt_so_finstatus_income_asperdoc',this,'hint_txt_so_finstatus_income_asperdoc');"
																	onkeyup="toInteger(this.id);" />
																<div class="hint"
																	id="hint_txt_so_finstatus_income_asperdoc"></div>

														</div>


														<div class="form-element6">

															<label>Date
																of Birth:<font color=#ff0000><b> *</b></font>
															</label>
																<input name="txt_so_finstatus_dob"
																	id="txt_so_finstatus_dob"
																	class="form-control datepicker"
																	 type="text"
																	value="<%=mybean.so_finstatus_dob%>" 
															        onchange="FinStatusCheck('txt_so_finstatus_dob',this,'hint_txt_so_finstatus_dob');" /> 
																<div class="hint" id="hint_txt_so_finstatus_dob"></div>

														</div>


														<!--		<div class="form-group"> -->
														<!--			<label class="control-label col-md-4">Date of Birth:</label> -->
														<!--			<div class="col-md-2 col-xs-12"> -->
														<!--				<input name="txt_so_finstatus_dob" -->
														<!--		id="txt_so_finstatus_dob" type="text" class="form-control" -->
														<%--		value="<%=mybean.so_finstatus_dob%>" size="12" --%>
														<!--		maxlength="10" -->
														<!--		onchange="FinStatusCheck('txt_so_finstatus_dob',this,'hint_txt_so_finstatus_dob');" /> -->
														<!--		<div class="hint" id="hint_txt_so_finstatus_dob"></div> -->

														<!--		</div> -->
														<!--		</div> -->
													<div class="row"></div>
														<div class="form-element6">
															<label >Gender:</label>
																<select name="dr_so_finstatus_sex" class="form-control"
																	id="dr_so_finstatus_sex"
																	onchange="FinStatusCheck('dr_so_finstatus_sex',this,'hint_dr_so_finstatus_sex');">
																	<%=mybean.PopulateGender()%>
																</select>
																<div class="hint" id="hint_dr_so_finstatus_sex"></div>

														</div>
														<div class="form-element6">
															<label>Notes:</label>
																<textarea name="txt_so_fin_notes" cols="70" rows="4"
																	class="form-control" id="txt_so_fin_notes"
																	onchange="SecurityCheck('txt_so_fin_notes',this,'hint_txt_so_fin_notes')"><%=mybean.so_notes%></textarea>
																<div class="hint" id="hint_txt_so_fin_notes"></div>
														</div>

													</form>
												</div>
											</div>
										</div>
									</div>

									<div class="tab-pane" id="tabs-9">
										<form name="form1" id="form1" method="post" class="form-horizontal">
											<%
												//if(mybean.msg.equals("")){
											%>
											<br />
											<!-- 											<center> -->
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Offers</div>
												</div>
												<div class="portlet-body portlet-empty">
													<div class="container-fluid">
														<!--START PORTLET BODY -->
														<br />
														<div class="row">
														<div class="form-element6">
															<label>Consumer Offers:</label>
																<input name="txt_so_off_consumer" type="text"
																	class="form-control" id="txt_so_off_consumer" size="12"
																	maxlength="10" value="<%=mybean.so_offer_consumer%>"
																	onchange="SecurityCheck('txt_so_off_consumer',this,'hint_txt_so_off_consumer');"
																	onkeyup="toInteger('txt_so_off_consumer','Consumer Offers');" />
																<div class="hint" id="hint_txt_so_off_consumer"></div>
														</div>
														<div class="form-element6">
															<label>Exchange Bonus: </label>
																<input name="txt_so_off_exchange_bonus" type="text"
																	class="form-control" id="txt_so_off_exchange_bonus"
																	size="12" maxlength="10"
																	value="<%=mybean.so_offer_exchange_bonus%>"
																	onchange="SecurityCheck('txt_so_off_exchange_bonus',this,'hint_txt_so_off_exchange_bonus');"
																	onkeyup="toInteger('txt_so_off_exchange_bonus','Exchange Bonus');" />
																<div class="hint" id="hint_txt_so_off_exchange_bonus"></div>
														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Corporate / Any: </label>
																<input name="txt_so_off_corporate" type="text"
																	class="form-control" id="txt_so_off_corporate"
																	size="12" maxlength="10"
																	value="<%=mybean.so_offer_corporate%>"
																	onchange="SecurityCheck('txt_so_off_corporate',this,'hint_txt_so_off_corporate');"
																	onkeyup="toInteger('txt_so_off_corporate','Corporate Amount');" />
																<div class="hint" id="hint_txt_so_off_corporate"></div>
														</div>
														<div class="form-element6">
															<label>Special Schemes: </label>
																<input name="txt_so_off_spcl_scheme" type="text"
																	class="form-control" id="txt_so_off_spcl_schemes"
																	size="12" maxlength="10"
																	value="<%=mybean.so_offer_spcl_scheme%>"
																	onchange="SecurityCheck('txt_so_off_spcl_scheme',this,'hint_txt_so_off_spcl_scheme');"
																	onkeyup="toInteger('txt_so_off_spcl_scheme','Special Schemes');" />
																<div class="hint" id="hint_txt_so_off_spcl_scheme"></div>
														</div>
														
														<div class="row"></div>
														<div class="form-element6">
															<label>Loyalty Bonus: </label>
																<input name="txt_so_off_loyalty_bonus" type="text"
																	class="form-control" id="txt_so_off_loyalty_bonus"
																	size="12" maxlength="10"
																	value="<%=mybean.so_offer_loyalty_bonus%>"
																	onchange="SecurityCheck('txt_so_off_loyalty_bonus',this,'hint_txt_so_off_loyalty_bonus');"
																	onkeyup="toInteger('txt_so_off_loyalty_bonus','Loyalty Bonus');" />
																<div class="hint" id="hint_txt_so_off_loyalty_bonus"></div>
														</div>
														
														<div class="form-element6">
															<label>Govt Emp Scheme: </label>
																<input name="txt_so_off_govtempscheme" type="text"
																	class="form-control" id="txt_so_off_govtempscheme"
																	size="12" maxlength="10"
																	value="<%=mybean.so_offer_govtempscheme%>"
																	onchange="SecurityCheck('txt_so_off_govtempscheme',this,'hint_txt_so_off_govtempscheme');"
																	onkeyup="toInteger('txt_so_off_govtempscheme','Govt Emp Scheme');" />
																<div class="hint" id="hint_txt_so_off_govtempscheme"></div>
														</div>
														
														
														</div>
													</div>
												</div>
											</div>
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Finance</div>
												</div>
												<div class="portlet-body portlet-empty">
													<div class="container-fluid">
														<!-- START PORTLET BODY -->
														<br />
														<div class="row">
														<div class="form-element6">
															<label>Finance Type:</label>
																<select name="dr_so_fintype_id" id="dr_so_fintype_id"
																	class="form-control"
																	onchange="PopulateCheck('dr_so_fintype_id',this,'hint_dr_so_fintype_id');">
																	<%=mybean.PopulateFinanceType(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_so_fintype_id"></div>
														</div>

														<div class="form-element6">
															<label>Finance By:</label>
																<select name="dr_finance_by" class="form-control"
																	id="dr_finance_by"
																	onchange="PopulateCheck('dr_finance_by',this,'hint_dr_finance_by');">
																	<%=mybean.PopulateFinanceBy(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_finance_by"></div>

														</div>
														</div>
														<!--<div class="form-group"> -->
														<!--	<label class="control-label col-md-4 col-xs-12">Finance -->
														<!--		By:</label> -->
														<!--	<div class="col-md-6 col-xs-12"> -->
														<!--		<select name="dr_finance_by" class="form-control" -->
														<!--			id="dr_finance_by" -->
														<!--			onchange="PopulateCheck('dr_finance_by',this,'hint_dr_finance_by');"> -->
														<%--			<%=mybean.PopulateFinanceBy(mybean.comp_id)%> --%>
														<!--		</select> -->
														<!--		<div class="hint" id="hint_dr_finance_by"></div> -->

														<!--	</div> -->
														<!--</div> -->
														<div class="row">
														<div class="form-element6">
															<label>Finance Amount:</label>
																<input name="txt_so_finance_amt" type="text"
																	class="form-control" id="txt_so_finance_amt"
																	value="<%=mybean.so_finance_amt%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_finance_amt',this,'hint_txt_so_finance_amt');"
																	onkeyup="toInteger('txt_so_finance_amt','Finance Amount');" />
																<div class="hint" id="hint_txt_so_finance_amt"></div>

														</div>
														<div class="form-element6">
															<label>Gross Payout:</label>
																<input name="txt_so_finance_gross" type="text"
																	class="form-control" id="txt_so_finance_gross"
																	value="<%=mybean.so_finance_gross%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_finance_gross',this,'hint_txt_so_finance_gross');"
																	onkeyup="toInteger('txt_so_finance_gross','Finance Gross');" />
																<div class="hint" id="hint_txt_so_finance_gross"></div>
														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Net Payout (After Tax):</label>
																<input name="txt_so_finance_net" type="text"
																	class="form-control" id="txt_so_finance_net"
																	value="<%=mybean.so_finance_net%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_finance_net',this,'hint_txt_so_finance_net');"
																	onkeyup="toInteger('txt_so_finance_net','Finance Net');" />
																<div class="hint" id="hint_txt_so_finance_net"></div>
														</div>
														</div>
													</div>
												</div>
											</div>
											<!-- 											</center> -->
											<!-- 											<center> -->
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Accessories</div>
												</div>
												<div class="portlet-body portlet-empty">
													<div class="container-fluid">
														<!-- START PORTLET BODY -->
														<br />
														<div class="row">
														<div class="form-element6">
															<label>Accessories Amount:</label>
																<input name="txt_so_mga_amount" type="text"
																	class="form-control" id="txt_so_mga_amount"
																	value="<%=mybean.so_mga_amount%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_mga_amount',this,'hint_txt_so_mga_amount');"
																	onkeyup="toInteger('txt_so_mga_amount','Accesories Amount');" />
																<div class="hint" id="hint_txt_so_mga_amount"></div>

														</div>
														<div class="form-element6">
															<label>Accessories Paid:</label>
																<input name="txt_so_mga_paid" type="text"
																	class="form-control" id="txt_so_mga_paid"
																	value="<%=mybean.so_mga_paid%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_mga_paid',this,'hint_txt_so_mga_paid');"
																	onkeyup="toInteger('txt_so_mga_paid','Accesories Paid');" />
																<div class="hint" id="hint_txt_so_mga_paid"></div>

														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Accessories FOC Amount:</label>
																<input name="txt_so_mga_focamount" type="text"
																	class="form-control" id="txt_so_mga_focamount"
																	value="<%=mybean.so_mga_foc_amount%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_mga_focamount',this,'hint_txt_so_mga_focamount');"
																	onkeyup="toInteger('txt_so_mga_focamount','Accesories FOC Amount');" />
																<div class="hint" id="hint_txt_so_mga_focamount"></div>

														</div>
														</div>
													</div>
												</div>
											</div>
											<!-- 											</center> -->
											<!-- 											<center> -->
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Insurance</div>
												</div>
												<div class="portlet-body portlet-empty">
													<div class="container-fluid">
														<!-- START PORTLET BODY -->
														<br />
														<div class="row">
														<div class="form-element6">
															<label>Insurance Amount:</label>
																<input name="txt_so_insur_amount" type="text"
																	class="form-control" id="txt_so_insur_amount"
																	value="<%=mybean.so_insur_amount%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_insur_amount',this,'hint_txt_so_insur_amount');"
																	onkeyup="toInteger('txt_so_insur_amount','Insurance Amount');" />
																<div class="hint" id="hint_txt_so_insur_amount"></div>

														</div>
														<div class="form-element6">
															<label>Type: </label>
																<select id="dr_insurtype_id" name="dr_insurtype_id"
																	class="form-control"
																	onchange="PopulateCheck('dr_insurtype_id',this,'hint_txt_so_insur_type');">
																	<%=mybean.PopulateTypeOfInsur(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_txt_so_insur_type"></div>

														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Gross Payout:</label>
																<input name="txt_so_insur_gross" type="text"
																	class="form-control" id="txt_so_insur_gross"
																	value="<%=mybean.so_insur_gross%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_insur_gross',this,'hint_txt_so_insur_gross');"
																	onkeyup="toInteger('txt_so_insur_gross','Insurance Gross');" />
																<div class="hint" id="hint_txt_so_insur_gross"></div>
														</div>
														<div class="form-element6">
															<label>Net Payout (After Tax):</label>
																<input name="txt_so_insur_net" type="text"
																	class="form-control" id="txt_so_insur_net"
																	value="<%=mybean.so_insur_net%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_insur_net',this,'hint_txt_so_insur_net');"
																	onkeyup="toInteger('txt_so_insur_net','Insurance Net');" />
																<div class="hint" id="hint_txt_so_insur_net"></div>
														</div>
														</div>

													</div>
												</div>
											</div>
											<!-- 											</center> -->

											<!-- 											<center> -->
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Extended
														Warranty</div>
												</div>
												<div class="portlet-body portlet-empty">
													<div class="container-fluid">
														<!-- START PORTLET BODY -->
														<br />
														<div class="row">
														<div class="form-element6">
															<label>Extended Warranty Amount:</label>
																<input name="txt_so_ew_amount" type="text"
																	class="form-control" id="txt_so_ew_amount"
																	value="<%=mybean.so_ew_amount%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_ew_amount',this,'hint_txt_so_ew_amount');"
																	onkeyup="toInteger('txt_so_ew_amount','Extended Warrenty Amount');" />
																<div class="hint" id="hint_txt_so_ew_amount"></div>

														</div>
														<div class="form-element6">
															<label>Type: </label>
																<select id="dr_ew_type_id" name="dr_ew_type_id"
																	class="form-control"
																	onchange="PopulateCheck('dr_ew_type_id',this,'hint_txt_so_ew_type');">
																	<%=mybean.PopulateTypeOfExtWarranty(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_txt_so_ew_type"></div>

														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Payout: </label>
																<input name="txt_so_ew_payout" type="text"
																	class="form-control" id="txt_so_ew_payout"
																	value="<%=mybean.so_ew_payout%>" size="20"
																	maxlength="255"
																	onchange="SecurityCheck('txt_so_ew_payout',this,'hint_txt_so_ew_payout');"
																	onkeyup="toInteger('txt_so_ew_payout','Extended Warrenty Payout');" />
																<div class="hint" id="hint_txt_so_ew_payout"></div>

														</div>
														</div>


													</div>
												</div>
											</div>
											<!-- 											</center> -->
											<!-- 											<center> -->
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Exchange</div>
												</div>
												<div class="portlet-body portlet-empty">
													<div class="container-fluid">
														<!-- START PORTLET BODY -->
														<br />
														<div class="row">
														<div class="form-element6">
															<label>Exchange:</label>
																<input id="chk_so_exchange" type="checkbox"
																	name="chk_so_exchange"
																	<%=mybean.PopulateCheck(mybean.so_exchange)%>
																	onchange="SecurityCheck('chk_so_exchange',this,'hint_chk_so_exchange');" />
																<div class="hint" id="hint_chk_so_exchange"></div>

														</div>

														<div class="form-element6">
															<label>Exchange Amount:</label>
																<input name="txt_so_exchange_amount" type="text"
																	class="form-control" id="txt_so_exchange_amount"
																	size="12" maxlength="10"
																	value="<%=mybean.so_exchange_amount%>"
																	onchange="SecurityCheck('txt_so_exchange_amount',this,'hint_txt_so_exchange_amount');"
																	onkeyup="toInteger('txt_so_exchange_amount','Exchange Amount');" />
																<div class="hint" id="hint_txt_so_exchange_amount"></div>

														</div>
														</div>

													</div>
												</div>
											</div>
											<!-- 											</center> -->
											<!-- 											<center> -->
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Others</div>
												</div>
												<div class="portlet-body portlet-empty">
													<div class="container-fluid">
														<!-- START PORTLET BODY -->
														<br />
														<div class="row">
														<div class="form-element6">
															<label>Excess Refund:</label>
																<input name="txt_so_refund_amount" type="text"
																	class="form-control" id="txt_so_refund_amount"
																	size="12" maxlength="10"
																	value="<%=mybean.so_refund_amount%>"
																	onchange="SecurityCheck('txt_so_refund_amount',this,'hint_txt_so_refund_amount');"
																	onkeyup="toInteger('txt_so_refund_amount','Excess Refund');" />
																<div class="hint" id="hint_txt_so_refund_amount"></div>

														</div>
														<div class="form-element6">
															<label>Bank Refund:</label>
																<input name="txt_so_bankrefund_amount" type="text"
																	class="form-control" id="txt_so_bankrefund_amount"
																	size="12" maxlength="10"
																	value="<%=mybean.so_bankrefund_amount%>"
																	onchange="SecurityCheck('txt_so_bankrefund_amount',this,'hint_txt_so_bankrefund_amount');"
																	onkeyup="toInteger('txt_so_bankrefund_amount','Bank Refund');" />
																<div class="hint" id="hint_txt_so_bankrefund_amount"></div>

														</div>
														</div>
														<div class="row">
														<div class="form-element6">
															<label>Type of Sale:</label>
																<select id="dr_saletype_id" name="dr_saletype_id"
																	class="form-control"
																	onchange="PopulateCheck('dr_saletype_id',this,'hint_dr_saletype_id');">
																	<%=mybean.PopulateTypeOfSale(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_saletype_id"></div>

														</div>
														</div>

													</div>
												</div>
											</div>
											<!-- 											</center> -->

											<%-- <div class="form-element6">
												<label>Entry By: <%=mybean.so_entry_by%></label>
													<input type="hidden" name="so_entry_date"
														value="<%=mybean.so_entry_date%>">
											</div>
											<div class="form-element6">
												<label>Entry Date:  <%=mybean.so_entry_date%></label>
													<input type="hidden" name="so_entry_date"
														value="<%=mybean.so_entry_date%>">
												</div> --%>
											<%
												if (mybean.so_modified_by != null
														&& !mybean.so_modified_by.equals("")) {
											%>
											<div class="form-element6">
												<label>Modified By: <%=mybean.so_modified_by%></label>
													<input type="hidden" name="modified_by"
														value="<%=mybean.so_modified_by%>">
											</div>
											<div class="form-element6">
												<label>Modified Date: <%=mybean.so_modified_date%></label>
													<input type="hidden" name="so_modified_date"
														value="<%=mybean.so_modified_date%>">
											</div>

											<%
												}
											%>
										</form>
									</div>

									<div class="tab-pane" id="tabs-2"></div>
									<div class="tab-pane" id="tabs-3"></div>
									<div class="tab-pane" id="tabs-4"></div>
									<div class="tab-pane" id="tabs-5"></div>
									<div class="tab-pane" id="tabs-6"></div>
									<div class="tab-pane" id="tabs-10"></div>
									<div class="tab-pane" id="tabs-11"></div>

								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 	</div> -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp" %>
	<script>
// 		$(document).ready( function() {
// 			$('#txt_so_promise_date').datepicker().on( 'changeDate', function() {
// 				SecurityCheck('txt_so_promise_date', this, 'hint_txt_so_promise_date');
// 			});
							// 		$('#txt_so_retail_date').datepicker().on('changeDate', function(){
							// 			SecurityCheck('txt_so_retail_date', this, 'hint_txt_so_retail_date');
							// 		});
							// 		$('#txt_so_retail_date').focusout(function(){
							// 			var value = document.getElementById('txt_so_retail_date').value;
							// 			if(value == ''){
							// 				SecurityCheck('txt_so_retail_date', this, 'hint_txt_so_retail_date');
							// 			}
							// 		});
// 			$('#txt_so_payment_date').datepicker().on( 'changeDate', function() {
// 				SecurityCheck('txt_so_payment_date', this, 'hint_txt_so_payment_date');
// 			});
							// 		$('#txt_so_delivered_date').datepicker().on('changeDate', function(){
							// 			SecurityCheck('txt_so_delivered_date',this,'hint_txt_so_delivered_date');
							// 		});
							// 		$('#txt_so_delivered_date').focusout(function(){
							// 			var value = document.getElementById('txt_so_delivered_date').value;
							// 			if(value == ''){
							// 				SecurityCheck('txt_so_delivered_date', this, 'hint_txt_so_delivered_date');
							// 			}
							// 		});
// 			$('#txt_so_reg_date').datepicker().on( 'changeDate', function() {
// 				SecurityCheck('txt_so_reg_date', this, 'hint_txt_so_reg_date');
// 			});
// 			$('#txt_so_dob').datepicker().on( 'changeDate', function() {
// 				SecurityCheck('txt_so_dob', this, 'hint_txt_so_dob');
// 			});
// 			$('#txt_so_cancel_date').datepicker().on( 'changeDate', function() {
// 				CheckActive('cancel_date', this);
// 			});
// 			$('#txt_so_finstatus_disbursed_date') .datepicker() .on( 'changeDate', function() {
// 				FinStatusCheck( 'txt_so_finstatus_disbursed_date', this, 'hint_txt_so_finstatus_disbursed_date');
// 			});
// 			$('#txt_so_reg_hsrp_received_date') .datepicker() .on( 'changeDate', function() {
// 				SecurityCheck( 'txt_so_reg_hsrp_received_date', this, 'hint_txt_so_reg_hsrp_received_date');
// 			});
// 			$('#txt_so_reg_hsrp_install_date') .datepicker() .on( 'changeDate', function() {
// 				SecurityCheck( 'txt_so_reg_hsrp_install_date', this, 'hint_txt_so_reg_hsrp_install_date');
// 			});
// 			$('#txt_so_reg_perm_reg_date') .datepicker() .on( 'changeDate', function() {
// 				SecurityCheck( 'txt_so_reg_perm_reg_date', this, 'hint_txt_so_reg_perm_reg_date');
// 			});
// 			$('#txt_so_reg_rc_received_date') .datepicker() .on( 'changeDate', function() {
// 				SecurityCheck( 'txt_so_reg_rc_received_date', this, 'hint_txt_so_reg_rc_received_date');
// 			});
// 			$('#txt_so_finstatus_dob').datepicker().on( 'changeDate', function() {
// 				FinStatusCheck('txt_so_finstatus_dob', this, 'hint_txt_so_finstatus_dob');
// 			});
// 		});
	</script>

	<!-- START OF TAGS CONFIGURATION -->
	<script src="../assets/js/bootstrap-tagsinput.js" type="text/javascript"></script>
	<script type="text/javascript">
	
	//	THIS IS TO POPULATE VALUE IN TAG-CONTAINER AT THE TIME OF PAGE LOAD
		<%=mybean.tagcheck.PopulateTags(mybean.so_customer_id, mybean.comp_id)%>
		
	//	THIS IS TO ADD TAGS IN TAG-CONTAINER ON CLICK FROM POPOVER
		<%=mybean.tagcheck.PopulateTagsJS( mybean.so_customer_id, mybean.comp_id)%>
		
		function deleteTag(){
			$('#customer_tagclass > > input').tagsinput('remove', { 'value':  0, 'text': 'No Tag Selected' , 'continent': '#ff0000' });
		}
		
		function addNoTag(){
			$('#customer_tagclass > > input').tagsinput('add', { 'value':  0, 'text': 'No Tag Selected' , 'continent': '#ff0000' });
		}
		
		$(function() {
				$("#enquiry_tags").on('itemRemoved',function(){
					var url = "../customer/customer-tags-check.jsp?";
						
						var param = "update=yes&tags="+ $("#enquiry_tags").val()+"&id="+<%=mybean.so_customer_id%>+"&enquiry_id="+<%=mybean.so_enquiry_id%>;
						var hint = "hint_enquiry_tags";
						
						var param2 = "tags_content=yes&name=enquiry&id=" + <%=mybean.so_customer_id%>;
						var hint2 = "popover-content";
						
						setTimeout('showHint("'+ url + param2+'", "'+hint2+'")', 100);
						
						setTimeout('if($("#enquiry_tags").val()==""){ addNoTag(); }', 150);
						
						showHint(url + param, hint);
						
				});
	
			}); 
		    
			function tagcall(idname){
				
				$('#customer_tagclass > > input').tagsinput('remove', { 'value':  0, 'text': 'No Tag Selected' , 'continent': '#ff0000' });
				
				var url = "../customer/customer-tags-check.jsp?";
				
				var param1 = "add=yes&name=enquiry&tags="+idname +"&id="+<%=mybean.so_customer_id%>+"&enquiry_id="+<%=mybean.so_enquiry_id%>;
				var hint1 = "hint_enquiry_tags";
				
				var param2 = "tags_content=yes&id=" + <%=mybean.so_customer_id%>;
				var hint2 = "popover-content";
				
				setTimeout('showHint("'+url + param2+'", "'+hint2+'")', 100);
				
				setTimeout('addTag('+idname+')', 150);
				
				showHint(url + param1, hint1);
				setTimeout('deleteTag();', 200);
				
				
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

	<script type="text/javascript">
		function ActivateSO() {
			var activate = confirmactive(this);
			if (activate) {
				var so_id = document.getElementById("so_id").value;
				showHint('../sales/salesorder-cancel-check.jsp?so_id=' + so_id
						+ '&cancel_date=' + "" + '&cancel_reason=' + ""
						+ '&cin_status=' + "" + '&add_button=yes',
						'hint_chk_so_active');
				document.getElementById("so_cinstatus_id").innerHTML = "";
				document.getElementById("cancel_reason").innerHTML = "";
				document.getElementById("cancel_date").innerHTML = "";
			} else {
				if ($('#chk_so_active').is(":checked")) {
					$('#chk_so_active').prop('checked', false);
				} else {
					$('#chk_so_active').prop('checked', true);
				}
			}
		}
	</script>
	<script language="JavaScript" type="text/javascript">
		$(function() {
			$('#Hintclicktocall').on('shown.bs.modal', function() {
				$("#txt_so_cancel_date").datepicker({
					showButtonPanel : true,
					autoclose: true,
					dateFormat : "dd/mm/yy"
				});
				
				var so_id = document.getElementById("so_id").value;
			});
			$('#Hintclicktocall').on('hidden.bs.modal', function() {
				location.reload();
				var active = <%=mybean.so_active%>
				if(active==0){
					$('#chk_so_active').prop('checked',false);
					}else{
						$('#chk_so_active').prop('checked',true);
					}
			});
		});
	</script>
	<script language="JavaScript" type="text/javascript">
		function CancelSalesOrder(){
			var so_active = <%=mybean.so_active%>;
			var so_id = document.getElementById("so_id").value;
			if(so_active == '1'){
				var cancel_date = document.getElementById("txt_so_cancel_date").value;
				var cancel_reason = document.getElementById("dr_cancel_reason").value;
				var so_notes = document.getElementById("txt_so_notes").value;
				showHint('../sales/salesorder-cancel-check.jsp?so_id=' + so_id
						+ '&cancel_date=' + cancel_date
						+ '&cancel_reason=' + cancel_reason
						+ '&add_button=yes'
						+ '&so_notes=' + so_notes, 'cancelmsg');
			}else {
				showHint('../sales/salesorder-cancel-check.jsp?so_id=' + so_id
						+ '&cancel_date=' + ""
						+ '&cancel_reason=' + ""
						+ '&cin_status=' + ""
						+ '&add_button=yes', 'cancelmsg');
			}
			
			
		}
	</script>
	<script language="JavaScript" type="text/javascript">
		// 		$('<div>')
		function confirmactive(form) {
			var ans;
			ans = window.confirm('Activate Sales Order!');
			//alert (ans);
			if (ans == true) {
				return (true);
			} else {
				return (false);
			}
		}
	</script>


	<!-- END OF TAGS CONFIGURATION -->
</body>
</HTML>
