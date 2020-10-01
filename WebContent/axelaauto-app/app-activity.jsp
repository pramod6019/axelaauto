<!DOCTYPE html>

<html lang="en">
<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Activity"
	scope="request" />
<%
	mybean.doPost(request, response);
%>

<head>
<title>Activity | AxelaAuto</title>
<meta content="width=device-width, initial-scale=1" name="viewport" />

<link
	href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all"
	rel="stylesheet" type="text/css" />
<link href="css/simple-line-icons.min.css" rel="stylesheet"
	type="text/css" />
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="css/uniform.default.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-switch.css" rel="stylesheet" type="text/css" />
<link href="css/fullcalendar.css" rel="stylesheet" type="text/css" />
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">
<link href="css/plugins-md.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="favicon.ico" />
<style>

a {
	color: #fff;
}

.container {
	padding-right: 0px;
	padding-left: 0px;
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

span {
	color: red;
}
</style>
</head>

<body>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Calender</center></strong></span>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12">
			<form role="form" class="form-horizontal" name="form1" id="form1"
				method="post">
				<div class="form-body">
					<div class="row">
						<div class="col-md-12 col-xs-12">
							<div class="form-group form-md-line-input">
								<label for="form_control_1"> Sales Consultant<span>*</span>:
								</label> <select class="form-control" name="dr_executive"
									id="dr_executive" onchange="this.form.submit()">
									<%=mybean.exec%>")>
									<%=mybean.PopulateExe()%>
								</select>
							</div>
							
<!-- 							<div class="form-group form-md-line-input"> -->
<!-- 										<label for="form_control_1" -->
<%-- 											value=<%=mybean.startdate1%>> Date<span>*</span>: --%>
<!-- 										</label> <input type="date" class="form-control" -->
<!-- 											name="txt_activity_date" id="txt_activity_date" onchange="checkform();"  readonly> -->
<!-- 									</div> -->


                             <div class="form-group form-md-line-input">
								<label for="form_control_1">Date<span>*</span>:
								</label> <input type="text" class="form-control" name="txt_date"
									id="txt_date" onfocusout="checkform();" onclick="datePicker('txt_date');" 
									value=<%=mybean.startdate1%> readonly>
							</div>
							
							
<!-- 							<div class="form-group form-md-line-input"> -->
<!-- 								<label for="form_control_1">Date<span>*</span>: -->
<!-- 								</label> <input type="date" class="form-control" name="txt_date" -->
<!-- 									id="txt_date" onchange="checkform();" -->
<%-- 									value=<%=mybean.startdate1%> /> --%>
<!-- 							</div> -->

									
									
<!-- 									<div class="form-group form-md-line-input"> -->
<%-- 								<label for="form_control_1" value=<%=mybean.startdate1%>>Date<span>*</span>: --%>
<!-- 								</label> <input type="date" class="form-control" name="txt_activity_date" id="txt_activity_date" -->
<!-- 								maxlength="10" onclick="datepicker('txt_activity_date')" /> -->
<!-- 							</div> -->
							
						</div>
					</div>
				</div>
			</form>
		</div>
		
		<div class="container">
			<table class="table table-bordered scrollable">
				<%=mybean.StrHTML%>
			</table>
		</div>
	</div>

	<script>
		function checkform() {
			var exe = document.getElementById("dr_executive").value;
			if (exe == 0) {
				var msg = "Select Sales Consultant!"
				showToast(msg);
			} else {
				document.getElementById('form1').submit();
			}

		}
	</script>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/jquery.uniform.min.js" type="text/javascript"></script>
	<script src="js/bootstrap-switch.js" type="text/javascript"></script>
	<script src="js/moment.min.js" type="text/javascript"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script src="js/fullcalendar.js" type="text/javascript"></script>
	<script src="js/jquery-ui.min.js" type="text/javascript"></script>
	<script src="js/app.min.js" type="text/javascript"></script>

</body>

</html>