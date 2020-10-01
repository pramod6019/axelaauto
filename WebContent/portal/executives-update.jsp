<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Executives_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<style>
@media ( max-width : 1024px) {
	/*    TABLE RESPONSIVEe START   */
	#mobile_view td {
		display: block;
	}
	/*    TABLE RESPONSIVE END   */
}
</style>
</head>
<body onLoad="FormFocus();DisplayBranch();DisplayBranch1();Displayemp();LoadExecutives();"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%> Executive</h1>
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
						<li><a href="../portal/exe.jsp">Executives</a> &gt;</li>
						<li><a href="exe-list.jsp?all=yes">List Executives</a> &gt;</li>
						<li><a href="exe-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Executive</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<form name="formemp" method="post" class="form-horizontal">
								<!-- 					Executive Portlet START -->
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>&nbsp;Executive
										</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
											<font size="1">Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.</font>	
											</center>
											<br />
											<div class="form-element6">
												<label> Name<font color=red>*</font>: </label>
													<input name="txt_emp_name" type="text" class="form-control"
														id="txt_emp_name" value="<%=mybean.emp_name%>" size="38"
														maxlength="255" />
											</div>
											<div class="form-element6">
												<label> Ref. No<font color=red>*</font>: </label>
													<input name="txt_emp_ref_no" type="text"
														class="form-control" id="txt_emp_ref_no"
														value="<%=mybean.emp_ref_no%>" size="20" maxlength="50" />
											</div>
											<div class="form-element6">
												<label> Role<font color=red>*</font>: </label>
													<select name="drop_emp_role_id" class="form-control"
														id="drop_emp_role_id" onChange="DisplayBranch();">
														<%=mybean.PopulateRole()%>
													</select>
											</div>
											<div class="form-element6">
												<label> Department<font color=red>*</font>: </label>
													<select name="drop_emp_department_id" class="form-control"
														id="drop_emp_department_id">
														<%=mybean.PopulateDepartment()%>
													</select>
											</div>
											<div class="form-element6">
												<label> Job Title<font color=red>*</font>: </label>
													<select name="drop_emp_jobtitle_id" class="form-control"
														id="drop_emp_jobtitle_id">
														<%=mybean.PopulateJobtitle()%>
													</select>
											</div>
										<div class="form-element6">
											<div class="form-element4 form-element">
												<label> DOB: </label>
													<select name="drop_DOBMonth" class="form-control" id="drop_DOBMonth">
													<%=mybean.PopulateMonth()%>
													</select>
											</div>
											<div class="form-element4   form-element-margin">
												<select name="drop_DOBDay" class="form-control" id="drop_DOBDay">
													<%=mybean.PopulateDay()%>
												</select>
											</div>
											<div class="form-element4 form-element form-element-margin">
											<select name="drop_DOBYear" class="form-control"
																id="drop_DOBYear">
																	<%=mybean.PopulateYear()%>
											</select>															
											</div>
										</div>
											<div class="row">
											<div class="form-element6">
												<label> Marital Status<font color=red>*</font>: </label>
													<select name="drop_emp_married" class="form-control"
														id="drop_emp_married">
														<%=mybean.PopulateMarried()%>
													</select>
											</div>
											<div class="form-element6">
												<label> Sex<font color=red>*</font>: </label>
													<select name="drop_emp_sex" class="form-control"
														id="drop_emp_sex">
														<%=mybean.PopulateSex()%>
													</select>

											</div>
											</div>
											<div class="form-element6">
												<label> Qualification: </label>
													<textarea name="txt_emp_qualification" cols="37" rows="4"
														class="form-control" id="txt_emp_qualification"
														maxlength="255"
														onKeyUp="charcount('txt_emp_qualification', 'span_txt_emp_qualification','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.emp_qualification%></textarea>
													<span id="span_txt_emp_qualification">(255
														Characters)</span>
											</div>
											<div class="form-element6">
												<label> Certification: </label>
													<textarea name="txt_emp_certification" cols="37" rows="4"
														class="form-control" id="txt_emp_certification"
														maxlength="255"
														onKeyUp="charcount('txt_emp_certification', 'span_txt_emp_certification','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.emp_certification%></textarea>
													<span id="span_txt_emp_certification">(255
														Characters)</span>
											</div>
											<div class="form-element6">
												<label> Phone 1: </label>
													<input name="txt_emp_phone1" type="text"
														class="form-control" id="txt_emp_phone1"
														onKeyUp="toPhone('txt_emp_phone1','Phone1')"
														value="<%=mybean.emp_phone1%>" size="20" maxlength="14" />
													(91-80-33333333)
											</div>
											<div class="form-element6">
												<label> Phone 2: </label>
													<input name="txt_emp_phone2" type="text"
														class="form-control" id="txt_emp_phone2"
														onKeyUp="toPhone('txt_emp_phone2','Phone2')"
														value="<%=mybean.emp_phone2%>" size="20" maxlength="14" />
													(91-80-33333333)

											</div>
											<div class="form-element6">
												<label> Mobile 1: </label>
													<input name="txt_emp_mobile1" type="text"
														class="form-control" id="txt_emp_mobile1"
														onKeyUp="toPhone('txt_emp_mobile1','Mobile 1')"
														value="<%=mybean.emp_mobile1%>" size="20" maxlength="13" />
													(91-9999999999)
											</div>
											<div class="form-element6">
												<label> Mobile 2: </label>
													<input name="txt_emp_mobile2" type="text"
														class="form-control" id="txt_emp_mobile2"
														onKeyUp="toPhone('txt_emp_mobile2','Mobile 2')"
														value="<%=mybean.emp_mobile2%>" size="20" maxlength="13" />
													(91-9999999999)
											</div>
											<div class="form-element6">
												<label> Email 1<font color=red>*</font>: </label>
													<input name="txt_emp_email1" type="text"
														class="form-control" id="txt_emp_email1"
														value="<%=mybean.emp_email1%>" size="38" maxlength="100" />
											</div>
											<div class="form-element6">
												<label> Email 2: </label>
													<input name="txt_emp_email2" type="text"
														class="form-control" id="txt_emp_email2"
														value="<%=mybean.emp_email2%>" size="38" maxlength="100" />
											</div>
<!-- 											<div class="form-group"> -->
<!-- 												<label class="control-label col-md-4"> Password<font -->
<!-- 													color=red>*</font>: -->
<!-- 												</label> -->
<!-- 												<div class="col-md-6 col-xs-12" > -->
<!-- 													<input name="txt_emp_upass" type="password" -->
<!-- 														autocomplete="off" class="form-control" id="txt_emp_upass" -->
<%-- 														value="<%=mybean.emp_upass%>" size="38" maxlength="20" /> --%>

