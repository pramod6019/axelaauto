<!DOCTYPE html>

<html lang="en">
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

<script src="../ddmotors-assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/bootstrap.min.js"
	type="text/javascript"></script>
<script src="../ddmotors-assets/js/mobilecall.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/jquery.mobile.custom.min.js"
	type="text/javascript"></script>

<style>
.table-bordered>tbody>tr>td, .table-bordered>tbody>tr>th,
	.table-bordered>tfoot>tr>td, .table-bordered>tfoot>tr>th,
	.table-bordered>thead>tr>td, .table-bordered>thead>tr>th {
	border: 3px solid #fff
}
.carousel-inner>.item>img, .carousel-inner>.item>a>img {
	width: 100%;
 	height: 250px; 
	margin: auto;
}

.container {
	padding-right: 0px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: auto;
}

.table td {
	width: 33%;
	height: 100px;
	text-align: center;
	color: #fff;
	
/* 	vertical-align: middle;  */
}

.carousel-indicators li {
	display: inline-block;
	width: 10px;
	height: 10px;
	margin: 1px;
	text-indent: -999px;
	cursor: pointer;
	background-color: rgba(0, 0, 0, 0);
	/* 	border: 1px solid #000; */
	border-radius: 10px;
	background-color: #fff
}

.carousel-indicators .active {
	width: 12px;
	height: 12px;
	margin: 0;
}

.bottom-title {
	position: relative;
	top: 15px;
	font-size: 12px;
	font-weight: bolder;
}
.icon-img{
 vertical-align: middle;
}
</style>
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
</head>
<body>
	<div class="container">

		<div id="myCarousel" class="carousel slide" data-ride="carousel">
			<ol class="carousel-indicators">
				<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
				<li data-target="#myCarousel" data-slide-to="1"></li>
				<li data-target="#myCarousel" data-slide-to="2" ></li>
				<li data-target="#myCarousel" data-slide-to="3" ></li>
				<li data-target="#myCarousel" data-slide-to="4" ></li>
				<li data-target="#myCarousel" data-slide-to="5" ></li>
				<li data-target="#myCarousel" data-slide-to="6" ></li>
				<li data-target="#myCarousel" data-slide-to="7" ></li>
				<li data-target="#myCarousel" data-slide-to="8" ></li>
			</ol>
			<div class="carousel-inner" role="listbox">
				
				<div class="item active">
					<img src="../ddmotors-assets/ifx/home-slides/ganesh chaturti.jpg"
						alt="" width="200" height="100">
				</div>
				
				<div class="item">
					<img src="../ddmotors-assets/ifx/home-slides/truevalue dwaraka.jpg"
						alt="" width="200" height="100">
				</div>
				
				<div class="item">
					<img src="../ddmotors-assets/ifx/home-slides/dehradun open now.jpg"
						alt="" width="200" height="100">
				</div>
				
			
				<div class="item">
						<img src="../ddmotors-assets/ifx/home-slides/mathiala now open.jpg"
							alt="" width="200" height="100">
				</div>
				
				<div class="item">
					<img src="../ddmotors-assets/ifx/home-slides/newdzire-banner.jpg"
						alt="" width="200" height="100" >
				</div>
				
				<div class="item">
					<img src="../ddmotors-assets/ifx/home-slides/ddm service.jpg"
						alt="" width="200" height="100">
				</div>
			
				<div class="item">
					<img src="../ddmotors-assets/ifx/home-slides/narela-web-banner.jpg"
						alt="" width="200" height="100">
				</div>

				<div class="item">
					<img src="../ddmotors-assets/ifx/home-slides/BalenoRS-banner.jpg" alt=""
						 width="200" height="100">
				</div>
				
				
				
				<div class="item">
					<img src="../ddmotors-assets/ifx/home-slides/Book now.jpg"
						alt="" width="200" height="100">
				</div>
				
			</div>
		</div>
		
		<table class="table table-bordered">
			<tr>
				<td style="background-color: #966FD6"
					onclick="callURL('model-list.jsp')"><img
					src="../ddmotors-assets/ifx/home-icons/model.png">
					<div class="bottom-title">MODELS</div></td>
					
				<td style="background-color: #FF6961"
					onclick="callURL('book-a-car.jsp?type=testdrive')">
						<img src="../ddmotors-assets/ifx/home-icons/test-drive.png">
					<div class="bottom-title">BOOK A TEST DRIVE</div></td>
					
				<td style="background-color: #77DD77"
					onclick="callURL('book-a-service.jsp')">
						<img src="../ddmotors-assets/ifx/home-icons/book-a-service.png">
					<div class="bottom-title"><b>BOOK A SERVICE</b></div></td>
			</tr>
			<tr>
				<td style="background-color: #FFB347"
					onclick="callURL('book-a-car.jsp?type=bookacar')"><img
					src="../ddmotors-assets/ifx/home-icons/my-car.png">
					<div class="bottom-title">BOOK A CAR</div></td>
				<td style="background-color: #779ECB"
					onclick="callURL('showroom-list.jsp')"><img
					src="../ddmotors-assets/ifx/home-icons/showroom.png">
					<div class="bottom-title">SHOWROOMS</div></td>
				<td style="background-color: #C23B22"
					onclick="callURL('servicecenter-list.jsp')"><img
					src="../ddmotors-assets/ifx/home-icons/service-center.png">
					<div class="bottom-title">SERVICE CENTERS</div></td>
			</tr>
			<tr>
				<td style="background-color: #CB99C9"
					onclick="callURL('aboutus.jsp')"><img
					src="../ddmotors-assets/ifx/home-icons/about-us.png">
					<div class="bottom-title">ABOUT US</div></td>
				<td style="background-color: #03C03C"
					onclick="callURL('offers-list.jsp')"><img
					src="../ddmotors-assets/ifx/home-icons/offers.png">
					<div class="bottom-title">OFFERS</div></td>
				<td style="background-color: #779ECB" onclick="callNo(09999003888)"><img
					src="../ddmotors-assets/ifx/home-icons/connect-now.png">
					<div class="bottom-title">CONNECT NOW</div></td>
			</tr>

		</table>

	</div>

</body>
<!-- END BODY -->
</html>