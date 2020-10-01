<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.TestDriveOutage_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</head>


<body onLoad="FormFocus();" class="page-container-bg-solid page-header-menu-fixed">

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
							<%=mybean.status%>
							Test Drive Outage
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
							<li><a href="testdriveoutage-list.jsp?all=yes"> List Test Drive Outage</a> &gt;</li>
							<li><a href="testdriveoutage-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Test Drive Outage</a>:</li>
						</ul>
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>
						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%>
											Test Drive Outage
										</center>
									</div>
								</div>
								
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										
										<form name="formcontact" method="post" class="form-horizontal">
										
											<center>
												<font size=""> Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.
												</font>
											</center>
											
											<input type="hidden" id="txt_status" name="txt_status" value="<%=mybean.status%>" />

											<div class="form-element6">
												<label>Branch<font color="#ff0000">*</font>:&nbsp; </label>
												<select name="dr_branch_id" id="dr_branch_id"
													class="form-control" onChange="PopulateVehicle(this.value)">
													<%=mybean.PopulateBranch()%>
												</select>
											</div>

											<div class="form-element6">
												<label>Vehicle<font color="#ff0000">*</font>:&nbsp; </label>
												<span id="vehicleHint"> <%=mybean.PopulateVehicle()%> </span>
											</div>

											<%-- <div class="form-element6">
												<label >From Date<font
												color=#ff0000><b>*</b></font>:</label>
												<div class="input-group date form_datetime">
													<input type="text" size="16"  name ="txt_outage_fromtime" id ="txt_outage_fromtime"
														value = "<%=mybean.outagefromtime%> class="form-control">
													<span class="input-group-btn">
														<button class="btn default date-set" type="button">
															<i class="fa fa-calendar"></i>
														</button>
													</span>
												</div>
											</div>	 --%>

											<div class="form-element6">
												<label>From Date<font color=#ff0000><b>*</b></font>:&nbsp; </label>
												<input name="txt_outage_fromtime" type="text"
												class="form-control datetimepicker"
												id="txt_outage_fromtime" value="<%=mybean.outagefromtime%>"
												size="20" maxlength="16" />
											</div>

											<!-- 		<div class="form-element6"> -->
											<!-- 			<label>From Date<font color="#ff0000">*</font>:</label> -->
											<!-- 				<input name="txt_outage_fromtime" type="text" class="form-control  datetimepicker"  id ="txt_outage_fromtime"  -->
											<%-- 				value = "<%=mybean.outagefromtime%>" size="20" maxlength="16" /> --%>
											<!-- 		</div> -->

											<%-- <div class="form-element6">
										<label >To Time<font
											color=#ff0000><b>*</b></font>:</label>
										<div class="col-md-6">
											<div class="input-group date form_datetime">
												<input type="text" size="16"  name ="txt_outage_totime" id ="txt_outage_totime"
													value = "<%=mybean.outagetotime%> class="form-control">
												<span class="input-group-btn">
													<button class="btn default date-set" type="button">
														<i class="fa fa-calendar"></i>
													</button>
												</span>
											</div>
										</div>
									</div> --%>


											<div class="form-element6">
												<label>To Time<font color=#ff0000><b>*</b></font>:&nbsp; </label>
												<input name="txt_outage_totime" type="text"
												class="form-control datetimepicker" id="txt_outage_totime"
												value="<%=mybean.outagetotime%>" size="20" maxlength="16" />
											</div>



											<div class="form-element6">
												<label>Description:&nbsp;</label>
												<textarea name="txt_outage_desc" rows="5"
												class="form-control" id="txt_outage_desc"
												onKeyUp="charcount('txt_outage_desc', 'span_txt_outage_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.outage_desc%></textarea>
												<span id="span_txt_outage_desc">(1000 Characters)</span>
											</div>

											<div class="form-element6">
												<label>Notes:&nbsp;</label>
												<textarea name="txt_outage_notes" rows="5"
												class="form-control" id="txt_outage_notes"
												onKeyUp="charcount('txt_outage_notes', 'span_txt_outage_notes','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.outage_notes%></textarea>
												<span id="span_txt_outage_notes">(1000 Characters)</span>
											</div>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Entry By:&nbsp;</label> 
												<span> <%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.entry_by%>">
												</span>
											</div>

											<div class="form-element6">
												<label>Entry Date:&nbsp;</label> 
												<span> <%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
													value="<%=mybean.entry_date%>">
												</span>
											</div>
											
											<% } %>
											<% if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											
											<div class="form-element6">
												<label>Modified By:&nbsp;</label> 
												<span> <%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
													value="<%=mybean.modified_by%>">
												</span>
											</div>

											<div class="form-element6">
												<label>Modified Date:&nbsp;</label> 
												<span> <%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>">
												</span>
											</div>
											
											<% } %>
											
											<div class="form-element12">
											
												<% if (mybean.status.equals("Add")) { %>


												<center>
													<input name="add_button" type="submit"
														class="btn btn-success" id="add_button"
														onclick="return SubmitFormOnce(document.formtestdrive, this);"
														value="Add Vehicle Outage" /> <input type="hidden"
														name="add_button" value="yes">
												</center>
												
												<% } else if (mybean.status.equals("Update")) { %>
												
												<center>
													<input name="update_button" type="submit"
														class="btn btn-success" id="update_button"
														onclick="return SubmitFormOnce(document.formtestdrive, this);"
														value="Update Vehicle Outage" /> <input type="hidden"
														name="update_button" value="yes" /> <input
														name="delete_button" type="submit" class="btn btn-success"
														id="delete_button" OnClick="return confirmdelete(this)"
														value="Delete Vehicle Outage" />
												</center>
												
												<% } %>
												
												<div>
													<input type="hidden" name="testdrive_id"
														value="<%=mybean.outage_id%>">
												</div>
											</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script type="text/javascript">
		function PopulateVehicle(branch_id) {
			status = document.getElementById("txt_status").value;
			showHint('../sales/testdrive-vehicle-check.jsp?dr_branch_id='
					+ GetReplace(branch_id) + '&status=' + status,
					'vehicleHint');
		}
	</script>
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			document.formtestdrive.dr_branch_id.focus();
		}
	</script>
</body>
</html>
