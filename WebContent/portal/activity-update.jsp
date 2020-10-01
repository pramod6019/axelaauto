<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Activity_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
     
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>

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
						<h1><%=mybean.status%> Activity</h1>
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
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="activity.jsp"> Activities</a> &gt;</li>
						<li><a href="../portal/activity-list.jsp?all=recent">List
								Activities</a></li>
						<%
							if (mybean.status.equals("Update")) {
						%>
						<li>&gt;<a
							href="activity-list.jsp?activity_id=<%=mybean.activity_id%>"><%=mybean.activity_title%></a>
						</li>
						<%
							}
						%>
						<li></li> &gt;
						<a href="activity-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
							Activity</a>:
						</li>
					</ul>

					<!-- END PAGE BREADCRUMBS -->
					
						<div class="tab-pane" id="">
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>
										Activity
									</div>
								</div>
								<center><font color="#ff0000"><b><%=mybean.msg%></b></font></center>
								<div class="portlet-body">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="Frmtasks" class="form-horizontal" method="post">
											<%
												if (!mybean.activity_contact_id.equals("")
														&& !mybean.activity_contact_id.equals("0")) {
											%>
											<span id="span_activity_contact_person"> <input
												name="txt_activity_contact_person" type="hidden"
												class="form-control"
												value="<%=mybean.activity_contact_person%>" size="40"
												maxlength="255" />
											</span>
											<%
												}
											%>
											<div>
												<input type="hidden" name="contact_id" id="contact_id"
													value="<%=mybean.contact_id%>"> <input
													type="hidden" name="activity_contact_id"
													id="activity_contact_id"
													value="<%=mybean.activity_contact_id%>">
											</div>
											<div>
												<center>
													<font size="1">Form fields marked with a red asterisk <b><font
															color="#ff0000">*</font> </b>are required. </font>
												</center>
											</div>

											<div class="form-element6">
												<label> Executive<font color=red>*</font>: </label>
													<select name="list_executives" class="form-control"
														id="list_executives" >
														<%=mybean.PopulateExe()%>
													</select>
											</div>
											<div class="form-element6">
												<label> Type<font color=red>*</font>: </label>
													<select name="drop_activity_type_id" class="form-control"
														visible="true">
														<%=mybean.PopulateActivitytype()%>
													</select>
											</div>
										
											<div class="form-element6">
												<label> Description <font color=red>*</font>: </label>
													<textarea name="txt_activity_desc" cols="37" rows="4"
														class="form-control" id="txt_activity_desc"
														onKeyUp="charcount('txt_activity_desc', 'span_txt_activity_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.activity_desc%></textarea>
											</div>
												<div class="form-element6">
												<label>Title<font color=red>*</font>: </label>
													<input name="txt_activity_title" type="text"
														class="form-control" id="txt_activity_title"
														value="<%=mybean.activity_title%>" maxlength="255" />
											</div>
											<div class="form-element3">
												<label>Start Time<font color=#ff0000><b>*</b></font>: </label>
														<input type="text" size="16" name="txt_start_time"
															id="txt_start_time"
															value="<%=mybean.start_datetime%>" class="form-control datetimepicker">
											</div>
											<div class="form-element3">
												<label>End Time<font color=#ff0000><b>*</b></font>: </label>
														<input type="text" size="16" name="txt_end_time"
															id="txt_end_time" value="<%=mybean.end_datetime%>"
															class="form-control datetimepicker"> 
											</div>
											<div class="form-element6">
												<label> Priority<font color=red>*</font>: </label>
													<select name="drop_activity_priority_id"
														class="form-control" visible="true">
														<%=mybean.PopulatePriority()%>
													</select>
											</div>
											<%
												if (!mybean.activity_contact_id.equals("")
														&& !mybean.activity_contact_id.equals("0")) {
											%>
											<div class="form-element6">
												<label> Contact<font color=red>*</font>: </label>
													<input type="hidden" name="contact"
														value="<%=mybean.client%>"> <input type="hidden"
														name="activity_contact_id"
														value="<%=mybean.activity_contact_id%>"> <input
														type="hidden" name="contact_id"
														value="<%=mybean.contact_id%>"> <input
														type="hidden" name="activity_id"
														value="<%=mybean.activity_id%>"> <span
														id="span_activity_contact_person"><%=mybean.unescapehtml(mybean.client)%></span>
											</div>
											<%
												}
											%>
										<div class="form-element12 btn-success"
												style="padding: 5px; font-size: 18px;">
												<center>Contact Info</center>
											</div>
											<br></br>
											<%
												if (mybean.activity_contact_id.equals("")
														|| mybean.activity_contact_id.equals("0")) {
											%>
											<div class="form-element6">
												<label> Contact<font color=red>*</font>: </label>
													<span id="span_activity_contact_person"> <input
														name="txt_activity_contact_person" type="text"
														class="form-control"
														value="<%=mybean.activity_contact_person%>" size="40"
														maxlength="255" />
													</span>
											</div>
											<div class="form-element3">
												<label> Phone 1<font color=red>*</font>: </label>
													<input name="txt_activity_Phone1" id="txt_activity_Phone1"
														type="text" class="form-control"
														value="<%=mybean.activity_Phone1%>" size="40"
														maxlength="10"
														onKeyUp="toPhone('txt_activity_Phone1','Phone 1')" />
											</div>
											<div class="form-element3">
												<label> Phone 2:</label>
													<input name="txt_activity_Phone2" id="txt_activity_Phone2" type="text"
														class="form-control" value="<%=mybean.activity_Phone2%>"
														size="40" maxlength="10" 
														onKeyUp="toPhone('txt_activity_Phone2','Phone 2')"/>
											</div>
											<div class="form-element6">
												<label> Venue<font color=red>*</font>: </label>
													<textarea name="txt_activity_venue" cols="37" rows="4"
														class="form-control" id="txt_activity_venue"
														onKeyUp="charcount('txt_activity_venue', 'span_txt_activity_venue','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.activity_venue%></textarea>
													<span id="span_txt_activity_venue"> 255 characters </span>
											</div>
											<%
												}
											%>
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label> Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
													<input name="entry_by" type="hidden" id="entry_by" style="top: 8px"
														value="<%=mybean.entry_by%>">
												</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label> Entry Date: <%=mybean.entry_date%></label>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>" style="top: 8px">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label> Modified By: </label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6">
												<label> Modified
													Date: </label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
											</div>
											<%
												}
											%>
											<div class="row"></div>
											<div>
												<center>
													<%
														if (mybean.status.equals("Add")) {
													%>
													<input name="addbutton" type="submit"
														class="btn btn-success" id="addbutton"
														value="Add Activity"
														onclick="return SubmitFormOnce(document.Frmtasks,this);" />
													<input type="hidden" name="add_button" value="yes" />
													<%
														} else if (mybean.status.equals("Update")) {
													%>
													<input type="hidden" name="update_button" value="yes" /> <input
														name="updatebutton" type="submit" class="btn btn-success"
														id="updatebutton" value="Update Activity"
														onclick="return SubmitFormOnce(document.Frmtasks,this);" />
													<input name="delete_button" type="submit"
														class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)"
														value="Delete Activity" />
													<%
														}
													%>
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
	<%@include file="../Library/js.jsp" %>
</body>
</HTML>
