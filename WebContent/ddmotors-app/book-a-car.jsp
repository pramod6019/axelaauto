<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.ddmotors_app.Book_A_Car"
	scope="request" />
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
<link href="../ddmotors-assets/css/components-rounded.css"
	id="style_components" rel="stylesheet" type="text/css">
<link href="../ddmotors-assets/css/default.css" rel="stylesheet"
	type="text/css" id="style_color">
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<script type="text/javascript" src="../Library/Validate.js"></script>
<script src="../ddmotors-assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/bootstrap.min.js"
	type="text/javascript"></script>
<script src="../ddmotors-assets/js/metronic.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/metronic.js" type="text/javascript"></script>
<script src="../Library/dynacheck.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/mobilecall.js" type="text/javascript"></script>
<script>
	function populateVariant(item_id) {
		document.getElementById('txt_enquiry_item').value = 'Select Variant';
		showHint('../ddmotors-app/model-check.jsp?enquiry=yes&model_id='
				+ item_id, 'txt_enquiry_item_hint');
	}
<%if(mybean.ostype.equals("android")){%>
	$(document).ready(function() {
		$("#addbutton").click(function() {
			checkForm();
		});
	});
	<%}%>

	function checkForm() {
		var msg = "";
		var title_id = document.getElementById("txt_enquiry_title_id").value;
		if (title_id == '0') {
			msg += '<br>Select Your Title!';
		}
		var fname = document.getElementById("txt_enquiry_fname").value;
		if (fname == '') {
			msg += '<br>Enter Your Name!';
		}
		// 		var lname = document.getElementById("txt_enquiry_lname").value;
		// 		if (lname == '') {
		// 			msg += '<br>Enter Your Last Name!';
		// 		}
		var mobile = document.getElementById("txt_enquiry_mobile").value;
		if (mobile == '') {
			msg += '<br>Enter Valid Contact Mobile!';
		}
		var email = document.getElementById("txt_enquiry_email").value;
		if (email == '') {
			msg += '<br>Enter Your Email!';
		}
		var model_id = document.getElementById("txt_enquiry_model_id").value;
		if (model_id == '0') {
			msg += '<br>Select Model!';
		}
		// 		var variant_id = document.getElementById("txt_enquiry_item_id").value;
		// 		if (variant_id == '0') {
		// 			msg += '<br>Select Variant!';
		// 		}

		if (msg != '') {
			<!--alert(msg);
			-->
			showToast(msg);
		} else {
			document.getElementById('frmaddenquiry').submit();
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

<body <%if (!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%}%>>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">
				<% if (mybean.type.equals("testdrive")) { %>
				<center>Book A Test Drive</center>
				<% } else if(!mybean.modelname.equals("")) { %>				
				<center>Book <%=mybean.modelname%></center>
				<% } else{ %>
				<center>Book A Car</center>
				<%} %>
			</h3>
		</div>
	</div>
	<div class="container">

		<form role="form" id="frmaddenquiry" name="frmaddenquiry"
			method="post">

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
				<!-- 					<select class="form-control" name="dr_enquiry_title_id" -->
				<!-- 						id="dr_enquiry_title_id"> -->
				<%-- 						<%=mybean.PopulateTitle(mybean.enquiry_title_id)%> --%>
				<!-- 					</select> <label for="form_control_1">Contact Title *</label> -->
				<!-- 				</div> -->
				<%
					if (mybean.ostype.equals("android")) {
				%>
				<div class="form-group form-md-line-input has-info">
					<input type="text" class="form-control" name="txt_enquiry_title"
						value="<%=mybean.titledesc%>" id="txt_enquiry_title"
						placeholder="Select Title"
						onClick="showDialog('<%=mybean.PopulateJSONTitle(mybean.enquiry_title_id)%>','populatetitle', 'title_id','title_desc','Select Title','txt_enquiry_title_id','');">
					<label for="form_control_1">Your Title *</label> <input type="text"
						class="form-control hidden" name="txt_enquiry_title_id"
						id="txt_enquiry_title_id" value="<%=mybean.enquiry_title_id%>">

				</div>
				<%
					} else if(mybean.ostype.equals("ios")) {
				%>
				<div class="form-group form-md-line-input has-info">
					<select class="form-control" name="txt_enquiry_title_id"
						id="txt_enquiry_title_id">
						<%=mybean.PopulateTitle(mybean.enquiry_title_id)%>
					</select> <label for="form_control_1">Your Title *</label>
				</div>
				<%
					}
				%>

				<div class="form-group form-md-line-input has-info">
					<input type="text" class="form-control" id="txt_enquiry_fname"
						name="txt_enquiry_fname" value="<%=mybean.enquiry_fname%>">
					<label for="form_control_1" name="txt_enquiry_fname"
						id="txt_enquiry_fname">Name * </label>
				</div>
				<div class="hidden">
					<!-- 					class="form-group form-md-line-input form-md-floating-label has-info"> -->
					<input type="text" class="form-control" id="txt_enquiry_lname"
						name="txt_enquiry_lname" value="<%=mybean.enquiry_lname%>">
					<label for="form_control_1" name="txt_enquiry_lname"
						id="txt_enquiry_lname">Last Name * </label>
				</div>
				<div class="form-group form-md-line-input has-info">
					<input type="text" class="form-control" id="txt_enquiry_mobile"
						name="txt_enquiry_mobile" maxlength="13"
						value="<%=mybean.enquiry_mobile%>"
						onkeyup="toPhone('txt_enquiry_mobile','Mobile');"> <label
						for="form_control_1" name="txt_enquiry_mobile"
						id="txt_enquiry_mobile" maxlength="13">Mobile * </label>
				</div>
				<div class="form-group form-md-line-input has-info">
					<input type="text" class="form-control" id="txt_enquiry_email"
						name="txt_enquiry_email" value="<%=mybean.enquiry_email%>">
					<label for="form_control_1">Email * </label>
				</div>


				<!-- 				<div -->
				<!-- 					class="form-group form-md-line-input has-info"> -->
				<!-- 					<select class="form-control" name="dr_enquiry_model_id" -->
				<!-- 						id="dr_enquiry_model_id" onchange="populateitem();"> -->
				<%-- 						<%=mybean.PopulateModel(mybean.enquiry_model_id)%> --%>
				<!-- 					</select> <label for="form_control_1">Model *</label> -->
				<!-- 				</div>	 -->
				<% if (mybean.ostype.equals("android")) { %>
				<% if (mybean.type.equals("testdrive") || mybean.type.equals("bookacar")) { %>
				<div class="form-group form-md-line-input has-info">
					<% } else { %> <div class="hidden"> <% } %>

<!-- 						<input type="text" class="form-control" name="txt_enquiry_model" -->
<!-- 							id="txt_enquiry_model" style="color: #000" -->
<%-- 							<%if (!mybean.enquiry_model_id.equals("0")) {%> --%>
<%-- 							placeholder="<%=mybean.modelname%>" <%} else {%> --%>
<%-- 							placeholder="Select Model" <%}%> --%>
<%-- 							onClick="showDialog('<%=mybean.PopulateJSONModel()%>','populatemodel','model_id','model_name','Select Model','txt_enquiry_model_id','populateVariant(itemid)');"> --%>
<select class="form-control" name="txt_enquiry_model_id"
							id="txt_enquiry_model_id" <%if (mybean.enquiry_model_id.equals("0")) {%> value="0"
							<%} else {%> value="<%=mybean.enquiry_model_id%>" <%}%>>
							<%=mybean.PopulateModel(mybean.enquiry_model_id)%>
						</select>
						<label for="form_control_1">Model * </label> 
<!-- 						<input type="text" -->
<!-- 							class="form-control hidden" name="txt_enquiry_model_id" -->
<!-- 							id="txt_enquiry_model_id" -->
<%-- 							<%if (mybean.enquiry_model_id.equals("0")) {%> value="0" --%>
<%-- 							<%} else {%> value="<%=mybean.enquiry_model_id%>" <%}%>> --%>
					</div>
					<% } else if(mybean.ostype.equals("ios")) { %>
					<% if (mybean.type.equals("testdrive") || mybean.type.equals("bookacar")) { %>
				<div class="form-group form-md-line-input has-info">
					<% } else { %> <div class="hidden"> <% } %>
<!-- 					<div class="form-group form-md-line-input has-info"> -->
						<select class="form-control" name="txt_enquiry_model_id"
							id="txt_enquiry_model_id" <%if (mybean.enquiry_model_id.equals("0")) {%> value="0"
							<%} else {%> value="<%=mybean.enquiry_model_id%>" <%}%>>
							<%=mybean.PopulateModel(mybean.enquiry_model_id)%>
						</select> <label for="form_control_1">Model *</label>
					</div>
					<% } %>

					<!-- 				<div class="form-group form-md-line-input has-info"> -->
					<!-- 					<label for="form_control_1" style="color: #888888">Variant *</label> -->
					<!-- 					<span id="modelitem"> -->
					<%-- 						<%=mybean.PopulateItem(mybean.enquiry_model_id, mybean.comp_id) %> --%>
					<!-- 					</span> -->
					<!-- 				</div>				 -->

					<%-- 				<%if(mybean.type.equals("testdrive")) {%> --%>
					<!-- 					<div class="form-group form-md-line-input has-info"> -->
					<%-- 					<%}else{ %> --%>
					<!-- 					<div class="hidden"> -->
					<%-- 					<%} %> --%>
					<div class="hidden">
						<!-- 				class="form-group form-md-line-input has-info"> -->
						<input type="text" class="form-control" name="txt_enquiry_item"
							id="txt_enquiry_item"
							<%if (!mybean.enquiry_item_id.equals("0")) {%>
							placeholder="<%=mybean.itemname%>" <%} else {%>
							placeholder="Select Variant" <%}%>
							onClick="showDialog(document.getElementById('txt_enquiry_item_hint').innerHTML,'populateitem','item_id','item_name','Select Variant','txt_enquiry_item_id','');">

						<label for="form_control_1">Variant * </label> <input type="text"
							class="form-control hidden" name="txt_enquiry_item_id"
							id="txt_enquiry_item_id"
							<%if (mybean.enquiry_item_id.equals("0")) {%> value="0"
							<%} else {%> value="<%=mybean.enquiry_item_id%>" <%}%>>
						<%-- 					value="<%=mybean.enquiry_item_id%>"> --%>
						<span class="hidden" name="txt_enquiry_item_hint"
							id="txt_enquiry_item_hint"><%=mybean.PopulateItem(mybean.enquiry_model_id,
					mybean.comp_id)%></span>
					</div>

					<!-- 				<div -->
					<!-- 					class="form-group form-md-line-input form-md-floating-label has-info"> -->
					<!-- 					<input type="date" class="form-control" id="txt_enquiry_date" name="txt_enquiry_date" > -->
					<!-- 					<label for="form_control_1" name="txt_enquiry_date" -->
					<%-- 						id="txt_enquiry_date"  >Demo Date * <%=mybean.enquiry_date%></label> --%>
					<!-- 				</div> -->
					<!-- 				<div -->
					<!-- 					class="form-group form-md-line-input form-md-floating-label has-info"> -->
					<!-- 					<input type="time" class="form-control" id="txt_enquiry_time" name="txt_enquiry_time" > -->
					<!-- 					<label for="form_control_1" name="txt_enquiry_time" -->
					<%-- 						id="txt_enquiry_time" >Demo Time * <%=mybean.enquiry_time%></label> --%>
					<!-- 				</div> -->


					<!-- 				<div class="form-group form-md-line-input form-md-floating-label"> -->
					<!-- 					<input type="text" class="form-control" id="txt_enquiry_comments" name="txt_enquiry_comments" > -->
					<!-- 					<label for="form_control_1" name="txt_enquiry_comments" -->
					<%-- 						id="txt_enquiry_comments" >Comments <%=mybean.enquiry_comments%></label> --%>
					<!-- 				</div> -->

					<div class="form-actions noborder">
								<center><button type="button" class="btn" name="addbutton" 
									id="addbutton"
<%-- 									<%if(mybean.ostype.equals("ios")){%>  --%>
									onClick="return SubmitFormOnce(document.frmaddenquiry, this);"
<%-- 									<%} %> --%>
									>Submit</button></center>
								<input type="hidden" name="add_button" value="yes"><br>
								<br>
								<!-- 						 onClick="return SubmitFormOnce(document.frmaddenquiry, this);" -->
							</div>
				</div>
				
				</form>
	</div>

	<script language="JavaScript" type="text/javascript">
		// 	function populateitem(){
		// 		var model_id = document.getElementById('dr_enquiry_model_id').value;
		// 		  showHint('../ddmotors-app/model-check.jsp?model=yes&model_id='+model_id,'modelitem');
		// 	}
		///

		jQuery(document).ready(function() {
			Metronic.init(); // init metronic core components
		});
	</script>


</body>
<!-- END BODY -->
</html>
