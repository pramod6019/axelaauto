<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_TestDrive_Mileage" scope="request"/>
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon" type="image/x-icon"  
	href="../admin-ifx/axela.ico">  

<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck-post.js"></script>

<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />

<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap-timepicker.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />


<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Update Mileage</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt; </li>
						<li><a href="index.jsp">Pre Owned</a> &gt; </li>
						<li><a href="preowned-testdrive.jsp">Test Drives</a> &gt; </li>
						<li><a href="preowned-testdrive-list.jsp?all=yes">List Test Drives</a> &gt; </li>
						<li><a href="preowned-testdrive-mileage.jsp">Update Mileage</a><b>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
                       <center><font color="#ff0000" ><b><%=mybean.msg%></font></center>
					<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						Update Mileage 
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
						<form name="formdemo" class="form-horizontal" method="post">
						<center><font>Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </font></center>
                                    <div class="form-group">
												<label class="control-label col-md-4">Customer:
													</label>
												<div class="txt-align">
													<a href="../customer/customer-list.jsp?customer_id=<%=mybean.testdrive_customer_id%>"><%=mybean.customer_name%> (<%=mybean.testdrive_customer_id%>)</a>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Enquiry No.:
													</label>
												<div class="txt-align">
					<a href="../sales/enquiry-dash-opportunity.jsp?enquiry_id=<%=mybean.testdrive_enquiry_id%>"><%=mybean.enquiry_no%></a></td>
                  
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Variant:
													</label>
												<div class="txt-align">
					<a href="../sales/managedemovehicle.jsp?variant_id=<%=mybean.variant_id%>"><%=mybean.variant_name%></a></td>
                    
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Location:
													</label>
												<div class="txt-align">
					<%=mybean.location_name%>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Test Drive Time:
													</label>
												<div class="txt-align">
					<%=mybean.testdrivetime%>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Pre-Owned Consultant<font color="#ff0000">*</font>:
													</label>
												<div class="txt-align">
					<%=mybean.executive_name%>
												</div>
											</div>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label">Driver:</label>
													<div class="col-md-6">
														<select name="dr_driver" class="form-control"
															id="dr_driver">
															<option value=0>Select Driver&nbsp;</option>
															<%=mybean.PopulateDriver()%>
														</select>

													</div>
												</div>
											</div>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label">DL No.: &nbsp;</label>
													<div class="col-md-6">
														<input name="txt_testdrive_license_no" type="text"
															class="form-control" id="txt_testdrive_license_no"
															value="<%=mybean.testdrive_license_no%>" size="32"
															maxlength="100">

													</div>
												</div>
											</div>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label">DL Address: &nbsp;</label>
													<div class="col-md-6">
														<textarea name="txt_testdrive_license_address" cols="50"
															rows="5" class="form-control"
															id="txt_testdrive_license_address"
															onKeyUp="charcount('txt_testdrive_license_address', 'span_txt_testdrive_license_address','<font color=red>({CHAR} characters left)</font>', '255')"
															><%=mybean.testdrive_license_address%></textarea>
														<span id="span_txt_testdrive_license_address"> (255
															Characters)</span>
													</div>
												</div>
											</div>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label">License
														Issued By: &nbsp;</label>
													<div class="col-md-6">
														<input name="txt_testdrive_license_issued_by" type="text"
															class="form-control" id="txt_testdrive_license_issued_by"
															value="<%=mybean.testdrive_license_issued_by%>"
															size="32" maxlength="100" >

													</div>
												</div>
											</div>
											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label">Out Time<font
														color="#ff0000">*</font>: &nbsp;
													</label>
													<div class="col-md-6">
														<%
															if (mybean.empEditPerm.equals("1")) {
														%>
														<%--                       <input name="txt_testdrive_out_time" type="text" class="form-control"  id ="txt_testdrive_out_time" value = "<%=mybean.testdriveouttime%>" size="20" maxlength="16" /> --%>
														<!--                       <div class="input-group date form_datetime"> -->

														<div class="input-group date form_datetime">
															<input type="text" size="16"
																name="txt_testdrive_out_time"
																id="txt_testdrive_out_time" class="form-control"
																value="<%=mybean.testdriveouttime%>" size="20"
																maxlength="16"> <span class="input-group-btn">
																<button class="btn default date-set" type="button">
																	<i class="fa fa-calendar"></i>
																</button>
															</span>
														</div>
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
												</div>
											</div>

											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label"> Out Kms: &nbsp;</label>
													<div class="col-md-6">
														<input name="txt_testdrive_out_kms" type="text"
															class="form-control" id="txt_testdrive_out_kms"
															value="<%=mybean.testdrive_out_kms%>" size="18"
															maxlength="10"
															onKeyUp="toFloat('txt_testdrive_out_kms','Out Kms')">

													</div>
												</div>
											</div>

											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label"> In Time: &nbsp;</label>
													<div class="col-md-6">
														<%
															if (mybean.empEditPerm.equals("1")) {
														%>
														<%--                       <input name="txt_testdrive_in_time" type="text" class="form-control"  id ="txt_testdrive_in_time" value = "<%=mybean.testdriveintime%>" size="20" maxlength="16" /> --%>
														<div class="input-group date form_datetime">
															<input type="text" size="16" name="txt_testdrive_in_time"
																id="txt_testdrive_in_time"
																value="<%=mybean.testdriveintime%>" size="20"
																maxlength="16" class="form-control"> <span
																class="input-group-btn">
																<button class="btn default date-set" type="button">
																	<i class="fa fa-calendar"></i>
																</button>
															</span>
														</div>
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
												</div>
											</div>

											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label"> In Kms: &nbsp;</label>
													<div class="col-md-6">
														<input name="txt_testdrive_in_kms" type="text"
															class="form-control" id="txt_testdrive_in_kms"
															value="<%=mybean.testdrive_in_kms%>" size="18"
															maxlength="10"
															onKeyUp="toFloat('txt_testdrive_in_kms','In Kms')">

													</div>
												</div>
											</div>

											<div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label">Notes:  &nbsp;</label>
													<div class="col-md-6">
														<textarea name="txt_testdrive_mileage_notes" cols="70"
															rows="5" class="form-control"
															id="txt_testdrive_mileage_notes"><%=mybean.testdrive_mileage_notes%></textarea>
														</td>

													</div>
												</div>
											</div>
											<%
												if (!mybean.entry_by.equals("")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry By: &nbsp;</label>
												<div class="txt-align">
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4">Entry Date:</label>
												<div class="txt-align">
													<%=mybean.entry_date%>
													<input name="entry_date" type="hidden" id="entry_date"
														value="<%=mybean.entry_date%>">
												</div>
											</div>
											<%} %>
											<% if (!mybean.modified_by.equals("")) { %>
											<div class="form-group">
												<label class="control-label col-md-4">Modified By: &nbsp;</label>
												<div class="txt-align">
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>">
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4">Modified Date: &nbsp;</label>
												<div class="txt-align">
													<%=mybean.modified_date%>
													<input name="modified_date" type="hidden"
														id="modified_date" value="<%=mybean.modified_date%>">
												</div>
											</div>
											<%} %>
											<center>
												<input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Mileage" onClick="return SubmitFormOnce(document.formdemo, this);"/>      
                    <input type="hidden" name="update_button" value="yes" />   
                      <input type="hidden" name="testdrive_id" value="<%=mybean.testdrive_id%>">
                      <input name="enquiry_id" type="hidden" id="enquiry_id" value="<%=mybean.testdrive_enquiry_id%>">
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
<%@ include file="../Library/admin-footer.jsp" %>
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript"
	src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script src="../assets/js/components-date-time-pickers.js"
	type="text/javascript"></script>
<script src="../assets/js/bootstrap-datepicker.js"
	type="text/javascript"></script>
<script src="../assets/js/bootstrap-timepicker.js"
	type="text/javascript"></script>
<script src="../assets/js/components-date-time-pickers.js"
	type="text/javascript"></script>
<script src="../assets/js/bootstrap-datetimepicker.js"
	type="text/javascript"></script>
<script>
	$(function() {

		//     $( "#txt_testdrive_out_time" ).datetimepicker({
		//       addSliderAccess: true,
		// 	  sliderAccessArgs: {touchonly: false},
		//       dateFormat: "dd/mm/yy",
		// 	  hour: 10,
		// 	  stepMinute: 5,
		// 	  minute: 00
		//     });
		// 	$( "#txt_testdrive_in_time" ).datetimepicker({
		//       addSliderAccess: true,
		// 	  sliderAccessArgs: {touchonly: false},
		//       dateFormat: "dd/mm/yy",
		// 	  hour: 10,
		// 	  stepMinute: 5,
		// 	  minute: 00
		//     });	
		$("#txt_testdrive_license_valid").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
	});
</script>
</body>
</HTML>
