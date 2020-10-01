<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Ticket_Dash"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-timepicker.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />	
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../assets/css/footable.core.css">
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<%-- 	<%@ include file="../Library/ticket-dash.jsp"%> --%>
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
						<h1>Ticket Dashboard</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->

			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="../service/ticket.jsp">Ticket</a> &gt;</li>
						<li><a href="../service/ticket-list.jsp?all=yes">List
								Tickets</a> &gt;</li>
						<li><a
							href="../service/ticket-dash.jsp?ticket_id=<%=mybean.ticket_id%>"><%=mybean.ticket_subject%>
								(<%=mybean.ticket_id%>)</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<input type="hidden" name="txt_ticket_id" id="txt_ticket_id"
								value="<%=mybean.ticket_id%>" /> <input type="hidden"
								name="txt_customer_id" id="txt_customer_id"
								value="<%=mybean.ticket_customer_id%>" />


							<div class="tabbable tabbable-tabdrop">
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tabs-1" data-toggle="tab">Ticket
											Details</a></li>
									<li><a href="#tabs-2" data-toggle="tab">Follow-up</a></li>
									<li><a href="#tabs-3" data-toggle="tab">Customer</a></li>
									<li><a href="#tabs-4" data-toggle="tab">Documents</a></li>
									<li><a href="#tabs-5" data-toggle="tab">History</a></li>
									<li><a href="#tabs-6" data-toggle="tab">Summary</a></li>
								</ul>
								<div class="tab-content">
									<div class="tab-pane active" id="tabs-1">
										<form name="form1" id="form1" method="post"
											class="form-horizontal">
											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Ticket
														Details</div>
												</div>
												<div class="portlet-body portlet-empty">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->
														<div class="container-fluid">
															<div class="col-md-6">

																<%
																	if (!mybean.ticket_contact_id.equals("") && !mybean.ticket_contact_id.equals("0")) {
																%>
																<div class="form-group">
																	<label class="col-md-3 control-label">Contact:&nbsp</label>
																	<div class="txt-align">
																		<%=mybean.client%>
																		<input type="hidden" name="contact_id" id="contact_id"
																			value="<%=mybean.ticket_contact_id%>" />
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">Customer:&nbsp</label>
																	<div class="txt-align">
																		<%=mybean.client1%>
																		<input type="hidden" name="customer_id"
																			id="customer_id"
																			value="<%=mybean.ticket_customer_id%>" />
																	</div>
																</div>
																<%
																	}
																%>
																<%
																	if (!mybean.veh_id.equals("0")) {
																%>
																<div class="form-group">
																	<label class="col-md-3 control-label">Vehicle
																		ID:&nbsp</label>
																	<div class="txt-align">
																		<b><a
																			href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>"><%=mybean.veh_id%></a>
																		</b>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">Reg. No.:&nbsp</label>
																	<div class="txt-align">
																		<b><a
																			href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>"><%=mybean.veh_reg_no%></a>
																		</b>
																	</div>
																</div>
																<%
																	}
																%>
																<%
																	if (!mybean.jc_id.equals("0")) {
																%>
																<div class="form-group">
																	<label class="col-md-3 control-label">JC ID:&nbsp</label>
																	<div class="txt-align">
																		<b><a
																			href="../service/jobcard-list.jsp?jc_id=<%=mybean.jc_id%>"><%=mybean.jc_id%></a>
																		</b>
																	</div>
																</div>
																<%
																	}
																%>
																<div class="form-group">
																	<label class="col-md-3 control-label">Subject<font
																		color="#ff0000">*</font>:&nbsp
																	</label>
																	<div class="col-md-6">
																		<input name="txt_ticket_subject" type="text"
																			class="form-control"
																			value="<%=mybean.ticket_subject%>" size="50"
																			maxlength="255"
																			onChange="SecurityCheck('txt_ticket_subject',this,'hint_txt_ticket_subject');">
																			&nbsp;<a
																			href="../service/kb.jsp?1_dr_field=0-text&1_dr_param=0-text&1_txt_value_1=<%=mybean.ticket_subject%>&1_dr_filter=and&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1"
																			target="_blank">KB</a>&nbsp;<a
																			href="ticket-faq-list.jsp?1_dr_field=0-text&1_dr_param=0-text&1_txt_value_1=<%=mybean.ticket_subject%>&1_dr_filter=and&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1"
																			target="_blank">FAQ</a><br> <font color="red">
																			<div class="hint" id="hint_txt_ticket_subject"></div>
																		</font>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">Description<font
																		color="#ff0000">*</font>:&nbsp
																	</label>
																	<div class="col-md-6">
																		<textarea name="txt_ticket_desc" cols="57" rows="7"
																			class="form-control" id="txt_ticket_desc"
																			onChange="SecurityCheck('txt_ticket_desc',this,'hint_txt_ticket_desc');"><%=mybean.ticket_desc%></textarea>
																		<font color="red">
																			<div class="hint" id="hint_txt_ticket_desc"></div>
																		</font>
																	</div>
																</div>
																
																<div class="form-group">
																	<label class="col-md-3 control-label">Solution
																		/ Closing Summary<font color="#ff0000">*</font><b></b>:&nbsp
																	</label>
																	<div class="col-md-6">
																		<textarea name="txt_comments" cols="57" rows="5"
																			class="form-control" id="txt_comments"
																			onChange="SecurityCheck123('txt_comments',this,'hint_txt_comments');"
																			onKeyUp="charcount('txt_comments', 'span_txt_comments','<font color=red>({CHAR} characters left)</font>', '2000');"><%=mybean.ticket_closed_comments%></textarea>
																		<font color="red">
																			<div class="hint" id="hint_txt_comments"></div>
																		</font>
																	</div>
																</div>
																<%if(!mybean.ticket_crm_id.equals("0")){ %>
<!-- 																<div class="form-group"> -->
<!-- 																	<label class="col-md-3 control-label">CRM ID :</label> -->
<!-- 																	<div class="txt-align"> -->
<%-- 																		<%=mybean.ticket_crm_id%> --%>
<!-- 																	</div> -->
<!-- 																</div> -->
																
																<div class="form-group">
																	<label class="col-md-3 control-label">CRM Follow-Up: &nbsp</label>
																	<div class="txt-align">
																	
																	<a href="javascript:remote=window.open('../sales/crm-update.jsp?update=yes&crm_id=<%=mybean.ticket_crm_id%>','crmupdate','');remote.focus();"><%=mybean.crm_followup_time%></a>
																	</div>
																</div>
																
																<div class="form-group">
																	<label class="col-md-3 control-label">Enquiry ID:&nbsp </label>
																	<div class="txt-align">
																		<a href="javascript:remote=window.open('../sales/enquiry-dash.jsp?enquiry_id=<%=mybean.enquiry_id%>','enquirydash','');remote.focus();"><%=mybean.enquiry_id%></a>
																	</div>
																</div>
																
																<div class="form-group">
																	<label class="col-md-3 control-label">CRM Days:&nbsp </label>
																	<div class="txt-align">
																		<%=mybean.crmdays_daycount%>
																	</div>
																</div>
																
																
																<div class="form-group">
																	<label class="col-md-3 control-label">CRM Executive: &nbsp</label>
																	<div class="txt-align">
																		<%=mybean.crmemp_name%>
																	</div>
																</div>
																
																<div class="form-group">
																	<label class="col-md-3 control-label">Sales Executive: &nbsp</label>
																	<div class="txt-align">
																		<%=mybean.salesexe_name%>
																	</div>
																</div>
																
																<%} %>
																
																
																
																<%if(!mybean.ticket_so_id.equals("0")){ %>
																<div class="form-group">
																	<label class="col-md-3 control-label">SO ID: &nbsp</label>
																	<div class="txt-align">
																		<%=mybean.ticket_so_id%>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">SO Date: &nbsp</label>
																	<div class="txt-align">
																		<%=mybean.so_date%>
																	</div>
																</div>
																
																
																<%} %>
																
																<%if(!mybean.ticket_jcpsf_id.equals("0")){ %>
																
																<div class="form-group">
																	<label class="col-md-3 control-label">PSF Follow-Up: &nbsp</label>
																	<div class="txt-align">
																	
																	<a href="javascript:remote=window.open('../service/jobcard-psf-update.jsp?update=yes&jcpsf_id=<%=mybean.ticket_jcpsf_id%>','jcpsfpdate','');remote.focus();"><%=mybean.jcpsf_followup_time%></a>
<%-- 																		<%=mybean.jcpsf_followup_time%> --%>
																	</div>
																</div>
																
																
																<div class="form-group">
																	<label class="col-md-3 control-label">JC ID:&nbsp </label>
																	<div class="txt-align">
																	<a href="javascript:remote=window.open('../service/jobcard-dash.jsp?jc_id=<%=mybean.ticket_jc_id%>','jcdash','');remote.focus();"><%=mybean.ticket_jc_id%></a>
<%-- 																		<%=mybean.ticket_jc_id%> --%>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">PSF Days:&nbsp </label>
																	<div class="txt-align">
																		<%=mybean.psfdays_daycount%>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">PSF Executive: &nbsp</label>
																	<div class="txt-align">
																		<%=mybean.psfemp_name%>
																	</div>
																</div>
																
																<div class="form-group">
																	<label class="col-md-3 control-label">Veh ID: &nbsp</label>
																	<div class="txt-align">
																	<a href="javascript:remote=window.open('../service/vehicle-dash.jsp?veh_id=<%=mybean.jc_veh_id%>','vehdash','');remote.focus();"><%=mybean.jc_veh_id%></a>
<%-- 																		<%=mybean.jc_veh_id%> --%>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">Veh Reg No.</label>
																	<div class="txt-align">
																	<a href="javascript:remote=window.open('../service/vehicle-dash.jsp?veh_id=<%=mybean.jc_veh_id%>','vehdash','');remote.focus();"><%=mybean.jc_reg_no%></a>
																	</div>
																</div>
																
																<%} %>
																
																<div class="form-group">
																	<label class="col-md-3 control-label">Entry By: &nbsp</label>
																	<div class="txt-align col-md-offset-0">
																		<%=mybean.entry_id%>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">Entry
																		Date: &nbsp</label>
																	<div class="txt-align col-md-offset-0">
																		<%=mybean.entry_date%>
																	</div>
																</div>
															</div>
															<div class="col-md-6">
																<div class="form-group">
																	<label class="col-md-3 control-label">Ticket
																		ID:&nbsp </label>
																	<div class="txt-align">
																		<b> <%=mybean.ticket_id%></b> <input name="ticket_id"
																			type="hidden" id="ticket_id"
																			value="<%=mybean.ticket_id%>">
																			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
																		<%
 	if (mybean.ticket_customer_id.equals("0") && mybean.ticketParentId.equals("0")) {
 %>
																		<a
																			href="ticket-add.jsp?add=yes&ticket_parent_id=<%=mybean.ticket_id%>">New
																			Child Ticket</a>
																		<%
 	} else if (mybean.ticketParentId.equals("0")) {
 %>
																		<a
																			href="ticket-add.jsp?add=yes&ticket_parent_id=<%=mybean.ticket_id%>">New
																			Child Ticket</a>
																		<%
 	}
 %>

																	</div>
																</div>
																<%
																	if (!mybean.childTickets.equals("")) {
																%>
																<div class="form-group">
																	<label class="col-md-3 control-label">Child
																		Tickets:&nbsp </label>
																	<div class="txt-align">
																		<%=mybean.childTickets%>
																	</div>
																</div>
																<%
																	}
																%>
																<%
																	if (!mybean.ticket_parent_id.equals("") && !mybean.ticket_parent_id.equals("0")) {
																%>
																<div class="form-group">
																	<label class="col-md-3 control-label">Parent
																		Ticket:&nbsp </label>
																	<div class="txt-align">
																		<b><a
																			href="ticket-dash.jsp?ticket_id=<%=mybean.ticket_parent_id%>"><%=mybean.ticket_parent_id%></a></b>
																	</div>
																</div>
																<%
																	}
																%>
																
																<div class="form-group">
										<label class="control-label col-md-3">Report Time<font
											color=#ff0000><b>*</b></font>:&nbsp</label>
										<div class="col-md-6">
											<div class="input-group date form_datetime">
												<input type="text" size="16"  name ="txt_report_time" id ="txt_report_time"
													value="<%=mybean.reporttime%>" class="form-control" size="20"
																			maxlength="14"
																			onChange="SecurityCheck('txt_report_time',this,'hint_txt_report_time');">
																			<font color="red">
																			<div class="hint" id="hint_txt_report_time"></div>
																		</font>
												<span class="input-group-btn">
													<button class="btn default date-set" type="button">
														<i class="fa fa-calendar"></i>
													</button>
												</span>
											</div>
										</div>
									</div>
																
																<%-- <div class="form-group">
																	<label class="col-md-3 control-label">Report
																		Time<font color="#ff0000">*</font>:
																	</label>
																	<div class="col-md-6">
																		<input name="txt_report_time" type="text"
																			class="form-control date-picker" id="txt_report_time"
																			value="<%=mybean.reporttime%>" size="20"
																			maxlength="14"
																			onChange="SecurityCheck('txt_report_time',this,'hint_txt_report_time');">
																		<font color="red">
																			<div class="hint" id="hint_txt_report_time"></div>
																		</font>
																	</div>
																</div> --%>
																<div class="form-group">
																	<label class="col-md-3 control-label">Source<font
																		color="#ff0000">*</font>:&nbsp
																	</label>
																	<div class="col-md-6">
																		<select name="dr_ticket_ticketsource_id"
																			class="form-control"
																			onChange="SecurityCheck('dr_ticket_ticketsource_id',this,'hint_dr_ticket_ticketsource_id');">
																			<%=mybean.PopulateSourceType()%>
																		</select> <font color="red">
																			<div class="hint" id="hint_dr_ticket_ticketsource_id"></div>
																		</font>
																	</div>
																</div>
																<%
																	if (mybean.config_service_ticket_type.equals("1")) {
																%>
																<div class="form-group">
																	<label class="col-md-3 control-label">Type:&nbsp</label>
																	<div class="col-md-6">
																		<select name="dr_ticket_tickettype_id"
																			class="form-control" id="dr_ticket_tickettype_id"
																			onChange="SecurityCheck('dr_ticket_tickettype_id',this,'hint_dr_ticket_tickettype_id');">
																			<%=mybean.PopulateTicketType()%>
																		</select> <font color="red">
																			<div class="hint" id="hint_dr_ticket_tickettype_id"></div>
																		</font>
																	</div>
																</div>
																<%
																	}
																%>
																<div class="form-group">
																	<label class="col-md-3 control-label">Status<font
																		color="#ff0000">*</font>:
																	</label>
																	<div class="col-md-6">
																		<select name="dr_ticket_ticketstatus_id"
																			id="dr_ticket_ticketstatus_id" class="form-control"
																			onChange="SecurityCheck123('dr_ticket_ticketstatus_id',this,'hint_dr_ticket_ticketstatus_id');">
																			<%=mybean.PopulateStatus()%>
																		</select> <font color="red">
																			<div class="hint" id="hint_dr_ticket_ticketstatus_id"></div>
																		</font>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">Priority<font
																		color="#ff0000">*</font>:&nbsp
																	</label>
																	<div class="col-md-6">
																		<select name="dr_ticket_priorityticket_id"
																			class="form-control" id="dr_ticket_priorityticket_id"
																			onChange="SecurityCheck('dr_ticket_priorityticket_id',this,'hint_dr_ticket_priorityticket_id');">
																			<%=mybean.PopulateTicketPrioirty()%>
																		</select> <font color="red">
																			<div class="hint"
																				id="hint_dr_ticket_priorityticket_id"></div>
																		</font>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">Department<font
																		color="#ff0000">*</font>:&nbsp
																	</label>
																	<div class="col-md-6">
																		<select name="dr_ticket_dept_id" class="form-control"
																			id="dr_ticket_dept_id"
																			onChange="SecurityCheck('dr_ticket_dept_id',this,'hint_dr_ticket_dept_id');populateCatogery(this);">
																			<%=mybean.PopulateDepartment()%>
																		</select> <font color="red">
																			<div class="hint" id="hint_dr_ticket_dept_id"></div>
																		</font>
																	</div>
																</div>
																<%
																	if (mybean.config_service_ticket_cat.equals("1")) {
																%>
																<div class="form-group">
																	<label class="col-md-3 control-label">Category: &nbsp;</label>
																	<div class="col-md-6">
																		<span id="categoryHint"> 
																		<%=mybean.PopulateTicketCategory(mybean.ticket_ticket_dept_id, mybean.comp_id)%>
																		</span> <font color="red">
																			<div class="hint" id="hint_dr_ticket_ticketcat_id"></div>
																		</font>
																	</div>

																</div>
																<%
																	}
																%>
																
																<div class="form-group">
																	<label class="col-md-3 control-label">Executive<font
																		color="#ff0000">*</font>:&nbsp
																	</label>
																	<div class="col-md-6">
																		<select name="dr_ticket_emp_id" class="form-control"
																			id="dr_ticket_emp_id"
																			onChange="SecurityCheck('dr_ticket_emp_id',this,'hint_dr_ticket_emp_id');">
																			<%
																				//if(!mybean.ticket_contact_id.equals("") && !mybean.ticket_contact_id.equals("0")) {
																			%>
																			<%
																				//=mybean.PopulateExecutive()
																			%>
																			<%
																				//} else {
																			%>
																			<%=mybean.PopulateAllExecutive()%>
																			<%
																				//}
																			%>
																		</select> <font color="red">
																			<div class="hint" id="hint_dr_ticket_emp_id"></div>
																		</font>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">Due Date:&nbsp</label>
																	<div class="txt-align">
																		<span id="span_duedate"><%=mybean.duedate%></span>
																	</div>
																</div>
																<div class="form-group">
																	<label class="col-md-3 control-label">Email CC:&nbsp</label>
																	<div class="col-md-6">
																		<input name="txt_ticket_cc" type="text"
																			class="form-control" value="<%=mybean.ticket_cc%>"
																			size="50" maxlength="1000"
																			onChange="SecurityCheck('txt_ticket_cc',this,'hint_txt_ticket_cc');">
																		<div class="hint" id="hint_txt_ticket_cc"></div>
																	</div>
																</div>
																<div style="margin-left: 150px">
																	<a
																		href="ticket-list.jsp?previous_ticket_id=<%=mybean.ticket_id%>">Previous
																		Tickets</a>
																</div>

															</div>
														</div>
													</div>
												</div>
											</div>
										</form>
									</div>
									<!-- end of tab1 -->
									<div class="tab-pane" id="tabs-2">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none"> <%=mybean.status%>&nbsp;Follow-up</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<div class="container-fluid">
														<%
															if (mybean.status.equals("")) {
														%>
														<div style="float: right">
															<a href="ticket-dash.jsp?add=yes&ticket_id=<%=mybean.ticket_id%>#tabs-2">Add
																Follow-Up...</a>&nbsp;
														</div></br>
														<div align="center">
															<font color="red"><b><%=mybean.msg%></b></font>
														</div>
														<center>
															<%=mybean.StrHTML%>
														</center>
														<%
															}
														%>
														<%
															if (mybean.status.equals("Add") || mybean.status.equals("Update")) {
														%>
														<div align="center">
															<font color="#ff0000"><b><%=mybean.msg%></b></font>
														</div><br>
														<form name="form2" method="post" class="form-horizontal">
<!-- 															<center> -->
<%-- 																<h3><%=mybean.status%>&nbsp;Follow-up --%>
<!-- 																</h3> -->
<!-- 															</center> -->
															<div align="center">
																Form fields marked with a red asterisk <b><font
																	color="#ff0000">*</font></b> are required.
															</div>
															<div class="form-group">
																<label class="col-md-3 control-label">Description<font
																	color="#ff0000">*</font>:
																</label>
																<div class="col-md-6">
																	<textarea name="txt_tickettrans_followup" cols="60"
																		rows="6" class="form-control"
																		id="txt_tickettrans_followup"
																		onKeyUp="charcount('txt_tickettrans_followup', 'span_txt_tickettrans_followup','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.tickettrans_followup%></textarea>
																	<span id="span_txt_tickettrans_followup"> 8000
																		characters </span>
																</div>
															</div>
															<div class="form-group">
																	<label class="col-md-3 control-label">Next Follow-up Time<font
																		color="#ff0000">*</font>:
																	</label>
														    <div class="col-md-6">
																<div class="input-group date form_datetime">
																	<input type="text" size="16" name="txt_tickettrans_nextfollowup_time"
																		id="txt_tickettrans_nextfollowup_time" class="form-control" value="<%=mybean.tickettrans_nextfollowup_time%>"></input> <span
																		class="input-group-btn">
																		<button class="btn default date-set"
																			type="button btn btn-success">
																			<i class="fa fa-calendar"></i>
																		</button>
																	</span>
																</div>
															</div>
																</div>
															<%
																if (mybean.add.equals("yes") || (mybean.tickettrans_contact_entry_id.equals("0"))) {
															%>
															<div class="form-group">
																<label class="col-md-3 control-label">Private:&nbsp</label>
																<div class="txt-align">
																	<input id="chk_private" type="checkbox"
																		name="chk_private"
																		<%=mybean.PopulateCheck(mybean.tickettrans_private)%> />
																</div>
															</div>
															<%
																}
															%>
															<%
																if (mybean.status.equals("Update") && !(mybean.contact_entry_by == null) && !(mybean.contact_entry_by.equals(""))) {
															%>
															<div class="form-group">
																<label class="col-md-3 control-label">Customer:&nbsp</label>
																<div class="txt-align">
																	<%=mybean.contact_entry_by%>
																	<input type="hidden" name="contact_entry_by"
																		value="<%=mybean.contact_entry_by%>">
																</div>
															</div>
															<%
																}
															%>
															<%
																if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
															%>
															<div class="form-group">
																<label class="col-md-3 control-label">Entry By:&nbsp</label>
																<div class="txt-align">
																	<%=mybean.unescapehtml(mybean.entry_by)%>
																	<input type="hidden" name="entry_by"
																		value="<%=mybean.unescapehtml(mybean.entry_by)%>">
																</div>
															</div>
															<div class="form-group">
																<label class="col-md-3 control-label">Entry
																	Date:&nbsp</label>
																<div class="txt-align">
																	<%=mybean.entry_date%>
																	<input type="hidden" name="entry_date"
																		value="<%=mybean.entry_date%>">
																</div>
															</div>

															<% } %>
															<% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
															<div class="form-group">
																<label class="col-md-3 control-label">Modified
																	By:&nbsp</label>
																<div class="txt-align">
																	<%=mybean.unescapehtml(mybean.modified_by)%>
																	<input type="hidden" name="modified_by"
																		value="<%=mybean.unescapehtml(mybean.modified_by)%>">
																</div>
															</div>
															<div class="form-group">
																<label class="col-md-3 control-label">Modified
																	Date:&nbsp</label>
																<div class="txt-align">
																	<%=mybean.modified_date%>
																	<input type="hidden" name="modified_date"
																		value="<%=mybean.modified_date%>">
																</div>
															</div>
															<%} %>
															<input name="ticket_id" type="hidden" id="ticket_id"
																value="<%=mybean.ticket_id%>">
															<%if(mybean.status.equals("Add")){%>
															<center>
																<input name="addbutton" type="submit"
																	class="btn btn-success" id="addbutton"
																	value="Add Follow-up"
																	onclick="return SubmitFormOnce(document.form2,this);" />
																<input type="hidden" name="add_button" value="yes">
															</center>
															<%}else if (mybean.status.equals("Update")){%>
															<center>
																<input type="hidden" name="update_button" value="yes">
																<input name="updatebutton" type="submit"
																	class="btn btn-success" id="updatebutton"
																	value="Update Follow-up"
																	onclick="return SubmitFormOnce(document.form2,this);" />
																<input name="delete_button" type="submit"
																	class="btn btn-success" id="delete_button"
																	OnClick="return confirmdelete(this)"
																	value="Delete Follow-up" />
															</center>
															<% } %>
														</form>
														<% 	} %>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="tab-pane" id="tabs-3">
										<%=mybean.Customer_dash.CustomerDetails(response, mybean.ticket_customer_id,"",mybean.comp_id)%>
									</div>
									<div class="tab-pane" id="tabs-4">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Document</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<div style="float: right">
														<a
															href="../portal/docs-update.jsp?add=yes&ticket_id=<%=mybean.ticket_id%>">Add
															New Document...</a><br />
													</div>
													<%=mybean.ListDocs()%>
												</div>
											</div>
										</div>
									</div>
									<div class="tab-pane" id="tabs-5">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">History</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<%=mybean.ListHistoryData()%>
												</div>
											</div>
										</div>
									</div>
									<div class="tab-pane" id="tabs-6">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Summary</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<div style="float: right">

														<a href="ticket-summary-print.jsp??ticket_id=<%=mybean.ticket_id%>">Print
															Ticket</a>
													</div></br>
													<%=mybean.TicketSummary()%>
												</div>
											</div>
										</div>
									</div>

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
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/slider.js"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap-timepicker.js"
		type="text/javascript"></script>
	<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datetimepicker.js"
		type="text/javascript"></script>
	<script src="../assets/js/footable.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>


	<script type="text/javascript" src="../Library/dynacheck.js"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-select2.min.js"
		type="text/javascript"></script>
	


	<script type="text/javascript" src="../Library/dynacheck-post.js"></script>
	<script type="text/javascript" src="../Library/dynacheck-post.js?target=<%=mybean.jsver%>"></script>
	<script type="text/javascript" src="../Library/sc.js?Math.random()"></script>
	<script>
	$(function() {
		$("#tabs").tabs({
			event : "mouseover"
		});
		document.getElementById("txt_report_time").style.zIndex = 100;
		$("#txt_report_time").datetimepicker({
			dateFormat : "dd/mm/yy"
		});
		$("#txt_tickettrans_nextfollowup_time" ).datepicker({
	        showButtonPanel: true,
	        dateFormat: "dd/mm/yy"
	      });
		
	});
