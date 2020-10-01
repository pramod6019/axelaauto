<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Report_Monitoring_Board"
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

<script src="js/axelamobilecall.js" type="text/javascript"></script>
<style>
a {
	color: #8E44AD;
}

.container {
	padding-right: 0px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: auto;
/* 	margin-top: 800px; */
}

.panel-heading {
	margin-bottom: 0px;
	background-color: #8E44AD;
	border: 1px solid transparent;
	border-radius: 0px;
	box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}

.heading {
/* 	position: fixed;  */
	width: 100%;
	top: 90;
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

<body onload="hide();">
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title">
			 <strong><center>Monitoring Board</center></strong>
			</span>
		</div>
	</div>
		<div class="heading">
			<div class="container" style="margin-top:30px">
				<form role="form" class="form-horizontal" name="form1" id="form1"
					method="post">
					<div class="form-body">
					<!-- 	<div class="form-group"> -->
							<div class="col-md-12">


						<div class="form-group form-md-line-input">
							<label for="form_control_1">From:</label> <input type="text"
								class="form-control" name="txt_from_date" id="txt_from_date"
								value="<%=mybean.fromdate%>"
								onclick="callme();datePicker('txt_from_date');"
								onchange="callme();"
								onfocusout="callme();this.form.submit();" readonly>
						</div>

						<div class="form-group form-md-line-input">
							<label for="form_control_1">To:</label> <input type="text"
								class="form-control" name="txt_to_date" id="txt_to_date"
								value="<%=mybean.todate%>" onclick="datePicker('txt_to_date');"
								onfocusout="this.form.submit();" readonly>
						</div>
						<br>

						<div class="form-element3 form-element-margin">
							<label>Include Inactive Executive: </label> <input
								type="checkbox" id="chk_include_inactive_exe"
								 onchange="this.form.submit();"
								name="chk_include_inactive_exe" <%=mybean.PopulateCheck(mybean.include_inactive_exe)%> />
						</div>

						<div class="form-element3 form-element-margin">
							<label>Include Pre-Owned: </label> <input type="checkbox"
								id="chk_include_preowned" name="chk_include_preowned"
								onchange="this.form.submit();"
								<%=mybean.PopulateCheck(mybean.include_preowned)%> />
						</div>


						<div>
								<input type="button" name="filter_button" id="filter_button" class="btn btn-sm" style="float: right;background-color:purple;color:white" onclick="FilterButton();" value="FILTER" > &nbsp;</input> 
								</div>
								
								<div class="form-group form-md-line-input" id="brandrow">
									<label for="form_control_1">Brand:</label> 
									<select class="form-control" name="dr_brand" id="dr_brand" onchange="this.form.submit();">
										<%=mybean.PopulatePrincipal(mybean.comp_id)%>
									</select>
								</div>
								
								<%if(!mybean.brand_id.equals("0")){%>
								<div class="form-group form-md-line-input" id="regionrow">
									<label for="form_control_1">Region:</label> 
									<select class="form-control" name="dr_region" id="dr_region" onchange="this.form.submit()">
										<%=mybean.PopulateRegion(mybean.comp_id)%>
									</select>
								</div>
								<%} %> 
								
								<%if(!mybean.region_id.equals("0")){%>
								<div class="form-group form-md-line-input" id="branchrow">
									<label for="form_control_1">Branch:</label> 
									<select class="form-control" name="dr_branch" id="dr_branch" onchange="this.form.submit()">
										<%=mybean.PopulateBranch(mybean.comp_id)%>
									</select>
								</div>
								<%} %> 
								
									<%if(!mybean.branch_id.equals("0") && !mybean.region_id.equals("0") && !mybean.brand_id.equals("0")){%>
								<div class="form-group form-md-line-input" id="teamrow">
									<label for="form_control_1">Team:</label> 
									<select class="form-control" name="dr_team" id="dr_team" onchange="this.form.submit()">
										<%=mybean.PopulateTeam(mybean.comp_id)%>
									</select>
								</div>
								<%} %> 
								
								<%if(!mybean.team_id.equals("0")){%>
								<div class="form-group form-md-line-input" id="emprow">
									<label for="form_control_1">Sales Consultant:</label> 
									<select class="form-control" name="dr_executive" id="dr_executive" onchange="this.form.submit()">
										<%=mybean.PopulateSalesExecutive(mybean.comp_id)%>
									</select>
								</div>
								<%} %> 
								
							</div>
						<!-- </div> ---->
					</div>
				</form>
			</div>
		</div>
	


	<div class="container">
	<%=mybean.StrHTML %>
	
	</div>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script>
	function hide() {
		var principal=<%=mybean.brand_id%>;
		if (principal == 0) {
				document.getElementById("brandrow").style.display = 'None';
				document.getElementById("branchrow").style.display = 'None';
				document.getElementById("emprow").style.display = 'None';
			}
		}

		function FilterButton() {
			var filter = document.getElementById("filter_button").value;
			if (filter == 'FILTER') {
				document.getElementById("brandrow").style.display = '';
				document.getElementById("brandrow").style.visibility = 'visible';
			} else {
				document.getElementById("brandrow").style.display = 'None';
			}

		}
		function callme(){
			alert('helo');
		}
		
	</script>
	
</body>

</html>