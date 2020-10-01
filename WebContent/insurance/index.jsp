<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Index" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<style>
.btn.btn-outline.blue-oleo.active, .btn.btn-outline.blue-oleo:active,
	.btn.btn-outline.blue-oleo:active:focus, .btn.btn-outline.blue-oleo:active:hover,
	.btn.btn-outline.blue-oleo:focus, .btn.btn-outline.blue-oleo:hover {
	border-color: #94A0B2;
	color: #FFF;
	background-color: #94A0B2;
}

.icon-btn {
	top: -13px;
	left: 17px;
}

.dashboard-stat2 {
	box-shadow: 2px 2px 2px #888888;
	width: 200px;
	height: 125px;
}

@media screen  {
	#portlet {
		margin-left: 7px;
	}
	.portlet.light {
		width: 97%;
		margin-left: 23px;
	}
}
</style>

</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed" onload="allFilter();">
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
						<h1>Insurance</h1>
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
						<li><a href="../insurance/index.jsp">Insurance</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="container-fluid">

								<!-- 		=====================start card================ -->

								<div class="col-md-12 col-sm-12" style="margin-left: -18px;">
									<div class="portlet light ">
										<div class="portlet-title">
											<div class="caption caption-md">
												<i class="icon-bar-chart font-dark hide"></i>
												<span class="caption-subject font-green-steel uppercase bold">Insurance Summary</span>
												<span class="caption-helper hide">weekly stats...</span>
											</div>
											<div class="actions col-md-3 col-sm-3" style="text-align: right;margin-top: -23px;">
														<div class="btn-group btn-group-devided"
															data-toggle="buttons">
															<label class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm active" id='today'>
															<input type="radio" name="dateopt" onchange="count(this.value);getvalue(this.value);" id="option1" value="today">Today </label>
															<label class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm" id='month'>
															<input type="radio" name="dateopt" onchange="count(this.value);getvalue(this.value);" id="option2" value="month">Month</label>
															<label class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm" id='quarter'>
															<input type="radio" name="dateopt" onchange="count(this.value);getvalue(this.value);" id="option3" value="quarter">Quarter </label>
														</div>
													</div>
										</div>
										
											<div class="actions col-md-12">
												<form name="form1" class="form-horizontal" id="form1">
													<div class="form-body col-md-3 col-sm-3">
														<div class="form-group">
															<label class="control-label col-md-4 col-sm-4">Brand:</label>
															<div class="col-md-6">
																<span id="multiprincipal" class="col-md-8 ">
																	<select name="dr_principal" size="10"
																		class="form-control multiselect-dropdown" multiple="multiple" style="padding:10px"
																		id="dr_principal"
																		onChange="PopulateBranches();PopulateRegion();">
																		<%=mybean.misCheck.PopulatePrincipal(mybean.brand_id, mybean.brand_ids, mybean.comp_id, request)%>
																	</select>
																</span>
															</div>
														</div>
													</div>

													<div class="form-body col-md-3 col-sm-3">
														<div class="form-group">
															<label class="control-label col-md-4 col-sm-4">Region:</label>
															<div class="col-md-6">
																<span id="regionHint" class="col-md-8">
																	<%=mybean.misCheck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
																</span>
															</div>
														</div>
													</div>
													<div class="form-body col-md-3 col-sm-3">
														<div class="form-group">
															<label class="control-label col-md-4 col-sm-4">Branch:</label>
															<div class="col-md-6">
																<span id="branchHint" class="col-md-8 "> 
																	<%=mybean.misCheck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
																</span>
															</div>
														</div>
													</div>
											</div>
											</form>
											<input type='text' value='today' id='cardvalue' name='cardvalue' hidden />
											<div class="portlet-body" id="portlet" >
												<div id="dashbox" style="margin-left: -29px;" class="row">
													<center>
														<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
															<a>
																<div class="dashboard-stat2" id="insurenquiry" onclick="checkcall(this.id);">
																	<div class="display" id='hint'>
																		<div class="number col-md-7 col-xs-7">
																			<h3 class="font-green-sharp">
																				<span data-counter="counterup" id='insurenquiry_count'></span>
																			</h3>
																			<small>Insurance Enquiry</small>
																		</div>
																		<div class="col-md-5 col-xs-5">
																			<i class='btn-sm bg-green-sharp icon-btn col-md-5'>
																				<i class="fa fa-list-alt" style="font-size: xx-large; margin-top: 5px;"></i>
																			</i>
																		</div>
																	</div>
																	<div class="progress-info">
																		<div class="progress">
																			<span class="progress-bar progress-bar-success green-sharp" id='insurenquiry_count_bar'> </span>
																		</div>
																		<div class="status">
																			<div class="status-title">Growth</div>
																			<div class="status-number" id='insurenquiry_count_label'></div>
																		</div>
																	</div>
																</div>
															</a>
														</div>
														<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
															<a>
																<div class="dashboard-stat2" id="fieldappt" onclick="checkcall(this.id);">
																	<div class="display" id='hint'>
																		<div class="number col-md-7 col-xs-7">
																			<h3 class="font-green-sharp">
																				<span data-counter="counterup" id='fieldappt_count'></span>
																			</h3>
																			<small>Field Appointment</small>
																		</div>
																		<div class="col-md-5 col-xs-5">
																			<i class='btn-sm bg-green-sharp icon-btn col-md-5'>
																				<i class="fa fa-cart-plus" style="font-size: xx-large; margin-top: 5px;"></i>
																			</i>
																		</div>
																	</div>
																	<div class="progress-info">
																		<div class="progress">
																			<span class="progress-bar progress-bar-success green-sharp" id='fieldappt_count_bar'></span>
																		</div>
																		<div class="status">
																			<div class="status-title">Growth</div>
																			<div class="status-number" id='fieldappt_count_label'></div>
																		</div>
																	</div>
																</div>
															</a>
														</div>

														<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
															<a>
																<div class="dashboard-stat2" id="policy" onclick="checkcall(this.id);">
																	<div class="display" id='hint'>
																		<div class="number col-md-7 col-xs-7">
																			<h3 class="font-green-sharp">
																				<span data-counter="counterup" id='policy_count'></span>
																			</h3>
																			<small>Policy</small>
																		</div>
																		<div class="col-md-5 col-xs-5">
																			<i class='btn-sm bg-green-sharp icon-btn col-md-5'>
																				<i class="fa fa-bar-chart" style="font-size: xx-large; margin-top: 5px;"></i>
																			</i>
																		</div>
																	</div>
																	<div class="progress-info">
																		<div class="progress">
																			<span class="progress-bar progress-bar-success green-sharp" id='policy_count_bar'> </span>
																		</div>
																		<div class="status">
																			<div class="status-title">Growth</div>
																			<div class="status-number" id='policy_count_label'></div>
																		</div>
																	</div>
																</div>
															</a>
														</div>

														<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
															<a>
																<div class="dashboard-stat2" id="premiumamt" onclick="checkcall(this.id);">
																	<div class="display" id='hint'>
																		<div class="number col-md-7 col-xs-7">
																			<h3 class="font-green-sharp">
																				<span data-counter="counterup" id='premiumamt_count'></span>
																			</h3>
																			<small>Premium Amount</small>
																		</div>
																		<div class="col-md-5 col-xs-5">
																			<i class='btn-sm bg-green-sharp icon-btn col-md-5'>
																				<i class="fa fa-ban" style="font-size: xx-large; margin-top: 5px;"></i>
																			</i>
																		</div>
																	</div>
																	<div class="progress-info">
																		<div class="progress">
																			<span class="progress-bar progress-bar-success green-sharp" id='premiumamt_count_bar'> </span>
																		</div>
																		<div class="status">
																			<div class="status-title">Growth</div>
																			<div class="status-number" id='premiumamt_count_label'></div>
																		</div>
																	</div>
																</div>
															</a>
														</div>
													</center>
												</div>
											</div>
										</div>
										</div>
										</div>
										<!-- 		=========================================end card========================================= -->
										<div class="col-md-12 col-sm-12">
											<div class="col-md-4 col-sm-6 col-xs-12" style="padding: 5px">
												<div class="portlet box">
													<div class="portlet-title" style="text-align: center">
														<div class="caption" style="float: none">&nbsp;</div>
													</div>
													<div class="portlet-body portlet-empty">
														<div class="tab-pane" id="">
															<div id="insurancechartdiv" style="height: 300px;"></div>
														</div>
													</div>
												</div>
											</div>
											<div class=" col-md-4 col-sm-6 col-xs-12"
												style="padding: 5px">
												<div class="portlet box">
													<div class="portlet-title" style="text-align: center">
														<div class="caption" style="float: none">Insurance Follow-up</div>
													</div>
													<div class="portlet-body portlet-empty">
														<div class="tab-pane" id="">
															<center>
																<%=mybean.InsuranceFollowup()%>
															</center>

														</div>
													</div>
												</div>
											</div>
											<div class=" col-md-4 col-xs-12" style="padding: 5px">
												<div class="portlet box">
													<div class="portlet-title" style="text-align: center">
														<div class="caption" style="float: none">Insurance Reports</div>
													</div>
													<div class="portlet-body portlet-empty">
														<div class="tab-pane" id="">
															<center>
																<%=mybean.ListReports()%>
															</center>

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
		</div>
		</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../Library/amcharts/amcharts.js"></script>
	<script type="text/javascript" src="../Library/amcharts/funnel.js"></script>
	<script type="text/javascript" src="../Library/amcharts/serial.js"></script>
	<script>

	function LoadAmChart(jsonstring, hint){
			var insurancechart;
			var chart;
			switch (hint){
					
			case 'insurancechartdiv':		
				
				var insurancechartData =  JSON.parse(jsonstring);
				
			// SERIAL CHART FOR FOLLOWUP
				insurancechart = new AmCharts.AmSerialChart();
				insurancechart.dataProvider = insurancechartData;
				insurancechart.categoryField = "level";
				// this single line makes the chart a bar chart,
				// try to set it to false - your bars will turn to columns
				insurancechart.rotate = true;
				// the following two lines makes chart 3D
				insurancechart.depth3D = 20;
				insurancechart.angle = 30;
				insurancechart.startDuration = 1;
				insurancechart.startEffect = "easeOutSine";
				insurancechart.marginsUpdated = false;
				insurancechart.autoMarginOffset = 0;
				insurancechart.marginLeft = 0;
				insurancechart.marginRight = 0;
		
				// AXES
				// Category
				var categoryAxis6 = insurancechart.categoryAxis;
				categoryAxis6.gridPosition = "start";
				categoryAxis6.axisColor = "#DADADA";
				categoryAxis6.fillAlpha = 1;
				categoryAxis6.gridAlpha = 1;
				categoryAxis6.gridColor = "#DADADA";
				categoryAxis6.fillColor = "#FAFAFA";
				// value
				var valueAxis6 = new AmCharts.ValueAxis();
				valueAxis6.axisColor = "#DADADA";
				valueAxis6.title = "Insurance Follow-up Escalation";
				valueAxis6.gridAlpha = 1;
				valueAxis6.gridColor = "#DADADA";
				insurancechart.addValueAxis(valueAxis6);
		
				// GRAPH
				var insurancegraph = new AmCharts.AmGraph();
				insurancegraph.title = "Insurance";
				insurancegraph.valueField = "value";
				insurancegraph.urlField = "url";
				insurancegraph.urlTarget = "_target";
				insurancegraph.type = "column";
				insurancegraph.balloonText = "[[category]]:[[value]]";
				insurancegraph.lineAlpha = 0;
				insurancegraph.colorField = "color";
				insurancegraph.fillAlphas = 1;
				insurancegraph.labelText = "[[value]]";
				insurancegraph.labelPosition = "top";
				insurancechart.addGraph(insurancegraph);
		
				insurancechart.creditsPosition = "top-right";
		
				// WRITE
				insurancechart.write("insurancechartdiv");	
				break;
			
			}
		}
	</script>

	<script type="text/javascript">
		
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			showHint('../insurance/mis-check.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() { //v1.0
			
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../insurance/mis-check.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
			
		}
		
		var carddata;
		var bardata;
		var periodType = "today";

		function count(type) {
			
			periodType = type;
			$('#cardvalue').val(type);

			switch(type){
			case 'today':
				count_type_loop(carddata[0], "insurenquiry_count");
				count_type_loop(carddata[3], "fieldappt_count");
				count_type_loop(carddata[6], "policy_count");
				count_type_loop(carddata[9], "premiumamt_count");

				bar_type_loop(bardata[0],carddata[0], "insurenquiry_count");
				bar_type_loop(bardata[3],carddata[3], "fieldappt_count");
				bar_type_loop(bardata[6],carddata[6], "policy_count");
				bar_type_loop(bardata[9],carddata[9], "premiumamt_count");
				
				break;
			
			case 'month':
				count_type_loop(carddata[1], "insurenquiry_count");
				count_type_loop(carddata[4], "fieldappt_count");
				count_type_loop(carddata[7], "policy_count");
				count_type_loop(carddata[10], "premiumamt_count");
				
				bar_type_loop(bardata[1],carddata[1], "insurenquiry_count");
				bar_type_loop(bardata[4],carddata[4], "fieldappt_count");
				bar_type_loop(bardata[7],carddata[7], "policy_count");
				bar_type_loop(bardata[10],carddata[10], "premiumamt_count");
				
				break;
			
			case 'quarter':
				count_type_loop(carddata[2],"insurenquiry_count");
				count_type_loop(carddata[5], "fieldappt_count");
				count_type_loop(carddata[8], "policy_count");
				count_type_loop(carddata[11], "premiumamt_count");
				
				bar_type_loop(bardata[2],carddata[2], "insurenquiry_count");
				bar_type_loop(bardata[5],carddata[5], "fieldappt_count");
				bar_type_loop(bardata[8],carddata[8], "policy_count");
				bar_type_loop(bardata[11],carddata[11], "premiumamt_count");

				break;
			}
		}

		function count_type_loop(mybean_value, Hint){
			var t= 100;
			if(mybean_value>999999){
				mybean_value = eval(mybean_value/100000).toFixed(3);
				for(i = 0, j = eval(mybean_value/10), t=100 ; Math.round(i) <= mybean_value ; i=i+j, t=t+100){
					setTimeout('document.getElementById("' + Hint + '").innerHTML=' + Math.round(i) +'+"L"', t) ;
					if(mybean_value==0 || mybean_value=="" || mybean_value==null){break;}
				}
				setTimeout('document.getElementById("' + Hint + '").innerHTML=' + Math.round(i) +'+"L"', t) ;
			}else{
				for(i = 0, j = eval(mybean_value/10), t=100 ; Math.round(i) <= mybean_value ; i=i+j, t=t+100){
					setTimeout('document.getElementById("' + Hint + '").innerHTML=' + Math.round(i), t) ;
					if(mybean_value==0 || mybean_value=="" || mybean_value==null){break;}
				}
			}
		}

		function bar_type_loop(mybean_old_value, mybean_new_value, Hint){
			var t = 100;
			if(mybean_old_value > 0){
				var per = Math.round(eval((mybean_new_value * 100)/mybean_old_value));
					for(i = 0, j = eval(per/10), t=100 ; Math.round(i) <= per ; i=i+j, t=t+100){
						setTimeout('document.getElementById("' + Hint + '_label").innerHTML=' + Math.round(i)+'+"%";', t) ;
						setTimeout('document.getElementById("' + Hint + '_bar").style.width="' + Math.round(i)+'%";', t) ;
						if(per==0 || per=="" || per==null){break;}
					}
			}else {
				if(mybean_old_value == 0 && mybean_new_value > 0){
					var per = Math.round(100);
					for(i = 0, j = eval(per/10), t=100 ; Math.round(i) <= per ; i=i+j, t=t+100){
						setTimeout('document.getElementById("' + Hint + '_label").innerHTML=' + Math.round(i)+'+"%";', t) ;
						setTimeout('document.getElementById("' + Hint + '_bar").style.width="' + Math.round(i)+'%";', t) ;
					}
				}else{
					setTimeout('document.getElementById("' + Hint + '_label").innerHTML="0%";', t) ;
					setTimeout('document.getElementById("' + Hint + '_bar").style.width="0%";', t) ;
				}
			}
		}

