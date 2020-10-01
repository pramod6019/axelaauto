<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Ticket_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>
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
						<h1><%=mybean.status%> Ticket </h1>
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
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="ticket.jsp">Tickets</a>&gt;</li>
						<li><a href="ticket-list.jsp?all=yes"> ListTickets</a> &gt;</li>
						<li><a href="ticket-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Ticket</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%> Ticket </center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<input name="txt_customer_branch_id" type="hidden" id="txt_customer_branch_id" value="<%=mybean.customer_branch_id%>" />
											<center>
												<font size="1">Form fields marked with a red asterisk <font color=#ff0000>*</font> are required. </font>
											</center>
											<br>
											<div class="row">
											 <div class="form-element6">
											<%
												if (!mybean.ticket_contact_id.equals("0") && !mybean.ticket_contact_id.equals("0")) {
											%>
											
											<div class="form-element6">
												<label >Customer:&nbsp;</label>
													<b> <a href="../customer/customer-pop.jsp?customer_id=<%=mybean.ticket_customer_id%>"><%=mybean.customer_name%> </a> </b>
													<input name="ticket_parent_id" type="hidden" id="ticket_parent_id" value="<%=mybean.ticket_parent_id%>" />
													<input name="ticket_customer_id" type="hidden" id="ticket_customer_id" value="<%=mybean.ticket_customer_id%>" />
											</div>
											<div class="form-element6">
												<label>Contact Name<font color="red">*</font>:</label>
													<b><a href="../customer/customer-contact-list.jsp?customer_id=<%=mybean.ticket_customer_id%>&contact_id=<%=mybean.ticket_contact_id%>">
													<%=mybean.contact_name%></a></b>
													<input name="ticket_contact_id" type="hidden" id="ticket_contact_id" value="<%=mybean.ticket_contact_id%>" />
											</div>
											<%
												}
											%>
											</div>
											<div class="form-element6">
