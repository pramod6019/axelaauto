<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.axelaauto_app.App_Ticket_Dash_Followup" scope="request" />

<%
	mybean.doPost(request, response);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Ticket Dash | Axelaauto-App</title>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">

<style>
a {
	text-shadow: none;
	color: black;
	text-decoration: none;
}

b {
	color: #8E44AD;
}

strong {
	color: #fff;
}

center {
	color: #8E44AD;
	font-weight: bold;
}

span {
	color: red;
}

.select2-container {
	width: 100%;
}

.header-wrap {
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
}

.label {
	font-size: 14px;
	color: #8E44AD;
}
</style>

</head>


<body >
	<div class="header-wrap">
		<div class="panel-heading"
			style="margin-bottom: 20px; background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05); padding: 18px;">
			<span class="panel-title">
				<center>
					<strong>Ticket Dashboard</strong>
				</center>
			</span>
		</div>
	</div>
	<div class="panel-heading"
		style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
		<span class="panel-title">
			<center>
				<h4>
					<strong>Follow-up </strong>
				</h4>
			</center>
		</span>
	</div>


	<form role="form" class="form-horizontal" name="form1" id="form1"
		method="post">
		<div style="margin-top: 15px; margin-left: 20px;">
			<%-- 			<% if (!mybean.status.equals("")) { %> <% --%>

			<!-- // 			} -->
			<%if (mybean.msg1.equals("")) { %>
			<center>Follow-up List</center>
			<% } else { %>
			<center>
				<span><%=mybean.msg1%></span>
			</center>
			<%} %>

			<div style="margin-right: 20px;">
				<div><%=mybean.followupHTML%></div>



				<div>
					<center>&nbsp;</center>
				</div>

			</div>
		</div>



		<%if(!mybean.ticket_ticketstatus_id.equals("3")) {%>
		<div class="  panel-heading"
			style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
			<span class="caption panel-title" style="float: none">
				<center>
					<h4>
						<strong>Add Follow-up </strong>
					</h4>
				</center>
			</span>
		</div>


		<center>
			<span><%=mybean.msg%></span>
		</center>

		<div class="col-md-12" style="margin-top: 0px; margin-left: 4px;">

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Description<font color="#ff0000">*</font>:
				</label>
				<textarea name="txt_tickettrans_followup" cols="60" rows="6"
					class="form-control" id="txt_tickettrans_followup"
					onKeyUp="charcount('txt_tickettrans_followup', 'span_txt_tickettrans_followup','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.tickettrans_followup%></textarea>
				<span id="span_txt_tickettrans_followup"> 8000 characters </span>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Next Follow-up Date<font
					color="#ff0000">*</font>:
				</label> <input type="text" size="16"
					name="txt_tickettrans_nextfollowup_date"
					id="txt_tickettrans_nextfollowup_date" class="form-control"
					value="<%=mybean.tickettrans_nextfollowup_time%>"
					onclick="datePicker('txt_tickettrans_nextfollowup_date')" readonly></input>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1">Next Follow-up Time<font
					color="#ff0000">*</font>:
				</label> <input type="text" size="16"
					name="txt_tickettrans_nextfollowup_time"
					id="txt_tickettrans_nextfollowup_time" class="form-control"
					value="<%=mybean.tickettrans_nextfollowup_time%>"
					onclick="timePicker('txt_tickettrans_nextfollowup_time')" readonly></input>
			</div>

			<% if (mybean.add.equals("yes") || (mybean.tickettrans_contact_entry_id.equals("0"))) { %>
			<div class="form-group form-md-line-input">
				<label for="form_control_1">Private:&nbsp <input
					id="chk_private" type="checkbox" name="chk_private"
					<%=mybean.PopulateCheck(mybean.tickettrans_private)%> />
				</label>

			</div>
			<%} %>



			<br>

			<div class="form-actions noborder">
				<center>
					<button type="button" class="btn1" name="submitbutton"
						id="submitbutton">Add follow-up</button>
					<input type="hidden" name="add_button" id="add_button" value="yes">
				</center>
				<br>
			</div>

		</div>


		<%} %>
	</form>
	</div>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/jquery.app.js" type="text/javascript"></script>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>

	<script>
$(function() { 
$(".select2-container").css("width","100%");
});
</script>

	<script>

$(document).ready(function() {
	$("#submitbutton").click(function() {
		checkForm();
	});
});

</script>

	<script>
	var msg = "";
	function checkForm() {
		msg = "";
		var tickettrans_followup = document.getElementById("txt_tickettrans_followup").value;
		if (tickettrans_followup == '') {
			msg += '<br>Enter Description!';
		}
		
		var tickettrans_nextfollowup_date = document.getElementById("txt_tickettrans_nextfollowup_date").value;
		if (tickettrans_nextfollowup_date == '') {
			msg += '<br>Select Next Follow-up Date!';
		}
		
		var txt_tickettrans_nextfollowup_time = document.getElementById("txt_tickettrans_nextfollowup_time").value;
		if (txt_tickettrans_nextfollowup_time == '') {
			msg += '<br>Select Next Follow-up Time!';
		}
		
		
		if (msg != '') {
			showToast(msg);
			return false;
		} else {
			document.getElementById('add_button').value = "yes";
			document.getElementById('form1').submit();
		}
		
	}
		</script>

</body>
<!-- END BODY -->
</html>