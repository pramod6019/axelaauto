<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.SO_Update2" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<title><%= mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>   
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
<style>
/* 	.tax-row{  */
/* 		Display:None;  */
/* 	}  */
</style>
</HEAD>
<body onLoad ="GrandTotal(); <%if (mybean.status.equals("Add")) {%>getVoucherno();<%}%>
			 <%if (mybean.status.equals("Update")) {%><%}%> " class="page-container-bg-solid page-header-menu-fixed">
	<div id="dialog-modal"></div>
	<div id="dialog-modal-bill"></div>
	<%@include file="../portal/header.jsp"%>
<!-- 	START -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%= mybean.status%> <%= mybean.vouchertype_name%></h1>
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
						<li><a href="../sales/index.jsp">Accounting</a> &gt;</li>
						<li><a href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=<%=mybean.voucherclass_id%>&vouchertype_id=<%=mybean.vouchertype_id%>">List <%=mybean.vouchertype_name%>s </a> &gt;</li>
						<li><a href="so-update.jsp?<%= mybean.QueryString%>"></a><%= mybean.status%> <%= mybean.vouchertype_name%></a> :</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<form name="form1" id="form1" method="post" class="form-horizontal">
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.status%>
											<%=mybean.vouchertype_name%></div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<center>
												Form fields marked with a red asterisk <b>
												<font color="#ff0000">*</font></b> are required.
											</center>
											<input type="hidden"
												name="txt_config_inventory_current_stock"
												id="txt_config_inventory_current_stock"
												value="<%=mybean.config_inventory_current_stock%>" />
											<div id="getlocation"></div>
											<!-- QUOTE DETAILS SECTION -->
												<div class="form-element7">
													<input type="hidden" name="txt_voucher_id" id="txt_voucher_id" value="<%= mybean.voucher_id%>" />
													<input type="hidden" name="txt_parentvoucher" id="txt_parentvoucher" value="<%= mybean.parentvoucher%>" />
													<input type="hidden" name="txt_session_id" id="txt_session_id" value="<%= mybean.session_id%>" />
													<input type="hidden" name="cart_id" id="cart_id" /> <input type="hidden" name="uom_id" id="uom_id" />
													<input type="hidden" name="txt_voucherclass_id" id="txt_voucherclass_id" value="<%= mybean.voucherclass_id%>" />
													<input type="hidden" name="txt_vouchertype_id" id="txt_vouchertype_id" value="<%= mybean.vouchertype_id%>" />
													<input type="hidden" name="txt_voucher_prev_grandtotal" id="txt_voucher_prev_grandtotal" value="<%= mybean.voucher_prev_grandtotal%>" />
													<input type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%= mybean.branch_id%>" />
													<input type="hidden" name="txt_status" id="txt_status" value="<%= mybean.status%>" />
													<input type="hidden" name="voucher_contact" id="voucher_contact" value="" />
													<input type="hidden" name="txt_voucher_contact_id" id="txt_voucher_contact_id" value="<%= mybean.voucher_contact_id%>" />
													<input type="hidden" name="txt_location_id" id="txt_location_id" value="<%= mybean.vouchertrans_location_id%>" />
													<input type="hidden" name="hid_voucher_no" id="hid_voucher_no" value="<%= mybean.voucher_no%>" />
													<input type="hidden" name="lead_id" id="lead_id" value="<%= mybean.voucher_lead_id%>" />
													<input type="hidden" name="hid_check_voucherno" id="hid_check_voucherno" value="<%= mybean.check_voucher_no%>" />
													<input type="hidden" name="hid_check_branch" id="hid_check_branch" value="<%= mybean.checkbranch%>" />
													<input type="hidden" name="span_customer_name" id="span_customer_name" value="<%= mybean.so_customer_name%>" />
													<input type="hidden" name="span_contact_name" id="span_contact_name" value="<%= mybean.so_contact_name%>" />
													<input type="hidden" name="txt_vouchertype_mobile" id="txt_vouchertype_mobile" value="<%= mybean.vouchertype_mobile%>" />
													<input type="hidden" name="txt_vouchertype_email" id="txt_vouchertype_email" value="<%= mybean.vouchertype_email%>" />
													<input type="hidden" name="txt_vouchertype_dob" id="txt_vouchertype_dob" value="<%= mybean.vouchertype_dob%>" />
													<input type="hidden" name="txt_vouchertype_dnd" id="txt_vouchertype_dnd" value="<%= mybean.vouchertype_dnd%>" />
 													<input type="hidden" name="txt_branch_name" id="txt_branch_name" value="<%= mybean.branch_name%>" />
													<input type="hidden" name="txt_location_name" id="txt_location_name" value="<%= mybean.location_name%>" />
													<input type="hidden" name="txt_voucher_enquiry_id" id="txt_voucher_enquiry_id" value="<%= mybean.voucher_enquiry_id%>" />
													<input type="hidden" name="txt_voucher_quote_id" id="txt_voucher_quote_id" value="<%= mybean.voucher_quote_id%>" />
													<input type="hidden" name="txt_voucher_so_id" id="txt_voucher_so_id" value="<%= mybean.voucher_so_id%>" />
													<input type="hidden" name="txt_voucher_vehstock_id" id="txt_voucher_vehstock_id" value="<%= mybean.voucher_vehstock_id%>" />
													<input type="hidden" name="txt_voucher_delnote_id" id="txt_voucher_delnote_id" value="<%= mybean.voucher_delnote_id%>" />
													<input type="hidden" name="txt_voucher_preorder_id" id="txt_voucher_preorder_id" value="<%= mybean.voucher_preorder_id%>" />
													<input type="hidden" name="txt_voucher_id" id="txt_voucher_id" value="<%= mybean.voucher_id%>" />
													<input type="hidden" name="txt_gst_type" id="txt_gst_type" onChange="itemRepopulate();" value="<%=mybean.gst_type %>" />
													<input type="hidden" name="txt_vouchertype_authorize" id="txt_vouchertype_authorize" value="<%=mybean.vouchertype_authorize %>" />
													<input type="hidden" name="txt_vouchertype_defaultauthorize" id="txt_vouchertype_defaultauthorize" value="<%=mybean.vouchertype_defaultauthorize %>" />
													<input type="hidden" name="txt_voucher_authorize" id="txt_voucher_authorize" value="<%=mybean.voucher_authorize %>" />
													<input type="hidden" name="txt_voucher_authorize_id" id="txt_voucher_authorize_id" value="<%=mybean.voucher_authorize_id %>" />
													<input type="hidden" name="txt_voucher_authorize_time" id="txt_voucher_authorize_time" value="<%=mybean.voucher_authorize_time %>" />
													<input type="hidden" name="txt_voucher_ref_customer_id" id="txt_voucher_ref_customer_id" value="<%=mybean.voucher_ref_customer_id %>" />
													<input type="hidden" name="txt_vouchertype_roundoff" id="txt_vouchertype_roundoff" value="<%=mybean.vouchertype_roundoff %>" />
											    	<input type="hidden" name="txt_vouchertype_roundoff_ledger_cr" id="txt_vouchertype_roundoff_ledger_cr" value="<%=mybean.vouchertype_roundoff_ledger_cr %>" />
											    	<input type="hidden" name="txt_vouchertype_roundoff_ledger_dr" id="txt_vouchertype_roundoff_ledger_dr" value="<%=mybean.vouchertype_roundoff_ledger_dr %>" />
											<div class="form-element6">					
												 <label> Branch<font color="#ff0000">*</font></b>: </label>
														<% if (mybean.emp_branch_id.equals("0")) { %>
															<% if (mybean.status.equals("Add")) { %>
																<select name="dr_branch_id" class="form-control" id="dr_branch_id" onchange="getVoucherno();PopulateLocation();" >
															<%} else { %>
																<select name="dr_branch_id" class="form-control" id="dr_branch_id" onchange="PopulateLocation();" >
															<% } %>
					                                    	<%=mybean.PopulateBranch(mybean.branch_id, "", "", "", request)%>
					                                   			</select>
														<%} else{%>
															<a href="../portal/branch-summary.jsp?branch_id=<%= mybean.branch_id%>"><%= mybean.branch_name%></a>
														<%}%></div>
														
												<div class="form-element6">
													<label>Date<b><font color="#ff0000">*</font></b>: </label>
													<% if (mybean.empEditperm.equals("1")) { %>
														<input name="txt_voucher_date" type="text" class="form-control datepicker"  id="txt_voucher_date" value="<%=mybean.voucherdate%>" />
													<%}else{ %>
													<div> <%=mybean.voucherdate%>
														 <input type="hidden" name="txt_voucher_date" id="txt_voucher_date" value="<%=mybean.voucherdate%>" />
													</div>
													<%} %>
												</div>
												<div class="row"></div>
												
												<div class="form-element6">
													<label>Location<font color="#ff0000">*</font>: </label>
														<% if (mybean.vouchertype_id.equals("6")
																|| mybean.vouchertype_id.equals("5")
																|| mybean.vouchertype_id.equals("27")) { %>
														
														<span id="span_location">
														<Select id="dr_location_id" name="dr_location_id" class="form-control" >
																 <%= mybean.PopulateLocation(mybean.branch_id)%>
														</Select>
														</span>
<%-- 															<select name="dr_location_id" class="form-control" id="dr_location_id"> <%= mybean.PopulateLocation(mybean.branch_id)%> </select> --%>
														<%} else if (!mybean.voucher_delnote_id.equals("0")) { %>
														<a href="../inventory/inventory-location-list.jsp?location_id=<%= mybean.vouchertrans_location_id%>"><%= mybean.location_name%></a>
														<%} else { %>
														<label class="control-label col-md-2 col-xs-12">
															<a href="../inventory/inventory-location-list.jsp?location_id=<%=mybean.vouchertrans_location_id%>"><%=mybean.location_name%></a>
														</label>
														<input type='hidden' id='dr_location_id' name='dr_location_id' value='<%=mybean.vouchertrans_location_id%>'>
														<%} %>	
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
												<% if (!mybean.voucher_quote_id.equals("0") && mybean.voucher_enquiry_id.equals("0")) { %>
												<div class="form-element12">
													<label>Quote ID: </label>
														<a href="../accounting/voucher-list.jsp?voucherclass_id=5&vouchertype_id=5&voucher_id=<%= mybean.voucher_quote_id%>"><%= mybean.voucher_quote_id%></a>
												</div>
												<%} %>
												<% if (!mybean.voucher_invoice_id.equals("0") && mybean.voucher_enquiry_id.equals("0")) { %>
												<div class="form-element12">
													<label>Invoice ID: </label>
														<a href="../accounting/voucher-list.jsp?voucherclass_id=6&vouchertype_id=6&voucher_id=<%= mybean.voucher_invoice_id%>"><%= mybean.voucher_invoice_id%></a>
												</div>
												<%} %>
												
												<% if (!mybean.voucher_enquiry_id.equals("0") && mybean.voucher_quote_id.equals("0")) { %>
													<div class="form-element12">
														<label>Enquiry ID:</label>
															<a href="../sales/enquiry-list.jsp?enquiry_id=<%=mybean.voucher_enquiry_id%>"><%=mybean.voucher_enquiry_id%></a>
													</div>
												<%} %>
												<% if (!mybean.voucher_enquiry_id.equals("0") && !mybean.voucher_quote_id.equals("0")) { %>
													<div class="form-element12">
														<label>Enquiry ID:</label>
															<a href="../sales/enquiry-list.jsp?enquiry_id=<%=mybean.voucher_enquiry_id%>"><%=mybean.voucher_enquiry_id%></a>
														<br><label>Quote ID: </label>
															<a href="../sales/veh-quote-list.jsp?quote_id=<%=mybean.voucher_quote_id%>"><%=mybean.voucher_quote_id%></a>
													</div>
												<%} %>
												
												<% if(!mybean.voucher_preorder_id.equals("0")) { %>
														<div class="form-element6">
														<label>Pre-Order ID:</label>
														<a href="../sales/enquiry-list.jsp?enquiry_id=<%=mybean.voucher_preorder_id%>"><%=mybean.voucher_preorder_id%></a>
														</div>
															<% if(!mybean.voucher_so_id.equals("0")) { %>
																	<div class="form-element12">
																		<label>SO ID:</label>
																			<a href='../sales/veh-salesorder-list.jsp?1_dr_field=0-numeric&1_dr_param=0-numeric&1_txt_value_1=<%=mybean.voucher_so_id%>
																			&1_dr_filter=and&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1' target='_blank'><%=mybean.voucher_so_id%></a>
																	</div>
															<% } else if (!mybean.voucher_vehstock_id.equals("0")) { %>
																	<div class="form-element12">
																		<label>Stock ID:</label>
																			<a href='../inventory/stock-list.jsp?stock_id=<%=mybean.voucher_vehstock_id%>' target='_blank'><%=mybean.voucher_vehstock_id%></a>
																	</div>
															<%} %>
												
												<% }else if (!mybean.voucher_so_id.equals("0") && !mybean.voucher_delnote_id.equals("0")) { %>
														<div class="form-element12">
															<label>SO ID:</label>
																<a href="../accounting/voucher-list.jsp?voucher_id=<%=mybean.voucher_so_id%>"><%=mybean.voucher_so_id%></a>
															<br>
															<label>Del. Note ID:</label>
																<a href="../accounting/voucher-list.jsp?voucher_id=<%=mybean.voucher_delnote_id%>"><%=mybean.voucher_delnote_id%></a>
													</div>
													
												
												<%} else if (!mybean.voucher_so_id.equals("0")
															&& mybean.voucher_delnote_id.equals("0")) {
												%>
														<div class="form-element12">
															<label>SO ID:</label>
																<a href='../sales/veh-salesorder-list.jsp?1_dr_field=0-numeric&1_dr_param=0-numeric&1_txt_value_1=<%=mybean.voucher_so_id%>
																&1_dr_filter=and&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1' target='_blank'><%=mybean.voucher_so_id%></a>
														</div>
														<%} else if (!mybean.voucher_jc_id.equals("0")){ %>
														<div class="form-element12">
															<label>JC ID:</label>
																<a href='../service/jobcard-list.jsp?jc_id=<%=mybean.voucher_jc_id%>
																&1_dr_filter=and&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1'><%=mybean.voucher_jc_id%></a>
															<input type='text' value=<%= mybean.voucher_jc_id %> id='txt_jcid' name='txt_jcid' hidden>
														</div>
												<%}  else if (!mybean.voucher_delnote_id.equals("0")
															&& mybean.voucher_so_id.equals("0")) { %>
														<div class="form-element12">
															<label>Del.Note ID:</label>
																<a href="../accounting/voucher-list.jsp?voucher_id=<%=mybean.voucher_delnote_id%>"><%=mybean.voucher_delnote_id%></a>
														</div>
												
												<%} else if (!mybean.voucher_vehstock_id.equals("0")) { %>
														<div class="form-element12">
															<label>Stock ID:</label>
																<a href="../inventory/stock-list.jsp?stock_id=<%=mybean.voucher_vehstock_id%>" target=_blank><%=mybean.voucher_vehstock_id%></a>
														</div>
												<%} %>
												
												
												<input type='hidden' id="txt_soid" name="txt_soid" value="<%=mybean.voucher_so_id%>">
											<!-- ITEM SEARCH SECTION -->

										<!--  Customer and Contact Will Not Come For Pre-Order -->
											<% if(!mybean.vouchertype_id.equals("27")) {%>
												       <div class="form-element12">
															<label>Party<font color="#ff0000">*</font>: </label>
															<select class="form-control select2" id="accountingcustomer" name="accountingcustomer">
															<%=mybean.ledgercheck.PopulateLedgers("32", mybean.voucher_customer_id, mybean.comp_id)%>
															</select>
															
															<div valign="top" align="left" id="customer_cur_bal"
																		width="50"><%=mybean.ReturnCustomerCurrBalance(mybean.voucher_customer_id, mybean.comp_id, mybean.vouchertype_id)%></div>
															<a href="ledger-add.jsp?add=yes&voucherclass_id=<%= mybean.voucherclass_id%>&vouchertype_id=<%= mybean.vouchertype_id%>&voucherclass_file=<%= mybean.voucherclass_file%>&vouchertype_mobile=<%= mybean.vouchertype_mobile%>&vouchertype_email=<%= mybean.vouchertype_email%>&vouchertype_dob=<%= mybean.vouchertype_dob%>&vouchertype_dnd=<%= mybean.vouchertype_dnd%>"> Add Ledger</a>
														</div>
													<div class="form-element12">
															<label>Contact<font color="#ff0000">*</font>:</label>
																<span id="span_contact_id">
																	<%=mybean.PopulateContact(mybean.voucher_customer_id)%>
																</span>
													</div>
													
													<% }else{%>
													
													<!--	THIS is to set default Customer and Contact value for Pre-Order -->
													<input type='text' value='0' name='accountingcustomer' hidden />
													<input type='text' value='0' id='dr_contact_id' name='dr_contact_id' hidden />
													<%} %>
													
													
													<div class="form-element12">
															<label>Rate Class<font color="#ff0000">*</font>: </label>
													<select
														name="dr_voucher_rateclass_id" class="form-control"
														id="dr_voucher_rateclass_id">
															<%= mybean.PopulateBranchClass(mybean.customer_branch_id, mybean.comp_id)%>
													</select>
													</div>
																						
												</div>
												
												
												<div id="showItems" name="showItems" class='row'>
												<%=mybean.populateItems(request)%>
												<div class='row'></div>
												<button type="button" id='deleteitem' class="deleteitem btn btn-success">- Delete</button>
												<button type="button" id='additem' class="additem btn btn-success">+ Add More</button>
												<span id='discount_model_link' style='display:none;' >
												 </span>
											</div>

											<div class="row"></div>
								
								<% if (mybean.status.equals("Add")
												&& ((!mybean.voucher_quote_id.equals("0")
														|| !mybean.voucher_enquiry_id.equals("0")
														|| !mybean.voucher_so_id.equals("0"))
														|| !mybean.voucher_delnote_id.equals("0")
														|| mybean.customer_add .equals("yes"))) { %>
									<% if (mybean.vouchertype_billing_add.equals("1")) { %>
											<div class="form-element6">
												<label>Billing Address<font color="#ff0000">*</font>:</label>
<%-- 														<% --%>
<!-- // 																	if ((mybean.voucher_contact_id.equals("0") || mybean.voucher_contact_id -->
<!-- // 																					.equals("")) && mybean.add.equals("yes")) { -->
<%-- 																%> <!--<div id="copy_cont_address_link"> (<a href="javascript:CopyContactAddress();">Same as Contact</a>)</div> --> --%>

<%-- 																	<% --%>
<!-- // 																		} -->
<%-- 																	%> --%>
													
													<textarea name="txt_voucher_billing_add" rows="4"
														class="form-control" id="txt_voucher_billing_add"
														onKeyUp="charcount('txt_voucher_billing_add', 'span_txt_voucher_billing_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_billing_add%></textarea>
													<br /> <span id="span_txt_voucher_billing_add">
														(255 Characters)</span>

											</div>
											<% if (mybean.vouchertype_consignee_add.equals("1")) { %>
											<div class="form-element6">
												<label> Consignee Address (<a href="javascript:CopyConsigneeAddress();">Same as Billing Address</a>)
													<font color="#ff0000">*</font>: </label>
													<textarea name="txt_voucher_consignee_add" rows="4"
														class="form-control" id="txt_voucher_consignee_add"
														onKeyUp="charcount('txt_voucher_consignee_add', 'span_txt_voucher_consignee_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_consignee_add%></textarea>
													<br /> <span id="span_txt_voucher_consignee_add">
														(255 Characters)</span>

											</div>
											<% } %> <% } %>
											
											<%
												} else {
											%>
											<%
												if (mybean.vouchertype_billing_add.equals("1")) {
											%>
											<div class="form-element6">
												<label><center>
														Billing Address
														<% if ((mybean.voucher_contact_id.equals("0")
															|| mybean.voucher_contact_id.equals(""))
															&& mybean.add.equals("yes")) { %>
<!-- 														<div id="copy_cont_address_link"> (<a href="javascript:CopyContactAddress();">Same as Contact</a>)</div> -->
														<% } %>
													</center> </label> <label>Address<font
													color="#ff0000">*</font>:
												</label>
													<textarea name="txt_voucher_billing_add" rows="4"
														class="form-control" id="txt_voucher_billing_add"
														onKeyUp="charcount('txt_voucher_billing_add', 'span_txt_voucher_billing_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_billing_add%></textarea>
													<br /> <span id="span_txt_voucher_billing_add">
														(255 Characters)</span>

											</div>

											<% if (mybean.vouchertype_consignee_add.equals("1")) { %>
											<div class="form-element6">
												<label>Consignee Address ( <a href="javascript:CopyConsigneeAddress();">Same as Billing Address</a>)<font color="#ff0000">*</font>:</label>
													<textarea name="txt_voucher_consignee_add" rows="4"
														class="form-control" id="txt_voucher_consignee_add"
														onKeyUp="charcount('txt_voucher_consignee_add', 'span_txt_voucher_consignee_add','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.voucher_consignee_add%></textarea>
													<br /> <span id="span_txt_voucher_consignee_add">
														(255 Characters)</span>
											</div>
											<% } %>
                                       <%} %>
								
								<%} %>
								
								<% if (mybean.vouchertype_transporter.equals("1")) { %>
												<div class="form-element6">
															<label>Transporter:
																</label>
												<input name="txt_voucher_transporter"
											type="text" class="form-control" id="txt_voucher_transporter"
											size="40" maxlength="255"
											value="<%= mybean.voucher_transporter%>" />
															</div>
									
									<%} %>
		                            <% if (mybean.vouchertype_gatepass.equals("1")) { %>
									
											<div class="form-element6">
															<label>Gate Pass:</label>
												<input name="txt_voucher_gatepass"
											type="text" class="form-control" id="txt_voucher_gatepass"
											size="40" maxlength="255"
											value="<%= mybean.voucher_gatepass%>" />
											</div>
								    <%} %>
								    
								<% if (mybean.vouchertype_lrno.equals("1")) { %>
											<div class="form-element6">
															<label>Vehicle. No.:</label>
													<input name="txt_voucher_lrno" type="text"
											class="form-control" id="txt_voucher_lrno" size="40"
											maxlength="255" value="<%= mybean.voucher_lrno%>" />
											</div>
									<%} %>
									<% if (mybean.vouchertype_driver_no.equals("1")) { %>
											<div class="form-element6">
															<label>Driver No.:</label>
													<input name="txt_voucher_driver_no"
											type="text" class="form-control" id="txt_voucher_driver_no"
											size="40" maxlength="255"
											value="<%= mybean.voucher_driver_no%>" />
											</div>
									<% } %>
									
									<% if (mybean.vouchertype_tempo_no.equals("1")) { %>
											<div class="form-element6">
															<label>Tempo No.:</label>
													<input name="txt_voucher_tempo_no"
											type="text" class="form-control" id="txt_voucher_tempo_no"
											size="40" maxlength="255"
											value="<%= mybean.voucher_tempo_no%>" />
											</div>
									<% } %>
									<% if (mybean.vouchertype_cashdiscount.equals("1")) { %>
									
											<div class="form-element6">
															<label>Cash Discount:</label>
													<input name="txt_voucher_cashdiscount"
											type="text" class="form-control" id="txt_voucher_cashdiscount"
											size="12" maxlength="11"
											value="<%= mybean.voucher_cashdiscount%>"
											onkeyup="toInteger(this.id);" />
											</div>
									
									<% } %>
									<% if (mybean.vouchertype_turnoverdisc.equals("1")) { %>
											<div class="form-element6">
															<label>Turnover Discount:</label>
													<input name="txt_voucher_turnoverdisc"
											type="text" class="form-control" id="txt_voucher_turnoverdisc"
											size="12" maxlength="11"
											value="<%= mybean.voucher_turnoverdisc%>"
											onkeyup="toInteger(this.id);" />
											</div>
									
									<%} %>
									
									
									<div class="form-element6">
															<label>Executive<font color="#ff0000">*</font>:</label>
										<select name="dr_executive"
											id="dr_executive" class="form-control">
										<%if (!mybean.status.equals("Update")){ %>
												<%if(mybean.voucher_jc_id.equals("0")){ %>
														<%if(!mybean.voucher_emp_id.equals("0")){ %>
																<%= mybean.PopulateExecutives(mybean.voucher_emp_id, mybean.comp_id)%>
														<%}else{ %>
																<%= mybean.PopulateExecutives(mybean.emp_id, mybean.comp_id)%>
														<%} %>
												<%}else{ %>
														<%= mybean.PopulateExecutives(mybean.voucher_jc_emp_id, mybean.comp_id)%>
												<%} %>
											<%}else{ %>
													<%= mybean.PopulateExecutives(mybean.voucher_emp_id, mybean.comp_id)%>
											<%} %>
										</select> 
										</div>
										
									<% if (mybean.vouchertype_ref_no_enable.equals("1")) { %>
									<div class="form-element6">
															<label>Reference No. <%
											if (mybean.vouchertype_ref_no_mandatory.equals("1")) {
										%> <font color="#ff0000">*</font>: <% } %></label>
									<input name="txt_voucher_ref_no"
											type="text" class="form-control" id="txt_voucher_ref_no"
											value="<%= mybean.voucher_ref_no%>" size="32" maxlength="50" />		
										</div>
									<% } %>
									
									<% if (mybean.vouchertype_id.equals("27")) { %>
									<div class="form-element6">
															<label>Fitted:</label>
									<input id="chk_voucher_fitted"
											type="checkbox" name="chk_voucher_fitted"
											<%= mybean.PopulateCheck(mybean.voucher_fitted)%> />
										</div>
									<% }else{ %>
									<% } %>
												<div class="row"></div>
												<div class="form-element6">
													<label>Notes:</label>
													<textarea name="txt_voucher_notes" cols="70" rows="4"
														class="form-control" id="txt_voucher_notes"><%=mybean.voucher_notes%></textarea>
												</div> 
												<div class="form-element6">
													<label>Active:</label> <input id="chk_voucher_active"
														type="checkbox" name="chk_voucher_active"
														<%=mybean.PopulateCheck(mybean.voucher_active)%> />
												</div>
												<div class="row"></div>
												
										<% if (mybean.status.equals("Update")
													&& !(mybean.entry_date == null)
													&& !(mybean.entry_date.equals(""))) { %>
										<div class="form-element6">
															<label>Entry By: <%= mybean.unescapehtml(mybean.voucher_entry_by)%></label>
												<input name="entry_by" type="hidden" id="entry_by"
												value="<%= mybean.unescapehtml(mybean.voucher_entry_by)%>" />
										</div>
										<div class="form-element6">
															<label>Entry Date: <%= mybean.entry_date%></label> <input
												type="hidden" id="entry_date" name="entry_date"
												value="<%= mybean.entry_date%>" />
										</div>
										
										<% } %>
										<% if (mybean.status.equals("Update")
													&& !(mybean.modified_date == null)
													&& !(mybean.modified_date.equals(""))) { %>
										<div class="form-element6">
															<label>Modified By: <%= mybean.unescapehtml(mybean.voucher_modified_by)%></label>
												<input type="hidden" id="modified_by" name="modified_by"
												value="<%= mybean.unescapehtml(mybean.voucher_entry_by)%>" />
										</div>
										<div class="form-element6">
															<label>Modified Date: <%= mybean.modified_date%></label>
															<input type="hidden" id="modified_date" name="modified_date"
												value="<%= mybean.modified_date%>" />
										</div>
										<% } %>
										
										
										<% if (mybean.status.equals("Add")) { %>
										<center><input name="addbutton" id="addbutton" type="button"
												onClick="return SubmitFormOnce(document.form1, this);"
												class="btn btn-success" value='Add <%= mybean.vouchertype_name%>' />
												<input type="hidden" name="add_button" value="yes"></center>
												<% } else if (mybean.status.equals("Update")) { %>
												<center><input type="hidden" name="update_button" value="yes">
												<input type="hidden" id="Update" name="Update" value="yes">
												<input name="updatebutton" id="updatebutton" type="submit"
												class="btn btn-success" value='Update <%= mybean.vouchertype_name%>'
												onClick="return SubmitFormOnce(document.form1, this);" />&nbsp;&nbsp;<input
												name="delete_button" type="submit" class="btn btn-success"
												id="delete_button" onClick="return confirmdelete(this)"
												value='Delete <%= mybean.vouchertype_name%>' /></center>
												
												<% } %>
