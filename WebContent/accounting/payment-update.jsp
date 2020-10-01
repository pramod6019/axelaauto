<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Payment_Update" scope="request" />
<% mybean.doPost(request, response); %>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body <%if (mybean.status.equals("Add")) {%>
	onload="Displaypaymode();CalPartyBal(0);" <%} else {%>
	onload="Displaypaymode();CalPartyBal(1);" <%}%> class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>
							Payment
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
						<li><a href="../accounting/voucher-list.jsp?all=yes&vouchertype_id= <%=mybean.vouchertype_id%>&voucherclass_id= <%=mybean.voucherclass_id%>">List <%=mybean.vouchertype_name%>s </a> &gt;</li>
						<li><a href="payment-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Payment</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>
										Payment
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="formemp" method="post" class="form-horizontal">
											<center>
												Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.
											</center>
											
											<input name="txt_vouchertype_id" type="hidden" id="txt_vouchertype_id" value="<%=mybean.vouchertype_id%>" />
											<input name="hid_voucher_no" type="hidden" id="hid_voucher_no" value="<%=mybean.voucher_no%>">
											<input type="hidden" name="txt_vouchertype_authorize" id="txt_vouchertype_authorize" value="<%=mybean.vouchertype_authorize %>" />
											<input type="hidden" name="txt_vouchertype_defaultauthorize" id="txt_vouchertype_defaultauthorize" value="<%=mybean.vouchertype_defaultauthorize %>" />
											<input type="hidden" name="txt_voucher_authorize" id="txt_voucher_authorize" value="<%=mybean.voucher_authorize %>" />
											<input type="hidden" name="txt_voucher_authorize_id" id="txt_voucher_authorize_id" value="<%=mybean.voucher_authorize_id %>" />
											<input type="hidden" name="txt_voucher_authorize_time" id="txt_voucher_authorize_time" value="<%=mybean.voucher_authorize_time %>" />
											<input type="hidden" name="txt_vouchertype_tds" id="txt_vouchertype_tds" value="<%=mybean.vouchertype_tds %>" />
											<input type="hidden" name="txt_vouchertype_tds_ledger_id" id="txt_vouchertype_tds_ledger_id" value="<%=mybean.vouchertype_tds_ledger_id %>" />
												
											<div class="form-element6">
													<label>
														Date<font color="#ff0000">*</font>: </label>
														<input type="hidden" id="txt_session_id"
															name="txt_session_id" value="<%=mybean.session_id%>">
															<%
																if (mybean.empEditperm.equals("1")) {
															%> <input name="txt_voucher_date" type="text"
															class="form-control datepicker" id="txt_voucher_date"
															value="<%=mybean.voucherdate%>" size="11" maxlength="10" />
															<%
																} else {
															%> <%=mybean.voucherdate%> <input
															name="txt_hid_voucher_date" type="hidden"
															class="form-control datepicker"
															id="txt_hid_voucher_date" value="<%=mybean.voucherdate%>">
																<% } %>
												</div>
											<div class="form-element6">
													<label>
														Branch<font color=red>*</font>:
													</label>
														<select name="dr_branch" id="dr_branch" class="form-control">
															<%=mybean.PopulateBranch(mybean.voucher_branch_id, "", "", "", request)%>
														</select> 
											</div>
											<div class="form-element6">
													<label> Party Ledger<font color="#ff0000">*</font>: </label>
														<select class="form-control select2" id="ledger" name="ledger">
															<%=mybean.ledgercheck.PopulateLedgers("0", mybean.voucher_customer_id, mybean.comp_id)%>
														</select>
														<span><a href="ledger-update.jsp?add=yes" target='_blank'>Add Ledger...</a></span>
														<div id="main_customer_cur_bal">
															<%=mybean.ReturnCustomerCurrBalance( mybean.voucher_customer_id, mybean.comp_id, mybean.vouchertype_id)%>
														</div>
											</div>
											<div class="form-element6">
													<label> Contact<font color="#ff0000">*</font>: </label>
<!-- 														<select name="dr_contact_id" class="form-control" -->
<!-- 															id="dr_contact_id"> -->
																<span id="dr_contact_id">
															<%=mybean.PopulateContact(mybean.voucher_customer_id)%>
															</span>
