<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.ddmotors_app.Model_Variant_Details"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">

<link href="../ddmotors-assets/css/bootstrap.min.css" rel="stylesheet"
	type="text/css">
<link href="../ddmotors-assets/css/components-rounded.css"
	id="style_components" rel="stylesheet" type="text/css">
<link href="../ddmotors-assets/css/default.css" rel="stylesheet"
	type="text/css" id="style_color">
	
<script src="../ddmotors-assets/js/mobilecall.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/bootstrap.min.js"
	type="text/javascript"></script>
<script src="../ddmotors-assets/js/jquery.mobile.custom.min.js"
	type="text/javascript"></script>
<script src="../ddmotors-assets/js/jquery.mobile.custom.min.js"
	type="text/javascript"></script>
<script>
	$(document).ready(function() {
		$("#myCarousel").carousel({
			interval : 1000 * 3
		});
		$("#myCarousel").swiperight(function() {
			$("#myCarousel").carousel('prev');
		});
		$("#myCarousel").swipeleft(function() {
			$("#myCarousel").carousel('next');
		});
	});
</script>
<style>
.table td {
	color: #000;
	width: 30%;
	text-align: center;
}
.carousel-indicators li {
	display: inline-block;
	width: 20px;
	height: 20px;
	margin: 1px;
	text-indent: -999px;
	cursor: pointer;
	background-color: rgba(0, 0, 0, 0);
/* 	border: 1px solid #000; */
	border-radius: 20px;
	background-color: #000
}

.carousel-indicators .active {
	width: 20px;
	height: 20px;
	margin: 0;
	border: 2px solid #000;
}

.col-xs-4 {
	color: grey;
}

center {
	font-size: 20px;
}

b {
	color: red;
}

/* .panel-heading { */
/*      color: #fff;  */
/*      background-color: #000;  */
/*  } */
.btn {
	position: absolute;
	margin-top: 50px;
}

p.text {
	text-align: justify;
	margin-top: 20px;
	margin-bottom: 20px;
	margin-left: 20px;
	margin-right: 20px;
}
</style>

</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">
				<center><%=mybean.model_name%></center>
			</h3>
		</div>
	</div>
	<div class="container">
		<center>
			<b>&#8377;:<%=mybean.showroomprice%></b>
		</center>
		<div class="row">
			<%=mybean.StrHTML%>
		</div>
</div>
	
	<h3>&nbsp;</h3>
<br>

	<div class="container">
		<div class="col-md-12">
			<div class="col-xs-4 col-md-4" style="border-right: 1px dashed #e3e3e3;">
				Mileage<br> <b><%=mybean.model_mileage %> kmpl</b><br> <i>Upto</i>
			</div>
			<div class="col-xs-4 col-md-4" style="border-right: 1px dashed #e3e3e3;">
				Engine<br> <b><%=mybean.model_engine %> cc</b><br> <i>Upto</i>
			</div>
			<div class="col-xs-4 col-md-4">
				EMI<br> <b>&#8377; <%=mybean.model_emi%>/m</b><br>
				 <i>60 months</i>
			</div>

		</div>
	</div>
	<br>
	<!-- 	<div class="panel panel-default"> -->
	<!-- 		<div class="panel-heading"> -->
	<!-- 			<h3 class="panel-title"> -->
	<!-- 				<center>Variants</center> -->
	<!-- 			</h3> -->
	<!-- 		</div> -->
	<!-- 	</div> -->
	<!-- 	<div class="row"> -->
	<%--<%=mybean.StrHTMLdetails%> --%>
	<!-- 	</div> -->
	<div class="panel panel-default">
		<div class="panel-heading" style="color: #fff; background-color: #000">
			<h3 class="panel-title">
				<center>Technical Specification</center>
			</h3>
		</div>
	</div>

	
<!-- 		<div class="panel-group accordion scrollable" id="accordion2"> -->
<!-- 			<div class="panel panel-default"> -->
			<%=mybean.StrHTMLFeature %>
<!-- 				<div class="panel-heading" style="color: #fff; background-color: #000"> -->
<!-- 					<h4 class="panel-title"> -->
<!-- 						<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 							data-parent="#accordion2" href="#collapse_2_1">Dimensions </a> -->
<!-- 					</h4> -->
<!-- 				</div> -->
<!-- 				<div id="collapse_2_1" class="panel-collapse in"> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-striped table-bordered table-hover"> -->
<!-- 							<tbody> -->
<!-- 								<tr> -->
<!-- 									<td>Overall length</td> -->
								
<!-- 									<td>3395 mm</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Overall Width</td> -->
									
<!-- 									<td>1490 mm</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Overall Height</td> -->
									
<!-- 									<td>1475 mm</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Wheelbase</td> -->
									
<!-- 									<td>2360 mm</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Tread Front</td> -->
									
<!-- 									<td>1295 mm</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Tread Rear</td> -->
									
<!-- 									<td>1290 mm</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Minimum Turning Radius</td> -->
									
<!-- 									<td>4.6 m</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Ground Clearance</td> -->
									
<!-- 									<td>160 mm</td> -->
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table> -->

<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="panel panel-default"> -->
<!-- 				<div class="panel-heading" style="color: #fff; background-color: #000"> -->
<!-- 					<h4 class="panel-title"> -->
<!-- 						<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 							data-parent="#accordion2" href="#collapse_2_2">Weight</a> -->
<!-- 					</h4> -->
<!-- 				</div> -->
<!-- 				<div id="collapse_2_2" class="panel-collapse collapse"> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-striped table-bordered table-hover"> -->
<!-- 							<tbody> -->
<!-- 								<tr> -->
<!-- 									<td>Kerb Weight Alto 800</td> -->
									
