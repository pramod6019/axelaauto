<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.ManageVoucherType_Update" scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
<link href="../assets/css/summernote.css" rel="stylesheet"
	type="text/css">
</head>
<body onLoad="CheckBillingAddress();CheckReferenceNo();FormFocus()"
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
						<h1><%=mybean.status%>&nbsp;Voucher Type
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
							<li><a href="../portal/manager.jsp">Business Manager</a>
								&gt;</li>
							<li><a href="managevouchertype.jsp?all=yes">List Voucher
									Types</a> &gt;</li>
							<li><a
								href="managevouchertype-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Voucher
									Type</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b> <%=mybean.msg%>
							</b></font>
						</center>
						<!-- 		BODY START -->
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Voucher
									Type
								</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" runat="server"
											method="post">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.<br>
												</font>
											</center><br>
											<div class="form-element6">
												<label>Name<font color="#ff0000">*</font>:
												</label> <input name="vouchertype_name" type="text"
													class="form-control" id="vouchertype_name"
													value="<%=mybean.vouchertype_name%>" />
											</div>
											<div class="form-element6">
												<label>Label<font color="#ff0000">*</font>:
												</label> <input name="txt_vouchertype_label"
													id="txt_vouchertype_label" type="text" class="form-control"
													value="<%=mybean.vouchertype_label%>" size="50"
													maxlength="255" />
											</div>
											<div class="row">
												<div class="form-element6">
													<label>Base Type:</label> <select
														id="dr_vouchertype_base_type"
														name="dr_vouchertype_base_type" class="form-control">
														<%=mybean.PopulateBaseType()%>
													</select>
												</div>
												<div class="form-element6">
													<label>Prefix:</label> <input name="txt_vouchertype_prefix"
														id="txt_vouchertype_prefix" type="text"
														class="form-control"
														value="<%=mybean.vouchertype_prefix%>" size="20"
														maxlength="10" />
												</div>
											</div>
											<div class="form-element6">
												<label>Suffix:</label> <input name="txt_vouchertype_suffix"
													id="txt_vouchertype_suffix" type="text"
													class="form-control" value="<%=mybean.vouchertype_suffix%>"
													size="20" maxlength="10" />
											</div>
											<div class="form-element3 form-element-margin">
												<label>Voucher Default Authorize:</label> <input type="checkbox"
													id="ch_vouchertype_defaultauthorize"
													name="ch_vouchertype_defaultauthorize"
													<%=mybean.PopulateCheck(mybean.vouchertype_defaultauthorize)%> />
											</div>
											<div class="form-element3 form-element-margin">
												<label>Voucher Authorize:</label> <input type="checkbox"
													id="ch_vouchertype_authorize"
													name="ch_vouchertype_authorize"
													<%=mybean.PopulateCheck(mybean.vouchertype_authorize)%> />
											</div>
											<div class="row"></div>
											<div class="form-element3">
												<label>Round Off:</label> <input type="checkbox"
													id="ch_vouchertype_vouchertype_roundoff"
													name="ch_vouchertype_vouchertype_roundoff"
													<%=mybean.PopulateCheck(mybean.vouchertype_roundoff)%> />
											</div>
											<div class="form-element4">
												<label>Round Off Dr: </label>
											<select class="form-control select2"
											id="ledger1" name="ledger1">
												<%=mybean.ledgercheck.PopulateLedgers("0",mybean.vouchertype_roundoff_ledger_dr, mybean.comp_id)%>
											</select>
											</div>
											<div class="form-element4">
												<label>Round Off Cr: </label>
											<select class="form-control select2"
											id="ledger2" name="ledger2">
												<%=mybean.ledgercheck.PopulateLedgers("0",mybean.vouchertype_roundoff_ledger_cr, mybean.comp_id)%>
											</select>
											</div>
											<%if(mybean.vouchertype_id.equals("15")) {%>
											
											<div class="row"></div>
											<div class="form-element3">
												<label>TDS:</label> <input type="checkbox"
													id="ch_vouchertype_tds"
													name="ch_vouchertype_tds"
													<%=mybean.PopulateCheck(mybean.vouchertype_tds)%> />
											</div>
											<div class="form-element4">
												<label>TDS Ledger: </label>
											<select class="form-control select2"
											id="ledger3" name="ledger3">
												<%=mybean.ledgercheck.PopulateLedgers("0",mybean.vouchertype_tds_ledger_id, mybean.comp_id)%>
											</select>
											</div>
											<%} %>
											<div class="row"></div>
											<div class="form-element3">
												<label>Affects Inventory:</label> <input type="checkbox"
													id="ch_vouchertype_affects_inventory"
													name="ch_vouchertype_affects_inventory"
													<%=mybean.PopulateCheck(mybean.vouchertype_affects_inventory)%> />
											</div>
											<div class="form-element3">
												<label>Affects Accounts:</label> <input type="checkbox"
													id="ch_vouchertype_affects_accounts"
													name="ch_vouchertype_affects_accounts"
													<%=mybean.PopulateCheck(mybean.vouchertype_affects_accounts)%> />
											</div>
											<div class="form-element3">
												<label>Mobile:</label> <input type="checkbox"
													id="ch_vouchertype_mobile" name="ch_vouchertype_mobile"
													<%=mybean.PopulateCheck(mybean.vouchertype_mobile)%> />
											</div>
											<div class="form-element3">
												<label>Email:</label> <input type="checkbox"
													id="ch_vouchertype_emails" name="ch_vouchertype_emails"
													<%=mybean.PopulateCheck(mybean.vouchertype_email)%> />
											</div>
											<div class="form-element3">
												<label>DOB:</label> <input type="checkbox"
													id="ch_vouchertype_dob" name="ch_vouchertype_dob"
													<%=mybean.PopulateCheck(mybean.vouchertype_dob)%> />
											</div>
											<div class="form-element3">
												<label title="Do Not Disturb">DND:</label> <input
													type="checkbox" id="ch_vouchertype_dnd"
													name="ch_vouchertype_dnd"
													<%=mybean.PopulateCheck(mybean.vouchertype_dnd)%> />
											</div>
											<div class="form-element3">
												<label>Billing Address:</label> <input type="checkbox"
													id="ch_vouchertype_billing_add"
													name="ch_vouchertype_billing_add"
													<%=mybean.PopulateCheck(mybean.vouchertype_billing_add)%>
													onclick="CheckBillingAddress();" />
											</div>
											<div class="form-element3" id="consignee_row">
												<label>Consignee Address:</label> <input type="checkbox"
													id="ch_vouchertype_consignee_add"
													name="ch_vouchertype_consignee_add"
													<%=mybean.PopulateCheck(mybean.vouchertype_consignee_add)%> />
											</div>
											<div class="form-element3" id="gatepass_row">
												<label>Gate Pass:</label> <input type="checkbox"
													id="ch_vouchertype_gatepass" name="ch_vouchertype_gatepass"
													<%=mybean.PopulateCheck(mybean.vouchertype_gatepass)%> />
											</div>
											<div class="form-element3" id="lrno_row">
												<label>Lr. No.:</label> <input type="checkbox"
													id="ch_vouchertype_lrno" name="ch_vouchertype_lrno"
													<%=mybean.PopulateCheck(mybean.vouchertype_lrno)%> />
											</div>
											<div class="form-element3" id="cashdiscount_row">
												<label>Cash Discount:</label> <input type="checkbox"
													id="ch_vouchertype_cashdiscount"
													name="ch_vouchertype_cashdiscount"
													<%=mybean.PopulateCheck(mybean.vouchertype_cashdiscount)%> />
											</div>
											<div class="form-element3" id="turnover_row">
												<label>Turnover Discount:</label> <input type="checkbox"
													id="ch_vouchertype_turnoverdisc"
													name="ch_vouchertype_turnoverdisc"
													<%=mybean.PopulateCheck(mybean.vouchertype_turnoverdisc)%> />
											</div>
											<div class="form-element6">
												<label>Terms:</label>
												<textarea name="txt_vouchertype_terms" cols="70" rows="4"
													class="form-control" id="txt_vouchertype_terms"><%=mybean.vouchertype_terms%></textarea>
											</div>
											<div class="form-element6 form-element-margin"
												id="gatepass_row">
												<label>Driver No.:</label> <input type="checkbox"
													id="ch_vouchertype_driver_no"
													name="ch_vouchertype_driver_no"
													<%=mybean.PopulateCheck(mybean.vouchertype_driver_no)%> />
											</div>
											<div class="row"></div>
											<div class="form-element3" id="gatepass_row">
												<label>Tempo No.:</label> <input type="checkbox"
													id="ch_vouchertype_tempo_no" name="ch_vouchertype_tempo_no"
													<%=mybean.PopulateCheck(mybean.vouchertype_tempo_no)%> />
											</div>
											<div class="form-element3">
												<label>Reference No. Enable:</label> <input type="checkbox"
													id="ch_vouchertype_ref_no_enable_active"
													name="ch_vouchertype_ref_no_enable_active"
													<%=mybean .PopulateCheck(mybean.vouchertype_ref_no_enable_active)%>
													onclick="CheckReferenceNo();" />
											</div>
											<div class="form-element3" id="refrow">
												<label>Reference No. Mandatory:</label> <input
													type="checkbox" id="ch_vouchertype_ref_no_mandatory_active"
													name="ch_vouchertype_ref_no_mandatory_active"
													<%=mybean .PopulateCheck(mybean.vouchertype_ref_no_mandatory_active)%> />
											</div>
											<div class="form-element6">
												<label>Send Email:</label> <input type="checkbox"
													id="ch_vouchertype_email_enable"
													name="ch_vouchertype_email_enable"
													<%=mybean.PopulateCheck(mybean.vouchertype_email_enable)%> />
											</div>
											<div class="form-element6">
												<label>Email Auto:</label> <input type="checkbox"
													id="ch_vouchertype_email_auto"
													name="ch_vouchertype_email_auto"
													<%=mybean.PopulateCheck(mybean.vouchertype_email_auto)%> />
											</div>
											<div class="form-element6 ">
												<label>Subject:</label> <input
													name="txt_vouchertype_email_sub"
													id="txt_vouchertype_email_sub" type="text"
													class="form-control"
													value="<%=mybean.vouchertype_email_sub%>" size="70"
													maxlength="70" />
											</div>
											<div class="row"></div>
											<div class="form-element6 form-element-center">
												<label>Email Format:</label>
												<textarea name="txt_vouchertype_email_format" cols="70"
													rows="4" class="form-control summernote_1"
													id="txt_vouchertype_email_format"><%=mybean.vouchertype_email_format%></textarea>
											</div>
											<div class="form-element6 form-element-center">
												<label></label>
												<div class="table-responsive table-bordered">
													<table
														class="table table-bordered table-hover table-responsive"
														data-filter="#filter">
														<thead>
															<tr>
																<th align="center" colspan="2">Email Substitution
																	Variables</th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<td align="right">Voucher Id:</td>
																<td align="left">[VOUCHERID]</td>
															</tr>
															<tr>
																<td align="right">Voucher No.:</td>
																<td align="left">[VOUCHERNO]</td>
															</tr>
															<tr>
																<td align="right">Voucher:</td>
																<td align="left">[VOUCHER]</td>
															</tr>
															<tr>
																<td align="right">Customer Name:</td>
																<td align="left">[CUSTOMERNAME]</td>
															</tr>
															<tr>
																<td align="right">Contact Name:</td>
																<td align="left">[CONTACTNAME]</td>
															</tr>
															<tr>
																<td align="right">Contact Mobile 1:</td>
																<td align="left">[CONTACTMOBILE1]</td>
															</tr>
															<tr>
																<td align="right">Contact Mobile 2:</td>
																<td align="left">[CONTACTMOBILE2]</td>
															</tr>
															<tr>
																<td align="right">Contact Email 1:</td>
																<td align="left">[CONTACTEMAIL1]</td>
															</tr>
															<tr>
																<td align="right">Contact Email 2:</td>
																<td align="left">[CONTACTEMAIL2]</td>
															</tr>
															<tr>
																<td align="right">Voucher Date:</td>
																<td align="left">[VOUCHERDATE]</td>
															</tr>
															<tr>
																<td align="right">Amount:</td>
																<td align="left">[AMOUNT]</td>
															</tr>
															<tr>
																<td align="right">Executive Name:</td>
																<td align="left">[EXENAME]</td>
															</tr>
															<tr>
																<td align="right">Executive Phone 1:</td>
																<td align="left">[EXEPHONE1]</td>
															</tr>
															<tr>
																<td align="right">Branch:</td>
																<td align="left">[BRANCH]</td>
															</tr>
														</tbody>
													</table>
												</div>
											</div>
											<div class="row">
												<div class="form-element6">
													<label>Send SMS:</label> <input type="checkbox"
														id="ch_vouchertype_sms_enable"
														name="ch_vouchertype_sms_enable"
														<%=mybean.PopulateCheck(mybean.vouchertype_sms_enable)%> />
												</div>
												<div class="form-element6">
													<label>SMS Format:</label>
													<textarea name="txt_vouchertype_sms_format" cols="70"
														rows="4" class="form-control"
														id="txt_vouchertype_sms_format"
														onkeyup="charcount('txt_vouchertype_sms_format', 'span_txt_vouchertype_sms_format','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.vouchertype_sms_format%></textarea>
													<span id=span_txt_vouchertype_sms_format> 1000
														characters</span>
												</div>
												</div>
											<div class="form-element6 form-element-center">
													<label></label>
													<table
														class="table table-bordered table-hover table-responsive"
														data-filter="#filter">
														<thead>
															<tr>
																<th align="center" colspan="2">SMS Substitution
																	Variables</th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<td align="right">Voucher Id:</td>
																<td align="left">[VOUCHERID]</td>
															</tr>
															<tr>
																<td align="right">Voucher No.:</td>
																<td align="left">[VOUCHERNO]</td>
															</tr>
															<tr>
																<td align="right">Voucher:</td>
																<td align="left">[VOUCHER]</td>
															</tr>
															<tr>
																<td align="right">Customer Name:</td>
																<td align="left">[CUSTOMERNAME]</td>
															</tr>
															<tr>
																<td align="right">Contact Name:</td>
																<td align="left">[CONTACTNAME]</td>
															</tr>
															<tr>
																<td align="right">Contact Mobile 1:</td>
																<td align="left">[CONTACTMOBILE1]</td>
															</tr>
															<tr>
																<td align="right">Contact Mobile 2:</td>
																<td align="left">[CONTACTMOBILE2]</td>
															</tr>
															<tr>
																<td align="right">Contact Email 1:</td>
																<td align="left">[CONTACTEMAIL1]</td>
															</tr>
															<tr>
																<td align="right">Contact Email 2:</td>
																<td align="left">[CONTACTEMAIL2]</td>
															</tr>
															<tr>
																<td align="right">Voucher Date:</td>
																<td align="left">[VOUCHERDATE]</td>
															</tr>
															<tr>
																<td align="right">Amount:</td>
																<td align="left">[AMOUNT]</td>
															</tr>
															<tr>
																<td align="right">Executive Name:</td>
																<td align="left">[EXENAME]</td>
															</tr>
															<tr>
																<td align="right">Executive Phone 1:</td>
																<td align="left">[EXEPHONE1]</td>
															</tr>
															<tr>
																<td align="right">Branch:</td>
																<td align="left">[BRANCH]</td>
															</tr>
														</tbody>
													</table>
												</div>
											<!-- 										</div> -->
											<div class="form-element12">
												<label>Active:</label> <input type="checkbox"
													name="ch_vouchertype_active"
													<%=mybean.PopulateCheck(mybean.vouchertype_active)%> />
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="row">
												<div class="form-element6">
													<label>Entry By:</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.entry_by%>" />
												</div>
												<%
													}
												%>
												<%
													if (mybean.status.equals("Update") && !(mybean.entry_date == null)
															&& !(mybean.entry_date.equals(""))) {
												%>
												<div class="form-element6">
													<label>Entry Date:</label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="row">
												<div class="form-element6">
													<label>Modified By:</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
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
													<label>Modified Date:</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")) {
											%>
											<center>
												<input type="hidden" name="update_button" value="yes">
												<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update Voucher Type"
													onclick="return SubmitFormOnce(document.form1,this);" /> <input
													type="hidden" name="Update" value="yes" />
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
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			document.form1.txt_priorityoppr_name.focus();
			document.getElementById('refrow').style.display = 'none';
			document.getElementById('consignee_row').style.display = 'none';

		}
		function CheckReferenceNo() {
			var check = document.form1.ch_vouchertype_ref_no_enable_active.checked;
			var check1 = document.form1.ch_vouchertype_ref_no_mandatory_active.checked;

			if (check == "1" || check == "on") {
				$('#refrow').show();
				// 			     displayRow('refrow');
				if (check1 == "1" || check1 == "on") {
					document.form1.ch_vouchertype_ref_no_mandatory_active.checked = 1;
				} else {
					document.form1.ch_vouchertype_ref_no_mandatory_active.checked = 0;
				}
			}
			if (check == "0" || check == " ") {
				$('#refrow').hide();
				//             	hideRow('refrow');				
				document.form1.ch_vouchertype_ref_no_mandatory_active.checked = 0;
			}
		}

		function CheckBillingAddress() {
			var check = document.form1.ch_vouchertype_billing_add.checked;
			var check1 = document.form1.ch_vouchertype_consignee_add.checked;

			if (check == "1" || check == "on") {
				$("#consignee_row").show();
				// 			     displayRow('consignee_row');
				if (check1 == "1" || check1 == "on") {
					document.form1.ch_vouchertype_consignee_add.checked = 1;
				} else {
					document.form1.ch_vouchertype_consignee_add.checked = 0;
				}
			}
			if (check == "0" || check == " ") {
				$("#consignee_row").hide();
				// 				hideRow('consignee_row');				
				document.form1.ch_vouchertype_consignee_add.checked = 0;
			}
		}
		var check = document.form1.vouchertype_gatepass.checked;
		if (check == "0" || check == " ") {
			$("#gatepass_row").hide();
			// 				hideRow('gatepass_row');				
			document.form1.ch_vouchertype_gatepass.checked = 0;
		}
		var check = document.form1.ch_vouchertype_lrno.checked;
		if (check == "0" || check == " ") {
			$("#lrno_row").hide();
			// 				hideRow('lrno_row');				
			document.form1.ch_vouchertype_lrno.checked = 0;
		}
		var check = document.form1.ch_vouchertype_cashdiscount.checked;
		if (check == "0" || check == " ") {
			$("#cashdiscount_row").hide();
			// 				hideRow('cashdiscount_row');				
			document.form1.ch_vouchertype_cashdiscount.checked = 0;
		}
		var check = document.form1.ch_vouchertype_turnoverdisc.checked;
		if (check == "0" || check == " ") {
			$("#turnover_row").hide();
			// 				hideRow('turnover_row');				
			document.form1.ch_vouchertype_turnoverdisc.checked = 0;
		}
	</script>
</body>
</HTML>
