<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.ManageTicketDepartment_Update" scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
</head>
<body onLoad="FormFocus();Displaypaymode();checkbox(chk_ticket_dept);"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%> Department</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="manageticketdept.jsp?all=yes">List Department</a> &gt;</li>
							<li><a href="manageticketdept-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									Department</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b> <%=mybean.msg%>
						</b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>
										Department
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
											<center>
												<font size="1">Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.
												</font>
											</center><br>
											
												<div class="form-element6">
													<label>Name<font color="#ff0000">*</font>:</label>
														<input name="txt_ticket_dept_name" id="txt_ticket_dept_name"
															type="text" class="form-control"
															value="<%=mybean.ticket_dept_name%>"  maxlength="255" />
												</div>
												
												<div class="form-element6">
													<label>Description<font color="#ff0000">*</font>:</label>
														<input name="txt_ticket_dept_desc" type="text"
															class="form-control" value="<%=mybean.ticket_dept_desc%>"
															 maxlength="255" />
												</div>
											
												<div class="form-element6">
													<label>Due Hours<font color="#ff0000">*</font>:</label>
														<input name="txt_ticket_dept_duehrs" type="text" id="txt_ticket_dept_duehrs"
															class="form-control" value="<%=mybean.ticket_dept_duehrs%>"
															onKeyUp="toNumber('txt_ticket_dept_duehrs','Due Hours')" maxlength="5"
															 maxlength="20" />
												</div>
											
												<div class="form-element6">
													<label>Level-1 Hours:</label>
														<input name="txt_ticket_dept_trigger1_hrs" id="txt_ticket_dept_trigger1_hrs" type="text"
															class="form-control" value="<%=mybean.ticket_dept_trigger1_hrs%>" 
															onKeyUp="toNumber('txt_ticket_dept_trigger1_hrs','Level-1 Hours')" maxlength="5" />
												</div>
											
												<div class="form-element6">
													<label>Level-2 Hours:</label>
														<input name="txt_ticket_dept_trigger2_hrs" id="txt_ticket_dept_trigger2_hrs" type="text"
															class="form-control" value="<%=mybean.ticket_dept_trigger2_hrs%>"
															onKeyUp="toNumber('txt_ticket_dept_trigger2_hrs','Level-2 Hours')" maxlength="5" />
												</div>
											
												<div class="form-element6">
													<label >Level-3 Hours:</label>
														<input name="txt_ticket_dept_trigger3_hrs" id="txt_ticket_dept_trigger3_hrs" type="text"
															class="form-control" value="<%=mybean.ticket_dept_trigger3_hrs%>" 
															onKeyUp="toNumber('txt_ticket_dept_trigger3_hrs','Level-3 Hours')" maxlength="5" />
												</div>
											
											
												<div class="form-element6">
													<label >Level-4 Hours:</label>
														<input name="txt_ticket_dept_trigger4_hrs" id="txt_ticket_dept_trigger4_hrs" type="text"
															class="form-control" value="<%=mybean.ticket_dept_trigger4_hrs%>" 
															onKeyUp="toNumber('txt_ticket_dept_trigger4_hrs','Level-4 Hours')" maxlength="5" />
												</div>
											
												<div class="form-element6">
													<label >Level-5 Hours:</label>
														<input name="txt_ticket_dept_trigger5_hrs" id="txt_ticket_dept_trigger5_hrs" type="text"
															class="form-control" value="<%=mybean.ticket_dept_trigger5_hrs%>" 
															onKeyUp="toNumber('txt_ticket_dept_trigger5_hrs','Level-5 Hours')" maxlength="5" />
												</div>
											
												<div class="form-element6">
													<label >Business Hours:</label>
														<input id="chk_ticket_dept_businesshrs"
															name="chk_ticket_dept_businesshrs" type="checkbox"
															<%=mybean.PopulateCheck(mybean.ticket_dept_business_hrs)%>
															onclick="Displaypaymode();" />
												</div>
												
											<div class="row"></div>
												<div class="form-element6" id="starttime">
													<label >Start Time<font color="#ff0000">*</font>:</label>
														<input name="txt_start_time" type="text"
															class="form-control" id="txt_start_time"
															value="<%=mybean.start_time%>" size="7" maxlength="5" />
												</div>
												
											
												<div class="form-element6" id="endtime">
													<label >End Time<font color="#ff0000">*</font>:</label>
														<input name="txt_end_time" type="text"
															class="form-control" id="txt_end_time"
															value="<%=mybean.end_time%>" size="7" maxlength="5" />
												</div>
												
											
											<div class="form-element6">
												<div class="form-element12" id="holiday">
													<label>Holiday</label>
												</div>
									
											
												<div class="form-element6" id="sun">
													<label >Sunday:</label>
														<input id="chk_ticket_dept1" name="chk_ticket_dept1" type="checkbox"
															<%=mybean.PopulateCheck(mybean.ticket_dept_sun)%>
															onchange="checkbox(this)" />
												</div>
											
										
												<div class="form-element6" id="mon">
													<label >Monday:</label>
														<input id="chk_ticket_dept2" name="chk_ticket_dept2" type="checkbox"
															<%=mybean.PopulateCheck(mybean.ticket_dept_mon)%>
															onchange="checkbox(this)" />
												</div>
											
											
												<div class="form-element6" id="tue">
													<label >Tuesday:</label>
														<input id="chk_ticket_dept3" name="chk_ticket_dept3" type="checkbox"
															<%=mybean.PopulateCheck(mybean.ticket_dept_tue)%>
															onchange="checkbox(this)" />
												</div>
										
												<div class="form-element6" id="wed">
													<label >Wednesday:</label>
														<input id="chk_ticket_dept4" name="chk_ticket_dept4" type="checkbox"
															<%=mybean.PopulateCheck(mybean.ticket_dept_wed)%>
															onchange="checkbox(this)" />
												</div>
											
												<div class="form-element6" id="thu">
													<label >Thursday:</label>
														<input id="chk_ticket_dept5" name="chk_ticket_dept5" type="checkbox"
															<%=mybean.PopulateCheck(mybean.ticket_dept_thu)%>
															onchange="checkbox(this)" />
												</div>
											
												<div class="form-element6" id="fri">
													<label >Friday:</label>
														<input id="chk_ticket_dept6" name="chk_ticket_dept6" type="checkbox"
															<%=mybean.PopulateCheck(mybean.ticket_dept_fri)%>
															onchange="checkbox(this)" />
												</div>
											
												<div class="form-element6" id="sat">
													<label >Saturday:</label>
														<input id="chk_ticket_dept7" name="chk_ticket_dept7" type="checkbox"
															<%=mybean.PopulateCheck(mybean.ticket_dept_sat)%>
															onchange="checkbox(this)" />
												</div>
											</div>	
											
											<div class="row"></div>	
											
											<%if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {%>
											
												<div class="form-element6">
													<label >Entry By:</label>
														<%=mybean.unescapehtml(mybean.entry_by)%>
														<input name="entry_by" type="hidden" id="entry_by"
															value="<%=mybean.entry_by%>" />
												</div>
											<%}%>
											
											<%if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {%>
											
												<div class="form-element6">
													<label>Entry Date:</label>
														<%=mybean.entry_date%>
														<input type="hidden" name="entry_date"
															value="<%=mybean.entry_date%>" />
												</div>
											<%}%>
											<%if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {%>
											
												<div class="form-element6">
													<label>Modified By:</label>
														<%=mybean.unescapehtml(mybean.modified_by)%>
														<input name="modified_by" type="hidden" id="modified_by"
															value="<%=mybean.unescapehtml(mybean.modified_by)%>" />
												</div>
												
											<%}%>
											<%if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {%>
											
												<div class="form-element6">
													<label >Modified Date:</label>
														<%=mybean.modified_date%>
														<input type="hidden" name="modified_date"
															value="<%=mybean.modified_date%>" />
												</div>
											<%}%>
											
											<center>
												<%if (mybean.status.equals("Add")) {%>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton" value="Add Department"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" id="add_button" name="add_button"
													value="yes" />
												<%} else if (mybean.status.equals("Update")) {%>
												<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update Department"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input id="update_button" name="update_button" type="hidden"
													value="yes" /> <input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Department" />
												<%}%>
											</center>
										</form>
									</div>
								</div>
							</div>


						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript">
		function FormFocus() {
			document.form1.txt_ticket_dept_name.focus()
		}

		function Displaypaymode() {
			var str = document.getElementById('chk_ticket_dept_businesshrs').checked;

			if (str == true) {
				// 			displayRow('starttime');
				$('#starttime').show();
				$('#endtime').show();
				$('#holiday').show();
				$('#sun').show();
				$('#mon').show();
				$('#tue').show();
				$('#wed').show();
				$('#thu').show();
				$('#fri').show();
				$('#sat').show();
				// 			displayRow('endtime');
				// 			displayRow('holiday');
				// 			displayRow('sun');
				// 			displayRow('mon');
				// 			displayRow('tue');
				// 			displayRow('wed');
				// 			displayRow('thu');
				// 			displayRow('fri');
				// 			displayRow('sat');
			} else {
				$('#starttime').hide();
				$('#endtime').hide();
				$('#holiday').hide();
				$('#sun').hide();
				$('#mon').hide();
				$('#tue').hide();
				$('#wed').hide();
				$('#thu').hide();
				$('#fri').hide();
				$('#sat').hide();
				// 			hideRow('starttime');
				// 			hideRow('endtime');
				// 			hideRow('holiday');
				// 			hideRow('sun');
				// 			hideRow('mon');
				// 			hideRow('tue');
				// 			hideRow('wed');
				// 			hideRow('thu');
				// 			hideRow('fri');
				// 			hideRow('sat');												
			}
		}

		function checkbox(chk_ticket_dept) {
			var flag = 0;
			for (var i = 1; i <= 7; i++) {
				var chkcurr = 'chk_ticket_dept' + i;
				if (chk_ticket_dept.name != chkcurr) {
					if (document.getElementById('chk_ticket_dept' + i).checked == true)
						flag++;
				}
			}
			if (flag > 5)
				document.getElementById(chk_ticket_dept.name).checked = false;
		}
		
		/* function isNumber(ob) {
			var invalidChar = /[^0-9-]/gi;
			if (invalidChar.test(ob.value)) {
				ob.value = ob.value.replace(invalidChar, "");
			}
		} */
	</script>
</body>
</html>