<!-- 												</div> -->
<!-- 											</div> -->
											<div class="form-element6">
												<label> Address<font color=red>*</font>: </label>
													<textarea name="txt_emp_address" cols="37" rows="4"
														class="form-control" id="txt_emp_address" maxlength="255"
														onKeyUp="charcount('txt_emp_address', 'span_txt_emp_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.emp_address%></textarea>
													<span id="span_txt_emp_address">(255 Characters)</span>
											</div>
											<div class="form-element6">
												<label> Landmark: </label>
													<textarea name="txt_emp_landmark" cols="37" rows="4"
														class="form-control" id="txt_emp_landmark"
														onkeyup="charcount('txt_emp_landmark', 'span_txt_emp_landmark','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.emp_landmark%></textarea>
													<br /> <span id="span_txt_emp_landmark">(255 Characters)</span>
											</div>
											<div class="form-element6">
												<label> State<font color=red>*</font>: </label>
													<input name="txt_emp_state" type="text"
														class="form-control" id="txt_emp_state"
														value="<%=mybean.emp_state%>" size="30" maxlength="255" />
											</div>
											<div class="form-element6">
												<label> City<font color=red>*</font>: </label>
													<input name="txt_emp_city" type="text" class="form-control"
														value="<%=mybean.emp_city%>" size="30" maxlength="255" />
											</div>
											<div class="form-element6">
												<label> Pin/Zip<font color=red>*</font>: </label>
													<input name="txt_emp_pin" type="text" class="form-control"
														maxlength="6" id="txt_emp_pin"
														onKeyUp="toInteger('txt_emp_pin')"
														value="<%=mybean.emp_pin%>" size="12" maxlength="10" />
											</div>
											<div class="form-element6">
												<label> Branch<font color=red>*</font>: </label>
													<select name="dr_emp_branch_id" class="form-control"
														id="dr_emp_branch_id"
														onChange="DisplayBranch();LoadExecutives();">
														<%=mybean.PopulateBranch()%>
													</select>
											</div>
											<div class="row">
											<div class="form-element6">
												<label> Weekly Off: </label>
													<select name="dr_weeklyoff_id" class="form-control"
														id="dr_weeklyoff_id">
														<%=mybean.PopulateWeeklyOff()%>
													</select>
											</div>
											</div>
											<div class="form-element3 ">
												<label> Web Access:</label>
													<input type="checkbox" id="ch_emp_web" name="ch_emp_web"
														<%=mybean.PopulateCheck(mybean.emp_mis_access)%>>
														</div>
												<div class="form-element3">
													<label> App Access: </label>
													<input id="ch_emp_app" type="checkbox" name="ch_emp_app"
														<%=mybean.PopulateCheck(mybean.emp_access_app)%> />
												</div>
												<div class="form-element3">
												<label> MIS:</label>
													<input type="checkbox" id="ch_emp_mis_access" name="ch_emp_mis_access"
														<%=mybean.PopulateCheck(mybean.emp_mis_access)%>>
												</div>
												<div class="form-element3">
													<label> Export:</label>
													<input type="checkbox" id="ch_emp_export_access" name="ch_emp_export_access"
														<%=mybean.PopulateCheck(mybean.emp_export_access)%>>
												</div>
												<div class="form-element3">
													<label> Report: </label>
														<input type="checkbox" id="ch_emp_report_access" name="ch_emp_report_access"
															<%=mybean.PopulateCheck(mybean.emp_report_access)%>>
												</div>
												<div class="form-element3">
													<label> Copy: </label>
														<input type="checkbox" id="ch_emp_copy_access" name="ch_emp_copy_access"
															<%=mybean.PopulateCheck(mybean.emp_copy_access)%>>
												</div>
												<div class="form-element3">
													<label> Daily Status Report: </label>
														<input type="checkbox" id="ch_emp_dailystatus_report" name="ch_emp_dailystatus_report"
															<%=mybean.PopulateCheck(mybean.emp_dailystatus_report)%>>
												</div>
												<div class="form-element3">
													<label> Active: </label>
														<input id="ch_emp_active" name="ch_emp_active" type="checkbox"
															<%=mybean.PopulateCheck(mybean.emp_active)%> />
												</div>
											</div>
										</div>
									</div>
								<!-- 			Executive Portlet END -->
								<!-- 			Select Branches START -->
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none" id="branchrowshead">
											Select Branches</div>
									</div>
									<div class="portlet-body">
									<div class="container-fluid">
										<div class="tab-pane" id="">
												<div id="emprows2">
                                                   <center>
												<label> All Branches: </label> &nbsp;&nbsp;<input
													name="chk_emp_all_branches" type="checkbox" id="chk_emp_all_branches"
													<%=mybean.PopulateCheck(mybean.emp_all_branches)%>
													onclick="DisplayBranch1();" />
											</center>
											<div id="emprows3">
											<div class="form-element12 form-element">
                                                   <div class="form-element5">
														<select name="exe_branch" size="20" multiple="multiple"
															class="form-control" id="exe_branch">
															<%=mybean.PopulateBranchs()%>
														</select>
													</div>
													<div class="form-element2">
														<center>
															<br />
															<br />
															<br />
															<br />
															<br /> <input name="Input3" type="button"
																class="btn btn-success"
																onclick="JavaScript:AddItem('exe_branch','exe_branch_trans', ''); getBranch();"
																value="   Add &gt;&gt;" /> <br />
															<br> <input name="Input3" type="button"
																class="btn btn-success"
																onclick="JavaScript:DeleteItem('exe_branch_trans'); getBranch();"
																value="&lt;&lt; Delete" />
														</center>
													</div>
													<div class="form-element5">
														<select name="exe_branch_trans" size="20"
															multiple="multiple" class="form-control"
															id="exe_branch_trans">
															<%=mybean.PopulateBranchTrans()%>
														</select>
													</div>
												</div>
                                             </div>
											</div>
										</div>
										</div>
									</div>
								</div>
								<!-- 			Select Branches END -->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none" id="branchrowshead">
											Select Executives for Reporting</div>
									</div>
									<div class="portlet-body">
										<div class="container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												<label> All Executives: </label> &nbsp;&nbsp;<input
													name="chk_emp_all_exe" type="checkbox" id="chk_emp_all_exe"
													<%=mybean.PopulateCheck(mybean.emp_all_exe)%>
													onclick="Displayemp();" />
											</center>
											<div id="emprows1">
												<div class="form-element12 form-element">
													<div class="form-element5">
														<span id="exeHint"> 
														<select name="dr_executive"
															size="20" multiple="multiple" class="form-control"
															id="dr_executive">
																<%=mybean.PopulateExecutives()%>
														</select>
														</span>

													</div>

													<div class="form-element2">
													<center><br />
															<br />
															<br />
															<br />
															<br />
													<input name="Input2" type="button"
																class="btn btn-success"
																onclick="JavaScript:AddItem('dr_executive','exe_team_trans', '')"
																value="   Add &gt;&gt;" /> <br />
															<br />
															
															<input name="Input" type="button" class="btn btn-success"
																onclick="JavaScript:DeleteItem('exe_team_trans')"
																value="&lt;&lt; Delete" />
														</center>
													</div>

													<div class="form-element5">

														<select name="exe_team_trans" size="20"
															multiple="multiple" class="form-control"
															id="exe_team_trans">
															<%=mybean.PopulateExecutivesTrans()%>
														</select>
													</div>
												</div>
										</div>
									</div>
								</div>
								</div>
							</div>
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Access Rights</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
									<div class="container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element2">
												<label>Module Portal:</label>
												<input name="ch_emp_module_portal" type="checkbox" id="ch_emp_module_portal"
													<%=mybean.PopulateCheck(mybean.emp_module_portal)%> />
											</div>
											<div class="form-element2">
												<label>Module Activity:</label>
												<input name="ch_emp_module_activity" type="checkbox" id="ch_emp_module_activity"
													<%=mybean.PopulateCheck(mybean.emp_module_activity)%> />
											</div>											
											<div class="form-element2">
												<label>Module Customer:</label>
												<input name="ch_emp_module_customer" type="checkbox" id="ch_emp_module_customer"
													<%=mybean.PopulateCheck(mybean.emp_module_customer)%> />
											</div>											
											<div class="form-element2">
												<label>Module Sales:</label>
												<input name="ch_emp_module_sales" type="checkbox" id="ch_emp_module_sales"
													<%=mybean.PopulateCheck(mybean.emp_module_sales)%> />
											</div>											
											<div class="form-element2">
												<label>Module Preowned:</label>
												<input name="ch_emp_module_preowned" type="checkbox" id="ch_emp_module_preowned"
													<%=mybean.PopulateCheck(mybean.emp_module_preowned)%> />
											</div>											
											<div class="form-element2">
												<label>Module Service:</label>
												<input name="ch_emp_module_service" type="checkbox" id="ch_emp_module_service"
													<%=mybean.PopulateCheck(mybean.emp_module_service)%> />
											</div>											
											<div class="form-element2">
												<label>Module Accessories:</label>
												<input name="ch_emp_module_accessories" type="checkbox" id="ch_emp_module_accessories"
													<%=mybean.PopulateCheck(mybean.emp_module_accessories)%> />
											</div>
											<div class="form-element2">
												<label>Module Insurance:</label>
												<input name="ch_emp_module_insurance" type="checkbox" id="ch_emp_module_insurance"
													<%=mybean.PopulateCheck(mybean.emp_module_insurance)%> />
											</div>											
											<div class="form-element2">
												<label>Module Helpdesk:</label>
												<input name="ch_emp_module_helpdesk" type="checkbox" id="ch_emp_module_helpdesk"
													<%=mybean.PopulateCheck(mybean.emp_module_helpdesk)%> />
											</div>											
											<div class="form-element2">
												<label>Module Inventory:</label>
												<input name="ch_emp_module_inventory" type="checkbox" id="ch_emp_module_inventory"
													<%=mybean.PopulateCheck(mybean.emp_module_inventory)%> />
											</div>											
											<div class="form-element2">
												<label>Module Accounting:</label>
												<input name="ch_emp_module_accounting" type="checkbox" id="ch_emp_module_accounting"
													<%=mybean.PopulateCheck(mybean.emp_module_accounting)%> />
											</div>											
											<div class="form-element2">
												<label>Module Invoice:</label>
												<input name="ch_emp_module_invoice" type="checkbox" id="ch_emp_module_invoice"
													<%=mybean.PopulateCheck(mybean.emp_module_invoice)%> />
											</div>											
											<div class="form-element2">
												<label>Module App:</label>
												<input name="ch_emp_module_app" type="checkbox" id="ch_emp_module_app"
													<%=mybean.PopulateCheck(mybean.emp_module_app)%> />
											</div>																						
							
											<div class ="row"></div> 	
													<div class="form-element2">
													<label>Sales Executive:</label>
													<input name="ch_emp_sales" type="checkbox"
														id="ch_emp_sales"
														<%=mybean.PopulateCheck(mybean.emp_sales)%> /></div>
													<div class="form-element2">
													<label>Close Enquiry:</label>
													<input name="ch_emp_close_enquiry" type="checkbox"
														id="ch_emp_close_enquiry"
														<%=mybean.PopulateCheck(mybean.emp_close_enquiry)%> /></div>
														<div class="form-element2">
														<label>Monitoring Board:</label>
													<input name="ch_emp_mtrboard" type="checkbox"
														id="ch_emp_mtrboard"
														<%=mybean.PopulateCheck(mybean.emp_mtrboard)%> /></div>
														<div class="form-element2">
														<label>Pre-Owned:</label>
													<input name="ch_emp_preowned" type="checkbox"
														id="ch_emp_preowned"
														<%=mybean.PopulateCheck(mybean.emp_preowned)%> /></div>
													<div class="form-element2">
													<label>Update Quote Price:</label>
													<input name="ch_emp_quote_priceupdate"
														type="checkbox" id="ch_emp_quote_priceupdate"
														<%=mybean.PopulateCheck(mybean.emp_quote_priceupdate)%> /></div>
													<div class="form-element2">
													<label>Update Quote Discount:</label>
													<input name="ch_emp_quote_discountupdate"
														type="checkbox" id="ch_emp_quote_discountupdate"
														<%=mybean.PopulateCheck(mybean.emp_quote_discountupdate)%> /></div>
													<div class="form-element2">
													<label>Update SO Price:</label>
													<input name="ch_emp_so_priceupdate"
														type="checkbox" id="ch_emp_so_priceupdate"
														<%=mybean.PopulateCheck(mybean.emp_so_priceupdate)%> />
														</div>
													<div class="form-element2">
													<label>Update SO Discount:</label>
													<input name="ch_emp_so_discountupdate"
														type="checkbox" id="ch_emp_so_discountupdate"
														<%=mybean.PopulateCheck(mybean.emp_so_discountupdate)%> />
														</div>
														
												<div class="form-element2">
													<label>Update Bill Price:</label>
													<input name="chk_emp_bill_priceupdate"
														type="checkbox" id="chk_emp_bill_priceupdate"
														<%=mybean.PopulateCheck(mybean.emp_bill_priceupdate)%> />
														</div>
													<div class="form-element2">
													<label>Update Bill Discount:</label>
													<input name="chk_emp_bill_discountupdate"
														type="checkbox" id="chk_emp_bill_discountupdate"
														<%=mybean.PopulateCheck(mybean.emp_bill_discountupdate)%> />
													</div>		
														
														
													<div class="form-element2">
													<label>Update Invoice Price:</label>
													<input name="chk_emp_invoice_priceupdate"
														type="checkbox" id="chk_emp_invoice_priceupdate"
														<%=mybean.PopulateCheck(mybean.emp_invoice_priceupdate)%> />
														</div>
													<div class="form-element2">
													<label>Update Invoice Discount:</label>
													<input name="chk_emp_invoice_discountupdate"
														type="checkbox" id="chk_emp_invoice_discountupdate"
														<%=mybean.PopulateCheck(mybean.emp_invoice_discountupdate)%> />
													</div>
												<%
													if (mybeanheader.autoservice == 1) {
												%>
													<div class="form-element2">
													<label>Service Executive:</label>
													<input name="ch_emp_service" type="checkbox"
														id="ch_emp_service"
														<%=mybean.PopulateCheck(mybean.emp_service)%> />
														</div>
													<div class="form-element2">
													<label>Service Technician:</label>
													<input name="ch_emp_technician" type="checkbox"
														id="ch_emp_technician"
														<%=mybean.PopulateCheck(mybean.emp_technician)%> />
														</div>
													<div class="form-element2">
													<label>Service PSF:</label>
													<td><input name="ch_emp_service_psf" type="checkbox"
														id="ch_emp_service_psf"
														<%=mybean.PopulateCheck(mybean.emp_service_psf)%> />
														</div>
													<div class="form-element2">
													<label>Service PSF IACS:</label>
													<input name="ch_emp_service_psf_iacs"
														type="checkbox" id="ch_emp_service_psf_iacs"
														<%=mybean.PopulateCheck(mybean.emp_service_psf_iacs)%> />
														</div>
													<div class="form-element2">
													<label>Service CRM:</label>
													<input name="ch_emp_crm" type="checkbox"
														id="ch_emp_crm" <%=mybean.PopulateCheck(mybean.emp_crm)%> />
														</div>
													<div class="form-element2">
													<label>Pick Up Driver:</label>
												<input name="ch_emp_pickup_driver" type="checkbox"
														id="ch_emp_pickup_driver"
														<%=mybean.PopulateCheck(mybean.emp_pickup_driver)%> />
														</div>
													<div class="form-element2">
													<label>Insurance:</label>
													<input name="ch_emp_insur" type="checkbox"
														id="ch_emp_insur"
														<%=mybean.PopulateCheck(mybean.emp_insur)%> /></div>
													<div class="form-element2">
													<label>Field Executive:</label>
													<input name="ch_emp_fieldinsur" type="checkbox"
														id="ch_emp_fieldinsur"
														<%=mybean.PopulateCheck(mybean.emp_fieldinsur)%> />
														</div>
													<div class="form-element2">
													<label>Update Job Card Price:</label>
													<input name="ch_emp_jc_priceupdate"
														type="checkbox" id="ch_emp_jc_priceupdate"
														<%=mybean.PopulateCheck(mybean.emp_jc_priceupdate)%> />
														</div>
													<div class="form-element2">
													<label>Update Job Card Discount:</label>
													<input name="ch_emp_jc_discountupdate"
														type="checkbox" id="ch_emp_jc_discountupdate"
														<%=mybean.PopulateCheck(mybean.emp_jc_discountupdate)%> />
														</div>
													<div class="form-element2">
													<label>Ticket Owner:</label>
													<input name="ch_emp_ticket_owner" type="checkbox"
														id="ch_emp_ticket_owner"
														<%=mybean.PopulateCheck(mybean.emp_ticket_owner)%> />
														</div>
													<div class="form-element2">.
													<label>Ticket Close:</label>
													<input name="ch_emp_ticket_close" type="checkbox"
														id="ch_emp_ticket_close"
														<%=mybean.PopulateCheck(mybean.emp_ticket_close)%> />
														</div>
												<%
													}
												%>
