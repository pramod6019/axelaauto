<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_JobCard_PSF_Followup_Status" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>


<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>

</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

	<!-- 	BODY -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>PSF Follow-up Status</h1>
					</div>
					<!-- END PAGE TITLE -->
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-jobcard-psf-followup-status.jsp">PSF Follow-up Status</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="red"><b> <%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							
						<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">PSF Follow-up Status</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form method="post" name="frm1" id="frm1" class="form-horizontal">
										<div class="row">
											<div class="form-element4">
												<label >Total By:&nbsp;</label>
												<div >
													<select name="dr_totalby" class="form-control" id="dr_totalby">
														<%=mybean.PopulateTotalBy(mybean.comp_id)%>
													</select>
												</div>
											</div>

											<div class="form-element4">
												<label >Start Date:&nbsp;</label>
												<input name="txt_starttime" id="txt_starttime" value="<%=mybean.start_time%>"
													class="form-control datepicker" type="text"/>
											</div>
											<div class="form-element4">
												<label >End Date:&nbsp;</label>
												<input name="txt_endtime" id="txt_endtime" value="<%=mybean.end_time%>"
													class="form-control datepicker" type="text"/>
											</div>
										</div>
										
										<div class="form-element3">
											<label>Brands:</label>
											<div>
												<select name="dr_principal" size="10" multiple="multiple"
													class="form-control multiselect-dropdown" id="dr_principal"
													onChange="PopulateModels();PopulateBranches();PopulateRegion();">
													<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
												</select>
											</div>
										</div>

										<div class="form-element3">
											<label>Regions:</label>
											<div id="regionHint">
												<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
											</div>
										</div>

										<div class="form-element3">
											<label>Branch:</label>
											<div id="branchHint"> 
												<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.jc_branch_ids, mybean.comp_id,request)%>
											</div>
										</div>

<!-- 											<div class="form-element3"> -->
<!-- 													<div>Service  Executive:</div> -->
<%-- 													<span id="exeHint"><%=mybean.PopulateSalesExecutives()%></span> --%>
<!-- 												</div> -->


											<div class="form-element3">
											<label>Model:</label>
											<div id="modelHint">
												<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.jc_branch_id, mybean.comp_id, request)%>
											</div>
										</div>
										<div class="form-element3">
											<label>CRM Executive:</label>
											<div id="multicrm">
												<%=mybean.mischeck.PopulateCRM(mybean.crm_emp_ids, mybean.jc_branch_id, mybean.comp_id, request)%>
											</div>
										</div>
												<div class="form-element3">
											<label>Follow-up Days:</label>
											<div id="multipsfdays">
												<%=mybean.mischeck.PopulatePSFDays(mybean.psfdays_ids, mybean.jc_branch_id, mybean.comp_id)%>
											</div>
										</div>
											<div class="form-element3">
												<label >Dis-Satisfied:</label>
												<div id="dissatisfiedHint">
													<%=mybean.mischeck.PopulatePsfConcern(mybean.comp_id, mybean.jcpsfconcern_ids)%>
												</div>
											</div>
													<div class="form-element12">
														<center><input name="submit_button" type="submit"
															class="btn btn-success" id="submit_button" value="Go" /></center>
														<input type="hidden" name="submit_button" value="Submit" />
												</div>

											</form>
									</div>
								</div>
							</div>
								
								<%
								if (!mybean.chart_data.equals("")) {
							%>
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">&nbsp;</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">

										<%
											if (mybean.NoChart.equals("")) {
										%>
											<div id="chart1" style="height: 700px;"></div>
											<center>
												<b>Total: <%=mybean.chart_data_total%></b>
											</center>
										<%
											} else {
										%>
											<%=mybean.NoChart%>
										<%
											}
										%>
										<%
											}
										%>
										<%
											if (!mybean.StrHTML.equals("")) {
										%>

											<center><%=mybean.StrHTML%></center>
											
										<%
											}
										%>
									</div>
								</div>

								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">


										<%
											if (!mybean.StrHTMLpsfconcern.equals("")) {
										%>

										<center><%=mybean.StrHTMLpsfconcern%></center>
										<%
											}
										%>
									</div>
								</div> 
							</div>
						</div>
					</div>

				<!-- END CONTAINER -->
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
	<!-- End additional plugins -->
	<script language="JavaScript" type="text/javascript">
		//    function PopulateTeam(){		
		//         var branch_id=document.getElementById("dr_branch").value;		
		// 	    showHint('../sales/report-team-check.jsp?team=yes&exe_branch_id='+branch_id,  'multiteam');		
		// 	    }
// 		function ExeCheck() { //v1.0
// 			var branch_id = document.getElementById("dr_branch").value;
// 			showHint( '../service/report-check.jsp?multiple=yes&executive=yes&exe_branch_id=' + GetReplace(branch_id), 'exeHint');
// 		}
// 		function PopulatePSFFollowupdays() {
// 			var branch_id = document.getElementById("dr_branch").value;
// 			// 		alert(branch_id);
// 			showHint( '../service/report-check.jsp?psfdays=yes&multiple=yes&exe_branch_id=' + GetReplace(branch_id), 'followupHint');
// 		}
// 		function PopulateModels() { 
// // 			var brand_id = outputSelected(document.getElementById("dr_principal").options);
// 			var branch_id = outputSelected(document.getElementById("dr_branch").options);
// 			showHint('../service/mis-check1.jsp?branch_id=' + branch_id + '&model=yes', 'modelHint');
// 		}
		
		
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
	
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}
	
		function PopulateModels() { 
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
			showHint('../service/mis-check1.jsp?brand_id=' + brand_id +"&branch_id= " + branch_id + '&model=yes', 'modelHint');
		}
	
		function PopulateAdviser() {
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
			showHint('../service/mis-check1.jsp?branch_id=' + branch_id + '&advisor=yes', 'multiadvisor');
		}
	
		function PopulateCRM() {
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
			showHint('../service/mis-check1.jsp?branch_id=' + branch_id + '&crmexe=yes', 'multicrm');
		}
	
		function Populatepsfdays() {
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
			showHint('../service/mis-check1.jsp?branch_id=' + branch_id + '&psfdays=yes', 'multipsfdays');
		}
		
		function PopulateTech(){}
		
		function PopulateCRMDays(){}
		function PopulateBranch(){}
		function PopulateZone(){}
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
				fileName : "PSF Follow-up Status"
			}
		};
		AmCharts.ready(function() {
			<%if (mybean.NoChart.equals("")) {%>
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
