<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Testdrive_Add"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Add Test Drive | Axelaauto</title>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">


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

<body onLoad="start(); TestDriveCheck()">
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Add
						Test Drive</center></strong></span>
		</div>
	</div>
	<!-- 	<div class="container"> -->
	<div class="col-md-6" style="margin-top: 40px; margin-left: 4px;">
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
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1">Customer: </label>
						<div class="col-md-9 col-xs-8">
							<label for="customer" class="form-control"><%=mybean.customer_name%>&nbsp(<%=mybean.testdrive_customer_id%>)</label>

						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1">Contact:</label>
						<div class="col-md-9 col-xs-8">
							<label for="contact" class="form-control"><%=mybean.contact_name%>&nbsp(<%=mybean.testdrive_contact_id%>)</label>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1">Enquiry Date: </label>
						<div class="col-md-9 col-xs-8">
							<label for="opprdate" class="form-control"><%=mybean.strToShortDate(mybean.enquiry_date)%></label>
							<input type="hidden" name="branch_id" id="branch_id"
								value="<%=mybean.enquiry_branch_id%>"> <input
								type="hidden" name="branch_id" id="branch_id"
								value="<%=mybean.enquiry_branch_id%>">
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1">Enquiry ID: </label>
						<div class="col-md-9 col-xs-8">
							<label for="opprid" class="form-control"><%=mybean.testdrive_enquiry_id%></label>
						</div>
					</div>
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-4 control-label"
							for="form_control_1">Sales Consultant: </label>
						<div class="col-md-9 col-xs-8">
							<label for="executive" class="form-control"><%=mybean.executive_name%>(<%=mybean.testdrive_emp_id%>)</label>

						</div>
					</div>
				</div>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Model:<span>*</span>:
					</label>
					<div>
						<%=mybean.PopulateModel(mybean.comp_id, mybean.model_id)%>
					</div>
					<input name="model_id" type="hidden" id="model_id"
						value="<%=mybean.model_id%>" />
				</div>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Vehicle<span>*</span>:
					</label>
					<div id="vehicleHint">
						<%=mybean.PopulateVehicle(mybean.comp_id, mybean.model_id, mybean.enquiry_branch_id)%>
					</div>
				</div>


				<div class="form-group form-md-line-input">
					<label for="form_control_1">Location<span>*</span>:
					</label> <span id="locHint"> <%=mybean.PopulateLocation(mybean.comp_id)%>
					</span>
				</div>

				<div class="form-group form-md-line-input">
					<label for="form_control_1"> Test Drive Date<span>*</span>:
					</label> <input type="" class="form-control" name="txt_testdrive_date"
						id="txt_testdrive_date" maxlength="16"
						onclick="datePicker('txt_testdrive_date');"
						onfocusout="TestDriveCheck();" value="<%=mybean.testdrivedate%>"
						readonly>
				</div>
				<div class="form-group form-md-line-input">
					<label for="form_control_1"> Test Drive Time<span>*</span>:
					</label> <input type="" class="form-control" name="txt_testdrive_time"
						id="txt_testdrive_time"
						onclick="timePicker('txt_testdrive_time');"
						onfocusout="TestDriveCheck();" value="<%=mybean.testdrivetime%>"
						readonly>
				</div>

				<div class="form-group form-md-line-input ">
					<div class="skin skin-minimal">
						<div class="input-group">
							<div class="icheck-inline">
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
				<center>
					<button type="button" class="btn1" name="addbutton" id="addbutton">Add
						Test Drive</button>
					<input type="hidden" name="add_button" id="add_button" value="yes"><br>
					<br>
				</center>
			</div>
	</div>
	<!-- Start Test Drive Calender -->
	<div class="  panel-heading"
		style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
		<span class="panel-title">
			<center>
				<h4>
					<strong>Test Drive Calender</strong>
				</h4>
			</center>
		</span>
	</div>

	<div id="calHint">
		<center><%=mybean.strHTML%></center>
	</div>

	<!-- End Test Drive Calender -->


	</form>


	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/form-icheck.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript" src="../Library/dynacheck.js"></script>
	<script type="text/javascript" src="../Library/jquery.js"></script>
	<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>

	<script language="JavaScript" type="text/javascript">
	
          function FormSubmit() {
              document.formcontact.submit();
          }

function populatevehicle() {
	var item_model_id = document.getElementById("dr_model").value;
	var branch_id = document.getElementById("branch_id").value;
	showHint('../sales/testdrive-check.jsp?model=yes&item_model_id=' + item_model_id + '&branch_id=' + branch_id + '', 'vehicleHint');
}

function start() {
	<%if (!mybean.msg.equals("")) {%>
	showToast('<%=mybean.msg%>
	')
<%}%>
	}
</script>


	<script>
		$(document).ready(function() {
			$("#addbutton").click(function() {
				checkForm();
			});
		});
	</script>

	<script language="JavaScript" type="text/javascript">
		function TestDriveCheck() {
			var testdrivetime = ""
			var veh_id = document.getElementById("dr_vehicle").value;
			var testdrivedate = document.getElementById("txt_testdrive_date").value;
			var testdrivetime = document.getElementById("txt_testdrive_time").value;
			
			if (testdrivetime == '') {
				testdrivetime = "00:00"
			}
			testdrivedate = testdrivedate + ' ' + testdrivetime;
			var branch_id = document.getElementById("branch_id").value;

			showHint(
					'../axelaauto-app/app-testdrive-check.jsp?testdrive=yes&veh_id='
							+ veh_id + '&testdrivedate=' + testdrivedate
							+ '&branch_id=' + branch_id + '', 'calHint');
		}
	</script>

	<script>
		var msg = "";
		function checkForm() {
			msg = "";
			var vehicle = document.getElementById("dr_vehicle").value;
			var location = document.getElementById("dr_location").value;
			var testdrivedate = document.getElementById("txt_testdrive_date").value;
			var testdrivetime = document.getElementById("txt_testdrive_time").value;
			
			if (vehicle == '0') {
				msg += '<br>Select Vehicle!';
			}
			
			if (location == '0') {
				msg += '<br>Select Location!';
			}
			if (testdrivedate == '') {
				msg += '<br>Select Test Drive Date!';
			}
			if (testdrivetime == '') {
				msg += '<br>Select Test Drive Time!';
			}
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

