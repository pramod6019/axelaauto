<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Report_Executive_Dash"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html lang="en">
<head>

<title>AxelaAuto</title>

<meta content="width=device-width, initial-scale=1" name="viewport" />


<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">

<style>
a {
	color: #fff;
}

.container {
	padding-right: 0px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: auto;
	margin-top: 130px;
}

.panel-heading {
	margin-bottom: 0px;
	background-color: #8E44AD;
	border: 1px solid transparent;
	border-radius: 0px;
	box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}

.heading {
	position: fixed;
	width: 100%;
	top: 40;
	z-index: 1;
	background-color: #fff;
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
b{
color: #8E44AD;
}
</style>
</head>

<body>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title">
			 <strong><center>Sales Consultant Dash</center></strong>
			</span>
		</div>
		<div class="heading">
			<div class="col-md-12">
				<form role="form" class="form-horizontal" name="form1" id="form1"
					method="post">
					<div class="form-body">
						<div class="row">
							<div class="col-md-12 col-xs-12">
							
								<div class="form-group form-md-line-input">
									<label for="form_control_1">Sales Consultant<span>*</span>:
									</label>
									 <select class="form-control" name="dr_executive" id="dr_executive" onchange="this.form.submit()">
									 <%=mybean.PopulateSalesExecutives()%>
									</select>
								</div>
								
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>


	<div class="container">
		<table class="table table-hover table-light">
			<tbody>
				<tr>
					<td><b>Enquiry:</b></td>
					<td align="right"><b><%= mybean.enquirycount%></b></td>
				</tr>
				<tr>
					<td><b>Test Drives:</b></td>
					<td align="right"><b><%= mybean.testdrives%></b></td>
				</tr>
				<tr>
					<td><b>Sales Orders:</b></td>
					<td align="right"><b><%= mybean.salesordercount%></b></td>
				</tr>
				<tr>
					<td><b>Deliveries:</b></td>
					<td align="right"><b><%= mybean.deliverycount%></b></td>
				</tr>
			</tbody>
		</table>
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
	
</body>

</html>