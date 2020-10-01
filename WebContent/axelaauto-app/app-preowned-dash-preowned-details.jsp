<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Preowned_Dash"
	scope="request" />
<%
	mybean.doPost(request, response);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Pre-Owned Dash | Axelaauto</title>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">


<!-- <link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" /> -->
<!-- <link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" /> -->
<!-- <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"> -->
<link href="css/select2-bootstrap.min.css" rel="stylesheet"
	type="text/css" />
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

<body>
	<div class="header-wrap">
		<div class="panel-heading"
			style="margin-bottom: 20px; background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05); padding: 18px;">
			<span class="panel-title">
				<center>
					<strong> Pre-Owned Dashboard</strong>
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
						<label class="col-md-3 col-xs-5 control-label"
							for="form_control_1"><b>Pre-Owned No: </b></label>
						<div class="col-md-4 col-xs-7">
							<label for="id" class="form-control"><%=mybean.preowned_id%></label>
							<input name="preowned_id" type="hidden" id="preowned_id"
								value="<%=mybean.preowned_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5 control-label"
							for="form_control_1"><b>Date: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="date" class="form-control"><%=mybean.date%></label> <input
								name="txt_preowned_date" type="hidden" class="form-control"
								id="txt_preowned_date" value="<%=mybean.date%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5 control-label"
							for="form_control_1"><b>Customer: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="customer" class="form-control"> <%=mybean.preowned_customer_name%></label>
							<input type="hidden" class="form-control"
								name="txt_preowned_customer_name"
								id="txt_preowned_customer_name"
								value="<%=mybean.preowned_customer_name%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5 control-label"
							for="form_control_1"><b>Contact: </b> </label>
						<div class="col-md-8 col-xs-7">
							<label for="customer" class="form-control"><%=mybean.preowned_title%><%=mybean.contact_fname%>&nbsp;<%=mybean.contact_lname%></label>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5  control-label"
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
						<label class="col-md-3 col-xs-5  control-label"
							for="form_control_1"><b>Branch: </b></label>
						<div class="col-md-8 col-xs-7">
							<label for="mobile" class="form-control"><%=mybean.branch_name%></label>
							<input type="hidden" class="form-control"
								value="<%=mybean.branch_name%>">
						</div>
					</div>
					<br>
					<!-- 						<center><button type="button" class="btn1" name="addbutton" id="addbutton" >Add Enquiry</button></center> -->
				</div>
			</div>
		</form>
	</div>
	<!-- 						<div class="" style="margin-top: 40px;"> -->
	<form role="form" class="form-horizontal" method="post" name="frm"
		id="frm">
		<!-- <h4 class="panel-title"> -->
		<!-- 							<a href="#"><strong>ENQUIRY DETAILS</strong></a> -->
		<!-- 						</h4> -->
		<!-- 			<div class="form-body"> -->
		<!-- 				<div class="form-group"> -->
		<div class="  panel-heading"
			style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
			<span class="panel-title">
				<center>
					<h4>
						<strong>Pre-Owned Details</strong>
					</h4>
				</center>
			</span>
		</div>

		<!--  						///////////////////////		Start pre-owned Details   ////////////////////// -->
		<div style="padding-left: 20px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_preowned_exp_close_date"></div>Closing Date<span>*</span>:</label>
				<input type="" class="form-control"
					name="txt_preowned_exp_close_date" id="txt_preowned_exp_close_date"
					value="<%=mybean.closedate%>" size="12" maxlength="10"
					onfocusout="SecurityCheck('txt_preowned_exp_close_date',this,'hint_txt_preowned_exp_close_date');"
					onclick="datePicker('txt_preowned_exp_close_date');" readonly>
			</div>


			<!-- 									<div class="form-group form-md-line-input"> -->
			<!-- 										<label for="form_control_1"><div class="hint" -->
			<!-- 												id="hint_txt_days_diff"></div>Days left: </label> <label -->
			<%-- 											for="form_control_1"><span style="color: #000"><%=mybean.days_diff%></span></label> --%>
			<!-- 									</div> -->
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_preowned_title"></div>Title<span>*</span>:</label> <input
					type="text" class="form-control" name="txt_preowned_title"
					id="txt_preowned_title" value="<%=mybean.preowned_title%>"
					size="52" maxlength="255"
					onChange="SecurityCheck('txt_preowned_title',this,'hint_txt_preowned_title')" />

			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="txt_preowned_desc"></div>Description<span>*</span>:
				</label>
				<textarea class="form-control" name="txt_preowned_desc" cols="50"
					rows="4" id="txt_preowned_desc" value=""
					onChange="SecurityCheck('txt_preowned_desc',this,'hint_txt_preowned_desc')"><%=mybean.preowned_desc%></textarea>

			</div>


			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"><div class="hint" -->
			<!-- 						id="hint_txt_preowned_value"></div>Budget:</label> <input type="tel" -->
			<!-- 					class="form-control" name="txt_preowned_value" -->
			<%-- 					id="txt_preowned_value" value="<%=mybean.preowned_value%>" --%>
			<!-- 					onKeyUp="toInteger('txt_preowned_value','Value')" -->
			<!-- 					onchange="SecurityCheck('txt_preowned_value',this,'hint_txt_preowned_value')" -->
			<!-- 					size="10" maxlength="10" /> -->
			<!-- 			</div> -->

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_preowned_emp_id"></div> Pre-Owned Consultant<span>*</span>:</label> <select
					class="form-control" name="dr_preowned_emp_id"
					id="dr_preowned_emp_id"
					onChange="SecurityCheck('dr_preowned_emp_id',this,'hint_dr_preowned_emp_id');">
					<%=mybean.PopulateExecutive()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_preowned_model_id"></div>Model<span>*</span>:</label> <select
					class="form-control select2" id="preownedvariant"
					name="preownedvariant"
					onchange="SecurityCheck('preownedvariant',this,'hint_dr_variant_id');">
					<%=mybean.modelcheck.PopulateVariant(mybean.preowned_variant_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_preowned_model_id"></div>Sub Variant:</label> <input
					name="txt_preowned_sub_variant" type="text" class="form-control"
					id="txt_preowned_sub_variant"
					value="<%=mybean.preowned_sub_variant%>" size="52" maxlength="255"
					onchange="SecurityCheck('txt_preowned_sub_variant',this,'hint_txt_preowned_sub_variant')" />
			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_preowned_model_id"></div>Fuel Type<span>*</span></label> <select
					name="dr_preowned_fueltype_id" id="dr_preowned_fueltype_id"
					class="form-control"
					onchange="SecurityCheck('dr_preowned_fueltype_id',this,'hint_dr_preowned_fueltype_id');">
					<%=mybean.PopulateFuel()%>
				</select>
			</div>


			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"><div class="hint" -->
			<!-- 						id="hint_dr_preowned_item_id"></div>Variant<span>*</span>: </label> <span -->
			<!-- 					id="modelitem"><select -->
			<!-- 					disabled="disabled"  class="form-control" -->
			<!-- 					name="dr_preowned_item_id" id="dr_preowned_item_id" -->
			<!-- 					onChange="SecurityCheck('dr_preowned_item_id',this,'hint_dr_preowned_item_id');"> -->
			<%-- 						<%=mybean.PopulateItem()%> --%>
			<!-- 				</select> </span> -->
			<!-- 			</div> -->

			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"><div class="hint" -->
			<!-- 						id="hint_dr_preowned_add_model_id"></div>Additional Model: </label> <span -->
			<!-- 					id="modelitem"><select class="form-control" -->
			<!-- 					name="dr_preowned_add_model_id" id="dr_preowned_add_model_id" -->
			<!-- 					onChange="SecurityCheck('dr_preowned_add_model_id',this,'hint_dr_preowned_add_model_id');"> -->
			<%-- 						<%=mybean.PopulateAdditionalModel(mybean.comp_id)%> --%>
			<!-- 				</select> </span> -->
			<!-- 			</div> -->

			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"><div class="hint" -->
			<!-- 						id="hint_dr_preowned_option_id"></div>Colour<span>*</span>: </label> <select -->
			<!-- 					class="form-control" name="dr_preowned_option_id" -->
			<!-- 					id="dr_preowned_option_id" -->
			<!-- 					onChange="SecurityCheck('dr_preowned_option_id',this,'hint_dr_preowned_option_id');"> -->

			<%-- 					<%=mybean.PopulateColor()%> --%>
			<%-- 					<%=mybean.PopulateOption(mybean.comp_id)%> --%>
			<!-- 				</select> -->
			<!-- 			</div> -->

			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"><div class="hint" -->
			<!-- 						id="hint_dr_preowned_age_id"></div>Age<span>*</span>: </label> <select -->
			<!-- 					class="form-control" name="dr_preowned_age_id" -->
			<!-- 					id="dr_preowned_age_id" -->
			<!-- 					onChange="SecurityCheck('dr_preowned_age_id',this,'hint_dr_preowned_age_id');"> -->
			<%-- 					<%=mybean.PopulateAge()%> --%>
			<%-- 					<%=mybean.PopulateAge(mybean.comp_id)%> --%>
			<!-- 				</select> -->
			<!-- 			</div> -->

			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"><div class="hint" -->
			<!-- 						id="hint_dr_preownedocc_id"></div>Occupation<span>*</span>:</label> <select -->
			<!-- 					class="form-control" name="dr_preowned_occ_id" -->
			<!-- 					id="dr_preowned_occ_id" -->
			<!-- 					onchange="SecurityCheck('dr_preowned_occ_id',this,'hint_dr_preowned_occ_id');"> -->
			<%-- 					<%=mybean.PopulateOccupation()%> --%>
			<!-- 				</select> -->
			<!-- 			</div> -->

			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"><div class="hint" -->
			<!-- 						id="hint_dr_preowned_custtype_id"></div>Type of Customer<span>*</span>:</label> -->
			<!-- 				<select class="form-control" name="dr_preowned_custtype_id" -->
			<!-- 					id="dr_preowned_custtype_id" -->
			<!-- 					onchange="SecurityCheck('dr_preowned_custtype_id',this,'hint_dr_preowned_custtype_id');"> -->
			<%-- 					<%=mybean.PopulateCustomerType()%> --%>
			<!-- 				</select> -->
			<!-- 			</div> -->

			<%-- 			<%if(mybean.branch_brand_id.equals("55")){%> --%>
			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"><div class="hint" -->
			<!-- 						id="hint_dr_preowned_custtype_id"></div>Category:</label> -->
			<!-- 				<select class="form-control" name="dr_preowned_preownedcat_id" -->
			<!-- 					id="dr_preowned_preownedcat_id" -->
			<!-- 					onchange="SecurityCheck('dr_preowned_preownedcat_id',this,'hint_dr_preowned_preownedcat_id');"> -->
			<%-- 					<%=mybeanenqdashmethods.PopulateCategory(mybean.preowned_preownedcat_id,mybean.comp_id)%> --%>
			<!-- 				</select> -->
			<!-- 			</div> -->
			<%-- 			<%} %> --%>

			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"><div class="hint" -->
			<!-- 						id="hint_dr_preowned_custtype_id"></div>Corporate:</label> -->
			<!-- 				<select class="form-control" name="dr_preowned_corporate_id" -->
			<!-- 					id="dr_preowned_corporate_id" -->
			<!-- 					onchange="SecurityCheck('dr_preowned_corporate_id',this,'hint_dr_preowned_corporate_id');"> -->
			<%-- 					<%=mybeanenqdashmethods.PopulateCorporate(mybean.branch_brand_id, mybean.preowned_corporate_id, mybean.comp_id)%> --%>
			<!-- 				</select> -->
			<!-- 			</div> -->

			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"><div class="hint" -->
			<!-- 						id="hint_txt_preowned_fuelallowance"></div>Fuel Allowance:</label> <input -->
			<!-- 					type="text" class="form-control" name="txt_preowned_fuelallowance" -->
			<!-- 					id="txt_preowned_fuelallowance" -->
			<%-- 					value="<%=mybean.preowned_fuelallowance%>" size="10" maxlength="10" --%>
			<!-- 					onKeyUp="toInteger('txt_preowned_fuelallowance','Fuel')" -->
			<!-- 					onChange="SecurityCheck('txt_preowned_fuelallowance',this,'hint_txt_preowned_fuelallowance')"> -->

			<!-- 			</div> -->

			<!-- 							//////////////////////	Pre Owned Details  ////////////////// -->


			<!-- 									<div class="form-group form-md-line-input"> -->
			<!-- 										<label for="form_control_1"> -->
			<!-- 											<div class="hint" id="hint_modelvariant"></div> Pre Owned model: -->
			<!-- 										</label> <select class="form-control" name="modelvariant" -->
			<!-- 											id="modelvariant" style="width: 300px" type="hidden" -->
			<!-- 											onChange="SecurityCheck('modelvariant',this,'hint_modelvariant')"> -->
			<%-- 											<%=mybean.PopulatePreOwnedModel(mybean.comp_id)%> --%>
			<!-- 										</select> -->
			<!-- 									</div> -->

			<!-- 		<div class="form-group form-md-line-input"> -->
			<!-- 										<label for="form_control_1"> -->
			<!-- 											<div class="hint" id="hint_dr_preowned_budget_id"></div>Budget: -->
			<!-- 										</label> <select class="form-control" name="dr_preowned_budget_id" -->
			<!-- 											id="dr_preowned_budget_id" -->
			<!-- 											onChange="SecurityCheck('dr_preowned_budget_id',this,'hint_dr_preowned_budget_id');"> -->
			<%-- 											<%=mybean.PopulateBudget()%> --%>
			<!-- 										</select> -->
			<!-- 									</div> -->

			<!-- 									<div class="form-group form-md-line-input"> -->
			<!-- 										<label for="form_control_1"> -->
			<!-- 											<div class="hint" id="hint_txt_preowned_regdyear"></div>Pref. -->
			<!-- 											Reg. Year: -->
			<!-- 										</label> <select class="form-control" name="txt_preowned_regdyear" -->
			<!-- 											id="txt_preowned_regdyear" -->
			<!-- 											onChange="SecurityCheck('txt_preowned_regdyear',this,'hint_txt_preowned_regdyear');"> -->
			<%-- 											<%=mybean.PopulateRegYear(mybean.preowned_regdyear)%> --%>
			<!-- 										</select> -->
			<!-- 									</div> -->
			<!-- 									<div class="form-group form-md-line-input"> -->
			<!-- 										<label for="form_control_1"> -->
			<!-- 											<div class="hint" id="hint_dr_preowned_prefmileage_id"></div>Mileage: -->
			<!-- 										</label> <select class="form-control" name="dr_preowned_prefmileage_id" -->
			<!-- 											id="dr_preowned_prefmileage_id" -->
			<!-- 											onChange="SecurityCheck('dr_preowned_prefmileage_id',this,'hint_dr_preowned_prefmileage_id');"> -->
			<%-- 											<%=mybean.PopulateMileage()%> --%>
			<!-- 										</select> -->
			<!-- 									</div> -->
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_preowned_extcolour_id"></div>Exterior<span>*</span>:
				</label> <select class="form-control" name="dr_preowned_extcolour_id"
					id="dr_preowned_extcolour_id"
					onChange="SecurityCheck('dr_preowned_extcolour_id',this,'hint_dr_preowned_extcolour_id');">
					<%=mybean.PopulateExterior()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Interior<span>*</span>:
				</label> <select class="form-control" name="dr_preowned_intcolour_id"
					id="dr_preowned_intcolour_id"
					onChange="SecurityCheck('dr_preowned_intcolour_id',this,'hint_dr_preowned_intcolour_id');">
					<%=mybean.PopulateInterior()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Options: </label>
				<textarea name="txt_preowned_options" cols="50" rows="4"
					class="form-control" id="txt_preowned_options"
					onchange="SecurityCheck('txt_preowned_options',this,'hint_txt_preowned_options')"><%=mybean.preowned_options%></textarea>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Manuf. Year<font
					color="#ff0000">*</font>:
				</label> <select name="txt_preowned_manufyear" type="text"
					class="form-control" id="txt_preowned_manufyear" maxlength="4"
					onchange="SecurityCheck('txt_preowned_manufyear',this,'hint_txt_preowned_manufyear')">
					<%=mybean.PopulateManufYear(mybean.preowned_manufyear)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Regd. Year: </label> <select
					name="txt_preowned_regdyear" type="text" class="form-control"
					id="txt_preowned_regdyear" maxlength="4"
					onchange="SecurityCheck('txt_preowned_regdyear',this,'hint_txt_preowned_regdyear')">
					<%=mybean.PopulateRegYear(mybean.preowned_regdyear)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Registration No<font
					color="#ff0000">*</font>:
				</label> <input name="txt_preowned_regno" type="text" class="form-control"
					id="txt_preowned_regno" value="<%=mybean.preowned_regno%>"
					size="15" maxlength="10"
					onchange="SecurityCheck('txt_preowned_regno',this,'hint_txt_preowned_regno')" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Kms: </label> <input
					name="txt_preowned_kms" type="tel" class="form-control"
					id="txt_preowned_kms" value="<%=mybean.preowned_kms%>" size="15"
					maxlength="10"
					onchange="SecurityCheck('txt_preowned_kms',this,'hint_txt_preowned_kms')" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Foreclosure Amount: </label> <input
					name="txt_preowned_fcamt" type="tel" class="form-control"
					id="txt_preowned_fcamt" value="<%=mybean.preowned_fcamt%>"
					size="15" maxlength="10"
					onchange="SecurityCheck('txt_preowned_fcamt',this,'hint_txt_preowned_fcamt')" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> NOC: </label> <select
					name="dr_preowned_noc" id="dr_preowned_noc" class="form-control"
					onchange="SecurityCheck('dr_preowned_noc',this,'hint_dr_preowned_noc')">
					<%=mybean.PopulateNoc()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Funding Bank: </label> <input
					name="txt_preowned_funding_bank" type="text" class="form-control"
					id="txt_preowned_funding_bank"
					value="<%=mybean.preowned_funding_bank%>" size="52" maxlength="255"
					onchange="SecurityCheck('txt_preowned_funding_bank',this,'hint_txt_preowned_funding_bank')" />
				<input name="preowned_funding_bank" type="hidden"
					class="form-control" id="preowned_funding_bank"
					value="<%=mybean.preowned_funding_bank%>">
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Loan No.: </label> <input
					name="txt_preowned_loan_no" type="text" class="form-control"
					id="txt_preowned_loan_no" value="<%=mybean.preowned_loan_no%>"
					size="15" maxlength="20"
					onchange="SecurityCheck('txt_preowned_loan_no',this,'hint_txt_preowned_loan_no')" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Insurance Date<font
					color="#ff0000">*</font>:
				</label> <input name="txt_preowned_insur_date" id="txt_preowned_insur_date"
					class="form-control date-picker" data-date-format="dd/mm/yyyy"
					type="text" value="<%=mybean.insurdate%>" size="12" maxlength="10"
					onclick="datePicker('txt_preowned_insur_date');"
					onfocusout="SecurityCheck('txt_preowned_insur_date',this,'hint_txt_preowned_insur_date');"
					readonly />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Insurance Type: </label> <select
					name="dr_preowned_insurance_id" id="dr_preowned_insurance_id"
					class="form-control"
					onChange="SecurityCheck('dr_preowned_insurance_id',this,'hint_dr_preowned_insurance_id');">
					<%=mybean.PopulateInsuranceType()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Ownership<font color="#ff0000">*</font>:
				</label> <select name="dr_preowned_ownership_id"
					id="dr_preowned_ownership_id" class="form-control"
					onChange="SecurityCheck('dr_preowned_ownership_id',this,'hint_dr_preowned_ownership_id');">
					<%=mybean.PopulateOwnership()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Invoice Value: </label> <input
					name="txt_preowned_invoicevalue" type="tel" class="form-control"
					id="txt_preowned_invoicevalue"
					value="<%=mybean.preowned_invoicevalue%>" size="15" maxlength="10"
					onchange="SecurityCheck('txt_preowned_invoicevalue',this,'hint_txt_preowned_invoicevalue')" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"> Expected Price: </label> <input
					name="txt_preowned_expectedprice" type="tel" class="form-control"
					id="txt_preowned_expectedprice"
					value="<%=mybean.preowned_expectedprice%>" size="15" maxlength="10"
					onchange="SecurityCheck('txt_preowned_expectedprice',this,'hint_txt_preowned_expectedprice')" />
			</div>
		</div>
		<br>

		<!-- Start Customer Details -->
		<div class="  panel-heading"
			style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
			<span class="panel-title">
				<center>
					<h4>
						<strong>Customer</strong>
					</h4>
				</center>
			</span>
		</div>
		<div style="padding-left: 20px;">
			<div class="form-group form-md-line-input">
				<label for="form_control_1"
					value="<%=mybean.preowned_customer_name%>"><div
						class="hint" id="hint_txt_preowned_customer_name"></div>Customer:
				</label> <input type="text" class="form-control"
					name="txt_preowned_customer_name" id="txt_preowned_customer_name"
					size="32" maxlength="255"
					value="<%=mybean.preowned_customer_name%>"
					onchange="SecurityCheck('txt_preowned_customer_name',this,'hint_txt_preowned_customer_name')">
			</div>

			<%-- <div class="form-group form-md-line-input">
										<label for="form_control_1"
											value="<%=mybean.preowned_customer_name%>"><div
												class="hint" id="hint_txt_preowned_customer_name"></div>Customer:
										</label> <input type="text" class="form-control"
											name="txt_preowned_customer_name"
											id="txt_preowned_customer_name"
											value="<%=mybean.preowned_customer_name%>"
											onchange="SecurityCheck('txt_preowned_customer_name',this,'hint_txt_preowned_customer_name')">
									</div> --%>

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
						id="hint_txt_contact_mobile1"></div>Mobile 1<span>*</span>:</label> <input
					type="tel" class="form-control" name="txt_contact_mobile1"
					id="txt_contact_mobile1" value="<%=mybean.contact_mobile1%>"
					size="32" maxlength="13"
					onchange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')">
			</div>
			(91-9999999999)

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_contact_mobile2"></div>Mobile 2:</label> <input type="tel"
					class="form-control" name="txt_contact_mobile2"
					id="txt_contact_mobile2" value="<%=mybean.contact_mobile2%>"
					placeholder="(91-9999999999)" maxlength="13" size="32"
					onchange="SecurityCheck('txt_contact_mobile2',this,'hint_txt_contact_mobile2')">
			</div>
			(91-9999999999)
			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"> -->
			<!-- 					<div class="hint" id="hint_txt_contact_phone1"></div>Phone 1: -->
			<!-- 				</label> <input type="tel" class="form-control" name="txt_contact_phone1" -->
			<%-- 					id="txt_contact_phone1" value="<%=mybean.contact_phone1%>" --%>
			<!-- 					placeholder="(080-33333333)" size="32" maxlength="12" -->
			<!-- 					onchange="SecurityCheck('txt_contact_phone1',this,'hint_txt_contact_phone1')"> -->

			<!-- 			</div> -->
			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"> -->
			<!-- 					<div class="hint" id="hint_txt_contact_phone2"></div>Phone 2: -->
			<!-- 				</label> <input type="tel" class="form-control" name="txt_contact_phone2" -->
			<%-- 					id="txt_contact_phone2" value="<%=mybean.contact_phone2%>" --%>
			<!-- 					placeholder="(080-33333333)" size="32" maxlength="12" -->
			<!-- 					onchange="SecurityCheck('txt_contact_phone2',this,'hint_txt_contact_phone2')"> -->

			<!-- 			</div> -->
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
			<%-- 
									<div class="form-group form-md-line-input">
										<label for="form_control_1"><div class="hint"
												id="hint_dr_customer_zone_id"></div>Zone<span>*</span>:</label> <input
											type="text" class="form-control" name="mainzone"
											id="mainzone" value="<%=mybean.customer_zone_id%>"
											onchange="SecurityCheck('mainzone',this,'hint_dr_customer_zone_id')">
									</div> --%>

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
		<!-- End Customer Details -->

		<!-- 		//////////////////////	Status Details    ///////////////////////////////  -->

		<div class="  panel-heading"
			style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">

			<span class="panel-title">
				<center>
					<h4>
						<strong>Status</strong>
					</h4>
				</center>
			</span>
		</div>

		<div style="padding-left: 20px;">
			<!-- 			<div class="form-group form-md-line-input"> -->
			<%-- 				<label for="form_control_1">Stage: <%=mybean.stage_name%> --%>
			<!-- 			</div> -->
			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1"> -->
			<!-- 					<div class="hint" id="hint_dr_prioritypreowned_id"></div> Priority<span>*</span>: -->
			<!-- 				</label> <select class="form-control" name="drop_prioritypreowned_id" -->
			<!-- 					id="drop_prioritypreowned_id" -->
			<!-- 					onchange="SecurityCheck('drop_prioritypreowned_id',this,'hint_drop_prioritypreowned_id');"> -->
			<%-- 					<%=mybean.PopulateEnquiryPriority()%> --%>
			<!-- 				</select> -->
			<!-- 			</div> -->
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Status<span>*</span>:
				</label> <select class="form-control" name="dr_preowned_preownedstatus_id"
					id="dr_preowned_preownedstatus_id" onChange="StatusUpdate();">
					<%=mybean.PopulateStatus(mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"
					value="<%=mybean.preowned_preownedstatus_desc%>"><div
						class="hint" id="hint_txt_preowned_status_desc"></div> Status
					comments<span>*</span>: </label>
				<textarea class="form-control" rows="1"
					name="txt_preowned_preownedstatus_desc"
					id="txt_preowned_preownedstatus_desc"
					onKeyUp="charcount('txt_preowned_preownedstatus_desc', 'span_txt_preowned_preownedstatus_desc','<font color=red>({CHAR} characters left)</font>', '1000')"
					onChange="SecurityCheck('txt_preowned_preownedstatus_desc',this,'hint_txt_preowned_preownedstatus_desc');StatusUpdate();"><%=mybean.preowned_preownedstatus_desc%></textarea>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_preowned_lostcase1_id"></div>Lost Case 1<span>*</span>:</label>
				<span id="span_lostcase1"> <select class="form-control"
					name="dr_preowned_lostcase1_id" id="dr_preowned_lostcase1_id"
					onchange="populateLostCase2('dr_preowned_lostcase1_id',this,'span_lostcase2');StatusUpdate();">
						<%=mybean.PopulateLostCase1(mybean.comp_id)%>
				</select>
				</span>
			</div>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_dr_preowned_lostcase2_id"></div>Lost
					Case 2<span>*</span>:
				</label> <span id="span_lostcase2"> <select class="form-control"
					name="dr_preowned_lostcase2_id" id="dr_preowned_lostcase2_id"
					onchange="populateLostCase3('dr_preowned_lostcase2_id',this,'span_lostcase3');StatusUpdate();">
						<%=mybean.PopulateLostCase2(mybean.comp_id)%>
				</select>
				</span>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_dr_preowned_lostcase3_id"></div> Lost Case 3:</label> <span
					id="span_lostcase3"> <select class="form-control"
					name="dr_preowned_lostcase3_id" id="dr_preowned_lostcase3_id"
					onchange="StatusUpdate();">
						<%=mybean.PopulateLostCase3(mybean.comp_id)%>
				</select>
				</span>
			</div>


			<!-- 			<div class="form-group form-md-line-input"> -->
			<%-- 				<label for="form_control_1" value="<%=mybean.preowned_dmsno%>"><div --%>
			<!-- 						class="hint" id="hint_txt_preowned_dmsno"></div>DMS No.<span>*</span>:</label> -->
			<!-- 				<input type="text" class="form-control" name="txt_preowned_dmsno" -->
			<%-- 					id="txt_preowned_dmsno" value="<%=mybean.preowned_dmsno%>" size="32" --%>
			<!-- 					maxlength="50" -->
			<!-- 					onchange="SecurityCheck('txt_preowned_dmsno',this,'hint_txt_preowned_dmsno')"> -->

			<!-- 			</div> -->

			<%-- <div class="form-group form-md-line-input">
									<label for="form_control_1 col-md-12"
										value="<%=mybean.preowned_action%>">
										<div class="hint" id="hint_txt_preowned_action"></div>
										Corrective Action<span>*</span>:
									</label>
									<textarea class="form-control" rows="1"
										name="txt_preowned_action" id="txt_preowned_action"
										onchange="SecurityCheck('txt_preowned_action',this,'hint_txt_preowned_action');StatusUpdate();" /></textarea>

								</div> --%>
			<!-- 			<div class="form-group form-md-line-input"> -->
			<!-- 				<label for="form_control_1">SOE: </label><label for="form_control_1"><span -->
			<%-- 					style="color: #000"><%=mybean.soe_name%></span></label> --%>

			<!-- 			</div> -->
			<!-- 								<div class="form-group form-md-line-input"> -->
			<!-- 									<label for="form_control_1">SOB: </label><label -->
			<%-- 										for="form_control_1"><span style="color: #000"><%=mybean.sob_name%></span></label> --%>

			<!-- 								</div> -->
			<%-- 			<div class="form-group form-md-line-input">
									<label for="form_control_1">Campaign: </label><label
										for="form_control_1"><span style="color: #000"><%=mybean.campaign_name%></span></label>

								</div> --%>
			<%-- <div class="form-group form-md-line-input">
									<label for="form_control_1"><div class="hint"
											id="hint_dr_preowned_preownedcat_id"></div>Category<span>*</span>:</label>
									<select class="form-control" name="dr_preowned_preownedcat_id"
										id="dr_preowned_preownedcat_id"
										onchange="SecurityCheck('dr_preowned_preownedcat_id',this,'hint_dr_preowned_preownedcat_id')">
										<%=mybean.PopulateCategory()%>
									</select>
								</div> --%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">
					<div class="hint" id="hint_txt_preowned_notes"></div> Notes<span>*</span>:
				</label>
				<textarea class="form-control" rows="1" name="txt_preowned_notes"
					id="txt_preowned_notes" cols="70" rows="4"
					onChange="SecurityCheck('txt_preowned_notes',this,'hint_txt_preowned_notes')"><%=mybean.preowned_notes%></textarea>

			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Entre By:</label><%=mybean.entry_by%>
				<input type="hidden" class="form-control" name="entry_by"
					value="<%=mybean.entry_by%>">
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Entry Date:</label><%=mybean.entry_date%>
				<input type="hidden" class="form-control" name="entry_date"
					value="<%=mybean.entry_date%>">
			</div>
			<%
									if (mybean.modified_by != null && !mybean.modified_by.equals("")) {
								%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Modified by:</label> <input type="text"
					class="form-control" value="<%=mybean.modified_by%>">
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Modified Date:</label> <input
					type="text" class="form-control" value="<%=mybean.modified_date%>">
			</div>

			<%
									}
								%>
		</div>
		<!-- </div> -->
		<!-- </div> -->
	</form>
	<!-- </div> -->
	<!-- accordian 2-->
	<!-- 	<script src="../assets/js/jquery.min.js" type="text/javascript"></script> -->
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/jquery.app.js" type="text/javascript"></script>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/select2.full.min.js" type="text/javascript"></script>
	<script src="js/components-select2.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<!-- 	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script> -->
	<!-- 	<script src="js/jquery.min.js" type="text/javascript"></script> -->
	<!-- 	<script type="text/javascript" src="../Library/dynacheck-post.js"></script> -->
	<script src="js/dynacheck-post.js" type="text/javascript"></script>
	<!-- 	<script src="js/axelamobilecall.js" type="text/javascript"></script> -->
	<script>

$(function() { 
$(".select2-container").css("width","100%");
});
</script>




	<script>
	// $(function() {
	// 		$("#txt_preowned_exp_close_date").datepicker({  ////////////////
	// 			showButtonPanel : true,
	// 	//		dateFormat : "dd/mm/yy"
	// 		});
	// 		$("#txt_preowned_travel_fromdate").datepicker({
	// 			showButtonPanel : true,
	// 			dateFormat : "dd/mm/yy"
	// 		});
	// 		$("#txt_preowned_travel_todate").datepicker({
	// 			showButtonPanel : true,
	// 			dateFormat : "dd/mm/yy" ///  /////
	// 		});
	/// 	});
	
// 	$(function() {				
// 		$("#txt_followup_date").datetimepicker({
// 			controlType : 'select',
// 			stepMinute : 5,
// 			dateFormat : 'dd/mm/yy',
// 			// 			timeFormat : 'HH:mm',
// 			hour : 10,
// 			minute : 00
// 		});
// 	});

// 	$(function() {
// 		$("#txt_crmfollowup_time").datetimepicker({
// 			addSliderAccess : true,
// 			sliderAccessArgs : {
// 				touchonly : false
// 			},
// 			dateFormat : "dd/mm/yy"
// 		});
// 	});

// 	$(function() {
// 		$("#txt_preowned_hyundai_ex_purchasedate").datepicker({
// 			showButtonPanel : true,
// 			dateFormat : "dd/mm/yy"
// 		});
// 	});
// 	$(function() {
// 		$("#txt_preowned_loancompletionmonth").datepicker({
// 			showButtonPanel : true,
// 			dateFormat : "dd/mm/yy"
// 		});
// 	});
	/* $(function() {
		 $( "#txt_preowned_loancompletionmonth" ).datepicker({
		      showButtonPanel: true,
		      sliderAccessArgs : {
					touchonly : false
		      },
		      dateFormat: "dd/mm/yy"
		    });
		}); */

		function daysdiff(name, obj, hint) {
			var names = name.split(", ");
			var dt1 = names[0];
			var dt2 = names[1];
			var date1 = document.getElementById(dt1).value;
			var date2 = document.getElementById(dt2).value;
			var value = date1 + ", " + date2;
			var checked = '';
			var preowned_id = GetReplacePost(document.form1.preowned_id.value);
			var url = "../preowned/preowned-dash-check.jsp?";
			var param = "name=" + name + "&value=" + value + "&checked=" + checked
					+ "&preowned_id=" + preowned_id;
			var str = "123";
			showHintPost(url + param, str, param, hint);
			setTimeout('RefreshHistory()', 1000);
		}

		function SecurityCheck(name, obj, hint) {
			var value = GetReplacePost(obj.value);
			var msg = "";
			var preowned_id = GetReplacePost(document.form1.preowned_id.value);
			var dat = document.getElementById("txt_preowned_date").value;
			//var fcamt = document.getElementById("txt_preowned_fcamt").value;
			//var fbank = document.getElementById("txt_preowned_funding_bank").value;
			//var floan = document.getElementById("txt_preowned_loan_no").value;
			var url = "../preowned/preowned-dash-check.jsp?";
			var str = "123";
			var param = "name=" + name + "&value=" + value + "&preowned_dat=" + dat
					+ "&preowned_id=" + preowned_id;
			showHintPost(url + param, str, param, hint);
			setTimeout('RefreshHistory()', 1000);
		}

		function RefreshHistory() {
			var preowned_id = document.form1.preowned_id.value;
		}

		function SecurityDoubleCheck(name1, obj1, name2, obj2, hint) {
			var value1 = GetReplacePost(obj1.value);
			var value2 = GetReplacePost(obj2.value);
			var preowned_id = GetReplacePost(document.form1.preowned_id.value);
			var url = "../preowned/preowned-dash-check.jsp?";
			var param = "name1=" + name1 + "&value1=" + value1 + "&name2=" + name2
					+ "&value2=" + value2 + "&preowned_id=" + preowned_id;
			var str = "123";
			showHintPost(url + param, str, param, hint);

			setTimeout('RefreshHistory()', 1000);
		}

		function RefreshHistory() {
			var preowned_id = document.form1.preowned_id.value;
		}

		function PopulateCheckVariant() {
			var preowned_id = GetReplacePost(document.form1.preowned_id.value);
			var model_id = document.getElementById('dr_preowned_preownedmodel_id').value;
			var param = "preowned_preownedmodel_id=" + model_id + "&preowned_id="
					+ preowned_id;
			var str = "123";
			showHintPost(
					'../preowned/preowned-dash-check.jsp?preowned_preownedmodel_id='
							+ model_id + "&preowned_id=" + preowned_id, param,
					'modelitem');
		}

		function populateLostCase2(name, obj, hint) {
			var preowned_id = document.getElementById('preowned_id').value;
			var value = obj.value;
			var url = "../preowned/preowned-dash-check.jsp?"
			var param = "name=dr_preowned_lostcase1_id&value=" + value
					+ "&preowned_id=" + preowned_id;
			var str = "123";
			setTimeout("showHintPost('" + url + param + "','" + str + "','" + param + "','" + hint + "')", 500);
			//setTimeout('StatusUpdate()', 1000);
		}

		function populateLostCase3(name, obj, hint) {
			var preowned_id = document.getElementById('preowned_id').value;
			var value = obj.value;
			var status_id = document.getElementById('dr_preowned_preownedstatus_id').value;
			var url = "../preowned/preowned-dash-check.jsp?"
			var param = "name=dr_preowned_lostcase2_id&value=" + value
			             + "&status_id=" + status_id +"&preowned_id=" + preowned_id;
			var str = "123";
			setTimeout("showHintPost('" + url + param + "','" +str + "','" + param + "', '" + hint + "')", 500);
			//setTimeout('StatusUpdate()', 1000);
		}

		function StatusUpdate() {
			var lostcase1 = "", lostcase2 = "", lostcase3 = "";
			var status_id = document.getElementById('dr_preowned_preownedstatus_id').value;
			var status_desc = document.getElementById('txt_preowned_preownedstatus_desc').value;
			lostcase1 = document.getElementById('dr_preowned_lostcase1_id').value;
			lostcase2 = document.getElementById('dr_preowned_lostcase2_id').value;
			lostcase3 = document.getElementById('dr_preowned_lostcase3_id').value;
			var url = "../preowned/preowned-dash-check.jsp?";
			var param;
			var str = "123";
			var preowned_id = document.form1.preowned_id.value;
			param = "name=dr_preowned_preownedstatus_id&status_id="+ status_id
					+ "&status_desc="+ status_desc
					+ "&lostcase1="+ lostcase1
					+ "&lostcase2="+ lostcase2
					+ "&lostcase3="+ lostcase3 
					+ "&preowned_id=" + preowned_id;
// 			setTimeout(showHintPost(url + param, str, param,"hint_dr_preowned_preownedstatus_id"),1000);
//	 		setTimeout('RefreshHistory()', 1000);
$.post(url, param, function(data, status) {
			//alert(data.trim());
			if (status == 'success') {
				strtoast = data.trim().replace('<font color=\"red\">', '')
						.replace('</font>', '');
				showToast(strtoast.trim());
			}
		})
		}
</script>

</body>
<!-- END BODY -->
</html>