<jsp:useBean id="mybean" class="axela.accounting.Returns_Update" scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%= mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>   
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp" %>
</HEAD>
<body
	onload="<%if (mybean.status.equals("Add")) {%>getVoucherno();list_cart_items();<%}%> 
<%if (mybean.status.equals("Update")) {%>list_cart_items();<%}%>"
	class="page-container-bg-solid page-header-menu-fixed">
	<div id="dialog-modal"></div>
	<div id="dialog-modal-bill"></div>
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
						<li><a
							href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=<%=mybean.voucherclass_id%>&vouchertype_id=<%=mybean.vouchertype_id%>">List
								<%=mybean.vouchertype_name%>s
						</a> &gt;</li>
						<li><a href="returns-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								<%=mybean.vouchertype_name%></a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="form1" id="form1" method="post"
								class="form-horizontal">
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.status%>
											<%=mybean.vouchertype_name%></div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<center>
												Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</center>
												<div class="form-element7">
													<input type="hidden" name="txt_config_inventory_current_stock" id="txt_config_inventory_current_stock" value="<%=mybean.config_inventory_current_stock%>" />
													<div id="getlocation"></div>
													<!-- RETURN DETAILS SECTION -->
													<input type="hidden" name="txt_session_id" id="txt_session_id" value="<%=mybean.session_id%>" />
													<input type="hidden" name="txt_parentvoucher" id="txt_parentvoucher" value="<%=mybean.parentvoucher%>" />
													<input type="hidden" name="cart_id" id="cart_id" />
													<input type="hidden" name="uom_id" id="uom_id" />
													<input type="hidden" name="alt_uom_id" id="alt_uom_id" />
													<input type="hidden" name="cart_multivoucher_id" id="cart_multivoucher_id" />
													<input type="hidden" name="txt_voucherclass_id" id="txt_voucherclass_id" value="<%=mybean.voucherclass_id%>" />
													<input type="hidden" name="txt_vouchertype_id" id="txt_vouchertype_id" value="<%=mybean.vouchertype_id%>" />
													<input type="hidden" id="txt_voucher_prev_grandtotal" name="txt_voucher_prev_grandtotal" value="<%=mybean.voucher_prev_grandtotal%>" />
													<input type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%=mybean.branch_id%>" />
													<input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>" />
													<input type="hidden" name="voucher_contact" id="voucher_contact" value="" />
													<input type="hidden" name="txt_voucher_contact_id" id="txt_voucher_contact_id" value="<%=mybean.voucher_contact_id%>" />
													<input type="hidden" name="txt_location_id" id="txt_location_id" value="<%=mybean.vouchertrans_location_id%>" />
													<input name="hid_voucher_no" type="hidden" id="hid_voucher_no" value="<%=mybean.voucher_no%>" />
													<input type="hidden" id="lead_id" name="lead_id" value="<%=mybean.voucher_lead_id%>" />
													<input type="hidden" id="hid_check_voucherno" name="hid_check_voucherno" value="<%=mybean.check_voucher_no%>" />
													<input type="hidden" id="hid_check_branch" name="hid_check_branch" value="<%=mybean.checkbranch%>" />
													<input type="hidden" id="span_customer_name" name="span_customer_name" value="<%=mybean.so_customer_name%>" />
													<input type="hidden" id="span_contact_name" name="span_contact_name" value="<%=mybean.so_contact_name%>" />
													<input type="hidden" id="txt_vouchertype_mobile" name="txt_vouchertype_mobile" value="<%=mybean.vouchertype_mobile%>" />
													<input type="hidden" id="txt_vouchertype_email" name="txt_vouchertype_email" value="<%=mybean.vouchertype_email%>" />
													<input type="hidden" id="txt_vouchertype_dob" name="txt_vouchertype_dob" value="<%=mybean.vouchertype_dob%>" />
													<input type="hidden" id="txt_vouchertype_dnd" name="txt_vouchertype_dnd" value="<%=mybean.vouchertype_dnd%>" />
													<input type="hidden" name="txt_gst_type" id="txt_gst_type" value="<%=mybean.gst_type %>" />
													<input type="hidden" name="txt_vouchertype_authorize" id="txt_vouchertype_authorize" value="<%=mybean.vouchertype_authorize %>" />
													<input type="hidden" name="txt_vouchertype_defaultauthorize" id="txt_vouchertype_defaultauthorize" value="<%=mybean.vouchertype_defaultauthorize %>" />
													<input type="hidden" name="txt_voucher_authorize" id="txt_voucher_authorize" value="<%=mybean.voucher_authorize %>" />
													<input type="hidden" name="txt_voucher_authorize_id" id="txt_voucher_authorize_id" value="<%=mybean.voucher_authorize_id %>" />
											        <input type="hidden" name="txt_voucher_authorize_time" id="txt_voucher_authorize_time" value="<%=mybean.voucher_authorize_time %>" />
											
													<div class="form-element6">
														<label> Branch: </label>
															<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.branch_id%>"><%=mybean.branch_name%></a>
															<input type="hidden" name="txt_branch_name"
																id="txt_branch_name" value="<%=mybean.branch_name%>" />
															<input type="hidden" name="txt_voucher_enquiry_id"
																id="txt_voucher_enquiry_id"
																value="<%=mybean.voucher_enquiry_id%>" /> <input
																type="hidden" name="txt_voucher_quote_id"
																id="txt_voucher_quote_id"
																value="<%=mybean.voucher_quote_id%>" /> <input
																type="hidden" name="txt_voucher_so_id"
																id="txt_voucher_so_id" value="<%=mybean.voucher_so_id%>" />
															<input type="hidden" name="txt_voucher_delnote_id"
																id="txt_voucher_delnote_id"
																value="<%=mybean.voucher_delnote_id%>" /> <input
																type="hidden" name="txt_voucher_dcr_request_id"
																id="txt_voucher_dcr_request_id"
																value="<%=mybean.voucher_dcr_request_id%>" /> <input
																type="hidden" name="txt_voucher_dcr_id"
																id="txt_voucher_dcr_id"
																value="<%=mybean.voucher_dcr_id%>" /> <input
																type="hidden" name="txt_voucher_grn_return_id"
																id="txt_voucher_grn_return_id"
																value="<%=mybean.voucher_grn_return_id%>" /> <input
																type="hidden" name="" id="txt_voucher_id"
																value="<%=mybean.voucher_id%>" />
															<%
																if (mybean.emp_branch_id.equals("0")) {
															%>
															<a
																href="../accounting/accounting-branch.jsp?para=sales&vouchertype_id=<%=mybean.vouchertype_id%>&voucherclass_id=<%=mybean.voucherclass_id%>">(Change
																Branch)</a>
															<%
																}
															%>
														</div>
														<div class="form-element6">
														<label>Date<b><font color="#ff0000">*</font></b>: </label>
															<input name="txt_voucher_date" type="text"
																class="form-control datepicker"
																id="txt_voucher_date"
																value="<%=mybean.voucherdate%>" /> <input type="hidden"
																name="txt_voucherdate" id="txt_voucherdate"
																value="<%=mybean.voucher_date%>" />
													</div>
													<div class="form-element6">
														<label>Location<font color="#ff0000">*</font>: </label>
															<%
																if (mybean.vouchertype_id.equals("24")
																		|| mybean.vouchertype_id.equals("23")) {
															%>
															<select name="dr_voucher_location_id"
																class="form-control" id="dr_voucher_location_id">
																<%=mybean.PopulateLocation(mybean.branch_id)%>
															</select>
															<% } else { %>
															<%=mybean.location_name%>
															<% } %>
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
													<%
														if (!mybean.voucher_dcr_request_id.equals("0")) {
													%>
													<div class="form-element6">
														<label>DCR. Request ID:</label>
															<a href="../accounting/voucher-list.jsp?voucher_id=<%=mybean.voucher_dcr_request_id%>"><%=mybean.voucher_dcr_request_id%></a>
													</div>
													<% } %>
													<%
														if (!mybean.voucher_dcr_id.equals("0")) {
													%>
													<div class="form-element6">
														<label>DCR. ID:</label>
															<a href="../accounting/voucher-list.jsp?voucher_id=<%=mybean.voucher_dcr_id%>"><%=mybean.voucher_dcr_id%></a>
													</div>
													<%
														}
													%>
													<%
														if (!mybean.voucher_grn_return_id.equals("0")) {
													%>
													<div class="form-element6">
														<label>GRN Returns ID:</label>
															<a
																href="../accounting/voucher-list.jsp?voucher_id=<%=mybean.voucher_grn_return_id%>"><%=mybean.voucher_grn_return_id%></a>
													</div>
													<%
														}
													%>
													<%
														if (!mybean.voucher_delnote_id.equals("0")) {
													%>
													<div class="form-element6">
														<label>Del.Note
															ID:</label>
															<a href="../accounting/voucher-list.jsp?voucherclass_id=25&vouchertype_id=25&voucher_id=<%=mybean.voucher_delnote_id%>"><%=mybean.voucher_delnote_id%></a>
													</div>
													<% } %>
													<%
														if (mybean.vouchertype_id.equals("23") || mybean.vouchertype_id.equals("4")
																|| mybean.vouchertype_id.equals("116")) {
													%>
													<div class="form-element12">
															<label>Customer<font color="#ff0000">*</font>: </label>
																<select class="form-control select2"
																	id="accountingcustomer" name="accountingcustomer">
																	<%=mybean.ledgercheck.PopulateLedgers("32", mybean.voucher_customer_id, mybean.comp_id)%>
																</select>
																<div valign="top" align="left" id="customer_cur_bal"><%=mybean.ReturnCustomerCurrBalance(mybean.voucher_customer_id, mybean.comp_id, mybean.vouchertype_id)%></div>
													</div>
													<% } else { %>
													<div class="form-element12">
															<label>Supplier<font color="#ff0000">*</font>: </label>
																<select class="form-control select2"
																	id="accountingsupplier" name="accountingsupplier">
																	<%=mybean.ledgercheck.PopulateLedgers("32", mybean.voucher_customer_id, mybean.comp_id)%>
																</select>

																<!-- 																<input tabindex="-1" class="bigdrop select2-offscreen" -->
																<!-- 																	id="accountingcustomer" name="accountingcustomer" -->
																<!-- 																	style="width: 250px" -->
																<%-- 																	value="<%=mybean.voucher_customer_id%>" type="hidden"> --%>
																<div valign="top" align="left" id="customer_cur_bal"
																	width="50"><%=mybean.ReturnCustomerCurrBalance(mybean.voucher_customer_id, mybean.comp_id, mybean.vouchertype_id)%></div>
															
															<div>
																<a href="ledger-add.jsp?add=yes&voucherclass_id=<%=mybean.voucherclass_id%>&vouchertype_id=<%=mybean.vouchertype_id%>
																	&voucherclass_file=<%=mybean.voucherclass_file%>&vouchertype_mobile=<%=mybean.vouchertype_mobile%>
																	&vouchertype_email=<%=mybean.vouchertype_email%>&vouchertype_dob=<%=mybean.vouchertype_dob%>
																	&vouchertype_dnd=<%=mybean.vouchertype_dnd%>">
																Add Ledger</a>
															</div>
															
															</div>
															


													<%
														}
													%>
													<div class="form-element12">
															<label>Contact<font color="#ff0000">*</font>: </label>
