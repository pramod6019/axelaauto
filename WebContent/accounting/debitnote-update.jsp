
<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.DebitNote_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>

<body <%if (mybean.status.equals("Add")) {%> onload="CalPartyBal(0);"
	<%} else {%> onload="CalPartyBal(1);" <%}%> class="page-container-bg-solid page-header-menu-fixed">
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
							<%=mybean.vouchertype_name%></h1>
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
						<li><a href="index.jsp">Accounting</a> &gt;</li>
						<li><a href="voucher-list.jsp?all=yes&vouchertype_id=<%=mybean.vouchertype_id%>&voucherclass_id=<%=mybean.voucherclass_id%>">List
								<%=mybean.vouchertype_name%>s
						</a> &gt;</li>
						<li><a href="debitnote-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> <%=mybean.vouchertype_name%></a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="container-fluid">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><span id="error"><%=mybean.msg%></span></b></font>
							</center>
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>
										<%=mybean.vouchertype_name%>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="formemp" class="form-horizontal" method="post">
											<center>
												<font>Form fields marked with a red asterisk <font
													color=#ff0000>*</font> are required.
												</font>
											</center>
											<input name="txt_vouchertype_id" type="hidden" id="txt_vouchertype_id" value="<%=mybean.vouchertype_id%>" />
											<input type="hidden" name="hid_voucher_no" id="hid_voucher_no" value="<%= mybean.voucher_no%>" />
											<input type="hidden" name="txt_vouchertype_authorize" id="txt_vouchertype_authorize" value="<%=mybean.vouchertype_authorize %>" />
											<input type="hidden" name="txt_voucher_authorize" id="txt_voucher_authorize" value="<%=mybean.voucher_authorize %>" />
											<input type="hidden" name="txt_vouchertype_defaultauthorize" id="txt_vouchertype_defaultauthorize" value="<%=mybean.vouchertype_defaultauthorize %>" />
											<input type="hidden" name="txt_voucher_authorize_id" id="txt_voucher_authorize_id" value="<%=mybean.voucher_authorize_id %>" />
											<input type="hidden" name="txt_voucher_authorize_time" id="txt_voucher_authorize_time" value="<%=mybean.voucher_authorize_time %>" />
												
												<div class="form-element6">
															<label class="">Date<font color="#ff0000">*</font>: </label>
																<%
																	if (mybean.empEditperm.equals("1")) {
																%>
																<input name="txt_voucher_date" type="text"
																	class="form-control datepicker" id="txt_voucher_date"
																	value="<%=mybean.voucherdate%>" size="11"
																	maxlength="10" />
																<%
																	} else {
																%>
																<%=mybean.voucherdate%>
																<input name="hid_txt_voucher_date" type="hidden"
																	class="textbox" id="hid_txt_voucher_date"
																	value="<%=mybean.voucherdate%>">
																<%
																	}
																%>
												</div>

												<div class="form-element6">
																<label>Include Zero BalanceDate : <input type="checkbox" id="chk_zero_bal"
																	name="chk_zero_bal" class="checkbox"
																	onclick="showzer0balance()"></input></label>
															 
														</div>
													
                                            <br/>  <div class="row"></div>
															<div class="form-element6">
																	<label>Branch<font color=red>*</font>: </label>
																		<select name="dr_branch" id="dr_branch"
																			class="form-control"
																			onChange="showHint('inventory-location-check.jsp?branch_id=' + GetReplace(this.value),'dr_location_id')">
																			<%=mybean.PopulateBranch(mybean.voucher_branch_id, "", "", "", request)%>
																		</select>
															</div>
                                                      
															<div class="form-element6">
																	<label>Debit To<font color="#ff0000">*</font>: </label>
																		<select class="form-control select2" id="ledger1" name="ledger1">
																	<%=mybean.ledgercheck.PopulateLedgers("0", mybean.debitcustid, mybean.comp_id)%>
																</select><div id="debitcustid_cur_bal"><%=mybean.ReturnCustomerCurrBalance(mybean.debitcustid, mybean.comp_id, mybean.vouchertype_id)%></div>
																</div>
 											 <div class="row"></div>
											<div class="form-element6">
												<label>Contact<font color="#ff0000">*</font>: </label>
												<span id="dr_contact_id"> <%=mybean.PopulateContact(mybean.debitcustid)%>
												</span>
											</div>
                                                      <div class="form-element6">
																	<label>Amount<font color="#ff0000">*</font>: </label>
																		<input id="txt_voucher_amount"
																			name="txt_voucher_amount" type="text"
																			onkeyup="toFloat(this.id);" class="form-control"
																			value="<%=mybean.voucher_amount%>" size="12"
																			maxlength="10" /> <input type="hidden"
																			id="txt_voucher_prev_amount"
																			name="txt_voucher_prev_amount"
																			value="<%=mybean.voucher_amount%>" size="12"
																			maxlength="10" />
																	<font color='red'><b>Balance: <span
																		id="remaining_party_bal"></span></b></font>
																		
																		<input type="hidden" id="hi_remaining_party_bal"
																name="hi_remaining_party_bal" value="" /> <input
																type="hidden" id="hi_remaining_party_bal1"
																name="hi_remaining_party_bal1" value="" />
																	</div>
                                                      
                                                     <div class="form-element6">
																	<label>Credit To<font color="#ff0000">*</font>: </label>
																		<select class="form-control select2"
																		id="ledger2" name="ledger2">
																		<%=mybean.ledgercheck.PopulateLedgers("0", mybean.creditcustid, mybean.comp_id)%>
																	</select>
																	<div id="creditcustid_cur_bal"><%=mybean.ReturnCustomerCurrBalance(mybean.creditcustid, mybean.comp_id, mybean.vouchertype_id)%></div>
															</div>

											<div class="form-element6">
												<label>Executive<font color="#ff0000">*</font>:
												</label> <select name="dr_executive"
													value="<%=mybean.vouchertrans_paymode_id%>"
													id="dr_executive" class="form-control">
													<%=mybean.PopulateExecutives(mybean.emp_id, mybean.comp_id)%>
												</select>
											</div>
											<%
												if (mybean.vouchertype_ref_no_enable.equals("1")) {
											%>
										
                                                     <div class="form-element6">
																	<label>Reference No.
											<% if (mybean.vouchertype_ref_no_mandatory.equals("1")) { %>
											<font color="#ff0000">*</font>: <% } %>
																	</label>
																		<input
												name="txt_voucher_ref_no" type="text" class="form-control"
												id="txt_voucher_ref_no" value="<%=mybean.voucher_ref_no%>"
												size="30" maxlength="20" />
															</div>
                                                      <%} %>

											<div class="form-element6">
												<label>Narration<font color="#ff0000">*</font>:
												</label>
												<textarea name="txt_voucher_narration" cols="70" rows="4"
													class="form-control" id="txt_voucher_narration"
													onkeyup="charcount('txt_voucher_narration', 'span_txt_voucher_narration','<font color=red>({CHAR} characters left)</font>', '5000')"><%=mybean.voucher_narration%></textarea>
												<span id=span_txt_voucher_narration> 5000 characters</span>
											</div>


											<div class="form-element6 form-element-margin">
												<label>Active:</label>
													<input type="checkbox"
												name="ch_voucher_active" id="ch_voucher_active"
												<%=mybean.PopulateCheck(mybean.voucher_active)%> />
                                             </div>
                                             <div class="row"></div>
											<div class="form-element12" id="customer_invoice"
															style="height: 300px; overflow-y: scroll">
															<% if (!mybean.debitcustid.equals("")) { %>
																<%=mybean.ledger.PopulatePendingInvoices(request, mybean.debitcustid, mybean.voucher_id, mybean.vouchertype_id)%>
															<% } %>
												</div>
												
											<% if (mybean.status.equals("Update") && !(mybean.entry_by == null)
													&& !(mybean.entry_by.equals(""))) { %>
										<div class="form-element6">
												<label>Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
												<input name="entry_by" type="hidden" id="entry_by"
												value="<%=mybean.unescapehtml(mybean.entry_by)%>">
                                             </div>
										<% } %>
										 
										<%
											if (mybean.status.equals("Update") && !(mybean.entry_date == null)
													&& !(mybean.entry_date.equals(""))) {
										%>
										<div class="form-element6">
												<label>Entry Date: <%=mybean.entry_date%></label> <input
												type="hidden" name="entry_date"
												value="<%=mybean.entry_date%>">
												</div>
										<% } %>
										<%
											if (mybean.status.equals("Update") && !(mybean.modified_by == null)
													&& !(mybean.modified_by.equals(""))) {
										%>
										<div class="form-element6">
												<label>Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
												<input name="modified_by" type="hidden" id="modified_by"
												value="<%=mybean.unescapehtml(mybean.modified_by)%>">
                                             </div>
												<% } %>
										<%
											if (mybean.status.equals("Update")
													&& !(mybean.modified_date == null)
													&& !(mybean.modified_date.equals(""))) {
										%>
										<div class="form-element6">
												<label>Modified Date: <%=mybean.modified_date%></label> <input
												type="hidden" name="modified_date"
												value="<%=mybean.modified_date%>">  
                                             </div>
												<% } %>
											<div class="row"></div>
														<center>
													<% if (mybean.status.equals("Add")) { %>
															<input name="addbutton" type="submit" class="btn btn-success"
															id="addbutton" value="Add Debit Note"
															onClick="return SubmitFormOnce(document.formemp, this);" />
															<input type="hidden" name="add_button" value="yes" />
												
												
													<% } else if (mybean.status.equals("Update")) { %> 
														<input type="hidden" name="update_button" value="yes" />
														<input name="updatebutton" type="submit" class="btn btn-success"
														id="updatebutton" value="Update Debit Note"
														onClick="return SubmitFormOnce(document.formemp, this);" />
														<input name="delete_button" type="submit" class="btn btn-success"
														id="delete_button" OnClick="return confirmdelete(this)"
														value="Delete Debit Note" /> 
														<% } %> 
														
														<input type="hidden" name="voucher_id" value="<%=mybean.voucher_id%>" />
											
														</center>
												
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
		function showzer0balance() {
			var party_id = document.getElementById("ledger1").value;
			var include_zero = document.getElementById("chk_zero_bal").checked;
			var include_zero_bal = 0;
			if (include_zero == true) {
				include_zero_bal = 1;
			}

			showHint(
					'../accounting/ledger-check.jsp?invoicedetail=yes&customer_id='
							+ party_id + '&include_zero=' + include_zero_bal,
					'customer_invoice');
		}

		function CheckNumeric(num) {
			if (isNaN(num) || num == '' || num == null) {
				num = 0;
			}
			return num;
		}

		$(function() {
			$("#txt_voucher_amount").keyup(function() {
				CalPartyBal(0);
			});
		});

		function CalPartyBal(id) {
			var totaladjamount = 0.00;
			var count = 0;
			if (eval(id) == 1) {
				count = CheckNumeric(document.getElementById("txt_count").value);
			}
			var partyamount = CheckNumeric(document
					.getElementById("txt_voucher_amount").value);

			for (var i = 0; i < count; i++) {
				if (document.getElementById("chk_" + i).checked == true) {
					totaladjamount += eval(document
							.getElementById("txt_amount_" + i).value);
				}
			}

			if (eval(count) == 0) {
				document.getElementById("remaining_party_bal").innerHTML = eval(
						partyamount).toFixed(2);
				document.getElementById("hi_remaining_party_bal").value = partyamount;
				document.getElementById("hi_remaining_party_bal1").value = partyamount;
			} else {
				document.getElementById("remaining_party_bal").innerHTML = eval(
						partyamount - totaladjamount).toFixed(2);
				document.getElementById("hi_remaining_party_bal").value = eval(partyamount
						- totaladjamount);
				document.getElementById("hi_remaining_party_bal1").value = eval(partyamount
						- totaladjamount);
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
			var creditto = CheckNumeric(document.getElementById("ledger2").value);
			var contact = CheckNumeric(document.getElementById("dr_contact_id").value);
			var partyamount = CheckNumeric(document
					.getElementById("txt_voucher_amount").value);
			var debitto = CheckNumeric(document.getElementById("ledger1").value);
			var executive = CheckNumeric(document
					.getElementById("dr_executive").value);
			var narration = document.getElementById("txt_voucher_narration").value;

			//For Date
			if (voucherdate == "") {
				msg += "Select Date! \n";
			}
			//For Branch
			if (branch == "0") {
				msg += "Select Branch! \n";
			}
			//For Debit To
			if (debitto == "0") {
				msg += "Select Debit To! \n";
			}
			//For Contact
			if (contact == "0") {
				msg += "Select Contact! \n";
			}
			//For Amount
			if (partyamount == "0") {
				msg += "Enter Amount! \n";
			}
			//For Ledger
			if (creditto == "0") {
				msg += "Select Credit To! \n";
			}
			// For Executive
			if (executive == "0") {
				msg += "Select Executive! \n";
			}
			// For Marration
			if (narration == "") {
				msg += "Enter Narration! \n";
			}
			if (msg != "") {
				alert(msg);
				//document.getElementById("error").innerHTML="Error !<br>"+msg;
				return 0;
			} else {
				//document.getElementById("error").innerHTML="";
				return 1;
			}
		}

		function CalAmountBal(click_count) {
			var partyamount = CheckNumeric(document
					.getElementById("txt_voucher_amount").value);
			var remainingpartyamount = CheckNumeric(document
					.getElementById("hi_remaining_party_bal").value);
			if (click_count != '-1') {
				var vouchertypeid = CheckNumeric(document
						.getElementById("txt_vouchertypeid_" + click_count).value);
			}

			var adjamt = 0;
			var amount = CheckNumeric(document
					.getElementById("hi_remaining_party_bal1").value);
			var ledger = CheckNumeric(document.getElementById("ledger1").value);
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
						adjamt = CheckNumeric(document
								.getElementById("txt_amount_" + i).value);
						var balamount = document
								.getElementById("txt_balamount_" + i).value;
						totaladj = totaladj + parseFloat(adjamt);
						if (vouchertypeid == "115") {
							if (eval(totaladj) <= eval(remainingpartyamount)) {
								document.getElementById("remaining_party_bal").innerHTML = (eval(eval(remainingpartyamount)
										- eval(totaladj)).toFixed(2));
								document
										.getElementById("hi_remaining_party_bal").value = eval(eval(remainingpartyamount)
										- eval(totaladj));
								document.getElementById("sp_rebaldispaly_" + i).innerHTML = eval(
										0).toFixed(2);
							} else if (eval(totaladj) > eval(remainingpartyamount)) {
								document.getElementById("txt_amount_" + i).value = eval(
										remainingpartyamount).toFixed(2);
								document.getElementById("remaining_party_bal").innerHTML = eval(
										0).toFixed(2);
								document.getElementById("sp_rebaldispaly_" + i).innerHTML = (eval(eval(balamount)
										- eval(remainingpartyamount))
										.toFixed(2));
								document
										.getElementById("hi_remaining_party_bal").value = eval(
										0).toFixed(2);
							}
						} else if (vouchertypeid == "102") {
							document.getElementById("txt_amount_" + i).value = (eval(remainingpartyamount))
									.toFixed(2);
							document.getElementById("sp_rebaldispaly_" + i).innerHTML = (eval(eval(balamount)
									+ eval(remainingpartyamount)).toFixed(2));
							document.getElementById("remaining_party_bal").innerHTML = eval(
									0).toFixed(2);
							document.getElementById("hi_remaining_party_bal").value = eval(
									0).toFixed(2);
						}

					} else {
						if (click_count == i
								&& (eval(remainingpartyamount) == 0)) {
							document.getElementById("chk_" + i).checked = false;
							var balamount = document
									.getElementById("txt_balamount_" + i).value;
							document.getElementById("txt_amount_" + i).value = eval(
									balamount).toFixed(2);
						}

					}
				}
			}

			//on uncheck	
			var checked = 0;
			var currbalance = 0;
			for (var j = 0; j < count; j++) {
				if (document.getElementById("chk_" + j).checked == true) {
					currbalance += eval(document.getElementById("txt_amount_"
							+ j).value);
					checked = 1;
				} else {
					var balamount = document.getElementById("txt_balamount_"
							+ j).value;
					document.getElementById("txt_amount_" + j).value = eval(
							balamount).toFixed(2);
					document.getElementById("sp_rebaldispaly_" + j).innerHTML = "";
				}
			}
			if (vouchertypeid == "115") {
				document.getElementById("remaining_party_bal").innerHTML = (eval(partyamount) - eval(currbalance))
						.toFixed(2);
				document.getElementById("hi_remaining_party_bal").value = (eval(partyamount) - eval(currbalance))
						.toFixed(2);
			} else if (vouchertypeid == "102") {
				if (checked == 0) {
					document.getElementById("remaining_party_bal").innerHTML = eval(
							partyamount).toFixed(2);
					document.getElementById("hi_remaining_party_bal").value = eval(
							partyamount).toFixed(2);
				} else if (checked == 1) {
					if (eval(partyamount) <= eval(currbalance)) {
						document.getElementById("remaining_party_bal").innerHTML = eval(
								0).toFixed(2);
						document.getElementById("hi_remaining_party_bal").value = eval(
								0).toFixed(2);
					} else if (eval(partyamount) > eval(currbalance)) {
						document.getElementById("remaining_party_bal").innerHTML = (eval(partyamount) - eval(currbalance))
								.toFixed(2);
						document.getElementById("hi_remaining_party_bal").value = (eval(partyamount) - eval(currbalance))
								.toFixed(2);
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
			} else if (click_count != '-1') {
				CalPartyBal(1);
			}

		}

		//Second Method onkey up	
		function CalAmountBal2(click_count) {
			var partyamount = CheckNumeric(document
					.getElementById("txt_voucher_amount").value);

			var txt_amount = CheckNumeric(document.getElementById("txt_amount_"
					+ click_count).value);
			var balamount = document.getElementById("txt_balamount_"
					+ click_count).value;

			var remainingpartyamount = CheckNumeric(document
					.getElementById("hi_remaining_party_bal").value);

			var count = CheckNumeric(document.getElementById("txt_count").value);
			var vouchertypeid = "";
			if (click_count != '-1') {
				vouchertypeid = CheckNumeric(document
						.getElementById("txt_vouchertypeid_" + click_count).value);
			}
			if (vouchertypeid == "102") {
				document.getElementById("sp_rebaldispaly_" + click_count).innerHTML = (eval(balamount) + eval(txt_amount))
						.toFixed(2);
			} else if (vouchertypeid == "115") {
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
					var balamount = document.getElementById("txt_balamount_"
							+ i).value;
					document.getElementById("txt_amount_" + i).value = eval(
							balamount).toFixed(2);
					document.getElementById("sp_rebaldispaly_" + i).innerHTML = "";
				}
			}

			if (vouchertypeid == "115") {
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

			} else if (vouchertypeid == "102") {
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

			// debit to      
			$("#ledger1").change(function() {
								showHint('../accounting/ledger-check.jsp?contact=yes&customer_id=' + $(this).val(), 'dr_contact_id');
								showHint('../accounting/ledger-check.jsp?currbal=yes&customer_id=' + $(this).val(), 'debitcustid_cur_bal');
								showHint('../accounting/ledger-check.jsp?invoicedetail=yes&vouchertype_id=' + <%=mybean.vouchertype_id%> + '&customer_id=' + $(this).val(), 'customer_invoice');
								$("#dr_contact_id").show();
							});
			// credit to
			$("#ledger2").change( function() {
								showHint('../accounting/ledger-check.jsp?currbal=yes&customer_id=' + $(this).val(), 'creditcustid_cur_bal');
							});

			// pending purchase invoice
			$("#pendinginvoice").click(
					function() {
						var invoiceno = document.getElementById("txt_invoice_no").value;
						showHint('../accounting/ledger-check.jsp?cdnotedetail=yes&vouchertype_id=' + <%=mybean.vouchertype_id%> + '&invoiceno=' + invoiceno, 'customer_invoice');
					});

			
		});

		
		///

		function FormFocus() {
			document.formemp.txt_voucher_amount.focus();
		}
	</script>
</body>
</HTML>
<!--created by Dhanesh on 26-07-08-->