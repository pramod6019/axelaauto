<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Veh_Quote_Update_New"
	scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<script type="text/javascript" src="../Library/veh-quote-new.js?target=<%=Math.random()%>"></script>
<%@include file="../Library/css.jsp"%> 
<link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />
</HEAD>
<body onLoad ="GetConfigurationDetails()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%> Quote
						</h1>
						<div class="form-group"></div>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<input type="hidden" name="txt_quote_contact_id"
						id="txt_quote_contact_id" value="<%=mybean.quote_contact_id%>" />
					<div class="container-fluid">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
							<li><a href="veh-quote.jsp">Quotes</a> &gt;</li>
							<li><a href="veh-quote-list.jsp?all=recent">List Quotes</a>
								&gt;</li>
							<li><a href="veh-quote-update-new.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Quote</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<div class="caption" style="float: none">
								<center>
									<font color="#ff0000"><b><div id="errmsg"><%=mybean.msg%></div></b></font>
								</center>
							</div>
							<form name="form1" id="form1" method="post"
								class="form-horizontal">
								<!-- 			BODY START PORTLET -->
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.status%>&nbsp;
											Quote
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- 										START PORTLET BODY -->
											<div class="container-fluid">
												<center>
													Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.
												</center>

												<div class="form-group">
													<input type="hidden" id="itemdetails" name="itemdetails" value="<%=mybean.itemdetails%>">
													<input type="hidden" id="txt_enquiry_enquirytype_id" name="txt_enquiry_enquirytype_id" value="<%=mybean.enquiry_enquirytype_id%>">
													<input type="hidden" name="txt_session_id" id="txt_session_id" value="<%=mybean.session_id%>" />
													<input type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%=mybean.branch_id%>">
													<input type="hidden" id="emp_quote_priceupdate" name="emp_quote_priceupdate" value="<%=mybean.emp_quote_priceupdate%>">
													<input type="hidden" id="emp_quote_discountupdate" name="emp_quote_discountupdate" value="<%=mybean.emp_quote_discountupdate%>">
													<input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>">
													<input type="hidden" name="quote_contact" id="quote_contact" value="" />
													<input type="hidden" name="txt_rateclass_id	" id="txt_rateclass_id	" value="<%=mybean.rateclass_id%>">
													<input type="hidden" id="lead_id" name="lead_id" value="<%=mybean.lead_id%>">
													<input type="hidden" id="txt_quote_id" name="txt_quote_id" value="<%=mybean.quote_id%>">
													<input type="hidden" id="txt_enquiry_id" name="txt_enquiry_id" value="<%=mybean.quote_enquiry_id%>">
													<input type="hidden" id="txt_item_id" name="txt_item_id" value="<%=mybean.item_id%>">
													<input type="hidden" id="txt_model_id" name="txt_model_id" value="<%=mybean.model_id%>">
													<input type="hidden" id="txt_total_disc" name="txt_total_disc">
													<input type="hidden" id="txt_item_netprice" name="txt_item_netprice" value="<%=mybean.item_netprice%>">
													<input type="hidden" id="txt_item_netdisc" name="txt_item_netdisc" value="<%=mybean.item_netdisc%>">
													<input type="hidden" id="txt_quote_netamt" name="txt_quote_netamt" value="<%=mybean.quote_netamt%>" />
													<input type="hidden" id="txt_quote_discamt" name="txt_quote_discamt" value="<%=mybean.quote_discamt%>" />
													<input type="hidden" id="txt_quote_totaltax" name="txt_quote_totaltax" value="<%=mybean.quote_totaltax%>" />
													<input type="hidden" id="txt_quote_grandtotal" name="txt_quote_grandtotal" value="<%=mybean.quote_grandtotal%>" />
												</div>
											</div>

											<div class="form-element6">
												<label> Branch: </label>
												<%
													if (mybean.add.equals("yes")) {
												%>
													<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.branch_id%>"><%=mybean.branch_name%></a>
													<input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>">
													<input type="hidden" name="txt_branch_name" id="txt_branch_name" value="<%=mybean.branch_name%>">
												<% } else { %>
													<select id="dr_branch" name="dr_branch" class="form-control" onChange="GetConfigurationDetails();">
															<%=mybean.PopulateBranch(mybean.comp_id)%>
													</select>
												 <% } %>
											</div>
											<div class="form-element6">
												<label> Date<b><font color="#ff0000">*</font>:</b>
												</label>
												<%
													if (mybean.empEditperm.equals("1")) {
												%>
												<input name="txt_quote_date" type="text"
													class="form-control datepicker" id="txt_quote_date"
													value="<%=mybean.quotedate%>" size="12" maxlength="10" />
												<%
													} else {
												%>
												<%=mybean.quotedate%>
												<input name="txt_quote_date" type="hidden"
													class="form-control datepicker" id="txt_quote_date"
													value="<%=mybean.quotedate%>" />
												<%
													}
												%>
											</div>
											<div class="row"></div>
											<div class="form-element3">
												<label> Customer: </label> <b><span
													id="span_quote_customer_id" name="span_quote_customer_id"><%=mybean.link_customer_name%></span></b>
												<input name="span_acct_id" type="hidden" id="span_acct_id"
													value="<%=mybean.customer_id%>"> <input
													name="acct_id" type="hidden" id="acct_id"
													value="<%=mybean.quote_customer_id%>">
											</div>
											<div class="form-element3">
												<label> Contact: </label> <b><span
													id="span_quote_contact_id" name="span_quote_contact_id"><%=mybean.link_contact_name%></span></b>
												<input name="span_cont_id" type="hidden" id="span_cont_id"
													value="<%=mybean.contact_id%>"> <input
													name="cont_id" type="hidden" id="cont_id"
													value="<%=mybean.quote_contact_id%>">
														<div id="dialog-modal"></div>
											</div>
											<%
												if (!mybean.quote_enquiry_id.equals("0")) {
											%>
											<div class="form-element6">
												<label> Enquiry ID:</label> <b><a
													href="enquiry-list.jsp?enquiry_id=<%=mybean.quote_enquiry_id%>"><%=mybean.quote_enquiry_id%></a></b>
											</div>
											<%
												}
											%>

											<div class="form-element6">
												<label> Model:</label>
												<%
													if (mybean.enquiry_enquirytype_id.equals("1")) {
												%>
												<span id="modellink">
													<a href="../inventory/item-model.jsp?model_id=<%=mybean.model_id%>"> <%=mybean.model_name%></a>
												</span>
												<%
													} else if (mybean.enquiry_enquirytype_id.equals("2")) {
												%>
												<input type="hidden" id="txt_model_name"
													name="txt_model_name" value="<%=mybean.model_name%>">
													<div class="col-md-4 col-xs-12" id="emprows"
														style="top: 8px">
														<a href="../preowned/managepreownedmodel.jsp?preownedmodel_id=<%=mybean.model_id%>"><%=mybean.model_name%></a>
													</div>
												<%
 													}
 												%>
												
											</div>
											<div class="form-element6">
												<label> Variant<b><font color="#ff0000">*</font></b>:
												</label>
												<% if (mybean.enquiry_enquirytype_id.equals("1")) { %>
												<input type="hidden" name="dr_item_id" id="dr_item_id" value="<%=mybean.item_id%>"> 
												<input type="hidden" name="txt_item_name" id="txt_item_name" value="<%=mybean.item_name%>" />
												<input type="hidden" name="txt_model_id" id="txt_model_id" value="<%=mybean.model_id%>" />
												<input type="hidden" name="txt_model_name" id="txt_model_name" value="<%=mybean.model_name%>" />
												<span id="variantlink">
													<a href="../inventory/inventory-item-list.jsp?item_id=<%=mybean.item_id%>"> <%=mybean.item_name%></a>
												</span>
												<% } else if (mybean.enquiry_enquirytype_id.equals("2")) { %>
												<input type="hidden" name="dr_item_id" id="dr_item_id" value="<%=mybean.item_id%>"> 
												<input type="hidden" name="dr_model_id" id="dr_model_id" value="<%=mybean.model_id%>"> 
												<a href="../preowned/managepreownedvariant.jsp?variant_id=<%=mybean.item_id%>"><%=mybean.item_name%></a>
											<% } %>
											</div>
											
											<%
												if ((!mybean.vehstock_id.equals("0") && !mybean.vehstock_id .equals("")) || (!mybean.quote_vehstock_id.equals("0") && !mybean.quote_vehstock_id .equals(""))) {
											%>
												<div class="form-element6">
													<label>Commission No.:</label> <b>
													<input name="txt_vehstock_comm_no" type="text" id="txt_vehstock_comm_no" size="15" maxlength="10"
														class="form-control" value="<%=mybean.vehstock_comm_no%>"
														onkeyup="toInteger(this.id);"  onChange="GetConfigurationDetails();"/></b>
													<input type="hidden" name="txt_stock_comm_no" id="txt_stock_comm_no" value="<%=mybean.vehstock_comm_no%>" />
												</div>
												
												<div class="form-element6">
													<label>Stock ID:</label> <b>
													<input name="txt_quote_vehstock_id" type="text" id="txt_quote_vehstock_id" size="15" maxlength="10"
														class="form-control" value="<%=mybean.quote_vehstock_id%>"
														onkeyup="toInteger(this.id);"  onChange="GetConfigurationDetails();"/></b>
													<input type="hidden" name="txt_vehstock_id" id="txt_vehstock_id" value="<%=mybean.quote_vehstock_id%>" />
												</div>
											<%
												}
											%>

											<div class="form-element12">
												<div class='btn btn-group' style='float: right;'><a class="addLink" href="../sales/veh-quote-item-add.jsp?itemmaster_id=<%=mybean.item_id %>" data-target="#Hintclicktocall" data-toggle="modal"><i class='fa fa-plus-square' style='font-size:30px' id='sample_editable_1_new'></i></a></div>
											</div>
											
											<div class="form-element12">
												<center>
													<font color="#ff0000"><b><div id="errormsg"></div></b></font
												</center>
												
												<div id="config_details">
