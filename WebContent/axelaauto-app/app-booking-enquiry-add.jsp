<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.axelaauto_app.App_Booking_Enquiry_Add" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en">
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">
<style>
/* b { */
/* 	color: #8f3e97; */
/* } */
a {
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
			<span class="panel-title"> <strong><center>Add
						Booking Enquiry</center></strong></span>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12">
			<form role="form" id="frmaddbooking" name="frmaddbooking"
				class="form-horizontal" method="post">
				<div class="form-body">

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Branch<font color=red>*</font>:
						</label> <select class="form-control" name="dr_vehbranch_id"
							id="dr_vehbranch_id" onchange="PopulateModel(this.value);"
							visible="true">
							<%=mybean.PopulateBranches(mybean.comp_id, mybean.booking_veh_branch_id)%>
						</select>
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Customer:</label> <input
							class="form-control" name="txt_booking_customer_name" type="text"
							id="txt_booking_customer_name" value="<%=mybean.customer_name%>"
							size="50" maxlength="255" /> (Name as on Invoice)
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Contact Title<font color=red>*</font>:
						</label> <select name="dr_title" class="form-control" id="dr_title">
							<%=mybean.PopulateTitle(mybean.contact_title_id, mybean.comp_id)%>
						</select>
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">First Name<font color=red>*</font>:
						</label> <input name="txt_contact_fname" type="text" class="form-control "
							id="txt_contact_fname" value="<%=mybean.contact_fname%>"
							size="30" maxlength="255" onkeyup="ShowNameHint()" />
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Last Name<font color=red>*</font>:
						</label> <input name="txt_contact_lname" type="text" class="form-control "
							id="txt_contact_lname" value="<%=mybean.contact_lname%>"
							size="30" maxlength="255" onkeyup="ShowNameHint()" />
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Mobile <font color="#ff0000">*</font>:
						</label> <input name="txt_contact_mobile1" type="tel" class="form-control"
							id="txt_contact_mobile1"
							onKeyUp="showHint('../service/report-check.jsp?contact_mobile1='+GetReplace(this.value)+'&search=yes','showcontacts');"
							value="<%=mybean.contact_mobile1%>" size="32" maxlength="13" />
						(91-9999999999)<br> <span id="showcontacts"></span>
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Email: </label> <input
							name="txt_contact_email1" type="text" class="form-control"
							id="txt_contact_email1"
							<%-- 														onkeyup="showHint('../service/booking-enquiry-check.jsp?contact_id='+<%=mybean.customer_id%>+'&booking_veh_branch_id='+<%=mybean.booking_veh_branch_id%>+'&contact_email=' + GetReplace(this.value),'showcontacts');" --%>
														onKeyUp="showHint('../service/report-check.jsp?contact_email1='+GetReplace(this.value)+'&search=yes','search-contact');"
							value="<%=mybean.contact_email1%>" size="32" maxlength="100"><span
							id="search-contact"></span>
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Model<font color="#ff0000">*</font>:
						</label>
						<span id="hintmodel">
						<%=mybean.PopulateModel(mybean.booking_veh_branch_id ,mybean.comp_id)%>
						</span>
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Variant<font color="#ff0000">*</font>:
						</label> <span id="model-item"> <%=mybean.PopulateItem(mybean.booking_veh_model_id, mybean.comp_id)%></span>
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Reg.No<font color="#ff0000">*</font>:
						</label> <input class="form-control" name="txt_booking_veh_reg_no"
							type="text" id="txt_booking_veh_reg_no"
							value="<%=mybean.booking_veh_reg_no%>" size="50" maxlength="255" />
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Chassis Number:</label> <input
							class="form-control" name="txt_booking_veh_chassis_no"
							type="text" id="txt_booking_veh_chassis_no"
							value="<%=mybean.booking_veh_chassis_no%>" size="50"
							maxlength="255" />
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Engine Number:</label> <input
							class="form-control" name="txt_booking_veh_engine_no" type="text"
							id="txt_booking_veh_engine_no"
							value="<%=mybean.booking_veh_engine_no%>" size="50"
							maxlength="255" />
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">CRM Executive<font
							color="#ff0000">*</font>:
						</label> <span id="crmexe"> <%=mybean.PopulateCRMExecutive(mybean.comp_id,request)%>
						</span>
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Booking Date<font color="red">*</font>:
						</label> <input name="txt_booking_date" type="text" class="form-control"
							onclick="datePicker('txt_booking_date')" id="txt_booking_date"
							value="<%=mybean.booking_date%>" size="20" maxlength="16" readonly>  
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Booking Time<font color="red">*</font>:
						</label> <input name="txt_booking_time" type="text" class="form-control"
							onclick="timePicker('txt_booking_time')" id="txt_booking_time"
							value="" size="20" maxlength="16" readonly>
					</div>
					<span id="datetimeerrormsg"></span>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Booking Type<font color="red">*</font>:
						</label> <select name="dr_booking_type" class="form-control"
							id="dr_booking_type" Onchange="Pickupband();PopulateDriver()"
							visible="true">
							<%=mybean.PopulateServiceBookingType(mybean.comp_id, mybean.booking_type)%>
						</select>
					</div>

					<div class="form-group form-md-line-input" id="pickupband">
						<label for="form_control_1">Driver<font color="red">*</font>:
						</label> <select name="dr_vehfollowup_pickupdriver_emp_id"
							class="form-control" id="dr_vehfollowup_pickupdriver_emp_id"
							visible="true">
							<span id="HintDriver"> <%=mybean.PopulateServicePickUp(mybean.comp_id, mybean.booking_driver)%>
						</span>
						</select>
					</div>

					<div class="form-group form-md-line-input">
						<label for="form_control_1">Other Address<font color="red">*</font>:
						</label>
						<textarea name="txt_vehfollowup_pickuplocation" cols="40" rows="4"
							class="form-control" id="txt_vehfollowup_pickuplocation"
							onchange="SecurityCheck('txt_vehfollowup_pickuplocation',this,'hint_txt_vehfollowup_pickuplocation')"
							onkeyup="charcount('txt_vehfollowup_pickuplocation', 'span_txt_vehfollowup_pickuplocation','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.booking_other_address%></textarea>
						<span id="span_txt_vehfollowup_pickuplocation"> (255
							Characters)</span>
					</div>

					<!-- 					<div class="form-group form-md-line-input"> -->
					<!-- 						<label for="form_control_1">Source -->
					<!-- 														of Enquiry<font color="#ff0000">*</font>:</label> -->
					<!-- 						<select name="dr_booking_soe_id" id="dr_booking_soe_id" -->
					<!-- 															class="dropdown form-control" onchange="populateSob();"> -->
					<%-- 															<%=mybean.PopulateSoe(mybean.comp_id)%> --%>
					<!-- 														</select> -->
					<!-- 					</div> -->

					<!-- 					<div class="form-group form-md-line-input"> -->
					<!-- 						<label for="form_control_1">Source of Business<font color="#ff0000">*</font>:</label> -->
					<!-- 						<select name="dr_booking_sob_id" id="dr_booking_sob_id" -->
					<!-- 															class="dropdown form-control"> -->
					<%-- 															<%=mybean.PopulateSOB()%> --%>
					<!-- 														</select> -->
				</div>


				<br>
				<br>
				<div class="form-actions noborder">
					<center>
						<button type="button" class="btn1" name="addbutton" id="addbutton">Add
							Enquiry</button>
						<input type="hidden" name="add_button" id="add_button"
							value="Add Enquiry"></input>

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
		
		var booking_veh_reg_no = document.getElementById("txt_booking_veh_reg_no").value;
		if (booking_veh_reg_no == '') {
			msg += '<br>Enter Reg.No!';
		}
		
		var booking_veh_executive = document.getElementById("dr_booking_veh_executive").value;
		if (booking_veh_executive == '0') {
			msg += '<br>Select CRM Executive!';
		}
		
		var booking_date = document.getElementById("txt_booking_date").value;
		if (booking_date == '') {
			msg += '<br>Select Booking Date!';
		}
		
		var booking_time = document.getElementById("txt_booking_time").value;
		if (booking_time == '') {
			msg += '<br>Select Booking Time!';
		}
		
		var booking_type = document.getElementById("dr_booking_type").value;
		if (booking_type == '0') {
			msg += '<br>Select Booking Type!';
		}
		
		if (booking_type == '2') {
		var vehfollowup_pickupdriver_emp_id = document.getElementById("dr_vehfollowup_pickupdriver_emp_id").value;
		if (vehfollowup_pickupdriver_emp_id == '0') {
			msg += '<br>Select Driver!';
		 }
		}
		
		var vehfollowup_pickuplocation = document.getElementById("txt_vehfollowup_pickuplocation").value;
		if (vehfollowup_pickuplocation == '') {
			msg += '<br>Enter Other Address';
		}
		if (msg != '') {
			showToast(msg);
		} 
		else {
			document.getElementById('add_button').value = "Add Enquiry";
			document.getElementById('frmaddbooking').submit();
		}
	}
    </script>
	<script language="JavaScript" type="text/javascript">
