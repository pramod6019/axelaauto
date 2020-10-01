<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.TestDrive_Cal" scope="request" />
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

<%@include file="../Library/css.jsp"%>

</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Test Drive Calendar</h1>
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
							<li><a href="testdrive.jsp">Test Drives</a> &gt; <a href="testdrive-cal.jsp?<%=mybean.QueryString%>">Test Drive Calendar</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="red"><b> <%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"></div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											Form fields marked with a red asterisk <b><font
												color="#ff0000">*</font></b> are required. </font>
										</center>
										<br>
										<form name="formemp" class="form-horizontal" method="post">
											<div class="container-fluid ">

												<div class="row">
													<%
														if (mybean.branch_id.equals("0")) {
													%>
													<div class="form-element4">
														<label>Branch<font color="red">*</font>: </label>
														<select name="dr_branch" id="dr_branch" class="dropdown form-control"
															onChange="TestDriveCheck();PopulateExecutives();">
															<%=mybean.PopulateBranch(mybean.enquiry_branch_id, "", "1,2", "", request)%>
														</select>
														<%
															} else {
														%>
														<input type="hidden" name="dr_branch" id="dr_branch"
															value="<%=mybean.branch_id%>" />
														<%
															}
														%>
													</div>

													<div class="form-element4">
														<label>Start Date<font color="red">*</font>: </label>
														<input name="txt_testdrive_time_from" type="text" id="txt_testdrive_time_from"
															class="form-control datepicker" value="<%=mybean.testdrive_time_from%>" />
													</div>

													<div class="form-element4">
														<label> End Date<font color="red">*</font>: </label>
														<input name="txt_testdrive_time_to" type="text" class="form-control datepicker"
															id="txt_testdrive_time_to" value="<%=mybean.testdrive_time_to%>" />
													</div>
												</div>
												<div class="form-element4">
													<label>Model:</label>
													<div id="modelHint">
														<select name="dr_model" size="10" multiple="multiple" id="dr_model"
															class="form-control multiselect-dropdown" onChange="TestDriveCheck();">
															<%=mybean.PopulateModel()%>
														</select>
													</div>
												</div>

												<div class="form-element4">
													<label>Vehicle :</label>
													<div id="vehHint">
														<%=mybean.PopulateVehicle(mybean.comp_id, mybean.item_model_id)%>
													</div>
												</div>

												<div class="form-element4">
													<label>Sales Consultant:</label>
													<div id="exeHint">
														<%=mybean.PopulateExecutive()%>
													</div>
												</div>

												<div class="form-element12">
													<center>
														<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Submit" />
														<input name="submit_button" type="hidden" id="submit_button" value="Submit" />
													</center>
												</div>
										</form>
									</div>
								</div>
							</div>
						</div>
										<center><%=mybean.StrHTML%></center>
					</div>
				</div>
			</div>
		</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
		function TestDriveCheck() { //v1.0
			var branch_id = document.getElementById("dr_branch").value;
			var model_id = outputSelected(document.getElementById("dr_model").options);
			showHint('../sales/testdrive-check.jsp?vehicle=yes&model_id=' + model_id + '&branch_id=' + GetReplace(branch_id), 'vehHint');
		}

		function PopulateExecutives() { //v1.0
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?exe_branch_id=' + branch_id + '&executives=yes', 'exeHint');
		}
	</script>
</body>
</HTML>
