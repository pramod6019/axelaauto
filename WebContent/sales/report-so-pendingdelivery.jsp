<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_SO_Pendingdelivery" scope="request"/>
<%mybean.doPost(request,response); %>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>
<style >
.fa{
font-size: 18px;
}
.fa-times{
color:red;
}
.fa-check{
color:green;
}


</style>
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
						<h1>Sales Order Pending Delivery (Booking Tracker)</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="red"><b> <%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Sales Order Pending Delivery</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">
												<!--<div class="form-element6"> -->
												<!--	<label >Delivery Status:</label> -->
												<!--	<div > -->
												<!--		<select name="dr_status_id" class="form-control" id="dr_status_id"> -->
												<%-- <%=mybean.PopulateStatus()%> --%>
												<!-- </select> -->
												<!--	</div> -->
												<!--</div> -->
												<div class="form-element6">
													<label >Order By:</label>
													<div >
														<select name="dr_order_by" class="form-control" id="dr_order_by">
															<%=mybean.PopulateOrderBy()%>
														</select>
													</div>
												</div>
												<div class="form-element6">
													<label >Filter By:</label>
													<div >
														<select name="dr_stock_allocate" class="form-control" id="dr_stock_allocate">
															<%=mybean.PopulateStockAllocate()%>
														</select>
													</div>
												</div>

												<div class="form-element3">
													<label >Brands<font color=red>*</font>: </label>
													<div >
														<select name="dr_brand" size="10" class="form-control multiselect-dropdown" multiple id="dr_brand"
															onChange="PopulateBranches();PopulateModels();PopulateRegion();">
																<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
														</select>
													</div>
												</div>

												<div class="form-element3">
													<label >Regions: </label>
													<div id="regionHint">
														<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
													</div>
												</div>

												<div class="form-element3">
													<label > Branches:</label>
													<div id="branchHint"> 
														<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
													</div>
												</div>

												<div class="form-element3">
													<label >Model:</label>
													<div id="modelHint">
														<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
													</div>
												</div>

												<div class="form-element3">
													<label >Teams:</label>
													<div id="teamHint">
														<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
													</div>
												</div>

												<div class="form-element3">
													<label >Sales Consultant:</label>
													<div  id="exeHint">
															<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
													</div>
												</div>
												<div class="form-element3">
													<label >Delivery Status: </label>
													<div >
														<select name="dr_deliverystatus_id" class="form-control multiselect-dropdown" size=10
															id="dr_deliverystatus_id" multiple="multiple">
															<%=mybean.PopulateDeliveryStatus(mybean.deliverystatus_ids, mybean.comp_id)%>
														</select>
													</div>
												</div>

												<div class="form-element3">
													<label >Fuel Type: </label>
													<div >
														<select id="dr_fuel_id" name="dr_fuel_id" size=10
															class="form-control multiselect-dropdown" multiple="multiple" id="dr_fuel_id">
																<%=mybean.PopulateFuelType(mybean.fueltype_ids, mybean.comp_id)%>
														</select>
													</div>
												</div>
												
												<div class="row"></div>
												<div class="form-element3">
													<label >Finance Status: </label>
													<div >
														<select id="dr_finstatus_id" name="dr_finstatus_id" size=10
															class="form-control multiselect-dropdown" multiple="multiple" >
																<%=mybean.PopulateFinanceStatus(mybean.comp_id)%>
														</select>
													</div>
												</div>
												<div class="row"></div>
												
												<div class="form-element12" align="center">
													<center>
														<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />
														<input type="hidden" name="submit_button" value="Submit">
														
														<input name="btn_export" id="btn_export" style="margin-left: 100px" type="submit" class="btn btn-success" value="Export" />
													</center>
												</div>


											<div>
												<%
													if (!mybean.StrHTML.equals("") && !mybean.StrHTML.contains("No Sales Order(s) found!")) {
												%>
												<center>
													<a href="veh-salesorder-list.jsp?smart=yes">[List Sales Orders]</a>
												</center>
											</div>
											<%
												}
											%>
										</form>
										<div><%=mybean.StrHTML%></div>
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

	<script type="text/javascript">

				
		//This sholuldn't be removed because it is need branch and model
		function PopulateCRMDays() {

		}
		function PopulateVariants() {

		}
		function PopulateColor() {

		}
		//This sholuldn't be removed because it is need branch and model

		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_brand").options);
			showHint( '../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}
		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_brand").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint( '../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}

		function PopulateModels() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_brand").options);
			showHint('../sales/mis-check1.jsp?brand_id=' + brand_id + '&model=yes', 'modelHint');
		}

		function PopulateTeams() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?branch_id=' + branch_id + '&team=yes', 'teamHint');
		}

		function PopulateExecutives() { //v1.0
			var team_id = outputSelected(document.getElementById("dr_team").options);
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?team_id=' + team_id
					+ '&exe_branch_id=' + branch_id + '&executives=yes', 'exeHint');
		}

		function ExeCheck() {
			var branch_id = document.getElementById("dr_branch").value;
			var team_id = outputSelected(document.getElementById("dr_team").options);
			//showHint('../sales/mis-check.jsp?multiple=yes&team_id='+team_id ,team_id,'exeHint');
			showHint('../sales/mis-check1.jsp?multiple=yes&team_id='
					+ team_id + '&exe_branch_id=' + GetReplace(branch_id), 'exeHint');
		}
	</script>
</body>
</HTML>
    
