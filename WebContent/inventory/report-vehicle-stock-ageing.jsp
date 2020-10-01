<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.inventory.Report_Vehicle_Stock_Ageing" scope="request" />
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
						<h1>Vehicle Stock Ageing</h1>
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
						<li><a href="../inventory/report-vehicle-stock-ageing.jsp">Vehicle
								Stock Ageing</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- BODY START -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font><br />
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Vehicle Stock Ageing</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" class="form-horizontal"
											id="frm1">
											<div class="form-element3">
														<label>Date<font color=#ff0000><b> *</b></font>: </label>
															<input name="txt_starttime" id="txt_starttime"
																class="form-control datepicker"
																value="<%=mybean.start_time%>"
																type="text" maxlength="10" />
												</div>
												
<!-- 												<div class="form-element4"> -->
<!-- 														<label>End Date<font color=#ff0000><b> *</b></font>: </label> -->
<!-- 															<input name="txt_endtime" id="txt_endtime" -->
<!-- 																class="form-control datepicker" -->
<%-- 																value="<%=mybean.end_time%>" --%>
<!-- 																type="text" maxlength="10" /> -->
<!-- 												</div> -->
												<div class="form-element3">
														<label>Group By: </label>
															<select name="dr_groupby" class="form-control" id="dr_groupby">
																	<%=mybean.PopulateGroupBy(mybean.comp_id)%>
																</select>
												</div>
											

											<div class="form-element3" id="multiprincipal">
															<label>Brands:</label>
																<div><select name="dr_principal" size="10"
																	multiple="multiple" class="form-control multiselect-dropdown"
																	id="dr_principal" style="padding: 10px"
																	onChange="PopulateBranches();PopulateModels();PopulateRegion();">
																	<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
																</select></div>
														</div>
												<div class="form-element3">
															<label> Regions: </label><div id="regionHint"> <%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
															</div>
													</div>
													<div class="row"></div>
													<div class="form-element3">
															<label>Branches:</label> <div id="branchHint"> <%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
															</div>
													</div>
													
													<div class="form-element3">
															<label>Model:</label> <div id="modelHint"> <%=mybean.mischeck.PopulateModels(mybean.brand_id,
																	mybean.model_ids, mybean.comp_id, request)%>
															</div>
													</div>
													
											<div class="form-element3" >
															<label>Variant:</label> <div id="itemHint"> <%=mybean.mischeck.PopulateVariants(mybean.model_id,
																	mybean.item_ids, mybean.comp_id, request)%>
															</div>
													</div>

													<div class="form-element3">
															<label>Fuel Type:</label> <div><%=mybean.PopulateFuelType(mybean.comp_id, request)%>
													</div></div>
													<div class="row"></div>
													<div class="form-element3">
															<label>Color: </label><div id="colorHint"> <%=mybean.mischeck.PopulateColor(mybean.brand_id,
																	mybean.option_ids, mybean.comp_id, request)%>
															</div>
													</div>
													
													<div class="form-element3">
															<label>Pending Delivery:</label><div> <%=mybean.PopulatePendingdelivery()%>
														</div></div>
														
													<div class="form-element3" >
														<label>Status:</label> <div id="itemHint"> 
														<%=mybean.PopulateStockStatus()%>
														</div>
													</div>
														
															<div class="form-element12">
													<center>
														<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" />
																<input type="hidden" name="submit_button" value="Submit" />
													</center>
												</div>
												</div>
											</div>
										</form>
									</div>
								</div>
							</div>
						</div>
						<%
							if (!mybean.AgeingHTML.equals("")) {
						%>
					
						<div class="portlet box ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">&nbsp;</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="container-fluid">
									<div id="chart1" style="height: 700px;"></div>
									<br></br>
									<div><%=mybean.AgeingHTML%></div>
									
									<br></br>
<%-- 									<div><%=mybean.TotalStockDetails()%></div> --%>
								</div>
							</div>
						</div>
						<%
							}if (!mybean.no_Ageingdata.equals("")) {
						%>
						<div class="portlet box ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">&nbsp;</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<br></br>
									<div><%=mybean.no_Ageingdata%></div>
								</div>
							</div>
						</div>
						<%
							}
						%>
<!-- 						<div class="portlet box "> -->
<!-- 							<div class="portlet-title" style="text-align: center"> -->
<!-- 								<div class="caption" style="float: none">&nbsp;</div> -->
<!-- 							</div> -->
<!-- 							<div class="portlet-body portlet-empty"> -->
<!-- 								<div class="tab-pane" id=""> -->
									
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
						
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
<!-- Start additional plugins -->
<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
<script type="text/javascript" src="../Library/amcharts/pie.js"></script>
<!-- Start Export Image additional plugins -->
<script src="../Library/amcharts/exporting/amexport.js" type="text/javascript"></script>
<script src="../Library/amcharts/exporting/rgbcolor.js" type="text/javascript"></script>
<script src="../Library/amcharts/exporting/canvg.js" type="text/javascript"></script>
<script src="../Library/amcharts/exporting/filesaver.js" type="text/javascript"></script>
<!-- End Export Image additional plugins -->

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
				fileName : "SOE"
			}
		};
		AmCharts
				.ready(function() {

					var chartData = <%=mybean.chart_data%>;
					// PIE CHART
					chart = new AmCharts.AmPieChart();
					chart.dataProvider = chartData;
					chart.titleField = "type";
					chart.valueField = "total";
					chart.colorField="color";
					chart.labelColorField="color";
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
				});
	</script>
	
	<script language="JavaScript" type="text/javascript">
	function Subjectlist() {
		document.form1.submit();
	}
	
	function PopulateRegion() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		//alert("111111------"+brand_id);
		showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
	}
	
	function PopulateBranches() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		//alert("222------"+region_id);
		showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
	}
	
	function PopulateModels() { //v1.0

		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		//salert("333------"+brand_id);
		showHint('../sales/mis-check1.jsp?brand_id=' + brand_id + '&model=yes', 'modelHint');
	}

	function PopulateVariants() {
		var model_id = outputSelected(document.getElementById("dr_model").options);
		//salert("333------"+brand_id);
		showHint('../sales/mis-check1.jsp?model_id=' + model_id + '&item=yes', 'itemHint');
	}

	function PopulateColor() {
		var model_id = outputSelected(document.getElementById("dr_model").options);
		showHint('../sales/mis-check1.jsp?model_id=' + model_id + '&color=yes', 'colorHint');
	}
</script>

</body>
</HTML>
