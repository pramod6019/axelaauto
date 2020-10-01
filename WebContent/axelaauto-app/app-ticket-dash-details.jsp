<jsp:useBean id="mybean"
	class="axela.axelaauto_app.App_Ticket_Dash_Details" scope="request" />
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
<link href="css/select2-bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<link href="css/select2.min.css" rel="stylesheet" type="text/css" />

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
	/* 	width:100%; */
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

.select2-container {
	width: 300px;
}

.label {
	font-size: 14px;
	color: #8E44AD;
}
</style>

</head>

<body>
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

	<form role="form" class="form-horizontal" method="post" name="form1"
		id="form1">
		<div class="  panel-heading"
			style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;">
			<span class="panel-title">
				<center>
					<h4>
						<strong>Ticket Details </strong>
					</h4>
				</center>
			</span>
		</div>


		<div style="padding-left: 20px;">
			<%
				if (mybean.ticket_customer_id.equals("0")
						&& mybean.ticketParentId.equals("0")) {
			%>
			<div class="form-group form-md-line-input">
				<div class="col-xs-6">
					<center>
						<a class="btn1"
							href="callurlapp-ticket-add.jsp?add=yes&comp_id=<%=mybean.comp_id%>&ticket_parent_id=<%=mybean.ticket_id%>"">Add
							Child Ticket</a>
					</center>
				</div>
				<div class="col-xs-6">
					<center>
						<a class="btn1"
							href="callurlapp-ticket-list.jsp?add=yes&comp_id=<%=mybean.comp_id%>&previous_ticket_id=<%=mybean.ticket_id%>"">Previous
							Ticket&nbsp(s)</a>
					</center>
				</div>

			</div>




			<%
				} else if (mybean.ticketParentId.equals("0")) {
			%>
			<div class="form-group form-md-line-input">
<div class="col-xs-6">
				<center>
					<a class="btn1"
						href="callurlapp-ticket-add.jsp?add=yes&comp_id=<%=mybean.comp_id%>&ticket_parent_id=<%=mybean.ticket_id%>"">Add
						Child Ticket</a>
				</center>
				</div>
				<div class="col-xs-6">
					<center>
						<a class="btn1"
							href="callurlapp-ticket-list.jsp?add=yes&comp_id=<%=mybean.comp_id%>&previous_ticket_id=<%=mybean.ticket_id%>"">Previous
							Ticket&nbsp(s)</a>
					</center>
				</div>

			</div>
			<%
				}
			%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Ticket ID: <b><font
						color="black"><%=mybean.ticket_id%></font></b></label> <input name="ticket_id"
					type="hidden" id="ticket_id" value="<%=mybean.ticket_id%>">
			</div>

			<%
				if (!mybean.childTickets.equals("")) {
			%>


			<div class="form-group form-md-line-input">

				<label for="form_control_1">Child Tickets:&nbsp<%=mybean.childTickets%></label>

			</div>

			<%
				}
			%>


			<%
				if (!mybean.ticket_contact_id.equals("")
						&& !mybean.ticket_contact_id.equals("0")) {
			%>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Contact: <font
					color="black"><%=mybean.contact_name%></font></label> <input type="hidden"
					name="contact_id" id="contact_id"
					value="<%=mybean.ticket_contact_id%>" />
			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Customer: <font
					color="black"><%=mybean.customer_name%></font></label> <input type="hidden"
					name="customer_id" id="customer_id"
					value="<%=mybean.ticket_customer_id%>" />
			</div>

			<%
				}
			%>

			<%
				if (!mybean.veh_id.equals("0")) {
			%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Vehicle ID: &nbsp<font
					color="black"><%=mybean.veh_id%></font></label>

			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Reg. No.: &nbsp<font
					color="black"><%=mybean.veh_reg_no%></font></label>

			</div>
			<%
				}
			%>

			<%
				if (!mybean.jc_id.equals("0")) {
			%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>JC ID:&nbsp<font
					color="black"><%=mybean.jc_id%></font></label>
			</div>
			<%
				}
			%>


			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Subject<font
					color="#ff0000">*</font>:</label> <input name="txt_ticket_subject"
					type="text" class="form-control" value="<%=mybean.ticket_subject%>"
					size="50" maxlength="255"
					onChange="SecurityCheck('txt_ticket_subject',this,'hint_txt_ticket_subject');">
			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Description<font
					color="#ff0000">*</font>:</label>
				<textarea name="txt_ticket_desc" cols="57" rows="3"
					class="form-control" id="txt_ticket_desc"
					onChange="SecurityCheck('txt_ticket_desc',this,'hint_txt_ticket_desc');"><%=mybean.ticket_desc%></textarea>
			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Report Date<font
					color=#ff0000>*</font>:</label> <input type="text" size="16"
					name="txt_report_date" id="txt_report_date"
					value="<%=mybean.ticket_report_date%>" class="form-control"
					size="20" maxlength="14" onclick="datePicker('txt_report_date')"
					readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Report Time<font
					color=#ff0000>*</font>:</label> <input type="text" size="16"
					name="txt_report_time" id="txt_report_time"
					value="<%=mybean.ticket_report_time%>" class="form-control"
					size="20" maxlength="16" onclick="timePicker('txt_report_time')"
					onfocusout="SecurityCheck('txt_report_time',this,'hint_txt_report_time');"
					readonly>
			</div>


			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Solution / Closing
					Summary<font color="#ff0000">*</font></font>:</label>
				<textarea name="txt_comments" cols="57" rows="5"
					class="form-control" id="txt_comments"
					onChange="SecurityCheck('txt_comments',this,'hint_txt_comments');"
					onKeyUp="charcount('txt_comments', 'span_txt_comments','<font color=red>({CHAR} characters left)</font>', '2000');"><%=mybean.ticket_closed_comments%></textarea>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Type:</label> <select
					name="dr_ticket_tickettype_id" class="form-control"
					id="dr_ticket_tickettype_id"
					onChange="SecurityCheck('dr_ticket_tickettype_id',this,'hint_dr_ticket_tickettype_id');">
					<%=mybean.PopulateTicketType()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Status<font
					color="#ff0000">*</font>:</label> <select name="dr_ticket_ticketstatus_id"
					id="dr_ticket_ticketstatus_id" class="form-control"
					onChange="SecurityCheck('dr_ticket_ticketstatus_id',this,'hint_dr_ticket_ticketstatus_id');">
					<%=mybean.PopulateStatus()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Priority<font
					color="#ff0000">*</font>:</label> <select
					name="dr_ticket_priorityticket_id" class="form-control"
					id="dr_ticket_priorityticket_id"
					onChange="SecurityCheck('dr_ticket_priorityticket_id',this,'hint_dr_ticket_priorityticket_id');">
					<%=mybean.PopulateTicketPrioirty()%>
				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Department<font
					color="#ff0000">*</font>:</label> <select name="dr_ticket_dept_id"
					class="form-control" id="dr_ticket_dept_id"
					onChange="SecurityCheck('dr_ticket_dept_id',this,'hint_dr_ticket_dept_id');populateCatogery(this);">
					<%=mybean.PopulateDepartment()%>
				</select>
			</div>


			<%
				if (mybean.config_service_ticket_cat.equals("1")) {
			%>
			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Category:</label>
				<%=mybean.PopulateTicketCategory(
						mybean.ticket_ticket_dept_id, mybean.comp_id)%>
			</div>
			<%
				}
			%>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Executive<font
					color="#ff0000">*</font>:</label> <select name="dr_ticket_emp_id"
					class="form-control" id="dr_ticket_emp_id"
					onChange="SecurityCheck('dr_ticket_emp_id',this,'hint_dr_ticket_emp_id');">

					<%=mybean.PopulateAllExecutive()%>

				</select>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Due Date</label> <input
					name="txt_due_date" id="txt_due_date" type="text"
					class="form-control" value="<%=mybean.duedate%>" size="50"
					maxlength="15"
					onChange="SecurityCheck('txt_ticket_cc',this,'hint_txt_ticket_cc');"
					readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Email CC:</label> <input
					name="txt_ticket_cc" type="text" class="form-control"
					value="<%=mybean.ticket_cc%>" size="50" maxlength="1000"
					onChange="SecurityCheck('txt_ticket_cc',this,'hint_txt_ticket_cc');">
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Entry By:</label> <input
					name="txt_ticket_cc" type="text" class="form-control"
					value="<%=mybean.entry_id%>" size="50" maxlength="1000" readonly>
			</div>

			<div class="form-group form-md-line-input">
				<label for="form_control_1"><div class="hint"
						id="hint_txt_enquiry_exp_close_date"></div>Entry Date:</label> <input
					name="txt_ticket_cc" type="text" class="form-control"
					value="<%=mybean.entry_date%>" size="50" maxlength="1000" readonly>
			</div>



		</div>
		<div id="hiddenfields">

			<!-- 		</div> -->
	</form>

	<div class="container-fluid" style="min-height: 10px"></div>
	<div class="modal fade" id="Hintclicktocall" role="basic"
		aria-hidden="true" style="transform: translate(0, 50%)">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<span> &nbsp;&nbsp;Loading... </span> <br> <br>

				</div>
			</div>
		</div>
	</div>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/jquery.app.js" type="text/javascript"></script>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/components-select2.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/dynacheck-post.js" type="text/javascript"></script>
	<script>
		$(function() {
			$(".select2-container").css("width", "100%");
		});
	</script>

	<script>
		function SecurityCheck(name, obj, hint) {
			var value = GetReplacePost(obj.value);
			var checked = '';
			var ticket_id = GetReplacePost(document.form1.ticket_id.value);
			var url = "../service/ticket-dash-historycheck.jsp?";
			var str = "123";
			var status = '';
			var comment = '';
			var strtoast = '';
			var param = "name=" + name + "&value=" + value + "&checked="
					+ checked + "&ticket_id=" + ticket_id;
			if (name == 'txt_report_time') {
				var report_date = document.getElementById("txt_report_date").value;
				if (report_date != '') {
					value = report_date + ' ' + value
				} else {
					showToast('Select Report Time!');
				}
				var param = "name=" + name + "&value=" + value + "&checked="
						+ checked + "&ticket_id=" + ticket_id;
			}
			if (name == 'txt_comments' || name == 'dr_ticket_ticketstatus_id') {
				status = document.getElementById("dr_ticket_ticketstatus_id").value;
				comment = document.getElementById("txt_comments").value;
				checked = '';
				param = "name=" + name + "&status=" + status + "&checked="
						+ checked + "&comment=" + comment + "&ticket_id="
						+ ticket_id;
			}

			$
					.post(
							url,
							param,
							function(data, status) {
								if (status == 'success') {
									strtoast = data.trim().replace('</font>',
											'');
									if (strtoast.indexOf('&nbsp') != -1) {
										strtoast = strtoast.split('&nbsp');
										var hiddenfields = strtoast[1];
										if (name == 'dr_ticket_ticketqueue_id'
												|| name == 'txt_report_time') {
											// 							setTimeout('populateduuedate()', 1000);
											document
													.getElementById("hiddenfields").innerHTML = hiddenfields;
											populateduuedate();

										}
										showToast(strtoast[0].replace(
												'</font>', ''));
									}
									showToast(strtoast.trim());

								}
							})
		}

		function populateduuedate() {
			var duedate = document.getElementById("duedate").value;
			document.getElementById("txt_due_date").value = duedate;
		}
	</script>
</body>
<!-- END BODY -->
</html>