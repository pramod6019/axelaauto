<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.service.Report_Vehicle_Service_Booking_Cancel_Model"
	scope="request" />
<%mybean.doPost(request, response); %>
<%=mybean.StrHTML%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap-timepicker.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet"
	type="text/css" />
<script src="../assets/js/components-date-time-pickers.js"
	type="text/javascript"></script>
<script src="../assets/js/bootstrap-datepicker.js"
	type="text/javascript"></script>
<script src="../assets/js/bootstrap-timepicker.js"
	type="text/javascript"></script>
<script src="../assets/js/components-date-time-pickers.js"
	type="text/javascript"></script>
<script src="../assets/js/bootstrap-datetimepicker.js"
	type="text/javascript"></script>

<script language="JavaScript" type="text/javascript">
function myValidate() {
var msg= "";
var vehfollowup_id = $("#vehfollowup_id").val();
var lostcase_id = $("#dr_veh_lostcase1_id").val();
var veh_followup_desc = $("#txt_veh_followup_desc").val();
var competitor_id = $("#dr_competitor_id").val();
if(lostcase_id == 0){
// 	msg +="Select Lost Case!";
 	$("#errormsg").html("<b>Select Lost Case!").css("color", "red");
 	return false;
}

if(lostcase_id==1 && competitor_id == 0){
// 	msg +="Enter Feedback!";
	$("#errormsg").html("<br><b>Select Competitor!").css("color", "red");
	return false;
}

if(veh_followup_desc =='' || veh_followup_desc == null){
// 	msg +="Enter Feedback!";
	$("#errormsg").html("<br><b>Enter Feedback!").css("color", "red");
	return false;
}

// $("#errormsg").html(msg);
		showHint('../service/report-vehicle-service-booking-cancel-model-check.jsp?add_button=yes&vehfollowup_id='
						+ vehfollowup_id + '&lostcase_id=' + lostcase_id + '&veh_followup_desc='+ veh_followup_desc + '&dr_competitor_id='+competitor_id+'', 'errormsg');
		//calling disablebuttuon to disable the button
		setTimeout('disablebuttuon()', 400);
	}
</script>

<script language="JavaScript" type="text/javascript">
$(document).ready(function(){
	var vehlostcase1_id = $("#dr_veh_lostcase1_id").val();
	if(vehlostcase1_id==1){
		 $("#competitorband").show();
	}
	else{
		 $("#competitorband").hide();
	}
})


function populateCompetetior(){
	var vehlostcase1_id = $("#dr_veh_lostcase1_id").val();
	if(vehlostcase1_id==1){
		 $("#competitorband").show();
	}
	else{
		 $("#competitorband").hide();
	}
}

</script>

<script>
	function disablebuttuon() {
		var message = $("#errormsg").text();
		if (message == 'Booking Cancelled!') {
			$('#lostcase').hide();
		} else {
			$('#lostcase').show();
		}
	}
</script>

<style type="">
@media ( min-width : 992px) {
	.control-label {
		text-align: right;
	}
}

.modelstyle {
	height: 400px;
}
</style>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<div id="reload" class="modelstyle">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">
				<center>
					<b>Cancel Booking</b>
				</center>
			</h4>
		</div>

		<div class="modal-body">
			<center>
				<div class="colo-md-4" id="errormsg"></div>
			</center>
			<div id="lostcase">
				<div class="form-group">
					<label class="col-md-3 control-label">Lost Case<font
						color="#ff0000">*</font>:
					</label>
					<div class="col-md-9">
						<select name="dr_veh_lostcase1_id" class="form-control"
							id="dr_veh_lostcase1_id" visible="true"
							onchange="populateCompetetior();">
							<%=mybean.PopulateVehLostcase1(mybean.comp_id, mybean.vehlostcase1_id)%>
						</select>
					</div>
				</div>
				<br>&nbsp;
					<div class="form-group" id="competitorband">
						<label class="control-label col-md-3">Competitor<font
							color="red">*</font>:
						</label>
						<div class="col-md-9">
							<select name="dr_competitor_id" class="form-control"
								id="dr_competitor_id" visible="true">
								<%=mybean.PopulateCompetitor(mybean.comp_id, mybean.competitor_id)%>
							</select>
						</div>
					</div>
					<span id="competitorerrormsg" style="margin-left: 26%"></span>
				<div class="form-group">
					<label class="col-md-3 control-label">Feedback<font
						color="#ff0000">*</font>:
					</label>
					<div class="col-md-9">
						<textarea name="txt_veh_followup_desc" cols="50" rows="4"
							class="form-control" id="txt_veh_followup_desc"
							onKeyUp="charcount('txt_veh_followup_desc', 'span_txt_veh_followup_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.veh_followup_desc%></textarea>
						<span id="span_txt_veh_followup_desc">1000 characters</span>
					</div>
				</div>
				<!-- 										<span id="reasonerrormsg" style="margin-left:26%"></span> -->
				<br>&nbsp;
				<center>
					<div class="form-group">
						<input name="addpostpone" class="btn btn-success" id="addpostpone"
							type='button' value="Cancel" style="margin-top: 0px;"
							onclick="return myValidate();" /> <input type="hidden"
							name="add_button" id="add_button" value="yes"> <input
							type="hidden" name="vehfollowup_id" id="vehfollowup_id"
							value="<%=request.getParameter("vehfollowup_id")%>">
					</div>
				</center>
				<br><br><br><br><br><br><br><br>
			</div>
		</div>
	</div>

</body>
</html>


