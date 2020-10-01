<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_CRM_Update"
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
<body onLoad="start()">
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Update
						<%=mybean.crmtype_name%>
				Follow-Up</center></strong></span>
		</div>
	</div>
	<div class="col-md-12">
		<br><br>
		<%
			if (!mybean.StrScript.equals("")) {
		%>
		<h3>
			<b><%=mybean.StrScript%></b>
		</h3>
		<%
			}
		%>
		<form method="post" id="form_sample_1" class="form-horizontal">
			<div class="form-body">

				<div>
					<input type="hidden" name="txt_add" id="txt_add" value="add">
					<input type="hidden" name="txt_crmfollowup_lostfollowup"
						id="txt_crmfollowup_lostfollowup" class="textbox"
						value="<%=mybean.crmdays_lostfollowup%>" /> <input type="hidden"
						name="txt_crmfollowupdays_id" id="txt_crmfollowupdays_id"
						class="textbox" value="<%=mybean.crm_crmdays_id%>" /> <input
						type="hidden" name="txt_StrScript" id="txt_StrScript"
						class="textbox" value="<%=mybean.StrScript%>" />
				</div>

				<%=mybean.CRMCustomFieldView(mybean.comp_id, mybean.crm_id, "2", request)%>
				<br>
				<div class="form-group">
					<label class="control-label col-md-6">Mobile 1: </label>
					<div class="col-md-6">
						<input name="txt_crm_mobile1" id="txt_crm_mobile1" type="tel"
							class="form-control" value="<%=mybean.crm_mobile1%>" size="13"
							maxlength="13" style="width: 250px" />
					</div>
					<div class="col-md-6 col-xs-6">(91-9999999999)</div>
				</div>
				

				<div class="form-group">
					<label class="control-label col-md-6">Mobile 2: </label>
					<div class="col-md-6">
						<input name="txt_crm_mobile2" id="txt_crm_mobile2" type="tel"
							class="form-control" value="<%=mybean.crm_mobile2%>" size="13"
							maxlength="13" style="width: 250px" />
					</div>
					<div class="col-md-6 col-xs-6">(91-9999999999)</div>
				</div>
				

				<div class="form-group">
					<label class="control-label col-md-6">Email 1: </label>
					<div class="col-md-6">
						<input name="txt_crm_email1" type="text" class="form-control"
							value="<%=mybean.crm_email1%>" size="32" maxlength="255"
							style="width: 250px" />
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-md-6">Email 2: </label>
					<div class="col-md-6">
						<input name="txt_crm_email2" type="text" class="form-control"
							value="<%=mybean.crm_email2%>" size="32" maxlength="255"
							style="width: 250px" />
					</div>
				</div>
				<% if (mybean.crmdays_lostfollowup.equals("1")) { %>
				<div class="form-group">
					<label class="control-label col-md-6">Lost Case 1:<span>
							* </span>
					</label>
					<div class="col-md-6">
						<span id="lostcase1"> <select name="dr_crm_lostcase1_id"
							class="form-control" id="dr_crm_lostcase1_id"
							onchange="populateLostCase2()">
								<%=mybean.PopulateLostCase1(mybean.comp_id, mybean.crm_lostcase1_id)%>
						</select></span>
						<div class="hint" id="hint_dr_crm_lostcase1_id"></div>
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-md-6">Lost Case 2:<span>
							* </span>
					</label>
					<div class="col-md-6">
						<span id="lostcase2"> <select name="dr_crm_lostcase2_id"
							class="form-control" id="dr_crm_lostcase2_id">
								<%=mybean.PopulateLostCase2(mybean.comp_id, mybean.crm_lostcase2_id, mybean.crm_lostcase1_id)%>
						</select></span>
					</div>
				</div>


				<div class="form-group">
					<label class="control-label col-md-6">Lost Case 3:<span>
							* </span>
					</label>
					<div class="col-md-6">
						<span id="lostcase3"> <select name="dr_crm_lostcase3_id"
							class="form-control" id="dr_crm_lostcase3_id">
								<%=mybean.PopulateLostCase3(mybean.comp_id, mybean.crm_lostcase3_id, mybean.crm_lostcase2_id)%>
						</select></span>
					</div>
				</div>
			</div>
			<% } %>

			<% if (mybean.crmdays_so_inactive.equals("1")) { %>

			<div class="form-group">
				<label class="control-label col-md-6">Cancel Reason: </label>
				<div class="col-md-6">
					<select name="dr_crm_cancelreason_id" class="form-control"
						id="dr_crm_cancelreason_id">
						<%=mybean.PopulateCancelReason(mybean.comp_id)%>
					</select>
				</div>
			</div>
			<%
				}
			%>

			<div class="form-group">
				<label class="control-label col-md-6">Feedback: <span>
						* </span>
				</label>
				<div class="col-md-6">
					<textarea class="form-control" name="txt_crmfollowup_desc" onchange="populateAsterisk();populateExperience();
						cols="50" rows="4" class="textbox" id="txt_crmfollowup_desc"><%=mybean.crm_desc%></textarea>
				</div>
			</div>

			<div class="form-group">
				<label class="control-label col-md-6">Feedback Type:<span>*</span>
				</label>
				<div class="col-md-6">
					<select name="dr_feedbacktype" class="form-control"
						id="dr_feedbacktype" onchange="populateAsterisk();populateExperience();">
						<%=mybean.PopulateCRMFeedbackType(mybean.comp_id)%>
					</select>
				</div>
			</div>

			<div class="form-group"id="experience">
				<label class="control-label col-md-6">How was your overall
					experience? </label>
				<div class="col-md-6">
					<select class="form-control" name="dr_crm_satisfied"
						id="dr_crm_satisfied" onchange="DisplayTicketExecutive(); populateAsterisk();"">
						<%=mybean.PopulateCRMSatisfied(mybean.comp_id)%>
					</select>
				</div>
			</div>
			
			<div class="form-group"id="concern">
				<label class="control-label col-md-6">CRM Concern<span
																id="star1"><font color="#ff0000">*</font></span>:</label>
				<div class="col-md-6">
					<select name="dr_crm_concern_id"
																id="dr_crm_concern_id" onchange="DisplayTicketExecutive(); populateAsterisk();" class="form-control">
																	<%=mybean.PopulateConcern(mybean.comp_id, mybean.crm_crmconcern_id)%>
															</select>
				</div>
			</div>

			<div class="form-group" id="executive">
				<label class="control-label col-md-6">Ticket Owner:</label>
				<div class="col-md-6">
					<select class="form-control" name="dr_ticketowner_id"
						id="dr_ticketowner_id">
						<%=mybean.PopulateExecutive(mybean.comp_id)%>
					</select>
				</div>
			</div>

			<%
				if (mybean.update.equals("yes") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
			%>

			<div class="form-group">
			<div class="col-md-12">
				<label class="control-label" style="color: #8E44AD;">Entry By:</label>
			

					<label><%=mybean.entry_by%></label> <input name="entry_by"
						type="hidden" id="entry_by" value="<%=mybean.entry_by%>">
				</div>
			</div>

			<div class="form-group">
			<div class="col-md-12">
				<label class="control-label" style="color: #8E44AD;">Entry Date:</label>
				
					<label><%=mybean.entry_date%></label> <input type="hidden"
						name="entry_date" value="<%=mybean.entry_date%>">
				</div>
			</div>
			<%
				}
			%>

			<%
				if (mybean.update.equals("yes") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
			%>
			<div class="form-group">
			<div class="col-md-12">
				<label class="control-label" style="color: #8E44AD;">Modified By:</label>
				
					<label><%=mybean.modified_by%></label> <input name="entry_by"
						type="hidden" id="entry_by" value="<%=mybean.modified_by%>">
				</div>
			</div>

			<div class="form-group">
			<div class="col-md-12">
				<label class="control-label" style="color: #8E44AD;">Modified Date:</label>
				

					<label><%=mybean.modified_date%></label> <input name="entry_by"
						type="hidden" id="entry_by" value="<%=mybean.modified_date%>">
				</div>
			</div>
			<%
				}
			%>
		
	</div>

	<div>
		<input type="hidden" name="enquiry_id" id="enquiry_id"
			value="<%=mybean.enquiry_id%>"> <input type="hidden"
			name="crmfollowup_id" id="crmfollowup_id" value="<%=mybean.crm_id%>">
		<input type="hidden" name="crmfollowup_entry_id"
			id="crmfollowup_entry_id" value="<%=mybean.crm_entry_id%>"> <input
			type="hidden" name="crmfollowup_modified_id"
			id="crmfollowup_modified_id" value="<%=mybean.crm_modified_id%>">
	</div>

	<div class="form-actions">
	
		<center>
			<button type="submit" name="update_button" id="update_button"
				class="btn purple" value="Update Follow-up">Update Follow-Up</button>
		</center>
		<br>
		<input type="hidden" name="crm_id" id="crm_id"
			value="<%=mybean.crm_id%>">
	</div>

	</form>
	</div>
	
	<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Validate.js"></script>
	
	<script> 
 function start()
 {
	 DisplayTicketExecutive();
 	<%if (!mybean.msg.equals("")) {%>
 	showToast('<%=mybean.msg%>')
<%}%>
	}

	function FormFocus() { //v1.0
		//document.formcontact.txt_customer_name.focus();
	}

			function DisplayTicketExecutive() {
				var str1 = document.getElementById('dr_crm_satisfied').value;
				if (str1 == "2") {
					displayRow('concern');
					displayRow('executive');
					
				} else {
					hideRow('concern');
					hideRow('executive');
					
				}
		}
			
	function populateLostCase2() {
		var crm_lostcase1_id = document.getElementById('dr_crm_lostcase1_id').value;
		showHint('app-enquiry-check.jsp?lostcase2=yes&crm_lostcase1_id='
				+ crm_lostcase1_id, 'lostcase2');
	}
	
	function populateLostCase3() {
		var crm_lostcase2_id = document.getElementById('dr_crm_lostcase2_id').value;
		showHint('app-enquiry-check.jsp?lostcase3=yes&crm_lostcase2_id='
				+ crm_lostcase2_id, 'lostcase3');
	}
</script>

<script>
	// if feedback type contactable sataisfied or 
	// dissatisfied is mandatory
	function populateAsterisk() {
		var contactable = document.getElementById("dr_feedbacktype").value;
		var satisfied = document.getElementById("dr_crm_satisfied").value;
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
	//alert("feedback---------"+feedback);
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

</body>
</html>