<!-- 											<span>&nbsp;</span> -->
											 <%
												if (!mybean.veh_id.equals("0")) {
											%> 
											
											<div class="form-element4">
												<label >Vehicle ID<font color="red">*</font>:</label>
													<b><a href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>"><%=mybean.veh_id%></a></b>
											</div>
											<div class="form-element4">
												<label >Reg. No.:&nbsp;</label>
													<b><a href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>"><%=mybean.veh_reg_no%></a> </b>
											</div>
										 <%
												}
											%> 
											
											<%
												if (!mybean.jc_id.equals("0")) {
											%> 
											<div class="form-element4">
												<label >JC ID<font color="red">*</font>:</label>
													<b><a href="../service/jobcard-list.jsp?jc_id=<%=mybean.jc_id%>"><%=mybean.jc_id%></a> </b>
											</div>
											
											 <% 
											}
 											%>		
									</div>  
									</div>
											<div class="form-element6">
												<label >Branch<font color="#ff0000">*</font>:&nbsp;</label>
												<select id="dr_ticket_branch_id" name="dr_ticket_branch_id" class="form-control" >
							                      <%=mybean.PopulateBranch(mybean.ticket_branch_id, mybean.param, mybean.branch_type, request) %>
							                    </select>					
											</div>
											
											<div class="form-element6">
												<label >Subject<font color="#ff0000">*</font>: </label>
													<input name="txt_ticket_subject" type="text" class="form-control" id="txt_ticket_subject" value="<%=mybean.ticket_subject%>" size="52" maxlength="255" />
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label>Description<font color="#ff0000">*</font>: </label>
													<textarea name="txt_ticket_desc" cols="50" rows="5" class="form-control" id="txt_ticket_desc"
													 onKeyUp="charcount('txt_ticket_desc', 'span_txt_ticket_desc','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.ticket_desc%></textarea>
													 
													<span id="span_txt_ticket_desc">(8000 Characters) </span>
											</div>
											<div class="form-element6">
												<label >Report Time<font color="#ff0000">*</font>: </label>
														<input type="text" size="16" name="txt_ticket_report_time" id="txt_ticket_report_time" 
														value="<%=mybean.ticket_report_time%>" class="form-control datetimepicker">
											</div>
											<%
												if (mybean.config_service_ticket_type.equals("1")) {
											%>
											<div class="form-element6">
												<label >Type:</label>
													<select name="dr_ticket_tickettype_id" id="dr_ticket_tickettype_id" class="form-control"> <%=mybean.PopulateTicketType()%>
													</select>
											</div>
											<%
												}
											%>
											<div class="row"></div>
											<div class="form-element6">
												<label >Status<font color="red">*</font>: </label>
													<select name="dr_ticket_ticketstatus_id" id="dr_ticket_ticketstatus_id" class="form-control">
														<%=mybean.PopulateStatus()%> </select>
											</div>
											<div class="form-element6">
												<label >Source<font color="red">*</font>: </label>
													<select name="dr_ticket_ticketsource_id" id="dr_ticket_ticketsource_id" class="form-control">
														<%=mybean.PopulateSourceType()%>
													</select>
											</div>
											<div class="form-element6">
												<label >Priority<font color="red">*</font>: </label>
													<select name="dr_ticket_priorityticket_id" class="form-control" id="dr_ticket_priorityticket_id">
														<%=mybean.PopulateTicketPrioirty()%>
													</select>
											</div>
											<div class="form-element6">
												<label>Department<font color="red">*</font>: </label>
													<select name="dr_ticket_dept_id" class="form-control" onchange="populateCatogery();" id="dr_ticket_dept_id"> 
													<%=mybean.PopulateDepartment()%>
													</select>
											</div>
											
											<%
												if (mybean.config_service_ticket_cat.equals("1")) {
											%>
											<div class="form-element6">
												<label >Category:</label> 
												 <span id="categoryHint"> <%=mybean.PopulateTicketCategory(mybean.ticket_ticket_dept_id, mybean.comp_id)%> </span>
													<input type="hidden" id="txt_ticket_report_time" name="txt_ticket_report_time" value="<%=mybean.ticket_report_time%>" />
											</div>
											<%
												}
											%>
											<div class="form-element6">
												<label >Executive<font color="red">*</font>: </label>
													<select name="dr_ticket_emp_id" class="form-control" id="dr_ticket_emp_id"> 
													<%=mybean.PopulateAllExecutive()%>
													</select>
											</div>
											<div class="form-element6">
												<label >Email CC:</label>
												<input name ="txt_ticket_cc" type="text" class="form-control" id="txt_ticket_cc"
												 value="<%=mybean.ticket_cc%>" size="52" maxlength="1000"/>
											</div>
										<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label >Entry By:</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.unescapehtml(mybean.entry_by)%>" />
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label >Entry Date:</label>
													<%=mybean.entry_date%>
													<input type="hidden" id="entry_date" name="entry_date" value="<%=mybean.entry_date%>" />
											</div>
											<%
												}
											%>
											
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label >Modified By:</label> 
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input type="hidden" id="modified_by" name="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>" />
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6">
												<label >Modified Date:</label> 
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														id="modified_date" value="<%=mybean.modified_date%>" />
											</div>
											<%
												}
											%>
											<center>
											<div class="form-element12">
												<%
													if (mybean.status.equals("Update")) {
												%>
												<input type="hidden" id="update_button" name="update_button" value="yes" />
												 <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton"
													value="Update Ticket" onClick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Ticket" />
												<%
													}
												%>
												<input type="hidden" name="ticket_id" value="<%=mybean.ticket_id%>" />
												</div>
											</center>
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

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_ticket_subject.focus();
	}
	function frmSubmit() {
		document.formcontact.submit();
	}
</script>
	<script type="text/javascript">
	function populateCatogery(){
		var  department_id= $('#dr_ticket_dept_id').val();
		  showHint('../service/report-check.jsp?ticket_category=ticket_update&ticket_dept_id='+department_id, 'categoryHint');
	}
	
	
	</script>
</body>
</HTML>