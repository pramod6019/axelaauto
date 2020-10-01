<%-- 
    Document : ticket-add
    Created on: Feb 11, 2013
    Author   : Gurumurthy TS
--%>
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Ticket_Add" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1"> 
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
 <script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
		document.form1.txt_ticket_subject.focus()
		}
</script>
</HEAD>
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
						<h1><%=mybean.status%>
							Ticket
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
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="ticket.jsp">Tickets</a>&gt</li>
						<li><a href="ticket-list.jsp?all=yes"> List Tickets</a> &gt;</li>
						<li><a href=""><%=mybean.status%> Ticket</a><b>:</b></li>
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
										<center><%=mybean.status%> Ticket </center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk <font color=#ff0000>*</font> are required. </font>
											</center>
											<br>
											<div class="row">
												<div class="form-element6">
												<%
													if (!mybean.ticket_contact_id.equals("") && !mybean.ticket_contact_id.equals("0")) {
												%>
												
												<div class="form-element6">
													<label >Customer:&nbsp;</label>
														<b> <a href="../customer/customer-list.jsp?customer_id=<%=mybean.ticket_customer_id%>"><%=mybean.customer_name%> </a> </b>
												</div>
												<div class="form-element6">
													<label>Contact Name<font color="red">*</font>:</label>
														<b><a href="../customer/customer-contact-list.jsp?customer_id=<%=mybean.ticket_customer_id%>&contact_id=<%=mybean.ticket_contact_id%>">
														<%=mybean.contact_name%></a></b>
												</div>
												<%
													}
												%>
												</div>
												<div class="form-element6">
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
											
											<% if (mybean.search_contact.equals("yes")) { %>
											
												<div class="row">
													<div class="form-element2 ">
														<label>Contact<font color="red">*</font>:&nbsp; </label>
														<select name="dr_search_title" class="form-control" id="dr_search_title">
															<%=mybean.PopulateTitle(mybean.search_contact_title_id, mybean.comp_id)%>
														</select>
														<span>Title</span>
													</div> 
													
													<div class="form-element5 form-element-margin">
														<input name="txt_search_contact_fname" type="text" class="form-control"
															id="txt_search_contact_fname" value="<%=mybean.search_contact_fname%>"
															size="30" maxlength="255" onkeyup="ShowNameHint();" />
														<span>First Name</span>
													</div> 
															
													<div class="form-element5 form-element-margin">
														<input name="txt_search_contact_lname" type="text" class="form-control"
															id="txt_search_contact_lname" value="<%=mybean.search_contact_lname%>"
															size="30" maxlength="255" onkeyup="ShowNameHint();" /> 
														<span>Last Name</span>
													</div>
												</div>
												
												<div class="row">
													<div class="form-element6">
														<label>Email 1:</label>
														<input name="txt_search_contact_email1" type="text" class="form-control"
															id="txt_search_contact_email1" size="32" maxlength="100"
															value="<%=mybean.search_contact_email1%>"/>
														<span id="showcontactsemail" class="hint" ></span>
													</div>
													
													<div class="form-element3">
														<label>Mobile 1<font color="#ff0000">*</font>: </label>
														<input name="txt_search_contact_mobile1" type="text" class="form-control"
															id="txt_search_contact_mobile1" size="32" maxlength="13" 
															onkeyup="toPhone('txt_contact_mobile1','Contact Mobile1');"
															value="<%=mybean.search_contact_mobile1%>" />(91-9999999999)
														<span id="showcontactsmobil1" class="hint" ></span>
													</div>

													<div class="form-element3">
														<label>Phone 1<font color="#ff0000">*</font>: </label>
														<input name="txt_search_contact_phone1" type="text" class="form-control"
															id="txt_search_contact_phone1" value="<%=mybean.search_contact_phone1%>"
															onkeyup="toPhone('txt_contact_phone1','Contact Phone1');"
															size="32" maxlength="14" />(91-80-33333333)
														<span id="showcontactsphone1" class="hint"></span>
													</div>
												</div>
											
											<% } %>
											
											<% if (!mybean.ticket_branch_id.equals("0")) { %>
												<div class="form-element6">
													<label >Branch<font color="#ff0000">*</font>:</label>
														<b><a href="../portal/branch-summary.jsp?branch_id=<%=mybean.ticket_branch_id%>"><%=mybean.branch_name%></a></b>
												</div>
												
											
											<% } else { %>
											
											<div class="form-element6">
												<label >Branch<font color="#ff0000">*</font>:</label>
													<select id="dr_ticket_branch_id" name="dr_ticket_branch_id" class="form-control">
														<%=mybean.PopulateBranch(mybean.ticket_branch_id, mybean.param, mybean.branch_type, request)%>
													</select>
											</div>
											
											<% } %>
											
											<div class="form-element6">
												<label>Subject<font color="#ff0000">*</font>:</label>
													<input name="txt_ticket_subject" type="text" class="form-control"
													 id="txt_ticket_subject" value="<%=mybean.ticket_subject%>" size="52" maxlength="255" />
													 <input name="ticket_parent_id" type="hidden" id="ticket_parent_id" 
													 value="<%=mybean.ticket_parent_id%>" />
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label >Description<font color="#ff0000">*</font>:</label>
													<textarea name="txt_ticket_desc" cols="50" rows="5" class="form-control" id="txt_ticket_desc" 
													onKeyUp="charcount('txt_ticket_desc', 'span_txt_ticket_desc',' <font color=red>({CHAR} characters left)</font>', '8000')"> <%=mybean.ticket_desc%></textarea>
													
													<span id="span_txt_ticket_desc">(8000 Characters) </span>
											</div>
											<%
												if (mybean.config_service_ticket_type.equals("1")) {
											%>
											<div class="form-element6">
												<label >Type:&nbsp;</label>
												<select name="dr_ticket_tickettype_id" class="form-control">
													<%=mybean.PopulateTicketType()%>
												</select>
											</div>
											<%
												}
											%>
										
											<div class="form-element6">
												<label >Source<font color="red">*</font>:</label>
													<select name="dr_ticket_ticketsource_id" class="form-control">
													 <%=mybean.PopulateSourceType()%> </select>
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label >Priority<font color="red">*</font>:</label>
													<select name="dr_ticket_priorityticket_id" class="form-control" id="dr_ticket_priorityticket_id">
													 <%=mybean.PopulateTicketPriority()%> </select>
											</div>
											
											<div class="form-element6">
												<label >Department<font color="red">*</font>:</label>
													<select name="dr_ticket_dept_id" class="form-control" onchange="populateCatogery(this);"
													id="dr_ticket_dept_id"> <%=mybean.PopulateDepartment()%> </select>
											</div>

											<%
												if (mybean.config_service_ticket_cat.equals("1")) {
											%>

											<div class="form-element6">
												<label >Category:&nbsp;</label>
												<span id="categoryHint">
												 <%=mybean.PopulateTicketCategory(mybean.ticket_ticket_dept_id, mybean.comp_id)%> </span>
											</div>
											<%
												}
											%>
											<div class="form-element6">
												<label>Executive<font color="red">*</font>:</label>
													<select name="dr_ticket_emp_id" class="form-control" id="dr_ticket_emp_id"> <%=mybean.PopulateAllExecutive()%> </select>
											</div>
											<div class="form-element6">
												<label >Email CC:&nbsp;</label>
												 <input name="txt_ticket_cc" type="text" class="form-control" id="txt_ticket_cc" value="<%=mybean.ticket_cc%>" size="52" maxlength="1000" />
											</div>
											<center>
											<div class="form-element12">
												<%if(mybean.status.equals("Add")){%>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton" value="Add Ticket"
													onClick="return SubmitFormOnce(document.form1, this);" /> <input
													type="hidden" name="add_button" value="yes" />
												<%}%>
												</div>
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

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript">
	 function populateCatogery(){
		var  department_id= $('#dr_ticket_dept_id').val();
// 		alert(department_id);
		  showHint('../service/report-check.jsp?ticket_category=ticket_add&ticket_dept_id='+department_id, 'categoryHint');
	} 
	
	
	</script>
</body>
</HTML>
                    
