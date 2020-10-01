<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.ddmotors_app.Book_A_Service"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">


<link href="../ddmotors-assets/css/bootstrap.min.css" rel="stylesheet"
	type="text/css">
<link href="../ddmotors-assets/css/components-rounded.css"
	id="style_components" rel="stylesheet" type="text/css">
<link href="../ddmotors-assets/css/default.css" rel="stylesheet"
	type="text/css" id="style_color">

<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<script src="../ddmotors-assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/bootstrap.min.js"
	type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script src="../ddmotors-assets/js/metronic.js" type="text/javascript"></script>
<script src="../Library/dynacheck.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/mobilecall.js" type="text/javascript"></script>

<script>
// 	function getOS() {
// // 		document.getElementById('os_type').value = 'ios';
// 			showHint('../ddmotors-app/book-a-service.jsp?os_type=ios');
// 	}
<%-- <%if(mybean.ostype.equals("android")){%> --%>
	$(document).ready(function() {
		$("#addbutton").click(function() {
			checkForm();
		});
	});
<%-- 	<%}%> --%>
	var msg = "";
	var len = 0;
	function checkForm() {
		msg = "";
		/* alert("111111"); */
		 var title_id = document.getElementById("txt_service_title_id").value;
		if (title_id == '0') {
			msg += '<br>Select Your Title!';
		} 
		var fname = document.getElementById("txt_service_fname").value;
		if (fname == '') {
			msg += '<br>Enter First Name!';
		}
		var lname = document.getElementById("txt_service_lname").value;
		if (lname == '') {
			msg += '<br>Enter Last Name!';
		}
// 		var lname = document.getElementById("txt_service_lname").value;
// 		if (lname == '') {
// 			msg += '<br>Enter the Contact Last Name!';
// 		}
		var mobile = document.getElementById("txt_service_mobile").value;
		if (mobile == '' || mobile == '91-') {
			msg += '<br>Enter Mobile No.!';
		}
		len = mobile.length;
		//alert("len------"+len);
		/* if(len!=10){
			msg += '<br>Mobile No Should Be 10 Digits.!';
		} */
	
		var email = document.getElementById("txt_service_email").value;
		if (email == '') {
			msg += '<br>Enter Email!';
		}
		/* if(email!=''){
			if(!email.contains("@")){
				msg += '<br>Enter Valid Email!';
			}
			if(!email.contains(".")){
				msg += '<br>Enter Valid Email!';
			}
		} */
		/* if(email!=''){
			if(!email.contains(".")){
				msg += '<br>Enter Valid Email!';
			}
		} */
// 		var mycar = document.getElementById("txt_service_mycar").value;
// 		if (mycar == '') {
// 			msg += '<br>Enter My car!';
// 		}
		var reg_no = document.getElementById("txt_service_reg_no").value;
		if (reg_no == '') {
			msg += '<br>Enter Registration No.!';
		}


		var service_comments = document.getElementById("txt_service_comments").value;

		if (msg != '') {
			showToast(msg);
		} else {
			document.getElementById('frmservice').submit();
		}
	}
</script>
<style>
center {
	font-size: 20px;
}
</style>
</head>

<body <%if (!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%}%>>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">
				<center>Book A Service</center>
			</h3>
		</div>
	</div>
	<div class="container">

		<form role="form" id="frmservice" name="frmservice" method="post">

			<div class="form-body">
				<div class="alert alert-danger display-hide">
					<button class="close" data-close="alert"></button>
					You have some form errors. Please check below.
				</div>
				<div class="alert alert-success display-hide">
					<button class="close" data-close="alert"></button>
					Your form validation is successful!
				</div>
<%-- 			<%if(mybean.ostype.equals("android")) {%> --%>
				
<!-- 						<input type="text" class="hidden" id="os_type" name="os_type" /> 
						<input type="text" class="form-control"
						name="txt_service_title" id="txt_service_title" value="<%=mybean.titledesc%>"
						placeholder="Select Title"
						onClick="showDialog('<%=mybean.PopulateJSONTitle(mybean.service_title_id)%>','populatetitle', 'title_id','title_desc','Select Title','txt_service_title_id','');">
					<label for="form_control_1">Your Title *</label> <input
						type="text" class="form-control hidden"
						name="txt_service_title_id" id="txt_service_title_id" value="<%=mybean.service_title_id%>">
				</div>
<%-- 				<%}else if(mybean.ostype.equals("ios")){ %> --%>
				<div
				class="form-group form-md-line-input has-info">
				<select class="form-control" name="txt_service_title_id"
					id="txt_service_title_id">
<%-- 				<%=mybean.PopulateTitle(mybean.service_title_id)%> --%>
				</select> <label for="form_control_1">Your Title *</label>
								</div>
<%-- 								<%} %> --%>

				<div class="form-group form-md-line-input ">
					<input type="text" class="form-control" id="txt_service_fname"
						name="txt_service_fname" value=<%=mybean.service_fname%>> <label for="form_control_1"
						name="txt_service_fname" id="txt_service_fname">First Name
						* </label>
				</div>
				<div class="hidden" >
