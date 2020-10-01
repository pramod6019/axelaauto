<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Report_Insurance_SOB_Dash" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@ include file="../Library/css.jsp"%>
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

</HEAD>
<script>



//This sholuldn't be removed because it is need branch and model
</script>

<%-- <script type="text/javascript">
var chart;
var legend;
var export1 = {
        menuTop:"0px",
        menuItems: [{
            icon: '../Library/amcharts/images/export.png',  
			format: 'jpg'
		}],
    	menuItemOutput:{
        	fileName: "SOE"
    	}
};
AmCharts.ready(function () {
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
</script> --%>

<body onload="convertmultiselect();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>SOB Dashboard</h1>
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
						<li><a href="../insurance/report-insurance-sob-dash.jsp">SOB Dashboard</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
                <center><font color="red"><b> <%=mybean.msg%></b></font></center>
					
						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">SOB Dashboard
									</div>
								</div>
								
								<div class="portlet-body portlet-empty container-fluid">
									<!-- START PORTLET BODY -->
									<form method="post" name="frm1" id="frm1"
										class="form-horizontal">

										<div class="form-element6">
											<label>Start Date<font color="red">*</font>:&nbsp;
											</label> <input name="txt_starttime" id="txt_starttime"
												value="<%=mybean.start_time%>"
												class="form-control datepicker" type="text" maxlength="10" />
										</div>


										<div class="form-element6">
											<label>End Date<font color="red">*</font>:&nbsp;
											</label> <input name="txt_endtime" id="txt_endtime"
												value="<%=mybean.end_time%>" class="form-control datepicker"
												type="text" maxlength="10" />
										</div>


										<div class="form-element3">
											<div>Brands:</div>
											<select name="dr_principal" size="10" multiple="multiple"
												class="form-control multiselect-dropdown" style="padding: 10px" id="dr_principal"
												onChange="PopulateBranches();PopulateRegion();">
												<%=mybean.mischeck.PopulatePrincipal(mybean.brand_id,
					mybean.brand_ids, mybean.comp_id, request)%>
											</select>
										</div>

										<div class="form-element3">
											<div>Regions:</div>
											<span id="regionHint"> <%=mybean.mischeck.PopulateRegion(mybean.brand_id,
					mybean.region_ids, mybean.comp_id, request)%>
											</span>
										</div>

										<div class="form-element3">
											<div>Branches:</div>
											<span id="branchHint"> <%=mybean.mischeck.PopulateBranches(mybean.brand_id,
					mybean.region_id, mybean.branch_ids, mybean.comp_id,
					request)%>
											</span>
										</div>


										<div class="form-element3">
											<div>Manufacturer:</div>
											<select name="dr_manufacturer" size="10" multiple="multiple"
												style="padding: 10px" class="form-control multiselect-dropdown"
												id="dr_manufacturer" onchange="PopulateModels();">
												<%=mybean.mischeck.PopulateManufacturer(mybean.carmanuf_ids,
					mybean.carmanuf_id, mybean.comp_id, request)%>
											</select>
										</div>



										<div class="form-element3">
											<div>Model:</div>
											<span id="modelHint"> <%=mybean.mischeck.PopulatePreownedModel(mybean.carmanuf_id,
					mybean.model_ids, mybean.comp_id, request)%>
											</span>
										</div>

										<div class="form-element3">
											<div>Insurance Executive:</div>
											<span id="exeHint"> <%=mybean.mischeck.PopulateInsurEmp(mybean.insur_emp_ids,
					mybean.branch_id, mybean.comp_id, request)%>
											</span>
										</div>

										<div class="form-element12">
											<center>
												<input type="submit" name="submit_button" id="submit_button"
													class="btn btn-success" style="margin-top: 15px;"
													value="Go" />
											</center>
											<input type="hidden" name="submit_button" value="Submit" />
										</div>

									</form>
								</div>
								</div>
								

									<%if(!mybean.chart_data.equals("")){%>
												<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">
														&nbsp;
													</div>
												</div>
												<div class="portlet-body portlet-empty">
													<div class="tab-pane" id="">
													
													  <% if(mybean.NoChart.equals("")){%> 
								              <div id="chart1" style="height:700px;"></div> 
								              <center><b>Total: <%=mybean.chart_data_total%></b></center> 
								             <%}else{%>
								            <%=mybean.NoChart%> 
								            <%}%>
								         <%} %>
					</div>
				</div>
			</div>
      
  <%if(!mybean.StrHTML.equals("")){ %> 
	<div class="portlet box  ">
		<div class="portlet-title" style="text-align: left">
		<title>SOB Summary </title>
		</div>
		<div class="portlet-body portlet-empty">
			<div class="tab-pane" id="">
				<!-- START PORTLET BODY -->
			<center><%=mybean.StrHTML%></center>	
			</div>
		</div>
	</div>
 <%} %> 
 
<%--     <%if(!mybean.StrClosedHTML.equals("")){ %> --%>
<!-- 	<div class="portlet box  "> -->
<!-- 		<div class="portlet-title" style="text-align: left"> -->
<!-- 		<title>Closed Summary </title> -->
<!-- 		</div> -->
<!-- 		<div class="portlet-body portlet-empty"> -->
<!-- 			<div class="tab-pane" id=""> -->
<!-- 				START PORTLET BODY -->
<%-- 			<center><%=mybean.StrClosedHTML%></center>	 --%>
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<%--  <%}%>  --%>

<%--    <%if(!mybean.Strhtml.equals("")){ %>  --%>
<!-- 	<div class="portlet box  "> -->
<!-- 		<div class="portlet-title" style="text-align: left"> -->
<!-- 		</div> -->
<!-- 		<div class="portlet-body portlet-empty"> -->
<!-- 			<div class="tab-pane" id=""> -->
<!-- 				START PORTLET BODY -->
<%-- 			<center><%=mybean.Strhtml%></center>	 --%>
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<%--  <%} %>  --%>
         </div>
			</div>
		</div>

	</div>	
	</div>
	</div>
		
	<%@ include file="../Library/js.jsp"%>	
	<%@include file="../Library/admin-footer.jsp"%>
	
	<script language="JavaScript" type="text/javascript">
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document
					.getElementById("dr_principal").options);
			showHint('../insurance/mis-check.jsp?multiple=yes&brand_id='
							+ brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() { //v1.0

			var brand_id = outputSelected(document
					.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../insurance/mis-check.jsp?multiple=yes&brand_id='
							+ brand_id + '&region_id=' + region_id
							+ '&branch=yes', 'branchHint');

		}

		function PopulateModels() {
			var manufacturer_id = outputSelected(document
					.getElementById("dr_manufacturer").options);
			//			alert(manufacturer_id);
			showHint('../insurance/mis-check.jsp?carmanuf_id='
					+ manufacturer_id + '&preownedmodel=yes', 'modelHint');
		}

		function PopulateInsurEmp() { //v1.0
			var branch_id = outputSelected(document
					.getElementById("dr_branch_id").options);
			showHint('../insurance/mis-check.jsp?branch_id='
					+ branch_id + '&insurexecutives=yes', 'exeHint');
		}

		function PopulateFieldExecutives() {
		}
	</script>
	
</body>
</HTML>
