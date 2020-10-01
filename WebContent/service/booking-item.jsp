<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Booking_Item"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999 /xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../assets/css/footable.core.css">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">

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
						<h1><%=mybean.status%>&nbsp;Booking Item
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
						<li><a href="../service/appt.jsp">Bookings</a>&gt;</li>
						<li><a href="booking-list.jsp?all=yes">List Bookings</a>&gt;</li>
						<li><a href="booking-item.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Booking
								Item</a><b>:</b></li>

					</ul>
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<form name="form1" class="form-horizontal" method="post">
								<!-- 					BODY START -->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Update Booking
											Item</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->

											<div class="container-fluid">
												<div class="form-body col-md-12 col-sm-12">
													<div class="form-group">
														<label class="col-md-6 col-sm-5 control-label">
															Branch: </label>
														<div class="txt-align">
															<b><%=mybean.link_branch%></b>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-6 col-sm-5 control-label">
															Vehicle ID: </label>
														<div class="txt-align">
															<b><%=mybean.veh_id%></b>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-6 col-sm-5 control-label">
															Veh Reg. No.: </label>
														<div class="txt-align">
															<b><%=mybean.veh_reg_no%></b>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-6 col-sm-5 control-label">
															Item: </label>
														<div class="txt-align">
															<b><%=mybean.link_item%></b>
														</div>
													</div>
													<div class="form-group">
														<label class="col-md-6 col-sm-5 control-label">
															Booking Time: </label>
														<div class="txt-align">
															<b><%=mybean.booking_time%></b>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Stock Status</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 col-sm-5 control-label">
															Location<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select name="dr_location_id" class="form-control"
																id="dr_location_id"
																onChange="SearchStockStatus(this.value);">
																<%=mybean.PopulateLocation(mybean.booking_branch_id)%>
															</select>
														</div>
													</div>
												</div>
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 col-sm-5 control-label">
															Enter Search Text: </label>
														<div class="col-md-6">
															<input type="text" id="txt_item" name="txt_item"
																class="form-control" size="42"
																onkeyup="SearchStockStatus(this.value);" />
														</div>
													</div>
												</div>
												<div id="div_stock_status"></div>
											</div>
										</div>
									</div>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Item Details</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="container-fluid">
												<div style="display: inline" id="booking_item_name"></div>
												<input name="txt_item_id" type="hidden" id="txt_item_id" />
												<input name="txt_location_id" type="hidden"
													id="txt_location_id" /> <input name="txt_bookingitem_id"
													type="hidden" id="txt_bookingitem_id" value="" /> <input
													type="hidden" id="txt_booking_id" name="txt_booking_id"
													value="<%=mybean.booking_id%>" />

												<div class="form-group">
													<label class="col-md-6 col-sm-5 control-label">
														Quantity:</label>
													<div class="col-md-3">
														<input name="txt_item_qty" type="text"
															class="form-control" id="txt_item_qty" size="10"
															maxlength="10" /> <span id="uom_name"></span>
													</div>
												</div>
												<div id="mode_button">
													<!--<input name="add_button" id="add_button" type="button" class="button" value="Add" onClick="AddBookingItem();"/> -->
												</div>

											</div>

										</div>
									</div>
									<div id="bookingitem_details"><%=mybean.StrHTML%>
									</div>
								</div>
							</form>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript"
	src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
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
<script type="text/javascript">
	/// functions for quote items
	function SearchStockStatus(itemName) {
		//alert("itemName=="+itemName);  
		var search_text = document.getElementById("txt_item").value;
		var location_id = document.getElementById("dr_location_id").value;
		showHint('booking-check.jsp?stock_status=yes&location_id='
				+ location_id + '&item_name=' + itemName + '&search_text='
				+ search_text, 'div_stock_status');
	}

	function PopulateDetails(item_id, item_name, item_qty, location_id,
			uom_name, bookingitem_id, mode) {
		//var item_name = item_name.replace("")
		document.getElementById("booking_item_name").innerHTML = item_name
				.replace(/single_quote/g, "'");
		;
		document.getElementById("uom_name").innerHTML = uom_name;
		document.getElementById("txt_item_id").value = item_id;
		document.getElementById("txt_location_id").value = location_id;
		document.getElementById("txt_bookingitem_id").value = bookingitem_id;
		document.getElementById("txt_item_qty").value = item_qty;
		if (mode == 'add') {
			document.getElementById("mode_button").innerHTML = "<input name=add_button id=add_button type=button class=button value='Add' onClick='AddBookingItem();'/>";
		} else if (mode == 'update') {
			document.getElementById("mode_button").innerHTML = "<input name=update_button id=update_button type=button class=button value='Update' onClick='UpdateBookingItem();'/>";
		}
	}

	function AddBookingItem() {
		var bookingitem_booking_id = document.getElementById("txt_booking_id").value;
		var bookingitem_item_id = document.getElementById("txt_item_id").value;
		var bookingitem_location_id = document.getElementById("dr_location_id").value;
		var bookingitem_qty = parseInt(document.getElementById("txt_item_qty").value);
		//if((parseInt(jcitem_qty) <= parseInt(current_qty)) || current_qty=='_'){
		if (bookingitem_qty == 0 || bookingitem_qty == ''
				|| bookingitem_qty == null || isNaN(bookingitem_qty) == true) {
			bookingitem_qty = 1;
		}

		showHint('booking-check.jsp?bookingitem_booking_id='
				+ bookingitem_booking_id + '&bookingitem_item_id='
				+ bookingitem_item_id + '&bookingitem_location_id='
				+ bookingitem_location_id + '&bookingitem_qty='
				+ bookingitem_qty + '&add_bookingitem=yes',
				'bookingitem_details');
		document.getElementById('mode_button').innerHTML = ' ';
		document.getElementById('txt_item_id').value = 0;
		document.getElementById('txt_location_id').value = 0;
		document.getElementById('txt_item_qty').value = 0;
		document.getElementById('booking_item_name').innerHTML = '';
		document.getElementById('txt_item').focus();
	}

	// for add item to appt cart table
	function UpdateBookingItem() {
		var bookingitem_id = document.getElementById("txt_bookingitem_id").value;
		var bookingitem_booking_id = document.getElementById("txt_booking_id").value;
		var bookingitem_item_id = document.getElementById("txt_item_id").value;
		var bookingitem_location_id = document
				.getElementById("txt_location_id").value;
		var bookingitem_qty = parseInt(document.getElementById("txt_item_qty").value);
		if (bookingitem_qty == 0 || bookingitem_qty == ''
				|| bookingitem_qty == null || isNaN(bookingitem_qty) == true) {
			bookingitem_qty = 1;
		}

		showHint('booking-check.jsp?bookingitem_id=' + bookingitem_id
				+ '&bookingitem_item_id=' + bookingitem_item_id
				+ '&bookingitem_location_id=' + bookingitem_location_id
				+ '&bookingitem_booking_id=' + bookingitem_booking_id
				+ '&bookingitem_qty=' + bookingitem_qty
				+ '&update_bookingitem=yes', 'bookingitem_details');
		document.getElementById('mode_button').innerHTML = ' ';
		document.getElementById('txt_item_id').value = 0;
		document.getElementById('txt_location_id').value = 0;
		document.getElementById('txt_item_qty').value = 0;
		document.getElementById('booking_item_name').innerHTML = '';
		document.getElementById('txt_item').focus();
	}

	//
	function delete_booking_item(bookingitem_id, bookingitem_booking_id) {
		showHint('booking-check.jsp?bookingitem_booking_id='
				+ bookingitem_booking_id + '&bookingitem_id=' + bookingitem_id
				+ '&delete_bookingitem=yes', 'bookingitem_details');
	}
</script>
</body>