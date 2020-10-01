<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Ticket_Add"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Add Ticket | Axelaauto-App</title>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">

<style>
b {
	color: #8E44AD;
}

span {
	color: red;
}

.container {
	padding-right: 15px;
	padding-left: 15px;
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
</style>

</head>

<body <%if(!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%} %>>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Add
						Ticket</center></strong></span>
		</div>
	</div>
	<!-- 	<div class="container"> -->
	<div class="col-md-6" style="margin-top: 40px; margin-left: 4px;">
		<form role="form" name="formticket" id="formticket"
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
					<label for="form_control_1">Branch<span>*</span>:
					</label> <select id="dr_ticket_branch_id" name="dr_ticket_branch_id"
						class="form-control">
						<%=mybean.PopulateBranch(mybean.ticket_branch_id, mybean.param, mybean.branch_type, request)%>
					</select>
				</div>


				<div class="form-group form-md-line-input">
					<label for="form_control_1">Subject<font color="#ff0000">*</font>:
					</label> <input name="txt_ticket_subject" type="text" class="form-control"
						id="txt_ticket_subject" value="<%=mybean.ticket_subject%>"
						size="52" maxlength="255" /> <input name="ticket_parent_id"
						type="hidden" id="ticket_parent_id"
						value="<%=mybean.ticket_parent_id%>" />
				</div>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Description<font
						color="#ff0000">*</font>:
					</label>
					<textarea name="txt_ticket_desc" cols="30" rows="3"
						class="form-control" id="txt_ticket_desc"
						onKeyUp="charcount('txt_ticket_desc', 'span_txt_ticket_desc',' <font color=red>({CHAR} characters left)</font>', '8000')"> <%=mybean.ticket_desc%></textarea>

					<span id="span_txt_ticket_desc">(8000 Characters) </span>
				</div>

				<%
												if (mybean.config_service_ticket_type.equals("1")) {
											%>
				<div class="form-group form-md-line-input">
					<label for="form_control_1">Type: </label> <select
						name="dr_ticket_tickettype_id" class="form-control">
						<%=mybean.PopulateTicketType()%>
					</select>
				</div>
				<%
												}
											%>


				<div class="form-group form-md-line-input">
					<label for="form_control_1">Source<font color="red">*</font>:
					</label> <select name="dr_ticket_ticketsource_id"
						id="dr_ticket_ticketsource_id" class="form-control">
						<%=mybean.PopulateSourceType()%>
					</select>
				</div>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Priority<font color="red">*</font>:
					</label> <select name="dr_ticket_priorityticket_id" class="form-control"
						id="dr_ticket_priorityticket_id">
						<%=mybean.PopulateTicketPriority()%>
					</select>
				</div>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Department<font color="red">*</font>:
					</label> <select name="dr_ticket_dept_id" class="form-control"
						onchange="populateCatogery(this);" id="dr_ticket_dept_id">
						<%=mybean.PopulateDepartment()%>
					</select>
				</div>


				<%
												if (mybean.config_service_ticket_cat.equals("1")) {
											%>
				<div class="form-group form-md-line-input">
					<label for="form_control_1">Category: </label> <span
						id="categoryHint"> <%=mybean.PopulateTicketCategory(mybean.ticket_ticket_dept_id, mybean.comp_id)%>
					</span>
				</div>
				<%
												}
											%>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Executive<font color="red">*</font>:
					</label> <select name="dr_ticket_emp_id" class="form-control"
						id="dr_ticket_emp_id">
						<%=mybean.PopulateAllExecutive()%>
					</select>
				</div>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Email CC: </label> <input
						name="txt_ticket_cc" type="text" class="form-control"
						id="txt_ticket_cc" value="<%=mybean.ticket_cc%>" size="52"
						maxlength="1000" />
				</div>
				<br>
				<center>
					<button type="button" class="btn1" name="addbutton" id="addbutton">Add
						Ticket</button>
					<input type="hidden" name="add_button" id="add_button" value="yes"><br>
					<br>
				</center>

			</div>
		</form>

		<script src="js/jquery.min.js" type="text/javascript"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="js/jquery-ui.js"></script>
		<script src="js/jquery.min.js" type="text/javascript"></script>
		<script src="js/axelamobilecall.js" type="text/javascript"></script>
		

		<script>
			$(document).ready(function() {
				$("#addbutton").click(function() {
					checkForm();
				});
			});
		</script>

		<script>
			var msg = "";
			function checkForm() {
				msg = "";
						var ticket_branch_id = document.getElementById("dr_ticket_branch_id").value;
						if (ticket_branch_id == '0') {
							msg += '<br>Select Branch!';
						}
						
						var ticket_subject = document.getElementById("txt_ticket_subject").value;
						if (ticket_subject == '') {
							msg += '<br>Select Subject!';
						}else if(ticket_subject.length < 5){
							msg += '<br>Subject should have atleast five characters!';
						}
						
						var ticket_desc = document.getElementById("txt_ticket_desc").value;
						if (ticket_desc == '') {
							msg += '<br>Select Description!';
						}else if(ticket_desc.length < 5){
							msg += '<br>Description should have atleast five characters!';
						}
						var ticket_ticketsource_id = document.getElementById("dr_ticket_ticketsource_id").value;
						if (ticket_ticketsource_id == '0') {
							msg += '<br>Select Source!';
						}
						
						var ticket_priorityticket_id = document.getElementById("dr_ticket_priorityticket_id").value;
						if (ticket_priorityticket_id == '0') {
							msg += '<br>Select Priority!';
						}
						
						var ticket_dept_id = document.getElementById("dr_ticket_dept_id").value;
						if (ticket_dept_id == '0') {
							msg += '<br>Select Department!';
						}
						
						var ticket_emp_id = document.getElementById("dr_ticket_emp_id").value;
						if (ticket_emp_id == '0') {
							msg += '<br>Select Executive!';
						}
						
				if (msg != '') {
					showToast(msg);
				} else {
					document.getElementById('add_button').value = "yes";
					document.getElementById('formticket').submit();
				}
			}
		</script>

		<script type="text/javascript">
	 function populateCatogery(){
		var  department_id= $('#dr_ticket_dept_id').val();
		  showHint('../service/report-check.jsp?ticket_category=ticket_add&ticket_dept_id='+department_id, 'categoryHint');
	} 
	
	
	</script>
</body>
<!-- END BODY -->
</html>

