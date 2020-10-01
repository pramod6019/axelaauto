<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Enquiry_Brochure_Email"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en">
<head>
<title>AxelaAuto</title>
<meta content="width=device-width, initial-scale=1" name="viewport" />

<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all"
	rel="stylesheet" type="text/css" />
<link href="css/simple-line-icons.min.css" rel="stylesheet"
	type="text/css" />
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="css/uniform.default.css" rel="stylesheet" type="text/css" />
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" />
<script src="js/axelamobilecall.js" type="text/javascript"></script>


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

b {
	color: #8E44AD;
}
</style>
</head>

<body <%if(!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%} %>>
 <div class="">
  <form role="form" class="form-horizontal" name="form1" id="form1"
					method="post">
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Email Brochure</center></strong>
			</span>
		</div>
		<div class="heading">
			<div class="col-md-12">
				
					<div class="form-body">
						<div class="row">
							<div class="col-md-12 col-xs-12">
								<div class="form-group form-md-line-input">
									<%-- <label for="form_control_1">Model: </label>
									<!-- <select class="form-control" name="dr_enquiry_model_id" id="dr_enquiry_model_id" onchange="this.form.submit()"> -->
								  <%=mybean.PopulateModel()%>  --%>
									<!-- </select> -->
								</div>
							</div> 
						</div>
					</div>
				
			</div>
		</div>
	</div>
	 
	


						<div><%=mybean.StrHTML%></div>
						</form></div>
	
	<script>
		function checkform() {
			var exe = document.getElementById("dr_model").value;
			if (exe == 0) {
				var msg = "Select Model!"
				showToast(msg);
			} else {
				document.getElementById('form1').submit();
			}

		}
	</script>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/table-datatables-scroller.js" type="text/javascript"></script>
	<script src="js/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="js/jquery.blockui.min.js" type="text/javascript"></script>
	<script src="js/jquery.uniform.min.js" type="text/javascript"></script>
	<script src="js/jquery-ui.min.js" type="text/javascript"></script>
	<script src="js/app.min.js" type="text/javascript"></script>
</body>
</html>