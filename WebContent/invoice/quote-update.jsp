<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.invoice.Quote_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-timepicker.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />

<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../assets/css/footable.core.css">
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />


<script>
 $(function() {
    $( "#txt_quote_date" ).datepicker({
      showButtonPanel: true,    
      dateFormat: "dd/mm/yy"
    });

  });
</script>
<style>
@media ( max-width : 1024px) {
	td {
		display: block;
	}
}
</style>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body
	onLoad="<%if (mybean.status.equals("Add")) {%>list_cart_items();<%}%> <%if (mybean.status.equals("Update")) {%>list_cart_items();<%}%>"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Quote Update</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="index.jsp">Invoice</a> &gt;</li>
						<li><a href="quote.jsp">Quotes</a> &gt;</li>
						<li><a href="quote-list.jsp?all=recent">List Quotes</a> &gt;</li>
						<li><a href="quote-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Quote</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<form name="form1" id="form1" method="post"
								class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>&nbsp;Quote
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<input type="hidden" name="txt_session_id"
												id="txt_session_id" value="<%=mybean.session_id%>" /> <input
												type="hidden" name="cart_id" id="cart_id" /> <input
												type="hidden" name="txt_branch_id" id="txt_branch_id"
												value="<%=mybean.branch_id%>" /> <input type="hidden"
												name="txt_status" id="txt_status" value="<%=mybean.status%>" />
											<input type="hidden" name="quote_contact" id="quote_contact"
												value="" /> <input type="hidden" name="txt_rateclass_id	"
												id="txt_rateclass_id	" value="<%=mybean.rateclass_id%>" />
											<input type="hidden" id="lead_id" name="lead_id"
												value="<%=mybean.lead_id%>" />
											<div class="form-group">
												<label class="control-label col-md-4"> Branch: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<a
														href="../portal/branch-summary.jsp?branch_id=<%=mybean.branch_id%>"><%=mybean.branch_name%></a>
													<input type="hidden" name="txt_branch_name"
														id="txt_branch_name" value="<%=mybean.branch_name%>" />
													<%
														if (mybean.emp_branch_id.equals("0")
																&& mybean.quote_enquiry_id.equals("0")
																&& mybean.add.equals("yes")) {
													%>
													<a href="invoice-branch.jsp?para=quote">(Change Branch)</a>
													<%
														}
													%>

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Date:<font
													color=#ff0000><b> *</b></font></label>
												<div class="col-md-6">
													<%
														if (mybean.empEditperm.equals("1")) {
													%>
													<input name="txt_quote_date" id="txt_quote_date"
														value="<%=mybean.quotedate%>"
														class="form-control date-picker"
														data-date-format="dd/mm/yyyy" type="text" value="" />
													<%
														} else {
													%>
													<%=mybean.quotedate%>
													<input name="txt_quote_date" type="hidden"
														class="form-control" id="txt_quote_date"
														value="<%=mybean.quotedate%>" />
													<%
														}
													%>
												</div>
											</div>

											<%
												if (mybean.status.equals("Add") && mybean.contact_id.equals("0")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4"> Customer: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_customer_name" type="text"
														class="form-control" id="txt_customer_name"
														value="<%=mybean.customer_name%>" size="50"
														maxlength="255" />

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Contact Name<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<table class="table">
														<tr>
															<td><select name="dr_title" class="form-control"
																id="dr_title">
																	<%=mybean.PopulateTitle()%>
															</select> Title</td>
															<td><input name="txt_contact_fname" type="text"
																class="form-control" id="txt_contact_fname"
																value="<%=mybean.contact_fname%>" size="20"
																maxlength="255" onkeyup="ShowNameHint()" /> First Name
															</td>
															<td><input name="txt_contact_lname" type="text"
																class="form-control" id="txt_contact_lname"
																value="<%=mybean.contact_lname%>" size="20"
																maxlength="255" onkeyup="ShowNameHint()" /> Last Name</td>
														</tr>
													</table>

													<!-- 													<span style="float: left"> <select name="dr_title" -->
													<!-- 														class="form-control" id="dr_title"> -->
													<%-- 															<%=mybean.PopulateTitle()%> --%>
													<!-- 													</select> <br /> Title -->
													<!-- 													</span> <input name="txt_contact_fname" type="text" -->
													<!-- 														class="form-control" id="txt_contact_fname" style="width:40%" -->
													<%-- 														value="<%=mybean.contact_fname%>" size="20" --%>
													<!-- 														maxlength="255" onkeyup="ShowNameHint()" /> <br /> First -->
													<!-- 													Name <input name="txt_contact_lname" type="text" -->
													<!-- 														class="form-control" id="txt_contact_lname" style="width:40%" -->
													<%-- 														value="<%=mybean.contact_lname%>" size="20" --%>
													<!-- 														maxlength="255" onkeyup="ShowNameHint()" /> <br /> Last -->
													<!-- 													Name -->

												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4"> Mobile<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_contact_mobile1" type="text"
														class="form-control" id="txt_contact_mobile1"
														value="<%=mybean.contact_mobile1%>" size="20"
														maxlength="13"
														onkeyup="showHint('quote-check.jsp?contact_mobile1='+GetReplace(this.value)+'&session_id=<%=mybean.session_id%>','quote_details');toPhone('txt_contact_mobile1','Mobile');" />
													(91-9999999999) <span id="txtHint1"></span>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Phone: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_contact_phone1" type="text"
														class="form-control" id="txt_contact_phone1"
														onkeyup="showHint('quote-check.jsp?contact_phone1='+GetReplace(this.value)+'&session_id=<%=mybean.session_id%>','quote_details'); toPhone('txt_contact_phone1','Phone');"
														value="<%=mybean.contact_phone1%>" size="20"
														maxlength="14"> (91-80-33333333) 
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Email: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_contact_email1" type="text"
														class="form-control" id="txt_contact_email1"
														value="<%=mybean.contact_email1%>" size="30"
														maxlength="255"
														onkeyup="showHint('quote-check.jsp?contact_email1='+GetReplace(this.value)+'&session_id=<%=mybean.session_id%>','quote_details');">
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Address<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<textarea name="txt_contact_address" rows="4"
														class="form-control" id="txt_contact_address"
														onkeyup="charcount('txt_contact_address', 'span_txt_contact_address','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.contact_address%></textarea>
													<span id="span_txt_contact_address"> (255
														Characters)</span>

												</div>
											</div>
											<div class="form-group" id="contact_city">
												<label class="control-label col-md-4"> City<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<span id="contact_city_id"><%=mybean.PopulateCity(mybean.state_id,
						mybean.contact_city_id, "dr_contact_city_id")%></span>
													

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Pin/Zip<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_contact_pin" type="text"
														class="form-control" id="txt_contact_pin"
														onkeyup="toInteger('txt_contact_pin','Pin')"
														value="<%=mybean.contact_pin%>" size="10" maxlength="6" />

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> State<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<span id="contact_state_id"><%=mybean.PopulateState(mybean.state_id,
						"contact_city_id", "dr_contact_state_id")%></span>
													

												</div>
											</div>
											<div id=selected_contact style="display: none">
												<div class="form-group">
													<label class="control-label col-md-4"> Customer: </label>
													<div class="col-md-6 col-xs-12" id="emprows">
														<b><span id="selected_customer_id"
															name="selected_customer_id"></span></b>&nbsp; <input
															name="span_acct_id" type="hidden" id="span_acct_id"
															value="<%=mybean.customer_id%>"> <input
															name="acct_id" type="hidden" id="acct_id"
															value="<%=mybean.quote_customer_id%>">
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-4"> Contact<font
														color=red>*</font>:
													</label>
													<div class="col-md-6 col-xs-12" id="emprows">
														<b><span id="selected_contact_id"
															name="selected_contact_id"></span></b>&nbsp;
														<%
															if (mybean.quote_enquiry_id.equals("0")) {
														%>
														<a href="#" id="dialog_link">(Select Contact)</a>
														<%
															}
														%>
														<input name="span_cont_id" type="hidden" id="span_cont_id"
															value="<%=mybean.contact_id%>"> <input
															name="cont_id" type="hidden" id="cont_id"
															value="<%=mybean.quote_contact_id%>">
														<div id="dialog-modal"></div>
													</div>
												</div>
											</div>
											<%
												} else {
											%>
											<div valign="center" id="contact_link">
												<div class="form-group">
													<label class="control-label col-md-4"> Customer: </label>
													<div class="col-md-6 col-xs-12" id="emprows">
														<b><span id="span_quote_customer_id"
															name="span_quote_customer_id"><%=mybean.link_customer_name%></span></b>&nbsp;
														<input name="span_acct_id" type="hidden" id="span_acct_id"
															value="<%=mybean.customer_id%>"> <input
															name="acct_id" type="hidden" id="acct_id"
															value="<%=mybean.quote_customer_id%>">
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-4"> Contact: </label>
													<div class="col-md-6 col-xs-12" id="emprows">
														<b><span id="span_quote_contact_id"
															name="span_quote_contact_id"><%=mybean.link_contact_name%></span></b>&nbsp;
														<%
															if (mybean.quote_enquiry_id.equals("0")) {
														%>
														<a href="#" id="dialog_link">(Select Contact)</a>
														<%
															}
														%>
														<input name="span_cont_id" type="hidden" id="span_cont_id"
															value="<%=mybean.contact_id%>"> <input
															name="cont_id" type="hidden" id="cont_id"
															value="<%=mybean.quote_contact_id%>">
														<div id="dialog-modal"></div>
													</div>
												</div>
											</div>
											<%
												if (!mybean.quote_enquiry_id.equals("0")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4"> Enquiry ID: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<b><a
														href="enquiry-list.jsp?enquiry_id=<%=mybean.quote_enquiry_id%>"><%=mybean.quote_enquiry_id%></a></b>

												</div>
											</div>
											<%
												}
											%>
											<%
												}
											%>
											<center>
												<div id="quote_details"></div>
											</center>
											<div class="form-group">
												<label class="control-label col-md-4"> Search: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_search" type="text" class="form-control"
														id="txt_search" value="" size="30" maxlength="255"
														onkeyup="QuoteItemSearch();" />
													<div class="admin-master">
														<a href="../inventory/inventory-item-list.jsp?all=yes"
															title="Manage Item"></a>
													</div>
													<div class="hint" id="hint_search_item">Enter your
														search parameter!</div>
												</div>
											</div>

										</div>
									</div>
								</div>
								<!-- 			Item Details Portlet Start -->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Item Details</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->

											<div class="form-group">
												<label class="control-label col-md-4"> Quantity: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_item_qty" type="text" class="form-control"
														id="txt_item_qty" value="<%//=mybean.item_qty%>" size="10"
														maxlength="10" onkeyup="CalItemTotal();"> <input
														type="hidden" id="emp_quote_priceupdate"
														name="emp_quote_priceupdate"
														value="<%=mybean.emp_quote_priceupdate%>"> <input
														type="hidden" id="emp_quote_discountupdate"
														name="emp_quote_discountupdate"
														value="<%=mybean.emp_quote_discountupdate%>"> <input
														type="hidden" id="txt_item_baseprice"
														name="txt_item_baseprice" value=""> <input
														type="hidden" id="txt_item_type_id"
														name="txt_item_type_id"> <input type="hidden"
														id="txt_item_pricevariable" name="txt_item_pricevariable"
														value=""> <input type="hidden" id="txt_serial"
														name="txt_serial"> <input type="hidden"
														id="txt_itemprice_updatemode"
														name="txt_itemprice_updatemode"> <input
														type="hidden" id="txt_mode" name="txt_mode">
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4"> Price: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_item_price" type="text"
														class="form-control" id="txt_item_price"
														value="<%//=mybean.item_price%>" size="10" maxlength="10"
														onkeyup="CalItemTotal();" onChange="CheckBasePrice();">
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4"> Discount: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_item_disc" type="text"
														class="form-control" id="txt_item_disc"
														value="<%//=mybean.item_disc%>" size="10" maxlength="10"
														onkeyup="CalItemTotal();">
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4" id="tax_name">
													<input name="txt_item_tax" type="hidden" id="txt_item_tax"
													value=""> <input name="txt_item_tax_id"
													type="hidden" id="txt_item_tax_id" value="">Tax: &nbsp;&nbsp;
													<span id="item_tax">0</span>
												</label>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4"> <input
													name="txt_item_total" type="hidden" id="txt_item_total"
													value="">Total: &nbsp;&nbsp;
													<span id="item_total">0</span>
												</label>
											</div>
											<center>
												<div id="mode_button">
													<input name="add_button" id="add_button" type="button"
														class="btn btn-success" value="Add"
														onClick="AddCartItem();" />
												</div>
											</center>
											<br>

											<div class="form-group" id="serial_details"
												style="display: none">
												<label class="control-label col-md-4"> Serial No.<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_item_serial" type="text"
														class="form-control" id="txt_item_serial" size="20"
														maxlength="30">
												</div>
											</div>
											<center>
												<span id="configure-details"></span>
											</center>
										</div>
									</div>
								</div>
								<!-- 			Item Details Portlet End -->
								<!-- 			Billing Address Portlet Start -->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											Billing Address
											<%
											if ((mybean.quote_contact_id.equals("0") || mybean.quote_contact_id
													.equals("")) && mybean.add.equals("yes")) {
										%>
											<div id="copy_cont_address_link"
												style="display: inline-block">
												(<a href="javascript:CopyContactAddress();">Same as
													Contact</a>)
											</div>
											<%
												}
											%>
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-group">
												<label class="control-label col-md-4"> Address<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<textarea name="txt_quote_bill_address" rows="4"
														class="form-control" id="txt_quote_bill_address"
														onkeyup="charcount('txt_quote_bill_address', 'span_txt_quote_bill_address','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.quote_bill_address%></textarea>
													<span id="span_txt_quote_bill_address"> (255
														Characters)</span>

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> City<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_quote_bill_city" type="text"
														class="form-control" id="txt_quote_bill_city"
														value="<%=mybean.quote_bill_city%>" size="30"
														maxlength="255" />

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Pin<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_quote_bill_pin" type="text"
														class="form-control" id="txt_quote_bill_pin"
														value="<%=mybean.quote_bill_pin%>" size="32"
														onkeyup="toInteger('txt_quote_bill_pin')" maxlength="10" />

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> State<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_quote_bill_state" type="text"
														class="form-control" id="txt_quote_bill_state"
														value="<%=mybean.quote_bill_state%>" size="32"
														maxlength="255" />

												</div>
											</div>

										</div>
									</div>
								</div>
								<!-- 			Billing Address Portlet End -->
								<!-- 			Shipping Address  Portlet Start -->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											Shipping Address (<a href="javascript:CopyBillingAddress();">Same
												as Billing</a>)
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-group">
												<label class="control-label col-md-4"> Address<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<textarea name="txt_quote_ship_address" rows="4"
														class="form-control" id="txt_quote_ship_address"
														onkeyup="charcount('txt_quote_ship_address', 'span_txt_quote_ship_address','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.quote_ship_address%></textarea>
													<span id="span_txt_quote_ship_address"> (255
														Characters)</span>

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> City<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_quote_ship_city" type="text"
														class="form-control" id="txt_quote_ship_city"
														value="<%=mybean.quote_ship_city%>" size="32"
														maxlength="255" />

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Pin<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_quote_ship_pin" type="text"
														class="form-control" id="txt_quote_ship_pin"
														value="<%=mybean.quote_ship_pin%>" size="32"
														onkeyup="toInteger('txt_quote_ship_pin')" maxlength="10" />

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> State<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_quote_ship_state" type="text"
														class="form-control" id="txt_quote_ship_state"
														value="<%=mybean.quote_ship_state%>" size="32"
														maxlength="255" />

												</div>
											</div>

										</div>
									</div>
								</div>
								<!-- 			Shipping Address  Portlet End -->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">&nbsp;</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-group">
										<label class="col-md-4 control-label">Description:</label>
										<div class="col-md-6">
											<div name="summernote" id="summernote_1"><%=mybean.quote_desc%></div>
											
										</div>
									</div>
											
											
											<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-group">
										<label class="col-md-4 control-label">Terms &amp;
													Conditions:</label>
										<div class="col-md-6">
											<div name="summernote" id="summernote_2"><%=mybean.quote_terms%></div>
											
										</div>
									</div>
											
											<%
												if (mybean.config_refno_enable.equals("1")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4"> Quote
													Reference No.<font color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input name="txt_quote_refno" type="text"
														class="form-control" id="txt_quote_refno"
														value="<%=mybean.quote_refno%>" size="32" maxlength="50" />

												</div>
											</div>
											<%
												}
											%>
											<div class="form-group">
												<label class="control-label col-md-4"> Executive<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<select name="dr_executive" id="dr_executive"
														class="form-control">
														<%=mybean.PopulateExecutive()%>
													</select>
													

												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4"> Active: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input id="chk_quote_active" type="checkbox"
														name="chk_quote_active"
														<%=mybean.PopulateCheck(mybean.quote_active)%> />

												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4"> Notes: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<textarea name="txt_quote_notes" cols="70" rows="4"
														class="form-control" id="txt_quote_notes"><%=mybean.quote_notes%></textarea>

												</div>
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4"> Entry By: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<%=mybean.unescapehtml(mybean.quote_entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.quote_entry_by)%>" />

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Entry Date: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<%=mybean.entry_date%>
													<input type="hidden" id="entry_date" name="entry_date"
														value="<%=mybean.entry_date%>" />

												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4"> Modified By:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<%=mybean.unescapehtml(mybean.quote_modified_by)%>
													<input type="hidden" id="modified_by" name="modified_by"
														value="<%=mybean.unescapehtml(mybean.quote_entry_by)%>" />

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Modified
													Date:<font color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<%=mybean.modified_date%>
													<input type="hidden" id="modified_date"
														name="modified_date" value="<%=mybean.modified_date%>" />

												</div>
											</div>
											<%
												}
											%>
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="addbutton" id="addbutton" type="button"
													onClick="return SubmitFormOnce(document.form1, this);"
													class="btn btn-success" value="Add Quote" /> <input
													type="hidden" name="add_button" value="yes">
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												<input type="hidden" name="update_button" value="yes">
												<input type="hidden" id="Update" name="Update" value="yes">
												<input name="updatebutton" id="updatebutton" type="submit"
													class="button" value="Update Quote"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit" class="button"
													id="delete_button" onClick="return confirmdelete(this)"
													value="Delete Quote" />
												<%
 	}
 %>
												<input type="hidden" id="emp_course_disc"
													name="emp_course_disc" value="<%//=mybean.emp_discount%>">
											</center>

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
	<!-- END CONTAINER -->




	<%@include file="../Library/admin-footer.jsp"%>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script> 
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/dynacheck.js"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
<script src="../assets/js/components-date-time-pickers.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap-datepicker.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap-timepicker.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
		
		
		<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	
	<script src="../assets/js/components-editors.min.js"
		type="text/javascript"></script>
		
</body>
</HTML>
