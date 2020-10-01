<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.axelaauto_app.App_Preowned_CRM_Update" scope="request" />
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
.panel-heading {
	margin-bottom: 20px;
	background-color: #8E44AD;
	border: 1px solid transparent;
	border-radius: 0px;
	box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}

span {
	color: red;
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
</HEAD>


<!--     <body onLoad="FormFocus(); DisplayTicketExecutive();" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0"> -->
<body <%if(!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%} %>>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Update
						CRM Follow-up Follow-Up</center></strong></span>
		</div>
	</div>
	<div class="col-md-12">
		<br> <br> <br> <br>
		
		<form method="post" id="form_sample_1" class="form-horizontal">
			<!-- 			<div class="form-body"> -->

			<center>
				<% if (!mybean.StrScript.equals("")) { %>
				<b><%=mybean.StrScript%></b> <br> <br>
				<%
																}
															%>
			</center>
			<%=mybean.AppPreownedCRMCustomFieldView(mybean.comp_id,
					mybean.precrmfollowup_id, "2", request)%>


			<div class="form-group form-md-line-input">
				<label for="form_control_1">Mobile 1:</label> <input
					name="txt_precrmfollowup_mobile1" type="tel" class="form-control"
					id="txt_precrmfollowup_mobile1"
					value="<%=mybean.precrmfollowup_mobile1%>" size="32" maxlength="13" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Mobile 2:</label> <input
					name="txt_precrmfollowup_mobile2" type="tel" class="form-control"
					id="txt_precrmfollowup_mobile2"
					value="<%=mybean.precrmfollowup_mobile2%>" size="32" maxlength="13" />
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Email 1:</label> <input
					name="txt_precrmfollowup_email1" type="text" class="form-control"
					id="txt_precrmfollowup_email1"
					value="<%=mybean.precrmfollowup_email2%>" size="32" maxlength="255">
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Email 2:</label> <input
					name="txt_precrmfollowup_email2" type="text" class="form-control"
					id="txt_precrmfollowup_email2"
					value="<%=mybean.precrmfollowup_email2%>" size="32" maxlength="255">
			</div>

			<% if (mybean.precrmfollowupdays_lostfollowup.equals("1")) { %>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Lost Case 1<font color="#ff0000">*</font>:
				</label> <select name="dr_precrmfollowup_lostcase1_id" class="form-control"
					id="dr_precrmfollowup_lostcase1_id" onchange="populateLostCase2()">
					<%=mybean.PopulateLostCase1(mybean.comp_id, mybean.precrmfollowup_lostcase1_id)%>
				</select>&nbsp;<%=mybean.preownedlostcase1_name%>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Lost Case 2<font color="#ff0000">*</font>:
				</label>
				<span id="lostcase2"><%=mybean.PopulateLostCase2(mybean.comp_id, mybean.precrmfollowup_lostcase2_id, mybean.precrmfollowup_lostcase1_id)%></span>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Lost Case 3<font color="#ff0000">*</font>:
				</label>
				<span id="lostcase3"><%=mybean.PopulateLostCase3(mybean.comp_id, mybean.precrmfollowup_lostcase3_id, mybean.precrmfollowup_lostcase2_id)%></span>
			</div>

			<%} %>

			<% if (mybean.precrmfollowupdays_so_inactive.equals("1")) { %>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Cancel Reason :</label> <select
					name="dr_precrmfollowup_cancelreason_id" class="form-control"
					id="dr_precrmfollowup_cancelreason_id">
					<%=mybean.PopulateCancelReason(mybean.comp_id)%>
				</select>
			</div>

			<%} %>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Feedback<font color="#ff0000">*</font>:
				</label>
				<textarea name="txt_precrmfollowup_desc" cols="50" rows="4"
					class="form-control" id="txt_precrmfollowup_desc"
					onKeyUp="charcount('txt_precrmfollowup_desc', 'span_txt_crmfollowup_desc','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.precrmfollowup_desc%></textarea>
				<span id="span_txt_crmfollowup_desc"> (8000 characters)</span>
			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1">Feedback Type: <span> * </span></label>
				<select name="dr_feedbacktype" class="form-control"
					id="dr_feedbacktype" visible="true"
					onchange="populateAsterisk();populateExperience();">
					<%=mybean.PopulateCRMFeedbackType(mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input" id="experience">
				<label for="form_control_1">How was your overall experience?<span
					id="star" style="font-color: black;"><font color="#ff0000">*</font></span>:
				</label> <select name="dr_precrmfollowup_satisfied" class="form-control"
					id="dr_precrmfollowup_satisfied"
					onchange="DisplayTicketExecutive(); populateAsterisk();">
					<%=mybean.PopulateCRMSatisfied(mybean.comp_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input" id="concern">
				<label for="form_control_1">CRM Concern:<span id="star1"><font
						color="#ff0000">*</font></span>:
				</label> <select name="dr_precrmfollowup_crmconcern_id"
					id="dr_precrmfollowup_crmconcern_id" class="form-control">
					<%=mybean.PopulateConcern(mybean.comp_id, mybean.precrmfollowup_precrmconcern_id)%>
				</select>
			</div>

			<div class="form-group form-md-line-input" id="executive">
				<label for="form_control_1">Ticket Owner<span id="star1"><font
						color="#ff0000">*</font></span>:
				</label> <select name="dr_ticketowner_id" id="dr_ticketowner_id"
					class="form-control">
					<%=mybean.PopulateExecutive(mybean.comp_id)%>
				</select>
			</div>

			<% if (mybean.update.equals("yes") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Entry By:</label> <input
					class="form-control" type="text"
					value="<%=mybean.unescapehtml(mybean.entry_by)%>" readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Entry Date:</label> <input
					class="form-control" type="text" value="<%=mybean.entry_date%> "
					readonly>
			</div>

			<%} %>

			<% if (mybean.update.equals("yes") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Modified By:</label> <input
					class="form-control" type="text"
					value="<%=mybean.unescapehtml(mybean.modified_by)%>" readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Modified Date:</label> <input
					class="form-control" type="text" value="<%=mybean.modified_date%>"
					readonly>
			</div>
			<%} %>


			<div class="form-actions">
				<br>
				<center>
					<button type="submit" name="update_button" id="update_button"
						class="btn purple" value="Update Follow-up">Update CRM
						Follow-Up</button>
				</center>
				<br> <input type="hidden" name="preownedcrmfollowup_id"
					id="preownedcrmfollowup_id" value="<%=mybean.precrmfollowup_id%>"><input
					type="hidden" name="preownedcrmfollowup_modified_id"
					id="preownedcrmfollowup_modified_id"
					value="<%=mybean.precrmfollowup_modified_id%>"> <input
					type="hidden" name="preowned_id" id="preowned_id"
					value="<%=mybean.preowned_id%>"> <input type="hidden"
					name="crmfollowup_id" id="crmfollowup_id"
					value="<%=mybean.precrmfollowup_id%>"> <input type="hidden"
					name="crmfollowup_entry_id" id="crmfollowup_entry_id"
					value="<%=mybean.precrmfollowup_entry_id%>"> <input
					name="entry_by" type="hidden" id="entry_by"
					value="<%=mybean.entry_by%>"> <input type="hidden"
					name="entry_date" value="<%=mybean.entry_date%>"> <input
					name="modified_by" type="hidden" id="modified_by"
					value="<%=mybean.modified_by%>"> <input type="hidden"
					name="modified_date" value="<%=mybean.modified_date%>">
			</div>

		</form>
	</div>

	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		//document.formcontact.txt_customer_name.focus();
	}

	function DisplayExecutive() {
		var str = document.getElementById('dr_precrmfollowup_2ndday_q7').value;
		if (str == "2") {
			displayRow('executive');
		} else {
			hideRow('executive');
		}
	}
	function DisplayTicketExecutive() {
		var str1 = document.getElementById('dr_precrmfollowup_satisfied').value;
// 		alert("str1------------"+str1);
		if (str1 == "2") {
			displayRow('concern');
			displayRow('executive');
			
		} else {
			hideRow('concern');
			hideRow('executive');
			
		}
	}
	function DisplaylostcaseExecutive() {
		var str = document.getElementById('dr_precrmfollowup_lostcase_q3').value;
		if (str == "2") {
			displayRow('executive');
		} else {
			hideRow('executive');
		}
	}
	function Displayregno() {
		var str = document.getElementById('dr_precrmfollowup_2ndday_q4').value;
		//alert(str);

		if (str == "1" || str == "0") {
			hideRow('regno');
		} else {
			displayRow('regno');
		}
	}
	function populateLostCase2() {
		var precrmfollowup_lostcase1_id = document.getElementById("dr_precrmfollowup_lostcase1_id").value;
// 		alert("precrmfollowup_lostcase1_id------------"+precrmfollowup_lostcase1_id);
		showHint("../preowned/preowned-check.jsp?lostcase2=yes&precrmfollowup_lostcase1_id="
				+ precrmfollowup_lostcase1_id, "lostcase2");
	}
	function populateLostCase3() {
		var crm_lostcase2_id = document.getElementById("dr_precrmfollowup_lostcase2_id").value;
// 		alert("crm_lostcase2_id-------" +crm_lostcase2_id);
		showHint('../preowned/preowned-check.jsp?lostcase3=yes&precrmfollowup_lostcase2_id='
				+ crm_lostcase2_id, 'lostcase3');
	}
</script>

	<script>
	// if feedback type contactable sataisfied or 
	// dissatisfied is mandatory
	function populateAsterisk() {
		var contactable = document.getElementById("dr_feedbacktype").value;
		var satisfied = document.getElementById("dr_precrmfollowup_satisfied").value;
		if (contactable == 1) {
			$('#star').show();
		} else {
			$('#star').hide();
		}
		
		if(satisfied == 2){
			$('#star1').show();
		}
		else {
			$('#star1').hide();
		}
		
	}
	function populateExperience(){ // experience
		var feedback = document.getElementById("dr_feedbacktype").value;
// 	alert("feedback---------"+feedback);
	if(feedback == 2){
		$("#experience").hide();
		$("#concern").hide();
		$("#executive").hide();
	}else{
		$("#experience").show();
		$("#concern").show();
		$("#executive").show();
	}
	} 
</script>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.js" type="text/javascript"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/Validate.js"></script>
</body>
</html>

