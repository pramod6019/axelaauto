<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.ManageJobCardPriority_Update" scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp" %> 
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
						<h1><%=mybean.status%> Job Card Priority</h1>
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
							<li><a href="managejobcardpriority.jsp?all=yes">List Job Card Priority</a> &gt;</li>
							<li><a href="managejobcardpriority-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Job Card Priority</a><b>:</b></li>
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
										<%=mybean.status%>
										Job Card Priority
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font>
										</center><br>
										<form name="form1" runat="server" method="post" class="form-horizontal">
											<div class="form-element6">
												<label>Name<font color=red>*</font>:</label>
													<input name="txt_priorityjc_name" id="txt_priorityjc_name" type="text" class="form-control"
														value="<%=mybean.priorityjc_name%>" maxlength="255" />
											</div>
											
											<div class="form-element6">
												<label>Description<font color=red>*</font>:</label>
													<input name="txt_priorityjc_desc" type="text"
														class="form-control" value="<%=mybean.priorityjc_desc%>"maxlength="255" />
											</div>
											
											<div class="form-element6">
												<label>Due Hours<font color=#ff0000>*</font>:</label>
														<input type="text"  name="txt_priorityjc_duehrs"
															id="txt_priorityjc_duehrs" value="<%=mybean.priorityjc_duehrs%>"
															class="form-control timepicker">
											</div>
											
											<div class="form-element6">
												<label>Level-1 Hours:</label>
														<input type="text" name="txt_priorityjc_trigger1_hrs"
															id="txt_priorityjc_trigger1_hrs"
															value="<%=mybean.priorityjc_trigger1_hrs%>"
															class="form-control timepicker">
											</div>
											
											<div class="form-element6">
												<label>Level-2 Hours:</label>
														<input type="text" name="txt_priorityjc_trigger2_hrs"
															id="txt_priorityjc_trigger2_hrs" value="<%=mybean.priorityjc_trigger2_hrs%>"
															class="form-control timepicker">
											</div>
											
											<div class="form-element6">
												<label>Level-3 Hours:</label>
														<input type="text" name="txt_priorityjc_trigger3_hrs"
															id="txt_priorityjc_trigger3_hrs" value="<%=mybean.priorityjc_trigger3_hrs%>"
															class="form-control timepicker"> 
											</div>
											
											<div class="form-element6">
												<label>Level-4 Hours:</label>
														<input type="text" name="txt_priorityjc_trigger4_hrs"
															id="txt_priorityjc_trigger4_hrs" value="<%=mybean.priorityjc_trigger4_hrs%>"
															class="form-control timepicker"> 
											</div>
											
											<div class="form-element6">
												<label>Level-5 Hours:</label>
														<input type="text" name="txt_priorityjc_trigger5_hrs"
															id="txt_priorityjc_trigger5_hrs" value="<%=mybean.priorityjc_trigger5_hrs%>"
															class="form-control timepicker"> 
											</div>
											
											<%if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {%>
											
											<div class="form-element6">
												<label>Entry By:</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
											</div>
											
											<%}%>
											<%if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {%>
											<div class="form-element6">
												<label>Entry Date:</label>
													<%=mybean.unescapehtml(mybean.entry_date)%>
													<input name="entry_by" type="hidden" id="entry_date"
														value="<%=mybean.unescapehtml(mybean.entry_date)%>">
											</div>
											
											<%}%>
											<%if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {%>
											
											<div class="form-element6">
												<label>Modified By:</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>">
											</div>
											
											<%}%>
											<%if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {%>
											
											<div class="form-element6">
												<label>Modified Date:</label>
													<%=mybean.unescapehtml(mybean.modified_date)%>
													<input name="modified_by" type="hidden" id="modified_date"
														value="<%=mybean.unescapehtml(mybean.modified_date)%>">
											</div>
											<%}%>
											<center>
												<%if (mybean.status.equals("Add")) {%>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton"
													value="Add Job Card Priority"
													onclick="return SubmitFormOnce(document.form1,this);" /> <input
													type="hidden" id="add_button" name="add_button" value="yes" />
												<%} else if (mybean.status.equals("Update")) {%>
												<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update Job Card Priority"
													onclick="return SubmitFormOnce(document.form1,this);" /> <input
													id="update_button" name="update_button" type="hidden"
													value="yes" /> <input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)"
													value="Delete Job Card Priority" />
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
  document.form1.txt_priorityjc_name.focus()
}
        </script>
</body>
</html>