</script>
<script language="JavaScript" type="text/javascript">
	function SecurityCheck123(name, obj, hint) {

		var status = document.getElementById("dr_ticket_ticketstatus_id").value;
// 		alert("status--"+status);			   
		var comment = document.getElementById("txt_comments").value;
		//alert("comment--"+comment);						 
		var checked = '';
		var ticket_id = GetReplacePost(document.form1.ticket_id.value);
		//alert("ticket_id--"+ticket_id);
		var url = "ticket-dash-historycheck.jsp?";
		var param = "name=" + name + "&status=" + status + "&checked="
				+ checked + "&comment=" + comment + "&ticket_id=" + ticket_id;
		var str = "123";
// 		alert("param--"+param);
		showHintPost(url + param, str, param, hint);
		setTimeout('RefreshHistory()', 1000);

	}

	function SecurityCheck(name, obj, hint) {
// 		alert("coming...");
		var value = GetReplacePost(obj.value);
// 		alert("value==="+value);
		var checked = '';
// 		alert("checked==="+checked);
		var ticket_id = GetReplacePost(document.form1.ticket_id.value);
// 		alert("ticket_id..."+ticket_id);
		var url = "ticket-dash-historycheck.jsp?";
// 		alert("coming...2");
		var param = "name=" + name + "&value=" + value + "&checked=" + checked
				+ "&ticket_id=" + ticket_id;
		var str = "123";
		showHintPost(url + param, str, param, hint);
		//alert("showHint======="+showHintPost(url + param, str, param, hint));
		if (name == 'dr_ticket_dept_id' || name == 'txt_report_time') {
			setTimeout('populateduuedate()', 1000);
		}
	}

	function populateduuedate() {
		//alert("---");
		var duedate = document.getElementById("duedate").value;
		document.getElementById("span_duedate").innerHTML = duedate;
	}
	
	function populateCatogery(){
		var  department_id= $('#dr_ticket_dept_id').val();
		  showHint('../service/report-check.jsp?ticket_category=ticket_dash&ticket_dept_id='+department_id, 'categoryHint');
	}
</script>
</body>
</HTML>