<!-- 														</select> -->
											</div>
											<div class="row"></div>
											<div class="form-element6">
													<label>
														Bank Ledger<font color="#ff0000">*</font>:
													</label>
														<select name="dr_voucher_payment" id="dr_voucher_payment"
															value="<%=mybean.vouchertrans_paymode_id%>"
															class="form-control" onChange="Displaypaymode();">
															<%=mybean.PopulatePayment()%>
														</select>
											</div>
											<div class="form-element6" id="by">
													<label> By<font color="#ff0000">*</font>: </label>
														<select name="dr_voucher_paymode" id="dr_voucher_paymode"
															class="form-control" onChange="Displaypaymode(1)"><%=mybean.PopulatePaymode()%>
														</select>
											</div>
											<div class="form-element6">
													<label>Amount<font color="#ff0000">*</font>: </label>
														<input type="text" id="txt_main_party_amount"
															name="txt_main_party_amount" class="form-control"
															value="<%=mybean.mainparty_amount%>" size="12"
															maxlength="10" onKeyUp="toNumber('txt_main_party_amount','Amount');
															 <%if(mybean.vouchertype_tds.equals("1")) {%>
															CalTDSAmountToPer();
															<%}%>"/>
														<div class="form-element3">
															<font color='red'><b>Balance: <span
																	id="remaining_party_bal"></span></font></b>
														</div>
														<input type="hidden" id="hi_remaining_party_bal"
															name="hi_remaining_party_bal" value="" /> <input
															type="hidden" id="hi_remaining_party_bal1"
															name="hi_remaining_party_bal1" value="" />
													</div>
													
													<%if(mybean.vouchertype_tds.equals("1")) {%>
													<div class="form-element6">
													<label><b>Payable Amount:</label>
													<input id='payable_amount' class='form-control' readonly='readonly' type='text' value='<%= Double.parseDouble(mybean.mainparty_amount)-Double.parseDouble(mybean.voucher_tds_amount)%>' />
													</div>
													<div class="row"></div>
													<div class="form-element6">
													<label>TDS %:</label>
														<input type="text" id="txt_tds_per"
															name="txt_tds_per" onKeyup='CalTDSPerToAmount();' class="form-control"
															value="<%=mybean.voucher_tdsper%>" size="12"
															maxlength="10" onKeyUp="toNumber('txt_tds_per','TDS %')"/>
													</div>
													<div class="form-element6">
													<label>TDS Amount:</label>
														<input type="text" id="txt_tds_amount"
															name="txt_tds_amount" onKeyup='CalTDSAmountToPer();' class="form-control"
															value="<%=mybean.voucher_tds_amount%>" size="12"
															maxlength="10" onKeyUp="toNumber('txt_tds_amount','TDS Amount')"/>
													</div>
													<%}else{%>
													<input type="hidden" id="txt_tds_per"
															name="txt_tds_per" onKeyup='CalTDSPerToAmount();' class="form-control"
															value="0" size="12"
															maxlength="10" onKeyUp="toNumber('txt_tds_per','TDS %')"/>
															
													<input type="hidden" id="txt_tds_amount"
															name="txt_tds_amount" onKeyup='CalTDSAmountToPer();' class="form-control"
															value="0" size="12"
															maxlength="10" onKeyUp="toNumber('txt_tds_amount','TDS Amount')"/>
													<%}%>
													
											<div class="form-element6" id="chequeno">
													<label>
														Cheque No<font color="#ff0000">*</font>:
													</label>
														<input name="txt_vouchertrans_cheque_no" type="text"
															id="txt_vouchertrans_cheque_no"
															onKeyUp="toInteger('txt_vouchertrans_cheque_no')"
															class="form-control"
															value="<%=mybean.vouchertrans_cheque_no%>" size="12"
															maxlength="6" />
											</div>
											
											<div class="form-element6" id="chequedate">
														<label>Cheque Date<font
															color="#ff0000">*</font>:
														</label>
															<input name="txt_vouchertrans_cheque_date" type="text"
																class="form-control datepicker" id="txt_vouchertrans_cheque_date"
																value="<%=mybean.vouchertrans_chequedate%>" size="12"
																maxlength="10" />
												</div>
											
											<div class="form-element6" id="cardno">
															<label>Card No<font color="#ff0000">*</font>: </label>
																<input name="txt_vouchertrans_card_no" type="text"
																	id="txt_vouchertrans_card_no"
																	onKeyUp="toInteger('txt_vouchertrans_card_no')"
																	class="form-control"
																	value="<%=mybean.vouchertrans_cheque_no%>" size="12"
																	maxlength="16" />
												</div>
												
												<div class="form-element6" id="txnno">
															<label>Transaction No<font color="#ff0000">*</font>: </label>
																<input name="txt_vouchertrans_txn_no" type="text"
																	id="txt_vouchertrans_txn_no"
																	class="form-control"
																	value="<%=mybean.vouchertrans_transaction_no%>" size="12"
																	maxlength="30" />
												</div>
												<div class="row"></div>
