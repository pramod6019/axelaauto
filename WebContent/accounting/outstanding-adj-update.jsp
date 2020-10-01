<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Outstanding_Adj_Update"
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
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css"
	rel="stylesheet" media="screen" type="text/css" />
<link
	href="../Library/theme<%=mybean.GetTheme(request)%>/font-awesome.css"
	rel="stylesheet" media="screen" type="text/css" />
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript"
	src="../Library/jquery-ui.js?target=<%=mybean.jsver%>"></script>

<script type="text/javascript"
	src="../Library/Validate.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript">
	function showzer0balance() {
		var party_id = document.getElementById("ledger").value;
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

	// for displaying remaining balance of un allocated vouchers
	function CalcBalanceCheck(i) {
		var totalenteredamount = 0.00;
		var invoiceamount = 0.00;
		var totalbills = 0, invcount = 0;
		var checked = false;
		var balinvoiceamount = document
				.getElementById("txt_hid_invoice_balance").value;
		totalbills = document.getElementById("txt_unallocated_count").value;
		invcount = document.getElementById("txt_count").value;
		for (j = 0; j < invcount; j++) {
			var invchecked = document.getElementById("chk_" + j).checked;
			if (invchecked == true) {
				invoiceamount += eval(document.getElementById("txt_balamount_"+ j).value);
			}
		}

		var enteredamount = document.getElementById("txt_unallocated_amount_"
				+ i).value;
		var amount = document
				.getElementById("txt_unallocated_main_amount_" + i).value;
		if (eval(balinvoiceamount) > 0) {
			checked = document.getElementById("chk_unallocated_" + i).checked;
		}
		if (checked == true) {
			if (enteredamount <= amount) {
				var diff = eval(amount - enteredamount);
				document.getElementById("txt_unallocated_bal_" + i).innerHTML = diff
						.toFixed(2);
			} else if (enteredamount > amount) {
				document.getElementById("txt_unallocated_bal_" + i).innerHTML = amount;
				document.getElementById("txt_unallocated_amount_" + i).value = amount;
			}

			if (eval(enteredamount) > eval(balinvoiceamount)) {
				document.getElementById("txt_invoice_balance").innerHTML = "Balance: 0.00";
				document.getElementById("txt_hid_invoice_balance").value = 0.00;
				document.getElementById("txt_unallocated_amount_" + i).value = eval(
						balinvoiceamount).toFixed(2);
				document.getElementById("txt_unallocated_bal_" + i).innerHTML = eval(
						enteredamount - balinvoiceamount).toFixed(2);
			} else if (eval(enteredamount) <= eval(balinvoiceamount)) {
				document.getElementById("txt_invoice_balance").innerHTML = "Balance: "
						+ eval(balinvoiceamount - enteredamount).toFixed(2);
				document.getElementById("txt_hid_invoice_balance").value = eval(
						balinvoiceamount - enteredamount).toFixed(2);
				document.getElementById("txt_unallocated_amount_" + i).value = eval(
						enteredamount).toFixed(2);
				document.getElementById("txt_unallocated_bal_" + i).innerHTML = eval(
						amount - enteredamount).toFixed(2);
			}

		} else {
			document.getElementById("txt_unallocated_amount_" + i).value = amount;
			document.getElementById("txt_unallocated_bal_" + i).innerHTML = "";
			document.getElementById("chk_unallocated_" + i).checked = false;
		}

		for (k = 0; k < totalbills; k++) {
			checked = document.getElementById("chk_unallocated_" + k).checked;
			if (checked == true) {
				totalenteredamount = eval(totalenteredamount
						+ eval(parseFloat(document
								.getElementById("txt_unallocated_amount_" + k).value)));
			}
		}

		if (eval(invcount) == 0) {
			document.getElementById("txt_invoice_balance").innerHTML = "Balance: 0.00";
			document.getElementById("txt_hid_invoice_balance").value = 0.00;
		} else {
			document.getElementById("txt_invoice_balance").innerHTML = "Balance: "
					+ eval(invoiceamount - totalenteredamount).toFixed(2);
			document.getElementById("txt_hid_invoice_balance").value = eval(
					invoiceamount - totalenteredamount).toFixed(2);
		}

	}

	function CalcBalanceText(i) {
		var invoiceamount = 0.00;
		var totalenteredamount = 0.00;
		var calcinvamount = 0.00;
		var invcount = document.getElementById("txt_count").value;
		var balinvoiceamount = document
				.getElementById("txt_hid_invoice_balance").value;
		for (j = 0; j < invcount; j++) {
			var invchecked = document.getElementById("chk_" + j).checked;
			if (invchecked == true) {
				invoiceamount = document.getElementById("txt_balamount_" + j).value;
			}
		}

		var totalbills = document.getElementById("txt_unallocated_count").value;
		for (k = 0; k < totalbills; k++) {
			var checked = document.getElementById("chk_unallocated_" + k).checked;
			if (checked == true) {
				var enteredamount = document
						.getElementById("txt_unallocated_amount_" + k).value;
				totalenteredamount = eval(eval(totalenteredamount)
						+ eval(enteredamount));
			}
		}

		document.getElementById("txt_invoice_balance").innerHTML = "Balance: "
				+ eval(invoiceamount - totalenteredamount).toFixed(2);
		document.getElementById("txt_hid_invoice_balance").value = eval(
				invoiceamount - totalenteredamount).toFixed(2);

		var enteredamount = document.getElementById("txt_unallocated_amount_"
				+ i).value;

		var amount = document
				.getElementById("txt_unallocated_main_amount_" + i).value;

		var checked = document.getElementById("chk_unallocated_" + i).checked;
		if (checked == true) {
			if (eval(enteredamount) <= eval(amount)) {
				var diff = eval(amount - enteredamount);
				document.getElementById("txt_unallocated_bal_" + i).innerHTML = diff
						.toFixed(2);
			} else if (eval(enteredamount) > eval(amount)) {
				document.getElementById("txt_unallocated_bal_" + i).innerHTML = amount;
				document.getElementById("txt_unallocated_amount_" + i).value = amount;
			}
		} else {
			document.getElementById("txt_unallocated_amount_" + i).value = amount;
			document.getElementById("txt_unallocated_bal_" + i).innerHTML = "";
		}

	}
</script>
<script type="text/javascript">
	$(function() {
		// credit to
		$("#ledger")
				.change(
						function() {
							showHint(
									'../accounting/ledger-check.jsp?contact=yes&customer_id='
											+ $(this).val(), 'dr_contact_id');
							// for displaying all invoices
							showHint(
									'../accounting/ledger-check.jsp?invoicedetail=yes&customer_id='
											+ $(this).val(), 'customer_invoice');
							// for displaying receipt/payment/credit/debit note/returns
							showHint(
									'../accounting/ledger-check.jsp?unallocateddetail=yes&customer_id='
											+ $(this).val(), 'customer_bills');

							document.getElementById("txt_invoice_balance").innerHTML = "Balance: 0.00";
							document.getElementById("txt_hid_invoice_balance").value = 0.00;

						});
	});

	$(function() {

		$("#txt_voucher_date").datepicker({
			showButtonPanel : true,
			//dateFormat: "dd/mm/yy"
			dateFormat : "dd/mm/yy"
		});
	});

	$(function() {
		$("#txt_vouchertrans_cheque_date").datepicker({
			showButtonPanel : true,
			//dateFormat: "dd/mm/yy"
			dateFormat : "dd/mm/yy"
		});
	});

	function FormFocus() {
		document.formemp.ledger.focus();
	}

	function PopulateBranchExecutive() {
		var branch_id = document.getElementById('dr_branch_id').value;
		showHint('invoice-check.jsp?voucher_branch_id=' + branch_id
				+ '&branch_exe=yes', 'dr_executive');
	}
</script>
<script>
	function PopulatePendingBalance() {
		var customer_id = document.getElementById('ledger').value;
		if (customer_id != 0) {
			showHint(
					'../accounting/ledger-check.jsp?customer_contact=yes&invoicedetail=yes&customer_id='
							+ customer_id, 'customer_invoice');
		}
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
	<%@include file="../portal/header.jsp"%>
	<TABLE width="98%" border="0" align="center" cellPadding="0"
		cellSpacing="0">
		<TR>
			<TD><a href="../portal/home.jsp">Home</a> &gt; <a
				href="index.jsp">Accounting</a> <%-- &gt; 
				<a
				href="voucher-list.jsp?all=yes&vouchertype_id=<%=mybean.vouchertype_id%>&voucherclass_id=<%=mybean.voucherclass_id%>">List
					<%=mybean.vouchertype_name%>s --%> </a> &gt; <a
				href="outstanding-adj-update.jsp?add=yes"> Outstanding
					Adjustment</a>:</TD>
		</TR>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<TR>
			<TD align="center" vAlign="top"><font color="#ff0000"><b><%=mybean.msg%>
				</b></font></TD>
		</TR>
		<TR>
			<TD height="400" align="center" vAlign="top"><form
					name="formemp" method="post">
					<table width="100%" border="1" align="center" cellpadding="0"
						cellspacing="0" class="tableborder">
						<tr>
							<td><table width="100%" border="0" align="center"
									cellpadding="0" cellspacing="0" class="listtable formdata">
									<tbody>
										<tr align="center">
											<th colspan="3">Outstanding Adjustment</th>
										</tr>
										<tr>
											<td></td>
											<td colspan="2"><font size="1">Form fields marked
													with a red asterisk <font color=#ff0000>*</font> are
													required.
											</font></td>
										</tr>
										<tr valign="middle">
											<td align="right">Date<font color="#ff0000">*</font>:
											</td>
											<td align=left><input name="txt_voucher_date"
												type="text" class="textbox" id="txt_voucher_date"
												value="<%=mybean.voucherdate%>" size="11" maxlength="10" />

											</td>
											<td align="center" valign="middle"><input
												type="checkbox" id="chk_zero_bal" name="chk_zero_bal"
												class="checkbox" onclick="showzer0balance()"></input>:
												Include Zero Balance</td>
										</tr>
										<tr>
											<td align="right">Branch<font color=red>*</font>:
											</td>
											<td colspan="2"><select name="dr_branch" id="dr_branch"
												class="selectbox">
													<%=mybean.PopulateBranch(mybean.voucher_branch_id, "", "", "", request)%>
											</select></td>
										</tr>
										<tr>
											<td align="right" valign='top'>Party Ledger<font
												color="#ff0000">*</font>:
											</td>
											<td align="left" valign="top"><input tabindex="-1"
												class="bigdrop select2-offscreen" id="ledger" name="ledger"
												style="width: 250px" value="<%=mybean.voucher_customer_id%>"
												type="hidden"></td>
											<td valign=top align=left id="customer_cur_bal"></td>
										</tr>


										<tr>
											<td height="29" align="right" valign="top">Contact<font
												color="#ff0000">*</font>:
											</td>
											<td align="left"><select name="dr_contact_id" class="selectbox" id="dr_contact_id" style="width: 250px">
											</select></td>
											<td align="center" valign="middle"><input type='hidden'
												id='txt_hid_invoice_balance' name='txt_hid_invoice_balance' />
												<font color='red'><b><span
														id='txt_invoice_balance'></span></b></font></td>
										</tr>
										<tr>
											<th colspan="3">Un-Allocated Bills List</th>
										</tr>
										<tr valign="middle">
											<td align="center" valign="middle" colspan="3"><div
													id="customer_bills"
													style="height: 300px; overflow-y: scroll"></div></td>
										</tr>
										<tr>
											<th colspan="3">Invoices List</th>
										</tr>
										<tr valign="middle">
											<td align="center" valign="middle" colspan="3"><div
													id="customer_invoice"
													style="height: 300px; overflow-y: scroll"></div></td>
										</tr>
										<tr align="center">
											<td colspan="3" valign=center>
												<%
													if (mybean.status.equals("Add")) {
												%> <input name="addbutton" type="submit" class="button"
												id="addbutton" value="Add Outstanding Adjustment"
												onClick="return SubmitFormOnce(document.formemp, this);" />
												<input type="hidden" name="add_button" value="yes">
												<%
													}
												%>

											</td>
										</tr>
									</tbody>
								</table></td>
						</tr>
					</table>
				</form></TD>
		</TR>
	</TABLE>
	<%@include file="../Library/admin-footer.jsp"%>
</body>
</HTML>
<!--created by Dhanesh on 26-07-08-->