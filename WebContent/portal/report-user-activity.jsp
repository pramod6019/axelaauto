<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Report_User_Activity"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
	<%@include file="../Library/css.jsp"%>
<style>
.dropdown-menu {
	overflow-y: "";
}
</style>
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
						<h1>User Activity</h1>
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
							<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
							<li><a href="../portal/report-user-activity.jsp">User
									Activity</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="red"><b> <%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">User Activity</div>
								</div>

								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<div class="form-element4">
												<label>Start Date:</label> <input name="txt_starttime"
													id="txt_starttime" class="form-control datetimepicker"
													type="text" value="<%=mybean.start_time%>" />
											</div>
											<div class="form-element4">
												<label>End Date:</label> <input type="text" size="16"
													name="txt_endtime" id="txt_endtime"
													class="form-control datetimepicker"
													value="<%=mybean.end_time%>">
											</div>

											<div class="form-element4">
												<label>Device:</label>
												<div>
													<select multiple="multiple" id="dr_device" name="dr_device"
														class="form-control multiselect-dropdown">
														<%=mybean.PopulateDevice()%>
													</select>
												</div>
											</div>
											<div class="form-element4">
												<label> Brands:</label>
												<div>
													<select name="dr_principal" size="10" multiple="multiple"
														class="form-control multiselect-dropdown"
														id="dr_principal"
														onChange="PopulateBranches();PopulateRegion();">
														<%=mybean.execheck.PopulatePrincipal( mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element4">
												<label> Regions:</label>
												<div id="regionHint">
													<%=mybean.execheck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label> Branches: </label>
												<div id="branchHint">
													<%=mybean.execheck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.type_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element4">
												<label> Department:</label>
												<div>
													<%=mybean.execheck.PopulateDepartment(mybean.department_ids,mybean.department_id,mybean.branch_id,mybean.region_id, mybean.comp_id, request)%>
												</div>
											</div>


											<div class="form-element4">
												<label> Job Title:</label>
												<div>
													<select name="dr_jobtitle" size="10" multiple="multiple"
														class="form-control multiselect-dropdown" id="dr_jobtitle"
														onChange="PopulateExecutives();">
														<%=mybean.execheck.PopulateJobTitle(mybean.jobtitle_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>
											<div class="form-element4">
												<label> Executive:</label>
												<div id="exeHint">
													<%=mybean.execheck.PopulateExecutives(request,mybean.branch_id,mybean.department_id, mybean.jobtitle_id, mybean.exe_ids, mybean.comp_id)%>
												</div>
											</div>
											<center>
												<input type="submit" name="submit_button" id="submit_button"
													class="btn btn-success" value="Go" /> <input type="hidden"
													name="submit_button" value="Submit" />
											</center>
											<%=mybean.StrHTML%>

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
	</div>
	</div>
	<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	<script language="JavaScript" type="text/javascript">
		function PopulateRegion() {
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../portal/executive-check.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() {
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../portal/executive-check.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}
		function PopulateExecutives() {
			var branch_id = $('#dr_branch').val();
			if (branch_id == undefined) {
				branch_id = 0;
			} 
// 			var branch_id = outputSelected(document.getElementById("dr_branch").options);
 			var department_id = outputSelected(document.getElementById("dr_department").options);
			var jobtitle_id = outputSelected(document.getElementById("dr_jobtitle").options);
			showHint('../portal/executive-check.jsp?multiple=yes&branch_id=' + branch_id + '&department_id=' + department_id
					+ '&jobtitle_id=' + jobtitle_id + '&executive=yes', 'exeHint');
		}
		function page_submit() {
			PopulateExecutives();
		}
	</script>
</body>
</HTML>