<!-- 				class="form-group form-md-line-input "> -->


<div class="form-group form-md-line-input has-info">
						<select class="form-control" name="txt_service_title_id"
					id="txt_service_title_id">
				<%=mybean.PopulateTitle(mybean.service_title_id)%>
				</select> <label for="form_control_1" ">Title
						* </label>
				</div>


<div class="form-group form-md-line-input has-info">
						<input type="text" class="form-control" 
						name="txt_service_fname" id="txt_service_fname"
						 value=<%=mybean.service_fname%>> <label for="form_control_1">First Name
						* </label>
				</div>


<div class="form-group form-md-line-input has-info">
					<input type="text" class="form-control" id="txt_service_lname"
						name="txt_service_lname" value="<%=mybean.service_lname%>"> <label for="form_control_1"
						name="txt_service_lname" id="txt_service_lname">Last Name
						* </label>
				</div>
				<div class="form-group form-md-line-input ">
					<input type="text" class="form-control" id="txt_service_mobile"
						name="txt_service_mobile" maxlength="13" value="<%=mybean.service_mobile%>"
						onkeyup="toPhone('txt_service_mobile','Mobile');"> <label
						for="form_control_1" name="txt_service_mobile"
						id="txt_service_mobile" maxlength="13">Mobile * </label>
				</div>

				<div class="form-group form-md-line-input ">
					<input type="text" class="form-control" id="txt_service_email"
						name="txt_service_email" value="<%=mybean.service_email%>"> <label for="form_control_1"
						name="txt_service_email" id="txt_service_email">Email * </label>
				</div>
<!-- 				<div   -->
<!-- <!-- 				class="form-group form-md-line-input "> -->
<!-- 					<input type="text" class="form-control" id="txt_service_mycar" -->
<%-- 						name="txt_service_mycar" value="<%=mybean.service_email%>"> <label for="form_control_1" --%>
<!-- 						name="txt_service_mycar" id="txt_service_mycar">My Car * </label> -->
<!-- 				</div> -->
				<div class="form-group form-md-line-input ">
					<input type="text" class="form-control" id="txt_service_reg_no"
						name="txt_service_reg_no" value="<%=mybean.service_reg_no%>"> <label for="form_control_1"
						name="txt_service_reg_no" id="txt_service_reg_no">Reg No.
						* </label>
				</div>
<!-- 				<div   -->
<!-- <!-- 				class="form-group form-md-line-input "> --> 
<!-- 					<input type="date" class="form-control" id="txt_service_time" -->
<%-- 						name="txt_service_time" value="<%=mybean.service_time%>"> <label for="form_control_1" --%>
<!-- 						name="txt_service_time" id="txt_service_time">Pick Date * -->
<!-- 						</label> -->
<!-- 				</div> -->
<%-- 				<%if(mybean.ostype.equals("android")) {%> --%>
<!-- 				<div class="form-group form-md-line-input has-info"> -->
<!-- 					<input type="text" class="form-control" name="txt_service_slot" -->
<!-- 						id="txt_service_slot" placeholder="Select Slot" -->
<%-- 						onClick="showDialog('<%=mybean.PopulateJSONSlot()%>','populateslot', 'slot_id','slot_name','Select Time Slot','txt_service_slot_id','');"> --%>
<!-- 					<label for="form_control_1">Time Slot *</label> <input type="text" -->
<!-- 						class="form-control hidden" name="txt_service_slot_id" -->
<!-- 						id="txt_service_slot_id" value="0"> -->
<!-- 				</div> -->
<%-- 				<%}else if(mybean.ostype.equals("ios")){ %> --%>
<!-- 				<div -->
<!-- 				class="form-group form-md-line-input has-info"> -->
<!-- 				<select class="form-control" name="txt_service_slot_id" -->
<!-- 					id="txt_service_slot_id"> -->
<%-- 				<%=mybean.PopulateSlot()%> --%>
<!-- 				</select> <label for="form_control_1">Time Slot *</label> -->
<!-- 								</div> -->
<%-- 				<%} %> --%>
				

				<div class="form-group form-md-line-input ">
					<input type="text" class="form-control" id="txt_service_comments"
						name="txt_service_comments" value="<%=mybean.service_comments%>"> <label for="form_control_1"
						name="txt_service_comments" id="txt_service_comments">Comments
						</label>
				</div>
			</div>

			<div class="form-actions noborder">
			<center>
						<button type="button" class="btn" name="addbutton"
							id="addbutton"
<%-- 							<%if(mybean.ostype.equals("ios")){%>  --%>
									onClick="return SubmitFormOnce(document.frmservice, this);"
<%-- 									<%} %> --%>
							>Submit</button></center>
						<input type="hidden" name="add_button" value="yes"><br>
						<br>
					
			</div>
		</form>

	</div>
	<script>
		jQuery(document).ready(function() {
			// initiate layout and plugins
			Metronic.init(); // init metronic core components
		});
	</script>
</body>
<!-- END BODY -->
</html>