<!-- 											<div class="portlet box"> -->
<!-- 												<div class="portlet-title" style="text-align: center"> -->
<!-- 													<div class="caption" style="float: none">Mutiple Entry's</div> -->
<!-- 												</div> -->
<!-- 												<br> -->
<!-- 												<div class="portlet-body portlet-empty"> -->
									             <div class="container-fluid">
<!-- 													<div class="form-element6"> -->
<!-- 														<label> -->
<!-- 															Multi Entry Ledger: </label> -->
<!-- 															<select class="form-control select2" id="ledger1" -->
<!-- 																name="ledger1"> -->
<!-- 															</select> -->
<!-- 															<div id="multi_customer_cur_bal"></div> -->
<!-- 													</div> -->
<!-- 													<div class="form-element5"> -->
<!-- 														<label> Multi Entry Amount: </label> -->
<!-- 															<input type="text" id="txt_multi_party_amount" -->
<!-- 																name="txt_multi_party_amount" class="form-control" -->
<!-- 																size="12" maxlength="10" onKeyUp="toNumber('txt_multi_party_amount','Amount')"/> -->
<!-- 															</div> -->
<!-- 															<div class="form-element1" style="margin-top:10px"><input type="button" id="add_customer" -->
<!-- 																name="add_customer" class="btn btn-success" -->
<!-- 																value="Add Entry" onclick="addmultipleparty();"> -->
<!-- 													</div> -->
												<div class="form-element6" id="customers">
															<label> </label>
																	<% if (mybean.status.equals("Add") && !mybean.msg.equals("")) { %>
																	<%=mybean.ledger.DisplayMultiParty(mybean.session_id, mybean.mainparty_amount, mybean.comp_id)%>
																	<% } else if (!mybean.voucher_id.equals("0")
																				&& mybean.status.equals("Update")) { %>
																	<%=mybean.ledger.DisplayMultiParty(mybean.session_id, mybean.mainparty_amount, mybean.comp_id)%>
																	<% } %>
													</div>
													<div class="row"></div>
													<div class="form-element6">
															<label> Executive<font color="#ff0000">*</font>: </label>
																<div id="customers">
																	<select name="dr_executive"
																		value="<%=mybean.vouchertrans_paymode_id%>"
																		id="dr_executive" class="form-control">
																		<%=mybean.PopulateExecutives(mybean.emp_id, mybean.comp_id)%>
																	</select>
															    </div>
													</div>
													<div class="form-element6">
															<label>
																Narration<font color="#ff0000">*</font>:
															</label>
																<textarea name="txt_voucher_narration" cols="70"
																	rows="4" class="form-control"
																	id="txt_voucher_narration"
																	onkeyup="charcount('txt_voucher_narration', 'span_txt_voucher_narration','<font color=red>({CHAR} characters left)</font>', '5000')"><%=mybean.voucher_narration%></textarea>
																<span id=span_txt_voucher_narration> 5000
																	characters</span>
													</div>
													<div class="form-element8" id="customer_invoice"
																style="height: 300px; overflow-y: scroll">
																<% if (!mybean.vouchertrans_customer_id.equals("")) { %>
																<%=mybean.ledger.PopulatePendingInvoices(request, mybean.voucher_customer_id, mybean.voucher_id, mybean.vouchertype_id)%>
																<% } %>
															</div>

													<% if (mybean.vouchertype_ref_no_enable.equals("1")) { %>
													<div class="form-element6">
															<label>
																Reference No.
																 <% if (mybean.vouchertype_ref_no_mandatory.equals("1")) { %>
															<font color="#ff0000">*</font>:<% } %>
															</label>
																<input name="txt_voucher_ref_no" type="text"
																	class="form-control" id="txt_voucher_ref_no"
																	value="<%=mybean.voucher_ref_no%>" size="30"
																	maxlength="20" />
													</div>
													<% } %>
													<% if (mybean.vouchertype_driver_no.equals("1")) { %>
													<div class="form-element6">
															<label> Driver No.: </label>
																<input name="txt_voucher_driver_no" type="text"
																	class="form-control" id="txt_voucher_driver_no"
																	size="30" maxlength="255"
																	value="<%=mybean.voucher_driver_no%>" />
													</div>
													<% } %>
													<% if (mybean.vouchertype_tempo_no.equals("1")) { %>
													<div class="form-element6">
															<label>
																Tempo No.: </label>
																<input name="txt_voucher_tempo_no" type="text"
																	class="form-control" id="txt_voucher_tempo_no"
																	size="30" maxlength="255"
																	value="<%=mybean.voucher_tempo_no%>" />
													</div>
													<% } %>
													<div class="row"></div>
													<div class="form-element6">
															<label> Notes: </label>
																<textarea name="txt_voucher_notes" cols="70" rows="4"
																	class="form-control" id="txt_voucher_notes"><%=mybean.voucher_notes%></textarea>
															</div>
															<div class="form-element6" style="margin-top:10px">
																<label> Active: </label> <input type="checkbox"
																	name="ch_voucher_active" id="ch_voucher_active"
																	<%=mybean.PopulateCheck(mybean.voucher_active)%> />
															</div>

