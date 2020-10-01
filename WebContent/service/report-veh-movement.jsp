<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_Veh_Movement"
	scope="request" />
<jsp:useBean id="mischeck" class="axela.service.Report_Veh_Movement"
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
</HEAD>

<script language="JavaScript" type="text/javascript">
	function PopulateRegion() { //v1.0
		var brand_id = outputSelected(document
				.getElementById("dr_principal").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id='
				+ brand_id + '&region=yes', 'regionHint');
	}

	function PopulateBranches() { //v1.0
		var brand_id = outputSelected(document
				.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id='
				+ brand_id + '&region_id=' + region_id + '&branch=yes',
				'branchHint');
	}
	function ExeTechnicianCheck() { ///v1.0
		var branch_id = document.getElementById("dr_branch").value;
		showHint(
				'../service/report-check.jsp?multiple=yes&technician=yes&branch_id='
						+ GetReplace(branch_id), 'technicianHint');
	}

	function ExeAdvisorCheck() { ///v1.0
		var branch_id = document.getElementById("dr_branch").value;
		showHint(
				'../service/report-check.jsp?multiple=yes&advisor=yes&branch_id='
						+ GetReplace(branch_id), 'advisorHint');
	}
	function PopulateBranch(){}
	function PopulateAdviser(){}
	function PopulateZone(){}
	function PopulateTech(){}
	function PopulateModels(){}
	function Populatepsfdays(){}
	function PopulateCRMDays(){}
</script>


<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- 	MULTIPLE SELECT END-->
	<!-- 	BODY -->
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
						<h1>Vehicle Movement Dashboard</h1>
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
						<li><a href="report-veh-movement.jsp">Vehicle Movement
								Dashboard</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Vehicle Movement
										Dashboard</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<!-- START PORTLET BODY -->
												<div class="form-element4">
													<div> Brands: </div>
															<select name="dr_principal" size="10" multiple="multiple"
																class="form-control multiselect-dropdown" id="dr_principal"
																onChange="PopulateBranches();PopulateRegion();">
																<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids,
					mybean.comp_id, request)%>
															</select>
												</div>

												<div class="form-element4">
													<div> Regions: </div>
														<span id="regionHint"> <%=mybean.mischeck.PopulateRegion(mybean.brand_id,
					mybean.region_ids, mybean.comp_id, request)%>
														</span>
												</div>
												<div class="form-element4">
													<div> Branches:</div>
														<%
															if (mybean.branch_id.equals("0")) {
														%>
														<span id="branchHint"> <%=mybean.mischeck.PopulateBranches(mybean.brand_id,
						mybean.region_id, mybean.jc_branch_ids, mybean.comp_id, request)%>
														</span>
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

												<div class="form-element12" align="center">
													<center>
														<input type="submit" name="submit_button"
															style="margin-top: 50px;" id="submit_button"
															class="btn btn-success" value="Go" /> <input
															type="hidden" name="submit_button" value="Submit" />
													</center>
												</div>
											<br>
										</form>
									</div>

								</div>
								<br>
								<div class="col-md-1"></div>
								<div class="container-fluid">
									<div class="portlet box  form-element5">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Vehicle
												Movement for Today</div>
										</div>
										<div class="portlet-body portlet-empty container-fluid">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<%=mybean.JCsToday%>
											</div>
										</div>
									</div>
									<div class="portlet box  form-element5">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Carry Over</div>
										</div>
										<div class="portlet-body portlet-empty container-fluid">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<%=mybean.JCsToday1%>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<!-- 	PORTLET -->

						<div class="portlet box  form-element12">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Vehicle Movement
									Traffic</div>
							</div>
							<div class="portlet-body portlet-empty container-fluid">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<div class="table-responsive table-bordered \">
										<table class="table" data-filter="#filter">
											<thead>
												<tr>
													<th data-toggle="true"><%=mybean.day8name%><br><%=mybean.day8%></th>
													<th><%=mybean.day7name%><br><%=mybean.day7%></th>
													<th><%=mybean.day6name%><br><%=mybean.day6%></th>
													<th data-hide="phone"><%=mybean.day5name%><br><%=mybean.day5%></th>
													<th data-hide="phone"><%=mybean.day4name%><br><%=mybean.day4%></th>
													<th data-hide="phone"><%=mybean.day3name%><br><%=mybean.day3%></th>
													<th data-hide="phone"><%=mybean.day2name%><br><%=mybean.day2%></th>
													<th data-hide="phone"><%=mybean.day1name%><br><%=mybean.day1%></th>
												</tr>
											</thead>
											<!--                               <tr align="center"> -->
											<!--                                 <td colspan="8">&nbsp;</td> -->
											<!--                               </tr> -->

											<tr align="center">
												<td colspan="2">Week <%=mybean.week4%><br><%=mybean.logWeek4%></td>
												<td colspan="2">Week <%=mybean.week3%><br><%=mybean.logWeek3%></td>
												<td colspan="2">Week <%=mybean.week2%><br><%=mybean.logWeek2%></td>
												<td colspan="2">Week <%=mybean.week1%><br><%=mybean.logWeek1%></td>
											</tr>
											<!--                               <tr align="center"> -->
											<!--                                 <td colspan="8">&nbsp;</td> -->
											<!--                               </tr> -->
											<tr align="center">
												<td colspan="2"><%=mybean.month4%><br><%=mybean.logMonth4%></td>
												<td colspan="2"><%=mybean.month3%><br><%=mybean.logMonth3%></td>
												<td colspan="2"><%=mybean.month2%><br><%=mybean.logMonth2%></td>
												<td colspan="2"><%=mybean.month1%><br><%=mybean.logMonth1%></td>
											</tr>
											<!--                               <tr align="center"> -->
											<!--                                 <td colspan="8">&nbsp;</td> -->
											<!--                               </tr> -->
											<tr align="center">
												<td colspan="2"><%=mybean.qur4%><br><%=mybean.logQur4%></td>
												<td colspan="2"><%=mybean.qur3%><br><%=mybean.logQur3%></td>
												<td colspan="2"><%=mybean.qur2%><br><%=mybean.logQur2%></td>
												<td colspan="2"><%=mybean.qur1%><br><%=mybean.logQur1%></td>
											</tr>
											</tbody>
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

	</div>
	<!-- END CONTAINER -->
	<!--                      <LINK REL="STYLESHEET" TYPE="text/css" -->
	<!-- 	HREF="../assets/css/footable.core.css"> -->
	<!-- <script src="../assets/js/footable.js" type="text/javascript"></script> -->
	<%@include file="../Library/admin-footer.jsp"%></body>
<%@include file="../Library/js.jsp"%>
</HTML>
