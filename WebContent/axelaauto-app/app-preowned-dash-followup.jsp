<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Preowned_Dash" scope="request" />
<jsp:useBean id="mybeanfollowup" class="axela.axelaauto_app.App_Preowned_Dash_Followup" scope="request" />
<%
	mybean.doPost(request, response);
mybeanfollowup.doPost(request, response);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Pre-Owned Dash | Axelaauto</title>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">


<!-- <link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" /> -->
<!-- <link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" /> -->
<!-- <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"> -->
<link href="css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="css/select2.min.css" rel="stylesheet" type="text/css" />




	
<style>
a {
	text-shadow: none;
	color: black;
	text-decoration:none;
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

 .select2-container{
 width:100%;
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

<body onLoad="TestdriveReq();">
	<div class="header-wrap">
		<div class="panel-heading"
			style="margin-bottom: 20px; background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);padding:18px">
			<span class="panel-title">
				<center>
					<strong> Pre-Owned Dashboard</strong>
				</center>
			</span>
		</div>
	</div>
	<div class="col-md-6" style="margin-top: 40px; margin-left: 4px;">
		<form role="form" class="form-horizontal" name="form1" id="form1" method="post">
			<div class="form-body">
				<div class="form-group">
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-5 control-label"
							for="form_control_1"><b>Pre-Owned No: </b></label>
						<div class="col-md-8 col-xs-7">
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
								name="txt_preowned_customer_name" id="txt_preowned_customer_name"
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
					</div><br>
<!-- 						<center><button type="button" class="btn1" name="addbutton" id="addbutton" >Add Enquiry</button></center> -->
				</div>
			</div>
		</form>
	</div>
	<div class="panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Follow-up </strong>
											</h4>
										</center>
									</span>
								</div>

				<!-- accordian 2-->
<!-- 				<h4> -->
<!-- 							<a href="#"><strong>FOLLOW-UP</strong></a> -->
<!-- 						</h4> -->

		<form role="form" class="form-horizontal" name="addfollowup" id="addfollowup" method="post">
		
<!-- 			<div class="form-body"> -->
<!-- 				<div class="form-group"> -->
<%-- 					<%if(mybean.followuptab.equals("active")){%> --%>
<!-- 					<div id="collapse_3_2" class="panel-collapse collapse in"> -->
<%-- 					<%}else{ %> --%>
<!-- 					<div id="collapse_3_2" class="panel-collapse collapse"> -->
<%-- 					<%}%> --%>
							<div style="margin-top: 15px;margin-left: 20px;">	
								<%
									if (!mybeanfollowup.status.equals("")) {
								%>
								<%
									if (mybeanfollowup.status.equals("Update")) {
								%>
								<center>Follow-up List</center>
								<%
									}
								%>
								
							<div style="margin-right:20px;">
							<div><%=mybeanfollowup.followupHTML%></div>
								<div><center>&nbsp;
								</center></div>
								</div>
								</div>
								
								
								
								
								<div class="  panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
									<span class="caption panel-title" style="float:none">
										<center>
											<h4>
												<strong><%=mybeanfollowup.status%>
									Follow-up </strong>
											</h4>
										</center>
									</span>
								</div>
								
<!-- 								<div class="form-body"> -->
									<%
										if (mybeanfollowup.status.equals("Update")) {
									%>
									<center>
									<span><%=mybeanfollowup.msg%></span>
								</center>
									<div class="col-md-12" style="margin-top: 0px;margin-left: 4px;">
									<div class="form-group form-md-line-input">
										<label for="form_control_1">Current Followup Time:<span>*</span>
										<span style="color:#000000"><%=mybeanfollowup.current_preownedfollowup_time%></span>
										</label>
											
									</div>
									
									<div class="form-group form-md-line-input">
										<label for="form_control_1">Current Feedback Type<span>*</span>
										</label>
										 <select class="form-control" name="dr_feedbacktype" id="dr_feedbacktype">
										<%=mybeanfollowup.PopulateFeedbacktype()%>
										</select>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">Current Feedback Description<span>*</span>
										</label>
										<textarea class="form-control" rows="1"
											name="txt_preownedfollowup_desc" id="txt_preownedfollowup_desc" /><%=mybeanfollowup.followup_desc%></textarea>
									</div>
									<% } %>

<% if (mybean.preowned_preownedstatus_id.equals("1")) { %>
									<div class="form-group form-md-line-input">
										<label for="form_control_1"
											value="<%=mybeanfollowup.followup_date%>">Next Follow-up Date<span>*</span>:
										</label> <input type="" class="form-control"
											name="txt_followup_date" id="txt_followup_date"
											onclick="datePicker('txt_followup_date');" readonly>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1"
											value="<%=mybeanfollowup.temp_time%>"> Next
											Follow-up Time<span>*</span>:
										</label> <input type="" class="form-control"
											name="txt_followup_time" id="txt_followup_time" onclick="timePicker('txt_followup_time')" readonly>
									</div>

									<div class="form-group form-md-line-input">
										<label for="form_control_1">Next Follow-up Type<span>*</span>:
										</label> <select class="form-control" name="dr_followuptype"
											id="dr_followuptype"><%=mybeanfollowup.PopulateFollowuptype()%>
										</select>
									</div>
<!-- 								</div> -->
								<br>

								<div class="form-actions noborder">
									<center>
<!-- 										<input name="submitbutton" type="submit" class="btn1" id="submitbutton" value="Submit" onClick="return SubmitFormOnce(document.addfollowup, this);" /> -->
										<button type="button" class="btn1" name="submitbutton" id="submitbutton" >Submit</button> 
										<input type="hidden" name="submit_button" id="submit_button" value="yes">
										<input type="hidden" name="preowned_id" id="preowned_id" value="<%=mybean.preowned_id%>">
									</center>
									<%} %>
									<%} %>
									<br>
								</div>
								
							</div>
							
							
							</form>
						</div>
<!-- 	<script src="../assets/js/jquery.min.js" type="text/javascript"></script> -->
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/jquery.app.js" type="text/javascript"></script>
<!-- 	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script> -->
<script src="js/jquery.min.js" type="text/javascript"></script>
<!-- 	<script type="text/javascript" src="../Library/dynacheck-post.js"></script> -->
	<script src="js/dynacheck-post.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	 	<script src="js/axelamobilecall.js" type="text/javascript"></script> 
	 	<script src="js/components-select2.min.js"
		type="text/javascript"></script>
	 	<script src="js/select2.full.min.js" type="text/javascript"></script>
<script>
$(function() { 
$(".select2-container").css("width","100%");
});
</script>
<script>

$(document).ready(function() {
	$("#submitbutton").click(function() {
		checkForm();
	});
});

</script>

<script>
	var msg = "";
	function checkForm() {
		msg = "";
<%-- 		<%if(mybeanfollowup.status.equals("Update") && mybeanfollowup.branch_brand_id.equals("55")){%> --%>
// 		var followup_status = document.getElementById("dr_followupstatus").value;
// 		if (followup_status == '0') {
// 			msg += '<br>Select Current Follow-up Status!';
// 		}
<%-- 		<%}%> --%>
		
		<%if(mybeanfollowup.status.equals("Update")){%>
		var feedback_type = document.getElementById("dr_followuptype").value;
		if (feedback_type == '0') {
			msg += '<br>Select Current Feedback Type!';
		}
		<%}%>
		
		<%if(mybeanfollowup.status.equals("Update")){%>
		var followup_desc = document.getElementById("txt_preownedfollowup_desc").value;
		if (followup_desc == '') {
			msg += '<br>Enter Current Feedback Description!';
		}
		<%}%>
		
		<%if(mybeanfollowup.preowned_preownedstatus_id.equals("1")){%>
		var followup_date = document.getElementById("txt_followup_date").value;
		if (followup_date == '') {
			msg += '<br>Select Next Follow-up Date!';
		}
		
		var followup_time = document.getElementById("txt_followup_time").value;
		if (followup_time == '') {
			msg += '<br>Select Next Follow-up Time!';
		}
		
		var followup_type = document.getElementById("dr_followuptype").value;
		if (followup_type == '0') {
			msg += '<br>Select Next Follow-up Type!';
		}
		<%}%>
		<%if(mybeanfollowup.preownedfollowup_emp_id.equals("0")){%>
		msg += '<br>No Pre-Owned Consultant allocated!';
		<%}%>
		if (msg != '') {
			showToast(msg);
			return false;
		} else {
			document.getElementById('submit_button').value = "yes";
			document.getElementById('addfollowup').submit();
		}
		
	}
		</script>
</body>
<!-- END BODY -->
</html>