<div class="row"></div>
															<%
																if (mybean.status.equals("Update") && !(mybean.entry_by == null)
																		&& !(mybean.entry_by.equals(""))) {
															%>
													<div class="form-element6">
															<label> Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
																<input name="entry_by" type="hidden" id="entry_by"
																	value="<%=mybean.unescapehtml(mybean.entry_by)%>">
															</div>
													<%
														}
													%>
													<%
														if (mybean.status.equals("Update") && !(mybean.entry_date == null)
																&& !(mybean.entry_date.equals(""))) {
													%>
													<div class="form-element6">
															<label> Entry Date: <%=mybean.entry_date%></label>
																<input type="hidden" name="entry_date"
																	value="<%=mybean.entry_date%>">
															</div>
													<%
														}
													%>
													<%
														if (mybean.status.equals("Update") && !(mybean.modified_by == null)
																&& !(mybean.modified_by.equals(""))) {
													%>
													<div class="form-element6">
															<label> Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
																<input name="modified_by" type="hidden" id="modified_by"
																	value="<%=mybean.unescapehtml(mybean.modified_by)%>">
													</div>

													<%
														}
													%>
													<%
														if (mybean.status.equals("Update")
																&& !(mybean.modified_date == null)
																&& !(mybean.modified_date.equals(""))) {
													%>
													<div class="form-element6">
															<label>
																Modified Date: <%=mybean.modified_date%></label>
																<input type="hidden" name="modified_date"
																	value="<%=mybean.modified_date%>">
													</div>
													<%
														}
													%>
																<center>
																	<%
																		if (mybean.status.equals("Add")) {
																	%>
																	<input name="addbutton" type="submit"
																		class="btn btn-success" id="addbutton"
																		value="Add Payment"
																		onClick="return SubmitFormOnce(document.formemp, this);" />
																	<input type="hidden" name="add_button" value="yes">
																		<%
																			} else if (mybean.status.equals("Update")) {
																		%> <input type="hidden" name="update_button"
																		value="yes"> <input name="updatebutton"
																			type="submit" class="btn btn-success"
																			id="updatebutton" value="Update Payment"
																			onClick="return SubmitFormOnce(document.formemp, this);" />
																			<input name="delete_button" type="submit"
																			class="btn btn-success" id="delete_button"
																			OnClick="return confirmdelete(this)"
																			value="Delete Payment" /> <% } %> <input type="hidden"
																			name="voucher_id" value="<%=mybean.voucher_id%>">
																</center>
														</div>
<!-- 												</div></div>  -->
											</div>
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

