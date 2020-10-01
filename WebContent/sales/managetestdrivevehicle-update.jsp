<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.sales.ManageTestDriveVehicle_Update" scope="request" />
<%mybean.doGet(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</head>

<body onLoad="FormFocus();HideAsterisk();" class="page-container-bg-solid page-header-menu-fixed">

	<%@include file="../portal/header.jsp"%>
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
							Vehicle
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
							<li><a href="managetestdrivevehicle.jsp?all=yes">List Vehicles</a> &gt;</li>
							<li><a href="managetestdrivevehicle-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Vehicle</a><b>:</b></li>
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
										<center>
											<%=mybean.status%>
											Vehicle
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
									
										<form name="form1" method="post" class="form-horizontal">
										
											<center>
												<font size="">Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.
												</font>
											</center>

											<div class="row">
													<div class="form-element6">
														<label>Vehicle Type<font color="#ff0000">*</font>:</label> 
														<select name="dr_testdriveveh_type_id"
															id="dr_testdriveveh_type_id" class="form-control" onchange="HideAsterisk();">
															<%=mybean.PopulateVehicleType(mybean.comp_id)%>
														</select>
													</div>
													
													<% if (mybean.status.equals("Add")) { %>
													<div class="form-element6">
													<label>Branch<font color="#ff0000">*</font>: </label>
														<select name="dr_branch" class="form-control" id="dr_branch">
														<%=mybean.PopulateBranch(mybean.testdriveveh_branch_id, "", "1,2", "", request)%>
														</select>
	 												<% } else if (mybean.status.equals("Update")){ %>
	 												<div class="form-element6 form-element-margin">
													<label>Branch<font color="#ff0000">*</font>: </label>
	 												<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.testdriveveh_branch_id%>">
														<b><%=mybean.branch_name%></b></a>
														<input type="hidden" id="dr_branch" name="dr_branch" value="<%=mybean.testdriveveh_branch_id%>"/>
	 												<% } %>
												</div>
											</div>
											
											<div class="row">
												<div class="form-element6">
													<label>Name<font color="#ff0000">*</font>: </label>
													<input name="txt_testdriveveh_name" type="text"
													class="form-control" id="txt_testdriveveh_name"
													value="<%=mybean.testdriveveh_name%>" maxlength="255" />
												</div>
												
												<div class="form-element6">
													<label>Registration No.:</label> 
													<input name="txt_testdriveveh_regno" type="text" class="form-control"
													id="txt_testdriveveh_regno" value="<%=mybean.testdriveveh_regno%>" size="20"
													maxlength="20" />
												</div>
											</div>

											<div class="row" id="hide">
												<div class="form-element6">
													<label>Model<font color="#ff0000">*</font>: </label>
													<select name="dr_model_id" id="dr_model_id"
													class="form-control"
													onChange="showHint('veh-quote-check.jsp?model_id=' + GetReplace(this.value) +'&list_model_item=yes','prodHint');">
													<%=mybean.PopulateModel()%>
													</select>
												</div>

												<div class="form-element6">
													<label>Variant<font color="#ff0000">*</font>: </label>
													<span id="prodHint"> <%=mybean.PopulateProduct()%>
													</span>
												</div>
											</div>

											<div class="row">

												<div class="form-element6">
													<label>Service Start Date<font color=#ff0000><b>*</b></font>: </label>
													<input name="txt_testdriveveh_service_start_date"
													id="txt_testdriveveh_service_start_date"
													class="form-control datepicker"
													data-date-format="dd/mm/yyyy" type="text"
													value="<%=mybean.testdriveveh_service_start_date%>" size="12"
													maxlength="10" />
												</div>
												
												<div class="form-element6">
													<label>Notes:</label>
													<textarea name="txt_testdriveveh_notes" cols="50" rows="4"
													class="form-control" id="txt_testdriveveh_notes"><%=mybean.testdriveveh_notes%></textarea>
												</div>
												
												<div class="form-element6">
													<label>Service End Date:</label> 
													<input name="txt_testdriveveh_service_end_date"
													id="txt_testdriveveh_service_end_date"
													value="<%=mybean.testdriveveh_service_end_date%>"
													class="form-control datepicker"
													data-date-format="dd/mm/yyyy" type="text" maxlength="10" />
												</div>
												
											</div>

											<div class="row">
												<div class="form-element6 ">
													<label>Active:</label> 
													<input type="checkbox" name="chk_testdriveveh_active" id="chk_testdriveveh_active"
													<%=mybean.PopulateCheck(mybean.testdriveveh_active)%> />
												</div>
											</div>

											<% if (mybean.status.equals("Update") && !(mybean.entry_by == null)
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

											<% 
													}
												%>


											<div class="form-element12">
												<% if (mybean.status.equals("Add")) { %>

												<center>
													<input name="addbutton" type="submit"
														class="btn btn-success" id="addbutton" value="Add Vehicle"
														onclick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" name="add_button" id="add_button"
														value="yes">
												</center>

												<%}else if (mybean.status.equals("Update")){%>

												<center>
													<input name="updatebutton" type="submit"
														class="btn btn-success" id="updatebutton"
														value="Update Vehicle"
														onclick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" name="update_button"
														id="update_button" value="yes"> <input
														name="delete_button" type="submit" class="btn btn-success"
														id="delete_button" OnClick="return confirmdelete(this)"
														value="Delete Vehicle" />
												</center>
												<%}%>
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

	<script language="JavaScript" type="text/javascript">
		function FormFocus() {
			document.form1.txt_testdriveveh_name.focus();
		}
		
		function HideAsterisk(){
			var veh_type_id = $("#dr_testdriveveh_type_id").val();
// 			console.log(veh_type_id);
			if(veh_type_id == 2){
				 $("#hide").hide();
			} else {
				 $("#hide").show();
			} 
		}
	</script>

</body>
</html>