<span id="dr_contact_id"> <%=mybean.PopulateContact(mybean.voucher_customer_id)%> </span>
															</div>
													<%
														if (mybean.vouchertype_id.equals("102")) {
													%>
													<div class="form-element12">
															<label>Ledger<font color="#ff0000">*</font>: </label>
																<select name="dr_Legder_id" class="form-control"
																	id="dr_Legder_id" style="width: 250px">
																	<%=mybean.PopulateLedgers()%>
																</select>
													</div>
													<% } %>
												<div class="form-element12">
																	<div id=invoice_details align="left"
																		style="height: 400px; overflow-y: scroll"></div>
																</div>
</div>
												<!--                                           ===========SEARCH ITEMS====== -->

												<div class="form-element5">
													<%
														if (mybean.vouchertype_id.equals("24")
																|| mybean.vouchertype_id.equals("23")) {
													%>
													<div class="form-element10">
														<label>Search: </label>
															<input name="txt_search" type="text" class="form-control"
																id="txt_search" value="" size="30" maxlength="255"
																onKeyUp="InvoiceItemSearch();" />
															<div class="admin-master">
																<a href="../inventory/inventory-item-list.jsp?all=yes"
																	title="Manage Item"></a>
															</div>
															</br> <select name="drop_item_itemgroup_id"
																id="drop_item_itemgroup_id"
																onchange="InvoiceItemSearch();" class="form-control">
																<%=mybean.PopulateItemCatPop()%>
															</select>

													</div>
													<% } %>

													<%
														if (mybean.vouchertype_id.equals("24")
																|| mybean.vouchertype_id.equals("23")) {
													%>

													<div class="form-element12">
																	<label id="hint_search_item" style="text-align: center">Enter
																		your search parameter!</label>
																</div>
													<%
														}
													%>
													<div class="form-element12">
																	<div class="hint" id="itemdetails"></div>
												</div>
												<!-- 												 <div class="col-md-7"> -->
												<!-- 										<div class="container-fluid"> -->

											
													
											

												<!-- 										</div> -->
												<!-- 									</div> -->
												<!--                                           ============SEARCH ITEMS END======== -->

										</div>
										<!-- 									</div> -->
										<!-- 								</div> -->
										<!-- 								===============OUTSIDE PORTLET BOX========== -->

										<!--                                ===========INVOICE ITEM DETAILS END======== -->
										<%
											if (mybean.status.equals("Add")
													&& ((!mybean.voucher_dcr_request_id.equals("0")
													|| !mybean.voucher_dcr_id.equals("0"))
													|| mybean.customer_add
															.equals("yes"))) {
										%>
										<%
											if (mybean.vouchertype_billing_add.equals("1")) {
										%>
										<div class="container-fluid">
											<div class="form-element6">
												<label><center>
														Billing Address
														<%
													if ((mybean.voucher_contact_id.equals("0") || mybean.voucher_contact_id
																	.equals("")) && mybean.add.equals("yes")) {
												%>
														<!--<div id="copy_cont_address_link"> (<a href="javascript:CopyContactAddress();">Same as Contact</a>)</div> -->

														<%
															}
														%>
													</center></label> <label>Address<font color="#ff0000">*</font>: </label>
													<textarea name="txt_voucher_billing_add" rows="4"
														class="form-control" id="txt_voucher_billing_add"
														onKeyUp="charcount('txt_voucher_billing_add', 'span_txt_voucher_billing_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_billing_add%></textarea>
													<span id="span_txt_voucher_billing_add"> (255
														Characters)</span>
											</div>
											<%
												if (mybean.vouchertype_consignee_add.equals("1")) {
											%>
											<div class="form-element6">
												<label><center>
														Consignee Address (<a
															href="javascript:CopyConsigneeAddress();">Same as
															Billing</a>)
													</center> </label> <label class="control-label col-md-3">Address<font
													color="#ff0000">*</font>:
												</label>
													<textarea name="txt_voucher_consignee_add" rows="4"
														class="form-control" id="txt_voucher_consignee_add"
														onKeyUp="charcount('txt_voucher_consignee_add', 'span_txt_voucher_consignee_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_consignee_add%></textarea>
													<span id="span_txt_voucher_consignee_add"> (255
														Characters)</span>
											</div>
											<% } %>
											<% } %>

											<% } else { %>
											<%
												if (mybean.vouchertype_billing_add.equals("1")) {
											%>
											<div class="form-element6">
												<label><center>
														Billing Address<%
													if ((mybean.voucher_contact_id.equals("0") || mybean.voucher_contact_id
																	.equals("")) && mybean.add.equals("yes")) {
												%>
														<!--<div id="copy_cont_address_link"> (<a href="javascript:CopyContactAddress();">Same as Contact</a>)</div> -->
														<%
															}
														%>
													</center> </label> <label>Address<font color="#ff0000">*</font>: </label>
													<textarea name="txt_voucher_billing_add" rows="4"
														class="form-control" id="txt_voucher_billing_add"
														onKeyUp="charcount('txt_voucher_billing_add', 'span_txt_voucher_billing_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_billing_add%></textarea>
													<span id="span_txt_voucher_billing_add"> (255
														Characters)</span>
											</div>

											<%
												if (mybean.vouchertype_consignee_add.equals("1")) {
											%>
											<div class="form-element6">
												<label><center>
														</>Consignee Address ( <a
															href="javascript:CopyConsigneeAddress();">Same as
															Billing </a>)
													</center> </label> <label>Address<font color="#ff0000">*</font>: </label>
													<textarea name="txt_voucher_consignee_add" rows="4"
														class="form-control" id="txt_voucher_consignee_add"
														onKeyUp="charcount('txt_voucher_consignee_add', 'span_txt_voucher_consignee_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_consignee_add%></textarea>
													<span id="span_txt_voucher_consignee_add"> (255
														Characters)</span>
											</div>
											<% } %>
											<% } %>
											<% } %>
											<br />
											<%
												if (mybean.vouchertype_transporter.equals("1")) {
											%>
											<div class="form-element6">
																<label>Transporter: </label>
																	<input name="txt_voucher_transporter" type="text"
																		class="textbox" id="txt_voucher_transporter" size="40"
																		maxlength="255"
																		value="<%=mybean.voucher_transporter%>" />
											</div>
											<%
												}
											%>
											<%
												if (mybean.vouchertype_gatepass.equals("1")) {
											%>
											<div class="form-element6">
																<label>Gate Pass:</label>
																	<input name="txt_voucher_gatepass" type="text"
																		class="textbox" id="txt_voucher_gatepass" size="40"
																		maxlength="255" value="<%=mybean.voucher_gatepass%>" />
																 
											</div>
											<%
												}
											%>
											<%
												if (mybean.vouchertype_lrno.equals("1")) {
											%>
											<div class="form-element6">
																<label>Lr. No.:</label>
																	<input name="txt_voucher_lrno" type="text"
																		class="textbox" id="txt_voucher_lrno" size="40"
																		maxlength="255" value="<%=mybean.voucher_lrno%>" />
																 
											</div>
											<%
												}
											%>
											<%
												if (mybean.vouchertype_driver_no.equals("1")) {
											%>
											<div class="form-element6">
																<label>Driver No.:</label>
																	<input name="txt_voucher_driver_no" type="text"
																		class="form-control" id="txt_voucher_driver_no"
																		size="40" maxlength="255"
																		value="<%=mybean.voucher_driver_no%>" />
											</div>
											<%
												}
											%>
											<%
												if (mybean.vouchertype_tempo_no.equals("1")) {
											%>
											<div class="form-element6">
																<label>Tempo No.:</label>
																	<input name="txt_voucher_tempo_no" type="text"
																		class="form-control" id="txt_voucher_tempo_no"
																		size="40" maxlength="255"
																		value="<%=mybean.voucher_tempo_no%>" />
											</div>
											<%
												}
											%>
											<%
												if (mybean.vouchertype_cashdiscount.equals("1")) {
											%>
											<div class="form-element6">
																<label>Cash Discount:</label>
																	<input name="txt_voucher_cashdiscount" type="text"
																		class="form-control" id="txt_voucher_cashdiscount"
																		size="12" maxlength="11"
																		value="<%=mybean.voucher_cashdiscount%>"
																		onkeyup="toInteger(this.id);" />
																</div>

											<% } %>
											<%
												if (mybean.vouchertype_turnoverdisc.equals("1")) {
											%>
											<div class="form-element6">
																<label>Turnover Discount:</label>
																	<input name="txt_voucher_turnoverdisc" type="text"
																		class="form-control" id="txt_voucher_turnoverdisc"
																		size="12" maxlength="11"
																		value="<%=mybean.voucher_turnoverdisc%>"
																		onkeyup="toInteger(this.id);" />

											<%
												}
											%>
											<br />
											<div class="form-element6">
																<label>Executive<font color="#ff0000">*</font>: </label>
																	<select name="dr_executive" id="dr_executive"
																		class="form-control">
																		<%=mybean.PopulateExecutives(mybean.emp_id, mybean.comp_id)%>
																	</select>
											</div>
											<%
												if (mybean.vouchertype_ref_no_enable.equals("1")) {
											%>
											<div class="form-element6">
																<label>Reference
																	No. <%
																	if (mybean.vouchertype_ref_no_mandatory.equals("1")) {
																%> <font color="#ff0000">*</font>: <%
 	}
 %>
																</label>
																	<input name="txt_voucher_ref_no" type="text"
																		class="form-control" id="txt_voucher_ref_no"
																		value="<%=mybean.voucher_ref_no%>" size="32"
																		maxlength="50" />
																</div>
											<%
												}
											%>
											<%
												if (mybean.vouchertype_id.equals("102")) {
											%>

											<div class="form-element6">
																<label>Purchase Order No.<font color="#ff0000">*</font>: </label>
																	<input name="txt_voucher_custref_no" type="text"
																		class="form-control" id="txt_voucher_custref_no"
																		value="<%=mybean.voucher_custref_no%>" size="32"
																		maxlength="50" />
											</div>
											<div class="form-element6">
																<label>Purchase Order Date:</label>
																	<input name="txt_voucher_custref_date" type="text"
																		class="form-control" id="txt_voucher_custref_date"
																		value="<%=mybean.voucher_custref_date%>" size="12"
																		maxlength="10" />
											</div>
											<%
												}
											%>
											<div class="form-element6">
																<label>Active:</label>
																	<input id="chk_voucher_active" type="checkbox"
																		name="chk_voucher_active"
																		<%=mybean.PopulateCheck(mybean.voucher_active)%> />
											</div>
											<div class="form-element6">
																<label>Notes:</label>
																	<textarea name="txt_voucher_notes" cols="70" rows="4"
																		class="form-control" id="txt_voucher_notes"><%=mybean.voucher_notes%></textarea>
											</div>
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
																<label>Entry By: <%=mybean.unescapehtml(mybean.voucher_entry_by)%></label>
																	<input name="entry_by" type="hidden" id="entry_by"
																		value="<%=mybean.unescapehtml(mybean.voucher_entry_by)%>" />
											</div>
											<div class="form-element6">
																<label>Entry Date: <%=mybean.entry_date%></label>
																	<input type="hidden" id="entry_date" name="entry_date"
																		value="<%=mybean.entry_date%>" />
																</div>

											<% } %>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6">
																<label>Modified By: <%=mybean.unescapehtml(mybean.voucher_modified_by)%></label>
																	<input type="hidden" id="modified_by" name="modified_by" value="<%=mybean.unescapehtml(mybean.voucher_entry_by)%>" />
																</div>
															 
											<div class="form-element6">
																<label>Modified
																	Date: <%=mybean.modified_date%><input type="hidden"
																		id="modified_date" name="modified_date"
																		value="<%=mybean.modified_date%>" />
																</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Add")) {
											%><center>
												<input name="addbutton" id="addbutton" type="button"
													onClick="return SubmitFormOnce(document.form1, this);"
													class="btn btn-success"
													value='Add <%=mybean.vouchertype_name%>' /> <input
													type="hidden" name="add_button" value="yes" />
											</center>
											<%
												} else if (mybean.status.equals("Update")) {
											%>
											<center>
												<input type="hidden" name="update_button" value="yes">
												<input type="hidden" id="Update" name="Update" value="yes">
												<input name="updatebutton" id="updatebutton" type="submit"
													class="btn btn-success"
													value='Update <%=mybean.vouchertype_name%>'
													onClick="return SubmitFormOnce(document.form1, this);" /><input
													name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" onClick="return confirmdelete(this)"
													value='Delete <%=mybean.vouchertype_name%>' />
											</center>
											<%
												}
											%>
										</div>
									</div>
							</form>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp" %>
