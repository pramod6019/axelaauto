<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_JobCard_Dashboard"
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

<%@include file="../Library/css.jsp"%>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Report Jobcard Dashboard</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-jobcard-dashboard.jsp">Job Card
								Dashboard</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<div class="container-fluid">
								<!-- 					BODY START -->
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Job Card
											Dashboard</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<form method="post" name="frm1" class="form-horizontal" id="frm1">
													<div class="form-element4">
														<label>Branch<font
															color="red">*</font>:
														</label>
															<%
																if (mybean.branch_id.equals("0")) {
															%>
															<select name="dr_branch" id="dr_branch"
																class="form-control"
																onChange="ExeTechnicianCheck(); ExeAdvisorCheck();">
																<%=mybean.PopulateBranch(mybean.dr_branch_id, "", "3", "", request)%>
															</select>
															<%
																} else {
															%>
															<input type="hidden" name="dr_branch" id="dr_branch"
																value="<%=mybean.branch_id%>" />
															<%=mybean.getBranchName(mybean.dr_branch_id,
						mybean.comp_id)%>
															<%
																}
															%>
													</div>


													<%-- <div class="col-md-6 col-xs-12">
														<div class="control-label col-md-4">
															<label class="">Branch<font color=red>*</font>:
															</label>
														</div>
														<div class="col-md-4 col-xs-12">
															<%
															if (mybean.branch_id.equals("0")) {
														%>
															<select name="dr_branch" id="dr_branch"
																class="form-control"
																onChange="ExeTechnicianCheck(); ExeAdvisorCheck();">
																<%=mybean.PopulateBranch(mybean.dr_branch_id, "", "1,3", "", request)%>
															</select>
															<%
															} else {
														%>
															<input type="hidden" name="dr_branch" id="dr_branch"
																value="<%=mybean.branch_id%>" />
															<%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
															<%
															}
														%>
														</div>
													</div> --%>

												<!-- START PORTLET BODY -->

													<div class="form-element4">
														<div> Advisor:</div>
															<span id="advisorHint"> <%=mybean.reportexe.PopulateServiceAdvisors(
					mybean.dr_branch_id, mybean.advisorexe_ids,
					mybean.ExeAccess, mybean.comp_id)%>
															</span>
													</div>
													<div class="form-element4">
														<div> Technician: </div>
														<span id="technicianHint"><%=mybean.reportexe.PopulateTechnicians(mybean.dr_branch_id,
					mybean.technicianexe_ids, mybean.ExeAccess, mybean.comp_id)%></span>
													</div>
													
													<div class="form-element12">
														<center>
															<input name="submit_button" type="submit"
																class="btn btn-success" id="submit_button" value="Go" />
															<input type="hidden" name="submit_button" value="Submit" />
														</center>
													</div>




											</form>
										</div>
									</div>
								</div>
								<div class="form-element3">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Job Cards for
												Today</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<table class="table table-boarder">
													<tr>

													</tr>
													<%=mybean.JCsToday%>
												</table>

											</div>
										</div>
									</div>
								</div>
								<div class="form-element3">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Job Cards Type
												For Today</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<table class="table table-boarder">

													<%=mybean.OpenTypeToday%>
												</table>
												</td>
												<td width="25%"><br>
											</div>
										</div>
									</div>
								</div>
								<div class="form-element3">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Ready Today</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<table class="table table-boarder">

													<%=mybean.ReadyJCsToday%>
												</table>
												<table class="table table-hover table-boarder">
													<tr align="center">
														<thead>
															<th colspan="2">Delivered Today</th>
														</thead>
													</tr>
													<%=mybean.DeliveredJCsToday%>
												</table>
												<table class="table table-hover table-boarder">
													<tr align="center">
														<thead>
															<th colspan="2">Overdue Priority</th>
														</thead>
													</tr>
													<%=mybean.OverduePriority%>
												</table>

											</div>
										</div>
									</div>
								</div>
								<div class="form-element3">
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Overdue Stage</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<table class="table table-boarder">

													<%=mybean.OverdueStage%>
												</table>

											</div>
										</div>
									</div>
								</div>



							</div>
						</div>
							<div class="form-element3">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Open Service
											Stage</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<table class="table table-boarder">

												<%=mybean.OpenServiceStage%>
											</table>
											</td>


										</div>
									</div>
								</div>
							</div>
							<div class="form-element3">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Open Priority</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<table class="table table-boarder">

												<%=mybean.OpenPriority%>
											</table>

										</div>
									</div>
								</div>
							</div>
							<div class="form-element3">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Open Type</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<table class="table table-boarder">

												<%=mybean.OpenType%>
											</table>

										</div>
									</div>
								</div>
							</div>
							<div class="form-element3">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Open Body Shop
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<table class="table table-boarder">

												<%=mybean.OpenBodyShop%>
											</table>

										</div>
									</div>
								</div>
							</div>
							
						<div class="container-fluid">
							<div class="form-element12">

								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"></div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div id="ageingchartdiv" style="height: 300px;"></div>

										</div>
									</div>
								</div>



							</div>
						</div>
						<div class="container-fluid">
							<div class="form-element12">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"></div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->

											<table class="table table-boarder table-responsive">
												<tr align="center">
													<th colspan="8">Job Cards Traffic</th>
												</tr>
												<tr align="center">
													<td><%=mybean.day8name%><br><%=mybean.day8%></td>
													<td><%=mybean.day7name%><br><%=mybean.day7%></td>
													<td><%=mybean.day6name%><br><%=mybean.day6%></td>
													<td><%=mybean.day5name%><br><%=mybean.day5%></td>
													<td><%=mybean.day4name%><br><%=mybean.day4%></td>
													<td><%=mybean.day3name%><br><%=mybean.day3%></td>
													<td><%=mybean.day2name%><br><%=mybean.day2%></td>
													<td><%=mybean.day1name%><br><%=mybean.day1%></td>
												</tr>
												<tr align="center">
													<td colspan="8">&nbsp;</td>
												</tr>
												<tr align="center">
													<td colspan="2">Week <%=mybean.week4%><br><%=mybean.logWeek4%></td>
													<td colspan="2">Week <%=mybean.week3%><br><%=mybean.logWeek3%></td>
													<td colspan="2">Week <%=mybean.week2%><br><%=mybean.logWeek2%></td>
													<td colspan="2">Week <%=mybean.week1%><br><%=mybean.logWeek1%></td>
												</tr>
												<tr align="center">
													<td colspan="8">&nbsp;</td>
												</tr>
												<tr align="center">
													<td colspan="2"><%=mybean.month4%><br><%=mybean.logMonth4%></td>
													<td colspan="2"><%=mybean.month3%><br><%=mybean.logMonth3%></td>
													<td colspan="2"><%=mybean.month2%><br><%=mybean.logMonth2%></td>
													<td colspan="2"><%=mybean.month1%><br><%=mybean.logMonth1%></td>
												</tr>
												<tr align="center">
													<td colspan="8">&nbsp;</td>
												</tr>
												<tr align="center">
													<td colspan="2"><%=mybean.qur4%><br><%=mybean.logQur4%></td>
													<td colspan="2"><%=mybean.qur3%><br><%=mybean.logQur3%></td>
													<td colspan="2"><%=mybean.qur2%><br><%=mybean.logQur2%></td>
													<td colspan="2"><%=mybean.qur1%><br><%=mybean.logQur1%></td>
												</tr>
											</table>

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
<%@include file="../Library/js.jsp"%>
		<script src="../Library/amcharts/amcharts.js" type="text/javascript"></script>
