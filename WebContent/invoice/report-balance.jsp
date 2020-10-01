<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.invoice.Report_Balance"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
	<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript">
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
	function PopulateTeam() {
		var branch_id = document.getElementById("dr_branch").value;
		showHint('../sales/report-team-check.jsp?team=yes&exe_branch_id='
				+ branch_id, 'multiteam');
	}

	function ExeCheck() { //v1.0
		var branch_id = document.getElementById("dr_branch").value;
		var team_id = outputSelected(document.getElementById("dr_team").options);
		showHint('../sales/mis-check1.jsp?team_id=' + team_id
				+ '&executives=yes', 'exeHint');
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
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
						<h1>Report Balance</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../invoice/report-balance.jsp">Balance Dues</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<center>
								<font color="red"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Balance Dues</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<div class="form-group">
												<label class="control-label col-md-4"> Branch: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<select name="dr_branch" id="dr_branch"
														class="form-control" onChange="PopulateTeam();ExeCheck();">
														<%=mybean.PopulateBranch(mybean.branch_id, "all", "", "", request)%>
													</select>

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Start Date:<font
													color=#ff0000><b> *</b></font></label>
												<div class="col-md-6">
													<input name="txt_starttime" id="txt_starttime"
														value="<%=mybean.starttime%>"
														class="form-control date-picker"
														data-date-format="dd/mm/yyyy" type="text" value="" />
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">End Date:<font
													color=#ff0000><b> *</b></font></label>
												<div class="col-md-6">
													<input name="txt_endtime" id="txt_endtime"
														value="<%=mybean.endtime%>"
														class="form-control date-picker"
														data-date-format="dd/mm/yyyy" type="text" value="" />
												</div>
											</div>
											<center>
												<input name="submit_button" type="submit"
													class="btn btn-success" id="submit_button" value="Go" /> <input
													type="hidden" name="submit_button" value="Submit">
											</center>
											<br>


											<div class="form-group">
												<label class="control-label col-md-4"> Teams: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<select name="dr_team" size="10" multiple="multiple"
														class="form-control" id="dr_team" onChange="ExeCheck();">
														<%=mybean.PopulateTeam(mybean.comp_id)%>
													</select>

												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Executive: </label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<span id="exeHint"><%=mybean.PopulateSalesExecutives(mybean.comp_id)%></span>
													<a href="invoice-list.jsp?smart=yes">(Export)</a>
												</div>
											</div>
											<center><%=mybean.StrHTML%></center>

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




	<%@include file="../Library/admin-footer.jsp"%>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
</body>
</HTML>