<%-- 													<%=mybean.GetConfigurationDetails(request, mybean.item_id, mybean.branch_id, mybean.vehstock_id, mybean.emp_quote_discountupdate, mybean.quote_date, "0", mybean.comp_id)%> --%>
												</div>
											</div>
										</div>
									</div>
								</div>
								

								<%
									if ((!mybean.vehstock_id.equals("0") && !mybean.vehstock_id .equals(""))
											|| (!mybean.quote_preownedstock_id.equals("0") && !mybean.quote_preownedstock_id .equals(""))) {
								%>
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"></div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<div class="form-element6">
												<label>Pre-Owned Stock ID:</label> <b><input
													name="txt_quote_preownedstock_id" type="text"
													id="txt_quote_preownedstock_id" size="15" maxlength="10"
													class="form-control"
													value="<%=mybean.quote_preownedstock_id%>"
													onkeyup="toInteger(this.id);" /></b>
											</div>
										</div>
									</div>
								</div>
								<%
									}
								%>

								<div class="portlet box ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Finance Proposal</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<div class="form-element4">
												<label>Finance Options:</label> <select
													id="dr_quote_fin_option1" name="dr_quote_fin_option1"
													class="form-control">
													<%=mybean.PopulateFinOption(mybean.comp_id, mybean.quote_fin_option1)%>
												</select>
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <select id="dr_quote_fin_option2"
													name="dr_quote_fin_option2" class="form-control">
													<%=mybean.PopulateFinOption(mybean.comp_id, mybean.quote_fin_option2)%>
												</select>
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <select id="dr_quote_fin_option3"
													name="dr_quote_fin_option3" class="form-control">
													<%=mybean.PopulateFinOption(mybean.comp_id, mybean.quote_fin_option3)%>
												</select>
											</div>
											<div class="form-element4">
												<label>Loan Amount:</label> <input
													name="txt_quote_fin_loan1" type="text" class="form-control"
													id="txt_quote_fin_loan1"
													value="<%=mybean.quote_fin_loan1%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input name="txt_quote_fin_loan2"
													type="text" class="form-control" id="txt_quote_fin_loan2"
													value="<%=mybean.quote_fin_loan2%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input name="txt_quote_fin_loan3"
													type="text" class="form-control" id="txt_quote_fin_loan3"
													value="<%=mybean.quote_fin_loan3%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>Tenure in Months:</label> <input
													name="txt_quote_fin_tenure1" type="text"
													class="form-control" id="txt_quote_fin_tenure1"
													value="<%=mybean.quote_fin_tenure1%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input name="txt_quote_fin_tenure2"
													type="text" class="form-control" id="txt_quote_fin_tenure2"
													value="<%=mybean.quote_fin_tenure2%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input name="txt_quote_fin_tenure3"
													type="text" class="form-control" id="txt_quote_fin_tenure3"
													value="<%=mybean.quote_fin_tenure3%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>

											<div class="form-element4">
												<label>No. of Advance E.M.I:</label> <input
													name="txt_quote_fin_adv_emi1" type="text"
													class="form-control" id="txt_quote_fin_adv_emi1"
													value="<%=mybean.quote_fin_adv_emi1%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input name="txt_quote_fin_adv_emi2"
													type="text" class="form-control"
													id="txt_quote_fin_adv_emi2"
													value="<%=mybean.quote_fin_adv_emi2%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input name="txt_quote_fin_adv_emi3"
													type="text" class="form-control"
													id="txt_quote_fin_adv_emi3"
													value="<%=mybean.quote_fin_adv_emi3%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>E.M.I./Rental:</label> <input
													name="txt_quote_fin_emi1" type="text" class="form-control"
													id="txt_quote_fin_emi1" value="<%=mybean.quote_fin_emi1%>"
													size="15" maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input name="txt_quote_fin_emi2"
													type="text" class="form-control" id="txt_quote_fin_emi2"
													value="<%=mybean.quote_fin_emi2%>" size="15" maxlength="10"
													onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input name="txt_quote_fin_emi3"
													type="text" class="form-control" id="txt_quote_fin_emi3"
													value="<%=mybean.quote_fin_emi3%>" size="15" maxlength="10"
													onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>Net Processing Fee:</label> <input
													name="txt_quote_fin_fee1" type="text" class="form-control"
													id="txt_quote_fin_fee1" value="<%=mybean.quote_fin_fee1%>"
													size="15" maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input name="txt_quote_fin_fee2"
													type="text" class="form-control" id="txt_quote_fin_fee2"
													value="<%=mybean.quote_fin_fee2%>" size="15" maxlength="10"
													onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input name="txt_quote_fin_fee3"
													type="text" class="form-control" id="txt_quote_fin_fee3"
													value="<%=mybean.quote_fin_fee3%>" size="15" maxlength="10"
													onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>Net Down Payment with / Without Optional
													Packages &amp; Accessories: </label> <input
													name="txt_quote_fin_downpayment1" type="text"
													class="form-control" id="txt_quote_fin_downpayment1"
													value="<%=mybean.quote_fin_downpayment1%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input
													name="txt_quote_fin_downpayment2" type="text"
													class="form-control" id="txt_quote_fin_downpayment2"
													value="<%=mybean.quote_fin_downpayment2%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>
											<div class="form-element4">
												<label>&nbsp;</label> <input
													name="txt_quote_fin_downpayment3" type="text"
													class="form-control" id="txt_quote_fin_downpayment3"
													value="<%=mybean.quote_fin_downpayment3%>" size="15"
													maxlength="10" onkeyup="toInteger(this.id)" />
											</div>


											<div class="container-fluid" style="display:<%=mybean.display%>">
												<div class="row"></div>
												<div class="form-element6">
													<label>Description: </label>
													<textarea name="txt_quote_desc"
														class="form-control summernote_1" id="txt_quote_desc"
														<%=mybean.readOnly%>><%=mybean.quote_desc%></textarea>
													<script type="text/javascript">
														CKEDITOR.replace( 'txt_quote_desc', { uiColor : hexc($( "a:link") .css( "color")), });
													</script>
												</div>
												<div class="form-element6">
													<label>Terms &amp; Conditions: </label>
													<textarea name="txt_quote_terms"
														class="form-control summernote_1" id="txt_quote_terms"
														<%=mybean.readOnly%>><%=mybean.quote_terms%></textarea>
													<script type="text/javascript">
														CKEDITOR .replace( 'txt_quote_terms', { uiColor : hexc($( "a:link") .css( "color")), });
													</script>
												</div>
											</div>
											<%
												if (mybean.config_refno_enable.equals("1")) {
											%>
											<div class="form-element6">
												<label>Quote Reference No.<font color="#ff0000">*</font>:
												</label> <input name="txt_quote_refno" type="text"
													class="form-control" id="txt_quote_refno"
													value="<%=mybean.quote_refno%>" size="32" maxlength="50" />
											</div>
											<%
												}
											%>

											<div class="form-element6">
												<label>Insurance Company:
												</label> <select name="dr_quote_inscomp_id" id="dr_quote_inscomp_id"
													class="form-control">
													<%=mybean.PopulateInsuranceCompany(mybean.comp_id)%>
												</select>
											</div>
											<div class="form-element6">
												<label>Sales Consultant<font color="#ff0000">*</font>: </label>
												<div id='sales_executive'>
