<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Notification_exe_send"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<%@include file="../Library/css.jsp"%>

<script language="JavaScript" type="text/javascript">
	function hideRow(element) {
		//element refer to row id where u want to hide a row.
		var myObj = document.getElementById(element);
		//u need to a give id for a row to which u want to hide
		var cels = myObj.getElementsByTagName('select');
		for (var col_no = 0; col_no < cels.length; col_no++) {
			cels[col_no].style.display = 'none';
		}
		myObj.style.visibility = 'hidden';
	}

	function displayRow(element) {
		var myObj = document.getElementById(element);
		//u need to a give id for a row to which u want to hide
		var cels = myObj.getElementsByTagName('select')
		for (var col_no = 0; col_no < cels.length; col_no++) {
			cels[col_no].style.display = '';
		}
		myObj.style.visibility = 'visible';
		//myObj.style.height = '0px';
	}
	
	function Displaypaymode() {
		var all_exe = document.getElementById('notification_allexe').checked;
		if (all_exe) {
			$("#emprows1").hide();
		}
		else{
			$("#emprows1").show();
		}
	}

	function FormSubmit() {
		document.form1.submit();
	}
</script>
</HEAD>
<body onLoad="Displaypaymode();" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Send Executive Notification</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="manager.jsp"> Business Manager</a> &gt;</li>
						<li><a href="notification-exe-send.jsp">Send Notification
								<%=mybean.status%></a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Send Executive Notification<%=mybean.status%>
									</div>
								</div>
								<div class="portlet-body">
									<div class="container-fluid">
										<center>
											<font size="1">Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form name="form1" method="post" class="form-horizontal">

											<%
												if (mybean.branch_id.equals("0")) {
											%>
											
											<div class="form-element6">
												<label> Branch<font color=red>*</font>: </label>
												<select name="dr_branch" id="dr_branch"
													class="form-control" onchange="FormSubmit();">
													<%=mybean.PopulateBranch()%>
												</select>
											</div>
											
											<%
												}
											%>
											<div class="row"></div>

											<!-- 			Select Jobtitle START -->
<!-- <div> Job Title</div> -->
											<div id="emprows3">
											<div style="margin-left: 10px;">Job Title:</div>
												<div class="form-element4">
												
													<select name="dr_title" size="20" multiple="multiple"
														class="form-control" id="dr_title">
														<%=mybean.PopulateJobTitle()%>
													</select>

												</div>

												<div class="form-element4">
													<center>
														<br /> <br /> <br /> <input name="Input3" type="button"
															class="btn btn-success"
															onclick="JavaScript:AddItem('dr_title','dr_title_trans', ''); getExecutives();"
															value="   Add &gt;&gt;" /> <br /> <br> <input
															name="Input3" type="button" class="btn btn-success"
															onclick="JavaScript:DeleteItem('dr_title_trans'); getExecutives();"
															value="&lt;&lt; Delete" />
													</center>
												</div>
												<div class="form-element4">
													<select name="dr_title_trans" size="20" multiple="multiple"
														class="form-control" id="dr_title_trans">
														<%=mybean.PopulateJobTitleTrans(mybean.exe_jobtitle_trans)%>
													</select>
												</div>
											</div>
											<!-- 			Select Jobtitle End -->
											
<!-- 											START OF EXECUTIVE -->

											<center>
												<label> All Executives: </label>
												<input type="checkbox" name="notification_allexe" class=""
														id="notification_allexe" onClick="Displaypaymode();"
														<%=mybean.PopulateCheck(mybean.notification_allexe)%> />
											</center>

											<div id="emprows1">
											<div style="margin-left: 10px;">Executives:</div>
												<div class="form-element4">
													<span id="exeHint"> <select name="dr_executive"
														size="20" multiple="multiple" class="form-control"
														id="dr_executive">
															<%=mybean.PopulateExecutives()%>
													</select>
													</span>
												</div>

												<div class="form-element4">
													<center>
														<br /> <br /> <br /> <br /> <br /> <input
															name="Input2" type="button" class="btn btn-success"
															onclick="JavaScript:AddItem('dr_executive','exe_team_trans', '')"
															value="   Add &gt;&gt;" /> <br /> <br /> <input
															name="Input" type="button" class="btn btn-success"
															onclick="JavaScript:DeleteItem('exe_team_trans')"
															value="&lt;&lt; Delete" />
													</center>
												</div>

												<div class="form-element4">
													<select name="exe_team_trans" size="20" multiple="multiple"
														class="form-control" id="exe_team_trans">
														<%=mybean.PopulateExecutivesTrans(mybean.exe_team_trans)%>
													</select>
												</div>
											</div>

											<!-- 											END OF EXECUTIVE -->


											<div class="form-element6">
												<label>Title<font
													color=red>*</font>:
												</label>
													<input type="type" name="notifiction_title"
														id="notifiction_title" class="form-control"></input>
											</div>
											
											
											
											<div class="form-element6">
												<label >Message<font color=red>*</font>: </label>
													<textarea name="txt_notification_msg" class="form-control"
														id="txt_notification_msg" cols="58" rows="4"
														onKeyUp="charcount('txt_notification_msg', 'span_sms_msg','<font color=red>({CHAR} characters left)</font>', '500')"><%=mybean.sms_msg%></textarea>
													<span id="span_sms_msg"> (500 characters) </span>
											</div>


											<center>
												<input name="send_button" type="submit" onclick="onPress();"
													class=" btn btn-success" id="send_button" value="Send" />

											</center>
												<div class="form-element6 form-element-center">
													<table class="table table-hover table-bordered">
														<tr>
															<th colspan="2"><b><center>Substitution
																		Variables</center></b></th>
														</tr>
														<tr>
															<td align="right">Executive Name:</td>
															<td>[NAME]</td>
														</tr>
														<tr>
															<td align="right">Executive ID:</td>
															<td>[EXECUTIVEID]</td>
														</tr>
														<tr>
															<td align="right">Executive Ref. No.:</td>
															<td>[EXECUTIVEREFNO]</td>
														</tr>
													</table>
												</div>
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

	<script>
		function onPress() {
			for (i = 0; i < document.form1.dr_title_trans.options.length; i++) {
				document.form1.dr_title_trans.options[i].selected = true;
			}
			for (i = 0; i < document.form1.dr_executive.options.length; i++) {
				document.form1.dr_executive.options[i].selected = true;
			}
			for (i = 0; i < document.form1.exe_team_trans.options.length; i++) {
				document.form1.exe_team_trans.options[i].selected = true;
			}
		}
		
		function getExecutives(){
			var dr_title = getoutputSelected(document.getElementById("dr_title_trans").options);
			var branch_id = $('#dr_branch').val();
			showHint('../portal/executive-check.jsp?executive=yes&branch_id='+branch_id+'&dr_title=' + GetReplaceString(dr_title), 'exeHint');
		}
	</script>

</body>
</HTML>
