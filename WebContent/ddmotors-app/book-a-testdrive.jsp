<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.ddmotors_app.Book_A_TestDrive" scope="request" />
<% mybean.doPost(request, response); %>
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
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<script src="../ddmotors-assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/bootstrap.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/metronic.js" type="text/javascript"></script>
<script src="../Library/dynacheck.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/mobilecall.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript">
function populateVariant(item_id){
	document.getElementById('txt_testdrive_item').value = 'Select Variant';
	showHint('../ddmotors-app/model-check.jsp?model=yes&model_id='+item_id,'txt_testdrive_item_hint');
}
	
	
	$(document).ready(function() {
		$("#addbutton").click(function() {
			checkForm();
		});
	});
	var msg = "";
	function checkForm() {
		msg = "";
		var title_id = document.getElementById("txt_testdrive_title_id").value;
		if (title_id == '0') {
			msg += '<br>Select Contact Title!';
		}
		var fname = document.getElementById("txt_testdrive_fname").value;     
		//var fname = document.forms.frmtestdrive.txt_testdrive_fname;
		if (fname =='') {
			msg += '<br>Enter the Contact First Name!';
		}
		var lname = document.getElementById("txt_testdrive_lname").value;
		if (lname == '') {
			msg += '<br>Enter the Contact Last Name!';
		}
		var mobile = document.getElementById("txt_testdrive_mobile").value;
		if (mobile == '') {
			msg += '<br>Enter Valid Contact Mobile 1!';
		}
		var email = document.getElementById("txt_testdrive_email").value;
		if (email == '') {
			msg += '<br>Enter Contact Email!';
		}
		var model_id = document.getElementById("txt_testdrive_model_id").value;
		if (model_id == '0') {
			msg += '<br>Select Model!';
		}
		var variant_id = document.getElementById("txt_testdrive_item_id").value;
		if (variant_id == '0') {
			msg += '<br>Select Variant!';
		}
		
		var date = document.getElementById("txt_testdrive_date").value;
		if (date == '') {
			msg += '<br>Select Test Drive Date!';
		}
		var time = document.getElementById("txt_testdrive_time").value;
		if (time == '') {
			msg += '<br>Select Test Drive Time!';
		}

		var testdrive_comments = document .getElementById("txt_testdrive_comments").value;

		if (msg != '') {
			<!--alert(msg);-->
			showToast(msg);
		} else {
			document.getElementById('frmtestdrive').submit();
		}
	}
</script>
<style>
h4 {
	color: #0f4c75;
}
center {
	font-size: 20px;
}
</style>
</head>

<body>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">
				<center>Book A Test Drive</center>
			</h3>
		</div>
	</div>
	<div class="container">
		
		<form role="form" id="frmtestdrive" name="frmtestdrive" method="post">
		
			<div class="form-body">

				<div class="alert alert-danger display-hide">
					<button class="close" data-close="alert"></button>
					You have some form errors. Please check below.
				</div>
				<div class="alert alert-success display-hide">
					<button class="close" data-close="alert"></button>
					Your form validation is successful!
				</div>
				
<!-- 				<div -->
<!-- 					class="form-group form-md-line-input has-info"> -->
<!-- 					<select class="form-control" name="dr_testdrive_title_id" -->
<%-- 						id="dr_testdrive_title_id" onClick="showDialog('<%=mybean.PopulateTitle(mybean.testdrive_title_id)%>','populatetitle', 'title_id','title_desc','Title','dr_testdrive_title_id','');"> --%>
<%-- <%-- 						<%=mybean.PopulateTitle(mybean.testdrive_title_id)%> --%> 
<!-- 					</select> <label for="form_control_1">Contact Title *</label> -->
					
<!-- 				</div> -->
				<%if(!mybean.ostype.equals("ios")) {%>
				<div
					class="form-group form-md-line-input has-info">
					<input type="text" class="form-control" name="txt_testdrive_title"
						id="txt_testdrive_title" placeholder="Select Title" onClick="showDialog('<%=mybean.PopulateJSONTitle(mybean.testdrive_title_id)%>','populatetitle', 'title_id','title_desc','Select Title','txt_testdrive_title_id','');">
					 <label for="form_control_1">Contact Title *</label>
					
					<input type="text" class="form-control hidden" name="txt_testdrive_title_id" id="txt_testdrive_title_id" value="0">
										
				</div>
				<%}else{ %>
				
				<div
					class="form-group form-md-line-input has-info">
					<select class="form-control" name="txt_testdrive_title_id"
						id="txt_testdrive_title_id">
						<%=mybean.PopulateTitle(mybean.testdrive_title_id)%>
					</select> <label for="form_control_1">Contact Title *</label>
				</div>
				<%} %>
				
				<div
					class="form-group form-md-line-input form-md-floating-label has-info">
					<input type="text" class="form-control" id="txt_testdrive_fname" name="txt_testdrive_fname">
					<label for="form_control_1" name="txt_testdrive_fname"
						id="txt_testdrive_fname" value="<%=mybean.testdrive_fname%>" >First Name * </label>
				</div>
				
				<div
					class="form-group form-md-line-input form-md-floating-label has-info">
					<input type="text" class="form-control" id="txt_testdrive_lname" name="txt_testdrive_lname">
					<label for="form_control_1" name="txt_testdrive_lname"
						id="txt_testdrive_lname">Last Name * <%=mybean.testdrive_lname%></label>
				</div>
				<div
					class="form-group form-md-line-input form-md-floating-label has-info">
					<input type="text" class="form-control" id="txt_testdrive_mobile" name="txt_testdrive_mobile" maxlength="10" onkeyup="toPhone('txt_testdrive_mobile','Mobile');">
					<label for="form_control_1" name="txt_testdrive_mobile"
						id="txt_testdrive_mobile" maxlength="10">Mobile * <%=mybean.testdrive_mobile %></label>
				</div>
				<div
					class="form-group form-md-line-input form-md-floating-label has-info">
					<input type="text" class="form-control" id="txt_testdrive_email" name="txt_testdrive_email">
					<label for="form_control_1" name="txt_testdrive_email"
						id="txt_testdrive_email">Email * <%=mybean.testdrive_email%></label>
				</div>
				
