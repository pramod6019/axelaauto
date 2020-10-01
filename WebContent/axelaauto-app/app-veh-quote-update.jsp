<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.axelaauto_app.App_Veh_Quote_Update" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>

<html lang="en">
<head>

<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>

<script src="js/axelamobilecall.js" type="text/javascript"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Validate.js"></script>
<script type="text/javascript" src="js/veh-quote.js"></script>

<script>
	$(document).ready(function() {
		$("#addbutton").click(function() {
			checkForm();
		});
	});
	var msg = "";
	function checkForm() {
		msg = "";
		var financetype = document.getElementById("dr_quote_inscomp_id").value;
		var brand_id = document.getElementById("brand_id").value;
		
		if (financetype == '0' && brand_id != 60) {
			msg += '<br>Select Insurance Company!';
		}
// 		var executive = document.getElementById("dr_executive").value;
// 		if (executive == '0') {
// 			msg += '<br>Select Consultant!';
// 		}

		if (msg != '') {
			showToast(msg);
		} else {
			document.getElementById('frmaddquote').submit();
		}
	}

	function FormSubmit() {
		document.frmaddquote.submit();
	}
</script>
<style>
table td {
	color: #8E44AD;
}

b {
	color: #8E44AD;
}

.panel-heading {
	margin-bottom: 20px;
	background-color: #8E44AD;
	border: 1px solid transparent;
	border-radius: 0px;
	box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}

strong {
	color: #fff;
}

.header-wrap {
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
}
</style>
</head>

