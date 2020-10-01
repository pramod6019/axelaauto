<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Fieldappt_Status_Update" scope="request" />
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
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Update Status</h1>
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="fieldappt.jsp"> Field Appointment</a>&gt;</li>
						<li><a href="fieldappt-list.jsp?all=yes">List Field Appointment</a>&gt;</li>
						<li><a href="fieldappt-status.jsp?<%=mybean.QueryString%>">Update Status</a><b>:</b></li>
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
										<div class="caption" style="float: none">Update Status </div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												<font size="1">Form fields marked with a red asterisk 
												<b><font color="#ff0000">*</font></b> are required.</font>
											</center><br></br>

											<div class="row">
											<div class="form-element6">
												<label>Customer:&nbsp;</label>
													<a href="../customer/customer-list.jsp?customer_id=<%=mybean.insurenquiry_customer_id%>"><%=mybean.customer_name%>
														(<%=mybean.insurenquiry_customer_id%>)</a>
											</div>

											<div class="form-element6">
												<label">Insurance Enquiry ID:&nbsp;</label>
													<a href="insurance-enquiry-dash.jsp?insurenquiry_id=<%=mybean.fieldappt_insurenquiry_id%>"><%=mybean.fieldappt_insurenquiry_id%></a>
											</div>
											</div>

											<div class="row">
											<div class="form-element6">
												<label>Field Appointment Time:&nbsp;</label>
													<%=mybean.fieldappt_fromtime%>
											</div>

											<div class="form-element6">
												<label>Insurance Executive<font
													color="#ff0000">*</font>:&nbsp;
												</label>
													<%=mybean.executive_name%>
											</div>
											<div>

												<div class="row">
												<div class="form-element6">
													<label>Field Appointment
														Taken<font color="#ff0000">*</font>:&nbsp;
													</label>
														<select name="dr_fieldappt_status_taken" class="form-control" id="dr_fieldappt_status_taken">
															<%=mybean.PopulateFieldApptTaken()%>
														</select>
												</div>

												<div class="form-element6">
													<label>Status Note<font
														color="#ff0000">*</font>:&nbsp;
													</label>
														<textarea name="txt_fieldappt_status_notes" class="form-control"
															cols="60" rows="5" id="txt_fieldappt_status_notes"
															onKeyUp="charcount('txt_fieldappt_status_notes', 'span_txt_fieldappt_status_notes','
															<font color=red>({CHAR} characters left)</font>', '1000')" ><%=mybean.fieldappt_status_notes%></textarea>
														<br> <span id="span_txt_fieldappt_status_notes">(1000 Characters)</span>
												</div>
												<div>

											<%
														if (!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
													%>
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
													<%
														}
													%>
													<%
														if (!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
													%>
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
													<%
														}
													%> 


											<center>
												<input name="update_button" type="submit" class="button btn btn-success"
													 id="update_button" value="Update Status" />
												<input name=fieldappt_id type="hidden" id="fieldappt_id"
													value="<%=mybean.fieldappt_id%>">
												<input name="fieldappt_insurenquiry_id" type="hidden" id="fieldappt_insurenquiry_id"
													value="<%=mybean.fieldappt_insurenquiry_id%>">
											</center>
											<br>
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
	<%@include file="../Library/js.jsp"%>
	</body>
</HTML>
