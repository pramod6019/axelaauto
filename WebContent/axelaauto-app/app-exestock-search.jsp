<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Exestock_Search"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en">
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">
<link href="css/font-awesome.css" rel="stylesheet"	type="text/css" />

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>	
<script type="text/javascript" src="js/Validate.js"></script>
	
	
<script language="JavaScript" type="text/javascript"> 
        
 function PopulateBranch(brand_id){
	 showHint("app-exestock-check.jsp?brand_id="+brand_id+"&region_id="+brand_id+"&branch=yes","hintbranch");
     showHint("app-exestock-check.jsp?brand_id="+brand_id+"&model=yes","hintmodel");
   }
 function PopulateModel(branch_id){
   showHint("app-exestock-check.jsp?branch_id="+branch_id+"&model=yes","hintmodel");
   }

 function PopulateItem(model_id){
	showHint("app-exestock-check.jsp?model_id="+model_id+"&item=yes","hintitem");
	}   
 
 function PopulateRegion(brand_id){ //v1.0
	 showHint("app-exestock-check.jsp?brand_id="+brand_id+"&region=yes","hintregion");
// 	   showHint("app-exestock-check.jsp?brand_id="+brand_id+"&region=yes","hintregion");
	} 	
	
 function PopulateColour(item_id){
	showHint("app-exestock-check.jsp?item_id="+item_id+"&colour=yes","div_colour");
	}

 function PopulateTeam(branch_id){
	showHint("app-exestock-check.jsp?branch_id="+branch_id+"&team=yes","hintteam");  
	}
        </script>
	
	<script>
	$(document).ready(function() {
		$("#addbutton").click(function() {
			document.getElementById('go_button').value = "Go";
			document.getElementById('frmaddenquiry').submit();
		});
		toggleBranch();
		toggleVariant();
		$("#branch-up").hide();
		$("#model-up").hide();
		$("#others").hide();
		$("#others-up").hide();
		
		$("#branch-up").click(function() {
			$("#branch-up").hide();
			$("#branch-down").show();
		});
		$("#branch-down").click(function() {
			$("#branch-down").hide();
			$("#branch-up").show();
		});
		
		$("#model-up").click(function() {
			$("#model-up").hide();
			$("#model-down").show();
		});
		$("#model-down").click(function() {
			$("#model-down").hide();
			$("#model-up").show();
		});
		
		$("#others-up").click(function() {
			$("#others-up").hide();
			$("#others-down").show();
		});
		$("#others-down").click(function() {
			$("#others-down").hide();
			$("#others-up").show();
		});
	});
	
	function toggleBranch()
	{
		$(".branch").toggle();
	}
	function toggleVariant()
	{
		$(".variant").toggle();
	}
	function toggleOthers()
	{
		$("#others").toggle();
	}
	function hideVariant(){
		$(".variant").hide();
		$("#model-up").hide();
		$("#model-down").show();
	}
	
    </script>
    
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

