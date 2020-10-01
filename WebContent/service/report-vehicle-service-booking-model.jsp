<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_Vehicle_Service_Booking_Model" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<style type="">
@media ( min-width : 992px) {
	.control-label {
		text-align: right;
	}
}

.modelstyle {
	height: auto;
}
</style>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<div id="reload" class="modelstyle">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">
				<center>
					<b>Postpone Booking</b>
				</center>
			</h4>
		</div>

		<div class="modal-body">
		<center><b><div class="colo-md-4" id="datetimeerrormsg" hidden></div></b></center>
			<div id ="timefield">
				<center>
					<label class="control-label col-md-4">Booking Postponed
						Time <font color="#ff0000">*</font>:
					</label>
					<div class="col-md-6 ">
						<input name="txt_postponed_time" id="txt_postponed_time"
							type="text" value="" class="form-control datetimepicker"
							onmouseover="$('.datetimepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY HH:mm',switchOnClick : true});"
							value="" />
					</div>
					<div class="row"></div>
					<br>
					<label class="control-label col-md-4">Booking Type<font color="#ff0000">*</font>:
					</label>
					<div class="col-md-6" style="margin-top: -16px;">
						<select name="dr_postponed_bookingtype_id" class="form-control"
							id="dr_postponed_bookingtype_id" visible="true"
							onchange="PickupBand();">
							<%=mybean.PopulateServiceBookingType(mybean.comp_id)%>
						</select>
					</div>
					<div id="pickupband"  <%if(!mybean.vehfollowup_bookingtype_id.equals("2")){ %> hidden  <%} %> >
					
					
					<div class="row"></div>
					<br>
					<label class="control-label col-md-4">Driver<font color="#ff0000">*</font>:
					</label>
					<div class="col-md-6" style="margin-top: -16px;">
						<select name="dr_postponed_driver_emp_id" class="form-control"
							id="dr_postponed_driver_emp_id" visible="true">
							<%=mybean.PopulateServicePickUp(mybean.comp_id)%>
						</select>
					</div>
					
					<div class="row"></div>
					<br> <label class="control-label col-md-4">Pickup Address:</label>
						<div class="col-md-6">
							<textarea name="txt_postponed_pickupaddress" cols="50" rows="5"
								class="form-control" id="txt_postponed_pickupaddress" ><%=mybean.vehfollowup_pickuplocation%></textarea>
						</div>
						
					</div>
					
					<div class="row"></div>
					<br> <label class="control-label col-md-4">Description<font color="#ff0000">*</font>:</label>
						<div class="col-md-6">
							<textarea name="txt_postponed_desc" cols="50" rows="5"
								class="form-control" id="txt_postponed_desc"
								onKeyUp="charcount('txt_postponed_desc', 'span_txt_postponed_desc',' <font color=red>({CHAR} characters left)</font>', '8000')"> <%=mybean.txt_postponed_desc%></textarea>
							<span style="margin-right: 150px;" id="span_txt_postponed_desc">(8000
								Characters) </span>
						</div>
				</center>

				<div class="row "></div>
				<br>
					<center>
						<input name="addpostpone" class="btn btn-success" id="addpostpone"
							type='button' value="Postpone" style="margin-top: 0px;"
							onclick="return myValidate();" /> <input type="hidden"
							name="add_button" id="add_button" value="yes"> <input
							type="hidden" name="vehfollowup_id" id="vehfollowup_id"
							value="<%=request.getParameter("vehfollowup_id")%>">
					</center>
			</div>
		</div>

	</div>

</body>
<script language="JavaScript" type="text/javascript">
	function myValidate() {
		var postponedate = $("#txt_postponed_time").val();
		var description = $("#txt_postponed_desc").val();
		var buttonval = $("#add_button").val();
		var vehfollowup_id = $("#vehfollowup_id").val();
		var dr_postponed_bookingtype_id = $("#dr_postponed_bookingtype_id").val();
		var dr_postponed_driver_emp_id = $("#dr_postponed_driver_emp_id").val();
		var txt_postponed_pickupaddress = $("#txt_postponed_pickupaddress").val();
		if (postponedate == '' || postponedate == 0) {
			$("#datetimeerrormsg").html("<b>Error!</br>Select Booking Postponed Time!</br>")
					.css("color", "red");
			$('#datetimeerrormsg').show();
			return false;
		}

		showHint(
				'../service/vehicle-service-booking-check.jsp?add_button=yes'
						+'&vehfollowup_id='+ vehfollowup_id
						+'&dr_postponed_bookingtype_id='+dr_postponed_bookingtype_id
						+'&dr_postponed_driver_emp_id='+dr_postponed_driver_emp_id
						+'&txt_postponed_pickupaddress='+txt_postponed_pickupaddress
						+'&postponed_time='+ postponedate 
						+'&txt_postponed_desc='+description
						+'', 'datetimeerrormsg');
		//calling disablebuttuon to disable the button
		console.log('description==='+description);
		setTimeout('disablebuttuon()', 400);
	}

	function disablebuttuon() {
		var message = $("#datetimeerrormsg").text();
		if (message == 'Booking Time Postponed!') {
			$('#timefield').hide();
		} else {
			$('#timefield').show();
		}
	}
	
	function PickupBand() {
		var bookingtype_id = $("#dr_postponed_bookingtype_id").val();
		if(bookingtype_id==2){
			$('#pickupband').show();
		}else{
			$('#pickupband').hide();	
		}
	}
	
</script>
<!-- this is to to provide UI property -->
<script>FormElements();</script>
</html>