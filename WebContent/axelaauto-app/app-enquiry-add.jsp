<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Enquiry_Add"
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
			<span class="panel-title"> <strong><center>Add Enquiry</center></strong></span>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12">
			<form role="form" id="frmaddenquiry" name="frmaddenquiry"
				class="form-horizontal" method="post">
				<div class="form-body">


<!-- =========================================== -->

<!-- <div class="container-fluid" style="min-height:10px"></div> -->
<!-- <div class="modal fade" id="Hintclicktocall" role="basic" aria-hidden="true" style="transform:translate(0,50%)"> -->
<!-- 		<div class="modal-dialog"> -->
<!-- 			<div class="modal-content"> -->
<!-- 				<div class="modal-body"> -->
<!-- 						 <span> &nbsp;&nbsp;Loading... </span> -->
<!-- 						 <br><br> -->
						 
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->





<!-- ===================================================== -->
					<div class="alert alert-danger display-hide">
						<button class="close" data-close="alert"></button>
						You have some form errors. Please check below.
					</div>
					<div class="alert alert-success display-hide">
						<button class="close" data-close="alert"></button>
						Your form validation is successful!
					</div>
					
					<%if(Integer.parseInt(mybean.branchcount) > 1){%>
	                 <div class="form-group form-md-line-input">
						<label for="form_control_1">Branch<span>*</span>:</label> 
						<select class="form-control" name="dr_branch_id"
							     id="dr_branch_id" onchange="submit()">
							      <%=mybean.PopulateBranches(mybean.branch_id, mybean.comp_id)%>
						</select>
						<input type="hidden" id="branch_id" name="branch_id" value="<%=mybean.branch_id%>"></input>
					</div>
					<%} %>
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Customer:</label>  -->
<!-- 						<input type="text" class="form-control" id="txt_customer_name" -->
<!-- 							   name="txt_customer_name" value=""> -->
<!-- 					</div> -->
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Contact Title<span>*</span>:</label> 
						<select class="form-control" name="dr_enquiry_title_id"
							     id="dr_enquiry_title_id">
							     <%=mybean.PopulateTitle(mybean.enquiry_title_id)%>
						</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">First Name<span>*</span>:</label>
						 <input type="text" class="form-control" id="txt_enquiry_fname"
							   name="txt_enquiry_fname" value="<%=mybean.enquiry_fname%>" size="32" maxlength="255">
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Last Name<span>*</span>:</label> 
						<input type="text" class="form-control" id="txt_enquiry_lname"
							   name="txt_enquiry_lname" value="<%=mybean.enquiry_fname%>" size="32" maxlength="255">
					</div>
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Job Title:</label>  -->
<!-- 						<input type="text" class="form-control" id="txt_contact_jobtitle" -->
<%-- 							  name="txt_contact_jobtitle" value="<%=mybean.contact_jobtitle%>" size="32" maxlength="255"> --%>
<!-- 					</div> -->

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Mobile1<span>*</span>:</label> 
						<input type="tel" class="form-control"
						onKeyup="toPhone('txt_contact_mobile1','Contact Mobile1');
								showHint('../axelaauto-app/app-enquiry-check.jsp?enquiry_branch_id='+<%=mybean.branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontacts');"
						 name="txt_contact_mobile1"
							   id="txt_contact_mobile1" 
							   maxlength="13"  value="<%=mybean.contact_mobile1%>">
							   
					</div>
					(91-9999999999)
					<br><span id="showcontacts"></span>
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Mobile2:</label>  -->
<!-- 						<input type="tel" class="form-control" name="txt_contact_mobile2" -->
<!-- 							   id="txt_contact_mobile2" placeholder="(9999999999)" -->
<%-- 							   maxlength="10" value="<%=mybean.contact_mobile2%>"> --%>
<!-- 					</div>  -->
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Phone 1<span>*</span>:</label>  -->
<!-- 						<input type="tel" class="form-control" name="txt_contact_phone1" -->
<!-- 							   id="txt_contact_phone1" placeholder="(080-33333333)" -->
<%-- 							   maxlength="12" value="<%=mybean.contact_phone1%>"> --%>
<!-- 					</div> -->
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Phone 2:</label>  -->
<!-- 						<input type="tel" class="form-control" name="txt_contact_phone2" -->
<!-- 							     id="txt_contact_phone2"  placeholder="(080-33333333)" -->
<%-- 							     maxlength="12" value="<%=mybean.contact_phone2%>"> --%>
<!-- 					</div> -->
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Email:</label> 
						<input type="text" class="form-control" id="txt_contact_email1"
						onkeyup="showHint('../axelaauto-app/app-enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&enquiry_branch_id='+<%=mybean.enquiry_branch_id%>+'&contact_email=' + GetReplace(this.value),'showcontactsemail');"
							   name="txt_contact_email1" value="<%=mybean.contact_email1%>"
							   size="32" maxlength="100" >
					</div>
					<br><span id="showcontactsemail"></span>
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Email2:</label>  -->
<!-- 						<input type="text" class="form-control" id="txt_contact_email2" -->
<%-- 							   name="txt_contact_email2" value="<%=mybean.contact_email2%>" --%>
<!-- 							   size="32" maxlength="100" > -->
<!-- 					</div> -->
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Address:</label>  -->
<!-- 						<textarea type="text" class="form-control" id="txt_contact_address" -->
<!-- 							     name="txt_contact_address" cols="60" rows="2"  -->
<%-- 						       	   > <%=mybean.contact_address %></textarea> --%>
<!-- 					</div> -->
					
<!-- 						<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">City<span>*</span>:</label>  -->
<!-- 						<select class="form-control" name="dr_city_id" -->
<!-- 							     id="dr_city_id"> -->
<%-- 							      <%=mybean.PopulateCity()%> --%>
<!-- 						</select> -->
<!-- 					</div> -->
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Pin/Zip:</label> -->
<!-- 						 <input type="tel" class="form-control" id="txt_contact_pin" -->
<!-- 							    name="txt_contact_pin"   -->
<%-- 							    value="<%=mybean.contact_pin%>" size="10" maxlength="6" > --%>
<!-- 					</div> -->
					
					
					
				                 	<!----- Enquiry Details -->
					
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Date<span>*</span>:</label>  -->
<%-- 						<%if (mybean.emp_enquiry_edit.equals("1")) { %> --%>
					
<!-- 						<input type="date" -->
<!-- 							class="form-control" id="txt_enquiry_date" -->
<!-- 							name="txt_enquiry_date"   -->
<%-- 							value="<%=mybean.enquiry_date%>" size="12" maxlength="10" > --%>
							
<!-- 					</div> -->
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Closing Date<span>*</span>:</label>  -->
<!-- 						<input type="date" class="form-control" id="txt_enquiry_close_date"  -->
<%-- 							   name="txt_enquiry_close_date" value="<%=mybean.enquiry_close_date%>" --%>
<!-- 							   size="12" maxlength="10" > -->
<!-- 					</div> -->
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Description:</label>  -->
<!-- 						<textarea type="text" class="form-control" id="txt_enquiry_desc" -->
<!-- 							     name="txt_enquiry_desc" cols="60" rows="2"  -->
<%-- 						       	  ><%=mybean.enquiry_desc%></textarea> --%>
<!-- 					</div> -->
					
<!-- 					<div id="model" class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Type<span>*</span>:</label> -->
<!-- 						 <select class="form-control" name="dr_enquiry_enquirytype_id" -->
<!-- 							     id="dr_enquiry_enquirytype_id" onchange="DisplayPreowned();DisplayModel();"> -->
<%-- 							    <%=mybean.PopulateType(mybean.comp_id )%> --%>
<!-- 						</select> -->
<!-- 					</div> -->
					
<!-- 					<div id="model" class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Fuel Type<span>*</span>:</label> -->
<!-- 						 <select class="form-control" name="dr_enquiry_fueltype_id" -->
<!-- 							     id="dr_enquiry_fueltype_id" onchange="DisplayPreowned();DisplayModel();"> -->
<%-- 							       <%=mybean.PopulateFuelType(mybean.comp_id) %> --%>
<!-- 						</select> -->
<!-- 					</div> -->
					
<!-- 					<div id="model" class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Type<span>*</span>:</label> -->
<!-- 						 <select class="form-control" name="dr_enquiry_enquirytype_id" -->
<!-- 							     id="dr_enquiry_enquirytype_id" onchange="DisplayPreowned();DisplayModel();"> -->
<%-- 							    <%=mybean.PopulateType(mybean.comp_id )%> --%>
<!-- 						</select> -->
<!-- 					</div> -->
					
<!-- 					<div id="model" class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Type<span>*</span>:</label> -->
<!-- 						 <select class="form-control" name="dr_enquiry_enquirytype_id" -->
<!-- 							     id="dr_enquiry_enquirytype_id" onchange="DisplayPreowned();DisplayModel();"> -->
<%-- 							    <%=mybean.PopulateType(mybean.comp_id )%> --%>
<!-- 						</select> -->
<!-- 					</div>-- -->
					
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Budget<span>*</span>:</label>  -->
<!-- 						<input type="date" class="form-control" id="txt_enquiry_budget"  -->
<%-- 							   name="txt_enquiry_budget" value="<%=mybean.enquiry_budget%>" --%>
<!-- 							   size="12" maxlength="10" > -->
<!-- 					</div> -->
					
					  <%if(mybean.branchtype_id.equals("1")){ %>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Model<span>*</span>:</label>
						<select class="form-control" name="dr_enquiry_model_id"
							    id="dr_enquiry_model_id" onchange="populateItem();">
							    <%=mybean.PopulateModel(mybean.enquiry_model_id,mybean.comp_id)%>
						</select>
					</div>
					
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Variant<span>*</span>:</label>
						<span id="modelitem">
						<select class="form-control" name="dr_enquiry_item_id"
							    id="dr_enquiry_item_id">
					    <option value="0">Select</option>
<%-- 							    <%=mybean.PopulateItem(mybean.enquiry_model_id,mybean.comp_id)%> --%>
						</select>
					</div>
					<%} %>
					
					
					<%if(!mybean.branch_brand_id.equals("1")){%>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Type of Buyer<span>*</span>:</label>
						<select class="form-control" name="dr_enquiry_buyertype_id"
							    id="dr_enquiry_buyertype_id" onchange="">
							<%=mybean.PopulateBuyerType(mybean.comp_id) %>
						</select>
						<%}%>
					</div>
					
					<% if (mybean.branch_brand_id.equals("55")) { %>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Category
						<font color="#ff0000">*</font>
						:</label>
						<select class="form-control" name="dr_enquiry_enquirycat_id"
							    id="dr_enquiry_enquirycat_id" >
							      	<%=mybean.PopulateCategory(mybean.comp_id)%>
						</select>
					</div>
					<%} %>
					                      <!-- Enquiry Status -->
					                      
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Team<span>*</span>:</label>
						<select class="form-control" name="dr_enquiry_team"
							    id="dr_enquiry_team" onchange="PopulateExecutive()">
							      <%=mybean.PopulateTeam(mybean.branch_id, mybean.enquiry_team_id, mybean.comp_id)%>
						</select>
					</div>  
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Sales Consultant<span>*</span>:</label>
						<span id="teamexe">
<!-- 						<select class="form-control" name="dr_enquiry_emp_id" id="dr_enquiry_emp_id"> -->
							 <%=mybean.PopulateSalesExecutives(mybean.branch_id, mybean.enquiry_team_id, mybean.enquiry_emp_id, mybean.comp_id, request)%>
<!-- 						</select> -->
					</div>
					
					<%if(mybean.config_sales_soe.equals("1")){%>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Source of Enquiry<span>*</span>:</label>
						<select class="form-control" name="dr_enquiry_soe_id"
							    id="dr_enquiry_soe_id" onchange=" populateSob();">
							     <%=mybean.PopulateSoe(mybean.comp_id) %>
						</select>
					</div>                
					<%}%>  
					
					<%if(mybean.config_sales_sob.equals("1")){%>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Source of Business<span>*</span>:</label>
						<select class="form-control" name="dr_enquiry_sob_id"
							    id="dr_enquiry_sob_id">
							   <%=mybean.PopulateSob(mybean.comp_id) %>  
						</select>
					</div> 
					<%}%>  
					
					<%if(mybean.config_sales_campaign.equals("1")){%>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Campaign<span>*</span>:</label>
						<select class="form-control" name="dr_enquiry_campaign_id"
							    id="dr_enquiry_campaign_id" onchange="">
							     <%=mybean.PopulateCampaign(mybean.comp_id) %>
						</select>
					</div>  
					<%}%>      
					
<% if(!mybean.comp_id.equals("1009")){ %>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Trade-In Model:</label>
						<select class="form-control select2" id="enquiry_tradein_preownedvariant_id" name="enquiry_tradein_preownedvariant_id">
						 	<%=mybean.variantcheck.PopulateVariant(mybean.enquiry_tradein_preownedvariant_id)%>
						 </select>
					</div> 
					<%} %>
					       
					<br>
					
<!-- 					Preowned Details -->
					<%if(mybean.branchtype_id.equals("2")){ %>
<!-- 										<div class="form-group">  -->
<!--  													<label class="control-label col-md-4 col-xs-12 col-sm-2">   -->
<!-- 														Pre Owned Model<font color="#ff0000">*</font>: -->
<!-- 													</label>  -->
<!-- 													<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">  -->
<!--  													<select class="form-control select2" id="preownedvariant" name="preownedvariant" >  -->
<%--  											        <%=mybean.variantcheck.PopulateVariant(mybean.enquiry_preownedvariant_id)%>  --%>
<!--  																</select>   -->
<!--   														<input tabindex="-1"     -->
<!--  															class="bigdrop select2-offscreen form-control"    -->
<!--  															id="modelvariant" name="modelvariant"   -->
<!--  															style="width: 300px"   -->
<%-- 															value="<%=mybean.enquiry_preownedvariant_id%>"  --%>
<!--  															type="hidden"   -->
<!--  															onchange="SecurityCheck('modelvariant',this,'hint_dr_variant_id')" />    -->
<!--  														<input type="hidden" id="preownedvariant"/>  -->
<!--  													</div>   -->
<!--  												</div>   -->
												
 												<div id="model" class="form-group form-md-line-input">  
 						<label for="form_control_1">Pre Owned Model<font color="#ff0000">*</font>:</label> 
 						<select class="form-control select2" id="preownedvariant" name="preownedvariant" >
											        <%=mybean.variantcheck.PopulateVariant(mybean.enquiry_preownedvariant_id)%>
 																</select> 
 					</div>   
					
					
 					<div id="model" class="form-group form-md-line-input"> 
						<label for="form_control_1">Fuel Type<font color="#ff0000">*</font>:</label>
						<select name="dr_enquiry_fueltype_id"  
 															id="dr_enquiry_fueltype_id" class="form-control"> 
															<%=mybean.PopulateFuelType(mybean.comp_id)%>
														</select> 
					</div>   
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Pref. Reg.:</label>
						<select name="dr_enquiry_prefreg_id" id="dr_enquiry_prefreg_id" class="form-control">
															<%=mybean.PopulatePrefReg(mybean.comp_id)%>
														</select>
					</div>  
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Present Car:</label>
						<input name="txt_enquiry_presentcar" type="text"
															class="form-control" id="txt_enquiry_presentcar"
															value="<%=mybean.enquiry_presentcar%>" size="32"
															maxlength="255" />
					</div>  
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Finance<font color="#ff0000">*</font>:</label>
						<select name="dr_enquiry_finance" id="dr_enquiry_finance"
															class="form-control">
															<%=mybean.PopulateFinance(mybean.comp_id) %>
														</select>
					</div>  
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Budget<font color="#ff0000">*</font>:</label>
						<input name="txt_enquiry_budget" type="text"
															class="form-control" id="txt_enquiry_budget"
															value="<%=mybean.enquiry_budget%>" size="14"
															maxlength="10" />
					</div> 
					
					
					
					
					<%} %>
					<br>
					<div class="form-actions noborder">
						<center>
								<button type="button" class="btn1" name="addbutton" id="addbutton" >Add Enquiry</button>
<!-- 							<input name="addbutton" id ="addbutton" type="button" class="btn1"  value="Add Enquiry" onClick="return SubmitFormOnce(document.frmaddenquiry, this);" /> -->
								<input type="hidden"  name="add_button1" id ="add_button1" value=""></input>
								<input type="hidden" id="branchtype_id" value="<%=mybean.branchtype_id%>"></input>
									<input class="form-control" type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%=mybean.enquiry_branch_id%>" />

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

function populateItem() {
	var model_id = document.getElementById('dr_enquiry_model_id').value;
	showHint('app-enquiry-check.jsp?item=yes&enquiry_model_id=' + model_id , 'modelitem');
	
}
function PopulateExecutive(){		
    var enquiry_branch_id = document.getElementById('branch_id').value;	
	var team_id = document.getElementById('dr_enquiry_team').value;
	showHint('app-enquiry-check.jsp?executive=yes&team_id=' + team_id+'&enquiry_branch_id='+enquiry_branch_id,'teamexe');
  }	

// function DisplayPreowned() {
// 	var str=document.getElementById('dr_enquiry_enquirytype_id').value;
// 	//alert(str);
	
// 	if(str=="2")
// 	{
// 		displayRow('preowned');
// 		displayRow('preownedmodel');
// 		displayRow('fueltype');
// 		displayRow('presentcar');
// 		displayRow('budget');
// 	} else{
// 		hideRow('preowned');
// 		hideRow('preownedmodel');
// 		hideRow('fueltype');
// 		hideRow('presentcar');
// 		hideRow('budget');
// 	}
// }

// function DisplayModel() {
// 	var str1=document.getElementById('dr_enquiry_enquirytype_id').value;	
	
// 	if(str1=="1")
// 		{
// 			displayRow('model');
// 			displayRow('package');
// 		} else{
// 			hideRow('model');
// 			hideRow('package');
// 		}
// 	}


</script>

<!-- // $("#dr_branch_id").change(function() -->
<!-- // 		{ -->
<!-- // 	//this.form.submit(); -->
<!-- // 	document.getElementById('frmaddenquiry').submit(); -->
<!-- // 		}); -->


	<script>
	var msg = "";
	function checkForm() {
		msg = "";
		var title_id = document.getElementById("dr_enquiry_title_id").value;
		if (title_id == '0') {
			msg += '<br>Select Contact Title';
		}
		var fname = document.getElementById("txt_enquiry_fname").value;
		if (fname == '') {
			msg += '<br>Enter the Contact Person First Name!';
		}
		var lname = document.getElementById("txt_enquiry_lname").value;
		if (lname == '') {
			msg += '<br>Enter the Contact Person Last Name!';
		}
		var mobile = document.getElementById("txt_contact_mobile1").value;
		if (mobile == '91-') {
			document.getElementById("txt_contact_mobile1").value="";
			mobile = "";
		}
		if (mobile == '') {
			msg += '<br>Enter Contact Mobile1!';
		}
// 		var phone1 = document.getElementById("txt_contact_phone1").value;
// 		if (phone1 == '') {
// 			msg += '<br>Enter Contact Phone1!';
// 		}
		
// 		else if (!IsValidEmail(contact_email1)) {
// 			msg = msg + "<br>Enter Valid Contact Email 1!";}
		<%if(mybean.branchtype_id.equals("1")){ %>
		var model = document.getElementById("dr_enquiry_model_id").value;
		if (model == '0') {
			msg += '<br>Select Model!';
		}
		var item = document.getElementById("dr_enquiry_item_id").value;
		if (item == '0') {
			msg += '<br>Select Variant!';
		}
		<%}%>
		<%if(!mybean.branch_brand_id.equals("1")){%>
		var buyertype = document.getElementById("dr_enquiry_buyertype_id").value;
		if (buyertype == '0') {
			msg += '<br>Select Type of Buyer!';
		}
		<%}%>
		<%if(mybean.branch_brand_id.equals("55")){%>
		var category = document.getElementById("dr_enquiry_enquirycat_id").value;
		if (category == '0') {
			msg += '<br>Select Category!';
		}
		<%}%>
		
<%-- 		<%if (enquiry_emp_id.equals("0")) {%> --%>
var team = document.getElementById("dr_enquiry_team").value;
if (team == '0') {
	msg = msg + "<br>Select Team!";
}
		var exe = document.getElementById("dr_enquiry_emp_id").value;
		if (exe == '0') {
			msg = msg + "<br>Select Sales Consultant!";
		}
<%-- 		<%}%> --%>
		<%if(mybean.config_sales_soe.equals("1") && (mybean.branchtype_id.equals("1"))){%>
		var soe = document.getElementById("dr_enquiry_soe_id").value;
		if (soe == '0') {
			msg += '<br>Select Source of Enquiry!';
		}
		<%}%>
		<%if(mybean.config_sales_sob.equals("1")){%>
		var sob = document.getElementById("dr_enquiry_sob_id").value;
		if (sob == '0') {
			msg += '<br>Select Source of Business!';
		}
		<%}%>
		<%if(mybean.config_sales_campaign.equals("1")){%>
		var campaign = document.getElementById("dr_enquiry_campaign_id").value;
		if (campaign == '0') {
			msg += '<br>Select Campaign!';
		}
		<%}%>
		<%if(mybean.branchtype_id.equals("2")){ %>
		var preowned = document.getElementById("preownedvariant").value;
		if (preowned == '') {
			msg += '<br>Select Pre Owned Model!';
		}
		
		var fueltype = document.getElementById("dr_enquiry_fueltype_id").value;
		//alert("preowned=="+preowned);
		if (fueltype == '0') {
			msg += '<br>Select Fuel Type!';
		}
		var finance = document.getElementById("dr_enquiry_finance").value;
		//alert("preowned=="+preowned);
		if (finance == '0') {
			msg += '<br>Select Finance!';
		}
		var budget = document.getElementById("txt_enquiry_budget").value;
		//alert("preowned=="+preowned);
		if (budget == '') {
			msg += '<br>Enter Budget!';
		}
// 		}
// 		 else {
		// model_name = ExecuteQuery("select preownedmodel_name from "
		// + compdb(comp_id)
		// + "axela_preowned_model where preownedmodel_id = "
		// + enquiry_preownedmodel_id);
		// enquiry_title = "Pre Owned " + model_name;
		// }
		// if (enquiry_preownedvariant_id.equals("0")) {
		// msg = msg + "<br>Select Pre Owned Variant!";
		// }
// 		if (mybean.enquiry_budget.equals("0")) {
// 			msg = msg + "<br>Enter Budget!";
// 		}
// 		if (mybean.enquiry_fueltype_id.equals("0")) {
// 			msg = msg + "<br>Select Fuel Type!";
// 		}
// 		if (mybean.enquiry_finance.equals("0")) {
// 			msg = msg + "<br>Select Finance!";
// 		}
		<%}%>
		
		if (msg != '') {
			showToast(msg);
		} 
		else {
			document.getElementById('add_button1').value = "yes";
			document.getElementById('frmaddenquiry').submit();
		}
	}
    </script>
    
    <script>
    function populateSob(){
		 //alert(soe_id);
		 var enquiry_soe_id = document.getElementById('dr_enquiry_soe_id').value;
//		 alert('../sales/enquiry-check.jsp?dr_enquiry_sob_id=yes&enquiry_soe_id='+enquiry_soe_id);
	  showHint('../sales/enquiry-check.jsp?dr_enquiry_sob_id=yes&enquiry_soe_id='+enquiry_soe_id, 'dr_enquiry_sob_id');
	  
	}
    </script>
    
    	<script type="text/javascript">
// 		function CheckEvaluation() {
<%-- 			var preowned_enquiry_id =<%=mybean.preowned_enquiry_id%>; --%>
// 			if(preowned_enquiry_id != "0"){
// 				$("#addEvalBut").html("<font color=\"red\"><b>Pre-Owned Enquiry added.</b></font>");
// 			}
// 		}
	</script>
    
</body>
<!-- END BODY -->
</html>