<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_SalesOrder_Dash"
	scope="request" />
<%-- <jsp:useBean id="mybeanaccount" --%>
<%-- 	class="axela.axelaauto_app.Oppr_Dash_Account" scope="request" /> --%>

<%
	mybean.doPost(request, response);
	// //	mybeanaccount.doPost(request, response);f
%>

<!DOCTYPE html>
<html lang="en">
<head>

<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">
	
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu-mobile.css" rel="stylesheet" 
media="screen" type="text/css" />
	

<script>
	function changedateformat(str){
		if(str.length==10)
			{
			var datePart=str.match(/\d+/g),
			year=datePart[0].substring(),
			month=datePart[1],
			day=datePart[2];
			return day+'/'+month+'/'+year;
			
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
		var so_id = GetReplacePost(document.form1.txt_so_id.value);
		var url = "../sales/salesorder-dash-check.jsp?";
		var param = "";
		var str = "123";

		value = GetReplacePost(obj.value);
		var stage_id;
		if (value == 0) {
			stage_id = 1;
		} else {
			stage_id = 2;
		}
		param = "name=" + name + "&value=" + value + "&so_id=" + so_id
				+ "&stage_id=" + stage_id;
		showHintPost(url + param, str, param, hint);
		setTimeout('RefreshHistory()', 1000);
	}

	function UpdateDOB() {
		var so_id = GetReplacePost(document.form1.txt_so_id.value);
		var day = document.getElementById("drop_bday").value;
		var month = document.getElementById("drop_bmonth").value;
		var year = document.getElementById("drop_byear").value;
		if (day == '-1') {
			//document.getElementById("hint_drop_dob").innerHTML = "<font color=red>Select Day for DOB!</font>";
			showToast("Select Day for DOB!");
		} else if (month == '-1') {
			//document.getElementById("hint_drop_dob").innerHTML = "<font color=red>Select Month for DOB!</font>";
			showToast("Select Month for DOB!");
		} else if (year == '-1') {
			//document.getElementById("hint_drop_dob").innerHTML = "<font color=red>Select Year for DOB!</font>";
			showToast("Select Year for DOB!");
		} else {
			showHintPost(
					"../sales/salesorder-dash-check.jsp?name=dr_contact_dob&so_id="
							+ so_id + "&drop_bday=" + day + "&drop_bmonth="
							+ month + "&drop_byear=" + year, '123',
					"name=dr_contact_dob&drop_bday=" + day + "&drop_bmonth="
							+ month + "&drop_byear=" + year, "hint_drop_dob");
			setTimeout('RefreshHistory()', 1000);
		}
	}

	function UpdateAnniversary() {
		var so_id = GetReplacePost(document.form1.txt_so_id.value);
		var day = document.getElementById("drop_aday").value;
		var month = document.getElementById("drop_amonth").value;
		var year = document.getElementById("drop_ayear").value;
		if (day == '-1') {
			//document.getElementById("hint_drop_anniversary").innerHTML = "<font color=red>Select Day for Anniversary!</font>";
			showToast("Select Day for DOB!");
		} else if (month == '-1') {
			//document.getElementById("hint_drop_anniversary").innerHTML = "<font color=red>Select Month for Anniversary!</font>";
			showToast("Select Month for DOB!");
		} else if (year == '-1') {
			//document.getElementById("hint_drop_anniversary").innerHTML = "<font color=red>Select Year for Anniversary!</font>";
			showToast("Select Year for DOB!");
		} else {
			showHintPost(
					"../sales/salesorder-dash-check.jsp?name=dr_contact_anniversary&so_id="
							+ so_id + "&drop_aday=" + day + "&drop_amonth="
							+ month + "&drop_ayear=" + year, '123',
					"name=dr_contact_anniversary&drop_aday=" + day
							+ "&drop_amonth=" + month + "&drop_ayear=" + year,
					"hint_drop_anniversary");
			setTimeout('RefreshHistory()', 1000);
		}
	}

	function SecurityCheck(name, obj, hint) {
		var value;
		var so_id = GetReplacePost(document.form1.txt_so_id.value);
		var item_id = document.getElementById("txt_item_id").value;
		var url = "../sales/salesorder-dash-check.jsp?";
		var param = "";
		var str = "123";
		
		 
	    if(name== "txt_so_payment_date" || name== "txt_so_promise_date"
	    		 || name=="txt_so_reg_date" || name== "txt_so_dob" || name== "txt_so_cancel_date")
		{
 			value = GetReplacePost(obj.value);
// 			value = changedateformat(value);

			
		
		}
	    else  if(name== "txt_so_retail_time"){
			value = document .getElementById("txt_so_retail_date").value;
			var temp = document .getElementById("txt_so_retail_time").value;
			name = "txt_so_retail_date";
			value =  value.concat(' ').concat(temp);
		 }
	    else  if(name== "txt_so_delivered_time"){
			value = document .getElementById("txt_so_delivered_date").value;
			var temp = document .getElementById("txt_so_delivered_time").value;
			name = "txt_so_delivered_date";
			value =  value.concat(' ').concat(temp);
		 }
		else if (name != "chk_so_reg_hsrp" && name != "chk_so_hni"
				&& name != "chk_so_open" && name != "chk_so_critical"
				&& name != "chk_so_active") {
			value = GetReplacePost(obj.value);
		}
		else {
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
		/* showHintPost(url + param, str, param, hint);
		//setTimeout('RefreshHistory()', 1000); */
		var strtoast = '';
		$.post(url, param, function(data, status) {
					if (status == 'success') {
						strtoast = data.trim().replace('<font color=\"red\">','').replace('</font>','');
						showToast(strtoast.trim());
					}
				})
	}

	//////////////////// eof security check /////////////////////
	function RefreshHistory() {
		var so_id = document.form1.txt_so_id.value;
	}

	function CheckActive(param, obj) {
		var active_value = document.getElementById("chk_so_active");
		var cancel_date = document.getElementById("txt_so_cancel_date").value;
		var cancel_reason = document.getElementById("dr_cancel_reason").value;
		if (active_value.checked == false && cancel_date == ''
				&& param == 'active') {
			showToast("Enter Cancel Date for Inactivating Sales Order!");
			document.getElementById("chk_so_active").checked = "checked";
		} else if (active_value.checked == false && cancel_reason == '0'
				&& param == 'active') {
			showToast("Select Cancel Reason for Inactivating Sales Order!");
			document.getElementById("chk_so_active").checked = "checked";
		} else {
			if (param == 'active') {
				SecurityCheck('chk_so_active', obj, 'hint_chk_so_active');
			} else if (param == 'cancel_date') {
				SecurityCheck('txt_so_cancel_date', obj,
						'hint_txt_so_cancel_date');
			} else if (param == 'cancel_reason') {
				SecurityCheck('dr_cancel_reason', obj, 'hint_dr_cancel_reason');
			}
		}
	}

	function LoadSODash(tab) {
		var so_id = document.getElementById("txt_so_id").value;
		var customer_id = document.getElementById("txt_customer_id").value;
		// 		if (tab == '4') {
		if (document.getElementById("collapse_3_2").innerHTML == "") {
			showHintPost(
					'../sales/salesorder-dash-check.jsp?customer_details=yes&customer_id='
							+ customer_id + '&so_id=' + so_id, so_id,
					'customer_details=yes&customer_id=' + customer_id + '&so_id='
							+ so_id, 'collapse_3_2');
		}
		// 		}
		// 		else if (tab == '5') {
		// 			showHintPost('salesorder-dash-check.jsp?doc_details=yes&so_id='
		// 					+ so_id, so_id, 'doc_details=yes&so_id=' + so_id, 'tabs-5');
		// 		} else if (tab == '6') {
		// 			if (document.getElementById("tabs-6").innerHTML == '') {
		// 				showHintPost(
		// 						'salesorder-dash-check.jsp?invoice_details=yes&so_id='
		// 								+ so_id, so_id, 'invoice_details=yes&so_id='
		// 								+ so_id, 'tabs-6');
		// 			}
		// 		} else if (tab == '7') {
		// 			if (document.getElementById("tabs-7").innerHTML == '') {
		// 				showHintPost(
		// 						'salesorder-dash-check.jsp?receipt_details=yes&so_id='
		// 								+ so_id, so_id, 'receipt_details=yes&so_id='
		// 								+ so_id, 'tabs-7');
		// 			}
		// 		} else if (tab == '8') {
		// 			if (document.getElementById("tabs-8").innerHTML == '') {
		// 				showHintPost('salesorder-dash-check.jsp?history=yes&so_id='
		// 						+ so_id, so_id, 'history=yes&so_id=' + so_id, 'tabs-8');
		// 			}
		// 		}
	}

	if (document.getElementById("collapse_3_2").innerHTML == "") {
		showHintPost(
				'..sales/salesorder-dash-check.jsp?customer_details=yes&customer_id='
						+ customer_id + '&so_id=' + so_id, so_id,
				'customer_details=yes&customer_id=' + customer_id + '&so_id='
						+ so_id, 'collapse_3_2');
	}
</script>

<style>
a {
	text-shadow: none;
	color: black;
}

b {
	color: #8E44AD;
}

strong {
	color: #fff;
}

center {
	color: #8E44AD;
	font-weight: bold;
}

span {
	color: red;
}
.header-wrap{
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
}
</style>
</head>

<body>
<div class="header-wrap">
		<div class="panel-heading" style="margin-bottom: 20px; background-color: #8E44AD; border: 1px solid transparent; border: 1px solid transparent; border-radius: 0px; box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05); ">
			<span class="panel-title">
				<center><strong>Sales Order Dashboard</strong></center>
			</span>
		</div>
	</div>

	<div class="col-md-12" style="margin-top: 40px;">
		<form role="form" class="form-horizontal" name="form1" id="form1"
			method="post">

			<input type="hidden" name="txt_customer_id" id="txt_customer_id"
				value="<%=mybean.so_customer_id%>" /> <input type="hidden"
				name="txt_so_id" id="txt_so_id" value="<%=mybean.so_id%>" />

			<div class="form-body">
				<div class="form-group">
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1"><b>SO ID: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="id" class="form-control"><%=mybean.so_id%></label> <input
								type="hidden" id="txt_item_id" name="txt_item_id"
								value="<%=mybean.item_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1"><b>SO Date: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="date" class="form-control"><%=mybean.sodate%></label>
							<input name="txt_so_date" type="hidden" class="form-control"
								id="txt_so_date" value="<%=mybean.so_date%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1"><b>Customer: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="customer" class="form-control"><%=mybean.link_customer_name%></label>
							<!-- 																		<input type="text" class="form-control" -->
							<%-- 																			value="<%=mybean.link_account_name%>"> --%>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1"><b>Contact: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="contact" class="form-control"><%=mybean.link_contact_name%></label>
						</div>
					</div>
					
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1"><b>Mobile: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="contact" class="form-control"><%=mybean.contact_mobile1%></label>
								<span style="position:absolute;left:160px; top:10px" onclick="callNo('<%=mybean.contact_mobile1%>')"> <img src="ifx/icon-call.png" class="img-responsive"></span>
						</div>
					</div>
					
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1"><b>Enquiry ID: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="mobile" class="form-control"><%=mybean.enquiry_link%></label>
							<input type="hidden" class="form-control"
								name="txt_oppr_opprtype_id" id="txt_oppr_opprtype_id"
								value="<%=mybean.enquiry_enquirytype_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1"><b>Quote ID: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="mobile" class="form-control"><%=mybean.quote_link%></label>
							<!-- 							<input type="hidden" class="form-control" -->
							<%-- 								value="<%=mybean.branch_name%>"> --%>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1"><b>Booking Amount: </b></label>
						<div class="col-md-8 col-xs-8">
							<label for="mobile" class="form-control"><%=mybean.so_booking_amount%></label>
							<!-- 							<input type="hidden" class="form-control" -->
							<%-- 								value="<%=mybean.branch_name%>"> --%>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>

	<div class="portlet light">
		<div class="portlet-body">
			<div class="panel-group accordion" id="accordion3">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled"
								data-toggle="collapse" data-parent="#accordion3"
								href="#collapse_3_1"><strong>SALES ORDER DETAILS</strong></a>
						</h4>
					</div>
					<div id="collapse_3_1" class="panel-collapse in">
						<div class="panel-body">
							<form role="form scrollable" class="form-horizontal"
								method="post" id="frm">
								<!--  						///////////////////////		SO Details   ////////////////////// -->
								<div class="form-body">
									<%-- <div class="row">

										<div class="col-md-3 col-xs-3">
											<div class="form-group form-md-line-input">
												<label for="form_control_1">
													<div class="hint" id="hint_drop_dob"></div>DOB<span>*</span>:
												</label> <select id="drop_bday" name="drop_bday"
													class="form-control" onchange="UpdateDOB();">
													<%=mybean.PopulateDay()%>
												</select>
											</div>
										</div>
										<div class="col-md-5 col-xs-5">
											<div class="form-group form-md-line-input">
												<label for="form_control_1">
													<div class="hint" id="hint_drop_dob"></div> &nbsp;
												</label> <select id="drop_bmonth" name="drop_bmonth"
													class="form-control" Placeholder="Month"
													onchange="UpdateDOB();">
													<%=mybean.PopulateMonth()%>
												</select>
											</div>
										</div>
										<div class="col-md-4 col-xs-4">
											<div class="form-group form-md-line-input">
												<label for="form_control_1">
													<div class="hint" id="hint_drop_dob"></div> &nbsp;
												</label> <select id="drop_byear" name="drop_byear"
													class="form-control" Placeholder="Year"
													onchange="UpdateDOB();">
													<%=mybean.PopulateYear()%>
												</select>
											</div>
										</div>
									</div> --%>

									<%-- <div class="row">
										<div class="col-md-3 col-xs-3">
											<div class="form-group form-md-line-input ">
												<label for="form_control_1">
													<div class="hint" id="hint_drop_anniversary"></div>Anniversary<span>*</span>:
												</label> <select id="drop_aday" name="drop_aday"
													class="form-control" onchange="UpdateAnniversary();">
													<%=mybean.PopulateDays()%>
												</select>
											</div>
										</div>
										<div class="col-md-5 col-xs-5">
											<div class="form-group form-md-line-input">
												<label for="form_control_1">
													<div class="hint" id="hint_drop_anniversary"></div> &nbsp;
												</label> <select id="drop_amonth" name="drop_amonth"
													class="form-control" Placeholder="Month"
													onchange="UpdateAnniversary();">
													<%=mybean.PopulateMonths()%>
												</select>
											</div>
										</div>
										<div class="col-md-4 col-xs-4">
											<div class="form-group form-md-line-input">
												<label for="form_control_1">
													<div class="hint" id="hint_drop_anniversary"></div> &nbsp;
												</label> <select id="drop_ayear" name="drop_ayear"
													class="form-control" Placeholder="Year"
													onchange="UpdateAnniversary();">
													<%=mybean.PopulateYears()%>
												</select>
											</div>
										</div>
									</div> --%>

<%-- 									<% --%>
<!-- // 										if (mybean.enquiry_enquirytype_id.equals("1")) { -->
<%-- 									%> --%>
<%-- 									<% --%>
<!-- // 										if (!mybean.so_vehstock_id.equals("0")) { -->
<%-- 									%> --%>
<!-- 									<div class="form-group form-md-line-input"> -->
<!-- 										<label class="col-md-1 col-xs-1 control-label" -->
<!-- 											for="form_control_1">Stock Comm. No.: </label> -->
<!-- 										<div class="col-md-6 col-xs-6"> -->
<%-- 											<label for="stockno" class="form-control"><%=mybean.stock_comm_no%></label> --%>
											<!-- 							<input type="text" id="txt_so_date" name="txt_so_date" -->
											<%-- 								class="form-control" value="<%=mybean.so_date%>"> --%>
<!-- 										</div> -->
<!-- 									</div> -->

<%-- 									<% --%>
<!-- // 										} -->
<%-- 									%> --%>
<%-- 									<% --%>
<!-- // 										} else if (mybean.enquiry_enquirytype_id.equals("2")) { -->
<%-- 									%> --%>
									
<!-- 									<div class="form-group form-md-line-input"> -->
<!-- 										<label for="form_control_1"><div class="hint" -->
<!-- 												id="hint_txt_so_preownedvehstock_id"></div>Pre-Owned Stock ID: -->
<!-- 										</label> <input name="txt_so_preownedvehstock_id" type="text" -->
<!-- 											class="form-control" id="txt_so_preownedvehstock_id" -->
<%-- 											value="<%=mybean.so_preownedvehstock_id%>" size="10" --%>
<!-- 											maxlength="10" -->
<!-- 											onchange="SecurityCheck('txt_so_preownedvehstock_id',this,'hint_txt_so_preownedvehstock_id');" /> -->

<!-- 									</div> -->
<%-- 									<% --%>
<!-- // 										} -->
<%-- 									%> --%>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_vehstock_id"></div> Stock ID:
											
										</label> <input name="txt_vehstock_id" type="text"
											class="form-control" id="txt_vehstock_id"
											value="<%=mybean.so_vehstock_id%>" size="20" maxlength="255"
											onchange="SecurityCheck('txt_vehstock_id',this,'hint_txt_vehstock_id');"
											onkeyup="toInteger(this.id);" />

									</div>
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_option_id"></div>Colour:
										</label> <select name="dr_option_id" class="form-control"
											id="dr_option_id"
											onchange="PopulateCheck('dr_option_id',this,'hint_dr_option_id');">
											<%=mybean.PopulateColour(mybean.comp_id)%>
										</select>
									</div>
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_allot_no"></div> Allotment No.:
											
										</label> <input name="txt_so_allot_no" type="text"
											class="form-control" id="txt_so_allot_no"
											value="<%=mybean.so_allot_no%>" size="20" maxlength="255"
											onchange="SecurityCheck('txt_so_allot_no',this,'hint_txt_so_allot_no');"
											onkeyup="toInteger(this.id);" />

									</div>
									
<!-- 									<div class="form-group form-md-line-input"> -->
<!-- 										<label for="form_control_1"> -->
<!-- 											<div class="hint" id="hint_txt_so_booking_amount"></div> Booking Amount: -->
<!-- 										</label> <input name="txt_so_booking_amount" type="text" -->
<!-- 											class="form-control" id="txt_so_booking_amount" size="12" -->
<%-- 											maxlength="10" value="<%=mybean.so_booking_amount%>" --%>
<!-- 											onchange="SecurityCheck('txt_so_booking_amount',this,'hint_txt_so_booking_amount');" -->
<!-- 											onkeyup="toInteger('txt_so_booking_amount','Booking Amount');" /> -->

<!-- 									</div> -->
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_payment_date"></div>
											Payment Date<span>*</span>:
										</label> <input name="txt_so_payment_date" type="text"
											class="form-control" id="txt_so_payment_date"
											value="<%=mybean.so_paymentdate%>" size="12" maxlength="10"
											onfocusout="SecurityCheck('txt_so_payment_date',this,'hint_txt_so_payment_date');"
											onclick="datePicker('txt_so_payment_date');"  readonly/>

									</div>
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_promise_date"></div>
											Tentative Delivery Date<span>*</span>:
										</label> <input name="txt_so_promise_date" type="text"
											class="form-control" id="txt_so_promise_date"
											value="<%=mybean.so_promisedate%>" size="12" maxlength="10"
											onfocusout="SecurityCheck('txt_so_promise_date',this,'hint_txt_so_promise_date');"
											onclick="datePicker('txt_so_promise_date');"  readonly/>

									</div>
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_retail_date"></div>
											Retail Date:
										</label> <input name="txt_so_retail_date" type="text"
											class="form-control" id="txt_so_retail_date"
											value="<%=mybean.so_retaildate%>" size="12" maxlength="10"
											onclick="datePicker('txt_so_retail_date');"  readonly />
<!-- 											onfocusout="SecurityCheck('txt_so_retail_date',this,'hint_txt_so_retail_date');" -->
											

									</div>
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1" >Retail Time:
										</label> <input type="" class="form-control"
											name="txt_so_retail_time" id="txt_so_retail_time" value="<%=mybean.so_retailtime%>""
											onclick="timePicker('txt_so_retail_time')"
											onfocusout="SecurityCheck('txt_so_retail_time',this,'txt_so_retail_time');" readonly>
									</div>
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_delivered_date"></div>
											Delivered Date:
										</label>
										<%if (mybean.so_delivereddate.equals("")) {%>
										<input name="txt_so_delivered_date" type="text"
											class="form-control" id="txt_so_delivered_date" onclick="datePicker('txt_so_delivered_date');" 
											value="<%=mybean.so_delivereddate%>"  size="12" maxlength="10" readonly/>
											<div class="form-group form-md-line-input">
										<label for="form_control_1" style="margin-left:5px">Delivered Time:
										</label> <input type="" class="form-control"
											name="txt_so_delivered_time" id="txt_so_delivered_time" value="<%=mybean.so_deliveredtime%>""
											onfocusout="SecurityCheck('txt_so_delivered_time',this,'txt_so_delivered_time');" 
											onclick="timePicker('txt_so_delivered_time')" readonly>
									</div>
										<% } else if(!mybean.emp_id.equals("1")){ %>
                                            <%=mybean.so_delivereddate%><br><br>	
                                            <label for="form_control_1">
											<div class="hint" id="hint_txt_so_delivered_time"></div>
											Delivered Time:
										</label>
                                             <%=mybean.so_deliveredtime%>	
                                            <input name="txt_so_delivered_date123" type="hidden" class="form-control" id ="txt_so_delivered_date123" value = "<%=mybean.so_delivereddate%>" />
                                             <input name="txt_so_delivered_time123" type="hidden" class="form-control" id ="txt_so_delivered_time123" value = "<%=mybean.so_deliveredtime%>" />
                                            <%} else {%>
                                            <input name="txt_so_delivered_date" type="text"
											class="form-control" id="txt_so_delivered_date" onclick="datePicker('txt_so_delivered_date');" 
											value="<%=mybean.so_delivereddate%>"  size="12" maxlength="10" readonly/>
											<div class="form-group form-md-line-input">
										<label  style="margin-left:5px" for="form_control_1" >Delivered Time:
										</label> <input type="" class="form-control"
											name="txt_so_delivered_time" id="txt_so_delivered_time" value="<%=mybean.so_deliveredtime%>"
											onfocusout="SecurityCheck('txt_so_delivered_time',this,'txt_so_delivered_time');" 
											onclick="timePicker('txt_so_delivered_time')" readonly>
									</div>
											<%} %>
											<div onchange="SecurityCheck('txt_so_delivered_date',this,'hint_txt_so_delivered_date');"> </div>

									</div>
									
<%-- 									<%if (mybean.so_delivereddate.equals("")) {%> --%>
<!-- 									<div class="form-group form-md-line-input"> -->
<!-- 										<label for="form_control_1" -->
<%-- 											value="<%=mybean.so_deliveredtime%>">Delivered Time<span>*</span>: --%>
<!-- 										</label> <input type="" class="form-control" -->
<%-- 											name="txt_so_delivered_time" id="txt_so_delivered_time" value="<%=mybean.so_deliveredtime%>" --%>
<!-- 											onfocusout="SecurityCheck('txt_so_delivered_time',this,'txt_so_delivered_time');"  -->
<!-- 											onclick="timePicker('txt_so_delivered_time')" readonly> -->
<!-- 									</div> -->
<%-- 									<%} %> --%>
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_reg_no"></div>Registration No.:
										</label> <input name="txt_so_reg_no" type="text"
											class="form-control" id="txt_so_reg_no"
											value="<%=mybean.so_reg_no%>" size="32"
											maxlength="255"
											onchange="SecurityCheck('txt_so_reg_no',this,'hint_txt_so_reg_no');" />

									</div>
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_reg_date"></div>
											Registration Date:
										</label> <input name="txt_so_reg_date" type="text"
											class="form-control" id="txt_so_reg_date"
											value="<%=mybean.so_reg_date%>" size="12" maxlength="10"
											onfocusout="SecurityCheck('txt_so_reg_date',this,'hint_txt_so_reg_date');"
											onclick="datePicker('txt_so_reg_date');" readonly  />

									</div>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_dob"></div>
											DOB:
										</label> <input name="txt_so_dob" type="text"
											class="form-control" id="txt_so_dob"
											value="<%=mybean.so_dob%>" size="12" maxlength="10"
											onfocusout="SecurityCheck('txt_so_dob',this,'hint_txt_so_dob');"
											onclick="datePicker('txt_so_dob');" readonly />

									</div>
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_txt_so_pan"></div>Pan No<span>*</span>: </label> <input
											name="txt_so_pan" type="text" class="form-control"
											id="txt_so_pan" value="<%=mybean.so_pan%>" size="10"
											maxlength="10"
											onchange="SecurityCheck('txt_so_pan',this,'hint_txt_so_pan');" />

									</div>

									<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_hypothecation"></div>Hypothecation:
										</label> <input name="txt_so_hypothecation" type="text"
											class="form-control" id="txt_so_hypothecation"
											value="<%=mybean.so_hypothecation%>" size="32"
											maxlength="255"
											onchange="SecurityCheck('txt_so_hypothecation',this,'hint_txt_so_hypothecation');" />

									</div> --%>

									<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_finance_by"></div>Finance By:
										</label> <select name="dr_finance_by" class="form-control"
											id="dr_finance_by"
											onchange="PopulateCheck('dr_finance_by',this,'hint_dr_finance_by');">
											<%=mybean.PopulateFinanceBy()%>
										</select>
									</div> --%>
									<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_finance_amt"></div> Finance
											Amount:
										</label> <input name="txt_so_finance_amt" type="text"
											class="form-control" id="txt_so_finance_amt"
											value="<%=mybean.so_finance_amt%>" size="20" maxlength="255"
											onchange="SecurityCheck('txt_so_finance_amt',this,'hint_txt_so_finance_amt');"
											onkeyup="toInteger('txt_so_finance_amt','Finance Amount');" />

									</div> --%>

									<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_dr_insurance_by"></div> Insurance
											By:
										</label> <select name="dr_insurance_by" class="form-control"
											id="dr_insurance_by"
											onchange="PopulateCheck('dr_insurance_by',this,'hint_dr_insurance_by')">
											<%=mybean.PopulateInsuranceBy()%></select>
									</div> --%>
									<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_insurance_amt"></div>
											Insurance Amount:
										</label> <input name="txt_so_insurance_amt" type="text"
											class="form-control" id="txt_so_insurance_amt"
											value="<%=mybean.so_insurance_amt%>" size="15" maxlength="11"
											onchange="SecurityCheck('txt_so_insurance_amt',this,'hint_txt_so_insurance_amt');"
											onkeyup="toInteger('txt_so_insurance_amt','Insurance Amount');" />

									</div> --%>
									<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_filestatus"></div> File
											Status:
										</label> <input name="txt_so_filestatus" type="text"
											class="form-control" id="txt_so_filestatus"
											value="<%=mybean.so_file_status%>" size="20" maxlength="255"
											onchange="SecurityCheck('txt_so_filestatus',this,'hint_txt_so_filestatus')" />

									</div> -----%>
									
									
									
									

									<div class="form-group form-md-line-input">
													<span style="position: relative; color: #8E44AD;">Sales Order Open:&nbsp&nbsp
													<input id="chk_so_open" type="checkbox" name="chk_so_open"
													<%=mybean.PopulateCheck(mybean.so_open)%> style="position: absolute; bottom:2px"
													onchange="SecurityCheck('chk_so_open',this,'hint_chk_so_open');" class="icheck" />
												<div class="hint" id="hint_chk_so_open"></div></span>
											
									</div>

									<%-- <div class="form-group form-md-line-input">
										<div class="checkbox-list">
											<div class="col-md-6 col-xs-6">
												<input id="chk_so_critical" type="checkbox"
													name="chk_so_critical"
													<%=mybean.PopulateCheck(mybean.so_critical)%>
													onchange="SecurityCheck('chk_so_critical',this,'hint_chk_so_critical');" />
												<label style="position: relative; bottom: 2px;">Sales Order Critical</label>
												<div class="hint" id="hint_chk_so_critical"></div>
											</div>
										</div>
									</div> --%>

									<%-- <div class="form-group form-md-line-input">
										<div class="checkbox-list">
											<div class="col-md-6 col-xs-6">
												<input id="chk_so_hni" type="checkbox" name="chk_so_hni"
													<%=mybean.PopulateCheck(mybean.so_hni)%>
													onchange="SecurityCheck('chk_so_hni',this,'hint_chk_so_hni');" />
													<label style="position: relative; bottom: 2px;">HNI</label><div class="hint" id="hint_chk_so_hni">
											</div>
										</div>									
									</div> --%>	

									<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_special_remarks"></div>
											Special Remarks:
										</label>
										<textarea name="txt_so_special_remarks" rows="2"
											class="form-control" id="txt_so_special_remarks"
											onchange="SecurityCheck('txt_so_special_remarks',this,'hint_txt_so_special_remarks')"><%=mybean.so_din_accessories_special_remarks%></textarea>

									</div> --%>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_refno"></div> Sales Order
											Reference No.<span>*</span>:
										</label> <input name="txt_so_refno" type="text" class="form-control"
											id="txt_so_refno" value="<%=mybean.so_refno%>" size="12"
											maxlength="10"
											onchange="SecurityCheck('txt_so_refno',this,'hint_txt_so_refno');"
											onkeyup="toInteger('txt_so_refno','Ref No.');" />
									</div>
<!-- 									<div class="form-group form-md-line-input"> -->
<!-- 										<label for="form_control_1"> -->
<!-- 											<div class="hint" id="hint_txt_so_cancel_date"></div> Cancel -->
<!-- 											Date: -->
<!-- 										</label> <input name="txt_so_cancel_date" type="text" -->
<!-- 											class="form-control" id="txt_so_cancel_date" -->
<%-- 											value="<%=mybean.so_canceldate%>" size="12" maxlength="10" --%>
<!-- 											onfocusout="CheckActive('cancel_date', this);" -->
<!-- 											onclick="datePicker('txt_so_cancel_date');" readonly  /> -->
<!-- 									</div> -->

									<div class="form-group form-md-line-input">
										<label for="form_control">Cancel Date: </label>
											<input name="txt_so_cancel_date" type="text" 
											class="form-control" id="txt_so_cancel_date"
											value="<%=mybean.so_canceldate%>" size="12" maxlength="10" readonly />
									</div>
									
<!-- 									<div class="form-group form-md-line-input"> -->
<!-- 										<label for="form_control_1"> -->
<!-- 											<div class="hint" id="hint_dr_cancel_reason"></div>Cancel -->
<!-- 											Reason: -->
<!-- 										</label> <select name="dr_cancel_reason" class="form-control" -->
<!-- 											id="dr_cancel_reason" -->
<!-- 											onchange="CheckActive('cancel_reason', this);"> -->
<%-- 											<%=mybean.PopulateCancelReason()%> --%>
<!-- 										</select> -->
<!-- 									</div> -->
									<div class="form-group form-md-line-input">
										<label for="form_control">Cancel Reason: </label>
											<input class="form-control" type="text" value="<%=mybean.cancelreason_name%>" readonly>
									</div>
									
									<div class="form-group form-md-line-input">
										<label for="form_control">CIN Status: </label>
															<input class="form-control" type="text" value="<%=mybean.cinstatus_name%>" readonly>
									</div>
									
<!-- 									<div class="form-group form-md-line-input"> -->
<!-- 												<input id="chk_so_active" type="checkbox" -->
<!-- 													name="chk_so_active" style="position: absolute; bottom:2px;" -->
<%-- 													<%=mybean.PopulateCheck(mybean.so_active)%> --%>
<!-- 													onchange="CheckActive('active', this);" /> -->
<!-- 										<span -->
<!-- 											style="position: relative; left: 20px; color: #8E44AD;">											 -->
<!-- 											Active<div class="hint" id="hint_chk_so_active"></div> -->
<!-- 										</span> -->

<!-- 									</div> -->
									<div class="form-group form-md-line-input">
										<%if(mybean.so_active.equals("1")){ %>
										<label for="form_control">Active</label>
										<%}else if(mybean.so_active.equals("0")){ %>
										<label for="form_control">In-Active</label>
										<%} %>
									</div>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">
											<div class="hint" id="hint_txt_so_notes"></div> Notes:
										</label>
										<textarea name="txt_so_notes" cols="2" rows="2"
											class="form-control" id="txt_so_notes"
											onchange="SecurityCheck('txt_so_notes',this,'hint_txt_so_notes')"><%=mybean.so_notes%></textarea>

									</div>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">Entre By:</label><%=mybean.so_entry_by%>
										<input type="hidden" class="form-control" name="entry_by"
											value="<%=mybean.so_entry_by%>">
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">Entry Date:</label><%=mybean.so_entry_date%>
										<input type="hidden" class="form-control" name="entry_date"
											value="<%=mybean.so_entry_date%>">
									</div>
									<%
										if (mybean.so_modified_by != null
												&& !mybean.so_modified_by.equals("")) {
									%>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">Modified by:</label> <%=mybean.so_modified_by%>
										<input type="hidden" class="form-control" name="modified_by"
											value="<%=mybean.so_modified_by%>">
									</div>
									<div class="form-group form-md-line-input">
										<label for="form_control_1">Modified Date:</label> <input
											type="text" class="form-control" name="so_modified_date"
											value="<%=mybean.so_modified_date%>">
									</div>
									<%
										}
									%>

								</div>
							</form>
						</div>
					</div>
				</div>



				<!-- 						///////////////////////// Account Details ///////////////////// -->


				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion3"
								href="#collapse_3_2"><strong>CUSTOMER</strong></a>
						</h4>
					</div>
					<div id="collapse_3_2" class="panel-collapse collapse">
						<div class="panel-body">
							<center><%=mybean.customerDetails(response, mybean.so_customer_id, "")%></center>
							<input type="hidden" name="txt_customer_id" id="txt_customer_id"
								value="<%=mybean.so_customer_id%>" /> <input type="hidden"
								name="txt_so_id" id="txt_so_id" value="<%=mybean.so_id%>" />

						
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	
	<script src="js/dynacheck-post.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>

</body>
<!-- END BODY -->
</html>