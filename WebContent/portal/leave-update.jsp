<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Leave_Update" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%> 

</style>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	
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
						<h1>Add Leave</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!--- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp?all=yes">Home</a> &gt;</li>
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="target.jsp?all=yes">Target</a>&gt;</li>
						<li><a href="leave-list.jsp?all=yes">List Leave</a> &gt;</li>
						<li><a href="leave-update.jsp?add=yes">Add Leave</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- BODY START -->
							<form name="form1" id="form1" class="form-horizontal" method="post">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>


								<!-- 	PORTLET customner details-->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Leave Details</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<!--     start customer details -->

											<center>
												<font size="2">Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.<br></br>
												</font>
											</center>
											
											<div class="form-element6">
												<label> Executive<font color=red>*</font>: </label>
												<div>
													<select name="dr_emp_id" id="dr_emp_id" class="dropdown form-control">
														<%=mybean.PopulateExecutives(mybean.leave_emp_id, mybean.comp_id)%>
													</select> 
												</div>
											</div>
											
											<div class="form-element6">
												<label> Leave Type<font color=red>*</font>: </label>
												<div>
													<select name="dr_leavetype_id" id="dr_leavetype_id" class="dropdown form-control">
														<%=mybean.PopulateLeaveType()%>
													</select> 
												</div>
											</div>
											<div class="row">
												<div class="form-element6 form-element">
													<div class="form-element12">
														<label >From Date<font color="#ff0000">*</font>: </label>
														<input name="txt_from_date" id="txt_from_date" value="<%=mybean.from_date%>"
															class="form-control datetimepicker" type="text" maxlength="10" />
													</div>
													
													<div class="form-element12">
														<label >To Date<font color="#ff0000">*</font>: </label>
														<input name="txt_to_date" id="txt_to_date" value="<%=mybean.to_date%>"
															class="form-control datetimepicker" type="text" maxlength="10" />
													</div>
												</div>
												
												<div class="form-element6">
													<label > Notes: </label>
													<textarea name="txt_leave_note" cols="40" rows="4" class="form-control" id="txt_leave_note"
														onKeyUp="charcount('txt_leave_note', 'span_txt_leave_note','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.leave_notes%></textarea>
													<span id="span_txt_leave_note"> (8000 Characters)</span>
												</div>
											</div>
											<div class="row">
												<div class="form-element6">
													<label> Active:&nbsp;  </label>
													<input id="chk_leave_active" type="checkbox" name="chk_leave_active"
														<%=mybean.PopulateCheck(mybean.leave_active)%> />
												</div>
											</div>
<!-- 											// -->
											<% if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
											<div class="form-element6">
												<label>Entry By:&nbsp; </label>
												<span >
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input type="hidden" name="txt_entry_by" value="<%=mybean.entry_by%>"> 
												</span>
											</div>
											<% } %>
											<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>

											<div class="form-element6">
												<label>Entry Date:&nbsp; </label>
												<span >
													<%=mybean.entry_date%><input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
												</span>
											</div>

											<% } %>

											<% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
											<div class="form-element6">
												<label>Modified By:&nbsp; </label>
												<span >
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input type="hidden" name="txt_modified_by" value="<%=mybean.modified_by%>"> 
												</span>
											</div>

											<% } %>


											<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>

											<div class="form-element6">
												<label>Modified Date:&nbsp; </label>
												<span >
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"> 
												</span>
											</div>

											<% } %>

											<div class="form-element12">
												<center>
													<label> 
													<% if (mybean.status.equals("Add")) { %>
														<input name="button" type="submit" class="btn btn-success" id="button" value="Add Leave"
														onClick="return SubmitFormOnce(document.form1, this);" />
														<input type="hidden" name="add_button" value="yes">
														<% } else if (mybean.status.equals("Update")) { %>
														<input type="hidden" name="update_button" value="yes">
														<input name="button" type="submit" class="btn btn-success" id="button" value="Update Leave"
														onClick="return SubmitFormOnce(document.form1, this);" />
														<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)" value="Delete Leave" />
														<% } %>
													</label>
												</center>
											</div>
											
											
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

	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%> 

</script>
</body>
</HTML>