<!-- 							CONTAINER FLUID END -->
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- 	END -->
<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
<script type="text/javascript" src="../Library/sales2.js?ver="+<%=mybean.jsver %>></script>
<script type="text/javascript">

function PopulateLocation(){
	var branch_id = document.getElementById("dr_branch_id").value;
	document.getElementById("txt_branch_id").value = branch_id;
	showHint('../accounting/accounting-branch-check.jsp?sales_branch_id='+branch_id+'&branch_location=yes','span_location');     
	}

function getVoucherno(){
	var vouchertype_defaultauthorize = <%= mybean.vouchertype_defaultauthorize%>
        var voucherid = document.getElementById("txt_voucher_id").value;
		var parentvoucher = document.getElementById("txt_parentvoucher").value;
		var status = "<%= mybean.status%>";
		if(parentvoucher == ""){
			if(<%= mybean.vouchertype_id%> == 27){
				branchid = document.getElementById("dr_branch_id").value;
			}else{
		 		branchid = <%= mybean.branch_id%>;
			}
		} else {
				branchid = document.getElementById("txt_branch_id").value;
		}
		
    	showHint('../accounting/ledger-check.jsp?voucherno=yes'
    			+ '&vouchertype_defaultauthorize='+vouchertype_defaultauthorize
    			+ '&vouchertype_id=' + <%= mybean.vouchertype_id%>
    			+ '&branch_id=' + branchid 
    			+ '&status=' + status 
    	        + '&voucher_id=' +voucherid, 'voucherno');
					}
 var srcEvent = null;  

 $("input[type=text],input[type=number]")
     .mousedown(function (event) {
         srcEvent = event;
     })
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
   $("#accountingcustomer").change( function() {
        	showHint('../accounting/ledger-check.jsp?contact=yes&customer_id='+$(this).val(),'span_contact_id');
            showHint('../accounting/ledger-check.jsp?invoicedetail=yes&vouchertype_id=' +<%= mybean.vouchertype_id%> + '&customer_id=' + $(this).val(),'customer_invoice');
            showHint('../accounting/ledger-check.jsp?vouchertype_id='+<%= mybean.vouchertype_id%> + '&currbal=yes&customer_id='+ $(this).val(), 'customer_cur_bal');
 			showHint('../accounting/ledger-check.jsp?address=yes&customer_id='+$(this).val(),'txt_voucher_billing_add');
 			
		});
 

	$("#maincity").change(function(){
		showHint('../sales/enquiry-check.jsp?city=yes&city_id=' +$(this).val(), 'zone');
	});
	                             	
// 	$("#accountingcustomer").change(function(){
// 			var voucher_date = document.getElementById("txt_voucher_date").value;
// 			showHint('../accounting/ledger-check.jsp?payment_date=yes&voucher_date='+voucher_date+'&customer_id='+$(this).val(),'paydate');
<%-- 	<%if (mybean.vouchertype_id.equals("114")) {%> --%>
// 			setTimeout(function(){ document.getElementById("txt_voucher_payment_date").value = $("#paydate").val(); }, 200);
<%-- 	<%}%> --%>
								   
// 	});   
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

</body>
</HTML>
