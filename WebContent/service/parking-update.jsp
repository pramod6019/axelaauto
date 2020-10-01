<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Parking_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="../Library/css.jsp"%>
</HEAD>
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
						<h1><%=mybean.status%>
							&nbsp;Parking
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
						<li><a href="parking-list.jsp?all=yes">List Parking</a>&gt;</li>
						<li><a href="parking-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Parking</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%>
											&nbsp;Parking
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font>
											</center>
											<div class="form-element6 form-element-center">
												<label> Branch<font color=red>*</font>: </label>
													<select name="dr_parking_branch_id" class="form-control"
														id="dr_parking_branch_id">
														<%=mybean.PopulateBranch(mybean.parking_branch_id, "", "1,3","", request)%>
													</select>
											</div>
											<div class="form-element6 form-element-center">
												<label> Name<font color=red>*</font>: </label>
													<input name="txt_parking_name" id="txt_parking_name"
														type="text" class="form-control"
														value="<%=mybean.parking_name%>" size="50" maxlength="255" />
											</div>
											<div class="form-element6 form-element-center">
												<label>Active:</label>
													<input id="chk_parking_active" type="checkbox"
														name="chk_parking_active"
														<%=mybean.PopulateCheck(mybean.parking_active)%> />
											</div>
											<div class="form-element6 form-element-center">
												<label>Notes:</label>
													<textarea name="txt_parking_notes" cols="70" rows="4"
														class="form-control"><%=mybean.parking_notes%></textarea>
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry By:</label>
												<div class="txt-align">
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry Date:</label>
												<div class="txt-align">
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Modified By:</label>
												<div class="txt-align">
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Modified Date:</label>
												<div class="txt-align">
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Add")) {
											%>
											<center>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton" value="Add Parking"
													onclick="return SubmitFormOnce(document.form1,this);" /> <input
													type="hidden" id="add_button" name="add_button" value="yes" />
											</center>
											<%
												} else if (mybean.status.equals("Update")) {
											%>
											<center>
												<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update Parking"
													onclick="return SubmitFormOnce(document.form1,this);" /> <input
													id="update_button" name="update_button" type="hidden"
													value="yes" /> <input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Parking" />
												<%
													}
												%>
											</center>
										</form>

									</div>
								</div>
							</div>
						</div>
					</div>
					<%@include file="../Library/admin-footer.jsp"%>
					<%@include file="../Library/js.jsp"%>
					<script language="JavaScript" type="text/javascript">
						function FormFocus() { //v1.0
							document.form1.txt_priorityjc_name.focus()
						}
					</script>
</body>
</HTML>
