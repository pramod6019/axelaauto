<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.axelaauto_app.App_Preownedstock_Search" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en">
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">

<style>
b {
	color: #8f3e97;
}

.container {
	padding-right: 0px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: auto;
	margin-top: 45px;
}

span {
	color: red;
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
</style>
</head>

<body <%if (!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%}%>>
	<!-- onLoad="DisplayPreOwned();DisplayModel();" -->
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Pre-Owned
						Stock Status</center></strong></span>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12">
			<form role="form" id="frmaddenquiry" name="frmaddenquiry"
				class="form-horizontal" method="get"
				action="callurlapp-preownedstock-list.jsp">
				<div class="form-body">
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Brand<font color=red>*</font>:
						</label> <select name="drop_brand_id" id="drop_brand_id"
							class="form-control"
							onChange="PopulateBranch();PopulateRegion();">
							<%=mybean.PopulateBrand(mybean.comp_id)%>
						</select>
					</div>

					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Region:</label> 
<!-- 						<select -->
<!-- 							name="dr_region" id="dr_region" class="form-control" -->
<!-- 							onChange="PopulateBranch();"> -->
							<%=mybean.PopulateRegion(mybean.brand_id, mybean.comp_id)%>
<!-- 						</select> -->
					</div>

					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Branch:</label> 
<!-- 						<select -->
<!-- 							name="dr_branch" id="dr_branch" class="form-control"> -->
							<%=mybean.PopulateBranch(mybean.brand_id, mybean.region_id, mybean.comp_id)%>
<!-- 						</select> -->
					</div>

					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1"> Location: </label> <select
							name="dr_location_id" class="form-control" id="dr_location_id">
							<%=mybean.PopulateLocation(mybean.branch_id, mybean.comp_id)%>
						</select>
					</div>

					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Status: </label> <select
							name="dr_preownedstatus_id" class="form-control"
							id="dr_preownedstatus_id">
							<%=mybean.PopulateStatus(mybean.comp_id)%>
						</select>
					</div>


					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Pending Delivery:</label> <select
							name="dr_pending_delivery_id" class="form-control"
							id="dr_pending_delivery_id">
							<%=mybean.PopulatePendingdelivery()%>
						</select>
					</div>

					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Blocked: </label> <select
							name="dr_preownedstock_blocked" class="form-control"
							id="dr_preownedstock_blocked">
							<%=mybean.PopulateBlocked()%>
						</select>
					</div>

					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Ownership: </label> <select
							name="dr_preownedownership_id" class="form-control"
							id="dr_preownedownership_id">
							<%=mybean.PopulateOwnership(mybean.comp_id)%>
						</select>
					</div>
					<br>
					<div class="form-actions noborder">
						<center>
							<!-- 							<input name="submit_button1" type="submit" class="btn1" id="submit_button" value="Go" /> -->
							<button type="button" class="btn1" name="submit_button"
								id="submit_button">Go</button>
							<input type="hidden" name="submit_button1" id="submit_button1"
								value="Submit">
						</center>
						<br>
					</div>
				</div>
			</form>
		</div>
	</div>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.js" type="text/javascript"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/Validate.js"></script>
	<script>
		$(document).ready(function() {
			$("#submit_button").click(function() {
				checkForm();
			});
		});
	</script>
	<script>
		var msg = "";
		function checkForm() {
			msg = "";
			var brand_id = document.getElementById("drop_brand_id").value;
			if (brand_id == '0') {
				msg += 'Select Brand!';
			}
			if (msg != '') {
				showToast(msg);
			} else {
				document.getElementById('submit_button1').value = "Go";
				document.getElementById('frmaddenquiry').submit();
			}
		}
	</script>

	<script language="JavaScript" type="text/javascript">
		function PopulateBranch() {
			var brand_id = document.getElementById("drop_brand_id").value;
			var region_id = document.getElementById("dr_region").value;
			showHint("../axelaauto-app/app-preowned-mis-check.jsp?brand_id=" + brand_id
					+ "&region_id=" + region_id + "&branch=branchpss",
					"dr_branch");
		}
		function PopulateRegion() { //v1.0
			var brand_id = document.getElementById("drop_brand_id").value;
			var region_id = document.getElementById("dr_region").value;
			showHint("../axelaauto-app/app-preowned-mis-check.jsp?brand_id=" + brand_id
					+ "&region=regionpss", "dr_region");
		}
		//  function PopulateLocations(preowned_branch_id){
		// 		showHint("../preowned/stock-exe-check.jsp?preowned_branch_id="+preowned_branch_id+"&branch=yes","dr_location_id");

		// 	}
	</script>
</body>
<!-- END BODY -->
</html>