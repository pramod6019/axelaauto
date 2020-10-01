<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.TestDrive_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<%@include file="../Library/css.jsp"%>
</HEAD>

<body onLoad="TestDriveCheck()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>
							<%=mybean.status%> Test Drive
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="testdrive.jsp">Test Drives</a> &gt;</li>
						<li><a href="testdrive-list.jsp?all=recent">List Test Drives</a> &gt;</li>
						<li><a href="testdrive-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Test Drive</a><b>:</b></li>
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
												<center>
													<%=mybean.status%> Test Drive
												</center>
											</div>
										</div>
										<div class="portlet-body portlet-empty container-fluid">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<form name="formcontact" method="post" class="form-horizontal">
													<center>
														<font size="">Form fields marked with a red
															asterisk <b><font color="#ff0000">*</font></b> are required.
														</font>
													</center>
													
													<br />
													<br />
													<div class="form-element6 ">
														<label> Customer:&nbsp;</label>
														<span>
															<b><a href="../customer/customer-list.jsp?customer_id=<%=mybean.testdrive_customer_id%>"><%=mybean.customer_name%>
																	(<%=mybean.testdrive_customer_id%>)</a></b>
														</span>
													</div>

													<div class="form-element6 ">
														<label> Enquiry Date:&nbsp;</label>
														<span >
															<%=mybean.strToShortDate(mybean.enquiry_date)%>
															<input type="hidden" name="branch_id" id="branch_id"
																value="<%=mybean.enquiry_branch_id%>"/>
														</span>
													</div>
													<div class="form-element6 ">
														<label> Enquiry No: &nbsp;</label>
														<span >
															<b><a href="enquiry-dash.jsp?enquiry_id=<%=mybean.testdrive_enquiry_id%>"
																target="_blank"><%=mybean.enquiry_no%></a></b>
														</span>
													</div>
													<div class="form-element6 ">
														<label> Sales Consultant:&nbsp; </label>
														<span >
															<b><a href="../portal/executive-summary.jsp?emp_id=<%=mybean.testdrive_emp_id%>"><%=mybean.executive_name%></a></b>
														</span>
													</div>
													<div class="form-element6">
														<label> Model: &nbsp;</label>
														<span >
																<%=mybean.PopulateModel(mybean.comp_id, mybean.model_id)%>
														</span>
														<input name="model_id" type="hidden" id="model_id" value="<%=mybean.model_id%>"/>
													</div>
													<div class="form-element6">
														<label> Vehicle&nbsp;<font color=red>*</font>: </label>
														<div id="vehicleHint" >
																<%=mybean.PopulateVehicle(mybean.comp_id, mybean.model_id, mybean.enquiry_branch_id)%>
														</div>
													</div>
													
													<div class="form-element6">
														<label>Test Drive Date<font color=#ff0000><b>*</b></font>:&nbsp; </label>
														<input type="text" size="16" name="txt_testdrive_date" id="txt_testdrive_date"
															value="<%=mybean.testdrivedate%>" class="form-control datetimepicker" onChange="TestDriveCheck();">
													</div>
		
													<div class="form-element6">
														<label> Location&nbsp;<font color=red>*</font>: </label>
														<select name="dr_location" class="form-control" id="dr_location">
															<option value=0>Select Location</option>
																<%=mybean.PopulateLocation(mybean.comp_id)%>
														</select>
													</div>
													
													<div class="row">
														<div class="form-element6">
															<label> Notes:&nbsp; </label>
															<textarea name="txt_testdrive_notes" cols="70" rows="3"
																class="form-control" id="txt_testdrive_notes"><%=mybean.testdrive_notes%></textarea>
														</div>
														
														<div class="form-element6 form-element-margin">
															<label> Confirmed:&nbsp;</label>
															<span >
																<input id="chk_testdrive_confirmed" type="checkbox"
																	name="chk_testdrive_confirmed"
																	<%=mybean.PopulateCheck(mybean.testdrive_confirmed)%> />
																<%
																	if (mybean.status.equals("Update")) {
																%>
																<a href="../sales/testdrive-update.jsp?testdrive_id=<%=mybean.testdrive_id%>&enquiry_id=<%=mybean.testdrive_enquiry_id%>&unconfirm=yes">Unconfirm
																	Test Drive</a>
																<%
																	}
																%>
															</span>
														</div>
													</div>


													<%
														if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
													%>
													<div class="form-element6">
														<label> Entry By:&nbsp;</label>
														<span>
															<%=mybean.unescapehtml(mybean.entry_by)%>
															<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>"/>
														</span>
													</div>
													<div class="form-element6">
														<label> Entry Date:&nbsp;</label>
														<span>
															<%=mybean.entry_date%>
															<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"/> 
														</span>
													</div>
													<%
														}
													%>
													<%
														if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
													%>
													<div class="form-element6">
														<label>Modified By:&nbsp;</label>
														<span >
															<%=mybean.unescapehtml(mybean.modified_by)%>
															<input name="modified_by"type="hidden" id="modified_by" value="<%=mybean.modified_by%>" />
														</span>
													</div>
													<div class="form-element6">
														<label>Modified Date:&nbsp;</label>
														<span >
															<%=mybean.modified_date%>
															<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"/>
														</span>
													</div>
													<%
														}
													%>

													<div class="form-element12">
														<center>
															<%
																if (mybean.status.equals("Add")) {
															%>
															<input name="add_button" type="submit" class="button btn btn-success" id="add_button"
																onclick="return SubmitFormOnce(document.formcontact, this);" value="Add Test Drive" />
															<input type="hidden" name="add_button" value="yes">
															<%
																} else if (mybean.status.equals("Update")) {
															%>
															<input name="update_button" type="submit" class="button btn btn-success"
																id="update_button" value="Update Test Drive"
																onclick="return SubmitFormOnce(document.formcontact, this);"/>
															<input type="hidden" name="update_button" value="yes">
															<input name="delete_button" type="submit"
																class="button btn btn-success" id="delete_button"
																OnClick="return confirmdelete(this)" value="Delete Test Drive" />
															<%
																}
															%>
														</center>
														<center>
															<input type="hidden" name="testdrive_id" value="<%=mybean.testdrive_id%>">
															<input name="enquiry_id" type="hidden" id="enquiry_id" value="<%=mybean.testdrive_enquiry_id%>">
														</center>
													</div>
												</form>
											</div>
										</div>
									</div>
								</div>


								<!-- 2nd ------------portlet -->
								<div class="form-element4 form-element">
									<div class="portlet box">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">
												<center>Test Drive Calendar</center>
											</div>
										</div>
										<div class="portlet-body portlet-empty" style="min-height: 486px;">
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
	<script language="JavaScript" type="text/javascript">
		function TestDriveCheck() {
			var testdriveveh_id = document.getElementById("dr_vehicle").value;
			var testdrivedate = document.getElementById("txt_testdrive_date").value;
			var branch_id = document.getElementById("branch_id").value;

			showHint('../sales/testdrive-check.jsp?testdrive=yes&testdriveveh_id=' + testdriveveh_id + '&testdrivedate=' + testdrivedate + '&branch_id=' + branch_id + '', 'calHint');
		}
		function populateVehicle() {
			var item_model_id = document.getElementById("dr_model").value;
			var branch_id = document.getElementById("branch_id").value;
// 			alert("item_model_id==="+item_model_id);
			showHint('../sales/testdrive-check.jsp?model=yes&item_model_id=' + item_model_id + '&branch_id=' + branch_id + '', 'vehicleHint');
		}
	</script>
	<script language="JavaScript" type="text/javascript">
		// 	$(function() {
		//     	$( "#txt_testdrive_date" ).datetimepicker({
		//       		showButtonPanel: true,
		//       		format: "dd/mm/yyyy hh:ii",
		// 			controlType: 'select'
		// 			});
		// 	});
	</script>

</body>
</HTML>
