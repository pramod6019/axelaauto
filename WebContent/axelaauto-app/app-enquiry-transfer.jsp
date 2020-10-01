<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybeantransfercheck" class="axela.sales.Enquiry_Transfer_Check" scope="request" />
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Enquiry_Transfer" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en">
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">
<link href="css/select2.min.css" rel="stylesheet" type="text/css" />
<link href="css/select2-bootstrap.min.css" rel="stylesheet" type="text/css" />
<style>
b {
	color: #8f3e97;
}

.container {
 	padding-right: 0px; 
 	padding-left: 0px; 
 	margin-right: auto; 
 	margin-left: 5px; 
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

.btn1{
/* background-color: #8E44AD; */
}
</style>
</head>
<body <%if(!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%} %>>
	<!-- onLoad="DisplayPreOwned();DisplayModel();" -->
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Transfer Enquiry</center></strong></span>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12">
			<form role="form" id="frmtransferenquiry" name="frmtransferenquiry"
				class="form-horizontal" method="post">
				<div class="form-body">
					<div class="alert alert-danger display-hide">
						<button class="close" data-close="alert"></button>
						You have some form errors. Please check below.
					</div>
					<div class="alert alert-success display-hide">
						<button class="close" data-close="alert"></button>
						Your form validation is successful!
					</div>
					
	                 <div class="form-group form-md-line-input">
						<label for="form_control_1">Brand<span>*</span>:</label> 
						<select name="dr_enquiry_brand_id" id="dr_enquiry_brand_id"
								class="form-control"
								onChange="PopulateBranch();PopulateModel();">
								<%=mybeantransfercheck.PopulateBrand(mybean.comp_id)%>
							</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Branch<span>*</span>:</label> 
						<select name="dr_enquiry_branch_id" id="dr_enquiry_branch_id"
								class="form-control" onChange="PopulateTeam();">
								<option value="0">Select</option>
							</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Model<span>*</span>:</label> 
						<select name="dr_enquiry_model_id" id="dr_enquiry_model_id"
								class="form-control" onChange="PopulateItem();">
								<option value="0">Select</option>
							</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">variant<span>*</span>:</label> 
						<select name="dr_enquiry_item_id" id="dr_enquiry_item_id"
								class="form-control">
								<option value="0">Select</option>
							</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Team<span>*</span>:</label> 
						<select name="dr_enquiry_team_id" id="dr_enquiry_team_id"
								class="form-control" onChange="PopulateExecutive();">
								<option value="0">Select</option>
							</select>
					</div>
					
					<div class="form-group form-md-line-input">
						<label for="form_control_1">Sales Consultant<span>*</span>:</label> 
						<select name="dr_enquiry_emp_id" id="dr_enquiry_emp_id"
								class="form-control">
								<option value="0">Select</option>
							</select>
					</div>
					
					
					<br>
					<div class="form-actions noborder">
						<center>
								<button type="button" class="btn1" name="addbutton" id="addbutton" >Transfer Enquiry</button>
<!-- 							<input name="addbutton" id ="addbutton" type="button" class="btn1"  value="Add Enquiry" onClick="return SubmitFormOnce(document.frmaddenquiry, this);" /> -->
								<input type="hidden"  name="add_button1" id ="add_button1" value=""></input>
								<input type="hidden" id="enquiry_id" name="enquiry_id" value="<%=request.getParameter("enquiry_id")%>" />
<%-- 								<input type="hidden" id="branchtype_id" value="<%=mybean.branchtype_id%>"></input> --%>

<!-- <button type="button" class="btn1" name="addbutton"  -->
<!-- 								id="addbutton" >Add Enquiry</button> -->
<!-- 								<input type="hidden"  name="add_button" id="add_button" value=""></input> -->
								
								
						</center>
						<br>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 	<script src="js/jquery.min.js" type="text/javascript"></script> -->
<!-- <script src="js/bootstrap.js" type="text/javascript"></script> -->
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<!-- 	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script> -->
<!-- <script src="../assets/js/components-select2.min.js" type="text/javascript"></script> -->
<!--  <script src="js/jquery-ui.js" type="text/javascript"></script>	 -->
<script src="js/jquery-ui.js" type="text/javascript"></script>	
<script src="js/jquery.app.js" type="text/javascript"></script>
<script src="js/jquery.min.js" type="text/javascript"></script>
<!-- 	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script> -->
<script src="js/select2.full.min.js" type="text/javascript"></script>
<script src="js/components-select2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Validate.js"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>
<script>

$(document).ready(function() {
	$("#addbutton").click(function() {
		checkForm();
	});
});

</script>

<script>
function PopulateBranch() {
	var brand_id = document.getElementById('dr_enquiry_brand_id').value;
	showHint('../sales/enquiry-check.jsp?brand_id='+brand_id,'dr_enquiry_branch_id');
}

function PopulateModel() {
	var brand_id = document.getElementById('dr_enquiry_brand_id').value;
	showHint('../sales/enquiry-check.jsp?model=yes&brand_id='+brand_id,'dr_enquiry_model_id');
}

function PopulateItem() {
	var model_id = document.getElementById('dr_enquiry_model_id').value;
	showHint('../sales/enquiry-check.jsp?model_id='+model_id,'dr_enquiry_item_id');
}

function PopulateTeam(){
	var branch_id = document.getElementById('dr_enquiry_branch_id').value;
	showHint('../sales/enquiry-check.jsp?team=yes&branch_id='+branch_id,'dr_enquiry_team_id');	
}

function PopulateExecutive() {
	var team_id = document.getElementById('dr_enquiry_team_id').value;
	showHint('../sales/enquiry-check.jsp?exe_transfer=yes&team_id='+team_id,'dr_enquiry_emp_id');
}

</script>

	<script>
	var msg = "";
	function checkForm() {
		msg = "";
		var brand_id = document.getElementById("dr_enquiry_brand_id").value;
		if (brand_id == '0') {
			msg += '<br>Select Brand!';
		}
		var branch_id = document.getElementById("dr_enquiry_branch_id").value;
		if (branch_id == '0') {
			msg += '<br>Select Branch!';
		}
		var model_id = document.getElementById("dr_enquiry_model_id").value;
		if (model_id == '0') {
			msg += '<br>Select Model!';
		}
		var variant_id = document.getElementById("dr_enquiry_item_id").value;
		if (variant_id == '0') {
			msg += '<br>Select Variant!';
		}
		var team_id = document.getElementById("dr_enquiry_team_id").value;
		if (team_id == '0') {
			msg += '<br>Select Team!';
		}
		var team_id = document.getElementById("dr_enquiry_emp_id").value;
		if (team_id == '0') {
			msg += '<br>Select Sales Consultant!';
		}
		if (msg != '') {
			showToast(msg);
		} 
		else {
			document.getElementById('add_button1').value = "yes";
			document.getElementById('frmtransferenquiry').submit();
		}
	}
    </script>
    
  
</body>
<!-- END BODY -->
</html>