<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.axelaauto_app.App_Preowned_Testdrive_Add" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Add Test Drive | Axelaauto</title>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">



<script language="JavaScript" type="text/javascript">
	
          function FormSubmit()
          {
              document.formcontact.submit();
          }
// function testdriveCheck()
// {
// 	var veh_id=document.getElementById("dr_vehicle").value;
// 	var testdrivedate=document.getElementById("txt_testdrive_date").value;
// 	var branch_id=document.getElementById("branch_id").value;
	
// 	showHint('../axelaauto-app/testdrive-check.jsp?testdrive=yes&veh_id='+veh_id+'&testdrivedate='+testdrivedate+'&branch_id='+branch_id+'',  'calHint');
// }
	
function populatelocation(zone_id)
{
	showHint('../axelaauto-app/testdrive-check.jsp?testdrive=zone&testdrivezone_id='+zone_id+'',  'locHint');
}
	
function populatevehicle(zone_id, model_id)
{
	showHint('../axelaauto-app/testdrive-check.jsp?testdrive=vehicle&model_id='+model_id+'&testdrivezone_id='+zone_id+'',  'vehHint');
}

// function DisableSendEmail()
// {
// 	//alert("inside DisableSendEmail");
// 		<!--document.getElementById("chk_testdrive_sendemail").disabled=true;-->
// 		document.getElementById("chk_testdrive_sendemail").disabled = true;
// }

