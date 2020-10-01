<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Bill_Update" scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%= mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>   
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>

</HEAD>
<body onLoad="getlocationno(); <%if (mybean.status.equals("Add")) {%>getVoucherno();list_cart_items();<%}%>
	<%if (mybean.status.equals("Update")) {%>list_cart_items();<%}%>"
class="page-container-bg-solid page-header-menu-fixed" >
	<%@include file="../portal/header.jsp"%>


	<div id="dialog-modal"></div>
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
							<li><a
								href="../accounting/voucher-list.jsp?all=yes&vouchertype_id=<%=mybean.vouchertype_id%>">List
									<%=mybean.vouchertype_name%>s
							</a> &gt;</li>
							<li><a href="bill-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									Bill</a>:</li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<form name="form1" id="form1" method="post"
								class="form-horizontal">
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>&nbsp;<%=mybean.vouchertype_name%>
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">

											<center>
												Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</center>
										<input type="hidden" name="txt_config_inventory_current_stock" id="txt_config_inventory_current_stock" value="<%=mybean.config_inventory_current_stock%>" />
											<div id="getlocation"></div>

											<div class="form-element7">
												<input type="hidden" name="txt_paymode_count" id="txt_paymode_count" value="<%=mybean.paymode_count%>" />
												<input type="hidden" name="cart_id" id="cart_id" />
												<input type="hidden" name="txt_voucher_id" id="txt_voucher_id" value="<%=mybean.voucher_id%>" />
												<input type="hidden" name="txt_voucherclass_id" id="txt_voucherclass_id" value="<%=mybean.voucherclass_id%>" />
												<input type="hidden" name="txt_vouchertype_id" id="txt_vouchertype_id" value="<%=mybean.vouchertype_id%>" />
												<input type="hidden" id="txt_voucher_prev_grandtotal" name="txt_voucher_prev_grandtotal" value="<%=mybean.voucher_prev_grandtotal%>" />
												<input type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%=mybean.branch_id%>" />
												<input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>" />
												<input type="hidden" name="voucher_contact" id="voucher_contact" value="" />
												<input type="hidden" name="txt_voucher_contact_id" id="txt_voucher_contact_id" value="<%=mybean.voucher_contact_id%>" />
												<input type="hidden" name="txt_rateclass_id	" id="txt_rateclass_id	" value="<%=mybean.rateclass_id%>" />
												<input type="hidden" name="txt_item_model_id" id="txt_item_model_id" value="<%=mybean.item_model_id%>" />
												<input type="hidden" name="hid_voucher_no" id="hid_voucher_no" value="<%=mybean.voucher_no%>" />
												<input type="hidden" id="lead_id" name="lead_id" value="<%=mybean.voucher_lead_id%>" />
												<input type="hidden" id="hid_check_voucherno" name="hid_check_voucherno" value="<%=mybean.check_voucher_no%>" />
												<input type="hidden" id="hid_check_branch" name="hid_check_branch" value="<%=mybean.checkbranch%>" />
												<input type="hidden" id="span_customer_name" name="span_customer_name" value="<%=mybean.bill_customer_name%>" />
												<input type="hidden" id="span_contact_name" name="span_contact_name" value="<%=mybean.bill_contact_name%>" />
												<input type="hidden" id="txt_vouchertype_mobile" name="txt_vouchertype_mobile" value="<%=mybean.vouchertype_mobile%>" />
												<input type="hidden" id="txt_vouchertype_email" name="txt_vouchertype_email" value="<%=mybean.vouchertype_email%>" />
												<input type="hidden" id="txt_vouchertype_dob" name="txt_vouchertype_dob" value="<%=mybean.vouchertype_dob%>" />
												<input type="hidden" id="txt_vouchertype_dnd" name="txt_vouchertype_dnd" value="<%=mybean.vouchertype_dnd%>" />
												<input type="hidden" name="txt_location_id" id="txt_location_id" value="<%=mybean.voucher_location_id%>" />
												<input type="hidden" name="txt_session_id" id="txt_session_id" value="<%=mybean.session_id%>" />
												<%-- <input type="hidden" id="txt_location_name" name="txt_location_name" value="<%=mybean.location_name%>"/> --%>
												<input type="hidden" name="txt_voucher_so_id" id="txt_voucher_so_id" value="<%=mybean.voucher_so_id%>" />
												<input type="hidden" name="txt_voucher_delnote_id" id="txt_voucher_delnote_id" value="<%=mybean.voucher_delnote_id%>" />
												<input type="hidden" name="uom_id" id="uom_id" />
												<input type="hidden" name="txt_gst_type" id="txt_gst_type" value="<%=mybean.gst_type %>" />
												<input type="hidden" name="txt_vouchertype_authorize" id="txt_vouchertype_authorize" value="<%=mybean.vouchertype_authorize %>" />
												<input type="hidden" name="txt_vouchertype_defaultauthorize" id="txt_vouchertype_defaultauthorize" value="<%=mybean.vouchertype_defaultauthorize %>" />
												<input type="hidden" name="txt_voucher_authorize" id="txt_voucher_authorize" value="<%=mybean.voucher_authorize %>" />
                                                <input type="hidden" name="txt_voucher_authorize_id" id="txt_voucher_authorize_id" value="<%=mybean.voucher_authorize_id %>" />
											    <input type="hidden" name="txt_voucher_authorize_time" id="txt_voucher_authorize_time" value="<%=mybean.voucher_authorize_time %>" />
											    <input type="hidden" name="txt_vouchertype_roundoff" id="txt_vouchertype_roundoff" value="<%=mybean.vouchertype_roundoff %>" />
											    <input type="hidden" name="txt_vouchertype_roundoff_ledger_cr" id="txt_vouchertype_roundoff_ledger_cr" value="<%=mybean.vouchertype_roundoff_ledger_cr %>" />
											    <input type="hidden" name="txt_vouchertype_roundoff_ledger_dr" id="txt_vouchertype_roundoff_ledger_dr" value="<%=mybean.vouchertype_roundoff_ledger_dr %>" />
												<div class="form-element6">
													<label> Branch: </label>
													<input type="hidden" name="txt_branch_name" id="txt_branch_name" value="<%=mybean.branch_name%>" />
													<%if (mybean.emp_branch_id.equals("0")) {%>
													<select name="dr_branch_id" class="form-control" id="dr_branch_id" onchange="PopulateLocation();" >
					                                    <%=mybean.PopulateBranch(mybean.branch_id, "", "", "", request)%>
					                                  </select>
<%-- 													<br> <a href="../accounting/accounting-branch.jsp?para=bill&vouchertype_id=<%=mybean.vouchertype_id%>">(Change Branch)</a> --%>
													<%}else{%>
													<a
														href="../portal/branch-summary.jsp?branch_id=<%=mybean.branch_id%>"><%=mybean.branch_name%></a>
													<input type="hidden" name="txt_branch_name"
														id="txt_branch_name" value="<%=mybean.branch_name%>" />
													<%}%>
												</div>

												<div class="form-element6">
													<label> <%if (mybean.vouchertype_id.equals("7")) {%>
														Date<b><font color="#ff0000">*</font></b>: <%}%>
													</label>

													<%if (mybean.vouchertype_id.equals("7")) {%>
													<%if (mybean.empEditperm.equals("1")) {%>
													<input name="txt_voucher_date" type="text"
														class="form-control datepicker" id="txt_voucher_date"
														value="<%=mybean.voucherdate%>" size="12" maxlength="10" />

													<%} else {%>
													<%=mybean.voucherdate%>
													<input name="txt_voucher_date" type="hidden"
														class="form-control datepicker" id="txt_voucher_date"
														value="<%=mybean.voucherdate%>" />
													<%}%>

													<%}%>
												</div>
