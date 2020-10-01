<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.ManagePreownedCRMFollowupDays_Update" scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@ include file="../Library/css.jsp"%>
</head>

<body onLoad="FormFocus();" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%> Pre-Owned CRM Follow-up Day</h1>
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
							<li><a href="../portal/manager.jsp">Business Manager</a>&gt;</li>
							<li><a href="managepreownedcrmfollowupdays.jsp?all=yes">List
									Pre-Owned CRM Follow-up Days</a>&gt;</li>
							<li><a href="managepreownedcrmfollowupdays-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									 Pre-Owned CRM Follow-up Day</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
				
						<div class="tab-pane" id="">
							<div align="center">
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</div>

							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<form name="form1" method="post" class="form-horizontal">
								<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Pre-Owned CRM Follow-up Day </div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
		<!-- 										<div class="tab-pane" id=""> -->
													<!-- START PORTLET BODY -->
														<div align="center">
															<font size="1">Form fields marked with a red asterisk
																<b><font color="#ff0000">*</font></b> are required.
															</font>
														</div><br>
													
														<div class="form-element6">
															<label>Brand<b><font color="#ff0000">*</font></b>:</label>
																<select name="dr_precrmfollowupdays_brand_id" class="form-control" id="dr_precrmfollowupdays_brand_id">
																	<%=mybean.PopulateBrand(mybean.comp_id)%>
																</select>
														</div>
														
														<div class="form-element6">
															<label>Type<b><font color="#ff0000">*</font></b>:</label>
																<select name="dr_precrmfollowupdays_precrmtype_id" class="form-control"
																	id="dr_precrmfollowupdays_precrmtype_id" onChange="DisplayType()">
																	<%=mybean.PopulateCRMType(mybean.precrmfollowupdays_precrmtype_id)%>
																</select>
														</div>
													
													
														<div class="form-element6">
															<label>Pre-Owned Consultant Type<b><font color="#ff0000">*</font></b>:</label>
																<select name="dr_precrmfollowupdays_exe_type" class="form-control" id="dr_precrmfollowupdays_exe_type">
																	<%=mybean.PopulateCRMExecutiveType()%>
																</select>
														</div>
													
														<div class="form-element6">
															<label>Days<b><font color="#ff0000">*</font></b>:</label>
																<input name="txt_precrmfollowupdays_daycount" type="text"
																	class="form-control" id="txt_precrmfollowupdays_daycount"
																	onKeyUp="toInteger('txt_precrmfollowupdays_daycount','Days')"
																	value="<%=mybean.precrmfollowupdays_daycount%>"
																	maxlength="6" />
														</div>
														
														<div class="row">
															<div class="form-element12">
															<label>Description<b><font color="#ff0000">*</font></b>:</label>
																<input name="txt_precrmfollowupdays_desc" type="text"
																	class="form-control" id="txt_precrmfollowupdays_desc"
																	value="<%=mybean.precrmfollowupdays_desc%>" 
																	maxlength="255" />
															</div>
														</div>
														
		
														<div class="form-element6">
															<label>Script<b><font color="#ff0000">*</font></b>:</label>
																<textarea name="precrmfollowupdays_script" cols="70" rows="6"
																	class="form-control" id="precrmfollowupdays_script"
																	onKeyUp="charcount('precrmfollowupdays_script', 'span_precrmfollowupdays_script','<font color=red>({CHAR} characters left)</font>', '2000')"><%=mybean.precrmfollowupdays_script%></textarea>
																<span id="span_precrmfollowupdays_script"> (2000 Characters)</span>
																<span id="showcontacts"></span>
														</div>
												
														<div class="form-element6">
																<label></label>
																	<table class="table table-responsive table-hover table-bordered">
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
																				<td align="right">Pre-Owned Consultant:</td>
																				<td align="left">[EXENAME]</td>
																			</tr>
																		</tbody>
																	</table>
															</div>
														<%if (mybean.status.equals("Update")) { %>
														<div class="row">
															<div class="form-element8 form-element-center">
					 												<label> Automated Tasks: </label>
					 												<table class="table table-responsive table-hover table-bordered">
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
																					<td align="left">Pre-Owned Contactable For Customer:</td>
					 																<td align="center"><input
																						name="chk_precrmfollowupdays_contactable_email_enable" type="checkbox"
					 																	id="chk_precrmfollowupdays_contactable_email_enable" 
																						<%=mybean.PopulateCheck(mybean.precrmfollowupdays_contactable_email_enable)%>/>
																						<td align="center"><a
																						href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&email=yes&status=Pre-Owned CRM Contactable&opt=precrmfollowupdays_contactable_email_format">Format</a></td>
																					<td align="center"><input type="checkbox"
																						name="chk_precrmfollowupdays_contactable_sms_enable" id ="chk_precrmfollowupdays_contactable_sms_enable"
																						<%=mybean.PopulateCheck(mybean.precrmfollowupdays_contactable_sms_enable)%>></td>
																					<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&sms=yes&status=Pre-Owned CRM Contactable&opt=precrmfollowupdays_contactable_sms_format">Format</a></td>
																				</tr>
																				<tr> 
																					<td align="left">Pre-Owned Contactable For Consultant:</td>
					 																<td align="center">
																						<td align="center"><a
																						href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&email=yes&status=Pre-Owned CRM Contactable For Executive&opt=precrmfollowupdays_contactable_email_exe_format">Format</a></td>
																					<td align="center"></td>
																					<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&sms=yes&status=Pre-Owned CRM Contactable For Executive&opt=precrmfollowupdays_contactable_sms_exe_format">Format</a></td>
																				</tr>
																				 <tr> 
																					<td align="left">Pre-Owned Non Contactable For Customer:</td>
					 																<td align="center"><input
																						name="chk_precrmfollowupdays_noncontactable_email_enable" type="checkbox"
					 																	id="chk_precrmfollowupdays_noncontactable_email_enable" 
																						<%=mybean.PopulateCheck(mybean.precrmfollowupdays_noncontactable_email_enable)%>/>
																						<td align="center"><a
																						href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&email=yes&status=Pre-Owned CRM Non Contactable&opt=precrmfollowupdays_noncontactable_email_format">Format</a></td>
																					<td align="center"><input type="checkbox"
																						name="chk_precrmfollowupdays_noncontactable_sms_enable" id ="chk_precrmfollowupdays_noncontactable_sms_enable"
																						<%=mybean.PopulateCheck(mybean.precrmfollowupdays_noncontactable_sms_enable)%>></td>
																					<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&sms=yes&status=Pre-Owned CRM Non Contactable&opt=precrmfollowupdays_noncontactable_sms_format">Format</a></td>
																				</tr>
																				<tr> 
																					<td align="left">Pre-Owned Non Contactable For Consultant:</td>
					 																<td align="center">
																						<td align="center"><a
																						href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&email=yes&status=Pre-Owned CRM Non Contactable For Executive&opt=precrmfollowupdays_noncontactable_email_exe_format">Format</a></td>
																					<td align="center"></td>
																					<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&sms=yes&status=Pre-Owned CRM Non Contactable For Executive&opt=precrmfollowupdays_noncontactable_sms_exe_format">Format</a></td>
																				</tr>
																				
																				<tr> 
																					<td align="left">Pre-Owned Satisfied For Customer:</td>
					 																<td align="center"><input
																						name="chk_precrmfollowupdays_satisfied_email_enable" type="checkbox"
					 																	id="chk_precrmfollowupdays_satisfied_email_enable" 
																						<%=mybean.PopulateCheck(mybean.precrmfollowupdays_satisfied_email_enable)%>/>
																						<td align="center">
																						<a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&email=yes&status=Pre-Owned CRM Satisfied&opt=precrmfollowupdays_satisfied_email_format">Format</a></td>
																					<td align="center"><input type="checkbox"
																						name="chk_precrmfollowupdays_satisfied_sms_enable" id ="chk_precrmfollowupdays_satisfied_sms_enable"
																						<%=mybean.PopulateCheck(mybean.precrmfollowupdays_satisfied_sms_enable)%>></td>
																					<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&sms=yes&status=Pre-Owned CRM Satisfied&opt=precrmfollowupdays_satisfied_sms_format">Format</a></td>
																				</tr>
																				<tr> 
																					<td align="left">Pre-Owned Satisfied For Consultant:</td>
					 																<td align="center">
																						<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&email=yes&status=Pre-Owned CRM Satisfied For Executive&opt=precrmfollowupdays_satisfied_email_exe_format">Format</a></td>
																					<td align="center"></td>
																					<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&sms=yes&status=Pre-Owned CRM Satisfied For Executive&opt=precrmfollowupdays_satisfied_sms_exe_format">Format</a></td>
																				</tr>
					
					 															<tr>
																					<td align="left">Pre-Owned Dis-Satisfied For Customer:</td>
					 																<td align="center"><input type="checkbox" 
					 																	name="chk_precrmfollowupdays_dissatisfied_email_enable" id="chk_precrmfollowupdays_dissatisfied_email_enable"
																						<%=mybean.PopulateCheck(mybean.precrmfollowupdays_dissatisfied_email_enable)%>></td> 
					 																<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&email=yes&status=Pre-Owned CRM Dis-Satisfied&opt=precrmfollowupdays_dissatisfied_email_format">Format</a></td> 
					 																<td align="center"><input type="checkbox"
																						name="chk_precrmfollowupdays_dissatisfied_sms_enable" id="chk_precrmfollowupdays_dissatisfied_sms_enable"
																						<%=mybean.PopulateCheck(mybean.precrmfollowupdays_dissatisfied_sms_enable)%>></td> 
																					<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&sms=yes&status=Pre-Owned CRM Dis-Satisfied&opt=precrmfollowupdays_dissatisfied_sms_format">Format</a></td>
																			</tr>
																			<tr> 
																					<td align="left">Pre-Owned Dis-Satisfied For Consultant:</td>
					 																<td align="center"></td>
																					<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&email=yes&status=Pre-Owned CRM Dis-Satisfied For Executive&opt=precrmfollowupdays_dissatisfied_email_exe_format">Format</a></td>
																					<td align="center"></td>
																					<td align="center"><a href="managepreownedcrmfollowupdays-format.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>&sms=yes&status=Pre-Owned CRM Dis-Satisfied For Executive&opt=precrmfollowupdays_dissatisfied_sms_exe_format">Format</a></td>
																				</tr>
																			</tbody>
																		</table>
																</div>
															</div>
															<% }%>		
												
													<div class="form-element6">
														<label>Active: </label>
															<input id="chk_precrmfollowupdays_active" type="checkbox"
																name="chk_precrmfollowupdays_active"
																<%=mybean.PopulateCheck(mybean.precrmfollowupdays_active)%> />
													</div>
		
													<div class="form-element6">
														<label>Lost Follow-up: </label>
															<input id="chk_precrmfollowupdays_lostfollowup" type="checkbox" name="chk_precrmfollowupdays_lostfollowup"
																<%=mybean.PopulateCheck(mybean.precrmfollowupdays_lostfollowup)%> />
													</div>
												
													<div class="form-element6">
														<label>Test Drive Follow-up: </label>
															<input id="chk_precrmfollowupdays_testdrivefollowup" type="checkbox"
																name="chk_precrmfollowupdays_testdrivefollowup"
																<%=mybean.PopulateCheck(mybean.precrmfollowupdays_testdrivefollowup)%> />
													</div>
												
													<div class="form-element6">
														<label>Home Visit Follow-up: </label>
															<input id="chk_precrmfollowupdays_homevisitfollowup" type="checkbox"
																name="chk_precrmfollowupdays_homevisitfollowup"
																<%=mybean.PopulateCheck(mybean.precrmfollowupdays_homevisitfollowup)%> />
													</div>
												
													<div class="form-element6">
														<label>Waiting Period: </label>
															<input id="chk_precrmfollowupdays_waitingperiod" type="checkbox"
																name="chk_precrmfollowupdays_waitingperiod"
																<%=mybean.PopulateCheck(mybean.precrmfollowupdays_waitingperiod)%> />
													</div>
		
		
													<div class="form-element6">
														<label>SO Inactive: </label>
															<input id="chk_precrmfollowupdays_so_inactive" type="checkbox"
																name="chk_precrmfollowupdays_so_inactive"
																<%=mybean.PopulateCheck(mybean.precrmfollowupdays_so_inactive)%> />
													</div>
													
		<!-- 									/////////////////		 -->
		
													<% if (mybean.status.equals("Update")&& !(mybean.entry_by == null) && !(mybean.entry_by.equals("")) ) { %>
													<div class="form-element6">
														<label>Entry By: </label>
															<%=mybean.unescapehtml(mybean.entry_by)%>
															<input name="entry_by" type="hidden" id="entry_by"
																value="<%=mybean.unescapehtml(mybean.entry_by)%>">
													</div>
													<%}%>
													<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals("")) ) { %>
													<div class="form-element6">
														<label>Entry Date: </label>
															<%=mybean.entry_date%>
															<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
													</div>
													<%}%>
													<% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals("")) ) { %>
													<div class="form-element6">
														<label>Modified By: </label>
															<%=mybean.unescapehtml(mybean.modified_by)%>
															<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.unescapehtml(mybean.modified_by)%>">
													</div>
													<%}%>
													<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
													<div class="form-element6">
														<label>Modified Date: </label>
															<%=mybean.modified_date%>
															<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
													</div>
													<%}%>
													<center>
													<%if(mybean.status.equals("Add")){%>
													
													
														<label></label>
														<input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Followup Days"
															onClick="return SubmitFormOnce(document.form1, this);" /> 
															<input type="hidden" name="add_button" id="add_button" value="yes" />
													
													<%}else if (mybean.status.equals("Update")){%>
										
														<label></label>
														<input type="hidden" name="update_button" id="update_button" value="yes" /> <input name="updatebutton" type="submit"
															class="btn btn-success" id="updatebutton" value="Update Followup Days" onClick="return SubmitFormOnce(document.form1, this);" /> <input
															name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)"
															value="Delete Followup Days" />
												
													<%}%>
													</center>
		                                       </div>
		                                       </div>
											</form>
                               
						</div>
								
					</div>
				</div>
			</div>
		</div>
	</div>

<!-- 	</div> -->
	<!-- END CONTAINER -->
	<!-- 	CONTAINER-FLUID -->

<%@ include file="../Library/admin-footer.jsp"%>
<%@ include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_precrmfollowupdays_desc.focus()
	}
</script>
</body>
</html>