<!-- 												                      <tr> -->
<!-- 												                          <td align="right">MIS:</td> -->
<%-- 												                          <td><input name="ch_emp_mis_access" type="checkbox" id="ch_emp_mis_access" <%=mybean.PopulateCheck(mybean.emp_mis_access) %> /></td> --%>
<!-- 												                          <td align="right">Export:</td> -->
<%-- 												                          <td><input type="checkbox" name="ch_emp_export_access" <%=mybean.PopulateCheck(mybean.emp_export_access) %> /></td> --%>
<!-- 												                        </tr> -->
<!-- 												                      <tr> -->
<!-- 												                          <td align="right">Report:</td> -->
<%-- 												                          <td><input type="checkbox" name="ch_emp_report_access" <%=mybean.PopulateCheck(mybean.emp_report_access) %> /></td> --%>
<!-- 												                          <td align="right">Browser Copy:</td> -->
<%-- 												                          <td><input name="ch_emp_copy_access" type="checkbox" id="ch_emp_copy_access" <%=mybean.PopulateCheck(mybean.emp_copy_access) %> /></td> --%>
<!-- 												                        </tr> -->
													<div class="form-element2">
													<label>Click to Call:</label>
													<input type="checkbox" name="ch_emp_clicktocall" id="ch_emp_clicktocall"
														<%=mybean.PopulateCheck(mybean.emp_clicktocall)%> />
														</div>
														
													<div class="form-element2">
													<label>Emax PM:</label>
													<input type="checkbox" name="ch_emp_emaxpm" id= "ch_emp_emaxpm"
														<%=mybean.PopulateCheck(mybean.emp_emaxpm)%> />
														</div>
														
													<div class="form-element2">
													<label>Stock Ageing:</label>
													<input name="ch_emp_stock_ageing" type="checkbox" id="ch_emp_stock_ageing"
														<%=mybean.PopulateCheck(mybean.emp_stock_ageing)%> />
														</div>
											
											<div class ="row"></div> 	
											
										<div class="form-element6">
												<label>Caller ID:</label>					
												<input name="txt_emp_caller_id" type="text"
												class="form-control" id="txt_emp_caller_id"
												onKeyUp="toInteger('txt_emp_caller_id')"
												value="<%=mybean.emp_callerid%>" size="22"
												maxlength="12" />	
										</div>		
										<div class="form-element6">				
												<label>Route No.:</label>
												<input name="txt_emp_routeno" type="text"
												class="form-control" id="txt_emp_routeno"
												value="<%=mybean.emp_routeno%>" size="40"
												maxlength="100" />
										</div>
										<div class="form-element6">		
											<label>ClickToCall Username:</label>
												<input name="txt_emp_clicktocall_username" type="text"
													class="form-control" id="txt_emp_clicktocall_username"
													value="<%=mybean.emp_clicktocall_username%>" size="32"
													maxlength="255" />
										</div>
										<div class="form-element6">
										<label>ClickToCall Password:</label>
											<input name="txt_emp_clicktocall_password" type="password"
												class="form-control" id="txt_emp_clicktocall_password"
												value="<%=mybean.emp_clicktocall_password%>" size="32"
												maxlength="255" />
										</div>
										<div class="form-element6">
												<label>ClickToCall Campaign:</label>
												<input
												name="txt_emp_clicktocall_campaign" type="text"
												class="form-control" id="txt_emp_clicktocall_campaign"
												value="<%=mybean.emp_clicktocall_campaign%>" size="32"
												maxlength="255" />
										</div>
										<div class="form-element6">
											<label>Device ID:</label>
											<input name="txt_emp_device_id"
											type="text" class="form-control"
											value="<%=mybean.emp_device_id%>" size="22"
											maxlength="20" />		
															<!----End Device ID-->
									</div>
										<div class="form-element6">
										<label>IP Access:</label>
																<input name="txt_emp_ip_access"
																	type="text" class="form-control"
																	value="<%=mybean.emp_ip_access%>" size="70"
																	maxlength="255" /></td>
																(comma and space seperated for each IP Address) <br /> Eg.:
																	192.168.0.1, 192.168.1.6
											</div>
										</div>
									</div>
								</div>
								</div>
								<!-- 			Priority START -->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Priority</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<table class="table table-bordered">
												<tr>
													<td align="right">Activity:&nbsp;</td>
													<td colspan="3">
													<div class="form-element1 ">Level 1
													<input type="checkbox" name="ch_emp_priorityactivity_level1"
														id="ch_emp_priorityactivity_level1"
														<%=mybean.PopulateCheck(mybean.emp_priorityactivity_level1)%> />
														</div>
														<div class="form-element1 ">Level 2
														<input type="checkbox" name="ch_emp_priorityactivity_level2"
														id="ch_emp_priorityactivity_level2"
														<%=mybean.PopulateCheck(mybean.emp_priorityactivity_level2)%> />
														</div>
														<div class="form-element1 ">Level 3
														<input type="checkbox" name="ch_emp_priorityactivity_level3"
														id="ch_emp_priorityactivity_level3"
														<%=mybean.PopulateCheck(mybean.emp_priorityactivity_level3)%> />
														</div>
														<div class="form-element1 ">Level 4
														<input type="checkbox" name="ch_emp_priorityactivity_level4"
														id="ch_emp_priorityactivity_level4"
														<%=mybean.PopulateCheck(mybean.emp_priorityactivity_level4)%> />
														</div>
														<div class="form-element1 ">Level 5
														<input type="checkbox" name="ch_emp_priorityactivity_level5"
														id="ch_emp_emp_priorityactivity_level5"
														<%=mybean.PopulateCheck(mybean.emp_priorityactivity_level5)%> />
														</div>
														</td>
												</tr>
												<tr>
													<td align="right">Project:&nbsp;</td>
													<td colspan="3">
													<div class="form-element1">Level 1
													<input type="checkbox" name="ch_emp_priorityproject_level1"
														id="ch_emp_priorityproject_level1"
														<%=mybean.PopulateCheck(mybean.emp_priorityproject_level1)%> />
														</div>
														<div class="form-element1 ">Level 2
														<input type="checkbox" name="ch_emp_priorityproject_level2"
														id="ch_emp_priorityproject_level2"
														<%=mybean.PopulateCheck(mybean.emp_priorityproject_level2)%> />
														</div>
														<div class="form-element1 ">Level 3
														<input type="checkbox" name="ch_emp_priorityproject_level3"
														id="ch_emp_priorityproject_level3"
														<%=mybean.PopulateCheck(mybean.emp_priorityproject_level3)%> />
														</div>
														<div class="form-element1 ">Level 4
														<input type="checkbox" name="ch_emp_priorityproject_level4"
														id="ch_emp_priorityproject_level4"
														<%=mybean.PopulateCheck(mybean.emp_priorityproject_level4)%> />
														</div>
														<div class="form-element1 ">Level 5
														<input type="checkbox" name="ch_emp_priorityproject_level5"
														id="ch_emp_priorityproject_level5"
														<%=mybean.PopulateCheck(mybean.emp_priorityproject_level5)%> />
														</div>
														</td>
												</tr>
												<tr>
													<td align="right">Task:&nbsp;</td>
													<td colspan="3">
													<div class="form-element1 ">Level 1
													<input type="checkbox" name="ch_emp_prioritytask_level1"
														id="ch_emp_prioritytask_level1"
														<%=mybean.PopulateCheck(mybean.emp_prioritytask_level1)%> />
														</div>
														<div class="form-element1 ">Level 2
														<input type="checkbox" name="ch_emp_prioritytask_level2"
														id="ch_emp_prioritytask_level2"
														<%=mybean.PopulateCheck(mybean.emp_prioritytask_level2)%> />
														</div>
														<div class="form-element1 ">Level 3
														<input type="checkbox" name="ch_emp_prioritytask_level3"
														id="ch_emp_prioritytask_level3"
														<%=mybean.PopulateCheck(mybean.emp_prioritytask_level3)%> />
														</div>
														<div class="form-element1 ">Level 4
														<input type="checkbox" name="ch_emp_prioritytask_level4"
														id="ch_emp_prioritytask_level4"
														<%=mybean.PopulateCheck(mybean.emp_prioritytask_level4)%> />
														</div>
														<div class="form-element1 ">Level 5
														<input type="checkbox" name="ch_emp_prioritytask_level5"
														id="ch_emp_prioritytask_level5"
														<%=mybean.PopulateCheck(mybean.emp_prioritytask_level5)%> />
														</div></td>
												</tr>
												<tr>
													<td align="right" nowrap>Enquiry Follow-up:&nbsp;</td>
													<td colspan="3">
														<div class="form-element1 ">Level 1
														<input type="checkbox" name="ch_emp_priorityenquiryfollowup_level1"
														id="ch_emp_priorityenquiryfollowup_level1"
														<%=mybean .PopulateCheck(mybean.emp_priorityenquiryfollowup_level1)%> />
														</div>
														<div class="form-element1 ">Level 2
														<input type="checkbox" name="ch_emp_priorityenquiryfollowup_level2"
														id="ch_emp_priorityenquiryfollowup_level2"
														<%=mybean .PopulateCheck(mybean.emp_priorityenquiryfollowup_level2)%> />
														</div>
														<div class="form-element1 ">Level 3
														<input type="checkbox" name="ch_emp_priorityenquiryfollowup_level3"
														id="ch_emp_priorityenquiryfollowup_level3"
														<%=mybean .PopulateCheck(mybean.emp_priorityenquiryfollowup_level3)%>>
														</div>
														<div class="form-element1 ">Level 4
														<input type="checkbox" name="ch_emp_priorityenquiryfollowup_level4"
														id="ch_emp_priorityenquiryfollowup_level4"
														<%=mybean .PopulateCheck(mybean.emp_priorityenquiryfollowup_level4)%> />
														</div>
														<div class="form-element1 ">Level 5 
															<input type="checkbox" name="ch_emp_priorityenquiryfollowup_level5"
															id="ch_emp_priorityenquiryfollowup_level5"
															<%=mybean .PopulateCheck(mybean.emp_priorityenquiryfollowup_level5)%> />
															</div>
													</td>
												</tr>
												<tr>
													<td align="right">Enquiry:&nbsp;</td>
													<td colspan="3">
													<div class="form-element1 ">Level 1
													<input type="checkbox" name="ch_emp_priorityenquiry_level1"
														id="ch_emp_priorityenquiry_level1"
														<%=mybean.PopulateCheck(mybean.emp_priorityenquiry_level1)%> />
														</div>
														<div class="form-element1 ">Level 2
														<input type="checkbox" name="ch_emp_priorityenquiry_level2"
														id="ch_emp_priorityenquiry_level2"
														<%=mybean.PopulateCheck(mybean.emp_priorityenquiry_level2)%> />
														</div>
														<div class="form-element1 ">Level 3
														<input type="checkbox" name="ch_emp_priorityenquiry_level3"
														id="ch_emp_priorityenquiry_level3"
														<%=mybean.PopulateCheck(mybean.emp_priorityenquiry_level3)%> />
														</div>
														<div class="form-element1 ">Level 4
														<input type="checkbox" name="ch_emp_priorityenquiry_level4"
														id="ch_emp_priorityenquiry_level4"
														<%=mybean.PopulateCheck(mybean.emp_priorityenquiry_level4)%> />
														</div>
														<div class="form-element1 ">Level 5
														<input type="checkbox" name="ch_emp_priorityenquiry_level5"
														id="ch_emp_priorityenquiry_level5"
														<%=mybean.PopulateCheck(mybean.emp_priorityenquiry_level5)%> />
														</div>
														</td>
												</tr>
												<tr>
													<tr>
														<td align="right">CRM Follow-up:&nbsp;</td>
														<td colspan="3">
														<div class="form-element1 ">Level 1
														<input type="checkbox" name="ch_emp_prioritycrmfollowup_level1"
															id="ch_emp_prioritycrmfollowup_level1"
															<%=mybean .PopulateCheck(mybean.emp_prioritycrmfollowup_level1)%> />
															</div>
															<div class="form-element1 ">Level 2
															<input type="checkbox" name="ch_emp_prioritycrmfollowup_level2"
															id="ch_emp_prioritycrmfollowup_level2"
															<%=mybean .PopulateCheck(mybean.emp_prioritycrmfollowup_level2)%> />
															</div>
															<div class="form-element1 ">Level 3
															<input type="checkbox" name="ch_emp_prioritycrmfollowup_level3"
															id="ch_emp_prioritycrmfollowup_level3"
															<%=mybean .PopulateCheck(mybean.emp_prioritycrmfollowup_level3)%> />
															</div>
															<div class="form-element1 ">Level 4
															<input type="checkbox" name="ch_emp_prioritycrmfollowup_level4"
															id="ch_emp_prioritycrmfollowup_level4"
															<%=mybean .PopulateCheck(mybean.emp_prioritycrmfollowup_level4)%> />
															</div>
															<div class="form-element1 ">Level 5
															<input type="checkbox" name="ch_emp_prioritycrmfollowup_level5"
															id="ch_emp_prioritycrmfollowup_level5"
															<%=mybean .PopulateCheck(mybean.emp_prioritycrmfollowup_level5)%> />
															</div>
													</td>
													</tr>
													<tr>
														<td align="right">Balance:&nbsp;</td>
														<td colspan="3"><div class="form-element1 ">Level 1
														<input name="ch_emp_prioritybalance_level1" type="checkbox"
															id="ch_emp_prioritybalance_level1"
															<%=mybean.PopulateCheck(mybean.emp_prioritybalance_level1)%> />
															</div><div class="form-element1 ">Level 2
															<input name="ch_emp_prioritybalance_level2"
															type="checkbox" id="ch_emp_prioritybalance_level2"
															<%=mybean.PopulateCheck(mybean.emp_prioritybalance_level2)%> />
															</div><div class="form-element1 ">Level 3
															<input name="ch_emp_prioritybalance_level3"
															type="checkbox" id="ch_emp_prioritybalance_level3"
															<%=mybean.PopulateCheck(mybean.emp_prioritybalance_level3)%> />
															</div><div class="form-element1 ">	Level 4
															<input name="ch_emp_prioritybalance_level4"
															type="checkbox" id="ch_emp_prioritybalance_level4"
															<%=mybean.PopulateCheck(mybean.emp_prioritybalance_level4)%> />
															</div><div class="form-element1 ">	Level 5
															<input name="ch_emp_prioritybalance_level5"
															type="checkbox" id="ch_emp_prioritybalance_level5"
															<%=mybean.PopulateCheck(mybean.emp_prioritybalance_level5)%> />
														</div></td>
													</tr>
													<%
														if (mybeanheader.autoservice == 1) {
													%>
													<tr>
														<td align="right">Call:&nbsp;</td>
														<td colspan="3"><div class="form-element1 ">Level 1
														<input name="ch_emp_prioritycall_level1" type="checkbox"
															id="ch_emp_prioritycall_level1"
															<%=mybean.PopulateCheck(mybean.emp_prioritycall_level1)%> />
															</div><div class="form-element1 ">Level 2
															<input name="ch_emp_prioritycall_level2" type="checkbox"
															id="ch_emp_prioritycall_level2"
															<%=mybean.PopulateCheck(mybean.emp_prioritycall_level2)%> />
															</div><div class="form-element1 ">Level 3
															<input name="ch_emp_prioritycall_level3" type="checkbox"
															id="ch_emp_prioritycall_level3"
															<%=mybean.PopulateCheck(mybean.emp_prioritycall_level3)%> />
															</div><div class="form-element1 ">	Level 4
															<input name="ch_emp_prioritycall_level4" type="checkbox"
															id="ch_emp_prioritycall_level14"
															<%=mybean.PopulateCheck(mybean.emp_prioritycall_level4)%> />
															</div><div class="form-element1 ">Level 5
															<input name="ch_emp_prioritycall_level5" type="checkbox"
															id="ch_emp_prioritycall_level5"
															<%=mybean.PopulateCheck(mybean.emp_prioritycall_level5)%> />
															</div></td>
													</tr>
													<tr>
														<td align="right">Job Card:&nbsp;</td>
														<td colspan="3"><div class="form-element1 ">Level 1
														<input name="ch_emp_priorityjc_level1" type="checkbox"
															id="ch_emp_priorityjc_level1"
															<%=mybean.PopulateCheck(mybean.emp_priorityjc_level1)%> />
															</div><div class="form-element1 ">Level 2
															<input name="ch_emp_priorityjc_level2" type="checkbox"
															id="ch_emp_priorityjc_level2"
															<%=mybean.PopulateCheck(mybean.emp_priorityjc_level2)%> />
															</div><div class="form-element1 ">Level 3
															<input name="ch_emp_priorityjc_level3" type="checkbox"
															id="ch_emp_priorityjc_level3"
															<%=mybean.PopulateCheck(mybean.emp_priorityjc_level3)%> />
															</div><div class="form-element1 ">Level 4
															<input name="ch_emp_priorityjc_level4" type="checkbox"
															id="ch_emp_priorityjc_level4"
															<%=mybean.PopulateCheck(mybean.emp_priorityjc_level4)%> />
															</div><div class="form-element1 ">Level 5
															<input name="ch_emp_priorityjc_level5" type="checkbox"
															id="ch_emp_priorityjc_level5"
															<%=mybean.PopulateCheck(mybean.emp_priorityjc_level5)%> />
															</div></td>
													</tr>
													<tr>
														<td align="right">Ticket:&nbsp;</td>
														<td colspan="3">
														<div class="form-element1 ">Level 1
														<input type="checkbox" name="ch_emp_priorityticket_level1"
															id="ch_emp_priorityticket_level1"
															<%=mybean .PopulateCheck(mybean.emp_priorityticket_level1)%> />
															</div><div class="form-element1 ">Level 2
															<input type="checkbox" name="ch_emp_priorityticket_level2"
															id="ch_emp_priorityticket_level2"
															<%=mybean .PopulateCheck(mybean.emp_priorityticket_level2)%> />
															</div><div class="form-element1 ">Level 3
															<input type="checkbox" name="ch_emp_priorityticket_level3"
															id="ch_emp_priorityticket_level3"
															<%=mybean .PopulateCheck(mybean.emp_priorityticket_level3)%> />
															</div><div class="form-element1 ">Level 4
															<input type="checkbox" name="ch_emp_priorityticket_level4"
															id="ch_emp_priorityticket_level4"
															<%=mybean .PopulateCheck(mybean.emp_priorityticket_level4)%> />
															</div><div class="form-element1 ">Level 5
															<input type="checkbox" name="ch_emp_priorityticket_level5"
															id="ch_emp_priorityticket_level5"
															<%=mybean .PopulateCheck(mybean.emp_priorityticket_level5)%> />
															</div></td>
													</tr>
													<%
														}
													%>
												
											</table>

										</div>
									</div>
								</div>
								<!-- 			Priority END -->
								<!-- START SEO-->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">SOE</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element12 form-element">
												<!-- 						
														<label class="control-label col-md-4"></label> -->
													<div class="form-element5">
														<select name="exe_soe" size="15" multiple="multiple"
															class="form-control" id="exe_soe">
															<%=mybean.PopulateSoe(mybean.comp_id)%>
														</select>
													</div>
													<div class="form-element2">
														<center>
															<br /> <br /> <br /> <br /> <br /> <input
																name="Input4" type="button" class="btn btn-success"
																onclick="JavaScript:AddItem('exe_soe','exe_soe_trans', '')"
																value="   Add &gt;&gt;" /> <br /><br> <input name="Input4"
																type="button" class="btn btn-success"
																onclick="JavaScript:DeleteItem('exe_soe_trans')"
																value="&lt;&lt; Delete" />
														</center>
													</div>
													<div class="form-element5">

														<select name="exe_soe_trans" size="15" multiple="multiple"
															class="form-control" id="exe_soe_trans">
															<%=mybean.PopulateSoeTrans(mybean.comp_id)%>
														</select>
													</div>
											</div>
											
										<div class="row">
										<div class="form-element6">
												<div>Previous Experience: </div>
												<div class="form-element6">
													<select id="dr_emp_prevexp_year" name="dr_emp_prevexp_year" 
													     class="form-control" >
													     <%=mybean.PopulateNumberDrop(0, 30, mybean.emp_prevexp_year, "Years")%>
													     </select>
												</div>
												<div class="form-element6">
													<select id="dr_emp_prevexp_month" name="dr_emp_prevexp_month" 
													     class="form-control" >
													     <%=mybean.PopulateNumberDrop(0, 11,  mybean.emp_prevexp_month, "Months")%>
													     </select>
												</div>
										</div>
										
										<div class="form-element3">
												<div> Date of Join<font color=red>*</font>: </div>
												<input name="txt_emp_date_of_join" id="txt_emp_date_of_join"
													value="<%=mybean.date_of_join%>"
													class="form-control datepicker" type="text" maxlength="10" />
										</div>
										
										<div class="form-element3">
												<div> Date of Relieve: </div>
												<input name="txt_emp_date_of_relieve"
													id="txt_emp_date_of_relieve"
													value="<%=mybean.date_of_relieve%>"
													class="form-control datepicker" type="text" maxlength="10" />
										</div>
										
										</div>
										
										<div class="form-element6">
												<div> Reason Of Leaving: </div>
												<textarea name="txt_emp_reason_of_leaving" cols="70"
													rows="4" class="form-control"
													id="txt_emp_reason_of_leaving"><%=mybean.emp_reason_of_leaving%></textarea>
										</div>
										
										<div class="form-element6">
												<div> Notes: </div>
												<textarea name="txt_emp_notes" cols="70" rows="4"
													class="form-control" id="txt_emp_notes"><%=mybean.emp_notes%></textarea>
										</div>
										
										<% if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
										<div class="form-element6">
												<label> Entry By: </label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.entry_by%>" />
										</div>
										<%}%>
										<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
										<div class="form-element6">
												<label> Entry Date: </label>
												<%=mybean.entry_date%>
												<input type="hidden" name="entry_date"
													value="<%=mybean.entry_date%>" />
										</div>
										<%}%>
										<% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
										<div class="form-element6">
												<label> Modified By: </label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by"
													value="<%=mybean.modified_by%>" />
										</div>
										<%}%>
										<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
										<div class="form-element6">
												<label> Modified Date: </label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>" />
										</div>
										<%}%>
										<div class="row"></div>
										<center>
											<%if (mybean.status.equals("Add")) {%>
											<input name="add_button" type="submit"
												class="btn btn-success" id="add_button"
												value="Add Executive"
												onClick="onPress(); return SubmitFormOnce(document.formemp, this);" />
											<input type="hidden" name="add_button1" value="yes" />
											<%} else if (mybean.status.equals("Update")) {%>
											<input type="hidden" name="update_button" value="yes" /> <input
												name="update_button" type="submit" class="btn btn-success"
												id="update_button"
												onClick="onPress(); return SubmitFormOnce(document.formemp, this);"
												value="Update Executive" /> <input name="delete_button"
												type="submit" class="btn btn-success" id="delete_button"
												onClick="return confirmdelete(this)"
												value="Delete Executive" />
											<%}%>
											<input type="hidden" name="emp_id" value="<%=mybean.emp_id%>" />
										</center>
										

									</div>
								</div>
						</div>
						<!-- END SEO-->
						</form>
					</div>
				</div>
			</div>
		</div>

	
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">

	function FormFocus() { //v1.0
		document.formemp.txt_emp_name.focus();
	}
	function DisplayBranch() {
		str1 = document.formemp.drop_emp_role_id.value;
		str2 = document.formemp.dr_emp_branch_id.value;
		if ((str1 != "1") && str2 == "0") {
			$("#emprows2").show(1000);
			$("#emprows2").show(1000);
		} else {
			$("#emprows2").hide(1000);
			$("#emprows2").hide(1000);
		}
	}
	
	function getBranch(){
		var branch_id = getoutputSelected(document.getElementById("exe_branch_trans").options);
		showHint('../portal/executive-check.jsp?multiple=yes&branch_id='
				+ GetReplace(branch_id), 'exeHint');
	}

	function LoadExecutives() {
		var branch_id = document.getElementById("dr_emp_branch_id").value;
		branch_id = branch_id+",";
		showHint('../portal/executive-check.jsp?multiple=yes&branch_id='
				+ GetReplace(branch_id), 'exeHint');
	}

	function Displayemp() {
		
		var str3 = document.formemp.chk_emp_all_exe.checked;
		
		if (str3 == "1" || str3 == "on") {
			$("#emprows1").hide(1000);
		}
		if (str3 == "0" || str3 == " ") {
			$("#emprows1").show(1000);
		}
	}
	

	function DisplayBranch1() {
		
		var str4 = document.formemp.chk_emp_all_branches.checked;
		
		if (str4 == "1" || str4 == "on") {
			$("#emprows3").hide(1000);
		}
		if (str4 == "0" || str4 == " ") {
			$("#emprows3").show(1000);
		}
	}
	

	function onPress() {
		for (i = 0; i < document.formemp.exe_team_trans.options.length; i++) {
			document.formemp.exe_team_trans.options[i].selected = true;
		}

		for (i = 0; i < document.formemp.exe_branch_trans.options.length; i++) {
			document.formemp.exe_branch_trans.options[i].selected = true;
		}

		for (i = 0; i < document.formemp.exe_soe_trans.options.length; i++) {
			document.formemp.exe_soe_trans.options[i].selected = true;
		}
	}
