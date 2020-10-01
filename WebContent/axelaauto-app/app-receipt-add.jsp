<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Receipt_Add"
	scope="request" />
<%
mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">
<link href="css/select2.min.css" rel="stylesheet" type="text/css" />
<link href="css/select2-bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<style>
b {
	color: #8f3e97;
}

.container {
	padding-right: 0px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: 5px;
	margin-top: 45px;
}

span {
	color: red;
}

.panel-heading {
	margin-bottom: 20px;
	background-color: #8E44AD;
	border: 1px solid transparent;
	border-radius: 0px;
	box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}

strong {
	color: #fff;
}

.header-wrap {
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
}

.btn1 {
	/* background-color: #8E44AD; */ /
	
}
</style>
</head>
<body <%if(!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%} %> onLoad="Displaypaymode();">
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Add
						Receipt</center></strong></span>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12">
			<form role="form" id="frmaddreceipt" name="frmaddreceipt"
				class="form-horizontal" method="post">
				<div class="form-body">
					<div class="alert alert-danger display-hide">
						<button class="close" data-close="alert"></button>
						You have some form errors. Please check below.
					</div>
					<div class="alert alert-success display-hide">
						<button class="close" data-close="alert"></button>
						Your form validation is successful!
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Bank Ledger<span>*</span>:
						</label> <select name="dr_voucher_payment" id="dr_voucher_payment"
							class="form-control" onChange="Displaypaymode();">
							<%=mybean.PopulatePayment()%>
						</select>
					</div>

					<div id="by">
						<div class="form-group form-md-line-input">
							<label for="form_control_1">By<span>*</span>:
							</label> <select name="dr_voucher_paymode" id="dr_voucher_paymode"
								class="form-control" onChange="Displaypaymode(1)">
								<%=mybean.PopulatePaymode()%>
							</select>
						</div>
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Party Amount<span>*</span>:
						</label> <input type="tel" id="txt_main_party_amount"
							name="txt_main_party_amount" class="form-control"
							value="<%=mybean.mainparty_amount%>" size="12" maxlength="10"
							onKeyUp="toNumber('txt_main_party_amount','Amount')" />
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Balance:</label> <span
							id="remaining_party_bal"></span> <input type="hidden"
							id="hi_remaining_party_bal" name="hi_remaining_party_bal"
							value="dfdf" /> <input type="hidden"
							id="hi_remaining_party_bal1" name="hi_remaining_party_bal1"
							value="sdsd" />


					</div>

					<div class="form-group form-md-line-input" id="chequeno">
						<label for="form_control_1">Cheque No<span>*</span>:
						</label> <input name="txt_vouchertrans_cheque_no" type="text"
							id="txt_vouchertrans_cheque_no"
							onKeyUp="toInteger('txt_vouchertrans_cheque_no')"
							class="form-control" value="<%=mybean.vouchertrans_cheque_no%>"
							size="12" maxlength="6" />
					</div>

					<div class="form-group form-md-line-input" id="chequedate">
						<label for="form_control_1">Cheque Date<span>*</span>:
						</label> <input name="txt_vouchertrans_cheque_date" type="text"
							class="form-control date-picker"
							id="txt_vouchertrans_cheque_date"
							value="<%=mybean.vouchertrans_chequedate%>" size="12"
							maxlength="10" data-date-format="dd/mm/yyyy"
							onclick="datePicker('txt_vouchertrans_cheque_date');"  />
					</div>

					<div class="form-group form-md-line-input" id="bank">
						<label for="form_control_1">Bank<span>*</span>:
						</label> <select name="dr_finance_by" class="form-control"
							id="dr_finance_by">
							<%=mybean.PopulateFinanceBy()%>
						</select>
					</div>

					<div class="form-group form-md-line-input" id="chequebranch">
						<label for="form_control_1">Cheque Branch<span>*</span>:
						</label> <input name="txt_vouchertrans_cheque_branch"
							id="txt_vouchertrans_cheque_branch" type="text"
							class="form-control"
							value="<%=mybean.vouchertrans_cheque_branch%>" size="70"
							maxlength="255" />
					</div>

					<div class="form-group form-md-line-input" id="cardno">
						<label for="form_control_1">Card No<span>*</span>:
						</label> <input name="txt_vouchertrans_card_no" type="tel"
							id="txt_vouchertrans_card_no"
							onKeyUp="toInteger('txt_vouchertrans_card_no')"
							class="form-control" value="<%=mybean.vouchertrans_cheque_no%>"
							size="12" maxlength="16" />
					</div>


					<div class="form-group form-md-line-input" id="txnno">
						<label for="form_control_1">Transaction No<span>*</span>:
						</label> <input name="txt_vouchertrans_transaction_no" type="text"
							id="txt_vouchertrans_transaction_no"
							class="form-control"
							value="<%=mybean.vouchertrans_transaction_no%>" size="12"
							maxlength="30" />
					</div>
					<div class="form-group form-md-line-input" >
					<label for="form_control_1">Narration<font color="#ff0000">*</font>: </label>
						<textarea name="txt_voucher_narration" cols="70" rows="4"
							class="form-control" id="txt_voucher_narration"
							onkeyup="charcount('txt_voucher_narration', 'span_txt_voucher_narration','<font color=red>({CHAR} characters left)</font>', '5000')"><%=mybean.voucher_narration%></textarea>
						<span id=span_txt_voucher_narration> 5000
							characters</span>
					</div>

					<% if (mybean.vouchertype_ref_no_enable.equals("1")) { %>
					<div class="form-body">
						<div class="form-group">
							<label class="col-md-4 control-label">Reference No. <%
															if (mybean.vouchertype_ref_no_mandatory.equals("1")) {
														%> <font color="#ff0000">*</font>: <%
 	}
 %>
							</label>
							<div class="col-md-6">
								<input name="txt_voucher_ref_no" type="text"
									class="form-control" id="txt_voucher_ref_no"
									value="<%=mybean.voucher_ref_no%>" size="30" maxlength="20" />
							</div>
						</div>
					</div>
					<%
													}
												%>


					<% if (mybean.vouchertype_driver_no.equals("1")) { %>
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Driver No.:</label> <input
							name="txt_voucher_driver_no" type="text" class="form-control"
							id="txt_voucher_driver_no" size="30" maxlength="255"
							value="<%=mybean.voucher_driver_no%>" />
					</div>
					<%} %>

					<% if (mybean.vouchertype_tempo_no.equals("1")) { %>
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Tempo No.:</label> <input
							name="txt_voucher_tempo_no" type="text" class="form-control"
							id="txt_voucher_tempo_no" size="30" maxlength="255"
							value="<%=mybean.voucher_tempo_no%>" />
					</div>
					<%} %>


					<br>
					<div class="form-actions noborder">
						<center>
							<button type="button" class="btn1" name="addbutton"
								id="addbutton">Add Receipt</button>
							<!-- 							<input name="addbutton" id ="addbutton" type="button" class="btn1"  value="Add Enquiry" onClick="return SubmitFormOnce(document.frmaddreceipt, this);" /> -->
							<input type="hidden" name="add_button1" id="add_button1" value="" />
							<input name="txt_hid_voucher_date" type="hidden"
								class="form-control" id="txt_hid_voucher_date"
								value="<%=mybean.voucherdate%>" /> <input type="hidden"
								name="dr_branch" id="dr_branch"
								value="<%=mybean.voucher_branch_id%>" /> <input type="hidden"
								name="voucher_id" value="<%=mybean.voucher_id%>">

						</center>
						<br>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 	<script src="js/jquery.min.js" type="text/javascript"></script> -->
	<!-- <script src="js/bootstrap.js" type="text/javascript"></script> -->
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<!-- 	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script> -->
	<!-- <script src="../assets/js/components-select2.min.js" type="text/javascript"></script> -->
	<!--  <script src="js/jquery-ui.js" type="text/javascript"></script>	 -->
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/jquery.app.js" type="text/javascript"></script>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<!-- 	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script> -->
	<script src="js/select2.full.min.js" type="text/javascript"></script>
	<script src="js/components-select2.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/Validate.js"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script>