<!-- 									<td>695 kg</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Kerb Weight Alto 800 LX</td> -->
									
<!-- 									<td>715 kg</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Kerb Weight Alto 800 LXi</td> -->
									
<!-- 									<td>720 kg. 725 kg (With Airbag)</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Gross Vehicle Weight&nbsp;</td> -->
									
<!-- 									<td>1185 kg</td> -->
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="panel panel-default"> -->
<!-- 				<div class="panel-heading" style="color: #fff; background-color: #000"> -->
<!-- 					<h4 class="panel-title"> -->
<!-- 						<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 							data-parent="#accordion2" href="#collapse_2_3">Engine</a> -->
<!-- 					</h4> -->
<!-- 				</div> -->
<!-- 				<div id="collapse_2_3" class="panel-collapse collapse"> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-striped table-bordered table-hover"> -->
<!-- 							<tbody> -->

<!-- 								<tr> -->
<!-- 									<td>Swept Volume</td> -->

<!-- 									<td>796 cc</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Engine Type</td> -->

<!-- 									<td>F8D</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>No. of Cylinders</td> -->

<!-- 									<td>3</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>No. of Valves</td> -->

<!-- 									<td>12</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Engine Control</td> -->

<!-- 									<td>32 Bit Computer</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Maximum Power</td> -->

<!-- 									<td>48 PS @ 6000 rpm</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Maximum Torque</td> -->

<!-- 									<td>69 Nm @ 3500 rpm</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Transmission</td> -->

<!-- 									<td>5 Speed MT</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>&nbsp;</td> -->

<!-- 									<td>All&nbsp; New Cable Type Gear Shift</td> -->
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="panel panel-default"> -->
<!-- 				<div class="panel-heading" style="color: #fff; background-color: #000"> -->
<!-- 					<h4 class="panel-title"> -->
<!-- 						<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 							data-parent="#accordion2" href="#collapse_2_4">Suspension -->
<!-- 							System</a> -->
<!-- 					</h4> -->
<!-- 				</div> -->
<!-- 				<div id="collapse_2_4" class="panel-collapse collapse"> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-striped table-bordered table-hover"> -->
<!-- 							<tbody> -->
<!-- 								<tr> -->
<!-- 									<td>Front</td> -->

<!-- 									<td>Gas Filled Mc Pherson Strut</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>&nbsp;</td> -->

<!-- 									<td>Torsion Roll Control Device</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Rear</td> -->

<!-- 									<td>Coil Spring, Gas Filled Shock</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>&nbsp;</td> -->

<!-- 									<td>&nbsp;Absorbers With Three Link Rigid</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>&nbsp;</td> -->

<!-- 									<td>Axle And Isolated Trailing Arm</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>&nbsp;</td> -->

<!-- 									<td>&nbsp;</td> -->
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="panel panel-default"> -->
<!-- 				<div class="panel-heading" style="color: #fff; background-color: #000"> -->
<!-- 					<h4 class="panel-title"> -->
<!-- 						<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 							data-parent="#accordion2" href="#collapse_2_5">Capacity</a> -->
<!-- 					</h4> -->
<!-- 				</div> -->
<!-- 				<div id="collapse_2_5" class="panel-collapse collapse"> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-striped table-bordered table-hover"> -->
<!-- 							<tbody> -->

<!-- 								<tr> -->
<!-- 									<td>Seating</td> -->

<!-- 									<td>5 Persons</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Fuel Tank</td> -->

<!-- 									<td>35 litres</td> -->
<!-- 								</tr> -->
<!-- 							</tbody> -->  
<!-- 						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->

<!-- 			<div class="panel panel-default"> -->
<!-- 				<div class="panel-heading" style="color: #fff; background-color: #000"> -->
<!-- 					<h4 class="panel-title"> -->
<!-- 						<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 							data-parent="#accordion2" href="#collapse_2_6">Tyres</a> -->
<!-- 					</h4> -->
<!-- 				</div> -->
<!-- 				<div id="collapse_2_6" class="panel-collapse collapse"> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-striped table-bordered table-hover"> -->
<!-- 							<tbody> -->
<!-- 								<tr> -->
<!-- 									<td>Tyre Size</td> -->

<!-- 									<td>145/80 R12 (Tubeless)</td> -->
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="panel panel-default"> -->
<!-- 				<div class="panel-heading" style="color: #fff; background-color: #000"> -->
<!-- 					<h4 class="panel-title"> -->
<!-- 						<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 							data-parent="#accordion2" href="#collapse_2_7">Brakes</a> -->
<!-- 					</h4> -->
<!-- 				</div> -->
<!-- 				<div id="collapse_2_7" class="panel-collapse collapse"> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-striped table-bordered table-hover"> -->
<!-- 							<tbody> -->
<!-- 								<tr> -->
<!-- 									<td>Front&nbsp;</td> -->

<!-- 									<td>Solid Disc</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>Rear</td> -->

<!-- 									<td>Drum</td> -->
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
		
	<div class="form-actions">
		<div class="col-md-5 col-xs-offset-4">
			<button type="button" class="btn" onclick="callURL('book-a-car.jsp?model_id=<%=mybean.model_id%>&item_id=<%=mybean.item_id%>')">Book This Car</button>
		</div>
	</div>
	
	<br><br><br><br><br>


	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>