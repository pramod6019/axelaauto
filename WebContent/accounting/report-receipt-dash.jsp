<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.Report_Receipt_Dash" scope="request"/>
<%mybean.doPost(request,response); %>
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
						<h1>Receipt Dashboard</h1>
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
							<li><a href="../accounting/report-receipt-dash.jsp">Receipt Dashboard</a><b>:</b></li> 
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="red"><b> <%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Receipt Dashboard</div>
								</div>

								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="form-element6">
												<label >Start Date:</label>
												<div>
													<input name="txt_starttime" id="txt_starttime" value="<%=mybean.start_time%>"
														class="form-control datepicker"  type="text" />
												</div>
											</div>
											
											<div class="form-element6">
												<label >End Date:</label>
												<div>
													<input name="txt_endtime" id="txt_endtime" value="<%=mybean.end_time%>"
														class="form-control datepicker" type="text" />
												</div>
											</div>

											<div class="form-element3">
												<label>Brands:</label>
												<div>
													<select name="dr_principal" size="10" multiple="multiple" class="form-control multiselect-dropdown"
														id="dr_principal" onChange="PopulateBranches();PopulateRegion();">
															<%=mybean.acccheck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element3">
												<label>Regions:</label>
												<div id="regionHint">
													<%=mybean.acccheck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label>Branches:</label>
												<div id="branchHint">
													<%=mybean.acccheck.PopulateBranch(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element3">
												<label> Executive:</label>
												<div id="exeHint">
													<%=mybean.acccheck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element12" align="center">
												<center>
													<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>

										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
					<%
						if (!mybean.StrHTML.equals("")) {
					%>
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Invoice Summary</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<center><%=mybean.InvoiceStrHTML%></center>
								</div>
							</div>
						</div>
					<%
						}
					%>

					<%
						if (!mybean.StrHTML.equals("")) {
					%>
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Receipt Summary</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<center><%=mybean.StrHTML%></center>
								</div>
							</div>
						</div>
					<%
						}
					%>


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

	
	<script type="text/javascript">
	
		//This sholuldn't be removed because it is need branch and model
		function PopulateCRMDays(){ 
		}
		function PopulateVariants(){ 
		} 
		function PopulateColor(){ 
		} 
		//This sholuldn't be removed because it is need branch and model
	
	
		function PopulateRegion() {
			var brand_id =outputSelected(document.getElementById("dr_principal").options);
			 //alert("111111------"+brand_id);
			showHint('../accounting/acc-check.jsp?multiple=yes&brand_id='+brand_id+'&region=yes','regionHint');
		} 
		function PopulateBranches() { 
			var brand_id=outputSelected(document.getElementById("dr_principal").options);
			var region_id=outputSelected(document.getElementById("dr_region").options);
			showHint('../accounting/acc-check.jsp?multiple=yes&brand_id='+brand_id+ '&region_id=' + region_id+'&branch=yes','branchHint');
		}
	
		function PopulateModels() { 
			var brand_id=outputSelected(document.getElementById("dr_principal").options);
			showHint('../accounting/acc-check.jsp?brand_id='+brand_id+'&model=yes','modelHint');
		}
	
		function PopulateTeams(){		
			var branch_id=outputSelected(document.getElementById("dr_branch").options);	
			showHint('../accounting/acc-check.jsp?branch_id='+branch_id+'&team=yes', 'teamHint');
		}
				
		function PopulateExecutives() {
			var exe_branch_id=outputSelected(document.getElementById("dr_branch").options);
			showHint('../accounting/acc-check.jsp?exe_branch_id='+exe_branch_id+'&branchexecutives=yes', 'exeHint');
		}
		   
		   
	</script>
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
				fileName : "Receipt"
			}
		};
		AmCharts.ready(function() {
		<% if(mybean.NoChart.equals("")){%> 
			var chartData = <%=mybean.chart_data%>;
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

</body>
</HTML>