<!-- 				<div -->
<!-- 					class="form-group form-md-line-input has-info"> -->
<!-- 					<select type="dropdown" class="form-control" name="dr_testdrive_model_id" -->
<!-- 						id="dr_testdrive_model_id" onchange="populateitem();"> -->
<%-- 						<%=mybean.PopulateModel(mybean.testdrive_model_id)%> --%>
<!-- 					</select> <label for="form_control_1">Model *</label> -->
<!-- 				</div>	 -->
				
				<div
					class="form-group form-md-line-input has-info">
					<input type="text" class="form-control" name="txt_testdrive_model"
						id="txt_testdrive_model" placeholder="Select Model" onClick="showDialog('<%=mybean.PopulateModel()%>','populatemodel','model_id','model_name','Select Model','txt_testdrive_model_id','populateVariant(itemid)');" >         
					
					<label for="form_control_1">Model *</label>
					
					<input type="text" class="form-control hidden" name="txt_testdrive_model_id" id="txt_testdrive_model_id" value="0">
				</div>
				
				
				
				
<!-- 				////span tag should add -->
<!-- 				<div class="form-group form-md-line-input has-info"> -->
<!-- 					<input type="text" class="form-control" name="txt_testdrive_item" -->
<%-- 						id="txt_testdrive_item" placeholder="Select Variant" onClick="showDialog('<%=mybean.PopulateItem(mybean.testdrive_model_id, mybean.comp_id)%>','populatevariant','item_id','item_name','Select Variant','dr_testdrive_item_id','');" >          --%>
					
<!-- 					<label for="form_control_1">Variant *</label> -->
					
<!-- 					<input type="text" class="form-control hidden" name="txt_testdrive_item_id" id="txt_testdrive_item_id" value="0"> -->
<!-- 				</div> -->
				
				<div class="form-group form-md-line-input has-info">
					<input type="text" class="form-control" name="txt_testdrive_item"
						id="txt_testdrive_item" placeholder="Select Variant" onClick="showDialog(document.getElementById('txt_testdrive_item_hint').innerHTML,'populateitem','item_id','item_name','Select Variant','txt_testdrive_item_id','');" >         
					
					<label for="form_control_1">Variant *</label>
					
					<input type="text" class="form-control hidden" name="txt_testdrive_item_id" id="txt_testdrive_item_id" value="0">
					<span class="hidden" name="txt_testdrive_item_hint" id="txt_testdrive_item_hint"><%=mybean.PopulateItem(mybean.testdrive_model_id, mybean.comp_id)%></span>
				</div>
								
<!-- 				<div class="form-group form-md-line-input has-info"> -->
<!-- 				<label for="form_control_1"  style="color: #888888">Item *</label> -->
<!-- 					<span id="modelitem"> -->
<%-- 						<%=mybean.PopulateItem(mybean.testdrive_model_id, mybean.comp_id) %> --%>
						
<!-- 					</span> -->
<!-- 				</div> -->
				<div
					class="form-group form-md-line-input form-md-floating-label has-info">
					<input type="date" class="form-control" id="txt_testdrive_date" name="txt_testdrive_date">
					<label for="form_control_1" name="txt_testdrive_date"
						id="txt_testdrive_date">Date * <%=mybean.testdrive_date%></label>
				</div>
				<div
					class="form-group form-md-line-input form-md-floating-label has-info">
					<input type="time" class="form-control" id="txt_testdrive_time" name="txt_testdrive_time">
					<label for="form_control_1" name="txt_testdrive_time"
						id="txt_testdrive_time">Time * <%=mybean.testdrive_time%></label>
				</div>
				<div class="form-group form-md-line-input form-md-floating-label">
					<input type="text" class="form-control" id="txt_testdrive_comments" name="txt_testdrive_comments" >
					<label for="form_control_1" name="txt_testdrive_comments"
						id="txt_testdrive_comments">Comments <%=mybean.testdrive_comments%></label>
				</div>
				
			<div class="form-actions noborder">
				<center>
						<button type="button" class="btn" name="addbutton" id="addbutton">Submit</button>
					</center>
						 <input type="hidden" name="add_button" value="yes"><br><br>
					</div>
				
			
	</div>
	</form>
	</div>

	<script language="JavaScript" type="text/javascript">
	jQuery(document).ready(function() {   
	   // initiate layout and plugins
	   Metronic.init(); // init metronic core components
// 	Layout.init(); // init current layout
// 	Demo.init(); // init demo features
	});
	</script>


</body>
<!-- END BODY -->
</html>