</script><script language="JavaScript" type="text/javascript">

	function FormFocus() { //v1.0
		document.formemp.txt_emp_name.focus();
	}
	function DisplayBranch() {
		str1 = document.formemp.drop_emp_role_id.value;
		str2 = document.formemp.dr_emp_branch_id.value;
		if ((str1 != "1") && str2 == "0") {
			$("#emprows2").show(1000);
			$("#emprows2").show(1000);
		} else {
			$("#emprows2").hide(1000);
			$("#emprows2").hide(1000);
		}
	}
	
	function getBranch(){
		var branch_id = getoutputSelected(document.getElementById("exe_branch_trans").options);
		showHint('../portal/executive-check.jsp?multiple=yes&branch_id='
				+ GetReplaceString(branch_id), 'exeHint');
	}

	function LoadExecutives() {
		var branch_id = document.getElementById("dr_emp_branch_id").value;
		branch_id = branch_id+",";
		showHint('../portal/executive-check.jsp?multiple=yes&branch_id='
				+ GetReplace(branch_id), 'exeHint');
	}

	function Displayemp() {
		
		var str3 = document.formemp.chk_emp_all_exe.checked;
		
		if (str3 == "1" || str3 == "on") {
			$("#emprows1").hide(1000);
		}
		if (str3 == "0" || str3 == " ") {
			$("#emprows1").show(1000);
		}
	}
	

	function DisplayBranch1() {
		
		var str4 = document.formemp.chk_emp_all_branches.checked;
		
		if (str4 == "1" || str4 == "on") {
			$("#emprows3").hide(1000);
		}
		if (str4 == "0" || str4 == " ") {
			$("#emprows3").show(1000);
		}
	}
	

	function onPress() {
		for (i = 0; i < document.formemp.exe_team_trans.options.length; i++) {
			document.formemp.exe_team_trans.options[i].selected = true;
		}

		for (i = 0; i < document.formemp.exe_branch_trans.options.length; i++) {
			document.formemp.exe_branch_trans.options[i].selected = true;
		}

		for (i = 0; i < document.formemp.exe_soe_trans.options.length; i++) {
			document.formemp.exe_soe_trans.options[i].selected = true;
		}
	}
</script>
</body>
</HTML>