<div class='row'></div>

												<div class="form-element6">
													<label>Location <font color="#ff0000">*</font>:
													</label>
													<span id="span_location">
													 <select name="dr_location_id" class="form-control"
														id="dr_location_id" onchange="getlocationno();">
														<%=mybean.PopulateLocation(mybean.branch_id)%>
													</select>
													</span>
												</div>
<div class="form-element6">	
												<%if(mybean.status.equals("Update")
														&& (mybean.vouchertype_defaultauthorize.equals("1") || mybean.vouchertype_authorize.equals("1"))) {%>
													<label>Voucher No.<font color="#ff0000">*</font>:</label>
														<span id='voucherno'>
															<%if (!mybean.voucher_id.equals("0")) { %>
																<input type='text' class='form-control' id='txt_voucher_no' name='txt_voucher_no' value=<%= mybean.voucher_no%> onKeyUp="toInteger('txt_voucher_no','Voucherno')" />
															<%} %>
														</span>	
														<%} else{%>
															<span id='voucherno'>
																<input type='hidden' name='txt_voucher_no' id='txt_voucher_no' value='<%= mybean.voucher_no%>' />
															</span>
														<%}%>													
												</div>
												<div class="form-element12">
													<%
											if(mybean.status.equals("Add") 
													&& mybean.customer_id.equals("0")
		   											&& mybean.contact_id.equals("0")
		   											&& !mybean.ledger_add.equals("yes")){%>

													<div id="customer_name">
														<div class="form-element12 form-element">
															<label>Customer<font color="#ff0000">*</font>:(<a
																href="ledger-add.jsp?add=yes&vouchertype_id=<%=mybean.vouchertype_id%>&voucherclass_file=<%=mybean.voucherclass_file%>&customer=yes&branch_id=<%=mybean.branch_id%>&vouchertype_mobile=<%=mybean.vouchertype_mobile%>&vouchertype_email=<%=mybean.vouchertype_email%>&vouchertype_dob=<%=mybean.vouchertype_dob%>&vouchertype_dnd=<%=mybean.vouchertype_dnd%>">Add
																	Ledger</a>)
															</label> <select class="form-control select2"
																id="accountingcustomer" name="accountingcustomer">
																<%=mybean.ledgercheck.PopulateLedgers("31", mybean.voucher_customer_id, mybean.comp_id)%>
															</select> <span valign=top align=left id="customer_cur_bal"><%=mybean.ReturnCustomerCurrBalance(
																		mybean.voucher_customer_id, mybean.comp_id, mybean.vouchertype_id)%></span>
														</div>
														<!-- 																<div class="form-element4"> -->
														<%-- 																	<a href="ledger-add.jsp?add=yes&vouchertype_id=<%=mybean.vouchertype_id%>&voucherclass_file=<%=mybean.voucherclass_file%>&customer=yes&branch_id=<%=mybean.branch_id%>&vouchertype_mobile=<%=mybean.vouchertype_mobile%>&vouchertype_email=<%=mybean.vouchertype_email%>&vouchertype_dob=<%=mybean.vouchertype_dob%>&vouchertype_dnd=<%=mybean.vouchertype_dnd%>">Add --%>
														<!-- 																		Ledger</a> -->
														<!-- 																</div> -->
													</div>

													<div id="contact_name">
														<div class="form-element12 form-element">
															<label>Contact<font color="#ff0000">*</font>:
															</label>
															<!-- 																	<select name="dr_contact_id" class="form-control" -->
															<!-- 																		id="dr_contact_id"> -->
															<%-- 																		<%=mybean.PopulateContact(mybean.voucher_customer_id)%> --%>
															<!-- 																	</select> -->

															<span id="dr_contact_id"> <select
																name="dr_contact_id" class="form-control"
																id="dr_contact_id">
																	<%=mybean.PopulateContact(mybean.voucher_customer_id)%></span>

														</div>
													</div>
													<%} else if(mybean.status.equals("Add") && !mybean.customer_id.equals("0") && !mybean.contact_id.equals("0")){%>
													<div class="form-element12">
														<label>Customer: </label> <b><%=mybean.bill_customer_name%></b>
														<input name="span_acct_id" type="hidden" id="span_acct_id"
															value="<%=mybean.customer_id%>"> <input
															name="acct_id" type="hidden" id="acct_id"
															value="<%=mybean.bill_customer_id%>"> <input
															name="span_ledger_id" type="hidden" id="span_ledger_id"
															value="<%=mybean.vouchertrans_ledger_id%>">
													</div>

													<div class="form-element12">
														<label>Contact: </label> <b><%=mybean.bill_contact_name%></b>&nbsp;
														<a href="#" id="contact_link">(Select Contact)</a> <input
															name="span_cont_id" type="hidden" id="span_cont_id"
															value="<%=mybean.contact_id%>"> <input
															name="cont_id" type="hidden" id="cont_id"
															value="<%=mybean.bill_contact_id%>">
														<div id="dialog-modal"></div>
													</div>
													<%}%>

													<%if(mybean.status.equals("Update") || mybean.ledger_add.equals("yes") || (!mybean.vouchertrans_ledger_id.equals("0") && !mybean.voucher_contact_id.equals("0"))){%>
													<div class="form-element12">
														<label>Customer<font color="#ff0000">*</font>:
														</label> <select class="form-control select2"
															id="accountingcustomer" name="accountingcustomer">
															<%=mybean.ledgercheck.PopulateLedgers("31", mybean.voucher_customer_id, mybean.comp_id)%>
														</select>
													<a href="ledger-add.jsp?add=yes&vouchertype_id=<%=mybean.vouchertype_id%>
													&voucherclass_file=<%=mybean.voucherclass_file%>&customer=yes&branch_id=<%=mybean.branch_id%>
													&vouchertype_mobile=<%=mybean.vouchertype_mobile%>&vouchertype_email=<%=mybean.vouchertype_email%>
													&vouchertype_dob=<%=mybean.vouchertype_dob%>&vouchertype_dnd=<%=mybean.vouchertype_dnd%>">Add Ledger</a>
													</div>
													
														
													
													<div class="form-element12">
														<label>Contact<font color="#ff0000">*</font>:
														</label> <select name="dr_contact_id" class="form-control"
															id="dr_contact_id">
															<%=mybean.PopulateContact(mybean.voucher_customer_id)%>
														</select>

													</div>
													<%}%>
												</div>
												<div class="form-element12">
													<label>Rate Class<font color="#ff0000">*</font>:
													</label> <select name="dr_voucher_rateclass_id"
														class="form-control" id="dr_voucher_rateclass_id">
														<%=mybean.PopulateBranchClass(mybean.customer_branch_id, mybean.comp_id)%>
													</select>
												</div>
												<div class="form-element12">
													<div id="invoice_details" align="left"
														style="height: 400px; overflow-y: scroll"></div>
												</div>
											</div>
											<div class="form-element5">
												<div class="form-element12">
													<label>Search: </label> <input name="txt_search"
														type="text" class="form-control" id="txt_search" value=""
														size="30" maxlength="255" onKeyUp="InvoiceItemSearch();" />
													<div class="admin-master">
														<a href="../inventory/inventory-item-list.jsp?all=yes"
															title="Manage Item"></a>
													</div>
												</div>
												<div class="form-element12">
													<label> </label> <select name="dr_item_cat_id"
														id="dr_item_cat_id" onchange="InvoiceItemSearch();"
														class="form-control">
														<%=mybean.PopulateItemGroup()%>
													</select>
												</div>
												<div class="form-element12">
													<label></label>
													<div class="hint" id="hint_search_item"
														style="text-align: center;">Enter your search
														parameter!</div>
													<div class="hint" id="itemdetails"></div>
												</div>
											</div>

											<!-- 									Portlet End -->

											<div class="form-element12">
												<p></p>
											</div>
											<div class="form-element6">
												<!-- 								<div class="container-fluid col-md-12"> -->
												<label>Executive<font color="#ff0000">*</font>:
												</label> <select name="dr_executive" id="dr_executive"
													class="form-control">
													<%=mybean.PopulateExecutives(mybean.emp_id, mybean.comp_id)%>
												</select>

												<%if (mybean.comp_module_inventory.equals("1") && mybean.config_inventory_current_stock.equals("1")) {%>
												<label>Location <%--<--%=mybean.config_inventory_location_name%>--%>
													<font color="#ff0000">*</font>:
												</label> <a
													href="../inventory/inventory-location-list.jsp?location_id=<%=mybean.voucher_location_id%>">
													<%//=mybean.location_name%>
												</a>
												<%}%>
											</div>
											<div class="row"></div>
											<%if(mybean.vouchertype_billing_add.equals("1")){%>
											<div class="form-element6">
												<label> <b>Billing Address</b> <%
 	//if((mybean.voucher_contact_id.equals("0") || mybean.voucher_contact_id.equals("")) && mybean.add.equals("yes")){
 %> <!--<div id="copy_cont_address_link"> (<a href="javascript:CopyContactAddress();">Same as Contact</a>)</div> -->

													<%
														//}
													%>

												</label>
												<div class="container-fluid">
													<label>Address<font color="#ff0000">*</font>:
													</label>
													<textarea name="txt_voucher_billing_add" rows="4"
														class="form-control" id="txt_voucher_billing_add"
														onKeyUp="charcount('txt_voucher_billing_add', 'span_txt_voucher_billing_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_billing_add%></textarea>
													<span id="span_txt_voucher_billing_add"> (255
														Characters)</span>
													</center>
												</div>
											</div>
											<%if(mybean.vouchertype_consignee_add.equals("1")){%>
											<div class="form-element6">
												<label><center>
														<b>Consignee Address (<a
															href="javascript:CopyConsigneeAddress();">Same as
																Billing</a>)
														</b>
													</center> </label>
												<div class="container-fluid">
													<label>Address<font color="#ff0000">*</font>:
													</label>
													<textarea name="txt_voucher_consignee_add" rows="4"
														class="form-control" id="txt_voucher_consignee_add"
														onKeyUp="charcount('txt_voucher_consignee_add', 'span_txt_voucher_consignee_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_consignee_add%></textarea>
													<span id="span_txt_voucher_consignee_add"> (255
														Characters)</span>
												</div>
											</div>
											<%}%>
											<% } %>

											<%if(mybean.status.equals("Add")){%>
											<%if(mybean.msg.equals("") && !mybean.addB.equals("yes")){%>
											<div class='form-body'>
												<div class='form-group'>
													<div class="form-element4 form-element">
														<label>Payment<font color="#ff0000">*</font>: </label> 
														<select name="dr_payment1" id="dr_payment1"
															class="form-control" onChange="Displaypaymode(1)">
															<%=mybean.PopulatePayment()%>
														</select>
													</div>
													<div class="form-element4 form-element">
														<label>By<font color="#ff0000">*</font>:
														</label> <select name="dr_paymode1" id="dr_paymode1"
															class="form-control" onChange="Displaypaymode(1)">
															<%=mybean.PopulatePaymode()%>
														</select>
													</div>
													<div class="form-element2 form-element">
														<label>Amount<font color="#ff0000">*</font>:
														</label> <input name="txt_bill_amt1"
															onKeyUp="toNumber('txt_bill_amt1')" id="txt_bill_amt1"
															type="text" class="form-control" maxlength="10" size="11">
													</div>

													<div class="form-element2 form-element-margin">
														<img src='../admin-ifx/add.png' align="middle" class="add"
															type="button" />&nbsp;
													</div>
													<div id='displayrow1'></div>
												</div>
											</div>
											<% } else { %>
											<%=mybean.AddRowGroupStr%>
											<%}%>
											<%}%>
											<%if(mybean.status.equals("Update")){%>
											<%if(mybean.msg.equals("") && !mybean.updateB.equals("yes")){%>
											<%=mybean.getPayDetails(mybean.voucher_id, mybean.comp_id)%>
											<%} else {%>
											<%=mybean.AddRowGroupStr%>
											<%}%>
											<%}%>
											<%if(mybean.vouchertype_ref_no_enable.equals("1")){%>
											<div class="form-element6">
												<label>Reference No. <%if(mybean.vouchertype_ref_no_mandatory.equals("1")){%>
													<font color="#ff0000">*</font>: <%}%></label> <input
													name="txt_voucher_ref_no" type="text" class="form-control"
													id="txt_voucher_ref_no" value="<%=mybean.voucher_ref_no%>"
													size="32" maxlength="50" />
											</div>
											<%}%>
											<div class="form-element6">
												<label>Active: </label> <input id="chk_voucher_active"
													type="checkbox" name="chk_voucher_active"
													<%=mybean.PopulateCheck(mybean.voucher_active)%> />
												</td>

											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label>Notes:
													</td>
												</label>

												<textarea name="txt_voucher_notes" cols="70" rows="4"
													class="form-control" id="txt_voucher_notes"><%=mybean.voucher_notes%></textarea>

											</div>
											<div class="row"></div>
											<%if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {%>
											<div class="form-element6">
												<label>Entry By: <%=mybean.unescapehtml(mybean.voucher_entry_by)%></label>
												<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.unescapehtml(mybean.voucher_entry_by)%>" />
											</div>
											<div class="form-element6">
												<label>Entry Date: <%=mybean.entry_date%></label> <input
													type="hidden" id="entry_date" name="entry_date"
													value="<%=mybean.entry_date%>" />
												</td>

											</div>
											<%} %>

											<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {%>
											<div class="form-element6">
												<label>Modified By: <%=mybean.unescapehtml(mybean.voucher_modified_by)%></label>
												<input type="hidden" id="modified_by" name="modified_by"
													value="<%=mybean.unescapehtml(mybean.voucher_entry_by)%>" />
											</div>
											<div class="form-element6">
												<label> Modified Date: <%=mybean.modified_date%></label> <input
													type="hidden" id="modified_date" name="modified_date"
													value="<%=mybean.modified_date%>" />

											</div>
											<%
										}
									%>


										</div>
										<input id="txt_soid" name="txt_soid" value="0" hidden />
											<%if (mybean.status.equals("Add")) {%>
											<center>
												<input name="addbutton" id="addbutton" type="button"
													onclick="return SubmitFormOnce(document.form1, this);"
													class="btn btn-success" value="Add Bill" /> <input
													type="hidden" name="add_button" value="yes"></input>
											</center>
											<%} else if (mybean.status.equals("Update")) {%>
											<center>
												<input type="hidden" name="update_button" value="yes" />
												<input type="hidden" id="Update" name="Update" value="yes" />
												<input name="updatebutton" id="updatebutton" type="submit"
													class="btn btn-success" value="Update Bill"
													onclick="return SubmitFormOnce(document.form1, this);" /> <input
													name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" onclick="return confirmdelete(this)"
													value="Delete Bill" />
											</center>
											<%}%>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
				</div>
			</div>
		</div>