<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
	<script type="text/javascript">
	function CheckNumeric(num) {
		if (isNaN(num) || num == '' || num == null) {
			num = 0;
		}
		return num;
	}

	$(function() {
		$("#txt_main_party_amount").keyup(function() {
			CalPartyBal(0);
		});
	});

	function CalPartyBal(id) {
		var totaladjamount = 0.00;
		var count = 0;
		if (eval(id) == 1) {
			count = CheckNumeric(document.getElementById("txt_count").value);
		}
		var partyamount = CheckNumeric(document.getElementById("txt_main_party_amount").value);

		for (var i = 0; i < count; i++) {
			if (document.getElementById("chk_" + i).checked == true) {
				totaladjamount += eval(document.getElementById("txt_amount_" + i).value);
			}
		}

		if (eval(count) == 0) {
			document.getElementById("remaining_party_bal").innerHTML = eval(partyamount).toFixed(2);
			document.getElementById("hi_remaining_party_bal").value = partyamount;
			document.getElementById("hi_remaining_party_bal1").value = partyamount;
		} else {
			document.getElementById("remaining_party_bal").innerHTML = eval(partyamount - totaladjamount).toFixed(2);
			document.getElementById("hi_remaining_party_bal").value = eval(partyamount - totaladjamount);
			document.getElementById("hi_remaining_party_bal1").value = eval(partyamount - totaladjamount);
		}
	}
	function HighlightRow(id, check) {
		if (check == 1) {
			var row_id = document.getElementById("row_" + id);
			row_id.style.color = "blue";
		} else if (check == 0) {
			var row_id = document.getElementById("row_" + id);
			row_id.style.color = "black";
		}
	}
	function ValidateForm() {
		var msg = "";
		var voucherdate = document.getElementById("txt_voucher_date").value;
		var branch = CheckNumeric(document.getElementById("dr_branch").value);
		var partyledger = CheckNumeric(document.getElementById("ledger").value);
		var contact = CheckNumeric(document.getElementById("dr_contact_id").value);
		var payment = document.getElementById("dr_voucher_payment").value;

		var paymentidarr = payment.split('-');
		var paymode = "";
		if (paymentidarr[0] != "0" && paymentidarr[0] != "141") {
			paymode = CheckNumeric(document.getElementById("dr_voucher_paymode").value);
			;
		}
		var partyamount = CheckNumeric(document.getElementById("txt_main_party_amount").value);
		var cheque_no = document.getElementById("txt_vouchertrans_cheque_no").value;
		var cheque_date = document.getElementById("txt_vouchertrans_cheque_date").value;
		var cheque_bank = document.getElementById("txt_vouchertrans_cheque_bank").value;
		var cheque_branch = document.getElementById("txt_vouchertrans_cheque_branch").value;
		var card_no = document.getElementById("txt_vouchertrans_card_no").value;
		var card_bank = document.getElementById("txt_vouchertrans_card_bank").value;
		var txn_no = document.getElementById("txt_vouchertrans_txn_no").value;
		var bank = document.getElementById("txt_vouchertrans_bank").value;
		var executive = CheckNumeric(document.getElementById("dr_executive").value);
		var narration = document.getElementById("txt_voucher_narration").value;

		//For Date
		if (voucherdate == "") {
			msg += "Select Date! \n";
		}
		//For Branch
		if (branch == "0") {
			msg += "Select Branch! \n";
		}
		//For Ledger
		if (partyledger == "0") {
			msg += "Select Party Ledger! \n";
		}
		//For Contact
		if (contact == "0") {
			msg += "Select Contact! \n";
		}
		//For Payment
		if (payment == "0-0") {
			msg += "Select Bank Ledger! \n";
		}
		//For Amount
		if (partyamount == "0") {
			msg += "Enter Amount! \n";
		}
		if (paymentidarr[0] != "0" && paymentidarr[0] != "141") {
			//For By
			if (paymode == "0") {
				msg += "Select By! \n";
			}
		}

		if (paymentidarr[1] != 0 && paymode != 0 && paymode != 1) {
			if (paymentidarr[1] == "2" && paymode == "2") {
				if (cheque_no == "") {
					msg += "Enter Cheque No.! \n";
				}
				if (cheque_date == "") {
					msg += "Enter Cheque Date! \n";
				}
				if (cheque_banknk == "") {
					msg += "Enter Cheque Bank! \n";
				}
				if (cheque_branch == "") {
					msg += "Enter Cheque Branch! \n";
				}
			} else if (paymentidarr[1] == "2" && paymode == "3") {
				if (card_no == "") {
					msg += "Enter Card No.! \n";
				}
				if (card_bank == "") {
					msg += "Enter Card Bank! \n";
				}
			} else if (paymentidarr[1] == "2" && paymode == "5") {

				if (txn_no == "") {
					msg += "Enter Txn No.! \n";
				}

			} else if (paymentidarr[1] == "2" && paymode == "6") {
				if (bank == "") {
					msg += "Enter Bank! \n";
				}
			}
		}
		if (executive == "0") {
			msg += "Select Executive! \n";
		}
		if (narration == "") {
			msg += "Enter Narration! \n";
		}
		if (msg != "") {
			alert(msg);
			return 0;
		} else {
			return 1;
		}
	}

	function CalAmountBal(click_count) {
		var partyamount = CheckNumeric(document.getElementById("txt_main_party_amount").value);
		var remainingpartyamount = CheckNumeric(document.getElementById("hi_remaining_party_bal").value);
		if (click_count != '-1') {
			var vouchertypeid = CheckNumeric(document.getElementById("txt_vouchertypeid_" + click_count).value);
		}

		var adjamt = 0;
		var amount = CheckNumeric(document.getElementById("hi_remaining_party_bal1").value);
		var ledger = CheckNumeric(document.getElementById("ledger").value);
		var check = 0;
		var totaladj = 0;

		if (click_count != '-1') {
			if (ValidateForm() == 0) {
				document.getElementById("chk_" + click_count).checked = false;
			}
		}

		if (ledger != '' && ledger != 0) {
			var count = CheckNumeric(document.getElementById("txt_count").value);
		}
		//if (count > 0 && amount > 0) {
			for (var i = 0; i < count; i++) {
				/* /* var vouchertypeid = CheckNumeric(document
						.getE ementById("txt_vouchertypeid_" + click_count).value); */
				if (document.getElementById("chk_" + i).checked == true) {
					if (eval(remainingpartyamount) > 0 && click_count == i) {
						check = 1;
						adjamt = CheckNumeric(document.getElementById("txt_amount_" + i).value);
						var balamount = document.getElementById("txt_balamount_" + i).value;
						totaladj = totaladj + parseFloat(adjamt);
						if (vouchertypeid == "21") {
							if (eval(totaladj) <= eval(remainingpartyamount)) {
								document.getElementById("remaining_party_bal").innerHTML = (eval(eval(remainingpartyamount) - eval(totaladj)).toFixed(2));
								document.getElementById("hi_remaining_party_bal").value = eval(eval(remainingpartyamount) - eval(totaladj));
								document.getElementById("sp_rebaldispaly_" + i).innerHTML = eval( 0).toFixed(2);
							} else if (eval(totaladj) > eval(remainingpartyamount)) {
								document.getElementById("txt_amount_" + i).value = eval(remainingpartyamount).toFixed(2);
								document.getElementById("remaining_party_bal").innerHTML = eval(0).toFixed(2);
								document.getElementById("sp_rebaldispaly_" + i).innerHTML =(eval(eval(balamount) - eval(remainingpartyamount)).toFixed(2));
								document .getElementById("hi_remaining_party_bal").value = eval( 0).toFixed(2);
							}
						} else if (vouchertypeid == "6") {
							document.getElementById("txt_amount_" + i).value = (eval(remainingpartyamount)).toFixed(2);
							document.getElementById("sp_rebaldispaly_" + i).innerHTML = (eval(eval(balamount) + eval(remainingpartyamount)).toFixed(2));
							document.getElementById("remaining_party_bal").innerHTML = eval( 0).toFixed(2);
							document.getElementById("hi_remaining_party_bal").value = eval( 0).toFixed(2);
						}

					} else {
						if (click_count == i && (eval(remainingpartyamount) == 0)) {
							document.getElementById("chk_" + i).checked = false;
							var balamount = document .getElementById("txt_balamount_" + i).value;
							document.getElementById("txt_amount_" + i).value = eval( balamount).toFixed(2);
						}
					}
				}
			}

			//on uncheck	
			var checked = 0;
			var currbalance = 0;
			for (var j = 0; j < count; j++) {
				if (document.getElementById("chk_" + j).checked == true) {
					currbalance += eval(document.getElementById("txt_amount_" + j).value);
					checked = 1;
				} else {
					var balamount = document.getElementById("txt_balamount_" + j).value;
					document.getElementById("txt_amount_" + j).value = eval( balamount).toFixed(2);
					document.getElementById("sp_rebaldispaly_" + j).innerHTML = "";
				}
			}
			if (vouchertypeid == "21") {
				document.getElementById("remaining_party_bal").innerHTML = (eval(partyamount) - eval(currbalance)).toFixed(2);
				document.getElementById("hi_remaining_party_bal").value = (eval(partyamount) - eval(currbalance)).toFixed(2);
			} else if (vouchertypeid == "6") {
				if (checked == 0) {
					document.getElementById("remaining_party_bal").innerHTML = eval(partyamount).toFixed(2);
					document.getElementById("hi_remaining_party_bal").value = eval(partyamount).toFixed(2);
				} else if (checked == 1) {
					if (eval(partyamount) <= eval(currbalance)) {
						document.getElementById("remaining_party_bal").innerHTML = eval(0).toFixed(2);
						document.getElementById("hi_remaining_party_bal").value = eval(0).toFixed(2);
					} else if (eval(partyamount) > eval(currbalance)) {
						document.getElementById("remaining_party_bal").innerHTML = (eval(partyamount) - eval(currbalance)).toFixed(2);
						document.getElementById("hi_remaining_party_bal").value = (eval(partyamount) - eval(currbalance)).toFixed(2);
					}
				}
			}
		//}
		//for Highliting the row
		for (var j = 0; j < count; j++) {
			if (document.getElementById("chk_" + j).checked == true) {
				HighlightRow(j, 1);
			} else {
				HighlightRow(j, 0);
			}
		}

		if (click_count == '-1') {
			CalPartyBal(1);
		} else if(click_count != '-1'){
			CalPartyBal(1);
		} 

	}

	//Second Method onkey up	
	function CalAmountBal2(click_count) {
		var partyamount = CheckNumeric(document
				.getElementById("txt_main_party_amount").value);

		var txt_amount = CheckNumeric(document.getElementById("txt_amount_"
				+ click_count).value);
		var balamount = document.getElementById("txt_balamount_" + click_count).value;

		var remainingpartyamount = CheckNumeric(document
				.getElementById("hi_remaining_party_bal").value);

		var count = CheckNumeric(document.getElementById("txt_count").value);
		var vouchertypeid = "";
		if (click_count != '-1') {
			vouchertypeid = CheckNumeric(document
					.getElementById("txt_vouchertypeid_" + click_count).value);
		}
		if (vouchertypeid == "6") {
			document.getElementById("sp_rebaldispaly_" + click_count).innerHTML = (eval(balamount) + eval(txt_amount))
					.toFixed(2);
		} else if (vouchertypeid == "21") {
			document.getElementById("sp_rebaldispaly_" + click_count).innerHTML = (eval(balamount) - eval(txt_amount))
					.toFixed(2);
		}

		var currbalance = 0;
		var checked = 0;
		var result = 0;
		for (var i = 0; i < count; i++) {
			if (document.getElementById("chk_" + i).checked == true) {
				currbalance += CheckNumeric(eval(document
						.getElementById("txt_amount_" + i).value));
				checked = 1;
			} else {
				var balamount = document.getElementById("txt_balamount_" + i).value;
				document.getElementById("txt_amount_" + i).value = eval(
						balamount).toFixed(2);
				document.getElementById("sp_rebaldispaly_" + i).innerHTML = "";
			}
		}

		if (vouchertypeid == "21") {
			if (checked == 0) {
				document.getElementById("remaining_party_bal").innerHTML = eval(
						partyamount).toFixed(2);
				document.getElementById("hi_remaining_party_bal").value = eval(
						partyamount).toFixed(2);

			} else if (checked == 1) {
				result = (eval(partyamount) - eval(currbalance));
				document.getElementById("remaining_party_bal").innerHTML = (result)
						.toFixed(2);
				document.getElementById("hi_remaining_party_bal").value = (result)
						.toFixed(2);
			}

		} else if (vouchertypeid == "6") {
			if (checked == 0) {
				document.getElementById("remaining_party_bal").innerHTML = eval(
						partyamount).toFixed(2);
				document.getElementById("hi_remaining_party_bal").value = eval(
						partyamount).toFixed(2);

			} else if (checked == 1) {
				result = (eval(partyamount) - eval(currbalance));
				document.getElementById("remaining_party_bal").innerHTML = (result)
						.toFixed(2);
				document.getElementById("hi_remaining_party_bal").value = (result)
						.toFixed(2);
			}
		}
	}
