<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Home"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html lang="en">
<title>AxelaAuto</title>
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />

<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/jquery.js"></script>
<script src="js/amcharts.js"></script>
<script src="js/serial.js"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>
<script>
/* function supportSVG() {
	return !!document.createElementNS && !! document.createElementNS('http://www.w3.org/2000/svg', "svg").createSVGRect; 
	
}
 */
</script>
<style>
b {
	color: #8E44AD;
}
.level1 {
	width: 90px;
	height: 90px;
	border: 2px solid #03a811;
	line-height: 90px;
	font-size: 20px;
}
.level2 {
	width: 90px;
	height: 90px;
	border: 2px solid #0a42a7;
	line-height: 90px;
	font-size: 20px;
}
.level3 {
	width: 90px;
	height: 90px;
	border: 2px solid #cc7e01;
	line-height: 90px;
	font-size: 20px;
}
.level4 {
	width: 90px;
	height: 90px;
	border: 2px solid #cc5200;
	line-height: 90px;
	font-size: 20px;
}
.level5 {
	width: 90px;
	height: 90px;
	border: 2px solid #cc0c00;
	line-height: 90px;
	font-size: 20px;
}
.type1 {
	width: 90px;
	height: 90px;
	border: 2px solid #006633;
	line-height: 90px;
	font-size: 20px;
}

.type2 {
	width: 90px;
	height: 90px;
	border: 2px solid #A80000;
	line-height: 90px;
	font-size: 20px;
}

.type3 {
	width: 90px;
	height: 90px;
	border: 2px solid #333399;
	line-height: 90px;
	font-size: 20px;
}

.circlebase {
	border-radius: 50px;
}

span {
	color: #606060;
	font-size: 16px;
}
</style>
</head>

<body>
	<div class="container" id="list">
		<center>
				<div class="img-responsive">
				<center>
					<%=mybean.image %><br>
					<h4><b><%=mybean.branch_name %></b></h4>
				</center>
			</div>
		</center>
		<center>
			<h4>
				<b>Welcome&nbsp;<%=mybean.emp_name%></b><br> <br> <b><%=mybean.month%>
					Status</b>
			</h4>
		</center>
		<div class="row">
			<div class="col-md-12">
				<div class="row text-center">
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
						<div class="panel1" id="enquirycount_id"
							onclick="callURL('app-enquiry-list.jsp?enquirystatus=monthenquries')">
							<h1><%=mybean.monthenquires%></h1>
							<p>Enquiries</p>
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
						<div class="panel2" id="bookingcount_id"
							onclick="callURL('app-veh-salesorder-list.jsp?enquirystatus=monthbooking')">
							<h1><%=mybean.monthbooking%></h1>
							<p>Bookings</p>
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
						<div class="panel3" id="deliverycount_id"
							onclick="callURL('app-veh-salesorder-list.jsp?enquirystatus=monthdelivered')">
							<h1><%=mybean.monthdeliveries%></h1>
							<p>Deliveries</p>
						</div>
					</div>
					<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
						<div class="panel4" id="cancelationcount_id"
							onclick="callURL('app-veh-salesorder-list.jsp?enquirystatus=monthcancellation')">
							<h1><%=mybean.monthcancellations%></h1>
							<p>Cancellations</p>
						</div>
					</div>

				</div>
			</div>
		</div>
		<div class="row">
			<center>
				<h4>
					<b>Today Status</b>
				</h4>
			</center>

			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase type1"
						onclick="callURL('app-enquiry-list.jsp?enquirystatus=todayenquires')">
						<%=mybean.todayenquires%>
					</div>
					<span>Enquiries</span>
				</center>
			</div>
			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase type2"
						onclick="callURL('app-veh-salesorder-list.jsp?enquirystatus=todaybooking')">
						<%=mybean.todaybooking%>
					</div>
					<span>Bookings</span>
				</center>
			</div>
			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase type3"
						onclick="callURL('app-veh-salesorder-list.jsp?enquirystatus=todaydeliveries')">
						<%=mybean.todaydeliveries%>
					</div>
					<span>Deliveries</span>
				</center>
			</div>
		</div>
		<br>
		<div class="row">
			<center>
				<h4>
					<b>Total Open Status</b>
				</h4>
			</center>

			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase type1"
						onclick="callURL('app-enquiry-list.jsp?enquirystatus=totalenquires')">
						<%=mybean.totalenquires%>
					</div>
					<span>Enquiries</span>
				</center>
			</div>
			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase type2"
						onclick="callURL('app-enquiry-list.jsp?enquirystatus=totalhotenquires')">
						<%=mybean.totalhotenquires%>
					</div>
					<span>Hot</span>
				</center>
			</div>
			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase type3"
						onclick="callURL('app-veh-salesorder-list.jsp?enquirystatus=totalbookings')">
						<%=mybean.totalbooking%>
					</div>
					<span>Bookings</span>
				</center>
			</div>
		</div>		
		
		<br><br>
		<center>
			<h4>
				<b>Enquiry Follow-up Escalation</b>
			</h4>
		</center>


