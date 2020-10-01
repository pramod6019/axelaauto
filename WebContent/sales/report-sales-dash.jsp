<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Sales_Dash" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
  
<%@include file="../Library/css.jsp"%>

  <link rel="stylesheet" type="text/css" href="../Library/jqplot/jquery.jqplot.min.css">
  <link rel="stylesheet" type="text/css" href="../Library/jqplot/syntaxhighlighter/styles/shCoreDefault.min.css">
  <link rel="stylesheet" type="text/css" href="../Library/jqplot/syntaxhighlighter/styles/shThemejqPlot.min.css">
  <!--[if lt IE 9]><script language="javascript" type="text/javascript" src="../excanvas.js"></script><![endif]-->

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
						<h1>Sales Dashboard</h1>
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
						<li><a href="../sales/report-sales-dash.jsp">Sales Dashboard</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Sales Dashboard
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<!-- <div class="form-element6"> -->
											<!-- 	Start Date<font color=red>*</font>: <input name="txt_starttime" id="txt_starttime" -->
											<!-- 		type="text" class="form-control datepicker"  -->
											<%-- 		value="<%=mybean.start_time%>" size="12" maxlength="10" /> --%>
											<!-- </div> -->

											<div class="row">
												<div class="form-element6">
													<label>End Date<font color=red>*</font>:</label>
													<input name="txt_endtime" id="txt_endtime" type="text"  maxlength="10" 
														class="form-control datepicker" value="<%=mybean.end_time%>" size="12"/>
												</div>
											</div>

											<div class="form-element4">
												<label> Brands:</label>
												<div>
													<select name="dr_principal" size="10" multiple="multiple" class="form-control multiselect-dropdown"
														id="dr_principal" onChange="PopulateBranches();PopulateRegion();">
														<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element4">
												<label> Regions:</label>
												<div id="regionHint">
													<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label> Branches:</label>
												<div id="branchHint">
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label> Model:</label>
												<div id="modelHint">
													<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
												</div>
											</div>


											<div class="form-element4">
												<label> Teams:</label>
												<div id="teamHint">
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element4">
												<label> Sales Consultant:</label>
												<div id="exeHint">
													<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
												</div>
											</div>
											
											<div class="form-element12" align="center">
												<input type="submit" name="submit_button" id="submit_button" class="btn btn-success" value="Go" />
												<input type="hidden" name="submit_button" value="Submit" />
											</div>
											
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

	<%if((mybean.so_grandtotal != "") || (mybean.invoice_grandtotal != "") || (mybean.receipt_amount != "") 
			|| (mybean.so_month != "")){%><div class="portlet box  ">
		<div class="portlet-title" style="text-align: left"></div>
		<div class="portlet-body portlet-empty">
			<div class="tab-pane" id="">
				<!-- START PORTLET BODY -->
				<div id="chart1" style="width: 1000px; height: 500px;"></div>
			</div>
		</div>
	</div>
	<%}%>
	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>
	
	<!-- Don't touch this! -->
	
	<script class="include" type="text/javascript" src="../Library/jqplot/jquery.jqplot.min.js"></script>
	<script type="text/javascript" src="../Library/jqplot/syntaxhighlighter/scripts/shCore.min.js"></script>
	<script type="text/javascript" src="../Library/jqplot/syntaxhighlighter/scripts/shBrushJScript.min.js"></script>
	<script type="text/javascript" src="../Library/jqplot/syntaxhighlighter/scripts/shBrushXml.min.js"></script>
	<!-- End Don't touch this! -->
	<!-- Additional plugins go here -->
	<script type="text/javascript" src="../Library/jqplot/plugins/jqplot.barRenderer.min.js"></script>
	<script class="include" language="javascript" type="text/javascript" src="../Library/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
	<script class="include" language="javascript" type="text/javascript" src="../Library/jqplot/plugins/jqplot.pointLabels.min.js"></script>
	
	  <!-- End additional plugins -->
	

	<script class="code" type="text/javascript">
		$(document).ready(function(){
			//alert(<%=mybean.so_grandtotal%>)
		    //var s1 = [200, 600, 700, 1000];
			 var s1 = <%=mybean.so_grandtotal%>;
			 var s2 =<%=mybean.invoice_grandtotal%>;
			  var s3 =<%=mybean.receipt_amount%>;
		    //var s2 = [460, -210, 690, 820];
		    //var s3 = [-260, -440, 320, 200];
		    // Can specify a custom tick Array.
		    // Ticks should match up one for each y value (category) in the series.
		    //var ticks = ['May', 'June', 'July', 'August'];
		    var ticks = <%=mybean.so_month%>;
		    var plot1 = $.jqplot('chart1',[s1, s2, s3], {
				// var plot1 = $.jqplot('chart1',[s1], {
		        // The "seriesDefaults" option is an options object that will
		        // be applied to all series in the chart.
				// Turns on animatino for all series in this plot.
		            animate: true,
		            // Will animate plot on calls to plot1.replot({resetAxes:true})
		            animateReplot: true,
		        seriesDefaults:{
					 pointLabels: { show: true, location: 'n', edgeTolerance: -50 },
		            renderer:$.jqplot.BarRenderer,
		            rendererOptions: {fillToZero: true}
		        },
		        // Custom labels for the series are specified with the "label"
		        // option on the series option.  Here a series option object
		        // is specified for each series.
		        series:[
		            {label:'Sales'},
		            {label:'Invoices'},
		            {label:'Receipts'}
		        ],
		        // Show the legend and put it outside the grid, but inside the
		        // plot container, shrinking the grid to accomodate the legend.
		        // A value of "outside" would not shrink the grid and allow
		        // the legend to overflow the container.
		        legend: {
		            show: true,
		            placement: 'outsideGrid',
					location: 's'
		        },
		        axes: {
		            // Use a category axis on the x axis and use our custom ticks.
		            xaxis: {
		                renderer: $.jqplot.CategoryAxisRenderer,
		                ticks: ticks
		            },
		            // Pad the y axis just a little so bars can get close to, but
		            // not touch, the grid boundaries.  1.2 is the default padding.
		            yaxis: {
		                pad: 1.05,
		                tickOptions: {formatString: '%d'}
		            }
					
		        }
		    });
		});
	  </script>

	<!-- End example scripts -->

	<script language="JavaScript" type="text/javascript">
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}
		function PopulateTeams() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?branch_id=' + branch_id + '&team=yes', 'teamHint');
		}

		function PopulateExecutives() { //v1.0
			var team_id = outputSelected(document.getElementById("dr_team").options);
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?team_id=' + team_id + '&exe_branch_id='+ branch_id +'&executives=yes', 'exeHint');
		}
		function PopulateCRMDays(){
		}
		function PopulateVariants(){
		}
	</script>
</body>
</HTML>