<body <%if (!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%}%>>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Add
						Quote</center></strong></span>
		</div>
	</div>
	<div class="container" style="margin-top: 25px;">
		<form role="form" id="frmaddquote" name="frmaddquote"
			class="form-horizontal" method="post">
			<div class="form-body">

				<div class="alert alert-danger display-hide">
					<button class="close" data-close="alert"></button>
					You have some form errors. Please check below.
				</div>
				<div class="alert alert-success display-hide">
					<button class="close" data-close="alert"></button>
					Your form validation is successful!
				</div>

				<div class="form-group">
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Branch: </label>
						<div class="col-md-8 col-xs-8">
							<label for="branch" class="form-control"><%=mybean.branch_name%></label>
							<input type="hidden" name="dr_branch" id="dr_branch"
								value="<%=mybean.branch_id%>">
						</div>
					</div>

					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Date: </label>
						<div class="col-md-8 col-xs-8">
							<%-- 							<label for="date" class="form-control"><%=mybean.quotedate%></label> --%>
							<input type="text" class="form-control" name="txt_quote_date"
								id="txt_quote_date"
								value="<%=mybean.quotedate%>" readonly> <input
								type="hidden" class="form-control" id="txt_quote_date"
								name="txt_quote_date" value="<%=mybean.quotedate%>">
						</div>
					</div>

					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Account: </label>
						<div class="col-md-8 col-xs-8">
							<label for="date" class="form-control"><%=mybean.customer_name%></label>
							<%-- <input type="text" class="form-control" value="<%=mybean.link_customer_name%>"> --%>
							<input name="span_acct_id" type="hidden" id="span_acct_id"
								value="<%=mybean.customer_id%>"> <input name="acct_id"
								type="hidden" id="acct_id" value="<%=mybean.quote_customer_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Contact: </label>
						<div class="col-md-8 col-xs-8">
							<label for="date" class="form-control"><%=mybean.link_contact_name1%></label>
							<%-- <input type="text" class="form-control" value="<%=mybean.link_contact_name %>"> --%>
							<input name="span_cont_id" type="hidden" id="span_cont_id"
								value="<%=mybean.contact_id%>"> <input name="cont_id"
								type="hidden" id="cont_id" value="<%=mybean.quote_contact_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Enquiry ID: </label>
						<div class="col-md-8 col-xs-8">
							<label for="date" class="form-control"><%=mybean.quote_enquiry_id%></label>
							<%-- <input type="text" class="form-control" value="<%=mybean.quote_enquiry_id%>"> --%>
						</div>
					</div>


					<%=mybean.GetConfigurationDetails(request, mybean.item_id,
					mybean.branch_id, mybean.vehstock_id,
					mybean.emp_quote_discountupdate, mybean.quote_date, "0")%>

					<% if ((!mybean.vehstock_id.equals("0") && !mybean.vehstock_id.equals(""))
		|| (!mybean.quote_vehstock_id.equals("0") && !mybean.quote_vehstock_id .equals(""))) { %> 
					<div class="col-md-12">
					<div class="form-group form-md-line-input">
						<label for="form_control_1" >Stock ID:</label>
						 <input name="txt_quote_vehstock_id" type="text" id="txt_quote_vehstock_id"
							size="15" maxlength="10" class="form-control"
							value="<%=mybean.quote_vehstock_id%>" onkeyup="toInteger(this.id);" />
					</div>
					</div>
					<%} %>
					<% if ((!mybean.vehstock_id.equals("0") && !mybean.vehstock_id.equals(""))
		|| (!mybean.quote_preownedstock_id.equals("0") && !mybean.quote_preownedstock_id .equals(""))) { %>
		<div class="col-md-12">
					<div class="form-group form-md-line-input">
						<label for="form_control_1" >Pre-Owned Stock ID:</label> <input
							name="txt_quote_preownedstock_id" type="text"
							id="txt_quote_preownedstock_id" size="15" maxlength="10"
							class="form-control" value="<%=mybean.quote_preownedstock_id%>"
							onkeyup="toInteger(this.id);" />
					</div>
					<br><br>
					</div>
					<%} %>



					<!-- 					<div class="col-md-12"> -->
					<!-- 						<div class="form-group form-md-line-input"> -->
					<!-- 							<label for="form_control_1">Stock Comm. No.: </label> <input -->
					<!-- 								type="text" class="form-control" id="txt_stock_comm_no" -->
					<%-- 								name="txt_stock_comm_no" value="<%=mybean.stock_comm_no%>"> --%>
					<!-- 						</div> -->
					<!-- 						<div class="form-group form-md-line-input"> -->
					<!-- 							<label for="form_control_1">Hypothecation:</label> <input -->
					<!-- 								type="text" class="form-control" id="txt_quote_hypothecation" -->
					<%-- 								name="txt_quote_hypothecation" value="<%=mybean.quote_hypothecation%>" size="32" maxlength="255"> --%>
					<!-- 						</div> -->

					<!-- 						<div class="form-group form-md-line-input"> -->
					<!-- 							<label for="form_control_1">Finance Type *:</label> -->
					<!-- 							 <select class="form-control" name="dr_quote_fintype" id="dr_quote_fintype"> -->
					<%-- 								<%=mybean.PopulateFinanceType()%> --%>
					<!-- 							</select> -->
					<!-- 						</div> -->





					<div class="  panel-heading"
						style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; margin-left: 0px">
						<span class="panel-title">
							<center>
								<h4>
									<strong>Finance Proposal</strong>
								</h4>
							</center>
						</span>
					</div>
					<br>


					<div class="col-md-12">

						<div class="form-group form-md-line-input">
							<label for="form_control_1">Finance Options:</label> <select
								id="dr_quote_fin_option1" name="dr_quote_fin_option1"
								class="form-control">
								<%=mybean.PopulateFinOption(mybean.quote_fin_option1)%>
							</select>
						</div>

						<div class="form-group form-md-line-input">
							<select id="dr_quote_fin_option2" name="dr_quote_fin_option2"
								class="form-control">
								<%=mybean.PopulateFinOption(mybean.quote_fin_option2)%>
							</select>
						</div>

						<div class="form-group form-md-line-input">
							<select id="dr_quote_fin_option3" name="dr_quote_fin_option3"
								class="form-control">
								<%=mybean.PopulateFinOption( mybean.quote_fin_option3)%>
							</select>
						</div>


						<div class="form-group form-md-line-input">
							<label for="form_control_1">Loan Amount:</label> <input
								name="txt_quote_fin_loan1" type="text" class="form-control"
								id="txt_quote_fin_loan1" value="<%=mybean.quote_fin_loan1%>"
								size="15" maxlength="10" onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_loan2" type="text"
								class="form-control" id="txt_quote_fin_loan2"
								value="<%=mybean.quote_fin_loan2%>" size="15" maxlength="10"
								onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_loan3" type="text"
								class="form-control" id="txt_quote_fin_loan3"
								value="<%=mybean.quote_fin_loan3%>" size="15" maxlength="10"
								onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<label for="form_control_1">Tenure in Months:</label> <input
								name="txt_quote_fin_tenure1" type="text" class="form-control"
								id="txt_quote_fin_tenure1" value="<%=mybean.quote_fin_tenure1%>"
								size="15" maxlength="10" onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_tenure2" type="text"
								class="form-control" id="txt_quote_fin_tenure2"
								value="<%=mybean.quote_fin_tenure2%>" size="15" maxlength="10"
								onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_tenure3" type="text"
								class="form-control" id="txt_quote_fin_tenure3"
								value="<%=mybean.quote_fin_tenure3%>" size="15" maxlength="10"
								onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<label for="form_control_1">No. of Advance E.M.I:</label> <input
								name="txt_quote_fin_adv_emi1" type="text" class="form-control"
								id="txt_quote_fin_adv_emi1"
								value="<%=mybean.quote_fin_adv_emi1%>" size="15" maxlength="10"
								onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_adv_emi2" type="text"
								class="form-control" id="txt_quote_fin_adv_emi2"
								value="<%=mybean.quote_fin_adv_emi2%>" size="15" maxlength="10"
								onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_adv_emi3" type="text"
								class="form-control" id="txt_quote_fin_adv_emi3"
								value="<%=mybean.quote_fin_adv_emi3%>" size="15" maxlength="10"
								onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<label for="form_control_1">E.M.I./Rental:</label> <input
								name="txt_quote_fin_emi1" type="text" class="form-control"
								id="txt_quote_fin_emi1" value="<%=mybean.quote_fin_emi1%>"
								size="15" maxlength="10" onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_emi2" type="text" class="form-control"
								id="txt_quote_fin_emi2" value="<%=mybean.quote_fin_emi2%>"
								size="15" maxlength="10" onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_emi3" type="text" class="form-control"
								id="txt_quote_fin_emi3" value="<%=mybean.quote_fin_emi3%>"
								size="15" maxlength="10" onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<label for="form_control_1">Net Processing Fee:</label> <input
								name="txt_quote_fin_fee1" type="text" class="form-control"
								id="txt_quote_fin_fee1" value="<%=mybean.quote_fin_fee1%>"
								size="15" maxlength="10" onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_fee2" type="text" class="form-control"
								id="txt_quote_fin_fee2" value="<%=mybean.quote_fin_fee2%>"
								size="15" maxlength="10" onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_fee3" type="text" class="form-control"
								id="txt_quote_fin_fee3" value="<%=mybean.quote_fin_fee3%>"
								size="15" maxlength="10" onkeyup="toInteger(this.id)" />
						</div>


						<div class="form-group form-md-line-input">
							<label for="form_control_1">Net Down Payment with/<br />
								Without Optional Packages<br /> &amp; Accessories:
							</label> <input name="txt_quote_fin_downpayment1" type="text"
								class="form-control" id="txt_quote_fin_downpayment1"
								value="<%=mybean.quote_fin_downpayment1%>" size="15"
								maxlength="10" onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_downpayment2" type="text"
								class="form-control" id="txt_quote_fin_downpayment2"
								value="<%=mybean.quote_fin_downpayment2%>" size="15"
								maxlength="10" onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<input name="txt_quote_fin_downpayment3" type="text"
								class="form-control" id="txt_quote_fin_downpayment3"
								value="<%=mybean.quote_fin_downpayment3%>" size="15"
								maxlength="10" onkeyup="toInteger(this.id)" />
						</div>

						<div class="form-group form-md-line-input">
							<label for="form_control_1">Insurance Company *:</label> <select
								class="form-control" name="dr_quote_inscomp_id"
								id="dr_quote_inscomp_id">
								<%=mybean.PopulateInsuranceCompany(mybean.comp_id)%>
							</select>
						</div>

						<div class="form-group form-md-line-input">
							<label for="form_control_1">Sales Consultant *:</label> <select
								class="form-control" name="dr_executive" id="dr_executive">
								<%=mybean.PopulateExecutive(mybean.branch_id)%>
							</select>
						</div>
						<div class="form-group form-md-line-input">
							<input type="checkbox" id="chk_quote_active"
								style="position: relative;" name="chk_quote_active"
								<%=mybean.PopulateCheck(mybean.quote_active)%>> <label
								for="form_control_1" style="position: absolute;">Active</label>
						</div>
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Notes: </label> <input type="text"
								class="form-control" id="txt_quote_notes" name="txt_quote_notes"
								value="<%=mybean.quote_notes%>" size="32" maxlength="255">
						</div>
						<br>
						<div class="form-actions noborder">
							<center>
								<button type="button" class="btn1" name="addbutton"
									id="addbutton">Add Quote</button>
								<input type="hidden" name="add_button" id="add_button"
									value="yes"><br> <br>
									<input type="hidden" name="brand_id" id="brand_id"
									value="<%=mybean.brand_id%>"/>
							</center>
						</div>
					</div>



				</div>
			</div>
		</form>
	</div>

	<table>
		<tr>
			<td><input type="hidden" id="txt_enquiry_enquirytype_id"
				name="txt_enquiry_enquirytype_id"
				value="<%=mybean.enquiry_enquirytype_id%>"> <input
				type="hidden" name="dr_item_id" id="dr_item_id"
				value="<%=mybean.item_id%>"> <input type="hidden"
				name="dr_model_id" id="dr_model_id" value="<%=mybean.model_id%>">
			</td>
			<td align="right" valign="top"><input type="hidden"
				name="txt_branch_id" id="txt_branch_id"
				value="<%=mybean.branch_id%>"> <input type="hidden"
				id="emp_quote_priceupdate" name="emp_quote_priceupdate"
				value="<%=mybean.emp_quote_priceupdate%>"> <input
				type="hidden" id="emp_quote_discountupdate"
				name="emp_quote_discountupdate"
				value="<%=mybean.emp_quote_discountupdate%>"> <input
				type="hidden" id="txt_status" name="txt_status"
				value="<%=mybean.status%>"> <input type="hidden"
				id="quote_contact" name="quote_contact" value="" /> <input
				type="hidden" id="txt_rateclass_id" name="txt_rateclass_id"
				value="<%=mybean.rateclass_id%>"> <input type="hidden"
				id="txt_enquiry_id" name="txt_enquiry_id"
				value="<%=mybean.quote_enquiry_id%>"> <input type="hidden"
				id="txt_vehstock_block" name="txt_vehstock_block"
				value="<%=mybean.vehstock_block%>"> <input type="hidden"
				id="lead_id" name="lead_id" value="<%=mybean.lead_id%>"> <input
				type="hidden" id="txt_item_id" name="txt_item_id"
				value="<%=mybean.item_id%>"> <input type="hidden"
				id="txt_so_id" name="txt_so_id" value="<%=mybean.so_id%>"> <input
				type="hidden" id="txt_model_id" name="txt_model_id"
				value="<%=mybean.model_id%>"> <input type="hidden"
				id="txt_total_disc" name="txt_total_disc"> <input
				type="hidden" id="txt_bt_total_disc" name="txt_bt_total_disc">
				<input type="hidden" id="txt_item_netprice" name="txt_item_netprice"
				value="<%=mybean.item_netprice%>"> <input type="hidden"
				id="txt_item_netdisc" name="txt_item_netdisc"
				value="<%=mybean.item_netdisc%>"> <input type="hidden"
				id="txt_quote_netamt" name="txt_quote_netamt"
				value="<%=mybean.quote_netamt%>" /> <input type="hidden"
				id="txt_quote_discamt" name="txt_quote_discamt"
				value="<%=mybean.quote_discamt%>" /> <input type="hidden"
				id="txt_quote_totaltax" name="txt_quote_totaltax"
				value="<%=mybean.quote_totaltax%>" /></td>
		</tr>
	</table>




</body>


</html>