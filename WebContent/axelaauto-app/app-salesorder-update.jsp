
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Salesorder_Update"
	scope="request" />
<%mybean.doPost(request, response);%>
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
<script>
$(document).ready(function() {
		$("#addbutton").click(function() {
			CheckForm();
		});
	});
	
	var msg = "";
	function CheckForm() {
		msg = "";
		
		var optionid = document.getElementById("dr_option_id").value;
		if (optionid == '0') {
			msg += '<br>Select Colour!'; 
		}
		var bookingamt = document.getElementById("txt_so_booking_amount").value;
		if (bookingamt == '') {
			msg += '<br>Enter Booking Amount!';
		}
		
		var paymentdate = document.getElementById("txt_so_payment_date").value;
		if (paymentdate == '') {
			msg += '<br>Enter Payment Date!';
		}
		
		var promisedate = document.getElementById("txt_so_promise_date").value;
		if (promisedate == '') {
			msg += '<br>Enter Promise Date!';
		}
		
		var panno = document.getElementById("txt_so_pan").value;
		var foem60 = document.getElementById("chk_so_form60");
		if (<%=mybean.comp_id%> != 1009 && panno == ''&& foem60.checked==false) {
			msg += '<br>Enter Pan No. or Form60!';
		}
		
		if (<%=mybean.comp_id%> == 1009 && panno == '') {
			msg += "<br>Enter Pan No.!";
		}
		
		if (msg != '') {
			showToast(msg);
		} else {
			document.getElementById('add_button').value= "yes";
			document.getElementById('frmaddso').submit();
		}
	}

	function FormSubmit() {
		document.frmaddso.submit();
	}
	
	function call(){
		var msg = "<%=mybean.msg%>";
		showToast(msg);
	}
</script>
<style>
table td {
	color: #8E44AD;
	text-align: right;
}
b {
	color: #8E44AD;
}
span{
	color: red;
}

 .panel-heading {
    margin-bottom: 20px;
    background-color: #8E44AD;
    border: 1px solid transparent;
    border-radius: 0px;
    box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}
