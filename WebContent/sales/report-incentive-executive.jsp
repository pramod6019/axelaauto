<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Report_Incentive_Executive" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

<style>

.left-right-border{
border-right: 1px solid #ff0000;
border-left: 1px solid #ff0000;
}
.right-border{
border-right: 1px solid #ff0000;
}

.dashboard-stat2 {
	box-shadow: 2px 2px 2px #888888;
}

 .dashboard-stat2 .display .number h3 {
        margin: 0 0 2px 0;
        padding: 0;
        font-size: 25px;
        font-weight: 400; }
        
</style>

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
						<h1>Sales Executive Incentive</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY ----->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../sales/report-executives-incentive.jsp">Sales Executive Incentive</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->
           			<center><font color="red"><b> <%=mybean.msg%></b></font></center>
						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Executive Incentive
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">
											
<!-- 											<div class="form-element11 form-element"> -->
											<div class="form-element2">
												<label>Year<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_year" class="form-control" id="dr_year" >
														<%=mybean.PopulateYear(mybean.year)%>
													</select>
												</div>
											</div>
											
											<div class="form-element2">
												<label>Month<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_month" class="form-control" id="dr_month" >
														<%=mybean.PopulateMonth(mybean.month)%>
													</select>
												</div>
											</div>
													
											<div class="form-element2">
												 <label>Brands:</label>
												<div>
													<select name="dr_principal" size="10" class="form-control multiselect-dropdown" multiple="multiple" id="dr_principal"
														onChange="PopulateModels();PopulateBranches();PopulateRegion();">
														<%=mybean.mischeck.PopulatePrincipal( mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

											<div class="form-element2">
												<label>Regions:</label> 
												<div id="regionHint" >
													<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element2">
												<label>Branches:</label> 
												<div id="branchHint" > 
													<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>


											<div class="form-element2">
												<label>Teams:</label>
												<div id="teamHint" >
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>
												
											<div class="row"></div>		
											<div class="form-element2">
												<label>Sales Consultant<font color=red>*</font>:</label> 
												<div id="exeHint" > 
												<%=mybean.PopulateEmp(mybean.team_id, mybean.exe_id, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element12">
												<center>
													<input type="submit" name="submit_button"
														id="submit_button" class="btn btn-success" value="Go" /> <input
														type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>

										</form>
									</div>
								</div>
							</div>
							
							<%
							if(mybean.go.equals("Go")){	
								%>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.emp_name %> </div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
							<!-- 		=====================start card================ -->
<!-- 							<div class="col-md-12 col-sm-12"> -->

									<div class="portlet light ">
										<div class="portlet-body" id="portlet">
											<div id="dashbox" style="margin-left: -29px;" class="row">
											
											<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12"></div>
											
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
<!-- 													<a> -->
														<div class="dashboard-stat2" style="padding: 15px 15px 1px 15px;" id="target">
															<div class="display" id='hint'>
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-green-sharp">
																		<span data-counter="counterup" id='target_count'><%=mybean.sotarget %></span>
																	</h3>
																	<small>SO Target</small>
																	<h3 class="font-green-sharp">
																		<span data-counter="counterup" id='target_count'><%=mybean.somintarget %></span>
																	</h3>
																	<small>SO Min. Target</small>
																</div>
																<div class="col-md-5 col-xs-5">
																	<i class='btn-sm bg-green-sharp icon-btn col-md-5'>
																		<i class="fa fa-list-alt" style="font-size: xx-large;"></i> 
																	</i>
																</div>
															</div>
														</div>
<!-- 														</a> -->
													</div>
													
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<div class="dashboard-stat2" id="achieved" >
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-red-haze">
																		<span data-counter="counterup" id='achieved_count'><%=mybean.soachieved %></span>
																	</h3>
																	<small>Retail Achieved</small>
																</div>
																<div class="col-md-5">
																	<i class='btn-sm bg-red-haze icon-btn '>
																	<img src="../admin-ifx/test-drive.png" style="width: 51px;margin-top:-8px" alt="achieved"></img>
																	</i>
																</div>
															</div>
														</div>
													</div>
													
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<div class="dashboard-stat2" id="achieved" >
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h4 class="font-purple-soft">
																		<span data-counter="counterup" id='achieved_count'><%=mybean.monthName %></span>
																	</h4>
																	<small><%=mybean.year %></small>
																</div>
																<div class="col-md-2">
																	<i class='btn-sm icon-btn col-md-7' style="background-color: #a495c3">
																		<i class="fa fa-bar-chart" style="font-size: xx-large;margin-top: 5px;"></i>
																	</i>
																</div>
															</div>
														</div>
													</div>
													<br></br><br></br><br></br><br></br><br></br>
													
<!-- 					For Finance, Insurance								-------------- -->
														<div class="col-lg-1 col-md-1 col-sm-6 col-xs-12"></div>
														<div class="row">
														
														<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<div class="dashboard-stat2" id="achieved" >
															<div class="display">
																<div class="number col-md-9 col-xs-9">
																	<h3 class="font-red-haze">
																		<span data-counter="counterup" id='achieved_count'><%=mybean.soinsurcount %></span>
																	</h3>
																	<h4 class="font-red-haze">
																		<span data-counter="counterup" id='achieved_count'><%=mybean.cardinsuramount %></span>
																	</h4>
																	<small>Insurance</small>
																</div>
																<div class="col-md-3">
																	<i class='btn-sm bg-red-haze icon-btn '>
																	<img src="../admin-ifx/test-drive.png" style="width: 51px;margin-top:-8px" alt="achieved"></img>
																	</i>
																</div>
															</div>
															
														</div>
													</div>
														
														<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<!-- 													<a> -->
														<div class="dashboard-stat2" id="target">
															<div class="display" id='hint'>
																<div class="number col-md-9 col-xs-9">
																	<h3 class="font-green-sharp">
																		<span data-counter="counterup" id='target_count'><%=mybean.sofinancecount %></span>
																	</h3>
																	<h4 class="font-green-sharp">
																	<span data-counter="counterup" id='target_count'><%=mybean.cardfinanceamount %></span>
																	</h4>
																	<small>Finance</small>
																</div>
																<div class="col-md-3">
																	<i class='btn-sm bg-green-sharp icon-btn col-md-5'>
																		<i class="fa fa-list-alt" style="font-size: xx-large;margin-top: 5px;"></i> 
																	</i>
																</div>
															</div>
														</div>
<!-- 														</a> -->
													</div>
													
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<div class="dashboard-stat2" id="achieved" >
															<div class="display">
																<div class="number col-md-9 col-xs-9">
																	<h3 class="font-blue-sharp">
																		<span data-counter="counterup" id='achieved_count'><%=mybean.somgacount %></span>
																	</h3>
																	<h4 class="font-blue-sharp">
																	<span data-counter="counterup" id='achieved_count'><%=mybean.cardmgaamount %></span>
																</h4>
																	<small>Accessories</small>
																</div>
																<div class="col-md-3">
																	<i class='btn-sm bg-blue-sharp icon-btn col-md-5'>
																		<i class="fa fa-cart-plus" style="font-size: xx-large;margin-top: 5px;"></i>
																	</i>
																</div>
															</div>
														</div>
													</div>
													
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
<!-- 													<a> -->
														<div class="dashboard-stat2" id="target">
															<div class="display" id='hint'>
																<div class="number col-md-9 col-xs-9">
																	<h3 class="font-purple-soft">
																		<span data-counter="counterup" id='target_count'><%=mybean.soewcount %></span>
																	</h3>
																	<h4 class="font-purple-soft">
																	<span data-counter="counterup" id='target_count'><%=mybean.cardewamount %></span>
																</h4>
																	<small>Ext. Warranty</small>
																</div>
																<div class="col-md-3">
																	<i class='btn-sm icon-btn col-md-7' style="background-color: #a495c3">
																		<i class="fa fa-bar-chart" style="font-size: xx-large;margin-top: 5px;"></i>
																	</i>
																</div>
															</div>
															
														</div>
<!-- 														</a> -->
													</div>
													
													<div class="col-lg-2 col-md-2 col-sm-6 col-xs-12">
														<div class="dashboard-stat2" id="achieved" >
															<div class="display">
																<div class="number col-md-9 col-xs-9">
																	<h3 class="font-blue-soft">
																		<span data-counter="counterup" id='achieved_count'><%=mybean.soexchangecount %></span>
																	</h3>
																	<h4 class="font-blue-soft">
																	<span data-counter="counterup" id='achieved_count'><%=mybean.cardexchangeamount %></span>
																</h4>
																	<small>Exchange</small>
																</div>
																<div class="col-md-3">
																	<i class='btn-sm icon-btn col-md-7' style="background-color:#f5b063 ">
																<img src="../admin-ifx/delivered1.png" style="width: 51px;margin-top: -4px;margin-top: -4px;height: 35px;" alt="Delivered"></img>
																</i>
																</div>
															</div>
														</div>
													</div>
													
													</div>
													


<!-- --------------------- -->
													
													
											</div>
										</div>
									</div>
<!-- 								</div> -->
							
							<!-- 		=====================end card================ -->
							<%
							if(mybean.sotarget != 0) {
							%>
							<center>
								<b>Incentive By Variant</b>
								<%=mybean.StrHTML1%>
								<br />
								
								<b>Incentive By Slab</b>
								<table class="table table-bordered table-hover table-responsive" data-filter="#filter">
			<thead>
			<tr>
				<th data-hide="phone">#</th>
				<th>Distribution By</th>
				<th>Achieved</th>
				<th>Band</th>
				<th>Incentive</th>
				<th>Amount</th>
			</tr>
			</thead>
			<tbody>

				<tr>
					<td valign=top align=center>1</td>
					<td valign=top align=left>SO Retail </td>
					<td valign=top align=right><%=mybean.soachieved %></td>
					<td valign=top align=center><%=mybean.soband %></td>
					<td valign=top align=right><%=mybean.sorateamount %></td>
					<td valign=top align=right><%=mybean.slabachsoamount %></td>
				</tr>
				
				<tr>
					<td valign=top align=center>2</td>
					<td valign=top align=left>Insurance </td>
					<td valign=top align=right><%=mybean.soinsurcount %></td>
					<td valign=top align=center><%=mybean.insurband %></td>
					<td valign=top align=right><%=mybean.soinsuramount %></td>
					<td valign=top align=right><%=mybean.slabachinsuramount %></td>
				</tr>
				
				<tr>
					<td valign=top align=center>3</td>
					<td valign=top align=left>Finance </td>
					<td valign=top align=right><%=mybean.sofinancecount %></td>
					<td valign=top align=center><%=mybean.financeband %></td>
					<td valign=top align=right><%=mybean.sofinanceamount %></td>
					<td valign=top align=right><%=mybean.slabachfinanceamount %></td>
				</tr>
				
				<tr>
					<td valign=top align=center>4</td>
					<td valign=top align=left>Accessories </td>
					<td valign=top align=right><%=mybean.somgacount %></td>
					<td valign=top align=center><%=mybean.mgaband %></td>
					<td valign=top align=right><%=mybean.somgaamount %></td>
					<td valign=top align=right><%=mybean.slabachmgaamount %></td>
				</tr>
				
				<tr>
					<td valign=top align=center>5</td>
					<td valign=top align=left>Ext. Warranty </td>
					<td valign=top align=right><%=mybean.soewcount %></td>
					<td valign=top align=center><%=mybean.ewband %></td>
					<td valign=top align=right><%=mybean.soewamount %></td>
					<td valign=top align=right><%=mybean.slabachewamount %></td>
				</tr>
				
				<tr>
					<td valign=top align=center>6</td>
					<td valign=top align=left>Exchange </td>
					<td valign=top align=right><%=mybean.soexchangecount %></td>
					<td valign=top align=center><%=mybean.exchangeband %></td>
					<td valign=top align=right><%=mybean.soexchangeamount %></td>
					<td valign=top align=right><%=mybean.slabachexchangeamount %></td>
				</tr>
				
			<tr><td valign=top align=right colspan=5><b>Total:</b></td>
			<td valign=top align=right><b><%=mybean.slabtotal%>
			<b></td>


			</tbody>
			</table>
								
								
								
								<br />
								
								<b>Overall Incentive</b>
								<%=mybean.StrHTML4%><br />
								
								<b>Incentive By Insurance</b>
								<%=mybean.StrHTML5%>
								<br />
								
								<b>Incentive By Finance</b>
								<%=mybean.StrHTML6%>
								<br />
								
								<b>Incentive By Accessories</b>
								<%=mybean.StrHTML7%>
								<br />
								
								<b>Incentive By Ext. Warranty</b>
								<%=mybean.StrHTML8%>
								<br />
								
								<b>Incentive By Exchange</b>
								<%=mybean.StrHTML9%>
								
								<%if(mybean.grandTotal != 0.00){%>
								<table class="table table-bordered table-hover table-responsive" data-filter="#filter">
									<tbody>
										<tr>
											<td valign=top align=right><b style="color:#ffffff"><%=mybean.incentivetotal %></b><b style="margin-inline-start: 260px;">Grand Total:</b></td>
											<td valign=top align=right><b><%=mybean.IndDecimalFormat(mybean.deci.format(mybean.grandTotal)) %></b></td>
										</tr>
									</tbody>
								</table>
								<%} %>
								
								<br /></center></div></div></div>
								
								
								<center><div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Incentive Program </div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
								
								<center><b>Slab-Wise Incentive</b></center>
								<%=mybean.StrHTML10%>
								<br />
								
								<center><b>Overall-Wise Incentive</b></center>
								<%=mybean.StrHTML11%>
								<br />
								
								<center><b>Insurance-Wise Incentive</b></center>
								<%=mybean.StrHTML12%>
								<br />
								
								<center><b>Finance-Wise Incentive</b></center>
								<%=mybean.StrHTML13%>
								<br />
								
								<center><b>Accessories-Wise Incentive</b></center>
								<%=mybean.StrHTML14%>
								<br />
								
								<center><b>Ext. Warranty-Wise Incentive</b></center>
								<%=mybean.StrHTML15%>
								<br />
								
								<center><b>Exchange-Wise Incentive</b></center>
								<%=mybean.StrHTML16%>
								<br />
								
							</center>
							
							<%
								}else{
							%>
							<div>
							<center>
							  <font color=red><br><br><br><b>No Details Found!</b></font>
							  </center>
							  </div>
							<%
								}
							%>
							</div>
							</div>
							</div>
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
	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>


	<script language="JavaScript" type="text/javascript">
		
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
	
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
			
		}
	
		function PopulateModels() { //v1.0
			var brand_id = outputSelected(document .getElementById("dr_principal").options);
			showHint('../sales/mis-check1.jsp?brand_id=' + brand_id + '&model=yes', 'modelHint');
		}
	
		function PopulateTeams() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint( '../sales/mis-check1.jsp?branch_id=' + branch_id + '&team=yes', 'teamHint');
		}
	
		function PopulateExecutives() { //v1.0
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			var team_id = outputSelected(document.getElementById("dr_team").options);
			showHint('../sales/mis-check1.jsp?exe_branch_id=' + branch_id + '&team_id=' + team_id + '&salesexecutive=yes', 'exeHint');
		}
		
		function returnpreownedcheck(){
			 var preownedmodel=document.getElementById('chk_preowned_model').checked;
		    	if(preownedmodel){
		     		$("#modelHint").find('button').prop("disabled",true);
		     		$('#dr_totalby').val('6');
		    	} else{
		    		$("#modelHint").find('button').prop("disabled",false);
		    	}
		    }
		
             
		//This sholuldn't be removed because it is need branch and model
		function PopulateCRMDays() {

		}
		function PopulateVariants() {

		}
		function PopulateColor() {

		}
	</script>
	<!-- // -->
</body>
</HTML>
