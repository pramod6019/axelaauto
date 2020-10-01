<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.sales.ManageEnquiryPriority_Update" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

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
						<h1><%=mybean.status%>&nbsp;Enquiry Priority
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
						<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
						<li><a href="manageenquirypriority.jsp?all=yes">List Enquiry Priority</a> &gt;</li>
						<li><a href="manageenquirypriority-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Enquiry Priority</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b> <%=mybean.msg%><br></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Enquiry Priority
									</div>
								</div>
								
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.<br>
											</font>
										</center>
										<br/>
										<form name="form1" method="post" class="form-horizontal">
											<!-- START PORTLET BODY -->
											
											<div class="form-element6">
												<label>Name <font color="#ff0000">*</font>:</label>
												<input name="txt_priorityenquiry_name"
													id="txt_priorityenquiry_name" type="text"
													class="form-control"
													value="<%=mybean.priorityenquiry_name%>" size="50"
													maxlength="255" />
											</div>
											
											<div class="form-element6">
												<label>Description<font color="#ff0000">*</font>:</label>
												<input name="txt_priorityenquiry_desc"
													id="txt_priorityenquiry_desc" type="text"
													class="form-control"
													value="<%=mybean.priorityenquiry_desc%>" size="50"
													maxlength="255" />
											</div>
											
											<div class="form-element12 form-element">
												<div class="form-element6">
													<label>Color<font color="#ff0000">*</font>:</label>
													<input name="txt_priorityenquiry_color" type="text"
													id="txt_priorityenquiry_color" class="form-control"
													value="<%=mybean.priorityenquiry_color%>" size="50"
													maxlength="255" />
												</div>
									       </div>
									       
												<div class="form-element4">
													<label>Met Count:</label>
													<input name="txt_priorityenquiry_metcount" type="text"
													id="txt_priorityenquiry_metcount" class="form-control"
													value="<%=mybean.priorityenquiry_metcount%>" 
													onkeyup="toInteger('txt_priorityenquiry_metcount','Met')" maxlength="3" />
												</div>
									       
												<div class="form-element4">
													<label>Test Drive Count:</label>
													<input name="txt_priorityenquiry_testdrivecount" type="text"
													id="txt_priorityenquiry_testdrivecount" class="form-control"
													value="<%=mybean.priorityenquiry_testdrivecount%>" 
													onkeyup="toInteger('txt_priorityenquiry_testdrivecount','Test Drive')" maxlength="3" />
												</div>
									       
												<div class="form-element4">
													<label>Quote Count:</label>
													<input name="txt_priorityenquiry_quotecount" type="text"
													id="txt_priorityenquiry_quotecount" class="form-control"
													value="<%=mybean.priorityenquiry_quotecount%>" 
													onkeyup="toInteger('txt_priorityenquiry_quotecount','Quote')" maxlength="3" />
												</div>
												
												<div class="form-element4">
													<label>Home Visit Count:</label>
													<input name="txt_priorityenquiry_homevisitcount" type="text"
													id="txt_priorityenquiry_homevisitcount" class="form-control"
													value="<%=mybean.priorityenquiry_homevisitcount%>" 
													onkeyup="toInteger('txt_priorityenquiry_homevisitcount','Home Visit')" maxlength="3" />
												</div>
									       
												<div class="form-element4">
													<label>Close Days Count:</label>
													<input name="txt_priorityenquiry_closedays" type="text"
													id="txt_priorityenquiry_closedays" class="form-control"
													value="<%=mybean.priorityenquiry_closedays%>" 
													onkeyup="toInteger('txt_priorityenquiry_closedays','Close Days')" maxlength="3" />
												</div>
									       
												<div class="form-element4">
												<label>Option Count:</label>
													<select name="dr_priorityenquiry_optioncount" class="form-control" id="dr_priorityenquiry_optioncount">
														<%=mybean.PopulateOptionCount(mybean.priorityenquiry_optioncount)%>
													</select>
											</div>
										
											
											<div class="form-element4">
												<label>Due Hours<font color=#ff0000><b>*</b></font>: </label>
											    <input type="text" size="16" class="form-control timepicker"
											    name="txt_priorityenquiry_duehrs"
												id="txt_priorityenquiry_duehrs"
												value="<%=mybean.priorityenquiry_duehrs%>" >
														<!-- <span
															class="input-group-addon"> -->
														<!-- <button class="btn default date-set" type="button">
																<i class="glyphicon glyphicon-time"></i>
																</button> -->
														<!-- </span> -->
											</div>
											
											
											<div class="form-element4">
												<label>Level-1 Hours:</label>
												<input type="text" size="16" class="form-control timepicker"
												name="txt_priorityenquiry_trigger1_hrs"
												id="txt_priorityenquiry_trigger1_hrs"
												value="<%=mybean.priorityenquiry_trigger1_hrs%>"
															>
														<!-- <span
															class="input-group-btn">
																<button class="btn default date-set" type="button">
																	<i class="fa fa-calendar"></i>
																</button>
														</span> -->
											</div>
											
											<div class="form-element4">
												<label>Level-2 Hours:</label>
												<input type="text" size="16" class="form-control timepicker"
												name="txt_priorityenquiry_trigger2_hrs"
												id="txt_priorityenquiry_trigger2_hrs"
												value="<%=mybean.priorityenquiry_trigger2_hrs%>"
															>
														<!-- <span
															class="input-group-btn">
																<button class="btn default date-set" type="button">
																	<i class="fa fa-calendar"></i>
																</button>
														</span> -->
											</div>
											
											<div class="form-element4">
												<label>Level-3 Hours:</label>
												<input type="text" size="16" class="form-control timepicker"
												name="txt_priorityenquiry_trigger3_hrs"
												id="txt_priorityenquiry_trigger3_hrs"
												value="<%=mybean.priorityenquiry_trigger3_hrs%>"
															>
														<!-- <span
															class="input-group-btn">
																<button class="btn default date-set" type="button">
																	<i class="fa fa-calendar"></i>
																</button>
														</span> -->
											</div>
											
											<div class="form-element4">
												<label>Level-4 Hours:</label>
												<input type="text" size="16" class="form-control timepicker"
												name="txt_priorityenquiry_trigger4_hrs"
												id="txt_priorityenquiry_trigger4_hrs"
												value="<%=mybean.priorityenquiry_trigger4_hrs%>"
															>
														<!-- <span
															class="input-group-btn">
																<button class="btn default date-set" type="button">
																	<i class="fa fa-calendar"></i>
																</button>
														</span> -->
											</div>
											
											<div class="form-element4">
												<label>Level-5 Hours:</label>
											    <input type="text" size="16" class="form-control timepicker"
												name="txt_priorityenquiry_trigger5_hrs"
												id="txt_priorityenquiry_trigger5_hrs"
												value="<%=mybean.priorityenquiry_trigger5_hrs%>"
															>
														<!-- <span
															class="input-group-btn">
																<button class="btn default date-set" type="button">
																	<i class="fa fa-calendar"></i>
																</button>
														</span> -->
											</div>
											
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											
											<div class="form-element4">
												<label>Entry By:</label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by"
												value="<%=mybean.unescapehtml(mybean.entry_by)%>">
											</div>
											
											<%
												}
											%>
											
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											
											<div class="form-element8">
												<label>Entry Date:</label>
												<%=mybean.entry_date%>
												<input type="hidden" name="entry_date"
												value="<%=mybean.entry_date%>">
											</div>
											
											<%
												}
											%>
											
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											
											<div class="form-element4">
												<label>Modified By:</label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by"
												value="<%=mybean.unescapehtml(mybean.modified_by)%>">
											</div>
											
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											
											<div class="form-element8">
												<label>Modified Date:</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
												value="<%=mybean.modified_date%>">
											</div>
											
											<%
												}
											%>
											
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Add Priority"
													onclick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" name="add_button" value="yes">
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Update Priority"
													onclick="return SubmitFormOnce(document.form1, this);" />
													<input name="update_button" type="hidden" value="yes" /> 
													<input name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" OnClick="return confirmdelete(this)"
													value="Delete Priority" />
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
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	
	<%@ include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>
	
	<script>
		function FormFocus() { //v1.0
			document.form1.txt_priorityenquiry_name.focus()
		}
	</script>

</body>
</html>

