<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.PSFDays_Update" scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>
</head>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>&nbsp;PSF Day</h1>
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
							<li><a href="psfdays.jsp?all=yes">List PSF Days</a> &gt;</li>
							<li><a href="psfdays-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									PSF Day</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;PSF Day
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font>
										</center><br>
										
										<form name="form1" method="post" class="form-horizontal">

											<div class="form-element3">
												<div>Brand<font color=red>*</font>:</div>
													<select name="dr_brand_id" class="form-control"
														id="dr_brand_id">
														<%=mybean.PopulateBrand(mybean.comp_id)%>
													</select>
											</div>
											<div class="form-element3">
												<div> Executive Type<b><font color="#ff0000">*</font></b>:</div>
													<select name="dr_psfdays_exe_type" class="form-control"
														id="dr_psfdays_exe_type">
														<%=mybean.PopulateCRMExecutiveType(mybean.psfdays_exe_type)%>
													</select>
											</div>
											<div class="form-element6">
												<div> Category: </div>
													<select name="dr_psfdays_jccat_id" class="form-control"
														id="dr_psfdays_jccat_id">
														<%=mybean.PopulateCategory(mybean.psfdays_jccat_id)%>
													</select>
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label> Days<b><font color="#ff0000">*</font></b>:</label>
													<input name="txt_psfdays_daycount" type="text"
														class="form-control" id="txt_psfdays_daycount"
														onKeyUp="toInteger('txt_psfdays_daycount','Days')"
														value="<%=mybean.psfdays_daycount%>"
														maxlength="6" />
											</div>
											<div class="form-element6">
												<label> Description<b><font color="#ff0000">*</font></b>:</label>
													<input name="txt_psfdays_desc" type="text"
														class="form-control" id="txt_psfdays_desc"
														value="<%=mybean.psfdays_desc%>"  maxlength="255" />
											</div>
											
											<div class="form-element12">
												<div> Category:</div>
														<div class="form-element5">
															<span id="catHint"> <select name="dr_category"
																 multiple="multiple" size="20" class="form-control"
																id="dr_category">
																	<%=mybean.PopulateTransCategory()%>
															</select>
															</span>
														</div>
														
														<div class="form-element2">
															
																<br /> <br /> <br /> 
																<center> <input
																	name="Input2" type="button" class="btn btn-success"
																	onclick="JavaScript:AddItem('dr_category','psf_cat_trans', '');onPress();"
																	value="  Add&gt;&gt; " align="left" /> <br /> <br /> 
																	</center>
																	<center>
																	<input
																	name="Input" type="button" class="btn btn-success"
																	onclick="JavaScript:DeleteItem('psf_cat_trans')"
																	value="&lt;&lt;Delete" align="left" />
																	 <br /> <br /> <br />  
																	
															</center>
														</div>

														<div class="form-element5">

															<select name="psf_cat_trans" size="20" multiple="multiple"
																class="form-control" id="psf_cat_trans">
																<%=mybean.PopulateSelectedCategory()%>
															</select>
														</div>
											</div>
											
											<div class="form-element12">
												<div> Type:</div>
														<div class="form-element5">
															<span id="typeHint"> <select name="dr_type"
																 multiple="multiple" size="20" class="form-control"
																id="dr_type">
																	<%=mybean.PopulateTransType()%>
															</select>
															</span>
														</div>
														
														<div class="form-element2">
															
																<br /> <br /> <br /> 
																<center> <input
																	name="Input2" type="button" class="btn btn-success"
																	onclick="JavaScript:AddItem('dr_type','psf_type_trans', '');onPress();"
																	value="  Add&gt;&gt; " align="left" /> <br /> <br /> 
																	</center>
																	<center>
																	<input
																	name="Input" type="button" class="btn btn-success"
																	onclick="JavaScript:DeleteItem('psf_type_trans')"
																	value="&lt;&lt;Delete" align="left" />
																	 <br /> <br /> <br />  
																	
															</center>
														</div>

														<div class="form-element5">

															<select name="psf_type_trans" size="20" multiple="multiple"
																class="form-control" id="psf_type_trans">
																<%=mybean.PopulateSelectedType()%>
															</select>
														</div>
											</div>
											
											<div class="form-element12">
												<div> PSF Executive<font color="#ff0000">*</font></b>:</div>
														<div class="form-element5">
															<span id="exeHint"> <select name="dr_executive"
																 multiple="multiple" size="20" class="form-control"
																id="dr_executive">
																	<%=mybean.PopulateExecutive(mybean.psfdays_emp_id)%>
															</select>
															</span>
														</div>
														
														<div class="form-element2">
															
																<br /> <br /> <br /> 
																<center> <input
																	name="Input2" type="button" class="btn btn-success"
																	onclick="JavaScript:AddItem('dr_executive','psf_exe', '');onPress();"
																	value="  Add&gt;&gt; " align="left" /> <br /> <br /> 
																	</center>
																	<center>
																	<input
																	name="Input" type="button" class="btn btn-success"
																	onclick="JavaScript:DeleteItem('psf_exe')"
																	value="&lt;&lt;Delete" align="left" />
																	 <br /> <br /> <br />  
																	
															</center>
														</div>

														<div class="form-element5">

															<select name="psf_exe" size="20" multiple="multiple"
																class="form-control" id="psf_exe">
																<%=mybean.PopulateSelectedExecutives()%>
															</select>
														</div>
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label> Script<b><font color="#ff0000">*</font></b>:</label>
												
													<textarea name="txt_psfdays_script" cols="70" rows="5"
														class="form-control" id="txt_psfdays_script"
														onKeyUp="charcount('txt_psfdays_script', 'span_txt_psfdays_script','<font color=red>({CHAR} characters left)</font>', '2000')"><%=mybean.psfdays_script%></textarea>
													<br> <span id="span_txt_psfdays_script"> (2000
															Characters)</span> <span id="showcontacts">
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
																<td align="right">Executive:</td>
																<td align="left">[EXENAME]</td>
															</tr>
														</tbody>
													</table>
											</div>

											<%if (mybean.status.equals("Update")) {%>
											
											<div class="form-element12">
												<div class="form-element8 form-element-center">
													<label> Automated Tasks: </label>
														<table
															class="table table-responsive table-hover table-bordered">
															<thead>
																<tr>
																	<th>PSF Satisfied</th>
																	<th>Send Email</th>
																	<th>Format Email</th>
																	<th>Send SMS</th>
																	<th>Format SMS</th>
																</tr>
															</thead>
															<tbody>
	
																<tr>
																	<td align="left">PSF Contactable For Customer:</td>
																	<td align="center"><input
																		name="chk_psfdays_contactable_email_enable"
																		type="checkbox"
																		id="chk_psfdays_contactable_email_enable"
																		<%=mybean.PopulateCheck(mybean.psfdays_contactable_email_enable)%> />
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&email=yes&status=PSF Contactable For Customer&opt=psfdays_contactable_email_format">Format</a></td>
																		<td align="center"><input type="checkbox"
																			name="chk_psfdays_contactable_sms_enable"
																			id="chk_psfdays_contactable_sms_enable"
																			<%=mybean.PopulateCheck(mybean.psfdays_contactable_sms_enable)%>></td>
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&sms=yes&status=PSF Contactable For Customer&opt=psfdays_contactable_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">PSF Contactable For Executive:</td>
																	<td align="center">
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&email=yes&status=PSF Contactable For Executive&opt=psfdays_contactable_email_exe_format">Format</a></td>
																		<td align="center"></td>
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&sms=yes&status=PSF Contactable For Executive&opt=psfdays_contactable_sms_exe_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">PSF Non-Contactable For Customer:</td>
																	<td align="center"><input
																		name="chk_psfdays_noncontactable_email_enable"
																		type="checkbox"
																		id="chk_psfdays_noncontactable_email_enable"
																		<%=mybean.PopulateCheck(mybean.psfdays_noncontactable_email_enable)%> />
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&email=yes&status=PSF Non-Contactable For Customer&opt=psfdays_noncontactable_email_format">Format</a></td>
																		<td align="center"><input type="checkbox"
																			name="chk_psfdays_noncontactable_sms_enable"
																			id="chk_psfdays_noncontactable_sms_enable"
																			<%=mybean.PopulateCheck(mybean.psfdays_noncontactable_sms_enable)%>></td>
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&sms=yes&status=PSF Non-Contactable For Customer&opt=psfdays_noncontactable_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">PSF Non-Contactable For Executive:</td>
																	<td align="center">
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&email=yes&status=PSF Non-Contactable For Executive&opt=psfdays_noncontactable_email_exe_format">Format</a></td>
																		<td align="center"></td>
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&sms=yes&status=PSF Non-Contactable For Executive&opt=psfdays_noncontactable_sms_exe_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">PSF Satisfied For Customer:</td>
																	<td align="center"><input
																		name="chk_psfdays_satisfied_email_enable"
																		type="checkbox" id="chk_psfdays_satisfied_email_enable"
																		<%=mybean.PopulateCheck(mybean.psfdays_satisfied_email_enable)%> />
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&email=yes&status=PSF Satisfied For Customer&opt=psfdays_satisfied_email_format">Format</a></td>
																		<td align="center"><input type="checkbox"
																			name="chk_psfdays_satisfied_sms_enable"
																			id="chk_psfdays_satisfied_sms_enable"
																			<%=mybean.PopulateCheck(mybean.psfdays_satisfied_sms_enable)%>></td>
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&sms=yes&status=PSF Satisfied SMS&opt=psfdays_satisfied_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">PSF Satisfied For Executive:</td>
																	<td align="center">
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&email=yes&status=PSF Satisfied For Executive&opt=psfdays_satisfied_email_exe_format">Format</a></td>
																		<td align="center"></td>
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&sms=yes&status=PSF Satisfied SMS For Executive &opt=psfdays_satisfied_sms_exe_format">Format</a></td>
																</tr>
	
																<tr>
																	<td align="left">PSF Dis-Satisfied For Customer:</td>
																	<td align="center"><input type="checkbox"
																		name="chk_psfdays_dissatisfied_email_enable"
																		id="chk_psfdays_dissatisfied_email_enable"
																		<%=mybean.PopulateCheck(mybean.psfdays_dissatisfied_email_enable)%>></td>
																	<td align="center"><a
																		href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&email=yes&status=PSF Dis-Satisfied For Customer&opt=psfdays_dissatisfied_email_format">Format</a></td>
																	<td align="center"><input type="checkbox"
																		name="chk_psfdays_dissatisfied_sms_enable"
																		id="chk_psfdays_dissatisfied_sms_enable"
																		<%=mybean.PopulateCheck(mybean.psfdays_dissatisfied_sms_enable)%>></td>
																	<td align="center"><a
																		href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&sms=yes&status=PSF Dis-Satisfied SMS&opt=psfdays_dissatisfied_sms_format">Format</a></td>
																</tr>
																<tr>
																	<td align="left">PSF Dis-Satisfied For Executive:</td>
																	<td align="center">
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&email=yes&status=PSF Dis-Satisfied For Executive&opt=psfdays_dissatisfied_email_exe_format">Format</a></td>
																		<td align="center"></td>
																		<td align="center"><a
																			href="managepsfdays-format.jsp?psfdays_id=<%=mybean.psfdays_id%>&sms=yes&status=PSF Dis-Satisfied SMS For Executive &opt=psfdays_dissatisfied_sms_exe_format">Format</a></td>
																</tr>
															</tbody>
													</table>
											</div>
										</div>
										
											<%}%>
											
											<div class="form-element3">
												<label> Active: </label>
													<input id="chk_psfdays_active" type="checkbox"
														name="chk_psfdays_active"
														<%=mybean.PopulateCheck(mybean.psfdays_active)%> />
											</div>
											
											<div class="row"></div>
											
											<%if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {%>
														
											<div class="form-element6">
												<label> Entry By:</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>" />
											</div>
											
											<%}%>
											<%if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {%>
														
											<div class="form-element6">
												<label> Entry Date:</label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>" />
											</div>
											
											<%}%>
											<%if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {%>
														
											<div class="form-element6">
												<label> Modified By:</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>" />
											</div>
											
											<%}%>
											<%if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {%>
											
											<div class="form-element6">
												<label> Modified Date:</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>" />
											</div>
											<%}%>

											<div class="row"></div>
											
											<center>
												<%if (mybean.status.equals("Add")) {%>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton"
													value="Add Followup Days"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" id="add_button"
													value="yes" />
												<%} else if (mybean.status.equals("Update")) {%>
												<input type="hidden" name="update_button" id="update_button"
													value="yes" /> <input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update Followup Days"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)"
													value="Delete Followup Days" />
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
// 			document.form1.txt_camptype_desc.focus()
		}
	
		function onPress() {
			for (i = 0; i < document.form1.psf_exe.options.length; i++) {
				document.form1.psf_exe.options[i].selected = true;
			}
			for (i = 0; i < document.form1.psf_cat_trans.options.length; i++) {
				document.form1.psf_cat_trans.options[i].selected = true;
			}
			for (i = 0; i < document.form1.psf_type_trans.options.length; i++) {
				document.form1.psf_type_trans.options[i].selected = true;
			}
		}
	</script>
</body>
</html>
