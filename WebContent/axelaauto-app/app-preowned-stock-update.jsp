<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Preowned_Stock_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en">
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">
<link href="css/select2.min.css" rel="stylesheet" type="text/css" />
<link href="css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
<style>
b {
	color: #8f3e97;
}

.container {
 	padding-right: 0px; 
 	padding-left: 0px; 
 	margin-right: auto; 
 	margin-left: 5px; 
	margin-top: 45px; 
}

span {
	color: red;
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

.btn1{
/* background-color: #8E44AD; */
}
</style>
</head>
<body <%if(!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%} %>>
	<!-- onLoad="DisplayPreOwned();DisplayModel();" -->
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center><%=mybean.status%> Pre-Owned Stock</center></strong></span>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12">
			<form role="form" id="frmaddpreownedstock" name="frmaddpreownedstock"
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
					
	                 <div class="form-group form-md-line-input">
						<label for="form_control_1">Pre-Owned:</label> 
						<input type="hidden" id="txt_preowned_title" name="txt_preowned_title" value="<%=mybean.preowned_title%>" />
						<%=mybean.preowned_title%> (<%=mybean.preowned_id%>)
<%-- 						<input type="hidden" id="branch_id" name="branch_id" value="<%=mybean.branch_id%>"></input> --%>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1" >Model:</label> 
						<input type="hidden" id="txt_preownedmodel_name" name="txt_preownedmodel_name"
															value="<%=mybean.preownedmodel_name%>" />
														<input type="hidden" id="txt_preownedmodel_id"
															name="txt_preownedmodel_id"
															value="<%=mybean.preownedmodel_id%>" />
													<%=mybean.preownedmodel_name%>
															(<%=mybean.preownedmodel_id%>)
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Variant:</label> 
						<input type="hidden" id="txt_variant_name" name="txt_variant_name" value="<%=mybean.variant_name%>" />
														<input type="hidden" id="txt_variant_id"
															name="txt_variant_id" value="<%=mybean.variant_id%>" />
														<%=mybean.variant_name%>
															(<%=mybean.variant_id%>)
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Status<font color="#ff0000">*</font>:</label>
						<select name="dr_preownedstock_status" class="form-control"
														id="dr_preownedstock_status">
														<%=mybean.PopulatePreownedStockStatus()%>
													</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Type<b><font color="#ff0000">*</font></b>:</label>
						<select name="dr_preownedstock_preownedtype" class="form-control"
														id="dr_preownedstock_preownedtype">
													<%=mybean.PopulatePreownedType()%>
													</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Purchase Amount<b><font color="#ff0000">*</font></b>:</label>
						<input name="txt_preownedstock_purchase_amt"
														id="txt_preownedstock_purchase_amt"
														onKeyUp="toFloat('txt_preownedstock_purchase_amt','amt')"
														type="text" class="form-control"
														value="<%=mybean.preownedstock_purchase_amt%>" size="20"
														maxlength="15" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Refurbishment Amount:</label>
						<input name="txt_preownedstock_refurbish_amt"
														id="txt_preownedstock_refurbish_amt"
														onKeyUp="toInteger('txt_preownedstock_refurbish_amt','amt')"
														type="text" class="form-control"
														value="<%=mybean.preownedstock_refurbish_amt%>" size="20"
														maxlength="10" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Selling Price<b><font color="#ff0000">*</font></b>:</label>
						<input name="txt_preownedstock_selling_price"
														id="txt_preownedstock_selling_price"
														onKeyUp="toFloat('txt_preownedstock_selling_price','price')"
														type="text" class="form-control"
														value="<%=mybean.preownedstock_selling_price%>" size="20"
														maxlength="15" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">
						Engine Number<b><font color="#ff0000">*</font></b>:
						</label>
						<input name="txt_preownedstock_engine_no"
														id="txt_preownedstock_engine_no" type="text"
														class="form-control"
														value="<%=mybean.preownedstock_engine_no%>" size="20"
														maxlength="20" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">
						Chassis Number<b><font color="#ff0000">*</font></b>:
						</label>
						<input name="txt_preownedstock_chassis_no"
														id="txt_preownedstock_chassis_no" type="text"
														class="form-control"
														value="<%=mybean.preownedstock_chassis_no%>" size="20"
														maxlength="20" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">
					Trade-in Contract:
						</label>
						<input id="chk_preownedstock_check_tradein_contract"
														type="checkbox"
														name="chk_preownedstock_check_tradein_contract" style="margin-left:5px;position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_tradein_contract)%> />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">
					R.C.(Original):
						</label>
						<input id="chk_preownedstock_check_original_rc"
														type="checkbox" name="chk_preownedstock_check_original_rc" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_original_rc)%> />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">
					Insurance(Policy + Cover Note) Minimum Third Party insurance required in
													case of NCB/insurance transfer case and application for
													insurance transfer: <input id="chk_preownedstock_check_insurance"
														type="checkbox" name="chk_preownedstock_check_insurance"  style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_insurance)%> />
						</label>
						
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1"><b>Forms:</b>
						</label>
						
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">a. 28*3:
						</label>
						<input id="chk_preownedstock_check_forms_283"
														type="checkbox" name="chk_preownedstock_check_forms_283" style="margin-left:5px;position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_forms_283)%> />
						
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">b. 29*2:
						</label>
						<input id="chk_preownedstock_check_forms_292" type="checkbox" name="chk_preownedstock_check_forms_292" style="margin-left:5px;position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_forms_292)%> />
						
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">c. 30*2:
						</label>
						<input id="chk_preownedstock_check_forms_302"
														type="checkbox" name="chk_preownedstock_check_forms_302" style="margin-left:5px;position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_forms_302)%> />
						
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">d. Form 35*2 with NOC of Bank in case of Hypothecation:
						<input id="chk_preownedstock_check_forms_352"
														type="checkbox" name="chk_preownedstock_check_forms_352" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_forms_352)%> />
						</label>
						
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1"><b>Affidavits:</b>
						</label>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">a. For Sale:
						</label>
						<input id="chk_preownedstock_check_aff_sale"
														type="checkbox" name="chk_preownedstock_check_aff_sale" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_aff_sale)%> />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">b. For Hypothecation Removal for R/C:
						</label>
						<input id="chk_preownedstock_check_aff_hypo"
														type="checkbox" name="chk_preownedstock_check_aff_hypo" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_aff_hypo)%> />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">c. For NOC From RTO:
						</label>
						<input id="chk_preownedstock_check_aff_noc" type="checkbox"
														name="chk_preownedstock_check_aff_noc" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_aff_noc)%> />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">d. Power of Attorney to Lally Motors Ltd.:
						</label>
					<input id="chk_preownedstock_check_aff_poa" type="checkbox"
														name="chk_preownedstock_check_aff_poa" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_aff_poa)%> />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Three
													Photographs: 
						</label>
						<input id="chk_preownedstock_check_photographs"
														type="checkbox" name="chk_preownedstock_check_photographs" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_photographs)%> />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1"><b>Residence/Address Proof Copies:</b>
						</label>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">a. Ration Card/Voter Card/PassPort:
						</label>
						<input id="chk_preownedstock_check_rationcard"
														type="checkbox" name="chk_preownedstock_check_rationcard" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_rationcard)%> />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">b. Telephone/Electricity/Water Bill:
						</label>
						<input id="chk_preownedstock_check_telebill"
														type="checkbox" name="chk_preownedstock_check_telebill" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_telebill)%> />
					</div>
					<br><br>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">PAN No. Copy(self attested) or form 60 & 61(2 copies each):<input id="chk_preownedstock_check_pancopy" type="checkbox"
														name="chk_preownedstock_check_pancopy" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_pancopy)%> />
						</label>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Mileage Verification Certificate:
						</label>
							<input id="chk_preownedstock_check_mileage_verification"
															type="checkbox" 
															name="chk_preownedstock_check_mileage_verification" style="position:absolute;"
															<%=mybean.PopulateCheck(mybean.preownedstock_check_mileage_verification)%> />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Second Key Of Car,Tool Kit,Spare
													Wheel,Service Book,Chassis Print:
													<input id="chk_preownedstock_check_servicebook"
														type="checkbox" name="chk_preownedstock_check_servicebook" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_servicebook)%> />
						</label>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Authority
													Letter For NOC From Bank in case of Finance:
													<input id="chk_preownedstock_check_noc_authority"
														type="checkbox"
														name="chk_preownedstock_check_noc_authority" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_noc_authority)%> />
						</label>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">F.O.P
													Report For Registration From Authority and N.C.R.B Report
													From Police Deptt.:
													<input id="chk_preownedstock_check_fop_ncrb"
														type="checkbox" name="chk_preownedstock_check_fop_ncrb" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_fop_ncrb)%> />
						</label>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Copy
													of Partnership Deed if car is of company & having
													Partnership & no. objection by remaining partners for
													selling Company's Car:<input id="chk_preownedstock_check_partnerdeed"
														type="checkbox" name="chk_preownedstock_check_partnerdeed" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_partnerdeed)%> />
						</label>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Original
													Board Resolution & Memorandum of Article if Company is
													limited/Private Limited & 3 Original copies of
													Authorization Letter By Remaining Director for selling
													Company's Car duly signed & stamped: <input id="chk_preownedstock_check_moa" type="checkbox"
														name="chk_preownedstock_check_moa" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_check_moa)%> />
						</label>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Location<b><font color="#ff0000">*</font></b>:
						</label>
						<select name="dr_preownedstock_preownedlocation"
														class="form-control"
														id="dr_preownedstock_preownedlocation">
														<%=mybean.PopulatePreownedLocation()%>
													</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">SO ID:
						</label>
						<input name="txt_preownedstock_so_id" type="text"
														class="form-control" id="txt_preownedstock_so_id"
														onKeyUp="toInteger('txt_preownedstock_so_id','so_id')"
														value="<%=mybean.preownedstock_so_id%>" size="11"
														maxlength="10" />

					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Blocked:
						</label>
						<input id="chk_preownedstock_blocked" type="checkbox"
														name="chk_preownedstock_blocked" style="position:absolute;"
														<%=mybean.PopulateCheck(mybean.preownedstock_blocked)%> />

					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Pre-Owned Consultant<b><font color="#ff0000">*</font></b>:
						</label>
						<select name="dr_preownedstock_executive"
														class="form-control" id="dr_preownedstock_executive">
														<%=mybean.PopulatePreownedStockExecutive()%>
													</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Notes:
						</label>
						<textarea name="txt_preownedstock_notes" cols="70" rows="5" class="form-control" id="txt_preownedstock_notes"><%=mybean.preownedstock_notes%></textarea>
					</div>
					
					
					<% if (mybean.status.equals("Update") && !(mybean.stock_entry_by == null) && !(mybean.stock_entry_by.equals(""))) { %>
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Entry By:
						</label>
						<%=mybean.unescapehtml(mybean.stock_entry_by)%>
					</div>
					<%} %>
					
					<% if (mybean.status.equals("Update") && !(mybean.preownedstock_entry_date == null) && !(mybean.preownedstock_entry_date.equals(""))) { %>
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Entry Date:
						</label>
					<%=mybean.preownedstock_entry_date%>
					</div>
					
					<%} %>
					
					<% if (mybean.status.equals("Update") && !(mybean.stock_modified_by == null) && !(mybean.stock_modified_by.equals(""))) { %>
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Modified By:
						</label>
					<%=mybean.unescapehtml(mybean.stock_modified_by)%>
					</div>
					
					<%} %>
					
					<% if (mybean.status.equals("Update") && !(mybean.preownedstock_modified_date == null) && !(mybean.preownedstock_modified_date.equals(""))) { %>
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Modified Date:
						</label>
					<%=mybean.preownedstock_modified_date%>
					</div>
					<%} %>
					
					<br>
					<div class="form-actions noborder">
						<center>
								<button type="button" class="btn1" name="addbutton" id="addbutton" >Add Stock</button>
