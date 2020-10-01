<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Enquiry_Dash"
	scope="request" />
<jsp:useBean id="mybeanenqdash" class="axela.sales.Enquiry_Dash"
	scope="request" />
<jsp:useBean id="mybeanenqdashmethods"
	class="axela.sales.Enquiry_Dash_Methods" scope="request" />
<%
	mybean.doPost(request, response);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Enquiry Dash | Axelaauto</title>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">
<link href="css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="css/select2.min.css" rel="stylesheet" type="text/css" />

<style>
a {
	text-shadow: none;
	color: black;
	text-decoration: none;
}

b {
	color: #8E44AD;
}

strong {
	color: #fff;
}

center {
	color: #8E44AD;
	font-weight: bold;
}

span {
	color: red;
	/* 	width:100%; */
}

.select2-container {
	width: 100%;
}

.header-wrap {
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
	/* 	margin-left: 5px;  */
	/* 	margin-top: 45px;  */
}

.select2-container {
	width: 300px;
}

.label {
	font-size: 14px;
	color: #8E44AD;
}
</style>

</head>

<body onLoad="CheckEvaluation();">
	<div class="header-wrap">
		<div class="panel-heading"
			style="margin-bottom: 20px; background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05); padding: 18px;">
			<span class="panel-title">
				<center>
					<strong>Enquiry Dashboard</strong>
				</center>
			</span>
		</div>
	</div>
	<div class="col-md-6" style="margin-top: 40px; margin-left: 4px;">
		<form role="form" class="form-horizontal" name="form1" id="form1"
			method="post">
			<div class="form-body">
				<div class="form-group">
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1"><b>Enquiry ID: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="id" class="form-control"><%=mybean.enquiry_id%></label>
							<input name="enquiry_id" type="hidden" id="enquiry_id"
								value="<%=mybean.enquiry_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1"><b>Date: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="date" class="form-control"><%=mybean.date%></label> <input
								name="txt_enquiry_date" type="hidden" class="form-control"
								id="txt_enquiry_date" value="<%=mybean.date%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1"><b>Customer: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="customer" class="form-control"> <%=mybean.enquiry_customer_name%></label>
							<input type="hidden" class="form-control"
								name="txt_enquiry_customer_name" id="txt_enquiry_customer_name"
								value="<%=mybean.enquiry_customer_name%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1"><b>Contact: </b> </label>
						<div class="col-md-8 col-xs-7">
							<label for="customer" class="form-control"><%=mybean.title_desc%><%=mybean.contact_fname%>&nbsp;<%=mybean.contact_lname%></label>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4  control-label"
							for="form_control_1"><b>Mobile: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="mobile" class="form-control"><%=mybean.contact_mobile1%></label>
							<span style="position: absolute; left: 130px; top: 10px"
								onclick="callNo('<%=mybean.contact_mobile1%>')"> <img
								src="ifx/icon-call.png" class="img-responsive"></span> <input
								type="hidden" class="form-control" name="txt_contact_mobile1"
								id="txt_contact_mobile1" value="<%=mybean.contact_mobile1%>"
								size="32" maxlength="13" style="width: 250px">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4  control-label"
							for="form_control_1"><b>Branch: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="mobile" class="form-control"><%=mybean.branch_name%></label>
							<input type="hidden" class="form-control"
								value="<%=mybean.branch_name%>">
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>



<!-- Accodian Start -->
	<div class="portlet light" style="padding-left: 0px;padding-right: 0px;">
		<div class="portlet-body">
			<div class="panel-group accordion" id="accordion1">
				