<script src="../Library/amcharts/serial.js" type="text/javascript"></script>

<script language="JavaScript" type="text/javascript">
	function ExeTechnicianCheck() { ///v1.0
		var branch_id = document.getElementById("dr_branch").value;
		showHint('../service/report-check.jsp?multiple=yes&technician=yes&branch_id='
						+ GetReplace(branch_id), 'technicianHint');
	}

	function ExeAdvisorCheck() { ///v1.0
		var branch_id = document.getElementById("dr_branch").value;
		showHint('../service/report-check.jsp?multiple=yes&advisor=yes&branch_id='
						+ GetReplace(branch_id), 'advisorHint');
	}
</script>

<script>
	var ageingchart;
	var ageingchartData =
<%=mybean.ageingchart_data%>
	;
	AmCharts.ready(function() {
		// SERIAL CHART FOR TICKET
		ageingchart = new AmCharts.AmSerialChart();
		ageingchart.dataProvider = ageingchartData;
		ageingchart.categoryField = "level";
		ageingchart.depth3D = 20;
		ageingchart.angle = 30;
		ageingchart.startDuration = 2;
		ageingchart.startEffect = "easeOutSine";
		ageingchart.marginsUpdated = false;
		ageingchart.autoMarginOffset = 0;
		ageingchart.marginLeft = 0;
		ageingchart.marginRight = 0;

		// AXES
		// Category
		var categoryAxis1 = ageingchart.categoryAxis;
		categoryAxis1.gridPosition = "start";
		categoryAxis1.autoRotateAngle = 50;
		categoryAxis1.autoRotateCount = 0;
		categoryAxis1.autogridCount = false;
		categoryAxis1.autoWrap = true;
		categoryAxis1.gridAlpha = 0.1;
		categoryAxis1.axisAlpha = 0;
		// value
		var valueAxis1 = new AmCharts.ValueAxis();
		valueAxis1.axisColor = "#DADADA";
		valueAxis1.title = "Job Card Ageing";
		//valueAxis1.position = "top";
		valueAxis1.gridAlpha = 1;
		valueAxis1.gridColor = "#DADADA";
		ageingchart.addValueAxis(valueAxis1);

		// GRAPH
		var ticketgraph = new AmCharts.AmGraph();
		ticketgraph.title = "Ageing";
		ticketgraph.valueField = "value";
		ticketgraph.type = "column";
		ticketgraph.balloonText = "[[category]]:[[value]]";
		ticketgraph.lineAlpha = 0;
		ticketgraph.colorField = "color";
		ticketgraph.fillAlphas = 1;
		ticketgraph.labelText = "[[value]]";
		ticketgraph.labelPosition = "top";
		ageingchart.addGraph(ticketgraph);

		ageingchart.creditsPosition = "top-right";

		// WRITE
		ageingchart.write("ageingchartdiv");
	});
</script>

</body>
</HTML>
