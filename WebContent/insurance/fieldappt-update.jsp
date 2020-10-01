<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.FieldAppt_Update" scope="request" />
<% mybean.doPost(request, response); %>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
<style>
.margin {
	top: 8px;
}
</style>
<script language="JavaScript" type="text/javascript">
	function FieldApptCheck() {
		var fieldapptdate = document.getElementById("txt_fieldappt_date").value;
		var exe_id = document.getElementById("dr_field_executive").value;

		showHint('../insurance/fieldappt-check.jsp?appt=yes&fieldapptemp_id=' + exe_id
				+ '&fieldapptdrivedate=' + fieldapptdate, 'calHint');
	}
</script>
</HEAD>

<body onLoad="FieldApptCheck();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%> Field Appointment </h1>
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
						<li><a href="../insurance/index.jsp">Isurance</a> &gt;</li>
						<li><a href="insurance.jsp">Field Appointment</a> &gt;</li>
						<li><a href="fieldappt-list.jsp?all=recent">List Field Appointment</a> &gt;</li>
						<li><a href="fieldappt-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Field Appointment</a><b>:</b></li>
					</ul>
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="container-fluid">
								<div class="form-element8">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">
												<center><%=mybean.status%> Field Appointment</center>
											</div>
										</div>
										<div class="portlet-body portlet-empty container-fluid">
											<div class="tab-pane" id="">
											
												<!-- START PORTLET BODY -->
												<form name="formcontact" method="post"
													class="form-horizontal">
													<center>
														<font size="">Form fields marked with a red
															asterisk <b><font color="#ff0000">*</font></b> are required.
														</font>
													</center><br></br>
					                                 <input type="hidden" id="txt_fieldappt_contact_id" name="txt_fieldappt_contact_id" value="<%=mybean.insurenquiry_contact_id%>" />
													 <input type="hidden" id="txt_fieldappt_contact_name" name="txt_fieldappt_contact_name" value="<%=mybean.insurenquiry_contact_name%>" />
													 <input type="hidden" id="txt_fieldappt_insurenquiry_date" name="txt_fieldappt_insurenquiry_date" value="<%=mybean.insurenquiry_date%>" />
								
													<div class="form-element6 ">
														<label> Contact:&nbsp;</label>
															<b><%=mybean.fieldappt_contact_link %></b>
													</div>

													<div class="form-element6 ">
														<label> Enquiry Date:&nbsp;</label>
															<%=mybean.strToShortDate(mybean.insurenquiry_date)%>
													</div>
													<div class="form-element6 form-element-margin">
														<label> Enquiry Id: &nbsp;</label>
															<b><%=mybean.fieldappt_insurenquiry_link %></b>
													</div>
													
													<div class="form-element6 ">
												<label>Executive<font
													color="#ff0000">*</font>:
												</label>
													<select name="dr_field_executive" id="dr_field_executive" class="form-control"  onchange="FieldApptCheck();">
														<%=mybean.PopulateFieldExecutive(mybean.comp_id, request)%>
													</select>
											</div>
											
											<div class="form-element6 ">
												<label>Appt Type<font
													color="#ff0000">*</font>:
												</label>
													<select name="dr_fieldappt_type_id" id="dr_fieldappt_type_id"
														class="form-control">
														<%=mybean.PopulateApptType(mybean.comp_id, request)%>
													</select>
											</div>
													
													<div class="row">
													<div class="form-element6 ">
														<label>Field Appointment
															Date<font color=#ff0000><b>*</b></font>:&nbsp;
														</label>
																<input type="text" size="16" name="txt_fieldappt_date" id="txt_fieldappt_date"
																	value="<%=mybean.fieldapptdate%>" class="form-control datetimepicker" onchange="FieldApptCheck();">
													</div>
													</div>

													<div class="row">
													<div class="form-element6 ">
														<label> Notes:&nbsp;
														</label>
															<textarea name="txt_fieldappt_notes" cols="70" rows="4"
																class="form-control" id="txt_fieldappt_notes"><%=mybean.fieldappt_notes%></textarea>
													</div>
													</div>



													<%
														if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
													%>
													<div class="row">
													<div class="form-element6">
														<label> Entry By:&nbsp;</label>
															<%=mybean.unescapehtml(mybean.entry_by)%>
															<input name="entry_by" type="hidden" id="entry_by"
																value="<%=mybean.entry_by%>">
													</div>
													<div class="form-element6">
														<label> Entry Date:&nbsp;</label>
															<%=mybean.entry_date%>
															<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"> 
													</div>
													</div>
													<%
														}
													%>
													<%
														if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
													%>
													<div class="row">
													<div class="form-element6">
														<label>Modified By:&nbsp;</label>
															<%=mybean.unescapehtml(mybean.modified_by)%>
															<input name="modified_by" type="hidden" id="modified_by"
																value="<%=mybean.modified_by%>" />
													</div>
													<div class="form-element6">
														<label>Modified Date:&nbsp;</label>
															<%=mybean.modified_date%>
															<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
													</div>
													</div>
													<%
														}
													%> 

													<center>
														<%
															if (mybean.status.equals("Add")) {
														%>
														<input name="add_button" type="submit"
															class="button btn btn-success" id="add_button"
															onclick="return SubmitFormOnce(document.formcontact, this);"
															value="Add Field Appointment" />
														<input type="hidden" name="add_button" value="yes"/>
														<%
															} else if (mybean.status.equals("Update")) {
														%>
														<input name="update_button" type="submit"
															class="button btn btn-success" id="update_button"
															onclick="return SubmitFormOnce(document.formcontact, this);"
															value="Update Field Appointment" />
														<input type="hidden" name="update_button" value="yes"/>
														<input name="delete_button" type="submit" class="button btn btn-success"
															id="delete_button" OnClick="return confirmdelete(this)"
															value="Delete Field Appointment" />
														<%
															}
														%>
													</center>
													<center>
														<input type="hidden" name="fieldappt_id" value="<%=mybean.fieldappt_id%>">
														<input name="enquiry_id" type="hidden" id="insurenquiry_id" value="<%=mybean.fieldappt_insurenquiry_id%>">
													</center>
												</form>
											</div>
										</div>
									</div>
								</div>


								<!-- 2nd ------------portlet -->
								<div class="form-element4">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">
												<center>Field Appointment Calendar</center>
											</div>
										</div>
										<div class="portlet-body portlet-empty" style="min-height: 560px;">
											<center>
												<div class="tab-pane" id="">
													<!-- START PORTLET BODY -->
													<div id="calHint"><%=mybean.strHTML%></div>
												</div>
											</center>
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
	<!-- END CONTAINER ------------>


	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
</body>
</HTML>