<script type="text/javascript" src="../Library/returns.js"> </script>

<script type="text/javascript">
 var srcEvent = null;

 $("input[type=text],input[type=number]") .mousedown(function (event) { srcEvent = event; }) 
     .mouseup(function (event) {
         var delta = Math.abs(event.clientX - srcEvent.clientX) 
                   + Math.abs(event.clientY - srcEvent.clientY);

         var threshold = 2;
         if (delta <= threshold) {
                    try {
                         // ios likes this but windows-chrome does not on number fields
                         $(this)[0].selectionStart = 0;
                         $(this)[0].selectionEnd = 1000;
                     } catch (e) {
                         // windows-chrome likes this
                         $(this).select();
                     }
         }
     });
 </script>
<script>
                            $(function() {
                        		// credit to
                        		<%if (mybean.vouchertype_id.equals("23")
					|| mybean.vouchertype_id.equals("4")
					|| mybean.vouchertype_id.equals("116")) {%>
                        		$("#accountingcustomer").change( 
                        		
                        				function() {
                        					showHint('../accounting/ledger-check.jsp?invoicedetail=yes&vouchertype_id='
                        							  +<%=mybean.vouchertype_id%>
                        	                          + '&customer_id=' + $(this).val(), 'customer_invoice');
                        					showHint('../accounting/ledger-check.jsp?vouchertype_id='+<%=mybean.vouchertype_id%>
                    	                          + '&currbal=yes&customer_id='+ $(this).val(), 'customer_cur_bal');
            	  							showHint('../accounting/ledger-check.jsp?contact=yes&customer_id='+$(this).val(),'dr_contact_id');               
            				                showHint('../accounting/ledger-check.jsp?customer_rateclass=yes&customer_id='+$(this).val(),'dr_voucher_rateclass_id');                 
            				                showHint('../accounting/ledger-check.jsp?address=yes&customer_id='+$(this).val(),'txt_voucher_billing_add'); 
            								var voucher_date = document.getElementById("txt_voucherdate").value;   
            								showHint('../accounting/ledger-check.jsp?payment_date=yes&voucher_date='+voucher_date+'&customer_id='+$(this).val(),'paydate');
            								//alert($("#paydate").html());
            								<%if (mybean.vouchertype_id.equals("4")) {%>
            								setTimeout(function(){
            									document.getElementById("txt_voucher_payment_date").value = $("#paydate").html();
            								}, 200);
            								<%}%>
                        				});
                        		<%} else {%>
                        		$("#accountingsupplier").change( 
                                		
                        				function() {
                        					showHint('../accounting/ledger-check.jsp?invoicedetail=yes&vouchertype_id='
                        							  +<%=mybean.vouchertype_id%>
                        	                          + '&customer_id=' + $(this).val(), 'customer_invoice');
                        					showHint('../accounting/ledger-check.jsp?vouchertype_id='+<%=mybean.vouchertype_id%>
                    	                          + '&currbal=yes&customer_id='+ $(this).val(), 'customer_cur_bal');
            	  							showHint('../accounting/ledger-check.jsp?contact=yes&customer_id='+$(this).val(),'dr_contact_id');               
            				                showHint('../accounting/ledger-check.jsp?customer_rateclass=yes&customer_id='+$(this).val(),'dr_voucher_rateclass_id');                 
            				                showHint('../accounting/ledger-check.jsp?address=yes&customer_id='+$(this).val(),'txt_voucher_billing_add'); 
            								var voucher_date = document.getElementById("txt_voucherdate").value;   
            								showHint('../accounting/ledger-check.jsp?payment_date=yes&voucher_date='+voucher_date+'&customer_id='+$(this).val(),'paydate');
            								//alert($("#paydate").html());
            								<%if (mybean.vouchertype_id.equals("4")) {%>
            								setTimeout(function(){
            									document.getElementById("txt_voucher_payment_date").value = $("#paydate").html();
            								}, 200);
            								<%}%>
                        					
                        				});
                        		<%}%>
                            });
                   $(function() {

								 $("#maincity").change(function(){
	                            	  showHint('../sales/enquiry-check.jsp?city=yes&city_id='
	                            			  +$(this).val(),
	                            			  'zone');
	                             });

	});  