<!-- 							<input name="addbutton" id ="addbutton" type="button" class="btn1"  value="Add Enquiry" onClick="return SubmitFormOnce(document.frmaddenquiry, this);" /> -->
								<input type="hidden"  name="add_button" id ="add_button" value="yes"></input>
<%-- 								<input type="hidden" id="branchtype_id" value="<%=mybean.branchtype_id%>"></input> --%>
<%-- 									<input class="form-control" type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%=mybean.enquiry_branch_id%>" /> --%>

<!-- <button type="button" class="btn1" name="addbutton"  -->
<!-- 								id="addbutton" >Add Enquiry</button> -->
<!-- 								<input type="hidden"  name="add_button" id="add_button" value=""></input> -->
								
								
						</center>
						<br>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 	<script src="js/jquery.min.js" type="text/javascript"></script> -->
<!-- <script src="js/bootstrap.js" type="text/javascript"></script> -->
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<!-- 	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script> -->
<!-- <script src="../assets/js/components-select2.min.js" type="text/javascript"></script> -->
<!--  <script src="js/jquery-ui.js" type="text/javascript"></script>	 -->
<script src="js/jquery-ui.js" type="text/javascript"></script>	
<script src="js/jquery.app.js" type="text/javascript"></script>
<script src="js/jquery.min.js" type="text/javascript"></script>
<!-- 	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script> -->
<script src="js/select2.full.min.js" type="text/javascript"></script>
<script src="js/components-select2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Validate.js"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>
<script>