strong{
color: #fff;
}
.header-wrap{
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
}
</style>
</head>
<body <%if (!mybean.msg.equals("")) {%>
	onload="call();" <%}%>
	>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title">
				<center><strong>Add Sales Order</strong></center>
			</span>
		</div>
	</div>
	
	<div class="container" style="margin-top: 45px;">
		<form role="form" id="frmaddso" name="frmaddso"
			class="form-horizontal" method="post">
			<div class="form-body">
				<div class="form-group">
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Branch: </label>
						<div class="col-md-8 col-xs-8">
							<label for="branch" class="form-control"><%=mybean.branch_name%></label>
							<input type="hidden" id="txt_enquiry_enquirytype_id" name="txt_enquiry_enquirytype_id" value="<%=mybean.enquiry_enquirytype_id%>">
							 <input type="hidden" name="txt_branch_name" id="txt_branch_name" value="<%=mybean.branch_name%>">
							  <input type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%=mybean.branch_id%>">
							   <input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>">
							    <input type="hidden" name="txt_so_grandtotal" id="txt_so_grandtotal" value="<%=mybean.so_grandtotal%>" />
							     <input type="hidden" name="txt_so_discamt" id="txt_so_discamt" value="<%=mybean.so_discamt%>" />
							      <input type="hidden" name="txt_so_netamt" id="txt_so_netamt" value="<%=mybean.so_netamt%>" />
							       <input type="hidden" name="txt_so_totaltax" id="txt_so_totaltax" value="<%=mybean.so_totaltax%>" />
							        <input type="hidden" name="txt_so_contact_id" id="txt_so_contact_id" value="<%=mybean.so_contact_id%>">
							         <input type="hidden" name="txt_branchclass_id" id="txt_branchclass_id" value="<%=mybean.rateclass_id%>">
							          <input type="hidden" id="lead_id" name="lead_id" value="<%=mybean.lead_id%>">
							           <input type="hidden" id="txt_so_item_id" name="txt_so_item_id" value="<%=mybean.so_item_id%>" />
							            <input type="hidden" id="txt_so_exprice" name="txt_so_exprice" value="<%=mybean.so_exprice%>" />
							             <input type="hidden" id="txt_so_quote_id" name="txt_so_quote_id" value="<%=mybean.so_quote_id%>" />
							              <input type="hidden" id="txt_enquiry" name="txt_enquiry" value="<%=mybean.enquiry_id%>" />
							               <input type="hidden" id="txt_contact_email1" name="txt_contact_email1" value="<%=mybean.contact_email1%>" />
							               <input type="hidden" name="txt_session_id" id="txt_session_id" value="<%= mybean.session_id%>" />
							                </div>
					</div>
					
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Date: </label>
						<div class="col-md-8 col-xs-8">
						 <input type="text" class="form-control" name="txt_so_date" id="txt_so_date" onclick="datePicker('txt_date');" value="<%=mybean.sodate%>">
							<input type="hidden" class="form-control" id="txt_so_date"
								name="txt_so_date" value="<%=mybean.sodate%>" readonly>
						</div>
						
						
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Customer: </label>
						<div class="col-md-8 col-xs-8">
							<label for="date" class="form-control"><%=mybean.customer_name%></label>
							<span id="span_so_customer_id" name="span_so_customer_id"><%=mybean.link_customer_name%></span></b>&nbsp;
							<input name="span_acct_id" type="hidden" id="span_acct_id"
								value="<%=mybean.customer_id%>"> <input name="acct_id"
								type="hidden" id="acct_id" value="<%=mybean.so_customer_id%>">
								<input name="txt_customer_name"
								type="hidden" id="txt_customer_name" value="<%=mybean.customer_name%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Contact: </label>
						<div class="col-md-8 col-xs-8">
							<label for="date" class="form-control"><%=mybean.contact_name%></label>
							<%-- <input type="text" class="form-control" value="<%=mybean.link_contact_name %>"> --%>
							<input name="span_cont_id" type="hidden" id="span_cont_id"
								value="<%=mybean.contact_id%>"> <input name="cont_id" type="hidden"
								id="cont_id" value="<%=mybean.so_contact_id%>">
						</div>
					</div>
					
					<%if(!mybean.enquiry_id.equals("0")) {%>
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Enquiry ID: </label>
						<div class="col-md-8 col-xs-8">
							<label for="date" class="form-control"><%=mybean.enquiry_id%></label>
							<input type="hidden" class="form-control" name="txt_so_enquiry_id" id="txt_so_enquiry_id" value="<%=mybean.enquiry_id%>"> 
						</div>
					</div>
					<%}%>
					
					
					<%if(!mybean.so_quote_id.equals("0")) {%>
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Quote ID: </label>
						<div class="col-md-8 col-xs-8">
							<label for="date" class="form-control"><%=mybean.so_quote_id%></label>
<%-- ------							 <input type="text" class="form-control" value="<%=mybean.quote_enquiry%>">  --%>
						</div>
					</div>
					<%}%>
									<%=mybean.GetConfigurationDetails(request)%> 
									 

					<div class="col-md-12">
						<%-- <div class="form-group form-md-line-input">
							<label for="form_control_1">Special Remarks: </label> <input
								type="text" class="form-control"
								id="txt_so_din_accessories_special_remarks"
								name="txt_so_din_accessories_special_remarks" value="<%=mybean.so_din_accessories_special_remarks%>">
						</div> --%>
						
						<div class="form-group form-md-line-input">
						 <%if(mybean.enquiry_enquirytype_id.equals("1")){%>
								<%if(!mybean.so_vehstock_id.equals("0")){%>  
							<label for="form_control_1">Stock ID:</label> <input type="text"
								class="form-control" id="txt_so_vehstock_id" name="txt_so_vehstock_id"
								value="<%=mybean.so_vehstock_id%>"size="15" maxlength="15" onkeyup="toInteger(this.id);">
									<%} %>
						 <%} else if(mybean.enquiry_enquirytype_id.equals("2")){%>
						 <label for="form_control_1">Pre-Owned Stock ID:</label> <input type="text"
								class="form-control" id="txt_so_preownedstock_id" name="txt_so_preownedstock_id"
								value="<%=mybean.so_preownedstock_id%>"size="15" maxlength="15" onkeyup="toInteger(this.id);">
						 <%} %>
						</div>
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Colour<span>*</span>:</label> <select
								class="form-control" name="dr_option_id" id="dr_option_id">
							  <%=mybean.PopulateColour()%>
							</select>
						</div>
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Allotment No.:</label> <input type="text"
								class="form-control" id="txt_so_allot_no" name="txt_so_allot_no"
								value="<%=mybean.so_allot_no %>" size="12" maxlength="10" onkeyup="toInteger(this.id);">
						</div>
						
					<div class="form-group form-md-line-input">
							<label for="form_control_1">Finance Type<span>*</span>:</label> <select
								class="form-control" name="dr_so_fintype" id="dr_so_fintype">
							   <%=mybean.PopulateFinanceType()%>
							</select>
						</div>
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Finance By:</label> <select
								class="form-control" name="dr_finance_by" id="dr_finance_by">
							  <%=mybean.PopulateFinanceBy()%>
							</select>
						</div>
							
							<div class="form-group form-md-line-input">
							<label for="form_control_1">Finance Amount</label><input
								type="tel" class="form-control" id="txt_so_finance_amt"
								name="txt_so_finance_amt" value="<%=mybean.so_finance_amt %>" size="20" maxlength="255">
						</div>
						
						
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Booking Amount<span>*</span>:</label><input
								type="tel" class="form-control" id="txt_so_booking_amount"
								name="txt_so_booking_amount" value="<%=mybean.so_booking_amount %>" size="12" maxlength="10">
						</div>
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Purchase Order:</label><input
								type="tel" class="form-control" id="txt_so_po"
								name="txt_so_po" value="<%=mybean.so_po %>" size="50" maxlength="50" >
						</div>
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Payment Date<span>*</span>:</label> <input
								type="text" class="form-control" name="txt_so_payment_date"  id="txt_so_payment_date"
								onclick="datePicker('txt_so_payment_date');"  value="<%=mybean.so_paymentdate%>" readonly>
						</div>
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Tentative Delivery Date<span>*</span>:</label> <input
								type="text" class="form-control" id="txt_so_promise_date"
								name="txt_so_promise_date" onclick="datePicker('txt_so_promise_date');"  value="<%=mybean.so_promisedate%>" size="12" maxlength="10" readonly>
						</div>
						
