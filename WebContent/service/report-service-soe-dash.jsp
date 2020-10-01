<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_Service_SOE_Dash" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</head>
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
						<h1>SOE Dashboard</h1>
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
						<li><a href="../service/report-service-soe-dash.jsp">SOE Dashboard</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
                <center><font color="red"><b> <%=mybean.msg%></b></font></center>
					
						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">SOE Dashboard
									</div>
								</div>
								
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											
												<div class="form-element3">
													<label>Start Date<font color="red">*</font>: </label>
														<input name="txt_starttime" id="txt_starttime"
																value="<%=mybean.start_time%>"
																class="form-control datepicker"
																type="text" maxlength="10" />
												</div>
												
											
											
												<div class="form-element3">
													<label>End Date<font color="red">*</font>: </label>
														<input name="txt_endtime" id="txt_endtime"
																value="<%=mybean.end_time%>"
																class="form-control datepicker"
																type="text" maxlength="10" />
												</div>
												
													<div class="form-element3">
														<div> Brands:</div>
																<select name="dr_principal" size="10"
																	multiple="multiple" class="form-control multiselect-dropdown"
																	id="dr_principal"
																	onChange="PopulateModels();PopulateBranches();PopulateRegion();">
																	<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids,
																			mybean.comp_id, request)%>
																</select>
													</div>
													
													<div class="form-element3">
														<div> Regions: </div>
														<span id="regionHint">
															<%=mybean.mischeck.PopulateRegion(mybean.brand_id,
																	mybean.region_ids, mybean.comp_id, request)%>
															</span>
													</div>
													
													<div class="row"></div>
													
													<div class="form-element3">
														<label>Zone:</label> 
														<div id="zoneHint" >
															<%=mybean.mischeck.PopulateZone(mybean.brand_id, mybean.region_ids, mybean.zone_ids, mybean.comp_id, request)%>
														</div>
													</div>
													
													<div class="form-element3">
														<div> Branches:</div> 
														<span id="branchHint">
															<%=mybean.mischeck.PopulateBranches(mybean.brand_id,
																	mybean.region_id, mybean.branch_ids, mybean.comp_id,request)%>
															</span>
													</div>

													<div class="form-element3">
														<div> Model: </div>
														<span id="modelHint">
															<%=mybean.mischeck.PopulateModels(mybean.brand_id,
																mybean.model_ids, mybean.branch_id, mybean.comp_id, request)%>
															</span>
													</div>
											
														<div class="form-element3">
															<div> Service Advisor: </div>
															<span id="exeHint">
																<%=mybean.mischeck.PopulateAdviser(mybean.advisor_ids, mybean.branch_id, 
																		mybean.comp_id, request)%>
																</span>
														</div>
														
														<div class="row"></div>
														
														<div class="form-element3">
															<div> Service Technician:</div> 
															<span id="techHint">
																<%=mybean.mischeck.PopulateTechnician(mybean.tech_ids, 
																		mybean.branch_id, mybean.comp_id,request)%>
																</span>
														</div>
														
														<div class="form-element12">
													<center>
														<input type="submit" name="submit_button"
															id="submit_button" class="btn btn-success" style="margin-top:15px;" value="Go" />
															</center>
														<input type="hidden" name="submit_button" value="Submit" />
													</div>
												</form>
											</div>
									</div>
								</div>
								
								<%if(!mybean.StrHTML.equals("")){ %> 
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: left">
										<title>SOE Summary </title>
										</div>
										<div class="portlet-body portlet-empty container-fluid">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
											<center><%=mybean.StrHTML%></center>	
											</div>
										</div>
									</div>
								 <%} %> 

									<%-- <%if(!mybean.chart_data.equals("")){%>
												<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">
														&nbsp;
													</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="tab-pane" id="">
													
													  <% if(mybean.NoChart.equals("")){%> 
								              <div id="chart1" style="height:700px;"></div> 
								              <center><b>Total: <%=mybean.chart_data_total%></b></center> 
					</div>
								             <%}else{%>
								            <%=mybean.NoChart%> 
								            <%}%>
								         <%} %> --%>
				</div>
			</div>
      
  
 
   <%--  <%if(!mybean.StrClosedHTML.equals("")){ %>
	<div class="portlet box  ">
		<div class="portlet-title" style="text-align: left">
		<title>Closed Summary </title>
		</div>
		<div class="portlet-body portlet-empty container-fluid">
			<div class="tab-pane" id="">
				<!-- START PORTLET BODY -->
			<center><%=mybean.StrClosedHTML%></center>	
			</div>
		</div>
	</div>
 <%}%> 

   <%if(!mybean.Strhtml.equals("")){ %> 
	<div class="portlet box  ">
		<div class="portlet-title" style="text-align: left">
		</div>
		<div class="portlet-body portlet-empty container-fluid">
			<div class="tab-pane" id="">
				<!-- START PORTLET BODY -->
			<center><%=mybean.Strhtml%></center>	
			</div>
		</div>
	</div>
 <%} %>  --%>
         </div>
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
<!-- End additional plugins -->
<script>

/* function convertmultiselect() {
	
	$('#dr_principal').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
        maxHeight: 200
    });
	
	$('#dr_region').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
        maxHeight: 200
    });
	
	$('#dr_model').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
        maxHeight: 200
    });
	
	$('#dr_branch').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
        maxHeight: 200
    });
	
	$('#dr_team').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
        maxHeight: 200
    });
	
	$('#dr_executive').multiselect({
        enableClickableOptGroups: true,
        includeSelectAllOption: true,
        maxHeight: 200
    });
	
}

function showHintMultySelect(url, Hint) {
$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
	$.ajax({
		url: url,
		type: 'GET',
		success: function (data){
			if(data.trim() != 'SignIn'){
				$('#'+Hint).fadeIn(500).html('' + data.trim() + '');
				convertmultiselect();
				}
			else{
				window.location.href = "../portal/";
				}
		}
	});

} */


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

<script language="JavaScript" type="text/javascript">
	function PopulateRegion() { //v1.0
// 		alert("in region");
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		//alert("111111------"+brand_id);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region=yes', 'regionHint');
	}
	
	function PopulateBranches() { //v1.0
// 		alert("in branch");
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		var zone_id = outputSelected(document.getElementById("dr_zone").options);

		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone_id=' + zone_id + '&branch=yes', 'branchHint');
	}

	function PopulateModels() { //v1.0
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
// 		alert("333------"+branch_id);
		showHint('../service/mis-check1.jsp?brand_id=' + brand_id + '&branch_id='+ branch_id
				+ '&model=yes', 'modelHint');
	}

	function PopulateAdviser() {
		var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
// 		alert("222------------"+branch_id);
		showHint('../service/mis-check1.jsp?advisor=yes&exe_branch_id=' + branch_id, 'exeHint');
	}

	function PopulateTech() {
		var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
		showHint('../service/mis-check1.jsp?technician=yes&exe_branch_id='
				+ branch_id, 'techHint');
	}
	
	function PopulateZone() { //v1.0
// 		alert("in zone");
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		//alert("111111------"+brand_id);
		showHint('../service/mis-check1.jsp?multiple=yes&brand_id=' + brand_id
				+ '&region_id=' + region_id + '&zone=yes', 'zoneHint');
	}
	
	function PopulateBranch() {
	}
	
	function PopulateCRMDays(){
	}
	
	function Populatepsfdays(){
	}
	
</script>
</body>
</html>
