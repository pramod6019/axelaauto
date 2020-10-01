<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.TestDrive_Mileage"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
	<%@include file="../Library/css.jsp" %>
<script type="text/javascript" src="../Library/dynacheck-post.js"></script>
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

<!-- <script type="text/javascript"
	$(function() {

		     $( "#txt_testdrive_out_time" ).datetimepicker({
		       addSliderAccess: true,
		 	  sliderAccessArgs: {touchonly: false},
		       dateFormat: "dd/mm/yy",
		 	  hour: 10,
		 	  stepMinute: 5,
		 	  minute: 00
		     });
		 	$( "#txt_testdrive_in_time" ).datetimepicker({
		       addSliderAccess: true,
		 	  sliderAccessArgs: {touchonly: false},
		       dateFormat: "dd/mm/yy",
		 	  hour: 10,
		 	  stepMinute: 5,
		 	  minute: 00
		     });	
		$("#txt_testdrive_license_valid").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
	});
</script> -->
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Test Drive Mileage</h1>
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="testdrive-list.jsp?all=yes">List Test Drives</a> &gt;</li>
						<li><a href="testdrive-mileage.jsp">Update Mileage</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>


							<form name="formtestdrive" method="post" class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Update Mileage
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<center>
												Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</center>
											<br>
											<div class="form-element6">
												<label>Customer:
													&nbsp;</label>
													<a href="../customer/customer-list.jsp?customer_id=<%=mybean.testdrive_customer_id%>"><%=mybean.customer_name%>
														(<%=mybean.testdrive_customer_id%>)</a>
											</div>


											<div class="form-element6">
												<label>Enquiry
													No.:&nbsp;</label>
													<a	href="enquiry-dash.jsp?enquiry_id=<%=mybean.testdrive_enquiry_id%>"><%=mybean.enquiry_no%></a>
											</div>

											<div class="form-element6">
												<label>Vehicle: &nbsp;</label>
													<a	href="../sales/managetestdrivevehicle.jsp?testdriveveh_id=<%=mybean.testdriveveh_id%>"><%=mybean.vehicle_name%></a>
											</div>

											<div class="form-element6">
												<label>Location: &nbsp;</label>
													<%=mybean.location_name%>
											</div>

											<div class="form-element6">
												<label>Test Drive
													Time: &nbsp;</label>
													<%=mybean.testdrivetime%>
											</div>

											<div class="form-element6">
												<label>Sales Consultant<font
													color="#ff0000">*</font>: &nbsp;
												</label>
													<%=mybean.executive_name%>
											</div>

												<div class="form-element6">
													<label>Driver:</label>
														<select name="dr_driver" class="form-control"
															id="dr_driver">
															<option value=0>Select Driver&nbsp;</option>
															<%=mybean.PopulateDriver()%>
														</select>
											</div>

												<div class="form-element6">
													<label>DL No.: &nbsp;</label>
														<input name="txt_testdrive_license_no" type="text"
															class="form-control" id="txt_testdrive_license_no"
															value="<%=mybean.testdrive_license_no%>" size="32"
															maxlength="100">

											</div>
<div class="row"></div>
												<div class="form-element6">
													<label>DL Address: &nbsp;</label>
														<textarea name="txt_testdrive_license_address" cols="50"
															rows="5" class="form-control"
															id="txt_testdrive_license_address"
															onKeyUp="charcount('txt_testdrive_license_address', 'span_txt_testdrive_license_address','<font color=red>({CHAR} characters left)</font>', '255')"
															><%=mybean.testdrive_license_address%></textarea>
														<span id="span_txt_testdrive_license_address"> (255
															Characters)</span>
											</div>

												<div class="form-element6">
													<label>License Issued By: &nbsp;</label>
														<input name="txt_testdrive_license_issued_by" type="text"
															class="form-control" id="txt_testdrive_license_issued_by"
															value="<%=mybean.testdrive_license_issued_by%>"
															size="32" maxlength="100">
											</div>

												<div class="form-element6">
													<label>License Valid Till: &nbsp;</label>
														<input name="txt_testdrive_license_valid" type="text"
															class="form-control datepicker"
															type="text"
															id="txt_testdrive_license_valid"
															value="<%=mybean.testdrivelicensevalid%>" size="18"
															maxlength="16">

												</div>
