<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.Expense_Update" scope="request"/>
<%mybean.doPost(request,response); %>
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

<body onLoad="Displaypaymode();" class="page-container-bg-solid page-header-menu-fixed">
<%@include file="../portal/header.jsp" %>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>&nbsp;<%=mybean.vouchertype_name%></h1>
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
							<li><a href="voucher-list.jsp?all=yes&vouchertype_id=<%=mybean.vouchertype_id%>">List <%=mybean.vouchertype_name%>s </a>
							 &gt; <a href="expense-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Expense</a>:</li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center> <font color="#ff0000"><b><%=mybean.msg%></b></font> </center>
							<form name="formemp" method="post" class="form-horizontal">
							
													<input name="txt_vouchertype_id" type="hidden" id="txt_vouchertype_id" value="<%=mybean.vouchertype_id%>" />
													<input name="hid_voucher_no" type="hidden" id="hid_voucher_no" value="<%=mybean.voucher_no%>">
													<input type="hidden" name="txt_vouchertype_authorize" id="txt_vouchertype_authorize" value="<%=mybean.vouchertype_authorize %>" />
													<input type="hidden" name="txt_vouchertype_defaultauthorize" id="txt_vouchertype_defaultauthorize" value="<%=mybean.vouchertype_defaultauthorize %>" />
													<input type="hidden" name="txt_voucher_authorize" id="txt_voucher_authorize" value="<%=mybean.voucher_authorize %>" />
								                    <input type="hidden" name="txt_voucher_authorize_id" id="txt_voucher_authorize_id" value="<%=mybean.voucher_authorize_id %>" />
											        <input type="hidden" name="txt_voucher_authorize_time" id="txt_voucher_authorize_time" value="<%=mybean.voucher_authorize_time %>" />
												
								<div class="portlet box  container-fluid">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>&nbsp;<%=mybean.vouchertype_name%>
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<center>
												Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.
											</center>
											<input type="hidden" name="txt_voucher_id" id="txt_voucher_id" value="<%=mybean.voucher_id%>" />
										</div>

										<div class="form-element6">
											<label> <%=mybean.vouchertype_name%> Date<font
												color="#ff0000">*</font>:
											</label>
											<%if(mybean.empEditperm.equals("1")){%>
											<input name="txt_voucher_date" type="text"
												class="form-control datepicker" id="txt_voucher_date"
												value="<%=mybean.voucherdate%>" size="11" maxlength="10" />
											<%} else{%>
											<%=mybean.voucherdate%>
											<input name="txt_voucher_date" type="hidden" class="textbox"
												id="txt_voucher_date" value="<%=mybean.voucherdate%>" />
											<%}%>
										</div>

										<div class="form-element6">
											<label> Branch<font color="#ff0000">*</font>:
											</label> <select id="dr_voucher_branch_id"
												name="dr_voucher_branch_id" class="form-control">
												<%=mybean.PopulateBranch(mybean.voucher_branch_id, "", "", "", request)%>
											</select>
										</div>

										<div class="form-element6">
											<label>Party<font color="#ff0000">*</font>:
											</label> <select class="form-control select2" id="accountingsupplier"
												name="accountingsupplier">
												<%=mybean.ledgercheck.PopulateLedgers("31", mybean.voucher_customer_id, mybean.comp_id)%>
											</select>
											<div id="creditcustid_cur_bal">
												<%=mybean.ReturnCustomerCurrBalance(mybean.voucher_customer_id,  mybean.comp_id, mybean.vouchertype_id)%>
											</div>
											<div>
												<a href="../customer/customer-update.jsp?Add=yes">Add Party</a>
											</div>
										</div>
										<div class="form-element6">
											<label>Contact: </label>
											<div id="contact_id">
											<%=mybean.PopulateContact(mybean.voucher_contact_id ,mybean.voucher_customer_id)%>
											</div>
										</div>
										<div class="form-element2" id="addcontact"></div>

										<div class="form-element6">
											<label>Expense Ledger<font color="#ff0000">*</font>:
											</label> <select class="form-control select2" id="exp_ledger_id"
												name="exp_ledger_id">
												<%=mybean.ledgercheck.PopulateExpLedgers("0", mybean.vouchertrans_exp_ledger_id, mybean.comp_id)%>
											</select>
										</div>
										<div class="form-element6">
											<label>Bank Ledger<font color="#ff0000">*</font>:
											</label> <select name="dr_voucher_payment" id="dr_voucher_payment"
												class="form-control" onChange="Displaypaymode();">
												<%=mybean.PopulatePayment()%>
											</select>
										</div>

										<div id="by">
											<div class="form-element6">
												<label>Payment By<font color="#ff0000">*</font>:
												</label> <select name="dr_voucher_paymode" id="dr_voucher_paymode"
													class="form-control" onChange="Displaypaymode(1)">
													<%=mybean.PopulatePayMode()%>
												</select>
											</div>
										</div>

										<div class="form-element6">
											<label>Amount<font color="#ff0000">*</font>:
											</label> <input id="txt_voucher_amount" name="txt_voucher_amount"
												type="text" class="form-control"
												onkeyup="toInteger(this.id);"
												value="<%=mybean.voucher_amount%>" size="12" maxlength="10" />
											<input type="hidden" id="txt_voucher_prev_amount"
												name="txt_voucher_prev_amount"
												value="<%=mybean.voucher_amount%>" size="12" maxlength="10" />
										</div>

										<div class="form-element3" id="chequeno">
											<label>Cheque No<font color="#ff0000">*</font>:
											</label> <input name="txt_vouchertrans_cheque_no" type="text"
												id="txt_vouchertrans_cheque_no"
												onKeyUp="toInteger('txt_vouchertrans_cheque_no')"
												class="form-control"
												value="<%=mybean.vouchertrans_cheque_no%>" size="12"
												maxlength="6" />
										</div>
										<div class="form-element3" id="chequedate">
											<label>Cheque Date<font color="#ff0000">*</font>:
											</label> <input name="txt_vouchertrans_cheque_date" type="text"
												class="form-control datepicker"
												id="txt_vouchertrans_cheque_date"
												value="<%=mybean.vouchertrans_chequedate%>" size="12"
												maxlength="10" />
										</div>
										<div class="form-element6" id="cardno">
											<label>Card No<font color="#ff0000">*</font>:
											</label> <input name="txt_vouchertrans_card_no" type="text"
												id="txt_vouchertrans_card_no"
												onKeyUp="toInteger('txt_vouchertrans_card_no')"
												class="form-control"
												value="<%=mybean.vouchertrans_cheque_no%>" size="12"
												maxlength="16" />
										</div>

										<div class="form-element6" id="txnno">
											<label>Transaction No<font color="#ff0000">*</font>:
											</label> <input name="txt_vouchertrans_transaction_no" type="text"
												id="txt_vouchertrans_transaction_no" class="form-control"
												value="<%=mybean.vouchertrans_transaction_no%>" size="12"
												maxlength="30" />
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
											<label>Reference No.<%if(mybean.vouchertype_ref_no_mandatory.equals("1")){%><font
												color="#ff0000">*</font>
												<%}%> :
											</label> <input name="txt_voucher_ref_no" type="text"
												class="form-control" id="txt_voucher_ref_no"
												value="<%=mybean.voucher_ref_no%>" size="30" maxlength="20" />
										</div>
										<%}%>
										<div class="row"></div>
										<div class="form-element6">
											<label>Narration<font color="#ff0000">*</font>:
											</label>
											<textarea name="txt_voucher_narration" cols="70" rows="4"
												class="form-control" id="txt_voucher_narration"
												onkeyup="charcount('txt_voucher_narration', 'span_txt_voucher_narration','<font color=red>({CHAR} characters left)</font>', '5000')"><%=mybean.voucher_narration%></textarea>
											<span id=span_txt_voucher_narration> 5000 characters</span>
										</div>
										<div class="form-element6">
											<label>Notes: </label>
											<textarea name="txt_voucher_notes" cols="70" rows="4"
												class="form-control" id="txt_voucher_notes"><%=mybean.voucher_notes %></textarea>
										</div>
										<div class="row"></div>
										<div class="form-element6">
											<label>Active:
											</label> <input type="checkbox" name="ch_voucher_active"
												id="ch_voucher_active"
												<%=mybean.PopulateCheck(mybean.voucher_active)%> />
										</div>
										<div class="row"></div>
										<% if (mybean.status.equals("Update")) {%>
										<div class="form-element6">
											<% if (!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {%>
											<label>Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
											<input name="entry_by" type="hidden" id="entry_by"
												value="<%=mybean.unescapehtml(mybean.entry_by)%>" />
											<%}%>
										</div>
										<div class="form-element6">
											<% if (!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {%>
											<label style="vertical-align: middle">Entry Date: <%=mybean.entry_date%></label>
											<input type="hidden" name="entry_date"
												value="<%=mybean.entry_date%>" />
										</div>
										<%}%>
										<div class="form-group"></div>
										<%}%>

										<% if (mybean.status.equals("Update")) {%>
											<div class="form-element6" >
												<% if (!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {%>
												<label>Modified By: </label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>" />
												<%}%>
											</div>
											<div class="form-element6">
												<% if (!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {%>
												<label>Modified Date:</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>" />
												<%}%>
											</div>
										<%}%>
									<div class="row"></div>
										<center>
											<%if (mybean.status.equals("Add")) {%>
											<input name="addbutton" type="submit" class="btn btn-success"
												id="addbutton" value="Add Expense"
												onclick="return SubmitFormOnce(document.formemp, this);" />
											<input type="hidden" name="add_button" value="yes" />
											<%} else if (mybean.status.equals("Update")) {%>
											<input type="hidden" name="update_button" value="yes" /> <input
												name="updatebutton" type="submit" class="btn btn-success"
												id="updatebutton" value="Update Expense"
												onclick="return SubmitFormOnce(document.formemp, this);" />
											<input name="delete_button" type="submit"
												class="btn btn-success" id="delete_button"
												onclick="return confirmdelete(this)" value="Delete Expense" />
											<%}%>
											<input type="hidden" name="voucher_id"
												value="<%=mybean.voucher_id%>" />
										</center>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
<%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp"%>

<script type="text/javascript">

	$(function() {
		$("#accountingsupplier").change(function(){
			var supplier=$("#accountingsupplier").val();
			 if(supplier!=0){
				 $("#addcontact").show();
				 $("#addcontact").html("<a href=../customer/customer-contact-update.jsp?Add=yes&customer_id=" + supplier + ">Add Contact</a>")
					}
				});

		// credit to
		$("#accountingsupplier").change(
				function() {
					showHint( '../accounting/ledger-check.jsp?contact=yes&customer_id=' + $(this).val(), 'contact_id');
					showHint( '../accounting/ledger-check.jsp?currbal=yes&customer_id=' + $(this).val(), 'creditcustid_cur_bal');
				});

	});

	function FormFocus() {
		document.formemp.txt_voucher_amount.focus();
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
		} else if (payment == "2" && paymode == "6") {
			$('#txnno').show();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
		} else if (payment == "2" && paymode == "4") {
			$('#txnno').show();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
		} else if (payment == "2" && paymode == "7") {
			$('#txnno').hide();
			$('#chequeno').hide();
			$('#chequedate').hide();
			$('#cardno').hide();
		}
	}
</script>

</body>
</HTML>
<!--created by Dhanesh on 26-07-08-->	