function start()
{
	//testdriveCheck();
	//DisableSendEmail();
	<%if (!mybean.msg.equals("")) {%>
	showToast('<%=mybean.msg%>
	')
<%}%>
	}
</script>

<style>
b {
	color: #8E44AD;
}

span {
	color: red;
}

.container {
	padding-right: 15px;
	padding-left: 15px;
	margin-right: auto;
	margin-left: auto;
	margin-top: 45px;
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
</style>

</head>

<body <%if(!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%} %>>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Add
						Pre-Owned Test Drive</center></strong></span>
		</div>
	</div>
	<div class="container">

		<form role="form" name="formcontact" id="formcontact"
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

				<div class="row">
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Customer: </label>
						<div class="col-md-9 col-xs-9">
							<label for="customer" class="form-control"><%=mybean.customer_name%>(<%=mybean.testdrive_customer_id%>)</label>

						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Contact:</label>
						<div class="col-md-9 col-xs-9">
							<label for="contact" class="form-control"><%=mybean.contact_name%>(<%=mybean.testdrive_contact_id%>)</label>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Pre-Owned Date: </label>
						<div class="col-md-9 col-xs-9">
							<label for="opprdate" class="form-control"><%=mybean.strToShortDate(mybean.enquiry_date)%></label>
							<input type="hidden" name="branch_id" id="branch_id"
								value="<%=mybean.enquiry_branch_id%>"> <input
								type="hidden" name="branch_id" id="branch_id"
								value="<%=mybean.enquiry_branch_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Enquiry ID: </label>
						<div class="col-md-9 col-xs-9">
							<label for="opprid" class="form-control"><%=mybean.testdrive_enquiry_id%></label>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Pre-Owned Consultant: </label>
						<div class="col-md-9 col-xs-9">
							<label for="executive" class="form-control"><%=mybean.executive_name%>(<%=mybean.testdrive_emp_id%>)</label>

						</div>
					</div>
					<!-- 					<div class="form-group form-md-line-input"> -->
					<!-- 						<label class="col-md-3 col-xs-3 control-label" -->
					<!-- 							for="form_control_1">Model: </label> -->
					<!-- 						<div class="col-md-9 col-xs-9"> -->
					<%-- 							<label for="model" class="form-control"><%=mybean.model_name%></label> --%>
					<!-- 						</div> -->
					<!-- 					</div> -->

					<!-- 					<div class="form-group form-md-line-input"> -->
					<!-- 						<label class="col-md-3 col-xs-3 control-label" -->
					<!-- 							for="form_control_1">Variant: </label> -->
					<!-- 						<div class="col-md-9 col-xs-9"> -->
					<%-- 							<label for="variant" class="form-control"><%=mybean.item_name%></label> --%>
					<!-- 						</div> -->
					<!-- 					</div> -->
				</div>

				<%-- <div class="form-group form-md-line-input">
					<label for="form_control_1">Zone<span>*</span>:
					</label> <select class="form-control" name="dr_testdrivezone_id"
						id="dr_testdrivezone_id"
						onChange="populatelocation(this.value);populatevehicle(this.value,<%=mybean.enquiry_model_id%>);">
						<%=mybean.PopulateZone()%></select>
				</div> --%>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Variant<span>*</span>:
					</label> <span id="vehHint"> <select name="dr_preownedstock_id"
						class="form-control" id="dr_preownedstock_id"
						onChange="TestDriveCheck()">
							<%=mybean.PopulateVariant()%>
					</select>
					</span>
				</div>


				<div class="form-group form-md-line-input">
					<label for="form_control_1">Location<span>*</span>:
					</label> <span id="locHint"> <%=mybean.PopulateLocation(mybean.comp_id)%>
					</span>
				</div>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Test Drive Type<span>*</span>:
					</label> <span id="locHint"> <select name="dr_testdrivetype"
						class="form-control" id="dr_testdrivetype"
						onChange="TestDriveCheck()">
							<%=mybean.PopulateTestDriveType()%>
					</select>
					</span>
				</div>

				<%-- <div class="form-group form-md-line-input">
					<label for="form_control_1">Test Drive Type<span>*</span>:
					</label> <select class="form-control" name="dr_testdrivetype" id="dr_testdrivetype"
						onChange="testdriveCheck()">
						<%=mybean.PopulateTestdriveType()%></select>
				</div> ------%>

				<div class="form-group form-md-line-input">
					<label for="form_control_1"> Test Drive Date<span>*</span>:
					</label> <input type="" class="form-control" name="txt_testdrive_date"
						id="txt_testdrive_date" maxlength="16"
						onclick="datePicker('txt_testdrive_date');"
						value="<%=mybean.testdrivedate%>" readonly>
				</div>
				<div class="form-group form-md-line-input">
					<label for="form_control_1"> Test Drive Time<span>*</span>:
					</label> <input type="" class="form-control" name="txt_testdrive_time"
						id="txt_testdrive_time" onclick="timePicker('txt_testdrive_time')"
						readonly>
				</div>

				<div class="form-group form-md-line-input ">
					<div class="skin skin-minimal">
						<div class="input-group">
							<div class="icheck-inline">

								<!-- 								<input type="checkbox" name="chk_testdrive_sendemail" class="icheck" -->
								<!-- 								 style="position: absolute; bottom:8px;" -->
								<!-- 									data-checkbox="icheckbox_square-grey" id="chk_testdrive_sendemail" -->
								<%-- 									<%=mybean.PopulateCheck(mybean.testdrive_sendemail)%> /> <label --%>
								<!-- 									style="position: relative; left: 20px; margin-right:15px;">Send Email</label>&nbsp;&nbsp;&nbsp; -->
								<input type="checkbox" class="icheck"
									data-checkbox="icheckbox_square-grey"
									name="chk_testdrive_confirmed" id="chk_testdrive_confirmed"
									style="position: absolute; font-color: red; bottom: 8px;"
									<%=mybean.PopulateCheck(mybean.testdrive_confirmed)%> /> <label
									style="position: relative; left: 20px;"> Confirmed </label>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group form-md-line-input">
					<label for="form_control_1">Notes:</label>
					<textarea name="txt_testdrive_notes" cols="60" rows="2"
						class="form-control" id="txt_testdrive_notes"><%=mybean.testdrive_notes%></textarea>

				</div>
				<br>
				<!-- 				<div class="form-actions noborder"> -->

				<center>
					<button type="button" class="btn1" name="addbutton" id="addbutton">Add
						Pre-Owned Test Drive</button>
					<input type="hidden" name="add_button" id="add_button" value="yes"><br>
					<br>
				</center>
				<!-- 				</div> -->

			</div>
		</form>
	</div>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/form-icheck.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript" src="../Library/dynacheck.js"></script>
	<script type="text/javascript" src="../Library/jquery.js"></script>
	<script type="text/javascript" src="../Library/jquery-ui.js"></script>
	<script type="text/javascript"
		src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
	<script src="js/jquery.min.js" type="text/javascript"></script>
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
			// 		var zone = document.getElementById("dr_testdrivezone_id").value;
			// 		if (zone == '0') {
			// 			msg += '<br>Select Zone!';
			// 		}
			var variant = document.getElementById("dr_preownedstock_id").value;
			if (variant == '0') {
				msg += '<br>Select Variant!';
			}
			var location = document.getElementById("dr_location").value;
			if (location == '0') {
				msg += '<br>Select Location!';
			}
			var testdrivetype = document.getElementById("dr_testdrivetype").value;
			if (testdrivetype == '0') {
				msg += '<br>Select test Drive Type!';
			}
			var testdrivedate = document.getElementById("txt_testdrive_date").value;
			if (testdrivedate == '') {
				msg += '<br>Select Test Drive Date!';
			}
			var testdrivetime = document.getElementById("txt_testdrive_time").value;
			if (testdrivetime == '') {
				msg += '<br>Select Test Drive Time!';
			}
			// 		if (msg != '') {
			// 			showToast(msg);
			// 		} else {
			// 			document.getElementById('formcontact').submit();
			// 		}
			if (msg != '') {
				showToast(msg);
			} else {
				document.getElementById('add_button').value = "yes";
				document.getElementById('formcontact').submit();
			}
		}
	</script>


</body>
<!-- END BODY -->
</html>