<!-- 						<div class="form-group form-md-line-input"> -->
<!-- 							<label for="form_control_1">Retail Date:</label> <input -->
<!-- 								type="text" class="form-control" id="txt_so_retail_date" -->
<%-- 								name="txt_so_retail_date" onclick="datePicker('txt_so_retail_date');"  value="<%=mybean.so_retaildate%>" size="12" maxlength="10" readonly> --%>
<!-- 						</div> -->
						
						<%-- <% if (mybean.emp_receipt_access.equals("1")) { %> --%>
				        
<!-- 						<div class="form-group form-md-line-input"> -->
<!-- 							<label for="form_control_1">Delivered Date:</label><input -->
<!-- 								type="text" class="form-control" id="txt_so_delivered_date" -->
<%-- 								name="txt_so_delivered_date" onclick="datePicker('txt_so_delivered_date');" value="<%=mybean.so_delivereddate%>" size="12" maxlength="10" readonly> --%>
<!-- 						</div> -->
						
					   <%-- <%  } %> --%>
					   
					   
					   <div class="form-group form-md-line-input">
							<label for="form_control_1">Registration No.:</label><input
								type="text" class="form-control" id="txt_so_reg_no"
								name="txt_so_reg_no"  value="<%=mybean.so_reg_no %>" size="22" maxlength="20">
						</div>
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Registration Date:</label><input
								type="text" class="form-control" id="txt_so_reg_date"
								name="txt_so_reg_date" onclick="datePicker('txt_so_reg_date');" value="<%=mybean.so_regdate%>" size="12" maxlength="10" readonly>
						</div>
						
						
						<%-- <div class="form-group form-md-line-input">
							<label for="form_control_1">Hypothecation:</label> <input
								type="text" class="form-control" id="txt_so_hypothecation"
								name="txt_so_hypothecation" value="<%=mybean.so_hypothecation%>" size="32" maxlength="255">
						</div> --%>
						
						<%-- <div class="form-group form-md-line-input">
							<label for="form_control_1">Finance Type<span>*</span>:</label> <select
								class="form-control" name="dr_so_fintype"
								id="dr_so_fintype">
								<%=mybean.PopulateFinanceType()%>
							</select>
						</div> --%>
						
						
						<%-- <div class="form-group form-md-line-input">
							<label for="form_control_1">Finance By<span>*</span>:</label> <select
								class="form-control" name="dr_finance_by" id="dr_finance_by">
								<%=mybean.PopulateFinanceBy()%>
							</select>
						</div> --%>
						
						<%-- <div class="form-group form-md-line-input">
							<label for="form_control_1">Finance Status Description<span>*</span>:</label>
							<input type="text" class="form-control"
								id="txt_so_finstatus_desc" name="txt_so_finstatus_desc" value="<%=mybean.so_finstatus_desc%>" size="32" maxlength="255">
						</div> --%>
						
						<%-- <div class="form-group form-md-line-input">
							<label for="form_control_1">Insurance By:</label> <select
								class="form-control" name="dr_insurance_by" id="dr_insurance_by">
                              <%=mybean.PopulateInsuranceBy()%>
							</select>
						</div> --%>
							<div class="row">

										<div class="col-md-3 col-xs-3">
											<div class="form-group form-md-line-input">
												<label for="form_control_1">
													DOB<span>*</span>:
												</label> <select id="dr_DOBDay" name="dr_DOBDay"
													class="form-control">
													  <%=mybean.PopulateDay(mybean.dr_day)%>
												</select>
											</div>
										</div>
										<div class="col-md-5 col-xs-5">
											<div class="form-group form-md-line-input">
												<label for="form_control_1">
													<div class="hint" id="hint_drop_dob"></div> &nbsp;
												</label> <select id="dr_DOBMonth" name="dr_DOBMonth"
													class="form-control" Placeholder="Month" >
												 <%=mybean.PopulateMonth(mybean.dr_month)%>
												</select>
											</div>
										</div>
										<div class="col-md-4 col-xs-4">
											<div class="form-group form-md-line-input">
												<label for="form_control_1">
													<div class="hint" id="hint_drop_dob"></div> &nbsp;
												</label> <select id="dr_DOBYear" name="dr_DOBYear"
													class="form-control" Placeholder="Year" >
													<%=mybean.PopulateYear(mybean.dr_year)%>
													
												</select>
											</div>
										</div>
									</div>
									
						 <div class="form-group form-md-line-input">
							<label for="form_control_1">Pan No<%if(mybean.comp_id.equals("1009")) {%><span>*</span><%} %>:</label> <input type="text"{
								class="form-control" id="txt_so_pan" name="txt_so_pan"
								value="<%=mybean.so_pan%>" size="10" maxlength="10">
						</div>		
						
						 <div class="form-group form-md-line-input">
							 <input type="checkbox" style="position:absolute; top:20px;"
								id="chk_so_form60" name="chk_so_form60" <%=mybean.PopulateCheck(mybean.so_form60)%>>
							 <label for="form_control_1" style="position:relative; left:20px;">Form 60:</label>
								
						</div>
						
						<%-- 
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Promised Delivery Date<span>*</span>:</label> <input
								type="date" class="form-control" id="txt_so_promise_date"
								name="txt_so_promise_date" value="<%=mybean.so_promisedate%>">
						</div>
						 --%>
						
					
				       
						
						<div class="form-group form-md-line-input">
							<input type="checkbox" style="position:absolute; top:20px;"
							 id="chk_so_open" name="chk_so_open"  <%=mybean.PopulateCheck(mybean.so_open)%>>
							<label for="form_control_1"
								style="position:relative; left:20px;">Sales Order Open
						 </label>
						</div>
						
						<!-- <div class="form-group form-md-line-input">
							<input type="checkbox" id="chk_so_critical"
								name="chk_so_critical"> <label for="form_control_1"
								style="position: relative; bottom: 3px;">Sales Order
								Critical</label>
						</div> -->
						
						<!-- <div class="form-group form-md-line-input">
							<input type="checkbox" id="chk_so_hni" name="chk_so_hni">
							<label for="form_control_1"
								style="position: relative; bottom: 3px;">HNI</label>
						</div> -->
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Sales Consultant<span>*</span>:</label> <select
								 <%if (!mybean.emp_id.equals("1")) {%> disabled
													<%}%>  class="form-control" name="dr_executive" id="dr_executive">
                                	<%=mybean.PopulateExecutive()%>
							</select>
							<%
													if (!mybean.emp_id.equals("1")) {
												%>
												<input hidden id="dr_executive" name="dr_executive"
													value="<%=mybean.so_emp_id%>" />
												<%
													}
												%>
						</div>
						
						 <%if(mybean.config_sales_so_refno.equals("1")){%>
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Sales Order Reference No.<span>*</span>:</label>
							<input type="text" class="form-control"
								id="txt_so_refno" name="txt_so_refno" value="<%=mybean.so_refno%>" size="32" maxlength="50">
						</div>
						 <%}%>
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Cancel Date:</label> <input
								type="text" class="form-control" id="txt_so_cancel_date"
								name="txt_so_cancel_date" onclick="datePicker('txt_so_cancel_date');" value="<%=mybean.so_canceldate%>" size="12" maxlength="10" readonly>
						</div>
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Cancel Reason:</label> <select
								class="form-control" name="dr_cancel_reason" id="dr_cancel_reason">
								<%=mybean.PopulateCancelReason()%>
							</select>
						</div>
						
<!-- 						<div class="form-group form-md-line-input"> -->
<%-- 							<input type="checkbox" id="chk_so_active" name="chk_so_active" <%=mybean.PopulateCheck(mybean.so_active)%>> --%>
<!-- 							<label for="form_control_1" -->
<!-- 								style="position: relative; bottom: 3px;">Active</label> -->
<!-- 						</div> -->
						
						<div class="form-group form-md-line-input">
							<label for="form_control_1">Notes: </label> <input type="text"
								class="form-control" id="txt_so_notes" name="txt_so_notes"
								cols="70" rows="4" value="<%=mybean.so_notes%>"
								 size="32" maxlength="255">
						</div>
						
						<%-- <%if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {%>
						 <div class="form-group form-md-line-input">
						 <label for="form_control_1">Entry By:</label>
							<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.unescapehtml(mybean.so_entry_by)%>" />
						</div>
						
						<div class="form-group form-md-line-input">
						<label for="form_control_1">Entry Date:</label>
							<input type="hidden" id="entry_date" name="entry_date" value="<%=mybean.entry_date%>" />
						</div>
					    <%}%>
					    
					    <% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {%>
						 <div class="form-group form-md-line-input">
						 <label for="form_control_1">Modified By:</label>
							<input type="hidden" id="modified_by" name="modified_by" value="<%=mybean.unescapehtml(mybean.so_entry_by)%>" />
						</div>
						
						<div class="form-group form-md-line-input">
						<label for="form_control_1">Modified Date:</label>
							<input type="hidden" id="modified_date" name="modified_date" value="<%=mybean.modified_date%>" />
						</div>
					    <%}%> --%>
					    
						<br>
						<div class="form-actions noborder">
							<center>
								<button type="button" class="btn1" name="addbutton"
									id="addbutton">ADD SALES ORDER</button>
								<input type="hidden" name="add_button" id="add_button"
									value=""><br>
									<!--  ><br> -->
								<br>
							</center>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>

</body>
<!-- END BODY --->
</html>