$(document).ready(function() {
	$("#addbutton").click(function() {
		checkForm();
	});
});

</script>

<script>
	var msg = "";
	function checkForm() {
		msg = "";
		var status_id = document.getElementById("dr_preownedstock_status").value;
		if (status_id == '0') {
			msg += '<br>Select Status!';
		}
		var type_id = document.getElementById("dr_preownedstock_preownedtype").value;
		if (type_id == '0') {
			msg += '<br>Select Type!';
		}
		var purchase_amt = document.getElementById("txt_preownedstock_purchase_amt").value;
		if (purchase_amt == '') {
			msg += '<br>Enter Purchase Amount!';
		}
		var selling_price = document.getElementById("txt_preownedstock_selling_price").value;
		if (selling_price == '' || selling_price == '0') {
			msg += '<br>Enter Selling Price!';
		}
		var engine_no = document.getElementById("txt_preownedstock_engine_no").value;
		if (engine_no == '' || engine_no == '0') {
			msg += '<br>Enter Engine Number!';
		}
		var chassis_no = document.getElementById("txt_preownedstock_chassis_no").value;
		if (chassis_no == '' || chassis_no == '0') {
			msg += '<br>Enter Chassis  Number!';
		}
		var location = document.getElementById("dr_preownedstock_preownedlocation").value;
		if (location == '0') {
			msg += '<br>Select Location!';
		}
		var executive_id = document.getElementById("dr_preownedstock_executive").value;
		if (executive_id == '0') {
			msg += '<br>Select Pre-Owned Consultant!';
		}
		if (msg != '') {
			showToast(msg);
		} 
		else {
			document.getElementById('add_button').value = "yes";
			document.getElementById('frmaddpreownedstock').submit();
		}
	}
    </script>
</body>
<!-- END BODY -->
</html>