<!-- 												<select name="dr_executive" id="dr_executive" class="form-control"> -->
													<%=mybean.PopulateExecutive(mybean.comp_id, mybean.branch_id)%>
<!-- 												</select> -->
												</div>
											</div>

											<div class="form-element6">
												<label>Notes:</label>
												<textarea name="txt_quote_notes" class="form-control"
													id="txt_quote_notes"><%=mybean.quote_notes%></textarea>
											</div>
											<div class="form-element6">
												<label>Active:</label> <input id="chk_quote_active"
													type="checkbox" name="chk_quote_active"
													<%=mybean.PopulateCheck(mybean.quote_active)%> />
											</div>
											<div class="row"></div>


											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label>Entry By: <%=mybean.unescapehtml(mybean.quote_entry_by)%></label>
												<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.unescapehtml(mybean.quote_entry_by)%>" />
											</div>
											<div class="form-element6">
												<label>Entry Date: <%=mybean.entry_date%></label> <input
													type="hidden" id="entry_date" name="entry_date"
													value="<%=mybean.entry_date%>" />
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified By: <%=mybean.unescapehtml(mybean.quote_modified_by)%></label>
												<input type="hidden" id="modified_by" name="modified_by"
													value="<%=mybean.unescapehtml(mybean.quote_entry_by)%>" />
											</div>
											<div class="form-element6">
												<label>Modified Date: <%=mybean.modified_date%></label> <input
													type="hidden" id="modified_date" name="modified_date"
													value="<%=mybean.modified_date%>" />
											</div>
											<%
												}
											%>
										</div>
										<center>
											<%
												if (mybean.status.equals("Add")) {
											%>
											<input name="addbutton" id="addbutton" type="button"
												class="btn btn-success" value="Add Quote" /> <input
												type="hidden" name="add_button" value="yes"> <%
											 	} else if (mybean.status.equals("Update")) {
											 %>
												<input type="hidden" name="update_button" value="yes">
													<input name="updatebutton" id="updatebutton" type="button"
													class="btn btn-success" value="Update Quote"/>
													<input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													onClick="return confirmdelete(this)" value="Delete Quote" />
													<%
														}
													%> <input type="hidden" id="emp_course_disc"
													name="emp_course_disc" value="<%//=mybean.emp_discount%>">
													
													 
													<p id="config_details_json" style='display:none;'><%=mybean.jsonconfiguredItemList%></p>
													
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		function getItemDetails(newAddedItem){
			var itemdetails = "";
		$("#Hintclicktocall").modal('toggle');
			newAddedItem = newAddedItem.substring(1, newAddedItem.length - 1);
			if($("#itemdetails").val() != ""){
				itemdetails = $("#itemdetails").val();
				itemdetails = itemdetails.substring(1, itemdetails.length - 1);
				$("#itemdetails").val("[" + newAddedItem + "]");
			} else {
				$("#itemdetails").val("[" + newAddedItem + "]");
			}
			console.log("itemdetails=="+$("#itemdetails").val());
			setTimeout('GetConfigurationDetails()', 200);
		}
		$(document).ready(function() {
			$("#addbutton").click(function() {
				checkForm();
			});
			$("#updatebutton").click(function() {
				checkForm();
			});
		});
		
		var msg = "";
		function checkForm() {
			msg = "";
			var financetype = document.getElementById("dr_quote_inscomp_id").value;
			var quotedate = $("#txt_quote_date").val();
// 			if (financetype == '0') {
// 				msg = "<br>Select Insurance Company!";
// 			}
			if (quotedate == '') {
				msg += "<br>Select Quote Date!";
			}

			if (msg != '') {
				$("#errmsg").html(msg);
				document.location.href = "#top";
			} else {
				document.getElementById('form1').submit();
			}
		}
	</script>
</body>
</HTML>
