<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.insurance.ManageInsurFollowupPriority_Update"
	scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</head>
<body onLoad="FormFocus()"
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
						<h1><%=mybean.status%>&nbsp;Insurance Follow-up Priority</h1>
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
						<li><a href="manageinsurfollowuppriority.jsp?all=yes">List
								Insurance Follow-up Priority</a> &gt;</li>
						<li><a href="manageinsurfollowuppriority-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Insurance
								Follow-up Priority</a><b>:</b> <li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b> <%=mybean.msg%> <br></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Insurance Follow-up Priority
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.<br>
											</font>
										</center>
										<form name="form1" runat="server" method="post"
											class="form-horizontal">
											<div class="form-element6">
												<label> Name<font color=red>*</font>: </label>
													<input name="txt_priorityinsurfollowup_name"
														id="txt_priorityinsurfollowup_name" type="text"
														class="form-control"
														value="<%=mybean.priorityinsurfollowup_name%>" 
														maxlength="255" />
											</div>
											
											<div class="form-element6">
												<label >Description<font color="#ff0000">*</font><b>:</b></label>
													<input name="txt_priorityinsurfollowup_desc" type="text"
														class="form-control" value="<%=mybean.priorityinsurfollowup_desc%>" 
														maxlength="255" />
											</div>
											
											<div class="form-element6">
												<label>Due Hours<font color=#ff0000><b>*</b></font>: </label>
														<input type="text" 
															name="txt_priorityinsurfollowup_duehrs"
															id="txt_priorityinsurfollowup_duehrs"
															value="<%=mybean.priorityinsurfollowup_duehrs%>"
															class="form-control timepicker"> 
											</div>
											
											<div class="form-element6">
												<label>Level-1 Hours:</label>
														<input type="text"
															name="txt_priorityinsurfollowup_trigger1_hrs"
															id="txt_priorityinsurfollowup_trigger1_hrs"
															value="<%=mybean.priorityinsurfollowup_trigger1_hrs%>"
															class="form-control timepicker"> 
											</div>
											
											<div class="form-element6">
												<label>Level-2 Hours:</label>
														<input type="text" 
															name="txt_priorityinsurfollowup_trigger2_hrs"
															id="txt_priorityinsurfollowup_trigger2_hrs"
															value="<%=mybean.priorityinsurfollowup_trigger2_hrs%>"
															class="form-control timepicker">
											</div>
											
											<div class="form-element6">
												<label>Level-3 Hours:</label>
														<input type="text" 
															name="txt_priorityinsurfollowup_trigger3_hrs"
															id="txt_priorityinsurfollowup_trigger3_hrs"
															value="<%=mybean.priorityinsurfollowup_trigger3_hrs%>"
															class="form-control timepicker"> 
											</div>
											
											<div class="form-element6">
												<label>Level-4 Hours:</label>
														<input type="text" 
															name="txt_priorityinsurfollowup_trigger4_hrs"
															id="txt_priorityinsurfollowup_trigger4_hrs"
															value="<%=mybean.priorityinsurfollowup_trigger4_hrs%>"
															class="form-control timepicker"> 
											</div>
											
											<div class="form-element6">
												<label>Level-5 Hours:</label>
														<input type="text" 
															name="txt_priorityinsurfollowup_trigger5_hrs"
															id="txt_priorityinsurfollowup_trigger5_hrs"
															value="<%=mybean.priorityinsurfollowup_trigger5_hrs%>"
															class="form-control timepicker"> 
											</div>	
														
														
															<!-- <span
															class="input-group-btn">
																<button class="btn default date-set" type="button">
																	<i class="fa fa-calendar"></i>
																</button>
														</span> -->
											
											<%-- <% if (mybean.status.equals("Update")&& !(mybean.priorityinsurfollowup_entry_by == null) && !(mybean.priorityinsurfollowup_entry_by.equals("")) ) { %>
											<div class="form-group">
												<label class="control-label col-md-4">Entry By:</label>
												<div class="txt-align">
													<%=mybean.unescapehtml(mybean.priorityinsurfollowup_entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.priorityinsurfollowup_entry_by)%>">
												</div>
											</div>
											<%}%>
											<% if (mybean.status.equals("Update") && !(mybean.priorityinsurfollowup_entry_date == null) && !(mybean.priorityinsurfollowup_entry_date.equals("")) ) { %>
											<div class="form-group">
												<label class="control-label col-md-4">Entry Date:</label>
												<div class="txt-align">
													<%=mybean.priorityinsurfollowup_entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.priorityinsurfollowup_entry_date%>">
												</div>
											</div>
											<%}%> --%>
											<% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
											<div class="form-element6">
												<label >Modified By:</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>">
											</div>
											<% } %>
											<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
											<div class="form-element6">
												<label>Modified Date:</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
											</div>
												
											<% } %>
											<div class="row"></div>
											<div class="form-element12">
											<center>
												<% if (mybean.status.equals("Update")) { %>
												<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update Insurance Follow-up Priority" /> <input
													name="update_button" type="hidden" value="yes" />

												<% } %>
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_priorityInsurfollowup_name.focus()
	}
</script>
	</body>
</html>
