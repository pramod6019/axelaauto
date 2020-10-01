<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.TestDrive_Feedback"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<script language="JavaScript" type="text/javascript">
	function TestDriveCheck() { //v1.0
		var testdrive_taken = document.getElementById("dr_testdrivetaken").value;
		var testdrive_status = document.getElementById("dr_status").value;
		var testdrive_type = document.getElementById("testdrive_type").value;
		if (testdrive_taken == "1") {
			document.getElementById("rowstatus").style.display = '';
			document.getElementById("rowstatus").style.visibility = 'visible';
			if (testdrive_status == "0") {
				//document.getElementById("feedHint").style.display='';
				//  document.getElementById("feedHint").style.visibility='visible';
				document.getElementById("rowstatuscomments").style.display = 'None';
			} else {
				//document.getElementById("feedHint").style.display='None';
				document.getElementById("rowstatuscomments").style.display = '';
				document.getElementById("rowstatuscomments").style.visibility = 'visible';
			}
		} else {
			document.getElementById("rowstatus").style.display = 'None';
			document.getElementById("rowstatuscomments").style.display = 'None';
			//document.getElementById("feedHint").style.display='None';	
		}
	}
</script>
</script>
</HEAD>

<body onLoad="TestDriveCheck();" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Update Feedback</h1>
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
						<li><a href="testdrive.jsp">Test Drives</a>&gt;</li>
						<li><a href="testdrive-list.jsp?all=yes">List Test Drives</a>&gt;
						</li>
						<li><a href="testdrive-feedback.jsp?<%=mybean.QueryString%>">Update
								Feedback</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->


							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font><br>
							</center>
							<form name="formtestdrive" method="post" class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Update Feedback
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<center>
												Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</center>

											<div class="form-element6">
												<label>Customer:&nbsp;</label>
													<a href="../customer/customer-list.jsp?customer_id=<%=mybean.testdrive_customer_id%>"><%=mybean.customer_name%>
														(<%=mybean.testdrive_customer_id%>)</a>
											</div>

												<div class="form-element6">
												<label> Enquiry No.:&nbsp;</label>
													<a href="enquiry-dash.jsp?enquiry_id=<%=mybean.testdrive_enquiry_id%>"><%=mybean.enquiry_no%></a>
											</div>

												<div class="form-element6">
												<label>Vehicle:&nbsp;</label>
													<a href="../sales/managetestdrivevehicle.jsp?testdriveveh_id=<%=mybean.testdriveveh_id%>"><%=mybean.vehicle_name%></a>
											</div>

												<div class="form-element6">
												<label>Location:&nbsp;</label>
													<%=mybean.location_name%>
											</div>

												<div class="form-element6">
												<label>Test Drive
													Time:&nbsp;</label>
													<%=mybean.testdrive_time_from%>
											</div>

												<div class="form-element6">
												<label>Sales Consultant<font color="#ff0000">*</font>:&nbsp; </label>
													<%=mybean.executive_name%>
											</div>

												<div class="form-element6">
													<label>Test Drive Taken<font color="#ff0000">*</font>:&nbsp; </label>
														<select name="dr_testdrivetaken" class="form-control"
															id="dr_testdrivetaken" onChange="TestDriveCheck();">
															<option value=0>Select</option>
															<%=mybean.PopulateTestDriveTaken()%>
														</select> <input name="testdrive_type" type="hidden"
															id="testdrive_type" value="<%=mybean.testdrive_type%>">
											</div>
											<div id="rowstatus">
												<div class="form-element6">
													<label>Status<font color="#ff0000">*</font>:&nbsp;
													</label> <select name="dr_status" class="form-control"
														id="dr_status" onChange="TestDriveCheck();">
														<option value=0>Select</option>
														<%=mybean.PopulateStatus()%>
													</select>
												</div>


												<%
													if (mybean.brand_id.equals("60")) {
												%>
												<div class="form-element6">
													<label>DL No.<font color="#ff0000">*</font>:&nbsp; </label> <input
														name="txt_testdrive_license_no" class="form-control"
														size="20" maxlength="20" id="txt_testdrive_license_no" value="<%=mybean.testdrive_license_no%>"/>
													<br>
												</div>



												<div class="form-element6">
													<label>License Valid Till<font color="#ff0000">*</font>:&nbsp; </label> <input
														name="txt_testdrive_license_valid"
														class="form-control datepicker"
														id="txt_testdrive_license_valid" value="<%=mybean.testdrive_license_valid%>" />
													<br>
												</div>

												<%
													}
												%>
											</div>

											<div class="form-element6" id="rowstatuscomments">
													<label>Comments<font color="#ff0000">*</font>:&nbsp; </label>
														<textarea name="txt_testdrive_fb_status_comments"
															cols="60" rows="5" class="form-control"
															id="txt_testdrive_fb_status_comments"
															onKeyUp="charcount('txt_testdrive_fb_status_comments', 'span_txt_testdrive_fb_status_comments','<font color=red>({CHAR} characters left)</font>', '1000')"
															><%=mybean.testdrive_fb_status_comments%></textarea>
														<br> <span
															id="span_txt_testdrive_fb_finance_comments">(1000
															Characters)</span>
											</div>

											<div class="form-element6">
													<label>Notes:&nbsp; </label>
														<textarea name="txt_testdrive_fb_notes" cols="60" rows="5"
															class="form-control" id="txt_testdrive_fb_notes"><%=mybean.testdrive_fb_notes%></textarea>
														<br>

											</div>
											
											<div class="row"></div>
											
											<% if (!mybean.entry_by.equals("")) { %>
											
											<div class="form-element6">
												<label>Entry By:&nbsp; <%=mybean.unescapehtml(mybean.entry_by)%></label>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.entry_by%>">
											</div>
											
											<div class="form-element6">
												<label>Entry Date:&nbsp; <%=mybean.entry_date%></label>
													<input name="entry_date" type="hidden" id="entry_date"
														value="<%=mybean.entry_date%>">
											</div>
											
											<% } %>

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
											
											<%} %>
											
											<div class="row"></div>
											<center>
												<input name="update_button" type="submit"
													class="button btn btn-success" id="update_button"
													value="Update Feedback" /> <input name="testdrive_id"
													type="hidden" id="testdrive_id"
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


	<%@include file="../Library/admin-footer.jsp"%></body>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
</HTML>