$(document).ready(function() {
	$("#addbutton").click(function() {
		checkForm();
	});
});

</script>

	</script>

	<!-- // $("#dr_branch_id").change(function() -->
	<!-- // 		{ -->
	<!-- // 	//this.form.submit(); -->
	<!-- // 	document.getElementById('frmaddenquiry').submit(); -->
	<!-- // 		}); -->


	<script>
	var msg = "";
	function checkForm() {
		
		
		var payment1 = document.frmaddreceipt.dr_voucher_payment.value;
		var paymentarr = payment1.split('-');
		var payment = paymentarr[1];
		var paymode = document.frmaddreceipt.dr_voucher_paymode.value;
		
		msg = "";
		
		var narration = document.getElementById("txt_voucher_narration").value;
		if (narration == '0' || narration == '') {
			msg += '<br>Enter Narration!';
		}
		
		var bankledger = document.getElementById("dr_voucher_payment").value;
		if (bankledger == '0' || bankledger == '0-0') {
			msg += '<br>Select Bank Ledger!';
		}
		
		var partyamount = document.getElementById("txt_main_party_amount").value;
		if (partyamount == '0' || partyamount == '') {
			msg += '<br>Enter Party Amount!';
		}
		
		if (!paymentarr[0] == "0" || !paymentarr[0] == "141") {
			var voucher_paymode = document.getElementById("dr_voucher_paymode").value;
			if (voucher_paymode == '0') {
				msg += '<br>Select By!';
			}
		}
		if (payment == "2" && paymode == "2") {
			var chequeno = document.getElementById("txt_vouchertrans_cheque_no").value;
			if (chequeno == '0' || chequeno == '') {
				msg += '<br>Enter Cheque No.!';
			}
			
			if(chequeno.length<6){
				msg += '<br>Cheque No. cannot be less than 6 digits!';
			}
			
			var chequedate = document.getElementById("txt_vouchertrans_cheque_date").value;
			if (chequedate == '0' || chequedate == '') {
				msg += '<br>Enter Cheque Date!';
			}
			
			var chequebank = document.getElementById("txt_vouchertrans_cheque_branch").value;
			if (chequebank == '0' || chequebank == '') {
				msg += '<br>Select Bank!';
			}
			
			var chequebranch = document.getElementById("txt_vouchertrans_cheque_branch").value;
			if (chequebranch == '0' || chequebranch == '') {
				msg += '<br>Enter Cheque Branch!';
			}
			
			
			
			
		} 
		
		if (payment == "2" && paymode == "3") {
			var cardno = document.getElementById("txt_vouchertrans_card_no").value;
			if (cardno == '0' || cardno == '') {
				msg += '<br>Enter Card No.!';
			}
			
			var cardbank = document.getElementById("dr_finance_by").value;
			if (cardbank == '0' || cardbank == '') {
				msg += '<br>Select Bank.!';
			}
			
			 var txnno = document.getElementById("txt_vouchertrans_transaction_no").value;
				if (txnno == '0' || txnno == '') {
					msg += '<br>Enter Transaction No.!';
				}
		} 
		
		if (payment == "2" && paymode == "4") {
			var txnno = document.getElementById("txt_vouchertrans_transaction_no").value;
			if (txnno == '0' || txnno == '') {
				msg += '<br>Enter Transaction No.!';
			}
			
			var cardbank = document.getElementById("dr_finance_by").value;
			if (cardbank == '0' || cardbank == '') {
				msg += '<br>Select Bank.!';
			}
}
		
 if (payment == "2" && paymode == "5") {
	 var txnno = document.getElementById("txt_vouchertrans_transaction_no").value;
		if (txnno == '0' || txnno == '') {
			msg += '<br>Enter Transaction No.!';
		}
		var cardbank = document.getElementById("dr_finance_by").value;
		if (cardbank == '0' || cardbank == '') {
			msg += '<br>Select Bank.!';
		}
		}
 
if (payment == "2" && paymode == "6") {
			var txnno = document.getElementById("txt_vouchertrans_transaction_no").value;
			if (txnno == '0' || txnno == '') {
				msg += '<br>Enter Transaction No.!';
			}
			var cardbank = document.getElementById("dr_finance_by").value;
			if (cardbank == '0' || cardbank == '') {
				msg += '<br>Select Bank.!';
			}
}


		
		if (msg != '') {
			showToast(msg);
		} 
		else {
			document.getElementById('add_button1').value = "yes";
			document.getElementById('frmaddreceipt').submit();
		}
	}
    </script>


	<script>

  
	function Displaypaymode() {
		var payment1 = document.frmaddreceipt.dr_voucher_payment.value;
		var paymentarr = payment1.split('-');
		var payment = paymentarr[1];
		var paymode = document.frmaddreceipt.dr_voucher_paymode.value;
		if (paymentarr[0] == "0" || paymentarr[0] == "141") {
			$("#by").hide();
		} else {
			$("#by").show();
		}
		if (payment == "0") {
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#chequebranch').hide();
			$('#cardno').hide();
			$('#cardbank').hide();
			$('#txnno').hide();
			$('#bank').hide();
		}
		
		if (payment == "1") {
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#bank').hide();
			$('#chequebranch').hide();
			$('#cardno').hide();
			$('#txnno').hide();
		}
		if (payment == "1" && paymode == "1") {
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#chequebranch').hide();
			$('#cardno').hide();
			$('#txnno').hide();
			$('#bank').hide();
		} else if (payment == "2" && paymode == "1") {
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#chequebranch').hide();
			$('#cardno').hide();
			$('#txnno').hide();
				$('#bank').hide();
		} else if (payment == "2" && paymode == "2") {
			$('#chequeno').show();
			$('#chequedate').show();
			$('#chequebranch').show();
			$('#cardno').hide();
			$('#txnno').hide();
				$('#bank').show();
		} else if (payment == "2" && paymode == "3") {
			$('#cardno').show();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#chequebranch').hide();
			$('#txnno').show();
			$('#bank').show();
		} else if (payment == "2" && paymode == "5") {
			$('#txnno').show();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#chequebranch').hide();
			$('#cardno').hide();
				$('#bank').show();
		}else if (payment == "2" && paymode == "6") {
			$('#txnno').show();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#chequebranch').hide();
			$('#cardno').hide();
				$('#bank').show();
		}
			else if (payment == "2" && paymode == "4") {
				$('#txnno').show();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#chequebranch').hide();
			$('#cardno').hide();
				$('#bank').show();
			}

	}
    </script>

	<script type="text/javascript">
		$(function() {
// 			$('#customer_invoice').hide();
//         	$('#customer_invoice1').hide();

           
			$("#txt_main_party_amount").keyup(function() {
				CalPartyBal(0);
			});
		});
		function CheckNumeric(num) {
			if (isNaN(num) || num == '' || num == null) {
				num = 0;
			}
			return num;
		}
		function CalPartyBal(id) {
			var totaladjamount = 0.00;
			var count = 0;
			if (eval(id) == 1) {
				count = CheckNumeric(document.getElementById("txt_count").value);
			}
			var partyamount = CheckNumeric(document
					.getElementById("txt_main_party_amount").value);
			//alert("partyamount==="+partyamount);
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

		function CalAmountBal(click_count) {

			var partyamount = CheckNumeric(document
					.getElementById("txt_main_party_amount").value);
			var remainingpartyamount = CheckNumeric(document
					.getElementById("hi_remaining_party_bal").value);
			if (click_count != '-1') {
				var vouchertypeid = CheckNumeric(document
						.getElementById("txt_vouchertypeid_" + click_count).value);
			}

			var adjamt = 0;
			var amount = CheckNumeric(document
					.getElementById("hi_remaining_party_bal1").value);
			var ledger = document.getElementById("ledger").value;
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
				if (document.getElementById("chk_" + i).checked == true) {
					if (eval(remainingpartyamount) > 0 && click_count == i) {
						check = 1;
						HighlightRow(i);
						adjamt = CheckNumeric(document
								.getElementById("txt_amount_" + i).value);
						var balamount = document
								.getElementById("txt_balamount_" + i).value;
						totaladj = totaladj + parseFloat(adjamt);
						if (vouchertypeid == "6") {
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
						} else if (vouchertypeid == "21") {
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
			var checkedtotal = 0.00;
			for (var j = 0; j < count; j++) {
				if (document.getElementById("chk_" + j).checked == true) {
					currbalance += eval(document.getElementById("txt_amount_"
							+ j).value);
					checked = 1;
					checkedtotal += eval(document.getElementById("txt_amount_"
							+ j).value)
							- eval(document
									.getElementById("txt_balamount_" + j).value)
				} else {
					var balamount = document.getElementById("txt_balamount_"
							+ j).value;
					document.getElementById("txt_amount_" + j).value = eval(
							balamount).toFixed(2);
					document.getElementById("sp_rebaldispaly_" + j).innerHTML = "";
				}
			}
			if (vouchertypeid == "6") {
				document.getElementById("remaining_party_bal").innerHTML = (eval(partyamount) - eval(currbalance))
						.toFixed(2);
				document.getElementById("hi_remaining_party_bal").value = (eval(partyamount) - eval(currbalance))
						.toFixed(2);
				document.getElementById("sp_rebaldispaly_" + i).innerHTML = "";
			} else if (vouchertypeid == "21") {
				if (checked == 0) {
					document.getElementById("remaining_party_bal").innerHTML = eval(
							partyamount).toFixed(2);
					document.getElementById("hi_remaining_party_bal").value = eval(
							partyamount).toFixed(2);
					document.getElementById("sp_rebaldispaly_" + i).innerHTML = "";
				} else if (checked == 1) {
					if (eval(partyamount) <= eval(currbalance)) {
						document.getElementById("remaining_party_bal").innerHTML = eval(
								0).toFixed(2);
						document.getElementById("hi_remaining_party_bal").value = eval(
								0).toFixed(2);
						document.getElementById("sp_rebaldispaly_" + i).innerHTML = "";
					} else if (eval(partyamount) > eval(currbalance)) {
						document.getElementById("remaining_party_bal").innerHTML = (eval(partyamount) - eval(currbalance))
								.toFixed(2);
						document.getElementById("hi_remaining_party_bal").value = (eval(partyamount) - eval(currbalance))
								.toFixed(2);
						document.getElementById("sp_rebaldispaly_" + i).innerHTML = "";
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
					.getElementById("txt_main_party_amount").value);

			var txt_amount = CheckNumeric(document.getElementById("txt_amount_"
					+ click_count).value);
			var balamount = document.getElementById("txt_balamount_"
					+ click_count).value;
			/* 	alert("partyamount:" + partyamount + "\ntxt_amount:" + txt_amount
						+ "\nbalamount:" + balamount); */

			var remainingpartyamount = CheckNumeric(document
					.getElementById("hi_remaining_party_bal").value);

			var count = CheckNumeric(document.getElementById("txt_count").value);
			var vouchertypeid = "";
			if (click_count != '-1') {
				vouchertypeid = CheckNumeric(document
						.getElementById("txt_vouchertypeid_" + click_count).value);
			}
			if (vouchertypeid == "21") {
				document.getElementById("sp_rebaldispaly_" + click_count).innerHTML = (eval(balamount) + eval(txt_amount))
						.toFixed(2);
			} else if (vouchertypeid == "6") {
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
			if (vouchertypeid == "6") {
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

			} else if (vouchertypeid == "21") {
				if (checked == 0) {
					result = (eval(partyamount) + eval(currbalance));
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

</body>
<!-- END BODY -->
</html>