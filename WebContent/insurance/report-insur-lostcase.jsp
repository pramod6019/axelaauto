<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Report_Insur_LostCase"
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
<script type="text/javascript">
	var chart;
	var legend;
	var export1 = {
		menuTop : "0px",
		menuItems : [ {
			icon : '../Library/amcharts/images/export.png',
			format : 'jpg'
		} ],
		menuItemOutput : {
			fileName : "Lost Case Scenario"
		}
	};
	AmCharts
			.ready(function() {
<%if (mybean.NoChart.equals("")) {%>
	var chartData =
<%=mybean.chart_data%>
	;
				// PIE CHART
				chart = new AmCharts.AmPieChart();
				chart.dataProvider = chartData;
				chart.titleField = "type";
				chart.valueField = "total";
				chart.minRadius = 200;

				// LEGEND
				legend = new AmCharts.AmLegend();
				legend.align = "center";
				legend.markerType = "circle";
				chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
				chart.exportConfig = export1;
				chart.addLegend(legend);

				// WRITE
				chart.write("chart1");
<%}%>
	});
</script>
<body class="page-container-bg-solid page-header-menu-fixed" onload="convertmultiselect();">
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
						<h1>Lost Case Scenario</h1>
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
						<li><a href="report-lostcase-scenario.jsp">Lost Case
								Scenario</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Lost Case
										Scenario</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<!-- FORM START -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
												<div class="form-element6">
													<label> Start Date<font
														color=red>*</font>:
													</label>
														<input name="txt_starttime" id="txt_starttime" type="text"
															value="<%=mybean.start_time%>" size="12" maxlength="10"
															class="form-control datepicker" />

												</div>

												<!-- FORM START -->
												<div class="form-element6">
													<label> End Date<font
														color=red>*</font>:
													</label>
														<input name="txt_endtime" id="txt_endtime" type="text"
															value="<%=mybean.end_time%>" size="12" maxlength="10"
															class="form-control datepicker" />
												</div>

												<!-- FORM START -->
<!-- 												<div class="form-element1"> -->
<!-- 												</div> -->
												
												<div class="form-element4">
														<div>
															Executive: </div>
														<select name="dr_executive" size="10" multiple="multiple" style="padding:10px"
															class="form-control multiselect-dropdown" id="dr_executive"><%=mybean.PopulateInsurExecutive()%></select>
													
												</div>

												<div class="form-element3">
														<div>
															Manufacturer:</div>
															<select name="dr_manufacturer" size="10" multiple="multiple" 
															class="form-control multiselect-dropdown" id="dr_manufacturer" onchange="PopulateModels();">
															<%=mybean.mischeck.PopulateManufacturer(mybean.carmanuf_ids, mybean.carmanuf_id, mybean.comp_id, request)%>
															</select>
												</div>
													
													
												<div class="form-element3">
													<div> Model: </div>
														<span id="modelHint">
															<%=mybean.mischeck.PopulatePreownedModel(mybean.carmanuf_id, mybean.model_ids, mybean.comp_id, request)%>
														</span>
												</div>
												
												<div class="form-element12">
														<center>
															<input name="submit_button" type="submit"
																class="btn btn-success" id="submit_button" value="Go" />
														</center>
														<input type="hidden" name="submit_button" value="Submit">
												</div>
										</form>
									</div>
								</div>
							</div>
							<!-- 	PORTLET -->

							<!-- 	PORTLET -->
							<%
								if (mybean.chart_data_total != 0 || !mybean.NoChart.equals("")) {
							%>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"></div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<%
											if (mybean.NoChart.equals("")) {
										%>
										<div id="chart1" style="height: 700px;"></div>
										<center>
											<b>Total: <%=mybean.chart_data_total%></b>
										</center>
										<%}else{%>
										<center><%=mybean.NoChart%></center>
										<%}%>

										<%} %>
									</div>
								</div>
							</div>
							<!-- 	PORTLET -->

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/js.jsp"%>
		<script src="../assets/js/footable.js" type="text/javascript"></script>
		<script type="text/javascript">
		function PopulateModels() {
			var manufacturer_id = outputSelected(document.getElementById("dr_manufacturer").options);
// 			alert(manufacturer_id);
			showHint('../insurance/mis-check.jsp?carmanuf_id=' + manufacturer_id + '&preownedmodel=yes', 'modelHint');
		}
		
	</script>

		<%@ include file="../Library/admin-footer.jsp"%>
</body>
</HTML>
