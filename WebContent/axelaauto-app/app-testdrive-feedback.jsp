<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.axelaauto_app.App_TestDrive_Feedback" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Test Drive Feedback | Axelaauto</title>
<meta content="width=device-width, initial-scale=1" name="viewport">
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">
<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Validate.js"></script>

<script language="JavaScript" type="text/javascript">
function start()
{
	TestDriveCheck();
	<%if (!mybean.msg.equals("")) {%>
	showToast('<%=mybean.msg%> ')
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

<script>
function checkForm() {
		msg = "";
		var testdrive_taken = document.getElementById("dr_testdrivetaken").value;
		if (testdrive_taken == '0') {
			msg += '<br>Select Test Drive Taken or Not!';
		}
		
		var status=document.getElementById("dr_status").value;
		if (testdrive_taken == '1' && status=='0' ) {
			msg += '<br>Select Status!';
		}
		
		var comments=document.getElementById("txt_testdrive_fb_status_comments").value;
		if (status != '0' && comments=='' && testdrive_taken=='1'  ) {
			msg += '<br>Enter Status Comments!';
		}
		
			if(<%=mybean.testdrive_doc_value.equals("") %> && <%=!mybean.brand_id.equals("60")%> && testdrive_taken=='1' ){
				msg += '<br>Upload Driving Licence for Test Drive!';
			}
			
			if (msg != '') {
				showToast(msg);
			} else {
				document.getElementById('update_button').value = "Update Feedback";
				document.getElementById('formcontact').submit();
			}
			
	
}
	
	function TestDriveCheck() { //v1.0
		var testdrive_taken = document.getElementById("dr_testdrivetaken").value;
		var testdrive_status = document.getElementById("dr_status").value;
		var testdrive_type = document.getElementById("testdrive_type").value;
		if (testdrive_taken == "1") {
			document.getElementById("rowstatus").style.display = '';
			document.getElementById("rowstatus").style.visibility = 'visible';
			if (testdrive_status == "0") {
				//document.getElementById("feedHint").style.display='';
				//document.getElementById("feedHint").style.visibility='visible';
				document.getElementById("rowstatuscomments").style.display = 'None';
			} else {
				//document.getElementById("feedHint").style.display='None';
				document.getElementById("rowstatuscomments").style.display = '';
				document.getElementById("rowstatuscomments").style.visibility = 'visible';
			}
		} else {
			document.getElementById("rowstatus").style.display = 'None';
			document.getElementById("rowstatuscomments").style.display = 'None';
			//document.getElementById("feedHint").style.display='None';	
		}
	}
</script>

<!-- <script language="JavaScript" type="text/javascript">
		 $(function() {
    $( "#txt_testdrive_fb_delexp_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	
	});
		 
     </script> -->

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

<body onLoad="start();">
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Update
						Feedback</center></strong></span>
		</div>
	</div>
	<div class="container">
		<form role="form" name="formcontact" id="formcontact"
			class="form-horizontal" method="post">
			<div class="form-body">
				<div class="row">
					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Customer: </label>
						<div class="col-md-9 col-xs-9">
							<label for="customer" class="form-control"><%=mybean.customer_name%>
								(<%=mybean.testdrive_customer_id%>)</label>
						</div>
					</div>

					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Enquiry No: </label>
						<div class="col-md-9 col-xs-9">
							<label for="Enquiryno" class="form-control"><%=mybean.enquiry_no%></label>
						</div>
					</div>

					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Vehicle: </label>
						<div class="col-md-9 col-xs-9">
							<label for="Vehicle" class="form-control"><%=mybean.vehicle_name%></label>
						</div>
					</div>

					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Location: </label>
						<div class="col-md-9 col-xs-9">
							<label for="Location" class="form-control"><%=mybean.location_name%></label>
						</div>
					</div>

					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Test Drive Time: </label>
						<div class="col-md-9 col-xs-9">
							<label for="TestDriveTime" class="form-control"><%=mybean.testdrive_time_from%></label>
						</div>
					</div>

					<div class="form-group form-md-line-input">
						<label class="col-md-3 col-xs-3 control-label"
							for="form_control_1">Sales Consultant: </label>
						<div class="col-md-9 col-xs-9">
							<label for="Consultant" class="form-control"><%=mybean.executive_name%></label>
						</div>
					</div>
					<div class="col-md-12 col-xs-12">
						<div class="form-group form-md-line-input">
							<label class="control-label" for="form_control_1">Test
								Drive Taken<span>*</span>:
							</label> <select class="form-control" name="dr_testdrivetaken"
								id="dr_testdrivetaken" onChange="TestDriveCheck();">
								<option value=0>Select</option>
								<%=mybean.PopulateTestDriveTaken()%>
							</select> <input name="testdrive_type" type="hidden" id="testdrive_type"
								value="<%=mybean.testdrive_type%>">
						</div>
					</div>

					<div class="col-md-12 col-xs-12" id="rowstatus">
						<div class="form-group form-md-line-input">
							<label class="control-label" for="form_control_1">Status<span>*</span>:
							</label> <select class="form-control" name="dr_status" id="dr_status"
								onChange="TestDriveCheck();">
								<option value=0>Select</option>
								<%=mybean.PopulateStatus()%>
							</select>
						</div>
					<%
													if (mybean.brand_id.equals("60")) {
												%>
						<div class="form-group form-md-line-input">
							<label class="control-label" for="form_control_1">DL No.<span>*</<span>:
							</label><input name="txt_testdrive_license_no" class="form-control"
														size="20" maxlength="20" id="txt_testdrive_license_no" value="<%=mybean.testdrive_license_no%>"/>
						</div>
					
						<div class="form-group form-md-line-input">
							<label class="control-label" for="form_control_1">License Valid Till<span>*</span>:
							</label><input
														name="txt_testdrive_license_validdate"
														class="form-control datepicker"
														id="txt_testdrive_license_validdate" value="<%=mybean.testdrive_license_valid%>"
														onclick = "datePicker('txt_testdrive_license_validdate')" readonly/>
						</div>
					
					<%
													}
												%>
												</div>

					<div class="col-md-12 col-xs-12">
						<div class="form-group form-md-line-input" id="rowstatuscomments">
							<label class="control-label" for="form_control_1">Comments<span>*</span>:
							</label>
							<textarea name="txt_testdrive_fb_status_comments" rows="2" class="form-control"
								id="txt_testdrive_fb_status_comments"><%=mybean.testdrive_fb_status_comments%> </textarea>
						</div>
					</div>

					<div class="col-md-12 col-xs-12">
						<div class="form-group form-md-line-input">
							<label class="control-label" for="form_control_1">Notes:</label>
							<textarea name="txt_testdrive_fb_notes" cols="60" rows="2"
								class="form-control" id="txt_testdrive_fb_notes"><%=mybean.testdrive_fb_notes%></textarea><br>
						</div>
					</div>
					<br>

					<div class="form-actions noborder">

						<center>
						<button type="button" class="btn1" name="addbutton"
								id="addbutton">Update
								Feedback</button>
							<input name="testdrive_id" type="hidden" id="testdrive_id"
								value="<%=mybean.testdrive_id%>"> 
								<input name="enquiry_id" type="hidden" id="enquiry_id" value="<%=mybean.testdrive_enquiry_id%>"> <br>
								<input type="hidden"  name="update_button" id="update_button" value=""></input>
						</center>
						<br>
					</div>
				</div>
			</div>
			
		</form>
	</div>
	

</body>
<!-- END BODY -->
</html>