<div class="row">			
			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase level1" 
						onclick="callURL('app-enquiry-list.jsp?enquirystatus=level1')">
						<%=mybean.level1%>
					</div>
					<span>Level 1</span>
				</center>
			</div>
			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase level2" 
						onclick="callURL('app-enquiry-list.jsp?enquirystatus=level2')">
						<%=mybean.level2%>
					</div>
					<span>Level 2</span>
				</center>
			</div>
			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase level3" 
						onclick="callURL('app-enquiry-list.jsp?enquirystatus=level3')">
						<%=mybean.level3%>
					</div>
					<span>Level 3</span>
				</center>
			</div>			
		</div>
		
		<div class="row">
				<div class="col-md-2 col-xs-2"></div>
				
			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase level4" 
						onclick="callURL('app-enquiry-list.jsp?enquirystatus=level4')">
						<%=mybean.level4%>
					</div>
					<span>Level 4</span>
				</center>
			</div>
			<div class="col-md-4 col-xs-4">
				<center>
					<div class="circlebase level5"
						onclick="callURL('app-enquiry-list.jsp?enquirystatus=level5')">
						<%=mybean.level5%>
					</div>
					<span>Level 5</span>
				</center>
			</div>
		</div>





		<div class="row" style="padding-left: 0px; padding-right: 0px;">
				<div id="chart_1" class="chart" style="height: 400px;"></div>
			
		</div>
	</div>

	<script>
		var ChartsAmcharts = function() {

			var initChartSample4 = function() {
				var chart = AmCharts
						.makeChart(
								"chart_1",
								{
									"type" : "serial",
									"theme" : "dark",
									"startDuration" : 3,
									"dataProvider" : [
											{
												"year" : '5',
												"escalation" : <%=mybean.level5%> ,
												"url" : "callurlapp-enquiry-list.jsp?enquirystatus=level5",
												"color" : "#FF0F00"
											},
											{
												"year" : '4',
												"escalation" : <%=mybean.level4%> ,
												"url" : "callurlapp-enquiry-list.jsp?enquirystatus=level4",
												"color" : "#FF6600"
											},
											{
												"year" : '3',
												"escalation" : <%=mybean.level3%> ,
												"url" : "callurlapp-enquiry-list.jsp?enquirystatus=level3",
												"color" : "#FF9E01"
											},
											{
												"year" : '2',
												"escalation" : <%=mybean.level2%> ,
												"url" : "callurlapp-enquiry-list.jsp?enquirystatus=level2",
												"color" : "#0D52D1"
											},
											{
												"year" : '1',
												"escalation" : <%=mybean.level1%> ,
												"url" : "callurlapp-enquiry-list.jsp?enquirystatus=level1",
												"color" : "#04D215"
											} ],
									"valueAxes" : [ {
										"position" : "top",
										"axisAlpha" : 0,
									} ],
									"startDuration" : 2,
								//	"startDuration" : supportsSVG() ? 1 : 0,
								//		"panEventsEnabled" : false,
										
									"graphs" : [ {
										//"balloonText": "<span style='font-size:13px; border:no-border'>[[title]] in [[category]]:<b>[[value]]</b></span>",
										"fillColorsField" : "color",
										"title" : "Escalation",
										"type" : "column",
										"fillAlphas" : 0.8,
										"lineAlphas" : 0.1,
										"valueField" : "escalation",
										"urlField" : "url"

									} ],
									"depth3D" : 40,
									"angle" : 30,
									"panEventsEnabled" : false,
									"chartCursor" : {
										"categoryBalloonEnabled" : false,
										"cursorAlpha" : 0,
										"zoomable" : false
									},
									"rotate" : true,
									"categoryField" : "year",
									"categoryAxis" : {
										"gridPosition" : "start"
									}
								});

//// 				function handleClick(event) {
// 					//$('#chart_1').attr('onclick', "document.location.href='http://localhost:8090/axelaauto-silverarrows/axelaauto-app/activity.jsp'");
					
// 				}
// 				chart.addListener("clickGraphItem", handleClick);

				$('#chart_1').closest('.portlet').find('.fullscreen').click(
						function() {
							chart.invalidateSize();
						});
			}

			return {
				///main function to initiate the module -----------------

				init : function() {

					initChartSample4();

				}

			};

		}();
		jQuery(document).ready(function() {
			ChartsAmcharts.init();
		});
	</script>
</body>
<!-- END BODY -->
</html>