</script>
<script type="text/javascript">                   
 function showlocstockstatus(branch_id, item_id) {   
	 $('#dialog-modal').dialog({       
         autoOpen: false,
         width: 700,
         height: 400,
         zIndex: 200,  
         modal: true,
         title: "Location Stock"  
     });
 	
 	//	alert("12345");    
 			$.ajax({
 				
 				//url: "home.jsp",  
 				success: function(data){      
				$('#dialog-modal').html('<iframe src="../accounting/location-stock.jsp?location=yes&branch_id='+branch_id+'&item_id='+item_id+'" height="100%" width="100%" frameborder=0></iframe>');
 				}
 			});
 			  
 			$('#dialog-modal').dialog('open');    
 			return true;

 }     
 
 // show all invoice of the selected party
 function showlocallinvoice(customerid, companyid) {   
	 $('#dialog-modal').dialog({       
         autoOpen: false,
         width: 1100,
         height: 500,
         zIndex: 200,  
         modal: true,
         title: "All Invoices"  
     });
 	
 			$.ajax({
 				
 				success: function(data){      
				$('#dialog-modal').html('<iframe src="../accounting/all-invoice.jsp?customer_id='+customerid+'&comp_id='+companyid+'" height="100%" width="100%" frameborder=0></iframe>');
 				}
 			});
 			  
 			$('#dialog-modal').dialog('open');    
 			return true;

 }        
