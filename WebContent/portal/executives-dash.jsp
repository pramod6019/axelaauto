<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Executives_Dash" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>
<link href="../assets/css/jquery_toast.min.css" rel="stylesheet" type="text/css" />

<style>
#display-line {
	display: inline-block;
	min-width: 100px;
}
/* // */
</style>

<style>
.pop {
	top: 280px;
	left: 740px;
}
.font-size{
	font-size: 17px;
}
.margin-h4{
margin :0px;
}
.left-right-border{
border-right: 1px solid #ff0000;
border-left: 1px solid #ff0000;
}
.right-border{
border-right: 1px solid #ff0000;
}
</style>
</head>
<!-- onload="FormFocus();" -->
<body  class="page-container-bg-solid page-header-menu-fixed" onLoad="DisplayBranch();DisplayBranch1();Displayemp();LoadExecutives();">
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
						<h1>Executives Dashboard</h1>
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
						<li><a href="../portal/executives-dash.jsp?exe_id=<%=mybean.exe_id%>">Executives Dash</a><b>:</b></li>
					</ul>
						<!-- END PAGE BREADCRUMBS -->
						
							<!-- 					BODY START -->
							<div>
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tabs-1" data-toggle="tab">Executive Overview</a></li>
									<%if(mybean.ReturnPerm(mybean.comp_id, "emp_role_id", request).equals("1")){ %>
										<li><a href="#tabs-2" data-toggle="tab">Executive Access</a></li>
									<%} %>
									<li onclick="LoadHistory('3');"><a href="#tabs-3" data-toggle="tab">History</a></li>
								</ul>
							<div class="tab-content">
								<div id="dialog-modal"></div>
								<div class="tab-pane in active" id="tabs-1">
									<center>
										<font color="#ff0000"><b><%=mybean.msg%></b></font>
									</center>
							<form id="formemp" name="formemp" method="post" class="form-horizontal" >	
							<input type="hidden" id="exe_id" name="exe_id" value="<%=mybean.exe_id%>" />
									<!-- 					Executive Portlet START -->
								<div class="portlet box">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none" id="branchrowshead">
											Executive Overview</div>
										</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
										<div class ="row">
											<div class="form-element6">
												<label> Name<font color=red>*</font>: </label>
													<input name="txt_emp_name" type="text" class="form-control"
														id="txt_emp_name" value="<%=mybean.emp_name%>" size="38"
														maxlength="255" onchange="SecurityCheck('txt_emp_name',this,'hint_txt_emp_name');"/>
											</div>
											
											<div class="form-element6">
												<label> Ref. No<font color=red>*</font>: </label>
													<input name="txt_emp_ref_no" type="text"
														class="form-control" id="txt_emp_ref_no"
														value="<%=mybean.emp_ref_no%>" size="20" maxlength="50" 
														onchange="SecurityCheck('txt_emp_ref_no',this,'hint_txt_emp_ref_no');"/>
											</div>
										</div>
										
										<div class ="row">	
											<div class="form-element6">
												<label> Role<font color=red>*</font>: </label>
													<select name="drop_emp_role_id" class="form-control"
														id="drop_emp_role_id" 
														onChange="DisplayBranch();SecurityCheck('drop_emp_role_id',this,'hint_drop_emp_role_id');">
														<%=mybean.PopulateRole()%>
													</select>
											</div>
											
											<div class="form-element6">
												<label> Department<font color=red>*</font>: </label>
													<select name="drop_emp_department_id" class="form-control"
														id="drop_emp_department_id" 
														onchange="SecurityCheck('drop_emp_department_id',this,'hint_drop_emp_department_id');">
														<%=mybean.PopulateDepartment()%>
													</select>
											</div>
										</div>
										
										<div class ="row">	
											<div class="form-element6">
												<label> Job Title<font color=red>*</font>: </label>
													<select name="drop_emp_jobtitle_id" class="form-control"
														id="drop_emp_jobtitle_id" 
														onchange="SecurityCheck('drop_emp_jobtitle_id',this,'hint_drop_emp_jobtitle_id');">
														<%=mybean.PopulateJobtitle()%>
													</select>
											</div>
											
											<div class="form-element6">
												<div class="form-element4 form-element">
													<label> DOB: </label>
														<select name="drop_DOBMonth" class="form-control" id="drop_DOBMonth"
														onchange="SecurityCheck('drop_DOBMonth',this,'hint_drop_DOBMonth');">
														<%=mybean.PopulateMonth()%>
														</select>
												</div>
												
												<div class="form-element4   form-element-margin">
													<select name="drop_DOBDay" class="form-control" id="drop_DOBDay"
													onchange="SecurityCheck('drop_DOBDay',this,'hint_drop_DOBDay');">
														<%=mybean.PopulateDay()%>
													</select>
												</div>
												
												<div class="form-element4 form-element form-element-margin">
												<select name="drop_DOBYear" class="form-control"
														id="drop_DOBYear" onchange="SecurityCheck('drop_DOBYear',this,'hint_drop_DOBYear');">
																		<%=mybean.PopulateYear()%>
												</select>	
												</div>
											</div>
										</div>
										
										<div class="row">
											<div class="form-element6">
												<label> Marital Status<font color=red>*</font>: </label>
													<select name="drop_emp_married" class="form-control"
														id="drop_emp_married" 
														onchange="SecurityCheck('drop_emp_married',this,'hint_drop_emp_married');">
														<%=mybean.PopulateMarried()%>
													</select>
											</div>
											
											<div class="form-element6">
												<label> Sex<font color=red>*</font>: </label>
													<select name="drop_emp_sex" class="form-control"
														id="drop_emp_sex" onchange="SecurityCheck('drop_emp_sex',this,'hint_drop_emp_sex');">
														<%=mybean.PopulateSex()%>
													</select>
											</div>
										</div>
										
										<div class ="row">
											<div class="form-element6">
												<label> Qualification: </label>
													<textarea name="txt_emp_qualification" cols="37" rows="4"
														class="form-control" id="txt_emp_qualification"
														maxlength="255" onchange="SecurityCheck('txt_emp_qualification',this,'hint_txt_emp_qualification');"
														onKeyUp="charcount('txt_emp_qualification', 'span_txt_emp_qualification','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.emp_qualification%></textarea>
													<span id="span_txt_emp_qualification">(255
														Characters)</span>
											</div>
											
											<div class="form-element6">
												<label> Certification: </label>
													<textarea name="txt_emp_certification" cols="37" rows="4"
														class="form-control" id="txt_emp_certification"
														maxlength="255" onchange="SecurityCheck('txt_emp_certification',this,'hint_txt_emp_certification');"
														onKeyUp="charcount('txt_emp_certification', 'span_txt_emp_certification','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.emp_certification%></textarea>
													<span id="span_txt_emp_certification">(255 Characters)</span>
											</div>
										</div>
										
										<div class ="row">	
											<div class="form-element6">
												<label> Phone 1: </label>
													<input name="txt_emp_phone1" type="text"
														class="form-control" id="txt_emp_phone1"
														onKeyUp="toPhone('txt_emp_phone1','Phone1')"
														value="<%=mybean.emp_phone1%>" size="20" maxlength="14" 
														onchange="SecurityCheck('txt_emp_phone1',this,'hint_txt_emp_phone1');"/>
													(91-80-33333333)
											</div>
											
											<div class="form-element6">
												<label> Phone 2: </label>
													<input name="txt_emp_phone2" type="text"
														class="form-control" id="txt_emp_phone2"
														onKeyUp="toPhone('txt_emp_phone2','Phone2')"
														value="<%=mybean.emp_phone2%>" size="20" maxlength="14"
														onchange="SecurityCheck('txt_emp_phone2',this,'hint_txt_emp_phone2');" />
													(91-80-33333333)
											</div>
										</div>
										<div class ="row">	
											<div class="form-element6">
												<label> Mobile 1: </label>
													<input name="txt_emp_mobile1" type="text"
														class="form-control" id="txt_emp_mobile1"
														onKeyUp="toPhone('txt_emp_mobile1','Mobile 1')"
														value="<%=mybean.emp_mobile1%>" size="20" maxlength="13" 
														onchange="SecurityCheck('txt_emp_mobile1',this,'hint_txt_emp_mobile1');"/>
													(91-9999999999)
											</div>
											
											<div class="form-element6">
												<label> Mobile 2: </label>
													<input name="txt_emp_mobile2" type="text"
														class="form-control" id="txt_emp_mobile2"
														onKeyUp="toPhone('txt_emp_mobile2','Mobile 2')"
														value="<%=mybean.emp_mobile2%>" size="20" maxlength="13" 
														onchange="SecurityCheck('txt_emp_mobile2',this,'hint_txt_emp_mobile2');"/>
													(91-9999999999)
											</div>
										</div>	
										
										<div class ="row">	
											<div class="form-element6">
												<label> Email 1<font color=red>*</font>: </label>
													<input name="txt_emp_email1" type="text"
														class="form-control" id="txt_emp_email1"
														value="<%=mybean.emp_email1%>" size="38" maxlength="100" 
														onchange="SecurityCheck('txt_emp_email1',this,'hint_txt_emp_email1');"/>
											</div>
											
											<div class="form-element6">
												<label> Email 2: </label>
													<input name="txt_emp_email2" type="text"
														class="form-control" id="txt_emp_email2"
														value="<%=mybean.emp_email2%>" size="38" maxlength="100" 
														onchange="SecurityCheck('txt_emp_email2',this,'hint_txt_emp_email2');"/>
											</div>
										</div>
										
										<div class ="row">	
											<div class="form-element6">
												<label> Address<font color=red>*</font>: </label>
													<textarea name="txt_emp_address" cols="37" rows="4"
														class="form-control" id="txt_emp_address" maxlength="255" onchange="SecurityCheck('txt_emp_address',this,'hint_txt_emp_address');"
														onKeyUp="charcount('txt_emp_address', 'span_txt_emp_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.emp_address%></textarea>
													<span id="span_txt_emp_address">(255 Characters)</span>
											</div>
											<div class="form-element6">
												<label> Landmark: </label>
												<textarea name="txt_emp_landmark" cols="37" rows="4"
													class="form-control" id="txt_emp_landmark" onchange="SecurityCheck('txt_emp_landmark',this,'hint_txt_emp_landmark');"
													onkeyup="charcount('txt_emp_landmark', 'span_txt_emp_landmark','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.emp_landmark%></textarea>
												<span id="span_txt_emp_landmark">(255 Characters)</span>
											</div>
										</div>	
											
										<div class ="row">	
											<div class="form-element6">
												<label> State<font color=red>*</font>: </label>
												<input name="txt_emp_state" type="text" class="form-control" id="txt_emp_state"
													value="<%=mybean.emp_state%>" size="30" maxlength="255" 
													onchange="SecurityCheck('txt_emp_state',this,'hint_txt_emp_state');"/>
											</div>
											
											<div class="form-element6">
												<label> City<font color=red>*</font>: </label>
												<input name="txt_emp_city" type="text" class="form-control"
													value="<%=mybean.emp_city%>" size="30" maxlength="255"
													onchange="SecurityCheck('txt_emp_city',this,'hint_txt_emp_city');" />
											</div>
										</div>	
										
										<div class ="row">	
											<div class="form-element6">
												<label> Pin/Zip<font color=red>*</font>: </label>
												<input name="txt_emp_pin" type="text" class="form-control"
													maxlength="6" id="txt_emp_pin" onKeyUp="toInteger('txt_emp_pin')"
													value="<%=mybean.emp_pin%>" size="12" maxlength="10" 
													onchange="SecurityCheck('txt_emp_pin',this,'hint_txt_emp_pin');"/>
											</div>
											
											<div class="form-element6">
												<label> Branch<font color=red>*</font>: </label>
												<select name="dr_emp_branch_id" class="form-control" id="dr_emp_branch_id"
													onChange="DisplayBranch();LoadExecutives();SecurityCheck('dr_emp_branch_id',this,'hint_dr_emp_branch_id');">
													<%=mybean.PopulateBranch()%>
												</select>
											</div>
										</div>	
											
										<div class="row">
											<div class="form-element6">
												<label> Weekly Off: </label>
												<select name="dr_weeklyoff_id" class="form-control"
													id="dr_weeklyoff_id" onchange="SecurityCheck('dr_weeklyoff_id',this,'hint_dr_weeklyoff_id');">
													<%=mybean.PopulateWeeklyOff()%>
												</select>
											</div>
											
											<div class="form-element6">
												<label>Password <font color=red>*</font>: </label>
													<input class="form-control" type="text" name="txt_emp_upass"
														id="txt_emp_upass" value="<%=mybean.emp_upass%>" maxlength="20"
														onchange="SecurityCheck('txt_emp_upass',this,'hint_txt_emp_upass');"> <span>(Combine
														8 to 20 letters and/or numbers to make your new password.)</span>
											</div>
										</div>
										
											<div class ="row">
												<div class="form-element3 ">
													<label> Web Access:</label>
													<input type="checkbox" id="ch_emp_access_web" name="ch_emp_access_web"
														onchange="SecurityCheck2('Web Access','ch_emp_access_web',this,'hint_ch_emp_access_web');"
															<%=mybean.PopulateCheck(mybean.emp_mis_access)%> />
												</div>
												<div class="form-element3">
													<label> App Access: </label>
													<input id="ch_emp_access_app" type="checkbox" name="ch_emp_access_app"
														onchange="SecurityCheck2('App Access','ch_emp_access_app',this,'hint_ch_emp_access_app');"
														<%=mybean.PopulateCheck(mybean.emp_access_app)%> />
												</div>
												<div class="form-element3">
													<label> MIS:</label>
													<input type="checkbox" id="ch_emp_mis_access" name="ch_emp_mis_access"
														onchange="SecurityCheck2('MIS','ch_emp_mis_access',this,'hint_ch_emp_mis_access');"
															<%=mybean.PopulateCheck(mybean.emp_mis_access)%> />
													</div>
												
												<div class="form-element3">
													<label> Export:</label>
													<input type="checkbox" id="ch_emp_export_access" name="ch_emp_export_access"
														onchange="SecurityCheck2('Export','ch_emp_export_access',this,'hint_ch_emp_export_access');"
														<%=mybean.PopulateCheck(mybean.emp_export_access)%> />
												</div>
										</div>
										
										<div class ="row">	
											<div class="form-element3">
												<label> Report: </label>
												<input type="checkbox" id="ch_emp_report_access" name="ch_emp_report_access"
													onchange="SecurityCheck2('Report','ch_emp_report_access',this,'hint_ch_emp_report_access');"
														<%=mybean.PopulateCheck(mybean.emp_report_access)%> />
											</div>
											<div class="form-element3">
												<label> Copy: </label>
												<input type="checkbox" id="ch_emp_copy_access" name="ch_emp_copy_access"
													onchange="SecurityCheck2('Copy','ch_emp_copy_access',this,'hint_ch_emp_copy_access');"
														<%=mybean.PopulateCheck(mybean.emp_copy_access)%> />
											</div>
											<div class="form-element3">
												<label> Daily Status Report: </label>
													<input type="checkbox" id="ch_emp_dailystatus_report" name="ch_emp_dailystatus_report"
													onchange="SecurityCheck2('Daily Status Report','ch_emp_dailystatus_report',this,'hint_ch_emp_dailystatus_report');"
														<%=mybean.PopulateCheck(mybean.emp_dailystatus_report)%>>
											</div>
											<div class="form-element3">
												<label> Active: </label>
													<input id="ch_emp_active" name="ch_emp_active" type="checkbox"
													onchange="SecurityCheck2('Active','ch_emp_active',this,'hint_ch_emp_active');"
														<%=mybean.PopulateCheck(mybean.emp_active)%> />
											</div>
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
													onclick="DisplayBranch1();DeleteItem('exe_branch_trans');HideAllCheckHint('chk_emp_all_branches');"
													onchange="SecurityCheck2('All Branches','chk_emp_all_branches',this,'hint_chk_emp_all_branches');"/>
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
																onclick="JavaScript:AddItem('exe_branch','exe_branch_trans', '');onPress();getBranch();SecurityCheck('exe_branch_trans', this, 'hint_exe_branch_trans');"
																value="   Add &gt;&gt;" /> <br />
															<br> <input name="Input3" type="button"
																class="btn btn-success"
																onclick="JavaScript:DeleteItem('exe_branch_trans'); onPress();getBranch();SecurityCheck('exe_branch_trans', this, 'hint_exe_branch_trans');"
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
													onclick="Displayemp();DeleteItem('exe_team_trans');HideAllCheckHint('chk_emp_all_exe');"
														onchange="SecurityCheck2('All Executives','chk_emp_all_exe',this,'hint_chk_emp_all_exe');"/>
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
																onclick="JavaScript:AddItem('dr_executive','exe_team_trans', '');onPress();SecurityCheck('exe_team_trans', this, 'hint_exe_team_trans');"
																value="   Add &gt;&gt;" /> <br />
															<br />
															
															<input name="Input" type="button" class="btn btn-success"
																onclick="JavaScript:DeleteItem('exe_team_trans');onPress();SecurityCheck('exe_team_trans', this, 'hint_exe_team_trans');"
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
											<div class ="row">
												<div class="form-element2">
													<label>Module Portal:</label>
													<input name="ch_emp_module_portal" type="checkbox" id="ch_emp_module_portal"
														<%=mybean.PopulateCheck(mybean.emp_module_portal)%> 
														onchange="SecurityCheck2('Module Portal','ch_emp_module_portal',this,'hint_ch_emp_module_portal');"/>
												</div>
												<div class="form-element2">
													<label>Module Activity:</label>
													<input name="ch_emp_module_activity" type="checkbox" id="ch_emp_module_activity"
														<%=mybean.PopulateCheck(mybean.emp_module_activity)%> 
														onchange="SecurityCheck2('Module Activity','ch_emp_module_activity',this,'hint_ch_emp_module_activity');"/>
												</div>											
												<div class="form-element2">
													<label>Module Customer:</label>
													<input name="ch_emp_module_customer" type="checkbox" id="ch_emp_module_customer"
														<%=mybean.PopulateCheck(mybean.emp_module_customer)%> 
														onchange="SecurityCheck2('Module Customer','ch_emp_module_customer',this,'hint_ch_emp_module_customer');"/>
												</div>											
												<div class="form-element2">
													<label>Module Sales:</label>
													<input name="ch_emp_module_sales" type="checkbox" id="ch_emp_module_sales"
														<%=mybean.PopulateCheck(mybean.emp_module_sales)%> 
														onchange="SecurityCheck2('Module Sales','ch_emp_module_sales',this,'hint_ch_emp_module_sales');"/>
												</div>											
												<div class="form-element2">
													<label>Module Preowned:</label>
													<input name="ch_emp_module_preowned" type="checkbox" id="ch_emp_module_preowned"
														<%=mybean.PopulateCheck(mybean.emp_module_preowned)%> 
														onchange="SecurityCheck2('Module Preowned','ch_emp_module_preowned',this,'hint_ch_emp_module_preowned');"/>
												</div>											
												<div class="form-element2">
													<label>Module Service:</label>
													<input name="ch_emp_module_service" type="checkbox" id="ch_emp_module_service"
														<%=mybean.PopulateCheck(mybean.emp_module_service)%> 
														onchange="SecurityCheck2('Module Service','ch_emp_module_service',this,'hint_ch_emp_module_service');"/>
												</div>	
											</div>	
											<div class ="row">										
												<div class="form-element2">
													<label>Module Accessories:</label>
													<input name="ch_emp_module_accessories" type="checkbox" id="ch_emp_module_accessories"
														<%=mybean.PopulateCheck(mybean.emp_module_accessories)%> 
														onchange="SecurityCheck2('Module Accessories','ch_emp_module_accessories',this,'hint_ch_emp_module_accessories');"/>
												</div>
												<div class="form-element2">
													<label>Module Insurance:</label>
													<input name="ch_emp_module_insurance" type="checkbox" id="ch_emp_module_insurance"
														<%=mybean.PopulateCheck(mybean.emp_module_insurance)%> 
														onchange="SecurityCheck2('Module Insurance','ch_emp_module_insurance',this,'hint_ch_emp_module_insurance');"/>
												</div>											
												<div class="form-element2">
													<label>Module Helpdesk:</label>
													<input name="ch_emp_module_helpdesk" type="checkbox" id="ch_emp_module_helpdesk"
														<%=mybean.PopulateCheck(mybean.emp_module_helpdesk)%> 
														onchange="SecurityCheck2('Module Helpdesk','ch_emp_module_helpdesk',this,'hint_ch_emp_module_helpdesk');"/>
												</div>											
												<div class="form-element2">
													<label>Module Inventory:</label>
													<input name="ch_emp_module_inventory" type="checkbox" id="ch_emp_module_inventory"
														<%=mybean.PopulateCheck(mybean.emp_module_inventory)%> 
														onchange="SecurityCheck2('Module Inventory','ch_emp_module_inventory',this,'hint_ch_emp_module_inventory');"/>
												</div>											
												<div class="form-element2">
													<label>Module Accounting:</label>
													<input name="ch_emp_module_accounting" type="checkbox" id="ch_emp_module_accounting"
														<%=mybean.PopulateCheck(mybean.emp_module_accounting)%> 
														onchange="SecurityCheck2('Module Accounting','ch_emp_module_accounting',this,'hint_ch_emp_module_accounting');"/>
												</div>											
												<div class="form-element2">
													<label>Module Invoice:</label>
													<input name="ch_emp_module_invoice" type="checkbox" id="ch_emp_module_invoice"
														<%=mybean.PopulateCheck(mybean.emp_module_invoice)%> 
														onchange="SecurityCheck2('Module Invoice','ch_emp_module_invoice',this,'hint_ch_emp_module_invoice');"/>
												</div>			
											</div>
											<div class ="row">									
												<div class="form-element2">
													<label>Module App:</label>
													<input name="ch_emp_module_app" type="checkbox" id="ch_emp_module_app"
														<%=mybean.PopulateCheck(mybean.emp_module_app)%> 
														onchange="SecurityCheck2('Module App','ch_emp_module_app',this,'hint_ch_emp_module_app');"/>
												</div>																						
											</div> 	
											<div class ="row">
												<div class="form-element2">
													<label>Sales Executive:</label>
													<input name="ch_emp_sales" type="checkbox" id="ch_emp_sales"
													onchange="SecurityCheck2('Sales Executive','ch_emp_sales',this,'hint_ch_emp_sales');"
													<%=mybean.PopulateCheck(mybean.emp_sales)%> />
												</div>
													
												<div class="form-element2">
													<label>Close Enquiry:</label>
													<input name="ch_emp_close_enquiry" type="checkbox" id="ch_emp_close_enquiry"
														onchange="SecurityCheck2('Close Enquiry','ch_emp_close_enquiry',this,'hint_ch_emp_close_enquiry');"
														<%=mybean.PopulateCheck(mybean.emp_close_enquiry)%> />
												</div>
															
												<div class="form-element2">
													<label>Monitoring Board:</label>
													<input name="ch_emp_mtrboard" type="checkbox" id="ch_emp_mtrboard"
													onchange="SecurityCheck2('Monitoring Board','ch_emp_mtrboard',this,'hint_ch_emp_mtrboard');"
														<%=mybean.PopulateCheck(mybean.emp_mtrboard)%> />
												</div>
													
												<div class="form-element2">
													<label>Pre-Owned:</label>
													<input name="ch_emp_preowned" type="checkbox" id="ch_emp_preowned"
													onchange="SecurityCheck2('Pre-Owned','ch_emp_preowned',this,'hint_ch_emp_preowned');"
														<%=mybean.PopulateCheck(mybean.emp_preowned)%> />
												</div>
													
												<div class="form-element2">
													<label>Update Quote Price:</label>
													<input name="ch_emp_quote_priceupdate" type="checkbox" id="ch_emp_quote_priceupdate"
													onchange="SecurityCheck2('Quote Price','ch_emp_quote_priceupdate',this,'hint_ch_emp_quote_priceupdate');"
														<%=mybean.PopulateCheck(mybean.emp_quote_priceupdate)%> />
												</div>
													
												<div class="form-element2">
													<label>Update Quote Discount:</label>
													<input name="ch_emp_quote_discountupdate" type="checkbox" id="ch_emp_quote_discountupdate"
													onchange="SecurityCheck2('Quote Discount','ch_emp_quote_discountupdate',this,'hint_ch_emp_quote_discountupdate');"
														<%=mybean.PopulateCheck(mybean.emp_quote_discountupdate)%> />
												</div>
											</div>		
											
											<div class ="row">													
												<div class="form-element2">
													<label>Update SO Price:</label>
													<input name="ch_emp_so_priceupdate" type="checkbox" id="ch_emp_so_priceupdate"
													onchange="SecurityCheck2('SO Price','ch_emp_so_priceupdate',this,'hint_ch_emp_so_priceupdate');"
														<%=mybean.PopulateCheck(mybean.emp_so_priceupdate)%> />
												</div>
													
												<div class="form-element2">
													<label>Update SO Discount:</label>
													<input name="ch_emp_so_discountupdate" type="checkbox" id="ch_emp_so_discountupdate"
													onchange="SecurityCheck2('SO Discount','ch_emp_so_discountupdate',this,'hint_ch_emp_so_discountupdate');"
														<%=mybean.PopulateCheck(mybean.emp_so_discountupdate)%> />
												</div>
													
												<div class="form-element2">
													<label>Update Invoice Price:</label>
													<input name="ch_emp_invoice_priceupdate" type="checkbox" id="ch_emp_invoice_priceupdate"
													onchange="SecurityCheck2('Invoice Price','ch_emp_invoice_priceupdate',this,'hint_ch_emp_invoice_priceupdate');"
													<%=mybean.PopulateCheck(mybean.emp_invoice_priceupdate)%> />
												</div>
													
												<div class="form-element2">
													<label>Update Invoice Discount:</label>
													<input name="ch_emp_invoice_discountupdate" type="checkbox" id="ch_emp_invoice_discountupdate"
													onchange="SecurityCheck2('Invoice Discount','ch_emp_invoice_discountupdate',this,'hint_ch_emp_invoice_discountupdate');"
														<%=mybean.PopulateCheck(mybean.emp_invoice_discountupdate)%> />
												</div>
													
												<% if (mybeanheader.autoservice == 1) { %>
												
												<div class="form-element2">
													<label>Service Executive:</label>
													<input name="ch_emp_service" type="checkbox" id="ch_emp_service"
														onchange="SecurityCheck2('Service Executive','ch_emp_service',this,'hint_ch_emp_service');"
														<%=mybean.PopulateCheck(mybean.emp_service)%> />
												</div>
													
												<div class="form-element2">
													<label>Service Technician:</label>
													<input name="ch_emp_technician" type="checkbox" id="ch_emp_technician"
													onchange="SecurityCheck2('Service Technician','ch_emp_technician',this,'hint_ch_emp_technician');"
														<%=mybean.PopulateCheck(mybean.emp_technician)%> />
												</div>
											</div>	
													
											<div class ="row">		
												<div class="form-element2">
													<label>Service PSF:</label>
													<td><input name="ch_emp_service_psf" type="checkbox" id="ch_emp_service_psf"
													onchange="SecurityCheck2('Service PSF','ch_emp_service_psf',this,'hint_ch_emp_service_psf');"
													<%=mybean.PopulateCheck(mybean.emp_service_psf)%> />
												</div>
													
												<div class="form-element2">
													<label>Service PSF IACS:</label>
													<input name="ch_emp_service_psf_iacs" type="checkbox" id="ch_emp_service_psf_iacs"
													onchange="SecurityCheck2('Service PSF IACS','ch_emp_service_psf_iacs',this,'hint_ch_emp_service_psf_iacs');"
														<%=mybean.PopulateCheck(mybean.emp_service_psf_iacs)%> />
												</div>
													
												<div class="form-element2">
													<label>Service CRM:</label>
													<input name="ch_emp_crm" type="checkbox"
														id="ch_emp_crm" <%=mybean.PopulateCheck(mybean.emp_crm)%> 
														onchange="SecurityCheck2('Service CRM','ch_emp_crm',this,'hint_ch_emp_crm');"/>
												</div>
													
												<div class="form-element2">
													<label>Pick Up Driver:</label>
													<input name="ch_emp_pickup_driver" type="checkbox" id="ch_emp_pickup_driver"
													onchange="SecurityCheck2('Pick Up Driver','ch_emp_pickup_driver',this,'hint_ch_emp_pickup_driver');"
														<%=mybean.PopulateCheck(mybean.emp_pickup_driver)%> />
												</div>
													
												<div class="form-element2">
													<label>Insurance:</label>
													<input name="ch_emp_insur" type="checkbox" id="ch_emp_insur"
													onchange="SecurityCheck2('Insurance','ch_emp_insur',this,'hint_ch_emp_insur');"
														<%=mybean.PopulateCheck(mybean.emp_insur)%> />
												</div>
													
												<div class="form-element2">
													<label>Field Executive:</label>
													<input name="ch_emp_fieldinsur" type="checkbox" id="ch_emp_fieldinsur"
													onchange="SecurityCheck2('Field Executive','ch_emp_fieldinsur',this,'hint_ch_emp_fieldinsur');"
														<%=mybean.PopulateCheck(mybean.emp_fieldinsur)%> />
												</div>
											</div>	
											
											<div class ="row">		
												<div class="form-element2">
													<label>Update Job Card Price:</label>
													<input name="ch_emp_jc_priceupdate" type="checkbox" id="ch_emp_jc_priceupdate"
													onchange="SecurityCheck2('Job Card Price','ch_emp_jc_priceupdate',this,'hint_ch_emp_jc_priceupdate');"
														<%=mybean.PopulateCheck(mybean.emp_jc_priceupdate)%> />
												</div>
													
												<div class="form-element2">
													<label>Update Job Card Discount:</label>
													<input name="ch_emp_jc_discountupdate" type="checkbox" id="ch_emp_jc_discountupdate"
													onchange="SecurityCheck2('Job Card Discount','ch_emp_jc_discountupdate',this,'hint_ch_emp_jc_discountupdate');"
														<%=mybean.PopulateCheck(mybean.emp_jc_discountupdate)%> />
												</div>
													
												<div class="form-element2">
													<label>Ticket Owner:</label>
													<input name="ch_emp_ticket_owner" type="checkbox" id="ch_emp_ticket_owner"
													onchange="SecurityCheck2('Ticket Owner','ch_emp_ticket_owner',this,'hint_ch_emp_ticket_owner');"
														<%=mybean.PopulateCheck(mybean.emp_ticket_owner)%> />
												</div>
													
												<div class="form-element2">
													<label>Ticket Close:</label>
													<input name="ch_emp_ticket_close" type="checkbox" id="ch_emp_ticket_close"
													onchange="SecurityCheck2('Ticket Close','ch_emp_ticket_close',this,'hint_ch_emp_ticket_close');"
														<%=mybean.PopulateCheck(mybean.emp_ticket_close)%> />
												</div>
													
												<% } %>
													
												<div class="form-element2">
													<label>Click to Call:</label>
													<input type="checkbox" name="ch_emp_clicktocall" id="ch_emp_clicktocall"
														<%=mybean.PopulateCheck(mybean.emp_clicktocall)%> 
														onchange="SecurityCheck2('Click to Call','ch_emp_clicktocall',this,'hint_ch_emp_clicktocall');"/>
												</div>
															
												<div class="form-element2">
													<label>Emax PM:</label>
													<input type="checkbox" name="ch_emp_emaxpm" id= "ch_emp_emaxpm"
														<%=mybean.PopulateCheck(mybean.emp_emaxpm)%> 
														onchange="SecurityCheck2('Emax PM','ch_emp_emaxpm',this,'hint_ch_emp_emaxpm');"/>
												</div>
											</div>	
											
											<div class ="row">	
												<div class="form-element2">
													<label>Stock Ageing:</label>
													<input name="ch_emp_stock_ageing" type="checkbox" id="ch_emp_stock_ageing"
														<%=mybean.PopulateCheck(mybean.emp_stock_ageing)%> 
														onchange="SecurityCheck2('Stock Ageing','ch_emp_stock_ageing',this,'hint_ch_emp_stock_ageing');"/>
												</div>
											</div>
											
										<div class ="row">	
											<div class="form-element6">
													<label>Caller ID:</label>					
													<input name="txt_emp_caller_id" type="text"
													class="form-control" id="txt_emp_caller_id"
													onKeyUp="toInteger('txt_emp_caller_id')"
													value="<%=mybean.emp_callerid%>" size="22"
													maxlength="12" onchange="SecurityCheck('txt_emp_caller_id',this,'hint_txt_emp_caller_id');"/>	
											</div>		
											
											<div class="form-element6">				
													<label>Route No.:</label>
													<input name="txt_emp_routeno" type="text"
													class="form-control" id="txt_emp_routeno"
													value="<%=mybean.emp_routeno%>" size="40"
													maxlength="100" onchange="SecurityCheck('txt_emp_routeno',this,'hint_txt_emp_routeno');"/>
											</div>
										</div>
										<div class ="row">
											<div class="form-element6">		
												<label>ClickToCall Username:</label>
													<input name="txt_emp_clicktocall_username" type="text"
														class="form-control" id="txt_emp_clicktocall_username" autocomplete='new-password'
														value="<%=mybean.emp_clicktocall_username%>" size="32"
														maxlength="255" onchange="SecurityCheck('txt_emp_clicktocall_username',this,'hint_txt_emp_clicktocall_username');"/>
											</div>
											
											<div class="form-element6">
												<label>ClickToCall Password:</label>
												<input name="txt_emp_clicktocall_password" type="password"
													class="form-control" id="txt_emp_clicktocall_password" autocomplete='new-password'
													value="<%=mybean.emp_clicktocall_password%>" size="32"
													maxlength="255" onchange="SecurityCheck('txt_emp_clicktocall_password',this,'hint_txt_emp_clicktocall_password');"/>
											</div>
										</div>
										<div class ="row">
											<div class="form-element6">
													<label>ClickToCall Campaign:</label>
													<input
													name="txt_emp_clicktocall_campaign" type="text"
													class="form-control" id="txt_emp_clicktocall_campaign"
													value="<%=mybean.emp_clicktocall_campaign%>" size="32"
													maxlength="255" onchange="SecurityCheck('txt_emp_clicktocall_campaign',this,'hint_txt_emp_clicktocall_campaign');"/>
											</div>
											<div class="form-element6">
												<label>Device ID:</label>
												<input name="txt_emp_device_id" id="txt_emp_device_id"
												type="text" class="form-control"
												value="<%=mybean.emp_device_id%>" size="22"
												maxlength="20" onchange="SecurityCheck('txt_emp_device_id',this,'hint_txt_emp_device_id');"/>		
																<!----End Device ID-->
											</div>
										</div>
										<div class ="row">
											<div class="form-element6">
												<label>IP Access:</label>
												<input name="txt_emp_ip_access" id="txt_emp_ip_access"
												type="text" class="form-control"
												value="<%=mybean.emp_ip_access%>" size="70"
												maxlength="255" onchange="SecurityCheck('txt_emp_ip_access',this,'hint_txt_emp_ip_access');"/>
												(comma and space seperated for each IP Address) <br /> Eg.: 192.168.0.1, 192.168.1.6
											</div>											
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
														<%=mybean.PopulateCheck(mybean.emp_priorityactivity_level1)%>
														onchange="SecurityCheck2('Activity Level 1','ch_emp_priorityactivity_level1',this,'hint_ch_emp_priorityactivity_level1');" />
													</div>
													
													<div class="form-element1 ">Level 2
														<input type="checkbox" name="ch_emp_priorityactivity_level2"
														id="ch_emp_priorityactivity_level2"
														<%=mybean.PopulateCheck(mybean.emp_priorityactivity_level2)%> 
														onchange="SecurityCheck2('Activity Level 2','ch_emp_priorityactivity_level2',this,'hint_ch_emp_priorityactivity_level2');"/>
													</div>
													
													<div class="form-element1 ">Level 3
														<input type="checkbox" name="ch_emp_priorityactivity_level3"
														id="ch_emp_priorityactivity_level3"
														<%=mybean.PopulateCheck(mybean.emp_priorityactivity_level3)%> 
														onchange="SecurityCheck2('Activity Level 3','ch_emp_priorityactivity_level3',this,'hint_ch_emp_priorityactivity_level3');"/>
													</div>
													
													<div class="form-element1 ">Level 4
														<input type="checkbox" name="ch_emp_priorityactivity_level4"
														id="ch_emp_priorityactivity_level4"
														<%=mybean.PopulateCheck(mybean.emp_priorityactivity_level4)%> 
														onchange="SecurityCheck2('Activity Level 4','ch_emp_priorityactivity_level4',this,'hint_ch_emp_priorityactivity_level4');"/>
													</div>
													
													<div class="form-element1 ">Level 5
														<input type="checkbox" name="ch_emp_priorityactivity_level5"
														id="ch_emp_emp_priorityactivity_level5"
														<%=mybean.PopulateCheck(mybean.emp_priorityactivity_level5)%> 
														onchange="SecurityCheck2('Activity Level 5','ch_emp_priorityactivity_level5',this,'hint_ch_emp_priorityactivity_level5');"/>
													</div>
												</td>
												</tr>
												<tr>
												<td align="right">Project:&nbsp;</td>
													<td colspan="3">
													<div class="form-element1">Level 1
													<input type="checkbox" name="ch_emp_priorityproject_level1"
														id="ch_emp_priorityproject_level1"
														<%=mybean.PopulateCheck(mybean.emp_priorityproject_level1)%> 
														onchange="SecurityCheck2('Project Level 1','ch_emp_priorityproject_level1',this,'hint_ch_emp_priorityproject_level1');"/>
													</div>
													
													<div class="form-element1 ">Level 2
														<input type="checkbox" name="ch_emp_priorityproject_level2"
														id="ch_emp_priorityproject_level2"
														<%=mybean.PopulateCheck(mybean.emp_priorityproject_level2)%> 
														onchange="SecurityCheck2('Project Level 2','ch_emp_priorityproject_level2',this,'hint_ch_emp_priorityproject_level2');"/>
													</div>
													
													<div class="form-element1 ">Level 3
														<input type="checkbox" name="ch_emp_priorityproject_level3"
														id="ch_emp_priorityproject_level3"
														<%=mybean.PopulateCheck(mybean.emp_priorityproject_level3)%> 
														onchange="SecurityCheck2('Project Level 3','ch_emp_priorityproject_level3',this,'hint_ch_emp_priorityproject_level3');"/>
													</div>
													
													<div class="form-element1 ">Level 4
														<input type="checkbox" name="ch_emp_priorityproject_level4"
														id="ch_emp_priorityproject_level4"
														<%=mybean.PopulateCheck(mybean.emp_priorityproject_level4)%> 
														onchange="SecurityCheck2('Project Level 4','ch_emp_priorityproject_level4',this,'hint_ch_emp_priorityproject_level4');"/>
													</div>
													
													<div class="form-element1 ">Level 5
														<input type="checkbox" name="ch_emp_priorityproject_level5"
														id="ch_emp_priorityproject_level5"
														<%=mybean.PopulateCheck(mybean.emp_priorityproject_level5)%> 
														onchange="SecurityCheck2('Project Level 5','ch_emp_priorityproject_level5',this,'hint_ch_emp_priorityproject_level5');"/>
													</div>
												</td>
												</tr>
												<tr>
												<td align="right">Task:&nbsp;</td>
													<td colspan="3">
													<div class="form-element1 ">Level 1
														<input type="checkbox" name="ch_emp_prioritytask_level1"
															id="ch_emp_prioritytask_level1"
															<%=mybean.PopulateCheck(mybean.emp_prioritytask_level1)%> 
															onchange="SecurityCheck2('Task Level 1','ch_emp_prioritytask_level1',this,'hint_ch_emp_prioritytask_level1');"/>
													</div>
													
													<div class="form-element1 ">Level 2
														<input type="checkbox" name="ch_emp_prioritytask_level2"
														id="ch_emp_prioritytask_level2"
														<%=mybean.PopulateCheck(mybean.emp_prioritytask_level2)%> 
														onchange="SecurityCheck2('Task Level 2','ch_emp_prioritytask_level2',this,'hint_ch_emp_prioritytask_level2');"/>
													</div>
													
													<div class="form-element1 ">Level 3
														<input type="checkbox" name="ch_emp_prioritytask_level3"
														id="ch_emp_prioritytask_level3"
														<%=mybean.PopulateCheck(mybean.emp_prioritytask_level3)%> 
														onchange="SecurityCheck2('Task Level 3','ch_emp_prioritytask_level3',this,'hint_ch_emp_prioritytask_level3');"/>
													</div>
													
													<div class="form-element1 ">Level 4
														<input type="checkbox" name="ch_emp_prioritytask_level4"
														id="ch_emp_prioritytask_level4"
														<%=mybean.PopulateCheck(mybean.emp_prioritytask_level4)%> 
														onchange="SecurityCheck2('Task Level 4','ch_emp_prioritytask_level4',this,'hint_ch_emp_prioritytask_level4');"/>
													</div>
													
													<div class="form-element1 ">Level 5
														<input type="checkbox" name="ch_emp_prioritytask_level5"
														id="ch_emp_prioritytask_level5"
														<%=mybean.PopulateCheck(mybean.emp_prioritytask_level5)%> 
														onchange="SecurityCheck2('Task Level 5','ch_emp_prioritytask_level5',this,'hint_ch_emp_prioritytask_level5');"/>
													</div>
													</td>
												</tr>
												<tr>
												<td align="right" nowrap>Enquiry Follow-up:&nbsp;</td>
													<td colspan="3">
														<div class="form-element1 ">Level 1
															<input type="checkbox" name="ch_emp_priorityenquiryfollowup_level1"
															id="ch_emp_priorityenquiryfollowup_level1"
															<%=mybean .PopulateCheck(mybean.emp_priorityenquiryfollowup_level1)%> 
															onchange="SecurityCheck2('Enquiry Follow-up Level 1','ch_emp_priorityenquiryfollowup_level1',this,'hint_ch_emp_priorityenquiryfollowup_level1');"/>
														</div>
														<div class="form-element1 ">Level 2
															<input type="checkbox" name="ch_emp_priorityenquiryfollowup_level2"
															id="ch_emp_priorityenquiryfollowup_level2"
															<%=mybean .PopulateCheck(mybean.emp_priorityenquiryfollowup_level2)%> 
															onchange="SecurityCheck2('Enquiry Follow-up Level 2','ch_emp_priorityenquiryfollowup_level2',this,'hint_ch_emp_priorityenquiryfollowup_level2');"/>
														</div>
														<div class="form-element1 ">Level 3
															<input type="checkbox" name="ch_emp_priorityenquiryfollowup_level3"
															id="ch_emp_priorityenquiryfollowup_level3"
															<%=mybean .PopulateCheck(mybean.emp_priorityenquiryfollowup_level3)%>
															onchange="SecurityCheck2('Enquiry Follow-up Level 3','ch_emp_priorityenquiryfollowup_level3',this,'hint_ch_emp_priorityenquiryfollowup_level3');"/>
														</div>
														<div class="form-element1 ">Level 4
															<input type="checkbox" name="ch_emp_priorityenquiryfollowup_level4"
															id="ch_emp_priorityenquiryfollowup_level4"
															<%=mybean .PopulateCheck(mybean.emp_priorityenquiryfollowup_level4)%> 
															onchange="SecurityCheck2('Enquiry Follow-up Level 4','ch_emp_priorityenquiryfollowup_level4',this,'hint_ch_emp_priorityenquiryfollowup_level4');"/>
														</div>
														<div class="form-element1 ">Level 5 
															<input type="checkbox" name="ch_emp_priorityenquiryfollowup_level5"
															id="ch_emp_priorityenquiryfollowup_level5"
															<%=mybean .PopulateCheck(mybean.emp_priorityenquiryfollowup_level5)%> 
															onchange="SecurityCheck2('Enquiry Follow-up Level 5','ch_emp_priorityenquiryfollowup_level5',this,'hint_ch_emp_priorityenquiryfollowup_level5');"/>
														</div>
													</td>
												</tr>
												<tr>
												<td align="right">Enquiry:&nbsp;</td>
													<td colspan="3">
													<div class="form-element1 ">Level 1
														<input type="checkbox" name="ch_emp_priorityenquiry_level1"
														id="ch_emp_priorityenquiry_level1"
														<%=mybean.PopulateCheck(mybean.emp_priorityenquiry_level1)%> 
														onchange="SecurityCheck2('Enquiry Level 1','ch_emp_priorityenquiry_level1',this,'hint_ch_emp_priorityenquiry_level1');"/>
													</div>
													
													<div class="form-element1 ">Level 2
														<input type="checkbox" name="ch_emp_priorityenquiry_level2"
														id="ch_emp_priorityenquiry_level2"
														<%=mybean.PopulateCheck(mybean.emp_priorityenquiry_level2)%> 
														onchange="SecurityCheck2('Enquiry Level 2','ch_emp_priorityenquiry_level2',this,'hint_ch_emp_priorityenquiry_level2');"/>
													</div>
													
													<div class="form-element1 ">Level 3
														<input type="checkbox" name="ch_emp_priorityenquiry_level3"
														id="ch_emp_priorityenquiry_level3"
														<%=mybean.PopulateCheck(mybean.emp_priorityenquiry_level3)%> 
														onchange="SecurityCheck2('Enquiry Level 3','ch_emp_priorityenquiry_level3',this,'hint_ch_emp_priorityenquiry_level3');"/>
													</div>
													
													<div class="form-element1 ">Level 4
														<input type="checkbox" name="ch_emp_priorityenquiry_level4"
														id="ch_emp_priorityenquiry_level4"
														<%=mybean.PopulateCheck(mybean.emp_priorityenquiry_level4)%> 
														onchange="SecurityCheck2('Enquiry Level 4','ch_emp_priorityenquiry_level4',this,'hint_ch_emp_priorityenquiry_level4');"/>
													</div>
													
													<div class="form-element1 ">Level 5
														<input type="checkbox" name="ch_emp_priorityenquiry_level5"
														id="ch_emp_priorityenquiry_level5"
														<%=mybean.PopulateCheck(mybean.emp_priorityenquiry_level5)%> 
														onchange="SecurityCheck2('Enquiry Level 5','ch_emp_priorityenquiry_level5',this,'hint_ch_emp_priorityenquiry_level5');"/>
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
															<%=mybean .PopulateCheck(mybean.emp_prioritycrmfollowup_level1)%> 
															onchange="SecurityCheck2('CRM Follow-up Level 1','ch_emp_prioritycrmfollowup_level1',this,'hint_ch_emp_prioritycrmfollowup_level1');"/>
														</div>
														
														<div class="form-element1 ">Level 2
															<input type="checkbox" name="ch_emp_prioritycrmfollowup_level2"
															id="ch_emp_prioritycrmfollowup_level2"
															<%=mybean .PopulateCheck(mybean.emp_prioritycrmfollowup_level2)%> 
															onchange="SecurityCheck2('CRM Follow-up Level 2','ch_emp_prioritycrmfollowup_level2',this,'hint_ch_emp_prioritycrmfollowup_level2');"/>
														</div>
														
														<div class="form-element1 ">Level 3
															<input type="checkbox" name="ch_emp_prioritycrmfollowup_level3"
															id="ch_emp_prioritycrmfollowup_level3"
															<%=mybean .PopulateCheck(mybean.emp_prioritycrmfollowup_level3)%> 
															onchange="SecurityCheck2('CRM Follow-up Level 3','ch_emp_prioritycrmfollowup_level3',this,'hint_ch_emp_prioritycrmfollowup_level3');"/>
														</div>
														
														<div class="form-element1 ">Level 4
															<input type="checkbox" name="ch_emp_prioritycrmfollowup_level4"
															id="ch_emp_prioritycrmfollowup_level4"
															<%=mybean .PopulateCheck(mybean.emp_prioritycrmfollowup_level4)%> 
															onchange="SecurityCheck2('CRM Follow-up Level 4','ch_emp_prioritycrmfollowup_level4',this,'hint_ch_emp_prioritycrmfollowup_level4');"/>
														</div>
														
														<div class="form-element1 ">Level 5
															<input type="checkbox" name="ch_emp_prioritycrmfollowup_level5"
															id="ch_emp_prioritycrmfollowup_level5"
															<%=mybean .PopulateCheck(mybean.emp_prioritycrmfollowup_level5)%> 
															onchange="SecurityCheck2('CRM Follow-up Level 5','ch_emp_prioritycrmfollowup_level5',this,'hint_ch_emp_prioritycrmfollowup_level5');"/>
														</div>
													</td>
													</tr>
													<tr>
													<td align="right">Balance:&nbsp;</td>
														<td colspan="3">
														<div class="form-element1 ">Level 1
															<input name="ch_emp_prioritybalance_level1" type="checkbox"
															id="ch_emp_prioritybalance_level1"
															<%=mybean.PopulateCheck(mybean.emp_prioritybalance_level1)%> 
															onchange="SecurityCheck2('Balance Level 1','ch_emp_prioritybalance_level1',this,'hint_ch_emp_prioritybalance_level1');"/>
														</div>
														
														<div class="form-element1 ">Level 2
															<input name="ch_emp_prioritybalance_level2"
															type="checkbox" id="ch_emp_prioritybalance_level2"
															<%=mybean.PopulateCheck(mybean.emp_prioritybalance_level2)%> 
															onchange="SecurityCheck2('Balance Level 2','ch_emp_prioritybalance_level2',this,'hint_ch_emp_prioritybalance_level2');"/>
														</div>
														
														<div class="form-element1 ">Level 3
															<input name="ch_emp_prioritybalance_level3"
															type="checkbox" id="ch_emp_prioritybalance_level3"
															<%=mybean.PopulateCheck(mybean.emp_prioritybalance_level3)%>
															onchange="SecurityCheck2('Balance Level 3','ch_emp_prioritybalance_level3',this,'hint_ch_emp_prioritybalance_level3');" />
														</div>
														
														<div class="form-element1 ">	Level 4
															<input name="ch_emp_prioritybalance_level4"
															type="checkbox" id="ch_emp_prioritybalance_level4"
															<%=mybean.PopulateCheck(mybean.emp_prioritybalance_level4)%> 
															onchange="SecurityCheck2('Balance Level 4','ch_emp_prioritybalance_level4',this,'hint_ch_emp_prioritybalance_level4');"/>
														</div>
														
														<div class="form-element1 ">	Level 5
															<input name="ch_emp_prioritybalance_level5"
															type="checkbox" id="ch_emp_prioritybalance_level5"
															<%=mybean.PopulateCheck(mybean.emp_prioritybalance_level5)%> 
															onchange="SecurityCheck2('Balance Level 5','ch_emp_prioritybalance_level5',this,'hint_ch_emp_prioritybalance_level5');"/>
														</div>
														</td>
													</tr>
													<%
														if (mybeanheader.autoservice == 1) {
													%>
													<tr>
													<td align="right">Call:&nbsp;</td>
														<td colspan="3">
														<div class="form-element1 ">Level 1
														<input name="ch_emp_prioritycall_level1" type="checkbox"
															id="ch_emp_prioritycall_level1"
															<%=mybean.PopulateCheck(mybean.emp_prioritycall_level1)%> 
															onchange="SecurityCheck2('Call Level 1','ch_emp_prioritycall_level1',this,'hint_ch_emp_prioritycall_level1');"/>
														</div>
														<div class="form-element1 ">Level 2
															<input name="ch_emp_prioritycall_level2" type="checkbox"
															id="ch_emp_prioritycall_level2"
															<%=mybean.PopulateCheck(mybean.emp_prioritycall_level2)%> 
															onchange="SecurityCheck2('Call Level 2','ch_emp_prioritycall_level2',this,'hint_ch_emp_prioritycall_level2');"/>
														</div>
														<div class="form-element1 ">Level 3
															<input name="ch_emp_prioritycall_level3" type="checkbox"
															id="ch_emp_prioritycall_level3"
															<%=mybean.PopulateCheck(mybean.emp_prioritycall_level3)%> 
															onchange="SecurityCheck2('Call Level 3','ch_emp_prioritycall_level3',this,'hint_ch_emp_prioritycall_level3');"/>
														</div>
														<div class="form-element1 ">	Level 4
															<input name="ch_emp_prioritycall_level4" type="checkbox"
															id="ch_emp_prioritycall_level14"
															<%=mybean.PopulateCheck(mybean.emp_prioritycall_level4)%> 
															onchange="SecurityCheck2('Call Level 4','ch_emp_prioritycall_level4',this,'hint_ch_emp_prioritycall_level4');"/>
														</div>
														<div class="form-element1 ">Level 5
															<input name="ch_emp_prioritycall_level5" type="checkbox"
															id="ch_emp_prioritycall_level5"
															<%=mybean.PopulateCheck(mybean.emp_prioritycall_level5)%> 
															onchange="SecurityCheck2('Call Level 5','ch_emp_prioritycall_level5',this,'hint_ch_emp_prioritycall_level5');"/>
														</div>
														</td>
													</tr>
													<tr>
													<td align="right">Job Card:&nbsp;</td>
														<td colspan="3">
														<div class="form-element1 ">Level 1
															<input name="ch_emp_priorityjc_level1" type="checkbox"
															id="ch_emp_priorityjc_level1"
															<%=mybean.PopulateCheck(mybean.emp_priorityjc_level1)%> 
															onchange="SecurityCheck2('Job Card Level 1','ch_emp_priorityjc_level1',this,'hint_ch_emp_priorityjc_level1');"/>
														</div>
														<div class="form-element1 ">Level 2
															<input name="ch_emp_priorityjc_level2" type="checkbox"
															id="ch_emp_priorityjc_level2"
															<%=mybean.PopulateCheck(mybean.emp_priorityjc_level2)%> 
															onchange="SecurityCheck2('Job Card Level 2','ch_emp_priorityjc_level2',this,'hint_ch_emp_priorityjc_level2');"/>
														</div>
														<div class="form-element1 ">Level 3
															<input name="ch_emp_priorityjc_level3" type="checkbox"
															id="ch_emp_priorityjc_level3"
															<%=mybean.PopulateCheck(mybean.emp_priorityjc_level3)%> 
															onchange="SecurityCheck2('Job Card Level 3','ch_emp_priorityjc_level3',this,'hint_ch_emp_priorityjc_level3');"/>
														</div>
														<div class="form-element1 ">Level 4
															<input name="ch_emp_priorityjc_level4" type="checkbox"
															id="ch_emp_priorityjc_level4"
															<%=mybean.PopulateCheck(mybean.emp_priorityjc_level4)%> 
															onchange="SecurityCheck2('Job Card Level 4','ch_emp_priorityjc_level4',this,'hint_ch_emp_priorityjc_level4');"/>
														</div>
														<div class="form-element1 ">Level 5
															<input name="ch_emp_priorityjc_level5" type="checkbox"
															id="ch_emp_priorityjc_level5"
															<%=mybean.PopulateCheck(mybean.emp_priorityjc_level5)%> 
															onchange="SecurityCheck2('Job Card Level 5','ch_emp_priorityjc_level5',this,'hint_ch_emp_priorityjc_level5');"/>
														</div>
														</td>
													</tr>
													<tr>
													<td align="right">Ticket:&nbsp;</td>
														<td colspan="3">
														<div class="form-element1 ">Level 1
															<input type="checkbox" name="ch_emp_priorityticket_level1"
															id="ch_emp_priorityticket_level1"
															<%=mybean .PopulateCheck(mybean.emp_priorityticket_level1)%> 
															onchange="SecurityCheck2('Ticket Level 1','ch_emp_priorityticket_level1',this,'hint_ch_emp_priorityticket_level1');"/>
														</div>
														<div class="form-element1 ">Level 2
															<input type="checkbox" name="ch_emp_priorityticket_level2"
															id="ch_emp_priorityticket_level2"
															<%=mybean .PopulateCheck(mybean.emp_priorityticket_level2)%> 
															onchange="SecurityCheck2('Ticket Level 2','ch_emp_priorityticket_level2',this,'hint_ch_emp_priorityticket_level2');"/>
														</div>
														<div class="form-element1 ">Level 3
															<input type="checkbox" name="ch_emp_priorityticket_level3"
															id="ch_emp_priorityticket_level3"
															<%=mybean .PopulateCheck(mybean.emp_priorityticket_level3)%> 
															onchange="SecurityCheck2('Ticket Level 3','ch_emp_priorityticket_level3',this,'hint_ch_emp_priorityticket_level3');"/>
														</div>
														<div class="form-element1 ">Level 4
															<input type="checkbox" name="ch_emp_priorityticket_level4"
															id="ch_emp_priorityticket_level4"
															<%=mybean .PopulateCheck(mybean.emp_priorityticket_level4)%> 
															onchange="SecurityCheck2('Ticket Level 4','ch_emp_priorityticket_level4',this,'hint_ch_emp_priorityticket_level4');"/>
														</div>
														<div class="form-element1 ">Level 5
															<input type="checkbox" name="ch_emp_priorityticket_level5"
															id="ch_emp_priorityticket_level5"
															<%=mybean .PopulateCheck(mybean.emp_priorityticket_level5)%> 
															onchange="SecurityCheck2('Ticket Level 5','ch_emp_priorityticket_level5',this,'hint_ch_emp_priorityticket_level5');"/>
														</div>
														</td>
													</tr>
													<% } %>
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
																onclick="JavaScript:AddItem('exe_soe','exe_soe_trans', '');onPress();SecurityCheck('exe_soe_trans', this, 'hint_exe_soe_trans');"
																value="   Add &gt;&gt;" /> <br /><br> <input name="Input4"
																type="button" class="btn btn-success"
																onclick="JavaScript:DeleteItem('exe_soe_trans');onPress();SecurityCheck('exe_soe_trans', this, 'hint_exe_soe_trans');"
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
														     class="form-control" onchange="SecurityCheck('dr_emp_prevexp_year',this,'hint_dr_emp_prevexp_year');">
														     <%=mybean.PopulateNumberDrop(0, 30, mybean.emp_prevexp_year, "Years")%>
														 </select>
													</div>
													<div class="form-element6">
														<select id="dr_emp_prevexp_month" name="dr_emp_prevexp_month" 
														     class="form-control" onchange="SecurityCheck('dr_emp_prevexp_month',this,'hint_dr_emp_prevexp_month');">
														     <%=mybean.PopulateNumberDrop(0, 11,  mybean.emp_prevexp_month, "Months")%>
													    </select>
													</div>
											</div>
											
											<div class="form-element3">
													<div> Date of Join<font color=red>*</font>: </div>
													<input name="txt_emp_date_of_join" id="txt_emp_date_of_join"
														value="<%=mybean.date_of_join%>"
														class="form-control datepicker" type="text" maxlength="10" 
														onchange="SecurityCheck('txt_emp_date_of_join',this,'hint_txt_emp_date_of_join');"/>
											</div>
											
											<div class="form-element3">
													<div> Date of Relieve: </div>
													<input name="txt_emp_date_of_relieve"
														id="txt_emp_date_of_relieve"
														value="<%=mybean.date_of_relieve%>"
														class="form-control datepicker" type="text" maxlength="10" 
														onchange="SecurityCheck('txt_emp_date_of_relieve',this,'hint_txt_emp_date_of_relieve');"/>
											</div>
										</div>
										
										<div class ="row">
											<div class="form-element6">
													<div> Reason Of Leaving: </div>
													<textarea name="txt_emp_reason_of_leaving" cols="70"
														rows="4" class="form-control" onchange="SecurityCheck('txt_emp_reason_of_leaving',this,'hint_txt_emp_reason_of_leaving');"
														id="txt_emp_reason_of_leaving"><%=mybean.emp_reason_of_leaving%></textarea>
											</div>
											
											<div class="form-element6">
													<div> Notes: </div>
													<textarea name="txt_emp_notes" cols="70" rows="4"
													onchange="SecurityCheck('txt_emp_notes',this,'hint_txt_emp_notes');"
														class="form-control" id="txt_emp_notes"><%=mybean.emp_notes%></textarea>
											</div>
										</div>
									</div>
								</div>
							</div>
						<!-- END SEO-->
						</form>
					</div>	
					<%if(mybean.ReturnPerm(mybean.comp_id, "emp_role_id", request).equals("1")){ %>						
						<div class="tab-pane" id="tabs-2">
							<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Access Rights</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<!-- <form name="formemp" method="post" class="form-horizontal"> -->
												<div class="container-fluid ">
													<div><%=mybean.ListAccess()%></div>
												</div>
											<!-- </form> -->
										</div>
									</div>
							</div>
						</div>
					<% } %>
						<div class="tab-pane" id="tabs-3"></div>
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
	<script src="../assets/js/jquery_toast.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript">


		function SecurityCheck(name, obj, hint) {
			var exe_id = GetReplace(document.formemp.exe_id.value);
			var url = "../portal/executives-dash-check.jsp?";
			var str = "123";
			var value = '';
			if(name == "exe_branch_trans" || name == "exe_team_trans" || name == "exe_soe_trans") {
				value = $('#'+ name).val();
			} else {
				value = GetReplace(obj.value);
			}
			if(value == null){
				value='';
			}
			if(name== "drop_DOBDay" || name=="drop_DOBMonth" || name=="drop_DOBYear") {
				var day = $('#drop_DOBDay').val();
				var month = $('#drop_DOBMonth').val();
				var year = $('#drop_DOBYear').val();
				if(day.length == 1) {
					day = "0" + day;
				}
				if(month.length == 1) {
					month = "0" + month;
				}
				value = year  + month + day+"000000";
// 				console.log('value=11==='+value);
			}
			var param = "updatefields=yes" + "&name=" + name + "&value=" + value + "&exe_id=" + exe_id ;
			console.log('param=='+param);
 			showHintWithToastMessage(url + param, hint);
		}
		
		//only for checkbox
		function SecurityCheck2(labelname, name, obj, hint) {
// 			console.log(obj.checked);
// 			console.log(hint);
// 			console.log(name);
// 			console.log(labelname);
			var exe_id = GetReplace(document.formemp.exe_id.value);
			var url = "../portal/executives-dash-check.jsp?";
			var value = '';
			if (obj.checked == true) {
				value = "1";
			} else {
				value = "0";
			}
// 			console.log(value);
 			/* var param = "name=" + name + "&value=" + value + "&exe_id=" + exe_id ; */
 			var param = "updatefields=yes" + "&labelname=" + labelname + "&name=" + name + "&value=" + value + "&exe_id=" + exe_id ;
 			showHintWithToastMessage(url + param, hint);
		}
		
		//only for Access rights checkbox
		function SecurityCheck3(name, accessId, obj, hint) {
			
			var exe_id = GetReplace(document.formemp.exe_id.value);
			var url = "../portal/executives-dash-check.jsp?";
			var value = '';
			if (obj.checked == true) {
				value = "1";
			} else {
				value = "0";
			}
// 			console.log(value);
 			var param = "name=" + name + "&value=" + value + "&accessid=" + accessId + "&exe_id=" + exe_id + "&accessright=yes" ;
 			showHintWithToastMessage(url + param, hint);
		}
		
		
	function showHintWithToastMessage(url, Hint) {
		$('#' + Hint).html('<div id=loading align=center><img align=center alt="test" src=\"../admin-ifx/loading.gif\" /></div>');
		$.ajax({
			url : url,
			type : 'GET',
			success : function(data) {
				if (data.trim() != 'SignIn') {
					$('#' + Hint).show();
					$('#' + Hint).fadeIn(500).html('' + data.trim() + '');
					FormElements();
					if (data.trim() != '') {
						UINotific8.init(data.trim());
					}
				} else {
					window.location.href = "../portal/";
				}
			}
		});
	}

	function LoadHistory(tab) {
		var exe_id = GetReplace(document.formemp.exe_id.value);
		if (tab == '3') {
			if (document.getElementById("tabs-3").innerHTML == '') {
				showHint( '../portal/executives-dash-check.jsp?history=yes&exe_id=' + exe_id, 'tabs-3');
			}
		}
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

	function LoadExecutives() {
		var branch_id = document.getElementById("dr_emp_branch_id").value;
		if(branch_id == 0) {
			branch_id = outputSelected(document.getElementById("exe_branch_trans").options);
		}
		showHint('../portal/executive-check.jsp?multiple=yes&branch_id=' + GetReplace(branch_id), 'exeHint');
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

	function HideAllCheckHint(name) {
		if (name == "chk_emp_all_branches") {
			var value = document.getElementById("chk_emp_all_branches").checked;
			document.getElementById("hint_exe_branch_trans").innerHTML = '';
		} else if (name == "chk_emp_all_exe") {
			var value = document.getElementById("chk_emp_all_exe").checked;
			document.getElementById("hint_exe_team_trans").innerHTML = '';
		}
	}

	function getBranch() {
		var branch_id = getoutputSelected(document.getElementById("exe_branch_trans").options);
		showHint('../portal/executive-check.jsp?multiple=yes&branch_id=' + GetReplaceString(branch_id), 'exeHint');
	}
</script>
</body>
</html>