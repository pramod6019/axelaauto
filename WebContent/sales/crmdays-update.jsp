<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.CRMDays_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed"
	onLoad="DisplayType();FormFocus();">
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
						<h1><%=mybean.status%>
							CRM Day
						</h1>
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
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="crmdays.jsp?all=yes">List CRM Days</a> &gt;</li>
							<li><a href="crmdays-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> CRM Day</a>:</li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div align="center">
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</div>
							
							<form name="form1" method="post" class="form-horizontal">
							
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.status%>&nbsp;CRM Day
										</div>
									</div>

									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div align="center">
												<font size="1"> Form fields marked with a red
													asterisk <b><font color="#ff0000">*</font></b> are required.<br>
												</font>
											</div>
											<br>

											<div class="form-element3">
												<label>Brand<b><font color="#ff0000">*</font></b>: </label>
												<select name="dr_crmdays_brand_id" class="form-control"
													id="dr_crmdays_brand_id">
													<%=mybean.PopulateBrand(mybean.comp_id)%>
												</select>
											</div>

											<div class="form-element3">
												<label>Type<b><font color="#ff0000">*</font></b>: </label>
												<select name="dr_crmdays_crmtype_id" class="form-control"
													id="dr_crmdays_crmtype_id" onChange="DisplayType()">
													<%=mybean.PopulateCRMType(mybean.crmdays_crmtype_id)%>
												</select>
											</div>


											<div class="form-element6">
												<label>Sales Sales Consultant Type<b><font
														color="#ff0000">*</font></b>: </label>
												<select name="dr_crmdays_exe_type" class="form-control"
													id="dr_crmdays_exe_type">
													<%=mybean.PopulateCRMExecutiveType()%>
												</select>
											</div>

											<div class="form-element6">
												<label>Days<b><font color="#ff0000">*</font></b>: </label>
												<input name="txt_crmdays_daycount" type="text"
													class="form-control" id="txt_crmdays_daycount"
													onKeyUp="toInteger('txt_crmdays_daycount','Days')"
													value="<%=mybean.crmdays_daycount%>" size="10"
													maxlength="6" />
											</div>

											<div class="form-element6">
												<label>Description<b><font color="#ff0000">*</font></b>: </label>
												<div>
													<input name="txt_crmdays_desc" type="text"
														class="form-control" id="txt_crmdays_desc"
														value="<%=mybean.crmdays_desc%>" size="20" maxlength="255" />
												</div>
											</div>

											<div class="form-element6">
												<label>Script<b><font color="#ff0000">*</font></b>: </label>
												<textarea name="txt_crmdays_script" cols="70" rows="5"
													class="form-control" id="txt_crmdays_script"
													onKeyUp="charcount('txt_crmdays_script', 'span_txt_crmdays_script','<font color=red>({CHAR} characters left)</font>', '2000')"><%=mybean.crmdays_script%></textarea>
												<span id="span_txt_crmdays_script"> (2000 Characters)</span>
												<span id="showcontacts"></span>
											</div>

											<div class="form-element6">
												<table
													class="table table-responsive table-hover table-bordered">
													<thead>
														<tr>
															<th colspan="2">Subtitution Variables:</th>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td align="right">Salutation:</td>
															<td align="left">[SALUTATION]</td>
														</tr>
														<tr>
															<td align="right">Contact Name:</td>
															<td align="left">[CONTACTNAME]</td>
														</tr>
														<tr>
															<td align="right">Sales Consultant:</td>
															<td align="left">[EXENAME]</td>
														</tr>
													</tbody>
												</table>
											</div>

											<%
												if (mybean.status.equals("Update")) {
											%>
											<div class="form-element12">
												<div class="form-element8 form-element-center">
													<label> Automated Tasks: </label>
													<table
														class="table table-responsive table-hover table-bordered">
														<thead>
															<tr>
																<th>CRM Satisfied</th>
																<th>Send Email</th>
																<th>Format Email</th>
																<th>Send SMS</th>
																<th>Format SMS</th>
															</tr>
														</thead>
														<tbody>
															<tr>
																<td align="left">CRM Contactable For Customer:</td>
																<td align="center"><input
																	name="chk_crmdays_contactable_email_enable"
																	type="checkbox"
																	id="chk_crmdays_contactable_email_enable"
																	<%=mybean.PopulateCheck(mybean.crmdays_contactable_email_enable)%> />
																</td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&email=yes&status=CRM Contactable&opt=crmdays_contactable_email_format">Format</a>
																</td>
																<td align="center"><input type="checkbox"
																	name="chk_crmdays_contactable_sms_enable"
																	id="chk_crmdays_contactable_sms_enable"
																	<%=mybean.PopulateCheck(mybean.crmdays_contactable_sms_enable)%>>
																</td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&sms=yes&status=CRM Contactable&opt=crmdays_contactable_sms_format">Format</a>
																</td>
															</tr>
															<tr>
																<td align="left">CRM Contactable For Sales
																	Consultant:</td>
																<td align="center"></td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&email=yes&status=CRM Contactable For Sales Consultant&opt=crmdays_contactable_email_exe_format">Format</a>
																</td>
																<td align="center"></td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&sms=yes&status=CRM Contactable For Sales Consultant&opt=crmdays_contactable_sms_exe_format">Format</a>
																</td>
															</tr>
															<tr>
																<td align="left">CRM Non Contactable For Customer:</td>
																<td align="center"><input
																	name="chk_crmdays_noncontactable_email_enable"
																	type="checkbox"
																	id="chk_crmdays_noncontactable_email_enable"
																	<%=mybean.PopulateCheck(mybean.crmdays_noncontactable_email_enable)%> />
																</td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&email=yes&status=CRM Non Contactable&opt=crmdays_noncontactable_email_format">Format</a>
																</td>
																<td align="center"><input type="checkbox"
																	name="chk_crmdays_noncontactable_sms_enable"
																	id="chk_crmdays_noncontactable_sms_enable"
																	<%=mybean.PopulateCheck(mybean.crmdays_noncontactable_sms_enable)%>>
																</td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&sms=yes&status=CRM Non Contactable&opt=crmdays_noncontactable_sms_format">Format</a>
																</td>
															</tr>
															<tr>
																<td align="left">CRM Non Contactable For Sales
																	Consultant:</td>
																<td align="center"></td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&email=yes&status=CRM Non Contactable For Sales Consultant&opt=crmdays_noncontactable_email_exe_format">Format</a>
																</td>
																<td align="center"></td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&sms=yes&status=CRM Non Contactable For Sales Consultant&opt=crmdays_noncontactable_sms_exe_format">Format</a>
																</td>
															</tr>

															<tr>
																<td align="left">CRM Satisfied For Customer:</td>
																<td align="center"><input
																	name="chk_crmdays_satisfied_email_enable"
																	type="checkbox" id="chk_crmdays_satisfied_email_enable"
																	<%=mybean.PopulateCheck(mybean.crmdays_satisfied_email_enable)%> />
																</td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&email=yes&status=CRM Satisfied&opt=crmdays_satisfied_email_format">Format</a>
																</td>
																<td align="center"><input type="checkbox"
																	name="chk_crmdays_satisfied_sms_enable"
																	id="chk_crmdays_satisfied_sms_enable"
																	<%=mybean.PopulateCheck(mybean.crmdays_satisfied_sms_enable)%>>
																</td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&sms=yes&status=CRM Satisfied&opt=crmdays_satisfied_sms_format">Format</a>
																</td>
															</tr>
															<tr>
																<td align="left">CRM Satisfied For Sales
																	Consultant:</td>
																<td align="center"></td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&email=yes&status=CRM Satisfied For Sales Consultant&opt=crmdays_satisfied_email_exe_format">Format</a>
																</td>
																<td align="center"></td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&sms=yes&status=CRM Satisfied For Sales Consultant&opt=crmdays_satisfied_sms_exe_format">Format</a>
																</td>
															</tr>

															<tr>
																<td align="left">CRM Dis-Satisfied For Customer:</td>
																<td align="center"><input type="checkbox"
																	name="chk_crmdays_dissatisfied_email_enable"
																	id="chk_crmdays_dissatisfied_email_enable"
																	<%=mybean.PopulateCheck(mybean.crmdays_dissatisfied_email_enable)%>>
																</td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&email=yes&status=CRM Dis-Satisfied&opt=crmdays_dissatisfied_email_format">Format</a>
																</td>
																<td align="center"><input type="checkbox"
																	name="chk_crmdays_dissatisfied_sms_enable"
																	id="chk_crmdays_dissatisfied_sms_enable"
																	<%=mybean.PopulateCheck(mybean.crmdays_dissatisfied_sms_enable)%>>
																</td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&sms=yes&status=CRM Dis-Satisfied&opt=crmdays_dissatisfied_sms_format">Format</a>
																</td>
															</tr>
															<tr>
																<td align="left">CRM Dis-Satisfied For Sales
																	Consultant:</td>
																<td align="center"></td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&email=yes&status=CRM Dis-Satisfied For Sales Consultant&opt=crmdays_dissatisfied_email_exe_format">Format</a>
																</td>
																<td align="center"></td>
																<td align="center"><a
																	href="managecrmdays-format.jsp?crmdays_id=<%=mybean.crmdays_id%>&sms=yes&status=CRM Dis-Satisfied For Sales Consultant&opt=crmdays_dissatisfied_sms_exe_format">Format</a>
																</td>
															</tr>
														</tbody>
													</table>
												</div>
											</div>
											<%
												}
											%>

											<div class="form-element12">
												<div class="form-element8 form-element-center">
													<div class="form-element4 form-element">
														<label>Active: </label> 
														<input id="chk_crmdays_active"
															type="checkbox" name="chk_crmdays_active"
															<%=mybean.PopulateCheck(mybean.crmdays_active)%> />
													</div>

													<div class="form-element4">
														<label>Lost Follow-up: </label> 
														<input id="chk_crmdays_lost" type="checkbox"
															name="chk_crmdays_lost"
															<%=mybean.PopulateCheck(mybean.crmdays_lostfollowup)%> />
													</div>

													<div class="form-element4">
														<label>Test Drive Follow-up: </label> 
														<input id="chk_crmdays_testdrivefollowup" type="checkbox"
															name="chk_crmdays_testdrivefollowup"
															<%=mybean.PopulateCheck(mybean.crmdays_testdrivefollowup)%> />
													</div>

													<div class="form-element4 form-element">
														<label>Home Visit Follow-up: </label> 
														<input id="chk_crmdays_homevisitfollowup" type="checkbox"
															name="chk_crmdays_homevisitfollowup"
															<%=mybean.PopulateCheck(mybean.crmdays_homevisitfollowup)%> />
													</div>

													<div class="form-element4">
														<label>Waiting Period:</label> 
														<input id="chk_crmdays_waitingperiod" type="checkbox"
															name="chk_crmdays_waitingperiod"
															<%=mybean.PopulateCheck(mybean.crmdays_waitingperiod)%> />
													</div>

													<div class="form-element4">
														<label>SO Inactive: </label> 
														<input id="chk_crmdays_so_inactive" type="checkbox"
															name="chk_crmdays_so_inactive"
															<%=mybean.PopulateCheck(mybean.crmdays_so_inactive)%> />
													</div>

												</div>
											</div>



											<div class="form-element12">
											
												<% if (mybean.status.equals("Update") && !(mybean.entry_by == null)
															&& !(mybean.entry_by.equals(""))) {
												%>
												
												<div class="form-element2"></div>
												<div class="form-element5">
													<label>Entry By: </label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
												</div>

												<% } %>

												<% if (mybean.status.equals("Update") && !(mybean.entry_date == null)
															&& !(mybean.entry_date.equals(""))) {
												%>


												<div class="form-element5">
													<label>Entry Date:</label> 
													<span> <%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>">
													</span>
												</div>

												<% } %>

												<% if (mybean.status.equals("Update") && !(mybean.modified_by == null)
															&& !(mybean.modified_by.equals(""))) {
												%>
												<div class="form-element2"></div>
												<div class="form-element5">
													<label>Modified By:</label> 
													<span> <%=mybean.unescapehtml(mybean.entry_by)%>
													    <input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
													</span>
												</div>
												<% } %>

												<% if (mybean.status.equals("Update") && !(mybean.modified_date == null)
															&& !(mybean.modified_date.equals(""))) {
												%>
												<div class="form-element5">
													<label>Modified Date:</label> 
													<span> <%=mybean.modified_date%>
													      <input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
													</span>
												</div>

											</div>

											<% } %>
											<% if (mybean.status.equals("Add")) { %>
											<center>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton" value="Add Followup Days"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" id="add_button" value="yes" />
											</center>
											<% } else if (mybean.status.equals("Update")) { %>

											<center>
												<input type="hidden" name="update_button" id="update_button" value="yes" />
												<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton" value="Update Followup Days"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Followup Days" />
											</center>
											<% } %>

										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script type="text/javascript">
		function FormFocus() { //v1.0
		// 				document.form1.txt_camptype_desc.focus();
		}

		function DisplayType() {

			var str1 = document.getElementById('dr_crmdays_crmtype_id').value;
			if (str1 == "1") {
				$('#lostfollowup').show();
				$('#tdfollowup').show();
				$('#hmfollowup').show();
			} else {
				$('#lostfollowup').hide();
				$('#tdfollowup').hide();
				$('#hmfollowup').hide();
			}
			if (str1 == "2") {
				$('#SOInactive').show();
				$('#waitingperiod').show();
			} else {
				$('#SOInactive').hide();
				$('#waitingperiod').hide();
			}
		}
	</script>

</body>
</HTML>
