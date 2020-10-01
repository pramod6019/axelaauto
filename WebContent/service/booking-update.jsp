<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Booking_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD> 
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">
<!-- <link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' /> -->

<link href="../assets/css/bootstrap-timepicker.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../assets/css/footable.core.css">
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />	
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
		 <style> 
		 
		@media (min-width: 992px) {
		#pickup_cal {
		min-height:830px;
		}
		
		} 
</style> 
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<%-- <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script> --%>
<script src="../assets/js/bootstrap-timepicker.js" type="text/javascript"></script>
<script src="../assets/js/components-date-time-pickers.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
<script src="../assets/js/footable.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>
		<script>
			$(function() {
				$("#tabs").tabs({
					event : "mouseover"
				});
			});

			// JavaScript
			$(function() {
				// Dialog
				$('#contact-dialog-modal').dialog({
					autoOpen : false,
					width : 900,
					height : 500,
					zIndex : 200,
					modal : true,
					title : "Select Contact"
				});
				$('#veh_contact_link')
						.click(
								function() {

									$
											.ajax({
												//url: "home.jsp",
												success : function(data) {
													$('#contact-dialog-modal')
															.html(
																	'<iframe src="../customer/customer-contact-list.jsp?group=select_booking_contact" width="100%" height="100%" frameborder=0></iframe>');
												}
											});
									$('#contact-dialog-modal').dialog('open');
									return true;
								});
			});

			$(function() {
				// Dialog
				$('#vehicle-dialog-modal').dialog({
					autoOpen : false,
					width : 900,
					height : 500,
					zIndex : 200,
					modal : true,
					title : "Select Vehicle"
				});
				$('#veh_booking_link')
						.click(
								function() {
									$
											.ajax({
												//url: "home.jsp",
												success : function(data) {
													$('#vehicle-dialog-modal')
															.html(
																	'<iframe src="../service/vehicle-list.jsp?group=select_veh_booking" width="100%" height="100%" frameborder=0></iframe>');
												}
											});
									$('#vehicle-dialog-modal').dialog('open');
									return true;
								});
			});

			$(function() {
				$("#txt_booking_time").datetimepicker({
					dateFormat : 'dd/mm/yy',
					stepMinute : 5
				});
				$("#txt_last_booking_time").datetimepicker({
					dateFormat : 'dd/mm/yy',
					stepMinute : 5
				});
				$("#txt_booking_followup_time").datetimepicker({
					dateFormat : 'dd/mm/yy',
					stepMinute : 5
				});

				$("#txt_courtesycar_startdate").datetimepicker({
					showButtonPanel : true,
					dateFormat : "dd/mm/yy"
				});

				$("#txt_courtesycar_enddate").datetimepicker({
					showButtonPanel : true,
					dateFormat : "dd/mm/yy"
				});
			});
		</script>
		<script language="JavaScript" type="text/javascript">
			function BranchCheck() {
				var status = document.getElementById("txt_status").value;
				var pickupdate = document.getElementById("txt_booking_time").value;
				var booking_branch_id = document
						.getElementById("dr_booking_branch_id").value;
				var booking_id = document.getElementById("txt_booking_id").value;
				if (status == 'Add') {
					var driver_id = document.getElementById("dr_pickup_emp_id").value;
					//for Courtesy Vehicle According to branch  
					var veh_id = document.getElementById("dr_vehicle").value;
					showHint('../service/pickup-check.jsp?branch_id='
							+ booking_branch_id + '', 'span_driver');
					showHint(
							'../service/pickup-check.jsp?pickup=yes&pickupdate='
									+ pickupdate + '&driver_id=' + driver_id
									+ '&branch_id=' + booking_branch_id + '',
							'calpickup');
					showHint(
							'../service/pickup-check.jsp?pickup_location=yes&branch_id='
									+ booking_branch_id,
							'dr_pickup_location_id');
					//for Courtesy Vehicle According to branch      
					showHint(
							'../service/courtesy-car-check.jsp?courtesy_veh=yes&branch_id='
									+ booking_branch_id, 'dr_vehicle');
				}
				//for PickUp According to branch     
				//alert("booking_branch_id===" + booking_branch_id);     
				showHint('../service/booking-check.jsp?booking_branch_id='
						+ booking_branch_id + '&booking_emp=yes',
						'dr_booking_emp_id');
				showHint('../service/booking-check.jsp?booking_branch_id='
						+ booking_branch_id + '&booking_time=' + pickupdate
						+ '&service_emp=yes', 'dr_booking_service_emp_id');
				showHint('../service/booking-check.jsp?booking_branch_id='
						+ booking_branch_id + '&booking_id=' + booking_id
						+ '&booking_time=' + pickupdate + '&parking=yes',
						'dr_booking_parking_id');
				showHint('../service/booking-check.jsp?booking_branch_id='
						+ booking_branch_id + '&location=yes', 'dr_location_id');
			}

			function SearchStockStatus(itemName) {
				var search_text = document.getElementById("txt_item").value;
				var location_id = document.getElementById("dr_location_id").value;
				showHint('booking-check.jsp?stock_status=yes&location_id='
						+ location_id + '&item_name=' + itemName
						+ '&search_text=' + search_text, 'div_stock_status');
			}

			function PickupCheck() { 
				//PickUp calendar..  
			
				var status = document.getElementById("txt_status").value;
				var pickupdate = document.getElementById("txt_booking_time").value;
				var branch_id = document.getElementById("dr_booking_branch_id").value;
				var booking_id = document.getElementById("txt_booking_id").value;
				if (status == 'Add') {
					
					var driver_id = document.getElementById("dr_pickup_emp_id").value;
					//for CourtesyCar calendar
					var veh_id = document.getElementById("dr_vehicle").value;
					var courtesydate = document
							.getElementById("txt_courtesycar_startdate").value;
					//for Pickup calendar 
					showHint(
							'../service/pickup-check.jsp?pickup=yes&driver_id='
									+ driver_id + '&pickupdate=' + pickupdate
									+ '&branch_id=' + branch_id + '',
							'calpickup');
					//for CourtesyCar calendar  
					showHint('../service/courtesy-car-check.jsp?veh_id='
							+ veh_id + '&courtesydate=' + courtesydate
							+ '&branch_id=' + branch_id + '&demo=yes',
							'courtesycar');
				}
				//alert("branch_id==="+branch_id);	 
				showHint('../service/booking-check.jsp?booking_branch_id='
						+ branch_id + '&booking_id=' + booking_id
						+ '&booking_time=' + pickupdate + '&parking=yes',
						'dr_booking_parking_id');
			}

			function ServiceExeCheck() {
				var pickupdate = document.getElementById("txt_booking_time").value;
				var branch_id = document.getElementById("dr_booking_branch_id").value;
				showHint('../service/booking-check.jsp?booking_branch_id='
						+ branch_id + '&booking_time=' + pickupdate
						+ '&service_emp=yes', 'dr_booking_service_emp_id');
			}

			function FormFocus() { //v1.0
				// document.form1.txt_stock_name.focus();
			}
			//For selecting existing contact
			function SelectContact(contact_id, contact_name, customer_id,
					customer_name, contact_address, contact_landmark,
					contact_mobile1, contact_mobile2, hide) {
				var contact_address = contact_address.replace("single_quote",
						"'");
				document.getElementById("span_contact_id").value = contact_id;
				document.getElementById("span_booking_contact_id").innerHTML = "<a href=../customer/customer-contact-list.jsp?contact_id="
						+ contact_id + ">" + contact_name + "</a>";
				document.getElementById("span_booking_customer_id").innerHTML = "<a href=../customer/customer-list.jsp?customer_id="
						+ customer_id + ">" + customer_name + "</a>";
				document.getElementById("customer_col").style.display = "";
				var status = document.getElementById("txt_status").value;
				if (status == 'Add') {
					showHint('../service/customer-dash-check.jsp?customer_id='
							+ customer_id + '&customerinfo=yes', 'tabs-a');
				}

				showHint('../service/booking-check.jsp?customer_id='
						+ customer_id + '&contact=yes', 'dr_booking_contact_id');

				var x = document.getElementById("txt_status").value;
				if (x == 'Add') {
					//Populating PickUp  
					document.getElementById("txt_pickup_contact_name").value = contact_name;
					document.getElementById("txt_pickup_mobile1").value = contact_mobile1;
					document.getElementById("txt_pickup_mobile2").value = contact_mobile2;
					document.getElementById("txt_pickup_add").value = contact_address;
					document.getElementById("txt_pickup_landmark").value = contact_landmark;
					//Populating Courtesy  
					document.getElementById("txt_courtesycar_contact_name").value = contact_name;
					document.getElementById("txt_courtesycar_mobile1").value = contact_mobile1;
					document.getElementById("txt_courtesycar_mobile2").value = contact_mobile2;
					document.getElementById("txt_courtesycar_add").value = contact_address;
					//alert();
					document.getElementById("txt_courtesycar_landmark").value = contact_landmark;
				}
				document.getElementById("courtesycar_customer_id").value = customer_id;
				document.getElementById("pickup_customer_id").value = customer_id;
				if (hide == 'hide') {
					document.getElementById("span_veh_id").value = '0';
					document.getElementById("txt_veh_id").value = '0';
					document.getElementById("span_booking_veh_id").innerHTML = "";
					document.getElementById("txt_item_name").value = "";
					document.getElementById("item_col").style.display = "none";
					document.getElementById("item_name").innerHTML = "";
					var status = document.getElementById("txt_status").value;
					if (status == 'Add') {
						showHint(
								'../service/vehicle-dash-check.jsp?veh_id=0&vehicleinfo=yes',
								'tabs-1');
						showHint(
								'../service/vehicle-dash-jobcard-check.jsp?veh_id=0&jobcardinfo=yes',
								'tabs-4');
						showHint(
								'../service/vehicle-dash-insurance-check.jsp?veh_id=0&insuranceinfo=yes',
								'tabs-5');
						showHint(
								'../service/vehicle-dash-insurance-followup-check.jsp?veh_id=0&followupinfo=yes',
								'tabs-6');
					}
				}
				$('#contact-dialog-modal').dialog('close');
				$('#vehicle-dialog-modal').dialog('close');
			}

			function SelectVehicle(veh_id, veh_reg_no, contact_id,
					contact_name, customer_id, customer_name, item_name,
					item_id, contact_address, contact_landmark,
					contact_mobile1, contact_mobile2) {
				var contact_address = contact_address.replace("single_quote",
						"'");
				document.getElementById("span_veh_id").value = veh_id;
				document.getElementById("span_booking_veh_id").innerHTML = "<a href=../service/vehicle-list.jsp?veh_id="
						+ veh_id + ">" + veh_reg_no + "</a>";
				document.getElementById("txt_item_name").value = item_name;
				document.getElementById("item_col").style.display = "";
				document.getElementById("item_name").innerHTML = "<b><a href=../inventory/inventory-item-list.jsp?item_id="
						+ item_id + ">" + item_name + "</a></b>";
				SelectContact(contact_id, contact_name, customer_id,
						customer_name, contact_address, contact_landmark,
						contact_mobile1, contact_mobile2, '');
				//ajax booking for showing vehicle info  
				var status = document.getElementById("txt_status").value;
				if (status == 'Add') {
					showHint('../service/vehicle-dash-check.jsp?veh_id='
							+ veh_id + '&vehicleinfo=yes', 'tabs-1');
					showHint(
							'../service/vehicle-dash-jobcard-check.jsp?veh_id='
									+ veh_id + '&jobcardinfo=yes', 'tabs-4');
					showHint(
							'../service/vehicle-dash-insurance-check.jsp?veh_id='
									+ veh_id + '&insuranceinfo=yes', 'tabs-5');
					showHint(
							'../service/vehicle-dash-insurance-followup-check.jsp?veh_id='
									+ veh_id + '&followupinfo=yes', 'tabs-6');
				}
				$('#contact-dialog-modal').dialog('close');
				$('#vehicle-dialog-modal').dialog('close');
			}

			function PopulateDetails(item_id, item_name, item_qty, locatoin_id,
					uom_name, cart_id, mode) {
				document.getElementById("booking_item_name").innerHTML = item_name
						.replace(/single_quote/g, "'");
				document.getElementById("uom_name").innerHTML = uom_name;
				document.getElementById("txt_item_id").value = item_id;
				document.getElementById("txt_location_id").value = locatoin_id;
				document.getElementById("txt_bookingcart_id").value = cart_id;
				document.getElementById("txt_item_qty").value = item_qty;
				if (mode == 'add') {
					document.getElementById("mode_button").innerHTML = "<input name=add_button id=add_button type=button class=btn btn-success value='Add' onClick='AddBookingItem();'/>";
				} else if (mode == 'update') {
					document.getElementById("mode_button").innerHTML = "<input name=update_button id=update_button type=button class=btn btn-success value='Update' onClick='UpdateBookingItem();'/>";
				}
			}

			// for add item to appt cart table
			function AddBookingItem() {
				var apptcart_item_id = document.getElementById("txt_item_id").value;
				var booking_location_id = document
						.getElementById("dr_location_id").value;
				var apptcart_qty = parseInt(document
						.getElementById("txt_item_qty").value);
				var session_id = document.getElementById("txt_session_id").value;
				//if((parseInt(jcitem_qty) <= parseInt(current_qty)) || current_qty=='_'){
				if (apptcart_qty == 0 || apptcart_qty == ''
						|| apptcart_qty == null || isNaN(apptcart_qty) == true) {
					apptcart_qty = 1;
				}

				showHint(
						'booking-item-details.jsp?apptcart_item_id='
								+ apptcart_item_id + '&booking_location_id='
								+ booking_location_id + '&session_id='
								+ session_id + '&apptcart_qty=' + apptcart_qty
								+ '&add_cartitem=yes', 'bookingitem_details');
				document.getElementById('mode_button').innerHTML = ' ';
				document.getElementById('txt_item_id').value = 0;
				document.getElementById('txt_location_id').value = 0;
				document.getElementById('txt_item_qty').value = 0;
				document.getElementById('booking_item_name').innerHTML = '';
				document.getElementById('txt_item').focus();
			}

			// for add item to appt cart table
			function UpdateBookingItem() {
				var apptcart_item_id = document.getElementById("txt_item_id").value;
				var booking_location_id = document
						.getElementById("txt_location_id").value;
				var session_id = document.getElementById("txt_session_id").value;
				var apptcart_id = document.getElementById("txt_bookingcart_id").value;
				var apptcart_qty = parseInt(document
						.getElementById("txt_item_qty").value);
				//if((parseInt(jcitem_qty) <= parseInt(current_qty)) || current_qty=='_'){
				if (apptcart_qty == 0 || apptcart_qty == ''
						|| apptcart_qty == null || isNaN(apptcart_qty) == true) {
					apptcart_qty = 1;
				}

				showHint('booking-item-details.jsp?apptcart_id=' + apptcart_id
						+ '&apptcart_item_id=' + apptcart_item_id
						+ '&booking_location_id=' + booking_location_id
						+ '&session_id=' + session_id + '&apptcart_qty='
						+ apptcart_qty + '&update_cartitem=yes',
						'bookingitem_details');
				document.getElementById('mode_button').innerHTML = ' ';
				document.getElementById('txt_item_id').value = 0;
				document.getElementById('txt_location_id').value = 0;
				document.getElementById('txt_item_qty').value = 0;
				document.getElementById('booking_item_name').innerHTML = '';
				document.getElementById('txt_item').focus();
			}

			function ListApptCartItem() {
				var session_id = document.getElementById("txt_session_id").value;
				showHint('booking-item-details.jsp?session_id=' + session_id
						+ '&list_cartitems=yes', 'bookingitem_details');
			}

			function delete_cart_item(apptcart_id) {
				var session_id = document.getElementById("txt_session_id").value;
				showHint('booking-item-details.jsp?session_id=' + session_id
						+ '&apptcart_id=' + apptcart_id
						+ '&delete_cartitem=yes', 'bookingitem_details');
			}
		</script>
		<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body <%if (mybean.add.equals("yes")) {%>
	onLoad="PickupCheck();ListApptCartItem();" <%}%>
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>
							&nbsp;Booking
						</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->

			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="booking-list.jsp?all=yes">List bookings</a> &gt;</li>
						<li><a href="booking-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;booking</a>:
							<li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%>
											Booking
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
									
										<form name="form1" method="post" class="form-horizontal">
										<input type="hidden" id="txt_status" name="txt_status" value="<%=mybean.status%>">
                                        <input type="hidden" id="txt_booking_id" name="txt_booking_id" value="<%=mybean.booking_id%>">
											<center>
												<font size="">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font>
											</center>

											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="control-label col-md-4">Branch<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_booking_branch_id" class="form-control"
																id="dr_booking_branch_id" onChange="BranchCheck();">
																<%=mybean.PopulateBranch(mybean.booking_branch_id, mybean.comp_id)%>
															</select>
														</div>
													</div>
												</div>

												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"> CRM
															Executive<font color="red">*</font>:
														</label>
														<div class="col-md-6">
															<span id="span_emp"> <select
																id="dr_booking_emp_id" name="dr_booking_emp_id"
																class="form-control">
																	<%=mybean.PopulateExecutive(mybean.booking_branch_id, mybean.comp_id)%>
															</select>
															</span>
														</div>
													</div>
												</div>


											</div>

											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="control-label col-md-4">Contact<font
															color="#ff0000">*</font>:
														</label>
														<div class="txt-align">
															<span id="span_booking_contact_id"
																name="span_booking_contact_id"> <%=mybean.link_contact_name%>
															</span></b>
															<%
																if (!mybean.booking_id.equals("0") && mybean.status.equals("Add")) {
															%>
															&nbsp;<a href="#" id="veh_contact_link"></a>
															<%
																} else {
															%>
															&nbsp;<a href="#" id="veh_contact_link">(Select
																Contact)</a>
															<%
																}
															%>
														</div>
													</div>
												</div>

												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"><span
															id="customer_col"
															style="display:<%=mybean.customer_display%>">Customer:</span></label>
														<div class="txt-align">
															<b><span id="span_booking_customer_id"
																name="span_booking_customer_id"> <%=mybean.link_customer_name%>
															</b></span>&nbsp;&nbsp; <input type="hidden" id="pickup_customer_id"
																name="pickup_customer_id"
																value="<%=mybean.pickup_customer_id%>" /> <input
																type="hidden" id="courtesycar_customer_id"
																name="courtesycar_customer_id"
																value="<%=mybean.courtesycar_customer_id%>" />
														</div>
													</div>
												</div>
											</div>
											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="control-label col-md-4">Contact:</label>
														<div class="col-md-6">
															<select name="dr_booking_contact_id" class="form-control"
																id="dr_booking_contact_id">
																<%=mybean.PopulateBookingContact(mybean.customer_id)%>
															</select>
														</div>
													</div>
												</div>

												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label">
															Service Advisor:</label>
														<div class="col-md-6">
															<select name="dr_booking_service_emp_id"
																class="form-control" id="dr_booking_service_emp_id">
																<%=mybean.PopulateServiceExecutive(mybean.booking_branch_id, mybean.booking_service_emp_id, mybean.comp_id)%>
															</select>
														</div>
													</div>
												</div>


											</div>
											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">


													<div class="form-group">
														<label class="control-label col-md-4">Booking
															Time:</label>
														<div class="col-md-6">
															<div class="input-group date form_datetime">
																<input type="text" size="16" name="txt_booking_time"
																	id="txt_booking_time" value="<%=mybean.booking_time%>"
																	class="form-control"> <span
																	class="input-group-btn">
																		<button class="btn default date-set" type="button">
																			<i class="fa fa-calendar"></i>
																		</button>
																</span>
															</div>
														</div>
													</div>

												</div>

												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"> Parking
															Lot:</label>
														<div class="col-md-6">
															<select name="dr_booking_parking_id" class="form-control"
																id="dr_booking_parking_id">
																<%=mybean.PopulateParking(mybean.booking_branch_id, mybean.booking_time, mybean.booking_id, mybean.comp_id)%>
															</select>
														</div>
													</div>
												</div>

											</div>
											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="control-label col-md-4">Instruction:</label>
														<div class="col-md-6">
															<textarea name="txt_booking_instruction"
																id="txt_booking_instruction" cols="70" rows="5"
																class="form-control"><%=mybean.booking_instruction%></textarea>

														</div>
													</div>
												</div>



												<%
													if (mybean.status.equals("Add")) {
												%>
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-4 control-label"> Last
															Booking Date:</label>
														<div class="txt-align">
															<div><%=mybean.last_booking_time%>
															</div>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-4 control-label"> Status:</label>
														<div class="txt-align">
															<div><%=mybean.bookingstatus_name%>
															</div>
														</div>
													</div>

													<div class="form-group">
														<label class="col-md-4 control-label"> Booking By:</label>
														<div class="txt-align">
															<div><%=mybean.booking_emp_name%>
															</div>
														</div>
													</div>
												</div>
											</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Update")) {
											%>
											<div class="container-fluid ">
												<!-- <div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="control-label col-md-4"></label>
														<div class="col-md-6"></div>
													</div>
												</div> -->
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="control-label col-md-4">Booking
															Status:</label>
														<div class="col-md-6">
															<select name="dr_booking_status_id" class="form-control"
																id="dr_booking_status_id">
																<%=mybean.PopulateBookingStatus(mybean.comp_id)%>
															</select>
														</div>
													</div>
												</div>

											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Add")) {
											%>
											<div class="tabbable tabbable-tabdrop" id="tabs">
												<ul class="nav nav-tabs">
													<li class="active"><a href="#tabs-1" data-toggle="tab">Vehicle
															Info</a></li>
													<li><a href="#tabs-a" data-toggle="tab">Customer</a></li>
													<li><a href="#tabs-stock" data-toggle="tab">Appt.
															Stock</a></li>
													<li><a href="#tabs-2" data-toggle="tab">Pickup</a></li>
													<li><a href="#tabs-3" data-toggle="tab">Courtesy
															Car</a></li>
													<li><a href="#tabs-4" data-toggle="tab">Job Cards</a></li>
													<li><a href="#tabs-5" data-toggle="tab">Insurance</a></li>
													<li><a href="#tabs-6" data-toggle="tab">Insurance
															Follow-up</a></li>
													<li><a href="#tabs-7" data-toggle="tab">Previous
															bookings</a></li>
												</ul>
												<div class="tab-content">
													<div class="tab-pane active" id="tabs-1">
														<%=mybean.veh_info%>
													</div>
													<div class="tab-pane" id="tabs-a">
														<%=mybean.Customer_dash.CustomerDetails(response, mybean.customer_id, "", mybean.comp_id)%>
													</div>
													<div class="tab-pane" id="tabs-stock">
														<div class="portlet box " style="margin-bottom: 5px;">
															<div class="portlet-title" style="text-align: center">
																<div class="caption" style="float: none">Stock
																	Status</div>
															</div>
															<div class="portlet-body portlet-empty">
																<div class="tab-pane" id="">
																	<!-- START PORTLET BODY -->
<!-- 																	<form class="form-horizontal"> -->
																		<div class="container-fluid ">
																			<div class="form-body col-md-6 col-sm-6">
																				<div class="form-group">
																					<label class="col-md-3 control-label">Location
																						No<font color="red">*</font>:
																					</label>
																					<div class="col-md-6" >
																						<select name="dr_location_id" class="form-control"
																							id="dr_location_id"
																							OnChange="SearchStockStatus(this.value);">
																							<%=mybean.PopulateLocation(mybean.booking_branch_id, mybean.comp_id)%></select>

																					</div>
																				</div>
																			</div>

																			<div class="form-body col-md-6 col-sm-6">
																				<div class="form-group">
																					<label class="col-md-3 control-label">Enter
																						Search Text:</label>
																					<div class="col-md-6" >
																						<input type="text" id="txt_item" name="txt_item"
																							class="form-control" size="42"
																							onkeyup="SearchStockStatus(this.value);" />
																					</div>
																				</div>
																			</div>
																		</div>
																		<div id="div_stock_status"></div>
<!-- 																	</form> -->
																</div>
															</div>
														</div>


														<div class="portlet box " style="margin-bottom: 5px;">
															<div class="portlet-title" style="text-align: center">
																<div class="caption" style="float: none">Item
																	Details</div>
															</div>
															<div class="portlet-body portlet-empty">
																<div class="tab-pane" id="">
																	<!-- START PORTLET BODY -->
<!-- 																	<form class="form-horizontal"> -->
																		<div class="container-fluid ">
																			<div style="display: inline" id="booking_item_name"></div>
																			<input name="txt_item_id" type="hidden"
																				id="txt_item_id" /> <input name="txt_location_id"
																				type="hidden" id="txt_location_id" /> <input
																				name="txt_session_id" type="hidden"
																				id="txt_session_id" value="<%=mybean.session_id%>" />
																			<input name="txt_bookingcart_id" type="hidden"
																				id="txt_bookingcart_id" />
																			<div class="form-body col-md-6 col-sm-6">
																				<div class="form-group">
																					<label class="col-md-3 control-label">Quantity:
																					</label>
																					<div class="col-md-6" >
																						<input name="txt_item_qty" type="text"
																							class="form-control" id="txt_item_qty" size="10"
																							maxlength="10" /> <span id="uom_name"></span>
																					</div>
																				</div>
																			</div>

																			<div class="form-body col-md-6 col-sm-6">
																				<div class="form-group">
																					<label class="col-md-3 control-label"></label>
																					<div class="col-md-6" ></div>
																				</div>
																			</div>
																		</div>
																		<div id="mode_button">
																			<!--input name="add_button" id="add_button" type="button" class="button" value="Add" onClick="AddBookingItem();"/-->
																		</div>
<!-- 																	</form> -->
																</div>
															</div>
														</div>
														<div id="bookingitem_details"></div>

													</div>
													<div class="tab-pane" id="tabs-2">
														<div class="container-fluid ">
															<div class="col-md-6">
																<div class="portlet box " style="margin-bottom: 5px;">
																	<div class="portlet-title" style="text-align: center">
																		<div class="caption" style="float: none">Add
																			Pickup</div>
																	</div>
																	<div class="portlet-body portlet-empty">
																		<div class="tab-pane" id="">
																			<!-- START PORTLET BODY -->
<!-- 																			<form class="form-horizontal"> -->
																				<div class="container-fluid ">

																					<div class="form-group">
																						<label class="col-md-3 control-label">Pickup
																							Type: </label>
																						<div class="col-md-6">
																							<select name="dr_pickup_pickuptype_id"
																								class="form-control"
																								id="dr_pickup_pickuptype_id">
																								<%=mybean.PopulatePickupType()%>
																							</select>
																						</div>
																					</div>



																					<div class="form-group">
																						<label class="col-md-3 control-label">Driver/Technician:</label>
																						<div class="col-md-6" >
																							<span id="span_driver"> <select
																								id="dr_pickup_emp_id" name="dr_pickup_emp_id"
																								class="form-control" onChange="PickupCheck();">
																									<%=mybean.PopulateDriver()%>
																							</select>
																							</span>
																						</div>
																					</div>

																					<div class="form-group">
																						<label class="col-md-3 control-label">Location<font
																							color="#ff0000">*</font>:
																						</label>
																						<div class="col-md-6" >
																							<select name="dr_pickup_location_id"
																								class="form-control" id="dr_pickup_location_id">
																								<%=mybean.PopulatePickUpLocation(mybean.booking_branch_id, mybean.comp_id)%>
																							</select>
																						</div>
																					</div>
																					<div class="form-group">
																						<label class="col-md-3 control-label">Contact
																							Person:</label>
																						<div class="col-md-6" >
																							<input name="txt_pickup_contact_name" type="text"
																								class="form-control"
																								id="txt_pickup_contact_name"
																								value="<%=mybean.pickup_contact_name%>"
																								size="20" maxlength="16" />
																						</div>
																					</div>
																					<div class="form-group">
																						<label class="col-md-3 control-label">Mobile
																							1<font color="#ff0000">*</font>:
																						</label>
																						<div class="col-md-6" >
																							<input name="txt_pickup_mobile1" type="text"
																								class="form-control" id="txt_pickup_mobile1"
																								value="<%=mybean.pickup_mobile1%>" size="20"
																								maxlength="13" />
																						</div>
																					</div>
																					<div class="form-group">
																						<label class="col-md-3 control-label">Mobile
																							2:</label>
																						<div class="col-md-6" >
																							<input name="txt_pickup_mobile2" type="text"
																								class="form-control" id="txt_pickup_mobile2"
																								value="<%=mybean.pickup_mobile2%>" size="20"
																								maxlength="13" />
																						</div>
																					</div>
																					<div class="form-group">
																						<label class="col-md-3 control-label">Address<font
																							color="#ff0000">*</font>:
																						</label>
																						<div class="col-md-6" >
																							<textarea name="txt_pickup_add" cols="40"
																								rows="4" class="form-control"
																								id="txt_pickup_add"
																								onKeyUp="charcount('txt_pickup_add', 'span_pickup_add','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.pickup_add%></textarea>
																							<br> <span id="span_pickup_add"> (255
																									Characters)</span><br>
																						</div>
																					</div>

																					<div class="form-group">
																						<label class="col-md-3 control-label">Landmark:</label>
																						<div class="col-md-6" >
																							<textarea name="txt_pickup_landmark" cols="40"
																								rows="4" class="form-control"
																								id="txt_pickup_landmark"
																								onKeyUp="charcount('txt_pickup_landmark', 'span_pickup_landmark','<font color=red>({CHAR} characters left)</font>', '255')"
																								><%=mybean.pickup_landmark%></textarea>
																							<br> <span id="span_pickup_landmark">
																									(255 Characters)</span>
																						</div>
																					</div>
																					<div class="form-group">
																						<label class="col-md-3 control-label">Instruction:</label>
																						<div class="col-md-6" >
																							<textarea name="txt_pickup_instruction" cols="40"
																								rows="4" class="form-control"
																								id="txt_pickup_instruction"
																								onKeyUp="charcount('txt_pickup_instruction', 'span_pickup_instruction','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.pickup_instruction%></textarea>
																							<br> <span id="span_pickup_instruction">
																									(1000 Characters)</span>
																						</div>
																					</div>
																				</div>

<!-- 																			</form> -->
																		</div>
																	</div>
																</div>

															</div>
															<div class="col-md-6">
																<div class="portlet box  ">
																	<div class="portlet-title" style="text-align: center">
																		<div class="caption" style="float: none">Pickup
																			Calendar</div>
																	</div>
																	<div class="portlet-body portlet-empty" id="pickup_cal">
																		<div class="tab-pane" id="">
																			<!-- START PORTLET BODY -->
																			<div id="calpickup"></div>

																		</div>
																	</div>
																</div>
															</div>
														</div>
													</div>
													<div class="tab-pane" id="tabs-3">
														<div class="container-fluid">
															<div class="col-md-6">
																<div class="portlet box  ">
																	<div class="portlet-title" style="text-align: center">
																		<div class="caption" style="float: none">Add
																			Courtesy Car</div>
																	</div>
																	<div class="portlet-body portlet-empty">
																		<div class="tab-pane" id="">
<!-- 																			<form class="form-horizontal"> -->
																				<div class="container-fluid ">
																					<div class="form-group">
																						<label class="col-md-3 control-label">Vehicle<font
																							color="#ff0000">*</font>:
																						</label>
																						<div class="col-md-6" >
																							<select name="dr_vehicle" class="form-control"
																								id="dr_vehicle" onchange="PickupCheck();">
																								<%=mybean.PopulateCourtesyVehicle(mybean.booking_branch_id)%>
																							</select>
																						</div>
																					</div>


																					<div class="form-group">
																						<label class="control-label col-md-3">Start
																							Time<font color=#ff0000><b>*</b></font>:
																						</label>
																						<div class="col-md-6">
																							<div class="input-group date form_datetime">
																								<input type="text" size="16"
																									name="txt_courtesycar_startdate"
																									id="txt_courtesycar_startdate"
																									value="<%=mybean.courtesycar_time_from%>"
																									class="form-control" onChange="PickupCheck()">
																									<span class="input-group-btn">
																										<button class="btn default date-set"
																											type="button">
																											<i class="fa fa-calendar"></i>
																										</button>
																								</span>
																							</div>
																						</div>
																					</div>

																					<div class="form-group">
																						<label class="control-label col-md-3">End
																							Time<font color=#ff0000><b>*</b></font>:
																						</label>
																						<div class="col-md-6">
																							<div class="input-group date form_datetime">
																								<input type="text" size="16"
																									name="txt_courtesycar_enddate"
																									id="txt_courtesycar_enddate"
																									value="<%=mybean.courtesycar_time_to%>"
																									class="form-control"><span
																									class="input-group-btn">
																										<button class="btn default date-set"
																											type="button">
																											<i class="fa fa-calendar"></i>
																										</button>
																								</span>
																							</div>
																						</div>
																					</div>
																					<div class="form-group">
																						<label class="col-md-3 control-label">Contact
																							Person:</label>
																						<div class="col-md-6" >
																							<span id="span_driver"> <input
																								name="txt_courtesycar_contact_name" type="text"
																								class="form-control"
																								id="txt_courtesycar_contact_name"
																								value="<%=mybean.courtesycar_contact_name%>"
																								size="20" maxlength="16"  />
																							</span>
																						</div>
																					</div>

																					<div class="form-group">
																						<label class="col-md-3 control-label">Mobile
																							1:</label>
																						<div class="col-md-6" >
																							<input name="txt_courtesycar_mobile1" type="text"
																								class="form-control"
																								id="txt_courtesycar_mobile1"
																								value="<%=mybean.courtesycar_mobile1%>"
																								size="20" maxlength="10" />
																						</div>
																					</div>

																					<div class="form-group">
																						<label class="col-md-3 control-label">Mobile
																							2<font color="#ff0000">*</font>:
																						</label>
																						<div class="col-md-6" >
																							<input name="txt_courtesycar_mobile2" type="text"
																								class="form-control"
																								id="txt_courtesycar_mobile2"
																								value="<%=mybean.courtesycar_mobile2%>"
																								size="20" maxlength="10" />
																						</div>
																					</div>
																					<div class="form-group">
																						<label class="col-md-3 control-label">Address<font
																							color="#ff0000">*</font>:
																						</label>
																						<div class="col-md-6" >
																							<textarea name="txt_courtesycar_add" cols="40"
																								rows="4" class="form-control"
																								id="txt_courtesycar_add"
																								onKeyUp="charcount('txt_courtesycar_add', 'span_courtesycar_add','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.courtesycar_add%></textarea>
																							<br> <span id="span_courtesycar_add">
																									(255 Characters)</span><br>
																						</div>
																					</div>

																					<div class="form-group">
																						<label class="col-md-3 control-label">Landmark<font
																							color="red">*</font>:
																						</label>
																						<div class="col-md-6" >
																							<textarea name="txt_courtesycar_landmark"
																								cols="40" rows="4" class="form-control"
																								id="txt_courtesycar_landmark"
																								onKeyUp="charcount('txt_courtesycar_landmark', 'span_courtesycar_landmark','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.courtesycar_landmark%></textarea>
																							<br> <span id="span_courtesycar_landmark">
																									(255 Characters)</span>
																						</div>
																					</div>
																					<div class="form-group">
																						<label class="col-md-3 control-label">Notes<font
																							color="red">*</font>:
																						</label>
																						<div class="col-md-6" >
																							<textarea name="txt_courtesycar_notes" cols="40"
																								rows="4" class="form-control"
																								id="txt_courtesycar_notes"><%=mybean.courtesycar_notes%></textarea>
																						</div>
																					</div>

																				</div>
<!-- 																			</form> -->

																		</div>
																	</div>
																</div>
															</div>
															<div class="col-md-6">
																<div class="portlet box " >
																	<div class="portlet-title" style="text-align: center">
																		<div class="caption" style="float: none">
																			Courtesy Car Calendar</div>
																	</div>
																	<div class="portlet-body portlet-empty" id="pickup_cal">
																		<div class="tab-pane" id="">
																			<!-- START PORTLET BODY -->
																			<div id="courtesycar"></div>

																		</div>
																	</div>
																</div>
															</div>
														</div>
													</div>
													<div class="tab-pane" id="tabs-4">
														<div class="portlet box  ">
															<div class="portlet-title" style="text-align: center">
																<div class="caption" style="float: none">Job Cards
																</div>
															</div>
															<div class="portlet-body portlet-empty">
																<div class="tab-pane" id="">
																	<!-- START PORTLET BODY -->
																	<%-- <%if(!mybean.booking_veh_id.equals("0")){%>
						 <a href="jobcard-update.jsp?add=yes&veh_id=<%=mybean.booking_veh_id%>">Add New Job Card</a>
														 
														  <%}%> --%>
																	<%=mybean.jobcard_info%>
																</div>

															</div>
															<%-- 				 --%>
														</div>

													</div>
													<div class="tab-pane" id="tabs-5">
														
																	<%=mybean.insurance_info%>


																
													</div>
													<div class="tab-pane" id="tabs-6">
														<div class="portlet box  ">
															<div class="portlet-title" style="text-align: center">
																<div class="caption" style="float: none">
																	Insurance Follow-up</div>
															</div>
															<div class="portlet-body portlet-empty">
																<div class="tab-pane" id="">
																	<!-- START PORTLET BODY -->
																	<%=mybean.insurance_followup_info%>

																</div>
															</div>
														</div>
													</div>
													<div class="tab-pane" id="tabs-7">
														<div class="portlet box  ">
															<div class="portlet-title" style="text-align: center">
																<div class="caption" style="float: none">Previous
																	bookings</div>
															</div>
															<div class="portlet-body portlet-empty">
																<div class="tab-pane" id="">
																	<!-- START PORTLET BODY -->
																	<%=mybean.previous_bookings_info%>

																</div>
															</div>
														</div>
													</div>


												</div>
											</div>

											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.booking_entry_by == null) && !(mybean.booking_entry_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="col-md-2 control-label"> Entry By:</label>
												<div class="txt-align">
													<div><%=mybean.unescapehtml(mybean.booking_entry_by)%>
													
													</div>
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="col-md-2 control-label"> Entry Date:</label>
												<div class="txt-align">
													<div><%=mybean.entry_date%>
													</div>
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.booking_modified_by == null) && !(mybean.booking_modified_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="col-md-2 control-label"> Modified By:</label>
												<div class="txt-align">
													<div><%=mybean.unescapehtml(mybean.booking_modified_by)%>
													</div>
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="col-md-2 control-label"> Modified
													Date:</label>
												<div class="txt-align">
													<div><%=mybean.modified_date%>
													</div>
												</div>
											</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Add")) {
											%>
											<center>
												
												 <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Booking" onclick="return SubmitFormOnce(document.form1,this);"/>
												 <input type="hidden" id="add_button" name="add_button" value="yes" />  
                                                  
											</center>
											<%
												} else if (mybean.status.equals("Update")) {
											%>
											<center>
												<input type="hidden" id="update_button" name="update_button"
													value="yes" /> <input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update Booking"
													onclick="return SubmitFormOnce(document.form1,this);" /> <input
													name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" onclick="return confirmdelete(this)"
													value="Delete Booking" />
											</center>
											<%
												}
											%>
											<div>
											<input type="hidden" name="booking_entry_by"
												value="<%=mybean.booking_entry_by%>" /> <input
												type="hidden" name="entry_date"
												value="<%=mybean.entry_date%>" /> <input type="hidden"
												name="booking_modified_by"
												value="<%=mybean.booking_modified_by%>" /> <input
												type="hidden" name="modified_date"
												value="<%=mybean.modified_date%>" />
												</div>
											</form>
         </div>
								</div>
							</div>



						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
<%@include file="../Library/admin-footer.jsp"%>

</body>
</HTML>