</script>

	<script type="text/javascript">
	$(function() {
		// credit to
// 		$('#multi_customer_cur_bal').hide();
		$("#ledger").change(
				function() {
					showHint('../accounting/ledger-check.jsp?contact=yes&customer_id='+ $(this).val(), 'dr_contact_id');
					showHint('../accounting/ledger-check.jsp?invoicedetail=yes&vouchertype_id='+<%=mybean.vouchertype_id%> + '&customer_id=' + $(this).val(), 'customer_invoice');
					showHint( '../accounting/ledger-check.jsp?currbal=yes&customer_id=' + $(this).val(), 'main_customer_cur_bal');
				});

		// multi ledgre
		$("#ledger1").change(
				function() {
					showHint( '../accounting/ledger-check.jsp?currbal=yes&customer_id=' + $(this).val(), 'multi_customer_cur_bal');
				});
	});
	$(function() {
		$('#chequeno').hide();
		$('#chequedate').hide();
		$('#chequebank').hide();
		$('#chequebranch').hide();
		$('#cardno').hide();
		$('#cardbank').hide();
		$('#txnno').hide();
		$('#bank').hide();
		
	});
	
	///

	function FormFocus() {
		document.formemp.txt_voucher_amount.focus();
	}

	// adding multiple party
	function addmultipleparty() {
		var msg = "";
		var main_party_amount = document
				.getElementById("txt_main_party_amount").value;
		var customer_id = document.getElementById("ledger1").value;
		var party_amount = document.getElementById("txt_multi_party_amount").value;

		if (customer_id == "") {
			msg += "\nSelect Multi Party Ledger";
		}
		if (party_amount == 0) {
			msg += "\nEnter Multi Party Amount";
		}
		if (msg == "") {
			showHint('../accounting/ledger-check.jsp?addmultiparty=yes&main_party_amount='
					+ main_party_amount
					+ '&customer_id='
					+ customer_id
					+ '&party_amount='
					+ party_amount
					+ '&session_id='
					+<%=mybean.session_id%>
					 ,'customers');
			// for reseting party ledger, contact , current balance and party amount
			$("#ledger1").select2("val", "");
			showHint('../accounting/ledger-check.jsp?currbal=yes&customer_id=' + '""', 'multi_customer_cur_bal');
			document.getElementById("txt_multi_party_amount").value = "";
			document.getElementById("multi_customer_cur_bal").setAttribute( "data-value", "");

		} else {
			alert(msg);
		}
	}

	// deleting party
	function delete_cart_item(cart_id) {
		showHint('../accounting/ledger-check.jsp?&deletemultiparty=yes&cart_id=' + cart_id + '&session_id=' + <%=mybean.session_id%> ,'customers');
	}
	
	
	function Displaypaymode() {
		var payment1 = document.getElementById("dr_voucher_payment").value;
		var paymentarr = payment1.split('-');
		var payment = paymentarr[1];
		var paymode = document.formemp.dr_voucher_paymode.value;
		if (paymentarr[0] == "0" || paymentarr[0] == "141") {
			$("#by").hide();
		} else {
			$("#by").show();
		}
		if (payment == "0") {
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
			$('#cardbank').hide();
			$('#txnno').hide();
		}
		if (payment == "1") {
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
			$('#txnno').hide();
		}
		if (payment == "1" && paymode == "1") {
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
			$('#txnno').hide();
		} else if (payment == "2" && paymode == "1") {
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
			$('#txnno').hide();
			$('#bank').hide();
		} else if (payment == "2" && paymode == "2") {
			$('#chequeno').show();
			$('#chequedate').show();
			$('#cardno').hide();
			$('#txnno').hide();
		} else if (payment == "2" && paymode == "3") {
			$('#cardno').show();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#txnno').show();
		} else if (payment == "2" && paymode == "5") {
			$('#txnno').show();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
		}else if (payment == "2" && paymode == "6") {
			$('#txnno').show();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
		}else if (payment == "2" && paymode == "4") {
			$('#txnno').show();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
		}else if (payment == "2" && paymode == "7") {
			$('#txnno').hide();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
			}
	}
	function CalTDSPerToAmount(){
		var tdsper = $('#txt_tds_per').val();
		var amount = $('#txt_main_party_amount').val();
		var tdsamount = eval(tdsper/100)*eval(amount);
		if(parseFloat(tdsper)>100){
			alert("TDS Percentage can't be greater then 100%!");
			$('#txt_tds_per').val(0);
			$('#txt_tds_amount').val(0);
		}else{
			$('#txt_tds_amount').val(tdsamount);
			$('#payable_amount').val(eval(amount-tdsamount));
		}
		
	}
	function CalTDSAmountToPer(){
		var tdsamount = $('#txt_tds_amount').val();
		var amount = $('#txt_main_party_amount').val();
		var txt_tdsper = eval(tdsamount/amount)*eval(100);
		if(parseFloat(tdsamount)>parseFloat(amount)){
			alert("TDS Amount can't be greater then Payment Amount!");
			$('#txt_tds_per').val(0);
			$('#txt_tds_amount').val(0);
		}else {
			$('#txt_tds_per').val(txt_tdsper);
			$('#payable_amount').val(eval(amount-tdsamount));
		}
	}
</script>
</body>
</HTML>
<!--created by Dhanesh on 26-07-08-->