<!-- 	</div> -->

<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
<script type="text/javascript" src="../Library/sales.js"></script>
<script>
		 $(function(){
			 //curr bal & contact
         		$("#accountingcustomer").change( 
         				function() {
         					showHint('../accounting/ledger-check.jsp?vouchertype_id='+<%=mybean.vouchertype_id%>
     	                          + '&currbal=yes&customer_id='+ $(this).val(), 'customer_cur_bal');
         					showHint('../accounting/ledger-check.jsp?contact=yes&customer_id='+$(this).val(),'dr_contact_id');
         					showHint('../accounting/ledger-check.jsp?address=yes&customer_id='+$(this).val(),'txt_voucher_billing_add');
         					showHint('../accounting/ledger-check.jsp?address=yes&customer_id='+$(this).val(),'txt_voucher_consignee_add');
         				});
			 

    $('#Hintclicktocall').on('hidden.bs.modal',function(){
			var vouchertype_id = document.getElementById('vouchertype_id').value;
			var cart_session_id = document.getElementById('cart_session_id').value;
			var voucher_id = document.getElementById('voucher_id').value;
			showHintFootable('../accounting/invoice-details.jsp?cart_session_id='+cart_session_id+'&cart_vouchertype_id='+vouchertype_id+'&cart_voucher_id='+voucher_id+'&list_cartitems=yes&refresh=no','invoice_details');
			});
		
});
		</script>
	<script>
		    var cnt = <%=mybean.paymode_count%>;   
			
			function listItems(name, value){
				var session_id = document.getElementById("txt_session_id").value;		
				var vouchertype_id = document.getElementById("txt_vouchertype_id").value;
				showHint('../accounting/bill-check.jsp?name='+value+'&cart_vouchertype_id='+vouchertype_id+'','invoice_details')
				
			}
			
			function getVoucherno(){
				var vouchertype_defaultauthorize = <%= mybean.vouchertype_defaultauthorize%>
			        var voucherid = document.getElementById("txt_voucher_id").value;
					var status = "<%= mybean.status%>";
				    var branchid = document.getElementById("txt_branch_id").value;
					
					
			    	showHint('../accounting/ledger-check.jsp?voucherno=yes'
			    			+ '&vouchertype_defaultauthorize='+vouchertype_defaultauthorize
			    			+ '&vouchertype_id=' + <%= mybean.vouchertype_id%>
			    			+ '&branch_id=' + branchid 
			    			+ '&status=' + status 
			    	        + '&voucher_id=' +voucherid, 'voucherno');
								}
			
		</script>

	<script type="text/javascript">

		function chequedate(cnt){
			  $('body').on('click', '#txt_vouchertrans_cheque_date'+cnt, function(){
				 $(this).datepicker({
                               // $( "#txt_vouchertrans_cheque_date"+cnt ).datepicker({
                                showButtonPanel: true,
                                dateFormat: "dd/mm/yy"	  
                                });
		   });
		}

