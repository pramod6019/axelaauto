<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.PO_Update" scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%= mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>   
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp" %>
</HEAD>
<body
	onLoad="<%if (mybean.status.equals("Add")) {%>getVoucherno();list_cart_items();<%}%>
			<%if (mybean.status.equals("Update")) {%>list_cart_items();<%}%>"
	class="page-container-bg-solid page-header-menu-fixed">
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
							href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=<%=mybean.voucherclass_id%>&vouchertype_id=<%=mybean.vouchertype_id%>">List
								<%=mybean.vouchertype_name%>
						</a> &gt;</li>
						<li><a
							href="../accounting/po-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								<%=mybean.vouchertype_name%></a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<form name="form1" id="form1" method="post"
								class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>&nbsp;<%=mybean.vouchertype_name%>
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="form-body">
											<div class="container-fluid">
												<center>
													Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.
												</center>
												<input type="hidden" name="txt_voucher_id"
													id="txt_voucher_id" value="<%=mybean.voucher_id%>" />
												<!-- 										</div> -->
														<div class="form-element7">
															<%
																if (!mybean.vouchertype_id.equals("2")) {
															%>
															<div class="form-element6">
																		<label>Branch<font
																			color="#ff0000">*</font><b>:</b></label>
																			<a
																				href="../portal/branch-summary.jsp?branch_id=<%=mybean.branch_id%>"><%=mybean.branch_name%></a>
																			<input type="hidden" name="txt_branch_name"
																				id="txt_branch_name" value="<%=mybean.branch_name%>">
																			<%
																				if (mybean.emp_branch_id.equals("0")) {
																			%>
																			<br> <a
																				href="../accounting/accounting-branch.jsp?para=purchase&branch_id=
														<%=mybean.branch_id%>&vouchertype_id=
														<%=mybean.vouchertype_id%>">(Change
																				Branch)</a>
																			<%
																				}
																			%>
															</div>
															<div class="form-element6">
																		<label>Date<b><font
																				color="#ff0000">*</font></b>:
																		</label>
																		<%
																			} else {
																		%>

																		<div class="form-element6">
																					<label>Date<b><font color="#ff0000">*</font></b>: </label>
																					<%
																						}
																					%>
																						<!--Start of Hidden filds -->
																						<input type="hidden" name="txt_voucher_id" id="txt_voucher_id" value="<%=mybean.voucher_id%>" />
																						<input type="hidden" name="txt_parentvoucher" id="txt_parentvoucher" value="<%=mybean.parentvoucher%>" />
																						<input type="hidden" name="txt_session_id" id="txt_session_id" value="<%=mybean.session_id%>">
																						<input type="hidden" name="cart_id" id="cart_id">
																						<input type="hidden" name="txt_voucherclass_id" id="txt_voucherclass_id" value="<%=mybean.voucherclass_id%>" />
																						<input type="hidden" name="txt_vouchertype_id" id="txt_vouchertype_id" value="<%=mybean.vouchertype_id%>" />
																						<input type="hidden" id="txt_voucher_prev_grandtotal" name="txt_voucher_prev_grandtotal" value="<%=mybean.voucher_prev_grandtotal%>">
																						<input type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%=mybean.branch_id%>">
																						<input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>">
																						<input type="hidden" name="voucher_contact" id="voucher_contact" value="" />
																						<input type="hidden" name="txt_voucher_contact_id" id="txt_voucher_contact_id" value="<%=mybean.voucher_contact_id%>">
																						<input type="hidden" name="txt_location_id" id="txt_location_id" value="<%=mybean.vouchertrans_location_id%>">
																						<input type="hidden" name="txt_location_name" id="txt_location_name" value="<%=mybean.location_name%>" />
																						<input name="hid_voucher_no" type="hidden" id="hid_voucher_no" value="<%=mybean.voucher_no%>">
																						<input type="hidden" id="lead_id" name="lead_id" value="<%=mybean.voucher_lead_id%>">
																						<input type="hidden" id="hid_check_voucherno" name="hid_check_voucherno" value="<%=mybean.check_voucher_no%>" />
																						<input type="hidden" id="hid_check_branch" name="hid_check_branch" value="<%=mybean.checkbranch%>" />
																						<input type="hidden" name="cart_id" id="cart_id">
																						<!--  <input type="hidden" name="uom_id" id="uom_id"><input type="hidden" name="uom_ratio" id="uom_ratio">  -->
																						<input type="hidden" name="txt_config_inventory_current_stock" id="txt_config_inventory_current_stock" value="<%=mybean.config_inventory_current_stock%>">
																						<input type="hidden" id="txt_item_purchase_customer_id" name="txt_item_purchase_customer_id">
																						<input type="hidden" name="txt_po_tax_id1" id="txt_po_tax_id1" value="<%=mybean.po_tax_id1%>">
																						<input type="hidden" name="txt_po_tax_rate1" id="txt_po_tax_rate1" value="<%=mybean.po_tax_rate1%>">
																						<input type="hidden" name="txt_po_tax_customer_id1" id="txt_po_tax_customer_id1" value="<%=mybean.po_tax_customer_id1%>">
																						<input type="hidden" name="txt_po_tax_id2" id="txt_po_tax_id2" value="<%=mybean.po_tax_id2%>">
																						<input type="hidden" name="txt_po_tax_rate2" id="txt_po_tax_rate2" value="<%=mybean.po_tax_rate2%>">
																						<input type="hidden" name="txt_po_tax_customer_id2" id="txt_po_tax_customer_id2" value="<%=mybean.po_tax_customer_id2%>">
																						<input type="hidden" name="txt_po_tax_id3" id="txt_po_tax_id3" value="<%=mybean.po_tax_id3%>">
																						<input type="hidden" name="txt_po_tax_rate3" id="txt_po_tax_rate3" value="<%=mybean.po_tax_rate3%>">
																						<input type="hidden" name="txt_po_tax_customer_id3" id="txt_po_tax_customer_id3" value="<%=mybean.po_tax_customer_id3%>">
																						<input type="hidden" id="txt_po_discount1_customer_id" name="txt_po_discount1_customer_id" />
																						<input type="hidden" name="txt_voucher_po_id" id="txt_voucher_po_id" value="<%=mybean.voucher_po_id%>" />
																						<input type="hidden" name="txt_voucher_git_id" id="txt_voucher_git_id" value="<%=mybean.voucher_git_id%>" />
																						<input type="hidden" name="txt_voucher_grn_id" id="txt_voucher_grn_id" value="<%=mybean.voucher_grn_id%>" />
																						<input type="hidden" name="txt_voucher_id" id="txt_voucher_id" value="<%=mybean.voucher_id%>" />
																						<input type="hidden" name="po_account" id="po_account" value="" />
																						<input type="hidden" name="txt_item_code" id="txt_item_code" />
																						<% if(mybean.vouchertype_id.equals("1")){%>
																						<input type="hidden" name="txt_gst_type" id="txt_gst_type" value="state" />
																						<%}else{%>
																							<input type="hidden" name="txt_gst_type" id="txt_gst_type" value="<%=mybean.gst_type %>" />
																						<%}%>
																						
                                                                                        <input type="hidden" name="txt_vouchertype_authorize" id="txt_vouchertype_authorize" value="<%=mybean.vouchertype_authorize %>" />
													                                    <input type="hidden" name="txt_vouchertype_defaultauthorize" id="txt_vouchertype_defaultauthorize" value="<%=mybean.vouchertype_defaultauthorize %>" />
																						<input type="hidden" name="txt_voucher_authorize" id="txt_voucher_authorize" value="<%=mybean.voucher_authorize %>" />
																					    <input type="hidden" name="txt_vouchertype_roundoff" id="txt_vouchertype_roundoff" value="<%=mybean.vouchertype_roundoff %>" />
																					    <input type="hidden" name="txt_vouchertype_roundoff_ledger_cr" id="txt_vouchertype_roundoff_ledger_cr" value="<%=mybean.vouchertype_roundoff_ledger_cr %>" />
																					    <input type="hidden" name="txt_vouchertype_roundoff_ledger_dr" id="txt_vouchertype_roundoff_ledger_dr" value="<%=mybean.vouchertype_roundoff_ledger_dr %>" />
																						<input type="hidden" name="txt_voucher_authorize_id" id="txt_voucher_authorize_id" value="<%=mybean.voucher_authorize_id %>" />
											                                            <input type="hidden" name="txt_voucher_authorize_time" id="txt_voucher_authorize_time" value="<%=mybean.voucher_authorize_time %>" />
																						
																						<!-- End of hidden filds -->
																						
																						<input name="txt_voucher_date" type="text" class="form-control datepicker" 
																							id="txt_voucher_date"
																							value="<%=mybean.voucherdate%>" size="12"
																							maxlength="12" />
																					</div>
																		<%
																			if (mybean.vouchertype_id.equals("2")) {
																		%>

																		<div class="form-element6"> <label></label> </div>

																		<% } %>
																	<%
																		if (mybean.vouchertype_id.equals("2")) {
																	%>

																	<div class="form-element6">
																					<label>From Location<font color="#ff0000">*</font>:
																					</label>
																						<select name="dr_vouchertrans_from_location_id"
																							class="form-control"
																							id="dr_vouchertrans_from_location_id">
																							<%=mybean.PopulateFromLocation()%>
																						</select>
																	</div>
																	<% } %>

																	<div class="form-element6">
																					<label>
																					<% if (!mybean.vouchertype_id.equals("2")) { %>
																					 Location<font color="#ff0000">*</font>:
																					  <% } else if (mybean.vouchertype_id.equals("2")) { %>
																					   To Location<font color="#ff0000">*</font>: <% } %>
																					    </label>

																						<%
																							if (mybean.vouchertype_id.equals("2")
																									|| mybean.vouchertype_id.equals("1")) {
																						%>
																						<select name="dr_voucher_location_id"
																							class="form-control" id="dr_voucher_location_id">
																							<%=mybean.PopulateLocation(mybean.branch_id)%>
																						</select>
																						<% } %>
																						<%
																							if (mybean.vouchertype_id.equals("20")) {
																						%>
																						<%
																							if (!mybean.voucher_git_id.equals("0")) {
																						%>
																						<a
																							href="../inventory/inventory-location-list.jsp?location_id=<%=mybean.vouchertrans_location_id%>">
																							<%=mybean.location_name%> <%
 																										} else {
																					 %> <select name="dr_voucher_location_id" class="form-control"
																							id="dr_voucher_location_id">
																								<%=mybean.PopulateLocation(mybean.branch_id)%>
																						</select> <% } %>
																						</a>
																						<%
																							} else if (mybean.vouchertype_id.equals("21")) {
																						%>
																						<%
																							if (mybean.status.equals("Add")) {
																						%>
																						<%
																							if (mybean.copyvouchertrans_voucher_id.equals("0")) {
																						%>
																						<select name="dr_voucher_location_id"
																							class="form-control" id="dr_voucher_location_id">
																							<%=mybean.PopulateLocation(mybean.branch_id)%>
																						</select>
																						<% } else { %>
																						<label class="control-label"><%=mybean.location_name%></label>
																						<% } %>
																						<% } else { %>
																						<%
																							if (mybean.voucher_grn_id.equals("0")) {
																						%>
																						<select name="dr_voucher_location_id"
																							class="form-control" id="dr_voucher_location_id">
																							<%=mybean.PopulateLocation(mybean.branch_id)%>
																						</select>
																						<% } else { %>
																						<label class="control-label"><%=mybean.location_name%></label>
																						<% } %>
																						<% } %>
																						<%
																							} else if (mybean.vouchertype_id.equals("12")) {
																						%>
																						<select name="dr_voucher_location_id"
																							class="form-control" id="dr_voucher_location_id">
																							<%=mybean.PopulateLocation(mybean.branch_id)%>
																						</select>
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
														if (!mybean.voucher_po_id.equals("0")) {
													%>
																	<div class="form-element6">
																					<label>PO ID: </label>
																						<a href="../accounting/voucher-list.jsp?voucherclass_id=12&vouchertype_id=12&voucher_id=<%=mybean.voucher_po_id%>"><%=mybean.voucher_po_id%></a>
																	</div>
																	<%
																		}
																	%>
																	<%
																		if (!mybean.voucher_git_id.equals("0")) {
																	%>

																	<div class="form-element6">
																					<label>GIT ID: </label>
																						<a href="../accounting/voucher-list.jsp?voucherclass_id=10&vouchertype_id=10&voucher_id=<%=mybean.voucher_git_id%>"><%=mybean.voucher_git_id%></a>
																	</div>
																	<%
																		}
																	%>

																	<%
																		if (!mybean.voucher_grn_id.equals("0")) {
																	%>

																	<div class="form-element6">
																					<label>GRN ID: </label>
																						<a
																							href="../accounting/voucher-list.jsp?voucherclass_id=20&vouchertype_id=20&voucher_id=<%=mybean.voucher_grn_id%>"><%=mybean.voucher_grn_id%></a>
																	</div>
																	<%
																		}
																	%>
																	<div class="row"></div>
																	<%
																		if (!mybean.vouchertype_id.equals("1")
																				&& !mybean.vouchertype_id.equals("2")) {
																	%>
																	<div class="form-element12">
																					<label>Supplier<font color="#ff0000">*</font>: </label>
																						<select class="form-control select2"
																							id="accountingsupplier" name="accountingsupplier">
																							<%=mybean.ledgercheck.PopulateLedgers("31",mybean.voucher_supplier_id, mybean.comp_id)%>
																						</select>
																						<span valign="top" " id="customer_cur_bal" width="50">
																							<%=mybean.ReturnCustomerCurrBalance(mybean.voucher_supplier_id, mybean.comp_id,mybean.vouchertype_id)%>
																						</span>
																						
																						<div> <a
																				href="ledger-add.jsp?add=yes&vouchertype_id=
																	<%=mybean.vouchertype_id%>&voucherclass_id=
																	<%=mybean.voucherclass_id%>&voucherclass_file=
																	<%=mybean.voucherclass_file%>&branch_id=
																	<%=mybean.branch_id%>&supplier=yes">Add
																					Ledger</a>
																			</div>
																	</div>
																			
																	<div class="form-element12">
																					<label>Contact<font color="#ff0000">*</font>: </label>
																						<span id="dr_contact_id">
																							<%=mybean.PopulateContact(mybean.voucher_supplier_id)%>
																										</span>
																	</div>

																	<%
																		}
																	%>

																	<div class="form-element12">
																					<label>Rate Class<font color="#ff0000">*</font>: </label>
																						<select name="dr_voucher_rateclass_id"
																							class="form-control" id="dr_voucher_rateclass_id">
																							<%=mybean.PopulateBranchClass(mybean.customer_branch_id, mybean.comp_id)%>
																						</select>
																	</div>
																	<%
																		if (mybean.vouchertype_id.equals("1")) {
																	%>
													<div class="form-element12">
														<input id='dr_incdec_id' name='dr_incdec_id'
															value='<%=mybean.PopulateIncreaseDecrease(mybean.incdec_id)%>'
															hidden></input>
													</div>
															 <% } %>
																	<%
																		if (mybean.vouchertype_id.equals("115")) {
																	%>

																	<div class="form-element10">
																					<label>Ledger </label>
																						<select name="dr_Legder_id" class="form-control"
																							id="dr_Legder_id">
																							<%=mybean.PopulateLedgers()%>
																						</select>
																	</div>

																	<%
																		}
																	%>
																	<div class="form-element12">
																						<br>
																						<div id="po_details" align="left" ></div>
																					</div>
																</div>

																<div class="form-element5">
																	<%
																		if (!mybean.voucher_git_id.equals("0")) {
// 																				|| !mybean.voucher_grn_id.equals("0")
																	%>
																	<%
																		} else {
																	%>

																		<div class="form-element10">
																					<label>Search: </label>
																						<input name="txt_search" type="text"
																							class="form-control" id="txt_search" value=""
																							size="30" maxlength="255"
																							onKeyUp="InvoiceItemSearch();" />
																						<div class="admin-master">
																							<a
																								href="../inventory/inventory-item-list.jsp?all=yes"
																								title="Manage Item"></a>
																						</div>
																						<br> <select name="dr_item_cat_id"
																							id="dr_item_cat_id"
																							onchange="InvoiceItemSearch();"
																							class="form-control">
																							<%=mybean.PopulateItemCatPop()%>
																						</select>
																		</div>
																	<% } %>
																	<%
																		if (!mybean.voucher_git_id.equals("0")
																				|| !mybean.voucher_grn_id.equals("0")) {
																	%>

																	<% } else { %>
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
																</div>
																<!-- 										</div> -->

																<div class="form-element6">
																					<label>Executive<font color="#ff0000">*</font>: </label>
																						<select name="dr_executive" id="dr_executive"
																							class="form-control">
																							<%=mybean.PopulateExecutives(mybean.emp_id, mybean.comp_id)%>
																						</select>
																</div>
																<%
																	if (!mybean.vouchertype_id.equals("1")
																			&& !mybean.vouchertype_id.equals("2")) {
																%>
																<div class="form-element6">
																					<label>Payment
																						Date<font color="#ff0000">*</font>:
																					</label>
																						<input name="txt_voucher_payment_date" type="text"
																							class="form-control datepicker"
																							id="txt_voucher_payment_date"
																							value="<%=mybean.paymentdate%>" size="12"
																							maxlength="10" />
																</div>
																<div class="row"></div>
																<%
																	if (mybean.status.equals("Add")
																				&& ((!mybean.voucher_po_id.equals("0")
																						|| !mybean.voucher_enquiry_id.equals("0")
																						|| !mybean.voucher_git_id.equals("0") || !mybean.voucher_grn_id
																							.equals("0"))
																						|| !mybean.voucher_poreturn_id.equals("0") || mybean.customer_add
																							.equals("yes"))) {
																%>
																
																<%
																	if (mybean.vouchertype_billing_add.equals("1")) {
																%>
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
																			</center></label> <label><font color="#ff0000">*</font>: </label>
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
																				</>Consignee Address(<a
																					href="javascript:CopyConsigneeAddress();">Same
																					as Billing</a>)
																			</center> </label> <label><font color="#ff0000">*</font>:
																		</label>
																			<textarea name="txt_voucher_consignee_add" rows="4"
																				class="form-control" id="txt_voucher_consignee_add"
																				onKeyUp="charcount('txt_voucher_consignee_add', 'span_txt_voucher_consignee_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_consignee_add%></textarea>
																			<span id="span_txt_voucher_consignee_add">
																				(255 Characters)</span>
																	</div>

																	<% } %> <% } %>
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
																</center> </label> 
																<label><font color="#ff0000">*</font>:
															</label>
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
																				</>Consignee Address (<a
																					href="javascript:CopyConsigneeAddress();">Same
																					as Billing</a>)
																			</center> </label> <label><font color="#ff0000">*</font>:
																		</label>
																			<textarea name="txt_voucher_consignee_add" rows="4"
																				class="form-control" id="txt_voucher_consignee_add"
																				onKeyUp="charcount('txt_voucher_consignee_add', 'span_txt_voucher_consignee_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_consignee_add%></textarea>
																			<span id="span_txt_voucher_consignee_add">
																				(255 Characters)</span>
																	</div>
																	<% } %>

																<% } %>
 <% } %> 
																<%
																	if (mybean.vouchertype_gatepass.equals("1")) {
																%>
																<div class="form-element6">
																					<label>ExecutiveGate Pass: </label> 
																						<input name="txt_voucher_gatepass" type="text"
																							class="form-control" id="txt_voucher_gatepass"
																							size="40" maxlength="255"
																							value="<%=mybean.voucher_gatepass%>" />
																</div>
																<%
																	}
																%>

																<%
																	if (mybean.vouchertype_lrno.equals("1")) {
																%>
																<div class="form-element6">
																					<label>Vehicle. No.: </label>
																						<input name="txt_voucher_lrno" type="text"
																							class="form-control" id="txt_voucher_lrno"
																							size="40" maxlength="255"
																							value="<%=mybean.voucher_lrno%>" />
																</div>
																<%
																	}
																%>

																<%
																	if (mybean.vouchertype_driver_no.equals("1")) {
																%>
																<div class="form-element6">
																					<label>Driver No.: </label>
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
																					<label>Tempo No.: </label>
																						<input name="txt_voucher_tempo_no" type="text"
																							class="form-control" id="txt_voucher_tempo_no"
																							size="40" maxlength="255"
																							value="<%=mybean.voucher_tempo_no%>" />
																</div>

																<% } %>
																<%
																	if (mybean.vouchertype_cashdiscount.equals("1")) {
																%>
																<div class="form-element6">
																					<label>Cash Discount: </label> 
																						<input name="txt_voucher_cashdiscount" type="text"
																							class="form-control"
																							id="txt_voucher_cashdiscount" size="12"
																							maxlength="11"
																							value="<%=mybean.voucher_cashdiscount%>"
																							onkeyup="toInteger(this.id);" />
																</div>

																<% } %>

																<%
																	if (mybean.vouchertype_turnoverdisc.equals("1")) {
																%>
																<div class="form-element6">
																					<label>Turnover Discount: </label>
																						<input name="txt_voucher_turnoverdisc" type="text"
																							class="form-control"
																							id="txt_voucher_turnoverdisc" size="12"
																							maxlength="11"
																							value="<%=mybean.voucher_turnoverdisc%>"
																							onkeyup="toInteger(this.id);" />
																</div>

																<% } %>
																<% } %>
																<%
																	if (mybean.vouchertype_ref_no_enable.equals("1")) {
																%>
																<%
																	if (mybean.vouchertype_id.equals("12")) {
																%>

																<!-- 										<div class="col-md-12"> -->
																<div class="form-element3">
																				<label>Special Order: </label>
																					<input name="chk_voucher_special" type="checkbox"
																						class="checkbox" id="chk_voucher_special"
																						<%=mybean.PopulateCheck(mybean.voucher_special)%> />
																</div>
																<!-- 										</div> -->
																<!-- 										<div class="col-md-12"> -->
																<div class="form-element3">
																				<label>Pending Order: </label>
																					<input name="chk_voucher_pending" type="checkbox"
																						class="checkbox" id="chk_voucher_pending"
																						<%=mybean.PopulateCheck(mybean.voucher_pending)%> />
																</div>
																<!-- 										</div> -->
																<%
																	}
																%>
																<!-- 										<div class="col-md-12"> -->
																<div class="form-element6">
																				<label>Reference No. <%
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
																<div class="row"></div>
																<div class="form-element6">
																				<label>Notes: </label>
																					<textarea name="txt_voucher_notes" cols="70"
																						rows="4" class="form-control"
																						id="txt_voucher_notes"
																						onkeyup="charcount('txt_voucher_notes', 'span_txt_voucher_notes','<font color=red>({CHAR} characters left)</font>', '5000')"><%=mybean.voucher_notes%></textarea>
																					<span id=span_txt_voucher_notes> 5000
																						characters</span>
																</div>
																<div class="form-element6 form-element-margin">
																				<label>Active: </label>
																					<input id="chk_voucher_active" type="checkbox"
																						name="chk_voucher_active"
																						<%=mybean.PopulateCheck(mybean.voucher_active)%> />
																</div>
																<!-- 										</div> -->
<div class="row"></div>
																<%
																	if (mybean.status.equals("Update") && !(mybean.entry_date == null)
																			&& !(mybean.entry_date.equals(""))) {
																%>
																<!-- 										<div class="col-md-12"> -->
																<div class="form-element6">
																				<label>Entry By: <%=mybean.unescapehtml(mybean.voucher_entry_by)%></label>
																					<input name="entry_by" type="hidden" id="entry_by"
																						value="<%=mybean.unescapehtml(mybean.voucher_entry_by)%>" />
																				</div>
																<div class="form-element6">
																				<label>Entry Date: <%=mybean.entry_date%></label>
																					<input type="hidden" id="entry_date"
																						name="entry_date" value="<%=mybean.entry_date%>" />
																				</div>
																<% } %>
																<%
																	if (mybean.status.equals("Update")
																			&& !(mybean.modified_date == null)
																			&& !(mybean.modified_date.equals(""))) {
																%>
																<!-- 										<div class="col-md-12"> -->
																			<div class="form-element6">
																				<label>Modified By:</label>
																					<%=mybean.unescapehtml(mybean.voucher_modified_by)%>
																					<input type="hidden" id="modified_by"
																						name="modified_by"
																						value="<%=mybean.unescapehtml(mybean.voucher_entry_by)%>" />
																			</div>
																<!-- 										</div> -->
																<!-- 										<div class="col-md-12"> -->
																			<div class="form-element6">
																				<label>Modified
																					Date:</label>
																					<%=mybean.modified_date%>
																					<input type="hidden" id="modified_date"
																						name="modified_date"
																						value="<%=mybean.modified_date%>" />
																			</div>
																<!-- 										</div> -->
																<%
																	}
																%>

																<!-- 										<div class="col-md-12"> -->

																			<div class="form-element12">
																				<center>
																					<%
																						if (mybean.status.equals("Add")) {
																					%>
																					<input name="addbutton" id="addbutton"
																						type="button"
																						onClick="return SubmitFormOnce(document.form1, this);"
																						class="btn btn-success"
																						value="Add <%=mybean.vouchertype_name%>" /> <input
																						type="hidden" name="add_button" value="yes">
																					<%
																						} else if (mybean.status.equals("Update")) {
																					%><input type="hidden" name="update_button"
																						value="yes"> <input type="hidden"
																						id="Update" name="Update" value="yes"> <input
																						name="updatebutton" id="updatebutton"
																						type="submit" class="btn btn-success"
																						value="Update <%=mybean.vouchertype_name%>"
																						onClick="return SubmitFormOnce(document.form1, this);" />
																					<input name="delete_button" type="submit"
																						class="btn btn-success" id="delete_button"
																						onClick="return confirmdelete(this)"
																						value="Delete <%=mybean.vouchertype_name%>" />
																					<%
																						}
																					%>
																				</center>
																			</div>
													</div>
											</div>
										</div>
									</div>
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
<script type="text/javascript" src="../Library/purchase.js"></script>
	
<script>    
$(function() {
		// credit to   
$("#accountingsupplier").change( function() {
		showHint( '../accounting/ledger-check.jsp?paymentdetail=yes&vouchertype_id=' + <%=mybean.vouchertype_id%> + '&customer_id=' + $(this).val(), 'customer_invoice');
  		showHint( '../accounting/ledger-check.jsp?currbal=yes&customer_id=' + $(this).val(), 'customer_cur_bal');
	 });

 $("#accountingsupplier").change(function(){
		showHint('../accounting/ledger-check.jsp?contact=yes&customer_id='+$(this).val(),'dr_contact_id');    
	});  
 $("#accountingsupplier").change(function(){
	 	showHint('../accounting/ledger-check.jsp?pay=yes&customer_id='+$(this).val(),'dr_paydays_id');  
	});
 $("#accountingsupplier").change(function(){   
        showHint('../accounting/ledger-check.jsp?address=yes&customer_id='+$(this).val(),'txt_voucher_billing_add');  
    });
});  
</script>


<script type="application/javascript">
$('#Hintclicktocall').on('hidden.bs.modal',function(){
	var vouchertype_id = document.getElementById('vouchertype_id').value;
	var cart_session_id = document.getElementById('cart_session_id').value;
	var voucher_id = document.getElementById('voucher_id').value;
	showHintFootable('../accounting/purchase-details.jsp?cart_session_id='+cart_session_id+'&cart_vouchertype_id='+vouchertype_id+'&cart_voucher_id='+voucher_id+'&list_cartitems=yes&refresh=no','po_details');
	});
</script>

<script type="application/javascript">
		

function getVoucherno(){
	var locationid;
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
	
<%-- 	showHint('../accounting/ledger-check.jsp?voucherno=yes&'+ '&vouchertype_defaultauthorize='+vouchertype_defaultauthorize + '&vouchertype_id=' + <%=mybean.vouchertype_id%> + '&branch_id='+ <%=mybean.branch_id%> --%>
// 				+ '&voucher_location_id=' +locationid + '&voucher_id=' +voucherid, 'voucherno');
}

 function MultipleGRN() {
	  var cart_session_id = document.getElementById("txt_session_id").value;  
	  var cart_vouchertype_id = document.getElementById("txt_vouchertype_id").value;  
	  var voucher_id = document.getElementById("txt_voucher_id").value;       
	  var voucher_customer_id = document.getElementById("accountingsupplier").value; 
	  if(voucher_customer_id == 0 || voucher_customer_id == '' || voucher_customer_id == null ){  
		  voucher_customer_id = 0;  
	  }
	  if(voucher_customer_id == 0){
		  alert("Select Supplier!");    
	  }else{
	  $('#dialog-modal').dialog({          
           autoOpen: false,
           width: 900,
           height: 600,
           zIndex: 200,  
           modal: true,
           title: "Multiple GRN",        
           close: function(){      
        	 //showHintFootable('../accounting/invoice-details.jsp?cart_vouchertype_id='+vouchertype_id+'&list_cartitems=yes','invoice_details');            
        	 showHintFootable('../accounting/purchase-details.jsp?cart_session_id='+cart_session_id+'&cart_vouchertype_id='+ cart_vouchertype_id +'&list_cartitems=yes','po_details');       
           }     
           });
                    
       $.ajax({  
    	   //url: "home.jsp",           
        success: function(data){             
       $('#dialog-modal').html('<iframe src="../accounting/voucher-multiple.jsp?cart_session_id='+cart_session_id+'&multiplegrn=yes&add=yes&cart_vouchertype_id='+ cart_vouchertype_id +'&voucher_id='+voucher_id+'&voucher_customer_id='+voucher_customer_id+'" height="100%" width="100%" frameborder=0></iframe>');
       }
       });
                        	 			  
      $('#dialog-modal').dialog('open');      
      return true;
    }   
 }
 
</script>
</body>
</HTML>
