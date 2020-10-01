<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Preowned_Add"
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
	<!------ onLoad="DisplayPreOwned();DisplayModel();" -->
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Add Pre-Owned</center></strong></span>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12">
			<form role="form" id="frmaddpreowned" name="frmaddpreowned"
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
						<label for="form_control_1">Branch<span>*</span>:</label> 
						<select class="form-control" name="dr_branch_id"
							     id="dr_branch_id" onchange="submit()">
							     <%=mybean.PopulateBranch(mybean.preowned_branch_id, "", "2",  "", request)%>
						</select>
						<input type="hidden" id="branch_id" name="branch_id" value="<%=mybean.branch_id%>"></input>
					</div>
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Customer:</label>  -->
<!--- 						<input type="text" class="form-control" id="txt_customer_name" -->
<!-- 							   name="txt_customer_name" value=""> -->
<!-- 					</div> -->
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Contact Title<span>*</span>:</label> 
						<select class="form-control" name="dr_preowned_title_id"
							     id="dr_preowned_title_id">
							     <%=mybean.PopulateTitle(mybean.preowned_title_id)%>
						</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">First Name<span>*</span>:</label>
						 <input type="text" class="form-control" id="txt_preowned_fname"
							   name="txt_preowned_fname" value="<%=mybean.preowned_fname%>" size="32" maxlength="255">
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Last Name<span>*</span>:</label> 
						<input type="text" class="form-control" id="txt_preowned_lname"
							   name="txt_preowned_lname" value="<%=mybean.preowned_fname%>" size="32" maxlength="255">
					</div>
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Job Title:</label>  -->
<!-- 						<input type="text" class="form-control" id="txt_contact_jobtitle" -->
<%-- 							  name="txt_contact_jobtitle" value="<%=mybean.contact_jobtitle%>" size="32" maxlength="255"> --%>
<!-- 					</div> -->

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Mobile1<span>*</span>:</label> 
						<input type="tel" class="form-control"
						onKeyUp="showHint('../preowned/preowned-check.jsp?preowned_branch_id='+<%=mybean.preowned_branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontacts');"
														onChange="showHint('../preowned/preowned-check.jsp?preowned_branch_id='+<%=mybean.preowned_branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontacts');"
						 name="txt_contact_mobile1"
							   id="txt_contact_mobile1" 
							   maxlength="13"  value="<%=mybean.contact_mobile1%>">
					</div>
					(91-9999999999)
					<span id="showcontacts" readonly></span>
					
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
						onKeyUp="showHint('../preowned/preowned-check.jsp?contact_email=' + GetReplace(this.value),'showcontacts');"
							   name="txt_contact_email1" value="<%=mybean.contact_email1%>"
							   size="32" maxlength="100" >
					</div>
					
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
<%-- 						<%if (mybean.emp_preowned_edit.equals("1")) { %> --%>
					
<!-- 						<input type="date" -->
<!-- 							class="form-control" id="txt_preowned_date" -->
<!-- 							name="txt_preowned_date"   -->
<%-- 							value="<%=mybean.preowned_date%>" size="12" maxlength="10" > --%>
							
<!-- 					</div> -->
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Closing Date<span>*</span>:</label>  -->
<!-- 						<input type="date" class="form-control" id="txt_preowned_close_date"  -->
<%-- 							   name="txt_preowned_close_date" value="<%=mybean.preowned_close_date%>" --%>
<!-- 							   size="12" maxlength="10" > -->
<!-- 					</div> -->
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Description:</label>  -->
<!-- 						<textarea type="text" class="form-control" id="txt_preowned_desc" -->
<!-- 							     name="txt_preowned_desc" cols="60" rows="2"  -->
<%-- 						       	  ><%=mybean.preowned_desc%></textarea> --%>
<!-- 					</div> -->
					
<!-- 					<div id="model" class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Type<span>*</span>:</label> -->
<!-- 						 <select class="form-control" name="dr_preowned_preownedtype_id" -->
<!-- 							     id="dr_preowned_preownedtype_id" onchange="DisplayPreowned();DisplayModel();"> -->
<%-- 							    <%=mybean.PopulateType(mybean.comp_id )%> --%>
<!-- 						</select> -->
<!-- 					</div> -->
					
<!-- 					<div id="model" class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Fuel Type<span>*</span>:</label> -->
<!-- 						 <select class="form-control" name="dr_preowned_fueltype_id" -->
<!-- 							     id="dr_preowned_fueltype_id" onchange="DisplayPreowned();DisplayModel();"> -->
<%-- 							       <%=mybean.PopulateFuelType(mybean.comp_id) %> --%>
<!-- 						</select> -->
<!-- 					</div> -->
					
<!-- 					<div id="model" class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Type<span>*</span>:</label> -->
<!-- 						 <select class="form-control" name="dr_preowned_preownedtype_id" -->
<!-- 							     id="dr_preowned_preownedtype_id" onchange="DisplayPreowned();DisplayModel();"> -->
<%-- 							    <%=mybean.PopulateType(mybean.comp_id )%> --%>
<!-- 						</select> -->
<!-- 					</div> -->
					
<!-- 					<div id="model" class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Type<span>*</span>:</label> -->
<!-- 						 <select class="form-control" name="dr_preowned_preownedtype_id" -->
<!-- 							     id="dr_preowned_preownedtype_id" onchange="DisplayPreowned();DisplayModel();"> -->
<%-- 							    <%=mybean.PopulateType(mybean.comp_id )%> --%>
<!-- 						</select> -->
<!-- 					</div>-- -->
					
					
<!-- 					<div class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Budget<span>*</span>:</label>  -->
<!-- 						<input type="date" class="form-control" id="txt_preowned_budget"  -->
<%-- 							   name="txt_preowned_budget" value="<%=mybean.preowned_budget%>" --%>
<!-- 							   size="12" maxlength="10" > -->
<!--- 					</div> -->
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Model<span>*</span>:</label>
						<select class="form-control select2" id="preownedvariant"
														name="preownedvariant" >
														<%=mybean.variantcheck.PopulateVariant(mybean.preowned_variant_id)%>
													</select>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Sub Variant:</label>
						<span id="modelitem">
						<input name="txt_preowned_sub_variant" type="text"
														class="form-control" id="txt_preowned_sub_variant"
														 size="42"
														maxlength="255" />
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Fuel Type<span>*</span>:</label>
						<select name="dr_preowned_fueltype_id" id="dr_preowned_fueltype_id" class="form-control">
							<%=mybean.PopulateFuel()%>
							</select>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Manufactured Year<span>*</span>:</label>
						<input name="txt_preowned_manufyear" type="tel" value="<%=mybean.preowned_manufyear %>"
														class="form-control" id="txt_preowned_manufyear"
														 size="42"
														maxlength="255"/>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Registration No.:
						<input name="txt_preowned_regno" type="text" value="<%=mybean.preowned_regno %>"
														class="form-control" id="txt_preowned_regno"
														 size="42"
														maxlength="255" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Ownership:</label>
						<select name="dr_preowned_ownership_id" id="dr_preowned_ownership_id" class="form-control">
						<%=mybean.PopulateOwnership()%>
						</select>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Team:<span>*</span>:</label>
							<%=mybean.PopulateTeam(mybean.preowned_branch_id, mybean.preownedteam_id)%>
					</div>

					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Pre-Owned Consultant<span>*</span>:</label>
						<span id="teamexe">
						<%=mybean.PopulatePreownedExecutives(mybean.preowned_branch_id, mybean.preownedteam_id, mybean.preowned_emp_id, mybean.comp_id, request)%>
						</span>
					</div>
					
					
					
					
					<%if(mybean.config_preowned_soe.equals("1")){%>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Source of Enquiry<span>*</span>:</label>
						<select class="form-control" name="dr_preowned_soe_id"
							    id="dr_preowned_soe_id" >
							     <%=mybean.PopulateSoe(mybean.comp_id) %>
						</select>
					</div>                
					<%}%>  
					
<%-- 					<%if(mybean.config_preowned_sob.equals("1")){%> --%>
<!-- 					<div id="model" class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Source of Business<span>*</span>:</label> -->
<!-- 						<select class="form-control" name="dr_preowned_sob_id" -->
<!-- 							    id="dr_preowned_sob_id"> -->
<%-- 							   <%=mybean.PopulateSob(mybean.comp_id) %>   --%>
<!-- 						</select> -->
<!-- 					</div>  -->
<%-- 					<%}%>   --%>
					
					<%if(mybean.config_preowned_campaign.equals("1")){%>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Campaign<span>*</span>:</label>
						<select class="form-control" name="dr_preowned_campaign_id"
							    id="dr_preowned_campaign_id" onchange="">
							     <%=mybean.PopulateCampaign(mybean.comp_id) %>
						</select>
					</div>  
					<%}%>             
					<br>
					<br>
					<div class="form-actions noborder">
						<center>
								<button type="button" class="btn1" name="addbutton" id="addbutton" >Add Pre-Owned</button>
<!-- 							<input name="addbutton" id ="addbutton" type="button" class="btn1"  value="Add Enquiry" onClick="return SubmitFormOnce(document.frmaddpreowned, this);" /> -->
								<input type="hidden"  name="add_button1" id ="add_button1" value=""></input>
								<input type="hidden" id="branchtype_id" value="<%=mybean.branchtype_id%>"></input>
									<input class="form-control" type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%=mybean.preowned_branch_id%>" />

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

// function populateItem() {
// 	var model_id = document.getElementById('dr_preowned_model_id').value;
// 	showHint('app-preowned-check.jsp?item=yes&preowned_model_id=' + model_id , 'modelitem');
	
// }
function PopulateExecutive(){		
    var preowned_branch_id = document.getElementById('branch_id').value;	
// 	var team_id = document.getElementById('dr_preowned_team').value;
	showHint('app-preowned-check.jsp?executive=yes&preowned_branch_id='+preowned_branch_id,'teamexe');
  }	

// function DisplayPreowned() {
// 	var str=document.getElementById('dr_preowned_preownedtype_id').value;
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
// 	var str1=document.getElementById('dr_preowned_preownedtype_id').value;	
	
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
<!-- // 	document.getElementById('frmaddpreowned').submit(); -->
<!-- // 		}); -->


	<script>
	var msg = "";
	function checkForm() {
		msg = "";
		var title_id = document.getElementById("dr_preowned_title_id").value;
		if (title_id == '0') {
			msg += '<br>Select Contact Title!';
		}
		var fname = document.getElementById("txt_preowned_fname").value;
		if (fname == '') {
			msg += '<br>Enter the Contact First Name!';
		}
		var lname = document.getElementById("txt_preowned_lname").value;
		if (lname == '') {
			msg += '<br>Enter the Contact Last Name!';
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
		var model = document.getElementById("preownedvariant").value;
		if (model == '0' || model == '') {
			msg += '<br>Select Model!';
		}
		var fuel_type = document.getElementById("dr_preowned_fueltype_id").value;
		if (fuel_type == '0') {
			msg += '<br>Select Fuel Type!';
		}
		var manuf_year = document.getElementById("txt_preowned_manufyear").value;
		if (manuf_year == '') {
			msg += '<br>Enter Manufactured Year!';
		}
		<%if(!mybean.branch_brand_id.equals("1")){%>
		var buyertype = document.getElementById("dr_preowned_buyertype_id").value;
		if (buyertype == '0') {
			msg += '<br>Select Type of Buyer!';
		}
		<%}%>
		<%if(mybean.branch_brand_id.equals("55")){%>
		var category = document.getElementById("dr_preowned_preownedcat_id").value;
		if (category == '0') {
			msg += '<br>Select Category!';
		}
		<%}%>
		
		var preowned_team = document.getElementById("dr_preowned_team").value;
		if (preowned_team == '0') {
			msg = msg + '<br>Select Team!';
		}
		
		var exe = document.getElementById("dr_preowned_emp_id").value;
		if (exe == '0') {
			msg = msg + '<br>Select Pre-Owned Consultant!';
		}
		<%if(mybean.config_preowned_soe.equals("1") && (mybean.branchtype_id.equals("1"))){%>
		var soe = document.getElementById("dr_preowned_soe_id").value;
		if (soe == '0') {
			msg += '<br>Select Source of Enquiry!';
		}
		<%}%>
		<%if(mybean.config_preowned_sob.equals("1")){%>
		var sob = document.getElementById("dr_preowned_sob_id").value;
		if (sob == '0') {
			msg += '<br>Select Source of Business!';
		}
		<%}%>
		<%if(mybean.config_preowned_campaign.equals("1")){%>
		var campaign = document.getElementById("dr_preowned_campaign_id").value;
		if (campaign == '0') {
			msg += '<br>Select Campaign!';
		}
		<%}%>
		if (msg != '') {
			showToast(msg);
		} 
		else {
			document.getElementById('add_button1').value = "yes";
			document.getElementById('frmaddpreowned').submit();
		}
	}
    </script>
    
    <script>
    function populateSob(){
		 //alert(soe_id);
		 var preowned_soe_id = document.getElementById('dr_preowned_soe_id').value;
//		 alert('../sales/preowned-check.jsp?dr_preowned_sob_id=yes&preowned_soe_id='+preowned_soe_id);
	  showHint('../sales/preowned-check.jsp?dr_preowned_sob_id=yes&preowned_soe_id='+preowned_soe_id, 'dr_preowned_sob_id');
	  
	}
    
    function PopulateExecutive(){
		
		var preowned_branch_id = document.getElementById('txt_branch_id').value;
		var team_id = document.getElementById('dr_preowned_team').value;
//			alert("team_id=="+team_id);
//			alert("preowned_branch_id=="+preowned_branch_id);
		showHint('../preowned/preowned-check.jsp?preexecutive=yes&team_id=' + team_id+'&preowned_branch_id='+preowned_branch_id,'teamexe');
	  }
    </script>
</body>
<!-- END BODY -->
</html>