function toInteger1(textid)
{
    var textidObj=textid;
    var newString = "";	
    var count = 0;
    for (i = 0; i < textidObj.value.length; i++) 
    {
        ch = textidObj.value.substring(i, i+1);
        // CHECK EACH CHARACTER
        if (ch >= "0" && ch <= "9") 
        {
            newString += ch;
        }
    }
    textidObj.value = newString;
    return (false);
}
			$(document).ready(function(){		
                //var cnt = 2;
				$('body').on('click', '.del', function(){
					$(this).parent().parent().hide();
					$(this).parent().parent().remove();
					//gettotalprice(<--%=mybean.updatecount%>,1,'ledger');     
				document.getElementById("txt_paymode_count").value = --cnt;	  
				});
				
				$('body').on('click', '.add', function(){
					cnt++;
					var txt = "<div class='col-md-12'> <div class='form-body'> <div class='form-group'> <div class='form-element4 form-element'> <label>Payment<b><font color='#ff0000'>*</font></b>:</label> <select name='dr_payment"+cnt+"' id='dr_payment"+cnt+"' class='form-control' onChange='Displaypaymode("+cnt+")'><%=mybean.PopulatePayment()%></select> </div> <div class='form-element4 form-element'> <label>BY<b><font color='#ff0000'>*</font></b>:</label> <select name='dr_paymode"+cnt+"' id='dr_paymode"+cnt+"' class='form-control' onChange='Displaypaymode("+cnt+")'><%=mybean.PopulatePaymode()%></select> </div> <div class='form-element2 form-element'> <label>Amount<b><font color='#ff0000'>*</font></b>:</label> <input name='txt_bill_amt"+cnt+"' id='txt_bill_amt"+cnt+"' type='text' class='form-control' onkeyup=toInteger1(this) maxlength='10' size='11'/> </div> <div class='form-element2 form-element-margin'> <img src='../admin-ifx/add.png' class='add' type='button' align='middle'/>&nbsp;&nbsp;<img src='../admin-ifx/cancel.png' class='del' type='button' align='middle'/> </div> <div id='displayrow" + cnt + "'></div> </div> </div></div>";
					$(this).parent().parent().after(txt);
				document.getElementById("txt_paymode_count").value = cnt;
				FormElements();
				}); 
			});
		</script>
	<script type="text/javascript">
	function getlocationno(){
        var locationid = document.getElementById("dr_location_id").value;
		$('#txt_location_id').val(locationid);
		}
		
		 function Displaypaymode(cnt) {
		var payment1 = document.getElementById('dr_payment'+cnt).value;
		var paymentarr = payment1.split('-');
		var payment = paymentarr[1];
		var paymode = document.getElementById('dr_paymode'+cnt).value;

		if(payment=="0")
		{
			document.getElementById('displayrow'+cnt).innerHTML = "";
		}
		if(payment=="1" && paymode=="1")
		{
			document.getElementById('displayrow'+cnt).innerHTML = "";
		}
		if(payment=="2" && paymode=="1")
		{
			document.getElementById('displayrow'+cnt).innerHTML = "";
		}
		if(payment=="2" && paymode=="2")
		{
			document.getElementById("displayrow"+cnt).innerHTML = "<div class='col-md-12'><div class='col-md-10'style='top:10px'><label class='control-label col-md-2'>Cheque No<font color='#ff0000'>*</font>:</label><div class='col-md-4'><input name ='txt_vouchertrans_cheque_no"+cnt+"' type='text' id='txt_vouchertrans_cheque_no"+cnt+"' onKeyUp='toInteger('txt_vouchertrans_cheque_no"+cnt+"')' class='form-control' value='<%=mybean.vouchertrans_cheque_no%>' size='12' maxlength='6'/></div><label class='control-label col-md-2'>Cheque Date<font color='#ff0000'>*</font>:</label><div class='col-md-4'><input name ='txt_vouchertrans_cheque_date"+cnt+"' data-date-format='dd/mm/yyyy' type='text' id='txt_vouchertrans_cheque_date"+cnt+"' onclick='chequedate("+cnt+")' class='form-control date-picker' value='<%=mybean.vouchertrans_cheque_date%>' size='12' maxlength='6'/> </div></div><div class='col-md-10'  style='top:10px'><label class='control-label col-md-2'>Cheque Bank<font color='#ff0000'>*</font>:</label> <div class='col-md-4'><input name ='txt_vouchertrans_cheque_bank"+cnt+"' type='text' id='txt_vouchertrans_cheque_bank"+cnt+"' onKeyUp='toInteger('txt_vouchertrans_cheque_bank"+cnt+"')' class='form-control' value='<%=mybean.vouchertrans_cheque_bank%>' size='12' maxlength='255'/></div><label class='control-label col-md-2'>Cheque Branch<font color='#ff0000'>*</font>:</label><div class='col-md-4'><input name ='txt_vouchertrans_cheque_branch"+cnt+"' type='text' id='txt_vouchertrans_cheque_branch"+cnt+"' onKeyUp='toInteger('txt_vouchertrans_cheque_branch"+cnt+"')' class='form-control' value='<%=mybean.vouchertrans_cheque_branch%>' size='12' maxlength='255'/></div></div></div>";
		}
		if(payment=="2" && paymode=="3")
		{
			document.getElementById("displayrow"+cnt).innerHTML = "<div class='col-md-12'><div class='col-md-10'style='top:10px'><label class='control-label col-md-2'>Card No<font color='#ff0000'>*</font>:</label> <div class='col-md-4'><input name ='txt_vouchertrans_cheque_no"+cnt+"' type='text' id='txt_vouchertrans_cheque_no"+cnt+"' onKeyUp='toInteger('txt_vouchertrans_cheque_no"+cnt+"')' class='form-control' value='<%=mybean.vouchertrans_cheque_no%>' size='12' maxlength='6'/></div><label class='control-label col-md-2'>Bank<font color='#ff0000'>*</font>:</label> <div class='col-md-4'><input name ='txt_vouchertrans_cheque_bank"+cnt+"' type='text' id='txt_vouchertrans_cheque_bank"+cnt+"' onKeyUp='toInteger('txt_vouchertrans_cheque_bank"+cnt+"')' class='form-control' value='<%=mybean.vouchertrans_cheque_bank%>' size='12' maxlength='255'/></div></div></div>";
		}
		if(payment=="2" && paymode=="4")
		{
			document.getElementById("displayrow"+cnt).innerHTML = "<div class='col-md-12'><div class='col-md-10'style='top:10px'><label class='control-label col-md-2'>Bank<font color='#ff0000'>*</font>:</label> <div class='col-md-4'><input name ='txt_vouchertrans_cheque_bank"+cnt+"' type='text' id='txt_vouchertrans_cheque_bank"+cnt+"' onKeyUp='toInteger('txt_vouchertrans_cheque_bank"+cnt+"')' class='form-control' value='<%=mybean.vouchertrans_cheque_bank%>' size='12' maxlength='255'/></div></div></div>";
		}
		if(payment=="2" && paymode=="5")
		{
			document.getElementById("displayrow"+cnt).innerHTML = "<div class='col-md-12'><div class='col-md-10'style='top:10px'><label class='control-label col-md-2'>Transaction No<font color='#ff0000'>*</font>:</label> <div class='col-md-4'><input name ='txt_vouchertrans_cheque_no"+cnt+"' type='text' id='txt_vouchertrans_cheque_no"+cnt+"' onKeyUp='toInteger('txt_vouchertrans_cheque_no"+cnt+"')' class='form-control' value='<%=mybean.vouchertrans_cheque_no%>' size='12' maxlength='6'/></div></div></div>";
		}
	}
		 
		 function PopulateLocation(){
				var branch_id = document.getElementById("dr_branch_id").value;
				document.getElementById("txt_branch_id").value = branch_id;
				showHint('../accounting/accounting-branch-check.jsp?sales_branch_id='+branch_id+'&branch_location=yes','span_location');     
				}
</script>

</body>
</HTML>
