<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Enquiry_Dash" scope="request" /> 
<%
	mybean.doPost(request, response);
%>

<jsp:useBean id="mybeanenqdashmethods" class="axela.sales.Enquiry_Dash_Methods" scope="request" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transi tional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>

<!-- tag CSS -->
<link href="../assets/css/bootstrap-tagsinput.css" rel="stylesheet"
	type="text/css" />
<!-- tag CSS -->

<style>
.pop {
	top: 280px;
	left: 740px;
}
.dropdown-header{
font-weight : bold;
font-size : 16px;
}
</style>
</head>

<body onLoad="CheckEvaluation();"
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
						<h1>
							Enquiry Dashboard &gt; Enquiry ID<b>: </b>
							<%=mybean.enquiry_id%></h1>
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="../sales/enquiry.jsp">Enquiry</a> &gt;</li>
						<li><a href="../sales/enquiry-list.jsp?all=yes">List Enquiry</a> &gt;</li>
						<li><a href="../sales/enquiry-dash.jsp?enquiry_id=<%=mybean.enquiry_id%>">Enquiry
								ID<b>: </b> <%=mybean.enquiry_id%></a></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">


						<!-- 			TAGS START -->
						<CENTER>
							<div class="tag_class" id="customer_tagclass">
								<div class="bs-docs-example" id="bs-docs-example">
									<input type="text" class="form-control" id="enquiry_tags" name="enquiry_tags" />
									<a href="#" id="popover" data-placement="bottom" class="btn btn-success btn-md">
									<span style="font-size: 20px; margin-top: 5px" class="fa fa-angle-down"></span></a>
									<%--<%if(mybean.comp_id.equals("1000") && mybean.AppRun().equals("1")){ %> --%>
									<a href="../portal/canned.jsp?canned=yes&sales=yes&enquiry_id=<%=mybean.enquiry_id%>"
										class="btn btn-success btn-lg" data-target="#Hintclicktocall"
										data-toggle="modal" style="margin-top: 1px;"><large>
										<span style="font-size: 20px; top: 4px"
											class="glyphicon glyphicon-envelope"></span></large>&nbsp; Messages</a>
									<%--<%} %> --%>
								</div>
							</div>
							<div class="hint" id="hint_enquiry_tags"></div>

							<div id="popover-head" class="hide">
								<center>Tag List</center>
							</div>
							<div id="popover-content" class="hide">
								<%=mybean.tagcheck.PopulateTagsPopover( mybean.enquiry_customer_id, mybean.comp_id)%>
							</div>
						</CENTER>

						<!-- 			TAGS END -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div>
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tabs-1" data-toggle="tab">Enquiry Details</a></li>
									<li><a href="#tabs-2" data-toggle="tab">Follow-up</a></li>
									<li><a href="#tabs-3" data-toggle="tab">CRM Follow-up</a></li>
									<li><a href="#tabs-12" data-toggle="tab">Pre-Owned Follow-up</a></li>
									<li><a href="#tabs-4" data-toggle="tab">Customer</a></li>
									<li><a href="#tabs-5" data-toggle="tab">Documents</a></li>
									<li><a href="#tabs-6" data-toggle="tab">Test Drives</a></li>
									<li><a href="#tabs-7" data-toggle="tab">Quotes</a></li>
									<li><a href="#tabs-8" data-toggle="tab">Sales Orders</a></li>
									<li onclick="LoadEnquiryDash('9')"><a href="#tabs-9" data-toggle="tab">Invoices</a></li>
									<li onclick="LoadEnquiryDash('10')"><a href="#tabs-10" data-toggle="tab">Receipts</a></li>
									<li><a href="#tabs-11" data-toggle="tab">History</a></li>
								</ul>


								<div class="tab-content">

									<div id="dialog-modal"></div>
									<div class="tab-pane active" id="tabs-1">
										<!--       Enquiry Details -->

										<form name="form1" id="form1" class="form-horizontal"
											method="post">
											<!-- 	Start	Enquiry details -->

											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Enquiry
														Details</div>
												</div>

												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->

														<br />
														<div class="row">
															<div class="form-element3">
																<label>Enquiry ID:</label>
																<!--<div class="  space-top col-md-6 col-sm-4 col-xs-4" -->
																<!--	 style="top: 9px"> -->
																<a
																	href="enquiry-list.jsp?enquiry_id=<%=mybean.enquiry_id%>"><b><%=mybean.enquiry_id%></b></a>
																<input class="form-control" name="enquiry_id"
																	type="hidden" id="enquiry_id"
																	value="<%=mybean.enquiry_id%>">
															</div>

															<div class="form-element3">
																<label>Branch:</label>
																<!--<div class="space-top col-md-6 col-sm-4 col-xs-4" -->
																<!--	 style="top: 9px"> -->
																<a
																	href="../portal/branch-summary.jsp?branch_id=<%=mybean.enquiry_branch_id%>"><%=mybean.branch_name%></a>
															</div>

															<div class="form-element3">
																<label> Date: </label> <input class="form-control"
																	name="txt_enquiry_date" type="hidden"
																	id="txt_enquiry_date" value="<%=mybean.date%>">
																<%=mybean.date%>
															</div>

															<div class="form-element3">
																<label>Type: </label>
																<%=mybean.enquirytype_name%>
															</div>
														</div>

														<div class="form-element6">
															<label>Closing Date<font color=red>*</font>: </label>
															<input name="txt_enquiry_exp_close_date" id="txt_enquiry_exp_close_date"
																class="form-control datepicker" type="text"
																value="<%=mybean.enquirydate%>" size="12" maxlength="10"
																onChange="SecurityCheck('txt_enquiry_exp_close_date',this,'hint_txt_enquiry_exp_close_date');" />
															<div class="hint" id="hint_txt_enquiry_exp_close_date"></div>

															<!--<input name="txt_enquiry_exp_close_date" type="text" class="textbox"  id ="txt_enquiry_exp_close_date"  -->
															<%--value = "<%=mybean.enquirydate%>" size="12" maxlength="10"  --%>
															<!--onChange="SecurityCheck('txt_enquiry_exp_close_date',this,'hint_txt_enquiry_exp_close_date');" > -->
														</div>
														
														<!-- <div class="form-element6"> -->
														<!-- <label > </label> -->
														<!-- <div style="top:9px"> -->
														<!-- <div class="hint" id="hint_txt_enquiry_exp_close_date"></div>	 -->

														<!-- </div> -->
														<!-- </div> -->


														<!-- <div class="form-element6"> -->
														<!-- <label >Closing Date<font color="#ff0000">*</font>:</label> -->
														<!-- <div class="col-md-6"> -->


														<!-- <input class="form-control" name="txt_enquiry_exp_close_date" id="txt_enquiry_exp_close_date" -->
														<%-- value= "<%=mybean.enquirydate%>" class="form-control datepicker" --%>
														<!--  type="text" -->
														<!-- onChange="SecurityCheck('txt_enquiry_exp_close_date',this,'hint_txt_enquiry_exp_close_date');" /> -->
														<!-- </div> -->
														<!-- </div> -->



														<!-- <div class="form-element6"> -->
														<!-- <!-- <label > Days left: </label> -->
														<!-- <div   style="top:9px"> -->
														<%-- <div class="hint" id="hint_txt_enquiry_exp_close_date"><%=mybean.days_diff%></div> --%>

														<!-- </div> -->
														<!-- </div> -->
														<div class="form-element6">
															<label> Title<font color=red>*</font>: </label>
															<input class="form-control" name="txt_enquiry_title" id="txt_enquiry_title" maxlength="255"
																value="<%=mybean.enquiry_title%>" size="52" type="text"
																onChange="SecurityCheck('txt_enquiry_title',this,'hint_txt_enquiry_title')" />
															<div class="hint" id="hint_txt_enquiry_title"></div>
														</div>
														<div class="row"></div>
														<div class="row">

															<div class="form-element6">

																<div class="form-element6 form-element-margin form-element">
																	<label> Team: </label>
																	<label> <%=mybean.team_name%> </label>
																</div>

																<div class="form-element6 form-element">
																	<label> Sales Consultant<font color="#ff0000">*</font>: </label>
																	<select name="dr_enquiry_emp_id" id="dr_enquiry_emp_id" class="form-control">
																		<!--		onChange="SecurityCheck('dr_enquiry_emp_id',this,'hint_dr_enquiry_emp_id');" -->
																		<%=mybean.PopulateExecutive(mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_emp_id"></div>
																</div>

																<div class="form-element12 form-element">
																	<label>Budget:&nbsp;</label>
																	<input name="txt_enquiry_value" type="text" class="form-control" id="txt_enquiry_value"
																		onKeyUp="toNumber('txt_enquiry_value','Value')"
																		onChange="SecurityCheck('txt_enquiry_value',this,'hint_txt_enquiry_value')"
																		value="<%=mybean.enquiry_value%>" size="10" maxlength="10" />
																	<div class="hint" id="hint_txt_enquiry_value"></div>
																</div>
															</div>


															<div class="form-element6">
																<label> Description:&nbsp; </label>
																<textarea name="txt_enquiry_desc" cols="50" rows="4"
																	class="form-control" id="txt_enquiry_desc"
																	onChange="SecurityCheck('txt_enquiry_desc',this,'hint_txt_enquiry_desc')"><%=mybean.enquiry_desc%></textarea>
																<div class="hint" id="hint_txt_enquiry_desc"></div>
															</div>
														</div>
														<%
															if (!mybean.ref_emp_name.equals("")) {
														%>
														<div class="form-element12 row">
															<div class="form-element6 form-element-margin">
																<label> Reference Consultant: &nbsp;</label> <span>
																	<%=mybean.ref_emp_name%>
																</span>
															</div>

														</div>
														<%
															}
														%>
														<div class="row">
															<div class="form-element6">
																<label>Model:</label> <select
																	<%if (mybean.enquiry_enquirytype_id.equals("2")) {%>
																	disabled="disabled" <%}%> name="dr_enquiry_model_id"
																	id="dr_enquiry_model_id" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_model_id',this,'hint_dr_enquiry_model_id');populateItem();">
																	<%=mybean.PopulateModel(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_model_id"></div>
															</div>

															<div class="form-element6">
																<label>Variant:</label> <span id="modelitem"> <select
																	<%if (mybean.enquiry_enquirytype_id.equals("2")) {%>
																	disabled="disabled" <%}%> name="dr_enquiry_item_id"
																	id="dr_enquiry_item_id" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_item_id',this,'hint_dr_enquiry_item_id');">
																		<%=mybean.PopulateItem(mybean.comp_id)%>
																</select>
																</span>
																<div class="hint" id="hint_dr_enquiry_item_id"></div>
															</div>
														</div>
														<div class="row">
															<div class="form-element6">
																<label>Additional Model:</label> <select
																	name="dr_enquiry_add_model_id"
																	id="dr_enquiry_add_model_id" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_add_model_id',this,'hint_dr_enquiry_add_model_id');">
																	<%=mybean.PopulateAdditionalModel(mybean.comp_id)%>
																</select> <span class="hint" id="hint_dr_enquiry_add_model_id"></span>
															</div>
															<div class="form-element6">
																<% if (mybean.branch_brand_id.equals("60")) { %>
																<label> Colour:&nbsp; </label>
																<% } else {%> 
																<label> Colour<font color="#ff0000">*</font>:&nbsp; </label>
																<% } %>
																<select name="dr_enquiry_option_id"
																	id="dr_enquiry_option_id" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_option_id',this,'hint_dr_enquiry_option_id');">
																	<%=mybean.PopulateOption(mybean.comp_id)%>
																</select> <span class="hint" id="hint_dr_enquiry_option_id"></span>
															</div>
														</div>

															<div class="form-element6">
																<% if (mybean.branch_brand_id.equals("60")) { %>
																<label> Age:&nbsp; </label>
																<% } else {%> 
																<label> Age<font color="#ff0000">*</font>:&nbsp; </label>
																<% } %>
																 <select name="dr_enquiry_age_id" id="dr_enquiry_age_id"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_age_id',this,'hint_dr_enquiry_age_id');">
																	<%=mybean.PopulateAge(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_age_id"></div>
															</div>

														<%
															if (mybean.branch_brand_id.equals("55")) {
														%>

														<div class="form-element6">
															<label>Category: </label> <select
																name="dr_enquiry_enquirycat_id"
																id="dr_enquiry_enquirycat_id" class="form-control"
																onChange="SecurityCheck('dr_enquiry_enquirycat_id',this,'hint_dr_enquiry_enquirycat_id');">
																<%=mybeanenqdashmethods.PopulateCategory( mybean.enquiry_enquirycat_id, mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_enquiry_enquirycat_id"></div>
														</div>
														<%
															}
														%>

														<%
															if (!mybean.branch_brand_id.equals("1")) {
														%>

														<div class="form-element6">
															<label>Corporate: </label> <select
																name="dr_enquiry_corporate_id"
																id="dr_enquiry_corporate_id" class="form-control"
																onChange="SecurityCheck('dr_enquiry_corporate_id',this,'hint_dr_enquiry_corporate_id');">
																<%=mybeanenqdashmethods.PopulateCorporate( mybean.branch_brand_id, mybean.enquiry_corporate_id, mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_enquiry_corporate_id"></div>
														</div>
														<div class="row"></div>
														<%
															}
														%>

														<%
															if (!mybean.branch_brand_id.equals("60")) {
														%>
														<div class="row">
															<div class="form-element6">
																<label>Type of Customer<font color="#ff0000">*</font>:&nbsp;
																</label> <select name="dr_enquiry_custtype_id"
																	id="dr_enquiry_custtype_id" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_custtype_id',this,'hint_dr_enquiry_custtype_id');">
																	<%=mybean.PopulateCustomerType(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_custtype_id"></div>
															</div>
														
															<div class="form-element6">
																<label> Occupation<font color="#ff0000">*</font>:&nbsp;
																</label> <select name="dr_enquiry_occ_id" id="dr_enquiry_occ_id"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_occ_id',this,'hint_dr_enquiry_occ_id');">
																	<%=mybean.PopulateOccupation(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_occ_id"></div>
															</div>
														</div>
															
														<%
															if (!mybean.branch_brand_id.equals("56") || !mybean.branch_brand_id.equals("60")) {
														%>
														<div class="row">
															<%-- <div class="form-element6">
																<label> ID:</label> <input name="txt_enquiry_custid"
																	type="text" class="form-control" id="txt_enquiry_custid"
																	value="<%=mybean.enquiry_custid%>" size="20"
																	maxlength="20"
																	onKeyUp="toInteger('txt_enquiry_custid','Exprice')"
																	onChange="SecurityCheck('txt_enquiry_custid',this,'hint_txt_enquiry_custid')" />
																<div class="hint" id="hint_txt_enquiry_custid"></div>
															</div> --%>
	
															<div class="form-element6">
																<label>Fuel Allowance:</label> <input
																	name="txt_enquiry_fuelallowance" type="text"
																	class="form-control" id="txt_enquiry_fuelallowance"
																	value="<%=mybean.enquiry_fuelallowance%>" size="10"
																	maxlength="10"
																	onKeyUp="toInteger('txt_enquiry_fuelallowance','Fuel')"
																	onChange="SecurityCheck('txt_enquiry_fuelallowance',this,'hint_txt_enquiry_fuelallowance')" />
																<div class="hint" id="hint_txt_enquiry_fuelallowance"></div>
															</div>
														</div>
														
														
														<%
															}
														%>
														<%
															}
														%>

														<!-- 														</div> -->

														<div class="form-element12">
															<center>
																<a class=" btn btn-success btn-outline sbold"
																	href="../sales/enquiry-transfer.jsp?enquiry_id=<%=mybean.enquiry_id%>"
																	data-target="#Hintclicktocall" data-toggle="modal">Transfer
																	Enquiry</a>
															</center>
														</div>

													</div>
												</div>
											</div>
											<!--  	End Enquiry details -->


											<!--  	Start Pre-Owned -->
											<%
												if (mybean.enquiry_enquirytype_id.equals("2")) {
											%>
											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Pre-Owned</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->

														<div class="row">
															<div class="form-element6">
																<label>Pre-Owned Model<font color="#ff0000">*</font>:
																</label> <select class="form-control select2"
																	id="preownedvariant1" name="preownedvariant1"
																	onchange="SecurityCheck('preownedvariant1',this,'hint_preownedvariant1')">
																	<%=mybean.variantcheck.PopulateVariant(mybean.enquiry_preownedvariant_id)%>
																</select>
																<div class="hint" id="hint_preownedvariant1"></div>
																<%--  value="<%=mybean.enquiry_preownedvariant_id%>"
																<input tabindex="-1" class="bigdrop select2-offscreen"
																	id="modelvariant" name="modelvariant"
																	style="width: 300px"
																	value="<%=mybean.enquiry_preownedvariant_id%>"
																	type="hidden"
																	onchange="SecurityCheck('modelvariant',this,'hint_dr_variant_id')" />
																<div class="hint" id="hint_dr_variant_id"></div> --%>
															</div>

															<div class="form-element6">
																<label>Fuel Type:</label> <select
																	name="dr_enquiry_fueltype_id"
																	id="dr_enquiry_fueltype_id" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_fueltype_id',this,'hint_dr_enquiry_fueltype_id');">
																	<%=mybean.PopulateFuelType(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_fueltype_id"></div>
															</div>

														</div>

														<div class="row">

															<div class="form-element6">
																<label>Pref. Reg.:</label> <select
																	name="dr_enquiry_prefreg_id" id="dr_enquiry_prefreg_id"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_prefreg_id',this,'hint_dr_enquiry_prefreg_id');">
																	<%=mybean.PopulatePrefReg(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_prefreg_id"></div>
															</div>

															<div class="form-element6">
																<label>Present Car:</label> <input
																	name="txt_enquiry_presentcar" type="text"
																	class="form-control" id="txt_enquiry_presentcar"
																	value="<%=mybean.enquiry_presentcar%>" size="32"
																	maxlength="255"
																	onchange="SecurityCheck('txt_enquiry_presentcar',this,'hint_txt_enquiry_presentcar')" />
																<div class="hint" id="hint_txt_enquiry_presentcar"></div>
															</div>

														</div>

														<div class="form-element6">
															<label>Finance:</label> <select name="dr_enquiry_finance"
																id="dr_enquiry_finance" class="form-control"
																onChange="SecurityCheck('dr_enquiry_finance',this,'hint_dr_enquiry_finance');">
																<%=mybean.PopulateFinance(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_enquiry_finance"></div>
														</div>
													</div>
												</div>

											</div>
											<%
												}
											%>

											<!-- End Pre-Owned -->
											<%
												if (mybean.branch_brand_id.equals("2")
														|| mybean.branch_brand_id.equals("10")) {
											%>
											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Understand
														Your Customer</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->

														<div class="row">
															<div class="form-element6">
																<div class="form-element12 form-element">
																	<label> Type of Buyer<font color="#ff0000">*</font>:
																	</label> <select name="dr_enquiry_buyertype_id"
																		id="dr_enquiry_buyertype_id" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_buyertype_id',this,'hint_dr_enquiry_buyertype_id');">
																		<%=mybeanenqdashmethods.PopulateBuyerType( mybean.comp_id, mybean.enquiry_buyertype_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_buyertype_id"></div>
																</div>

																<div class="form-element12 form-element">
																	<label> What will be your mode of purchase?<font
																		color="#ff0000">*</font>
																	</label> <select name="dr_enquiry_purchasemode_id"
																		id="dr_enquiry_purchasemode_id" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_purchasemode_id',this,'hint_dr_enquiry_purchasemode_id');">
																		<%=mybean.PopulatePurchaseMode(mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_purchasemode_id"></div>
																</div>
															</div>

															<div class="form-element6">
																<label> How many kilometers you drive in a
																	month?<font color="#ff0000">*</font>
																</label> <select name="dr_enquiry_monthkms_id"
																	id="dr_enquiry_monthkms_id" class="form-control"
																	size="5" maxlength="7" multiple="multiple"
																	"
																	onChange="SecurityCheck('dr_enquiry_monthkms_id',this,'hint_dr_enquiry_monthkms_id');">
																	<%=mybean.PopulateMonthKms(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_monthkms_id"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label> What is your approximate annual
																	household income (Rs)?<font color="#ff0000">*</font>
																</label> <select name="dr_enquiry_income_id"
																	id="dr_enquiry_income_id" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_income_id',this,'hint_dr_enquiry_income_id');">
																	<%=mybean.PopulateIncome(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_income_id"></div>
															</div>

															<!-- <div class="form-element6"> -->
															<!--<label >What is -->
															<!--	your approximate annual household income (Rs)?<font -->
															<!--	color="#ff0000">*</font> -->
															<!--</label> -->
															<!--	<select name="dr_enquiry_income_id" -->
															<!--		id="dr_enquiry_income_id" class="form-control" -->
															<!--		onChange="SecurityCheck('dr_enquiry_income_id',this,'hint_dr_enquiry_income_id');"> -->
															<%--		<%=mybean.PopulateIncome(mybean.comp_id)%> --%>
															<!--	</select> -->
															<!--	<div class="hint" id="hint_dr_enquiry_income_id"></div> -->
															<!-- </div> -->

															<div class="form-element6">
																<label> How many members are there in your
																	family?<font color="#ff0000">*</font>
																</label> <input name="txt_enquiry_familymember_count"
																	type="text" class="form-control"
																	id="txt_enquiry_familymember_count"
																	value="<%=mybean.enquiry_familymember_count%>" size="5"
																	maxlength="3"
																	onKeyUp="toInteger('txt_enquiry_familymember_count','members')"
																	onChange="SecurityCheck('txt_enquiry_familymember_count',this,'hint_txt_enquiry_familymember_count')" />
																<div class="hint"
																	id="hint_txt_enquiry_familymember_count"></div>
															</div>
														</div>

														<div class="row">

															<div class="form-element6">
																<label> What is top most priority expectations
																	from the car?<font color="#ff0000">*</font>
																</label> <select name="dr_enquiry_expectation_id"
																	id="dr_enquiry_expectation_id" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_expectation_id',this,'hint_dr_enquiry_expectation_id');">
																	<%=mybean.PopulateExpectation(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_expectation_id"></div>
															</div>

															<div class="form-element6">
																<label> Any other car you have in mind?<font
																	color="#ff0000">*</font>
																</label> <input name="txt_enquiry_othercar" type="text"
																	class="form-control" id="txt_enquiry_othercar"
																	value="<%=mybean.enquiry_othercar%>" size="32"
																	maxlength="255"
																	onChange="SecurityCheck('txt_enquiry_othercar',this,'hint_txt_enquiry_othercar')" />
																<div class="hint" id="hint_txt_enquiry_othercar"></div>
															</div>
															
															<% if (mybean.branch_brand_id.equals("2")) { %>
															<div class="row"></div>
															<div class="form-element6">
																<label>Corporate</label>
																<select id="dr_enquiry_corporate" name="dr_enquiry_corporate" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_corporate', this, 'hint_dr_enquiry_corporate')" >
																	<%= mybeanenqdashmethods.PopulateEnquiryCorporate(mybean.enquiry_corporate, mybean.comp_id) %>
																</select>
																<div class="hint" id="hint_dr_enquiry_corporate"></div>
															</div>
															
															<% } %>

														</div>

													</div>
												</div>
											</div>

											<%
												}
											%>
											<!--                       Start Hyundai Fields -->
											<%
												if (mybean.branch_brand_id.equals("6")) {
											%>

											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Need Assessment (Hyundai)</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->

														<div class="row">
															<div class="form-element6">
																<label>Please choose one option:</label> <select
																	name="dr_enquiry_hyundai_chooseoneoption"
																	id="dr_enquiry_hyundai_chooseoneoption"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_hyundai_chooseoneoption',this,'hint_dr_enquiry_hyundai_chooseoneoption')">
																	<%=mybeanenqdashmethods.PopulateHyundaiChooseOneOption( mybean.enquiry_hyundai_chooseoneoption, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_hyundai_chooseoneoption"></div>
															</div>

															<div class="form-element6">
																<label>How many kilometers you drive in a
																	month?:</label> <select name="dr_enquiry_hyundai_kmsinamonth"
																	id="dr_enquiry_hyundai_kmsinamonth"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_hyundai_kmsinamonth',this,'hint_dr_enquiry_hyundai_kmsinamonth')">
																	<%=mybeanenqdashmethods.PopulateHyundaiKmsInAMonth( mybean.enquiry_hyundai_kmsinamonth, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_hyundai_kmsinamonth"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label>How many members are there in your
																	family?:</label> <select
																	name="dr_enquiry_hyundai_membersinthefamily"
																	id="dr_enquiry_hyundai_membersinthefamily"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_hyundai_membersinthefamily',this,'hint_dr_enquiry_hyundai_membersinthefamily')">
																	<%=mybeanenqdashmethods .PopulateHyundaiMembersInTheFamily( mybean.enquiry_hyundai_membersinthefamily, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_hyundai_membersinthefamily"></div>
															</div>

															<div class="form-element6">
																<label>What is your top most priority
																	expectation from your car?:</label> <select
																	name="dr_enquiry_hyundai_topexpectation"
																	id="dr_enquiry_hyundai_topexpectation"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_hyundai_topexpectation',this,'hint_dr_enquiry_hyundai_topexpectation')">
																	<%=mybeanenqdashmethods.PopulateHyundaiTopExpectation( mybean.enquiry_hyundai_topexpectation, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_hyundai_topexpectation"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label>By when are you expecting to finalize
																	your new car?:</label> <select
																	name="dr_enquiry_hyundai_finalizenewcar"
																	id="dr_enquiry_hyundai_finalizenewcar"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_hyundai_finalizenewcar',this,'hint_dr_enquiry_hyundai_finalizenewcar')">
																	<%=mybeanenqdashmethods.PopulateHyundaiFinalizeNewCar( mybean.enquiry_hyundai_finalizenewcar, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_hyundai_finalizenewcar"></div>
															</div>

															<div class="form-element6">
																<label>What will be your mode of purchase?:</label> <select
																	name="dr_enquiry_hyundai_modeofpurchase"
																	id="dr_enquiry_hyundai_modeofpurchase"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_hyundai_modeofpurchase',this,'hint_dr_enquiry_hyundai_modeofpurchase')">
																	<%=mybeanenqdashmethods.PopulateHyundaiModeOfPurchase( mybean.enquiry_hyundai_modeofpurchase, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_hyundai_modeofpurchase"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label>What is your appropriate annual household
																	income (INR)?:</label> <select
																	name="dr_enquiry_hyundai_annualincome"
																	id="dr_enquiry_hyundai_annualincome"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_hyundai_annualincome',this,'hint_dr_enquiry_hyundai_annualincome')">
																	<%=mybeanenqdashmethods.PopulateHyundaiAnnualIncome( mybean.enquiry_hyundai_annualincome, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_hyundai_annualincome"></div>
															</div>

															<div class="form-element6">
																<label>Which other cars are you considering?:</label> <input
																	name="txt_enquiry_hyundai_othercars" type="text"
																	class="form-control" id="txt_enquiry_hyundai_othercars"
																	value="<%=mybean.enquiry_hyundai_othercars%>" size="32"
																	maxlength="255"
																	onChange="SecurityCheck('txt_enquiry_hyundai_othercars',this,'hint_txt_enquiry_hyundai_othercars')" />
																<div class="hint"
																	id="hint_txt_enquiry_hyundai_othercars"></div>
															</div>
														</div>

														<div class="row">

															<div class="form-element6">
																<label>Current Car(s):</label> <input
																	name="txt_enquiry_hyundai_currentcars" type="text"
																	class="form-control"
																	id="txt_enquiry_hyundai_currentcars"
																	value="<%=mybean.enquiry_hyundai_currentcars%>"
																	size="32" maxlength="255"
																	onChange="SecurityCheck('txt_enquiry_hyundai_currentcars',this,'hint_txt_enquiry_hyundai_currentcars')" />
																<div class="hint"
																	id="hint_txt_enquiry_hyundai_currentcars"></div>
															</div>

															<div class="form-element6">
																<label>Date Of Birth:</label> <input
																	name="txt_enquiry_hyundai_dob" type="text"
																	class="form-control datepicker" size="12"
																	maxlength="10" id="txt_enquiry_hyundai_dob"
																	onChange="SecurityCheck('txt_enquiry_hyundai_dob',this,'hint_txt_enquiry_hyundai_dob')"
																	value="<%=mybean.enquiry_hyundai_dob%>" />
																<div class="hint" id="hint_txt_enquiry_hyundai_dob"></div>
															</div>
														</div>

													</div>
												</div>
											</div>
											<%
												}
											%>
											<!--                       End Hyundai Fields -->

											<!--                       Start Ford Fields -->
											<%
												if (mybean.branch_brand_id.equals("7")) {
											%>
											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Need
														Assessment of Customer (Ford)</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->

														<div class="row">
															<div class="form-element6">
																<label>Type of Customer<font color="#ff0000">*</font>:
																</label> <select name="dr_enquiry_ford_custtype_id"
																	id="dr_enquiry_ford_custtype_id" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_ford_custtype_id',this,'hint_dr_enquiry_ford_custtype_id')">
																	<%=mybeanenqdashmethods.PopulateFordCustomerType( mybean.enquiry_ford_customertype, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_ford_custtype_id"></div>
															</div>

															<div class="form-element6">
																<label>Intention to purchase:</label> <select
																	name="dr_enquiry_ford_intentionpurchase"
																	id="dr_enquiry_ford_intentionpurchase"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_ford_intentionpurchase',this,'hint_dr_enquiry_ford_intentionpurchase')">
																	<%=mybeanenqdashmethods.PopulateFordIntentionPurchase( mybean.enquiry_ford_intentionpurchase, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_ford_intentionpurchase"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label>No. of Kms Driven Every Day?:</label> <input
																	name="txt_enquiry_ford_kmsdriven" type="text"
																	id="txt_enquiry_ford_kmsdriven" class="form-control"
																	value="<%=mybean.enquiry_ford_kmsdriven%>" size="32"
																	maxlength="4"
																	onKeyUp="toInteger('txt_enquiry_ford_kmsdriven','kmsdrive')"
																	onChange="SecurityCheck('txt_enquiry_ford_kmsdriven',this,'hint_txt_enquiry_ford_kmsdriven')" />
																<div class="hint" id="hint_txt_enquiry_ford_kmsdriven"></div>
															</div>

															<div class="form-element6">
																<label>New Vehicle for Self or Someone else?:</label> <select
																	name="dr_enquiry_ford_newvehfor"
																	id="dr_enquiry_ford_newvehfor" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_ford_newvehfor',this,'hint_dr_enquiry_ford_newvehfor')">
																	<%=mybeanenqdashmethods.PopulateFordNewVehFor( mybean.enquiry_ford_newvehfor, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_ford_newvehfor"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label>Amount of investment in new car?:</label> <input
																	name="txt_enquiry_ford_investment" type="text"
																	class="form-control" id="txt_enquiry_ford_investment"
																	value="<%=mybean.enquiry_ford_investment%>" size="32"
																	maxlength="9"
																	onKeyUp="toInteger('txt_enquiry_ford_investment','investment')"
																	onChange="SecurityCheck('txt_enquiry_ford_investment',this,'hint_txt_enquiry_ford_investment')" />
																<div class="hint" id="hint_txt_enquiry_ford_investment"></div>
															</div>

															<div class="form-element6">
																<label>Any specific colour choice?:</label> <select
																	name="dr_enquiry_ford_colourofchoice"
																	id="dr_enquiry_ford_colourofchoice"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_ford_colourofchoice',this,'hint_txt_enquiry_ford_colourofchoice')">
																	<%=mybeanenqdashmethods.PopulateFordColourOfChoice( mybean.enquiry_ford_colourofchoice, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_txt_enquiry_ford_colourofchoice"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label>Cash / Finance?:</label> <select
																	name="dr_enquiry_ford_cashorfinance"
																	id="dr_enquiry_ford_cashorfinance" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_ford_cashorfinance',this,'hint_dr_enquiry_ford_cashorfinance')">
																	<%=mybeanenqdashmethods.PopulateFordCashOrFinance( mybean.enquiry_ford_cashorfinance, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_ford_cashorfinance"></div>
															</div>

															<div class="form-element6">
																<label>Which Car you Driving now?:</label> <input
																	name="txt_enquiry_ford_currentcar" type="text"
																	class="form-control" id="txt_enquiry_ford_currentcar"
																	value="<%=mybean.enquiry_ford_currentcar%>" size="32"
																	maxlength="255"
																	onChange="SecurityCheck('txt_enquiry_ford_currentcar',this,'hint_txt_enquiry_ford_currentcar')" />
																<div class="hint" id="hint_txt_enquiry_ford_currentcar"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label>Do you want to Exchange your old car?:</label> <select
																	name="dr_enquiry_ford_exchangeoldcar"
																	id="dr_enquiry_ford_exchangeoldcar"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_ford_exchangeoldcar',this,'hint_dr_enquiry_ford_exchangeoldcar')">
																	<%=mybeanenqdashmethods.PopulateFordExchangeOldCar( mybean.enquiry_ford_exchangeoldcar, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_ford_exchangeoldcar"></div>
															</div>

															<div class="form-element6">
																<label>Which Other cars you considering?:</label> <input
																	name="txt_enquiry_ford_othercarconcideration"
																	class="form-control" size="32" maxlength="255"
																	id="txt_enquiry_ford_othercarconcideration" type="text"
																	value="<%=mybean.enquiry_ford_othercarconcideration%>"
																	onChange="SecurityCheck('txt_enquiry_ford_othercarconcideration',this,'hint_txt_enquiry_ford_othercarconcideration')" />
																<div class="hint"
																	id="hint_txt_enquiry_ford_othercarconcideration"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label>Vista Contract Number: </label> <input
																	name="txt_enquiry_ford_vistacontractnumber" type="text"
																	class="form-control" size="32" maxlength="20"
																	id="txt_enquiry_ford_vistacontractnumber"
																	value="<%=mybean.enquiry_ford_vistacontractnumber%>"
																	onChange="SecurityCheck('txt_enquiry_ford_vistacontractnumber',this,'hint_txt_enquiry_ford_vistacontractnumber')" />
																<div class="hint"
																	id="hint_txt_enquiry_ford_vistacontractnumber"></div>
															</div>

															<div class="form-element6">
																<label>NSC Order Number: </label> <input
																	name="txt_enquiry_ford_nscordernumber" type="text"
																	class="form-control"
																	id="txt_enquiry_ford_nscordernumber" size="32"
																	maxlength="20"
																	value="<%=mybean.enquiry_ford_nscordernumber%>"
																	onChange="SecurityCheck('txt_enquiry_ford_nscordernumber',this,'hint_txt_enquiry_ford_nscordernumber')" />
																<div class="hint"
																	id="hint_txt_enquiry_ford_nscordernumber"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label>QCS ID: </label> <input
																	name="txt_enquiry_ford_qcsid" type="text"
																	class="form-control" id="txt_enquiry_ford_qcsid"
																	value="<%=mybean.enquiry_ford_qcsid%>" size="32"
																	maxlength="20"
																	onChange="SecurityCheck('txt_enquiry_ford_qcsid',this,'hint_txt_enquiry_ford_qcsid')" />
																<div class="hint" id="hint_txt_enquiry_ford_qcsid"></div>
															</div>

															<div class="form-element6">
																<label>QPD ID: </label> <input
																	name="txt_enquiry_ford_qpdid" type="text"
																	class="form-control" id="txt_enquiry_ford_qpdid"
																	value="<%=mybean.enquiry_ford_qpdid%>" size="32"
																	maxlength="20"
																	onChange="SecurityCheck('txt_enquiry_ford_qpdid',this,'hint_txt_enquiry_ford_qpdid')" />
																<div class="hint" id="hint_txt_enquiry_ford_qpdid"></div>
															</div>
														</div>

													</div>
												</div>
											</div>

											<%
												}
											%>
											<!-- End Ford Fields -->

											<!--       Start Honda Fields -->
											<%
												if (mybean.branch_brand_id.equals("9")) {
											%>

											<%
												}
											%>
											<!--  End Honda Fields -->


											<!----Start Maruti and Nexa Fields -->
											<%
												if (mybean.branch_brand_id.equals("10")) {
											%>
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Customer
														Profile (Maruti-Nexa)</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->

														<div class="row">
															<div class="form-element6">
																<label>Gender: </label> <select
																	name="dr_enquiry_nexa_gender"
																	id="dr_enquiry_nexa_gender" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_nexa_gender',this,'hint_dr_enquiry_nexa_gender')">
																	<%=mybeanenqdashmethods.PopulateEnquiryNexaGender( mybean.enquiry_nexa_gender, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_nexa_gender"></div>
															</div>

															<div class="form-element6">
																<label>Beverage Choice:</label> <select
																	name="dr_enquiry_nexa_beveragechoice"
																	id="dr_enquiry_nexa_beveragechoice"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_nexa_beveragechoice',this,'hint_dr_enquiry_nexa_beveragechoice')">
																	<%=mybeanenqdashmethods .PopulateEnquiryNexaBeveragechoice( mybean.enquiry_nexa_beveragechoice, mybean.comp_id)%>
																</select>
																<div class="hint"
																	id="hint_dr_enquiry_nexa_beveragechoice"></div>
															</div>
														</div>


														<div class="row">
															<div class="form-element6">
																<label>Interested in Autocard:</label> <select
																	name="dr_enquiry_nexa_autocard"
																	id="dr_enquiry_nexa_autocard" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_nexa_autocard',this,'hint_dr_enquiry_nexa_autocard')">
																	<%=mybeanenqdashmethods.PopulateEnquiryNexaAutocard( mybean.enquiry_nexa_autocard, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_nexa_autocard"></div>
															</div>

															<div class="form-element6">
																<label>Fuel Type:</label> <select
																	name="dr_enquiry_nexa_fueltype"
																	id="dr_enquiry_nexa_fueltype" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_nexa_fueltype',this,'hint_dr_enquiry_nexa_fueltype')">
																	<%=mybeanenqdashmethods.PopulateEnquiryNexaFueltype( mybean.enquiry_nexa_fueltype, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_nexa_fueltype"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6">
																<label>Specific requirement from the car:</label> <select
																	name="dr_enquiry_nexa_specreq"
																	id="dr_enquiry_nexa_specreq" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_nexa_specreq',this,'hint_dr_enquiry_nexa_specreq')">
																	<%=mybeanenqdashmethods.PopulateEnquiryNexaSpecreq( mybean.enquiry_nexa_specreq, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_nexa_specreq"></div>
															</div>

															<div class="form-element6">
																<label>Test Drive Required:</label> <select
																	name="dr_enquiry_nexa_testdrivereq"
																	id="dr_enquiry_nexa_testdrivereq" class="form-control"
																	onChange="TestdriveReq('dr_enquiry_nexa_testdrivereq',this,'hint_dr_enquiry_nexa_testdrivereq')">
																	<%=mybeanenqdashmethods.PopulateEnquiryNexaTestdrivereq( mybean.enquiry_nexa_testdrivereq, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_nexa_testdrivereq"></div>
															</div>
														</div>

														<div class="form-element6">
															<div id="reason">
																<label>Reason:</label>
															</div>
															<textarea name="txt_enquiry_nexa_testdrivereqreason"
																class="form-control"
																id="txt_enquiry_nexa_testdrivereqreason" cols="45"
																rows="3"
																onChange="SecurityCheck('txt_enquiry_nexa_testdrivereqreason',this,'hint_txt_enquiry_nexa_testdrivereqreason')"> <%=mybean.enquiry_nexa_testdrivereqreason%></textarea>
															<div class="hint"
																id="hint_txt_enquiry_nexa_testdrivereqreason"></div>
															<!-- 														</div> -->
															<!-- 														</div> -->
														</div>
													</div>
													<!-- 													</div> -->
												</div>
											</div>
											<%
												}
											%>

											<%
												if (mybean.branch_brand_id.equals("1")
														|| mybean.branch_brand_id.equals("2")
														|| mybean.branch_brand_id.equals("10")) {
											%>

											<%
												}
											%>
											<!----            End Maruti and Nexa Fields -->

											<!--                       Start Volvo Fields -->
											<%
												if (mybean.branch_brand_id.equals("51")) {
											%>

											<%
												}
											%>
											<!-- End Volvo Fields -->

											<!-- 			Start MB details -->
											<%
												if (mybean.branch_brand_id.equals("55")) {
											%>

											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Customer
														Profile (Mercedes Benz)</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">

														<div class="row">

															<div class="form-element6">
																<label>Occupation:</label> <select
																	name="dr_enquiry_mb_occupation"
																	id="dr_enquiry_mb_occupation" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_mb_occupation',this,'hint_dr_enquiry_mb_occupation');">
																	<%=mybeanenqdashmethods.PopulateMBOccupation( mybean.enquiry_mb_occupation, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>
															</div>

															<div class="form-element6 ">
																<label>Car Usage Conditions:</label> <select
																	name="dr_enquiry_mb_carusage"
																	id="dr_enquiry_mb_carusage" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_mb_carusage',this,'hint_dr_enquiry_mb_carusage');">
																	<%=mybeanenqdashmethods.PopulateMBCarUsageConditions( mybean.enquiry_mb_carusage, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_mb_carusage"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6 ">
																<label>Type:</label> <select name="dr_enquiry_mb_type"
																	id="dr_enquiry_mb_type" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_mb_type',this,'hint_dr_enquiry_mb_type');">
																	<%=mybeanenqdashmethods.PopulateMBType( mybean.enquiry_mb_type, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_mb_type"></div>
															</div>

															<div class="form-element6 ">
																<label>Driving Pattern:</label> <select
																	name="dr_enquiry_mb_drivingpattern"
																	id="dr_enquiry_mb_drivingpattern" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_mb_drivingpattern',this,'hint_dr_enquiry_mb_drivingpattern');">
																	<%=mybeanenqdashmethods.PopulateMBDrivingPattern( mybean.enquiry_mb_drivingpattern, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_mb_drivingpattern"></div>
															</div>
														</div>

														<div class="row">
															<div class="form-element6 ">
																<label>Income:</label> <select
																	name="dr_enquiry_mb_income" id="dr_enquiry_mb_income"
																	class="form-control"
																	onChange="SecurityCheck('dr_enquiry_mb_income',this,'hint_dr_enquiry_mb_income');">
																	<%=mybeanenqdashmethods.PopulateMBIncome( mybean.enquiry_mb_income, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_mb_income"></div>
															</div>

															<div class="form-element6 ">
																<label>Avg. Driving (kms/month):</label> <select
																	name="dr_enquiry_mb_avgdriving"
																	id="dr_enquiry_mb_avgdriving" class="form-control"
																	onChange="SecurityCheck('dr_enquiry_mb_avgdriving',this,'hint_dr_enquiry_mb_avgdriving');">
																	<%=mybeanenqdashmethods.PopulateMBAverageDriving( mybean.enquiry_mb_avgdriving, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_enquiry_mb_avgdriving"></div>
															</div>
														</div>


														<div class="form-element6 ">
															<label>Presently Owned:</label> <span> <%=mybeanenqdashmethods.PopulateMBCurrentCars(mybean.enquiry_mb_currentcars)%>
																<div class="hint" id="hint_dr_enquiry_mb_currentcars"></div>
															</span>
														</div>
													</div>
												</div>
											</div>

											<%
												}
											%>
											<!-- 				End MB details -->

											<!-- Start Porsche Details -->
												<%
													if (mybean.branch_brand_id.equals("56")) {
												%>
	
												<div class="portlet box">
													<div class="portlet-title" style="text-align: center">
														<div class="caption" style="float: none">Porsche Customer Need Assessement</div>
													</div>
													<div class="portlet-body portlet-empty container-fluid">
														<div class="tab-pane" id="">
															
															<div class="row">
																<div class="form-element6 ">
																	<label>Gender<font color="#ff0000">*</font>:</label>
																	<select name="dr_enquiry_porsche_gender" id="dr_enquiry_porsche_gender" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_porsche_gender', this, 'hint_dr_enquiry_porsche_gender');">
																		<%=mybeanenqdashmethods.PopulateEnquiryPorscheGender( mybean.enquiry_porsche_gender, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_porsche_gender"></div>
																</div>
	
																<div class="form-element6 ">
																	<label>Nationality:</label>
																	<select name="dr_enquiry_porsche_nationality" id="dr_enquiry_porsche_nationality" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_porsche_nationality', this, 'hint_dr_enquiry_porsche_nationality');">
																		<%=mybeanenqdashmethods.PopulateEnquiryPorscheNationality( mybean.enquiry_porsche_nationality, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_porsche_nationality"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6 form-element-margin">
																	<label>Language<font color="#ff0000">*</font>:</label>
																		<%=mybeanenqdashmethods.PopulateEnquiryPorscheLanguage( mybean.enquiry_porsche_language, mybean.comp_id)%>
																	<div class="hint" id="hint_chk_enquiry_porsche_language"></div>
																</div>
	
																<div class="form-element6 ">
																	<label>Religion<font color="#ff0000">*</font>:</label>
																	<select name="dr_enquiry_porsche_religion" id="dr_enquiry_porsche_religion" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_porsche_religion', this, 'hint_dr_enquiry_porsche_religion');">
																		<%=mybeanenqdashmethods.PopulateEnquiryPorscheReligion( mybean.enquiry_porsche_religion, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_porsche_religion"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6 ">
																	<label>Preferred Communication<font color="#ff0000">*</font>:</label>
																	<%=mybeanenqdashmethods.PopulateEnquiryPorschePreferredCommunication( mybean.enquiry_porsche_preferredcomm, mybean.comp_id)%>
																	<div class="hint" id="hint_chk_enquiry_porsche_preferredcomm"></div>
																</div>
	
																<div class="form-element6 ">
																	<label>Social Media Preference<font color="#ff0000">*</font>:</label>
																	<%=mybeanenqdashmethods.PopulateEnquiryPorscheSocialMediaPref( mybean.enquiry_porsche_socialmediapref, mybean.comp_id)%>
																	<div class="hint" id="hint_chk_enquiry_porsche_socialmediapref"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6 ">
																	<label>Marital Status<font color="#ff0000">*</font>:</label>
																	<select name="dr_enquiry_porsche_maritalstatus" id="dr_enquiry_porsche_maritalstatus" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_porsche_maritalstatus', this, 'hint_dr_enquiry_porsche_maritalstatus');">
																		<%=mybeanenqdashmethods.PopulateEnquiryPorscheMaritalStatus( mybean.enquiry_porsche_maritalstatus, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_porsche_maritalstatus"></div>
																</div>
	
																<div class="form-element6 ">
																	<label>Spouse Name:</label>
																	<input type="text" name="txt_enquiry_porsche_spousename" id="txt_enquiry_porsche_spousename" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_porsche_spousename', this, 'hint_txt_enquiry_porsche_spousename');"
																		value= '<%=mybean.enquiry_porsche_spousename%>'/>
																	<div class="hint" id="hint_txt_enquiry_porsche_spousename"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6 form-element">
																	<div class="form-element12 ">
																		<label>Does Spouse Drive:</label>
																		<select name="dr_enquiry_porsche_spousedrive" id="dr_enquiry_porsche_spousedrive" class="form-control"
																			onChange="SecurityCheck('dr_enquiry_porsche_spousedrive', this, 'hint_dr_enquiry_porsche_spousedrive');">
																			<%=mybeanenqdashmethods.PopulateSpouseDriveStatus( mybean.enquiry_porsche_spousedrive, mybean.comp_id)%>
																		</select>
																		<div class="hint" id="hint_dr_enquiry_porsche_spousedrive"></div>
																	</div>
																	
																	<div class="form-element12 ">
																		<label>Club Membership:</label>
																		<input type='text' name="txt_enquiry_porsche_clubmembership" id="txt_enquiry_porsche_clubmembership" class="form-control"
																			onChange="SecurityCheck('txt_enquiry_porsche_clubmembership', this, 'hint_txt_enquiry_porsche_clubmembership');"
																			value='<%=mybean.enquiry_porsche_clubmembership%>' />
																		<div class="hint" id="hint_txt_enquiry_porsche_clubmembership"></div>
																	</div>
																</div>
	
																<div class="form-element6 ">
																	<label>Interests<font color="#ff0000">*</font>:</label>
																	<div>
																		<%=mybeanenqdashmethods.PopulateEnquiryPorscheInsterest(mybean.enquiry_porsche_interest, mybean.comp_id) %>
																	</div>
																	<div class="hint" id="hint_chk_enquiry_porsche_interest"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6 ">
																	<label>Interested In Financing<font color="#ff0000">*</font>:</label>
																	<select name="dr_enquiry_porsche_financeoption" id="dr_enquiry_porsche_financeoption" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_porsche_financeoption', this, 'hint_dr_enquiry_porsche_financeoption');">
																		<%=mybeanenqdashmethods.PopulateEnquiryPorscheFinance( mybean.enquiry_porsche_financeoption, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_porsche_financeoption"></div>
																</div>
																
																<div class="form-element6 ">
																	<label>Interested In Insurance<font color="#ff0000">*</font>:</label>
																	<select name="dr_enquiry_porsche_insuranceoption" id="dr_enquiry_porsche_insuranceoption" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_porsche_insuranceoption', this, 'hint_dr_enquiry_porsche_insuranceoption');">
																		<%=mybeanenqdashmethods.PopulateEnquiryPorscheInsurance( mybean.enquiry_porsche_insuranceoption, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_porsche_insuranceoption"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6 ">
																	<label>Number Of Vehicle In House<font color="#ff0000">*</font>:</label>
																	<input type='text' name="txt_enquiry_porsche_vehicleinhouse" id="txt_enquiry_porsche_vehicleinhouse" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_porsche_vehicleinhouse', this, 'hint_txt_enquiry_porsche_vehicleinhouse');"
																		onKeyUp="toInteger('txt_enquiry_porsche_vehicleinhouse','Exprice')"
																		value='<%=mybean.enquiry_porsche_vehicleinhouse%>' />
																	<div class="hint" id="hint_txt_enquiry_porsche_vehicleinhouse"></div>
																</div>
																
																<div class="form-element6">
																	<label>Current Cars<font color="#ff0000">*</font>:</label>
																	<select class="form-control select2" id="porscheothervehicle" name="porscheothervehicle" multiple
																		onchange="SecurityCheck('porscheothervehicle',this,'hint_porscheothervehicle')">
																		<%=mybean.variantcheck.PopulatePorscheOtherVehicle(mybean.enquiry_id,mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_porscheothervehicle"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6 ">
																	<label>Person's In Household<font color="#ff0000">*</font>:</label>
																	<input type='text' name="txt_enquiry_porsche_householdcount" id="txt_enquiry_porsche_householdcount" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_porsche_householdcount', this, 'hint_txt_enquiry_porsche_householdcount');"
																		onKeyUp="toInteger('txt_enquiry_porsche_householdcount','Exprice')"
																		value='<%=mybean.enquiry_porsche_householdcount%>' />
																	<div class="hint" id="hint_txt_enquiry_porsche_householdcount"></div>
																</div>
																
																<div class="form-element6">
																	<label>DOB<font color="#ff0000">*</font>:</label>
																	<input name="txt_enquiry_porsche_contact_dob" id="txt_enquiry_porsche_contact_dob"
																		onChange="SecurityCheck('txt_enquiry_porsche_contact_dob', this, 'hint_txt_enquiry_porsche_contact_dob');"
																		value="<%=mybean.enquiry_porsche_contact_dob%>" class="form-control datepicker" type="text"
																		size="12" maxlength="10" />
																	<div class="hint" id="hint_txt_enquiry_porsche_contact_dob"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6 ">
																	<label>Anniversary:</label>
																	<input name="txt_enquiry_porsche_contact_anniversary" id="txt_enquiry_porsche_contact_anniversary"
																		onChange="SecurityCheck('txt_enquiry_porsche_contact_anniversary', this, 'hint_txt_enquiry_porsche_contact_anniversary');"
																		value="<%=mybean.enquiry_porsche_contact_anniversary%>" class="form-control datepicker" type="text"
																		size="12" maxlength="10" />
																	<div class="hint" id="hint_txt_enquiry_porsche_contact_anniversary"></div>
																</div>
																
																<div class="form-element6 ">
																	<label>Industry:</label>
																	<select name="dr_enquiry_porsche_industry" id="dr_enquiry_porsche_industry" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_porsche_industry', this, 'hint_enquiry_porsche_industry');">
																		<%=mybeanenqdashmethods.PopulateEnquiryPorscheIndustry( mybean.enquiry_porsche_industry, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_enquiry_porsche_industry"></div>
																</div>
															</div>
	
														</div>
													</div>
												</div>
	
												<%
													}
												%>
																							
											<!-- End Porsche Details -->
											
												<!-- Start JLR Details  -->
											<% if (mybean.branch_brand_id.equals("60")) { %>
	
												<div class="portlet box">
													<div class="portlet-title" style="text-align: center">
														<div class="caption" style="float: none">JLR Customer Need Assessement</div>
													</div>
													<div class="portlet-body portlet-empty container-fluid">
														<div class="tab-pane" id="">

															<div class="row">
																<div class="form-element6 ">
																<label>Employment Status:</label>
																	<select name="dr_enquiry_jlr_employmentstatus" id="dr_enquiry_jlr_employmentstatus" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_jlr_employmentstatus', this, 'hint_dr_enquiry_jlr_employmentstatus');">
																		<%=mybeanenqdashmethods.PopulateJLREmploymentStatus(mybean.enquiry_jlr_employmentstatus, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_jlr_employmentstatus"></div>
																</div>
																
																<div class="form-element6 ">
																	<label>Industry<font color="#ff0000">*</font>:</label>
																	<select name="dr_enquiry_jlr_industry" id="dr_enquiry_jlr_industry" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_jlr_industry', this, 'hint_dr_enquiry_jlr_industry');">
																		<%=mybeanenqdashmethods.PopulateEnquiryJLRIndustry(mybean.enquiry_jlr_industry, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_jlr_industry"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6">
																	<label>Birthday:</label>
																	<input name="txt_enquiry_jlr_birthday" id="txt_enquiry_jlr_birthday"
																		onChange="SecurityCheck('txt_enquiry_jlr_birthday', this, 'hint_txt_enquiry_jlr_birthday');"
																		value="<%=mybean.enquiry_jlr_birthday%>" class="form-control datepicker" type="text"
																		size="12" maxlength="10" />
																	<div class="hint" id="hint_txt_enquiry_jlr_birthday"></div>
																</div>
																
																<div class="form-element6 ">
																	<label>Gender:</label>
																	<select name="dr_enquiry_jlr_gender" id="dr_enquiry_jlr_gender" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_jlr_gender', this, 'hint_dr_enquiry_jlr_gender');">
																		<%=mybeanenqdashmethods.PopulateEnquiryJLRGender(mybean.enquiry_jlr_gender, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_jlr_gender"></div>
																</div>	
															</div>
															
															<div class="row">	
																<div class="form-element6">
																	<label>Occupation<font color="#ff0000">*</font>:</label> <select name="dr_enquiry_jlr_occupation"
																		id="dr_enquiry_jlr_occupation" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_jlr_occupation',this,'hint_dr_enquiry_jlr_occupation');">
																		<%=mybeanenqdashmethods.PopulateJLROccupation(mybean.enquiry_jlr_occupation, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_jlr_occupation"></div>
																</div>	
																													
																<div class="form-element6">
																	<label>Current Vehicles:</label>
																	<select class="form-control select2" id="jlrcurrentvehicle" name="jlrcurrentvehicle" multiple
																		onchange="SecurityCheck('jlrcurrentvehicle',this,'hint_jlrcurrentvehicle')">
																		<%=mybean.variantcheck.PopulateJLRCurrentCars(mybean.enquiry_id, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_jlrcurrentvehicle"></div>
																</div>
															</div>
															
															<div class="row">	
																<div class="form-element6">
																	<label>Other Model Interested:</label>
																	<select class="form-control select2" id="jlrothermodelofinterest" name="jlrothermodelofinterest" multiple
																		onchange="SecurityCheck('jlrothermodelofinterest',this,'hint_jlrothermodelofinterest')">
																		<%=mybean.variantcheck.PopulateJLROtherModel(mybean.enquiry_id, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_jlrothermodelofinterest"></div>
																</div>
																
																<div class="form-element3 form-element-margin">
																	<label>Finance Interest:</label>
																	<input type="checkbox" name="chk_enquiry_jlr_financeinterest" id="chk_enquiry_jlr_financeinterest"
																	<%=mybean.PopulateCheck(mybean.enquiry_jlr_financeinterest)%>
																	onclick="SecurityCheck('chk_enquiry_jlr_financeinterest',this,'hint_chk_enquiry_jlr_financeinterest')">
																	<div class="hint" id="hint_chk_enquiry_jlr_financeinterest"></div>
																</div>
																
																<div class="form-element3 form-element-margin">
																	<label>High Net Worth:</label>
																	<input type="checkbox" name="chk_enquiry_jlr_highnetworth" id="chk_enquiry_jlr_highnetworth"
																	<%=mybean.PopulateCheck( mybean.enquiry_jlr_highnetworth)%>
																	onclick="SecurityCheck('chk_enquiry_jlr_highnetworth',this,'hint_chk_enquiry_jlr_highnetworth')">
																	<div class="hint" id="hint_chk_enquiry_jlr_highnetworth"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6 ">
																	<label>No. of Children:</label>
																	<input type='text' name="txt_enquiry_jlr_noofchildren" id="txt_enquiry_jlr_noofchildren" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_jlr_noofchildren', this, 'hint_txt_enquiry_jlr_noofchildren');"
																		onKeyUp="toInteger('txt_enquiry_jlr_noofchildren','Exprice');" size="10" maxlength="3"
																		value='<%=mybean.enquiry_jlr_noofchildren%>' />
																	<div class="hint" id="hint_txt_enquiry_jlr_noofchildren"></div>
																</div>
																
																<div class="form-element6 ">
																	<label>No. of People in Household:</label>
																	<input type='text' name="txt_enquiry_jlr_noofpeopleinhousehold" id="txt_enquiry_jlr_noofpeopleinhousehold" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_jlr_noofpeopleinhousehold', this, 'hint_txt_enquiry_jlr_noofpeopleinhousehold');"
																		onKeyUp="toInteger('txt_enquiry_jlr_noofpeopleinhousehold','Exprice');" size="10" maxlength="3"
																		value='<%=mybean.enquiry_jlr_noofpeopleinhousehold%>' />
																	<div class="hint" id="hint_txt_enquiry_jlr_noofpeopleinhousehold"></div>
																</div>
															</div>
															
															<div class="row">
																<div class="form-element6 ">
																	<label>Household Income:</label>
																	<input type='text' name="txt_enquiry_jlr_householdincome" id="txt_enquiry_jlr_householdincome" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_jlr_householdincome', this, 'hint_txt_enquiry_jlr_householdincome');"
																		onKeyUp="toInteger('txt_enquiry_jlr_householdincome','Exprice');" size="15" maxlength="9"
																		value='<%=mybean.enquiry_jlr_householdincome%>' />
																	<div class="hint" id="hint_txt_enquiry_jlr_householdincome"></div>
																</div>
															
																<div class="form-element6 ">
																	<label>Annual Revenue:</label>
																	<input type='text' name="txt_enquiry_jlr_annualrevenue" id="txt_enquiry_jlr_annualrevenue" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_jlr_annualrevenue', this, 'hint_txt_enquiry_jlr_annualrevenue');"
																		onKeyUp="toInteger('txt_enquiry_jlr_annualrevenue','Exprice');" size="15" maxlength="9"
																		value='<%=mybean.enquiry_jlr_annualrevenue%>' />
																	<div class="hint" id="hint_txt_enquiry_jlr_annualrevenue"></div>
																</div>
															</div>
															
															<div class="row">	
																<div class="form-element6 ">
																	<label>No. of Employees:</label>
																	<input type='text' name="txt_enquiry_jlr_noofemployees" id="txt_enquiry_jlr_noofemployees" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_jlr_noofemployees', this, 'hint_txt_enquiry_jlr_noofemployees');"
																		onKeyUp="toInteger('txt_enquiry_jlr_noofemployees','Exprice')" size="15" maxlength="9"
																		value='<%=mybean.enquiry_jlr_noofemployees%>' />
																	<div class="hint" id="hint_txt_enquiry_jlr_noofemployees"></div>
																</div>
																
																<div class="form-element6">
																	<label>Account Type:&nbsp; </label>
																	<select name="dr_enquiry_jlr_accounttype"
																		id="dr_enquiry_jlr_accounttype" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_jlr_accounttype',this,'hint_dr_enquiry_jlr_accounttype');">
																		<%=mybeanenqdashmethods.PopulateJLRAccountType(mybean.enquiry_jlr_accounttype, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_jlr_accounttype"></div>
																</div>
															</div>
															
															<div class="form-element6">
																<label>Enquiry Status:</label>
																<span><b><%=mybean.enquiry_jlr_enquirystatus%></b></span> 
<!-- 																<div class="hint" id="hint_dr_enquiry_jlr_status"></div> -->
															</div>
															
															<div class="form-element6 ">
																<label>Interests:</label>
																<div>
																	<%=mybeanenqdashmethods.PopulateEnquiryJLRInsterest(mybean.enquiry_jlr_interests, mybean.comp_id) %>
																</div>
																<div class="hint" id="hint_chk_enquiry_jlr_interest"></div>
															</div>
															
														</div>
													</div>
												</div>
	
												<%
													}
												%>
											<!-- End JLR Details  -->
											
											
											<!-- Start of Skoda Details  -->
											<%
												if (mybean.branch_brand_id.equals("11")) {
											%>
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Skoda
														Customer Need Assessment</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">

														<div class="row">
															<div class="form-element6">
																<label>Own a Business<font color="#ff0000">*</font>:</label> <select
																	class="form-control " id="na_skoda_ownbusiness"
																	name="na_skoda_ownbusiness"
																	onchange="SecurityCheckSkoda('na_skoda_ownbusiness',this,'hint_na_skoda_ownbusiness')">
																	<%=mybeanenqdashmethods.PopulateSkodaOwnBusiness(mybean.na_skoda_ownbusiness, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_na_skoda_ownbusiness"></div>
															</div>

															<div class="form-element6">
																<label>Company Name<font color="#ff0000">*</font>:</label> <input
																	class="form-control " id="na_skoda_companyname"
																	name="na_skoda_companyname"
																	onchange="SecurityCheckSkoda('na_skoda_companyname',this,'hint_na_skoda_companyname')"
																	 value="<%=mybean.na_skoda_companyname%>"/>
																<div class="hint" id="hint_na_skoda_companyname"></div>
															</div>
														</div>
														
														<div class="row">
															<div class="form-element6">
																<label>Job Title<font color="#ff0000">*</font>:</label>
																<input name="txt_skoda_contact_jobtitle" type="text" maxlength="255"
																	class="form-control" id="txt_skoda_contact_jobtitle" size="35"
																	onChange="SecurityCheckSkoda('txt_skoda_contact_jobtitle', this, 'hint_txt_skoda_contact_jobtitle')"
																	value="<%=mybean.contact_jobtitle%>" />
																<div class="hint" id="hint_txt_skoda_contact_jobtitle"></div>
															</div>

															<div class="form-element6">
																<label>Finance Required<font color="#ff0000">*</font>:</label> <select
																	class="form-control " id="na_skoda_financerequired"
																	name="na_skoda_financerequired"
																	onchange="SecurityCheckSkoda('na_skoda_financerequired',this,'hint_na_skoda_financerequired')">
																	<%=mybeanenqdashmethods.PopulateSkodaFinance(mybean.na_skoda_financerequired, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_na_skoda_financerequired"></div>
															</div>
														</div>
														
														<div class="row">
															<div class="form-element6">
															<label>How soon is the purchase<font color=red>*</font>: </label>
															<input name="txt_skoda_enquiry_exp_close_date" id="txt_skoda_enquiry_exp_close_date"
																class="form-control datepicker" type="text"
																value="<%=mybean.enquirydate%>" size="12" maxlength="10"
																onChange="SecurityCheckSkoda('txt_skoda_enquiry_exp_close_date',this,'hint_txt_skoda_enquiry_exp_close_date');" />
															<div class="hint" id="hint_txt_skoda_enquiry_exp_close_date"></div>

														</div>

															<div class="form-element6">
																	<label>Current Cars<font color="#ff0000">*</font>:</label>
																	<select class="form-control select2" id="porscheothervehicle" name="porscheothervehicle" multiple
																		onchange="SecurityCheckSkoda('dr_na_skoda_enquiry_currentcars',this,'hint_dr_na_skoda_enquiry_currentcars')">
																		<%=mybean.variantcheck.PopulatePorscheOtherVehicle(mybean.enquiry_id, mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_na_skoda_enquiry_currentcars"></div>
															</div>
														</div>
														<div class="row">
														
														<div class="form-element6">
																<label>Approximate kms run<font color="#ff0000">*</font>:</label> <select
																	class="form-control " id="na_skoda_currentcarappxkmsrun"
																	name="na_skoda_currentcarappxkmsrun"
																	onchange="SecurityCheckSkoda('na_skoda_currentcarappxkmsrun',this,'hint_na_skoda_currentcarappxkmsrun')">
																	<%=mybeanenqdashmethods.PopulateSkodaAppxCurrentCarRun(mybean.na_skoda_currentcarappxkmsrun, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_na_skoda_currentcarappxkmsrun"></div>
															</div>

															<div class="form-element6 ">
																<label>What are you looking for in your car<font color="#ff0000">*</font>:
																</label>
																<div>
																	<%=mybeanenqdashmethods.PopulateSkodaWhatAreYouLookingFor(mybean.na_skoda_whatareyoulookingfor, mybean.comp_id)%>
																</div>
																<div class="hint" id="hint_chk_na_skoda_whatareyoulookingfor"></div>
															</div>

														</div>
														
														<div class="row">
														
														<div class="form-element6">
																<label>Number of Family Members<font color="#ff0000">*</font>:</label> <select
																	class="form-control " id="na_skoda_numberoffamilymembers"
																	name="na_skoda_numberoffamilymembers"
																	onchange="SecurityCheckSkoda('na_skoda_numberoffamilymembers',this,'hint_na_skoda_numberoffamilymembers')">
																	<%=mybeanenqdashmethods.PopulateSkodaMembersInTheFamily(mybean.na_skoda_numberoffamilymembers, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_na_skoda_numberoffamilymembers"></div>
															</div>
															
															<div class="form-element6">
																<label>Who will drive the car<font color="#ff0000">*</font>:</label> <select
																	class="form-control " id="na_skoda_whowilldrive"
																	name="na_skoda_whowilldrive"
																	onchange="SecurityCheckSkoda('na_skoda_whowilldrive',this,'hint_na_skoda_whowilldrive')">
																	<%=mybeanenqdashmethods.PopulateWhoWillDrive(mybean.na_skoda_whowilldrive, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_na_skoda_whowilldrive"></div>
															</div>


														</div>
														
														<div class="row">
														
														<div class="form-element6">
																<label>Who are you buying the car for<font color="#ff0000">*</font>:</label> <select
																	class="form-control "
																	id="na_skoda_whoareyoubuyingfor"
																	name="na_skoda_whoareyoubuyingfor"
																	onchange="SecurityCheckSkoda('na_skoda_whoareyoubuyingfor',this,'hint_na_skoda_whoareyoubuyingfor')">
																	<%=mybeanenqdashmethods.PopulateWhoAreYouBuyingFor(mybean.na_skoda_whoareyoubuyingfor, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_na_skoda_whoareyoubuyingfor"></div>
															</div>
															
															<div class="form-element6">
																<label>Approximately how many kms in a day will the car be run<font color="#ff0000">*</font>:</label> <select
																	class="form-control "
																	id="na_skoda_newcarappxrun"
																	name="na_skoda_newcarappxrun"
																	onchange="SecurityCheckSkoda('na_skoda_newcarappxrun',this,'hint_na_skoda_newcarappxrun')">
																	<%=mybeanenqdashmethods.PopulateSkodaAppxNewCarRun(mybean.na_skoda_newcarappxrun, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_na_skoda_newcarappxrun"></div>
															</div>


														</div>
														
														<div class="row">
														
														
															
															<div class="form-element6">
																<label>Where will the car be driven mostly<font color="#ff0000">*</font>:</label> <select
																	class="form-control "
																	id="na_skoda_wherewillbecardriven"
																	name="na_skoda_wherewillbecardriven"
																	onchange="SecurityCheckSkoda('na_skoda_wherewillbecardriven',this,'hint_na_skoda_wherewillbecardriven')">
																	<%=mybeanenqdashmethods.PopulateSkodaWhereWillBeDriven(mybean.na_skoda_wherewillbecardriven, mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_na_skoda_wherewillbecardriven"></div>
															</div>


														</div>
														

													</div>
												</div>
											</div>
											<%
												}
											%>

											<!-- END of Skoda Details  -->
											
											
											<!-- Start Of Exchange Details -->

											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Exchange Details</div>
												</div>

												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->

														<div class="row">
															<div class="form-element6">
																<label>Trade-In Model:</label>
																<select class="form-control select2" id="preownedvariant2" name="preownedvariant2"
																	onchange="SecurityCheck('preownedvariant2',this,'hint_preownedvariant2')">
																	<%=mybean.variantcheck.PopulateVariant(mybean.enquiry_tradein_preownedvariant_id)%>
																</select>
																<div class="hint" id="hint_preownedvariant2"></div>
															</div>

															<div class="form-element6" style="margin-top: 12px">
																<span id="addEvalBut"> <a
																	class=" btn btn-success btn-outline sbold"
																	onclick="return validatePreowned();">Add Evaluation
																</a> <input id="preowned_new_enquiry_id" type="hidden"
																	value="<%=mybean.enquiry_id%>" /> <span
																	id='linkpreowned'> <a id="submitpreowned"
																		href="../preowned/preowned-dash-add.jsp"
																		data-target="#Hintclicktocall" data-toggle="modal">
																	</a>
																</span>
																</span>
															</div>
														</div>
														<!--	Start Pre-owned details -->

														<div class="row">
															<div class="form-element3">
																<label>Registration No <%
																	if (mybean.comp_id.equals("1009")) {
																%>
																	<font color="#ff0000">*</font> <%
 	}
 %> :
																</label>
																<%
																	if (mybean.preowned_enquiry_id.equals("0")) {
																%>

																<input id="txt_preowned_regno" name="txt_preowned_regno"
																	type="text" class="form-control"
																	value="<%=mybean.preowned_regno%>" />

																<%
																	} else {
																%>

																<input id="txt_preowned_regno" name="txt_preowned_regno"
																	disabled type="text" class="form-control"
																	value="<%=mybean.preowned_regno%>" />

																<%
																	}
																%>

															</div>

															<div class="form-element3">
																<label>Manuf. Year <%
																	if (mybean.comp_id.equals("1009")) {
																%>
																	<font color="#ff0000">*</font> <%
 	}
 %>:
																</label>
																<%
																	if (mybean.preowned_enquiry_id.equals("0")) {
																%>
																<select id="dr_preowned_manufyear"
																	name="dr_preowned_manufyear" class="form-control">
																	<%=mybean.PopulatePreownedManufYear()%>
																</select>
																<%
																	} else {
																%>
																<select id="dr_preowned_manufyear"
																	name="dr_preowned_manufyear" disabled
																	class="form-control">
																	<%=mybean.PopulatePreownedManufYear()%>
																</select>
																<%
																	}
																%>
															</div>

															<div class="form-element3">
																<label>Kms <%
																	if (mybean.comp_id.equals("1009")) {
																%>
																	<font color="#ff0000">*</font> <%
 	}
 %> :
																</label>
																<%
																	if (mybean.preowned_enquiry_id.equals("0")) {
																%>

																<input id="txt_preowned_kms" name="txt_preowned_kms"
																	onKeyUp="toPhone('txt_preowned_kms','Kms')" type="text"
																	class="form-control" value="<%=mybean.preowned_kms%>" />

																<%
																	} else {
																%>

																<input id="txt_preowned_kms" name="txt_preowned_kms"
																	disabled onKeyUp="toPhone('txt_preowned_kms','Kms')"
																	type="text" class="form-control"
																	value="<%=mybean.preowned_kms%>" />
																<%
																	}
																%>

															</div>

															<!-- <div class="row"></div> -->

															<div class="form-element3">
																<label>Ownership <%
																	if (mybean.comp_id.equals("1009")) {
																%>
																	<font color="#ff0000">*</font> <%
 	}
 %> :
																</label>
																<!--  </div> -->

																<!--  <div class="form-element6"> -->
																<%
																	if (mybean.preowned_enquiry_id.equals("0")) {
																%>
																<select id="dr_preowned_ownership"
																	name="dr_preowned_ownership" class="form-control">
																	<%=mybean.PopulatePreOwnedOwnership(mybean.comp_id)%>
																</select>
																<%
																	} else {
																%>
																<select id="dr_preowned_ownership"
																	name="dr_preowned_ownership" disabled
																	class="form-control">
																	<%=mybean.PopulatePreOwnedOwnership(mybean.comp_id)%>
																</select>
																<%
																	}
																%>
															</div>
														</div>

														<div class="row">
															<div class="row form-element-margin">
																<div class="form-element3">
																	<label> Sub Variant: </label>
																	<div>
																		<%=mybean.preowned_sub_variant%>
																		<!--<div class="hint" id="hint_txt_enquiry_quotedprice"></div> -->
																	</div>
																</div>

																<div class="form-element3">
																	<label> Fuel Type:</label>
																	<div>
																		<%=mybean.fueltype_name%>
																		<!--<div class="hint" id="hint_txt_enquiry_quotedprice"></div> -->
																	</div>
																</div>

																<div class="form-element3">
																	<label> Exterior:</label>
																	<div>
																		<%=mybean.extcolour_name%>
																	</div>
																</div>

																<div class="form-element3">
																	<label>Interior:</label>
																	<div>
																		<%=mybean.intcolour_name%>
																	</div>
																</div>
															</div>
														</div>

														<div class="row">
															<div class="row form-element-margin">
																<div class="form-element3">
																	<label>Foreclosure Amount:&nbsp;</label> <span>
																		<%=mybean.preowned_fcamt%> <!-- <div class="hint" id="hint_txt_enquiry_quotedprice"></div> -->
																	</span>
																</div>

																<div class="form-element3">
																	<label>Expected Price:&nbsp;</label> <span> <%=mybean.preowned_expectedprice%>
																		<!--<div class="hint" id="hint_txt_enquiry_quotedprice"></div> -->
																	</span>
																</div>

																<!-- <div class="row"></div> -->

																<!--Start Evaluation -->
																<div class="form-element3">
																	<label>Offered Price:&nbsp;</label> <span> <%=mybean.eval_offered_price%>
																		<!--<div class="hint" id="hint_txt_enquiry_quotedprice"></div> -->
																	</span>
																</div>

																<div class="form-element3">
																	<label>Evaluation Time:&nbsp;</label> <span> <%=mybean.eval_entry_date%>
																		<!--<div class="hint" id="hint_txt_enquiry_quotedprice"></div> -->
																	</span>
																</div>
															</div>
														</div>
														<!-- End Evaluation -->
														<!--End Pre-owned details -->
													</div>
												</div>
											</div>


											<!-- End Of exchange Details -->


											<!--                       Start Customer Details-->
											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Customer</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->

														<div class="row">

															<div class="form-element6">
																<label>Customer:</label> <input
																	name="txt_enquiry_customer_name" type="text"
																	class="form-control" id="txt_enquiry_customer_name"
																	value="<%=mybean.enquiry_customer_name%>" size="32"
																	maxlength="255"
																	onChange="SecurityCheck('txt_enquiry_customer_name',this,'hint_txt_enquiry_customer_name')" />
																<span class="hint" id="hint_txt_enquiry_customer_name"></span>
															</div>

															<div class="form-element6">
																<div class="form-element2 form-element">
																	<label>Contact:</label> <select name="dr_title"
																		class="form-control" id="dr_title"
																		onChange="SecurityCheck('dr_title',this,'hint_dr_title')">
																		<%=mybean.PopulateContactTitle(mybean.comp_id)%>
																	</select> <span>Title</span>
																	<div class="hint" id="hint_dr_title"></div>
																</div>

																<div class="form-element5 form-element-margin">
																	<input name="txt_contact_fname" type="text"
																		class="form-control" id="txt_contact_fname"
																		value="<%=mybean.contact_fname%>" size="30" maxlength="255"
																		onChange="SecurityCheck('txt_contact_fname',this,'hint_txt_contact_fname')" />
																	<span>First Name</span>
																	<div class="hint" id="hint_txt_contact_fname"></div>
																</div>

																<div
																	class="form-element5 form-element form-element-margin">
																	<input name="txt_contact_lname" type="text"
																		class="form-control" id="txt_contact_lname"
																		value="<%=mybean.contact_lname%>" size="30" maxlength="255"
																		onChange="SecurityCheck('txt_contact_lname',this,'hint_txt_contact_lname')" />
																	<span>Last Name</span>
																	<div class="hint" id="hint_txt_contact_lname"></div>
																</div>
															</div>
														</div>
														<!-- 															</div> -->
														<!-- 														</div> -->
														<div class="row">
															<div class="form-element6">
																<label>Job Title:</label>
																<input name="txt_contact_jobtitle" type="text" maxlength="255"
																	class="form-control" id="txt_contact_jobtitle" size="35"
																	onChange="SecurityCheck('txt_contact_jobtitle', this, 'hint_txt_contact_jobtitle')"
																	value="<%=mybean.contact_jobtitle%>" />
																<div class="hint" id="hint_txt_contact_jobtitle"></div>
															</div>
															
															<div class="form-element3">
																<label> Mobile 1<font color="#ff0000">*</font>:
																</label> <input name="txt_contact_mobile1" type="text"
																	class="form-control" id="txt_contact_mobile1"
																	onKeyUp="toPhone('txt_contact_mobile1','Contact mobile2')"
																	value="<%=mybean.contact_mobile1%>" size="32"
																	maxlength="13"
																	onChange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')" />
																(91-9999999999)
																<div class="hint" id="hint_txt_contact_mobile1"></div>
															</div>

															<div class="form-element3">
																<label>Mobile 2:</label> <input
																	name="txt_contact_mobile2" type="text"
																	class="form-control" id="txt_contact_mobile2"
																	onKeyUp="toPhone('txt_contact_mobile2','Contact Mobile2')"
																	value="<%=mybean.contact_mobile2%>" size="32"
																	maxlength="13"
																	onChange="SecurityCheck('txt_contact_mobile2',this,'hint_txt_contact_mobile2')" />
																(91-9999999999)
																<div class="hint" id="hint_txt_contact_mobile2"></div>
															</div>
														</div>


														<div class="row">
															<div class="form-element3 ">
																<label>Phone 1<font color="#ff0000">*</font>:
																</label> <input name="txt_contact_phone1" type="text"
																	class="form-control" id="txt_contact_phone1"
																	onKeyUp="toPhone('txt_contact_phone1','Contact Phone1')"
																	value="<%=mybean.contact_phone1%>" size="32"
																	maxlength="15"
																	onchange="SecurityCheck('txt_contact_phone1',this,'hint_txt_contact_phone1')" />
																(91-80-33333333)
																<div class="hint" id="hint_txt_contact_phone1"></div>
															</div>

															<div class="form-element3">
																<label>Phone 2:</label> <input name="txt_contact_phone2"
																	type="text" class="form-control"
																	id="txt_contact_phone2"
																	onKeyUp="toPhone('txt_contact_phone2','Contact Phone2')"
																	value="<%=mybean.contact_phone2%>" size="32"
																	maxlength="15"
																	onchange="SecurityCheck('txt_contact_phone2',this,'hint_txt_contact_phone2')" />
																(91-80-33333333)
																<div class="hint" id="hint_txt_contact_phone2"></div>
															</div>
															
															<div class="form-element3">
																<label> Email 1:</label> <input
																	name="txt_contact_email1" type="text"
																	class="form-control" id="txt_contact_email1"
																	value="<%=mybean.contact_email1%>" size="35"
																	maxlength="100"
																	onChange="SecurityCheck('txt_contact_email1',this,'hint_txt_contact_email1')">
																<div class="hint" id="hint_txt_contact_email1"></div>
															</div>

															<div class="form-element3">
																<label> Email 2:</label> <input
																	name="txt_contact_email2" type="text"
																	class="form-control" id="txt_contact_email2"
																	value="<%=mybean.contact_email2%>" size="35"
																	maxlength="100"
																	onChange="SecurityCheck('txt_contact_email2',this,'hint_txt_contact_email2')">
																<div class="hint" id="hint_txt_contact_email2"></div>
															</div>
														</div>

														<div class="row">

															<div class="form-element6">
																<label>Address<font color="#ff0000">*</font>:
																</label>
																<textarea name="txt_contact_address" cols="20" rows="4"
																	class="form-control" id="txt_contact_address"
																	onKeyUp="charcount('txt_contact_address', 'span_txt_contact_address','<font color=red>({CHAR} characters left)</font>', '255')"
																	onChange="SecurityCheck('txt_contact_address',this,'hint_txt_contact_address')"><%=mybean.contact_address%></textarea>
																<span id="span_txt_contact_address"> (255
																	Characters)</span>
																<div class="hint" id="hint_txt_contact_address"></div>
															</div>

															<div class="form-element6">
																<label>City<font color="#ff0000">*</font>:
																</label> <select class="form-control select2" id="maincity"
																	name="maincity"
																	onchange="SecurityCheck('maincity',this,'hint_maincity')">
																	<%=mybean.PopulateCity(mybean.contact_city_id)%>
																</select>
																<!--<input tabindex="-1" class="bigdrop select2-offscreen" -->
																<!--id="maincity" name="maincity" style="width: 250px" -->
																<%--value="<%=mybean.contact_city_id)%>" type="hidden" --%>
																<!--onChange="SecurityCheck('maincity',this,'hint_maincity')"> -->
																<div class="hint" id="hint_maincity"></div>
															</div>

															<div class="form-element6">
																<label>Pin/Zip<font color="#ff0000">*</font>:
																</label> <input name="txt_contact_pin" type="text"
																	class="form-control" id="txt_contact_pin"
																	onKeyUp="toInteger('txt_contact_pin','Pin')"
																	onChange="SecurityCheck('txt_contact_pin',this,'hint_txt_contact_pin')"
																	value="<%=mybean.contact_pin%>" size="10" maxlength="6" />
																<div class="hint" id="hint_txt_contact_pin"></div>
															</div>
														</div>


													</div>
												</div>
											</div>

											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Status</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->

														<div class="row">

															<div class="form-element6 form-element-margin">
																<center>
																	<label><b>Stage</b><font color="#ff0000">*</font>:&nbsp;</label>
																	<span> <b><%=mybean.stage_name%></b>
																	</span>
																</center>
															</div>


															<div class="form-element6">
																<label>Priority<font color="#ff0000">*</font>:
																</label> <span> <b><%=mybean.priorityenquiry_desc%> (<%=mybean.priorityenquiry_duehrs%>)</b></span>
																</span>
															</div>

														</div>

														<div class="row">

															<div class="form-element6">
																<div class="form-element12 form-element">
																	<label>Status<font color="#ff0000">*</font>:
																	</label> <select name="dr_enquiry_status_id"
																		class="form-control" id="dr_enquiry_status_id"
																		onChange="StatusUpdate();">
																		<%=mybean.PopulateStatus(mybean.comp_id)%>
																	</select>
																	<div class="hint" id="hint_dr_enquiry_status_id"></div>
																</div>

																<div class="form-element12 form-element">
																	<label> Lost Case 1<font color="#ff0000">*</font>:
																	</label> <span id="span_lostcase1"> <select
																		name="dr_enquiry_lostcase1_id" class="form-control"
																		id="dr_enquiry_lostcase1_id"
																		onchange="populateLostCase2('dr_enquiry_lostcase1_id',this,'span_lostcase2');StatusUpdate();">
																			<%=mybean.PopulateLostCase1(mybean.comp_id)%>
																	</select>
																		<div class="hint" id="hint_dr_enquiry_lostcase1_id"></div>
																</div>
															</div>
															<div class="form-element6 ">
																<label>Status Comments<font color="#ff0000">*</font>:
																</label>
																<textarea name="txt_enquiry_status_desc" cols="50"
																	rows="4" class="form-control"
																	id="txt_enquiry_status_desc"
																	onKeyUp="charcount('txt_enquiry_status_desc', 'span_txt_enquiry_status_desc','<font color=red>({CHAR} characters left)</font>', '1000')"
																	onChange="SecurityCheck('txt_enquiry_status_desc',this,'hint_txt_enquiry_status_desc'); StatusUpdate();"><%=mybean.enquiry_status_desc%></textarea>
																<span id="span_txt_enquiry_status_desc"> (1000
																	Characters)</span>
																<div class="hint" id="hint_txt_enquiry_status_desc"></div>
															</div>

														</div>

														<div class="row">

															<div class="form-element6">
																<div class="form-element12 form-element">
																	<label>Lost Case 2<font color="#ff0000">*</font>:
																	</label> <span id="span_lostcase2"> <span
																		id="span_lostcase2"> <select
																			name="dr_enquiry_lostcase2_id" class="form-control"
																			id="dr_enquiry_lostcase2_id"
																			onChange="populateLostCase3('dr_enquiry_lostcase2_id',this,'span_lostcase3');StatusUpdate();"></span>
																		<%=mybean.PopulateLostCase2(mybean.comp_id)%> </select>
																	</span>
																	<div class="hint" id="hint_dr_enquiry_lostcase2_id"></div>
																</div>

																<div class="form-element12 form-element">
																	<label>Lost Case 3<font color="#ff0000">*</font>:
																	</label> <span id="span_lostcase3"> <select
																		name="dr_enquiry_lostcase3_id" class="form-control"
																		id="dr_enquiry_lostcase3_id"
																		onchange="StatusUpdate();">
																			<%=mybean.PopulateLostCase3(mybean.comp_id)%>
																	</select>
																	</span>
																	<div class="hint" id="hint_dr_enquiry_lostcase3_id"></div>
																</div>

															</div>

															<div class="form-element6">
																<label>Notes:</label>
																<textarea name="txt_enquiry_notes" cols="70" rows="4"
																	class="form-control" id="txt_enquiry_notes"
																	onChange="SecurityCheck('txt_enquiry_notes',this,'hint_txt_enquiry_notes')"><%=mybean.enquiry_notes%></textarea>
																<div class="hint" id="hint_txt_enquiry_notes"></div>
															</div>

														</div>

														<div class="row">
															<div class="form-element6">
																<label>DMS No.:</label> <input name="txt_enquiry_dmsno" type="text"
																	class="form-control" id="txt_enquiry_dmsno"
																	value="<%=mybean.enquiry_dmsno%>" size="32"
																	maxlength="50"
																	onChange="SecurityCheck('txt_enquiry_dmsno',this,'hint_txt_enquiry_dmsno');" />
																<div class="hint" id="hint_txt_enquiry_dmsno"></div>
															</div>
															
															<div class="form-element3 form-element-margin">
																<label>DMS:</label>
																<input id="chk_enquiry_dms" type="checkbox" name="chk_enquiry_dms"
																	onchange="SecurityCheck('chk_enquiry_dms',this,'hint_chk_enquiry_dms');"
																	<%=mybean.PopulateCheck(mybean.enquiry_dms)%> />
																<div class="hint" id="hint_chk_enquiry_dms"></div>
															</div>

															<div class="form-element3 form-element-margin">
																<label>SOE:&nbsp;</label> <span> <%=mybean.soe_name%>
																</span>
															</div>
														</div>


														<div class="row">

															<div class="form-element3 form-element-margin">
																<label>Entry By:&nbsp;</label> <span> <%=mybean.entry_by%>
																	<input type="hidden" name="entry_by"
																	value="<%=mybean.entry_by%>">
																</span>
															</div>

															<div class="form-element3 form-element-margin">
																<label>Entry Date:&nbsp;</label> <span> <%=mybean.entry_date%>
																	<input type="hidden" name="entry_date"
																	value="<%=mybean.entry_date%>">
																</span>
															</div>

															<%
																if (mybean.modified_by != null && !mybean.modified_by.equals("")) {
															%>


															<div class="form-element3 form-element-margin">
																<label>Modified By:&nbsp;</label> <span> <%=mybean.modified_by%>
																	<input type="hidden" name="modified_by"
																	value="<%=mybean.modified_by%>">
																</span>
															</div>



															<div class="form-element3 form-element-margin">
																<label>Modified Date:&nbsp;</label> <span> <%=mybean.modified_date%>
																	<input type="hidden" name="modified_date"
																	value="<%=mybean.modified_date%>">
																</span>
															</div>


															<%
																}
															%>
														</div>
													</div>
												</div>
											</div>
										</form>
									</div>

									<!--       End Customer Details-->

									<div class="tab-pane" id="tabs-2">
										<!-- 	 	Follow up -->
										<center>
											<font color="#ff0000"><b><%=mybean.msg%></b></font>
										</center>
										<%--  <div><%=mybean.customerdetail%></div> --%>

										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Enquiry
													Follow-up</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<%=mybean.customerdetail%>
												</div>
											</div>
										</div>

										<div><%=mybean.followupHTML%></div>

										<!--  followup -->
										<%--    <%if(mybean.status.equals("")){%> --%>
										<div class="portlet box">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">
													<center>
														<%=mybean.status%>
														Follow-up
													</center>

												</div>
											</div>


											<div class="portlet-body portlet-empty container-fluid">

												<div class="tab-pane" id="">
													<form class="form-horizontal">
														<!-- START PORTLET BODY -->
														<div class="form-element6">
															<%
																if (mybean.status.equals("Update")) {
															%>
															<%
																if (mybean.branch_brand_id.equals("55")) {
															%>
															<div class="form-element6">
																<label> Current Follow-up Status<font
																	color="#ff0000">*</font>:
																</label> <select name="dr_followupstatus" class="form-control"
																	id="dr_followupstatus">
																	<%=mybeanenqdashmethods.PopulateFollowupStatus( mybean.comp_id, mybean.followup_followupstatus_id)%>
																</select>
															</div>
															<%
																}
															%>

															<div class="form-element6">
																<label> Action Taken<font color="#ff0000">*</font>:
																</label> <select name="dr_followup_feedbacktype_id"
																	class="form-control" id="dr_followup_feedbacktype_id">
																	<%=mybean.PopulateFollowupDesc(mybean.comp_id)%>
																</select>
															</div>
															
															<%
																}
															%>
															<%
																if (mybean.branch_brand_id.equals("60")) {
															%>
															<div class="form-element6">
																<label>Enquiry Status<font color="#ff0000">*</font>:&nbsp; </label>
																<select name="drop_enquiry_jlr_status"
																	id="drop_enquiry_jlr_status" class="form-control">
																	<%=mybeanenqdashmethods.PopulateEnquiryStatus(mybean.enq_jlr_enquirystatus, mybean.comp_id)%>
																</select>
															</div>
															
															<!--  added to store history old value -->
															<input type="hidden" id="jlr_enquirystatus" name="jlr_enquirystatus" value="<%=mybean.jlr_enquirystatus%>"/>
															
															<%
																}
															%>
															<%
																if ((mybean.enquiry_status_id.equals("1") && mybean.status
																		.equals("Update")) || mybean.status.equals("Add")) {
															%>
															<div class="form-element6">
																<label> Next Follow-up Time<font color=#ff0000><b>*</b></font>:
																</label> <input type="text" size="16" name="txt_followup_time"
																	id="txt_followup_time"
																	class="form-control datetimepicker"> <span
																	class="input-group-btn"> </span>
															</div>

															<div class="form-element6">
																<label>Next Follow-up Type<font color="#ff0000">*</font>:
																</label> <select name="dr_followuptype" class="form-control"
																	id="dr_followuptype" visible="true">
																	<%=mybean.PopulateFollowuptype(mybean.comp_id)%>
																</select>
															</div>
															<%
																}
															%>
 
														</div>

														<div class="form-element6">
															<label> Feedback<font color="#ff0000">*</font>:
															</label>
															<textarea name="txt_followup_desc" cols="50" rows="4"
																class="form-control" id="txt_followup_desc"
																onKeyUp="charcount('txt_followup_desc', 'span_txt_followup_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.followup_desc%></textarea>
															<span id="span_txt_followup_desc">1000 characters</span>
														</div>


														<div class="form-element12">
															<center>
																<input name="submit_button"
																	class="button btn btn-success" type="submit"
																	id="submit_button" value="Submit" />
															</center>
															<input type="hidden" name="enquiry_id" id="enquiry_id"
																value="<%=mybean.enquiry_id%>">
														</div>
													</form>
												</div>
											</div>
										</div>

										<%-- 										<%} %> --%>
									</div>


									<div class="tab-pane" id="tabs-3">

										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">CRM Follow-up</div>
											</div>
											<center>
												<font color="#ff0000"><b><%=mybean.crmmsg%></b></font> <br>
												<font color="#ff0000"><b><%=mybean.crmticketmsg%></b></font>
											</center>
											<div class="portlet-body portlet-empty">
											<%if(!mybean.enquiry_id.equals("0")){ %>
												<%=mybean.ListCRMFollowup(mybean.enquiry_id, "0", mybean.comp_id)%>
												<%} %>
											</div>
										</div>
									</div>

									<div class="tab-pane" id="tabs-12">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Pre-Owned
													Follow-up</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<%=mybean.ListPreOwnedFollowup(mybean.enquiry_id, mybean.comp_id)%>

												</div>
											</div>
										</div>
									</div>

									<div class="tab-pane" id="tabs-4">
										<div>
											<%=mybean.Customer_dash.CustomerDetails(response, mybean.enquiry_customer_id, "", mybean.comp_id)%>
										</div>
									</div>
									<div class="tab-pane" id="tabs-5">

										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Doucumets</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<%=mybean.ListDocs(mybean.enquiry_id, "1", "", mybean.recperpage, mybean.comp_id)%>
												</div>
											</div>
										</div>
									</div>
									<div class="tab-pane" id="tabs-6">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Test Drive(s)</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<%
														if (mybean.enquiry_enquirytype_id.equals("1")) {
													%>
													<%=mybean.ListTestDrive(mybean.comp_id)%>
													<%
														}
													%>
													<%
														if (mybean.enquiry_enquirytype_id.equals("2")) {
													%>
													<%=mybean.ListPreownedTestDrive(mybean.comp_id)%>
													<%
														}
													%>
												</div>
											</div>
										</div>
									</div>

									<div class="tab-pane" id="tabs-7">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Quote(s)</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<%=mybean.ListQuotes(mybean.comp_id)%>
												</div>
											</div>
										</div>
									</div>

									<div class="tab-pane" id="tabs-8">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Sales Order(s)</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<%=mybean.ListSO(mybean.comp_id)%> 
												</div>
											</div>
										</div>
									</div>

									<div class="tab-pane" id="tabs-9"></div>

									<div class="tab-pane" id="tabs-10"></div>
									
									<div class="tab-pane" id="tabs-11">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">History</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<%=mybean.ListHistory(mybean.comp_id)%>
												</div>
											</div>
										</div>
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
	<!-- 	Tags -->
	<script src="../assets/js/bootstrap-tagsinput.js" type="text/javascript"></script>
	<!-- 	Tags -->
	<script>
	// 	$(document).ready(function() {
	// 		$("#open").hide();
	// 		$("#search").click(function() {
	// 			$("#open").toggle("slow");
	// 			 $(".fa-plus").toggle();
	// 			 $(".fa-minus").toggle();
				
	// 		});
	// 		$("#minus").hide();	    
			
	// 	});
	</script>

	<script type="text/javascript">
		function CheckEvaluation() {

			var preowned_enquiry_id =<%=mybean.preowned_enquiry_id%>;

			if(preowned_enquiry_id != "0"){
				$("#addEvalBut").html("<font color=\"red\"><b>Pre-Owned Enquiry added.</b></font>");
			}
	
		}
	</script>


	<script>

// 		$(document).ready(function() {
// 			$('#txt_enquiry_exp_close_date').datepicker().on('changeDate', function(){
// 				SecurityCheck('txt_enquiry_exp_close_date',this,'hint_txt_enquiry_exp_close_date');
// 			});
			
// 			$('#txt_enquiry_purchasemonth').datepicker().on('changeDate', function(){
// 				SecurityCheck('txt_enquiry_purchasemonth',this,'hint_enquiry_purchasemonth');
// 			});
			
// 			$('#txt_enquiry_loancompletionmonth').datepicker().on('changeDate', function(){
// 				SecurityCheck('txt_enquiry_loancompletionmonth',this,'hint_enquiry_loancompletionmonth');
// 			});
			
// 			$('#txt_enquiry_hyundai_ex_purchasedate').datepicker().on('changeDate', function(){
// 				SecurityCheck('txt_enquiry_hyundai_ex_purchasedate',this,'hint_txt_enquiry_hyundai_ex_purchasedate')
// 			});
			
// 			$('#txt_enquiry_hyundai_ex_loancompletion').datepicker().on('changeDate', function(){
// 				SecurityCheck('txt_enquiry_hyundai_ex_loancompletion',this,'hint_txt_enquiry_hyundai_ex_loancompletion');
// 			});
			
// 			$('#txt_veh_renewal_date').datepicker().on('changeDate', function(){
// 				SecurityCheck('txt_veh_renewal_date',this,'hint_txt_veh_renewal_date');
// 			});
			
// 			$('#txt_enquiry_hyundai_dob').datepicker().on('changeDate', function(){
// 				SecurityCheck('txt_enquiry_hyundai_dob',this,'hint_txt_enquiry_hyundai_dob');
// 			});
			
// 		});
	</script>
	<script>
		function TestdriveReq(name, obj, hint) {
			var testdrive_yes = document.getElementById("dr_enquiry_nexa_testdrivereq").value;
			if (testdrive_yes == "2") {
				document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display = '';
				document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.visibility = 'visible';
				document.getElementById("reason").style.display = 'inline';
				document.getElementById("hint_txt_enquiry_nexa_testdrivereqreason").style.display = '';
	
				SecurityCheck(name, obj, hint);
			} else if (testdrive_yes == "0") {
				document.getElementById("reason").style.display = 'None';
				document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display = 'None';
			} else {
	
				document.getElementById("reason").style.display = 'None';
				document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display = 'None';
				document.getElementById("hint_txt_enquiry_nexa_testdrivereqreason").style.display = 'None';
				SecurityCheck(name, obj, hint);
			}
		}
	</script>

	<script language="JavaScript" type="text/javascript">
		function daysdiff(name, obj, hint) {
			var names = name.split(", ");
			var dt1 = names[0];
			var dt2 = names[1];
			var date1 = document.getElementById(dt1).value;
			var date2 = document.getElementById(dt2).value;
			var value = date1 + ", " + date2;
			var checked = '';
			var enquiry_id = GetReplace(document.form1.enquiry_id.value);
			var url = "../sales/enquiry-dash-check.jsp?";
			var param = "name=" + name + "&value=" + value + "&checked=" + checked + "&enquiry_id=" + enquiry_id;
			var str = "123";
			// 		showHintPost(url + param, str, param, hint);
			showHint(url + param, hint);
			setTimeout('RefreshHistory()', 1000);
		}
	
		function SecurityCheck(name, obj, hint) {
			var enquiry_id = GetReplace(document.form1.enquiry_id.value);
			var url = "../sales/enquiry-dash-check.jsp?";
			var dat = document.getElementById("txt_enquiry_date").value;
			var str = "123";
			var fromdate = "";
			if (name != "chk_enquiry_dms" && name != "chk_enquiry_avpresent" && name != "chk_enquiry_evaluation"
					&& name != "chk_enquiry_manager_assist" && name != "chk_enquiry_jlr_financeinterest"
					&& name != "chk_enquiry_jlr_highnetworth") {
				var value = GetReplace(obj.value);
			} else {
				if (obj.checked == true) {
					var value = "1";
				} else {
					var value = "0";
				}
			}
			if (name == "dr_enquiry_model_id") {
				var value = GetReplace(obj.value);
				var stage_id;
				if (value == 0) {
					stage_id = 1;
				} else {
					stage_id = 2;
				}
	
				var param = "name=" + name + "&value=" + value + "&enquiry_dat="
						+ dat + "&enquiry_id=" + enquiry_id + "&stage_id=" + stage_id;
			}
			
			if (name == "dr_enquiry_mb_currentcars") {
				var chks = document.getElementsByName('currentcars[]');
				var checkCount = 0;
				var text = new Array();
				for (var i = 0; i < chks.length; i++) {
					if (chks[i].checked) {
						var arlength = text.length;
						text[arlength] = chks[i].value;
					}
				}
				var value = text.join(",");
				var param = "name=" + name + "&value=" + value + "&enquiry_id=" + enquiry_id;
			}
			
			if (name == "porscheothervehicle") {
				var value = outputSelected(document.getElementById("porscheothervehicle").options);
				var param = "name=" + name + "&value=" + value + "&enquiry_id=" + enquiry_id;
			}
			if (name == "jlrcurrentvehicle") {
				var value = outputSelected(document.getElementById("jlrcurrentvehicle").options);
				var param = "name=" + name + "&value=" + value + "&enquiry_id=" + enquiry_id;
			}
			if (name == "jlrothermodelofinterest") {
				var value = outputSelected(document.getElementById("jlrothermodelofinterest").options);
				var param = "name=" + name + "&value=" + value + "&enquiry_id=" + enquiry_id;
			}
// console.log(value);
			if (name == "chk_enquiry_porsche_interest"
					|| name == "chk_enquiry_porsche_socialmediapref"
					|| name == "chk_enquiry_porsche_preferredcomm"
					|| name == "chk_enquiry_porsche_language" 
					|| name == "chk_enquiry_jlr_interest" ) {
				var chks = '';
				if (name == "chk_enquiry_porsche_interest") {
					chks = document.getElementsByName('porscheinterest[]');
				} else if (name == "chk_enquiry_porsche_socialmediapref") {
					chks = document.getElementsByName('porschesocialmediapref[]');
				}else if(name == "chk_enquiry_porsche_preferredcomm"){
					chks = document.getElementsByName('porscheprefcomm[]');
				} else if(name == "chk_enquiry_porsche_language"){
					chks = document.getElementsByName('porschelanguage[]');
				} else if(name == "chk_enquiry_jlr_interest"){
					chks = document.getElementsByName('jlrinterest[]');
				}
				var checkCount = 0;
				var text = new Array();
				for (var i = 0; i < chks.length; i++) {
					if (chks[i].checked) {
						var arlength = text.length;
						text[arlength] = chks[i].value;
					}
				}
				var value = text.join(",");
				var param = "name=" + name + "&value=" + value + "&enquiry_id=" + enquiry_id;
			}

			if (fromdate != "") {
				var param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&from_date=" + fromdate + "&enquiry_id=" + enquiry_id;
			} else {
				var param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&enquiry_id=" + enquiry_id;
			}
// 			console.log("name---"+name+" value---"+value+" param----"+param);
// 			console.log("hint---"+hint);
			// 		showHintPost(url + param, str, param, hint);
			showHint(url + param, hint);

		}
		
		
		function SecurityCheckSkoda(name, obj, hint){
			var enquiry_id = GetReplace(document.form1.enquiry_id.value);
			var url = "../sales/enquiry-dash-check.jsp?needassesment=skoda&enquiry_brand_id=11";
			var dat = document.getElementById("txt_enquiry_date").value;
			var value = GetReplace(obj.value);
			
			if (name == "chk_na_skoda_whatareyoulookingfor"){
				var chks = document.getElementsByName('whatareyoulookingfor[]');
				var text = new Array();
				for (var i = 0; i < chks.length; i++) {
					if (chks[i].checked) {
						var arlength = text.length;
						text[arlength] = chks[i].value;
					}
				}
				var value = text.join(",");
			}
			
			if (name == "dr_na_skoda_enquiry_currentcars") {
				 value = outputSelected(document.getElementById("porscheothervehicle").options);
				}
			
			var param = '&enquiry_id='+ enquiry_id +'&name='+ name + '&value=' +value;
			showHint(url + param, hint);
		}
		
		
		
		
		function SecurityDoubleCheck(name1, obj1, name2, obj2, hint) {
			var value1 = GetReplace(obj1.value);
			var value2 = GetReplace(obj2.value);
			var enquiry_id = GetReplace(document.form1.enquiry_id.value);
			var url = "../sales/enquiry-dash-check.jsp?";
			var param = "name1=" + name1 + "&value1=" + value1 + "&name2="
					+ name2 + "&value2=" + value2 + "&enquiry_id=" + enquiry_id;
			var str = "123";
			//alert("value1=="+value1+" n  value2=="+value2+" n enquiry_id=="+enquiry_id+" n name1=="+name1+"  n name2=="+name2);
			// 		showHintPost(url + param, str, param, hint);
			showHint(url + param, hint);
			setTimeout('RefreshHistory()', 1000);
		}

		function RefreshHistory() {
			var enquiry_id = document.form1.enquiry_id.value;
		}

		function StatusUpdate() {
			var lostcase1 = "", lostcase2 = "", lostcase3 = "";
			var status_id = document.getElementById('dr_enquiry_status_id').value;
			// 		alert(status_id);
			var status_desc = document
					.getElementById('txt_enquiry_status_desc').value;
			//alert(status_desc);
			lostcase1 = document.getElementById('dr_enquiry_lostcase1_id').value;
			lostcase2 = document.getElementById('dr_enquiry_lostcase2_id').value;
			lostcase3 = document.getElementById('dr_enquiry_lostcase3_id').value;
			var url = "../sales/enquiry-dash-check.jsp?";
			var param;
			var str = "123";
			var enquiry_id = document.form1.enquiry_id.value;
			param = "name=dr_enquiry_status_id&status_id=" + status_id
					+ "&status_desc=" + status_desc + "&lostcase1=" + lostcase1
					+ "&lostcase2=" + lostcase2 + "&lostcase3=" + lostcase3
					+ "&enquiry_id=" + enquiry_id;
			// 		showHintPost(url + param, str, param, "hint_dr_enquiry_status_id");
			showHint(url + param, "hint_dr_enquiry_status_id");
			setTimeout('RefreshHistory()', 1000);
		}

		function BuyerTypeCheck(name) {
			var buyertype_id = document
					.getElementById("dr_enquiry_buyertype_id").value;
			var existingvehicle = document
					.getElementById("txt_enquiry_existingvehicle").value;
			if (buyertype_id == "2" && existingvehicle == "") {
				alert("Enter existing vehicle make!");
			} else {
				SecurityCheck('dr_enquiry_buyertype_id', name,
						'hint_dr_enquiry_buyertype_id');
			}
		}

		function ExistingVehicleCheck(name) {
			var buyertype_id = document
					.getElementById("dr_enquiry_buyertype_id").value;
			var existingvehicle = document
					.getElementById("txt_enquiry_existingvehicle").value;
			if (buyertype_id == "2" && existingvehicle == "") {
				// 			alert("Enter existing vehicle make!");
			} else {
				SecurityCheck('txt_enquiry_existingvehicle', name,
						'hint_txt_enquiry_existingvehicle');
			}
		}

		function populateLostCase2(name, obj, hint) {
			//// var enquiry_id =  GetReplace(document.form1.enquiry_id.value);
			var enquiry_id = document.getElementById('enquiry_id').value; // Use this in all
			var value = obj.value;
			//alert(value);
			var url = "../sales/enquiry-dash-check.jsp?"
			var param = "name=dr_enquiry_lostcase1_id&value=" + value
					+ "&enquiry_id=" + enquiry_id;
			var str = "123";
			// 		showHintPost(url + param, str, param, hint);
			setTimeout("showHint('" + url + param + "','" + hint + "')", 500);
			// 				setTimeout('StatusUpdate()',1000);
		}

		function populateLostCase3(name, obj, hint) {
			//alert(name);
			// 			  var enquiry_id =  GetReplace(document.form1.enquiry_id.value);
			var enquiry_id = document.getElementById('enquiry_id').value;
			var value = obj.value;
			//alert(value);
			var status_id = document.getElementById('dr_enquiry_status_id').value;
			var url = "../sales/enquiry-dash-check.jsp?"
			var param = "name=dr_enquiry_lostcase2_id&value=" + value
					+ "&status_id=" + status_id + "&enquiry_id=" + enquiry_id;
			var str = "123";
			//alert(url+param);
			setTimeout("showHint('" + url + param + "', '" + hint + "')", 500);

			//setTimeout('StatusUpdate()',1000);    
		}

		function populateItem() {
			//alert("itemmm");
			// 			  var enquiry_id =  GetReplace(document.form1.enquiry_id.value);
			var enquiry_id = document.getElementById('enquiry_id').value;
			var model_id = document.getElementById('dr_enquiry_model_id').value;
			// alert('../sales/enquiry-dash-check.jsp?enquiry_model_id='+model_id+"&enquiry_id=" + enquiry_id,model_id);
			//var url = "../sales/enquiry-dash-check.jsp?";
			var param = "enquiry_model_id=" + model_id + "&enquiry_id="
					+ enquiry_id;
			var str = "123";
			// 		showHintPost('../sales/enquiry-dash-check.jsp?enquiry_model_id='
			// 				+ model_id + "&enquiry_id=" + enquiry_id, str, param,
			// 				'modelitem');
			showHint('../sales/enquiry-dash-check.jsp?enquiry_model_id='
					+ model_id + "&enquiry_id=" + enquiry_id, 'modelitem');
			//showHintPost('../sales/enquiry-dash-check.jsp?enquiry_model_id='+model_id+"&enquiry_id=" + enquiry_id,model_id,'modelitem');
		}

		function populateVariant() {

			// 			  var enquiry_id =  GetReplace(document.form1.enquiry_id.value);
			var enquiry_id = document.getElementById('enquiry_id').value;
			var preownedmodel_id = document
					.getElementById('dr_enquiry_preownedmodel_id').value;
			//alert("preownedmodel_id===" + preownedmodel_id + "enquiry_id=====" + enquiry_id);
			//// alert('../sales/enquiry-dash-check.jsp?oppr_model_id='+model_id+"&oppr_id=" + oppr_id,model_id);
			//var url = "../sales/enquiry-dash-check.jsp?";
			var param = "enquiry_preownedmodel_id=" + preownedmodel_id
					+ "&enquiry_id=" + enquiry_id;
			var str = "123";
			// 		showHintPost(
			// 				'../sales/enquiry-dash-check.jsp?enquiry_preownedmodel_id='
			// 						+ preownedmodel_id + "&enquiry_id=" + enquiry_id,
			// 				param, 'modelvariant');
			showHint(
					'../sales/enquiry-dash-check.jsp?enquiry_preownedmodel_id='
							+ preownedmodel_id + "&enquiry_id=" + enquiry_id,
					'modelvariant');

		}
	</script>

	<script language="JavaScript" type="text/javascript">
		function LoadEnquiryDash(tab) {
			var enquiry_id = document.getElementById("enquiry_id").value;
			var so_id = document.getElementById("txt_so_id").value;
// 			var customer_id = document.getElementById("txt_customer_id").value;
			if (tab == '3') {
				if (document.getElementById("tabs-3").innerHTML == "") {
					// 				showHintPost(
					// 						'enquiry-dash-check.jsp?crm_details=yes&enquiry_id='
					// 								+ enquiry_id, enquiry_id,
					// 						'crm_details=yes&enquiry_id=' + enquiry_id, 'tabs-3');
					showHint( '../sales/enquiry-dash-check.jsp?enquiry_preownedmodel_id='
									+ preownedmodel_id + "&enquiry_id=" + enquiry_id, 'modelvariant');
				}
			} else if (tab == '4') {
				if (document.getElementById("tabs-4").innerHTML == "") {
					// 				showHintPost(
					// 						'enquiry-dash-check.jsp?customer_details=yes&customer_id='
					// 								+ customer_id + '&enquiry_id=' + enquiry_id,
					// 						enquiry_id, 'customer_details=yes&customer_id='
					// 								+ customer_id + '&enquiry_id=' + enquiry_id,
					// 						'tabs-4');
					showHint( 'enquiry-dash-check.jsp?customer_details=yes&customer_id='
									+ customer_id + '&enquiry_id=' + enquiry_id, 'tabs-4');
				}
			} else if (tab == '5') {
				// 			showHintPost('enquiry-dash-check.jsp?doc_details=yes&enquiry_id='
				// 					+ enquiry_id, enquiry_id, 'doc_details=yes&enquiry_id='
				// 					+ enquiry_id, 'tabs-5');
				showHint('enquiry-dash-check.jsp?customer_details=yes&customer_id='
						+ customer_id + '&enquiry_id=' + enquiry_id, 'tabs-5');
			} else if (tab == '6') {
				if (document.getElementById("tabs-6").innerHTML == '') {
					// 				showHintPost(
					// 						'enquiry-dash-check.jsp?testdrive_details=yes&enquiry_id='
					// 								+ enquiry_id, enquiry_id,
					// 						'testdrive_details=yes&enquiry_id=' + enquiry_id,
					// 						'tabs-6');
					showHint( 'enquiry-dash-check.jsp?testdrive_details=yes&enquiry_id=' + enquiry_id, 'tabs-6');
				}
			} else if (tab == '7') {
				// 							if(document.getElementById("tabs-7").innerHTML==''){
				// 								showHintPost('enquiry-dash-check.jsp?quote_details=yes&enquiry_id='+enquiry_id, enquiry_id, 'quote_details=yes&enquiry_id='+enquiry_id, 'tabs-7');
				// 							}
			} else if (tab == '8') {
				if (document.getElementById("tabs-8").innerHTML == '') {
					// 				showHintPost(
					// 						'enquiry-dash-check.jsp?so_details=yes&enquiry_id='
					// 								+ enquiry_id, enquiry_id,
					// 						'so_details=yes&enquiry_id=' + enquiry_id, 'tabs-8');
					showHint('enquiry-dash-check.jsp?so_details=yes&enquiry_id=' + enquiry_id, 'tabs-8');
				}
			} else if (tab == '9') {
					showHint( 'enquiry-dash-check.jsp?invoice_details=yes&enquiry_id=' + enquiry_id, 'tabs-9');
			} else if (tab == '10') {
				// 			showHintPost(
				// 					'enquiry-dash-check.jsp?receipt_details=yes&enquiry_id='
				// 							+ enquiry_id, enquiry_id,
				// 					'receipt_details=yes&enquiry_id=' + enquiry_id, 'tabs-10');
				showHint('enquiry-dash-check.jsp?receipt_details=yes&so_id=' + so_id, 'tabs-10');
			} else if (tab == '11') {
				// 			showHintPost('enquiry-dash-check.jsp?history=yes&enquiry_id='
				// 					+ enquiry_id, enquiry_id, 'history=yes&enquiry_id='
				// 					+ enquiry_id, 'tabs-11');
				showHint('enquiry-dash-check.jsp?history=yes&enquiry_id=' + enquiry_id, 'tabs-11');
	
			}
		}
	</script>

	<script>
		// Enquiry Transfer Modal Window
		function enquiryTransfer() {
			var msg = "";
			if($("#dr_enquiry_brand_id").val()=='0'){
				msg += "Select Brand!";
			}
			if($("#dr_enquiry_branch_id").val()=='0'){
				msg += "<br>Select Branch!";
			}
			if($("#dr_enquiry_model_id").val()=='0'){
				msg += "<br>Select Model!";
			}
			if($("#dr_enquiry_item_id").val()=='0'){
				msg += "<br>Select Variant!";
			}
			if($("#dr_enquiry_team_id").val()=='0'){
				msg += "<br>Select Team!";
			}
			if($("#dr_enquiry_emp_id").val()=='0'){
				msg += "<br>Select Sales Consultant!<br>";
			}
			
			$("#errormsg").html(msg);
			
			if($("#errormsg").html() == ""){
				showHint( '../sales/enquiry-transfer-check.jsp?brand_id=' + $("#dr_enquiry_brand_id").val()
						+ '&branch_id=' + $("#dr_enquiry_branch_id").val()
						+ '&model_id='+$("#dr_enquiry_model_id").val()
						+ '&item_id=' + $("#dr_enquiry_item_id").val()
						+ '&team_id='+$("#dr_enquiry_team_id").val()
						+ '&executive_id='+$("#dr_enquiry_emp_id").val()
						+ '&enquiry_id='+$("#enquiry_id").val()
						+ '&button=' +$("#transfer").val() + '', 'enquirytransfer');
			}
		}
		
		function PopulateBranch() {
			var brand_id = document.getElementById('dr_enquiry_brand_id').value;
			showHint('../sales/enquiry-check.jsp?brand_id='+brand_id,'dr_enquiry_branch');
		}
		
		function PopulateModel() {
			var brand_id = document.getElementById('dr_enquiry_brand_id').value;
			showHint('../sales/enquiry-check.jsp?model=yes&brand_id='+brand_id,'dr_enquiry_model');
		}
	
		function PopulateItem() {
			var model_id = document.getElementById('dr_enquiry_model_id').value;
			showHint('../sales/enquiry-check.jsp?model_id='+model_id,'dr_enquiry_item');
		}
	
		function PopulateTeam(){
			var branch_id = document.getElementById('dr_enquiry_branch_id').value;
			showHint('../sales/enquiry-check.jsp?exe_transfer=yes&team=yes&branch_id='+branch_id,'dr_enquiry_team');	
		}
		
		function PopulateExecutive() {
			var team_id = document.getElementById('dr_enquiry_team_id').value;
			showHint('../sales/enquiry-check.jsp?exe_transfer=yes&team_id='+team_id,'dr_enquiry_emp');
		}
	</script>

	<!-- START OF TAGS CONFIGURATION -->

	<script type="text/javascript">
	
	//	THIS IS TO POPULATE VALUE IN TAG-CONTAINER AT THE TIME OF PAGE LOAD
		<%=mybean.tagcheck.PopulateTags(mybean.enquiry_customer_id, mybean.comp_id)%>
		
	///	THIS IS TO ADD TAGS IN TAG-CONTAINER ON CLICK FROM POPOVER
		<%=mybean.tagcheck.PopulateTagsJS( mybean.enquiry_customer_id, mybean.comp_id)%>
		
		function deleteTag(){
			$('#customer_tagclass > > input').tagsinput('remove', { 'value':  0, 'text': 'No Tag Selected' , 'continent': '#ff0000' });
		}
		
		function addNoTag(){
			$('#customer_tagclass > > input').tagsinput('add', { 'value':  0, 'text': 'No Tag Selected' , 'continent': '#ff0000' });
		}
		
		$(function() {
				$("#enquiry_tags").on('itemRemoved',function(){
						
						var url = "../customer/customer-tags-check.jsp?";
						
						var param = "update=yes&tags="+ $("#enquiry_tags").val()+"&customer_id="+<%=mybean.enquiry_customer_id%>+"&enquiry_id="+<%=mybean.enquiry_id%>;
						var hint = "hint_enquiry_tags";
						
						var param2 = "tags_content=yes&name=enquiry&customer_id=" + <%=mybean.enquiry_customer_id%>;
						var hint2 = "popover-content";
						
						setTimeout('showHint("'+ url + param2+'", "'+hint2+'")', 100);
						
						setTimeout('if($("#enquiry_tags").val()==""){ addNoTag(); }', 150);
						
						showHint(url + param, hint);
						
				});
			}); 
		    
			function tagcall(idname){
				
				var url = "../customer/customer-tags-check.jsp?";
				
				var param1 = "add=yes&name=enquiry&tags="+idname +"&customer_id="+<%=mybean.enquiry_customer_id%>+"&enquiry_id="+<%=mybean.enquiry_id%>;
				var hint1 = "hint_enquiry_tags";
				
				var param2 = "tags_content=yes&customer_id=" + <%=mybean.enquiry_customer_id%>;
				var hint2 = "popover-content";
				
				setTimeout('showHint("'+url + param2+'", "'+hint2+'")', 100);
				
				setTimeout('addTag('+idname+')', 150);
				
				setTimeout('showHint("'+url + param1+'", "'+hint1+'")', 50);
				
				deleteTag();
				
			}
			
			//this is provide property to the TAG POPOVER 
	// 		alert(navigator.appCodeName);
			
			var browser_name = "<%=request.getHeader("User-Agent")%>";
		// 		alert(tempname);

		if (browser_name.includes("Safari")) {
			// 			alert("Safari");
			$('#popover').popover({
				html : true,
				title : function() {
					return $("#popover-head").html();
				},
				//	 		    trigger:'onclick',
				content : function() {
					return $("#popover-content").html();
				}
			});

		} else {
			// 			alert("outher");
			$('#popover').popover({
				html : true,
				title : function() {
					return $("#popover-head").html();
				},
				trigger : 'onclick',
				content : function() {
					return $("#popover-content").html();
				}
			});
		}

		// 		 For Sending canned messages 

		function SendEmail(email_id) {
			var enquiry_id = "<%=mybean.enquiry_id%>";
			showHint( '../portal/canned-message-check.jsp?type=1&email=yes&email_id=' + email_id + '&value=' + enquiry_id, 'sentmsg');
		}

		function SendSMS(sms_id) {
			var enquiry_id = <%=mybean.enquiry_id%> ;
			showHint( '../portal/canned-message-check.jsp?type=1&sms=yes&sms_id=' + sms_id + '&value=' + enquiry_id, 'sentmsg');
		}

		function validatePreowned() {
			var comp_id = <%=mybean.comp_id%> ;
			var preowned_regno = $('#txt_preowned_regno').val();
			var preowned_manufyear = $('#dr_preowned_manufyear').val();
			var preowned_kms = $('#txt_preowned_kms').val();
			var preowned_ownership = $('#dr_preowned_ownership').val();
			var errormsg = '';
			var preowned_details = "";
			var new_enquiry_id = $('#preowned_new_enquiry_id').val();
			var preowned_variant = $('#preownedvariant2').val();

			if (preowned_variant == null) {
				preowned_variant = 0;
			}
			if (preowned_variant != 0) {
				if (preowned_regno == '') {
					errormsg += "<br><font color=red> Enter Registration No! </font>";
				}
				if (preowned_manufyear == 0) {
					errormsg += "<br><font color=red> Select Manuf. Year! </font>";
				}
				if (preowned_kms == '') {
					errormsg += "<br><font color=red> Enter Kms! </font>";
				}
				if (preowned_ownership == 0) {
					errormsg += "<br><font color=red> Select Ownership! </font>";
				}
				if (errormsg != '' && comp_id == '1009') {
					errormsg = '<div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button> <h4 class="modal-title" style=text-align:center>Error!</h4> </div> <div class="modal-body" id="preowned-modal"> <div class="row" > <div class="col-md-12" style=text-align:center> '
							+ errormsg
							+ ' </div> </div> </div> <div class="modal-footer"> <button type="button" class="btn default" data-dismiss="modal">Close</button> </div>';
					$(".modal-content").html(errormsg);
					$("#Hintclicktocall").modal('show');
				} else {
					preowned_details = "enquiry_id=" + new_enquiry_id
							+ "&preowned_regno=" + preowned_regno
							+ "&preowned_manufyear=" + preowned_manufyear
							+ "&preowned_kms=" + preowned_kms
							+ "&preowned_ownership=" + preowned_ownership;

					url = '<a id=submitpreowned href=../preowned/preowned-dash-add.jsp?'
							+ preowned_details
							+ ' data-target=#Hintclicktocall data-toggle=modal></a>';
					$('#linkpreowned').html(url);
					$('#submitpreowned').click();
				}
			} else {
				preowned_details = "enquiry_id=" + new_enquiry_id;
				url = '<a id=submitpreowned href=../preowned/preowned-dash-add.jsp?'
						+ preowned_details
						+ ' data-target=#Hintclicktocall data-toggle=modal></a>';
				$('#linkpreowned').html(url);
				$('#submitpreowned').click();
			}
		}
	</script>

	<!-- END OF TAGS CONFIGURATION -->
</body>
</html>
