<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.preowned.Preowned_TestDrive_Feedback" scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ include file="../Library/css.jsp"%>
</HEAD>

<body onLoad="TestDriveCheck();"
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
						<li><a href="index.jsp">Pre-Owned</a> &gt;</li>
						<li><a href="preowned-testdrive.jsp">Test Drives</a> &gt;</li>
						<li><a href="preowned-testdrive-list.jsp?all=yes">List
								Test Drives</a> &gt;</li>
						<li><a
							href="preowned-testdrive-feedback.jsp?<%=mybean.QueryString%>">Update
								Feedback</a>:<b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font><br>
					</center>
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Update Feedback
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="formdemo" method="post" class="form-horizontal">
											<center>
												Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</center></br>
												<div class="form-element6">
													<label >Customer:</label>
														<a
															href="../customer/customer-list.jsp?customer_id=<%=mybean.testdrive_customer_id%>"><%=mybean.customer_name%>
															(<%=mybean.testdrive_customer_id%>)</a>
												</div>
												<div class="form-element6">
													<label >Enquiry No.:</label>
														<a
															href="../sales/enquiry-dash-opportunity.jsp?enquiry_id=<%=mybean.testdrive_enquiry_id%>"><%=mybean.enquiry_no%></a>
												</div>
												<div class="form-element6">
													<label >Variant:</label>
														<a
															href="managepreownedvariant.jsp?variant_id=<%=mybean.variant_id%>"><%=mybean.variant_name%></a>
												</div>
												<div class="form-element6">
													<label >Location:</label>
														<%=mybean.location_name%>
												</div>
												<div class="form-element6">
													<label >Test Drive
														Time:</label>
														<%=mybean.testdrive_time_from%>
												</div>
												<div class="form-element6">
													<label >Pre-Owned Consultant<font
														color="#ff0000">*</font>:
													</label>
														<a
															href="../portal/executive-summary.jsp?emp_id=<%=mybean.testdrive_emp_id%>"><%=mybean.executive_name%></a>
												</div>
												<div class="form-element6">
													<label >Test Drive
														Taken<font color="#ff0000">*</font>:
													</label>
														<select name="dr_demotaken" class="form-control"
															id="dr_demotaken" onChange="TestDriveCheck();">
															<option value=0>Select</option>
															<%=mybean.PopulateTestDriveTaken()%>
														</select> <input name="testdrive_type" type="hidden"
															id="testdrive_type" value="<%=mybean.testdrive_type%>">
												</div>
												<div class="form-element6">
													<label >Notes:</label>
														<textarea name="txt_testdrive_fb_notes" cols="70" rows="4"
															class="form-control" id="txt_testdrive_fb_notes"><%=mybean.testdrive_fb_notes%></textarea>
												</div>
												<%
													if (!mybean.entry_by.equals("")) {
												%>
												<div class="form-element6">
													<label  >Entry By:</label>
														<%=mybean.unescapehtml(mybean.entry_by)%>
														<input name="entry_by" type="hidden" id="entry_by" 
															value="<%=mybean.entry_by%>">
												</div>
												<div class="form-element6">
													<label >Entry Date:</label>
														<%=mybean.entry_date%>
														<input name="entry_date" type="hidden" id="entry_date"
															value="<%=mybean.entry_date%>">
												</div>
												<%
													}
												%>
												<%
													if (!mybean.modified_by.equals("")) {
												%>
												<div class="form-element6">
													<label >Modified By:</label>
														<%=mybean.unescapehtml(mybean.modified_by)%>
														<input name="modified_by" type="hidden" id="modified_by"
															value="<%=mybean.modified_by%>">
												</div>
												<div class="form-element6">
													<label >Modified
														Date:</label>
														<%=mybean.modified_date%>
														<input name="modified_date" type="hidden"
															id="modified_date" value="<%=mybean.modified_date%>">
												</div>
												<%
													}
												%>
												<center>
												<div class="form-element12">
													<input name="updatebutton" type="submit"
														class="btn btn-success" id="updatebutton"
														value="Update Feedback"
														onClick="return SubmitFormOnce(document.formdemo, this)" />
													<input type="hidden" name="update_button" value="yes" /> <input
														name="testdrive_id" type="hidden" id="testdrive_id"
														value="<%=mybean.testdrive_id%>"> <input
														name="enquiry_id" type="hidden" id="enquiry_id"
														value="<%=mybean.testdrive_enquiry_id%>">
														</div>
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

	<%@ include file="../Library/admin-footer.jsp"%>
	<%@ include file="../Library/js.jsp"%>
<script language="JavaScript" type="text/javascript">
	function TestDriveCheck() { //v1.0
		//var testdrive_taken=document.getElementById("dr_demotaken").value;

		//var testdrive_type=document.getElementById("testdrive_type").value;
		// if(testdrive_taken=="1")
		//{
		// document.getElementById("feedHint").style.display='';
		//document.getElementById("feedHint").style.visibility='visible';
		// }
		//else
		//{
		// document.getElementById("feedHint").style.display='None';	
		// }
	}
</script>
	</body>
</HTML>