<div class="row"></div>
												<div class="form-element3">
													<label>Out Time<font color="#ff0000">*</font>: &nbsp; </label>
														<%
															if (mybean.empEditPerm.equals("1")) {
														%>

<!-- 														<div class="input-group date form_datetime"> -->
															<input type="text" size="16"
																name="txt_testdrive_out_time"
																id="txt_testdrive_out_time" class="form-control datetimepicker"
																value="<%=mybean.testdriveouttime%>" size="20"
																maxlength="16"> <span class="input-group-btn">
<!-- 																<button class="btn default date-set" type="button"> -->
<!-- 																	<i class="fa fa-calendar"></i> -->
<!-- 																</button> -->
															</span>
<!-- 														</div> -->
														<%
															} else {
														%>
														<%=mybean.testdriveouttime%>
														<input name="txt_testdrive_out_time" type="hidden"
															id="txt_testdrive_out_time"
															value="<%=mybean.testdriveouttime%>" />

														<%
															}
														%>

											</div>

											<div class="form-element3">
													<label> Out Kms: &nbsp;</label>
														<input name="txt_testdrive_out_kms" type="text"
															class="form-control" id="txt_testdrive_out_kms"
															value="<%=mybean.testdrive_out_kms%>" size="18"
															maxlength="10"
															onKeyUp="toFloat('txt_testdrive_out_kms','Out Kms')">

											</div>

											<div class="form-element3">
													<label> In Time: &nbsp;</label>
														<%
															if (mybean.empEditPerm.equals("1")) {
														%>
														<%--                       <input name="txt_testdrive_in_time" type="text" class="form-control"  id ="txt_testdrive_in_time" value = "<%=mybean.testdriveintime%>" size="20" maxlength="16" /> --%>
<!-- 														<div class="input-group date form_datetime"> -->
															<input type="text" size="16" name="txt_testdrive_in_time"
																id="txt_testdrive_in_time"
																value="<%=mybean.testdriveintime%>" size="20"
																maxlength="16" class="form-control datetimepicker"> <span
																class="input-group-btn">
<!-- 																<button class="btn default date-set" type="button"> -->
<!-- 																	<i class="fa fa-calendar"></i> -->
<!-- 																</button> -->
															</span>
<!-- 														</div> -->
														<%
															} else {
														%>
														<%=mybean.testdriveintime%>
														<input name="txt_testdrive_in_time" type="hidden"
															id="txt_testdrive_in_time"
															value="<%=mybean.testdriveintime%>" />
														<%
															}
														%>
											</div>

											<div class="form-element3">
													<label> In Kms: &nbsp;</label>
														<input name="txt_testdrive_in_kms" type="text"
															class="form-control" id="txt_testdrive_in_kms"
															value="<%=mybean.testdrive_in_kms%>" size="18"
															maxlength="10"
															onKeyUp="toFloat('txt_testdrive_in_kms','In Kms')">

											</div>

											<div class="form-element6">
													<label>Notes:  &nbsp;</label>
														<textarea name="txt_testdrive_mileage_notes" cols="70"
															rows="5" class="form-control"
															id="txt_testdrive_mileage_notes"><%=mybean.testdrive_mileage_notes%></textarea>
											</div>
											<%
												if (!mybean.entry_by.equals("")) {
											%>
											<div class="form-element6">
												<label>Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
											</div>

											<div class="form-element6">
												<label>Entry Date: <%=mybean.entry_date%></label>
													<input name="entry_date" type="hidden" id="entry_date"
														value="<%=mybean.entry_date%>">
											</div>
											<%} %>
											<% if (!mybean.modified_by.equals("")) { %>
											<div class="form-element6">
												<label>Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>">
											</div>
											<div class="form-element6">
												<label>Modified Date: <%=mybean.modified_date%></label>
													<input name="modified_date" type="hidden"
														id="modified_date" value="<%=mybean.modified_date%>">
												</div>
											</div>
											<%} %>
											<div class="row"></div>
											<center>
												<input name="update_button" type="submit"
													class="button btn btn-success" id="update_button"
													value="Update Mileage"
													onClick="onPress();return SubmitFormOnce(document.formtestdrive, this);" />
												<input type="hidden" name="testdrive_id"
													value="<%=mybean.testdrive_id%>"> <input
													name="enquiry_id" type="hidden" id="enquiry_id"
													value="<%=mybean.testdrive_enquiry_id%>">
											</center>
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
	<%@include file="../Library/js.jsp" %>
	</body>
</HTML>
