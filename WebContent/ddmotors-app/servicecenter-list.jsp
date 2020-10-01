<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.ddmotors_app.ServiceCenter_List" scope="request" />
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
<link href="../ddmotors-assets/css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">
<link href="../ddmotors-assets/css/default.css" rel="stylesheet" type="text/css"
	id="style_color">

<script src="../ddmotors-assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/bootstrap.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/mobilecall.js" type="text/javascript"></script>

<style>
	b {
	color: #0f4c75;
}
.container {
	padding-right: 0px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: auto;
}
</style>
</head>
<body>
	<div class="container">
		<center>
			<h3>
				<b>Service Centers</b>
			</h3>
		</center>
		
						<%=mybean.StrHTML%>
		
<!-- 		<div class="panel-group accordion scrollable" id="accordion2">  -->
<!-- 			<div class="panel panel-default"> -->
<!-- 				<div class="panel-heading"> -->
<!-- 					<h4 class="panel-title"> -->
<!-- 						<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 							data-parent="#accordion2" href="#collapse_2_1">Service -->
<!-- 							Center1</a> -->
<!-- 					</h4> -->
<!-- 				</div> -->
<!-- 				<div id="collapse_2_1" class="panel-collapse collapse"> -->
<!-- 					<div class="panel-body"> -->

<!-- 						<table class="table table-responsive"> -->
<!-- 							<tr> -->
<!-- 								<td><b>Address:</b></td> -->

<!-- 								<td><b>Audi</b></td> -->
<!-- 							</tr> -->

<!-- 						</table> -->


<!-- 					</div> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-responsive"> -->
<!-- 							<tr> -->
<!-- 								<td><b>Phone:</b></td> -->

<!-- 								<td><b>Tel: </b><br> <b>Mob: </b></td> -->
<!-- 							</tr> -->

<!-- 						</table> -->

<!-- 					</div> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-responsive"> -->
<!-- 							<tr> -->
<!-- 								<td><b>Web:</b></td> -->

<!-- 								<td><b>Email: </b><br> <b>Web: </b></td> -->
<!-- 							</tr> -->

<!-- 						</table> -->

<!-- 					</div> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-responsive"> -->
<!-- 							<tr> -->
<!-- 								<td><b>Location:</b></td> -->

<!-- 								<td><b>Audi</b></td> -->
<!-- 							</tr> -->

<!-- 						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="panel panel-default"> -->
<!-- 				<div class="panel-heading"> -->
<!-- 					<h4 class="panel-title"> -->
<!-- 						<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 							data-parent="#accordion2" href="#collapse_2_2">Service -->
<!-- 							Center2</a> -->
<!-- 					</h4> -->
<!-- 				</div> -->
<!-- 				<div id="collapse_2_2" class="panel-collapse collapse"> -->
<!-- 					<div class="panel-body"> -->

<!-- 						<table class="table table-responsive"> -->
<!-- 							<tr> -->
<!-- 								<td><b>Address:</b></td> -->

<!-- 								<td><b>Audi</b></td> -->
<!-- 							</tr> -->

<!-- 						</table> -->


<!-- 					</div> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-responsive"> -->
<!-- 							<tr> -->
<!-- 								<td><b>Phone:</b></td> -->

<!-- 								<td><b>Tel: </b><br> <b>Mob: </b></td> -->
<!-- 							</tr> -->

<!-- 						</table> -->

<!-- 					</div> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-responsive"> -->
<!-- 							<tr> -->
<!-- 								<td><b>Web:</b></td> -->

<!-- 								<td><b>Email: </b><br> <b>Web: </b></td> -->
<!-- 							</tr> -->

<!-- 						</table> -->

<!-- 					</div> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-responsive"> -->
<!-- 							<tr> -->
<!-- 								<td><b>Location:</b></td> -->

<!-- 								<td><b>Audi</b></td> -->
<!-- 							</tr> -->

<!-- 						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
	</div>

</body>
<!-- END BODY -->
</html>