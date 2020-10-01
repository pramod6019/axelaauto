<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Report_Model_Dash"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<%
	if (!mybean.header.equals("no")) {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">

<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<link rel="shortcut icon" href="../test/favicon.ico" />


<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />

<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<script type="text/javascript"
	src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../assets/css/footable.core.css">
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
							</script>

<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />

<script language="JavaScript" type="text/javascript">
								function PopulateRegion() { //v1.0
									var brand_id = outputSelected(document
											.getElementById("dr_principal").options);
									//alert("111111------"+brand_id);
									showHint(
											'../sales/mis-check1.jsp?multiple=yes&brand_id='
													+ brand_id + '&region=yes',
											'regionHint');
								}

								function PopulateBranches() { //v1.0
									var brand_id = outputSelected(document
											.getElementById("dr_principal").options);
									var region_id = outputSelected(document
											.getElementById("dr_region").options);
									//alert("222------------"+region_id);
									showHint(
											'../sales/mis-check1.jsp?multiple=yes&brand_id='
													+ brand_id + '&region_id='
													+ region_id + '&branch=yes',
											'branchHint');
								}

								function PopulateModels() { //v1.0

									var brand_id = outputSelected(document
											.getElementById("dr_principal").options);
									//alert("333------"+brand_id);
									showHint(
											'../sales/mis-check1.jsp?brand_id='
													+ brand_id + '&model=yes',
											'modelHint');
								}
								
// 								function PopulateFuelTypes() { //v1.0

// 									var brand_id = outputSelected(document
// 											.getElementById("dr_principal").options);
// 									//alert("333------"+brand_id);
// 									showHint(
// 											'../sales/mis-check1.jsp?brand_id='
// 													+ brand_id + '&model=yes',
// 											'fuelHint');
// 								}

							
							</script>
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
						<h1>Model Dashboard</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY ----->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../sales/report-monitoring-board.jsp">Model
								Dashboard</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="red"><b> <%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Model Dashboard
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<div class="container-fluid">
												<div class="col-md-3 col-sm-4">
													<label class="control-label col-md-4 col-sm-4 col-xs-12">Start
														Date:</label>
													<div class="col-md-6">
														<input name="txt_starttime" id="txt_starttime"
															value="<%=mybean.start_time%>"
															class="form-control date-picker"
															data-date-format="dd/mm/yyyy" type="text" maxlength="10" />
													</div>
												</div>
												<div class="col-md-3 col-sm-4">
													<label class="control-label col-md-4 col-sm-4 col-xs-12">End
														Date:</label>
													<div class="col-md-6">
														<input name="txt_endtime" id="txt_endtime"
															value="<%=mybean.end_time%>"
															class="form-control date-picker"
															data-date-format="dd/mm/yyyy" type="text" maxlength="10" />
													</div>
												</div>
												<div class="col-md-3 col-sm-4">
													<label class="control-label col-md-4 col-sm-4 col-xs-12">Total
														By: </label>
													<div class="col-md-6">
														<select name="dr_totalby" class="form-control"
															id="dr_totalby">
															<%=mybean.PopulateTotalBy(mybean.comp_id)%>
														</select>
													</div>
												</div>
												<div class="col-md-3 col-sm-12" align="center">
													<center>
														<input type="submit" name="submit_button"
															id="submit_button" class="btn btn-success" value="Go" />
														<input type="hidden" name="submit_button" value="Submit" />
													</center>
												</div>
											</div>
											<br>

											<div class="container-fluid">
												<div class="">
													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Brands:
															<div id="multiprincipal">
																<select name="dr_principal" size="10"
																	multiple="multiple" class="form-control"
																	id="dr_principal"
																	onChange="PopulateBranches();PopulateModels();PopulateRegion();">
																	<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids,
						mybean.comp_id, request)%>
																</select>
															</div>
														</div>
													</div>

													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Regions: <span id="regionHint"> <%=mybean.mischeck.PopulateRegion(mybean.brand_id,
						mybean.region_ids, mybean.comp_id, request)%>
															</span>
														</div>
													</div>

													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Branches: <span id="branchHint"> <%=mybean.mischeck.PopulateBranches(mybean.brand_id,
						mybean.region_id, mybean.branch_ids, mybean.comp_id,
						request)%>
															</span>
														</div>
													</div>
													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															SOE: <select name="dr_soe" size="10" multiple="multiple"
																class="form-control" id="dr_soe">
																<%=mybean.PopulateSoe()%>
															</select>
														</div>
													</div>


												</div>
											</div>
											<br>
											<div class="container-fluid">
												<div class="">
													<div class="col-md-3 col-sm-6">
														<div style="margin: 10px;">
															Model: <span id="modelHint"> <%=mybean.mischeck.PopulateModels(mybean.brand_id,
						mybean.model_ids, mybean.comp_id, request)%>
															</span>
														</div>
													</div>
<!-- 													<div class=""> -->
<!-- 														<div class="col-md-3 col-sm-6"> -->
<!-- 															<div style="margin: 10px;"> -->
<%-- 																Fuel Type: <span id="fuelHint"> <%=mybean.PopulateFuelTypes(request)%>  --%>
<!-- 																</span> -->
<!-- 															</div> -->
<!-- 														</div> -->


<!-- 													</div> -->
												</div>
										</form>
									</div>
								</div>
							</div>

							<%
								if (!mybean.StrHTML.equals("")) {
							%>


							<center><%=mybean.StrHTML%></center>

							<%
								}
							%>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%
		}
	%>
	<%@include file="../Library/admin-footer.jsp"%>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="../assets/js/footable.js" type="text/javascript"></script>
	<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>
</body>
</HTML>