//         function PopulateExecutive(branch_id){	
// 			showHint("../service/booking-enquiry-check.jsp?executive=yes&active=1&booking_enquiry_branch_id="+branch_id,'crmexe');
// 		  }
        function PopulateModel(branch_id){
        	showHint("../service/booking-enquiry-check.jsp?veh_branch_id="+branch_id+"&model=yes", "hintmodel");
        }
        function PopulateItem(model_id){
        	showHint("../service/booking-enquiry-check.jsp?item_model_id="+model_id+"&list_model_item=yes", "model-item");
        }
//          function PopulateCampaign(branch_id){
//  			showHint("../service/booking-enquiry-check.jsp?veh_branch_id="+branch_id+"&campaign=yes", "campaign");
//  		  }
//          function PopulateServiceExecutive(branch_id){
//         	 showHint("vehicle-dash-check.jsp?veh_branch_id="+branch_id+"&serviceexe=yes", "Hintmodel");
//          }
         
	function Pickupband() { 
		var pickup=$("#dr_booking_type").val();
			 if(pickup==0){
				 $("#pickupband").hide();
			 }
			 else if(pickup==1){
				 $("#pickupband").hide();
			 }
			 else if(pickup==2){
				 $("#pickupband").show();
			 }
			 else if(pickup==3){
				 $("#pickupband").hide();
			 }
		 }
	function PopulateDriver() {
		var bookingid = document.getElementById("dr_booking_type").value;
		showHint('../service/booking-enquiry-check.jsp?listdriver=yes&booking_id=' + bookingid, 'HintDriver');
	}
// 	function populateSob(){
	
// 		 var booking_soe_id = document.getElementById('dr_booking_soe_id').value;
		
// 		  showHint('../service/booking-enquiry-check.jsp?dr_booking_sob_id=yes&booking_soe_id='+booking_soe_id, 'dr_booking_sob_id');
	  
// 	}
	
    </script>
</body>
</html>