//			end populate cards

		function allFilter(){
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			var dr_branch_id = outputSelected(document.getElementById("dr_branch_id").options);
		
			var url="../insurance/index-check.jsp?refreshAll=yes&brand_id=" + brand_id + "&region_id="+region_id+"&dr_branch_id="+dr_branch_id;
		
			// for Am-Chart
			setTimeout('showHintChart("'+url+'&insurfollowupescstatus=yes", "insurancechartdiv");',100);
			
			// for Cards
			setTimeout('showHintCards("'+url+'&cards=yes", "insurenquiry_count,fieldappt_count,policy_count,premiumamt_count");',80);
			
		}
		
		function showHintChart(url, Hint) {
			$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
				$.ajax({
					url: url,
					type: 'GET',
					success: function (data){
						if(data.trim() != 'SignIn'){
						LoadAmChart(data.trim(), Hint);
						} else{
							window.location.href = "../portal/";
						}
					}
				});
		}
		
		function showHintCards(url, Hint) {
			var card_ids = Hint.split(',');
			
			$('#'+card_ids[0]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[1]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[2]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[3]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			
				$.ajax({
					url: url,
					type: 'GET',
					success: function (data){
						if(data.trim() != 'SignIn'){
							carddata=data.trim().substring(0,data.trim().indexOf(":")).split(",");
							bardata=data.trim().substring(data.trim().indexOf(":")+1,data.trim().length).split(",");
							count(periodType);
						} else {
							window.location.href = "../portal/";
						}
					}
				});
		}

	<!-- 	//should not be deleted written for multiple ajax calls -->
	//
	function PopulateInsurEmp(){
	}
	function PopulateFieldExecutives(){
	}
	</script>

	<script type="text/javascript">
	var dts = 'today';
	
	//to get the Date selected [Today , Month, Quarter]
	function getvalue(getdt){
		dts = getdt;
	}
	
	function checkcall(opt){
		var url;
		
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		var dr_branch_id = outputSelected(document.getElementById("dr_branch_id").options);
		url = "../insurance/index-check.jsp?filter=yes&opt="+opt;
		url = url+"&brand_id=" + brand_id + "&region_id="+region_id+"&dr_branch_id="+dr_branch_id+"&period="+dts;
		window.open(url,'_blank');
	}
	
	</script>
</body>
</html>