<!-- 				Enquiry Details Accordian Strat -->
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_1"><strong>Enquiry Details</strong></a>
						</h4>
					</div>
					<div id="collapse_1_1" class="panel-collapse in">
						<div class="panel-body">
							<form role="form scrollable" class="form-horizontal"
								method="post" id="frm">
								<!--  						///////////////////////		Enquiry Details Strat   ////////////////////// -->
								<div class="form-body">
								<div style="padding-left: 8px; padding-right: 8px;">
										<div class="form-group form-md-line-input">
											<label for="form_control_1"><div class="hint"
													id="hint_txt_enquiry_exp_close_date"></div>Closing Date<span>*</span>:</label>
											<input type="" class="form-control"
												name="txt_enquiry_exp_close_date"
												id="txt_enquiry_exp_close_date"
												value="<%=mybean.closedate%>" size="12" maxlength="10"
												onfocusout="SecurityCheck('txt_enquiry_exp_close_date',this,'hint_txt_enquiry_exp_close_date');"
												onclick="datePicker('txt_enquiry_exp_close_date');" readonly>
										</div>


										<!-- 									<div class="form-group form-md-line-input"> -->
			<!-- 										<label for="form_control_1"><div class="hint" -->
			<!-- 												id="hint_txt_days_diff"></div>Days left: </label> <label -->
			<%-- 											for="form_control_1"><span style="color: #000"><%=mybean.days_diff%></span></label> --%>
			<!-- 									</div> -->
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_title"></div>Title<span>*</span>:</label> <input
					type="text" class="form-control" name="txt_enquiry_title"
					id="txt_enquiry_title" value="<%=mybean.enquiry_title%>" size="52"
					maxlength="255"
					onChange="SecurityCheck('txt_enquiry_title',this,'hint_txt_enquiry_title')" />

			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_desc"></div>Description:
				</label>
				<textarea class="form-control" name="txt_enquiry_desc" cols="50"
					rows="4" id="txt_enquiry_desc" value=""
					onChange="SecurityCheck('txt_enquiry_desc',this,'hint_txt_enquiry_desc')"><%=mybean.enquiry_desc%></textarea>

			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_value"></div>Budget:</label> <input type="tel"
					class="form-control" name="txt_enquiry_value"
					id="txt_enquiry_value" value="<%=mybean.enquiry_value%>"
					onKeyUp="toInteger('txt_enquiry_value','Value')"
					onchange="SecurityCheck('txt_enquiry_value',this,'hint_txt_enquiry_value')"
					size="10" maxlength="10" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_emp_id"></div> Sales Consultant<span>*</span>:</label>
				<select class="form-control" name="dr_enquiry_emp_id"
					id="dr_enquiry_emp_id">
					<!-- 					onChange="SecurityCheck('dr_enquiry_emp_id',this,'hint_dr_enquiry_emp_id');" -->
					<%=mybean.PopulateExecutive()%>
				</select>
			</div>

			<%
				if (!mybean.ref_emp_name.equals("")) {
			%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Reference Sales
					Consultant:&nbsp <span style="color: #000000"><%=mybean.ref_emp_name%></span>
				</label>
			</div>
			<%
				}
			%>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_model_id"></div>Model<span>*</span>:</label> <select
					<%if (mybean.enquiry_enquirytype_id.equals("2")) {%>
					disabled="disabled" <%}%> class="form-control"
					name="dr_enquiry_model_id" id="dr_enquiry_model_id"
					onChange="populateItem();SecurityCheck('dr_enquiry_model_id',this,'hint_dr_enquiry_model_id')">
					<%=mybean.PopulateModel()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_item_id"></div>Variant<span>*</span>: </label> <span
					id="modelitem"><select
					<%if (mybean.enquiry_enquirytype_id.equals("2")) {%>
					disabled="disabled" <%}%> class="form-control"
					name="dr_enquiry_item_id" id="dr_enquiry_item_id"
					onChange="SecurityCheck('dr_enquiry_item_id',this,'hint_dr_enquiry_item_id');">
						<%=mybean.PopulateItem()%>
				</select> </span>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_add_model_id"></div>Additional Model: </label> <span
					id="modelitem"><select class="form-control"
					name="dr_enquiry_add_model_id" id="dr_enquiry_add_model_id"
					onChange="SecurityCheck('dr_enquiry_add_model_id',this,'hint_dr_enquiry_add_model_id');">
						<%=mybean.PopulateAdditionalModel(mybean.comp_id)%>
				</select> </span>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_option_id"></div>Colour <%if(!mybean.branch_brand_id.equals("60")){ %><span>*</span>
						<%} %>: </label> <select
					class="form-control" name="dr_enquiry_option_id"
					id="dr_enquiry_option_id"
					onChange="SecurityCheck('dr_enquiry_option_id',this,'hint_dr_enquiry_option_id');">

					<%-- <%=mybean.PopulateColor()%> --%>
					<%=mybean.PopulateOption(mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_age_id"></div>Age<%if(!mybean.branch_brand_id.equals("60")){ %><span>*</span>
						<%} %>: </label> <select
					class="form-control" name="dr_enquiry_age_id"
					id="dr_enquiry_age_id"
					onChange="SecurityCheck('dr_enquiry_age_id',this,'hint_dr_enquiry_age_id');">
					<%-- <%=mybean.PopulateAge()%> --%>
					<%=mybean.PopulateAge(mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiryocc_id"></div>Occupation<span>*</span>:</label> <select
					class="form-control" name="dr_enquiry_occ_id"
					id="dr_enquiry_occ_id"
					onchange="SecurityCheck('dr_enquiry_occ_id',this,'hint_dr_enquiry_occ_id');">
					<%=mybean.PopulateOccupation()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_custtype_id"></div>Type of Customer<span>*</span>:</label>
				<select class="form-control" name="dr_enquiry_custtype_id"
					id="dr_enquiry_custtype_id"
					onchange="SecurityCheck('dr_enquiry_custtype_id',this,'hint_dr_enquiry_custtype_id');">
					<%=mybean.PopulateCustomerType()%>
				</select>
			</div>

			<%
				if (mybean.branch_brand_id.equals("55")) {
			%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_custtype_id"></div>Category:</label> <select
					class="form-control" name="dr_enquiry_enquirycat_id"
					id="dr_enquiry_enquirycat_id"
					onchange="SecurityCheck('dr_enquiry_enquirycat_id',this,'hint_dr_enquiry_enquirycat_id');">
					<%=mybeanenqdashmethods.PopulateCategory(
						mybean.enquiry_enquirycat_id, mybean.comp_id)%>
				</select>
			</div>
			<%
				}
			%>

			<%
				if (!mybean.branch_brand_id.equals("1")) {
			%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_custtype_id"></div>Corporate:</label> <select
					class="form-control" name="dr_enquiry_corporate_id"
					id="dr_enquiry_corporate_id"
					onchange="SecurityCheck('dr_enquiry_corporate_id',this,'hint_dr_enquiry_corporate_id');">
					<%=mybeanenqdashmethods.PopulateCorporate(
						mybean.branch_brand_id, mybean.enquiry_corporate_id,
						mybean.comp_id)%>
				</select>
			</div>
			<%
				}
			%>
	<%
															if (!mybean.branch_brand_id.equals("56") || !mybean.branch_brand_id.equals("60")) {
														%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_fuelallowance"></div>Fuel Allowance:</label> <input
					type="text" class="form-control" name="txt_enquiry_fuelallowance"
					id="txt_enquiry_fuelallowance"
					value="<%=mybean.enquiry_fuelallowance%>" size="10" maxlength="10"
					onKeyUp="toInteger('txt_enquiry_fuelallowance','Fuel')"
					onChange="SecurityCheck('txt_enquiry_fuelallowance',this,'hint_txt_enquiry_fuelallowance')">

			</div>
			<%
															}
														%>
		</div>
								
								</div>
							</form>
						</div>
					</div>
				</div>
				<!-- 				Enquiry Details Accordian End -->
				
				
				<!-- 				Preowned Enquiry Details Accordian Strat -->
				<% if (mybean.enquiry_enquirytype_id.equals("2")) { %>

				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_2"><strong>Pre-Owned</strong></a>
						</h4>
					</div>
					<div id="collapse_1_2" class="form-horizontal panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 20px;">
								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_dr_enquiry_fueltype_id"></div>
										Pre-Owned Model<font color="#ff0000">*</font>:
									</label> <select class="form-control select2" id="preownedvariant1"
										name="preownedvariant1"
										onchange="SecurityCheck('preownedvariant1',this,'hint_preownedvariant1')">
										<%=mybean.variantcheck
						.PopulateVariant(mybean.enquiry_preownedvariant_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"> Fuel Type: </label> <select
										class="form-control" name="dr_enquiry_fueltype_id"
										id="dr_enquiry_fueltype_id"
										onChange="SecurityCheck('dr_enquiry_fueltype_id',this,'hint_dr_enquiry_fueltype_id');">
										<%=mybean.PopulateFuelType()%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"> Pref. Reg.: </label> <select
										class="form-control" name="dr_enquiry_prefreg_id"
										id="dr_enquiry_prefreg_id"
										onChange="SecurityCheck('dr_enquiry_prefreg_id',this,'hint_dr_enquiry_prefreg_id');">
										<%=mybean.PopulatePrefReg()%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"> Present Car: </label> <input
										type="text" class="form-control" name="txt_enquiry_presentcar"
										id="txt_enquiry_presentcar"
										value="<%=mybean.enquiry_presentcar%>" size="32"
										maxlength="255"
										onchange="SecurityCheck('txt_enquiry_presentcar',this,'hint_txt_enquiry_presentcar')">
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"> Finance: </label> <select
										class="form-control" name="dr_enquiry_finance"
										id="dr_enquiry_finance"
										onChange="SecurityCheck('dr_enquiry_finance',this,'hint_dr_enquiry_finance');">
										<%=mybean.PopulateFinance()%>
									</select>
								</div>
							</div>


						</div>
					</div>
				</div>

				<%} %>
					<!-- 				Preowned Enquiry Details Accordian End -->
				
				
				<!-- 		  Understand your Customer Strat  -->
				
				<!-- 		  Understand your Customer For Maruti And Nexa Strat  -->
				
				
				<% if (mybean.branch_brand_id.equals("2") || mybean.branch_brand_id.equals("10")) { %>

				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_3"><strong>Understand Your
									Customer</strong></a>
						</h4>
					</div>
					<div id="collapse_1_3" class="form-horizontal panel-collapse collapse">
						<div class="panel-body">



							<div class="form-group form-md-line-input">
								<label for="form_control_1"><div class="hint"
										id="hint_txt_enquiry_exp_close_date"></div>Closing Date<span>*</span>:</label>
								<input type="" class="form-control"
									name="txt_enquiry_exp_close_date"
									id="txt_enquiry_exp_close_date" value="<%=mybean.closedate%>"
									size="12" maxlength="10"
									onfocusout="SecurityCheck('txt_enquiry_exp_close_date',this,'hint_txt_enquiry_exp_close_date');"
									onclick="datePicker('txt_enquiry_exp_close_date');" readonly>
							</div>

							<div style="padding-left: 20px;">
								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_buyertype_id"></div>Type of Buyer<span>*</span>:</label>
									<select class="form-control" name="dr_enquiry_buyertype_id"
										id="dr_enquiry_buyertype_id"
										onchange="SecurityCheck('dr_enquiry_buyertype_id',this,'hint_dr_enquiry_buyertype_id');">
										<%=mybeanenqdashmethods.PopulateBuyerType( mybean.comp_id, mybean.enquiry_buyertype_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_monthkms_id"></div>How many kilometers
										you drive in a month?<span>*</span></label> <select
										class="form-control" name="dr_enquiry_monthkms_id"
										id="dr_enquiry_monthkms_id" maxlength="4"
										onchange="SecurityCheck('dr_enquiry_monthkms_id',this,'hint_dr_enquiry_monthkms_id');">
										<%=mybean.PopulateMonthKms(mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_purchasemode_id"></div>What will be your
										mode of purchase?<span>*</span></label> <select class="form-control"
										name="dr_enquiry_monthkms_id" id="dr_enquiry_purchasemode_id"
										onchange="SecurityCheck('dr_enquiry_purchasemode_id',this,'hint_dr_enquiry_purchasemode_id');">
										<%=mybean.PopulatePurchaseMode(mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_income_id"></div>What is your approximate
										annual household income (Rs)?<span>*</span></label> <select
										class="form-control" name="dr_enquiry_income_id"
										id="dr_enquiry_income_id"
										onchange="SecurityCheck('dr_enquiry_income_id',this,'hint_dr_enquiry_income_id');">
										<%=mybean.PopulateIncome(mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_familymember_count"></div>How
										many members are there in your family?:
									</label> <input type="tel" class="form-control"
										name="txt_enquiry_familymember_count"
										id="txt_enquiry_familymember_count"
										value="<%=mybean.enquiry_familymember_count%>" size="5"
										maxlength="3"
										onchange="SecurityCheck('txt_enquiry_familymember_count',this,'hint_txt_enquiry_familymember_count')">
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_enquiry_expectation_id"></div>What is top most
										priority expectations from the car?<span>*</span></label> <select
										class="form-control" name="dr_enquiry_expectation_id"
										id="dr_enquiry_expectation_id"
										onchange="SecurityCheck('dr_enquiry_expectation_id',this,'hint_dr_enquiry_expectation_id');">
										<%=mybean.PopulateExpectation(mybean.comp_id)%>
									</select>
								</div>

								<div class="form-group form-md-line-input">
									<label for="form_control_1">
										<div class="hint" id="hint_txt_enquiry_familymember_count"></div>Any
										other car you have in mind?<span>*</span>
									</label> <input type="tell" class="form-control"
										name="txt_enquiry_othercar" id="txt_enquiry_othercar"
										value="<%=mybean.enquiry_othercar%>" size="32" maxlength="255"
										onchange="SecurityCheck('txt_enquiry_othercar',this,'hint_txt_enquiry_othercar')">
								</div>


								<div class="form-group form-md-line-input">
									<label for="form_control_1"> Corporate </label> <select
										id="dr_enquiry_corporate" name="dr_enquiry_corporate"
										class="form-control"
										onchange="SecurityCheck('dr_enquiry_corporate',this,'hint_dr_enquiry_corporate')">
										<%= mybeanenqdashmethods.PopulateEnquiryCorporate(mybean.enquiry_corporate, mybean.comp_id) %>
									</select>
								</div>

							</div>

						</div>
					</div>
				</div>

				<%} %>
				<!-- 		  Understand your Customer For Maruti And Nexa Strat  -->
				
				
				<!---- Start Hyundai Fields -->
		<% if (mybean.branch_brand_id.equals("6")) { %>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_4"><strong>Need Assessment (Hyundai)</strong></a>
						</h4>
					</div>
					<div id="collapse_1_4" class="form-horizontal panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 20px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_hyundai_chooseoneoption"></div>Please choose
					one option: </label> <select class="form-control"
					name="dr_enquiry_hyundai_chooseoneoption"
					id="dr_enquiry_hyundai_chooseoneoption"
					onchange="SecurityCheck('dr_enquiry_hyundai_chooseoneoption',this,'hint_dr_enquiry_hyundai_chooseoneoption');">
					<%=mybeanenqdashmethods.PopulateHyundaiChooseOneOption(
						mybean.enquiry_hyundai_chooseoneoption, mybean.comp_id)%>
				</select>
			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_hyundai_kmsinamonth"></div>How many kilometers
					you drive in a month?: </label> <select class="form-control"
					name="dr_enquiry_hyundai_kmsinamonth"
					id="dr_enquiry_hyundai_kmsinamonth"
					onChange="SecurityCheck('dr_enquiry_hyundai_kmsinamonth',this,'hint_dr_enquiry_hyundai_kmsinamonth')">
					<%=mybeanenqdashmethods.PopulateHyundaiKmsInAMonth(
						mybean.enquiry_hyundai_kmsinamonth, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_hyundai_membersinthefamily"></div>How many
					members are there in your family?: </label> <select class="form-control"
					name="dr_enquiry_hyundai_membersinthefamily"
					id="dr_enquiry_hyundai_membersinthefamily"
					onChange="SecurityCheck('dr_enquiry_hyundai_membersinthefamily',this,'hint_dr_enquiry_hyundai_membersinthefamily')">
					<%=mybeanenqdashmethods
						.PopulateHyundaiMembersInTheFamily(
								mybean.enquiry_hyundai_membersinthefamily,
								mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_hyundai_topexpectation"></div>What is your top
					most priority expectation from your car?: </label> <select
					class="form-control" name="dr_enquiry_hyundai_topexpectation"
					id="dr_enquiry_hyundai_topexpectation" class="selectbox"
					onChange="SecurityCheck('dr_enquiry_hyundai_topexpectation',this,'hint_dr_enquiry_hyundai_topexpectation')">
					<%=mybeanenqdashmethods.PopulateHyundaiTopExpectation(
						mybean.enquiry_hyundai_topexpectation, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_hyundai_finalizenewcar"></div>By when are you
					expecting to finalize your new car?: </label> <select class="form-control"
					name="dr_enquiry_hyundai_finalizenewcar"
					id="dr_enquiry_hyundai_finalizenewcar" class="selectbox"
					onChange="SecurityCheck('dr_enquiry_hyundai_finalizenewcar',this,'hint_dr_enquiry_hyundai_finalizenewcar')">
					<%=mybeanenqdashmethods.PopulateHyundaiFinalizeNewCar(
						mybean.enquiry_hyundai_finalizenewcar, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_hyundai_modeofpurchase"></div>What will be
					your mode of purchase?: </label> <select class="form-control"
					name="dr_enquiry_hyundai_modeofpurchase"
					id="dr_enquiry_hyundai_modeofpurchase"
					onChange="SecurityCheck('dr_enquiry_hyundai_modeofpurchase',this,'hint_dr_enquiry_hyundai_modeofpurchase')">
					<%=mybeanenqdashmethods.PopulateHyundaiModeOfPurchase(
						mybean.enquiry_hyundai_modeofpurchase, mybean.comp_id)%>
				</select>
			</div>



			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_hyundai_annualincome"></div>What is your
					appropriate annual household income (INR)?: </label> <select
					class="form-control" name="dr_enquiry_hyundai_annualincome"
					id="dr_enquiry_hyundai_annualincome"
					onChange="SecurityCheck('dr_enquiry_hyundai_annualincome',this,'hint_dr_enquiry_hyundai_annualincome')">
					<%=mybeanenqdashmethods.PopulateHyundaiAnnualIncome(
						mybean.enquiry_hyundai_annualincome, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_hyundai_othercars"></div>Which other cars are
					you considering?:</label> <input type="text" class="form-control"
					name="txt_enquiry_hyundai_othercars"
					id="txt_enquiry_hyundai_othercars"
					value="<%=mybean.enquiry_hyundai_othercars%>" size="52"
					maxlength="255"
					onchange="SecurityCheck('txt_enquiry_hyundai_othercars',this,'hint_txt_enquiry_hyundai_othercars')" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_hyundai_othercars"></div>Current Car(s):</label>
						 <input
					name="txt_enquiry_hyundai_currentcars" type="text"
					class="form-control" id="txt_enquiry_hyundai_currentcars"
					value="<%=mybean.enquiry_hyundai_currentcars%>" size="32"
					maxlength="255"
					onChange="SecurityCheck('txt_enquiry_hyundai_currentcars',this,'hint_txt_enquiry_hyundai_currentcars')" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_hyundai_othercars"></div>Date Of Birth:</label> <input
					name="txt_enquiry_hyundai_dob" type="text"
					class="form-control date-picker" data-date-format="dd/mm/yyyy"
					size="12" maxlength="10" id="txt_enquiry_hyundai_dob"
					value="<%=mybean.enquiry_hyundai_dob%>"
					onfocusout="SecurityCheck('txt_enquiry_hyundai_dob',this,'hint_txt_enquiry_hyundai_dob');"
					onclick="datePicker('txt_enquiry_hyundai_dob');" readonly />
			</div>
			<br>
		</div>

						
						</div>
					</div>
				</div>
			<%} %>	
			
			<!---- END Hyundai Fields -->
				
				<!---- Strat Ford Fields -->
				<% if (mybean.branch_brand_id.equals("7")) { %>
				
			<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_5"><strong>Need Assessment of Customer (Ford)</strong></a>
						</h4>
					</div>
					<div id="collapse_1_5" class="panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 20px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Type of Customer<span>*</span>:
				</label> <select class="form-control" name="dr_enquiry_ford_custtype_id"
					id="dr_enquiry_ford_custtype_id"
					onChange="SecurityCheck('dr_enquiry_ford_custtype_id',this,'hint_dr_enquiry_ford_custtype_id')">
					<%=mybeanenqdashmethods.PopulateFordCustomerType(
						mybean.enquiry_ford_customertype, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_ford_custtype_id"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Intention to purchase:</label> <select
					class="form-control" name="dr_enquiry_ford_intentionpurchase"
					id="dr_enquiry_ford_intentionpurchase"
					onChange="SecurityCheck('dr_enquiry_ford_intentionpurchase',this,'hint_dr_enquiry_ford_intentionpurchase')">
					<%=mybeanenqdashmethods.PopulateFordIntentionPurchase(
						mybean.enquiry_ford_intentionpurchase, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_ford_intentionpurchase"></div>
			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1">No. of Kms driven every day?:</label> <input
					name=txt_enquiry_ford_kmsdriven type="tel" class="form-control"
					id="txt_enquiry_ford_kmsdriven"
					value="<%=mybean.enquiry_ford_kmsdriven%>" size="32" maxlength="4"
					onChange="SecurityCheck('txt_enquiry_ford_kmsdriven ',this,'hint_txt_enquiry_ford_kmsdriven ')" />
				<div class="hint" id="hint_txt_enquiry_ford_kmsdriven "></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">New vehicle for self or someone
					else?:</label> <select class="form-control"
					name="dr_enquiry_ford_newvehfor" id="dr_enquiry_ford_newvehfor"
					onChange="SecurityCheck('dr_enquiry_ford_newvehfor',this,'hint_dr_enquiry_ford_newvehfor')">
					<%=mybeanenqdashmethods.PopulateFordNewVehFor(
						mybean.enquiry_ford_newvehfor, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_ford_newvehfor"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Amount of investment in new
					car?:</label> <input name=txt_enquiry_ford_investment type="tel"
					class="form-control" id="txt_enquiry_ford_investment"
					value="<%=mybean.enquiry_ford_investment%>" size="32" maxlength="9"
					onChange="SecurityCheck('txt_enquiry_ford_investment ',this,'hint_txt_enquiry_ford_investment')" />
				<div class="hint" id="hint_txt_enquiry_ford_investment"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Any specific colour choice?:</label> <select
					class="form-control" name="dr_enquiry_ford_colourofchoice"
					id="dr_enquiry_ford_colourofchoice"
					onChange="SecurityCheck('dr_enquiry_ford_colourofchoice',this,'hint_dr_enquiry_ford_colourofchoice')">
					<%=mybeanenqdashmethods.PopulateFordColourOfChoice(
						mybean.enquiry_ford_colourofchoice, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_ford_colourofchoice"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Cash / Finance?:</label> <select
					class="form-control" name="dr_enquiry_ford_cashorfinance"
					id="dr_enquiry_ford_cashorfinance"
					onChange="SecurityCheck('dr_enquiry_ford_cashorfinance',this,'hint_dr_enquiry_ford_cashorfinance')">
					<%=mybeanenqdashmethods.PopulateFordCashOrFinance(
						mybean.enquiry_ford_cashorfinance, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_ford_cashorfinance"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Which car you driving now?:</label> <input
					name=txt_enquiry_ford_currentcar type="text" class="form-control"
					id="txt_enquiry_ford_currentcar"
					value="<%=mybean.enquiry_ford_currentcar%>" size="32"
					maxlength="255"
					onChange="SecurityCheck('txt_enquiry_ford_currentcar',this,'hint_txt_enquiry_ford_currentcar')" />
				<div class="hint" id="hint_txt_enquiry_ford_currentcar"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Do you want to exchange your old
					car?:</label> <select class="form-control"
					name="dr_enquiry_ford_exchangeoldcar"
					id="dr_enquiry_ford_exchangeoldcar"
					onChange="SecurityCheck('dr_enquiry_ford_exchangeoldcar',this,'hint_dr_enquiry_ford_exchangeoldcar')">
					<%=mybeanenqdashmethods.PopulateFordExchangeOldCar(
						mybean.enquiry_ford_exchangeoldcar, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_ford_exchangeoldcar"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Which other cars you
					considering?:</label> <input name=txt_enquiry_ford_othercarconcideration
					type="text" class="form-control"
					id="txt_enquiry_ford_othercarconcideration"
					value="<%=mybean.enquiry_ford_othercarconcideration%>" size="32"
					maxlength="255"
					onChange="SecurityCheck('txt_enquiry_ford_othercarconcideration ',this,'hint_txt_enquiry_ford_othercarconcideration')" />
				<div class="hint" id="hint_txt_enqusiry_ford_othercarconcideration"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Vista Contract Number:</label> <input
					name="txt_enquiry_ford_vistacontractnumber" type="text"
					class="form-control" id="txt_enquiry_ford_vistacontractnumber"
					value="<%=mybean.enquiry_ford_vistacontractnumber%>" size="32"
					maxlength="20"
					onChange="SecurityCheck('txt_enquiry_ford_vistacontractnumber',this,'hint_txt_enquiry_ford_vistacontractnumber')" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">NSC Order Number:</label> <input
					name="txt_enquiry_ford_nscordernumber" type="text"
					class="form-control" id="txt_enquiry_ford_nscordernumber"
					value="<%=mybean.enquiry_ford_nscordernumber%>" size="32"
					maxlength="20"
					onChange="SecurityCheck('txt_enquiry_ford_nscordernumber',this,'hint_txt_enquiry_ford_nscordernumber')" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">QCS ID:</label> <input
					name="txt_enquiry_ford_qcsid" type="text" class="form-control"
					id="txt_enquiry_ford_qcsid" value="<%=mybean.enquiry_ford_qcsid%>"
					size="32" maxlength="20"
					onChange="SecurityCheck('txt_enquiry_ford_qcsid',this,'hint_txt_enquiry_ford_qcsid')" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">QPD ID: </label> <input
					name="txt_enquiry_ford_qpdid" type="text" class="form-control"
					id="txt_enquiry_ford_qpdid" value="<%=mybean.enquiry_ford_qpdid%>"
					size="32" maxlength="20"
					onChange="SecurityCheck('txt_enquiry_ford_qpdid',this,'hint_txt_enquiry_ford_qpdid')" />
				<div class="hint" id="hint_txt_enqusiry_ford_othercarconcideration"></div>
			</div>

		</div>
						</div>
					</div>
				</div>	
				
				<%} %>
				<!---- End Ford Fields -->
				
				<!---- Strat Skoda Fields -->
				<% if (mybean.branch_brand_id.equals("11")) { %>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion3"
								href="#collapse_3_2"><strong>Skoda Customer Need Assessment</strong></a>
						</h4>
					</div>
					<div id="collapse_3_2" class="panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 20px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_na_skoda_ownbusiness"></div>Own a Business<span>*</span>:
				</label>
				<select class="form-control" name="na_skoda_ownbusiness"
					id="na_skoda_ownbusiness"
					onchange="SecurityCheckSkoda('na_skoda_ownbusiness',this,'hint_na_skoda_ownbusiness');">
					<%=mybeanenqdashmethods.PopulateSkodaOwnBusiness(mybean.na_skoda_ownbusiness, mybean.comp_id)%>
				</select>
			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_na_skoda_companyname"></div>Company Name<span>*</span>:
				</label>
				<input	class="form-control " id="na_skoda_companyname"
					name="na_skoda_companyname"
					onchange="SecurityCheckSkoda('na_skoda_companyname',this,'hint_na_skoda_companyname')"
					 value="<%=mybean.na_skoda_companyname%>"/>
			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_skoda_contact_jobtitle"></div>Job Title<span>*</span>:
				</label>
				<input name="txt_skoda_contact_jobtitle" type="text" maxlength="255"
				class="form-control" id="txt_skoda_contact_jobtitle" size="35"
				onChange="SecurityCheckSkoda('txt_skoda_contact_jobtitle', this, 'hint_txt_skoda_contact_jobtitle')"
				value="<%=mybean.contact_jobtitle%>" />
			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_na_skoda_financerequired"></div>Finance Required<span>*</span>:
				</label>
				<select
				class="form-control " id="na_skoda_financerequired"
				name="na_skoda_financerequired"
				onchange="SecurityCheckSkoda('na_skoda_financerequired',this,'hint_na_skoda_financerequired')">
				<%=mybeanenqdashmethods.PopulateSkodaFinance(mybean.na_skoda_financerequired, mybean.comp_id)%>
				</select>
			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_skoda_enquiry_exp_close_date"></div>How soon is the purchase<span>*</span>:
				</label>
				<input name="txt_skoda_enquiry_exp_close_date" id="txt_skoda_enquiry_exp_close_date"
				class="form-control datepicker" type="text"
				value="<%=mybean.enquirydate%>" size="12" maxlength="10"
				onfocusout="SecurityCheckSkoda('txt_skoda_enquiry_exp_close_date',this,'hint_txt_skoda_enquiry_exp_close_date');"
				onclick="datePicker('txt_skoda_enquiry_exp_close_date');" readonly />
			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_na_skoda_enquiry_currentcars"></div>Current Cars<span>*</span>:
				</label>
					<select class="form-control select2" id="porscheothervehicle" name="porscheothervehicle" multiple
						onchange="SecurityCheckSkoda('dr_na_skoda_enquiry_currentcars',this,'hint_dr_na_skoda_enquiry_currentcars')">
						<%=mybean.variantcheck.PopulatePorscheOtherVehicle(mybean.enquiry_id, mybean.comp_id)%>
					</select>
			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_na_skoda_currentcarappxkmsrun"></div>Approximate kms run<span>*</span>:
				</label>
				<select
					class="form-control " id="na_skoda_currentcarappxkmsrun"
					name="na_skoda_currentcarappxkmsrun"
					onchange="SecurityCheckSkoda('na_skoda_currentcarappxkmsrun',this,'hint_na_skoda_currentcarappxkmsrun')">
					<%=mybeanenqdashmethods.PopulateSkodaAppxCurrentCarRun(mybean.na_skoda_currentcarappxkmsrun, mybean.comp_id)%>
				</select>
			</div>
			
			 <div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_na_skoda_ownbusiness"></div>What are you looking for in your car<span>*</span>:
				</label>
				<div class="table-responsive">
					<%=mybeanenqdashmethods.PopulateSkodaWhatAreYouLookingFor(mybean.na_skoda_whatareyoulookingfor, mybean.comp_id)%>
				</div>
			</div> 
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_na_skoda_numberoffamilymembers"></div>Number of Family Members<span>*</span>:
				</label>
				<select
					class="form-control " id="na_skoda_numberoffamilymembers"
					name="na_skoda_numberoffamilymembers"
					onchange="SecurityCheckSkoda('na_skoda_numberoffamilymembers',this,'hint_na_skoda_numberoffamilymembers')">
					<%=mybeanenqdashmethods.PopulateSkodaMembersInTheFamily(mybean.na_skoda_numberoffamilymembers, mybean.comp_id)%>
				</select>
			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_na_skoda_whowilldrive"></div>Who will drive the car<span>*</span>:
				</label>
				<select
					class="form-control " id="na_skoda_whowilldrive"
					name="na_skoda_whowilldrive"
					onchange="SecurityCheckSkoda('na_skoda_whowilldrive',this,'hint_na_skoda_whowilldrive')">
					<%=mybeanenqdashmethods.PopulateWhoWillDrive(mybean.na_skoda_whowilldrive, mybean.comp_id)%>
				</select>
			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_na_skoda_whoareyoubuyingfor"></div>Who are you buying the car for<span>*</span>:
				</label>
				<select
					class="form-control "
					id="na_skoda_whoareyoubuyingfor"
					name="na_skoda_whoareyoubuyingfor"
					onchange="SecurityCheckSkoda('na_skoda_whoareyoubuyingfor',this,'hint_na_skoda_whoareyoubuyingfor')">
					<%=mybeanenqdashmethods.PopulateWhoAreYouBuyingFor(mybean.na_skoda_whoareyoubuyingfor, mybean.comp_id)%>
				</select>
			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_na_skoda_newcarappxrun"></div>Approximately how many kms in a day will the car be run<span>*</span>:
				</label>
				<select
					class="form-control "
					id="na_skoda_newcarappxrun"
					name="na_skoda_newcarappxrun"
					onchange="SecurityCheckSkoda('na_skoda_newcarappxrun',this,'hint_na_skoda_newcarappxrun')">
					<%=mybeanenqdashmethods.PopulateSkodaAppxNewCarRun(mybean.na_skoda_newcarappxrun, mybean.comp_id)%>
				</select>
			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_na_skoda_wherewillbecardriven"></div>Where will the car be driven mostly<span>*</span>:
				</label>
				<select
					class="form-control "
					id="na_skoda_wherewillbecardriven"
					name="na_skoda_wherewillbecardriven"
					onchange="SecurityCheckSkoda('na_skoda_wherewillbecardriven',this,'hint_na_skoda_wherewillbecardriven')">
					<%=mybeanenqdashmethods.PopulateSkodaWhereWillBeDriven(mybean.na_skoda_wherewillbecardriven, mybean.comp_id)%>
				</select>
			</div>

</div>
						</div>
					</div>
				</div>
				
				
				<%} %>
				
				<!---- End Skoda Fields -->
				
				
				
				<!---- Strat Nexa Fields -->
				<% if (mybean.branch_brand_id.equals("10")) { %>
				
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_6"><strong>Customer Profile (Maruti-Nexa)</strong></a>
						</h4>
					</div>
					<div id="collapse_1_6" class="panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 20px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Gender: </label> <select
					class="form-control" name="dr_enquiry_nexa_gender"
					id="dr_enquiry_nexa_gender"
					onchange="SecurityCheck('dr_enquiry_nexa_gender',this,'hint_dr_enquiry_nexa_gender');">
					<%=mybeanenqdashmethods.PopulateEnquiryNexaGender(
						mybean.enquiry_nexa_gender, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_nexa_gender"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Beverage Choice: </label> <select
					class="form-control" name="dr_enquiry_nexa_beveragechoice"
					id="dr_enquiry_nexa_beveragechoice"
					onchange="SecurityCheck('dr_enquiry_nexa_beveragechoice',this,'hint_dr_enquiry_nexa_beveragechoice');">
					<%=mybeanenqdashmethods
						.PopulateEnquiryNexaBeveragechoice(
								mybean.enquiry_nexa_beveragechoice,
								mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_nexa_beveragechoice"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Interested in Autocard: </label> <select
					class="form-control" name="dr_enquiry_nexa_autocard"
					id="dr_enquiry_nexa_autocard"
					onchange="SecurityCheck('dr_enquiry_nexa_autocard',this,'hint_dr_enquiry_nexa_autocard');">
					<%=mybeanenqdashmethods.PopulateEnquiryNexaAutocard(
						mybean.enquiry_nexa_autocard, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_nexa_autocard"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Fuel Type: </label> <select
					class="form-control" name="dr_enquiry_nexa_fueltype"
					id="dr_enquiry_nexa_fueltype"
					onchange="SecurityCheck('dr_enquiry_nexa_fueltype',this,'hint_dr_enquiry_nexa_fueltype');">
					<%=mybeanenqdashmethods.PopulateEnquiryNexaFueltype(
						mybean.enquiry_nexa_fueltype, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_nexa_fueltype"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Specific requirement from the
					car: </label> <select class="form-control" name="dr_enquiry_nexa_specreq"
					id="dr_enquiry_nexa_specreq"
					onchange="SecurityCheck('dr_enquiry_nexa_specreq',this,'hint_dr_enquiry_nexa_specreq');">
					<%=mybeanenqdashmethods.PopulateEnquiryNexaSpecreq(
						mybean.enquiry_nexa_specreq, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_nexa_specreq"></div>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Test Drive Required: </label> <select
					class="form-control" name="dr_enquiry_nexa_testdrivereq"
					id="dr_enquiry_nexa_testdrivereq"
					onchange="TestdriveReq('dr_enquiry_nexa_testdrivereq',this,'hint_dr_enquiry_nexa_testdrivereq');">
					<%=mybeanenqdashmethods.PopulateEnquiryNexaTestdrivereq(
						mybean.enquiry_nexa_testdrivereq, mybean.comp_id)%>
				</select>
				<div class="hint" id="hint_dr_enquiry_nexa_testdrivereq"></div>
			</div>

			<div class="form-group form-md-line-input" id="reason">
				<label for="form_control_1">Reason:</label> <input type="text"
					class="form-control" name="txt_enquiry_nexa_testdrivereqreason"
					id="txt_enquiry_nexa_testdrivereqreason"
					value="<%=mybean.enquiry_nexa_testdrivereqreason%>" size="10"
					maxlength="255"
					onChange="SecurityCheck('txt_enquiry_nexa_testdrivereqreason',this,'hint_txt_enquiry_nexa_testdrivereqreason')" />
				<div class="hint" id="hint_txt_enquiry_nexa_testdrivereqreason"></div>

			</div>
		</div>
							
						
						</div>
					</div>
				</div>
				
				<%} %>
				
				<!---- End Nexa Fields -->
				
				
				
				<!-- 								Start MB fields -->
		<% if (mybean.branch_brand_id.equals("55")) { %>
		<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_7"><strong>Customer Profile (Mercedes Benz)</strong></a>
						</h4>
					</div>
					<div id="collapse_1_7" class="panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 20px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Occupation:
				</label> <select class="form-control" name="dr_title"
					id="dr_enquiry_mb_occupation"
					onChange="SecurityCheck('dr_enquiry_mb_occupation',this,'hint_dr_enquiry_mb_occupation')">
					<%=mybeanenqdashmethods.PopulateMBOccupation(
						mybean.enquiry_mb_occupation, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_carusage"></div>Car Usage
					Conditions:
				</label> <select class="form-control" name="dr_enquiry_mb_carusage"
					id="dr_enquiry_mb_carusage"
					onChange="SecurityCheck('dr_enquiry_mb_carusage',this,'hint_dr_enquiry_mb_carusage')">
					<%=mybeanenqdashmethods.PopulateMBCarUsageConditions(
						mybean.enquiry_mb_carusage, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_type"></div>Type:
				</label> <select class="form-control" name="dr_enquiry_mb_type"
					id="dr_enquiry_mb_type"
					onChange="SecurityCheck('dr_enquiry_mb_type',this,'hint_dr_enquiry_mb_type')">
					<%=mybeanenqdashmethods.PopulateMBType(
						mybean.enquiry_mb_type, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_drivingpattern"></div>Driving
					Pattern:
				</label> <select class="form-control" name="dr_enquiry_mb_drivingpattern"
					id="dr_enquiry_mb_drivingpattern"
					onChange="SecurityCheck('dr_enquiry_mb_drivingpattern',this,'hint_dr_enquiry_mb_drivingpattern')">
					<%=mybeanenqdashmethods.PopulateMBDrivingPattern(
						mybean.enquiry_mb_drivingpattern, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_income"></div>Income:
				</label> <select class="form-control" name="dr_enquiry_mb_income"
					id="dr_enquiry_mb_income"
					onChange="SecurityCheck('dr_enquiry_mb_income',this,'hint_dr_enquiry_mb_income')">
					<%=mybeanenqdashmethods.PopulateMBIncome(
						mybean.enquiry_mb_income, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_avgdriving"></div>Avg.
					Driving (kms/month):
				</label> <select class="form-control" name="dr_enquiry_mb_avgdriving"
					id="dr_enquiry_mb_avgdriving"
					onChange="SecurityCheck('dr_enquiry_mb_avgdriving',this,'hint_dr_enquiry_mb_avgdriving')">
					<%=mybeanenqdashmethods.PopulateMBAverageDriving(
						mybean.enquiry_mb_avgdriving, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_currentcars"></div>Presently
					Owned:
				</label>
				<%=mybeanenqdashmethods.PopulateMBCurrentCars(mybean.enquiry_mb_currentcars)%>
			</div>
		</div>

						</div>
					</div>
				</div>
 <%} %>
			<!-- 	End MB fields -->	
				
				
				<!-- Start Porsche Details -->
		<% if (mybean.branch_brand_id.equals("56")) { %>
		
		<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_8"><strong>Porsche Customer Need Assessement</strong></a>
						</h4>
					</div>
					<div id="collapse_1_8" class="panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 20px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Gender<font style="color: red">*</font>:
				</label> <select name="dr_enquiry_porsche_gender"
					id="dr_enquiry_porsche_gender" class="form-control"
					onChange="SecurityCheck('dr_enquiry_porsche_gender', this, 'hint_dr_enquiry_porsche_gender');">
					<%=mybeanenqdashmethods.PopulateEnquiryPorscheGender( mybean.enquiry_porsche_gender, mybean.comp_id)%>
				</select>
			</div>



			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Nationality:
				</label> <select name="dr_enquiry_porsche_nationality"
					id="dr_enquiry_porsche_nationality" class="form-control"
					onChange="SecurityCheck('dr_enquiry_porsche_nationality', this, 'hint_dr_enquiry_porsche_nationality');">
					<%=mybeanenqdashmethods.PopulateEnquiryPorscheNationality( mybean.enquiry_porsche_nationality, mybean.comp_id)%>
				</select>
			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Language<font style="color: red">*</font>:
				</label>
				<%=mybeanenqdashmethods.PopulateEnquiryPorscheLanguage( mybean.enquiry_porsche_language, mybean.comp_id)%>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Religion<font style="color: red">*</font>:
				</label> <select name="dr_enquiry_porsche_religion"
					id="dr_enquiry_porsche_religion" class="form-control"
					onChange="SecurityCheck('dr_enquiry_porsche_religion', this, 'hint_dr_enquiry_porsche_religion');">
					<%=mybeanenqdashmethods.PopulateEnquiryPorscheReligion( mybean.enquiry_porsche_religion, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Preferred
					Communication<font style="color: red">*</font>:
				</label>
				<%=mybeanenqdashmethods.PopulateEnquiryPorschePreferredCommunication( mybean.enquiry_porsche_preferredcomm, mybean.comp_id)%>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Social
					Media Preference<font style="color: red">*</font>:
				</label>
				<%=mybeanenqdashmethods.PopulateEnquiryPorscheSocialMediaPref( mybean.enquiry_porsche_socialmediapref, mybean.comp_id)%>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Marital
					Status<font style="color: red">*</font>:
				</label> <select name="dr_enquiry_porsche_maritalstatus"
					id="dr_enquiry_porsche_maritalstatus" class="form-control"
					onChange="SecurityCheck('dr_enquiry_porsche_maritalstatus', this, 'hint_dr_enquiry_porsche_maritalstatus');">
					<%=mybeanenqdashmethods.PopulateEnquiryPorscheMaritalStatus( mybean.enquiry_porsche_maritalstatus, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Spouse
					Name:
				</label> <input type="text" name="txt_enquiry_porsche_spousename"
					id="txt_enquiry_porsche_spousename" class="form-control"
					onChange="SecurityCheck('txt_enquiry_porsche_spousename', this, 'hint_txt_enquiry_porsche_spousename');"
					value='<%=mybean.enquiry_porsche_spousename%>' />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Does
					Spouse Drive:
				</label> <select name="dr_enquiry_porsche_spousedrive"
					id="dr_enquiry_porsche_spousedrive" class="form-control"
					onChange="SecurityCheck('dr_enquiry_porsche_spousedrive', this, 'hint_dr_enquiry_porsche_spousedrive');">
					<%=mybeanenqdashmethods.PopulateSpouseDriveStatus( mybean.enquiry_porsche_spousedrive, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Interests:
				</label>
				<%=mybeanenqdashmethods.PopulateEnquiryPorscheInsterest(mybean.enquiry_porsche_interest, mybean.comp_id) %>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Club
					Membership:
				</label> <input type='text' name="txt_enquiry_porsche_clubmembership"
					id="txt_enquiry_porsche_clubmembership" class="form-control"
					onChange="SecurityCheck('txt_enquiry_porsche_clubmembership', this, 'hint_txt_enquiry_porsche_clubmembership');"
					value='<%=mybean.enquiry_porsche_clubmembership%>' />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Interested
					In Financing<font style="color: red">*</font>:
				</label> <select name="dr_enquiry_porsche_financeoption"
					id="dr_enquiry_porsche_financeoption" class="form-control"
					onChange="SecurityCheck('dr_enquiry_porsche_financeoption', this, 'hint_dr_enquiry_porsche_financeoption');">
					<%=mybeanenqdashmethods.PopulateEnquiryPorscheFinance( mybean.enquiry_porsche_financeoption, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Interested
					In Insurance<font style="color: red">*</font>:
				</label> <select name="dr_enquiry_porsche_insuranceoption"
					id="dr_enquiry_porsche_insuranceoption" class="form-control"
					onChange="SecurityCheck('dr_enquiry_porsche_insuranceoption', this, 'hint_dr_enquiry_porsche_insuranceoption');">
					<%=mybeanenqdashmethods.PopulateEnquiryPorscheInsurance( mybean.enquiry_porsche_insuranceoption, mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Number
					Of Vehicle In House<font style="color: red">*</font>:
				</label> <input type='text' name="txt_enquiry_porsche_vehicleinhouse"
					id="txt_enquiry_porsche_vehicleinhouse" class="form-control"
					onChange="SecurityCheck('txt_enquiry_porsche_vehicleinhouse', this, 'hint_txt_enquiry_porsche_vehicleinhouse');"
					onKeyUp="toInteger('txt_enquiry_porsche_vehicleinhouse','Exprice')"
					value='<%=mybean.enquiry_porsche_vehicleinhouse%>' />
			</div>


			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_mb_occupation"></div>Current
					Cars<font style="color: red">*</font>:
				</label> <select class="form-control select2" id="porscheothervehicle"
					name="porscheothervehicle" multiple
					onchange="SecurityCheck('porscheothervehicle',this,'hint_porscheothervehicle')">
					<%=mybean.variantcheck.PopulatePorscheOtherVehicle(mybean.enquiry_id,mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_porsche_householdcount"></div>Person's
					In Household<font style="color: red">*</font>:
				</label> <input type='text' name="txt_enquiry_porsche_householdcount"
					id="txt_enquiry_porsche_householdcount" class="form-control"
					onChange="SecurityCheck('txt_enquiry_porsche_householdcount', this, 'hint_txt_enquiry_porsche_householdcount');"
					onKeyUp="toInteger('txt_enquiry_porsche_householdcount','Exprice')"
					value='<%=mybean.enquiry_porsche_householdcount%>' '
																	/>
			</div>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_porsche_householdcount"></div>DOB<font style="color: red">*</font>:
				</label> <input name="txt_enquiry_porsche_contact_dob" class="form-control"
					id="txt_enquiry_porsche_contact_dob"
					value="<%=mybean.enquiry_porsche_contact_dob%>"
					onfocusout="SecurityCheck('txt_enquiry_porsche_contact_dob',this,'hint_txt_enquiry_porsche_contact_dob');"
					onclick="datePicker('txt_enquiry_porsche_contact_dob');" readonly>
			</div>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_porsche_householdcount"></div>Anniversary<font style="color: red">*</font>:
				</label> <input name="txt_enquiry_porsche_contact_anniversary"
					class="form-control" id="txt_enquiry_porsche_contact_anniversary"
					value="<%=mybean.enquiry_porsche_contact_anniversary%>"
					onfocusout="SecurityCheck('txt_enquiry_porsche_contact_anniversary',this,'hint_txt_enquiry_porsche_contact_anniversary');"
					onclick="datePicker('txt_enquiry_porsche_contact_anniversary');"
					readonly>
			</div>
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Industry:
				</label> <select name="dr_enquiry_porsche_industry"
					id="dr_enquiry_porsche_industry" class="form-control"
					onChange="SecurityCheck('dr_enquiry_porsche_industry', this, 'hint_enquiry_porsche_industry');">
					<%=mybeanenqdashmethods.PopulateEnquiryPorscheIndustry( mybean.enquiry_porsche_industry, mybean.comp_id)%>
				</select>
			</div>


			<br>
			<br>
		</div>
						</div>
					</div>
				</div>
		
		<% }%>
			<!-- End Porsche Details -->	
			
			<!-- Start JLR Details  -->
			<% if (mybean.branch_brand_id.equals("60")) { %>
			
			
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_9"><strong>JLR Customer Need Assessement</strong></a>
						</h4>
					</div>
					<div id="collapse_1_9" class=" form-horizontal panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 20px;">
		
		<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Employment Status:
				</label> <select name="dr_enquiry_jlr_employmentstatus" id="dr_enquiry_jlr_employmentstatus" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_jlr_employmentstatus', this, 'hint_dr_enquiry_jlr_employmentstatus');">
																		<%=mybeanenqdashmethods.PopulateJLREmploymentStatus(mybean.enquiry_jlr_employmentstatus, mybean.comp_id)%>
																	</select>
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Industry:
				</label>
				<select name="dr_enquiry_jlr_industry" id="dr_enquiry_jlr_industry" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_jlr_industry', this, 'hint_dr_enquiry_jlr_industry');">
																		<%=mybeanenqdashmethods.PopulateEnquiryJLRIndustry(mybean.enquiry_jlr_industry, mybean.comp_id)%>
																	</select>
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Birthday:
				</label>
				<input name="txt_enquiry_jlr_birthday" id="txt_enquiry_jlr_birthday"
																		value="<%=mybean.enquiry_jlr_birthday%>" class="form-control" type="text"
																		size="12" maxlength="10"
																			onfocusout="SecurityCheck('txt_enquiry_jlr_birthday', this, 'hint_txt_enquiry_jlr_birthday');"
																		onclick="datePicker('txt_enquiry_jlr_birthday');" readonly />
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Gender:
				</label>
				<select name="dr_enquiry_jlr_gender" id="dr_enquiry_jlr_gender" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_jlr_gender', this, 'hint_dr_enquiry_jlr_gender');">
																		<%=mybeanenqdashmethods.PopulateEnquiryJLRGender(mybean.enquiry_jlr_gender, mybean.comp_id)%>
																	</select>
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Occupation:
				</label>
				<select name="dr_enquiry_jlr_occupation"
																		id="dr_enquiry_jlr_occupation" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_jlr_occupation',this,'hint_dr_enquiry_jlr_occupation');">
																		<%=mybeanenqdashmethods.PopulateJLROccupation(mybean.enquiry_jlr_occupation, mybean.comp_id)%>
																	</select>
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Current Vehicles:
				</label>
				<select class="form-control select2" id="jlrcurrentvehicle" name="jlrcurrentvehicle" multiple
																		onchange="SecurityCheck('jlrcurrentvehicle',this,'hint_jlrcurrentvehicle')">
																		<%=mybean.variantcheck.PopulateJLRCurrentCars(mybean.enquiry_id, mybean.comp_id)%>
																	</select>
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Other Model Interested:
				</label>
				<select class="form-control select2" id="jlrothermodelofinterest" name="jlrothermodelofinterest" multiple
																		onchange="SecurityCheck('jlrothermodelofinterest',this,'hint_jlrothermodelofinterest')">
																		<%=mybean.variantcheck.PopulateJLROtherModel(mybean.enquiry_id, mybean.comp_id)%>
																	</select>
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Finance Interest:&nbsp 
					 <input type="checkbox" name="chk_enquiry_jlr_financeinterest" id="chk_enquiry_jlr_financeinterest"
																	<%=mybean.PopulateCheck(mybean.enquiry_jlr_financeinterest)%>
																	onclick="SecurityCheck('chk_enquiry_jlr_financeinterest',this,'hint_chk_enquiry_jlr_financeinterest')">
				</label>
				
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>High Net Worth: &nbsp<input type="checkbox" name="chk_enquiry_jlr_highnetworth" id="chk_enquiry_jlr_highnetworth"
																	<%=mybean.PopulateCheck( mybean.enquiry_jlr_highnetworth)%>
																	onclick="SecurityCheck('chk_enquiry_jlr_highnetworth',this,'hint_chk_enquiry_jlr_highnetworth')">
				</label>
				
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>No. of Children: &nbsp<input type='text' name="txt_enquiry_jlr_noofchildren" id="txt_enquiry_jlr_noofchildren" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_jlr_noofchildren', this, 'hint_txt_enquiry_jlr_noofchildren');"
																		onKeyUp="toInteger('txt_enquiry_jlr_noofchildren','Exprice');" size="10" maxlength="3"
																		value='<%=mybean.enquiry_jlr_noofchildren%>' />
				</label>
				
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>No. of People in Household:
				</label>
				<input type='text' name="txt_enquiry_jlr_noofpeopleinhousehold" id="txt_enquiry_jlr_noofpeopleinhousehold" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_jlr_noofpeopleinhousehold', this, 'hint_txt_enquiry_jlr_noofpeopleinhousehold');"
																		onKeyUp="toInteger('txt_enquiry_jlr_noofpeopleinhousehold','Exprice');" size="10" maxlength="3"
																		value='<%=mybean.enquiry_jlr_noofpeopleinhousehold%>' />
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Household Income:
				</label>
				<input type='text' name="txt_enquiry_jlr_householdincome" id="txt_enquiry_jlr_householdincome" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_jlr_householdincome', this, 'hint_txt_enquiry_jlr_householdincome');"
																		onKeyUp="toInteger('txt_enquiry_jlr_householdincome','Exprice');" size="15" maxlength="9"
																		value='<%=mybean.enquiry_jlr_householdincome%>' />
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Annual Revenue:
				</label>
				<input type='text' name="txt_enquiry_jlr_annualrevenue" id="txt_enquiry_jlr_annualrevenue" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_jlr_annualrevenue', this, 'hint_txt_enquiry_jlr_annualrevenue');"
																		onKeyUp="toInteger('txt_enquiry_jlr_annualrevenue','Exprice');" size="15" maxlength="9"
																		value='<%=mybean.enquiry_jlr_annualrevenue%>' />
			</div>
			
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>No. of Employees:
				</label>
				<input type='text' name="txt_enquiry_jlr_noofemployees" id="txt_enquiry_jlr_noofemployees" class="form-control"
																		onChange="SecurityCheck('txt_enquiry_jlr_noofemployees', this, 'hint_txt_enquiry_jlr_noofemployees');"
																		onKeyUp="toInteger('txt_enquiry_jlr_noofemployees','Exprice')" size="15" maxlength="9"
																		value='<%=mybean.enquiry_jlr_noofemployees%>' />
			</div>
			
			
				<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Account Type:
				</label>
				<select name="dr_enquiry_jlr_accounttype"
																		id="dr_enquiry_jlr_accounttype" class="form-control"
																		onChange="SecurityCheck('dr_enquiry_jlr_accounttype',this,'hint_dr_enquiry_jlr_accounttype');">
																		<%=mybeanenqdashmethods.PopulateJLRAccountType(mybean.enquiry_jlr_accounttype, mybean.comp_id)%>
																	</select>
			</div>
			
			
			
		                <div class="form-group form-md-line-input" style="margin-right: 20px">
									<label for="form_control_1">Enquiry Status:<span style="color: #000"><%=mybean.enquiry_jlr_enquirystatus%></span></label>
												<div class="hint" id="hint_dr_enquiry_jlr_status"></div>
						</div>	
			<div class="form-group form-md-line-input" style="margin-right: 20px">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_porsche_industry"></div>Interests:
				</label>
				<div class="table-responsive">
				<%=mybeanenqdashmethods.PopulateEnquiryJLRInsterest(mybean.enquiry_jlr_interests, mybean.comp_id) %>
				</div>
			</div>
		</div>

						
						</div>
					</div>
				</div>
			
			
			<%} %>
			<!-- End JLR Details  -->
			
	<!-- 		  Understand your Customer End  -->		
			
			
			<!-- Start Exchange Details  -->
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_10"><strong>Exchange Details</strong></a>
						</h4>
					</div>
					<div id="collapse_1_10" class="form-horizontal panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 8px; padding-right: 8px;">

			<div class="container-fluid">
				<br>
				<div class="form-group">
						<div class="form-group form-md-line-input col-md-6 col-xs-12">
				<label c="form_control_1"> Trade-In
								Model:</label>
						<!-- 												</div> -->
						<div class="col-md-8">
							<select class="form-control select2" id="preownedvariant2"
								name="preownedvariant2"
								onchange="SecurityCheck('preownedvariant2',this,'hint_preownedvariant2')">
								<%=mybean.variantcheck
						.PopulateVariant(mybean.enquiry_tradein_preownedvariant_id)%>

							</select>
							<div class="hint" id="hint_preownedvariant2"></div>
						</div>
						<br>
					</div>
					<center>
						<span id="addEvalBut"> <a class="btn1"
							onclick="return validatePreowned();" style="color: white;">Add
								Evaluation </a> <input id="preowned_new_enquiry_id" type="hidden"
							value="<%=mybean.enquiry_id%>" /> <span id='linkpreowned'><a
								id="submitpreowned" href="../preowned/preowned-dash-add.jsp"
								data-target="#Hintclicktocall" data-toggle="modal"></a></span>
						</span>
					</center>
					<!--                                                        </div>  -->
				</div>
			</div>

			<!-- 								Start Pre-owned -->
			<div class="form-group form-md-line-input">
				<label c="form_control_1">
					<div class="hint" id="hint_txt_enquiry_kms"></div>Sub Variant:
				</label> <input type="text" class="form-control"
					name="txt_enquiry_sub_variant" id="txt_enquiry_sub_variant"
					value="<%=mybean.preowned_sub_variant%>" size="10" maxlength="10"
					readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_kms"></div>Fuel Type:
				</label> <input type="text" class="form-control" name="txt_enquiry_fueltype"
					id="txt_enquiry_fueltype" value="<%=mybean.fueltype_name%>"
					size="10" maxlength="10" readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_kms"></div>Exterior:
				</label> <input type="text" class="form-control" name="txt_enquiry_exterior"
					id="txt_enquiry_exterior" value="<%=mybean.extcolour_name%>"
					size="10" maxlength="10" readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_kms"></div>Interior:
				</label> <input type="text" class="form-control" name="txt_enquiry_interior"
					id="txt_enquiry_interior" value="<%=mybean.intcolour_name%>"
					size="10" maxlength="10" readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_kms"></div>Registration No.<%if(mybean.comp_id.equals("1009")){ %><span>*</span>
					<%} %>:
				</label> <input id="txt_preowned_regno" name="txt_preowned_regno"
					type="text" class="form-control" value="<%=mybean.preowned_regno%>"
					<%if(!mybean.preowned_enquiry_id.equals("0")){  %> readonly <%} %> />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_kms"></div>Manuf. Year<%if(mybean.comp_id.equals("1009")){ %><span>*</span>
					<%} %>:
				</label>
				<%if(mybean.preowned_enquiry_id.equals("0")){ %>
				<select id="dr_preowned_manufyear" name="dr_preowned_manufyear"
					class="form-control">
					<%=mybean.PopulatePreownedManufYear()%>
				</select>
				<%}else{ %>
				<input id="txt_preowned_regno" name="txt_preowned_regno" type="text"
					class="form-control" value="<%=mybean.preowned_regno%>" readonly />
				<%} %>

			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint"></div>Kms<%if(mybean.comp_id.equals("1009")){ %><span>*</span>
					<%} %>:
				</label><input id="txt_preowned_kms" name="txt_preowned_kms"
					onKeyUp="toPhone('txt_preowned_kms','Kms')" type="text"
					class="form-control" value="<%=mybean.preowned_kms%>"
					<%if(!mybean.preowned_enquiry_id.equals("0")){ %> readonly <%} %> />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint"></div>Foreclosure Amount:
				</label> <input type="text" class="form-control" name="txt_enquiry_fcamt"
					id="txt_enquiry_fcamt" value="<%=mybean.preowned_fcamt%>" size="10"
					maxlength="10" readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_kms"></div>Ownership<%if(mybean.comp_id.equals("1009")){ %><span>*</span>
					<%} %>:
				</label>
				<%if(mybean.preowned_enquiry_id.equals("0")){ %>
				<select id="dr_preowned_ownership" name="dr_preowned_ownership"
					class="form-control">
					<%=mybean.PopulatePreOwnedOwnership(mybean.comp_id)%>
				</select>

				<%} else{ %>
				<input id="txt_preowned_kms" name="txt_preowned_kms"
					onKeyUp="toPhone('txt_preowned_kms','Kms')" type="text"
					class="form-control" value="<%=mybean.preowned_kms%>" readonly />
				<%} %>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_kms"></div>Expected Price:
				</label> <input type="text" class="form-control" name="txt_enquiry_expected"
					id="txt_enquiry_expected"
					value="<%=mybean.preowned_expectedprice%>" size="10" maxlength="10"
					readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_kms"></div>Offered Price:
				</label> <input type="text" class="form-control" name="txt_enquiry_offered"
					id="txt_enquiry_offered" value="<%=mybean.eval_offered_price%>"
					size="10" maxlength="10" readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_kms"></div>Evaluation Time:
				</label> <input type="text" class="form-control"
					name="txt_enquiry_evaluationtime" id="txt_enquiry_evaluationtime"
					value="<%=mybean.eval_entry_date%>" size="10" maxlength="10"
					readonly>
			</div>

		</div>

						
						</div>
					</div>
				</div>
			<!-- End Exchange Details  -->
			
			
			<!-- Start Customer Details  -->
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_11"><strong>Customer</strong></a>
						</h4>
					</div>
					
					<div id="collapse_1_11" class=" form-horizontal panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 8px; padding-right: 8px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1"
					value="<%=mybean.enquiry_customer_name%>"><div class="hint"
						id="hint_txt_enquiry_customer_name"></div>Customer: </label> <input
					type="text" class="form-control" name="txt_enquiry_customer_name"
					id="txt_enquiry_customer_name" size="32" maxlength="255"
					value="<%=mybean.enquiry_customer_name%>"
					onchange="SecurityCheck('txt_enquiry_customer_name',this,'hint_txt_enquiry_customer_name')">
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_title"></div>Contact<span>*</span>:</label> <select
					class="form-control" name="dr_title" id="dr_title"
					onChange="SecurityCheck('dr_title',this,'hint_dr_title')">
					<%=mybean.PopulateContactTitle()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_contact_fname"></div>First Name: </label> <input type="text"
					class="form-control" name="txt_contact_fname"
					id="txt_contact_fname" value="<%=mybean.contact_fname%>" size="32"
					maxlength="255"
					onChange="SecurityCheck('txt_contact_fname',this,'hint_txt_contact_fname')">

			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_contact_lname"></div>Last Name: </label> <input type="text"
					class="form-control" name="txt_contact_lname"
					id="txt_contact_lname" value="<%=mybean.contact_lname%>" size="32"
					maxlength="255"
					onchange="SecurityCheck('txt_contact_lname',this,'hint_txt_contact_lname')">

			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_contact_mobile1"></div>Mobile 1:</label> <input type="tel"
					class="form-control" name="txt_contact_mobile1"
					id="txt_contact_mobile1" value="<%=mybean.contact_mobile1%>"
					size="32" maxlength="13"
					onchange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')">
				(91-9999999999)


			</div>
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_contact_mobile2"></div>Mobile 2:</label> <input type="tel"
					class="form-control" name="txt_contact_mobile2"
					id="txt_contact_mobile2" value="<%=mybean.contact_mobile2%>"
					size="32" maxlength="13"
					onchange="SecurityCheck('txt_contact_mobile2',this,'hint_txt_contact_mobile2')">


			</div>
			(91-9999999999)
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_contact_phone1"></div>Phone 1:
				</label> <input type="tel" class="form-control" name="txt_contact_phone1"
					id="txt_contact_phone1" value="<%=mybean.contact_phone1%>"
					size="32" maxlength="15"
					onchange="SecurityCheck('txt_contact_phone1',this,'hint_txt_contact_phone1')">

			</div>
			(91-080-33333333)
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_contact_phone2"></div>Phone 2:
				</label> <input type="tel" class="form-control" name="txt_contact_phone2"
					id="txt_contact_phone2" value="<%=mybean.contact_phone2%>"
					size="32" maxlength="15"
					onchange="SecurityCheck('txt_contact_phone2',this,'hint_txt_contact_phone2')">

			</div>
			(91-080-33333333)
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_contact_email1"></div>Email 1:
				</label> <input type="text" class="form-control" name="txt_contact_email1"
					id="txt_contact_email1" value="<%=mybean.contact_email1%>"
					size="35" maxlength="100"
					onchange="SecurityCheck('txt_contact_email1',this,'hint_txt_contact_email1')">

			</div>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_contact_email2"></div>Email 2:
				</label> <input type="text" class="form-control" name="txt_contact_email2"
					id="txt_contact_email2" value="<%=mybean.contact_email2%>"
					size="35" maxlength="100"
					onchange="SecurityCheck('txt_contact_email2',this,'hint_txt_contact_email2')">

			</div>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_contact_address"></div>Address<span>*</span>:
				</label> <input type="text" class="form-control" name="txt_contact_address"
					id="txt_contact_address" value="<%=mybean.contact_address%>"
					cols="40" rows="4"
					onchange="SecurityCheck('txt_contact_address',this,'hint_txt_contact_address')">

			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_contact_pin"></div>Pin/Zip<span>*</span>:</label> <input
					type="tel" class="form-control" name="txt_contact_pin"
					id="txt_contact_pin" value="<%=mybean.contact_pin%>"
					onfocusout="SecurityCheck('txt_contact_pin',this,'hint_txt_contact_pin');"
					onKeyUp="toInteger('txt_contact_pin','Pin')" size="10"
					maxlength="6"> <br />
			</div>
		</div>

						
						</div>
					</div>
				</div>
			
			<!-- End Customer Details  -->
			
			<!-- Status Details Start  -->
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle accordion-toggle-styled collapsed"
								data-toggle="collapse" data-parent="#accordion1"
								href="#collapse_1_12"><strong>Status</strong></a>
						</h4>
					</div>
					
					<div id="collapse_1_12" class="form-horizontal panel-collapse collapse">
						<div class="panel-body">
							<div style="padding-left: 8px; padding-right: 8px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Stage: </label> <%=mybean.stage_name%>
			</div>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Priority:</label> 
				<span	style="color: #000"><%=mybean.priorityenquiry_desc%> (<%=mybean.priorityenquiry_duehrs%>)</span></label>
			</div>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Status<span>*</span>:
				</label> <select class="form-control" name="dr_enquiry_status_id"
					id="dr_enquiry_status_id" onChange="StatusUpdate();">
					<%=mybean.PopulateStatus()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1" value="<%=mybean.enquiry_status_desc%>"><div
						class="hint" id="hint_txt_enquiry_status_desc"></div> Status
					comments<span>*</span>: </label>
				<textarea class="form-control" rows="1"
					name="txt_enquiry_status_desc" id="txt_enquiry_status_desc"
					onKeyUp="charcount('txt_enquiry_status_desc', 'span_txt_enquiry_status_desc','<font color=red>({CHAR} characters left)</font>', '1000')"
					onchange="SecurityCheck('txt_enquiry_status_desc',this,'hint_txt_enquiry_status_desc');StatusUpdate();"><%=mybean.enquiry_status_desc%></textarea>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_lostcase1_id"></div>Lost Case 1<span>*</span>:</label>
				<span id="span_lostcase1"> <select class="form-control"
					name="dr_enquiry_lostcase1_id" id="dr_enquiry_lostcase1_id"
					onchange="populateCase2()">
						<%=mybean.PopulateLostCase1()%>
				</select>
				</span>
			</div>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_enquiry_lostcase2_id"></div>Lost Case
					2<span>*</span>:
				</label> <span id="span_lostcase2"> <select class="form-control"
					name="dr_enquiry_lostcase2_id" id="dr_enquiry_lostcase2_id"
					onchange="populateCase3()">
						<%=mybean.PopulateLostCase2()%>
				</select>
				</span>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_enquiry_lostcase3_id"></div> Lost Case 3:</label> <span
					id="span_lostcase3"> <select class="form-control"
					name="dr_enquiry_lostcase3_id" id="dr_enquiry_lostcase3_id"
					onchange="StatusUpdate();">
						<%=mybean.PopulateLostCase3()%>
				</select>
				</span>
			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1" value="<%=mybean.enquiry_dmsno%>"><div
						class="hint" id="hint_txt_enquiry_dmsno"></div>DMS No.<span>*</span>:</label>
				<input type="text" class="form-control" name="txt_enquiry_dmsno"
					id="txt_enquiry_dmsno" value="<%=mybean.enquiry_dmsno%>" size="32"
					maxlength="50"
					onchange="SecurityCheck('txt_enquiry_dmsno',this,'hint_txt_enquiry_dmsno')">

			</div>

			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">SOE: </label><label for="form_control_1"><span
					style="color: #000"><%=mybean.soe_name%></span></label>

			</div>
			
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_enquiry_notes"></div> Notes<span>*</span>:
				</label>
				<textarea class="form-control" rows="1" name="txt_enquiry_notes"
					id="txt_enquiry_notes" cols="70" rows="4"
					onChange="SecurityCheck('txt_enquiry_notes',this,'hint_txt_enquiry_notes')"><%=mybean.enquiry_notes%></textarea>

			</div>
			
		</div>

						
						</div>
					</div>
				</div>
			<!-- Status Details End  -->
			
<!-- 			//Entry Strat -->
<div style="padding-left: 15px;" style="padding-left: 15px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Entry By: </label><%=mybean.entry_by%>
				<input type="hidden" class="form-control" name="entry_by"
					value="<%=mybean.entry_by%>">
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Entry Date: </label><%=mybean.entry_date%>
				<input type="hidden" class="form-control" name="entry_date"
					value="<%=mybean.entry_date%>">
			</div>
			<%
				if (mybean.modified_by != null && !mybean.modified_by.equals("")) {
			%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Modified by: </label> <input type="text"
					class="form-control" value="<%=mybean.modified_by%>">
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Modified Date: </label> <input
					type="text" class="form-control" value="<%=mybean.modified_date%>">
			</div>
</div>
			<% } %>
			
			<!-- 			//Entry eND -->
			
			</div>
		</div>
	</div>

<!-- Accodian End -->

	<div class="container-fluid" style="min-height: 10px"></div>
	<div class="modal fade" id="Hintclicktocall" role="basic"
		aria-hidden="true" style="transform: translate(0, 50%)">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<span> &nbsp;&nbsp;Loading... </span> <br> <br>

				</div>
			</div>
		</div>
	</div>
	
	
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/jquery.app.js" type="text/javascript"></script>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/select2.full.min.js" type="text/javascript"></script>
	<script src="js/components-select2.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/dynacheck-post.js" type="text/javascript"></script>
	<script>
		$(function() {
			$(".select2-container").css("width", "100%");
		});
		
		$('#accordion1').on('shown.bs.collapse', function () {
			  
			  var panel = $(this).find('.in');
			  
			  $('html, body').animate({
			        scrollTop: panel.offset().top - 110
			  }, 500);
			  console.log("panel.offset().top==="+panel.offset().top);
			});
	</script>




	<script>

		function daysdiff(name, obj, hint) {
			var enquiry_id = GetReplacePost(document.form1.txt_enquiry_id.value);
			var value = GetReplacePost(obj.value);
			var param = "name=" + name + "&value=" + value
					+ "&daysdiff=yes&enquiry_id=" + enquiry_id;
			var url = "../sales/enquiry-dash-check.jsp?";
			var str = "123";
			showHintPost(url + param, str, param, hint);
			setTimeout('RefreshHistory()', 1000);
		}

		function BuyerTypeCheck(name) {
			var buyertype_id = document
					.getElementById("dr_enquiry_buyertype_id").value;
			var existingvehicle = document
					.getElementById("txt_enquiry_existingvehicle").value;
			SecurityCheck('dr_enquiry_buyertype_id', name,
					'hint_dr_enquiry_buyertype_id');
		}

		function ExistingVehicleCheck(name) {
			var buyertype_id = document
					.getElementById("dr_enquiry_buyertype_id").value;
			var existingvehicle = document
					.getElementById("txt_enquiry_existingvehicle").value;
			if (buyertype_id == "2" && existingvehicle == "") {
				showToast("Enter existing vehicle make!");
			} else {
				SecurityCheck('txt_enquiry_existingvehicle', name,
						'hint_txt_enquiry_existingvehicle');
			}
		}

		function StatusUpdate() {
			var lostcase1 = "", lostcase2 = "", lostcase3 = "";
			var status_id = document.getElementById('dr_enquiry_status_id').value;
			var status_desc = document
					.getElementById('txt_enquiry_status_desc').value;
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
			var strtoast = '';
			$.post(url, param, function(data, status) {
				if (status == 'success') {
					strtoast = data.trim().replace('<font color=\"red\">', '')
							.replace('</font>', '');
					showToast(strtoast.trim());
				}
			})

		}


		function SecurityCheck(name, obj, hint) {
			var value = '';
			var param = '';
			var enquiry_id = GetReplacePost(document.form1.enquiry_id.value);
			var url = "../sales/enquiry-dash-check.jsp?";
			var dat = document.getElementById("txt_enquiry_date").value;
			var str = "123";
			var status_id = document.getElementById("dr_enquiry_status_id").value;
			if (name == "txt_enquiry_exp_close_date"
					|| name == "txt_enquiry_hyundai_ex_purchasedate"
					|| name == "txt_enquiry_hyundai_ex_loancompletion"
					|| name == "txt_enquiry_loancompletionmonth"
					|| name == "txt_enquiry_purchasemonth") {
				value = GetReplacePost(obj.value);
				param = "name=" + name + "&value=" + value + "&enquiry_dat="
						+ dat + "&enquiry_id=" + enquiry_id;
			} else if (dat != "") {
				value = GetReplacePost(obj.value);
				param = "name=" + name + "&value=" + value + "&enquiry_dat="
						+ dat + "&enquiry_id=" + enquiry_id;
			} else if (name != "chk_enquiry_avpresent"
					&& name != "chk_enquiry_manager_assist"
					&& name != "chk_customer_dnd"
					&& name != "chk_enquiry_evaluation") {
				value = GetReplacePost(obj.value);
				param = "name=" + name + "&value=" + value + "&enquiry_dat="
						+ dat + "&enquiry_id=" + enquiry_id;
			} else {
				if (obj.checked == true) {
					value = "1";
				} else {
					value = "0";
				}
				param = "name=" + name + "&value=" + value + "&enquiry_dat="
						+ dat + "&enquiry_id=" + enquiry_id;
			}
		
			if (name == "dr_enquiry_model_id") {
				value = GetReplacePost(obj.value);
				var stage_id;
				if (value == 0) {
					stage_id = 1;
				} else {
					stage_id = 2;
				}
				param = "name=" + name + "&value=" + value + "&enquiry_dat="
						+ dat + "&enquiry_id=" + enquiry_id + "&stage_id="
						+ stage_id;
			}
			if ((name == "dr_enquiry_lostcase1_id")
					|| (name == "dr_enquiry_lostcase2_id")
					|| (name == "dr_enquiry_lostcase3_id")) {
				value = GetReplacePost(obj.value);
				param = "name=" + name + "&value=" + value + "&status_id="
						+ status_id + "&enquiry_dat=" + dat + "&enquiry_id="
						+ enquiry_id;
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
				var param = "name=" + name + "&value=" + value + "&enquiry_id="
						+ enquiry_id;
				
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
			if (name == "chk_enquiry_porsche_interest"
				|| name == "chk_enquiry_porsche_socialmediapref"
				|| name == "chk_enquiry_porsche_preferredcomm"
				|| name == "chk_enquiry_porsche_language"
				|| name == "chk_enquiry_jlr_interest") {
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
			if (name == "chk_enquiry_jlr_financeinterest" || name == "chk_enquiry_jlr_highnetworth") {
				if (obj.checked == true) {
					var value = "1";
					param = "name=" + name + "&value=" + value + "&enquiry_dat="
							+ dat + "&enquiry_id=" + enquiry_id;
				} else {
					var value = "0";
					param = "name=" + name + "&value=" + value + "&enquiry_dat="
							+ dat + "&enquiry_id=" + enquiry_id;
				}
		} 
			var strtoast = '';
			$.post(url, param, function(data, status) {
				if (status == 'success') {
					strtoast = data.trim().replace('<font color=\"red\">', '')
							.replace('</font>', '');
					showToast(strtoast.trim());

				}
			})
			
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
			var strtoast = '';
			$.post(url, param, function(data, status) {
				if (status == 'success') {
					strtoast = data.trim().replace('<font color=\"red\">', '')
							.replace('</font>', '');
					showToast(strtoast.trim());

				}
			})
		}

		//////////////////////// eof security check /////////////////////

		function RefreshHistory() {
			var enquiry_id = document.form1.txt_enquiry_id.value;
		}

		function populateItem() {
			var enquiry_id = GetReplacePost(document.form1.enquiry_id.value);
			var model_id = document.getElementById('dr_enquiry_model_id').value;

			showHint('app-enquiry-check.jsp?enquiry_id=' + enquiry_id
					+ '&enquiry_model_id=' + model_id, 'modelitem');
			
		}

		function populateVariant() {
			var enquiry_id = GetReplacePost(document.form1.txt_enquiry_id.value);
			var preownedmodel_id = document
					.getElementById('dr_enquiry_preownedmodel_id').value;
			showHint('enquiry-check.jsp?enquiry_id=' + enquiry_id
					+ '&enquiry_preownedmodel_id=' + preownedmodel_id,
					'modelvariant');
		}

		function populateCase2() {

			var lostcaseid = document.getElementById('dr_enquiry_lostcase1_id').value;
			showHint('app-enquiry-check.jsp?caseid1=' + lostcaseid,
					'span_lostcase2');

		}
		function populateCase3() {
			var lostcaseid2 = document
					.getElementById('dr_enquiry_lostcase2_id').value;
			showHint('app-enquiry-check.jsp?caseid2=' + lostcaseid2,
					'span_lostcase3');

		}

		function RefreshHistory() {
			var enquiry_id = document.form1.txt_enquiry_id.value;
		}

		function PopulateBrandsOwned(name, obj) {
			var value = 0;
			var enquiry_id = GetReplacePost(document.form1.txt_enquiry_id.value);
			var check1;
			value = obj.value;
			if (obj.checked == true) {
				check1 = "1";
			} else {
				check1 = "0";
			}
			var str = "123";
			var param = "name=" + name + "&compbrand_id=" + value
					+ "$enquiry_id=" + enquiry_id + "&checked=" + check1;
			showHintPost('../sales/enquiry-dash-check.jsp?name=' + name
					+ '&value=' + value + '&enquiry_id=' + enquiry_id
					+ '&checked=' + check1, "123", param,
					'hint_chk_compbrand_id');
			setTimeout('RefreshHistory()', 1000);
		}

		function populateLostCase2(name, obj, hint) {
			var enquiry_id = GetReplacePost(document.form1.enquiry_id.value);
			var value = obj.value;
			var url = "../sales/enquiry-dash-check.jsp?"
			var param = "name=dr_enquiry_lostcase1_id&value=" + value
					+ "&enquiry_id=" + enquiry_id;
			var str = "123";
			showHintPost(url + param, str, param, hint);
			setTimeout('RefreshHistory()', 1000);
		}

		function populateLostCase3(name, obj, hint) {
			var enquiry_id = GetReplacePost(document.form1.enquiry_id.value);
			var value = obj.value;
			var status_id = document.getElementById('dr_enquiry_status_id').value;
			var url = "../sales/enquiry-dash-check.jsp?"
			var param = "name=dr_enquiry_lostcase2_id&value=" + value
					+ "&status_id=" + status_id + "&enquiry_id=" + enquiry_id;
			var str = "123";
			setTimeout("showHintPost('" + url + param + "', '" + str + "', '"
					+ param + "', '" + hint + "')", 500);
			setTimeout('RefreshHistory()', 1000);
		}
		
		function validatePreowned(){
			var comp_id = <%=mybean.comp_id%>;
			var preowned_regno = $('#txt_preowned_regno').val();
			var preowned_manufyear = $('#dr_preowned_manufyear').val();
			var preowned_kms =  $('#txt_preowned_kms').val();
			var preowned_ownership =  $('#dr_preowned_ownership').val();
			var errormsg = '';
			var preowned_details="";
			var new_enquiry_id= $('#preowned_new_enquiry_id').val();
// 			alert("1===="+new_enquiry_id)
			var preowned_variant= $('#preownedvariant2').val();
			
			if(preowned_variant==null){
				preowned_variant=0;
			}
			if(preowned_variant != 0){
				if(preowned_regno == ''){
					errormsg += "<br><font color=red> Enter Registration No! </font>";
				}
				if(preowned_manufyear == 0 ){
					errormsg += "<br><font color=red> Select Manuf. Year! </font>";
				}
				if(preowned_kms == ''){
					errormsg += "<br><font color=red> Enter Kms! </font>";
				}
				if(preowned_ownership == 0){
					errormsg += "<br><font color=red> Select Ownership! </font>";
				}
				if(errormsg != '' && comp_id == '1009'){
	            errormsg =  '<div class="modal-header"> <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button> <h4 class="modal-title" style=text-align:center>Error!</h4> </div> <div class="modal-body" id="preowned-modal"> <div class="row" > <div class="col-md-12" style=text-align:center> '+errormsg+' </div> </div> </div> <div class="modal-footer"> <button type="button" class="btn default" data-dismiss="modal">Close</button> </div>';
					$(".modal-content").html(errormsg);
					$("#Hintclicktocall").modal('show');
				}
				else{
					preowned_details = "enquiry_id="+new_enquiry_id
					                  +"&preowned_regno="+preowned_regno
					                  +"&preowned_manufyear="+preowned_manufyear
					                  +"&preowned_kms=" +preowned_kms
					                  +"&preowned_ownership="+preowned_ownership;
					
					url = '<a id=submitpreowned href=../preowned/preowned-dash-add.jsp?' + preowned_details +' data-target=#Hintclicktocall data-toggle=modal></a>';
					$('#linkpreowned').html(url);
					$('#submitpreowned').click();
				}
			}
			else{
				preowned_details = "enquiry_id="+new_enquiry_id;
				url = '<a id=submitpreowned href=../preowned/preowned-dash-add.jsp?' + preowned_details +' data-target=#Hintclicktocall data-toggle=modal></a>';
				$('#linkpreowned').html(url);
				$('#submitpreowned').click();
			}
		}
	</script>

	<script>
		function TestdriveReq(name, obj, hint) {
			var testdrive_yes = document
					.getElementById("dr_enquiry_nexa_testdrivereq").value;
			if (testdrive_yes == "2") {
				document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display = '';
				document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.visibility = 'visible';
				document.getElementById("reason").style.display = '';
				document
						.getElementById("hint_txt_enquiry_nexa_testdrivereqreason").style.display = '';

				SecurityCheck(name, obj, hint);
			} else if (testdrive_yes == "0") {
				document.getElementById("reason").style.display = 'None';
				document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display = 'None';
			} else {

				document.getElementById("reason").style.display = 'None';
				document.getElementById("txt_enquiry_nexa_testdrivereqreason").style.display = 'None';
				document
						.getElementById("hint_txt_enquiry_nexa_testdrivereqreason").style.display = 'None';

				SecurityCheck(name, obj, hint);
			}
		}
	</script>

	<script type="text/javascript">
		function CheckEvaluation() {

			var preowned_enquiry_id =<%=mybean.preowned_enquiry_id%>;

			if(preowned_enquiry_id != "0"){
				$("#addEvalBut").html("<font color=\"red\">Pre-Owned Enquiry added.</font>");
			}
	
		}
	</script>

</body>
<!-- END BODY -->
</html>