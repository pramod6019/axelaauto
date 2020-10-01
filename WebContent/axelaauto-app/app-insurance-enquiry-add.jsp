<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Insurance_Enquiry_Add"
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
<style>
/* b { */
/* 	color: #8f3e97; */
/* } */
a{
color: black;
}
.container {
 	padding-right: 0px; 
 	padding-left: 0px; 
 	margin-right: auto; 
 	margin-left: 5px; 
	margin-top: 45px; 
}

/* span { */
/* 	color: red; */
/* } */

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
<body <%if(!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%} %>>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Add Insurance Enquiry</center></strong></span>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12">
			<form role="form" id="frmaddinsurance" name="frmaddinsurance"
				class="form-horizontal" method="post">
				<div class="form-body">
	                 <div class="form-group form-md-line-input">
						<label for="form_control_1">Branch<font color=red>*</font>:</label>
						<select class="form-control" name="dr_vehbranch_id"
															id="dr_vehbranch_id" onchange="PopulateModel(this.value);PopulateCampaign(this.value)";>
															<%=mybean.PopulateBranches(mybean.comp_id, mybean.insurance_veh_branch_id)%>
														</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Customer:</label> 
						<input class="form-control"
														name="txt_insurance_customer_name" type="text"
														id="txt_insurance_customer_name"
														value="<%=mybean.customer_name%>" size="50"
														maxlength="255" /> (Name as on Invoice)
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Contact Title<font
													color=red>*</font>:</label> 
						<select name="dr_title" class="form-control"
																id="dr_title">
																	<%=mybean.PopulateTitle(mybean.contact_title_id, mybean.comp_id)%>
															</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">First Name<font
													color=red>*</font>:</label> 
						<input name="txt_contact_fname" type="text"
																class="form-control " id="txt_contact_fname"
																value="<%=mybean.contact_fname%>" size="30"
																maxlength="255" onkeyup="ShowNameHint()" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Last Name<font
													color=red>*</font>:</label> 
						<input name="txt_contact_lname" type="text"
																class="form-control " id="txt_contact_lname"
																value="<%=mybean.contact_lname%>" size="30"
																maxlength="255" onkeyup="ShowNameHint()" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Mobile<font color="#ff0000">*</font>:</label> 
						<input name="txt_contact_mobile1" type="tel"
														class="form-control" id="txt_contact_mobile1"
														onKeyUp="showHint('../service/report-check.jsp?contact_mobile1='+GetReplace(this.value)+'&search=yes','showcontacts');toPhone('txt_contact_mobile1','Mobile1');"
														value="<%=mybean.contact_mobile1%>" size="32"
														maxlength="13" /> (91-9999999999)<br> <span id="showcontacts"></span>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Email
													:</label> 
						<input name="txt_contact_email1" type="text"
														class="form-control" id="txt_contact_email1"
														onKeyUp="showHint('../service/report-check.jsp?contact_email1='+GetReplace(this.value)+'&search=yes','search-contact');"
														value="<%=mybean.contact_email1%>" size="32"
														maxlength="100"><br><span style="color:black" id="search-contact"></span>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Model<font color="#ff0000">*</font>:</label> 
							<span id="Hintmodel"> <%=mybean.PopulateModel(mybean.insurance_veh_branch_id ,mybean.comp_id)%>
													</span>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Variant<font
													color="#ff0000">*</font>:</label> 
							<span id="model-item"> <%=mybean.PopulateItem(mybean.insurance_veh_model_id, mybean.comp_id)%></span>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Reg.No<font
													color="#ff0000">*</font>:</label> 
							<input class="form-control" name="txt_insurance_veh_reg_no"
														type="text" id="txt_insurance_veh_reg_no"
														value="<%=mybean.insurance_veh_reg_no%>" size="50"
														maxlength="255" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Chassis
													Number: </label> 
							<input class="form-control"
														name="txt_insurance_veh_chassis_no" type="text"
														id="txt_insurance_veh_chassis_no"
														value="<%=mybean.insurance_veh_chassis_no%>" size="50"
														maxlength="255" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Engine
													Number:</label> 
							<input class="form-control"
														name="txt_insurance_veh_engine_no" type="text"
														id="txt_insurance_veh_engine_no"
														value="<%=mybean.insurance_veh_engine_no%>" size="50"
														maxlength="255" />
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Insurance Executive<font color="#ff0000">*</font>:</label> 
							<span id="insurexe"><%=mybean.PopulateInsurExecutive(mybean.comp_id,request)%>
													</span>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Next Follow-up Date<font color="#ff0000">*</font>:</label>
							<input name="txt_insurfollowup_date" type="text"
																class="form-control"
																id="txt_insurfollowup_date" onclick="datePicker('txt_insurfollowup_date')"
																value="<%=mybean.insurfollowup_date%>" size="20"
																maxlength="16" readonly>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Next Follow-up Time<font color="#ff0000">*</font>:</label>
							<input name="txt_insurfollowup_time" type="text"
																class="form-control date form_datetime"
																id="txt_insurfollowup_time" onclick="timePicker('txt_insurfollowup_time')"
																 size="20"
																maxlength="16" readonly>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Next Follow-up Type<font color="#ff0000">*</font>:</label>
							<select name="dr_insurfollowup_followuptype_id"
																class="form-control"
																id="dr_insurfollowup_followuptype_id" visible="true">
																<%=mybean.PopulateFollowuptype(mybean.comp_id)%>
															</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Next
															Follow-up Priority<font color="#ff0000">*</font>:</label>
							<select name="dr_insurfollowup_priorityinsurfollowup_id"
																class="form-control"
																id="dr_insurfollowup_priorityinsurfollowup_id"
																visible="true">
																<%=mybean.PopulateFollowupPriority(mybean.comp_id)%>
															</select>
					</div>
					
					<br><br>
					<div class="form-actions noborder">
						<center>
								<button type="button" class="btn1" name="addbutton" id="addbutton">Add Enquiry</button>
								<input type="hidden"  name="add_button" id ="add_button" value="Add Enquiry"></input>
								<input class="form-control" name="txt_contact_id"
														type="hidden" id="txt_contact_id"
														value="<%=mybean.insurance_contact_id%>" />
						</center>
						<br>
					</div>
				</div>
			</form>
		</div>
	</div>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>	
