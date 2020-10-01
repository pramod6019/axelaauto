<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.MIS_Dash_Enquiry"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />

<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script>
	$(function() {
		$("#txt_starttime").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
		$("#txt_endtime").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
	});

	function GroupCheck() { //v1.0
		var branch_id = document.getElementById("dr_branch").value;
		showHint('../MIS_Check.do?multiple=yes&exe_branch_id='
				+ GetReplace(branch_id), 'exeHint');
	}
</script>

<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Enquiry Dashboard</h1>
					</div>
					<!-- END PAGE TITLE -->

				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- BEGIN PAGE BREADCRUMBS -->
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="mis.jsp">MIS</a> &gt;</li>
						<li><a href="mis-dash-enquiry.jsp">Enquiry Dashboard</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Search</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form method="post" name="formemp" class="form-horizontal">
											<div class="form-group">
												<%
													if (mybean.branch_id.equals("0")) {
												%>
												<label class="control-label col-md-4">Branch: </label>
												<div class="col-md-6 col-xs-12">
													<select name="dr_branch" id="dr_branch"
														class="form-control" onChange="GroupCheck();">
														<%=mybean.PopulateBranch(mybean.dr_branch_id, "", "", "", request)%>
													</select>
													</td>

													<%
														} else {
													%>
													<input type="hidden" name="dr_branch" id="dr_branch"
														value="<%=mybean.branch_id%>" />
													<%
														}
													%>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Start Date<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<input name="txt_starttime" id="txt_starttime" type="text"
														class="form-control" value="<%=mybean.start_time%>"
														size="12" maxlength="10" />
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">End Date<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<input name="txt_endtime" id="txt_endtime" type="text"
														class="form-control" value="<%=mybean.end_time%>"
														size="12" maxlength="10" />
												</div>
											</div>											
											<div class="form-group">
												<label class="control-label col-md-4">Executive:</label>
												<div class="col-md-6 col-xs-12">
													<span id="exeHint"><%=mybean.PopulateExecutive()%></span>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Courses:</label>
												<div class="col-md-6 col-xs-12">
													<select name="dr_course" size="10" multiple="multiple"
														class="selectbox" id="dr_course" style="width: 250px">
														<%=mybean.PopulateCourse()%>
													</select>
												</div>
											</div>
											<div class="form-group">
												<center>
													<input name="submit_button" type="submit"
														class="btn btn-success" id="submit_button" value="Go" />
													<input type="hidden" name="submit_button" value="Submit">
												</center>
											</div>
										</form>
									</div>
								</div>
							</div>
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Source Summary</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<%=mybean.StrHTML%>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@include file="../Library/admin-footer.jsp"%>
</body>
</HTML>