</script>
<script type="application/javascript">
	
	
  function BillSundry(vouchertype_id, cart_session_id, voucher_id, total) {                  
     $(document).ready(function(){      
	  $('#dialog-modal').dialog({          
	         autoOpen: false,
	         width: 700,
	         height: 400,
	         zIndex: 200,    
	         modal: true,
	         title: "Bill Sundry",    
	         close: function(){     
	      	  showHintFootable('../accounting/returns-details.jsp?cart_vouchertype_id='+vouchertype_id+'&cart_session_id='+cart_session_id+'&cart_voucher_id='+voucher_id+'&list_cartitems=yes&refresh=no','invoice_details');                             
	         	}         
	         });
	  
	  $.ajax({    
       success: function(data){          
      $('#dialog-modal').html('<iframe src="../accounting/bill-sundry.jsp?so=yes&add=yes&cart_session_id='+cart_session_id+'&vouchertype_id='+vouchertype_id+'&voucher_id='+voucher_id+'&total='+total+'" height="100%" width="100%" frameborder=0></iframe>');
      }
      });
                       	 			  
     $('#dialog-modal').dialog('open');    
     return true;
     });   
          
 }          


</script>

<script type="application/javascript">
	
function getVoucherno(){
	var vouchertype_defaultauthorize = <%= mybean.vouchertype_defaultauthorize%>
	var voucherid = document.getElementById("txt_voucher_id").value;
	var parentvoucher = document.getElementById("txt_parentvoucher").value;
	var status = "<%= mybean.status%>";
	if(parentvoucher == ""){
		locationid = document.getElementById("dr_voucher_location_id").value;
	} else {
		locationid = document.getElementById("txt_location_id").value;
	}
	showHint('../accounting/ledger-check.jsp?voucherno=yes'
			+ '&vouchertype_defaultauthorize='+vouchertype_defaultauthorize
			+ '&vouchertype_id=' + <%= mybean.vouchertype_id%>
			+ '&branch_id=' + <%=mybean.branch_id%> 
			+ '&voucher_location_id=' +locationid
			+ '&status=' + status 
	        + '&voucher_id=' +voucherid, 'voucherno');
	
<%-- 	showHint('../accounting/ledger-check.jsp?voucherno=yes&vouchertype_id=' + <%=mybean.vouchertype_id%>  --%>
<%-- 				+ '&branch_id='+ <%=mybean.branch_id%> --%>
// 				+ '&voucher_location_id=' +locationid 
// 				+ '&voucher_id=' +voucherid, 'voucherno');
}

</script>
</body>
</HTML>