<script src="js/jquery.min.js" type="text/javascript"></script>
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
		
		var vehbranch_id = document.getElementById("dr_vehbranch_id").value;
		if (vehbranch_id == '0') {
			msg += '<br>Select Branch!';
		}
		
		var title = document.getElementById("dr_title").value;
		if (title == '0') {
			msg += '<br>Select Contact Title!';
		}
		
		var contact_fname = document.getElementById("txt_contact_fname").value;
		if (contact_fname == '') {
			msg += '<br>Enter First Name!';
		}
		
		var contact_lname = document.getElementById("txt_contact_lname").value;
		if (contact_lname == '') {
			msg += '<br>Enter Last Name!';
		}
		
		var contact_mobile1 = document.getElementById("txt_contact_mobile1").value;
		if (contact_mobile1 == '' || contact_mobile1 == '91-') {
			msg += '<br>Enter Mobile!';
		}
		
		var item_model_id = document.getElementById("dr_item_model_id").value;
		if (item_model_id == '0') {
			msg += '<br>Select Model!';
		}
		
		var item_id = document.getElementById("dr_item_id").value;
		if (item_id == '0') {
			msg += '<br>Select Variant!';
		}
		
		var insurance_veh_reg_no = document.getElementById("txt_insurance_veh_reg_no").value;
		if (insurance_veh_reg_no == '') {
			msg += '<br>Enter Reg.No!';
		}
		
		var insurance_veh_executive = document.getElementById("dr_insurance_veh_executive").value;
		if (insurance_veh_executive == '0') {
			msg += '<br>Select Insurance Executive!';
		}
		
		var insurfollowup_date = document.getElementById("txt_insurfollowup_date").value;
		if (insurfollowup_date == '') {
			msg += '<br>Select Next Follow-up Date!';
		}
		
		var insurfollowup_time = document.getElementById("txt_insurfollowup_time").value;
		if (insurfollowup_time == '') {
			msg += '<br>Select Next Follow-up Time!';
		}
		
		var insurfollowup_followuptype_id = document.getElementById("dr_insurfollowup_followuptype_id").value;
		if (insurfollowup_followuptype_id == '0') {
			msg += '<br>Select Next Follow-up Type!';
		}
		
		var insurfollowup_priorityinsurfollowup_id = document.getElementById("dr_insurfollowup_priorityinsurfollowup_id").value;
		if (insurfollowup_priorityinsurfollowup_id == '0') {
			msg += '<br>Select Next Follow-up Priority';
		}
		
		if (msg != '') {
			showToast(msg);
		} 
		else {
			document.getElementById('add_button').value = "Add Enquiry";
			document.getElementById('frmaddinsurance').submit();
		}
	}
    </script>
    <script language="JavaScript" type="text/javascript">
        function PopulateModel(branch_id){
        	showHint("../service/booking-enquiry-check.jsp?veh_branch_id="+branch_id+"&insurmodel=yes", "Hintmodel");
        }
         function PopulateItem(model_id){
        	showHint("../service/booking-enquiry-check.jsp?item_model_id="+model_id+"&insurlist_model_item=yes", "model-item");
        }

// 	function populateSob(){
// 		 var insur_soe_id = document.getElementById('dr_insur_soe_id').value;
// 		  showHint("../service/booking-enquiry-check.jsp?dr_insur_sob_id=yes&insur_soe_id="+insur_soe_id, "dr_insur_sob_id");
	  
// 	}
	function PopulateCampaign(branch_id){
			showHint("../service/booking-enquiry-check.jsp?veh_branch_id="+branch_id+"&insurcampaign=yes", "insurcampaign");
		  }
	
    </script>
</body>
</html>