<body <%if(!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%} %>>
	<!-- onLoad="DisplayPreOwned();DisplayModel();" -->
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center> Stock Status</center></strong></span>
		</div>
	</div>
	<div class="container">
		<div class="col-md-12">
			<form role="form" id="frmaddenquiry" name="frmaddenquiry"
				class="form-horizontal" method="get" action="callurlapp-exestock-list.jsp">
				<div class="form-body">   
 				<% if(mybean.branchcount>1) { %>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Brand:</label>
						<label id="branch-down" class="fa fa-chevron-circle-down" hide style="font-size: 15px; cursor: pointer" onclick="toggleBranch()"></label>
						<label id="branch-up" class="fa fa-chevron-circle-up" style="font-size: 15px; cursor: pointer" onclick="toggleBranch()"></label>
						<select class="form-control name="drop_brand_id" 
							    id="drop_brand_id" onchange= "PopulateBranch(this.value); PopulateModel(this.value);PopulateRegion(this.value);">
							    <%=mybean.PopulatePrincipal(mybean.comp_id)%>
						</select>
					</div>
<!-- 					<div id="model" class="form-group form-md-line-input"> -->
<!-- 						<label for="form_control_1">Region:</label><span id="hintregion"> -->
<!-- 						<select class="form-control" name="dr_region" -->   
<!-- 							    id="dr_region" onchange= "PopulateBranch(this.value)""; > -->
<%-- 							   <%=mybean.PopulateRegion(mybean.brand_id, mybean.comp_id)%> --%>
<!-- 						</select></span> -->
<!-- 					</div> -->
					

					<div id="model" class="form-group form-md-line-input branch">
						<label for="form_control_1">Branch:</label><span id="hintbranch">
						<select class="form-control" name="dr_branch"
							    id="dr_branch" onchange="PopulateTeam(this.value); PopulateModel(this.value)";>
							   <%=mybean.PopulateBranch(mybean.brand_id, mybean.comp_id)%>
						</select></span>
					</div>
					<% }else{%>
					<div id="model" class="form-group form-md-line-input">
					<input type='hidden' name="drop_brand_id" id="drop_brand_id" value="<%=mybean.brand_id%>">
						<input type='hidden' name="dr_branch" id="dr_branch" value="<%=mybean.branch_id %>">
					</div>
					<% }%>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Model:</label>
						<label id="model-down" class="fa fa-chevron-circle-down" style="font-size: 15px; cursor: pointer" onclick="toggleVariant()" ></label>
						<label  id="model-up" class="fa fa-chevron-circle-up" style="font-size: 15px; cursor: pointer" onclick="toggleVariant()"></label>
						<span id="hintmodel"><select class="form-control" name="dr_model_id" 
							    id="dr_model_id" onchange="PopulateItem(this.value); hideVariant();" >
							     <option value="0">Select</option>
							    <%=mybean.PopulateModel(mybean.brand_id, mybean.vehstock_branch_id, mybean.comp_id)%> 
						</select>
						</span>
					</div>
					
					<div id="model" class="form-group form-md-line-input variant">
						<label for="form_control_1">Variant:</label><span id="hintitem">
						<select class="form-control" name="dr_item_id"
							    id="dr_item_id">
							     <!-- <option value="0">Select</option> -->
							   <%=mybean.PopulateItem(mybean.model_id, mybean.comp_id)%>
						</select></span>
					</div>
					<div id="model" class="form-group form-md-line-input variant">
						<label for="form_control_1">Colour:</label>
						<select class="form-control" name="dr_option_id"
							    id="dr_option_id">
							   <%=mybean.PopulateColour(mybean.comp_id)%>
						</select>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Others: </label>
						<label id="others-down" class="fa fa-chevron-circle-down" style="font-size: 15px; cursor: pointer" onclick="toggleOthers();"></label>
						<label  id="others-up" class="fa fa-chevron-circle-up" style="font-size: 15px; cursor: pointer" onclick="toggleOthers();"></label>
					</div>
					
					<div id="others">
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Delivery Status:</label>
						<select class="form-control" name="dr_delstatus_id"
							    id="dr_delstatus_id" >
							     <%=mybean.PopulateStatus(mybean.comp_id)%>
						</select>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Pending Delivery:</label>
						<select class="form-control" name="dr_pending_delivery_id"
							    id="dr_pending_delivery_id" onchange="populateItem()">
							    <%=mybean.PopulatePendingdelivery(mybean.comp_id)%>
						</select>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Team:</label><span id="hintteam">
						<select class="form-control" name="dr_team_id"
							    id="dr_team_id">
							 <option value="0">Select</option>
							    <%=mybean.PopulateTeam(mybean.branch_id, mybean.comp_id)%>
						</select></span>
					</div>
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Fuel:</label>
						<select class="form-control" name="dr_fuel_id"
							    id="dr_fuel_id">
							   <%=mybean.PopulateFuelType(mybean.comp_id)%>
						</select>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Blocked:</label>
						<select class="form-control" name="dr_blocked"
							    id="dr_blocked">
							    <%=mybean.PopulateBlocked(mybean.comp_id)%>
						</select>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Location:</label>
						<select class="form-control" name="dr_loc_id"
							    id="dr_loc_id">
							   <%=mybean.PopulateLocation(mybean.comp_id)%>
						</select>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Order By:</label>
						<select class="form-control" name="dr_order_by"
							    id="dr_order_by">
							   <%=mybean.PopulateOrderBy(mybean.comp_id)%>
						</select>
					</div>
					
					<div id="model" class="form-group form-md-line-input">
						<label for="form_control_1">Stock Status::</label>
						<select id="dr_vehstock_status_id" name="dr_vehstock_status_id" class="form-control">
														<%=mybean.PopulateStockStatus(mybean.comp_id)%>
													</select>
					</div>
					</div>
					<br>
					<div class="form-actions noborder">
						<center>
							<button type="button" class="btn1" name="addbutton" 
								id="addbutton" >Go</button>
								<input type="hidden"  name="go_button" id="go_button" value=""></input>
						</center>
						<br>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
<!-- END BODY -->
</html>